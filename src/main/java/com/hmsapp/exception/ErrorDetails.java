package com.hmsapp.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class ErrorDetails {
    private String msg;
    private Date date;
    private String request;
}
