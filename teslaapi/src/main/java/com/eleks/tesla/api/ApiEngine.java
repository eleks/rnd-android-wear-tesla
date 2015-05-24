package com.eleks.tesla.api;

import android.util.Log;

import com.eleks.tesla.api.auth.AuthCredentials;
import com.eleks.tesla.api.exception.TeslaApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.Map;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.CookieStore;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.config.RequestConfig;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.client.methods.HttpUriRequest;
import ch.boye.httpclientandroidlib.entity.StringEntity;
import ch.boye.httpclientandroidlib.impl.client.BasicCookieStore;
import ch.boye.httpclientandroidlib.impl.client.HttpClientBuilder;
import ch.boye.httpclientandroidlib.impl.client.HttpClients;
import ch.boye.httpclientandroidlib.util.EntityUtils;

/**
 * Created by bogdan.melnychuk on 18.05.2015.
 */
class ApiEngine {
    private CookieStore cookieStore;
    private File dataDir;
    private final HttpClient httpClient;

    public ApiEngine(KeyStore keystore, File file, int k, int l, String userAgent) {
        final HttpClientBuilder httpclientbuilder = HttpClients.custom();
        final RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectionRequestTimeout(k);
        requestConfigBuilder.setSocketTimeout(l);
        requestConfigBuilder.setCookieSpec("best-match");
        httpclientbuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        httpclientbuilder.setUserAgent(userAgent);

        cookieStore = null;
        if (file != null && file.exists() && file.isDirectory()) {
            File cookieStoreFile = new File(file, "cookie.store");
            if (cookieStoreFile.exists() && cookieStoreFile.canRead()) {
                try {
                    ObjectInputStream is = new ObjectInputStream(new FileInputStream(cookieStoreFile));
                    cookieStore = (BasicCookieStore) is.readObject();
                    is.close();
                } catch (Exception e) {
                    Log.e(Config.TAG, "Cookie file can not be read", e);
                    cookieStore = null;
                }
            }
        }
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }
        httpclientbuilder.setDefaultCookieStore(cookieStore);
        httpClient = httpclientbuilder.build();
    }

    public void saveState() {
        if (dataDir != null && dataDir.exists()) {
            try {
                ObjectOutputStream objectoutputstream = new ObjectOutputStream(new FileOutputStream(new File(dataDir, "cookie.store"), false));
                objectoutputstream.writeObject(cookieStore);
                objectoutputstream.close();
            } catch (Exception e) {
                Log.e(Config.TAG, "State can not be saved", e);
            }
        }
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public JSONObject dispatchJSONPostRequest(ApiSpec apiSpec, String requestPath, JSONObject jsonObject, AuthCredentials authCredentials) throws TeslaApiException {
        try {
            final URI uri = new URI(apiSpec.scheme, null, apiSpec.host, apiSpec.port, apiSpec.getPathForRequestPath(requestPath), null, null);
            Log.d(Config.TAG, "Executing request: " + uri.toURL());
            if(jsonObject != null){
                Log.d(Config.TAG, "Params: " + jsonObject.toString());
            }

            final HttpPost httpPost = new HttpPost(uri);
            if (authCredentials != null) {
                apiSpec.authModule.authorizeRequest(httpPost, authCredentials);
            }
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            if (jsonObject != null){
                httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
            }

            return executeRequest(httpPost);
        } catch (UnsupportedEncodingException | URISyntaxException | MalformedURLException e) {
            throw new TeslaApiException(e);
        }
    }

    public JSONObject dispatchGetRequest(ApiSpec apiSpec, String requestPath, Map<String, String> paramsMap, AuthCredentials authcredentials) throws TeslaApiException {
        try {
            String params = null;
            if (paramsMap != null) {
                final StringBuilder builder = new StringBuilder();
                for (Map.Entry<String, String> e : paramsMap.entrySet()) {
                    builder.append(String.format("%s=%s", e.getKey(), e.getValue()));
                    builder.append("&");
                }
                builder.setLength(builder.length() - 1);
                params = builder.toString();
            }

            final URI uri = new URI(apiSpec.scheme, null, apiSpec.host, apiSpec.port, apiSpec.getPathForRequestPath(requestPath), params, null);
            Log.d(Config.TAG, "Executing request: " + uri.toURL());

            final HttpUriRequest get = new HttpGet(uri);
            apiSpec.authModule.authorizeRequest(get, authcredentials);
            return executeRequest(get);
        } catch (IOException | URISyntaxException e) {
            throw new TeslaApiException(e);
        }


    }

    private JSONObject executeRequest(HttpUriRequest request) throws TeslaApiException {
        try {
            final HttpResponse httpResponse = httpClient.execute(request);
            Log.d(Config.TAG, "Status: " + httpResponse.getStatusLine());

            final int status = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.isSuccessful(status)) {
                final HttpEntity entity = httpResponse.getEntity();
                final String responseString = EntityUtils.toString(entity, "UTF-8");
                Log.d(Config.TAG, "Response: " + responseString);
                return new JSONObject(responseString);
            } else {
                throw TeslaApiException.fromCode(status);
            }
        } catch (IOException | JSONException e) {
            throw new TeslaApiException(e);
        }
    }
}
