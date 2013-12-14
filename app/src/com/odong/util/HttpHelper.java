package com.odong.util;

import android.util.Log;
import com.odong.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by flamen on 13-12-13 下午3:23.
 */
public class HttpHelper {
    public static String get(String url) {
        try {
            HttpGet get = new HttpGet(url);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (IOException e) {
            Log.e(Constants.LOG_NAME, "HTTP CLIENT出错", e);
        }
        return null;
    }

    private HttpHelper() {
    }

}
