package com.am.induster.Model;


import java.util.HashMap;

public class Products
{
    private String pname;
    private String description;
    private String price;
    private String poffer;
    private String pofferprize;
    private String pofferbrand;
    private String image;
    private String category;
    private String pid;
    private String date;
    private String time;
    private String productType;
    private String puserid;
    private HashMap<String, String> images;

    public Products()
    {

    }

    public Products(String pname, String description, String price,String poffer,String pofferprize, String image, String category, String pid, String date, String time, String productType,HashMap<String, String> images, String puserid, String pofferbrand) {
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.pofferprize = pofferprize;
        this.poffer = poffer;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.productType = productType;
        this.images = images;
        this.puserid  = puserid;
        this.pofferbrand = pofferbrand;
    }


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getPoffer() {
        return poffer;
    }

    public void setPoffer(String poffer) {
        this.poffer = poffer;
    }

    public String getPofferprize() {
        return pofferprize;
    }

    public void setPofferprize(String pofferprize) {
        this.pofferprize = pofferprize;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public HashMap<String, String> getImages() {
        return images;
    }

    public void setImages(HashMap<String, String> images) {
        this.images = images;
    }

    public String getPuserid() {
        return puserid;
    }

    public void setPuserid(String puserid) {
        this.puserid = puserid;
    }

    public String getPofferbrand() {
        return pofferbrand;
    }

    public void setPofferbrand(String pofferbrand) {
        this.pofferbrand = pofferbrand;
    }
}