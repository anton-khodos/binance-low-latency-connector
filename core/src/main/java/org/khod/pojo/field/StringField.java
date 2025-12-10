package org.khod.pojo.field;

public class StringField extends Field {
    private final StringBuilder value = new StringBuilder(128);

    public StringField(final String name, final String description) {
        super(name, description, FieldType.STRING);
    }

    public StringBuilder getValue() {
        return value;
    }

    public void setValue(final CharSequence value) {
        this.value.setLength(0);
        this.value.append(value);
    }
}
