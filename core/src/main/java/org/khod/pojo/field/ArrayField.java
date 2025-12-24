package org.khod.pojo.field;

public class ArrayField extends Field {

    private final Field[] fields;
    private int size = 0;
    private int counter = 0;

    public ArrayField(String name, String description, Field[] fields ) {
        super(name, description, FieldType.ARRAY);
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
    public int getSize() {
        return size;
    }

    public void initialize() {
        this.size = 0;
        this.counter = 0;
    }

    public Field getNextField() {
        if (counter < fields.length) {
            return fields[counter++];
        }
        throw new IndexOutOfBoundsException("No more fields in the array");
    }

    public void finalizeArray() {
        this.size = counter;
    }
}
