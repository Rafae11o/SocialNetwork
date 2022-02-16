package com.socialNetwork.helpers;

public class Converter {
    /**
     * Returning a string in the format:
     * the first letter is capital, the rest are lowercase
     * @param str - string to convert
     * @return - converted string
     */
    public static String adjustCaseForName(String str){
        String lowerCaseStr = str.toLowerCase();
        return lowerCaseStr.substring(0, 1).toUpperCase() + lowerCaseStr.substring(1);
    }
}
