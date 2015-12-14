/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
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
package generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * <p>
 * Run it as a Java application (not Android).
 *
 * @author Markus
 */
public class DMPDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "itc.dev.com.greendao");
        addUsers(schema);
//        add_tag(schema);
//        add_tagName(schema);
        new DaoGenerator().generateAll(schema, args[0]);


    }

    private static void addUsers(Schema schema) {
        Entity article = schema.addEntity("User");
        article.setTableName("user");
        article.addLongProperty("_id").primaryKey();
        article.addStringProperty("email");
        article.addStringProperty("password");
        article.addStringProperty("first_name");
        article.addStringProperty("last_name");
        article.addStringProperty("photo_url");
        article.addStringProperty("phone_code");
        article.addStringProperty("phone_value");
        article.addIntProperty("age");
        article.addBooleanProperty("isAdmin");
        article.addFloatProperty("height");

    }


}
