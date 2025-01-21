package com.hmsapp.controller;

import com.hmsapp.entity.City;
import com.hmsapp.payrole.CityDto;
import com.hmsapp.repository.CityRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/city")
public class CityController {


    private CityRepository cityRepository;

    public CityController(CityRepository cityRepository, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }

    private ModelMapper modelMapper;

    public City mapToCity(CityDto dto){
        return modelMapper.map(dto,City.class);
    }

    public CityDto mapToDto(City city){
        return modelMapper.map(city,CityDto.class);
    }

    @PostMapping("/addCity")
    public ResponseEntity<?> addCity(@Valid  @RequestBody CityDto dto){
        City city = mapToCity(dto);
        Optional<City> opCityName = cityRepository.findByName(city.getName());
        if(opCityName.isPresent()){
            return new ResponseEntity<>("City already present", HttpStatus.BAD_REQUEST);
        }
        City c = cityRepository.save(city);
        return new ResponseEntity<>(mapToDto(c),HttpStatus.CREATED);
    }

    @PostMapping("/addCities")
    public ResponseEntity<?> addCities(@Valid @RequestBody List<CityDto> cities){
        List<City> savedCities = new ArrayList<>();
        for(CityDto dto:cities){
            City city = mapToCity(dto);
            Optional<City> opCity = cityRepository.findByName(city.getName());
            if(opCity.isPresent()){
                City c = opCity.get();
                return new ResponseEntity<>(c.getName()+" is already Present",HttpStatus.BAD_REQUEST);
            }
            City ct = cityRepository.save(city);
            savedCities.add(ct);

        }
        List<CityDto> cityDtos = savedCities.stream().map(e->mapToDto(e)).collect(Collectors.toList());
        return new ResponseEntity<>(cityDtos,HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCityById(@PathVariable long id){
        cityRepository.deleteById(id);
        return  new ResponseEntity<>("Record deleted successfully",HttpStatus.OK);
    }

    @DeleteMapping("/deleteName/{name}")
    public ResponseEntity<String> deleteCityByName(@PathVariable String name){
        Optional<City> opCity = cityRepository.findByName(name);
        if(opCity.isPresent()){
            City city = opCity.get();
            cityRepository.deleteById(city.getId());
            return new ResponseEntity<>("Record deleted successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("No record found on this name",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/deleteCities")
    public ResponseEntity<?> deleteCities(@RequestBody List<CityDto> cities){
        List<String> cityName = new ArrayList<>();
        List<String> savedCities = new ArrayList<>();
        for(CityDto dto:cities){
            if (cityRepository.existsByName(dto.getName())) {
                City city = cityRepository.findByName(dto.getName()).get();
                savedCities.add(city.getName());
                cityRepository.deleteById(city.getId());
            }
            else{
                cityName.add(dto.getName());
            }
        }
        Map<String,List<String>> map = new HashMap<>();
        map.put("deletedCities",savedCities);
        map.put("Not found cities",cityName);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

}
