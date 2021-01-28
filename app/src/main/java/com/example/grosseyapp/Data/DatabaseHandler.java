package com.example.grosseyapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.grosseyapp.Model.Grocery;
import com.example.grosseyapp.Util.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROCERY_TABLE= "CREATE TABLE " + Constant.TABLE_NAME + "("
                + Constant.KEY_ID + " INTEGER PRIMARY KEY," + Constant.KEY_GROCERY_ITEM +" TEXT,"
                + Constant.KEY_QTY_NUMBER + " TEXT," + Constant.KEY_DATE_NUMBER + " LONG);";
        db.execSQL(CREATE_GROCERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Constant.TABLE_NAME);
        onCreate(db);
    }

    public void AddGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(Constant.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constant.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constant.KEY_DATE_NUMBER,java.lang.System.currentTimeMillis());
        db.insert(Constant.TABLE_NAME,null,values);

    }

    public Grocery getGrocery(int id){
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor cursor=db.query(Constant.TABLE_NAME,new String[]{ Constant.KEY_ID,
        Constant.KEY_GROCERY_ITEM,Constant.KEY_QTY_NUMBER,Constant.KEY_DATE_NUMBER},
                Constant.KEY_ID + "=?",new String[] {String.valueOf(id)},
                null,null,null,null);
        if (cursor!=null)
            cursor.moveToFirst();

        Grocery grocery=new Grocery();
        grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constant.KEY_ID))));
        grocery.setName(cursor.getString(cursor.getColumnIndex(Constant.KEY_GROCERY_ITEM)));
        grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constant.KEY_QTY_NUMBER)));

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constant.KEY_DATE_NUMBER))).getTime());

        grocery.setDateItemAdded(formatedDate);

        return grocery;
    }

    public List<Grocery> getAllGroceries(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Grocery> groceryList = new ArrayList<>();

        Cursor cursor=db.query(Constant.TABLE_NAME,new String[]{
                Constant.KEY_ID,Constant.KEY_GROCERY_ITEM,Constant.KEY_QTY_NUMBER,
                Constant.KEY_DATE_NUMBER},null,null,null,null,Constant.KEY_DATE_NUMBER + " DESC");

        if (cursor.moveToFirst()){
            do{
                Grocery grocery=new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constant.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constant.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constant.KEY_QTY_NUMBER)));

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constant.KEY_DATE_NUMBER))).getTime());

                grocery.setDateItemAdded(formatedDate);
                groceryList.add(grocery);

            }while (cursor.moveToNext());
        }

        return groceryList;
    }

    public int updateGrocery(Grocery grocery){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constant.KEY_QTY_NUMBER,grocery.getQuantity());
        values.put(Constant.KEY_DATE_NUMBER,java.lang.System.currentTimeMillis());

        return db.update(Constant.TABLE_NAME,values,Constant.KEY_ID + "=?", new String[]{String.valueOf(grocery.getId())});
    }

    public void deleteGrocery(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Constant.TABLE_NAME,Constant.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public int getGroceryCount(){
        SQLiteDatabase db=this.getReadableDatabase();
        String CountQuery="SELECT*FROM " + Constant.TABLE_NAME;
        Cursor cursor = db.rawQuery(CountQuery,null);
        return cursor.getCount();
    }
}
