package com.odong.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.odong.Constants;
import com.odong.model.Feed;
import com.odong.model.Item;
import com.odong.util.HttpHelper;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by flamen on 13-12-10 下午12:31.
 */
public class ListFeedA extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);

        progressDlg = new ProgressDialog(this);
        progressDlg.setMessage(getText(R.string.lbl_wait));
        progressDlg.show();

        new Thread(runnable).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            if (data != null) {
                Feed feed = (Feed) data.getSerializable("feed");
                if (feed != null) {
                    Log.d(Constants.LOG_NAME, "请求结果" + feed.getType() + " " + feed.getSize());
                    show(feed);
                    return;
                }
            }
            Log.e(Constants.LOG_NAME, "数据为空");

        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            Feed feed = getFeed();
            progressDlg.cancel();
            data.putSerializable("feed", feed);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    private void show(final Feed feed) {
        if (feed == null) {
            return;
        }
        switch (feed.getType()) {
            case BOOK:
                setTitle(R.string.lbl_book);
                break;
            case VIDEO:
                setTitle(R.string.lbl_video);
                break;
            default:
                Log.e(Constants.LOG_NAME, "未知的类型" + feed.getType());
                return;
        }

        ListView lv = (ListView) findViewById(R.id.feedList);
        lv.setAdapter(new SimpleAdapter(
                this,
                feed.getData(),
                android.R.layout.simple_list_item_2,
                new String[]{Item.NAME, Item.DETAILS},
                new int[]{android.R.id.text1, android.R.id.text2})
        );

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                Bundle data = new Bundle();
                Item item = feed.getItem(position);
                data.putString("title", item.getName());
                data.putString("url", item.getUrl());

                switch (feed.getType()) {
                    case VIDEO:
                        intent = createIntent(VideoItemA.class);
                        intent.putExtra(Constants.LIST_2_VIDEO, data);
                        break;
                    case BOOK:
                        intent = createIntent(BookItemA.class);
                        intent.putExtra(Constants.LIST_2_BOOK, data);
                        break;
                    default:
                        return;
                }
                startActivityForResult(intent, 0);

            }
        });

    }

    private Intent createIntent(Class<?> clazz) {
        return new Intent(this, clazz);
    }

    private Feed getFeed() {

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.MAIN_2_LIST);
        if (bundle != null) {
            try {
                String type = bundle.getString("type");
                if (type != null) {
                    Log.d(Constants.LOG_NAME, "http://" + getText(R.string.domain) + "/" + type.toLowerCase());
                    String json = HttpHelper.get("http://" + getText(R.string.domain) + "/" + type.toLowerCase());
                    Log.d(Constants.LOG_NAME, json);
                    if (json != null) {
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
                        return feed;
                    }
                }
            } catch (Exception e) {
                Log.e(Constants.LOG_NAME, "取数据出错", e);
            }
        }
        return null;
    }


    private ProgressDialog progressDlg;

}