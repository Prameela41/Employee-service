package com.tekikons.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    @RequestMapping(value= "/welcome")
    public String welcome(){
        return "Welcome to SpringBootWorld" ;
    }
}
