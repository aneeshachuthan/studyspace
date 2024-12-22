package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Students {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    private Long id; // Add the ID field

    @Column(length = 10) // Adjust length as needed for seat number
    private String seatNumber;

    @Column(length = 100)
    private String name;

    @Column(length = 15) // Adjust based on phone number format
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column // Use a date format appropriate for your use case
    private String joiningDate;

    @Column
    private Double fee;

    @Column(length = 20) // Adjust based on your status format (e.g., Paid, Unpaid)
    private String status;

    // Getters and Setters
    public Long getId() {
        return id; // Getter for id
    }

    public void setId(Long id) {
        this.id = id; // Setter for id (optional)
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
