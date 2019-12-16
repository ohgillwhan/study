package com.company;

public class TestClass implements Cloneable{
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public TestClass clone() throws CloneNotSupportedException {
        return (TestClass) super.clone();
    }
}
