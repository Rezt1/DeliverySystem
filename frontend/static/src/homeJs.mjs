import { fetchingCities } from "./fetchingData.mjs";
import { ifAdmin, ifDeliveryGuy } from "./userWork.mjs";

console.log(sessionStorage.getItem("accessToken"));

let menuCities = document.getElementById("search-main-dropdown-3");
menuCities.addEventListener("click", showOptionsCities);
menuCities.addEventListener("change", picked);

let btnSearch = document.getElementById("btn-search-main");
console.log(btnSearch);
btnSearch.addEventListener("click", setCity);

let btnMyDelivery = document.getElementById("my-delivery-button");
btnMyDelivery.addEventListener("click", () => {
    window.location.href = "./my_delivery.html";
});

if (ifDeliveryGuy()) {
    btnMyDelivery.classList.remove("hidden");
} else {
    btnMyDelivery.classList.add("hidden");
}

if(ifAdmin()){
    window.location.href = "./admin.html"
}

export async function showOptionsCities(e) {
    try {
        e.preventDefault();

        let target = document.getElementById("search-main-dropdown-3");
        let isToggled = target.dataset.customInfo === "istoggled";

        target.dataset.customInfo = isToggled ? "nottoggled" : "istoggled";

        if (!isToggled) {
            while (target.options.length > 1) {
                target.remove(1);
            }

            let data = await fetchingCities();

            data.forEach(data => {
                let option = document.createElement("option");
                option.id = data.id;
                option.textContent = data.name;
                target.appendChild(option);
            });
        }

    } catch (e) {
        console.error("Failed to load cities:", e);
        document.getElementById("search-main-dropdown-3").dataset.customInfo = "nottoggled";
    }
}

async function picked(e) {
    e.preventDefault();
    let target = e.target;
    let selectedOption = target.options[target.selectedIndex];
    let spaceForCIty = document.getElementById("option-city");
    spaceForCIty.textContent = selectedOption.textContent + "-" + selectedOption.id;
    
}

async function setCity() {
    try {
        let spaceForCIty = document.getElementById("option-city");
        let nameAndID = spaceForCIty.textContent.split("-");
        if (nameAndID.length !== 2 || !nameAndID[1]) {
            throw new Error("Please pick a city!");
        }
        sessionStorage.setItem("location", nameAndID[0]);
        sessionStorage.setItem("location-id", nameAndID[1]);
        window.location.href = "./restaurants.html";
    } catch (e) {
        window.alert(e.message);
    }
}
