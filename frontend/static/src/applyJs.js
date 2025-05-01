import { ip } from "./ipSearch.mjs";
import { fetchingCities } from "./fetchingData.mjs";
import { logout } from "./userWork.mjs";

document.getElementById("btn-apply-for-position-main").addEventListener("click", becomeDeliveryGuy);
document.getElementById("city-input").addEventListener("click", showOptionsCities);
document.getElementById("city-input").addEventListener("change", pick);

let citySpace = document.getElementById("city-input");

async function becomeDeliveryGuy(e) {
    e.preventDefault();
    
try{
    let spaceForCity = citySpace.dataset.cityId;

    let ibanSpace = document.getElementById("iban-input").value;

    console.log(spaceForCity);
    console.log(ibanSpace);

    let objCourier = {
        "iban": ibanSpace,
        "cityId": spaceForCity
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
        throw new Error("You cannot become a delivery guy again!");
      }
    
      let result = await resp.text();
      alert(result);
      await logout();
      window.location.href = "./home.html";

}
catch(e){
    alert(e.message);
}
}
async function showOptionsCities(e) {
    try {
        e.preventDefault();
        console.log("hi");

        let target = document.getElementById("city-input");
        let isToggled = target.dataset.customInfo === "istoggled";

        target.dataset.customInfo = isToggled ? "nottoggled" : "istoggled";

        console.log(isToggled);

        if (!isToggled) {
            while (target.options.length > 1) {
                target.remove(1);
            }

            let data = await fetchingCities();

            data.forEach(data => {
                let option = document.createElement("option");
                option.id = data.id;
                option.textContent = data.name;
                target.appendChild(option);
            });
        }
    

    } catch (e) {
        console.error("Failed to load cities:", e);
        document.getElementById("city-input").dataset.customInfo = "nottoggled";
    }
}

function pick(e){
    let targetid = e.target;
    console.log(targetid.value);
    let options = targetid.querySelectorAll("option");
    console.log(options);
    options.forEach(el => {
        if(targetid.value === el.textContent){
            citySpace.dataset.cityId = el.id;
        }
    });
    console.log(document.getElementById("city-input").dataset.cityId);
    
}