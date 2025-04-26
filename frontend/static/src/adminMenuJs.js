import { ip } from "./ipSearch.mjs";
import { hideEverything } from "./adminJs.mjs";

let categories = ["APPETIZER", "MAIN_COURSE", "DESSERT", "SALAD", "BREAD", "STARTER"];

let nameInput = document.getElementById("item-name");
let priceInput = document.getElementById("item-price");
let descriptionInput = document.getElementById("item-description");

let menuMenusBtn = document.getElementById("btn-menus");
let menuMain = document.getElementById("menus-main"); 
let menu = document.getElementById("menu-items-admin");
let loadingMenu = document.getElementById("menu-loading-admin");

let addFoodBtn = document.getElementById("btn-add-item");

let restaurantsDropdown = document.getElementById("restaurants-name-search");
let firstCategoriesDropdown = document.getElementById("categories-name-search");
let secondCategoriesDropdown = document.getElementById("categories-name-input");
let cuisinesDropdown = document.getElementById("cuisine-name-input");

menuMenusBtn.addEventListener("click", onMenuMenusClick);
addFoodBtn.addEventListener("click", addFood);

async function onMenuMenusClick(e) {
    e.preventDefault(); 

    hideEverything();

    menu.firstElementChild.classList.add("hidden");
    menu.classList.add("hidden");

    loadingMenu.classList.remove("hidden");

    fillRestaurantDropdown();
    fillCategoriesDropdown();
    fillCuisinesDropdown();
    removeInputValues();

    menuMain.classList.remove("hidden");
}

async function showFood(e) {
    try {
        e.preventDefault();

        while (menu.children.length > 1) {
            menu.removeChild(menu.lastChild);
        }

        loadingMenu.classList.remove("hidden");
    
        let id = e.target.value;

        let address = ip() + "/api/foods/restaurant/" + id;
        
        let foods = await fetch(address, {
            method: "Get",
            headers: {
                'Content-Type': 'application/json'
            }
        });

        let foodsData = await foods.json();

        foodsData.forEach(f => {
            let outerDivEl = document.createElement("div");
            outerDivEl.id = f.id;
            outerDivEl.setAttribute("data-category", f.foodCategory);
            outerDivEl.className = "flex flex-col gap-2 justify-between shadow-md bg-gray-100 p-2 md:!p-4 rounded-md w-full lg:w-[50%]";

            let firstInnerDivEl = document.createElement("div");
            firstInnerDivEl.className = "flex flex-wrap items-start justify-between w-full gap-2";

            let firstInnerDivH4El = document.createElement("h4");
            firstInnerDivH4El.className = "text-2xl lg:text-3xl";
            firstInnerDivH4El.textContent = f.name;

            let secondInnerDivH4El = document.createElement("h4");
            secondInnerDivH4El.className = "text-2xl lg:text-3xl";
            secondInnerDivH4El.textContent = "€" + f.price;

            firstInnerDivEl.append(firstInnerDivH4El, secondInnerDivH4El);

            let secondInnerDivEl = document.createElement("div");
            secondInnerDivEl.className = "flex flex-wrap items-start justify-between w-full gap-2";

            let pEl = document.createElement("p");
            pEl.className = "text-sm lg:text-lg text-gray-500 w-[70%] md:w-[60%]";
            pEl.textContent = f.description;

            let button = menu.firstElementChild.querySelector("button");
            let clonedBtn = button.cloneNode(true);
            clonedBtn.addEventListener("click", removeFood);

            secondInnerDivEl.append(pEl, clonedBtn);

            outerDivEl.append(firstInnerDivEl, secondInnerDivEl);

            menu.appendChild(outerDivEl);
        });

        loadingMenu.classList.add("hidden");
        menu.classList.remove("hidden");

    } catch (error) {
        console.log(error);
    }
}

async function removeFood(e) {
    try {
        e.preventDefault();

        let id = e.target.closest("div").parentElement.id;
        console.log(id);
        let address = ip() + "/api/admin/remove-food/" + id;
        let token = sessionStorage.getItem("accessToken");

        let response = await fetch(address, {
            method: "Delete",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })

        if (!response.ok) {
            let errorData = await response.json();
            throw new Error(errorData.message || "failed to remove city");
        }

        restaurantsDropdown.dispatchEvent(new Event("change"));
    } catch (error) {
        console.log(error);
    }
}

