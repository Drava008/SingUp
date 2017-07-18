package com.example.user.singup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class QueryActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        listView = (ListView)findViewById(R.id.listview);
        new QueryLoadActivity(this).execute("123456789");

        ArrayList<String> ar = new ArrayList<String>();

        String[] tokens = {"星期一","星期二","星期三"};
        for (String token:tokens) {
            ar.add(token);
        }


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar);

        listView = (ListView)findViewById(R.id.listview);

        listView.setAdapter(adapter);
    }
}
