package com.newtv.http.internal.httpconnect;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.newtv.http.EventListener;
import com.newtv.http.MethodType;
import com.newtv.http.config.HttpConfig;
import com.newtv.http.request.BaseHttpRequest;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.internal.HttpService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author ZhangXu
 * @date 2020/11/9
 */
public class HttpConnectionServiceImpl implements HttpService {

    private static volatile HttpConnectionServiceImpl INSTANCE;

    private final ExecutorService executor;
    private final Handler mHandler;


    private final Map<Object, List<HttpURLConnection>> connectionMap= new ConcurrentHashMap<>();


    private static final String TAG = HttpConnectionServiceImpl.class.getSimpleName();

    private HttpConnectionServiceImpl() {
        executor = Executors.newFixedThreadPool(5);
        mHandler = new Handler(Looper.myLooper());
    }

    public static HttpConnectionServiceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpConnectionServiceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpConnectionServiceImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void sendRequest(BaseHttpRequest request, HttpListener listener, EventListener eventListener) {
        executor.execute(() -> {

            if (request.methodType() == MethodType.GET) {
                sendGetRequestInternal(request, initGetUrl(request), listener, eventListener);
            } else {
                sendPostRequestInternal(request, listener, eventListener);
            }
        });
    }

    private String initGetUrl(BaseHttpRequest request) {

        StringBuilder data = new StringBuilder();
        data.append(request.baseUrl())
                .append(request.secondUrl()).append("?");
        Map<String, String> params = request.params();
        if (params != null) {
            String[] keys = params.keySet().toArray(new String[0]);
            for (int i = 0; i < keys.length; i++) {
                if (i == 0) {
                    data.append(keys[i])
                            .append("=")
                            .append(params.get(keys[i]));
                } else {
                    data.append("&")
                            .append(keys[i])
                            .append("=")
                            .append(params.get(keys[i]));
                }
            }
        }

        return data.toString();
    }




