package com.rxjavahttprequest.model;

import java.io.Serializable;

/**
 * Created by wang on 2015/12/14.
 */
public class UserInfo implements Serializable {

    String userName;
    String password;
    String token;
    String AddressName;
    String AddressCode;
    String operatorname;
    String tel;
    String province;
    String company;
    String city;

    int addrlv;
    int statuscode;

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setAddressName(String AddressName){
        this.AddressName = AddressName;
    }

    public void setAddressCode(String AddressCode){
        this.AddressCode = AddressCode;
    }

    public void setAddrlv(int addrlv){
        this.addrlv = addrlv;
    }

    public void setStatuscode(int statuscode){
        this.statuscode = statuscode;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public String getToken(){
        return token;
    }

    public String getAddressName(){
        return AddressName;
    }

    public String getAddressCode(){
        return AddressCode;
    }

    public int getAddrlv(){
        return addrlv;
    }

    public int getStatuscode(){
        return statuscode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String gettel() {
        return tel;
    }

    public void settel(String tel) {
        this.tel = tel;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }

    @Override
    public String toString() {
        return token + AddressName + AddressCode + addrlv + statuscode;
    }
}
