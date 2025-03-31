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
    if(sessionStorage.length > 1){
        return true;
    }
    return false;
}


export async function logout(e){
    e.preventDefault();
    /*let settings = {
        method: "Get",
        headers: {"X-Authorization": sessionStorage.accessToken}
    }

    let response = await fetch("http", settings);
    sessionStorageRemove(response.json());*/
    
    console.log('logout');
    sessionStorage.removeItem(1, "logged");
    window.location.href = "./home.html";

}