package com.driver.Utility;

public class CountryPicker {

    public static String getCountryFromIP(String userIp){
        String[] ip = userIp.split(".");
        String countrych = ip[0];
        String countryName = null;
        switch(countrych){
            case ("001"):
                countryName = "IND";
                break;
            case ("002"):
                countryName = "USA";
                break;
            case ("003"):
                countryName = "AUS";
                break;
            case ("004"):
                countryName = "CHI";
                break;
            case ("005"):
                countryName = "JAP";
                break;
        }
        return countryName;
    }
}