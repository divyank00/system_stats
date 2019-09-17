package com.example.system_stats;

public class model_class {
    private String name;
    private String email;
    private String labNo;
    private String access_given;

    public model_class(){}

    public model_class(String name, String email, String labNo, String access_given) {
        this.name = name;
        this.email = email;
        this.labNo = labNo;
        this.access_given = access_given;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLabNo() {
        return labNo;
    }

    public String getAccess_given() {
        return access_given;
    }

    public void setAccess_given(String access_given) {
        this.access_given = access_given;
    }
}
