package com.test;


import java.util.TooManyListenersException;

public class Test4 {


    public static void main(String[] args) {

        //xx2(512,1023,2045,4022,4599,8193 );
        //System.out.println(xx3(1023));
        System.out.println(getDeliver2(511));


    }

    public static int xx2(int... item) {
        int maxLength = 50;

        for (int i : item) {
            int normalizedCapacity = i;
            normalizedCapacity--;
            normalizedCapacity |= normalizedCapacity >>> 1;
            normalizedCapacity |= normalizedCapacity >>> 2;
            normalizedCapacity |= normalizedCapacity >>> 4;
            normalizedCapacity |= normalizedCapacity >>> 8;
            normalizedCapacity |= normalizedCapacity >>> 16;
            normalizedCapacity++;
            String a = "i=" + i;
            String b = "normalizedCapacity=" + normalizedCapacity + ", cal = " + getDeliver(i);
            System.out.println(a + Utils.getSpace(a, maxLength + 3) + ", 二进制 ：" + Utils.printIntBinary(i));
            System.out.println(b + Utils.getSpace(b, maxLength + 3) + ", 二进制 ：" + Utils.printIntBinary(normalizedCapacity));
        }
        return 0;
    }

    public static int getDeliver(int i ) {
        String intStr = Integer.toBinaryString(i);
        System.out.println(intStr);
        // 如1023 的二进制为 11 1111 1111，先转化为10 0000 0000
        int temp  = i >> intStr.length() -1 ;
        temp = temp << intStr.length() -1;
        // 如果 将除最高位1外，其他位全部转化为0的值和原值相等，则返回返回。
        if(i == temp){
            return i ;
        }
        // 否则将这个抹除的数值乘2即可
        return temp << 1;
    }



    public static int getDeliver2(int i ) {
        if(i % 16 == 0 ){
            return i ;
        }
        return (i / 16 )* 16 + 16 ;
    }



}
