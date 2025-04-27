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
    if(sessionStorage.getItem("username")){
        return true;
    }
    return false;
}


export async function logout(){
    
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

/* export function ifDeliveryGuy(){
    let email = sessionStorage.getItem("email");
    if(/deliveryGuy[a-zA-Z0-9._%+-]*@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)){
        return true;
    }
    else{
        return false;
    }
} */

export function ifDeliveryGuy(){

    let token = sessionStorage.getItem("accessToken");
    if (!token) {
        console.log("No token found");
        return;
    }

    let decodedToken = parseJwt(token);

    let roles = decodedToken.roles;  
    if (roles && roles.includes("ROLE_DELIVERY_GUY")) {
        return true;
    } else {
        return false;
    }

    function parseJwt(token) {
        let base64Url = token.split('.')[1];
        let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        let jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
    
        return JSON.parse(jsonPayload);
    }
    
}

export function ifAdmin(){

    let token = sessionStorage.getItem("accessToken");
    if (!token) {
        console.log("No token found");
        return;
    }

    let decodedToken = parseJwt(token);

    let roles = decodedToken.roles;  
    if (roles && roles.includes("ROLE_ADMIN")) {
        return true;
    } else {
        return false;
    }

    function parseJwt(token) {
        let base64Url = token.split('.')[1];
        let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        let jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
    
        return JSON.parse(jsonPayload);
    }

}