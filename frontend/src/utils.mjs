export function togglePassword(e){
    e.preventDefault();
        let passField = document.getElementById("password-input");
        let passBtn = document.getElementById("password-show");

        if(passField.type === "password"){
            passField.type = "text";
            passBtn.textContent = "Hide";
        }
        else{
            passField.type = "password";
            passBtn.textContent = "Show";
        }
    }