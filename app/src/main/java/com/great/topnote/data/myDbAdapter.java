package com.great.topnote.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class myDbAdapter {
    myDbHelper myhelper;
    Context context;
    public myDbAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
        this.context=context;
    }

    public long insertUserData(String name, String phone, String mail,String password,String level)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME, name);
        contentValues.put(myDbHelper.MyPASSWORD, password);
        contentValues.put(myDbHelper.phone,phone );
        contentValues.put(myDbHelper.Email,mail);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
        return id;
    }
    public long insertNoteData(Note note)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.ID, note.getId());
        contentValues.put(myDbHelper.NAME, note.getName());
        contentValues.put(myDbHelper.Group_Id, note.getGroupId());
        contentValues.put(myDbHelper.Details, note.getDetails());
        contentValues.put(myDbHelper.Title, note.getTitle());
        contentValues.put(myDbHelper.file, note.getFile());
        contentValues.put(myDbHelper.Image, note.getImage());
        contentValues.put(myDbHelper.Sound, note.getSound());
        long id = dbb.insert(myDbHelper.TABLE_NAME_2, null , contentValues);
        return id;
    }
    public long insertGroupData(String Id,String Group)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.ID, Id);
        contentValues.put(myDbHelper.Title, Group);
        long id = dbb.insert(myDbHelper.TABLE_NAME_4, null , contentValues);
        Toast.makeText(context,id+"",Toast.LENGTH_LONG).show();

        ContentValues values = new ContentValues();
        values.put(myDbHelper.ID, Id);
        values.put(myDbHelper.Title, 1);
        id = dbb.insert(myDbHelper.TABLE_NAME_5, null , values);
        return id;
    }
    public void insertTaskData(Task task,String id)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.ID, id);
        contentValues.put(myDbHelper.NAME, task.getName());
        contentValues.put(myDbHelper.Details, task.getDetails());
        contentValues.put(myDbHelper.Title, task.getTitle());
        contentValues.put(myDbHelper.Email, task.getEmail());
         dbb.insert(myDbHelper.TABLE_NAME_3, null , contentValues);
    }

    public String getUserData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String Sl="a b c";
        String[] columns = {myDbHelper.ID,myDbHelper.Email,myDbHelper.MyPASSWORD,myDbHelper.NAME,myDbHelper.phone};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.ID));
            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.Email));
            String  password =cursor.getString(cursor.getColumnIndex(myDbHelper.MyPASSWORD));

            Sl=(cid+ " " + name + " " + password+" "+cursor.getString(cursor.getColumnIndex(myDbHelper.NAME))+" "+cursor.getString(cursor.getColumnIndex(myDbHelper.phone)));
        }
        return Sl;
    }
    public String[] getUserData_inf()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String Sl[]=new String[10];
        Sl[1]="not  vaild";Sl[3]="not vaild";
        String[] columns = {myDbHelper.ID,myDbHelper.Email,myDbHelper.MyPASSWORD,myDbHelper.NAME,myDbHelper.phone};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            String mail =cursor.getString(cursor.getColumnIndex(myDbHelper.Email));
            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            String  phone =cursor.getString(cursor.getColumnIndex(myDbHelper.phone));
            String  password =cursor.getString(cursor.getColumnIndex(myDbHelper.MyPASSWORD));
            Sl= new String[]{name, mail, phone,password };
        }
        return Sl;
    }
    public String getIndexData_inf(String id_g)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String Sl ="1";
        String[] columns = {myDbHelper.ID,myDbHelper.Title};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME_5,columns,null,null,null,null,null);

         while (cursor.moveToNext())
        {
            String id =cursor.getString(cursor.getColumnIndex(myDbHelper.ID));
            if(id.equals(id_g))
            Sl=cursor.getString(cursor.getColumnIndex(myDbHelper.Title));
        }
        return Sl;
    }
    public ArrayList<Note> getNotes(String group) {

        ArrayList<Note> notes=new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.Title,myDbHelper.NAME,myDbHelper.Group_Id
                ,myDbHelper.Details,myDbHelper.Sound,myDbHelper.Image,myDbHelper.file
        };
        Cursor cursor =db.rawQuery("SELECT * FROM "+myDbHelper.TABLE_NAME_2+" Where "+myDbHelper.Group_Id+" = ?", new String[]{group});
        while (cursor.moveToNext())
        {
            Note note=new Note();
            note.setId(cursor.getString(cursor.getColumnIndex(myDbHelper.ID)));
            note.setName(cursor.getString(cursor.getColumnIndex(myDbHelper.NAME)));
            note.setDetails(cursor.getString(cursor.getColumnIndex(myDbHelper.Details)));
            note.setGroupId(cursor.getString(cursor.getColumnIndex(myDbHelper.Group_Id)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(myDbHelper.Title)));
            note.setFile(Long.parseLong(cursor.getString(cursor.getColumnIndex(myDbHelper.file))));
            note.setImage(Long.parseLong(cursor.getString(cursor.getColumnIndex(myDbHelper.Image))));
            note.setSound(Long.parseLong(cursor.getString(cursor.getColumnIndex(myDbHelper.Sound))));
            notes.add(note);
        }
        return notes;
    }
    public ArrayList<Task> getTask() {

        ArrayList<Task> tasks=new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.Title,myDbHelper.NAME,myDbHelper.Group_Id
                ,myDbHelper.Details,myDbHelper.Sound,myDbHelper.Image,myDbHelper.file
        };
        Cursor cursor =db.rawQuery("SELECT * FROM "+myDbHelper.TABLE_NAME_3, null);
        while (cursor.moveToNext())
        {
            Task task=new Task();
            task.setName(cursor.getString(cursor.getColumnIndex(myDbHelper.NAME)));
            task.setDetails(cursor.getString(cursor.getColumnIndex(myDbHelper.Details)));
            task.setTitle(cursor.getString(cursor.getColumnIndex(myDbHelper.Title)));
            task.setEmail(cursor.getString(cursor.getColumnIndex(myDbHelper.Email)));
            tasks.add(task);
        }
        return tasks;
    }
    public ArrayList<String> getTaskId() {

        ArrayList<String> tasks=new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID};
        Cursor cursor =db.rawQuery("SELECT "+myDbHelper.ID+" FROM "+myDbHelper.TABLE_NAME_3, null);
        while (cursor.moveToNext())
        {
            Task task=new Task();
            tasks.add(cursor.getString(cursor.getColumnIndex(myDbHelper.ID)));

        }
        return tasks;
    }
    public ArrayList<Group> getGroup() {

        ArrayList<Group> Groups=new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.Title};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME_4,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            Group Group=new Group();
            Group.setId(cursor.getString(cursor.getColumnIndex(myDbHelper.ID)));
            Group.setName(cursor.getString(cursor.getColumnIndex(myDbHelper.Title)));
            Groups.add(Group);
        }
        return Groups;
    }
    public  int deleteUser(String uname)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uname};

        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.NAME+" = ?",whereArgs);
        return  count;
    }

    public int updateUserName(String oldName , String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
        return count;
    }
    public int updateNote( Note note) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.ID, note.getId());
        contentValues.put(myDbHelper.NAME, note.getName());
        contentValues.put(myDbHelper.Group_Id, note.getGroupId());
        contentValues.put(myDbHelper.Details, note.getDetails());
        contentValues.put(myDbHelper.Title, note.getTitle());
        contentValues.put(myDbHelper.file, note.getFile());
        contentValues.put(myDbHelper.Image, note.getImage());
        contentValues.put(myDbHelper.Sound, note.getSound());
        String[] whereArgs = {note.getId()};
        int count = db.update(myDbHelper.TABLE_NAME_2, contentValues, myDbHelper.ID + " = ?", whereArgs);
    return  count;
    }

        static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myDatabase";
        private static final String TABLE_NAME = "User";
        private static final String TABLE_NAME_2 = "Note";
        private static final String TABLE_NAME_3 = "Task";
        private static final String TABLE_NAME_4 = "group_me";
        private static final String TABLE_NAME_5 = "group_itor";
        private static final int DATABASE_Version = 1;
        private static final String ID="id";
        private static final String Group_Id="group_Id";
        private static final String Title="title";
        private static final String Sound="sound";
        private static final String Image="image";
        private static final String file="file";
        private static final String Details="details";
        private static final String NAME = "Name";
        private static final String Email = "Email";
        private static final String Email2 = "Email_Resever";
        private static final String phone = "Phone";
        private static final String MyPASSWORD= "Password";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+ID+" VARCHAR(225) PRIMARY KEY, "+NAME+" VARCHAR(255) ,"+
                MyPASSWORD+" VARCHAR(225) , "+ Email+" VARCHAR(225) , "+  phone+" VARCHAR(225));";
        private static final String  CREATE_TABLE_2 = "CREATE TABLE "+TABLE_NAME_2+
                " ("+ID+" VARCHAR(225) PRIMARY KEY, "+NAME+" VARCHAR(255) ,"+Title+" VARCHAR(255) ,"
                +Group_Id+" VARCHAR(255) ,"+Sound+" VARCHAR(255) ,"+Image+" VARCHAR(255) ,"
                +file+" VARCHAR(255) ," + Details+" VARCHAR(225));";
        private static final String  CREATE_TABLE_3 = "CREATE TABLE "+TABLE_NAME_3+
                " ("+ID+" VARCHAR(225) PRIMARY KEY, "+NAME+" VARCHAR(255) ,"+Title+" VARCHAR(255) ,"
                +Email+" VARCHAR(255) ,"
                + Details+" VARCHAR(225));";
        private static final String  CREATE_TABLE_4 = "CREATE TABLE "+TABLE_NAME_4+
                " ("+ID+" VARCHAR(225) PRIMARY KEY, "+Title+" VARCHAR(255));";
        private static final String  CREATE_TABLE_5 = "CREATE TABLE "+TABLE_NAME_5+
                " ("+ID+" VARCHAR(225) PRIMARY KEY, "+Title+" VARCHAR(255));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private static final String DROP_TABLE_2 ="DROP TABLE IF EXISTS "+TABLE_NAME_2;
        private static final String DROP_TABLE_3 ="DROP TABLE IF EXISTS "+TABLE_NAME_3;
        private static final String DROP_TABLE_4 ="DROP TABLE IF EXISTS "+TABLE_NAME_4;
        private static final String DROP_TABLE_5 ="DROP TABLE IF EXISTS "+TABLE_NAME_5;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
                db.execSQL(CREATE_TABLE_2);
                db.execSQL(CREATE_TABLE_3);
                db.execSQL(CREATE_TABLE_4);
                db.execSQL(CREATE_TABLE_5);

            } catch (Exception e) {
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                db.execSQL(DROP_TABLE_2);
                db.execSQL(DROP_TABLE_3);
                db.execSQL(DROP_TABLE_4);
                db.execSQL(DROP_TABLE_5);

                onCreate(db);

            }catch (Exception e) {
            }
        }
    }
}
