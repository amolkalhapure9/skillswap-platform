import{CONFIG} from './config.js';
    function registeruser(event){
          event.preventDefault();
         let firstname=document.getElementById("firstname").value;
    let lastname=document.getElementById("lastname").value;
    let email=document.getElementById("email").value;
    let password=document.getElementById("password").value;
    let bio=document.getElementById("bio").value;
    let userid=document.getElementById("userid").value;
    let mobileno=document.getElementById("mobileno").value;
    let confirmpassword=document.getElementById("confirmpassword").value;

    fetch(`${CONFIG.API_BASE_URL}/register`,{
        method : "POST",
        headers :{
            "content-Type" : "application/json"
        },
        body: JSON.stringify({
            firstname:firstname,
            lastname: lastname,
            email:email,
            password:password,
            confirmpassword:confirmpassword,
            bio:bio,
            userid:userid,
            mobileno:mobileno
        })
    })
    .then(response=>response.json())
    .then(data=>{
        if(data.success==true){
           window.location.href="login.html";
           alert("You have successfully registered");
           console.log("User successfully registered");
        }
        else{
            const fields = ["firstname", "lastname",  "userid", "password","confirmpassword","email"];

            for(let field of fields){
                let el=document.getElementById(field+"error");
                if(el)el.innerHTML="";
            };

           let error=data.data;
           for(let er in error){
             let el = document.getElementById(er + "error");

    if (el) {
        el.innerHTML = error[er];
    }
           }
           console.log(data["message"]);
           
           
            
        }
      
        //
    })
      .catch(error=>{
           alert("Invalid details");
            console.log(error);
        });

    }

    document.addEventListener("DOMContentLoaded",()=>{
        document.getElementById("registerForm")
        .addEventListener("submit",registeruser)
    });
   