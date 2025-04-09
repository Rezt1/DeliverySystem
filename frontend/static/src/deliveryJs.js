import { fetchDelivery } from "./fetchingData.mjs";
import { ip } from "./ipSearch.mjs";

let data = await fetchDelivery(sessionStorage.getItem("delivery-id"));
console.log(data);

let orderSum = document.getElementById("order-summary-list");
orderSum.classList.remove("hidden");

let loading = document.getElementById("order-summary-loading");
loading.classList.add("hidden");

let nameBlock = document.getElementById("name");
let addressBlock = document.getElementById("address");
let phoneBlock = document.getElementById("phone");

let orderId = document.getElementById("order-id");
let status = document.getElementById("status");

data.foods.forEach(food => {
    console.log(food);
    let div = document.createElement("div");
    div.textContent = food.foodName + " x " + food.quantity;
    orderSum.appendChild(div);
});

nameBlock.textContent = data.username;
addressBlock.textContent = data.address;
phoneBlock.textContent = data.userPhoneNumber;
orderId.textContent = data.deliveryId;
status.textContent = data.status;

let btndelivered = document.getElementById("btn-delivered-main");

btndelivered.addEventListener("click", delivered);

async function delivered() {

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