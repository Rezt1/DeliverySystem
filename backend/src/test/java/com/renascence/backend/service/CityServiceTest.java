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
    void getAllCities_ShouldReturnOnlyNonDeletedCitiesAsDtos() {
        City plevenEntity = new City();
        plevenEntity.setId(1L);
        plevenEntity.setName("Pleven");
        plevenEntity.setSalary(1400.0);
        plevenEntity.setDeleted(true);
        CityDto expectedPlevenDto = new CityDto(1L, "Pleven", 1400.0);

        City petrichEntity = new City();
        petrichEntity.setId(2L);
        petrichEntity.setName("Petrich");
        petrichEntity.setSalary(1300.0);
        CityDto expectedPetrichDto = new CityDto(2L, "Petrich", 1300.0);

        City blagoevgradEntity = new City();
        blagoevgradEntity.setId(3L);
        blagoevgradEntity.setName("Blagoevgrad");
        blagoevgradEntity.setSalary(1300.0);
        CityDto expectedBlagoevgradDto = new CityDto(3L, "Blagoevgrad", 1300.0);

        when(cityRepository.findAll()).thenReturn(List.of(plevenEntity, petrichEntity, blagoevgradEntity));

        List<CityDto> result = cityService.getAllCities();

        assertEquals(2, result.size());
        Assertions.assertTrue(result.containsAll(List.of(expectedPetrichDto, expectedBlagoevgradDto)));
        Assertions.assertFalse(result.stream().anyMatch(dto -> dto.getName().equals("Pleven")));
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void getAllCities_WhenNoCitiesExist_ShouldReturnEmptyList() {
        when(cityRepository.findAll()).thenReturn(List.of());

        List<CityDto> result = cityService.getAllCities();

        assertTrue(result.isEmpty());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void getCityById_WithExistingId_ShouldReturnCityDto() {
        City cityEntity = new City();
        cityEntity.setId(1L);
        cityEntity.setName("Pleven");
        cityEntity.setSalary(1400.0);
        CityDto expectedDto = new CityDto(1L, "Pleven", 1400.0);

        when(cityRepository.findById(1L)).thenReturn(Optional.of(cityEntity));

        CityDto result = cityService.getCityById(1L);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void getCityById_WithNonExistentId_ShouldThrowException() {
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> cityService.getCityById(99L)
        );
        assertEquals("City not found", exception.getMessage());
        verify(cityRepository, times(1)).findById(99L);
    }

    @Test
    void getCityById_throwsException_whenCityIsDeleted() {
        City cityEntity = new City();
        cityEntity.setId(1L);
        cityEntity.setName("Pleven");
        cityEntity.setSalary(1400.0);
        cityEntity.setDeleted(true);
        CityDto expectedDto = new CityDto(1L, "Pleven", 1400.0);
        when(cityRepository.findById(2L)).thenReturn(Optional.of(cityEntity));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> cityService.getCityById(2L));

        assertEquals("City no longer exists in our system", ex.getMessage());
    }

    @Test
    void convertToDto_ShouldMapEntityToDto() throws Exception {
        City plevenEntity = new City();
        plevenEntity.setId(1L);
        plevenEntity.setName("Pleven");

        Method method = CityService.class.getDeclaredMethod("convertToDto", City.class);
        method.setAccessible(true);

        CityDto result = (CityDto) method.invoke(cityService, plevenEntity);

        assertEquals(plevenEntity.getId(), result.getId());
        assertEquals(plevenEntity.getName(), result.getName());
    }
}
