package com.company;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
	    TestClass a = new TestClass();
	    a.setTest("cloneTest");

        System.out.println(a.clone().getTest());
    }
}
