package com.example.hatsnative.helpers.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class DatasetHandler {

//    public static ArrayList<ArrayList<String>> readCsv(InputStream inputStream) {
//        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        ArrayList<ArrayList<String>> csvData = new ArrayList<>();
//        String[] headers = {};
//        try {
//            String csvLine;
//            while ((csvLine = bufferedReader.readLine()) != null) {
//                csvLine.split("\\;")
//                csvData.add(csvLine);
//            }
//        } catch (IOException ex) {
//            throw new RuntimeException("Error in reading csv: " + ex);
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                throw new RuntimeException("Error while closing csv: " + e);
//            }
//        }
//        return csvData;
//    }

    public static Vector<String> readTextFile(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        Vector<String> stopWords = new Vector<>();
        try {
            String word;
            while ((word = bufferedReader.readLine()) != null) {
                sb.append(word);
                sb.append("\n");
                stopWords.add(word);
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

        return stopWords;
    }

        public static HashMap<String, Vector<String>> readCSV(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        HashMap<String, Vector<String>> table = new HashMap<String, Vector<String>>();
        String[] headers = {};
        try {
            String csvLine;
            int count = 0;
            while ((csvLine = bufferedReader.readLine()) != null) {
                if (count == 0) {
                    headers = csvLine.split("\\;");
                    for (String header : headers) {
                        table.put(header, new Vector<String>());
                    }
                    count++;
                } else {
                    String[] values = csvLine.split("\\;");
                    for (int i = 0; i < values.length; i++) {
                        if (!values[i].isEmpty()) {
                            Vector<String> curData = table.get(headers[i]);

                            curData.add(values[i]);
                            table.put(headers[i], curData);
                        }
                    }
                }
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

        return table;
    }

    public static void writeTempCsv(String content) {
        // TODO: Complete this code
    }

}
