package com.renascence.backend.service;

import com.renascence.backend.dtos.cuisine.CuisineDto;
import com.renascence.backend.entities.Cuisine;
import com.renascence.backend.repositories.CuisineRepository;
import com.renascence.backend.services.CuisineService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuisineServiceTest {

    @Mock
    private CuisineRepository cuisineRepository;

    @InjectMocks
    private CuisineService cuisineService;

    @Test
    public void getAllCuisines_ShouldReturnAllCuisinesAsDtos() {
        Cuisine italian = new Cuisine();
        italian.setId(1L);
        italian.setName("italian");
        CuisineDto expectedItalian = new CuisineDto(1L, "italian");

        Cuisine bulgarian = new Cuisine();
        bulgarian.setId(2L);
        bulgarian.setName("bulgarian");
        CuisineDto expectedBulgarian = new CuisineDto(2L, "bulgarian");

        when(cuisineRepository.findAll()).thenReturn(List.of(italian, bulgarian));

        List<CuisineDto> result = cuisineService.getAllCuisines();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(expectedItalian, expectedBulgarian)));
        verify(cuisineRepository, times(1)).findAll();
    }

    @Test
    void getAllCuisines_WhenNoCuisinesExist_ShouldReturnEmptyList() {
        when(cuisineRepository.findAll()).thenReturn(List.of());

        List<CuisineDto> result = cuisineService.getAllCuisines();

        assertTrue(result.isEmpty());
        verify(cuisineRepository, times(1)).findAll();
    }

    @Test
    void getCuisineById_WithExistingId_ShouldReturnConvertedDto() {
        Cuisine italian = new Cuisine();
        italian.setId(1L);
        italian.setName("italian");
        CuisineDto expectedItalian = new CuisineDto(1L, "italian");

        when(cuisineRepository.findById(1L)).thenReturn(Optional.of(italian));

        CuisineDto result = cuisineService.getCuisineById(1L);

        assertNotNull(result);
        assertEquals(expectedItalian.getId(), result.getId());
        assertEquals(expectedItalian.getName(), result.getName());
        verify(cuisineRepository, times(1)).findById(1L);
    }

    @Test
    void getCuisineById_WithNonExistentId_ShouldThrowException() {
        when(cuisineRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> cuisineService.getCuisineById(99L)
        );
        assertEquals("Cuisine not found", exception.getMessage());
        verify(cuisineRepository, times(1)).findById(99L);
    }

    @Test
    void getCuisineByName_WithExistingName_ShouldReturnOptionalDto() {
        Cuisine italian = new Cuisine();
        italian.setId(1L);
        italian.setName("italian");
        CuisineDto expectedItalian = new CuisineDto(1L, "italian");

        when(cuisineRepository.findByName("italian")).thenReturn(Optional.of(italian));

        Optional<CuisineDto> result = cuisineService.getCuisineByName("italian");

        assertTrue(result.isPresent());
        assertEquals(expectedItalian, result.get());
        verify(cuisineRepository, times(1)).findByName("italian");
    }

    @Test
    void getCuisineByName_WithNonExistentName_ShouldReturnEmptyOptional() {
        when(cuisineRepository.findByName("Unknown")).thenReturn(Optional.empty());

        Optional<CuisineDto> result = cuisineService.getCuisineByName("Unknown");

        assertFalse(result.isPresent());
        verify(cuisineRepository, times(1)).findByName("Unknown");
    }
}
