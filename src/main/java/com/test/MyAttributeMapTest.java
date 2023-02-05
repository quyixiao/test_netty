package com.test;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.DefaultAttributeMap;
import org.w3c.dom.Attr;

import java.util.concurrent.ConcurrentHashMap;

public class MyAttributeMapTest {


    public static void main(String[] args) {
        AttributeKey<String> key = AttributeKey.newInstance("zhangsan");
        DefaultAttributeMap defaultAttributeMap = new DefaultAttributeMap();
        Attribute attr= defaultAttributeMap.attr(key);
        attr.set(10);



        System.out.println(attr.getAndRemove());

        System.out.println(attr.get());

        AttributeKey<String> key1 = AttributeKey.newInstance("zhangsan");
    }




}