    private void sendGetRequestInternal(BaseHttpRequest request, String urlString, HttpListener listener, EventListener eventListener) {
        HttpURLConnection connection = null;

        InputStream inputStream = null;


        HttpConfig config = request.httpConfig();
        if (config != null) {
            if (config.getHostnameVerifier() != null) {
                HttpsURLConnection.setDefaultHostnameVerifier(config.getHostnameVerifier());
            }
            if (config.getSslSocketFactory() != null) {
                HttpsURLConnection.setDefaultSSLSocketFactory(config.getSslSocketFactory());
            } else {
                HttpsURLConnection.setDefaultSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
            }
        }

        eventListener.callStart();
        try {

            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            if (connectionMap.containsKey(request.tag())) {
                List<HttpURLConnection> httpURLConnections = connectionMap.get(request.tag());
                httpURLConnections.add(connection);
            } else {
                List<HttpURLConnection> httpURLConnections = new ArrayList<>();
                httpURLConnections.add(connection);
                connectionMap.put(request.tag(), httpURLConnections);
            }

            connection.setDoInput(true);

            httpConfig(request, connection);

            httpHeaders(request, connection);

            connection.setRequestMethod("GET");

            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                inputStream = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int length;
                StringBuilder str = new StringBuilder();
                while ((length = inputStream.read(buffer)) > -1) {
                    str.append(new String(buffer, 0, length));
                }

                Log.d(TAG, "response = " + str.toString());

                mHandler.post(() -> {
                    listener.onRequestResult(str.toString());
                });

            } else {
                inputStream = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int length;
                StringBuilder str = new StringBuilder();
                while ((length = inputStream.read(buffer)) > -1) {
                    str.append(new String(buffer, 0, length));
                }

                Log.d(TAG, "response = " + str.toString());

                mHandler.post(() -> {
                    listener.onRequestError(new Exception(str.toString()));
                });
            }

            eventListener.callEnd();

        } catch (Exception e) {
            mHandler.post(() -> {
                listener.onRequestError(e);
            });
            eventListener.callFailed();
            e.printStackTrace();
        } finally {
            Log.e(TAG, "HttpConnectionServiceImpl ----> send GET FINAL");
            if (connection != null) {
                connection.disconnect();
                if (connectionMap.get(request.tag()) != null) {
                    connectionMap.get(request.tag()).remove(connection);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void sendPostRequestInternal(BaseHttpRequest request, HttpListener listener, EventListener eventListener) {


        HttpURLConnection connection = null;

        InputStream inputStream = null;

        OutputStream outputStream = null;

        HttpConfig config = request.httpConfig();
        if (config != null) {
            if (config.getHostnameVerifier() != null) {
                HttpsURLConnection.setDefaultHostnameVerifier(config.getHostnameVerifier());
            }
            if (config.getSslSocketFactory() != null) {
                HttpsURLConnection.setDefaultSSLSocketFactory(config.getSslSocketFactory());
            } else {
                HttpsURLConnection.setDefaultSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
            }
        }


        try {

            URL url = new URL(request.baseUrl() + request.secondUrl());
            connection = (HttpURLConnection) url.openConnection();

            if (connectionMap.containsKey(request.tag())) {
                List<HttpURLConnection> httpURLConnections = connectionMap.get(request.tag());
                httpURLConnections.add(connection);
            } else {
                List<HttpURLConnection> httpURLConnections = new ArrayList<>();
                httpURLConnections.add(connection);
                connectionMap.put(request.tag(), httpURLConnections);
            }

            connection.setDoInput(true);
            connection.setDoOutput(true);

            httpConfig(request, connection);

            httpHeaders(request, connection);

            connection.setRequestMethod("POST");

            outputStream = connection.getOutputStream();
            StringBuilder data = new StringBuilder();
            Map<String, String> params = request.params();
            if (params != null) {
                String[] keys = params.keySet().toArray(new String[0]);
                for (int i = 0; i < keys.length; i++) {
                    if (i == 0) {
                        data.append(keys[i])
                                .append("=")
                                .append(params.get(keys[i]));
                    } else {
                        data.append("&")
                                .append(keys[i])
                                .append("=")
                                .append(params.get(keys[i]));
                    }
                }
            }
            outputStream.write(data.toString().getBytes());//上传参数

            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                inputStream = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int length = 0;
                StringBuilder str = new StringBuilder();
                while ((length = inputStream.read(buffer)) > -1) {
                    str.append(new String(buffer, 0, length));
                }

                Log.d(TAG, "response = " + str.toString());

                mHandler.post(() -> {
                    listener.onRequestResult(str.toString());
                });
                eventListener.callEnd();

            } else {
                inputStream = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int length = 0;
                StringBuilder str = new StringBuilder();
                while ((length = inputStream.read(buffer)) > -1) {
                    str.append(new String(buffer, 0, length));
                }

                Log.d(TAG, "response = " + str.toString());
                mHandler.post(() -> {
                    listener.onRequestResult(str.toString());
                });
                eventListener.callFailed();

            }


        } catch (Exception e) {
            mHandler.post(() -> listener.onRequestError(e));
            eventListener.callFailed();
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
                if (connectionMap.get(request.tag()) != null) {
                    connectionMap.get(request.tag()).remove(connection);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void httpConfig(BaseHttpRequest request, HttpURLConnection connection) {
        HttpConfig config = request.httpConfig();
        if (config != null) {
            if (config.getConnectTimeout() > 0) {
                if (config.getConnectTimeoutTimeUnit() == TimeUnit.SECONDS) {
                    connection.setConnectTimeout(config.getConnectTimeout() * 1000);
                } else if (config.getConnectTimeoutTimeUnit() == TimeUnit.MILLISECONDS) {
                    connection.setConnectTimeout(config.getConnectTimeout());
                }

            }
            if (config.getReadTimeout() > 0) {
                if (config.getConnectTimeoutTimeUnit() == TimeUnit.SECONDS) {
                    connection.setReadTimeout(config.getReadTimeout() * 1000);
                } else if (config.getConnectTimeoutTimeUnit() == TimeUnit.MILLISECONDS) {
                    connection.setReadTimeout(config.getReadTimeout());
                }
            }

            if (config.getWriteTimeout() > 0) {
                if (config.getConnectTimeoutTimeUnit() == TimeUnit.SECONDS) {
                    connection.setReadTimeout(config.getWriteTimeout() * 1000);
                } else if (config.getConnectTimeoutTimeUnit() == TimeUnit.MILLISECONDS) {
                    connection.setReadTimeout(config.getWriteTimeout());
                }

            }
        }
    }


    private void httpHeaders(BaseHttpRequest request, HttpURLConnection connection) {
        // 设置请求头
        Map<String, String> headers = request.headers();
        if (headers != null) {
            Set<String> keys = headers.keySet();

            for (String key : keys) {
                connection.setRequestProperty(key, headers.get(key));
            }
        }
    }

    @Override
    public void cancelRequest(Object tag) {
        executor.execute(() -> {

            if (tag == null) {
                return;
            }
            if (connectionMap.containsKey(tag)) {
                List<HttpURLConnection> httpURLConnections = connectionMap.remove(tag);

                Log.e("zhangxu", "HttpConnectionServiceImpl ---> cancel()  +  size = " + httpURLConnections.size());

                for (HttpURLConnection connection : httpURLConnections) {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });

    }

    @Override
    public void cancelAllRequest() {
        Set<Object> keys = connectionMap.keySet();
        for (Object key : keys) {
            cancelRequest(key);
        }
    }
}
