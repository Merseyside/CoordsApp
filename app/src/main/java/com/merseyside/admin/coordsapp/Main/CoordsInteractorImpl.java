package com.merseyside.admin.coordsapp.Main;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import com.merseyside.admin.coordsapp.Application;
import com.merseyside.admin.coordsapp.Constants;
import com.merseyside.admin.coordsapp.MyUtils;
import com.merseyside.admin.coordsapp.Point;
import com.merseyside.admin.coordsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by ivan_ on 22.08.2017.
 */

public class CoordsInteractorImpl implements CoordsInteractor {
    @Inject Context context;
    private OnFinishedResult listener;

    CoordsInteractorImpl(OnFinishedResult listener) {
        Application.getComponent().inject(this);
        this.listener = listener;
    }

    @Override
    public void getCoords(int count) {
        if (isOnline()) {
            NetworkAsyncTask asyncTask = new NetworkAsyncTask(Constants.URL, count);
            try {
                asyncTask.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        else listener.onError(context.getResources().getString(R.string.no_internet));
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public JSONObject getJSONobject(String url_str, int count) throws IOException, JSONException, CertificateException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");
        AssetManager assManager = context.getAssets();
        InputStream is = null;
        try {
            is = assManager.open("cert.cer");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert is != null;
        InputStream caInput = new BufferedInputStream(is);
        Certificate ca = null;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
            keyStore = KeyStore.getInstance(keyStoreType);

        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        String parameters = "count=" + count + "&version=1.1";

        URL url = new URL(url_str);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(MyUtils.getSSLSocketFactory());
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
        conn.setRequestProperty("Host", "demo.bankplus.ru");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        DataOutputStream os = new DataOutputStream(conn.getOutputStream ());
        os.writeBytes(parameters);
        os.flush();
        os.close();

        try {
            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                JSONObject jsonObject = MyUtils.getJsonObject(in);
                in.close();
                return jsonObject;
            } else {
            }
        } finally {
            conn.disconnect();
        }
        return null;
    }

    public void getPointsFromJSON(JSONObject json) {
        assert json != null;
        try {
            int result = json.getInt("result");
            switch (result) {
                case (0):
                    ArrayList<Point> points = new ArrayList<>();
                    json = json.getJSONObject("response");
                    JSONArray array = json.getJSONArray("points");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject point = array.getJSONObject(i);
                        float x = Float.valueOf(point.getString("x"));
                        float y = Float.valueOf(point.getString("y"));
                        points.add(new Point(x, y));
                    }
                    Collections.sort(points);
                    listener.onSuccess(points);
                    break;

                case (-100):
                    listener.onError(context.getResources().getString(R.string.wrong_parameters));
                    break;

                default:
                    json = json.getJSONObject("response");
                    listener.onError(MyUtils.base64ToString(json.getString("message")));
                    break;
            }
        }
        catch (JSONException | UnsupportedEncodingException ignored) {}
    }

    private class NetworkAsyncTask extends AsyncTask<Void, Void, JSONObject> {
        private String url;
        private int count;

        NetworkAsyncTask(String url, int count) {
            this.url = url;
            this.count = count;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                return getJSONobject(url, count);
            } catch (IOException | JSONException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            if (object != null) getPointsFromJSON(object);
            else listener.onError(context.getResources().getString(R.string.unknown_error));
        }
    }
}
