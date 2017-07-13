package com.example.user.singup;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText etFullName, etUserName, etPassword, etPhone, etEmail;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFullName = (EditText) findViewById(R.id.etFullName);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
    }

    public void signup(View v) {
        String fullName = etFullName.getText().toString();
        String userName = etUserName.getText().toString();
        String passWord = etPassword.getText().toString();
        String phoneNumber = etPhone.getText().toString();
        String emailAddress = etEmail.getText().toString();
        String regid = str;

        Toast.makeText(this, "Signing up...", Toast.LENGTH_SHORT).show();
        new SignupActivity(this).execute(fullName, userName, passWord, phoneNumber, emailAddress, regid);
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

    public void radom(View v){
        char arr[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        int strlenth = 150;
        char randStr[] = new char[strlenth];
        for (int i=0;i<randStr.length;i++)
        {
            randStr[i] = arr[(int)Math.round(Math.random() * (arr.length - 1))];
        }

        str = new String(randStr);
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("RadomText");
        MyAlertDialog.setMessage(str);
        MyAlertDialog.show();
    }
}
