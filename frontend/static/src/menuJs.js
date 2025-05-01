import { fetchingFood } from "./fetchingData.mjs";

let params = new URLSearchParams(window.location.search);
let restaurantId = params.get("restaurant");
let restaurantName = params.get("resturantName");

try{
let dataForRest = await fetchingFood(restaurantId);
console.log(dataForRest);

if(dataForRest){

    let templateMenu = document.getElementById("template-menu");
    let loadingMenu = document.getElementById("loading-state");

    loadingMenu.classList.add("hidden");
    templateMenu.classList.remove("hidden");

    let restaurantNameDisplay = document.getElementById("name-of-restaurant");

    restaurantNameDisplay.textContent = restaurantName;

    laodingItems(dataForRest, templateMenu);
}
}
catch(e){
    console.log(e.message);
}
async function laodingItems(data) {

    data.forEach(item => {
        
        let div = document.createElement("div");
        let classes = "flex justify-between shadow-md shadow-gray-100 p-2 md:!p-4 rounded-md w-full md:!w-[50%]";
        div.classList.add(...classes.split(" "));
        div.innerHTML = `
          <div class="flex flex-col items-start gap-2">
            <h4 class="text-2xl md:text-3xl">${item.name}</h4>
            <p class="max-md:text-sm md:!text-md lg:!text-lg text-gray-500 w-[90%] md:w-[85%]">${item.description}</p>
          </div>
          <div class="flex flex-col items-end justify-between gap-2">
            <h4 class="text-2xl md:text-3xl">€${parseFloat(item.price)}</h4>
            <button class="border rounded-lg w-9 md:!w-12 h-9 md:!h-12 text-3xl md:!text-4xl bg-blue-50 hover:cursor-pointer hover:text-[#ff66c4] hover:bg-pink-50 active:opacity-70">+</button>
          </div>
      `;
        div.getElementsByTagName("button")[0].addEventListener("click", () => {
            addToCart(item.name, item.price, item.description, item.id);

        });
      if(item.foodCategory == "SALAD"){
        document.getElementById("menu-items-salad").appendChild(div);
      }
      else if(item.foodCategory == "APPETIZER"){
        document.getElementById("menu-items-appetizer").appendChild(div);
      }

      else if(item.foodCategory == "MAIN_COURSE"){
        document.getElementById("menu-items-main_course").appendChild(div);
      }
      else{
        document.getElementById("menu-items-dessert").appendChild(div);
      }
    });

    if(document.getElementById("menu-items-salad").children.length == 0){
        document.getElementById("menu-items-salad").textContent = "No food available!"
    }

    if(document.getElementById("menu-items-appetizer").children.length == 0){
        document.getElementById("menu-items-appetizer").textContent = "No food available!"
    }

    if(document.getElementById("menu-items-main_course").children.length == 0){
        document.getElementById("menu-items-main_course").textContent = "No food available!"
    }

    if(document.getElementById("menu-items-dessert").children.length == 0){
        document.getElementById("menu-items-dessert").textContent = "No food available!"
    }


}

function addToCart(name, price, description, id){

  let cart = JSON.parse(localStorage.getItem("cart")) || [];

  let existingItem = cart.find(item => item.name === name);

  if (existingItem) {
    existingItem.quantity += 1;
    if(existingItem.quantity > 6){
      alert("Max 6 of each item!");
      return;
    }
  } else {
    cart.push({ name, price, description, id, quantity: 1 });
  }

  let popup = document.getElementById("cart-popup");
  popup.style.display = "block";

  setTimeout(() => {
    popup.style.display = "none";
  }, 3000);

  localStorage.setItem("cart", JSON.stringify(cart));
}