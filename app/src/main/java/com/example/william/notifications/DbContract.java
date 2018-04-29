package com.example.william.notifications;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by william on 3/21/18.
 */

public final class DbContract {

    public static final String CONTENT_AUTHORITY = "com.example.william.notifications.notifprovider";
    public static final String SCHOOL_PATH = "school";
    public static final String ClASS_PATH = "class";
    public static final String SCHEDULE_PATH = "schedules";
    public static final String STUDENT_PATH = "students";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

   /******** School Data Table   ********/
    public static  final class SchoolEntry implements BaseColumns{

        public static final Uri SCHOOL_URI = Uri.withAppendedPath(BASE_CONTENT_URI, SCHOOL_PATH);

        public static final String TABLE_NAME = "school";

        public static final String id = BaseColumns._ID;
        public static final String SCHOOL_NAME = "schoolName";

        public static final String  SQL_CREATE_TABLE = "Create table School (\n" +
                "\t_id integer primay key,\n" +
                "\tschoolName Text\t\n" +
                ")\n";


    }

    /********  Class Data Table    ********/
    public static final class ClassEntry implements BaseColumns{

        public static final Uri CLASS_URI = Uri.withAppendedPath(BASE_CONTENT_URI,ClASS_PATH);

        public static final String TABLE_NAME = "class";
        public static final String id = BaseColumns._ID;

        public static final String SCHOOL_ID = "schoolId";
        public static final String ClASS_NAME = "className";

        public static final String SQL_CREATE_TABLE = "\n" +
                "create table class (\n" +
                "\t_id integer primary key,\n" +
                "\tschoolId integer,\n" +
                "\tclassName TEXT\n" +
                ")";
    }

    /******** Teacher Details Table     ********/
    public static final class TeachersEntry implements BaseColumns{

        public static final String TABLE_NAME = "teachers";

        public static final String id = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PHOTO_URL = "photo_url";

        public static final String SQL_CREATE_TABLE = "create table teachers (\n" +
                "\t_id integer primary key,\n" +
                "\tname TEXT,\n" +
                "\temail TEXT,\n" +
                "\taccessToken TEXT,\n"+
                "\tphoto_url TEXT\n" +
                ")\n" +
                "\n";

    }


    /********  Teachers Schedules Table  ********/
    public static final class ScheduleEntry implements BaseColumns{

        public static final Uri SCHEDULE_URI = Uri.withAppendedPath(BASE_CONTENT_URI,SCHEDULE_PATH);

        public static final String TABLE_NAME = "schedules";

        public static final String id = BaseColumns._ID;
        public static final String SCHOOL_ID = "schoolId";
        public static final String CLASS_ID = "classId";
        public static final String TEACHER_ID = "teacher_id";
        public static final String CONTENT = "content";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
        public static final String SCHOOL_NAME = "school_name";
        public static final String CLASS_NAME = "class_name";
        public static final String DELIVER_DATE = "delivery_date";
        public static final String CANCELLED = "cancelled";
        public static final String VACATION = "vacation";
        public static final String DAYS = "days";


        public static final String SQL_CREATE_TABLE = "CREATE TABLE schedules (\n" +
                "\t_id integer primary key,\n" +
                "\tschoolId INTEGER ,\n" +
                "\tclassId INTEGER ,\n" +
                "\tteacher_id INTEGER ,\n" +
                "\tcontent INTEGER ,\n" +
                "\tstart_date TEXT ,\n" +
                "\tend_date TEXT ,\n" +
                "\tschool_name TEXT ,\n"+
                "\tclass_name TEXT ,\n"+
                "\tdelivery_date TEXT ,\n" +
                "\tdays TEXT ,\n" +
                "\tcancelled TEXT ,\n" +
                "\tvacation TEXT ,\n" +
                "\tFOREIGN KEY (schoolId) REFERENCES schedules ( id ),\n" +
                "\tFOREIGN KEY (classId) REFERENCES class ( id )\n" +
                ")";


    }


    public static final class StudentsEntry implements BaseColumns{

        public static final Uri STUDENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, STUDENT_PATH);


        public static final String TABLE_NAME = "students";

        public static final String id = BaseColumns._ID;
        public static final String STUDENT_FNAME = "student_fname";
        public static final String STUDENT_LNAME = "student_lname";
        public static final String CLASS_ID = "classId";
        public static final String SCHOOL_ID = "schoolId";
        public static final String ATTENDANCE = "attendance";

        public static final String CREATE_STUDENT_TABLE = "CREATE TABLE students (\n" +
                "\t\t_id integer primary key,\n" +
                "\t\tstudent_fname TEXT,\n" +
                "\t\tstudent_lname TEXT,\n" +
                "\t\tattendance INTEGER,\n"+
                "\t\tschoolId integer,\n" +
                "\t\tclassId integher,\n" +
                "\t\tFOREIGN KEY (schoolId) REFERENCES schedules ( schoolId ),\n" +
                "\t\tFOREIGN KEY (classId) REFERENCES schedules ( classId )\n" +
                ")";
    }





}
