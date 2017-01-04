package com.liangshan.xfwl.testhttps_demo;

import android.content.Context;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Administrator on 2016/12/22.
 */

public class HttpUtil {

    /** Https 证书验证对象 */
    private static SSLContext s_sSLContext = null;

    /**
     * Https请求发送
     * @param context Activity（fragment）的资源上下文
     * @param params 发送的请求
     * @param callBack 回调对象（具体接口形式参见xUtils sample的httpFragment.java）
     * @return true=正常调用 false＝异常调用
     */
    public static boolean send(Context context, RequestParams params, Callback.CommonCallback callBack) {
        /* 判断https证书是否成功验证 */
        SSLContext sslContext = getSSLContext(context);
        if(null == sslContext){
            if (BuildConfig.DEBUG) Log.d("HttpUtil", "Error:Can't Get SSLContext!");
            return false;
        }
        //绑定SSL证书
        params.setSslSocketFactory(sslContext.getSocketFactory());
        //SSLContext sslContext = getSSLContext(MainActivity.this);
        //params.setSslSocketFactory(sslContext.getSocketFactory()); //绑定SSL证书(https请求)
        HttpsURLConnection.setDefaultHostnameVerifier(new CnHostnameVerifier());
        x.http().request(HttpMethod.POST, params, callBack);
        return true;
    }
    private static class CnHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }
    }
    /**
     * Https下载图片
     * @param context Activity（fragment）的资源上下文
     * @return InputStream
     */
    public static InputStream getRequestInputstream(Context context, String path) throws Exception {
        /* 判断https证书是否成功验证 */
        SSLContext sslContext = getSSLContext(context);
        if(null == sslContext){
            if (BuildConfig.DEBUG) Log.d("HttpUtil", "Error:Can't Get SSLContext!");
            return null;
        }
        //绑定SSL证书
        java.net.URL url = new java.net.URL(path);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(sslContext.getSocketFactory());
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        //连接
        conn.connect();
        return conn.getInputStream();
    }

    /**
     * Https请求发送(同步请求 上传图片使用)
     * @param context Activity（fragment）的资源上下文
     * @param params 发送的请求
     * @param callBack 回调对象（具体接口形式参见xUtils sample的httpFragment.java）
     * @return true=正常调用 false＝异常调用
     */
    public static boolean sendSync(Context context, RequestParams params, Callback.TypedCallback callBack) throws Throwable {
        /* 判断https证书是否成功验证 */
        SSLContext sslContext = getSSLContext(context);
        if(null == sslContext){
            if (BuildConfig.DEBUG) Log.d("HttpUtil", "Error:Can't Get SSLContext!");
            return false;
        }
        //绑定SSL证书
        params.setSslSocketFactory(sslContext.getSocketFactory());
        x.http().requestSync(HttpMethod.POST, params, callBack);
        return true;
    }

    /**
     * 获取Https的证书
     * @param context Activity（fragment）的上下文
     * @return SSL的上下文对象
     */
    private static SSLContext getSSLContext(Context context) {
        if (null != s_sSLContext) {
            return s_sSLContext;
        }

        //以下代码来自百度 参见http://www.tuicool.com/articles/vmUZf2
        CertificateFactory certificateFactory = null;

        InputStream inputStream = null;
        KeyStore keystore = null;
        String tmfAlgorithm = null;
        TrustManagerFactory trustManagerFactory = null;
        try {
            try {
                certificateFactory = CertificateFactory.getInstance("X.509");
            } catch (CertificateException e) {
                e.printStackTrace();
            }

            inputStream = context.getAssets().open("server.crt");//这里导入SSL证书文件
//            inputStream = context.getAssets().open("51p2b_server_bs.pem");//这里导入SSL证书文件

            Certificate ca = certificateFactory.generateCertificate(inputStream);

            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            keystore.setCertificateEntry("ca", ca);

            tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
            trustManagerFactory.init(keystore);

            // Create an SSLContext that uses our TrustManager
            s_sSLContext = SSLContext.getInstance("TLS");
            s_sSLContext.init(null, trustManagerFactory.getTrustManagers(), null);
            return s_sSLContext;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

