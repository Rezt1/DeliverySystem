package com.renascence.backend;

import com.renascence.backend.entities.City;
import com.renascence.backend.entities.Cuisine;
import com.renascence.backend.entities.Food;
import com.renascence.backend.entities.Restaurant;
import com.renascence.backend.enums.FoodCategory;
import com.renascence.backend.repositories.CityRepository;
import com.renascence.backend.repositories.CuisineRepository;
import com.renascence.backend.repositories.FoodRepository;
import com.renascence.backend.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CuisineRepository cuisineRepository;
    @Autowired
    private FoodRepository foodRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. Cities
        City newYork = new City();
        newYork.setName("New York");
        cityRepository.save(newYork);

        City london = new City();
        london.setName("London");
        cityRepository.save(london);

        // 2. Cuisines
        Cuisine italian = new Cuisine();
        italian.setName("Italian");
        cuisineRepository.save(italian);

        Cuisine japanese = new Cuisine();
        japanese.setName("Japanese");
        cuisineRepository.save(japanese);

        // 3. Restaurants
        Restaurant pizzaPalace = new Restaurant();
        pizzaPalace.setName("Pizza Palace");
        pizzaPalace.setDeliveryGuySalary(915.50);
        pizzaPalace.setIban("GB123456789");
        pizzaPalace.setRating(4.5f);
        pizzaPalace.setCity(newYork);
        restaurantRepository.save(pizzaPalace);

        Restaurant sushiWorld = new Restaurant();
        sushiWorld.setName("Sushi World");
        sushiWorld.setDeliveryGuySalary(18.75);
        sushiWorld.setIban("JP987654321");
        sushiWorld.setRating(4.8f);
        sushiWorld.setCity(london);
        restaurantRepository.save(sushiWorld);

        // 4. Foods
        Food pizza = new Food();
        pizza.setName("Margherita Pizza");
        pizza.setPrice(12.99);
        pizza.setCategory(FoodCategory.MAIN_COURSE);
        pizza.setDescription("Classic cheese and tomato");
        pizza.setCuisine(italian);
        pizza.setRestaurant(pizzaPalace);
        foodRepository.save(pizza);

        Food sushi = new Food();
        sushi.setName("Salmon Sushi");
        sushi.setPrice(8.50);
        sushi.setCategory(FoodCategory.APPETIZER);
        sushi.setDescription("Fresh salmon with rice");
        sushi.setCuisine(japanese);
        sushi.setRestaurant(sushiWorld);
        foodRepository.save(sushi);

        System.out.println("Sample data loaded successfully!");
    }
}
