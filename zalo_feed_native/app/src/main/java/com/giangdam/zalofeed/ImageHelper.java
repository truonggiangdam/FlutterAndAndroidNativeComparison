package com.giangdam.zalofeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cpu11326-local on 19/03/2018.
 */

public class ImageHelper {

    public static void displayImage(String url, ImageView imageView, boolean isCircle) {
        //set placeholder for imageView
        imageView.setImageResource(R.drawable.place_holder);
        new GetBitmapTask(imageView, isCircle).execute(url);
    }

    private static class GetBitmapTask extends AsyncTask<String, Void, Bitmap> {
        WeakReference<ImageView> imageViewReference;
        boolean isCircle;

        GetBitmapTask(ImageView imageView, boolean isCircle) {
            imageViewReference= new WeakReference<>(imageView);
            this.isCircle = isCircle;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL imageURL = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                connection.setInstanceFollowRedirects(true);

                // Open InputStream
                InputStream is = connection.getInputStream();

                if(isCircle) {
                    return getCircularBitmapWithWhiteBorder(BitmapFactory.decodeStream(is), 0, Color.WHITE);
                }

                return BitmapFactory.decodeStream(is);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
                                                              int borderWidth, int borderColor) {
            if (bitmap == null || bitmap.isRecycled()) {
                return null;
            }

            final int width = bitmap.getWidth() + borderWidth;
            final int height = bitmap.getHeight() + borderWidth;

            Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);

            Canvas canvas = new Canvas(canvasBitmap);
            float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
            canvas.drawCircle(width / 2, height / 2, radius, paint);
            paint.setShader(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);
            paint.setStrokeWidth(borderWidth);
            canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
            return canvasBitmap;
        }

        private Bitmap scaleBitmap(Bitmap bitmap, WeakReference<ImageView> imageViewReference) {
            return Bitmap.createScaledBitmap(bitmap,
                    imageViewReference.get().getWidth(), imageViewReference.get().getHeight(), false);
        }


        private int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(imageViewReference.get() != null) {
                imageViewReference.get().setImageBitmap(bitmap);
            }
        }
    }
}
