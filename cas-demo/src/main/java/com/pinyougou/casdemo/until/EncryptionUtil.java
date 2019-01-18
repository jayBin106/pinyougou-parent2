package com.pinyougou.casdemo.until;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 加密/解密
 *
 * @author 2014年11月24日 上午11:32:41
 */
public class EncryptionUtil {

    public static void main(String[] args) {
        System.out.println(md5Encode("admin"));
        System.out.println(base64Encrypt("123456"));
        System.out.println(base64Encrypt("MTIzNDU2"));
    }

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public final static String md5Encode(String string) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = string.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * base64 加密
     *
     * @param str 字符串
     * @return
     */
    public static String base64Encrypt(String str) {
        try {
            return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * base64 解密
     *
     * @param str 字符串
     * @return
     */
    public static String base64Decrypt(String str) {
        try {
            byte[] result = Base64.getDecoder().decode(str);
            return new String(result, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * HloveyRC4 加密/解密
     *
     * @param aInput
     * @param aKey
     * @return
     */
    public static String HloveyRC4(String aInput, String aKey) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];
        for (int i = 0; i < 256; i++)
            iS[i] = i;
        int j = 1;
        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) aKey.charAt((i % aKey.length()));
        }
        j = 0;
        for (int i = 0; i < 255; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }
        int i = 0;
        j = 0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for (short x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
        }
        return new String(iOutputChar);
    }
}
