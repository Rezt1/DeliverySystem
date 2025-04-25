import { ip } from "./ipSearch.mjs";
import { hideEverything } from "./adminJs.mjs";
import { fetchingCities } from "./fetchingData.mjs";

let menuMenusBtn = document.getElementById("btn-menus");

let menuMain = document.getElementById("menus-main"); 

menuMenusBtn.addEventListener("click", onMenuMenusClick);

async function onMenuMenusClick() {
    hideEverything();

    menuMain.classList.remove("hidden");
}