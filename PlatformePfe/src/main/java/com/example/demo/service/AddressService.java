package com.example.demo.service;

import com.example.demo.model.Address;
import com.example.demo.model.Country;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;

    public AddressService(AddressRepository addressRepository, CountryRepository countryRepository) {
        this.addressRepository = addressRepository;
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }

}
