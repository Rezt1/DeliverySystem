package com.renascence.backend.services;

import com.renascence.backend.dtos.city.CityDto;
import com.renascence.backend.entities.City;
import com.renascence.backend.repositories.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<CityDto> getAllCities() {
        return cityRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public CityDto getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found"));

        return convertToDto(city);
    }

    private CityDto convertToDto(City city) {
        return new CityDto(city.getId(), city.getName());
    }
}