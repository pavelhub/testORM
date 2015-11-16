/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package itc.dev.com.sqlbriteprovider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public final class DbOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    private static final String CREATE_LIST = ""
            + "CREATE TABLE " + Users.TABLE + "("
            + Users.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Users.FIRST_NAME + " TEXT NOT NULL,"
            + Users.LAST_NAME + " TEXT NOT NULL,"
            + Users.EMAIL + " TEXT NOT NULL,"
            + Users.PASSWORD + " TEXT NOT NULL,"
            + Users.PHOTO_URL + " TEXT NOT NULL,"
            + Users.PHONE_CODE + " TEXT NOT NULL,"
            + Users.PHONE_VALUE + " TEXT NOT NULL,"
            + Users.AGE + " INTEGER NOT NULL,"
            + Users.HEIGHT + " FLOAT NOT NULL,"
            + Users.IS_ADMIN + " INTEGER NOT NULL DEFAULT 0"
            + ")";


    public DbOpenHelper(Context context, File fileDB) {
        super(context, fileDB.getAbsolutePath(), null /* factory */, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
