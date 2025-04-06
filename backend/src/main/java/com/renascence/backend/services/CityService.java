package com.renascence.backend.services;

import com.renascence.backend.dtos.City.CityDto;
import com.renascence.backend.entities.City;
import com.renascence.backend.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<CityDto> getAllCities() {
        return cityRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<CityDto> getCityById(Long id) {
        return cityRepository.findById(id)
                .map(this::convertToDto);
    }

    private CityDto convertToDto(City city) {
        return new CityDto(city.getId(), city.getName());
    }
}