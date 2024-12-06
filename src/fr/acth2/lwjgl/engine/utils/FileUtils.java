package fr.acth2.lwjgl.engine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static fr.acth2.lwjgl.utils.Logger.*;

public class FileUtils {
    public static String loadAsString(String path) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(path)))) {
            String line = "";
            while ((line = reader.readLine()) != null){
                result.append(line).append("\n");
            }
        }catch(IOException e) {
            err("Cannot find the file at: " + path);
        }
        return result.toString();
    }
}
