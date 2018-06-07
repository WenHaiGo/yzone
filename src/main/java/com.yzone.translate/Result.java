package com.yzone.translate;

import java.util.List;

public class Result {

    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<trans_result> getList() {
        return list;
    }

    public void setList(List<trans_result> list) {
        this.list = list;
    }

    private  String to;
    private List<trans_result> list;
}
