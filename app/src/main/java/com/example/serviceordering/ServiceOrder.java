package com.example.serviceordering;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

public class ServiceOrder {

    private String id;
    private String description;
    private Date requestedCompletionDate;
    private  Date orderDate;
    private String notificationContact;
    private Date cancellationDate;
    private String cancellationReason  ;
    private List<ServiceOrderItem> serviceOrderItem;

    public ServiceOrder(String id, String description, Date requestedCompletionDate, Date orderDate,   String notificationContact, Date cancellationDate, String cancellationReason, List<ServiceOrderItem> serviceOrderItem) {
        this.id = id;
        this.description = description;
        this.requestedCompletionDate = requestedCompletionDate;
        this.orderDate = orderDate;
        this.notificationContact = notificationContact;
        this.cancellationDate = cancellationDate;
        this.cancellationReason = cancellationReason;
        this.serviceOrderItem = serviceOrderItem;
    }

    public ServiceOrder() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRequestedCompletionDate() {
        return requestedCompletionDate;
    }

    public void setRequestedCompletionDate(Date requestedCompletionDate) {
        this.requestedCompletionDate = requestedCompletionDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getNotificationContact() {
        return notificationContact;
    }

    public void setNotificationContact(String notificationContact) {
        this.notificationContact = notificationContact;
    }

    public Date getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(Date cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public List<ServiceOrderItem> getServiceOrderItem() {
        return serviceOrderItem;
    }

    public void setServiceOrderItem(List<ServiceOrderItem> serviceOrderItem) {
        this.serviceOrderItem = serviceOrderItem;
    }
}
