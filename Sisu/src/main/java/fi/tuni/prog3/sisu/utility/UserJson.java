/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.utility;

/**
* A class represent User object which is configured in Json files
* @author Cuong Nguyen
*/
public class UserJson{
   private String username;
   private String fullname;
   private String salt;
   private String hashedPassword;
   private String role;

   /**
    * Constructor with given information
    * @param username username of user
    * @param fullname full name of user
    * @param salt salt-string of user
    * @param hashedPassword hashed-password of user
    * @param role role of user: 'student' or 'teacher'
    */
   public UserJson(String username, String fullname, String salt, String hashedPassword, String role) {
       this.username = username;
       this.fullname = fullname;
       this.salt = salt;
       this.hashedPassword = hashedPassword;
       this.role = role;
   }
   
   /**
    * Empty constructor
    */
   public UserJson(){}

   /**
    * return username of user
    * @return username of user
    */
   public String getUsername() {
       return username;
   }

   /**
    * set new username of user
    * @param username new username of user
    */
   public void setUsername(String username) {
       this.username = username;
   }

   /**
    * return full name of user
    * @return full name of user
    */
   public String getFullname() {
       return fullname;
   }

   /**
    * set new full name of user
    * @param fullname new full name of user
    */
   public void setFullname(String fullname) {
       this.fullname = fullname;
   }

   /**
    * return salt-string of user
    * @return salt-string of user
    */
   public String getSalt() {
       return salt;
   }

   /**
    * set new salt-string of user
    * @param salt new salt-string of user
    */
   public void setSalt(String salt) {
       this.salt = salt;
   }

   /**
    * return hashed password of user
    * @return hashed password of user
    */
   public String getHashedPassword() {
       return hashedPassword;
   }

   /**
    * set new hashed password
    * @param hashedPassword new hashed password
    */
   public void setHashedPassword(String hashedPassword) {
       this.hashedPassword = hashedPassword;
   }

   /**
    * return role of user
    * @return role of user
    */
   public String getRole() {
       return role;
   }

   /**
    * set new role of user
    * @param role new role of user
    */
   public void setRole(String role) {
       this.role = role;
   }
   
  
}
