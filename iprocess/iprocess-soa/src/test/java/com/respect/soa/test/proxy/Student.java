package com.respect.soa.test.proxy;

/**
 * Created by sjc on 2017/12/27.
 */
public class Student implements Person {
    private String name;
    public Student(String name) {
        this.name = name;
    }

    @Override
    public void giveMoney() {
        try {
            //假设数钱花了一秒时间
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + "上交班费50元");
    }

    @Override
    public void say(String word) {
        System.out.println("老师:" + word);
    }

}
