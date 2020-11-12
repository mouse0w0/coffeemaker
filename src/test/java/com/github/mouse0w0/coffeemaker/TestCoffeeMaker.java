package com.github.mouse0w0.coffeemaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TestCoffeeMaker {

    public static void main(String[] args) throws IOException {
        CoffeeMaker coffeeMaker = new CoffeeMaker();
        coffeeMaker.loadTemplate(TestCoffeeMaker.class.getResourceAsStream("TemplateItemGroup.class"));
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("className", "com/example/examplemod/itemGroup/ExampleItemGroup");
        dataModel.put("icon", "minecraft:stone");
        dataModel.put("registerName", "examplemod_example");
        dataModel.put("translationKey", "itemGroup.examplemod.example");
        dataModel.put("hasSearchBar", false);
        byte[] bytes = coffeeMaker.getTemplate("template/TemplateItemGroup")
                .process(new SimpleEvaluator(dataModel));
        Files.write(Paths.get("ExampleItemGroup.class"), bytes);
    }
}
