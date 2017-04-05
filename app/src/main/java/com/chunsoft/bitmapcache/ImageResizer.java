package com.chunsoft.bitmapcache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Developer：chunsoft on 2017/3/27 15:22
 * Email：chun_soft@qq.com
 * Content：图片压缩，降低OOM概率
 */

public class ImageResizer {
    private static final String TAG = "ImageResizer";

    public ImageResizer() {

    }
    public Bitmap decodeSampledBitmapFromResource(Resources res,
                                                  int resId, int reqWidth, int reqheight) {
        // 1.将BitmapFactory.Options的inJustDecodeBounds参数设为true并加载图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId,options);
        // 2.从BitmapFactory.Options中取出图片的原始宽高信息，它们对应于outWidth和outHeight参数

        // 3.根据采样率的规则并结合目标View的所需大小计算出采样率inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqheight);
        // 4.将BitmapFactory.Options的inJustDecodeBounds参数设置为false，然后重新加载图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId, options);
    }


    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd,
                                                        int reqWidth,int reqHeight) {
        // 1.将BitmapFactory.Options的inJustDecodeBounds参数设为true并加载图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        // 2.从BitmapFactory.Options中取出图片的原始宽高信息，它们对应于outWidth和outHeight参数

        // 3.根据采样率的规则并结合目标View的所需大小计算出采样率inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 4.将BitmapFactory.Options的inJustDecodeBounds参数设置为false，然后重新加载图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);

    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqheight) {
        if (reqheight == 0 || reqWidth == 0) {
            return 1;
        }
        // 从BitmapFactory.Options中取出图片的原始宽高信息，它们对应于outWidth和outHeight参数
        final int height = options.outHeight;
        final int width = options.outWidth;

        Log.e(TAG,"origin, w=" + width +" h=" + height);

        int inSampleSize = 1;

        if (height > reqheight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqheight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.e(TAG, "sampleSize:" + inSampleSize);
        return inSampleSize;
    }
}
