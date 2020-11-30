package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.coffeemaker.evaluator.EvaluatorImpl;
import com.github.mouse0w0.coffeemaker.template.Field;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCoffeeMaker {

    public static void main(String[] args) throws IOException {
        CoffeeMaker coffeeMaker = new CoffeeMaker();
//        coffeeMaker.loadTemplate(TestCoffeeMaker.class.getResourceAsStream("TemplateItemGroup.class"));
//        Map<String, Object> dataModel = new HashMap<>();
//        dataModel.put("icon", "minecraft:stone");
//        dataModel.put("registerName", "examplemod_example");
//        dataModel.put("translationKey", "itemGroup.examplemod.example");
//        dataModel.put("hasSearchBar", false);
//        byte[] bytes = coffeeMaker.getTemplate("template/TemplateItemGroup")
//                        .process("com/example/examplemod/itemGroup/ExampleItemGroup", new SimpleEvaluator(dataModel));
//        Files.write(Paths.get("ExampleItemGroup.class"), bytes);

        coffeeMaker.loadTemplate(TestCoffeeMaker.class.getResourceAsStream("ModItems.class"));
        List<Field> items = Arrays.asList(
                new Field("template/ModItems", "EXAMPLE_ITEM", "Lnet/minecraft/item/Item;"),
                new Field("template/ModItems", "EXAMPLE_ITEM_2", "Lnet/minecraft/item/Item;"));
        Map<String, Object> map = new HashMap<>();
        map.put("modid", "examplemod");
        map.put("items", items);
        EvaluatorImpl evaluator = new EvaluatorImpl(map);
        byte[] bytes = coffeeMaker.getTemplate("template/ModItems")
                .process("com/example/examplemod/item/ModItems", evaluator);
        Files.write(Paths.get("ModItems.class"), bytes);
    }
}
