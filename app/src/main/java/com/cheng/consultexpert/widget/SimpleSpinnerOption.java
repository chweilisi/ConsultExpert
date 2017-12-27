package com.cheng.consultexpert.widget;

/**
 * Created by cheng on 2017/12/21.
 */

public class SimpleSpinnerOption {
    private String name;

    private Object value;

    public SimpleSpinnerOption(){
        this.name="";
        this.value="";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
