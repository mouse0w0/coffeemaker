package com.github.mouse0w0.coffeemaker.template;

import java.io.IOException;
import java.io.InputStream;

public interface TemplateParser {
    Template parse(InputStream in) throws IOException, TemplateParseException;
}
