
package com.example

import org.hibernate.HibernateException
import org.hibernate.metamodel.spi.ValueAccess
import org.hibernate.usertype.CompositeUserType

class MonetaryAmountType implements CompositeUserType<MonetaryAmount> {

    @Override
    Object getPropertyValue(MonetaryAmount component, int property) {
        // index follows the ALPHABETICAL order of the embeddable's attribute names
        return switch (property) {
            case 0 -> component.amount
            case 1 -> component.currency.code
            default -> throw new HibernateException("Unknown property " + property);
        }
    }

    @Override
    MonetaryAmount instantiate(ValueAccess values) {
        // same ordering: amount = 0, currency = 1
        Long amount = values.getValue(0, Long)
        String currency = values.getValue(1, String)

        if (amount == null || currency == null) { return null }
        return new MonetaryAmount(
                amount: amount,
                currency: Currency.of(currency.trim())
        )
    }

    @Override
    Class<?> embeddable() {
        return MonetaryAmount
    }

    @Override
    Class returnedClass() {
        return MonetaryAmount
    }

    @Override
    boolean equals(MonetaryAmount x, MonetaryAmount y) throws HibernateException {
        if (x == y) return true
        if (x == null || y == null) return false

        MonetaryAmount mx = (MonetaryAmount) x
        MonetaryAmount my = (MonetaryAmount) y

        return mx.amount == my.amount && mx.currency == my.currency
    }

    @Override
    int hashCode(MonetaryAmount x) throws HibernateException {
        return x?.hashCode() ?: 0
    }

    @Override
    MonetaryAmount deepCopy(MonetaryAmount value) throws HibernateException {
        return value
    }

    @Override
    boolean isMutable() {
        return false
    }

    @Override
    Serializable disassemble(MonetaryAmount value) throws HibernateException {
        return (Serializable) value
    }

    @Override
    MonetaryAmount assemble(Serializable cached, Object owner)
            throws HibernateException {
        return (MonetaryAmount) cached
    }

    @Override
    MonetaryAmount replace(MonetaryAmount detached, MonetaryAmount managed, Object owner)
            throws HibernateException {
        return detached
    }
}