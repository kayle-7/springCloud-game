package com.springboot.workflow.util;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.Map;

public class ActivitiElSupport {
    private static final ExpressionFactory factory = new ExpressionFactoryImpl();
    private static final String key = "${%s}";

    public static Object result(Map<String, Object> map, String expression) {
        SimpleContext context = new SimpleContext();
        ValueExpression e = factory.createValueExpression(context, expression, Boolean.TYPE);
        for (Map.Entry entry : map.entrySet()) {
            factory.createValueExpression(context, String.format(key, entry.getKey()), Object.class).setValue(context, entry.getValue());
        }
        return e.getValue(context);
    }
}