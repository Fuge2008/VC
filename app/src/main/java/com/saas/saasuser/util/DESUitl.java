package com.saas.saasuser.util;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import Decoder.BASE64Encoder;


public class DESUitl {
    public final static String ALGORITHM_DES = "DES/CBC/PKCS7Padding";

    public static String tran2cypher(String str1) {
        return encode("Key123Ac", str1);
    }

    public static String encode(String key,String data) {
        if(data == null)
            return null;
        try{
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            //IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            IvParameterSpec iv = new IvParameterSpec(new byte[]{1,2,3,4,5,6,7,8});
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);
            data = new String(data.getBytes("gbk"),"utf-8");
            byte[] bytes = cipher.doFinal(data.getBytes());
            //   return new String(bytes);
            return byte2Base64(bytes);
        }catch(Exception e){
            e.printStackTrace();
            return data;
        }
    }
    public static String byte2Base64(byte[] bytes){
        BASE64Encoder encode = new BASE64Encoder();
        String base64 = encode.encode(bytes);
        return base64;
    }

}
