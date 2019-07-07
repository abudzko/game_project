package com.game.utils;

import com.game.utils.log.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

    public static String loadAsString(String path) {
        StringBuilder resultBuilder = new StringBuilder();
        try (
                InputStream inputStream = new FileInputStream(new File(path));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                resultBuilder.append(line);
                resultBuilder.append("\r\n");
            }
        } catch (IOException e) {
            LogUtil.logError(e.getMessage(), e);
        }
        return resultBuilder.toString();
    }
}
