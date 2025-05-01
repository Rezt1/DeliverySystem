import { logout } from "./userWork.mjs";
import { ifDeliveryGuy } from "./userWork.mjs";
import { ip } from "./ipSearch.mjs";

let ipAddress = ip();
let btnLogout = document.getElementById("logout-btn");

btnLogout.addEventListener("click", logout);

let token = sessionStorage.getItem("accessToken");

document.getElementById("checkDeliveries-btn").addEventListener("click", () => {
  window.location.href = "./delivery_status.html"
})

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
    

await logout();

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

document.addEventListener("DOMContentLoaded", function () {

    const editBtn = document.getElementById("edit-btn");
    const saveBtn = document.getElementById("save-btn");
    const cancelBtn = document.getElementById("cancel-btn");
    const userNameElement = document.getElementById("user-name");


    editBtn.addEventListener("click", () => {
      nameField.setAttribute("contenteditable", "true");
      nameField.classList.add("ring-2", "ring-[#006bb8]");
      nameField.focus(); 

      emailField.setAttribute("contenteditable", "true");
      emailField.classList.add("ring-2", "ring-[#006bb8]");
      emailField.focus(); 

      phoneField.setAttribute("contenteditable", "true");
      phoneField.classList.add("ring-2", "ring-[#006bb8]");
      phoneField.focus(); 

      editBtn.classList.add("hidden");
      saveBtn.classList.remove("hidden");
      cancelBtn.classList.remove("hidden");
    });

    saveBtn.addEventListener("click", async () => {
      let newName = nameField.textContent.trim();
      let newPhone = phoneField.textContent.trim();
      let newEmail = emailField.textContent.trim();

      if (!newName || !newPhone || !newEmail) {
        alert("Name cannot be empty!");
        return;
      }

      let newUser = {
        name: newName,
        phoneNumber: newPhone,
        email: newEmail
      }

      try {
        const response = await fetch(`${ip()}/api/users/update-account`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(newUser),
        });
    
        if (!response.ok) throw new Error('Failed to update developer info.');
    
        const data = await response.json();
        console.log(data);
        alert("User updated successfully!");
        userNameElement.textContent = newName;
        nameField.setAttribute("contenteditable", "false");
        nameField.classList.remove("ring-2", "ring-[#006bb8]");

        sessionStorage.setItem("username", newName);
        sessionStorage.setItem("email", newEmail);
        sessionStorage.setItem("phoneNumber", newPhone);
  
        editBtn.classList.remove("hidden");
        saveBtn.classList.add("hidden");
        cancelBtn.classList.add("hidden");
      } catch (error) {
        console.error('Error updating user:', error);
      }
    

    });

    cancelBtn.addEventListener("click", () => {
      nameField.setAttribute("contenteditable", "false");
      nameField.classList.remove("ring-2", "ring-[#006bb8]");

      emailField.setAttribute("contenteditable", "false");
      emailField.classList.remove("ring-2", "ring-[#006bb8]");

      phoneField.setAttribute("contenteditable", "false");
      phoneField.classList.remove("ring-2", "ring-[#006bb8]");

      editBtn.classList.remove("hidden");
      saveBtn.classList.add("hidden");
      cancelBtn.classList.add("hidden");
    });

    nameField.addEventListener("keydown", function (e) {
      if (e.key === "Enter") {
        e.preventDefault();
      }
    });

  });