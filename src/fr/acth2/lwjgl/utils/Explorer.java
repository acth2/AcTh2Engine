package fr.acth2.lwjgl.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Explorer {
    public static boolean isExplorerRunning() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("explorer.exe")) {
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
