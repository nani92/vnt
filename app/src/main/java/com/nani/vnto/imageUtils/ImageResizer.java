package com.nani.vnto.imageUtils;

import android.graphics.Bitmap;

import java.io.File;

public class ImageResizer {

    public static Bitmap resize(File original, int maxEdgeLength) {
        int width = maxEdgeLength;
        int height = maxEdgeLength;
        Bitmap sampledSrcBitmap = ImageDecoder.decodeFile(original, maxEdgeLength, maxEdgeLength);
        int sourceWidth = sampledSrcBitmap.getWidth();
        int sourceHeight = sampledSrcBitmap.getHeight();
        ResizeMode mode = calculateResizeMode(sourceWidth, sourceHeight);

        if (mode == ResizeMode.FIT_TO_WIDTH) {
            height = calculateHeight(sourceWidth, sourceHeight, width);
        } else if (mode == ResizeMode.FIT_TO_HEIGHT) {
            width = calculateWidth(sourceWidth, sourceHeight, height);
        }

        return Bitmap.createScaledBitmap(sampledSrcBitmap, width, height, true);
    }

    private static ResizeMode calculateResizeMode(int width, int height) {

        if (ImageOrientation.getOrientation(width, height) == ImageOrientation.LANDSCAPE) {
            return ResizeMode.FIT_TO_WIDTH;
        }

        return ResizeMode.FIT_TO_HEIGHT;
    }

    private static int calculateWidth(int originalWidth, int originalHeight, int height) {
        return (int) Math.ceil(originalWidth / ((double) originalHeight/height));
    }

    private static int calculateHeight(int originalWidth, int originalHeight, int width) {
        return (int) Math.ceil(originalHeight / ((double) originalWidth/width));
    }
}
