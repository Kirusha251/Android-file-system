package com.example.kirill.studentsprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kirill on 05.12.2016.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper {
    public MyDataBaseHelper(Context context) {
        super(context, "STUDENTSDB", null,7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table STUDGROUPS(IDGROUP integer primary key Autoincrement,"
                        + "FACULTY text not null, "
                        + "COURSE integer not null,"
                        + "NAME text not null,"
                        + "HEAD text not null);"
        );

        db.execSQL(
                "create table STUDENTS(IDGROUP integer,"
                        + "IDSTUDENT integer primary key Autoincrement,"
                        + "NAME text," +
                        "foreign key(IDGROUP) references STUDGROUPS(IDGROUP) ON DELETE CASCADE ON UPDATE CASCADE);"
        );

        db.execSQL("CREATE TRIGGER disableAddStudents\n" +
                "BEFORE INSERT ON STUDENTS\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "SELECT\n" +
                "CASE\n" +
                "    WHEN (Select Count(STUDENTS.NAME)From STUDENTS Where IDGROUP = new.IDGROUP)>=6 THEN\n" +
                "    RAISE (ABORT,'ERROR ADD')\n" +
                "    END;\n" +
                "END;");


        db.execSQL("CREATE TRIGGER disableDeleteStudents\n" +
                "AFTER Delete ON STUDENTS\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "SELECT\n" +
                "CASE\n" +
                "    When old.NAME IS NUll THEN\n" +
                "    RAISE(IGNORE)\n" +
                "    WHEN(Select Count(STUDENTS.NAME) From STUDENTS where IDGROUP = old.IDGROUP) < 3 THEN\n" +
                "                      RAISE (ABORT,'ERROR DELETE')\n" +
                "    END;\n" +
                "END;");

        db.execSQL("CREATE TRIGGER deleteCascade\n" +
                "BEFORE Delete ON STUDGROUPS\n" +
                "for each row\n" +
                "BEGIN\n" +
                "    update STUDENTS SET NAME = null where IDGROUP = old.IDGROUP;\n" +
                "END;");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist STUDGROUPS");
        db.execSQL("drop table if exist STUDENTS");
        db.execSQL("drop trigger if exist deleteCascade");
        db.execSQL("drop trigger if exist disableDeleteStudents");
        db.execSQL("drop trigger if exist disableAddStudents");
        db.execSQL(
                "create table STUDGROUPS("+"IDGROUP integer primary key Autoincrement,"
                        + "FACULTY text not null, "
                        + "COURSE integer not null,"
                        + "NAME text not null,"
                        + "HEAD text not null);"
        );

        db.execSQL(
                "create table STUDENTS(IDGROUP integer,"
                        + "IDSTUDENT integer primary key Autoincrement,"
                        + "NAME text," +
                        "foreign key(IDGROUP) references STUDGROUPS(IDGROUP) ON DELETE CASCADE ON UPDATE CASCADE);"
        );

        db.execSQL("CREATE TRIGGER disableAddStudents\n" +
                "BEFORE INSERT ON STUDENTS\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "SELECT\n" +
                "CASE\n" +
                "    WHEN (Select Count(STUDENTS.NAME)From STUDENTS Where IDGROUP = new.IDGROUP)>=6 THEN\n" +
                "    RAISE (ABORT,'ERROR ADD')\n" +
                "    END;\n" +
                "END;");


        db.execSQL("CREATE TRIGGER disableDeleteStudents\n" +
                "AFTER Delete ON STUDENTS\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "SELECT\n" +
                "CASE\n" +
                "    When old.NAME IS NUll THEN\n" +
                "    RAISE(IGNORE)\n" +
                "    WHEN(Select Count(STUDENTS.NAME) From STUDENTS where IDGROUP = old.IDGROUP) < 3 THEN\n" +
                "                      RAISE (ABORT,'ERROR DELETE')\n" +
                "    END;\n" +
                "END;");

        db.execSQL("CREATE TRIGGER deleteCascade\n" +
                "BEFORE Delete ON STUDGROUPS\n" +
                "for each row\n" +
                "BEGIN\n" +
                "    update STUDENTS SET NAME = null where IDGROUP = old.IDGROUP;\n" +
                "END;");

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
            //db.setForeignKeyConstraintsEnabled(true);

        }
    }
}
