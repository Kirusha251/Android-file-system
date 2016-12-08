package com.example.kirill.lab6db;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewActivity extends AppCompatActivity {
    ListView groupList;
    ArrayAdapter<String> groupAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        groupList = (ListView)findViewById(R.id.groupListView);
        groupAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                MyDataBaseQueryHelper.getInfoByView().
                        toArray(new String[MyDataBaseQueryHelper.getInfoByView().size()]));
        groupList.setAdapter(groupAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
