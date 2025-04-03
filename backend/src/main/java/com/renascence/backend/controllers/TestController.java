package com.renascence.backend.controllers;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String basicSecured() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(auth.getName());

        return "hello customer";
    }

    @GetMapping("/adminSecured")
    public String adminSecured(){
        return "hello admin";
    }
}
