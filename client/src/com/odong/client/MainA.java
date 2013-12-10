package com.odong.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
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

    @Override
    public void onBackPressed() {
        exit();
    }


    private Intent createIntent(Class<?> clazz) {
        return new Intent(this, clazz);
    }

    private View.OnClickListener createListener(final Class<?> clazz) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = createIntent(clazz);
                startActivityForResult(intent, 0);
            }
        };
    }

    private void initEvents() {
        SparseArray<View.OnClickListener> events = new SparseArray<View.OnClickListener>();
        events.put(R.id.mainRss, createListener(RssFeedA.class));
        events.put(R.id.mainVideo, createListener(VideoA.class));
        events.put(R.id.mainBook, createListener(BookA.class));
        events.put(R.id.mainExit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        events.put(R.id.mainAboutMe, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutMe();
            }
        });

        for (int i = 0; i < events.size(); i++) {
            Button btn = (Button) findViewById(events.keyAt(i));
            btn.setOnClickListener(events.valueAt(i));
        }

    }

    private void aboutMe() {
        new AlertDialog.Builder(this).
                setTitle(R.string.dlg_aboutMe_title).
                setIcon(android.R.drawable.ic_dialog_info).
                setMessage(R.string.dlg_aboutMe_message).
                setPositiveButton(R.string.dlg_ok, null).
                show();
    }

    private void exit() {
        new AlertDialog.Builder(this).
                setTitle(R.string.dlg_exit_title).
                setIcon(android.R.drawable.ic_dialog_alert).
                setPositiveButton(R.string.dlg_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.dlg_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
