//MENU
let dashboardMain = document.getElementById("dashboard-main");
let citiesMain = document.getElementById("cities-main");
let restaurantsMain = document.getElementById("restaurants-main");
let deliveryGuysMain = document.getElementById("delivery-guys-main");
let paymentsMain = document.getElementById("payments-main");
let reportsMain = document.getElementById("reports-main");



export function hideEverything() {
    dashboardMain.classList.add("hidden");
    citiesMain.classList.add("hidden");
    restaurantsMain.classList.add("hidden");
    deliveryGuysMain.classList.add("hidden");
    paymentsMain.classList.add("hidden");
    reportsMain.classList.add("hidden");
}