import { fetchingCities } from "./fetchingData.mjs";
import { ip } from "./ipSearch.mjs";
import { showOptions } from "./utils.mjs";


async function fetchingRestaurants(){
try{
  let address = `${ip()}/api/restaurants`;
  let token = sessionStorage.getItem("accessToken");
  let cityOptionsContainer = document.getElementById('city-selected');
  if("location-id" in sessionStorage){
     address = `${ip()}/api/restaurants/by-city/${sessionStorage.getItem("location-id")}`;
  }
  if( cityOptionsContainer.textContent != "All Cities"){
    let id = parseInt(cityOptionsContainer.dataset.cityId);
    address = `${ip()}/api/restaurants/by-city/${id}`
  }
    let resp = await fetch(address, {
      method: "Get",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

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


    
  
        let btnRatingSort = document.getElementById("sort-menu");
        btnRatingSort.addEventListener("click", showOptions);
        

        let data = await fetchingRestaurants();
        
        console.log(data);

        async function renderRest(data) {
        let restaurantsList = document.getElementById("restaurants-list");
        let loadingRest = document.getElementById("restaurants-loading");

        loadingRest.classList.add("hidden");
        restaurantsList.classList.remove("hidden");

       restaurantsList.innerHTML = '';
    
        console.log(restaurantsList);


        data.forEach(data => {
            let div = document.createElement("div");
            div.classList.add("restaurant-item", "bg-white", "rounded-lg", "shadow-md", "p-4");
            div.innerHTML = `<strong>${data.name}</strong> <br> Rating: ⭐${data.rating}`;
            restaurantsList.appendChild(div);
        });
      
        chefsPick();
      }

      renderRest(data);

    async function  chefsPick() {
        data.sort((a, b) => b.rating - a.rating);
        
        let loading1 = document.getElementById("loading-1");
        let loading2 = document.getElementById("loading-2");

        if(loading1 != null && loading2 != null){
          loading1.classList.add("hidden");
        loading2.classList.add("hidden");
        }
    
        let buttonChef1 = document.getElementById("chef-pick-1");
        buttonChef1.classList.remove('text-transparent'); 
        buttonChef1.classList.add('text-black');  
        buttonChef1.disabled = false;

        if(data[0] === undefined){
          buttonChef1.textContent = "No available"
        }
        else{
          buttonChef1.textContent =  `${data[0].name} - Rating: ⭐${data[0].rating}`;
        }

        let buttonChef2 = document.getElementById("chef-pick-2");
        buttonChef2.classList.remove('text-transparent'); 
        buttonChef2.classList.add('text-black');  
        buttonChef2.disabled = false;

        if(data[1] === undefined){
          buttonChef2.textContent = "No available"
        }
        else{
          buttonChef2.textContent =  `${data[1].name} - Rating: ⭐${data[1].rating}`;
        }
    }

    let highest = document.getElementById("Highest-Rated");
    highest.addEventListener("click", sorting);

    let alphabeticalAc = document.getElementById("Alphabetical(A-Z)");
    alphabeticalAc.addEventListener("click", sorting);

    let alphabeticalDec = document.getElementById("Alphabetical(Z-A)");
    alphabeticalDec.addEventListener("click", sorting);

    async function sorting(e){
      let target = e.target;
      if(target.textContent == "Alphabetical (Z-A)"){
        data.sort((a, b) => b.name.localeCompare(a.name));
      }
      else if(target.textContent == "Alphabetical (A-Z)"){
        data.sort((a, b) => a.name.localeCompare(b.name));
      }
      else{
        data.sort((a, b) => b.rating - a.rating);
      }

        restaurantsList.innerHTML = '';

        data.forEach(data => {
            let div = document.createElement("div");
            div.classList.add("restaurant-item", "bg-white", "rounded-lg", "shadow-md", "p-4");
            div.innerHTML = `<strong>${data.name}</strong> <br> Rating: ⭐${data.rating}`;
            restaurantsList.appendChild(div);
        });

        btnRatingSort.click();
    } 

let cityDoc = document.getElementById("city-menu");
cityDoc.addEventListener("click", cityShow);
cityDoc.addEventListener("change", cityShow);



async function cityShow(e) {
    let data = await fetchingCities();
    console.log(data);

    let cityOptionsContainer = document.getElementById('city-options');
    cityOptionsContainer.innerHTML = "";

    data.forEach(city => {
      let option = document.createElement("div");
      option.textContent = city.name;
      option.classList.add('px-4', 'py-2', 'text-sm', 'hover:bg-gray-100', 'hover:text-[#ff66c4]');

      option.addEventListener("click", () => {
        document.getElementById('city-selected').textContent = city.name;
        document.getElementById('city-selected').dataset.cityId = city.id;
        document.querySelector('.origin-top-right').classList.add('hidden');
        let data1 = fetchingRestaurants();
        renderRest(data1);
        showHide(cityOptionsContainer);
      });

      cityOptionsContainer.appendChild(option);
    });
    showHide(cityOptionsContainer);
}

async function showHide(conatiner) {
  if(conatiner.parentElement.classList.contains("hidden")){
    conatiner.parentElement.classList.toggle("hidden", false);
}
else{
    conatiner.parentElement.classList.toggle("hidden", true);
}
}

