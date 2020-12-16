
### 目的：

---

1.  向产品链提供一个公用的网络请求框架。
2. 使业务层与封装的网络请求库彻底解耦，应用层只依赖框架暴露的接口，并不依赖任何三方网络请求库，对底层逻辑替换无感。

### 功能

----

1. 网络请求
2. 网络请求取消
3. 通过使用RxJava，解决了多个网络请求的嵌套问题（避免业务层引入Rxjava，以后可以改进自己实现这层逻辑）

### 使用

----

#### 业务层需要关注的类包括：

1. `ApiHelper.java`: 其中包含着发送请求方法，取消请求方法，取消所有网络请求
2. Request:
   1. `BaseHttpRequest.java`:  抽象请求类，业务层的Request必须继承该类
   2. `PostBaseHttpRequest.java`：设置请求方法为**POST**
   3. `GetBaseHttpRequest.java`： 设置请求方法为**GET**
3. `EventListener.java`: 请求事件监听回调
4. 自定义**Response** bean 类： 底层会根据传入的类型，将返回的JSON字符串转成Bean对象

#### 发送网络请求

```java
TestGetRequest testGetRequest = new TestGetRequest(MainActivity.this);
testGetRequest.a = "fy";
testGetRequest.f = "auto";
testGetRequest.t = "auto";
testGetRequest.w = "hello%20world";

ApiHelper.send(testGetRequest, new NewTypeReference<TestResponse>() {})
  .subscribe(new Observer<TestResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull TestResponse testResponse) {
                Log.e(TAG, testResponse.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
```

#### 详细API说明

##### `ApiHelper.java`

```java
/**
 * 发送网络请求
 * @param request 请求
 * @param type 网络请求返回Bean的类型
 */
public static <T> Observable<T> send(BaseHttpRequest request, TypeReference<T> type){
  ......
}

/**
 * 发送网络请求
 * @param request 请求
 * @param type 网络请求返回Bean的类型
 * @param eventListener 请求事件监听回调
 */
public static <T> Observable<T> send(BaseHttpRequest request, TypeReference<T> type, EventListener eventListener){
  ......
}

/**
 * 取消固定标志位的网络请求
 * @param tag 标志位
 */
public static void cancelRequest(Object tag) {
  ....
}

/**
 * 取消所有请求
 */
public static void cancelAllRequest() {
  ....
}
```



##### `BaseHttpRequest.java`

```java
		/**
     * 获得当前Request的标识
     * @return context
     */
    public final Object tag() {
        return mContext;
    }

    /**
     * 服务器地址
     * @return 正式服务器或测试服务器
     */
    public abstract String baseUrl();

    /**
     * 请求URL
     * @return
     */
    public abstract String secondUrl();

    /**
     * 获得头信息
     * @return
     */
    public abstract Map<String, String> headers();

    /**
     * 将请求转成JSON
     * @return
     */
    public abstract String toJson();

    /**
     * 网络请求的配置
     * @return
     */
    public abstract HttpConfig httpConfig();

    /**
     * 将请求参数转成MAP
     * @return
     */
    public abstract Map<String, String> params();

    /**
     * 请求方法
     * @return
     */
    public abstract @MethodType.Method int methodType();
```

##### `HttpConfig.java`

```java
		/**
     * 连接超时时间
     */
    private final int connectTimeout;

    /**
     * 连接超时时间 单位
     */
    private final TimeUnit connectTimeoutTimeUnit;

    /**
     * 读超时时间
     */
    private final int readTimeout;


    /**
     * 读超时时间 单位
     */
    private final TimeUnit readTimeoutTimeUnit;


    /**
     * 写 超时时间
     */
    private final int writeTimeout;


    /**
     * 写 超时时间 单位
     */
    private final TimeUnit writeTimeoutTimeUnit;


    /**
     * 拦截器
     */
    private final List<NewInterceptor> interceptors;

    /**
     * 重试
     */
    private final RetryParam retryParam;

    /**
     * SSLSocket
     */
    private final SSLSocketFactory sslSocketFactory;

    /**
     * X509TrustManager
     */
    private final X509TrustManager x509TrustManager;

    /**
     * 域名
     */
    private final HostnameVerifier hostnameVerifier;
```

##### `RetryParam.java`

```java
		/**
     * 重试次数
     */
    private final int maxRetryCount;
    /**
     * 重试时间间隔
     */
    private final int retryDelay;
```



### 框架说明

----

##### 层级图：

![层级图](https://github.com/ClickListener/picRepository/blob/master/img/层级图.png)

> 1. 业务调用层：**ApiHelper**向业务层接口，**Request层** 封装请求数据和网络配置， **EventListener**接口提供网络请求生命周期的回调。
> 2. 中间层：中间层的封装借鉴了**OkHttp**框架的责任链模式，并给业务层提供了自定义拦截器的能力
> 3. 底层网络请求层：
>    1. 底层通过接口封装，通过工厂模式，可以切换底层不同的网络请求的具体实现。（比如说目前是实现了三个底层请求，分别是**OKHttp底层请求**、**OkHttp + retrofit + rxJava**、**HttpUrlConnection底层请求**）
>    2. **底层**与**中间层**之间是通过接口交互的，所以未来需要集成新的底层网络请求，具体实现只需要继承并实现**HttpService**中的相应接口既可。

##### 调用时序图

![时序图](https://github.com/ClickListener/picRepository/blob/master/img/时序图.png)












