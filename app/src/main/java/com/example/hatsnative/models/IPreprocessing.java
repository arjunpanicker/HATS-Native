package com.example.hatsnative.models;

import java.util.Vector;

public interface IPreprocessing {
    public Vector<String> toLowerCase(Vector<String> commands);
    public String toLowerCase(String commands);

    public Vector<String> removePunctuations(Vector<String> commands);
    public String removePunctuations(String commands);

    public Vector<String> removeStopWords(Vector<String> commands);
    public String removeStopWords(String commands);

    public Vector<String> convertShortText(Vector<String> values);
    public String convertShortText(String value);

    public Vector<String> pipeline(Vector<String> commands);
    public String pipeline(String command);
}
