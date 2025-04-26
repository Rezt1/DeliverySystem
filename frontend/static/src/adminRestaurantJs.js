import { ip } from "./ipSearch.mjs";
import { hideEverything } from "./adminJs.mjs";
import { fetchingCities } from "./fetchingData.mjs";

let nameInput = document.getElementById("restaurant-name-input");
let ibanInput = document.getElementById("restaurant-iban-input");
let ratingInput = document.getElementById("restaurant-rating-input");

let menuRestaurantsBtn = document.getElementById("btn-restaurants");
let addRestaurantBtn = document.getElementById("btn-add-restaurant");

let restaurantsMain = document.getElementById("restaurants-main");

menuRestaurantsBtn.addEventListener("click", onMenuRestaurantClick);
addRestaurantBtn.addEventListener("click", addRestaurant);

async function onMenuRestaurantClick(e) {
    e.preventDefault();

    let loadingState = document.getElementById("restaurants-loading-admin");
    let restaurantsList = document.getElementById("restaurants-list-admin");

    hideEverything();

    restaurantsList.classList.add("hidden");
    restaurantsList.innerHTML = "";

    loadingState.classList.remove("hidden");
    removeInputValues();
    restaurantsMain.classList.remove("hidden");

    let address = ip() + "/api/restaurants";
    let token = sessionStorage.getItem("accessToken");

    let restaurants = await fetch(address, {
        method: "Get",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    });

    restaurants = await restaurants.json();

    restaurants.forEach(r => {
        let divEl = document.createElement("div");
        divEl.id = r.id;
        divEl.className = "text-[#006bb8] font-bold h-14 md:h-16 lg:h-18 border rounded-lg my-auto max-md:text-xl md:!text-2xl lg:!text-3xl flex justify-between items-center py-2 px-4 gap-1 overflow-x-hidden";

        let h3El = document.createElement("h3");
        h3El.className = "truncate";
        h3El.textContent = r.name;

        let removeBtn = document.createElement("Button");
        removeBtn.className = "border border-[#ff66c4] p-1 text-[#ff66c4] font-medium rounded-md text-sm hover:bg-[#ff66c4] hover:text-white hover:cursor-pointer active:opacity-70";
        removeBtn.textContent = "Remove";

        removeBtn.addEventListener("click", removeRestaurant);

        divEl.append(h3El, removeBtn);

        restaurantsList.appendChild(divEl);
    });

    let cityDropdown = document.getElementById("city-input");
    
    let optionsToRemove = document.querySelectorAll("#city-input option:not(:first-child)");
    optionsToRemove.forEach(o => o.remove());

    let cities = await fetchingCities();
    
    cities.forEach(c => {
        let optionEl = document.createElement("Option");
        optionEl.value = c.id;
        optionEl.textContent = c.name;

        cityDropdown.appendChild(optionEl);
    })

    cityDropdown.selectedIndex = 0;
    
    loadingState.classList.add("hidden");
    restaurantsList.classList.remove("hidden");
}

async function removeRestaurant(e) {
    try {
        e.preventDefault();

        let id = e.target.parentElement.id;

        let address = ip() + "/api/admin/remove-restaurant/" + id;
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

        menuRestaurantsBtn.click();
    } catch (error) {
        console.log(error);
    }
}

async function addRestaurant(e) {
    try {
        e.preventDefault();

        let cityInput = document.getElementById("city-input");

        let name = nameInput.value;
        let iban = ibanInput.value;
        let rating = ratingInput.value;
        let cityId = cityInput.value;

        if (name === "" || iban === "" || rating === "" || cityId === "") {
            throw new Error("All fields must be filled");
        }

        let address = ip() + "/api/admin/create-restaurant";
        let token = sessionStorage.getItem("accessToken");

        let restaurantObj = {
            name,
            iban,
            rating,
            cityId
        }

        let response = await fetch(address, {
            method: "Post",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }, 
            body: JSON.stringify(restaurantObj)
        })

        if (response.status !== 201) {
            
            let jsonRespone = await response.json();

            let errorMsg = jsonRespone.errors 
                ? Object.values(jsonRespone.errors).flat().join('\n')
                : jsonRespone.message || 'Creation failed';

            throw new Error(errorMsg);
        }

        nameInput.value = "";
        ibanInput.value = "";
        ratingInput.value = "";

        menuRestaurantsBtn.click();
    
    } catch (error) {
        window.alert(error.message);
    }
}

function removeInputValues() {
    nameInput.value = "";
    ibanInput.value = "";
    ratingInput.value = "";
}