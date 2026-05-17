
import{CONFIG} from './config.js';

    function loginUser(event){
        event.preventDefault();

        const userid=document.getElementById("userid").value;
        const password=document.getElementById("password").value;

        fetch(`${CONFIG.API_BASE_URL}/login`,{
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userid:userid,
                password:password

            })

        })
        .then(response=> response.json())
        .then(data=>{
            console.log("Response", data);
            let token=data.message.split("=")[1];
            console.log(data.data.userid);
            localStorage.setItem("Token", token);
            localStorage.setItem("USERID",data.data.userid);
            window.location.href="dashboard.html";
        })
        .catch(error=>{
           alert("Invalid userid or password");
            console.log(error);
        });
    }


    document.addEventListener("DOMContentLoaded", () => {

    document
        .getElementById("loginForm")
        .addEventListener("submit", loginUser);

});
