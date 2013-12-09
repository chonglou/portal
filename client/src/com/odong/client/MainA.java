package com.odong.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainA extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initEvents();
    }

    private Intent createIntent(Class<?> clazz){
        return new Intent(this, clazz);
    }

    private void initEvents(){
        Button btn = (Button) findViewById(R.id.mainRss);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = createIntent(RssFeedA.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
