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

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import auto.parcel.AutoParcel;
import rx.functions.Func1;

// Note: normally I wouldn't prefix table classes but I didn't want 'List' to be overloaded.
@AutoParcel
public abstract class Users {
    public static final String TABLE = "users";

    public static final String ID = "_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PHOTO_URL = "photo_url";
    public static final String PHONE_CODE = "phone_code";
    public static final String PHONE_VALUE = "phone_value";
    public static final String IS_ADMIN = "is_admin";
    public static final String AGE = "age";
    public static final String HEIGHT = "height";

    public abstract long id();

    public abstract String email();

    public abstract String password();

    public abstract String first_name();

    public abstract String last_name();

    public abstract String photo_url();

    public abstract String phone_code();

    public abstract String phone_value();

    public abstract boolean isAdmin();

    public abstract int age();

    public abstract float height();


    public static Func1<Cursor, List<Users>> MAP = new Func1<Cursor, List<Users>>() {
        @Override
        public List<Users> call(final Cursor cursor) {
            try {

                List<Users> usersList = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    long id = Db.getLong(cursor, ID);
                    String fN = Db.getString(cursor, FIRST_NAME);
                    String lN = Db.getString(cursor, LAST_NAME);
                    String email = Db.getString(cursor, EMAIL);
                    String pass = Db.getString(cursor, PASSWORD);
                    String photo = Db.getString(cursor, PHOTO_URL);
                    String phoneCode = Db.getString(cursor, PHONE_CODE);
                    String phoneValue = Db.getString(cursor, PHONE_VALUE);
                    int age = Db.getInt(cursor, AGE);
                    boolean isAdmin = Db.getBoolean(cursor, IS_ADMIN);
                    float height = Db.getFloat(cursor, HEIGHT);
                    usersList.add(new AutoParcel_Users(id, email, pass, fN, lN, photo, phoneCode, phoneValue, isAdmin, age, height));
                }
                return usersList;//new AutoParcel_Users();
            } finally {
                cursor.close();
            }
        }
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(ID, id);
            return this;
        }

        public Builder firstName(String firstName) {
            values.put(FIRST_NAME, firstName);
            return this;
        }

        public Builder lastName(String lastName) {
            values.put(LAST_NAME, lastName);
            return this;
        }

        public Builder email(String email) {
            values.put(EMAIL, email);
            return this;
        }

        public Builder password(String password) {
            values.put(PASSWORD, password);
            return this;
        }

        public Builder photoUrl(String photoUrl) {
            values.put(PHOTO_URL, photoUrl);
            return this;
        }

        public Builder phoneCode(String phoneCode) {
            values.put(PHONE_CODE, phoneCode);
            return this;
        }

        public Builder phoneValue(String phoneValue) {
            values.put(PHONE_VALUE, phoneValue);
            return this;
        }

        public Builder isAdmin(boolean isAdmin) {
            values.put(IS_ADMIN, isAdmin);
            return this;
        }

        public Builder age(int age) {
            values.put(AGE, age);
            return this;
        }

        public Builder height(float height) {
            values.put(HEIGHT, height);
            return this;
        }

        public ContentValues build() {
            return values; // TODO defensive copy?
        }
    }
}
