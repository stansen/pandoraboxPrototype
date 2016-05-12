package com.rxjavahttprequest.utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;


import com.google.gson.Gson;
import com.rxjavahttprequest.model.BaseResponseModel;
import com.rxjavahttprequest.model.EncryptRequestModel;
import com.rxjavahttprequest.model.SercurityPara;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by Gstansen on 2015/11/24.
 */
public class Sercurity {

    /**
     * 指定加密算法为DESede
     */
    private static String ALGORITHM = "RSA";
    /**
     * 指定key的大小
     */
    private static int KEYSIZE = 1024;
    /**
     * 指定公钥存放文件
     */
    private static String PUBLIC_KEY_FILE = "file:///android_asset/cer/publickey";
    /**
     * 指定私钥存放文件
     */
    private static String PRIVATE_KEY_FILE = "file:///android_asset/cer/privatekey";

    static Cipher cipher;
    static final String KEY_ALGORITHM = "AES";
    /*
     *
     */
    static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";


    public Sercurity(Context context) {
    }

    /**
     * 生成16位字节数组  IV 加密向量
     *
     * @return
     */
    public static byte[] generateKey() {
        byte[] bytes = new byte[16];
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            Integer integer = random.nextInt(9);
            bytes[i] = Byte.parseByte(integer.toString());
        }
//        String iv = "1234567812345678"; //IV length: must be 16 bytes long
//        return iv.getBytes();
        return bytes;
    }

    /**
     * RSA加密key方法
     * source： 源数据
     *
     * @param randomAk 随机16位byte数组(AK) 作为加密的内容
     * @return
     * @throws Exception
     */
    public static String rsaEncrypt(byte[] randomAk) throws Exception {
        /** 将文件中的公钥对象读出 */
        byte[] destiny = Base64.decode(SercurityPara.PUBLICKEY, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(destiny);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(randomAk);
        String serurityKey = "";
        serurityKey = Base64.encodeToString(b1, Base64.DEFAULT);
        return serurityKey;
    }

    /**
     * 使用AES 算法 加密，默认模式 AES/CBC/PKCS5Padding
     *
     * @param jsonString 需要加密的json数据
     * @param randomAk   随机16位byte数组(AK)  作为aes加密秘钥使用
     * @return 最终加密过的数据
     * @throws Exception
     */
    public static String aesEncrypt(String jsonString, byte[] randomAk) throws Exception {

        cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        SecretKeySpec key = new SecretKeySpec(randomAk, KEY_ALGORITHM);
        System.out.println("密钥的长度为：" + key.getEncoded().length);
        cipher.init(Cipher.ENCRYPT_MODE, key);//使用加密模式初始化 密钥
        //encrypt aes加密过一次的数据
        byte[] encrypt = cipher.doFinal(jsonString.getBytes()); //按单部分操作加密或解密数据，或者结束一个多部分操作。
        byte[] iv = cipher.getIV();
        byte[] destiny = new byte[encrypt.length + iv.length];
        System.arraycopy(encrypt, 0, destiny, 0, encrypt.length);
        System.arraycopy(iv, 0, destiny, encrypt.length, iv.length);
        Log.d("tag1", Arrays.toString(encrypt));
        Log.d("tag2", Arrays.toString(destiny));
        //最终加密过得string数据
        String encryptString = Base64.encodeToString(destiny, Base64.DEFAULT);
        Log.d("tag3", encryptString);
        return encryptString;
    }

    public static String aesDecrypt(String data, byte[] randomAk) throws Exception {
        byte[] tempData2 = Base64.decode(data, Base64.DEFAULT);
        byte[] iv = new byte[16];
        byte[] encryptedData = new byte[tempData2.length - 16];

        System.arraycopy(tempData2, tempData2.length - 16, iv, 0, 16);
        System.arraycopy(tempData2, 0, encryptedData, 0, tempData2.length - 16);
        cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        SecretKeySpec key = new SecretKeySpec(randomAk, KEY_ALGORITHM);
        System.out.println("密钥的长度为：" + key.getEncoded().length);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));//使用解密模式初始化 密钥

        byte[] decryptedData = cipher.doFinal(encryptedData); //按单部分操作加密或解密数据，或者结束一个多部分操作。
        String temp = new String(decryptedData);
        return temp;
    }

    /**
     * 加密request
     *
     * @param AKbytes           AK
     * @param needEncryptString 需要加密的json
     * @return 加密过后的请求
     */
    public static EncryptRequestModel encryptRequest(final byte[] AKbytes, String needEncryptString) {
        EncryptRequestModel encryptRequestModel = null;
        String key = "";
        String encryptedData = "";
        try {
            encryptRequestModel = new EncryptRequestModel();
            key = Sercurity.rsaEncrypt(AKbytes);
            encryptedData = Sercurity.aesEncrypt(needEncryptString, AKbytes);
            encryptRequestModel.Key = key;
            encryptRequestModel.Data = encryptedData;
        } catch (Exception e) {

        }
        return encryptRequestModel;
    }

    /**
     * 解密response
     *
     * @param AKbytes           AK
     * @param baseResponseModel http返回的数据
     * @param className         目标responseModel的类名
     * @return 解密过后的response
     */
    public static Object decryptResponse(byte[] AKbytes, BaseResponseModel baseResponseModel, Class className) {
        Gson gson = new Gson();
        Object objectResponse;
        try {
            String jsonDestiny = Sercurity.aesDecrypt(baseResponseModel.Data, AKbytes);
            objectResponse = gson.fromJson(jsonDestiny, className);
        } catch (Exception e) {
            Log.e("tag4", e.toString());
            return null;
        }
        return objectResponse;
    }
    /**
     * 解密response
     *
     * @param AKbytes           AK
     * @param data http返回的加密数据
     * @return 解密过后的response
     */
    public static String decryptRawResponse(byte[] AKbytes, String  data) {
        String jsonDestiny;
        try {
             jsonDestiny = Sercurity.aesDecrypt(data, AKbytes);
        } catch (Exception e) {
            Log.e("tag4", e.toString());
            return null;
        }
        return jsonDestiny;
    }
    static byte[] getIV() {
        String iv = "1234567812345678"; //IV length: must be 16 bytes long
        return iv.getBytes();
    }
}
