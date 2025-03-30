package com.renascence.backend.services;

import com.renascence.backend.dtos.Cuisine.CreateCuisineDto;
import com.renascence.backend.dtos.Cuisine.CuisineDto;
import com.renascence.backend.entities.Cuisine;
import com.renascence.backend.repositories.CuisineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuisineService {

    private final CuisineRepository cuisineRepository;

    public CuisineService(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    public CuisineDto createCuisine(CreateCuisineDto createCuisineDto) {
        Cuisine cuisine =new Cuisine();
        cuisine.setName(createCuisineDto.getName());
        Cuisine savedCuisine = cuisineRepository.save(cuisine);
        return convertToDto(savedCuisine);
    }

    public List<CuisineDto> getAllCuisines() {
        return cuisineRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<CuisineDto> getCuisineById(Long id) {
        return cuisineRepository.findById(id)
                .map(this::convertToDto);
    }

    private CuisineDto convertToDto(Cuisine cuisine) {
        return new CuisineDto(cuisine.getId(), cuisine.getName());
    }
}
