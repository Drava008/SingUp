package com.example.user.singup;

import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class QueryActivity extends AppCompatActivity {

    ArrayList<String> StringArray = new ArrayList<String>();

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        listView = (ListView)findViewById(R.id.listview);

        new TheTask().execute("123456789");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                ListView listView = (ListView) arg0;

                String del = StringArray.get(arg2);
                Log.d("arg2=",del);

                new TheDelete().execute(del);
            }
        });

    }




    class TheTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            listView.setAdapter(null);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String phoneNumber = arg0[0];

            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?phone=" + URLEncoder.encode(phoneNumber, "UTF-8");
                link = "http://140.130.36.246/php/message.php" + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                Log.d("result=",result);


                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }



        @Override
        protected void onPostExecute(String result) {

            List<HashMap<String , String>> list = new ArrayList<>();

            String jsonStr = result;

            Log.d("result=",result);


            if (jsonStr != null) try {

                JSONArray new_array = new JSONArray(result);

                for (int i = 0, count = new_array.length(); i < count; i++) {
                    try {

                        JSONObject jsonObject = new_array.getJSONObject(i);

                        String msg = jsonObject.getString("msg").toString();
                        String time = jsonObject.getString("created_data").toString();
                        String msg_id = jsonObject.getString("msg_id").toString();

                        HashMap<String , String> hashMap = new HashMap<>();

                        hashMap.put("msg" , msg);
                        hashMap.put("time" , time);

                        StringArray.add(msg_id);

                        list.add(hashMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ListAdapter listAdapter = new SimpleAdapter(
                        QueryActivity.this,
                        list,
                        android.R.layout.simple_list_item_2 ,
                        new String[]{"msg" , "time"} ,
                        new int[]{android.R.id.text1 , android.R.id.text2});

                listView.setAdapter(listAdapter);

            } catch (JSONException e) {

                e.printStackTrace();

                Toast.makeText(QueryActivity.this, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();

            }
            else {

                Toast.makeText(QueryActivity.this, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    class TheDelete extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            String msg_id = arg0[0];

            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?msg_id=" + URLEncoder.encode(msg_id, "UTF-8");
                link = "http://140.130.36.246/php/delete.php" + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                Log.d("result=",result);

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
                    if (query_result.equals("SUCCESS")) {
                        Toast.makeText(QueryActivity.this, "資料刪除成功", Toast.LENGTH_SHORT).show();
                        new TheTask().execute("123456789");
                    } else if (query_result.equals("FAILURE")) {
                        Toast.makeText(QueryActivity.this, "資料刪除失敗", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(QueryActivity.this, "無法連接伺服器", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(QueryActivity.this, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(QueryActivity.this, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

