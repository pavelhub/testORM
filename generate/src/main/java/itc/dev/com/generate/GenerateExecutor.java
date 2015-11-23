package itc.dev.com.generate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
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


    public static List<User> generateAndSaveToFile() {
        Gson gson = new Gson();
        List<User> listUsers = new LinkedList<>();
        for (int i = 0; i < 50000; i++) {
            Random random = new Random(i);
            User user = new User();
            user.id = i;
            user.first_name = firstName[random.nextInt(firstName.length)];
            user.last_name = lastNames[random.nextInt(lastNames.length)];
            String emailString = email[random.nextInt(email.length)] + "@" + domain[random.nextInt(domain.length)];
            user.email = emailString;
            user.password = String.valueOf(i) + user.email.hashCode();
            user.photo_url = domain[random.nextInt(domain.length)] + "/" + UUID.randomUUID().toString() + ".jpg";
            user.phone_code = phoneCode[random.nextInt(phoneCode.length)];
            user.phone_value = phone[random.nextInt(phone.length)];
            user.isAdmin = random.nextBoolean();
            user.height = height[random.nextInt(height.length)];
            user.age = random.nextInt(30) + 16;
            listUsers.add(user);

        }
        File modelFile = FileUril.getModelFile();

        FileUril.saveToFile(gson.toJson(listUsers), modelFile);
        return listUsers;
    }

    public static List<User> restoreFromFileModel() {
        String content = FileUril.readFromFile(FileUril.getModelFile());
        Type listType = new TypeToken<LinkedList<User>>() {
        }.getType();
        return new Gson().fromJson(content, listType);
    }

}
