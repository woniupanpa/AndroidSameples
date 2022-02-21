package com.example.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author yanjim
 * @Date 2022/1/4 08:39
 */
@Entity
public class User {
    @Id
    private Long id;
    private String name;
    private int age;
    private String Address;



    @Generated(hash = 208707295)
    public User(Long id, String name, int age, String Address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.Address = Address;
    }

    @Generated(hash = 586692638)
    public User() {
    }

   

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }
    
}
