/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sw;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
/**
 *
 * @author lskre
 */
public class passwordHandler {
    
    static String filename = "src\\sw\\password.pswd";
    static String defaultPassword = "admin";
    
    protected static String genHash(String inputPassword) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(inputPassword.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
    
    protected static String genSaltedHash(String inputPassword, byte[] salt) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        
        byte[] bytes = md.digest(inputPassword.getBytes());
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        String generatedHash = sb.toString();
        return generatedHash;
    }
    
    protected static String genRandomSaltedHash(String inputPassword, int saltLength) throws NoSuchAlgorithmException{
        byte[] salt = new byte[saltLength];
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        
        byte[] bytes = md.digest(inputPassword.getBytes());
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        String generatedPassword = sb.toString();
        return generatedPassword;
    }
    
    static byte[] genSalt() throws NoSuchAlgorithmException{
        //Always use a SecureRandom generator
        SecureRandom sr = new SecureRandom();
        //Create array for salt
        byte[] salt = new byte[32];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        for(int i=0; i<salt.length; i++){
            if(salt[i] <0)
                salt[i] = (byte) -salt[i];
            if(salt[i] < 48)
                salt[i]+= 48;
            else
                if(salt[i] > 122)
                    salt[i]-= 5;
        
        }
        return salt;
    }
    
    public static boolean hashCompare(byte[] hash1, byte[] hash2){
        return Arrays.equals(hash1, hash2);
    }
    
    static void setPassword(String password) throws NoSuchAlgorithmException, IOException{
        
        byte[] newSalt = genSalt();
        String newHash = genSaltedHash(password, newSalt);
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        String newSaltString = new String(newSalt);
        writer.write(new String(newSalt)+"\n");
        writer.write(newHash);
        writer.close();
        System.out.println("password is changed to "+ password);
        
    }
    
    static void resetPassword() throws NoSuchAlgorithmException, IOException{
        
        setPassword(defaultPassword);
        
    }
    
    
    
    public static void main(String[] args) throws NoSuchAlgorithmException{
        byte[] salt = genSalt();
        String saltstring = new String(salt);
        System.out.println(saltstring);
        System.out.println(genSaltedHash(args[0], salt));
    }
}
