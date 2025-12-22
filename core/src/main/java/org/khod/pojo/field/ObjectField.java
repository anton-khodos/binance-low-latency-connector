package org.khod.pojo.field;

import org.khod.pojo.item.DefaultPojoItem;

public class ObjectField extends Field {

    private final DefaultPojoItem internalObject;

    public ObjectField(String name, String description, FieldType type, Field[] fields) {
        super(name, description, type);
        internalObject = new DefaultPojoItem() {
            @Override
            protected Field[] getFieldDefinition() {
                return fields;
            }
        };
    }

    @Override
    public String toString() {
        return String.format("%s(%s)=%s", getName(), getDescription(), internalObject);
    }

    public DefaultPojoItem getValue() {
        return internalObject;
    }
}
