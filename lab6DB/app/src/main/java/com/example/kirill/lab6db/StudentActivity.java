package com.example.kirill.lab6db;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class StudentActivity extends AppCompatActivity {
    EditText idGroup;
    EditText idStudent;
    EditText name;
    ListView viewStudents;
    ArrayAdapter<String> studentListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        idGroup = (EditText)findViewById(R.id.idGroup);
        idStudent = (EditText)findViewById(R.id.idStudent);
        name = (EditText)findViewById(R.id.name);
        viewStudents = (ListView)findViewById(R.id.studentsByGroupIdList);
    }

    public void onAddButtonClick(View view){
        if(idStudent.getText().toString().equals("")){
            MyDataBaseQueryHelper.insertStudentQuery(Integer.valueOf(idGroup.getText().toString()),
                    -1,name.getText().toString());
        }
        else {
            MyDataBaseQueryHelper.insertStudentQuery(Integer.valueOf(idGroup.getText().toString()),
                    Integer.valueOf(idStudent.getText().toString()), name.getText().toString());
        }
    }



    public void onUpdateButtonClick(View view){
        MyDataBaseQueryHelper.updateStudentQuery(Integer.valueOf(idGroup.getText().toString()),
                Integer.valueOf(idStudent.getText().toString()),name.getText().toString());

    }
    public void onGetButtonClick(View view){
        studentListAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,
                MyDataBaseQueryHelper.getStudentsListByGroupId(Integer.valueOf(idGroup.getText().toString())));
        viewStudents.setAdapter(studentListAdapter);
    }

    public void onDeleteButtonClick(View view){
       MyDataBaseQueryHelper.deleteStudentQuery(Integer.valueOf(idStudent.getText().toString()),
               Integer.valueOf(idGroup.getText().toString()));
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
