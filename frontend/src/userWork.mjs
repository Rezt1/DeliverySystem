export function sessionStorageSet(response) {
    Object.keys(response).forEach(key => {
        if (key !== "password") { 
            sessionStorage.setItem(key, response[key]);
        }
    });
}

export function ifLoggedIn(){
    /*if(sessionStorage.length != 0){
        return true;
    }
    return false;*/
    return true;
}