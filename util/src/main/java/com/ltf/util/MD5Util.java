package com.ltf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by justin on 16/12/3.
 */
public class MD5Util {

    private static String sTag = "MD5Util";
    private static MessageDigest mMessageDigest;

    static {
        try {
            mMessageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e(sTag, "MessageDigest.init.Exception : " + e.getMessage());
        }
    }

    /**
     * 获取文件的MD5码
     *
     * @param file
     * @return
     */
    public static String getMD5(File file) {
        if (mMessageDigest != null) {
            FileInputStream fis = null;
            MappedByteBuffer byteBuffer = null;
            try {
                fis = new FileInputStream(file);
                FileChannel channel = fis.getChannel();
                byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                synchronized (MD5Util.class) {
                    mMessageDigest.update(byteBuffer);
                }
            } catch (FileNotFoundException e) {
                LogUtil.e(sTag, "getFileMD5.Exception : " + e.getMessage());
            } catch (IOException e) {
                LogUtil.e(sTag, "getFileMD5.Exception : " + e.getMessage());
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        LogUtil.e(sTag, "getFileMD5.Exception : " + e.getMessage());
                    }
                }
                if (byteBuffer != null) {
                    byteBuffer.clear();
                }
            }
            return bytesToHexString(mMessageDigest.digest());
        }
        return null;
    }

    /**
     * 字符串的MD5码
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        if (str != null) {
            return getMD5(str.getBytes());
        }
        return null;
    }

    /**
     * 获取字节数组的MD5码
     *
     * @param bytes
     * @return
     */
    public static String getMD5(byte[] bytes) {
        if (mMessageDigest != null && bytes != null) {
            synchronized (MD5Util.class) {
                mMessageDigest.reset();
                mMessageDigest.update(bytes);
                return bytesToHexString(mMessageDigest.digest());
            }
        }
        return null;
    }

    /**
     * 校验MD5码
     *
     * @param str
     * @param md5Str
     * @return
     */
    public static boolean checkMD5(String str, String md5Str) {
        if (str != null && md5Str != null) {
            return getMD5(str).equals(md5Str);
        }
        return false;
    }

    /**
     * Convert byte[] to hex
     * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param bytes
     *            byte[] data
     * @return hex string
     */
    private static String bytesToHexString(byte[] bytes) {
        if (bytes != null) {
            StringBuffer stringBuffer = new StringBuffer();
            String hv;
            for (int i = 0; i < bytes.length; i++) {
                int v = bytes[i] & 0xFF;
                hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(hv);
            }
            return stringBuffer.toString();
        }
        return null;
    }
}
