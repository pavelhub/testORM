package itc.dev.com.generate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Random;
import java.util.UUID;

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


    public static void generateAndSaveToFile() {
        Gson gson = new Gson();
        JsonArray rootArrat = new JsonArray();
        for (int i = 0; i < 30000; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", i);
            Random random = new Random(i);
            jsonObject.addProperty("firstName", firstName[random.nextInt(firstName.length)]);
            jsonObject.addProperty("lastName", lastNames[random.nextInt(lastNames.length)]);
            String emailString = email[random.nextInt(email.length)] + "@" + domain[random.nextInt(domain.length)];
            jsonObject.addProperty("email", emailString);
            jsonObject.addProperty("photoUrl", domain[random.nextInt(domain.length)] + "/" + UUID.randomUUID().toString() + ".jpg");
            jsonObject.addProperty("phoneCode", phoneCode[random.nextInt(phoneCode.length)]);
            jsonObject.addProperty("phone", phone[random.nextInt(phone.length)]);
            jsonObject.addProperty("isGood", random.nextBoolean());
            jsonObject.addProperty("height", height[random.nextInt(height.length)]);

            rootArrat.add(jsonObject);
        }
        File modelFile = FileUril.getModelFile();

        FileUril.saveToFile(gson.toJson(rootArrat), modelFile);

    }

    public static JsonElement restoreFromFileModel() {
        String content = FileUril.readFromFile(FileUril.getModelFile());
        return new Gson().fromJson(content, JsonArray.class);
    }

}
