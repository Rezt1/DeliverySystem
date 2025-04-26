import { ip } from "./ipSearch.mjs";
import { hideEverything } from "./adminJs.mjs";

let menuDeliveryGuysBtn = document.getElementById("btn-delivery-guys");
let deliveryGuysMain = document.getElementById("delivery-guys-main");

let removeDeliveryGuyBtn = document.getElementById("remove-delivery-guy-btn");

let idEl = document.getElementById("delivery-guy-id");
let phoneEl = document.getElementById("delivery-guy-phone");
let ibanEl = document.getElementById("delivery-guy-iban");
let cityEl = document.getElementById("delivery-guy-city");
let deliveryGuyNameEl = document.getElementById("form-delivery-guy-name");

menuDeliveryGuysBtn.addEventListener("click", onMenuDeliveryGuysClick);
removeDeliveryGuyBtn.addEventListener("click", removeDeliveryGuy);

async function onMenuDeliveryGuysClick(e) {
    e.preventDefault();

    hideEverything();

    let loadingDeliveryGuys = document.getElementById("delivery-guys-loading-admin");
    let deliveryGuysList = document.getElementById("delivery-guys-list-admin");

    deliveryGuysList.classList.add("hidden");
    deliveryGuysList.innerHTML = "";
    loadingDeliveryGuys.classList.remove("hidden");

    removeInfoToTheRight();

    deliveryGuysMain.classList.remove("hidden");

    let address = ip() + "/api/admin/get-all-delivery-guys";
    let token = sessionStorage.getItem("accessToken");

    let deliveryGuys = await fetch(address, {
        method: "Get",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }, 
    });

    let deliveryGuysData = await deliveryGuys.json();

    deliveryGuysData.forEach(dg => {
        let btnEl = document.createElement("button");
        btnEl.className = "hover:cursor-pointer hover:text-[#ff66c4] active:opacity-70 w-full max-lg:h-14 lg:h-16 md:h-18 border rounded-lg my-auto font-bold text-xl md:text-2xl lg:text-3xl flex justify-between items-center py-2 px-4 overflow-x-hidden";
        btnEl.id = dg.userId;
        btnEl.setAttribute("data-phone", dg.deliveryGuyPhoneNumber);
        btnEl.setAttribute("data-iban", dg.iban);
        btnEl.setAttribute("data-city", dg.workCity);
        btnEl.setAttribute("data-name", dg.deliveryGuyName);

        btnEl.addEventListener("click", showInfoToTheRight);

        let spanEl = document.createElement("span");
        spanEl.className = "truncate";
        spanEl.textContent = dg.deliveryGuyName;

        btnEl.appendChild(spanEl);

        deliveryGuysList.appendChild(btnEl);
    });

    loadingDeliveryGuys.classList.add("hidden");
    deliveryGuysList.classList.remove("hidden");
}

function showInfoToTheRight(e) {
    e.preventDefault();

    let btn = e.currentTarget;

    deliveryGuyNameEl.textContent = btn.dataset.name;
    idEl.textContent = btn.id;
    phoneEl.textContent = btn.dataset.phone;
    ibanEl.textContent = btn.dataset.iban;
    cityEl.textContent = btn.dataset.city;
}

async function removeDeliveryGuy(e) {
    try {
        e.preventDefault();

        let id = idEl.textContent;

        if (id === "") {
            throw new Error("Select a delivery guy");
        }

        if (Number.isNaN(Number.parseInt(id))) {
            throw new Error("Invalid id");
        }

        let address = ip() + "/api/admin/fire-delivery-guy/" + id;
        let token = sessionStorage.getItem("accessToken");

        let response = await fetch(address, {
            method: "Delete",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })

        if (!response.ok) {
            let errorData = await response.json();
            throw new Error(errorData.message || "failed to remove city");
        }

        menuDeliveryGuysBtn.click();
    } catch (error) {
        console.log(error);
        window.alert(error);
    }
}

function removeInfoToTheRight() {
    deliveryGuyNameEl.textContent = "Delivery guy name";
    idEl.textContent = "";
    phoneEl.textContent = "";
    ibanEl.textContent = "";
    cityEl.textContent = "";
}