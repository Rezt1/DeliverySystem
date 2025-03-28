package com.renascence.backend.services;

import com.renascence.backend.dtos.City.CityDto;
import com.renascence.backend.dtos.City.CreateCityDto;
import com.renascence.backend.entities.City;
import com.renascence.backend.repositories.CityRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public CityDto createCity(CreateCityDto createCityDto) {
        City city = new City();
        city.setName(createCityDto.getName());
        City savedCity = cityRepository.save(city);
        return convertToDto(savedCity);
    }

    public List<CityDto> getAllCities() {
        return cityRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<CityDto> getCityById(Long id) {
        return cityRepository.findById(id)
                .map(this::convertToDto);
    }

//    public void deleteCity(Long id) {
//        cityRepository.deleteById(id);
//    }

    private CityDto convertToDto(City city) {
        return new CityDto(city.getId(), city.getName(), null, null);
    }
}