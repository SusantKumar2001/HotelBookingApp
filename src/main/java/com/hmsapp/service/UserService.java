package com.hmsapp.service;

import com.hmsapp.entity.User;
import com.hmsapp.payrole.LoginDto;
import com.hmsapp.payrole.UserDto;
import com.hmsapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class  UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private EmailService emailService;

    public User mapToEntity(UserDto dto){
        User user = modelMapper.map(dto,User.class);
        return user;
    }
    public UserDto mapToDto(User user){
        UserDto dto = modelMapper.map(user,UserDto.class);
        return dto;
    }

    public ResponseEntity<?> addUser(UserDto dto){
        User user = mapToEntity(dto);
        Optional<User> name = userRepository.findByUsername(user.getUsername());
        if(name.isPresent()){
            return new ResponseEntity<>("name already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> email = userRepository.findByEmail(user.getEmail());
        if(email.isPresent()){
            return new ResponseEntity<>("email already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> mobile = userRepository.findByMobile(user.getMobile());
        if(mobile.isPresent()){
            return new ResponseEntity<>("mobileNumber already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        user.setRole("ROLE_USER");
        User us = userRepository.save(user);
        emailService.sendEmail(us.getEmail(),"Welcome","Thanks for showing interest in this application ❤️❤️❤️.");
        return new ResponseEntity<>(mapToDto(us),HttpStatus.CREATED);
    }

    public ResponseEntity<?> createPropertyOwnerAccount(UserDto dto){
        User user = mapToEntity(dto);
        Optional<User> opname = userRepository.findByUsername(user.getUsername());
        if(opname.isPresent()){
            return new ResponseEntity<>("name already exists",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opemail = userRepository.findByEmail(user.getEmail());
        if(opemail.isPresent()){
            return new ResponseEntity<>("email already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opmobile = userRepository.findByMobile(user.getMobile());
        if(opmobile.isPresent()){
            return new ResponseEntity<>("mobileNumber already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        user.setRole("ROLE_OWNER");
        User us = userRepository.save(user);
        return new ResponseEntity<>(mapToDto(us),HttpStatus.CREATED);
    }

    public ResponseEntity<?> createBlogManagerAccount(UserDto dto) {
        User user = mapToEntity(dto);
        Optional<User> opname = userRepository.findByUsername(user.getUsername());
        if(opname.isPresent()){
            return new ResponseEntity<>("name already exists",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opemail = userRepository.findByEmail(user.getEmail());
        if(opemail.isPresent()){
            return new ResponseEntity<>("email already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opmobile = userRepository.findByMobile(user.getMobile());
        if(opmobile.isPresent()){
            return new ResponseEntity<>("mobileNumber already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        user.setRole("ROLE_BLOGMANAGER");
        User us = userRepository.save(user);
        return new ResponseEntity<>(mapToDto(us),HttpStatus.CREATED);
    }


    public String login(LoginDto dto){
        Optional<User> opUser = userRepository.findByUsername(dto.getUsername());
        if(opUser.isPresent()){
            User user = opUser.get();
            if(BCrypt.checkpw(dto.getPassword(),user.getPassword())){
                emailService.sendEmail(user.getEmail(),"Login successful","Login successfully 😊😊");
                String token = jwtService.generateToken(user.getUsername());
                return token;
            }
        }
        return null;
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public ResponseEntity<?> updateUser(long id, UserDto dto) {
        User user = mapToEntity(dto);
        Optional<User> opUser = userRepository.findById(id);
        if(opUser.isPresent()){
            user.setId(id);
            user.setRole("ROLE_USER");
            user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
            User us = userRepository.save(user);
            return new ResponseEntity<>(mapToDto(us),HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
