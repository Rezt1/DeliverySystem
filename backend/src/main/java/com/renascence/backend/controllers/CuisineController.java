package com.renascence.backend.controllers;

import com.renascence.backend.dtos.cuisine.CuisineDto;
import com.renascence.backend.services.CuisineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cuisines")
public class CuisineController {

    private final CuisineService cuisineService;

    @GetMapping
    public ResponseEntity<List<CuisineDto>> getAllCuisines() {
        List<CuisineDto> cuisines = cuisineService.getAllCuisines();
        return ResponseEntity.ok(cuisines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuisineDto> getCuisineById(@PathVariable Long id) {
        return ResponseEntity.ok(cuisineService.getCuisineById(id));
    }

    //probably not needed!
    @GetMapping("/{name}")
    public ResponseEntity<CuisineDto> getCuisineByName(@PathVariable String name) {
        return cuisineService.getCuisineByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
