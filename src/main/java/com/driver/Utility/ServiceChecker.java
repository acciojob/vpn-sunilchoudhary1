package com.driver.Utility;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class ServiceChecker {

    public static List<ServiceProvider> isCountryAvailable(List<ServiceProvider> serviceProviders, CountryName cntyName){
        List<ServiceProvider> availableProviders = new ArrayList<>();

        for(ServiceProvider serviceProvider :serviceProviders){
            for(Country country : serviceProvider.getCountryList()){
                if(country.getCountryName().equals(cntyName)){
                    availableProviders.add(serviceProvider);
                }
            }
        }

        return availableProviders;
    }

    public static ServiceProvider getLowIDProvider(List<ServiceProvider> availableProviders){
        ServiceProvider serviceProvider = availableProviders.get(0);
        for(ServiceProvider serviceProvider1 : availableProviders){
            if(serviceProvider1.getId() < serviceProvider.getId()){
                serviceProvider = serviceProvider1;
            }
        }

        return  serviceProvider;
    }
}