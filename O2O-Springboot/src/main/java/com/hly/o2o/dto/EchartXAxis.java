package com.hly.o2o.dto;

import java.util.HashSet;

/**
 * ExchartXAxis中的xAxis项
 */
public class EchartXAxis {
    private String type = "category";
    // 去重
    private HashSet<String> data;

    public HashSet<String> getData() {
        return data;
    }

    public void setData(HashSet<String> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }
}
