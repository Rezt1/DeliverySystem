import { logout } from "./userWork.mjs";
import { ifDeliveryGuy } from "./userWork.mjs";
import { ip } from "./ipSearch.mjs";

let ipAddress = ip();
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
quitDel.addEventListener("click", async () => {
    let token = sessionStorage.getItem("accessToken");
    try{
    let address = `${ipAddress}/api/delivery-guys/quit`;
   
    let resp = fetch(address, {

        method: "PUT",
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
    });

    
    if (!resp.ok) {
        let errorData = await resp.json();
        throw new Error(errorData.message);
      }
  
}
catch(e){
    console.error(e.message);
}
    

logout();

//window.location.href = "/home.html";
    
});



if(ifDeliveryGuy()){
    console.log("delivery");
    becomeDel.classList.add("hidden");
    quitDel.classList.remove("hidden");
}
else{
    becomeDel.classList.remove("hidden");
    quitDel.classList.add("hidden");
}

