 let myusername=null;
 let skillsoffered=0;
 let requestsent=0;
 
 function loadPage(event){
    if(event)event.preventDefault();
            let token=localStorage.getItem("Token");
        if(!token)
    {
        window.location.href="login.html";
        alert("Please login to access the website");
    }
    else{
        fetch("http://localhost:8080/validate-token",{
            method:"GET",
             headers:{
                "Authorization" : "Bearer "+localStorage.getItem("Token")
            }


        })
        .then(response=>response.json())
        .then(data=>{
           
                if(data.success){
              document.body.style.display = "block";
              toggleForm(false);

              let userid=localStorage.getItem("USERID");
              fetch("http://localhost:8080/dashboard?userid="+userid,{
                method:"GET",
                headers:{
                    "Authorization" : "Bearer "+localStorage.getItem("Token")
                }
              }).then(response=>response.json())
              .then(data=>{
                console.log(data.data.firstname);
               
                document.querySelector(".username").innerText="Welcome Back "+data.data.firstname+" "+data.data.lastname;
               
                document.querySelector(".knowSkills").innerText=data.data.knowskills;
                
                document.querySelector(".requestReceived").innerText=data.data.requestSent;

              })
              .catch(error=>{
                console.log(error);
              });
              







            
             
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
        });
       
    }
    }

    function loadProfile(event,userid){
        event.preventDefault();
        toggleForm(false);

        getProfileImage(event,userid);
    

        let selected=document.getElementById("profilesection");
        if(selected){
            document.querySelector(".main").style.display="none";
            document.querySelector(".profile").style.display="block";
            document.querySelector(".skills").style.display="none";
            document.querySelector(".friendrequest").style.display="none";
              document.querySelector(".imageUpdate").style.display="none";
             document.getElementById("submitbutton").style.display="none";
        }
       
         
        fetch("http://localhost:8080/profile?userid="+userid,{
            method: "GET", 
            headers:{
                "Authorization" : "Bearer "+localStorage.getItem("Token")
            }
        })
        .then(response=> response.json())
        .then(data=>{
            console.log(data);

            let info=data.data;
           for (let key in info) {
    let element = document.getElementById(key);

    if (element) {
       
        element.value = info[key];
        if(element.id==="firstname"){
            myusername+=info[key];
        }
        if(element.key==="lastname"){
            myusername+=" "+info[key];
        }
       
       
    }

    
    document.getElementById("pfirstname").innerHTML=data.data.firstname+" "+data.data.lastname;
    document.getElementById("bio").innerHTML=data.data.bio;
    if(data.data.currentUser){
    document.getElementById("updatebutton").style.display="block";
   
}
else{
    document.getElementById("updatebutton").style.display="none";
   
}

    
}

   
        })
         .catch(error=>{
           alert("Invalid userid or password");
            console.log(error);
        });

    }

function updateProfile(event){
    event.preventDefault();
   toggleForm(true);
    document.getElementById("submitbutton").style.display="block";
    document.getElementById("updatebutton").style.display="none";
    document.querySelector(".imageUpdate").style.display="block";
      
          


}

function submitProfile(event){
    event.preventDefault();
    const fields=["firstname", "lastname","bio","email","mobileno"];
     let firstname=document.getElementById("firstname").value;
    let lastname=document.getElementById("lastname").value;
    let email=document.getElementById("email").value;
    let userid=document.getElementById("userid").value;
    let mobileno=document.getElementById("mobileno").value;
    let about=document.getElementById("about").value;
    let bio=document.getElementById("bio").value;

   
    
    
    fetch("http://localhost:8080/updateProfile",{
        method:"POST",
          headers :{
            "Content-Type" : "application/json",
            "Authorization": "Bearer " + localStorage.getItem("Token")
        },
        body: JSON.stringify({
            firstname:firstname,
            lastname: lastname,
            email:email,
            userid:userid,
            mobileno:mobileno,
            bio:bio,
            about:about
        })
       
    })
    .then(response=>response.json())
    .then(data=>{
        if(!data.success){
            alert("Error in Profile update");
        }
        else{
            alert("Profile updated");
            loadProfile(event, userid);
             document.getElementById("submitbutton").style.display="none";
    document.getElementById("updatebutton").style.display="block";
    document.querySelector(".imageUpdate").style.display="none";
         toggleForm(false);
            
        }
    })
    .catch(error=>{
        console.log(error);

    });



}

function toggleForm(enable){
    let form = document.getElementById("userInfo");

    let elements = form.elements; // all inputs inside form

    for(let i = 0; i < elements.length; i++){
        let el=elements[i];
        if(el.id==="userid")continue;

        elements[i].disabled = !enable;
    }
    document.getElementById("bio").disabled=!enable;
}

function renderSkills(skills, containerId, category){
   
    let container=document.querySelector(containerId);
    container.innerHTML="";

    skills.forEach(skills=>{
        let span=document.createElement("span");
        span.className="skill-tag";
        span.innerText=skills.name;

        let button=document.createElement("button");
        button.innerText="❌";

         button.onclick = function(){
            removeSkill(skills.id, category,span);
        };

        span.appendChild(button);
        container.appendChild(span);

    });
}

function loadSkill(event){
    if(event)event.preventDefault();
    let selected=document.getElementById("skillsection");
        if(selected){
            document.querySelector(".main").style.display="none";
            document.querySelector(".profile").style.display="none";
            document.querySelector(".skills").style.display="block";
            document.querySelector(".friendrequest").style.display="none";
              document.querySelector(".imageUpdate").style.display="none";
             document.getElementById("submitbutton").style.display="none";
        }
    
    const username=localStorage.getItem("USERID");
    let categories=["KNOW","LEARN"];
    for(let category of categories){
        fetch("http://localhost:8080/skills?userid="+username+"&category="+category,{
        method: "GET",
        headers :{
            "Content-Type" : "application/json",
            "Authorization": "Bearer " + localStorage.getItem("Token")
        }

    }).then(response=>response.json())
    .then(data=>{
        if(data.success){
            let allskills=data.data;
            renderSkills(allskills, "."+category, category);
        }
    }).catch(error=>{
        console.log(error);
    });

    }
    

}

 function removeSkill(id, category,element){
     
    const username=localStorage.getItem("USERID");

    fetch("http://localhost:8080/removeskills?userid="+username+"&skillid="+id+"&category="+category,{
        method: "DELETE",
         headers :{
            
            "Authorization": "Bearer " + localStorage.getItem("Token")
        }

    }).then(response=>response.json)
    .then(data=>{
        element.remove();
    }).catch(error=>{
        console.log(error);
    });

 }

function renderAllSkills(skills, containerId,category){
      let container=document.querySelector(containerId);
    container.innerHTML="";

    skills.forEach(skills=>{
        let span=document.createElement("span");
        span.className="skill-tag";
        span.innerText=skills.name;

        let button=document.createElement("button");
        button.innerText="✔️";

         button.onclick = function(){
            addSkill(skills.id,category);
        };

        span.appendChild(button);
        container.appendChild(span);

    });
}
let isShow=0;
function showKnowSkill(event){
    event.preventDefault();

    const username=localStorage.getItem("USERID");

    fetch("http://localhost:8080/getSkills",{
        method:"GET",
        headers:{
            "Authorization":"Bearer "+localStorage.getItem("Token")
        }
    })
    .then(response=>response.json())
    .then(data=>{
        let allskills=data.data;
        let skills=["KNOW","LEARN"];

        if(isShow%2===0){
            document.querySelector(".addSkill-KNOW").style.display="block";
       renderAllSkills(allskills,".addSkill-KNOW", "KNOW");
       

        }
        else {
            document.querySelector(".addSkill-KNOW").style.display="none";

        }
        isShow+=1;
        console.log(isShow);
      
        
        

    })
    .catch(error=>{
        console.log(error);
    });


   
}

function addSkill(id ,category){
    const username=localStorage.getItem("USERID");

    fetch("http://localhost:8080/addSkills",{
        method:"POST",
        headers:{
            "Content-Type":"application/json",
            "Authorization":"Bearer "+localStorage.getItem("Token")
        },
        body:JSON.stringify({
            userid:username,
            skills: [
         { id: id, category: category }
  ]
        })

        
    })
    .then(response=>response.json())
    .then(data=>{
        console.log(data);
        loadSkill();
        
    })
    .catch(error=>{
        console.log(error);
    });
}
let learnShow=0;
function showLearnSkill(event){
    event.preventDefault();

    const username=localStorage.getItem("USERID");

    fetch("http://localhost:8080/getSkills",{
        method:"GET",
        headers:{
            "Authorization":"Bearer "+localStorage.getItem("Token")
        }
    })
    .then(response=>response.json())
    .then(data=>{
        let allskills=data.data;
        let skills=["KNOW","LEARN"];
       
      if(learnShow%2===0){
        document.querySelector(".addSkill-LEARN").style.display="block";
         renderAllSkills(allskills,".addSkill-LEARN", "LEARN");
      }
      else{
         document.querySelector(".addSkill-LEARN").style.display="none";
      }
      learnShow+=1;
        
        

    })
    .catch(error=>{
        console.log(error);
    });


   
}
function logout(){
    localStorage.removeItem("Token");
    localStorage.removeItem("USERID");

    window.location.href="index.html";
}

function renderFriendList(userlist, containerid,type, append=false){
    let container=document.querySelector(containerid);

    if(!append){
    container.innerHTML="";
    }


    userlist.forEach(list=>{
        console.log(type);
        if(type===null){
        if(list.status==="Y"){
           type="friendSection";
        }
        else if(list.status==="N"){
            type="RequestSection";
        }
        else{
            type="recommended";
        }
    }
    console.log(type);
        
    let buttonsection="";

    if(type==="recommended"){
       buttonsection=` <button onclick="loadProfile(event, '${list.userid}')">View</button>
                    <button class="sendRequest" onclick="sendRequest(event,'${list.userid}')">Connect</button>`

    }
    else if(type==="RequestSection"){
        buttonsection=` <button onclick="loadProfile(event, '${list.userid}')">View</button>
                    <button class="sendRequest" onclick="cancelRequest(event,'${list.userid}')">Cancel Request</button>`

    }
    else if(type==="RequestReceiveSection"){
        buttonsection=` <button onclick="loadProfile(event, '${list.userid}')">View</button>
                    <button class="sendRequest" onclick="approveRequest(event,'${list.userid}')">Accept</button>
                    <button class="sendRequest" onclick="cancelRequest(event,'${list.userid}')">Reject</button>`

    }
    else if(type==="friendSection"){
        buttonsection=` <button onclick="loadProfile(event, '${list.userid}')">View</button>
        <button class="sendRequest" onclick="cancelRequest(event,'${list.userid}')">Remove Connection</button>`
    }

        let card = `
            <div class="friendmain">
                <div class="friendinfo">
                    <h3 class="username">${list.firstname} ${list.lastname}</h3>
                    <p class="userbio">${list.bio || "No bio available"}</p>
                </div>

                <div class="friendbutton">
                ${buttonsection}
                    
                </div>
            </div>
        `;

        container.innerHTML += card;

    })

}

let pageno=0;
let keyword="";
let sortBy="firstname";
let direction="asc";

function loadUser(event){
    if(event) event.preventDefault();
  toggleForm(false);
    let selected=document.getElementById("friendsection");
        if(selected){
            document.querySelector(".main").style.display="none";
            document.querySelector(".profile").style.display="none";
            document.querySelector(".skills").style.display="none";
            document.querySelector(".friendrequest").style.display="block";
              document.querySelector(".imageUpdate").style.display="none";
             document.getElementById("submitbutton").style.display="none";
        }

     let url="http://localhost:8080/userlist?myuser="+localStorage.getItem("USERID")+"&size=5&page="+pageno;
     if(keyword && keyword.trim()!==""){
        url+=`&keyword=${keyword}`;
     }

     if(sortBy && sortBy.trim!==""){
        url+=`&sortBy=${sortBy}`;
     }
     if(direction && direction.trim!==""){
        url+=`&direction=${direction}`;
     }


    fetch(url,{
        method: "GET",
        headers:{
            "Authorization": "Bearer "+localStorage.getItem("Token")
        }
    })
    .then(response=>response.json())
    .then(data=>{
        let userlist=data.data.content;
        let type="recommended";
        renderFriendList(userlist,".friendlist",type);

        document.getElementById("nextBtn").disabled = data.data.last;
document.getElementById("prevBtn").disabled = data.data.first;
       
    })
    .catch(error=>{
        console.log(error);
    });

    

}
function nextButton(event){
    event.preventDefault();
    pageno++;
    loadUser();


}

function prevButton(event){
    event.preventDefault();
    pageno--;
    loadUser();
    
}

function searchUser(event){
    event.preventDefault();

    pageno=0;
  

    keyword=document.getElementById("userName").value;
    
    loadSearchUser();

}

function loadSearchUser(event){
    if(event) event.preventDefault();
      toggleForm(false);
    let selected=document.getElementById("friendsection");
        if(selected){
            document.querySelector(".main").style.display="none";
            document.querySelector(".profile").style.display="none";
            document.querySelector(".skills").style.display="none";
            document.querySelector(".friendrequest").style.display="block";
              document.querySelector(".imageUpdate").style.display="none";
             document.getElementById("submitbutton").style.display="none";
        }
        let url="http://localhost:8080/searchUser?myuser="+localStorage.getItem("USERID")+"&size=5&page="+pageno;
         if(keyword && keyword.trim()!==""){
       url+=`&keyword=${keyword}`;
     }

     if(sortBy && sortBy.trim()!==""){
    url+=`&sortBy=${sortBy}`;
     }
     if(direction && direction.trim!==""){
        url+=`&direction=${direction}`;
     }
      fetch(url,{
        method: "GET",
        headers:{
            "Authorization": "Bearer "+localStorage.getItem("Token")
        }
    })
    .then(response=>response.json())
    .then(data=>{
        let userlist=data.data.content;
        let type=null;
        
        renderFriendList(userlist,".friendlist",type);

        document.getElementById("nextBtn").disabled = data.data.last;
document.getElementById("prevBtn").disabled = data.data.first;
       
    })
    .catch(error=>{
        console.log(error);
    });


}

function sendRequest(event, userid){
    event.preventDefault();
  toggleForm(false);
    let currentuserid=userid;
    let myuserid=localStorage.getItem("USERID");

    fetch("http://localhost:8080/sendRequest?currentuserid="+currentuserid+"&myuserid="+myuserid,{
        method: "POST",
        headers:{
             "Content-Type":"application/json",
              "Authorization": "Bearer "+localStorage.getItem("Token")
        }
    })
    .then(response=>response.json())
    .then(data=>{
        let buttonsection=event.target.closest(".friendbutton");

         buttonsection.innerHTML=` <button onclick="viewProfile(event, '${currentuserid}')">View</button>
                    <button class="sendRequest" onclick="cancelRequest(event,'${currentuserid}')">Cancel Request</button>`
        
       
    })





}

function loadRequestedUser(event){

      let selected=document.getElementById("friendsection");
        toggleForm(false);
        if(selected){
            document.querySelector(".main").style.display="none";
            document.querySelector(".profile").style.display="none";
            document.querySelector(".skills").style.display="none";
            document.querySelector(".friendrequest").style.display="block";
              document.querySelector(".imageUpdate").style.display="none";
             document.getElementById("submitbutton").style.display="none";
        }

           let requestedUser="http://localhost:8080/requested-user?myuser="+localStorage.getItem("USERID")+"&size=5&page="+pageno;
     if(keyword && keyword.trim()!==""){
        requestedUser+=`&keyword=${keyword}`;
     }

     if(sortBy && sortBy.trim()!==""){
    requestedUser+=`&sortBy=${sortBy}`;
     }
     if(direction && direction.trim!==""){
        requestedUser+=`&direction=${direction}`;
     }
     let receivedRequestUser="http://localhost:8080/received-user?myuser="+localStorage.getItem("USERID")+"&size=5&page="+pageno;
    if(keyword && keyword.trim()!==""){
       receivedRequestUser+=`&keyword=${keyword}`;
     }

     if(sortBy && sortBy.trim()!==""){
        receivedRequestUser+=`&sortBy=${sortBy}`;
     }
     if(direction && direction.trim!==""){
        receivedRequestUser+=`&direction=${direction}`;
     }

     fetch(requestedUser,{
        method: "GET",
        headers:{
            "Authorization": "Bearer "+localStorage.getItem("Token")
        }
    })
    .then(response=>response.json())
    .then(data=>{
        let userlist=data.data.content;
        let type="RequestSection"
        renderFriendList(userlist,".friendlist", type);

        
        document.getElementById("nextBtn").disabled = data.data.last;
document.getElementById("prevBtn").disabled = data.data.first;

return fetch(receivedRequestUser,{
    method: "GET",
    headers: {
        "Authorization": "Bearer " + localStorage.getItem("Token")
    }
    });
       
    })
    .then(response=>response.json())
    .then(data=>{
          let userlist=data.data.content;
         let type="RequestReceiveSection"
        renderFriendList(userlist,".friendlist", type, true);
    })
    .catch(error=>{
        console.log(error);

        
    });
    

     




}

function cancelRequest(event,userid){
    if(event) event.preventDefault();
  
    let innerText=event.target.closest(".sendRequest").innerText;


    let myuser=localStorage.getItem("USERID");
    let url;
    if(innerText===`Remove Connection`){
     url=`http://localhost:8080/cancelrequest?userid=${userid}&myuser=${myuser}`;  
    }
    else{
  url=`http://localhost:8080/cancelrequest?userid=${userid}&myuser=${myuser}`;
    }
    fetch(url,{
        method: "DELETE",
        headers:{
            "Authorization": "Bearer "+localStorage.getItem("Token")

        }
    })
    .then(response=>response.json())
    .then(data=>{
        console.log(data);
         event.target.closest(".friendmain").remove();

    })
   .catch(error=>{
    console.log(error);
   });


}


function loadfrienduser(event){
    if(event)event.preventDefault();
    console.log("Done");
  toggleForm(false);
    let myuser=localStorage.getItem("USERID");

    let url=`http://localhost:8080/connectedUser?myuser=${myuser}`;

    fetch(url,{
        method:"GET",
        headers:{
            "Authorization": "Bearer "+localStorage.getItem("Token")

        }

    })
    .then(response=>response.json())
    .then(data=>{
        console.log("Executed");
        let userlist=data.data.content;
        console.log(userlist);
        let type="friendSection"
        renderFriendList(userlist,".friendlist", type);

    })

}

function rejectRequest(event, userid){
    const receiver=localStorage.getItem("USERID");
    const sender=userid;
  
    let url=`http://localhost:8080/cancelrequest?userid=${receiver}&myuser=${sender}`;
    fetch(url,{
        method: "DELETE",
        headers:{
            "Authorization": "Bearer "+localStorage.getItem("Token")

        }
    })
    .then(response=>response.json())
    .then(data=>{
        console.log(data);
         event.target.closest(".friendmain").remove();

    })
   .catch(error=>{
    console.log(error);
   });

    
}

function approveRequest(event, userid){
    if(event)event.preventDefault();
  toggleForm(false);
    let sender=userid;
    let receiver=localStorage.getItem("USERID");
     let url=`http://localhost:8080/approverequest?userid=${sender}&myuser=${receiver}`;
    fetch(url,{
        method: "PUT",
        headers:{
            "Authorization": "Bearer "+localStorage.getItem("Token")

        }
    })
    .then(response=>response.json())
    .then(data=>{
        console.log(data);
         event.target.closest(".friendmain").remove();

    })
   .catch(error=>{
    console.log(error);
   });




}

function openImagePicker(event){
    if(event)event.preventDefault();
    document.getElementById("profileInput").click();

    

}
document.getElementById("profileInput").addEventListener("change", uploadImage);

function uploadImage(){
    file = document.getElementById("profileInput").files[0];
 

    if(!file){
        return;
    }

     // preview image instantly
    let imageUrl =
        URL.createObjectURL(file);
    document.getElementById("imageprofile").src=imageUrl;

    let formData=new FormData();
    formData.append("image", file);

    fetch("http://localhost:8080/updateImage",{
        method:"PUT",
        headers:{
             "Authorization": "Bearer "+localStorage.getItem("Token")
        },
        body:formData
    })
    .then(response=>response.json())
    .then(data=>{
        console.log("Image uploaded in server");
    })
    .catch(error=>
    {
        console.log(error);
    }
    )

   document.getElementById("")
    


    

}

function getProfileImage(event, userid){
    if(event)event.preventDefault();
  toggleForm(false);
    fetch("http://localhost:8080/getProfileImage?userid="+userid,{
        method:"GET",
        headers:{
            "Authorization": "Bearer "+localStorage.getItem("Token")
        }

    })
    .then(response=>response.blob())
    .then(data=>{
       if(data.size > 0){

            let imageUrl =
                URL.createObjectURL(data);

            document
            .getElementById("imageprofile")
            .src = imageUrl;

        }
        else{
            console.log("2");
            document.getElementById("imageprofile").src="C:/Users/amolk/Downloads/default.jpg";
        }
        
    })


}






