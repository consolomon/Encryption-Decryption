package encryptdecrypt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    static String mode;
    static String data;
    static int key;
    static String alg;

    public static String encryptText(String originalText, String alg, int key) {

        String cypherText = "";
        int code = 0;

        if (alg.equals("unicode")) {
            for (int i = 0; i < originalText.length(); i++) {
                code = (int) originalText.charAt(i) + key;
                cypherText += Character.toString(code);
            }
        } else if (alg.equals("shift")) {
            char chr;
            String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
            String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for (int i = 0; i < originalText.length(); i++) {
                chr = originalText.charAt(i);
                if (Character.isUpperCase(chr)) {
                    code = upperAlphabet.indexOf(chr) + key;
                    code = code > 25 ? code - 26 : code;
                    chr = upperAlphabet.charAt(code);
                } else if (Character.isLowerCase(chr)) {
                    code = lowerAlphabet.indexOf(chr) + key;
                    code = code > 25 ? code - 26: code;
                    chr = lowerAlphabet.charAt(code);
                }
                cypherText += Character.toString(chr);
            }
        }
            return cypherText;
    }

    public static String decryptText(String cypherText, String alg, int key) {

        String originalText = "";
        int code = 0;

        if (alg.equals("unicode")) {
            for (int i = 0; i < cypherText.length(); i++) {
                code = (int) cypherText.charAt(i) - key;
                originalText += Character.toString(code);
            }
        } else if (alg.equals("shift")) {
            char chr;
            String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
            String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for (int i = 0; i < cypherText.length(); i++) {
                chr = cypherText.charAt(i);
                if (Character.isUpperCase(chr)) {
                    code = upperAlphabet.indexOf(chr) - key;
                    if (code > 25) {
                        code -= 26;
                    } else if (code < 0) {
                        code += 26;
                    }
                    chr = upperAlphabet.charAt(code);
                } else if (Character.isLowerCase(chr)) {
                    code = lowerAlphabet.indexOf(chr) - key;
                    if (code > 25) {
                        code -= 26;
                    } else if (code < 0) {
                        code += 26;
                    }
                    chr = lowerAlphabet.charAt(code);
                }
                originalText += Character.toString(chr);
            }
        }
        return originalText;
    }
    public static String getMode(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-mode")) {
                return args[i + 1];
            }
        }
        return "enc";
    }
    public static String getAlg(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-alg")) {
                return args[i + 1];
            }
        }
        return "shift";
    }

    public static int getKey(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-key")) {
                return Integer.parseInt(args[i + 1]);
            }
        }
        return 0;
    }
    public static String getIn(String[] args) {
        String output = new String("");
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-in")) {
                try {
                    File file = new File(args[i + 1]);
                    Scanner inFile = new Scanner(file);
                    String outputData = inFile.nextLine();
                    inFile.close();
                    return outputData;
                } catch (IOException e) {
                    System.out.printf("Error in getIn method is %s", e.getMessage());
                }
            } else if (args[i].equals("-data")) {
                output = args[i + 1];
            }
        }
        return output;
    }

    public static void setOut(String data, String[] args) {
        boolean isPrinted = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-out")) {
                File file = new File(args[i + 1]);
                try {
                    PrintWriter outFile = new PrintWriter(file);
                    outFile.print(data);
                    outFile.flush();
                    isPrinted = true;
                } catch (IOException e) {
                    System.out.printf("Error in setOut method is %s", e.getMessage());
                }
            }
        }
        if (!isPrinted) {
            System.out.print(data);
        }
    }

    public static void main(String[] args) {

        mode = getMode(args);
        key = getKey(args);
        alg = getAlg(args);
        if (mode.equals("enc")) {
            data = getIn(args);
            setOut(encryptText(data, alg, key), args);
        } else if (mode.equals("dec")) {
            data = getIn(args);
            setOut(decryptText(data, alg, key), args);
        }
    }
}