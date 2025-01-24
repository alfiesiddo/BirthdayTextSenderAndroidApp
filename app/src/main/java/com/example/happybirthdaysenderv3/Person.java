package com.example.happybirthdaysenderv3;
public class Person {

    private String name;
    private String phone;
    private String birthday;

    private String text;

    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}