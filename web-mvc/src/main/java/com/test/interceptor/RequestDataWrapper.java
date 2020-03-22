package com.test.interceptor;

import com.fasterxml.jackson.databind.JsonNode;

public class RequestDataWrapper {

    private JsonNode params;
    private boolean hashParams;
    private boolean canRead;

    public RequestDataWrapper(boolean canRead) {
        this.canRead = canRead;
    }

    public void processParam(JsonNode params) {
        if (params != null) {
            this.params = params;
            this.hashParams = !params.isNull() && !params.isMissingNode();
        }
    }

    public JsonNode getParams() {
        return params;
    }

    public void setParams(JsonNode params) {
        this.params = params;
    }

    public boolean isHashParams() {
        return hashParams;
    }

    public void setHashParams(boolean hashParams) {
        this.hashParams = hashParams;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }
}
