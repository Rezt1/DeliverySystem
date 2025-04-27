import { ip } from "./ipSearch.mjs";
import { ifLoggedIn } from "./userWork.mjs";

let ipAddress = ip();

let addressBlock = document.getElementById("delivery-address");

//let apartmentblock = document.getElementById("delivery-address-optional");

let confirmOrder = document.getElementById("confirm-order-button");

const deliveryInput = document.getElementById('delivery-time');

let paymentCard = document.getElementById("card-radio");

let paymentCash = document.getElementById("cash-radio");

paymentCard.addEventListener("click", () => {
    document.getElementById("card-info").classList.remove("hidden");
})

paymentCash.addEventListener("click", () => {
    document.getElementById("card-info").classList.add("hidden");
})

confirmOrder.addEventListener("click", async ()=> {
    try{
 
        console.log(isTimeBetween(deliveryInput.value, deliveryInput.min, deliveryInput.max));
        if(isTimeBetween(deliveryInput.value, deliveryInput.min, deliveryInput.max)){
            if(!addressBlock.value){
                throw new Error("Address must be filled!");
            }
            
            let paymentMethod = "";

            if(paymentCard.checked){
                paymentMethod = "CARD"
            }
            else{
                paymentMethod = "CASH"
            }

            let foods = JSON.parse(localStorage.getItem("cart")) || [];


            let foodsInfo = [];

            Array.from(foods).forEach(el => {
                foodsInfo.push({"foodId": el.id, "quantity": el.quantity})
            });

            let totalPrice = sessionStorage.getItem("subtotal").slice(1);

            try{
            
                await deliverySend(addressBlock.value, paymentMethod, foodsInfo, deliveryInput.value, totalPrice);
                
                localStorage.clear();

                sessionStorage.removeItem("subtotal");

                window.location.href = "./thank_you_for_order.html";


            } catch(e){
                console.error(e.message);
            }

        }
        else{
            throw new Error(`Time must be between ${deliveryInput.min} and ${deliveryInput.max}`);
        }
    }
    catch(e){
        alert(e.message);
    }
})

function setMinDeliveryTime() {
    const now = new Date();
    now.setHours(now.getHours() + 1); 
    now.setSeconds(0);
    now.setMilliseconds(0);

    const hours = now.getHours().toString().padStart(2, '0');
    const minutes = Math.ceil(now.getMinutes() / 5) * 5;
    const mins = (minutes === 60 ? '00' : minutes.toString().padStart(2, '0'));
    const adjustedHours = minutes === 60 ? (now.getHours() + 1).toString().padStart(2, '0') : hours;

    const minTime = `${adjustedHours}:${mins}`;

    deliveryInput.min = minTime > "10:00" ? minTime : "10:00";
    deliveryInput.max = "23:30";
    deliveryInput.value = deliveryInput.min;
}

setMinDeliveryTime();

function isTimeBetween(time, start, end) {
    console.log(time + " " + start + " " + end);
    // Convert all times to minutes since midnight for accurate comparison
    const toMinutes = (timeStr) => {
      const [hours, minutes] = timeStr.split(':').map(Number);
      return hours * 60 + minutes;
    };
  
    const timeMins = toMinutes(time);
    const startMins = toMinutes(start);
    const endMins = toMinutes(end);
  
    if (startMins <= endMins) {
      // Normal case: start and end on same day
      return timeMins >= startMins && timeMins <= endMins;
    } else {
      // Case where time period crosses midnight
      return timeMins >= startMins || timeMins <= endMins;
    }
  }

  async function deliverySend(address, paymentMethod, foods, hourToBeDelivered, totalPrice) {
        
    let token = sessionStorage.getItem("accessToken");

    let delivery = {
        address,
        foods,
        paymentMethod,
        hourToBeDelivered,
        totalPrice
    }


    let settings = {
            method: "Post",
            headers: {"Content-Type":"application/json",
            "Authorization": `Bearer ${token}`

            },
            
            body: JSON.stringify(delivery)
        }


        
    let resp = await fetch(`${ipAddress}/api/deliveries/create-delivery`, settings);
    
    if (resp.status !== 201) {
        let errorData = await resp.json();
        throw new Error(errorData.message);
    }

    return resp;    
}
          
      
  