package com.hmsapp.payrole;

import com.hmsapp.entity.City;
import com.hmsapp.entity.Country;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDto {
    private long id;
    private String name;
    private int noOfGuest;
    private int noOfBedrooms;
    private int noOfBathrooms;
    private Country country;
    private City city;
}
