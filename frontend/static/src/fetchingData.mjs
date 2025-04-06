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