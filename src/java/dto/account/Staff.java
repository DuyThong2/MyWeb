/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.account;

/**
 *
 * @author Admin
 */
public class Staff {
    private String email;
    private String pw;
    private String name;
    
    public Staff(){
        
    }
    public Staff(String name){
        this.name=name;
    }

    public Staff(String email, String pw, String name) {
        this.email = email;
        this.pw = pw;
        this.name = name;
    }
    
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
