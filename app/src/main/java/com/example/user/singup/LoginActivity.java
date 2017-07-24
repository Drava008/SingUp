package com.example.user.singup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class LoginActivity extends AppCompatActivity {

    CheckBox checkBox1,checkBox2;
    private EditText etPhone , etPassword;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox)findViewById(R.id.checkBox2);

        SharedPreferences setting = getSharedPreferences("login",MODE_PRIVATE);

        checkBox1.setChecked(setting.getBoolean("b1",false));
        checkBox2.setChecked(setting.getBoolean("b2",false));

        if (checkBox1.isChecked() == true){
            etPhone.setText(setting.getString("etPhone", ""));
        }
        if (checkBox2.isChecked() == true){
            etPassword.setText(setting.getString("etPassword", ""));
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        settingcommit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        settingcommit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        settingcommit();
    }

    public void settingcommit(){
        SharedPreferences setting = getSharedPreferences("login",MODE_PRIVATE);

        setting.edit().putBoolean("b1",checkBox1.isChecked()).commit();
        setting.edit().putBoolean("b2",checkBox2.isChecked()).commit();
        setting.edit().putString("etPhone",etPhone.getText().toString()).commit();
        setting.edit().putString("etPassword",etPassword.getText().toString()).commit();
    }
    public void login(View v) {

        if(etPhone.getText().toString().equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("登入失敗")
                    .setMessage("密碼")
                    .setPositiveButton("ok",null)
                    .show();

            etPhone.setFocusableInTouchMode(true);
            etPhone.requestFocus();

        }else if (etPassword.getText().toString().equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("登入失敗")
                    .setMessage("密碼不得為空")
                    .setPositiveButton("ok",null)
                    .show();

            etPassword.setFocusableInTouchMode(true);
            etPassword.requestFocus();

        }else {

            String passWord = etPassword.getText().toString();
            String phoneNumber = etPhone.getText().toString();

            settingcommit();

            new CheckLoginActivity(this).execute(passWord, phoneNumber);

            if (checkBox1.isChecked() == false){
                etPhone.setText("");
            }
            if (checkBox2.isChecked() == false){
                etPassword.setText("");
            }

        }

    }

    public void singup(View v){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    class CheckLoginActivity extends AsyncTask<String, Void, String> {


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
            String data = null;
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
            Log.d("result=",result);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");
                    if (query_result.equals("SUCCESS")) {
                        String username = jsonObj.getString("username");
                        Toast.makeText(context, username + "歡迎登入", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, QueryActivity.class);

                        Bundle bag = new Bundle();
                        bag.putString("phone", etPhone.getText().toString());
                        intent.putExtras(bag);

                        startActivity(intent);
                    } else if (query_result.equals("FAILURE")) {
                        Toast.makeText(context, "會員登入失敗", Toast.LENGTH_SHORT).show();
                        etPhone.setText("");
                        etPassword.setText("");
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

}
