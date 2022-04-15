/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.utility;

/**
* A class represent User object which is configured in Json files
*/
public class UserJson{
   private String username;
   private String fullname;
   private String salt;
   private String hashedPassword;
   private String role;

   public UserJson(String username, String fullname, String salt, String hashedPassword, String role) {
       this.username = username;
       this.fullname = fullname;
       this.salt = salt;
       this.hashedPassword = hashedPassword;
       this.role = role;
   }
   
   public UserJson(){}

   public String getUsername() {
       return username;
   }

   public void setUsername(String username) {
       this.username = username;
   }

   public String getFullname() {
       return fullname;
   }

   public void setFullname(String fullname) {
       this.fullname = fullname;
   }

   public String getSalt() {
       return salt;
   }

   public void setSalt(String salt) {
       this.salt = salt;
   }

   public String getHashedPassword() {
       return hashedPassword;
   }

   public void setHashedPassword(String hashedPassword) {
       this.hashedPassword = hashedPassword;
   }

   public String getRole() {
       return role;
   }

   public void setRole(String role) {
       this.role = role;
   }
   
  
}
