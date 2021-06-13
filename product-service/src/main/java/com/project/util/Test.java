package com.project.util;

public class Test {


    public String computeA(){
        System.out.println("compute a");
        return "a";
    }

    public String computeB(){
        System.out.println("compute b");
        return "b";
    }

    public void display(String a, String b){
        if (a != null) {
            System.out.println("euu a" + a);
        } else {
            System.out.println("euu b" + b);
        }
    }

    public static void main(String[] args) {
        Test t = new Test();
        t.display(t.computeA(), t.computeB());
    }
}
