package me.k28611.manage.utils;


import java.security.SecureRandom;
import java.util.Random;

/**
 * @author K28611
 * @date 2020/5/3 22:46
 */
public class VerCodeGenerateUtils {
    private static final String  SYMBOLS = "0123456789";
    private static final Random  random = new SecureRandom();
    private static Integer  codeLength = 6;
    public static String generateVerCode( Integer num){
        if (num == 0)
            num = codeLength;
        char[] tmpChars = new char[num];
        for (int i = 0; i < tmpChars.length ; i++) {
            tmpChars[i] =  SYMBOLS.charAt(random.nextInt(tmpChars.length));
        }
        return new String(tmpChars);
    }
}
