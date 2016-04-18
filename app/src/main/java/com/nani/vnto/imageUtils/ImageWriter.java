package com.nani.vnto.imageUtils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageWriter {

    private static File getImagesDirectory() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PlantSnapp");

        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        return mediaStorageDir;
    }

    public static File getOutputMediaFile() {
        File mediaStorageDir = getImagesDirectory();

        if (mediaStorageDir == null) {
            return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    public static File copyResizedFileToImagesDirectory(int maxEdgeSize, File file) {
        File directory = getImagesDirectory();
        File outFile = null;

        if (directory == null) {
            return null;
        }

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            outFile = new File(directory.getAbsolutePath() + file.getName());
            in = new FileInputStream(file.getAbsolutePath());
            out = new FileOutputStream(outFile);

            byte[] buffer = new byte[1024];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {

                if (in != null) {
                    in.close();
                    in = null;
                }

                if (out != null) {
                    out.flush();
                    out.close();
                    out = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap bitmap = ImageResizer.resize(outFile, maxEdgeSize);

        return writeToFile(bitmap, outFile);
    }

    public static File writeToFile(Bitmap image, File file) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            fos.write(bytes.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return null;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        } finally {

            try {

                if (fos != null) {
                    fos.flush();
                    fos.close();
                    fos = null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }
}
