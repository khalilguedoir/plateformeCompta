package com.example.demo.controller;

import com.example.demo.model.Address;
import com.example.demo.model.Country;
import com.example.demo.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/countries")
    public List<Country> getCountries() {
        return addressService.getAllCountries();
    }

    @GetMapping
    public List<Address> getAddresses() {
        return addressService.getAllAddresses();
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        return addressService.save(address);
    }
}
