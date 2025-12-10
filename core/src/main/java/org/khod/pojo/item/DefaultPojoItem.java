package org.khod.pojo.item;

import org.khod.pojo.field.Field;

import java.util.HashMap;
import java.util.Map;

public abstract class DefaultPojoItem {
    private final Field[] fields;
    private final Map<String, Field> fieldMap;

    public DefaultPojoItem() {
        this.fields = getFieldDefinition();
        this.fieldMap = new HashMap<>(fields.length);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            fieldMap.put(field.getName(), field);
        }

    }

    public Field[] getFields() {
        return fields;
    }

    abstract Field[] getFieldDefinition();

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }
}
