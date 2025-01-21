package com.hmsapp.controller;
import com.hmsapp.entity.City;
import com.hmsapp.entity.Country;
import com.hmsapp.entity.Property;
import com.hmsapp.entity.PropertyImage;
import com.hmsapp.payrole.PropertyDto;
import com.hmsapp.repository.CityRepository;
import com.hmsapp.repository.CountryRepository;
import com.hmsapp.repository.PropertyImageRepository;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.service.ImageService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PropertyImageRepository propertyImageRepository;

    public Property mapToEntity(PropertyDto dto){
        return modelMapper.map(dto,Property.class);
    }

    public PropertyDto mapToDto(Property property){
        return modelMapper.map(property,PropertyDto.class);
    }

    @PostMapping("/addProperty/{country}/{city}")
    public ResponseEntity<?> addProperty(@RequestBody PropertyDto dto,@PathVariable String country,@PathVariable String city){
        Property property = mapToEntity(dto);
        Country c = countryRepository.findByName(country).get();
        City ct = cityRepository.findByName(city).get();
        property.setCity(ct);
        property.setCountry(c);
        Property p = propertyRepository.save(property);
        return new ResponseEntity<>(mapToDto(p), HttpStatus.CREATED);
    }


    @GetMapping("/{searchParam}")
    public List<Property> searchProperty(@PathVariable String searchParam){
        return propertyRepository.searchProperty(searchParam);
    }

    @PostMapping("/createProperty")
    public ResponseEntity<?> addProperty2(@RequestBody PropertyDto dto,@RequestParam String country,@RequestParam String city){
        Property property = mapToEntity(dto);
        Country c = countryRepository.findByName(country).get();
        City ct = cityRepository.findByName(city).get();
        property.setCity(ct);
        property.setCountry(c);
        Property p = propertyRepository.save(property);
        return new ResponseEntity<>(mapToDto(p), HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteProperty")
    public ResponseEntity<String>  deleteProperty(@RequestParam long id){
        propertyRepository.deleteById(id);
        return new ResponseEntity<>("Record deleted successfull",HttpStatus.OK);
    }

    @PutMapping("/updateProperty/{country}/{city}")
    public ResponseEntity<?> updateProperty(@RequestParam long id, @Valid @RequestBody PropertyDto dto, @PathVariable String country, @PathVariable String city, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Property property = mapToEntity(dto);
        Country c = countryRepository.findByName(country).get();
        City ct = cityRepository.findByName(city).get();
        property.setCountry(c);
        property.setCity(ct);
        property.setId(id);
        Property p = propertyRepository.save(property);
        return new ResponseEntity<>(mapToDto(p),HttpStatus.OK);
    }

    @PostMapping("/upload/propertyPhotos")
    public ResponseEntity<String> uploadPropertyPhotos(@RequestParam long id, @RequestParam MultipartFile file) throws IOException {
        String imageUrl = imageService.uploadImage(id,file);
        Property property  = propertyRepository.findById(id).get();
        PropertyImage image = new PropertyImage();
        image.setImageUrl(imageUrl);
        image.setProperty(property);
        propertyImageRepository.save(image);
        return new ResponseEntity<>("Photo uploaded successfully",HttpStatus.OK);
    }

    @GetMapping("/getProperty/images")
    public ResponseEntity<List<PropertyImage>> getPropertyImages(@RequestParam long id){
        Property property = propertyRepository.findById(id).get();
        List<PropertyImage> allImages = propertyImageRepository.findByProperty(property);
        return new ResponseEntity<>(allImages,HttpStatus.OK);
    }

}
