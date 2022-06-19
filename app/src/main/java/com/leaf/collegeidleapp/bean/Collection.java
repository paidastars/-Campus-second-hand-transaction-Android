package com.leaf.collegeidleapp.bean;


import java.util.Arrays;

/**
 * 我的收藏实体类
 * @author autumn_leaf
 */
public class Collection {


    private Integer id;

    private Integer username;

    private byte[] picture;

    private String title;

    private String description;

    private String price;

    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsername() {
        return username;
    }

    public void setUsername(Integer username) {
        this.username = username;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", username=" + username +
                ", picture=" + Arrays.toString(picture) +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
