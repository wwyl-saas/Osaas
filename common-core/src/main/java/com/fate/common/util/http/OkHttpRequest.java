package com.fate.common.util.http;

import java.util.Map;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-05-08 11:04
 **/
public final class OkHttpRequest {
    final String url;
    final Map<String, ?> headerParams;
    final Map<String, ?> formParams;
    final Map<String, ?> getParams;
    final String tag;
    final Integer retryTimes;
    final String jsonString;

    public String url() {
        return url;
    }

    public Map<String, ?> headerParams() {
        return headerParams;
    }

    public Map<String, ?> formParams() {
        return formParams;
    }

    public Map<String, ?> getParams() {
        return getParams;
    }

    public String tag() {
        return tag;
    }

    public Integer retryTimes() {
        return retryTimes;
    }

    public String jsonString() {
        return jsonString;
    }


    OkHttpRequest(Builder builder) {
        this.url = builder.url;
        this.headerParams = builder.headerParams;
        this.formParams = builder.formParams;
        this.getParams = builder.getParams;
        this.tag = builder.tag;
        this.retryTimes = builder.retryTimes;
        this.jsonString = builder.jsonString;
    }

    public static class Builder {
        String url;
        Map<String, ?> headerParams;
        Map<String, ?> formParams;
        Map<String, ?> getParams;
        String tag;
        Integer retryTimes;
        String jsonString;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder headerParams(Map<String, ?> headerParams) {
            this.headerParams = headerParams;
            return this;
        }

        public Builder formParams(Map<String, ?> formParams) {
            this.formParams = formParams;
            return this;
        }

        public Builder getParams(Map<String, ?> getParams) {
            this.getParams = getParams;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder retryTimes(Integer retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        public Builder jsonString(String jsonString) {
            this.jsonString = jsonString;
            return this;
        }

        public OkHttpRequest build() {
            return new OkHttpRequest(this);
        }
    }
}