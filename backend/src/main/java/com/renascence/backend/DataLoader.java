package com.renascence.backend;

import com.renascence.backend.entities.*;
import com.renascence.backend.enums.FoodCategory;
import com.renascence.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CuisineRepository cuisineRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (cityRepository.count() > 0){
            return;
        }

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

        City vratsa = new City();
        vratsa.setName("Vratsa");
        cityRepository.save(vratsa);

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

        Cuisine greek = new Cuisine();
        turkish.setName("Greek");
        cuisineRepository.save(greek);

        Cuisine bulgarian = new Cuisine();
        turkish.setName("Bulgarian");
        cuisineRepository.save(bulgarian);

        Cuisine korean = new Cuisine();
        turkish.setName("Korean");
        cuisineRepository.save(korean);

        Cuisine indian = new Cuisine();
        turkish.setName("Indian");
        cuisineRepository.save(indian);

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
        kebabHouse.setRating(5.0f);
        kebabHouse.setCity(burgas);
        restaurantRepository.save(kebabHouse);

        Restaurant vega = new Restaurant();
        vega.setName("Vega");
        vega.setDeliveryGuySalary(1200.78);
        vega.setIban("BG10988143");
        vega.setRating(5.0f);
        vega.setCity(varna);
        restaurantRepository.save(vega);

        Restaurant bulgari = new Restaurant();
        bulgari.setName("Bulgari");
        bulgari.setDeliveryGuySalary(979.00);
        bulgari.setIban("BG14574829");
        bulgari.setRating(4.0f);
        bulgari.setCity(sofia);
        restaurantRepository.save(bulgari);

        Restaurant kalos = new Restaurant();
        kalos.setName("Kalos");
        kalos.setDeliveryGuySalary(1050.00);
        kalos.setIban("GR54424567");
        kalos.setRating(4.2f);
        kalos.setCity(plovdiv);
        restaurantRepository.save(kalos);

        Restaurant balkan = new Restaurant();
        balkan.setName("Balkan");
        balkan.setDeliveryGuySalary(1030.50);
        balkan.setIban("GR55321237");
        balkan.setRating(3.7f);
        balkan.setCity(burgas);
        restaurantRepository.save(balkan);

        Restaurant royalDragon = new Restaurant();
        royalDragon.setName("Royal Dragon");
        royalDragon.setDeliveryGuySalary(1200.50);
        royalDragon.setIban("JP56323674");
        royalDragon.setRating(4.5f);
        royalDragon.setCity(sofia);
        restaurantRepository.save(royalDragon);

        Restaurant indianTaste = new Restaurant();
        indianTaste.setName("Indian Taste");
        indianTaste.setDeliveryGuySalary(1200.75);
        indianTaste.setIban("IN15835374");
        indianTaste.setRating(3.1f);
        indianTaste.setCity(sofia);
        restaurantRepository.save(indianTaste);

        Restaurant bellaItalia = new Restaurant();
        bellaItalia.setName("Bella Italia");
        bellaItalia.setDeliveryGuySalary(1300.00);
        bellaItalia.setIban("IT56382910");
        bellaItalia.setRating(4.7f);
        bellaItalia.setCity(vratsa);
        restaurantRepository.save(bellaItalia);

        Restaurant yemek = new Restaurant();
        yemek.setName("Yemek");
        yemek.setDeliveryGuySalary(1100.50);
        yemek.setIban("TR41565423");
        yemek.setRating(4.1f);
        yemek.setCity(plovdiv);
        restaurantRepository.save(yemek);

        Restaurant frenchBaguette = new Restaurant();
        frenchBaguette.setName("French Baguette");
        frenchBaguette.setDeliveryGuySalary(1150.20);
        frenchBaguette.setIban("FR16432897");
        frenchBaguette.setRating(4.3f);
        frenchBaguette.setCity(burgas);
        restaurantRepository.save(frenchBaguette);

        Restaurant kkot = new Restaurant();
        kkot.setName("Kkot");
        kkot.setDeliveryGuySalary(1250.30);
        kkot.setIban("KR96321457");
        kkot.setRating(3.6f);
        kkot.setCity(varna);
        restaurantRepository.save(kkot);

        Restaurant amigo = new Restaurant();
        amigo.setName("Amigo");
        amigo.setDeliveryGuySalary(1180.45);
        amigo.setIban("MX71394268");
        amigo.setRating(3.3f);
        amigo.setCity(sofia);
        restaurantRepository.save(amigo);

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

        Food saladeNiçoise = new Food();
        saladeNiçoise.setName("Salade niçoise");
        saladeNiçoise.setPrice(9.29);
        saladeNiçoise.setFoodCategory(FoodCategory.SALAD);
        saladeNiçoise.setDescription("Traditionally made of tomatoes, hard-boiled eggs, Niçoise olives and anchovies or tuna, dressed with olive oil, or in some historical versions, a vinaigrette");
        saladeNiçoise.setCuisine(french);
        saladeNiçoise.setRestaurant(vega);
        foodRepository.save(saladeNiçoise);

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

        Food shopskaSalad = new Food();
        shopskaSalad.setName("Shopska Salad");
        shopskaSalad.setPrice(6.50);
        shopskaSalad.setFoodCategory(FoodCategory.SALAD);
        shopskaSalad.setDescription("Traditional Bulgarian salad with tomatoes, cucumbers, onion, peppers, and white cheese.");
        shopskaSalad.setCuisine(bulgarian);
        shopskaSalad.setRestaurant(bulgari);
        foodRepository.save(shopskaSalad);

        Food tarator = new Food();
        tarator.setName("Tarator");
        tarator.setPrice(5.00);
        tarator.setFoodCategory(FoodCategory.APPETIZER);
        tarator.setDescription("Cold yogurt soup with cucumber, garlic, walnuts, and dill.");
        tarator.setCuisine(bulgarian);
        tarator.setRestaurant(bulgari);
        foodRepository.save(tarator);

        Food grilledMeat = new Food();
        grilledMeat.setName("Grilled Meat - Kyufte and Kebapche");
        grilledMeat.setPrice(9.50);
        grilledMeat.setFoodCategory(FoodCategory.MAIN_COURSE);
        grilledMeat.setDescription("Traditional Bulgarian grilled minced meat patties - Kyufte and Kebapche.");
        grilledMeat.setCuisine(bulgarian);
        grilledMeat.setRestaurant(bulgari);
        foodRepository.save(grilledMeat);

        Food stuffedPeppers = new Food();
        stuffedPeppers.setName("Stuffed Peppers with Minced Meat");
        stuffedPeppers.setPrice(10.00);
        stuffedPeppers.setFoodCategory(FoodCategory.MAIN_COURSE);
        stuffedPeppers.setDescription("Bell peppers stuffed with minced meat and rice, baked in tomato sauce.");
        stuffedPeppers.setCuisine(bulgarian);
        stuffedPeppers.setRestaurant(bulgari);
        foodRepository.save(stuffedPeppers);

        Food moussaka = new Food();
        moussaka.setName("Moussaka");
        moussaka.setPrice(12.00);
        moussaka.setFoodCategory(FoodCategory.MAIN_COURSE);
        moussaka.setDescription("Greek dish made of layers of eggplant, minced meat, and béchamel sauce.");
        moussaka.setCuisine(greek);
        moussaka.setRestaurant(kalos);
        foodRepository.save(moussaka);

        Food tzatziki = new Food();
        tzatziki.setName("Tzatziki");
        tzatziki.setPrice(5.50);
        tzatziki.setFoodCategory(FoodCategory.APPETIZER);
        tzatziki.setDescription("Greek yogurt dip with cucumber, garlic, and dill.");
        tzatziki.setCuisine(greek);
        tzatziki.setRestaurant(kalos);
        foodRepository.save(tzatziki);

        Food greekSalad = new Food();
        greekSalad.setName("Greek Salad");
        greekSalad.setPrice(7.00);
        greekSalad.setFoodCategory(FoodCategory.SALAD);
        greekSalad.setDescription("Fresh salad with tomatoes, cucumbers, olives, feta cheese, and olive oil.");
        greekSalad.setCuisine(greek);
        greekSalad.setRestaurant(kalos);
        foodRepository.save(greekSalad);

        Food dolmadakia = new Food();
        dolmadakia.setName("Dolmadakia");
        dolmadakia.setPrice(8.00);
        dolmadakia.setFoodCategory(FoodCategory.APPETIZER);
        dolmadakia.setDescription("Stuffed grape leaves with rice, herbs, and lemon.");
        dolmadakia.setCuisine(greek);
        dolmadakia.setRestaurant(kalos);
        foodRepository.save(dolmadakia);

        Food gyuvetch = new Food();
        gyuvetch.setName("Gyuvetch");
        gyuvetch.setPrice(11.50);
        gyuvetch.setFoodCategory(FoodCategory.MAIN_COURSE);
        gyuvetch.setDescription("Balkan-style slow-cooked stew with meat and vegetables.");
        gyuvetch.setCuisine(bulgarian);
        gyuvetch.setRestaurant(balkan);
        foodRepository.save(gyuvetch);

        Food banitsa = new Food();
        banitsa.setName("Banitsa");
        banitsa.setPrice(4.00);
        banitsa.setFoodCategory(FoodCategory.DESSERT);
        banitsa.setDescription("Baked pastry with layers of filo dough filled with cheese and eggs.");
        banitsa.setCuisine(bulgarian);
        banitsa.setRestaurant(balkan);
        foodRepository.save(banitsa);

        Food skewers = new Food();
        skewers.setName("Grilled Skewers");
        skewers.setPrice(10.50);
        skewers.setFoodCategory(FoodCategory.MAIN_COURSE);
        skewers.setDescription("Juicy grilled meat skewers, a popular Balkan specialty.");
        skewers.setCuisine(greek);
        skewers.setRestaurant(balkan);
        foodRepository.save(skewers);

        Food ramen = new Food();
        ramen.setName("Ramen");
        ramen.setPrice(12.00);
        ramen.setFoodCategory(FoodCategory.MAIN_COURSE);
        ramen.setDescription("Traditional Japanese noodle soup with broth, meat, and vegetables.");
        ramen.setCuisine(asian);
        ramen.setRestaurant(royalDragon);
        foodRepository.save(ramen);

        Food dimSum = new Food();
        dimSum.setName("Dim Sum");
        dimSum.setPrice(9.00);
        dimSum.setFoodCategory(FoodCategory.APPETIZER);
        dimSum.setDescription("Steamed dumplings filled with meat, seafood, or vegetables.");
        dimSum.setCuisine(asian);
        dimSum.setRestaurant(royalDragon);
        foodRepository.save(dimSum);

        Food mochi = new Food();
        mochi.setName("Mochi Ice Cream");
        mochi.setPrice(6.50);
        mochi.setFoodCategory(FoodCategory.DESSERT);
        mochi.setDescription("Sweet rice cake filled with ice cream in various flavors.");
        mochi.setCuisine(asian);
        mochi.setRestaurant(royalDragon);
        foodRepository.save(mochi);

        Food naan = new Food();
        naan.setName("Naan");
        naan.setPrice(3.50);
        naan.setFoodCategory(FoodCategory.BREAD);
        naan.setDescription("Traditional Indian flatbread baked in a tandoor oven.");
        naan.setCuisine(indian);
        naan.setRestaurant(indianTaste);
        foodRepository.save(naan);

        Food samosa = new Food();
        samosa.setName("Samosa");
        samosa.setPrice(5.00);
        samosa.setFoodCategory(FoodCategory.APPETIZER);
        samosa.setDescription("Crispy pastry filled with spiced potatoes, peas, and herbs.");
        samosa.setCuisine(indian);
        samosa.setRestaurant(indianTaste);
        foodRepository.save(samosa);

        Food sambarTandoori = new Food();
        sambarTandoori.setName("Sambar Tandoori");
        sambarTandoori.setPrice(8.50);
        sambarTandoori.setFoodCategory(FoodCategory.MAIN_COURSE);
        sambarTandoori.setDescription("Spicy lentil-based vegetable stew, cooked with tamarind and aromatic spices.");
        sambarTandoori.setCuisine(indian);
        sambarTandoori.setRestaurant(indianTaste);
        foodRepository.save(sambarTandoori);

        Food butterChicken = new Food();
        butterChicken.setName("Butter Chicken");
        butterChicken.setPrice(12.00);
        butterChicken.setFoodCategory(FoodCategory.MAIN_COURSE);
        butterChicken.setDescription("Creamy tomato-based curry with tender pieces of tandoori chicken.");
        butterChicken.setCuisine(indian);
        butterChicken.setRestaurant(indianTaste);
        foodRepository.save(butterChicken);

        Food malaiKofta = new Food();
        malaiKofta.setName("Malai Kofta");
        malaiKofta.setPrice(11.50);
        malaiKofta.setFoodCategory(FoodCategory.MAIN_COURSE);
        malaiKofta.setDescription("Soft paneer and potato dumplings served in a rich, creamy tomato sauce.");
        malaiKofta.setCuisine(indian);
        malaiKofta.setRestaurant(indianTaste);
        foodRepository.save(malaiKofta);

        Food risotto = new Food();
        risotto.setName("Risotto");
        risotto.setPrice(12.00);
        risotto.setFoodCategory(FoodCategory.MAIN_COURSE);
        risotto.setDescription("A creamy rice dish often cooked with broth, vegetables, and various seasonings.");
        risotto.setCuisine(italian);
        risotto.setRestaurant(bellaItalia);
        foodRepository.save(risotto);

        Food chickenParmesanWithPepperoni = new Food();
        chickenParmesanWithPepperoni.setName("Chicken Parmesan with Pepperoni");
        chickenParmesanWithPepperoni.setPrice(14.50);
        chickenParmesanWithPepperoni.setFoodCategory(FoodCategory.MAIN_COURSE);
        chickenParmesanWithPepperoni.setDescription("Breaded and fried chicken cutlet topped with marinara sauce, melted cheese, and pepperoni.");
        chickenParmesanWithPepperoni.setCuisine(italian);
        chickenParmesanWithPepperoni.setRestaurant(bellaItalia);
        foodRepository.save(chickenParmesanWithPepperoni);

        Food fettuccineAlfredo = new Food();
        fettuccineAlfredo.setName("Fettuccine Alfredo");
        fettuccineAlfredo.setPrice(13.00);
        fettuccineAlfredo.setFoodCategory(FoodCategory.MAIN_COURSE);
        fettuccineAlfredo.setDescription("Fettuccine pasta served with a creamy Alfredo sauce made with butter, cream, and Parmesan cheese.");
        fettuccineAlfredo.setCuisine(italian);
        fettuccineAlfredo.setRestaurant(bellaItalia);
        foodRepository.save(fettuccineAlfredo);

        Food bruschetta = new Food();
        bruschetta.setName("Bruschetta");
        bruschetta.setPrice(7.00);
        bruschetta.setFoodCategory(FoodCategory.BREAD);
        bruschetta.setDescription("Toasted bread topped with diced tomatoes, garlic, basil, and olive oil.");
        bruschetta.setCuisine(italian);
        bruschetta.setRestaurant(bellaItalia);
        foodRepository.save(bruschetta);

        Food gnocchi = new Food();
        gnocchi.setName("Gnocchi");
        gnocchi.setPrice(11.00);
        gnocchi.setFoodCategory(FoodCategory.MAIN_COURSE);
        gnocchi.setDescription("Soft potato dumplings, often served with various sauces like pesto, butter, or tomato.");
        gnocchi.setCuisine(italian);
        gnocchi.setRestaurant(bellaItalia);
        foodRepository.save(gnocchi);

        Food pide = new Food();
        pide.setName("Pide");
        pide.setPrice(9.50);
        pide.setFoodCategory(FoodCategory.MAIN_COURSE);
        pide.setDescription("Turkish-style flatbread with various toppings like minced meat, cheese, and vegetables.");
        pide.setCuisine(turkish);
        pide.setRestaurant(yemek);
        foodRepository.save(pide);

        Food gozleme = new Food();
        gozleme.setName("Gözleme");
        gozleme.setPrice(7.50);
        gozleme.setFoodCategory(FoodCategory.MAIN_COURSE);
        gozleme.setDescription("Traditional Turkish stuffed flatbread, filled with cheese, spinach, or minced meat.");
        gozleme.setCuisine(turkish);
        gozleme.setRestaurant(yemek);
        foodRepository.save(gozleme);

        Food mercimekCorbasi = new Food();
        mercimekCorbasi.setName("Mercimek Çorbası");
        mercimekCorbasi.setPrice(5.50);
        mercimekCorbasi.setFoodCategory(FoodCategory.APPETIZER);
        mercimekCorbasi.setDescription("Rich and creamy Turkish lentil soup, seasoned with spices.");
        mercimekCorbasi.setCuisine(turkish);
        mercimekCorbasi.setRestaurant(yemek);
        foodRepository.save(mercimekCorbasi);

        Food sultanKebab = new Food();
        sultanKebab.setName("Sultan Kebab");
        sultanKebab.setPrice(13.50);
        sultanKebab.setFoodCategory(FoodCategory.MAIN_COURSE);
        sultanKebab.setDescription("Slow-cooked lamb or beef kebab with vegetables, served with a creamy sauce.");
        sultanKebab.setCuisine(turkish);
        sultanKebab.setRestaurant(yemek);
        foodRepository.save(sultanKebab);

        Food croissant = new Food();
        croissant.setName("Croissant");
        croissant.setPrice(4.00);
        croissant.setFoodCategory(FoodCategory.APPETIZER);
        croissant.setDescription("Flaky, buttery pastry, perfect for breakfast or a light snack.");
        croissant.setCuisine(french);
        croissant.setRestaurant(frenchBaguette);
        foodRepository.save(croissant);

        Food ratatouille = new Food();
        ratatouille.setName("Ratatouille");
        ratatouille.setPrice(10.00);
        ratatouille.setFoodCategory(FoodCategory.MAIN_COURSE);
        ratatouille.setDescription("Traditional French vegetable stew made with eggplant, zucchini, and bell peppers.");
        ratatouille.setCuisine(french);
        ratatouille.setRestaurant(frenchBaguette);
        foodRepository.save(ratatouille);

        Food onionSoup = new Food();
        onionSoup.setName("French Onion Soup");
        onionSoup.setPrice(8.50);
        onionSoup.setFoodCategory(FoodCategory.APPETIZER);
        onionSoup.setDescription("Rich and savory soup made with caramelized onions, beef broth, and topped with melted cheese.");
        onionSoup.setCuisine(french);
        onionSoup.setRestaurant(frenchBaguette);
        foodRepository.save(onionSoup);

        Food cremeBrulee = new Food();
        cremeBrulee.setName("Crème Brûlée");
        cremeBrulee.setPrice(6.50);
        cremeBrulee.setFoodCategory(FoodCategory.DESSERT);
        cremeBrulee.setDescription("Classic French dessert with a creamy vanilla custard base and a caramelized sugar crust.");
        cremeBrulee.setCuisine(french);
        cremeBrulee.setRestaurant(frenchBaguette);
        foodRepository.save(cremeBrulee);

        Food kimchi = new Food();
        kimchi.setName("Kimchi");
        kimchi.setPrice(6.00);
        kimchi.setFoodCategory(FoodCategory.SIDE);
        kimchi.setDescription("Spicy fermented cabbage with chili peppers and garlic.");
        kimchi.setCuisine(korean);
        kimchi.setRestaurant(kkot);
        foodRepository.save(kimchi);

        Food bibimbap = new Food();
        bibimbap.setName("Bibimbap");
        bibimbap.setPrice(12.00);
        bibimbap.setFoodCategory(FoodCategory.MAIN_COURSE);
        bibimbap.setDescription("Rice bowl topped with sautéed vegetables, sliced meat, egg, and spicy gochujang sauce.");
        bibimbap.setCuisine(korean);
        bibimbap.setRestaurant(kkot);
        foodRepository.save(bibimbap);

        Food tteokbokki = new Food();
        tteokbokki.setName("Tteokbokki");
        tteokbokki.setPrice(9.50);
        tteokbokki.setFoodCategory(FoodCategory.MAIN_COURSE);
        tteokbokki.setDescription("Chewy rice cakes in a spicy and slightly sweet gochujang sauce.");
        tteokbokki.setCuisine(korean);
        tteokbokki.setRestaurant(kkot);
        foodRepository.save(tteokbokki);

        Food hotteok = new Food();
        hotteok.setName("Hotteok");
        hotteok.setPrice(5.50);
        hotteok.setFoodCategory(FoodCategory.DESSERT);
        hotteok.setDescription("Sweet Korean pancakes filled with brown sugar, cinnamon, and nuts.");
        hotteok.setCuisine(korean);
        hotteok.setRestaurant(kkot);
        foodRepository.save(hotteok);

        Food tacos = new Food();
        tacos.setName("Tacos");
        tacos.setPrice(10.00);
        tacos.setFoodCategory(FoodCategory.MAIN_COURSE);
        tacos.setDescription("Traditional Mexican dish made with corn tortillas filled with meat, vegetables, and salsa.");
        tacos.setCuisine(mexican);
        tacos.setRestaurant(amigo);
        foodRepository.save(tacos);

        Food burrito = new Food();
        burrito.setName("Burrito");
        burrito.setPrice(11.50);
        burrito.setFoodCategory(FoodCategory.MAIN_COURSE);
        burrito.setDescription("Large flour tortilla filled with rice, beans, meat, and cheese.");
        burrito.setCuisine(mexican);
        burrito.setRestaurant(amigo);
        foodRepository.save(burrito);

        Food quesadilla = new Food();
        quesadilla.setName("Quesadilla");
        quesadilla.setPrice(9.00);
        quesadilla.setFoodCategory(FoodCategory.MAIN_COURSE);
        quesadilla.setDescription("Grilled tortilla filled with melted cheese and other ingredients like chicken or vegetables.");
        quesadilla.setCuisine(mexican);
        quesadilla.setRestaurant(amigo);
        foodRepository.save(quesadilla);

        Food churros = new Food();
        churros.setName("Churros");
        churros.setPrice(6.50);
        churros.setFoodCategory(FoodCategory.DESSERT);
        churros.setDescription("Crispy fried dough pastries sprinkled with cinnamon sugar and served with chocolate sauce.");
        churros.setCuisine(mexican);
        churros.setRestaurant(amigo);
        foodRepository.save(churros);

        //Roles
        Role deliveryGuyRole = new Role();
        deliveryGuyRole.setName(com.renascence.backend.enums.Role.DELIVERY_GUY.toString());
        roleRepository.save(deliveryGuyRole);

        Role ownerRole = new Role();
        ownerRole.setName(com.renascence.backend.enums.Role.OWNER.toString());
        roleRepository.save(ownerRole);

        Role adminRole = new Role();
        adminRole.setName(com.renascence.backend.enums.Role.ADMIN.toString());
        roleRepository.save(adminRole);

        //Users
        User customer1 = new User();
        customer1.setEmail("customer1@gmail.com");
        customer1.setPassword(encoder.encode("customer1"));
        customer1.setName("Gosho");
        customer1.setPhoneNumber("+359 0594231552");
        customer1.setLocation(sofia);
        userRepository.save(customer1);

        User admin1 = new User();
        admin1.setEmail("admin1@gmail.com");
        admin1.setPassword(encoder.encode("admin1"));
        admin1.setName("Tosho");
        admin1.setPhoneNumber("+359 0596131442");
        admin1.setLocation(sofia);
        admin1.getRoles().add(adminRole);
        userRepository.save(admin1);

        System.out.println("Sample data loaded successfully!");
    }
}
