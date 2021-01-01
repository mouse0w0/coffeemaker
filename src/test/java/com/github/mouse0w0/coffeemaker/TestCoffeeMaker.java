package com.github.mouse0w0.coffeemaker;

import java.io.IOException;

public class TestCoffeeMaker {

    public static void main(String[] args) throws IOException {
        CoffeeMaker coffeeMaker = new CoffeeMaker();
        coffeeMaker.loadTemplate(TestCoffeeMaker.class.getResourceAsStream("Template.class"));
        System.out.println("Hello World");
    }
}
