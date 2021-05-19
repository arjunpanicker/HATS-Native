package com.example.hatsnative.helpers.services;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.hatsnative.helpers.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

public class DatasetHandler {
    static JSONObject writeW;
    static JSONObject readW;

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

    public static void writeWeights(LinkedHashMap<Integer, List<List<Double>>> data, Context context) {
        AssetManager assetManager = context.getAssets();
        try {
//            File file = new File(Constants.WEIGHTS_FILE);
//            file.createNewFile();
            OutputStream os = context.openFileOutput(Constants.WEIGHTS_FILE, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os);
            JSONObject json = new JSONObject();
            for (int l = 0; l < data.size() - 1; l++) {
                List<List<Double>> layerWeights = data.get(l);
                JSONObject layerJson = new JSONObject();
                for (int n = 0; n < layerWeights.size(); n++) {

                    JSONArray weightArrayJson = new JSONArray();
                    for (Double w : layerWeights.get(n)) {
                        weightArrayJson.put(w);
                    }
                    layerJson.put(String.valueOf(n), weightArrayJson);
                }
                json.put(String.valueOf(l), layerJson);
            }
            writeW = json;
            out.write(json.toString());
            out.flush();
            out.close();
            os.close();
        } catch (JSONException ex) {
            Log.e("Exception", "JSON failed: " + ex.toString());
        }
        catch (IOException ex) {
            Log.e("Exception", "Weights write failed: " + ex.toString());
        }
    }

    public static JSONObject readWeights(Context context) {
        AssetManager assetManager = context.getAssets();
        StringBuilder sb = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        JSONObject json = null;
        try {
            inputStreamReader = new InputStreamReader(
                    context.openFileInput(Constants.WEIGHTS_FILE), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                sb.append(content);
            }

            json = new JSONObject(sb.toString());

            inputStreamReader.close();
        } catch (JSONException ex) {
            Log.e("Exception", "JSON failed: " + ex.toString());
        }
        catch (IOException ex) {
            return null;
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader = null;
            }
        }
        readW = json;
        return json;
    }

    public static boolean checkWeights() {
        return readW.equals(writeW);
    }
}
