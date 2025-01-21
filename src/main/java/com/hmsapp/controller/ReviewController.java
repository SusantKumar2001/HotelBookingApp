package com.hmsapp.controller;

import com.hmsapp.entity.Property;
import com.hmsapp.entity.Reviews;
import com.hmsapp.entity.User;
import com.hmsapp.payrole.ProfileDto;
import com.hmsapp.payrole.ReviewDto;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.ReviewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    @Autowired
    private ReviewsRepository reviewRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PropertyRepository propertyRepository;

    public Reviews mapToEntity(ReviewDto dto){
        return modelMapper.map(dto,Reviews.class);
    }

    public ReviewDto mapToDto(Reviews review){
        return modelMapper.map(review,ReviewDto.class);
    }
    @PostMapping("/addReviews")
    public ResponseEntity<?> addReview(@RequestBody ReviewDto dto, @RequestParam long propertyId, @AuthenticationPrincipal User user){
        Reviews reviews = mapToEntity(dto);
        Property property = propertyRepository.findById(propertyId).get();
        Reviews rev = reviewRepository.findByPropertyAndUser(property, user);
        if(rev== null) {
            reviews.setProperty(property);
            reviews.setUser(user);
            Reviews r = reviewRepository.save(reviews);
            return new ResponseEntity<>(mapToDto(r), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("You are already given review",HttpStatus.BAD_REQUEST);

    }
    @GetMapping("/user/review")
    public ResponseEntity<List<ReviewDto>> showMyReviews(@AuthenticationPrincipal User user){
        List<Reviews> getReviews = reviewRepository.findByUser(user);
        List<ReviewDto> res = getReviews.stream().map(e->mapToDto(e)).collect(Collectors.toList());
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
    @GetMapping("/userProfile")
    public ResponseEntity<ProfileDto> getUserProfile(@AuthenticationPrincipal User user) {
        ProfileDto  dto = new ProfileDto();
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
}
