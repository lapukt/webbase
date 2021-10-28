package com.yss.proxy.servlet;

import com.yss.proxy.servlet.utils.ProfileConfigUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: WasProxyServlet
 * @Description: 反向代理服务 Servlet
 * @author: zhengjing
 * @createdate: 2021-03-04 15:18
 */
public class WasProxyServlet extends javax.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;
    private String head = null;
    private String mapUrl = ProfileConfigUtil.SSO_GETWAY_URL;

    Logger logger = Logger.getLogger(getClass());

    HttpClientBuilder builder = HttpClientBuilder.create();

    final int buffsize = 8192;

    public WasProxyServlet() {

        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(50);
        clientConnectionManager.setDefaultMaxPerRoute(30);
        clientConnectionManager.closeIdleConnections(5, TimeUnit.MINUTES);
        builder.setConnectionManager(clientConnectionManager);
        builder.setRedirectStrategy(null);

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        head = config.getServletContext().getContextPath() + this.getInitParameter("head");
        System.out.println("root path==>:" + head);
    }

    /**
     * 处理get请求
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder stringBuilder = new StringBuilder(req.getRequestURI());
        String query = req.getQueryString();
        if (query != null) {
            stringBuilder.append("?" + query);
        }
        if (mapUrl != null) {
            final String uri = stringBuilder.toString();
            logger.info(req.getRemoteHost() + "get" + uri);
            long time1 = System.currentTimeMillis();

            try {
                doProx(req, resp, mapUrl, uri, true);
            } catch (IOException e) {
                logger.warn("请求异常", e);
                throw e;
            }
            long time2 = System.currentTimeMillis();
            logger.info("prox" + uri + "耗时" + (time2 - time1));
        }
    }

    /**
     * 处理post请求
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder stringBuilder = new StringBuilder(req.getRequestURI());
        String query = req.getQueryString();
        if (query != null) {
            stringBuilder.append("?" + query);
        }
        if (mapUrl != null) {
            try {
                doProx(req, resp, mapUrl, stringBuilder.toString(), false);
            } catch (IOException e) {
                logger.warn("请求异常", e);
                throw e;
            }
        }

    }

    /**
     * 代理处理
     *
     * @param req
     * @param resp
     * @param host
     * @param uri
     * @param isget
     * @throws IOException
     */
    private void doProx(HttpServletRequest req, HttpServletResponse resp, String host, String uri,
                        boolean isget) throws IOException {
        HttpClient client = builder.build();
        HttpClientContext clientContext = builderClientContext();
        HttpRequestBase request = null;
        if (head != null && uri.startsWith(head)) {
            if (head.equals(uri)) {
                uri = "";
            } else {
                uri = uri.substring(head.length());
            }
        }
        if (isget) {
            request = new HttpGet(host + uri);
        } else {
            HttpPost httpPost = new HttpPost(host + uri);
            String contentType = req.getContentType();
            if (contentType == null || "".equals(req.getContentType())) {
                contentType = "application/x-www-form-urlencoded";
            }
            httpPost.setEntity(new InputStreamEntity(req.getInputStream(), ContentType.create(contentType)));
            request = httpPost;
        }

        addHeader(req, request);


        HttpResponse httpResponse = client.execute(request, clientContext);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        Header[] headers = httpResponse.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            resp.addHeader(headers[i].getName(), headers[i].getValue());
        }
        resp.setStatus(statusCode);

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            Header contentType = entity.getContentType();
            try(InputStream inputStream = entity.getContent(); OutputStream outputStream =  resp.getOutputStream()) {
                int b = -1;
                byte[] bs = new byte[buffsize];
                while ((b = inputStream.read(bs)) != -1) {
                    outputStream.write(bs, 0, b);
                }
                outputStream.flush();
            }catch (Exception e){
                logger.warn("请求异常", e);
            }

        }

        request.releaseConnection();
    }

    /**
     * 添加头消息
     *
     * @param request
     * @param httpRequestBase
     */
    private void addHeader(HttpServletRequest request, HttpRequestBase httpRequestBase) {
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            if (name.equalsIgnoreCase("Content-Length")) {
                continue;
            }
            String header = request.getHeader(name);
            logger.info("addHeader:" + name);

            httpRequestBase.addHeader(name, header);
        }
    }

    /**
     * 构建clientcontext
     *
     * @return
     */
    private HttpClientContext builderClientContext() {

        CookieStore cookieStore = new BasicCookieStore();
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);


        RequestConfig config = context.getRequestConfig();
        context.setRequestConfig(
                RequestConfig.custom()
                        .setSocketTimeout(config.getSocketTimeout()).setConnectTimeout(config.getConnectTimeout())
                        .setConnectionRequestTimeout(config.getConnectionRequestTimeout()).
                        setRedirectsEnabled(false).build());
        return context;

    }

}