async function addFood(e) {
    try {
        e.preventDefault();

        let categoryInput = document.getElementById("categories-name-input");
        let cuisineInput = document.getElementById("cuisine-name-input");
        let restaurantInput = document.getElementById("restaurants-name-search");

        let name = nameInput.value;
        let foodCategory = categoryInput.value;
        let cuisineId = cuisineInput.value;
        let price = priceInput.value;
        let description = descriptionInput.value;
        let restaurantId = restaurantInput.value;

        if (name === "" || foodCategory === "" 
            || cuisineId === "" || price === "" || description === "") {
            throw new Error("All fields must be filled");
        }

        if (Number.isNaN(price) || Number.isNaN(parseFloat(price))) {
            throw new Error("Invalid price")
        }

        if (restaurantId === "") {
            throw new Error("Restaurant must be selected")
        }

        let address = ip() + "/api/admin/create-food";
        let token = sessionStorage.getItem("accessToken");

        let foodObj = {
            name,
            foodCategory,
            cuisineId,
            price,
            description,
            restaurantId
        };

        let response = await fetch(address, {
            method: "Post",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }, 
            body: JSON.stringify(foodObj)
        })

        if (response.status !== 201) {
            
            let jsonRespone = await response.json();

            let errorMsg = jsonRespone.errors 
                ? Object.values(jsonRespone.errors).flat().join('\n')
                : jsonRespone.message || 'Creation failed';

            throw new Error(errorMsg);
        }

        nameInput.value = "";
        priceInput.value = "";
        descriptionInput.value = "";
        secondCategoriesDropdown.selectedIndex = 0;
        cuisinesDropdown.selectedIndex = 0;

        restaurantsDropdown.dispatchEvent(new Event("change"));

    } catch (error) {
        window.alert(error.message);
    }
}

async function fillRestaurantDropdown() {
    let address = ip() + "/api/restaurants";
    
    let restaurants = await fetch(address, {
        method: "Get",
        headers: {
            'Content-Type': 'application/json'
        }
    });

    let optionsToRemove = restaurantsDropdown.querySelectorAll("option:not(:first-child)");
    optionsToRemove.forEach(o => o.remove());

    let restaurantData = await restaurants.json();

    restaurantData.forEach(r => {
        let optionEl = document.createElement("Option");
        optionEl.value = r.id;
        optionEl.textContent = r.name;

        restaurantsDropdown.appendChild(optionEl);
    });

    restaurantsDropdown.selectedIndex = 0;

    restaurantsDropdown.addEventListener("change", showFood);
}

function fillCategoriesDropdown() {
    let optionsToRemove = firstCategoriesDropdown.querySelectorAll("option:not(:first-child)");
    optionsToRemove.forEach(o => o.remove());

    let optionsToRemoveAlso = secondCategoriesDropdown.querySelectorAll("option:not(:first-child)");
    optionsToRemoveAlso.forEach(o => o.remove());

    categories.forEach(c => {
        let optionEl = document.createElement("Option");
        optionEl.value = c;
        optionEl.textContent = c;

        firstCategoriesDropdown.appendChild(optionEl);
        secondCategoriesDropdown.appendChild(optionEl.cloneNode(true));
    });

    firstCategoriesDropdown.selectedIndex = 0;
    secondCategoriesDropdown.selectedIndex = 0;
}

async function fillCuisinesDropdown() {
    let address = ip() + "/api/cuisines";
    
    let cuisines = await fetch(address, {
        method: "Get",
        headers: {
            'Content-Type': 'application/json'
        }
    });

    let optionsToRemove = cuisinesDropdown.querySelectorAll("option:not(:first-child)");
    optionsToRemove.forEach(o => o.remove());

    let cuisineData = await cuisines.json();

    cuisineData.forEach(c => {
        let optionEl = document.createElement("Option");
        optionEl.value = c.id;
        optionEl.textContent = c.name;

        cuisinesDropdown.appendChild(optionEl);
    });

    cuisinesDropdown.selectedIndex = 0;
}

function removeInputValues() {
    nameInput.value = "";
    priceInput.value = "";
    descriptionInput.value = "";
}