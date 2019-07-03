package com.springboot.workflow.util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtilsExt {
    public static final String charset = "UTF-8";

    public static String readFileToString(Object currentObject, String fileName) {
        String content = null;
        try {
            URL url = currentObject.getClass().getResource("/");
            if (url != null)
                content = new String(Files.readAllBytes(Paths.get(url.toString().substring(6) + fileName)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String readFileToString(String filePath) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}