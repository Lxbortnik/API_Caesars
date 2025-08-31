package com.alex.caesars;

public class ExternalServiceException extends RuntimeException{
    private final int status;

    public ExternalServiceException (String message, int status, Throwable cause){
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public class ParsingException extends RuntimeException{
        public ParsingException(String message, Throwable cause){
            super(message,cause);

        }
    }
}
