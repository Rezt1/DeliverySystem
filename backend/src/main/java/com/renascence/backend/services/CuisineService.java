package com.renascence.backend.services;

import com.renascence.backend.dtos.Cuisine.CreateCuisineDto;
import com.renascence.backend.dtos.Cuisine.CuisineDto;
import com.renascence.backend.entities.Cuisine;
import com.renascence.backend.repositories.CuisineRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CuisineService {

    private final CuisineRepository cuisineRepository;


    public List<CuisineDto> getAllCuisines() {
        return cuisineRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public CuisineDto getCuisineById(Long id) {
        Cuisine cuisine = cuisineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuisine not found"));

        return convertToDto(cuisine);
    }

    public Optional<CuisineDto> getCuisineByName(String name) {
        return cuisineRepository.findByName(name)
                .map(this::convertToDto);
    }

    private CuisineDto convertToDto(Cuisine cuisine) {
        return new CuisineDto(cuisine.getId(), cuisine.getName());
    }
}
