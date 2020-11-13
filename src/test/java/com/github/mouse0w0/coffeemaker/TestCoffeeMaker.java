package com.github.mouse0w0.coffeemaker;

import com.github.mouse0w0.coffeemaker.evaluator.SimpleEvaluator;
import com.github.mouse0w0.coffeemaker.template.Field;
import com.github.mouse0w0.coffeemaker.util.AsmUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestCoffeeMaker {

    public static void main(String[] args) throws IOException {
        CoffeeMaker coffeeMaker = new CoffeeMaker();
//        coffeeMaker.loadTemplate(TestCoffeeMaker.class.getResourceAsStream("TemplateItemGroup.class"));
//        Map<String, Object> dataModel = new HashMap<>();
//        dataModel.put("icon", "minecraft:stone");
//        dataModel.put("registerName", "examplemod_example");
//        dataModel.put("translationKey", "itemGroup.examplemod.example");
//        dataModel.put("hasSearchBar", false);
//        byte[] bytes = AsmUtils.rename(
//                coffeeMaker.getTemplate("template/TemplateItemGroup")
//                        .process(new SimpleEvaluator(dataModel)),
//                "template/TemplateItemGroup",
//                "com/example/examplemod/itemGroup/ExampleItemGroup");
//        Files.write(Paths.get("ExampleItemGroup.class"), bytes);

        coffeeMaker.loadTemplate(TestCoffeeMaker.class.getResourceAsStream("ModItems.class"));
        List<Field> items = Arrays.asList(
                new Field("template/ModItems", "EXAMPLE_ITEM", "Lnet/minecraft/item/Item;"),
                new Field("template/ModItems", "EXAMPLE_ITEM_2", "Lnet/minecraft/item/Item;"));
        SimpleEvaluator evaluator = new SimpleEvaluator(Collections.singletonMap("items", items));
        byte[] bytes = AsmUtils.rename(
                coffeeMaker.getTemplate("template/ModItems")
                        .process(evaluator),
                "template/ModItems",
                "com/example/examplemod/item/ModItems");
        Files.write(Paths.get("ModItems.class"), bytes);
    }
}
