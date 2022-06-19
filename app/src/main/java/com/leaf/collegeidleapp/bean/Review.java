package com.leaf.collegeidleapp.bean;

/**
 * 评论实体类
 * @author autumn_leaf
 */
public class Review {

    private Integer id;
    private String username;
    private String currentTime;
    private String content;
    private Integer position;
    private Integer commodityid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCommodityid() {
        return commodityid;
    }

    public void setCommodityid(Integer commodityid) {
        this.commodityid = commodityid;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", content='" + content + '\'' +
                ", position=" + position +
                ", commodityid=" + commodityid +
                '}';
    }
}


