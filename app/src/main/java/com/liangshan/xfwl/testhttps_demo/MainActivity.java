package com.liangshan.xfwl.testhttps_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String uniqueId=null;
    private TextView tv,request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv= (TextView) findViewById(R.id.tv);
        request= (TextView) findViewById(R.id.request);
        request.setOnClickListener(this);

        UUID uuid = UUID.randomUUID();
        uniqueId = uuid.toString();
        Log.d("首页数据","--uniqueId--"+uniqueId+"--");

    }

    /**
     * 获取Https的证书
     * @param （fragment）的上下文
     * @return SSL的上下文对象
     */
//    private static SSLContext getSSLContext(Context context) {
//
//        CertificateFactory certificateFactory = null;
//        InputStream inputStream = null;
//        java.security.cert.Certificate cer = null;
//        KeyStore keystore = null;
//        TrustManagerFactory trustManagerFactory = null;
//        try {
//
//            certificateFactory = CertificateFactory.getInstance("X.509");
//            inputStream = context.getAssets().open("server.crt");//这里导入SSL证书文件
//
//            try {
//                //读取证书
//                cer = certificateFactory.generateCertificate(inputStream);
//
//            } finally {
//                inputStream.close();
//            }
//
//            //创建一个证书库，并将证书导入证书库
//            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keystore.load(null,null); //双向验证时使用
//            keystore.setCertificateEntry("trust", cer);
//
//            // 实例化信任库
//            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            // 初始化信任库
//            trustManagerFactory.init(keystore);
//
//            SSLContext s_sSLContext = SSLContext.getInstance("TLS");
//            s_sSLContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
//
//            return s_sSLContext;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private void requestData(String url_homepage) {

        //声明：网路请求参数
        RequestParams params = new RequestParams(url_homepage);
        params.addBodyParameter("uuid", uniqueId);    // 添加参数
        params.addBodyParameter("city_code", "");    // 添加参数
        params.addBodyParameter("city_name", "");    // 添加参数
        /** 发送登录请求 */
        HttpUtil.send(MainActivity.this, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                tv.setText(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                tv.setText(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }//回调函数内容
            });
}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.request:
                String url="https://119.10.73.218:8443/XFWLServletProject/home/getHomeInfo.do";
                requestData(url);
                break;
            default:break;
        }
    }
}