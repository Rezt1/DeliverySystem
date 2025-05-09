import { ip } from "./ipSearch.mjs";
import { sessionStorageSet } from "./userWork.mjs";
import { togglePassword } from "./utils.mjs";


let loginBtnMob = document.getElementById("btn-login-main");
let btnToShow = document.getElementById("password-show");


btnToShow.addEventListener("click", togglePassword);
loginBtnMob.addEventListener("click", onLogin);

if (document.referrer === `http://localhost:8082/cart.html`) {
    alert("You must be logged in to order!");
}


async function onLogin(e){
    e.preventDefault();
    sessionStorage.clear();
    try{
        let email = document.getElementById("e-mail-input").value;
       let password = document.getElementById("password-input").value;

        if(email === '' || password === ""){
            throw new Error("All fields must be filled!");
        }

       let response = await login(email, password);
        let resp = await response.json();

        if(response.status !== 200){
            throw new Error("Wrong!");
        }

        sessionStorageSet(resp);
        window.location.href = "./home.html";
    }
    catch(e){
        window.alert(e.message);
    }
}

async function login(email, password) {


       let user = {
            email,
            password
        }


        let settings = {
            method: "Post",
            headers: {"Content-Type":"application/json"},
            body: JSON.stringify(user)
        }

        let address = ip();

        try{
            let resp = await fetch(`${address}/api/auth/login`, settings);
            return resp;
        }
        catch(e){
            console.log(e.message);
        }
    
}

