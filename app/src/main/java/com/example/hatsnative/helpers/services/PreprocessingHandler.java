package com.example.hatsnative.helpers.services;

import com.example.hatsnative.models.IPreprocessing;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class PreprocessingHandler implements IPreprocessing {
    private static InputStream stopwords_inputstream = null;
    private static InputStream shorttext_inputstream = null;
    private static Vector<String> stopWords;
    private static HashMap<String, Vector<String>> shorttextData;

    public PreprocessingHandler(InputStream stopWordsInputStream, InputStream shorttextInputStream) {
        stopwords_inputstream = stopWordsInputStream;
        shorttext_inputstream = shorttextInputStream;
        getData();
    }

    private static void getData() {
        stopWords = DatasetHandler.readTextFile(stopwords_inputstream);
        shorttextData = DatasetHandler.readShortTextCSV(shorttext_inputstream);
    }

    public PreprocessingHandler() {}

    @Override
    public Vector<String> toLowerCase(Vector<String> commands) {
        Vector<String> preprocessedCommands = new Vector<String>();
        for (int i = 0; i < commands.size(); ++i) {
            preprocessedCommands.add(commands.get(i).toLowerCase());
        }

        return preprocessedCommands;
    }

    @Override
    public String toLowerCase(String commands) {
        return commands.toLowerCase();
    }

    @Override
    public Vector<String> removePunctuations(Vector<String> commands) {
        // TODO: Need to implement later
        return commands;
    }

    @Override
    public String removePunctuations(String commands) {
        // TODO: Need to implement later
        return commands;
    }

    @Override
    public Vector<String> removeStopWords(Vector<String> commands) {
        Vector<String> preprocessedCommands = new Vector<>();
        if (!stopWords.isEmpty()) {
            for (String command : commands) {
                preprocessedCommands.add(removeStopWords(command));
            }
        }

        return preprocessedCommands;
    }

    @Override
    public String removeStopWords(String command) {
        StringBuilder builder = new StringBuilder();
        String[] words = command.split("\\s+");

        for (int i = 0; i < words.length; i++) {
//            words[i] = words[i].replaceAll("^\\w", "");

            if (!stopWords.contains(words[i])) {
                builder.append(words[i]);
                builder.append(" ");
            }
        }

        return builder.toString().trim();
    }

    @Override
    public Vector<String> convertShortText(Vector<String> values) {
        Vector<String> preprocessedCommands = new Vector<>();

        if (!shorttextData.isEmpty()) {
            for (String command : values) {
                preprocessedCommands.add(convertShortText(command));
            }
        }

        return preprocessedCommands;
    }

    @Override
    public String convertShortText(String value) {
        StringBuilder builder = new StringBuilder();
        String[] words = value.split("\\s+");

        for (String word : words) {
            boolean matchFound = false;
            for (String header : shorttextData.keySet()) {
                if (shorttextData.get(header).contains(word)) {
                    matchFound = true;
                    builder.append(header);
                    break;
                }
            }

            if (!matchFound) {
                builder.append(word);
            }
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    @Override
    public Vector<String> pipeline(Vector<String> commands) {
        Vector<String> preprocessedData = toLowerCase(commands);
        preprocessedData = removePunctuations(preprocessedData);
        preprocessedData = removeStopWords(preprocessedData);
        preprocessedData = convertShortText(preprocessedData);

        return preprocessedData;
    }

    @Override
    public String pipeline(String command) {
        Vector<String> commands = new Vector<>(Arrays.asList(command));

        Vector<String> preprocessedData = toLowerCase(commands);
        preprocessedData = removePunctuations(preprocessedData);
        preprocessedData = removeStopWords(preprocessedData);
        preprocessedData = convertShortText(preprocessedData);

        return preprocessedData.get(0);
    }
}
