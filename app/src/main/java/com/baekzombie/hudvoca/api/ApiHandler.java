package com.baekzombie.hudvoca.api;

import android.util.Log;

import com.baekzombie.hudvoca.common.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApiHandler {
    private String responseMessage = null;

    public int getMessage(String url, String param) {
        return getMessage(url, param, Constants.API_TIMEOUT);
    }

    public int getMessage(String url, String param, int timeout) {
        httpLogger(url, param);

        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);
        HttpConnectionParams.setTcpNoDelay(params, true);


        HttpGet httpGet = new HttpGet(url + param);
        int responseCode = 0;
        responseMessage = null;

        try {
            httpGet.setHeader("Accept", "application/json");
            httpGet.addHeader("Content-Type", "application/json");

            HttpResponse httpresponse = httpclient.execute(httpGet);
            responseCode = httpresponse.getStatusLine().getStatusCode();
            responseMessage = EntityUtils.toString(httpresponse.getEntity());
            responseLogger(responseCode, responseMessage);

        } catch (ClientProtocolException e) {
            Log.d("Debug", "ClientProtocolException === " + e.getMessage());
        } catch (IOException e) {
            Log.d("Debug", "IOException === " + e.getMessage());
        }

        return responseCode;
    }

    public int getMessage(String url, String param, String authKey) {
        return getMessage(url, param, authKey, Constants.API_TIMEOUT);
    }

    public int getMessage(String url, String param, String authKey, int timeout) {
        httpLogger(url, param);

        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);
        HttpConnectionParams.setTcpNoDelay(params, true);

        HttpGet httpGet = new HttpGet(url + param);
        int responseCode = 0;
        responseMessage = null;

        try {
            httpGet.setHeader("Accept", "application/json");
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("authKey", authKey);

            HttpResponse httpresponse = httpclient.execute(httpGet);
            responseCode = httpresponse.getStatusLine().getStatusCode();
            responseMessage = EntityUtils.toString(httpresponse.getEntity());
            responseLogger(responseCode, responseMessage);

        } catch (ClientProtocolException e) {
            Log.d("Debug", "ClientProtocolException === " + e.getMessage());
        } catch (IOException e) {
            Log.d("Debug", "IOException === " + e.getMessage());
        }

        return responseCode;
    }

    public int postMessage(String url, String json, String authKey) {
        return postMessage(url, json, Constants.API_TIMEOUT, authKey);
    }

    public int postMessage(String url, String json, String authKey, String token, String sid) {
        return postMessage(url, json, Constants.API_TIMEOUT);
    }

    public int postMessage(String url, String json, int timeout, String authKey) {
        httpLogger(url, json);

        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);

        HttpPost httpPost = new HttpPost(url);
        int responseCode = 0;
        responseMessage = null;

        try {
            StringEntity entity = new StringEntity(json, HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Content-Type", "application/json");
            if (authKey != null)
                httpPost.addHeader("authKey", authKey);

            HttpResponse httpresponse = httpclient.execute(httpPost);
            responseCode = httpresponse.getStatusLine().getStatusCode();
            responseMessage = EntityUtils.toString(httpresponse.getEntity());
            responseLogger(responseCode, responseMessage);
        } catch (ClientProtocolException e) {
            Log.d("Debug", "ClientProtocolException === " + e);
        } catch (IOException e) {
            Log.d("Debug", "IOException === " + e);
        }

        return responseCode;
    }

    public int postMessage(String url, String json, int timeout) {
        httpLogger(url, json);

        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);

        HttpPost httpPost = new HttpPost(url);
        int responseCode = 0;
        responseMessage = null;
        try {
            StringEntity entity = new StringEntity(json, HTTP.UTF_8);
            entity.setContentType("application/x-www-form-urlencoded");
//            httpPost.setEntity(entity);
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse httpresponse = httpclient.execute(httpPost);
            responseCode = httpresponse.getStatusLine().getStatusCode();
            responseMessage = EntityUtils.toString(httpresponse.getEntity());
            responseLogger(responseCode, responseMessage);
        } catch (ClientProtocolException e) {
            Log.d("Debug", "ClientProtocolException === " + e);
        } catch (IOException e) {
            Log.d("Debug", "IOException === " + e);
        }

        return responseCode;
    }

    public int deleteMessage(String url, String param) {
        return deleteMessage(url, param, Constants.API_TIMEOUT);
    }

    public int deleteMessage(String url, String param, int timeout) {
        httpLogger(url, param);

        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);

        HttpDelete httpDelete = new HttpDelete(url + param);
        int responseCode = 0;

        try {
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-Type", "application/json");

            HttpResponse httpresponse = httpclient.execute(httpDelete);

            responseCode = httpresponse.getStatusLine().getStatusCode();
            responseMessage = EntityUtils.toString(httpresponse.getEntity());

            responseLogger(responseCode, responseMessage);
        } catch (ClientProtocolException e) {
            Log.d("Debug", "ClientProtocolException == " + e.getMessage());
        } catch (IOException e) {
            Log.d("Debug", "IOException == " + e.getMessage());
        }

        return responseCode;
    }

    public int putMessage(String url, String param) {
        return putMessage(url, param, Constants.API_TIMEOUT);
    }

    public int putMessage(String url, String param, int timeout) {
        httpLogger(url, param);

        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);

        HttpPut httpPut = new HttpPut(url + param);
        int responseCode = 0;

        try {
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-Type", "application/json");

            HttpResponse httpresponse = httpclient.execute(httpPut);

            responseCode = httpresponse.getStatusLine().getStatusCode();
            responseMessage = EntityUtils.toString(httpresponse.getEntity());

            responseLogger(responseCode, responseMessage);
        } catch (ClientProtocolException e) {
            Log.d("Debug", "ClientProtocolException == " + e.getMessage());
        } catch (IOException e) {
            Log.d("Debug", "IOException == " + e.getMessage());
        }

        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    private void httpLogger(String url, String data) {

        if (true) {
            Log.d("Debug", "================================================");
            Log.d("Debug", "url:" + url);
            if (data != null)
                Log.d("Debug", "data:" + data);
            Log.d("Debug", "================================================");
        }
    }

    private void responseLogger(int responseCode, String responseMessage) {

        if (true) {
            Log.d("Debug", "================================================");
            Log.d("Debug", "responseCode:" + responseCode);
            Log.d("Debug", "responseMessage:" + responseMessage);
            Log.d("Debug", "================================================");
        }
    }
}
