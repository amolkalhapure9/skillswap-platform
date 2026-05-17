package com.skillswap.userdto;

import com.skillswap.userenum.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;


public class RegisterDto {
    @NotBlank(message = "Please enter first name")
    private String firstname;
    @NotBlank(message=" Please enter  the last name")
    private String lastname;
    @Pattern(regexp = "^[A-Za-z0-9]{5,}$", message = "userid should contain atleast 5 character. Use only letters or digits!")
    private String userid;
    @Email(message = "Enter valid email")
    private String email;
    private String bio;
    private String mobileno;
    @Size(max=12, min=6)
    private String password;
    private String confirmpassword;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
}
