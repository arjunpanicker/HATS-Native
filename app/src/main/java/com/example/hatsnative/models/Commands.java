package com.example.hatsnative.models;

import com.opencsv.bean.CsvBindByName;

public class Commands {
    @CsvBindByName
    private String commands;

    @CsvBindByName
    private String label;
}
