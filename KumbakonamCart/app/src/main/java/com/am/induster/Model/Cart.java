package com.am.induster.Model;

public class Cart
{
    private String pid;
    private String pname;
    private String price;
    private Integer quantity;
    private String discount;
    private String pofferprize;
    private String poffer;
    private String pofferbrand;
    private String image;
    private Products cartProducts;

    public Cart(String pid, String pname, String price, Integer quantity, String discount,String poffer,String image, String pofferprize, Products cartProducts, String pofferbrand) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.poffer = poffer;
        this.image = image;
        this.pofferprize = pofferprize;
        this.cartProducts = cartProducts;
        this.pofferbrand = pofferbrand;
    }

    public Cart() {
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPoffer() {
        return poffer;
    }

    public void setPoffer(String poffer) {
        this.poffer = poffer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPofferprize() {
        return pofferprize;
    }

    public void setPofferprize(String pofferprize) {
        this.pofferprize = pofferprize;
    }

    public Products getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(Products cartProducts) {
        this.cartProducts = cartProducts;
    }

    public String getPofferbrand() {
        return pofferbrand;
    }

    public void setPofferbrand(String pofferbrand) {
        this.pofferbrand = pofferbrand;
    }
}
