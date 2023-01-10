package com.tuling;

import java.util.concurrent.*;

public class Test {

    private String userName;
    private Integer age ;


    public static void main(String[] args) {


        ThreadPoolExecutor executor =  new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("111111");
            }
        });
    }

    public Test() {
    }


    public Test(String userName, Integer age) {
        this.userName = userName;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
