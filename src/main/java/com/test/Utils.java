package com.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static void sleep(int second){
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void print(String msg ){
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(format0.format(new Date()) +" "+ msg);
    }


    public static void main(String[] args) {
        System.out.println( printIntBinary(100));
    }


    public static String printIntBinary(Integer a ){
        String b = Integer.toBinaryString(a);
        StringBuilder sb = new StringBuilder();
        int countZero = 32 - b.length();

        for(int i = 0 ;i < countZero; i ++){
            sb.append("0");
        }
        for(char c : b.toCharArray()){
            sb.append(c);
        }
        StringBuilder xx = new StringBuilder();
        char cs [] =sb.toString().toCharArray();
        for(int i = 0 ;i < cs.length;i ++){
            xx.append(cs[i]);
            if((i + 1) %4 == 0 ){
                xx.append(" ");
            }
        }
        return xx.toString();
    }


    public static String intBinary(Integer a ){
        String b = Integer.toBinaryString(a);
        StringBuilder sb = new StringBuilder();
        int countZero = 32 - b.length();

        for(int i = 0 ;i < countZero; i ++){
            sb.append("0");
        }
        for(char c : b.toCharArray()){
            sb.append(c);
        }
        StringBuilder xx = new StringBuilder();
        char cs [] =sb.toString().toCharArray();
        for(int i = 0 ;i < cs.length;i ++){
            xx.append(cs[i]);
            if((i + 1) %4 == 0 ){
                xx.append(" ");
            }
        }
        return xx.toString().replaceAll(" ","");
    }


    public static String getSpace(String a, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <length -  a.length(); i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
