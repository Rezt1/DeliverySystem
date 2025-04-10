import { ip } from "./ipSearch.mjs";


export function sessionStorageSet(response) {
    Object.keys(response).forEach(key => {
        if (key !== "password") { 
            sessionStorage.setItem(key, response[key]);
        }
    });
}

export function sessionStorageRemove(response){
    Object.keys(response).forEach(key => {
        if (key !== "password") { 
            sessionStorage.removeItem(key, response[key]);
        }
    });
}

export function ifLoggedIn(){
    if(sessionStorage.length != 0){
        return true;
    }
    return false;
}


export async function logout(e){
    e.preventDefault();
    let settings = {
        method: "Post",
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("accessToken")}`
          },
    }
    let address = ip();
    let response = await fetch(`${address}/api/auth/logout`, settings);


    if (response.status === 401) {
        sessionStorage.clear();
        window.location.reload();
        return;
    
    }

    if (!response.ok) throw new Error('Logout failed');

    sessionStorage.clear();
    window.location.href = "./home.html";

}

export function ifDeliveryGuy(){
    let email = sessionStorage.getItem("email");
    if(/deliveryGuy[a-zA-Z0-9._%+-]*@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)){
        return true;
    }
    else{
        return false;
    }
}