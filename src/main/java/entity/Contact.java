package main.java.entity;

/**
 * Created by Vadim on 14.04.2016.
 *
 */
public class Contact {
    private long id;
    private long userId;
    private String lastName;
    private String name;
    private String middleName;
    private String mobilePhone;
    private String homePhone;
    private String address;
    private String email;

    public Contact() {

    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomePhone() {
        return homePhone == null ? "" : homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Contact: " + userId + " " + lastName + " " + name + " " + middleName + " " + mobilePhone + " " + address + " " + email;
    }
}
