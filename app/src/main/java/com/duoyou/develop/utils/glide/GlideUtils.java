package com.duoyou.develop.utils.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.duoyou_cps.appstore.R;

import java.io.File;


public class GlideUtils {
    private static RequestOptions requestOptions;

    public static RequestOptions getRequestOptions() {
        if (requestOptions == null) {
            requestOptions = new RequestOptions();
            requestOptions = GlideUtils.requestOptions.error(R.drawable.lib_default_bg).centerCrop();
        }
        return requestOptions;

    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        loadImage(context, url, imageView, getDefaultRequestOptions());
    }

    public static void loadNoHolderImage(Context context, String url, ImageView imageView) {
        loadImage(context, url, imageView, getRequestOptions());
    }

    public static void loadImage(Context context, String url, ImageView imageView, int rid) {
        RequestOptions requestOptions = getRequestOptions();
        requestOptions.placeholder(rid).error(rid).centerCrop();
        loadImage(context, url, imageView, requestOptions);
    }

    public static void loadImageFitXy(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = getRequestOptions();
        requestOptions.placeholder(R.drawable.lib_default_bg).error(R.drawable.lib_default_bg);
        loadImage(context, url, imageView, requestOptions);
    }


    public static void loadImage(Context context, String url, ImageView imageView, RequestListener<Drawable> requestListener) {
        loadImage(context, url, imageView, getDefaultRequestOptions(), requestListener);
    }

    public static void loadImage(Context mContext, String path, ImageView mImageView, RequestOptions requestOptions) {
        if (isDestroy(mContext) || mImageView == null) {
            return;
        }
        path = formatUrl(path);
        Glide.with(mContext).load(path).apply(requestOptions).into(mImageView);
    }

    public static void loadImage(Context mContext, String path, ImageView mImageView, RequestOptions requestOptions, RequestListener<Drawable> requestListener) {
        if (isDestroy(mContext)) {
            return;
        }
        Glide.with(mContext).load(path).apply(requestOptions).addListener(requestListener).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

            }


        });
    }

    public static void loadImage(Context mContext, Object path, ImageView mImageView) {
        if (isDestroy(mContext)) {
            return;
        }
        Glide.with(mContext).load(path).apply(getDefaultRequestOptions()).into(mImageView);
    }

    public static void loadImage(Context mContext, int rid, ImageView imageView) {
        if (isDestroy(mContext)) {
            return;
        }
//        imageView.setImageResource(rid);
        Glide.with(mContext).load(rid).into(imageView);
    }

    public static void loadCircleImageView(Context context, String url, ImageView imageView) {
        if (isDestroy(context)) {
            return;
        }
        url = formatUrl(url);
        int dp = SizeUtils.px2dp(imageView.getWidth());
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = new RequestOptions()
                .transform(new GlideRoundTransform(context, dp));
//                .placeholder(R.drawable.default_bg)
//                .error(R.drawable.default_bg);

        Glide.with(context).load(url).apply(options).into(imageView);
    }
    public static void loadImageAddHttp(Context context, String url, ImageView imageView) {
        url = formatUrl(url);
        loadImage(context, url, imageView, getDefaultRequestOptions());
    }

    public static void loadRoundImage(Context context, String url, ImageView imageView) {
        loadRoundImage(context, url, imageView, 6);
    }

    public static void loadAsGifImage(Context context, @DrawableRes Integer drawId, ImageView iv) {
        if (isDestroy(context)) {
            return;
        }
        Glide.with(context).asGif().load(drawId).into(iv);
    }

    public static void loadRoundImage(Context context, String url, ImageView imageView, int dp) {
        if (isDestroy(context)) {
            return;
        }
        url = formatUrl(url);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = new RequestOptions()
                .transform(new GlideRoundTransform(context, dp))
                .placeholder(R.drawable.lib_default_bg)
                .error(R.drawable.lib_default_bg);

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadRoundImageNoHolder(Context context, String url, ImageView imageView, int dp) {
        if (isDestroy(context)) {
            return;
        }
        url = formatUrl(url);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = new RequestOptions()
                .transform(new GlideRoundTransform(context, dp));

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void getThumb(Context context, Uri uri, ImageView imageView) {
        if (isDestroy(context)) {
            return;
        }
        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .apply(new RequestOptions().centerCrop())
                .into(imageView);

    }

    public static String formatUrl(String url) {
        if (url == null || "".equals(url)) {
            return url;
        }
        File file = new File(url);
        if (file.length() < 10 && !url.startsWith("http")) {
            return "http:" + url;
        }
        return url;
    }

    public static RequestOptions getDefaultRequestOptions() {
        RequestOptions requestOptions = new RequestOptions();

        requestOptions.placeholder(R.drawable.lib_default_bg).error(R.drawable.lib_default_bg).centerCrop();
        return requestOptions;
    }

    public static class CenterCropRoundCornerTransform extends CenterCrop {

        private int radius = 4;

        public CenterCropRoundCornerTransform(int radius) {
            this.radius = radius;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                   int outWidth, int outHeight) {
            Bitmap transform = super.transform(pool, toTransform, outWidth, outHeight);
            return roundCrop(pool, transform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null)
                return null;
            Bitmap result = pool.get(source.getWidth(), source.getHeight(),
                    Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
                        Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP,
                    BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

    }


    public static boolean isDestroy(Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            Activity mActivity = (Activity) context;
            return mActivity.isFinishing() || mActivity.isDestroyed();
        }
        return true;
    }

}
