package com.hmsapp.payrole;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class UserDto {

    private long id;
    @NotNull(message = "name can not be null")
    @Size(min=5,max=20,message="name should contain min 5 characters")
    private String name;
    @NotNull(message = "username can not be null")
    @Size(min=5,max=20,message="name should contain min 5 characters")
    private String username;
    @NotNull(message = "name can not be null")
    @Email(message="email should be valid")
    private String email;
    @NotNull(message = "mobile can not be null")
    @Pattern(regexp = "^[7-9][0-9]{9}$",message="mobile should be valid")
    private String mobile;
    @NotNull(message = "password can not be null")
    @Size(min=6,max=20,message="password should contain min 6 characters")
    private String password;

}
