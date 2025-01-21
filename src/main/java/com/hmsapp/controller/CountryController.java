package com.hmsapp.controller;

import com.hmsapp.entity.Country;
import com.hmsapp.payrole.CountryDto;
import com.hmsapp.repository.CountryRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/country")
public class CountryController {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Country mapToEntity(CountryDto dto){
        return modelMapper.map(dto,Country.class);
    }

    public CountryDto mapToDto(Country country){
        return modelMapper.map(country,CountryDto.class);
    }

    @PostMapping("/addCountry")
    public ResponseEntity<?> addCountry(@Valid @RequestBody CountryDto dto){
        Country country = mapToEntity(dto);
        Optional<Country> opCountry = countryRepository.findByName(country.getName());
        if(opCountry.isPresent()){
            return new ResponseEntity<>("Country already present", HttpStatus.BAD_REQUEST);
        }
        Country c = countryRepository.save(country);
        return new ResponseEntity<>(mapToDto(c),HttpStatus.CREATED);
    }

    @PostMapping("/addCountries")
    public ResponseEntity<?> addCountries(@Valid @RequestBody List<CountryDto> countries){
        List<Country> savedCountry = new ArrayList<>();
        for(CountryDto dto:countries){
            Country country = mapToEntity(dto);
            Optional<Country> opCountry = countryRepository.findByName(country.getName());
            if(opCountry.isPresent()){
                Country c = opCountry.get();
                return new ResponseEntity<>(c.getName()+"is already present",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Country saveCountry = countryRepository.save(country);
            savedCountry.add(saveCountry);

        }
        List<CountryDto> countryDtos = savedCountry.stream().map(e->mapToDto(e)).collect(Collectors.toList());
        return new ResponseEntity<>(countryDtos,HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCountryById(@PathVariable long id){
        countryRepository.deleteById(id);
        return new ResponseEntity<>("Record deleted successfully",HttpStatus.OK);
    }
    @DeleteMapping("/deleteName/{name}")
    public ResponseEntity<String> deleteCountryByName(@PathVariable String name){
         Optional<Country> opCountry = countryRepository.findByName(name);
         if(opCountry.isPresent()){
             Country country = opCountry.get();
             countryRepository.deleteById(country.getId());
             return new ResponseEntity<>("Record deleted successfully",HttpStatus.OK);
         }
         return new ResponseEntity<>("Record not found",HttpStatus.NOT_FOUND);
    }
}
