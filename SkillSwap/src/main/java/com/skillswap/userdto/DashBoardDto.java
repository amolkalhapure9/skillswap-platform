package com.skillswap.userdto;

public class DashBoardDto {
    private String firstname;
    private String lastname;
    private String knowskills;
    private String RequestSent;

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

    public String getRequestSent() {
        return RequestSent;
    }

    public void setRequestSent(String requestSent) {
        RequestSent = requestSent;
    }

    public String getKnowskills() {
        return knowskills;
    }

    public void setKnowskills(String knowskills) {
        this.knowskills = knowskills;
    }
}
