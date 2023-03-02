package com.example.sportclubdb.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ClubContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "club";

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.sportclubdb";
    public static final String PATH_MEMBER = "members";

    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);


    private ClubContract(){}

    public static final class MemberEntry implements BaseColumns {

        public static final String TABLE_NAME = "members";

        public static final String KEY_ID = BaseColumns._ID;
        public static final String KEY_NAME = "name";
        public static final String KEY_LAST_NAME = "lastName";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_SPORT = "sport";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBER);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBER;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBER;
    }

}
