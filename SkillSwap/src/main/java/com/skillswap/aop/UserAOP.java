package com.skillswap.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;




@Aspect
@Component
public class UserAOP {
    private static final Logger logger= LoggerFactory.getLogger(UserAOP.class);

    @Pointcut("execution(* com.skillswap.userservice.*.*(..))")
    public void servicelayer(){}

    @Around("servicelayer()")
    public Object MethodExecution(ProceedingJoinPoint joinPoint) throws Throwable
    {
      logger.info("Method execution started: "+joinPoint.getSignature().getName());
      Object result=joinPoint.proceed();
      logger.info("Method Execution completed: "+joinPoint.getSignature().getName());
      return result;
    }

    @Pointcut(("execution(String com.skillswap.userservice.ServiceImpl.registerUser(..))"))
    public void userRegistration(){}


    @AfterReturning(
            value = "userRegistration()",
            returning = "result"
    )
    public void registerUser(String result){
        if(result.equals("SUCCESS")){
            logger.info("User is successfully registered");
        }
        else{
            logger.warn("User registration failed");
        }

    }

    @AfterThrowing(
            value="userRegistration()",
            throwing ="exception"
    )
    public void registeUser(Exception ex){
        logger.error("Exception encountered:"+ex);
    }



    @AfterReturning(
            value= "execution(* com.skillswap.usercontroller.UserController.loginUser(..))",
            returning = "result"
    )
    public void userLogin(ResponseEntity<?> result){

        logger.info("Login: "+result.getBody());

    }






}
