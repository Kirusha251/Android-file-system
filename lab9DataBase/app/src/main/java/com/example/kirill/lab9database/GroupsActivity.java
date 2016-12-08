package com.example.kirill.lab9database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class GroupsActivity extends AppCompatActivity {
    EditText idGroup;
    EditText faculty;
    EditText course;
    EditText name;
    EditText head;
    EditText newIdGroup;
    ListView viewGroup;
    ArrayAdapter<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        idGroup = (EditText)findViewById(R.id.idGroup);
        faculty = (EditText)findViewById(R.id.faculty);
        course = (EditText)findViewById(R.id.course);
        name = (EditText)findViewById(R.id.name);
        head = (EditText)findViewById(R.id.head);
        newIdGroup = (EditText)findViewById(R.id.newIdGroup);
        viewGroup= (ListView) findViewById(R.id.showStudentGroupId);
    }
    public void onAddGroupButtonClick(View view ){
        if(idGroup.getText().toString().equals("")){
            QueryHelper.insertGroupQuery(-1,
                    faculty.getText().toString(), Integer.valueOf(course.getText().toString()),
                    name.getText().toString(), head.getText().toString());
        }
        else {
            QueryHelper.insertGroupQuery(Integer.valueOf(idGroup.getText().toString()),
                    faculty.getText().toString(), Integer.valueOf(course.getText().toString()),
                    name.getText().toString(), head.getText().toString());
        }
    }
    public void onDeleteGroupButtonClick(View view){
        QueryHelper.deleteGroupQuery(Integer.valueOf(idGroup.getText().toString()));
    }

    public void onUpdateGroupButtonClick(View view){
        QueryHelper.updateGroupQuery(idGroup.getText().toString(),Integer.valueOf(newIdGroup.getText().toString()));
    }
    public void onDeleteAllButtonClick(View view){
        QueryHelper.deleteGroupQuery(-1);
    }
    public void onShowGroupStudentsButtonClick(View view){
        list = new ArrayAdapter<String>(this.getBaseContext(),android.R.layout.simple_list_item_1,
                QueryHelper.getGroupInfoById(Integer.valueOf(idGroup.getText().toString())));
        viewGroup.setAdapter(list);
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
