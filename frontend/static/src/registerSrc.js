import { sessionStorageSet } from "./userWork.mjs";
import { togglePassword } from "./utils.mjs";
import { ip } from "./ipSearch.mjs";

let registerBtn = document.getElementById("btn-login-main");
registerBtn.addEventListener("click", onRegister);
let btnToShow = document.getElementById("password-show");
btnToShow.addEventListener("click", togglePassword);
let btnToShow2 = document.getElementById("password-show-2");
btnToShow2.addEventListener("click", togglePassword);

async function onRegister(e){
    e.preventDefault();
    try{
        let username = document.getElementById("name-input").value;
       let phone = document.getElementById("phone-input").value;
        let email = document.getElementById("e-mail-input").value;
       let password = document.getElementById("password-input").value;
       let passwordRepeat = document.getElementById("repeat-password-input").value;

        if(!email || !password || !phone || !username || !passwordRepeat){
           throw new Error("All fields must be filled!");
       }

        let response = await register(email, password, phone, username, passwordRepeat);
        let resp = await response.json();

        if (!response.ok) {
            let errorMsg = resp.errors 
                ? Object.values(resp.errors).join('\n')
                : resp.message || 'Registration failed';
            throw new Error(errorMsg);
        }

        sessionStorageSet(resp);
        window.location.href = "./home.html";

    }
    catch(e){
        window.alert(e.message);
    }
}

async function register(email, password, phoneNumber, name, repeatPassword) {
    
    let user = {
        email,
        password,
        name,
        phoneNumber,
        repeatPassword,
        locationId: 1
    }

    let settings = {
        method: 'Post',
        headers : {"Content-Type":"application/json"},
        body: JSON.stringify(user)
    }

    try{

        let address = ip();

        let resp = await fetch(`${address}/api/auth/register`, settings);
        return resp;

    }
    catch(e){
        console.log(e.message);
    }
}