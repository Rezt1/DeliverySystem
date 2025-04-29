import { fetchActiveDeliveriesUser, fetchingDelivered } from "./fetchingData.mjs";

    let data = await fetchActiveDeliveriesUser();
    let deliveryContainer = document.getElementById("deliveries-conatiner");

    let data2 = await fetchingDelivered();

    console.log(data);

    async function displayActiveDeliveries (deliveries) {

        if(deliveries.length === 0){
            const noDeliveriesDiv = document.createElement('div');
            noDeliveriesDiv.className = 'text-center p-6 text-gray-500 text-lg';
            noDeliveriesDiv.innerHTML = `
              <p>You haven't made any orders yet or they haven't been picked up!😌</p>
            `;
            deliveryContainer.appendChild(noDeliveriesDiv);
        }

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
        else{

        }
        });
    };

    let newDivForTracking = document.createElement("div");
    let classes = "w-full max-w-3xl bg-white shadow-xl rounded-3xl p-8 space-y-8";
    newDivForTracking.classList.add(...classes.split(" "));
    newDivForTracking.id = "delivered-orders";
    newDivForTracking.innerHTML = ` <div class="relative w-full h-60 bg-gradient-to-br from-[#ff66c4] to-[#006bb8] rounded-2xl flex items-center justify-center">
          <img src=${"./Images/smilingCutomer.png"} alt="Map Tracker" class="w-40 h-40 animate-bounce opacity-80" />
          <!--<span class="absolute bottom-4 right-4 text-white text-sm font-bold bg-black bg-opacity-30 px-3 py-1 rounded-full">Estimated: 18:45</span>-->
        </div>`
    if(data2.length !== 0){
        document.getElementById("main").appendChild(newDivForTracking);
        console.log(data2);
    }
    async function displayDeliveredOrders(deliveries){
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

           <div class="h-1 flex-1 bg-[#ff66c4] mx-2"></div>

            <div class="flex flex-col items-center">
               <div class="w-8 h-8 rounded-full bg-[#ff66c4] flex items-center justify-center text-white font-bold">4</div>
              <p class="text-sm mt-2 text-[#006bb8]">Delivered</p>
            </div>
          </div>`
          newDivForTracking.appendChild(section);
          order.foods.forEach(food => {
            console.log(food);
            let item = document.createElement("li");
            item.textContent = food.foodName + " x " + food.quantity;
            section.querySelector("#listFoods").appendChild(item);
            });
        }
        });
    }
   
  displayActiveDeliveries(data);
  displayDeliveredOrders(data2);

