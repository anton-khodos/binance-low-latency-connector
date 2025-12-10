package org.khod.pojo.field;

import org.khod.data.Decimal64;

public class DecimalField extends Field {
    private final Decimal64 value = new Decimal64();

    public DecimalField(final String name, final String description) {
        super(name, description, FieldType.DECIMAL);
    }

    public Decimal64 getValue() {
        return value;
    }

    public void setValue(final Decimal64 value) {
        value.copyTo(this.value);
    }
}
