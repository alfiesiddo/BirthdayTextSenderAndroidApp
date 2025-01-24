package com.example.happybirthdaysenderv3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseOperations {
    private SQLiteDatabase db;
    private BirthdayDatabaseHelper dbH;

    public DatabaseOperations (Context c){
        dbH = new BirthdayDatabaseHelper(c);
    }
    public void open(){
        db = dbH.getWritableDatabase();
    }
    public void close(){
        dbH.close();
    }
    public long addPerson(String name, String phone, String birthday, String message){
        ContentValues values = new ContentValues();

        values.put(BirthdayDatabaseHelper.KEY_NAME, name);
        values.put(BirthdayDatabaseHelper.KEY_MOBILENUM, phone);
        values.put(BirthdayDatabaseHelper.KEY_BIRTHDAY, birthday);
        values.put(BirthdayDatabaseHelper.KEY_TEXT, message);
        return db.insert(BirthdayDatabaseHelper.TABLE_PERSON, null, values);
    }
    public String remPerson(String name){
        Person p = getPersonByName(name);
        if(p == null){
            return "Cannot delete as person doesn't exist";
        }
        else{
            db.delete(BirthdayDatabaseHelper.TABLE_PERSON, BirthdayDatabaseHelper.KEY_NAME + " = ?", new String[] {name});
            return "Person removed!";
        }
    }

    public String editPerson(String name, String newText) {
        Person p = getPersonByName(name);
        if (p == null) {
            return "Person doesn't exist, check you've entered their name correctly.";
        }
        // Prepare new values for update
        ContentValues values = new ContentValues();
        values.put(BirthdayDatabaseHelper.KEY_TEXT, newText);

        String[] whereArgs = { name };
        int rowsAffected = db.update(BirthdayDatabaseHelper.TABLE_PERSON, values, BirthdayDatabaseHelper.KEY_NAME + " = ?", whereArgs);

        if (rowsAffected > 0) {
           //person updated
        } else {
           //person not updated
        }
        return "Text updated successfully!";
    }
    private Person getPersonByName(String name) {
        Person person = null; // Initialize person as null

        // Perform the query
        Cursor cursor = db.query(BirthdayDatabaseHelper.TABLE_PERSON, null,
                BirthdayDatabaseHelper.KEY_NAME + " = ?",
                new String[]{name}, null, null, null);

        // Check if the cursor is not null and has at least one result
        if (cursor != null && cursor.moveToFirst()) {
            // Convert the first row of the cursor into a Person object
            person = cursorToPerson(cursor);
        }

        // Close the cursor to release resources
        if (cursor != null) {
            cursor.close();
        }

        return person; // Return the Person object or null if not found
    }
    public ArrayList<Person> getAllPersons(){
        ArrayList<Person> list = new ArrayList<Person>();
        Cursor cursor = db.query(BirthdayDatabaseHelper.TABLE_PERSON, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Person p = cursorToPerson(cursor);
            list.add(p);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public ArrayList<Person> getAllCurrentBirthdays(){
        Calendar cal = Calendar.getInstance();
        ArrayList<Person> list = new ArrayList<Person>();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String combinedDate = day + "/" + month;
        String selection = BirthdayDatabaseHelper.KEY_BIRTHDAY + " = ?";
        String[] selectionArgs = {combinedDate};

        Cursor cursor = db.query(BirthdayDatabaseHelper.TABLE_PERSON, null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Person p = cursorToPerson(cursor);
                list.add(p);
                cursor.moveToNext();
            }
            cursor.close();
            for (Person o: list) {
                Log.d("test", o.toString());

            }
        }
        return list;
    }
    private Person cursorToPerson(Cursor c) {
        Person person = new Person();

        int nameIndex = c.getColumnIndex(BirthdayDatabaseHelper.KEY_NAME);
        if (nameIndex >= 0) {
            person.setName(c.getString(nameIndex));
        }

        int phoneIndex = c.getColumnIndex(BirthdayDatabaseHelper.KEY_MOBILENUM);
        if (phoneIndex >= 0) {
            person.setPhone(c.getString(phoneIndex));
        }

        int birthdayIndex = c.getColumnIndex(BirthdayDatabaseHelper.KEY_BIRTHDAY);
        if (birthdayIndex >= 0) {
            person.setBirthday(c.getString(birthdayIndex));
        }

        int messageIndex = c.getColumnIndex(BirthdayDatabaseHelper.KEY_TEXT);
        if (messageIndex >= 0) {
            person.setText(c.getString(messageIndex));
        }
        return person;
    }

}
