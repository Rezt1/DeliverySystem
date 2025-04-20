import { ip } from "./ipSearch.mjs";

document.getElementById("btn-apply-for-position-main").addEventListener("click", becomeDeliveryGuy);

async function becomeDeliveryGuy() {
    
try{
    let citySpace = document.getElementById("city-input").value;

    let ibanSpace = document.getElementById("iban-input").value;

    let objCourier = {
        "iban": ibanSpace,
        "cityId": citySpace
    }

    let token = sessionStorage.getItem("accessToken");

    let resp = await fetch(`${ip()}/api/users/apply-delivery-guy`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify(objCourier)
    });

    if (!resp.ok) {
        const errorText = await resp.text();
        throw new Error(`Failed to apply: ${errorText}`);
      }
    
      const result = await resp.text();
      alert(result);
      window.location.href = "/home.html";
}
catch(e){
    alert(e.message);
}
}