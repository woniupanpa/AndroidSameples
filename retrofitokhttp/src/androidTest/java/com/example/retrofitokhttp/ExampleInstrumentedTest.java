package com.example.retrofitokhttp;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.retrofitokhttp.kbankproject.CommunicationEvent;
import com.example.retrofitokhttp.kbankproject.HttpCommunication;
import com.example.retrofitokhttp.util.BytesUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Connection;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private final static String HOST = "free-api.heweather.com";
    private final static Integer PORT = 443;
    private Connection connection;
    private final String TAG = ExampleInstrumentedTest.class.getSimpleName();

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.retrofitokhttp", appContext.getPackageName());
    }


    /**
     * Okhttp Get
     *
     * send data:
     * GET http://api.k780.com/?app=weather.history HTTP/1.1
     * Host: api.k780.com
     * Connection: Keep-Alive
     * Accept-Encoding: gzip
     * User-Agent: okhttp/3.8.1
     *
     * receive data:
     * HTTP/1.1 200 OK
     * Server: nginx
     * Date: Mon, 10 Jan 2022 00:35:23 GMT
     * Content-Type: application/json; charset=utf-8;
     * Transfer-Encoding: chunked
     * Connection: keep-alive
     * Access-Control-Allow-Origin: *
     *
     * 36
     * {"success":"0","msgid":"1000555","msg":"SIGN_INVALID"}
     * 0
     */
    @Test
    public void testOkhttpGet() {
        String url = "http://api.k780.com/?app=weather.history";
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).get().build();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                message.obj = e.getMessage();
                //mHandler.sendMessage(message);
                Log.d(TAG, "onFailure: " + message.obj.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = response.body().string();//string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                //mHandler.sendMessage(message);
                Log.d(TAG, "response: " + message.obj.toString());
            }
        });
    }


    /**
     * Retrofit + httpGet:
     * @GET("search")
     * Observable<List < ZhuangbiImage>> search(@Query("q") String query);
     *
     * send data:
     * GET http://www.zhuangbi.info/search?q=%E8%A3%85%E9%80%BC HTTP/1.1
     * Host: www.zhuangbi.info
     * Connection: Keep-Alive
     * Accept-Encoding: gzip
     * User-Agent: okhttp/3.10.0
     *
     * get data:
     *
     * HTTP/1.1 301 Moved Permanently
     * Server: nginx/1.10.3 (Ubuntu)
     * Date: Mon, 10 Jan 2022 01:50:17 GMT
     * Content-Type: text/html
     * Content-Length: 194
     * Connection: keep-alive
     * Location: https://www.zhuangbi.info/search?q=%E8%A3%85%E9%80%BC
     *
     * <html>
     * <head><title>301 Moved Permanently</title></head>
     * <body bgcolor="white">
     * <center><h1>301 Moved Permanently</h1></center>
     * <hr><center>nginx/1.10.3 (Ubuntu)</center>
     * </body>
     * </html>
     */
    @Test
    public void httpsClientTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String url = "http://www.zhuangbi.info/";
        HttpCommunication httpCommunication = new HttpCommunication(context, url, false);
        String sendData = "装逼";


        httpCommunication.sendAndReceive(sendData)
                .subscribe(communicationEvent -> {

                            Log.i(TAG, praseCommunicationType(communicationEvent.getCommunicationType()));

                            switch (communicationEvent.getCommunicationType()) {
                                case CommunicationEvent.SENDING:
                                case CommunicationEvent.SENDED:
                                    Log.d(TAG, "send data:" + communicationEvent.getData());
                                    break;
                                case CommunicationEvent.RECEIVED:
                                    Log.d(TAG, "RECEIVED data:" + communicationEvent.getData());
                                    break;
                            }
                        }, throwable -> Log.e(TAG, "OnError:" + throwable.getMessage()),
                        () -> Log.e(TAG, "OnComplete"));

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @GET("data/福利/{number}/{page}")
     * Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);
     *
     * send data:
     * GET http://www.zhuangbi.info/data/%E7%A6%8F%E5%88%A9/100/1 HTTP/1.1
     * Host: www.zhuangbi.info
     * Connection: Keep-Alive
     * Accept-Encoding: gzip
     * User-Agent: okhttp/3.10.0
     *
     * receive data:
     * HTTP/1.1 301 Moved Permanently
     * Server: nginx/1.10.3 (Ubuntu)
     * Date: Mon, 10 Jan 2022 03:10:42 GMT
     * Content-Type: text/html
     * Content-Length: 194
     * Connection: keep-alive
     * Location: https://www.zhuangbi.info/data/%E7%A6%8F%E5%88%A9/100/1
     *
     * <html>
     * <head><title>301 Moved Permanently</title></head>
     * <body bgcolor="white">
     * <center><h1>301 Moved Permanently</h1></center>
     * <hr><center>nginx/1.10.3 (Ubuntu)</center>
     * </body>
     * </html>
     */
    @Test
    public void httpsClientTest2() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String url = "http://www.zhuangbi.info/";
        HttpCommunication httpCommunication = new HttpCommunication(context, url, false);


        httpCommunication.sendAndReceive2()
                .subscribe(communicationEvent -> {

                            Log.i(TAG, praseCommunicationType(communicationEvent.getCommunicationType()));

                            switch (communicationEvent.getCommunicationType()) {
                                case CommunicationEvent.SENDING:
                                case CommunicationEvent.SENDED:
                                    Log.d(TAG, "send data:" + communicationEvent.getData());
                                    break;
                                case CommunicationEvent.RECEIVED:
                                    Log.d(TAG, "RECEIVED data:" + communicationEvent.getData());
                                    break;
                            }
                        }, throwable -> Log.e(TAG, "OnError:" + throwable.getMessage()),
                        () -> Log.e(TAG, "OnComplete"));

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @POST Observable<ResponseBody> postWithoutEncryption(
     * @Url String url,
     * @Header("Authorization") String authorization,
     * @Body RequestBody body
     * );
     * <p>
     * POST http://192.168.43.110:5507/gateway/jsonGateway/alipay/order/ HTTP/1.1
     * Authorization: secretkeysecretkeysecretkeysecretkey
     * Content-Type: application/json; charset=utf-8
     * Content-Length: 82
     * Host: 192.168.43.110:5507
     * Connection: Keep-Alive
     * Accept-Encoding: gzip
     * User-Agent: okhttp/3.10.0
     * <p>
     * {"code":"0000","message":"SUCCESS","data":{"mid":"1234567890","tid":"1234567890"}}
     */
    @Test
    public void httpsClientTest3() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String url = "http://192.168.43.110:5507/gateway/jsonGateway/alipay/order/ ";
        HttpCommunication httpCommunication = new HttpCommunication(context, url, false);

        String sendData = "{\"code\":\"0000\",\"message\":\"SUCCESS\",\"data\":{\"mid\":\"1234567890\",\"tid\":\"1234567890\"}}";

        httpCommunication.sendAndReceive3(sendData)
                .subscribe(communicationEvent -> {

                            Log.i(TAG, praseCommunicationType(communicationEvent.getCommunicationType()));

                            switch (communicationEvent.getCommunicationType()) {
                                case CommunicationEvent.SENDING:
                                case CommunicationEvent.SENDED:
                                    Log.d(TAG, "send data:" + communicationEvent.getData());
                                    break;
                                case CommunicationEvent.RECEIVED:
                                    Log.d(TAG, "RECEIVED data:" + communicationEvent.getData());
                                    break;
                            }
                        }, throwable -> Log.e(TAG, "OnError:" + throwable.getMessage()),
                        () -> Log.e(TAG, "OnComplete"));

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testHttpsMutualAuth() {

//        HttpCommunication communication = new HttpCommunication("https://60.208.131.91:12809/", true);
//
//        communication.setMutualAuthEnabled(true);
//        communication.setCaFile("certificate/CFCA_TEST_CS_CA.cer");
//        communication.setCertificateFile("certificate/new_cert.crt");
//        communication.setPrivateKeyFile("certificate/privatekey.pem");
//
//        communication.oauth("ZGP2P00", "ZGP2P00", "client_credentials")
//            .subscribe(communicationEvent -> {
//
//                    Timber.i(praseCommunicationType(communicationEvent.getCommunicationType()));
//
//                    switch (communicationEvent.getCommunicationType()) {
//                        case CommunicationEvent.SENDING:
//                            Timber.e("sending data");
//                            break;
//                        case CommunicationEvent.SENDED:
//                            Timber.e("send data");
//                            break;
//                        case CommunicationEvent.RECEIVED:
//                            Timber.e("RECEIVED data" + new String(communicationEvent.getData()));
//                            break;
//                    }
//                }, throwable -> Timber.e("OnError:" + throwable.getMessage()),
//                () -> Timber.e("OnComplete"));
//
//        try {
//            Thread.sleep(100000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private String praseCommunicationType(@CommunicationEvent.Type int type) {
        switch (type) {
            case CommunicationEvent.CONNECTING:
                return "CONNECTING";
            case CommunicationEvent.CONNECTED:
                return "CONNECTED";
            case CommunicationEvent.SENDING:
                return "SENDING";
            case CommunicationEvent.SENDED:
                return "SENDED";
            case CommunicationEvent.RECEIVEING:
                return "RECEIVEING";
            case CommunicationEvent.RECEIVED:
                return "RECEIVED";
        }

        return "unknown";
    }

}