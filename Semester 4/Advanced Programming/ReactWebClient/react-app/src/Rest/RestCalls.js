import {CURSE_BASE_URL} from "./URL";


function checkStatus(response){
    console.log("response status : ",response.status);
    if(response.status >=200 && response.status<=299)
        return Promise.resolve(response);
    else
        return Promise.reject(new Error(response.statusText));
}

function convertToJson(response){
    console.log("About to convert to JSON response:",response);
    return response.json();
}

function getAll(){
    console.log("Enter getAll function");
    var headers = new Headers();
    headers.append('Accept','application/json');
    var body ={method:'GET',
    headers:headers,
    mode:'cors'};
    var request = new Request(CURSE_BASE_URL,body);
    console.log("About to fetch all curse :",request);
    return fetch(request).then(checkStatus).then(convertToJson).then(data=>{
        console.log("Converted to Json: ",data);
        return data;
    }).catch(error=>{
        console.log('request failed',error);
        return error;
    });
}

function add(cursa){
    console.log("Enter add function :" ,JSON.stringify(cursa));
    var headers = new Headers();
    headers.append("Accept","application/json");
    headers.append("Content-Type","application/json");
    var antet = {
        method:'POST',
        headers:headers,
        mode:'cors',
        body:JSON.stringify(cursa)
    };
    return fetch(CURSE_BASE_URL,antet).then(checkStatus).then(response=>{
        return response.text();
    }).catch(error=>{
        console.log("eroare la POST",error);
        return  Promise.reject(error);
    });
}

function removeCursa(cursa){
    console.log("Enter removeCursa :",cursa);
    var headers = new Headers();
    headers.append("Accept", "application/json");
    var antet = {method:'DELETE',
    headers:headers,
    mode:'cors'};
    var url = CURSE_BASE_URL+'/'+cursa;
    return fetch(url,antet).then(checkStatus).then(res=>{
        return res.text();
    }).catch(e=>{
        console.log("eroare la delete",e);
        return Promise.reject(e);
    });
}

export class RestCaller{
    static getCurse(){
        return  getAll();
    }
    static addCursa(cursa){
        return add(cursa);
    }
    static deleteCursa(cursa){
        return removeCursa(cursa);
    }
}