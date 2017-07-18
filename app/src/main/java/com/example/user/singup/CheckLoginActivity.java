package com.example.user.singup;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CheckLoginActivity extends AsyncTask<String, Void, String> {

            private Context context;

            public CheckLoginActivity(Context context) {
        this.context = context;
    }

            protected void onPreExecute() {

    }

            @Override
    protected String doInBackground(String... arg0) {
        String passWord = arg0[0];
        String phoneNumber = arg0[1];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
            data = "?phone=" + URLEncoder.encode(phoneNumber, "UTF-8");
            data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
            link = "http://140.130.36.246/php/checkLogin.php" + data;
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = bufferedReader.readLine();
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

            @Override
    protected void onPostExecute(String result) {
        String jsonStr = result;
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("query_result");
                String username = jsonObj.getString("username");
                if (query_result.equals("SUCCESS")) {
                    Toast.makeText(context, username + "登入成功", Toast.LENGTH_SHORT).show();
                } else if (query_result.equals("FAILURE")) {
                    Toast.makeText(context, "會員登入失敗", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "無法連接伺服器", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }
}