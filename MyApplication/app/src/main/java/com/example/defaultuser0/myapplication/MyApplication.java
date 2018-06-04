package com.example.defaultuser0.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDexApplication;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends MultiDexApplication {
    static int rounded_value = 120;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
    }

    static ImageLoader imageLoader;

    public static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {

            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;

                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_image_not_available)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .cacheInMemory(false)
            .displayer(new RoundedBitmapDisplayer(rounded_value))
            .bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true)
            .build();


    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheExtraOptions(250, 250)
                .tasksProcessingOrder(QueueProcessingType.FIFO).build();

        setImageLoader(ImageLoader.getInstance());
        getImageLoader().init(config);
    }


    public static ImageLoader getImageLoader() {

        return MyApplication.imageLoader;
    }

    public static void setImageLoader(ImageLoader imageLoader) {
        MyApplication.imageLoader = imageLoader;
    }

}
