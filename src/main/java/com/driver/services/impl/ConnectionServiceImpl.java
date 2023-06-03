package com.driver.services.impl;

import com.driver.Utility.CountryPicker;
import com.driver.Utility.ServiceChecker;
import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        User user=userRepository2.findById(userId).get();
        if(user.getConnected()) throw new Exception("Already connected");
        else if(countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())) return user;
        else{
            int sip=Integer.MAX_VALUE;
            Connection connection=new Connection();
            ServiceProvider serviceProvider=null;
            Country country=null;
            for(ServiceProvider serviceProvider1: user.getServiceProviderList()){
                if(serviceProvider1.getId() < sip){
                    for(Country country1:serviceProvider1.getCountryList()) {
                        System.out.println(countryName+" "+country1.getCountryName().toString());
                        if (countryName.equalsIgnoreCase(country1.getCountryName().toString())) {
                            sip = serviceProvider1.getId();
                            serviceProvider = serviceProvider1;
                            country = country1;
                        }
                    }
                }
            }
            if(sip!=Integer.MAX_VALUE){
                connection.setUser(user);
                connection.setServiceProvider(serviceProvider);
                user.setMaskedIp(country.getCode()+"."+serviceProvider.getId()+"."+user.getId());
                user.setConnected(true);
                user.getConnectionList().add(connection);
                serviceProvider.getConnectionList().add(connection);
                serviceProviderRepository2.save(serviceProvider);
                userRepository2.save(user);
            }
            else throw new Exception("Unable to connect");
        }
        return user;

    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user=userRepository2.findById(userId).get();
        if(!user.getConnected()) throw new Exception("Already disconnected");
        else{
            user.setConnected(false);
            user.setMaskedIp(null);
            userRepository2.save(user);
        }
        return user;

    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User sender=userRepository2.findById(senderId).get();
        User receiver=userRepository2.findById(receiverId).get();
        try{
            String receiver_country, sender_country;
            if(receiver.getConnected()) receiver_country=getCountry(receiver.getMaskedIp().substring(0,3));
            else receiver_country=receiver.getOriginalCountry().getCountryName().toString();
            sender_country=sender.getOriginalCountry().getCountryName().toString();
            if(receiver_country.equalsIgnoreCase(sender_country)){
                return sender;
            }
            else{
                int sip=Integer.MAX_VALUE;
                Connection connection=new Connection();
                ServiceProvider serviceProvider=null;
                Country country=null;
                for(ServiceProvider serviceProvider1: sender.getServiceProviderList()){
                    if(serviceProvider1.getId() < sip){
                        for(Country country1:serviceProvider1.getCountryList()) {
                            System.out.println(receiver_country+" "+country1.getCountryName().toString());
                            if (receiver_country.equalsIgnoreCase(country1.getCountryName().toString())) {
                                sip = serviceProvider1.getId();
                                serviceProvider = serviceProvider1;
                                country = country1;
                            }
                        }
                    }
                }
                if(sip!=Integer.MAX_VALUE){
                    connection.setUser(sender);
                    connection.setServiceProvider(serviceProvider);
                    sender.setMaskedIp(country.getCode()+"."+serviceProvider.getId()+"."+sender.getId());
                    sender.setConnected(true);
                    sender.getConnectionList().add(connection);
                    serviceProvider.getConnectionList().add(connection);
                    serviceProviderRepository2.save(serviceProvider);
                    userRepository2.save(sender);
                }
                else throw new Exception("Cannot establish communication");
            }
        }
        catch (Exception e){
            throw new Exception("Cannot establish communication");
        }
        return sender;
    }

    public String getCountry(String code){
        if(code.equals("001")) return CountryName.IND.toString();
        else if(code.equals("002")) return CountryName.USA.toString();
        else if(code.equals("003")) return CountryName.AUS.toString();
        else if(code.equals("004")) return CountryName.CHI.toString();
        else if(code.equals("005")) return CountryName.JPN.toString();
        return null;

    }
}
