package com.lk.app.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by lavakush.v on 14-10-2015.
 */
public class RestProcessor {
    public static final RestProcessor Instance = new RestProcessor();
    private static final String TAG = RestProcessor.class.getSimpleName();
    private static RequestQueue mRequestQueue = null;
    private Context mContext = null;

    public void initialize(Context context) {
        mContext = context;
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext, new HurlStack(null, newSslSocketFactory()));
        }
    }

    private SSLSocketFactory newSslSocketFactory() {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            //KeyStore trusted = KeyStore.getInstance("BKS");
            // Get the resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            final InputStream keyStore = mContext.getAssets().open("myBKS.bks");
            /*try {
                // Initialize the keystore with the provided trusted certificates
                // Provide the password of the keystore
                trusted.load(keyStore, KEYSTORE_PASSWORD.toCharArray());
            } finally {
                keyStore.close();
            }
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);*/
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new SX509TrustManager(keyStore, KEYSTORE_PASSWORD)}, null);
            javax.net.ssl.SSLSocketFactory sf = context.getSocketFactory();
            return sf;
        } catch (Exception e) {
            Log.e(TAG, "Exception getting SSLSocketFactory : " + e.getMessage());
            throw new AssertionError(e);
        }
    }

    private void verify() {
        if (mContext == null || mRequestQueue == null) {
            throw new IllegalArgumentException("RestProcessor.Instance.initialize(context) should be called first.");
        }
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        verify();
        req.setTag(StringUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        verify();
        req.setTag(TAG);
        mRequestQueue.add(req);
    }

    public void cancelPendingRequests(Object tag) {
        verify();
        if (mRequestQueue != null) {
            final Object requestTag = StringUtils.isEmpty(String.valueOf(tag)) ? TAG : tag;
            mRequestQueue.cancelAll(tag);
        }
    }

    private JSONObject parseNetworkResponse(NetworkResponse response) {
        JSONObject jsonObject = null;
        try {
            final String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            jsonObject = new JSONObject(jsonString);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException :" + e.getMessage());
        } catch (JSONException je) {
            Log.e(TAG, "JSONException :" + je.getMessage());
        }
        return jsonObject;
    }

    private String getEncodedParams(HashMap<String, String> params) {
        final StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append(AND_APPEND);
                result.append(URLEncoder.encode(entry.getKey(), PROTOCOL_CHARSET));
                result.append(EQUALS_APPEND);
                result.append(URLEncoder.encode(entry.getValue(), PROTOCOL_CHARSET));
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Exception while encoding params");
        }
        Log.d(TAG, "encoded params : " + result.toString());
        return result.toString();
    }

    public void checkForUpdates(final Listener listener) {
        final StringRequest request = new StringRequest(requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse : " + error.getMessage());
            }
        });
        addToRequestQueue(request);
    }
}
