package com.socialNetwork.exceptions;

public class DeveloperException extends Exception{

    private String info;
    private String placeOfCode;

    /**
     *
     * @param msg - message to display to the user
     */
    public DeveloperException(String msg) {
        super(msg);
    }

    /**
     *
     * @param msg - message to display to the user
     * @param placeOfCode - place of code, where exception has been thrown
     * @param info - what's happened or any valuable information
     */
    public DeveloperException(String msg, String placeOfCode, String info) {
        super(msg);
        this.info = info;
        this.placeOfCode = placeOfCode;
    }

    /**
     *
     * @param placeOfCode - place of code, where exception has been thrown
     * @param info - what's happened or any valuable information
     */
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
