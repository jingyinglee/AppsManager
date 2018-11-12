package com.android.launcher3.util;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by wzz on 2017/9/12.
 * 获取打开vpn的游戏列表和VPN配置的加密方式
 * 有关VPN的接口加密工具（后台配置需要打开VPN和VPN配置接口加密 庄铭那边的）
 */
public class DesUtil_VPN_INF {

    private static byte[] DESkey = {114, 35, 44, 87, 15, 12, 17, 12};
    private static byte[] DESIV = {21, 116, 23, 52, 125, 24, 85, 51};

    static AlgorithmParameterSpec iv = null;
    private static Key key = null;

    public DesUtil_VPN_INF() {
    }

    /**
     * 动态传入加密key
     *
     * @param data
     * @param desKey
     * @return
     * @throws Exception
     */
    public static String encodeToKey(String data/* byte[] desKey*/) throws Exception {
        init(DESkey, DESIV);
        Cipher enCipher = Cipher.getInstance("DES/ECB/NoPadding");
        enCipher.init(1, key);
        byte[] encrypt = data.getBytes("utf-8");
//        Log.i(DesUtil.class.getSimpleName(),"length:"+encrypt.length);
        if (encrypt.length % 8 != 0) { //not a multiple of 8
            //create a new array with a size which is a multiple of 8
            byte[] padded = new byte[encrypt.length + 8 - (encrypt.length % 8)];
            //copy the old array into it
            System.arraycopy(encrypt, 0, padded, 0, encrypt.length);
            encrypt = padded;
        }
        byte[] pasByte = enCipher.doFinal(encrypt);
        return Base64.encodeToString(pasByte, Base64.NO_WRAP);
    }

    private static void init(byte[] DESkey, byte[] DESIV) {
        try {
            DESKeySpec keySpec = new DESKeySpec(DESkey);
            iv = new IvParameterSpec(DESIV);
            SecretKeyFactory e = SecretKeyFactory.getInstance("DES");
            key = e.generateSecret(keySpec);
        } catch (InvalidKeyException var4) {
            var4.printStackTrace();
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        } catch (InvalidKeySpecException var6) {
            var6.printStackTrace();
        }

    }

    public static String decodeToKey(String data/* byte[] desKey*/) throws Exception {
        init(DESkey, DESIV);
        Cipher deCipher = Cipher.getInstance("DES/ECB/NoPadding");
        deCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] base64 = Base64.decode(data, 1);
        byte[] pasByte = deCipher.doFinal(base64);
        String trim = new String(pasByte, "UTF-8").trim();
        return trim;
    }
}
