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
import org.springframework.beans.factory.annotation.Autowired;

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
        //Arrange
        Cuisine italian = new Cuisine();
        italian.setId(1L);
        italian.setName("italian");
        CuisineDto expectedItalian = new CuisineDto(1L, "italian");

        Cuisine bulgarian = new Cuisine();
        bulgarian.setId(2L);
        bulgarian.setName("bulgarian");
        CuisineDto expectedBulgarian = new CuisineDto(2L, "bulgarian");

        when(cuisineRepository.findAll()).thenReturn(List.of(italian, bulgarian));

        //Act
        List<CuisineDto> result = cuisineService.getAllCuisines();

        //Assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(expectedItalian, expectedBulgarian)));
        verify(cuisineRepository, times(1)).findAll();
    }

    @Test
    void getAllCuisines_WhenNoCuisinesExist_ShouldReturnEmptyList() {
        // Arrange
        when(cuisineRepository.findAll()).thenReturn(List.of());

        // Act
        List<CuisineDto> result = cuisineService.getAllCuisines();

        // Assert
        assertTrue(result.isEmpty());
        verify(cuisineRepository, times(1)).findAll();
    }

    @Test
    void getCuisineById_WithExistingId_ShouldReturnConvertedDto() {
        //Arrange
        Cuisine italian = new Cuisine();
        italian.setId(1L);
        italian.setName("italian");
        CuisineDto expectedItalian = new CuisineDto(1L, "italian");

        when(cuisineRepository.findById(1L)).thenReturn(Optional.of(italian));

        // Act
        CuisineDto result = cuisineService.getCuisineById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(expectedItalian.getId(), result.getId());
        assertEquals(expectedItalian.getName(), result.getName());
        verify(cuisineRepository, times(1)).findById(1L);
    }

    @Test
    void getCuisineById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(cuisineRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> cuisineService.getCuisineById(99L)
        );
        assertEquals("Cuisine not found", exception.getMessage());
        verify(cuisineRepository, times(1)).findById(99L);
    }

    @Test
    void getCuisineByName_WithExistingName_ShouldReturnOptionalDto() {
        // Arrange
        Cuisine italian = new Cuisine();
        italian.setId(1L);
        italian.setName("italian");
        CuisineDto expectedItalian = new CuisineDto(1L, "italian");

        when(cuisineRepository.findByName("italian")).thenReturn(Optional.of(italian));

        // Act
        Optional<CuisineDto> result = cuisineService.getCuisineByName("italian");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedItalian, result.get());
        verify(cuisineRepository, times(1)).findByName("italian");
    }

    @Test
    void getCuisineByName_WithNonExistentName_ShouldReturnEmptyOptional() {
        // Arrange
        when(cuisineRepository.findByName("Unknown")).thenReturn(Optional.empty());

        // Act
        Optional<CuisineDto> result = cuisineService.getCuisineByName("Unknown");

        // Assert
        assertFalse(result.isPresent());
        verify(cuisineRepository, times(1)).findByName("Unknown");
    }
}
