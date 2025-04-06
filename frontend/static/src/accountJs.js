import { logout } from "./userWork.mjs";

let btnLogout = document.getElementById("logout-btn");

btnLogout.addEventListener("click", logout);

let nameField = document.getElementById("name-field");
nameField.textContent = sessionStorage.getItem("username");

let emailField = document.getElementById("email-field");
emailField.textContent = sessionStorage.getItem("email");

let phoneField = document.getElementById("phone-field");
phoneField.textContent = sessionStorage.getItem("phoneNumber");