package com.example.hatsnative.helpers.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Utility {

    public static Map<String, Vector<Integer>> labelMap;
    public static Map<Vector<Integer>, String> reverseLabelMap;

    /**
     * Creates a one-hot-encoding of the
     * @param data
     * @return
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
        Set<String> uniqueData = new HashSet<>();
        uniqueData.addAll(data);

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
}
