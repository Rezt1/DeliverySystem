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
        City sofia = new City();
        sofia.setName("Sofia");
        cityRepository.save(sofia);

        City varna = new City();
        varna.setName("Varna");
        cityRepository.save(varna);

        City plovdiv = new City();
        plovdiv.setName("Plovdiv");
        cityRepository.save(plovdiv);

        City burgas = new City();
        burgas.setName("Burgas");
        cityRepository.save(burgas);

        // 2. Cuisines
        Cuisine italian = new Cuisine();
        italian.setName("Italian");
        cuisineRepository.save(italian);

        Cuisine japanese = new Cuisine();
        japanese.setName("Japanese");
        cuisineRepository.save(japanese);

        Cuisine french = new Cuisine();
        french.setName("French");
        cuisineRepository.save(french);

        Cuisine chinese = new Cuisine();
        chinese.setName("Chinese");
        cuisineRepository.save(chinese);

        Cuisine mexican = new Cuisine();
        mexican.setName("Mexican");
        cuisineRepository.save(mexican);

        Cuisine turkish = new Cuisine();
        turkish.setName("Turkish");
        cuisineRepository.save(turkish);

        // 3. Restaurants
        Restaurant italianFantasy = new Restaurant();
        italianFantasy.setName("Italian Fantasy");
        italianFantasy.setDeliveryGuySalary(915.50);
        italianFantasy.setIban("GB123456789");
        italianFantasy.setRating(4.5f);
        italianFantasy.setCity(sofia);
        restaurantRepository.save(italianFantasy);

        Restaurant sushiWorld = new Restaurant();
        sushiWorld.setName("Sushi World");
        sushiWorld.setDeliveryGuySalary(1018.75);
        sushiWorld.setIban("JP987654321");
        sushiWorld.setRating(4.8f);
        sushiWorld.setCity(plovdiv);
        restaurantRepository.save(sushiWorld);

        Restaurant kebabHouse = new Restaurant();
        kebabHouse.setName("Kebab House");
        kebabHouse.setDeliveryGuySalary(1000.00);
        kebabHouse.setIban("TR11985643");
        kebabHouse.setRating(4.0f);
        kebabHouse.setCity(burgas);
        restaurantRepository.save(kebabHouse);

        Restaurant vega = new Restaurant();
        vega.setName("Vega");
        vega.setDeliveryGuySalary(1200.78);
        vega.setIban("BG10988143");
        vega.setRating(5.0f);
        vega.setCity(varna);
        restaurantRepository.save(vega);

        // 4. Foods
        Food pizza = new Food();
        pizza.setName("Margherita Pizza");
        pizza.setPrice(12.99);
        pizza.setFoodCategory(FoodCategory.MAIN_COURSE);
        pizza.setDescription("Classic cheese and tomato");
        pizza.setCuisine(italian);
        pizza.setRestaurant(italianFantasy);
        foodRepository.save(pizza);

        Food pastaCarbonara = new Food();
        pastaCarbonara.setName("Pasta Carbonara");
        pastaCarbonara.setPrice(10.50);
        pastaCarbonara.setFoodCategory(FoodCategory.MAIN_COURSE);
        pastaCarbonara.setDescription("Italian pasta with ham, mushrooms and cheese");
        pastaCarbonara.setCuisine(italian);
        pastaCarbonara.setRestaurant(italianFantasy);
        foodRepository.save(pastaCarbonara);

        Food lasagna = new Food();
        lasagna.setName("Lasagna");
        lasagna.setPrice(10.50);
        lasagna.setFoodCategory(FoodCategory.MAIN_COURSE);
        lasagna.setDescription("Layers of pasta alternating with fillings such as ragù, béchamel sauce, vegetables, cheeses, and seasonings and spices");
        lasagna.setCuisine(italian);
        lasagna.setRestaurant(italianFantasy);
        foodRepository.save(lasagna);

        Food tiramisu = new Food();
        tiramisu.setName("Tiramisu");
        tiramisu.setPrice(7.99);
        tiramisu.setFoodCategory(FoodCategory.DESSERT);
        tiramisu.setDescription("Classic cheese and tomato");
        tiramisu.setCuisine(italian);
        tiramisu.setRestaurant(italianFantasy);
        foodRepository.save(tiramisu);

        Food sushi = new Food();
        sushi.setName("Salmon Sushi");
        sushi.setPrice(8.50);
        sushi.setFoodCategory(FoodCategory.APPETIZER);
        sushi.setDescription("Fresh salmon with rice");
        sushi.setCuisine(japanese);
        sushi.setRestaurant(sushiWorld);
        foodRepository.save(sushi);

        Food tempura = new Food();
        tempura.setName("Tempura");
        tempura.setPrice(18.90);
        tempura.setFoodCategory(FoodCategory.APPETIZER);
        tempura.setDescription("Deep-fried seafood and vegetables, coated in a thin batter");
        tempura.setCuisine(japanese);
        tempura.setRestaurant(sushiWorld);
        foodRepository.save(tempura);

        Food yakitori = new Food();
        yakitori.setName("Yakitori");
        yakitori.setPrice(13.49);
        yakitori.setFoodCategory(FoodCategory.MAIN_COURSE);
        yakitori.setDescription("Seasoned with tare sauce or salt skewered chicken grilled over a charcoal firegrilled over a charcoal fire");
        yakitori.setCuisine(japanese);
        yakitori.setRestaurant(sushiWorld);
        foodRepository.save(yakitori);

        Food donburi = new Food();
        donburi.setName("Donburi");
        donburi.setPrice(20.49);
        donburi.setFoodCategory(FoodCategory.MAIN_COURSE);
        donburi.setDescription(" \"Rice-bowl dish\" consisting of fish, meat, vegetables or other ingredients simmered together and served over rice");
        donburi.setCuisine(japanese);
        donburi.setRestaurant(sushiWorld);
        foodRepository.save(donburi);

        Food coqAuVin = new Food();
        coqAuVin.setName("Coq au vin");
        coqAuVin.setPrice(13.29);
        coqAuVin.setFoodCategory(FoodCategory.MAIN_COURSE);
        coqAuVin.setDescription("French dish of chicken braised with wine, lardons, mushrooms, and optionally garlic");
        coqAuVin.setCuisine(french);
        coqAuVin.setRestaurant(vega);
        foodRepository.save(coqAuVin);

        Food saladeNicoise = new Food();
        saladeNicoise.setName("Salade niçoise");
        saladeNicoise.setPrice(9.29);
        saladeNicoise.setFoodCategory(FoodCategory.SALAD);
        saladeNicoise.setDescription("Traditionally made of tomatoes, hard-boiled eggs, Niçoise olives and anchovies or tuna, dressed with olive oil, or in some historical versions, a vinaigrette");
        saladeNicoise.setCuisine(french);
        saladeNicoise.setRestaurant(vega);
        foodRepository.save(saladeNicoise);

        Food quicheLorraine = new Food();
        quicheLorraine.setName("Quiche Lorraine");
        quicheLorraine.setPrice(16.99);
        quicheLorraine.setFoodCategory(FoodCategory.MAIN_COURSE);
        quicheLorraine.setDescription("Savoury French tart with a filling of cream, eggs, and bacon or ham, in an open pastry case");
        quicheLorraine.setCuisine(french);
        quicheLorraine.setRestaurant(vega);
        foodRepository.save(quicheLorraine);

        Food macaron = new Food();
        macaron.setName("French macaroon");
        macaron.setPrice(6.50);
        macaron.setFoodCategory(FoodCategory.DESSERT);
        macaron.setDescription("Sweet meringue-based confection made with egg white, icing sugar, granulated sugar, almond meal, and often food colouring");
        macaron.setCuisine(french);
        macaron.setRestaurant(vega);
        foodRepository.save(macaron);

        Food croissant  = new Food();
        croissant.setName("Croissant");
        croissant.setPrice(3.50);
        croissant.setFoodCategory(FoodCategory.DESSERT);
        croissant.setDescription("Buttery, flaky, viennoiserie pastry made from a laminated yeast dough similar to puff pastry");
        croissant.setCuisine(french);
        croissant.setRestaurant(vega);
        foodRepository.save(croissant);

        Food bouillabaisse = new Food();
        bouillabaisse.setName("Bouillabaisse");
        bouillabaisse.setPrice(6.50);
        bouillabaisse.setFoodCategory(FoodCategory.APPETIZER);
        bouillabaisse.setDescription("traditional Provençal fish soup");
        bouillabaisse.setCuisine(french);
        bouillabaisse.setRestaurant(vega);
        foodRepository.save(bouillabaisse);

        Food kungPaoChicken = new Food();
        kungPaoChicken.setName("Kung Pao Chicken");
        kungPaoChicken.setPrice(14.89);
        kungPaoChicken.setFoodCategory(FoodCategory.MAIN_COURSE);
        kungPaoChicken.setDescription("Famous Sichuan-style specialty with diced chicken, dried chili, cucumber, and fried peanuts (or cashews)");
        kungPaoChicken.setCuisine(chinese);
        kungPaoChicken.setRestaurant(sushiWorld);
        foodRepository.save(kungPaoChicken);

        Food dumplings = new Food();
        dumplings.setName("Dumplings");
        dumplings.setPrice(7.49);
        dumplings.setFoodCategory(FoodCategory.APPETIZER);
        dumplings.setDescription("Consist of minced meat and/or chopped vegetables wrapped in a thin dough skin");
        dumplings.setCuisine(chinese);
        dumplings.setRestaurant(sushiWorld);
        foodRepository.save(dumplings);

        Food friedRice = new Food();
        friedRice.setName("Fried Rice");
        friedRice.setPrice(7.49);
        friedRice.setFoodCategory(FoodCategory.MAIN_COURSE);
        friedRice.setDescription("Fried cooked rice and other ingredients, often including eggs, vegetables, seafood, or meat");
        friedRice.setCuisine(chinese);
        friedRice.setRestaurant(sushiWorld);
        foodRepository.save(friedRice);

        Food burritos  = new Food();
        burritos.setName("Burritos");
        burritos.setPrice(13.50);
        burritos.setFoodCategory(FoodCategory.APPETIZER);
        burritos.setDescription("Tortilla filled with meat, vegetables and sauce");
        burritos.setCuisine(mexican);
        burritos.setRestaurant(vega);
        foodRepository.save(burritos);

        Food guacamole = new Food();
        guacamole.setName("Guacamole");
        guacamole.setPrice(10.50);
        guacamole.setFoodCategory(FoodCategory.APPETIZER);
        guacamole.setDescription("Avocado-based dip, spread, or salad");
        guacamole.setCuisine(mexican);
        guacamole.setRestaurant(vega);
        foodRepository.save(guacamole);

        Food nachos = new Food();
        nachos.setName("Nachos");
        nachos.setPrice(3.99);
        nachos.setFoodCategory(FoodCategory.APPETIZER);
        nachos.setDescription("Tortilla chips or totopos covered with cheese or chile con queso");
        nachos.setCuisine(mexican);
        nachos.setRestaurant(vega);
        foodRepository.save(nachos);

        Food baklava = new Food();
        baklava.setName("Baklava");
        baklava.setPrice(17.89);
        baklava.setFoodCategory(FoodCategory.DESSERT);
        baklava.setDescription("Layered pastry dessert made of filo pastry, filled with chopped nuts, and sweetened with syrup or honey");
        baklava.setCuisine(turkish);
        baklava.setRestaurant(kebabHouse);
        foodRepository.save(baklava);

        Food shishKebab = new Food();
        shishKebab.setName("Shish kebab");
        shishKebab.setPrice(9.39);
        shishKebab.setFoodCategory(FoodCategory.MAIN_COURSE);
        shishKebab.setDescription("Popular meal of skewered and grilled cubes of meat");
        shishKebab.setCuisine(turkish);
        shishKebab.setRestaurant(kebabHouse);
        foodRepository.save(shishKebab);

        Food bakedPotato = new Food();
        bakedPotato.setName("Baked potato");
        bakedPotato.setPrice(7.89);
        bakedPotato.setFoodCategory(FoodCategory.APPETIZER);
        bakedPotato.setDescription("Preparation of potato served with fillings, toppings or condiments such as butter, cheese, sour cream, gravy, baked beans and tuna");
        bakedPotato.setCuisine(turkish);
        bakedPotato.setRestaurant(kebabHouse);
        foodRepository.save(bakedPotato);

        Food meze = new Food();
        meze.setName("Meze");
        meze.setPrice(10.00);
        meze.setFoodCategory(FoodCategory.APPETIZER);
        meze.setDescription("Colorful spread of hot and cold dishes, made for nibbling or having as an appetizer alongside raki");
        meze.setCuisine(turkish);
        meze.setRestaurant(kebabHouse);
        foodRepository.save(meze);

        Food simit = new Food();
        simit.setName("Simit");
        simit.setPrice(10.00);
        simit.setFoodCategory(FoodCategory.BREAD);
        simit.setDescription("Circular bread, typically encrusted with sesame seeds or, less commonly, poppy, flax or sunflower seeds");
        simit.setCuisine(turkish);
        simit.setRestaurant(kebabHouse);
        foodRepository.save(simit);

        System.out.println("Sample data loaded successfully!");
    }
}
