package com.odong.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.odong.Constants;
import com.odong.model.Feed;
import com.odong.model.Item;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by flamen on 13-12-10 下午12:31.
 */
public class ListFeedA extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        Feed feed = getFeed();
        if (feed != null) {
            switch (feed.getType()) {
                case BOOK:
                    setTitle(R.string.lbl_book);
                    break;
                case VIDEO:
                    setTitle(R.string.lbl_video);
                    break;
            }

            initList(feed);
        }
    }

    private void initList(Feed feed) {
        ListView lv = (ListView) findViewById(R.id.feedList);
        lv.setAdapter(new SimpleAdapter(
                this,
                feed.getData(),
                android.R.layout.simple_list_item_2,
                new String[]{Item.NAME, Item.DETAILS},
                new int[]{android.R.id.text1, android.R.id.text2})
        );
    }

    private Feed getFeed() {

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.MAIN_2_LIST);
        if (bundle != null) {
            String type = bundle.getString("type");
            String json = httpGet("http://" + getText(R.string.domain) + "/" + type);
            if (json != null) {
                try {

                    Feed feed = new Feed(Feed.Type.valueOf(type));
                    JSONArray array = new JSONArray(json);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Item item = new Item();
                        item.setUrl(object.getString("url"));
                        item.setName(object.getString("name"));
                        item.setDetails(object.getString("details"));
                        feed.addItem(item);
                    }
                } catch (JSONException e) {
                    Log.e(Constants.LOG_NAME, "JSON解析出错", e);
                }

            }
        }
        return null;
    }


    private String httpGet(String url) {
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

}