package com.renascence.backend.controllers;

import com.renascence.backend.dtos.City.CityDto;
import com.renascence.backend.dtos.City.CreateCityDto;
import com.renascence.backend.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    @GetMapping
    public ResponseEntity<List<CityDto>> getAllCities() {
        List<CityDto> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable Long id) {
        return cityService.getCityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}