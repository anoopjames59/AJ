/*    */ package com.ibm.ier.plugin.tasks.util;
/*    */ 
/*    */ import com.ibm.ier.plugin.configuration.Config;
/*    */ import com.ibm.ier.plugin.configuration.SettingsConfig;
/*    */ import com.ibm.ier.plugin.nls.Logger;
/*    */ import java.io.PrintStream;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.KeyGenerator;
/*    */ import javax.crypto.SecretKey;
/*    */ import javax.crypto.spec.IvParameterSpec;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ import org.apache.commons.codec.binary.Base64;
/*    */ 
/*    */ public class AESEncryptionUtil
/*    */ {
/*    */   public static final String SYMMETRIC_ALGORITHM_BASENAME = "AES";
/*    */   public static final String SYMMETRIC_ALGORITHM_NAME = "AES/CBC/PKCS5Padding";
/*    */   private static final String PRIVATE_IVPSEC_KEY = "3ThGqoC+wZqmovV2togrDQ==";
/*    */   
/*    */   public static String generateKey()
/*    */     throws NoSuchAlgorithmException
/*    */   {
/* 24 */     KeyGenerator kgen = null;
/* 25 */     kgen = KeyGenerator.getInstance("AES");
/* 26 */     kgen.init(128);
/* 27 */     SecretKey key = kgen.generateKey();
/* 28 */     byte[] bytes = key.getEncoded();
/* 29 */     return Base64.encodeBase64String(bytes);
/*    */   }
/*    */   
/*    */   public static String encrypt(String password, String encryptionKey) throws Exception {
/* 33 */     byte[] decodedEncryptionKey = Base64.decodeBase64(encryptionKey);
/* 34 */     Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 35 */     SecretKeySpec key = new SecretKeySpec(decodedEncryptionKey, "AES");
/* 36 */     IvParameterSpec ivspec = new IvParameterSpec(Base64.decodeBase64("3ThGqoC+wZqmovV2togrDQ=="));
/* 37 */     cipher.init(1, key, ivspec);
/* 38 */     byte[] value = cipher.doFinal(password.getBytes());
/* 39 */     String encryptedValue = Base64.encodeBase64String(value);
/* 40 */     return encryptedValue;
/*    */   }
/*    */   
/*    */   public static String decrypt(String encryptedPassword, String encryptionKey) throws Exception {
/* 44 */     byte[] decodedEncryptionKey = Base64.decodeBase64(encryptionKey);
/* 45 */     byte[] decodedEncryptedPassword = Base64.decodeBase64(encryptedPassword);
/* 46 */     Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 47 */     IvParameterSpec ivspec = new IvParameterSpec(Base64.decodeBase64("3ThGqoC+wZqmovV2togrDQ=="));
/* 48 */     SecretKeySpec key = new SecretKeySpec(decodedEncryptionKey, "AES");
/* 49 */     cipher.init(2, key, ivspec);
/* 50 */     byte[] rawPassword = cipher.doFinal(decodedEncryptedPassword);
/* 51 */     return new String(rawPassword);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String getStoredAESEncryptionKey(String schema, String jndi)
/*    */   {
/*    */     try
/*    */     {
/* 61 */       Config.initialize(null, null, schema, jndi);
/* 62 */       return Config.getSettingsConfig().getAESEncryptionKey();
/*    */     }
/*    */     catch (Exception exp)
/*    */     {
/* 66 */       Logger.logError(AESEncryptionUtil.class, "getStoredAESEncryptionKey", exp); }
/* 67 */     return null;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception
/*    */   {
/* 72 */     String encryptionKey = generateKey();
/* 73 */     System.out.println(encryptionKey);
/*    */     
/* 75 */     String rawPassword = "this is a test";
/* 76 */     String encryptedPassword = encrypt(rawPassword, encryptionKey);
/* 77 */     String decryptedRawPassword = decrypt(encryptedPassword, encryptionKey);
/*    */     
/* 79 */     System.out.println(rawPassword);
/* 80 */     System.out.println(encryptedPassword);
/* 81 */     System.out.println(decryptedRawPassword);
/*    */     
/* 83 */     rawPassword = "12312312312312";
/* 84 */     encryptedPassword = encrypt(rawPassword, encryptionKey);
/* 85 */     decryptedRawPassword = decrypt(encryptedPassword, encryptionKey);
/*    */     
/* 87 */     System.out.println(rawPassword);
/* 88 */     System.out.println(encryptedPassword);
/* 89 */     System.out.println(decryptedRawPassword);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\tasks\util\AESEncryptionUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */