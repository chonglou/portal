package com.odong.util;

import android.os.Bundle;
import android.util.Log;
import com.odong.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by flamen on 13-12-10 上午11:19.
 */
public class JsonHelper {
    private JsonHelper() {

    }

    public String bundle2json(Bundle bundle) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (String k : bundle.keySet()) {
            map.put(k, bundle.get(k));
        }
        return new JSONObject(map).toString();
    }

    public Bundle json2bundle(String json) {
        Bundle bundle = new Bundle();
        try {
            JSONObject object = new JSONObject(json);
            Iterator it = object.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                bundle.putString(key, object.getString(key));
            }
        } catch (JSONException e) {
            Log.e(Constants.LOG_NAME, "JSON解析出错", e);
        }
        return bundle;
    }


}
