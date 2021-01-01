package com.example.hatsnative.helpers.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatasetHandler {

    public static List readCsv(InputStream inputStream) {
        List resultList = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] row = csvLine.split(";");
                resultList.add(row);
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

        return resultList;
    }

}
