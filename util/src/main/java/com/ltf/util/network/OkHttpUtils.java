package com.ltf.util.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ltf.util.BaseApplication;
import com.ltf.util.BitmapUtils;
import com.ltf.util.LogUtil;
import com.ltf.util.R;
import com.ltf.util.ToastUtils;
import com.ltf.util.jsondata.BaseJsonData;
import com.ltf.util.jsondata.Parseable;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by justin on 16/12/3.
 * OKHttp封装请求工具了
 */
public class OkHttpUtils {

  private static final MediaType CONTENT_TYPE
      = MediaType.parse("application/x-www-form-urlencoded");

  private static final String CANCELED = "Canceled";
//    public class GetExample {
//        OkHttpClient client = new OkHttpClient();
//
//        String run(String url) throws IOException {
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        }
//
//        public static void main(String[] args) throws IOException {
//            GetExample example = new GetExample();
//            String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
//            System.out.println(response);
//        }
//    }

//    public class PostExample {
//        public static final MediaType JSON
//                = MediaType.parse("application/json; charset=utf-8");
//
//        OkHttpClient client = new OkHttpClient();
//z
//        String post(String url, String json) throws IOException {
//            RequestBody body = RequestBody.create(JSON, json);
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .build();
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        }
//
//        String bowlingJson(String player1, String player2) {
//            return "{'winCondition':'HIGH_SCORE',"
//                    + "'name':'Bowling',"
//                    + "'round':4,"
//                    + "'lastSaved':1367702411696,"
//                    + "'dateStarted':1367702378785,"
//                    + "'players':["
//                    + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
//                    + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
//                    + "]}";
//        }
//
//        public static void main(String[] args) throws IOException {
//            PostExample example = new PostExample();
//            String json = example.bowlingJson("Jesse", "Jake");
//            String response = example.post("http://www.roundsapp.com/post", json);
//            System.out.println(response);
//        }
//    }

  private static OkHttpClient client;

  static {
    OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .connectTimeout(60000, TimeUnit.MILLISECONDS)
        .readTimeout(60000, TimeUnit.MILLISECONDS)
        .writeTimeout(60000, TimeUnit.MILLISECONDS);
    try {
      SSLContext sslContext = SSLContext.getInstance("TLS");
      UnSafeTrustManager trustManager = new UnSafeTrustManager();
      sslContext.init(null, new TrustManager[]{trustManager}, null);
      builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
    } catch (Exception e) {
      e.printStackTrace();
    }
    client = builder.build();
  }

  private static class UnSafeTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new java.security.cert.X509Certificate[]{};
    }
  }

  /**
   * 同步get
   **/
  public static final <T extends BaseJsonData> String get(String url, Class<T> cla,
      Map<String, String> params, Object tag) throws IOException {
    return request(false, url, cla, params, CONTENT_TYPE, tag);
  }

  /**
   * 同步post
   **/
  public static final <T extends BaseJsonData> String post(String url, Class<T> cla,
      Map<String, String> params, Object tag) throws IOException {
    return request(true, url, cla, params, CONTENT_TYPE, tag);
  }

  /**
   * 同步请求
   **/
  public static final <T extends BaseJsonData> String request(boolean post, String url,
      Class<T> cla, Map<String, String> params, MediaType contentType, Object tag)
      throws IOException {
    if (!NetworkUtils.isNetworkAvailable(BaseApplication.getContext())) {
      BaseApplication.getContext().mMainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          ToastUtils.showToast(BaseApplication.getContext(), R.string.no_network_message);
        }
      });
      return null;
    }
    String paramStr = obtainParams(params);
    Request request = null;
    if (post) {
      RequestBody body = RequestBody.create(contentType, paramStr);
      request = new Request.Builder()
          .url(url)
          .post(body)
          .tag(tag)
          .build();
    } else {
      if (paramStr != null) {
        if (url.indexOf("?") > -1) {
          url += "&" + paramStr;
        } else {
          url += "?" + paramStr;
        }
      }
      request = new Request.Builder()
          .url(url)
          .tag(tag)
          .build();
    }
    Response response = client.newCall(request).execute();
    int responseCode = response.code();
    long dur = 0;
    String receivedMillis = response.header("OkHttp-Received-Millis", "0");
    String sentMillis = response.header("OkHttp-Sent-Millis", "0");
    if (TextUtils.isDigitsOnly(receivedMillis) && TextUtils.isDigitsOnly(sentMillis)) {
      dur = Long.valueOf(receivedMillis) - Long.valueOf(sentMillis);
    }
    String targetAddr = request.url().toString();
    if (!response.isSuccessful()) {
      throw new IOException("Unexpected code " + response);
    }
    String responseBody = response.body().string();
    if (!TextUtils.isEmpty(responseBody)) {
      T t = null;
      try {
        if (Parseable.class.isAssignableFrom(cla)) {
          t = cla.newInstance();
          ((Parseable) t).parse(new JSONObject(responseBody));
        } else {
          Gson gson = new Gson();
          t = gson.fromJson(responseBody, cla);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (t != null) {
        String code = t.getStatus();
        String msg = t.getMsg();
        if (!t.isSucc()) {
          if (!TextUtils.isEmpty(code)) {
            LogUtil.e("OkHttpUtils", "request is not success, code = " + code + " , msg = " + msg);
          }
        }
      }
    }
    response.body().close();
    return responseBody;
  }

  /**
   * 异步get
   **/
  public static final void getAsync(String url, Callback callback) {
    Request request = new Request.Builder()
        .url(url)
        .build();
    client.newCall(request).enqueue(callback);
  }

  /**
   * 异步post
   **/
  public static final void postAsync(String url, String json, MediaType contentType,
      Callback callback) {
    RequestBody body = RequestBody.create(contentType, json);
    Request request = new Request.Builder()
        .url(url)
        .post(body)
        .build();
    client.newCall(request).enqueue(callback);
  }

  //
  // 根据对应的接口再封装请求
  //
  public static final <T extends BaseJsonData> void getAsync(RequestAction action,
      HttpRequestListener<T> listener) {
    requestAsync(action, false, CONTENT_TYPE, listener);
  }

  public static final <T extends BaseJsonData> void postAsync(RequestAction action,
      HttpRequestListener<T> listener) {
    requestAsync(action, true, CONTENT_TYPE, listener);
  }

  public static final <T extends BaseJsonData> void postFormAsync(RequestAction action,
      HttpRequestListener<T> listener) {
    requestAsync(action, true, MultipartBody.FORM, listener);
  }

  /**
   * 异步请求
   **/
  public static final <T extends BaseJsonData> void requestAsync(final RequestAction action,
      boolean post, MediaType contentType, final HttpRequestListener<T> listener) {
    if (listener == null) {
      return;
    }
    if (!NetworkUtils.isNetworkAvailable(BaseApplication.getContext())) {
      handlerFailure(listener, new Exception("no network"), action, RequestError.CODE_NO_NETWORK);
      BaseApplication.getContext().mMainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          ToastUtils.showToast(BaseApplication.getContext(), R.string.no_network_message);
        }
      });
      return;
    }
    String url = listener.getUrl();
    Request request = null;
    if (MultipartBody.FORM == contentType) {
      LogUtil.e("okHttpUtils", " FORM url = " + url);
      request = new Request.Builder()
          .url(url)
          .post(obtainRequestFormBody(listener))
          .tag(listener.getTag())
          .build();
    } else {
      String paramStr = obtainParams(listener.getParams());
      if (post) {
        LogUtil.e("okHttpUtils", " POST url = " + url + " paramStr = " + paramStr);
        RequestBody body = RequestBody.create(contentType, paramStr);
        request = new Request.Builder()
            .url(url)
            .post(body)
            .tag(listener.getTag())
            .build();
      } else {
        if (paramStr != null) {
          if (url.contains("?")) {
            url += "&" + paramStr;
          } else {
            url += "?" + paramStr;
          }
        }
        LogUtil.e("okHttpUtils", " GET url = " + url);
        request = new Request.Builder()
            .url(url)
            .tag(listener.getTag())
            .build();
      }
    }
    enqueue(action, listener, request);
  }

  public static <T extends BaseJsonData> void enqueue(final RequestAction action,
      final HttpRequestListener<T> listener, final Request request) {
    final String targetAddr = request.url().toString();
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        int code = !NetworkUtils.isNetworkAvailable(BaseApplication.getContext())
            ? RequestError.CODE_NO_NETWORK : RequestError.CODE_ERROR;
        String msg = e.getMessage();
        if (CANCELED.equals(msg)) {
          code = RequestError.CODE_CANCELED;
        }
        LogUtil.e("OkHttpUtils",
            listener.getUrl() + " -->> onFailure Code： -->>   " + code + " Exception: " + msg);
        handlerFailure(listener, e, action, code);
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        Class<T> cla = listener.getClassForJsonData();
        //请求成功
        T t = null;
        String body = null;
        int responseCode = response.code();
        boolean postSucc = false;
        Object obj = null;
        if (!response.isSuccessful()) {
          IOException ioe = new IOException("Unexpected code " + response);
          handlerFailure(listener, ioe, action, responseCode);
          throw ioe;
        }
        if (listener.isIgnoreResult()) {
          response.body().close();
          return;
        }
        try {
          body = response.body().string();
          LogUtil.e("OkHttpUtils", listener.getUrl() + " -->> Server Return ： -->>   " + body);
          if (!TextUtils.isEmpty(body)) {
            try {
              if (Parseable.class.isAssignableFrom(cla)) {
                t = cla.newInstance();
                ((Parseable) t).parse(new JSONObject(body));
              } else {
                Gson gson = new Gson();
                t = gson.fromJson(body, cla);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          if (t != null) {
            t.setAction(action);
            if ((obj = t.obtainUIModel()) != null) {
              final Object object = obj;
              String code = t.getStatus();
              String msg = t.getMsg();
              postSucc = true;
              if (listener.isMainThread()) {
                BaseApplication.getContext().mMainThreadHandler.post(new Runnable() {
                  @Override
                  public void run() {
                    listener.onRequestSucc(object);
                  }
                });
              } else {
                listener.onRequestSucc(object);
              }
              if (!t.isSucc()) {
                if (!TextUtils.isEmpty(code)) {
                  LogUtil.e("OkHttpUtils",
                      "request2 is not success, code = " + code + " , msg = " + msg);
                }
              }
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          handlerFailure(listener, e, action, responseCode);
          return;
        } finally {
          response.body().close();
        }
        if (!postSucc) {
          // 抛错误
          String msg;
          if (TextUtils.isEmpty(body)) {
            msg = "response body is empty";
          } else if (t == null) {
            msg = "json obj is empty，解析可能出错";
          } else if (t != null) {
            msg = t.getMsg();
          } else {
            msg = "原因未知";
          }

          handlerFailure(listener, new Exception(t != null ? msg : (msg + ", [" + cla + "]")),
              action, responseCode);
        }
      }
    });
  }


  private static final <T extends BaseJsonData> void handlerFailure(
      final HttpRequestListener<T> listener, Exception e, RequestAction action, int code) {
    // 请求失败／错误时候
    final RequestError<T> re = new RequestError<T>();
    re.setError(e.getMessage());
    re.setAction(action);
    re.setUrl(listener.getUrl());
    re.setCode(code);

    if (listener.isMainThread()) {
      BaseApplication.getContext().mMainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          listener.onRequestFail(re);
        }
      });
    } else {
      listener.onRequestFail(re);
    }
  }

  public static final OkHttpClient getClient() {
    return client;
  }

  /**
   * 根据tag取消请求
   **/
  public static final void cancelRequestByTag(final Object tag) {
    BaseApplication.getContext().mThreadHandler.post(new Runnable() {
      @Override
      public void run() {
        cancelTag(tag);
      }
    });
  }

  private static void cancelTag(Object tag) {
    if (client == null) {
      return;
    }
    for (Call call : client.dispatcher().queuedCalls()) {
      if (tag.equals(call.request().tag())) {
        call.cancel();
      }
    }
    for (Call call : client.dispatcher().runningCalls()) {
      if (tag.equals(call.request().tag())) {
        call.cancel();
      }
    }
  }

  private static final String obtainParams(Map<String, String> params) {
    String paramStr = null;
    if (params != null && params.size() > 0) {
      final StringBuilder content = new StringBuilder();
      Set<String> keys = params.keySet();
      for (String name : keys) {
        if (content.length() > 0) {
          content.append('&');
        }
        if (TextUtils.isEmpty(name)) {
          continue;
        }
        try {
          String value = params.get(name);
          content.append(URLEncoder.encode(name, "UTF-8"))
              .append('=')
              .append(TextUtils.isEmpty(value) ? "" : URLEncoder.encode(value, "UTF-8"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      paramStr = content.toString();
    }
    return paramStr;
  }

  private static <T extends BaseJsonData> RequestBody obtainRequestFormBody(
      HttpRequestListener<T> listener) {
    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
        .setType(MultipartBody.FORM);

    Map<String, String> params = listener.getParams();
    if (params != null && params.size() > 0) {
      Set<String> keys = params.keySet();
      for (String name : keys) {
        if (TextUtils.isEmpty(name)) {
          continue;
        }
        try {
          String value = params.get(name);
          multipartBuilder.addPart(
              Headers.of("Content-Disposition",
                  "form-data; name=\"" + URLEncoder.encode(name, "UTF-8") + "\""),
              RequestBody
                  .create(null, TextUtils.isEmpty(value) ? "" : URLEncoder.encode(value, "UTF-8")));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    HashMap<String, String> uploadFilesParams = listener.getUploadFileParams();
    if (uploadFilesParams != null) {
      File cacheDir = BaseApplication.getContext().getExternalCacheDir();
      String fileType = listener.getUploadFileType();
      Iterator<Map.Entry<String, String>> iterator = uploadFilesParams.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, String> entry = iterator.next();
        String uploadName = entry.getKey();
        String filePath = entry.getValue();
        File tempFile = new File(filePath);
        String fileName = tempFile.getName();

        if (tempFile.exists()) {
          File temp = new File(cacheDir, fileName);
          File delTemp = temp;
          if (!temp.exists()) {
            // 获取扩展名
            String[] array = fileName.split("\\.");
            String suffix = (array != null && array.length > 0) ? array[array.length - 1] : "jpeg";

            try {//TODO 文件和图片
              if (BitmapUtils.compressImage(tempFile, suffix, temp)) {
                if (!temp.exists()) {
                  temp = tempFile;
                  delTemp = null;
                }
              } else {
                temp = tempFile;
                delTemp = null;
              }
            } catch (OutOfMemoryError e) {
              e.printStackTrace();
//                            if (BuildConfig.DEBUG) {
//                                ToastUtils.showToast(mContext, "上传图片前进行压缩出现了内存溢出");
//                            }
              delFile(delTemp);
              temp = tempFile;
              delTemp = null;
            }
          }
          multipartBuilder.addPart(
              Headers.of("Content-Disposition", "form-data; name=\"" + uploadName + "\";" +
                  " filename=\"" + URLEncoder.encode(temp.getName()) + "\""
              ),
              RequestBody.create(MediaType.parse(fileType), temp));
        }
      }
    }
    RequestBody requestBody = multipartBuilder.build();
    return requestBody;
  }

  private static void delFile(File file) {
    if (file != null && file.exists()) {
      try {
        file.delete();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
