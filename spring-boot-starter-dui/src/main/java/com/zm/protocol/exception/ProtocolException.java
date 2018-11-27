package com.zm.protocol.exception;

public class ProtocolException extends Exception {
    private static final long serialVersionUID = 8914106243553844621L;

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ProtocolException(Throwable throwable) {
        super(throwable);
    }
}
