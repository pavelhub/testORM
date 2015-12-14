package itc.dev.com.generate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import itc.dev.com.baseprovider.FileUril;
import itc.dev.com.baseprovider.UserModel;

/**
 * Created by pavel on 11/10/15.
 */
public class GenerateExecutor {
    private static String[] firstName = new String[]{"Natalia", "Andrey", "Konstantin", "Artem", "Pavel", "Sem", "Mat", "Gosef", "Molli", "Holli", "Devide", "Will"};
    private static String[] lastNames = new String[]{"Dmuh", "Drihulias", "Barbin", "Uvarenko", "Hubskiy", "Havech", "Immen", "Zoo", "Malbert", "Igoshev", "Tarasenko"};
    private static float[] height = new float[]{165.5f, 190.2f, 190.3f, 175.7f, 187.2f};
    private static String[] phone = new String[]{"555-22-555", "4454-2265-45464", "46546-4564-646546", "65465-4654-46", "844-4564-444"};
    private static String[] phoneCode = new String[]{"555", "445", "465", "654", "844"};
    private static String[] email = new String[]{"sem", "matt", "wilson", "endry", "lopitsh"};
    private static String[] domain = new String[]{"teach.com", "mail.ru", "gmail.com", "yandex.ua", "mall.li", "arish.pub"};


    public static List<UserModel> generateAndSaveToFile(int count) {
        Gson gson = new Gson();
        List<UserModel> listUserModels = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            Random random = new Random(i);
            UserModel userModel = new UserModel();
            userModel.id = i + 1;
            userModel.first_name = firstName[random.nextInt(firstName.length)];
            userModel.last_name = lastNames[random.nextInt(lastNames.length)];
            String emailString = email[random.nextInt(email.length)] + "@" + domain[random.nextInt(domain.length)];
            userModel.email = emailString;
            userModel.password = String.valueOf(i) + userModel.email.hashCode();
            userModel.photo_url = domain[random.nextInt(domain.length)] + "/" + UUID.randomUUID().toString() + ".jpg";
            userModel.phone_code = phoneCode[random.nextInt(phoneCode.length)];
            userModel.phone_value = phone[random.nextInt(phone.length)];
            userModel.isAdmin = random.nextBoolean();
            userModel.height = height[random.nextInt(height.length)];
            userModel.age = random.nextInt(30) + 16;
            listUserModels.add(userModel);

        }
        File modelFile = FileUril.getModelFile();

//        FileUril.saveToFile(gson.toJson(listUserModels), modelFile);
        return listUserModels;
    }

    public static List<UserModel> restoreFromFileModel() {
        String content = FileUril.readFromFile(FileUril.getModelFile());
        Type listType = new TypeToken<LinkedList<UserModel>>() {
        }.getType();
        return new Gson().fromJson(content, listType);
    }

    public static JsonArray restoreFromFileJsonModel() {
        String content = FileUril.readFromFile(FileUril.getModelFile());
        Type listType = new TypeToken<JsonArray>() {
        }.getType();
        return new Gson().fromJson(content, listType);
    }

}
