import { ip } from "./ipSearch.mjs";
import { hideEverything } from "./adminJs.mjs";

let menuPaymentsBtn = document.getElementById("btn-payments");
let paymentsMain = document.getElementById("payments-main");

let makePaymentBtn = document.getElementById("btn-make-payment");

let deliveryGuyInput = document.getElementById("payment-recipient");
let fromDateInput = document.getElementById("from-date");
let toDateInput = document.getElementById("to-date");

menuPaymentsBtn.addEventListener("click", onMenuPaymentsClick);
makePaymentBtn.addEventListener("click", makePayment);

async function onMenuPaymentsClick(e) {
    e.preventDefault();

    hideEverything();

    let deliveryGuysDropdown = document.getElementById("payment-recipient").querySelector("optgroup");

    deliveryGuysDropdown.innerHTML = "";

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
        }

        deliveryGuysDropdown.appendChild(option);
    });

    fromDateInput.value = "";
    toDateInput.value = "";

    paymentsMain.classList.remove("hidden");

    localStorage.removeItem("delivery-guy-id");
}

async function makePayment(e) {
    try {
        e.preventDefault();
        
        let id = deliveryGuyInput.value;
        let salaryStartDate = fromDateInput.value;
        let salaryEndDate = toDateInput.value;

        if (id === "" || salaryStartDate === "" || salaryEndDate === "") {
            throw new Error("All fields must be filled");
        }

        let address = ip() + "/api/admin/pay-delivery-guy/" + id;
        let token = sessionStorage.getItem("accessToken");

        let paymentObj = {
            salaryStartDate,
            salaryEndDate
        };        

        let response = await fetch(address, {
            method: "Post",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }, 
            body: JSON.stringify(paymentObj)
        })

        if (response.status !== 201) {
            
            let jsonRespone = await response.json();

            let errorMsg = jsonRespone.errors 
                ? Object.values(jsonRespone.errors).flat().join('\n')
                : jsonRespone.message || 'Creation failed';

            throw new Error(errorMsg);
        }

        window.alert("Successfully paid delivery guy")
    
        fromDateInput.value = "";
        toDateInput.value = "";
        deliveryGuyInput.selectedIndex = 0;
        
    } catch (error) {
        window.alert(error.message);
    }
}