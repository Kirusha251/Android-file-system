package com.example.kirill.lab6db.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.kirill.lab6db.MyDataBaseHelper;

/**
 * Created by Kirill on 28.11.2016.
 */

public class StudentsProvider extends ContentProvider {

    static final String AUTHORITY = "com.example.kirill.lab6db";
    static final String GROUP_LIST_PATH = "groupList";
    static final String STUDENT_lIST_PATH ="studentList";

    public static final Uri GROUP_LIST_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+GROUP_LIST_PATH);
    public static final Uri STUDENT_LIST_CONTEN_URI = Uri.parse("content://"+AUTHORITY+"/"+STUDENT_lIST_PATH);

    static final int URI_GROUP_LIST = 1;
    static final int URI_GROUP_LIST_ID =2;
    static final int URI_STUDENT_LIST_IDGROUP = 3;
    static final int URI_STUDENT_LIST_IDGROUP_IDSTUDENT = 4;
    static final int URL_STUDENT_LIST = 5;

    static final String LOG_TAG="CO";

    public static UriMatcher uriMatcher;
    MyDataBaseHelper dataBaseHelper;
    SQLiteDatabase dataBase;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,GROUP_LIST_PATH,URI_GROUP_LIST);
        uriMatcher.addURI(AUTHORITY,GROUP_LIST_PATH+"/#",URI_GROUP_LIST_ID);
        uriMatcher.addURI(AUTHORITY,STUDENT_lIST_PATH+"/#",URI_STUDENT_LIST_IDGROUP);
        uriMatcher.addURI(AUTHORITY,STUDENT_lIST_PATH+"/#"+"/#",URI_STUDENT_LIST_IDGROUP_IDSTUDENT);
        uriMatcher.addURI(AUTHORITY,STUDENT_lIST_PATH,URL_STUDENT_LIST);
    }

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG,"onCreate");
        dataBaseHelper = new MyDataBaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String idGroup="";
        String idStudent="";
        String currentTable="";
        Uri currentUri;
        dataBase = dataBaseHelper.getWritableDatabase();
        switch(uriMatcher.match(uri)){
            case URI_GROUP_LIST: {
                Log.d(LOG_TAG,"GroupList uri");
                currentUri = GROUP_LIST_CONTENT_URI;
                //currentTable = "STUDGROUPS";
                dataBase.execSQL("DROP VIEW IF EXISTS countStudents");

                dataBase.execSQL("CREATE VIEW countStudents as SELECT Tgroups.IDGROUP, Tgroups.HEAD, COALESCE(Tstudents.csn,0)\n" +
                        "From  STUDGROUPS as Tgroups  left join (\n" +
                        "     SELECT s.IDGROUP, Count(s.NAME)csn\n" +
                        "     From STUDENTS as s\n" +
                        "     group by s.IDGROUP\n" +
                        ") as Tstudents ON Tstudents.IDGROUP = Tgroups.IDGROUP;");
                currentTable = "countStudents";
            }break;
            case URI_GROUP_LIST_ID:{
                Log.d(LOG_TAG,"GroupListId uri");
                idGroup = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    selection = "IDGROUP = "+idGroup;
                }
                else{
                    selection = selection + " and IDGROUP = "+idGroup;
                }
                currentUri = GROUP_LIST_CONTENT_URI;
                //currentTable = "STUDGROUPS";
                dataBase.execSQL("DROP VIEW IF EXISTS countStudents");

                dataBase.execSQL("CREATE VIEW countStudents as SELECT Tgroups.IDGROUP, Tgroups.HEAD, COALESCE(Tstudents.csn,0)\n" +
                        "From  STUDGROUPS as Tgroups  left join (\n" +
                        "     SELECT s.IDGROUP, Count(s.NAME)csn\n" +
                        "     From STUDENTS as s\n" +
                        "     group by s.IDGROUP\n" +
                        ") as Tstudents ON Tstudents.IDGROUP = Tgroups.IDGROUP;");
                currentTable = "countStudents";
            } break;
            case URI_STUDENT_LIST_IDGROUP:{
                Log.d(LOG_TAG,"StudentListIdGroup uri");
                idGroup = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    selection = "IDGROUP = "+idGroup;
                }
                else{
                    selection = selection + " and IDGROUP = "+idGroup;
                }
                currentUri = STUDENT_LIST_CONTEN_URI;
                currentTable = "STUDENTS";
            } break;
            case URI_STUDENT_LIST_IDGROUP_IDSTUDENT:{
                Log.d(LOG_TAG,"StudentListIdGroup uri");
                idGroup = uri.getLastPathSegment();
                idStudent = uri.getPathSegments().get(uri.getPathSegments().size()-1); // проверь правильность!!!
                if(TextUtils.isEmpty(selection)){
                    selection = "IDGROUP = "+idGroup + " and IDSTUDENT = " + idStudent;
                }
                else{
                    selection = selection + " and IDGROUP = "+idGroup + " and IDSTUDENT = " +idStudent;
                }
                currentUri = STUDENT_LIST_CONTEN_URI;
                currentTable = "STUDENTS";

            } break;
            case URL_STUDENT_LIST:{
                Log.d(LOG_TAG,"StudentsList uri");
                currentUri = STUDENT_LIST_CONTEN_URI;
                currentTable = "STUDENTS";
            }break;
            default: throw  new IllegalArgumentException("Wrong uri:"+uri);
        }
        Cursor cursor = dataBase.query(currentTable,projection,selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),currentUri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        return null;
    }



    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String currentTable = "";

        switch (uriMatcher.match(uri)){
            case URI_GROUP_LIST:{
                currentTable = "STUDGROUPS";
                    //добавить новую группу
            }break;

            case URI_STUDENT_LIST_IDGROUP:{
                currentTable = "STUDENTS";
                // добавить студента в данную группу
            }break;

            default:throw  new IllegalArgumentException("Wrong uri:"+uri);

        }
        dataBase = dataBaseHelper.getWritableDatabase();
        Long rowID = dataBase.insert(currentTable, null,values);
        Uri resultUri = ContentUris.withAppendedId(uri,rowID);
        getContext().getContentResolver().notifyChange(resultUri,null);
        return resultUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String currentTable = "";
        switch (uriMatcher.match(uri)){
            case URI_GROUP_LIST_ID:{
                currentTable = "STUDGROUPS";
                String idGroup = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    selection = "IDGROUP = " + idGroup;
                }
                else{
                    selection = selection +"and IDGROUP ="+idGroup;
                }
            }break;
            case URI_STUDENT_LIST_IDGROUP_IDSTUDENT:{
                currentTable = "STUDENTS";
                String idGroup;
                String idStudent;
                idGroup = uri.getLastPathSegment();
                idStudent = uri.getPathSegments().get(uri.getPathSegments().size()-1);
                if(TextUtils.isEmpty(selection)){
                    selection = "IDGROUP = "+idGroup + " and IDSTUDENT =" + idStudent;
                }
                else{
                    selection = selection+" and IDGROUP = "+idGroup + " and IDSTUDENT =" + idStudent;
                }
            }break;
        }
        dataBase = dataBaseHelper.getWritableDatabase();
        int cnt = dataBase.update(currentTable, values,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return cnt;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String currentTable ="";
        String idGroup="";
        String idStudent="";
        switch (uriMatcher.match(uri)){
            case URI_GROUP_LIST:{
                currentTable = "STUDGROUPS";
            }break;
            case URI_GROUP_LIST_ID:{
                currentTable = "STUDGROUPS";
                idGroup = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    selection = "IDGROUP  =" + idGroup;
                }
                else{
                    selection = selection + " and IDGROUP ="+idGroup;
                }
            }break;
            case URI_STUDENT_LIST_IDGROUP:{
                currentTable = "STUDENTS";
                idStudent = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    selection = "IDSTUDENT ="+idStudent;
                }
                else{
                    selection = selection + " and IDSTUDENT ="+idStudent;
                }

            }break;
            case URI_STUDENT_LIST_IDGROUP_IDSTUDENT:{
                currentTable = "STUDENTS";
                idStudent = uri.getLastPathSegment();
                idGroup = uri.getPathSegments().get(uri.getPathSegments().size()-1);
                if(TextUtils.isEmpty(selection)){
                    selection = "IDGROUP ="+idGroup+" and IDSTUDENT ="+idStudent;
                }
                else{
                    selection = selection + " and IDGROUP ="+idGroup+" and IDSTUDENT ="+idStudent;
                }
            }break;
        }
        dataBase = dataBaseHelper.getWritableDatabase();
        int i = dataBase.delete(currentTable,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return i;
    }
}
