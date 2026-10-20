
package com.example

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.type.LongType
import org.hibernate.type.StringType
import org.hibernate.type.Type
import org.hibernate.usertype.CompositeUserType

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class MonetaryAmountType implements CompositeUserType {

    @Override
    String[] getPropertyNames() {
        return [ 'amount', 'currency' ] as String[]
    }

    @Override
    Type[] getPropertyTypes() {
        return [ LongType.INSTANCE, StringType.INSTANCE ] as Type[]
    }

    @Override
    Object getPropertyValue(Object component, int property) throws HibernateException {
        if (component == null) return null

        MonetaryAmount amount = (MonetaryAmount) component
        switch (property) {
            case 0: return amount.amount
            case 1: return amount.currency
            default: throw new HibernateException("Invalid property index: " + property)
        }
    }

    @Override
    void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        if (component == null) return

        MonetaryAmount amount = (MonetaryAmount) component
        switch (property) {
            case 0:
                amount.amount = (Long) value
                break
            case 1:
                amount.currency = (value as String).trim()
                break
            default:
                throw new HibernateException("Invalid property index: " + property)
        }
    }

    @Override
    Class returnedClass() {
        return MonetaryAmount.class
    }

    @Override
    boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) return true
        if (x == null || y == null) return false

        MonetaryAmount mx = (MonetaryAmount) x
        MonetaryAmount my = (MonetaryAmount) y

        return mx.amount == my.amount && mx.currency == my.currency
    }

    @Override
    int hashCode(Object x) throws HibernateException {
        return x?.hashCode() ?: 0
    }

    @Override
    Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {

        Long amount = rs.getLong(names[0])
        String currency = rs.getString(names[1])

        if (rs.wasNull() || currency == null) {
            return null
        }

        return new MonetaryAmount(amount: amount, currency: currency.trim())
    }

    @Override
    void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {

        if (value == null) {
            st.setNull(index, LongType.INSTANCE.sqlType())
            st.setNull(index + 1, StringType.INSTANCE.sqlType())
        } else {

            MonetaryAmount amount = (MonetaryAmount) value

            st.setLong(index, amount)
            st.setString(index + 1, amount.currency)
        }
    }

    @Override
    Object deepCopy(Object value) throws HibernateException {
        if (value == null) return null

        MonetaryAmount original = (MonetaryAmount) value
        return new MonetaryAmount(amount: original.amount, currency: original.currency)
    }

    @Override
    boolean isMutable() {
        return true
    }

    @Override
    Serializable disassemble(Object value, SharedSessionContractImplementor session) throws HibernateException {
        return (Serializable) deepCopy(value)
    }

    @Override
    Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner)
            throws HibernateException {
        return deepCopy(cached)
    }

    @Override
    Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner)
            throws HibernateException {
        return deepCopy(original)
    }
}