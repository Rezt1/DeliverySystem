package com.renascence.backend.repositories;

import com.renascence.backend.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByOrderByRatingDesc();

    List<Restaurant> findAllByCityIdAndIsDeleted(Long cityId, boolean isDeleted);
//    List<Restaurant> findByCityId(Long cityId);
//
//    @Query("SELECT r FROM Restaurant r ORDER BY r.rating DESC LIMIT :count")
//    List<Restaurant> findTopRatedRestaurants(@Param("count") int count);
//
//    @Query("SELECT DISTINCT r FROM Restaurant r JOIN r.foods f JOIN f.cuisine c " +
//            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :cuisineName, '%'))")
//    List<Restaurant> findByCuisineNameContainingIgnoreCase(@Param("cuisineName") String cuisineName);
}
