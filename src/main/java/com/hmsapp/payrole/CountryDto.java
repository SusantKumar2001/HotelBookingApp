package com.hmsapp.payrole;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryDto {
    private long id;
    @NotNull
    private String name;
}
