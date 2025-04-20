import { fetchPendingDel } from "./fetchingData.mjs"
import { ip } from "./ipSearch.mjs";

document.getElementById("my-delivery-button").addEventListener("click", () => {
    window.location.href = "./my_delivery.html";
})

let loading = document.getElementById("orders-loading");

let ordersList = document.getElementById("list-of-orders");



async function  renderDeliveries() {

try{
    
let pendingDeliveries = await fetchPendingDel();

//let pendingDeliveries = [];
if(pendingDeliveries.length == 0) {
    console.log("hidden");
    loading.classList.remove("hidden");
    return;
}

pendingDeliveries.forEach(element => {
    console.log(element);
    let items = 0;
    element.foods.forEach(el => {
        items += el.quantity;
    })
    let div = document.createElement("div");
    let classes = "flex items-center justify-between gap-4 p-2 text-2xl md:text-3xl";
    div.classList.add(...classes.split(" "));
    div.id = `order-${element.id}`
    div.innerHTML = `<div
      id="orders-list"
      class="flex-1 flex items-center justify-between p-4 bg-gray-200 rounded-lg"
    >
      <div>
        <h3 id="order-number" class="font-bold">Order #${element.deliveryId}</h3>
        <span
          id="qty-and-price"
          class="text-xs md:text-sm text-gray-600"
          >${items} items • $24.50</span
        >
        <!-- 2 items • $24.50 -->
      </div>
      <span id="time-ago" class="text-xs md:text-sm text-gray-500"
        >${timeAgo(element.creationDate)}</span
      >
      <!-- 10 min ago -->
    </div>

    <button
      id="accept-order-button"
      class="w-[90px] h-full flex-shrink-0 bg-[#006bb8] text-white rounded-lg font-bold flex justify-center items-center hover:cursor-pointer hover:bg-white hover:text-[#ff66c4] hover:border-2 hover:border-[#ff66c4] active:opacity-70 transition-all"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-12 w-12"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M5 13l4 4L19 7"
        />
      </svg>
    </button>`

    ordersList.appendChild(div);
    div.querySelector("#accept-order-button").addEventListener("click", async () => {
        let address = `${ip()}/api/delivery-guys/deliveries/${element.deliveryId}/assign`
        let token = sessionStorage.getItem("accessToken");

        let resp = await fetch(address, {
            method: "PUT",
            headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
            }
        })

        let data = await resp.json();
        console.log(data);
    })
});


}
catch(e){
    console.log(e.message);
}
}

renderDeliveries();

function timeAgo(timestamp) {
    const now = new Date();
    const past = new Date(timestamp);
    const diffMs = now - past;
  
    const seconds = Math.floor(diffMs / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours   = Math.floor(minutes / 60);
    const days    = Math.floor(hours / 24);
  
    if (days > 0) return `${days} day(s) ago`;
    if (hours > 0) return `${hours} hour(s) ago`;
    if (minutes > 0) return `${minutes} minute(s) ago`;
    return `${seconds} second(s) ago`;
  }