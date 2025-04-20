import { logout } from "./userWork.mjs";
import { ifDeliveryGuy } from "./userWork.mjs";

let btnLogout = document.getElementById("logout-btn");

btnLogout.addEventListener("click", logout);

let nameField = document.getElementById("name-field");
nameField.textContent = sessionStorage.getItem("username");

let userName = document.getElementById("user-name");
userName.textContent = sessionStorage.getItem("username");

let emailField = document.getElementById("email-field");
emailField.textContent = sessionStorage.getItem("email");

let phoneField = document.getElementById("phone-field");
phoneField.textContent = sessionStorage.getItem("phoneNumber");

let becomeDel = document.getElementById("become-delivery-guy-btn");

becomeDel.addEventListener("click", () => {
    window.location.href = "./apply_as_courier.html";
})

let quitDel = document.getElementById("quit-delivery-guy-btn");



if(ifDeliveryGuy()){
    console.log("delivery");
    becomeDel.classList.add("hidden");
    quitDel.classList.remove("hidden");
}
else{
    becomeDel.classList.remove("hidden");
    quitDel.classList.add("hidden");
}

