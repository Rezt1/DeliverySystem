import { sessionStorageSet } from "./userWork.mjs";
import { togglePassword } from "./utils.mjs";


let loginBtnMob = document.getElementById("btn-login-main");
let btnToShow = document.getElementById("password-show");


btnToShow.addEventListener("click", togglePassword);
loginBtnMob.addEventListener("click", onLogin);


async function onLogin(e){
    e.preventDefault();
    try{
        let email = document.getElementById("e-mail-input").value;
       let password = document.getElementById("password-input").value;

        if(email === '' || password === ""){
            throw new Error("All fields must be filled!");
        }

       /* let response = await login();
        let resp = await response.json();

        if(response.status !== 200){
            throw new Error("Wrong!");
        }

        sessionStorageSet(resp);
        */
        sessionStorage.setItem(1,"logged");
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

        try{
            let resp = await fetch("http://localhost:5501", settings);
            return resp;
        }
        catch(e){
            console.log(e.message);
        }
    
}

