package ru.nsu.fit.fedyaeva.template;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, IncorrectFormException {
        try {
            String text = getFileString();
            ru.nsu.fit.fedyaeva.template.Template tmp = new ru.nsu.fit.fedyaeva.template.Template(text);
            Map<String, String> values = new HashMap<>();
            Map<String, Boolean> conditions = new HashMap<>();
            Map<String, Integer> iterations = new HashMap<>();
            values.put("user", "Dolly");
            values.put("name", "Vasya");
            conditions.put("Want Sleep", true);
            conditions.put("isUser", true);
            iterations.put("count iter", 5);
            String result = tmp.fill(values, conditions, iterations);
            System.out.println(result);
        } catch (IncorrectFormException | IOException | ArgumentNotFoundException formException) {
            System.out.println(formException.getMessage());
        }
    }

    private static String getFileString() throws IOException {
        FileInputStream fStream = new FileInputStream("template.txt");
        BufferedReader inStrReader = new BufferedReader(new InputStreamReader(fStream));
        StringBuilder res = new StringBuilder();
        String curLine;
        while ((curLine = inStrReader.readLine()) != null) {
            curLine += "\n";
            res.append(curLine);
        }
        return res.toString();
    }
}
