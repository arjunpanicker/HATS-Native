package com.example.hatsnative.helpers.services;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Utility {

    public static Map<String, Vector<Integer>> labelMap;
    public static Map<Vector<Integer>, String> reverseLabelMap;
    public static int numUniqueLabel;

    /**
     * Creates a one-hot-encoding of the
     * @param data
     * @return one hot encodings of input data
     */
    public static Vector<Vector<Integer>> OneHotEncoder(Vector<String> data) {
        Vector<String> uniqueData = getUniqueData(data);
        oheLabelMapper(data, uniqueData);

        Vector<Vector<Integer>> oneHotEncoding = new Vector<>();

        for (String str : data) {
            oneHotEncoding.add(labelMap.get(str));
        }

        return oneHotEncoding;
    }

    /**
     * Returns the unique values in the list of data
     * @param data
     * @return Unique list of data
     */
    public static Vector<String> getUniqueData(Vector<String> data) {
        Set<String> uniqueData = new HashSet<>(data);
        numUniqueLabel = uniqueData.size();

        Vector<String> uniqueDataList = new Vector<>(uniqueData);
        return uniqueDataList;
    }

    /**
     * Creates a Label -> OHE Mapper
     * @param data
     * @param uniqueData
     */
    public static void oheLabelMapper(Vector<String> data, Vector<String> uniqueData) {
        labelMap = new HashMap<>();

        for (int i = 0; i < uniqueData.size(); ++i) {
            Vector<Integer> ohe = new Vector<>(uniqueData.size());
            for (int j = 0; j < uniqueData.size(); ++j) {
                ohe.add(0);
            }

            ohe.set(i, 1);
            labelMap.put(uniqueData.get(i), ohe);
        }

        reverseLabelMap = new HashMap<>();
        for (String key : labelMap.keySet()) {
            reverseLabelMap.put(labelMap.get(key), key);
        }
    }

    public static int argmax(Vector<Double> dlist) {
        int pos = 0;
        double maxVal = dlist.get(0);
        for (int i = 1; i < dlist.size(); ++i) {
            if (dlist.get(i) > maxVal) {
                pos = i;
                maxVal = dlist.get(i);
            }
        }
        Log.d("Personal Max", String.valueOf(maxVal));
        return pos;
    }

    public static Vector<Integer> getEmptyOHEVector() {
        Vector<Integer> labelOheVec = new Vector<>(Utility.numUniqueLabel);
        for (int i = 0; i < Utility.numUniqueLabel; ++i) {
            labelOheVec.add(0);
        }

        return labelOheVec;
    }
}
