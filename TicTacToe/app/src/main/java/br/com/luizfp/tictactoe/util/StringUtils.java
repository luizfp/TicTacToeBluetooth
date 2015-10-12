package br.com.luizfp.tictactoe.util;

/**
 * Created by luiz on 10/11/15.
 */
public class StringUtils {

    public static String getDigitFromString(String s) {
        return s.replaceAll("\\D+","");
    }

    public static String removeDigitsFromString(String s) {
        return s.replaceAll("\\d","");
    }
}
