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

console.log(pendingDeliveries);

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
    div.innerHTML =  `
    <div
      id="orders-list"
      class="flex-1 flex max-md:flex-col gap-1 max-md:items-start items-center justify-between p-2 md:p-4 bg-gray-200 rounded-lg"
    >
      <div class="flex flex-col gap-1">
        <h3 id="order-number" class="font-bold max-md:text-xl">
          Order #${element.deliveryId}
        </h3>
        <p
          id="order-address"
          class="text-xs md:text-sm text-gray-600"
        >
        ${element.address}
        </p>
        <span
          id="qty-and-price"
          class="text-xs md:text-sm text-gray-600"
        >
          ${items} items • €${element.totalPrice}
        </span>
        <span
          id="delivery-time"
          class="text-xs md:text-sm text-[#ff66c4] font-semibold"
        >
          Delivery Time: ${ifPmOrAM(element.toBeDeliveredTime)}
        </span>
      </div>
      <span id="time-ago" class="text-xs md:text-sm text-gray-500"
        >${timeAgo(element.creationDate)}</span
      >
      <!-- 10 min ago -->
    </div>

    <!-- Accept button - Sends to my_delivery -->
    <button
      id="accept-order-button"
      class="max-w-[60px] max-h-[60px] flex-shrink-0 bg-[#006bb8] text-white rounded-lg font-bold flex justify-center items-center hover:cursor-pointer hover:bg-white hover:text-[#ff66c4] hover:border-2 hover:border-[#ff66c4] active:opacity-70 transition-all"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-10 w-10 md:h-12 md:w-12 lg:h-14 lg:w-14"
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

        if (!resp.ok) {
          let errorData = await resp.json();
          throw new Error(errorData.message);
        }
    
        let data = await resp.json();
        console.log(data);
        ordersList.removeChild(div);
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

  function ifPmOrAM(time){
    if(parseInt(time.slice(0,2)) >= 13){
      return `${time} PM`;
    }
    else{
      return `${time} AM`;
    }
  }