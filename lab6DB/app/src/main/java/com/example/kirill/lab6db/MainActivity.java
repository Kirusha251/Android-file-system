package com.example.kirill.lab6db;

import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    Button groups;
    Button student;
    ListView studentList;
    ListView groupList;
    ArrayAdapter<String> studentAdapter;
    ArrayAdapter<String> groupAdapter;
    public MyDataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyDataBaseQueryHelper.context = getApplicationContext();
        dataBaseHelper = new MyDataBaseHelper(getApplicationContext());
        MyDataBaseQueryHelper.dataBase = dataBaseHelper.getWritableDatabase();
        groups = (Button)findViewById(R.id.groups);
        student = (Button)findViewById(R.id.students);
        studentList = (ListView)findViewById(R.id.studentList);
        groupList = (ListView)findViewById(R.id.groupList);
        studentAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                MyDataBaseQueryHelper.getStudentList(getContentResolver()).
                        toArray(new String[MyDataBaseQueryHelper.getStudentList(getContentResolver()).size()]));
        groupAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                MyDataBaseQueryHelper.getGroupList().
                        toArray(new String[MyDataBaseQueryHelper.getGroupList().size()]));
        studentList.setAdapter(studentAdapter);
        groupList.setAdapter(groupAdapter);
    }

    public void onGroupsButtonClick(View view){
        Intent intent = new Intent(MainActivity.this,StudentsGroupActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        studentAdapter.notifyDataSetChanged();
    }

    public void onStudentButtonClick(View view){
        Intent intent = new Intent(MainActivity.this,StudentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
    public void onViewButtonClick(View view){
        Intent intent = new Intent(MainActivity.this,ViewActivity.class);
        startActivity(intent);
    }
}
