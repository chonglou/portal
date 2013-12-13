package com.odong.client;

import android.app.Activity;
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
import com.odong.model.RSSFeed;
import com.odong.model.RSSItem;
import com.odong.util.RSSHandler;
import com.odong.util.RSSHelper;


/**
 * Created by flamen on 13-12-9.
 */
public class RssFeedA extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        new Thread(runnable).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            if (data != null) {
                RSSFeed feed = (RSSFeed) data.get("feed");
                Log.d(Constants.LOG_NAME, "请求结果:" + feed.getSize());
                show(feed);
            } else {
                Log.e(Constants.LOG_NAME, "RSS数据为空");
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            RSSFeed feed = getFeed("http://" + getText(R.string.domain) + "/rss.xml");
            data.putSerializable("feed", feed);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };


    private RSSFeed getFeed(String url) {
        RSSFeed feed;
        try {
            RSSHandler handler = new RSSHandler();
            RSSHelper.get().parse(url, handler);
            feed = handler.getFeed();
        } catch (Exception e) {
            Log.i(Constants.LOG_NAME, "抓取RSS", e);
            feed = new RSSFeed();
        }
        return feed;
    }

    private Intent createIntent() {
        return new Intent(this, RssItemA.class);
    }

    private void show(final RSSFeed feed) {
        ListView lv = (ListView) findViewById(R.id.feedList);
        if (feed == null) {
            setTitle(R.string.lbl_rss_error);
            return;
        }

        lv.setAdapter(new SimpleAdapter(
                this,
                feed.getData(),
                android.R.layout.simple_list_item_2, new String[]{RSSItem.TITLE, RSSItem.PUB_DATE},
                new int[]{android.R.id.text1, android.R.id.text2}));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = createIntent();
                Bundle bundle = new Bundle();
                RSSItem item = feed.getItem(position);
                bundle.putString("title", item.getTitle());
                bundle.putString("description", item.getDescription());
                bundle.putString("link", item.getLink());
                bundle.putString("pubDate", item.getPubDate());
                intent.putExtra(Constants.RSS_2_ITEM, bundle);
                startActivityForResult(intent, 0);
            }
        });

        lv.setSelection(0);
    }


}