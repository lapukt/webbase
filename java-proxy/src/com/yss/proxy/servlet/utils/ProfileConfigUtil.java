package com.yss.proxy.servlet.utils;

import com.yss.proxy.servlet.SetServlet;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName: ProfileConfigUtil
 * @Description: 环境变量配置文件工具类
 * @author: zhengjing
 * @createdate: 2021-04-01 18:02
 */
public final class ProfileConfigUtil {

    private static Logger logger = Logger.getLogger(ProfileConfigUtil.class);


    /**
     * 网关地址
     */
    public final static String SSO_GETWAY_URL;
    /**
     * SSO统一单点登录地址
     */
    public final static String SSO_LOGIN_URL;
    /**
     * 配置文件
     */
    private final static String CONFIG_FILENAME = "config.properties";
    /**
     * linux 具体配置文件路径（pro生产环境）
     */
    public final static String CONFIG_PATH = "/acs/application/yssreport/war/conf/config.properties";
    /**
     * 本地测试(uat环境)
     */
    public final static String CONFIG_PATH2 = ProfileConfigUtil.class.getClassLoader().getResource("config.properties").getPath();

    static {
        //Properties pros = getEnvProfile();
        File file = new File(CONFIG_PATH);
        String filePath = CONFIG_PATH;
        if(!file.exists()){
            System.out.println("================>The file is not exist. [" + CONFIG_PATH+"]");
            logger.warn("================>The file is not exist. [" + CONFIG_PATH+"]");
            filePath = CONFIG_PATH2;
        }
        System.out.println("================>init load file " + filePath);
        Properties pros = readProperties(filePath);
        String profile = pros.getProperty("config.profile");
        SSO_GETWAY_URL = pros.getProperty("proxy.getway." + profile);
        SSO_LOGIN_URL = pros.getProperty("proxy.login." + profile);
        System.out.println("================>SSO_GETWAY_URL:"+SSO_GETWAY_URL);
        System.out.println("================>SSO_LOGIN_URL:"+SSO_LOGIN_URL);
    }

    /**
     * 读取环境变量
     *
     * @return string
     */
    private static Properties getEnvProfile() throws RuntimeException {
        String env = "";
        Map map = System.getenv();
        Iterator i = map.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            //读取BBSJ_PROXY_HOME的环境变量 如:BBSJ_PROXY_HOME = D:\\conf\
            if (entry.getKey().equals("BBSJ_PROXY_HOME")) {
                env = entry.getValue().toString();
                break;
            }
        }
        if ("".equals(env)) {
            logger.error("获取环境变量BBSJ_PROXY_HOME失败,请检查是否配置了环境变量BBSJ_PROXY_HOME.");
            throw new RuntimeException("获取环境变量BBSJ_PROXY_HOME失败,请检查是否配置了环境变量BBSJ_PROXY_HOME.");
        }
        return readProperties(env + CONFIG_FILENAME);
    }

    /**
     * 读取外部properties文件内容
     *
     * @param path
     * @return
     */
    private static Properties readProperties(String path) throws RuntimeException {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(path);
            prop.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }

    /**
     * @param args
     * @return void
     * @methodName: main
     * @description: 测试
     * @date: 2021-04-01 18:04
     * @author: zhengjing
     */
/*    public static void main(String[] args) throws Exception {
        //Properties pros = getTomcatEnvByLinux();
        Properties pros = readProperties(CONFIG_PATH);
        String profile = pros.getProperty("config.profile");
        System.out.println(pros.getProperty("proxy.getway." + profile));
        System.out.println(pros.getProperty("proxy.login." + profile));
    }*/
}
