export function togglePassword(e){
    e.preventDefault();
    console.log(e.target.parentNode);

        let parent = e.target.parentNode;
        let passField = parent.getElementsByTagName("input")[0];
        let passBtn = parent.getElementsByTagName("button")[0];

        if(passField.type === "password"){
            passField.type = "text";
            passBtn.textContent = "Hide";
        }
        else{
            passField.type = "password";
            passBtn.textContent = "Show";
        }
    }
