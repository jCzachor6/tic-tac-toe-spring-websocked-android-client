package client.tictactoe.jakub.czachor.tictactoeclient.utils;

/**
 * @author jakub
 * Created on 12.05.2019.
 */
public class StringUtils {
    public static String defaultString(String str, String defaultString){
        if(str != null){
            return str;
        }
        return defaultString;
    }
}
