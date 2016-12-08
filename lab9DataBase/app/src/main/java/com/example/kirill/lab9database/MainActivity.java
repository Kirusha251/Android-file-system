package com.example.kirill.lab9database;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.viewDataId);
        QueryHelper.context = getApplicationContext();
       /* Uri uri = Uri.parse("content://authotity/GroupsList/gcontent://authotity/GroupsList/g");
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.example.kirill.lab6db/studentList/1"),new String[]{"IDGROUP","NAME","IDSTUDENT"},null,null,null);
        int x = 333;*/
    }
    public void onGetGroupsButtonClick(View view){
        ArrayList<String> listGroup= new ArrayList<>();
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.example.kirill.studentsprovider/groupList"),null,null,null,null);
        if(cursor.moveToFirst()){

            do{
                listGroup.add("IDGROUP: "+cursor.getInt(0)+", HEAD: " + cursor.getString(1)+", Count:"+cursor.getInt(2)+".");
            }
            while(cursor.moveToNext());

        }
        adapter  = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,listGroup);
        listView.setAdapter(adapter);
    }
    public void onGetStudentsButtonClick(View view){
        ArrayList<String> listStudent= new ArrayList<>();
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.example.kirill.studentsprovider/studentList"),new String[]{"IDGROUP","NAME","IDSTUDENT"},null,null,null);
        if(cursor.moveToFirst()){

            do{
                listStudent.add("IDGROUP: "+cursor.getInt(0)+", Name: " + cursor.getString(1)+", ID:"+cursor.getInt(2)+".");
            }
            while(cursor.moveToNext());

        }
        adapter  = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,listStudent);
        listView.setAdapter(adapter);
    }
    public void onGroupsButtonClick(View view){
        Intent intent = new Intent(MainActivity.this,GroupsActivity.class);
        startActivity(intent);
    }
    public void onStudentsButtonClick(View view){
        Intent intent = new Intent(MainActivity.this,StudentActivity.class);
        startActivity(intent);
    }
}
