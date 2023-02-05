package com.test;

public class MySelf {

    public MySelf group(){
        return this;
    }

    public MySelf option(){
        return this;
    }

    public MySelf childOption(){
        return this;
    }
    public static void main(String[] args) {
        MySelf self = new MySelf();
        self.group()
                .option()
                .childOption();

    }

}
