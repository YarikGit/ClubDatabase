package com.example.sportclubdb.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.sportclubdb.data.ClubContract.*;

public class SportClubContentProvider extends ContentProvider {

    SportClubDBOpenHelper sportClubDBOpenHelper;

    private static final int MEMBERS = 111;
    private static final int MEMBER_ID = 222;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ClubContract.AUTHORITY, ClubContract.PATH_MEMBER, MEMBERS);
        uriMatcher.addURI(ClubContract.AUTHORITY, ClubContract.PATH_MEMBER + "/#", MEMBER_ID);

    }

    @Override
    public boolean onCreate() {
        sportClubDBOpenHelper = new SportClubDBOpenHelper(getContext());
        return true;
    }


    @Override
    //uri = content://com.example.sportclubdb/clubMembers/34
    //strings = {"lastName", "gender"}  - якщо ми передамо ці два аргументи
    public Cursor query(Uri uri,String[] strings,String s,String[] strings1,String s1) {
        SQLiteDatabase db = sportClubDBOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                cursor = db.query(MemberEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
                // s = "KEY_ID=?"
                // strings1 = 34
            case MEMBER_ID:
                s = MemberEntry.KEY_ID + "=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};  // ContentUris.parseId(uri) - перетворює останній сегмент в число
                cursor = db.query(MemberEntry.TABLE_NAME, strings, s, strings1, null,null, s1);
                break;
            default:
                throw new IllegalArgumentException("Can't query incorrect URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        if(contentValues.containsKey(MemberEntry.KEY_NAME)) {
            String name = contentValues.getAsString(MemberEntry.KEY_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Insertion of name failed for " + uri);
            }
        }
        if(contentValues.containsKey(MemberEntry.KEY_LAST_NAME)) {
            String lastName = contentValues.getAsString(MemberEntry.KEY_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("Insertion of last name failed for " + uri);
            }
        }
        if(contentValues.containsKey(MemberEntry.KEY_GENDER)) {
            int gender = contentValues.getAsInteger(MemberEntry.KEY_GENDER);
            if (!(gender == MemberEntry.GENDER_UNKNOWN || gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("Insertion of gender failed for " + uri);
            }
        }
        if(contentValues.containsKey(MemberEntry.KEY_SPORT)) {
            String sport = contentValues.getAsString(MemberEntry.KEY_SPORT);
            if (sport == null) {
                throw new IllegalArgumentException("Insertion of sport failed for " + uri);
            }
        }

        SQLiteDatabase db = sportClubDBOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                long id = db.insert(MemberEntry.TABLE_NAME,null,contentValues);

                if(id == -1){
                    throw new IllegalArgumentException("Insertion of id failed for " + uri);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri,id);                                          // - метод поакртає uri з додаванням id - content://com.example.sportclubdb/clubMembers + "/id"
            default:
                throw new IllegalArgumentException("Insertion of data in the table failed for " + uri);
        }
    }

    @Override
    public int delete(Uri uri,String s,String[] strings) {
        SQLiteDatabase db = sportClubDBOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case MEMBERS:
                rowsDeleted = db.delete(MemberEntry.TABLE_NAME, s, strings);
                break;
            // s = "KEY_ID=?"
            // strings1 = 34
            case MEMBER_ID:
                s = MemberEntry.KEY_ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};  // ContentUris.parseId(uri) - перетворює останній сегмент в число
                rowsDeleted = db.delete(MemberEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Can't delete incorrect URI " + uri);
        }

        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri,ContentValues contentValues,String s,String[] strings) {
        SQLiteDatabase db = sportClubDBOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsUpdate;

        switch (match){
            case MEMBERS:
                rowsUpdate = db.update(MemberEntry.TABLE_NAME, contentValues, s, strings);
                break;
            // s = "KEY_ID=?"
            // strings1 = 34
            case MEMBER_ID:
                s = MemberEntry.KEY_ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};  // ContentUris.parseId(uri) - перетворює останній сегмент в число
                rowsUpdate = db.update(MemberEntry.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Can't update incorrect URI " + uri);
        }

        if(rowsUpdate != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdate;

    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                return MemberEntry.CONTENT_MULTIPLE_ITEMS;
            case MEMBER_ID:
                return MemberEntry.CONTENT_SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


}
/*
URI - Unified Resource Identifier
content://com.example.sportclubdb/clubMembers
URL - Unified Resource Locator
http://google.com
content://com.example.sportclubdb/clubMembers/34 - для роботи з конкретним id
content://com.example.sportclubdb/clubMembers    - для роботи з всiєю таблицею
content:// - scheme
com.android.contacts - content authority
contacts - type of data
 */
