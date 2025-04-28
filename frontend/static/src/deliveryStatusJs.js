import { fetchActiveDeliveriesUser } from "./fetchingData.mjs";

    let data = await fetchActiveDeliveriesUser();
    let deliveryContainer = document.getElementById("deliveries-conatiner");

    console.log(data);

    async function displayActiveDeliveries (deliveries) {
        deliveries.forEach(order => {
            if(order.status != "PENDING"){
            let section = document.createElement("section");
            section.classList.add("space-y-4");
            section.innerHTML = `<h2 class="text-2xl font-bold text-[#006bb8]">Order Details</h2>

          <div class="grid grid-cols-2 gap-4 text-gray-700">
            <div>
              <p class="font-semibold">Order ID:</p>
              <p>#${order.deliveryId}</p>
            </div>
            <div>
              <p class="font-semibold">Status:</p>
              <p class="text-[#ff66c4]">${order.status}</p>
            </div>
            <div>
              <p class="font-semibold">Price:</p>
              <p>€${order.totalPrice.toFixed(2)}</p>
            </div>
            <div>
              <p class="font-semibold">Estimated Delivery:</p>
              <p>Today at ${order.toBeDeliveredTime}</p>
            </div>
          </div>

          <div class="pt-4">
            <p class="font-semibold text-[#006bb8] mb-2">Items:</p>
            <ul id = "listFoods" class="list-disc list-inside">
            </ul>
          </div>
        </section>

        <!-- Courier Info -->
        <section class="flex items-center space-x-4 mt-6">
          <img src="https://i.imgur.com/7k12EPD.png" alt="Courier" class="w-16 h-16 rounded-full border-4 border-[#ff66c4]" />
          <div>
            <p class="font-bold text-lg">${order.deliveryGuyName}</p>
            <p class="text-sm text-gray-500">Your Delivery Hero 🚴‍♂️</p>
          </div>
        </section>

        <!-- Visual Status Tracker -->
        <section class="pt-6">
          <div class="flex items-center justify-between">
            <div class="flex flex-col items-center">
              <div class="w-8 h-8 rounded-full bg-[#ff66c4] flex items-center justify-center text-white font-bold">1</div>
              <p class="text-sm mt-2 text-[#006bb8]">Order Placed</p>
            </div>

            <div class="h-1 flex-1 bg-[#ff66c4] mx-2"></div>

            <div class="flex flex-col items-center">
              <div class="w-8 h-8 rounded-full bg-[#ff66c4] flex items-center justify-center text-white font-bold">2</div>
              <p class="text-sm mt-2 text-[#006bb8]">Cooking</p>
            </div>

            <div class="h-1 flex-1 bg-[#ff66c4] mx-2"></div>

            <div class="flex flex-col items-center">
              <div class="w-8 h-8 rounded-full bg-[#ff66c4] flex items-center justify-center text-white font-bold">3</div>
              <p class="text-sm mt-2 text-[#006bb8]">Out for Delivery</p>
            </div>

            <div class="h-1 flex-1 bg-gray-300 mx-2"></div>

            <div class="flex flex-col items-center">
              <div class="w-8 h-8 rounded-full bg-gray-300 flex items-center justify-center text-white font-bold">4</div>
              <p class="text-sm mt-2 text-gray-400">Delivered</p>
            </div>
          </div>`
          deliveryContainer.appendChild(section);
          order.foods.forEach(food => {
            console.log(food);
            let item = document.createElement("li");
            item.textContent = food.foodName + " x " + food.quantity;
            section.querySelector("#listFoods").appendChild(item);
            });
        }
        });
    };

   
  displayActiveDeliveries(data);

