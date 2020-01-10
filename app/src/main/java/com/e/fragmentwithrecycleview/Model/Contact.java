package com.e.fragmentwithrecycleview.Model;

public class Contact {

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getPhoto() {
        return Photo;
    }

    public void setPhoto(int photo) {
        Photo = photo;
    }

    String Name;
    String Phone;
    int Photo;

    public Contact(String asif, String s, int ic_launcher_background){
        Name = asif;
        Phone = s;
        Photo = ic_launcher_background;
    }

}
