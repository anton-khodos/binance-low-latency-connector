package org.khod.pojo.field;

public abstract class Field {
    private final String name;
    private final String description;
    private final FieldType type;

    public Field(final String name, final String description, final FieldType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
