package com.xn.alex.data.common;

import java.security.InvalidKeyException;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.NoSuchAlgorithmException;  
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;  
  
import javax.crypto.BadPaddingException;  
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.NoSuchPaddingException;  

public class RSAEncrypt {

	    //private String privateKey = "AlexSoft";
	    
	    //private String publicKey = "92312046424226317963320878453162606023015028700005093871370481317100648520085747417154228107688416069173711828291526292149764473674272945368779089318574335596999570695499653243356838901876234908146177237943388305870890418676849136625340452990290308326285296783143124386718835730923979998767901658195172785797";
	    
	    private static RSAEncrypt rsaEncryptHandler = null;
	    
	    private RSAEncrypt(){
	    		    		    	
	    }
	    
	    public static RSAEncrypt Instance(){
	    	
            if(null == rsaEncryptHandler){
	    		
	    		rsaEncryptHandler = new RSAEncrypt();
	    		
	    	}
            
            return rsaEncryptHandler;
	    }
          
        /** 
         * Encrypt
         * @param publicKey 
         * @param srcBytes 
         * @return 
         * @throws NoSuchAlgorithmException 
         * @throws NoSuchPaddingException 
         * @throws InvalidKeyException 
         * @throws IllegalBlockSizeException 
         * @throws BadPaddingException 
         */  
        protected byte[] encrypt(RSAPublicKey publicKey,byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{  
            if(publicKey!=null){  
                //Cipher������ɼ��ܻ���ܹ���������RSA  
                Cipher cipher = Cipher.getInstance("RSA");  
                //���ݹ�Կ����Cipher������г�ʼ��  
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
                byte[] resultBytes = cipher.doFinal(srcBytes);  
                return resultBytes;  
            }  
            return null;  
        }  
          
        /** 
         * Decrppt 
         * @param privateKey 
         * @param srcBytes 
         * @return 
         * @throws NoSuchAlgorithmException 
         * @throws NoSuchPaddingException 
         * @throws InvalidKeyException 
         * @throws IllegalBlockSizeException 
         * @throws BadPaddingException 
         */  
        protected byte[] decrypt(RSAPrivateKey privateKey,byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{  
            if(privateKey!=null){  
                //Cipher������ɼ��ܻ���ܹ���������RSA  
                Cipher cipher = Cipher.getInstance("RSA");  
                //���ݹ�Կ����Cipher������г�ʼ��  
                cipher.init(Cipher.DECRYPT_MODE, privateKey);  
                byte[] resultBytes = cipher.doFinal(srcBytes);  
                return resultBytes;  
            }  
            return null;  
        }  
      
        /** 
         * @param args 
         * @throws NoSuchAlgorithmException  
         * @throws BadPaddingException  
         * @throws IllegalBlockSizeException  
         * @throws NoSuchPaddingException  
         * @throws InvalidKeyException  
         */  
        public static void test() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {  
        	RSAEncrypt rsa = new RSAEncrypt();  
            String msg = "0";  
            //KeyPairGenerator use to generate public key and private key based on RSA
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
            //��ʼ����Կ������������Կ��СΪ1024λ  
            keyPairGen.initialize(1024);  
            //����һ����Կ�ԣ�������keyPair��  
            KeyPair keyPair = keyPairGen.generateKeyPair();  
            //�õ�˽Կ  
            RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();               
            //�õ���Կ  
            RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();  
              
            //�ù�Կ����  
            byte[] srcBytes = msg.getBytes();  
            byte[] resultBytes = rsa.encrypt(publicKey, srcBytes);  
              
            //��˽Կ����  
            byte[] decBytes = rsa.decrypt(privateKey, resultBytes);  
              
            System.out.println("������:" + msg);  
            System.out.println("���ܺ���:" + new String(resultBytes));  
            System.out.println("���ܺ���:" + new String(decBytes));  
        }  
 
    }  
