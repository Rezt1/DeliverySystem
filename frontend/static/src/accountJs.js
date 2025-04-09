import { logout } from "./userWork.mjs";
import { ifDeliveryGuy } from "./userWork.mjs";

let btnLogout = document.getElementById("logout-btn");

btnLogout.addEventListener("click", logout);

let nameField = document.getElementById("name-field");
nameField.textContent = sessionStorage.getItem("username");

let emailField = document.getElementById("email-field");
emailField.textContent = sessionStorage.getItem("email");

let phoneField = document.getElementById("phone-field");
phoneField.textContent = sessionStorage.getItem("phoneNumber");

let btndelivery = document.getElementById("deliveries-button");
btndelivery.addEventListener("click", () => {
    window.location.href = "./my_delivery.html";
} )

if(ifDeliveryGuy){
    console.log("delivery");
    btndelivery.classList.remove("hidden");
    sessionStorage.setItem("delivery-id", 1);
}
