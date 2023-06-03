package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        countryName = countryName.toUpperCase();
        CountryName cntyName;

        try {
            cntyName = CountryName.valueOf(countryName);
        }catch (Exception e){
            throw new Exception("Country Not Found");
        }

        Country country = new Country();
        country.setCountryName(cntyName);
        country.setCode(cntyName.toCode());

        User user = new User();
        user.setOriginalCountry(country);
        user.setConnected(false);
        user.setUsername(username);
        user.setOriginalCountry(country);
        user.setPassword(password);
        country.setUser(user);

        userRepository3.save(user);

        String countryCode = cntyName.toCode();
        countryCode = countryCode+user.getId();

        user.setOriginalIp(countryCode);

        return user;


    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) throws Exception {
        User user;
        try {
            user = userRepository3.findById(userId).get();
        }catch (Exception e){
            throw new Exception("User Not found");
        }

        ServiceProvider serviceProvider;
        try {
            serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();
        }catch (Exception e){
            throw new Exception("Service Provider Not Found");
        }

        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);

        userRepository3.save(user);


        return user;
    }
}
