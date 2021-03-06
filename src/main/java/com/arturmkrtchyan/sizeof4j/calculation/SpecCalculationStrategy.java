package com.arturmkrtchyan.sizeof4j.calculation;

import com.arturmkrtchyan.sizeof4j.Primitive;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SpecCalculationStrategy extends BaseCalculationStrategy {

    @Override
    public <T> int calculateShallow(final Class<T> clazz) {
        if (clazz.isArray()) {
            return 0;
        }
        int size = memoryLayout.objectHeaderSize() + calculateDeclaredFields(clazz);
        return roundToMultiple(size, memoryLayout.objectPadding());
    }

    private <T> int calculateDeclaredFields(Class<T> clazz) {
        int size = 0;
        for (Field f : fieldsOf(clazz)) {
            if(f.getType().isPrimitive()) {
                size += Primitive.get(f.getType()).size();
            } else {
                size += memoryLayout.referenceSize();
            }
        }
        return size;
    }

    private Iterable<Field> fieldsOf(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Field f : type.getDeclaredFields()) {
            // exclude static fields
            if (!Modifier.isStatic(f.getModifiers())) {
                fields.add(f);
            }
        }
        return fields;
    }

}