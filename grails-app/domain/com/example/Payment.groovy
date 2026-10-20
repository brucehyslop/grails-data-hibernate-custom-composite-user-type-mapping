package com.example

class Payment {

    MonetaryAmount amount
    MonetaryAmount fee

    static mapping = {

        amount type: MonetaryAmountType, {
            column name: 'amount'
            column name: 'currency'
        }

        fee type: MonetaryAmountType, {
            column name: 'fee_amount'
            column name: 'fee_currency'
        }
    }
}
