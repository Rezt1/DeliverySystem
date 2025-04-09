package com.renascence.backend.service;

import com.renascence.backend.dtos.city.CityDto;
import com.renascence.backend.entities.City;
import com.renascence.backend.repositories.CityRepository;
import com.renascence.backend.services.CityService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    void getAllCities_ShouldReturnAllCitiesAsDtos() {
        // Arrange
        City plevenEntity = new City();
        plevenEntity.setId(1L);
        plevenEntity.setName("Pleven");
        CityDto expectedPlevenDto = new CityDto(1L, "Pleven");

        City petrichEntity = new City();
        petrichEntity.setId(2L);
        petrichEntity.setName("Petrich");
        CityDto expectedPetrichDto = new CityDto(2L, "Petrich");

        when(cityRepository.findAll()).thenReturn(List.of(plevenEntity, petrichEntity));

        // Act
        List<CityDto> result = cityService.getAllCities();

        // Assert
        assertEquals(2, result.size());
        Assertions.assertTrue(result.containsAll(List.of(expectedPlevenDto, expectedPetrichDto)));
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void getAllCities_WhenNoCitiesExist_ShouldReturnEmptyList() {
        // Arrange
        when(cityRepository.findAll()).thenReturn(List.of());

        // Act
        List<CityDto> result = cityService.getAllCities();

        // Assert
        assertTrue(result.isEmpty());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void getCityById_WithExistingId_ShouldReturnCityDto() {
        // Arrange
        City cityEntity = new City();
        cityEntity.setId(1L);
        cityEntity.setName("Pleven");
        CityDto expectedDto = new CityDto(1L, "Pleven");

        when(cityRepository.findById(1L)).thenReturn(Optional.of(cityEntity));

        // Act
        CityDto result = cityService.getCityById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void getCityById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> cityService.getCityById(99L)
        );
        assertEquals("City not found", exception.getMessage());
        verify(cityRepository, times(1)).findById(99L);
    }

    @Test
    void convertToDto_ShouldMapEntityToDto() throws Exception {
        City plevenEntity = new City();
        plevenEntity.setId(1L);
        plevenEntity.setName("Pleven");

        // Get the private method
        Method method = CityService.class.getDeclaredMethod("convertToDto", City.class);
        method.setAccessible(true); // Override access restriction

        // Invoke the method
        CityDto result = (CityDto) method.invoke(cityService, plevenEntity);

        // Assert
        assertEquals(plevenEntity.getId(), result.getId());
        assertEquals(plevenEntity.getName(), result.getName());
    }
}
