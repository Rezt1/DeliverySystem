package com.renascence.backend.controllers;

import com.renascence.backend.dtos.Cuisine.CreateCuisineDto;
import com.renascence.backend.dtos.Cuisine.CuisineDto;
import com.renascence.backend.services.CuisineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cuisines")
public class CuisineController {

    private final CuisineService cuisineService;

    public CuisineController(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @PostMapping
    public ResponseEntity<CuisineDto> createCuisine(@Valid @RequestBody CreateCuisineDto createCuisineDto) {
        CuisineDto savedCuisine = cuisineService.createCuisine(createCuisineDto);
        return ResponseEntity.ok(savedCuisine);
    }

    @GetMapping
    public ResponseEntity<List<CuisineDto>> getAllCuisines() {
        List<CuisineDto> cuisines = cuisineService.getAllCuisines();
        return ResponseEntity.ok(cuisines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuisineDto> getCuisineById(@PathVariable Long id) {
        return cuisineService.getCuisineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
