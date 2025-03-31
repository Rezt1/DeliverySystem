import { sessionStorageSet } from "./userWork.mjs";
let registerBtn = document.getElementById("btn-login-main");


registerBtn.addEventListener("click", onRegister);

async function onRegister(e){
    e.preventDefault();
    try{
        let username = document.getElementById("name-input").value;
       let phone = document.getElementById("phone-input").value;
        let email = document.getElementById("e-mail-input").value;
       let password = document.getElementById("password-input").value;

        if(email === '' || password === "" || phone === '' || username === ''){
           throw new Error("All fields must be filled!");
       }

        let response = await register();
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

async function register(email, password, phone, username) {
    
    let user = {
        email,
        password,
        username,
        phone
    }

    let settings = {
        method: 'Post',
        headers : {"Content-Type":"application/json"},
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