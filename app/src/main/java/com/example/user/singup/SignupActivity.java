package com.example.user.singup;

        import android.content.Context;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.widget.Toast;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLEncoder;

public class SignupActivity extends AsyncTask<String, Void, String> {

            private Context context;

            public SignupActivity(Context context) {
        this.context = context;
    }

            protected void onPreExecute() {

    }

            @Override
    protected String doInBackground(String... arg0) {
        String fullName = arg0[0];
        String userName = arg0[1];
        String passWord = arg0[2];
        String phoneNumber = arg0[3];
        String emailAddress = arg0[4];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
            data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
            data += "&username=" + URLEncoder.encode(userName, "UTF-8");
            data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
            data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
            data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");

            link = "http://140.130.33.104/hello.php" + data;
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
                if (query_result.equals("SUCCESS")) {
                    Toast.makeText(context, "Data inserted successfully. Signup successful.", Toast.LENGTH_SHORT).show();
                } else if (query_result.equals("FAILURE")) {
                    Toast.makeText(context, "Data could not be inserted. Signup failed.", Toast.LENGTH_SHORT).show();
                } else if (query_result.equals("REWITE")) {
                    Toast.makeText(context, "Data rewite.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
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