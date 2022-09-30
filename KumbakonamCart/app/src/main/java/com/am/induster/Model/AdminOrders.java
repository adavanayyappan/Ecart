package com.am.induster.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class AdminOrders
{
    private String name;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String state;
    private String date;
    private String time;
    private String totalAmount;
    private String state1;
    private String country;
    private String pincode;
    private String orderID;
    private String enterprise;
    private String gst;
    private ArrayList<Products> productsArray;
    private ArrayList<String> productsQuantityArray;

    public AdminOrders()
    {
    }

    public AdminOrders(String name, String phone, String email, String address, String city, String state, String date, String time, String totalAmount,String state1, String pincode, String country, ArrayList<Products> productsArray, String orderID, String enterprise, String gst, ArrayList<String> productsQuantityArray) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.state = state;
        this.date = date;
        this.time = time;
        this.totalAmount = totalAmount;
        this.state1 = state1;
        this.country = country;
        this.pincode = pincode;
        this.orderID = orderID;
        this.gst = gst;
        this.enterprise = enterprise;
        this.productsArray = productsArray;
        this.productsQuantityArray = productsQuantityArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getState1() {
        return state1;
    }

    public void setState1(String state1) {
        this.state1 = state1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public ArrayList<Products> getProductsArray() {
        return productsArray;
    }

    public void setProductsArray(ArrayList<Products> productsArray) {
        this.productsArray = productsArray;
    }


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }


    public ArrayList<String> getProductsQuantityArray() {
        return productsQuantityArray;
    }

    public void setProductsQuantityArray(ArrayList<String> productsQuantityArray) {
        this.productsQuantityArray = productsQuantityArray;
    }

}
