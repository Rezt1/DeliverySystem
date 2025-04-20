import { ip } from "./ipSearch.mjs";
import { hideEverything } from "./adminJs.mjs";

let menuDashboardBtn = document.getElementById("btn-dashboard");
let dashboardMain = document.getElementById("dashboard-main");

menuDashboardBtn.addEventListener("click", onDashboardClick);
menuDashboardBtn.click();

async function onDashboardClick(e) {
    e.preventDefault();

    hideEverything();

    let clientsCountEl = document.getElementById("client-qty");
    let restaurantsCountEl = document.getElementById("restaurants-qty");
    let deliveryGuysCountEl = document.getElementById("delivery-guys-qty");

    let address = ip() + "/api/admin/get-statistics";
    let token = sessionStorage.getItem("accessToken");

    let response = await fetch(address, {
        method: "Get",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })

    let respData = await response.json();

    clientsCountEl.textContent = respData.userCount;
    restaurantsCountEl.textContent = respData.restaurantsCount;
    deliveryGuysCountEl.textContent = respData.deliveryGuysCount;

    dashboardMain.classList.remove("hidden");
}