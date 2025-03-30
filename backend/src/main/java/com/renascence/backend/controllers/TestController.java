package com.renascence.backend.controllers;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/unsecured1")
    public String unsecured1(){
        return "accessed unsecured1";
    }

    @GetMapping("/unsecured2")
    public String unsecured2(){
        return "accessed unsecured2";
    }

    @GetMapping("/basicSecured")
    public String basicSecured(){
        return "hello customer";
    }

    @GetMapping("/adminSecured")
    public String adminSecured(){
        return "hello admin";
    }
}
