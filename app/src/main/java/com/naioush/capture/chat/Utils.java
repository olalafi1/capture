package com.naioush.capture.chat;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.naioush.capture.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Utils {
    final static String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";

    // Takes Strings like "[a, b, c]"
    @NonNull
    public static ArrayList<String> parseStringToList(String s) {
        String tempString = s;
        if (s.contains("[")) {
            tempString = tempString.replace("[", "");
            tempString = tempString.replace("]", "");
            tempString = tempString.replace(" ", "");
            return new ArrayList<>(Arrays.asList(tempString.split(",")));
        }
        return new ArrayList<>(Collections.singletonList(s));

    }



    public static String getRealPathFromURI(Context context , Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public   static File getFile(String path) {
        return new File(path);
    }





    public static void loadImage(final Context context, final String url, final ImageView image   ) {
        if (url == null  || url.isEmpty())
            return;



        Log.d("PicassoImage",url);
        Picasso.get().load(url)
                .placeholder(R.drawable.img)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(500,500)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("PicassoImage", "Done Load Image " );
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("PicassoImage", "onError "  +url);

                        Picasso.get().load(url)
                                .placeholder(R.drawable.img)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .resize(500,500)
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(image);
                    }
                });


    }


    public static void loadImage(final Context context, final String url, final ImageView image  , LoadImage loadImage  ) {
        if (url == null  || url.isEmpty())
            return;



        Log.d("PicassoImage",url);
        Picasso.get().load(url)
                .placeholder(R.drawable.img)
                .networkPolicy(NetworkPolicy.OFFLINE)
                 .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("PicassoImage", "Done Load Image " );
                        loadImage.onLoadImage(true);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("PicassoImage", "onError "  +url);

                        Picasso.get().load(url)
                                .placeholder(R.drawable.img)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                 .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(image, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        loadImage.onLoadImage(true);
                                    }

                                    @Override
                                    public void onError(Exception e) {   }
                                });
                    }
                });


    }



    public interface LoadImage{
        void onLoadImage(boolean x);
    }




    public static String resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        final int MAX_IMAGE_SIZE = 700 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }
        //return the path of resized and compressed file
        return context.getCacheDir() + fileName;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }






}