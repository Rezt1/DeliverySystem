import { ifAdmin, ifDeliveryGuy, ifLoggedIn } from "./userWork.mjs";
   
  export function navigationStuff(){
    let loginBtn = document.getElementById("mobile-login-button");
    let registerBtn = document.getElementById("mobile-register-button");
    let acctountBtn = document.getElementById("mobile-account-button");
    let homeBtn = document.getElementsByClassName("btn-primary w-full text-white text-lg py-2")[0];
    document.getElementsByClassName("flex items-center shrink-0")[0].addEventListener('click', loadHome);
    let restBtn = document.getElementById("restaurants-button");
    let cartBtn = document.getElementsByClassName("btn-cart bg-white rounded-full w-11 h-11 flex items-center justify-center shadow-md relative")[0];
    document.getElementById("faqBtn").addEventListener("click", () => {
      window.location.href = "./faq.html"
    });


    loginBtn.addEventListener('click', loadLogin);
    registerBtn.addEventListener("click", loadRegister);
    acctountBtn.addEventListener("click", loadAccount);
    homeBtn.addEventListener("click", loadHome);
    restBtn.addEventListener("click", loadResturants);
    cartBtn.addEventListener("click", loadCart);

    let loginBtnDe = document.getElementById("login-button");
    let registerBtnDe = document.getElementById("register-button");
    let acctountBtnDe = document.getElementById("account-button");
    let homeBtnDe = document.getElementsByClassName("btn-primary")[0];

    document.getElementById("aboutBtn").addEventListener("click", () => {
      window.location.href ="./about_us.html";
    })

    document.getElementById("feedbackBtn").addEventListener("click", () => {
      window.location.href ="./feedback.html";
    })

    if(ifLoggedIn()){
      console.log("logged");
      acctountBtn.classList.remove("hidden");
      acctountBtnDe.classList.remove("hidden");
      loginBtn.classList.add("hidden");
      loginBtnDe.classList.add("hidden");
      registerBtn.classList.add("hidden");
      registerBtnDe.classList.add("hidden");
    }
    else{
      console.log("not logged");
      acctountBtn.classList.add("hidden");
      acctountBtnDe.classList.add("hidden");
      loginBtn.classList.remove("hidden");
      loginBtnDe.classList.remove("hidden");
      registerBtn.classList.remove("hidden");
      registerBtnDe.classList.remove("hidden");
    }

    if(ifDeliveryGuy()){
      let btndelivery = document.getElementById("deliveries-button");
      console.log("deliveryGuy")
      btndelivery.classList.remove("hidden");
    btndelivery.addEventListener("click", () => {
    window.location.href = "./deliveries.html";
    });
    }
  
    else{
      console.log('not delivery guy')
    }

    loginBtnDe.addEventListener('click', loadLogin);
    registerBtnDe.addEventListener("click", loadRegister);
    acctountBtnDe.addEventListener("click", loadAccount);
    homeBtnDe.addEventListener("click", loadHome);

function loadLogin(){
    window.location.href = "./login.html";
  };

  function loadAccount(){
    window.location.href = "./account.html";
  }

  function loadRegister(){
    window.location.href = "./register.html";
  }

  function loadHome(){
    window.location.href = "./home.html";
  }

  function loadResturants(){
    window.location.href = "./restaurants.html";
  }

  function loadCart(){
    window.location.href = "./cart.html";
  }


  }