package com.odong.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import com.odong.Constants;
import com.odong.client.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by flamen on 13-12-13 下午4:21.
 */
public class VersionHelper {
    public VersionHelper(Context context){
        this.context = context;
    }
    public void check(int version){
        if(isUpdate(version)){
            showNoticeDlg();
        }
    }
    private void showNoticeDlg(){
        new AlertDialog.Builder(context)
                .setTitle(R.string.dlg_update_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.dlg_update_message)
                .setPositiveButton(R.string.dlg_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDlg();
                    }
                })
                .setNegativeButton(R.string.dlg_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private boolean isUpdate(int version){
        int code = 0;
        try{
            code = context.getPackageManager().getPackageInfo("com.odong.client", 0).versionCode;
        }
        catch (PackageManager.NameNotFoundException e){
            Log.e(Constants.LOG_NAME, "取得版本号出错", e);
        }
        return version > code;
    }



    private void showDownloadDlg(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.download,null);
        progressBar = (ProgressBar)view.findViewById(R.id.downloadProgressBar);
        new AlertDialog.Builder(context)
                .setTitle(R.string.dlg_download_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setView(view)
                .setNegativeButton(R.string.dlg_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cancel = true;
                    }
                }).show();

        new Thread(download).start();
    }

    private void install(){
        File file = new File(savePath, NAME);
        if(!file.exists()){
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://"+file.toString()),  "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    private Runnable download = new Runnable() {
        @Override
        public void run() {
            try{
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    savePath = Environment.getExternalStorageDirectory()+"/download";
                    HttpURLConnection conn = (HttpURLConnection)new URL("http://"+context.getText(R.string.domain)+"/attachments/"+NAME).openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    File file = new File(savePath);
                    if(!file.exists()){
                        if(!file.mkdir()){
                            Log.e(Constants.LOG_NAME,"创建目录"+savePath+"失败");
                        }
                    }

                    FileOutputStream fos = new FileOutputStream(new File(savePath, NAME));
                    int count = 0;
                    byte[] buf = new byte[1024];
                    do{
                        int read = is.read(buf);
                        if(read <=0){
                            handler.sendEmptyMessage(FINISH);
                            break;
                        }
                        count += read;
                        progress = (int)(((float)count/length)*100);
                        handler.sendEmptyMessage(DOWNLOAD);

                        fos.write(buf, 0, read);

                    }
                    while (!cancel);

                    fos.close();
                    is.close();
                }
            }
            catch (IOException e){
                Log.e(Constants.LOG_NAME, "IO出错", e);
            }

            dialog.dismiss();
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DOWNLOAD:
                    progressBar.setProgress(progress);
                    break;
                case FINISH:
                    install();
                    break;
            }
        }
    };

    private boolean cancel;
    private Context context;
    private int progress;
    private ProgressBar progressBar;
    private Dialog dialog;
    private String savePath;
    private final String NAME="portal.apk";
    private final int DOWNLOAD=1;
    private final int FINISH=2;
}
