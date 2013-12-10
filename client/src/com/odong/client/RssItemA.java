package com.odong.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by flamen on 13-12-9.
 */
public class RssItemA extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_item);
        Intent intent = getIntent();
        String content = "程序出错";
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("android.intent.extra.portal.rssItem");
            if (bundle != null) {
                content = bundle.getString("title") + "\n\n" +
                        bundle.getString("pubDate") + "\n\n" +
                        bundle.getString("description") +
                        "\n\n详细信息请访问以下网址：\n" + bundle.getString("link");

            }
        }

        TextView tv = (TextView) findViewById(R.id.rssItemContent);
        tv.setText(content);

        Button back = (Button) findViewById(R.id.rssItemBack);
        back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}