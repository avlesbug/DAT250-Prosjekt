package no.hvl.dat250.jpa.basicexample.Poll;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.lambdaworks.codec.Base64;
import com.lambdaworks.crypto.SCrypt;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

public class SCryptUtil {
    public SCryptUtil() {
    }

    public static String scrypt(String passwd, int N, int r, int p) {
        try {
            byte[] salt = new byte[16];
            SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
            byte[] derived = SCrypt.scrypt(passwd.getBytes("UTF-8"), salt, N, r, p, 32);
            String params = Long.toString((long)(log2(N) << 16 | r << 8 | p), 16);
            StringBuilder sb = new StringBuilder((salt.length + derived.length) * 2);
            sb.append("$s0$").append(params).append('$');
            sb.append(Base64.encode(salt)).append('$');
            sb.append(Base64.encode(derived));
            return sb.toString();
        } catch (UnsupportedEncodingException var8) {
            throw new IllegalStateException("JVM doesn't support UTF-8?");
        } catch (GeneralSecurityException var9) {
            throw new IllegalStateException("JVM doesn't support SHA1PRNG or HMAC_SHA256?");
        }
    }

    public static boolean check(String passwd, String hashed) {
        try {
            String[] parts = hashed.split("\\$");
            if (parts.length == 5 && parts[1].equals("s0")) {
                long params = Long.parseLong(parts[2], 16);
                byte[] salt = Base64.decode(parts[3].toCharArray());
                byte[] derived0 = Base64.decode(parts[4].toCharArray());
                int N = (int)Math.pow(2.0D, (double)(params >> 16 & 65535L));
                int r = (int)params >> 8 & 255;
                int p = (int)params & 255;
                byte[] derived1 = SCrypt.scrypt(passwd.getBytes("UTF-8"), salt, N, r, p, 32);
                if (derived0.length != derived1.length) {
                    return false;
                } else {
                    int result = 0;

                    for(int i = 0; i < derived0.length; ++i) {
                        result |= derived0[i] ^ derived1[i];
                    }

                    return result == 0;
                }
            } else {
                throw new IllegalArgumentException("Invalid hashed value");
            }
        } catch (UnsupportedEncodingException var13) {
            throw new IllegalStateException("JVM doesn't support UTF-8?");
        } catch (GeneralSecurityException var14) {
            throw new IllegalStateException("JVM doesn't support SHA1PRNG or HMAC_SHA256?");
        }
    }

    private static int log2(int n) {
        int log = 0;
        if ((n & -65536) != 0) {
            n >>>= 16;
            log = 16;
        }

        if (n >= 256) {
            n >>>= 8;
            log += 8;
        }

        if (n >= 16) {
            n >>>= 4;
            log += 4;
        }

        if (n >= 4) {
            n >>>= 2;
            log += 2;
        }

        return log + (n >>> 1);
    }
}
