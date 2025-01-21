package com.hmsapp.controller;

import com.hmsapp.payrole.JwtToken;
import com.hmsapp.payrole.LoginDto;
import com.hmsapp.payrole.UserDto;
import com.hmsapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/hms")
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("/sign-up")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDto dto, BindingResult result){
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResponseEntity<?> responseEntity = userService.addUser(dto);
        return responseEntity;
    }

    @PostMapping("/property/sign-up")
    public ResponseEntity<?> addPropertyOwner(@Valid @RequestBody UserDto dto,BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResponseEntity<?> responseEntity = userService.createPropertyOwnerAccount(dto);
        return responseEntity;
    }

    @PostMapping("/blog/sign-up")
    public ResponseEntity<?> createBlogManagerAccount(@Valid @RequestBody UserDto dto,BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResponseEntity<?> responseEntity = userService.createBlogManagerAccount(dto);
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){
        String token = userService.login(dto);
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setType("JWT");
        if(token != null){
            return new ResponseEntity<>(jwtToken,HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @DeleteMapping("delUser")
    public ResponseEntity<String> deleteUser(@RequestParam Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>("Record deleted successfully",HttpStatus.OK);
    }
    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestParam long id,@Valid @RequestBody UserDto dto,BindingResult result){
        if (result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResponseEntity<?> response = userService.updateUser(id,dto);
        return response;
    }



}
