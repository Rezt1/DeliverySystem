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

  if (!resp.ok) {
    const errorData = await resp.json();
    throw new Error(errorData.message || 'Request failed');
  }

  let data = await resp.json();
  return data;
  }
  catch(e){
    console.error(e.message);
  }

}

export async function fettchUser() {
  try{
  let token = sessionStorage.getItem("accessToken");
  let resp = await fetch(`${ip()}/api/users/me`, {
    method: "Get",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!resp.ok) {
    const errorData = await resp.json();
    throw new Error(errorData.message || 'Request failed');
  }

  let data = await resp.json();
  return data;
}catch(e){
  console.error(e.message);
}
}

export async function fetchDelivery(id) {
  try{
  let token = sessionStorage.getItem("accessToken");
  let address = `${ip()}/api/deliveries`;
  if(id){
    address = `${ip()}/api/deliveries/${id}`;
  }
  let resp = await fetch(address, {
    method: "Get",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!resp.ok) {
    const errorData = await resp.json();
    throw new Error(errorData.message || 'Request failed');
  }

  let data = await resp.json();
  return data;
}
  catch(e){
    console.error(e.message);
  }
}

export async function fetchCuisine() {
  try{
  let token = sessionStorage.getItem("accessToken");
  let resp = await fetch(`${ip()}/api/cuisines`, {
    method: "Get",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!resp.ok) {
    const errorData = await resp.json();
    throw new Error(errorData.message || 'Request failed');
  }

  let data = await resp.json();
  return data;
}catch(e){
  console.error(e.message);
}
}

export async function fetchPendingDel() {
  try{
  let token = sessionStorage.getItem("accessToken");
  let address = `${ip()}/api/delivery-guys/pending-deliveries`;
  let resp = await fetch(address, {
    method: "Get",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!resp.ok) {
    const errorData = await resp.json();
    throw new Error(errorData.message || 'Request failed');
  }

  let data = await resp.json();
  return data;
}
  catch(e){
    console.error(e.message);
  }
}

export async function fetchActiveDel() {
  try{
  let token = sessionStorage.getItem("accessToken");
  let resp = await fetch(`${ip()}/api/delivery-guys/my-active-delivery`, {
    method: "Get",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    }
  });

  if (!resp.ok) {
    const errorData = await resp.json();
    throw new Error(errorData.message || 'Request failed');
  }
  
  let data = await resp.json();
  return data;
}catch(e){
  console.error(e.message);
}
}

export async function fetchActiveDeliveriesUser() {
 try {
      const response = await fetch(`${ip()}/api/users/my-active-orders`, {
          method: 'GET',
          headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}` // Assuming the user is authenticated with a token
          }
      });

      if (response.ok) {
          const deliveries = await response.json(); // Parse JSON response
          return deliveries; // Function to display deliveries
      } else {
          console.error('Failed to fetch active deliveries', response.status);
      }
  } catch (error) {
      console.error('Error:', error);
  }
}
