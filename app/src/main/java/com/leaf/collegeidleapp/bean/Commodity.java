package com.leaf.collegeidleapp.bean;

import java.util.Arrays;

/**
 * 商品实体类
 * @author : autumn_leaf
 */
public class Commodity {

    //编号
    private Integer id;
    //标题
    private String title;
    //类别
    private String category;
    //价格
    private String price;
    //联系方式
    private String phone;
    //商品描述
    private String description;
    //商品图片,以二进制字节存储
    private byte[] picture;
    //用户学号
    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", price='" + price + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                ", picture=" + Arrays.toString(picture) +
                ", username='" + username + '\'' +
                '}';
    }
}
