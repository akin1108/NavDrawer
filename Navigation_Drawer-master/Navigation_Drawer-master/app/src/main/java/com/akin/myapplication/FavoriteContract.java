package com.akin.myapplication;

import android.provider.BaseColumns;

/**
 * Created by delaroy on 5/27/17.
 */
public class FavoriteContract {

    public static final class FavoriteEntry implements BaseColumns{

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_QUOTEID = "quoteid";
        //public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE= "image";
        //public static final String COLUMN_TAG1 = "tag#1";
        //public static final String COLUMN_TAG2 = "tag#2";
    }
}
