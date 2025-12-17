package org.khod.pojo.field;

public class LongField extends Field {
    private long value;

    public LongField(final String name, final String description) {
        super(name, description, FieldType.LONG);
    }

    public long getValue() {
        return value;
    }

    public void setValue(final long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)=%s", getName(), getDescription(), value);
    }
}
