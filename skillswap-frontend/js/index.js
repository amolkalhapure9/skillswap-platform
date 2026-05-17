 import{CONFIG} from './config.js';
 
 
 function loadPage(){
            let token=localStorage.getItem("Token");
        if(!token)
    {
        return;
       
    }
    else{
        fetch(`${CONFIG.API_BASE_URL}/validate-token`,{
            method:"GET",
             headers:{
                "Authorization" : "Bearer "+localStorage.getItem("Token")
            }


        })
        .then(response=>response.json())
        .then(data=>{
                console.log("Valid1");
                if(data.success){
                    console.log("valid2");
                document.querySelector(".space-x-4").innerHTML="";
                const userid=localStorage.getItem("USERID");
               fetch(`${CONFIG.API_BASE_URL}/profile?userid=${userid}`,{
                 method: "GET", 
            headers:{
                "Authorization" : "Bearer "+localStorage.getItem("Token")
            }

               })
               .then(response=>response.json())
               .then(data=>{
                if(data.success){
                    const username=data.data.firstname+" "+data.data.lastname;
                     document.querySelector(".space-x-4").innerText=username;
                     document.getElementById("exploreskills").href="dashboard.html";
                      document.getElementById("getstarted").style.display="none";
                      document.getElementById("explore").href="dashboard.html";
                }
                else{
                    window.location.href="index.html";
                }

               })
                }
                else{
                    throw new Error("Invalid Token");
                }
                

            
        

        })
        .catch(error=>{
            localStorage.removeItem("Token");
            localStorage.removeItem("USERID");
            window.location.href="login.html";
            alert("Please login");
        })
       
    }
    }

    window.onload = loadPage;