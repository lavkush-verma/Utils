package com.lk.app.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private static final String ENCRYPTION_KEY = "test123";
    private static final String TAG = AESUtil.class.getSimpleName();

    public synchronized static String encrypt(String src) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
            return Base64.encodeBytes(cipher.doFinal(src.getBytes("UTF-8")));
        } catch (Exception e) {
            Log.e(TAG, "Exception while Encryption : ", e);
            throw new RuntimeException(e);
        }
    }

    public synchronized static String decrypt(String src) {
        String decrypted = "";
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv());
            decrypted = new String(cipher.doFinal(Base64.decode(src)));
        } catch (Exception e) {
            Log.e(TAG, "Exception while Decryption : ", e);
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    static AlgorithmParameterSpec makeIv() {
        final String ivString = MD5(new StringBuilder(ENCRYPTION_KEY).reverse().toString()).substring(0, 16);
        try {
            final byte[] key = ivString.getBytes("UTF-8");
            return new IvParameterSpec(key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Key makeKey() {
        try {
            return new SecretKeySpec(MD5(new StringBuilder(ENCRYPTION_KEY).reverse().toString()).getBytes("UTF-8"), "AES");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String MD5(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(key.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "NoSuchAlgorithmException while making Key : ", e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException while making Key : ", e);
        }
        return null;
    }
}
