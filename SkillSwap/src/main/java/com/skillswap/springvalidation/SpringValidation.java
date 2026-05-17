package com.skillswap.springvalidation;

import com.skillswap.userdto.RegisterDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SpringValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterDto dto=(RegisterDto) target;

        if(dto.getPassword()!=null && dto.getConfirmpassword()!=null){
            if(!dto.getPassword().equals(dto.getConfirmpassword())){
                errors.rejectValue("confirmpassword","confirmpassword.error", "Password and confirmpassword are not matching");
            }

        }

    }
}
