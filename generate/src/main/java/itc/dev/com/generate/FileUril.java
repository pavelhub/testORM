package itc.dev.com.generate;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Pavel on 19.10.2015.
 */
public class FileUril {
    public static final String VOICE_TEMP_FILE = "model.txt";
    public static final String CACHE_FOLDER = "text_orm";
    public static final String FLODER_SD = Environment.getExternalStorageDirectory().getAbsolutePath();

    //    public static final String VOICE_TEMP_PATH = FLODER_SD+ File.separator + VOICE_TEMP_FILE;
    public static void deleteCacheFolder() {
        deleteFiles(new File(FLODER_SD, CACHE_FOLDER));
    }

    public static File getModelFile() {
        File file = new File(FLODER_SD, CACHE_FOLDER);
        if (!file.exists())
            file.mkdir();

        file = new File(file, VOICE_TEMP_FILE);

        return file;
    }

    public static void deleteFiles(File dirOfFiles) {
        if (dirOfFiles.exists()) {
            if (dirOfFiles.isDirectory()) {
                for (File file : dirOfFiles.listFiles()) {
                    if (file.isDirectory()) {
                        if (file.list().length > 0) {
                            deleteFiles(file);
                        }
                        file.delete();

                    } else {
                        file.delete();

                    }
                }
                dirOfFiles.delete();
            } else {
                dirOfFiles.delete();

            }
        }
    }


    public static String readFromFile(File file) {

        String ret = "";

        try {
            InputStream inputStream = new FileInputStream(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static void saveToFile(String content, File file) {

        FileOutputStream outputStream = null;

        try {
            // write the inputStream to a FileOutputStream
            if (!file.exists())
                file.createNewFile();
            outputStream =
                    new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        System.out.println("Done!");

    }
}
