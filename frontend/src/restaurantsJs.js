async function fetchingRestaurants(){

    let resp = await fetch(`http://localhost:8080/api/restaurants`);
    let data = await resp.json();
    return data;
    
    }

    export async function displayDashBoard(){

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
      
      }
      displayDashBoard();