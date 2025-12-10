package org.khod.pojo.field;

public class BooleanField extends Field {
    private boolean value;

    public BooleanField(final String name, final String description) {
        super(name, description, FieldType.BOOLEAN);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(final boolean value) {
        this.value = value;
    }
}
