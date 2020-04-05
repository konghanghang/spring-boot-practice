package com.test.model;

public final class ThreadContext {

    private ThreadContext() {}

    private final static ThreadLocal<LogModel> LOG_MODEL = new ThreadLocal<>();

    public static void setLogModel(LogModel logModel) {
        LOG_MODEL.set(logModel);
    }

    public static LogModel getLogModel() {
        return LOG_MODEL.get();
    }

    public static void clear() {
        LOG_MODEL.remove();
    }
}
