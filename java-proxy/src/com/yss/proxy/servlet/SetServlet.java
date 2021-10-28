package com.yss.proxy.servlet;

import com.yss.proxy.servlet.utils.ProfileConfigUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @ClassName: SetServlet
 * @Description: Index Servlet
 * @author: zhengjing
 * @createdate: 2021-02-24 13:20
 */
public class SetServlet extends HttpServlet {

    /**
     * 加载文件路径
     */
    private final static String filePath = ProfileConfigUtil.CONFIG_PATH;


    /**
     * @param request
     * @param response
     * @return void
     * @methodName: service
     * @description: 请求业务处理
     * @date: 2021-02-24 11:46
     * @author: zhengjing
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 保存文件内容
        if ("save".equals(request.getParameter("exc"))) {
            System.out.println(request.getParameter("content"));
            if (writeToLocal(request.getParameter("content"))) {
                request.setAttribute("msg", "修改成功");
            } else {
                request.setAttribute("msg", "修改失败");
            }

        }

        // 读取文件内容
        StringBuffer sb = new StringBuffer("");
        FileReader fr2 = null;
        BufferedReader bufr = null;
        try {
            fr2 = new FileReader(filePath);
            bufr = new BufferedReader(fr2);
            String line = null;
            while ((line = bufr.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取文件内容失败...." + filePath);
            request.setAttribute("msg", "读取文件内容失败...." + "\r\n" + filePath);
        } finally {
            fr2.close();
            bufr.close();
        }

        // 请求转发
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        request.setAttribute("data", sb.toString());
        request.getRequestDispatcher("set.jsp").forward(request, response);
    }

    private static boolean writeToLocal(String content) throws IOException {
        boolean seccessful = false;
        try {
            System.out.println(filePath);
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            seccessful = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return seccessful;
    }

}
