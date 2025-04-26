import { fetchingCities } from "./fetchingData.mjs";
import { ip } from "./ipSearch.mjs";
import { hideEverything } from "./adminJs.mjs";

let nameInput = document.getElementById("city-name-input");
let salaryInput = document.getElementById("city-salary-input");

let menuCityBtn = document.getElementById("btn-cities");
let citiesMain = document.getElementById("cities-main");

let addCityBtn = document.getElementById("btn-add-city");

menuCityBtn.addEventListener("click", onMenuCityClick);
addCityBtn.addEventListener("click", addCity);

async function onMenuCityClick(e) {
    e.preventDefault();

    hideEverything();

    nameInput.value = "";
    salaryInput.value = "";

    citiesMain.classList.remove("hidden");

    let loadingCitiesList = document.getElementById("cities-loading-admin");
    let citiesList = document.getElementById("cities-list-admin");
 
    citiesList.classList.add("hidden");
    citiesList.innerHTML = "";
  
    loadingCitiesList.classList.remove("hidden");

    let cities = await fetchingCities();

    cities.forEach(c => {
        let divElement = document.createElement("div");
        divElement.id = c.id;
        divElement.className = "text-[#006bb8] font-bold h-14 md:h-18 border rounded-lg my-auto text-xl md:text-3xl flex justify-between items-center px-4 py-2 gap-1 overflow-x-hidden";

        let h3Element = document.createElement("h3");
        h3Element.className = "truncate";
        h3Element.textContent = c.name;

        let removeBtn = document.createElement("button");
        removeBtn.className = "border border-[#ff66c4] p-1 text-[#ff66c4] font-medium rounded-md text-sm hover:bg-[#ff66c4] hover:text-white hover:cursor-pointer active:opacity-70";
        removeBtn.textContent = "Remove";
        removeBtn.addEventListener("click", removeCity);

        divElement.appendChild(h3Element);
        divElement.appendChild(removeBtn);

        citiesList.appendChild(divElement);
    })

    loadingCitiesList.classList.add("hidden");
    citiesList.classList.remove("hidden");
}

async function removeCity(e) {
    try {
        e.preventDefault();

        let id = e.target.parentElement.id;

        let address = ip() + "/api/admin/remove-city/" + id;
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

        menuCityBtn.click();
    } catch (error) {
        console.log(error);
    }
}

async function addCity(e) {
    try {
        e.preventDefault();

        let name = nameInput.value;
        let salary = salaryInput.value;

        if (name === "" || salary === "") {
            throw new Error("All fields must be filled");
        }

        if (Number.isNaN(salary) || Number.isNaN(parseFloat(salary))) {
            throw new Error("Invalid salary")
        }

        let address = ip() + "/api/admin/create-city";
        let token = sessionStorage.getItem("accessToken");

        let cityObj = {
            name,
            salary
        }

        let response = await fetch(address, {
            method: "Post",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }, 
            body: JSON.stringify(cityObj)
        })

        if (response.status !== 201) {
            
            let jsonRespone = await response.json();

            let errorMsg = jsonRespone.errors 
                ? Object.values(jsonRespone.errors).flat().join('\n')
                : jsonRespone.message || 'Creation failed';

            throw new Error(errorMsg);
        }

        nameInput.value = "";
        salaryInput.value = "";
        menuCityBtn.click();

    } catch (error) {
        window.alert(error.message);
    }

}