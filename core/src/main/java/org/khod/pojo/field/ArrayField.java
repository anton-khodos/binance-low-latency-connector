package org.khod.pojo.field;

public class ArrayField extends Field {

    private final Field[] fields;

    public ArrayField(String name, String description, FieldType type, Field[] fields ) {
        super(name, description, type);
        this.fields = fields;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s(%s)=[", getName(), getDescription()));
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i].toString());
            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public Field[] getValue() {
        return fields;
    }
}
