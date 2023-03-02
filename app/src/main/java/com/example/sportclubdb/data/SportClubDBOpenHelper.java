package com.example.sportclubdb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sportclubdb.data.ClubContract.MemberEntry;

public class SportClubDBOpenHelper extends SQLiteOpenHelper {
    public SportClubDBOpenHelper(Context context) {
        super(context, ClubContract.DATABASE_NAME,
                null, ClubContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MEMBERS_TABLE = "CREATE TABLE " + MemberEntry.TABLE_NAME + "("
                + MemberEntry.KEY_ID + " INTEGER PRIMARY KEY,"
                + MemberEntry.KEY_NAME + " TEXT,"
                + MemberEntry.KEY_LAST_NAME + " TEXT,"
                + MemberEntry.KEY_GENDER + " INTEGER NOT NULL,"
                + MemberEntry.KEY_SPORT + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_MEMBERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClubContract.DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}
