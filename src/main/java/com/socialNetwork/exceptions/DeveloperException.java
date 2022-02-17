package com.socialNetwork.exceptions;

public class DeveloperException extends Exception{

    private String info;
    private String placeOfCode;

    public DeveloperException(String msg) {
        super(msg);
    }

    public DeveloperException(String msg, String placeOfCode, String info) {
        super(msg);
        this.info = info;
        this.placeOfCode = placeOfCode;
    }

    public DeveloperException(String placeOfCode, String info) {
        super("Error");
        this.info = info;
        this.placeOfCode = placeOfCode;
    }

    public String getInfo() {
        return info;
    }

    public String getPlaceOfCode() {
        return placeOfCode;
    }
}
