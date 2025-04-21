import { fetchActiveDel } from "./fetchingData.mjs";
import { ip } from "./ipSearch.mjs";

let orderSum = document.getElementById("order-summary-list");
let loading = document.getElementById("order-summary-loading");
let subtotal = document.getElementById("total-price");

async function renderMyDel() {
    try{

        let data = await fetchActiveDel();

        console.log(data);

        orderSum.classList.remove("hidden");

        loading.classList.add("hidden");

        subtotal.textContent = "€" + data.totalPrice;

        if(!data || Object.keys(data).length === 0){
            console.log("empy")
            orderSum.classList.add("hidden");
            loading.classList.remove("hidden");
            return;
        }

        let nameBlock = document.getElementById("name");
        let addressBlock = document.getElementById("address");
        let phoneBlock = document.getElementById("phone");

        let orderId = document.getElementById("order-id");
        let status = document.getElementById("status");

        data.foods.forEach(food => {

            let foodItem = document.createElement("div");
            let classes = "flex-1 flex gap-1 max-md:items-start items-center justify-between p-2 md:p-4 bg-gray-200 rounded-lg";
            foodItem.classList.add(...classes.split(" "));
            foodItem.innerHTML = ` <h3 id="order-item" class="font-bold max-md:text-xl text-2xl">
              ${food.foodName}
            </h3>
            <div class="flex items-center justify-center gap-2">
              <label
                for="qty-items"
                class="text-md md:text-lg md:text-base text-[#ff66c4]"
                >Quantity: ${food.quantity}
              </label>`

              orderSum.appendChild(foodItem);
        });

        nameBlock.textContent = data.username;
        addressBlock.textContent = data.address;
        phoneBlock.textContent = data.userPhoneNumber;
        orderId.textContent = data.deliveryId;
        status.textContent = data.status;

        let btndelivered = document.getElementById("btn-delivered-main");

        btndelivered.addEventListener("click", async () => {
            await delivered(data);
            window.location.href = "./my_delivery.html";
        });
   
        } 

    catch(e){
        orderSum.classList.add("hidden");
        loading.classList.remove("hidden");
        let totalDiv = document.getElementById("total-div");
        totalDiv.classList.add("hidden");
        
        console.log(e.message);
    }
}

renderMyDel();

async function delivered(data) {

let addressIp = `${ip()}/api/delivery-guys/deliveries/${data.deliveryId}/delivered`;
let token = sessionStorage.getItem("accessToken");

try {
    let resp = await fetch(addressIp, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!resp.ok) {
        throw new Error(`HTTP error! status: ${resp.status}`);
    }

    let data = await resp.json();
    return data;
} catch (e) {
    console.error("Delivery marking failed:", e.message);
}
}