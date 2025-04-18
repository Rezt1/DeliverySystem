let addressBlock = document.getElementById("delivery-address");

let apartmentblock = document.getElementById("delivery-address-optional");

let confirmOrder = document.getElementById("confirm-order-button");

const deliveryInput = document.getElementById('delivery-time');

confirmOrder.addEventListener("click", ()=> {
    console.log(isTimeBetween(deliveryInput.value, deliveryInput.min, deliveryInput.max));
    if(isTimeBetween(deliveryInput.value, deliveryInput.min, deliveryInput.max)){
        console.log("valid");
    }
    else{
        alert(`Time must be between ${deliveryInput.min} and ${deliveryInput.max}`);
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