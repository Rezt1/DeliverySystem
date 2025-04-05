import { ip } from "./ipSearch.mjs";
import { showOptions } from "./utils.mjs";

async function fetchingCities(){
    try{

        let address = ip();
        let token = sessionStorage.getItem("accessToken");
        let resp = await fetch(`${address}/api/cities`, {
      method: "Get",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }});

      if (!resp.ok) {
        let errorData = await resp.json();
        throw new Error(errorData.message || 'Failed to fetch restaurants');
      }

      let data = await resp.json();
      console.log(data);
      return data;

    }
    catch(e){
        console.error(e);
    }
}


let menuCities = document.getElementById("search-main-dropdown-3");
menuCities.addEventListener("click", showOptionsCities);
menuCities.addEventListener("change", picked);

let btnSearch = document.getElementById("btn-search-main");
console.log(btnSearch);
btnSearch.addEventListener("click", setCity)

async function showOptionsCities(e){
    try{
    e.preventDefault();

    let target = e.target;
    let isToggled = target.dataset.customInfo === "istoggled";

    target.dataset.customInfo = isToggled ? "nottoggled" : "istoggled";

    if(!isToggled){
        while (target.options.length > 1) {
            target.remove(1);
        }

        let data = await fetchingCities();
        data.forEach(data => {
            let option = document.createElement("option");
            option.id = data.id;
            option.addEventListener("change", picked); 
            option.textContent = data.name;   
            target.appendChild(option);
        });
    }

}
catch(e){
    console.error("Failed to load cities:", error);
    target.dataset.customInfo = "nottoggled";
}
}
    
async function picked(e) {
    e.preventDefault();
    let target = e.target;
    let selectedOption = target.options[target.selectedIndex];
    let spaceForCIty = document.getElementById("option-city");
    spaceForCIty.textContent = selectedOption.textContent +  "-" + selectedOption.id;
}



async function setCity() {
    try{
    let spaceForCIty = document.getElementById("option-city");
    let nameAndID = spaceForCIty.textContent.split("-");
    if(nameAndID[0] == "City"){

        throw new Error("Pleace pick a city!");

        }
    sessionStorage.setItem("location", nameAndID[0]);
    sessionStorage.setItem("location-id", nameAndID[1]);
    window.location.href = "./restaurants.html";
}
catch(e){
    window.alert(e.message);
}
}