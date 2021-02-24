package com.example.hatsnative.helpers.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DatasetHandler {

    public static ArrayList<String> readCsv(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        ArrayList<String> csvData = new ArrayList<>();
        try {
            String csvLine;
            while ((csvLine = bufferedReader.readLine()) != null) {
                sb.append(csvLine);
                sb.append("\n");
                csvData.add(csvLine);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading csv: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing csv: " + e);
            }
        }
        String result = sb.toString();
        return csvData;
    }

    public static void writeTempCsv(String content) {
        // TODO: Complete this code
    }

}
