package com.duoyou.develop.utils.image;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.duoyou.develop.application.DyApplication;
import com.duoyou.develop.view.ToastHelper;

import java.io.File;

public class FileShareUtil {

    // 调用系統方法分享文件

    public static void shareFile(Context context, File file) {
        if (null == file || !file.exists()) {
            ToastHelper.showShort("文件不存在");
            return;
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        Uri uri;
        // 判断版本大于等于7.0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "项目包名.fileprovider"即是在清单文件中配置的authorities
            uri = FileProvider.getUriForFile(DyApplication.Companion.getApp(), DyApplication.Companion.getApp().getPackageName() + ".DyFileProvider", file);
            // 给目标应用一个临时授权
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);

//        share.setType(getMimeType(file.getAbsolutePath()));//此处可发送多种文件
        share.setType("image/*");//此处可发送多种文件

        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(share, "分享文件"));

    }

// 根据文件后缀名获得对应的MIME类型。

    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        String mime = "*/*";

        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);

            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }
}
