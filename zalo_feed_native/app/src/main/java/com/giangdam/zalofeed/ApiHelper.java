package com.giangdam.zalofeed;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cpu11326-local on 19/03/2018.
 */

public class ApiHelper {

    public static void getJsonData(String url, final ApiCallback callback ) {

        new ApiTask(new ApiCallback() {
            @Override
            public void getApiDone(String result) {
                callback.getApiDone(result);
            }

            @Override
            public void getApiFailed() {
                callback.getApiFailed();
            }
        }).execute(url);
    }


    public interface ApiCallback {
        void getApiDone(String result);
        void getApiFailed();
    }

    private static class ApiTask extends AsyncTask<String, String, String> {
        ApiCallback callback;

        ApiTask(ApiCallback callback){
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Open InputStream
                InputStream stream = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                connection.disconnect();
                reader.close();
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            if(jsonResult != null) {
                callback.getApiDone(jsonResult);
            } else {
                callback.getApiFailed();
            }
        }
    }
}
