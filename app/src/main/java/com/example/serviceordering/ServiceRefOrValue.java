package com.example.serviceordering;

public class ServiceRefOrValue {
    private String name;
    private String description;


    public ServiceRefOrValue(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ServiceRefOrValue() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
