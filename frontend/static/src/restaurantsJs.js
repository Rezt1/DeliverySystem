import { ip } from "./ipSearch.mjs";
import { showOptions, toggleDropdown } from "./utils.mjs";


async function fetchingRestaurants(){
try{
  let address = `${ip()}/api/restaurants`;
  let token = sessionStorage.getItem("accessToken");
  if("location-id" in sessionStorage){
     address = `${ip()}/api/restaurants/by-city/${sessionStorage.getItem("location-id")}`;
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
        btnRatingSort.addEventListener("click", filterByRating);

        let optionsBtn = document.getElementById("options");
        btnRatingSort.addEventListener("click", showOptions);
        

        let data = await fetchingRestaurants();
        
        console.log(data);

        let restaurantsList = document.getElementById("restaurants-list");
        let loadingRest = document.getElementById("restaurants-loading");

        loadingRest.classList.add("hidden");
        restaurantsList.classList.remove("hidden");

       // restaurantsList.innerHTML = '';
    
        console.log(restaurantsList);


        data.forEach(data => {
            let div = document.createElement("div");
            div.classList.add("restaurant-item", "bg-white", "rounded-lg", "shadow-md", "p-4");
            div.innerHTML = `<strong>${data.name}</strong> <br> Rating: ⭐${data.rating}`;
            restaurantsList.appendChild(div);
        });
      
        chefsPick();

      async function filterByRating(){

        data.sort((a, b) => b.rating - a.rating);

        restaurantsList.innerHTML = '';

        data.forEach(data => {
            let div = document.createElement("div");
            div.classList.add("restaurant-item", "bg-white", "rounded-lg", "shadow-md", "p-4");
            div.innerHTML = `<strong>${data.name}</strong> <br> Rating: ⭐${data.rating}`;
            restaurantsList.appendChild(div);
        });

      }

    async function  chefsPick() {
        data.sort((a, b) => b.rating - a.rating);
        
        let loading1 = document.getElementById("loading-1");
        let loading2 = document.getElementById("loading-2");

        loading1.classList.add("hidden");
        loading2.classList.add("hidden");

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
