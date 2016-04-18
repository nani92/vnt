package com.nani.vnto.imageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class ImageDecoder {

    public static Bitmap decodeFile(File bitmapFile, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int requestedWidth, int requestedHeight) {

        if (requestedWidth == -1) {
            requestedWidth = options.outWidth;
        }

        if (requestedHeight == -1) {
            requestedHeight = options.outHeight;
        }

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requestedHeight || width > requestedWidth) {

            if (width > height) {
                inSampleSize = Math.round((float) height / (float) requestedHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) requestedWidth);
            }
        }

        return inSampleSize;
    }
}
