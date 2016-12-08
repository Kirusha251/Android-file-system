package com.example.kirill.lab9database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kirill on 04.12.2016.
 */

public class QueryHelper {

    public static SQLiteDatabase dataBase;
    public static Context context;
    static final String AUTHORITY = "com.example.kirill.studentsprovider";
    public static void insertStudentQuery(int idGroup, int idStudent, String name) {
        long i = 1;
        try {
            ContentValues values = new ContentValues();
            if (idStudent != -1) {
                values.put("IDSTUDENT", idStudent);
            }
            values.put("IDGROUP", idGroup);
            values.put("NAME", name);
            context.getContentResolver().insert(Uri.parse("content://"+AUTHORITY+"/studentList/" + idGroup), values);
        } catch (SQLiteException ex) {
            Toast.makeText(context, "not entered:" + ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.d("error", " cant insert");
        }
        if (i == -1) {
            Toast.makeText(context, "cant inserted", Toast.LENGTH_SHORT).show();
        }

    }

    public static void updateStudentQuery(int idGroup, int idStudent, String name) {
        try {
            ContentValues values = new ContentValues();
            values.put("IDGROUP", idGroup);
            values.put("IDSTUDENT", idStudent);
            values.put("NAME", name);
            context.getContentResolver().update(Uri.parse("content://"+AUTHORITY+"/studentList/" + idGroup + "/" + idStudent), values, null, null);

            //dataBase.update("STUDENTS", values, "IDSTUDENT=?", new String[]{Integer.toString(idStudent)});
        } catch (SQLiteException ex) {
            Toast.makeText(context, "cant updated", Toast.LENGTH_SHORT).show();
            Log.d("error", "cant update", null);
        }
    }


    public static void insertGroupQuery(int idGroup, String faculty, int course, String name, String head) {
        try {
            ContentValues values = new ContentValues();
            if (idGroup != -1) {
                values.put("IDGROUP", idGroup);
            }
            values.put("FACULTY", faculty);
            values.put("COURSE", course);
            values.put("NAME", name);
            values.put("HEAD", head);
            Uri uri = context.getContentResolver().insert(Uri.parse("content://"+AUTHORITY+"/groupList/"), values);
            long i = Long.valueOf(uri.getLastPathSegment());
        } catch (SQLiteException ex) {
            Toast.makeText(context, "cant insert", Toast.LENGTH_SHORT).show();
            Log.d("error", " cant insert");
        }

    }

    public static void updateGroupQuery(String editID, int idGroup) {
        try {
            ContentValues values = new ContentValues();
            values.put("IDGROUP", idGroup);
            //dataBase.update("STUDGROUPS",values,"IDGROUP=?",new String[]{editID});
            context.getContentResolver().update(Uri.parse("content://"+AUTHORITY+"/groupList/" + editID), values, null, null);
        } catch (SQLiteException ex) {
            Toast.makeText(context, "cant updated", Toast.LENGTH_SHORT).show();
            Log.d("error", "entered id exist", null);
        }

    }

    public static void deleteStudentQuery(int idStudent, int idGroup) {
        try {
            int i = context.getContentResolver().delete(Uri.parse("content://"+AUTHORITY+"/studentList/" + idGroup + "/" + idStudent), null, null);
        } catch (SQLiteException ex) {
            Toast.makeText(context, "Can't delete", Toast.LENGTH_SHORT).show();
            Log.d("error", "error", null);
        }
    }

    public static void deleteGroupQuery(int idGroup) {
        try {
            if (idGroup != -1) {
                int i = context.getContentResolver().delete(Uri.parse("content://"+AUTHORITY+"/groupList/" + idGroup), null, null);
            } else {
                int i = context.getContentResolver().delete(Uri.parse("content://"+AUTHORITY+"/groupList"), null, null);
            }
        } catch (SQLiteException ex) {
            Toast.makeText(context, "Can't delete", Toast.LENGTH_SHORT).show();
            Log.d("error", "error", null);
        }
    }

    public static ArrayList<String> getStudentList(ContentResolver resolver) {
        ArrayList<String> listStudent = new ArrayList<>();
        Cursor cursor = resolver.query(Uri.parse("content://"+AUTHORITY+"/studentList"), new String[]{"IDGROUP", "NAME", "IDSTUDENT"}, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                listStudent.add("IDGROUP: " + cursor.getInt(0) + ", Name: " + cursor.getString(1) + ", ID:" + cursor.getInt(2) + ".");
            }
            while (cursor.moveToNext());

        }
        return listStudent;
    }

    public static ArrayList<String> getGroupList() {
        ArrayList<String> listGroup = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://"+AUTHORITY+"/groupList"), null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                listGroup.add("IDGROUP: " + cursor.getInt(0) + ", HEAD: " + cursor.getString(1) + ", Count:" + cursor.getInt(2) + ".");
            }
            while (cursor.moveToNext());

        }
        return listGroup;
    }

    public static ArrayList<String> getStudentsListByGroupId(int idGroup) {
        ArrayList<String> listGroup = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://"+AUTHORITY+"/" +
                "studentList/" + idGroup), null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                listGroup.add("IDSTUDENT: " + cursor.getInt(1) + ", IDGROUP: " + cursor.getInt(0) + ", NAME:" + cursor.getString(2) + ".");
            }
            while (cursor.moveToNext());

        }
        return listGroup;
    }

    public static ArrayList<String> getGroupInfoById(int idGroup) {
        ArrayList<String> information = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://"+AUTHORITY+"/" +
                "groupList/" + idGroup), null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                information.add("IDGROUP:" + cursor.getInt(0) + ", HEAD:" + cursor.getString(1) + ", COUNT:" + cursor.getInt(2));
            }
            while (cursor.moveToNext());
        }

        return information;
    }

    public static ArrayList<String> getInfoByView() {
        ArrayList<String> info = new ArrayList<>();
        //context.getContentResolver().query(Uri.parse(""),)
        dataBase.execSQL("DROP VIEW IF EXISTS countStudents");

        dataBase.execSQL("CREATE VIEW countStudents as SELECT Tgroups.IDGROUP, Tgroups.HEAD, COALESCE(Tstudents.csn,0)\n" +
                "From  STUDGROUPS as Tgroups  left join (\n" +
                "     SELECT s.IDGROUP, Count(s.NAME)csn\n" +
                "     From STUDENTS as s\n" +
                "     group by s.IDGROUP\n" +
                ") as Tstudents ON Tstudents.IDGROUP = Tgroups.IDGROUP;");


        Cursor cursor = dataBase.rawQuery("SELECT * FROM countStudents", null);

        if (cursor.moveToFirst()) {

            do {
                info.add("IDGROUP: " + cursor.getInt(0) + ", Head: " + cursor.getString(1) + ", Count:" + cursor.getInt(2) + ".");
            }
            while (cursor.moveToNext());

        }
        return info;
    }
}