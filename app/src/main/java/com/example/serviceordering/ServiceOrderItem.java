package com.example.serviceordering;

public class ServiceOrderItem {

    private int id;
    private ServiceRefOrValue service;

    public ServiceOrderItem(int id, ServiceRefOrValue service) {
        this.id = id;
        this.service = service;
    }
    public ServiceOrderItem() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServiceRefOrValue getService() {
        return service;
    }

    public void setService(ServiceRefOrValue service) {
        this.service = service;
    }
}
