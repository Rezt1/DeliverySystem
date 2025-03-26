   
  export function navigationStuff(){
    let loginBtn = document.getElementById("mobile-login-button");
    let registerBtn = document.getElementById("mobile-register-button");
    let acctountBtn = document.getElementById("mobile-account-button");
    let homeBtn = document.getElementsByClassName("btn-primary w-full text-white text-lg py-2")[0];
    document.getElementsByClassName("flex items-center shrink-0")[0].addEventListener('click', loadHome);
    
    loginBtn.addEventListener('click', loadLogin);
    registerBtn.addEventListener("click", loadRegister);
    acctountBtn.addEventListener("click", loadAccount);
    homeBtn.addEventListener("click", loadHome);


    let loginBtnDe = document.getElementById("login-button");
    let registerBtnDe = document.getElementById("register-button");
    let acctountBtnDe = document.getElementById("account-button");
    let homeBtnDe = document.getElementsByClassName("btn-primary")[0];
    
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

    }

