package com.odong.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.odong.Constants;

/**
 * Created by flamen on 13-12-9.
 */
public class BookItemA extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(Constants.LIST_2_BOOK);
            if (bundle != null) {
                setTitle(bundle.getString("title"));
                //TODO 显示书本内容
            }
        }
    }
}