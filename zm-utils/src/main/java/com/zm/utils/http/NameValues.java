package com.zm.utils.http;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * http post request form data
 * @author zhangZhenfei
 */
public class NameValues implements Iterable<BasicNameValuePair> {

    public NameValues(List<BasicNameValuePair> nameValues) {
        this.nameValues = nameValues;
    }

    private List<BasicNameValuePair> nameValues;

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<BasicNameValuePair> iterator() {
        if (this.nameValues == null) {
            nameValues = new ArrayList<>();
        }
        return nameValues.iterator();
    }

    public List<BasicNameValuePair> getNameValues() {
        return nameValues;
    }

    public void setNameValues(List<BasicNameValuePair> nameValues) {
        this.nameValues = nameValues;
    }
}