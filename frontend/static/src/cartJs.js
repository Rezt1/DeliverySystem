

let cartItems = JSON.parse(localStorage.getItem("cart")) || [];

let itemSummery = document.getElementById("order-summary");

document.getElementById("continue-shopping").addEventListener("click", () => {
  window.location.href = "./restaurants.html";
})

document.getElementById("checkout").addEventListener("click", checkout);

if(localStorage.getItem("cart") !=  null){
    let classes = "grid grid-cols-[45%_30%_25%] md:grid-cols-[55%_25%_20%] py-4 border-gray-300 border-b-2"
    cartItems.forEach(item => {
        let itemInfo = document.createElement("div");
        itemInfo.id = "product-overview";
        itemInfo.classList.add(...classes.split(" "));
        itemInfo.innerHTML = `
            <div id="product-details" class="flex flex-col gap-1">
              <h3 id="item-name" class="text-lg md:text-3xl">${item.name}</h3>
              <p id="item-description" class="text-sm md:text-lg text-gray-500 w-[80%]">${item.description}</p>
            </div>

            <div
              id="qty-details"
              class="flex gap-7 px-1 justify-center items-start"
            >
              <!-- Quantity Dropdown -->
              <select
              id="qty-dropdown"
                class="block w-13 h-10 px-2 py-1 border border-gray-300 rounded-md text-sm md:text-lg hover:cursor-pointer focus:outline-none focus:ring-1 focus:ring-[#006bb8]"
              >
                <option>1</option>
                <option>2</option>
                <option>3</option>
                <option>4</option>
                <option>5</option>
                <option>6</option>
              </select>

              <!-- Remove Button -->
              <button
              id="remove-item-btn"
                class="text-sm md:text-lg text-[#ff66c4] h-10 hover:underline hover:cursor-pointer active:opacity-70"
              >
                Remove
              </button>
            </div>

            <div id="price-details" class="h-10 flex justify-end items-start">
              <span id="price-item" class="text-lg md:text-2xl">€${(item.price * item.quantity).toFixed(2)}</span>
            </div>
`
        itemSummery.appendChild(itemInfo);

        itemInfo.querySelector("#remove-item-btn").addEventListener("click", () => {
          let cart = JSON.parse(localStorage.getItem("cart")) || [];

          cart = cart.filter(food => food.name !== item.name);

          localStorage.setItem("cart", JSON.stringify(cart));

          itemSummery.removeChild(itemInfo);

          addingSubtotal()

        });

        let dropdown = itemInfo.querySelector("#qty-dropdown");
        dropdown.addEventListener("change", () => {
          let priceItem = itemInfo.querySelector("#price-item");
          let amount = dropdown.value;

          priceItem.textContent = "€" + (item.price*amount).toFixed(2);
          addingSubtotal();
        });
         dropdown.value = item.quantity;
    });
}
else{
    itemSummery.textContent = "No items in cart"
}

addingSubtotal();

function addingSubtotal(){
  let prices = document.querySelectorAll(".text-lg.md\\:text-2xl");
  let placeForSubtotal = document.getElementById("subtotal-price");

  let deliveryPrice = document.getElementById("delivery-price").textContent;

  let orderTotal = document.getElementById("total-price");

  let sum = 0;

  Array.from(prices).forEach(el => sum += parseInt(el.textContent.slice(1)));

  orderTotal.textContent = "€" + (sum + parseInt(deliveryPrice.slice(1))).toFixed(2);

  placeForSubtotal.textContent = "€" + sum.toFixed(2);
}

async function checkout(){

  sessionStorage.setItem("subtotal", (document.getElementById("subtotal-price").textContent));
  window.location.href = "./checkout.html";

}