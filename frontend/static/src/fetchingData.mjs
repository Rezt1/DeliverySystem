import { ip } from "./ipSearch.mjs";

export async function fetchingCities(){
    try{

        let address = ip();
        let token = sessionStorage.getItem("accessToken");
        let resp = await fetch(`${address}/api/cities`, {
      method: "Get",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }});

      if (!resp.ok) {
        let errorData = await resp.json();
        throw new Error(errorData.message || 'Failed to fetch restaurants');
      }

      let data = await resp.json();
      console.log(data);
      return data;

    }
    catch(e){
        console.error(e);
    }
}

export async function fetchingFood(id){
  let token = sessionStorage.getItem("accessToken");
  try{
  let resp = await fetch(`${ip()}/api/foods/restaurant/${id}`, {
    method: "Get",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  let data = await resp.json();
  return data;
  }
  catch(e){
    console.log(e.message);
  }

}

export async function fettchUser() {
  let token = sessionStorage.getItem("accessToken");
  let resp = await fetch(`${ip()}/api/users/me`, {
    method: "Get",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  let data = await resp.json();
  return data;
}

export async function fetchDelivery(id) {
  let token = sessionStorage.getItem("accessToken");
  let resp = await fetch(`${ip()}/api/deliveries/${id}`, {
    method: "Get",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  let data = await resp.json();
  return data;
}

export async function fetchCuisine() {

  let token = sessionStorage.getItem("accessToken");
  let resp = await fetch(`${ip()}/api/cuisines`, {
    method: "Get",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  let data = await resp.json();
  return data;
  
}