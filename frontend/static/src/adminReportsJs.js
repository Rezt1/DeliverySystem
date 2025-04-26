import { ip } from "./ipSearch.mjs";
import { hideEverything } from "./adminJs.mjs";

let menuReportsBtn = document.getElementById("btn-reports");

let reportsMain = document.getElementById("reports-main");

let totalEarningsSection = document.getElementById("total-earnings");
let deliveryGuyEarningsSection = document.getElementById("delivery-guy-earnings");

let totalEarningsBtn = document.getElementById("total-earnings-btn");
let deliveryGuysEarningsBtn = document.getElementById("delivery-guy-earnings-btn");

let getDeliveryGuyEarningReportBtn = document.getElementById("btn-get-delivery-guy-report");

let deliveryGuyIdEl = document.getElementById("delivery-guy-id-earnings");
let fromDateInput = document.getElementById("from-date-delivery-guy-report");
let toDateInput = document.getElementById("to-date-delivery-guy-report");

menuReportsBtn.addEventListener("click", onMenuReportsClick);
totalEarningsBtn.addEventListener("click", showTotalEarnings);
deliveryGuysEarningsBtn.addEventListener("click", showDeliveryGuyEarnings);
getDeliveryGuyEarningReportBtn.addEventListener("click", getDeliveryGuyReport);

async function onMenuReportsClick(e) {
    e.preventDefault();

    hideEverything();

    let deliveryGuyId = localStorage.getItem("delivery-guy-id");

    if (deliveryGuyId !== null) {
        deliveryGuysEarningsBtn.click();
    } else {
        totalEarningsBtn.click();
    }

    reportsMain.classList.remove("hidden");
}

function showTotalEarnings() {
    deliveryGuyEarningsSection.classList.add("hidden");
    totalEarningsSection.classList.remove("hidden");
}

async function showDeliveryGuyEarnings() {

    let deliveryGuysDropdown = document.getElementById("menu-delivery-guys");
    deliveryGuysDropdown.querySelectorAll("option:not(:first-child)").forEach(o => o.remove());

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
    let deliveryGuyId = localStorage.getItem("delivery-guy-id");

    deliveryGuysData.forEach(dg => {
        let option = document.createElement("option");
        option.value = dg.userId;
        option.textContent = dg.deliveryGuyName;

        if (option.value === deliveryGuyId) {
            option.selected = true;
            deliveryGuyIdEl.textContent = option.value;
            localStorage.removeItem("delivery-guy-id");
        }

        deliveryGuysDropdown.appendChild(option);
    });

    if (deliveryGuyId === null) {
        deliveryGuysDropdown.selectedIndex = 0;
        deliveryGuyIdEl.textContent = "ID";
    }
    
    deliveryGuysDropdown.addEventListener("change", showIdToTheRight);

    fromDateInput.value = "";
    toDateInput.value = "";
    document.querySelector("#delivery-guy-total").textContent = "??? лв."

    totalEarningsSection.classList.add("hidden");
    deliveryGuyEarningsSection.classList.remove("hidden");
}

function showIdToTheRight(e) {
    e.preventDefault();

    deliveryGuyIdEl.textContent = e.target.value;
}

async function getDeliveryGuyReport(e) {
    try {
        e.preventDefault();
        
        let id = deliveryGuyIdEl.textContent;
        let startDate = fromDateInput.value;
        let endDate = toDateInput.value;

        if (id === "ID" || startDate === "" || endDate === "") {
            throw new Error("All fields must be filled");
        }

        let address = ip() + "/api/admin/get-income-by-delivery-guys";
        let token = sessionStorage.getItem("accessToken");

        let reportObj = {
            startDate,
            endDate
        };        

        let response = await fetch(address, {
            method: "Post",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }, 
            body: JSON.stringify(reportObj)
        })

        if (!response.ok) {
            
            let jsonRespone = await response.json();

            let errorMsg = jsonRespone.errors 
                ? Object.values(jsonRespone.errors).flat().join('\n')
                : jsonRespone.message || 'Creation failed';

            throw new Error(errorMsg);
        }
    
        let deliveryGuysData = await response.json();

        deliveryGuysData.forEach(dg => {
            if (dg.deliveryGuyId == id) {
                document.querySelector("#delivery-guy-total").textContent = Number(dg.amount).toFixed(2) + " лв."
            }
        })
        
    } catch (error) {
        window.alert(error.message);
    }
}