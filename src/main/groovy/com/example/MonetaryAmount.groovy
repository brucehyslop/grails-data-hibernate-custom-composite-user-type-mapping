package com.example

import jakarta.validation.constraints.NotNull

class MonetaryAmount {

    long amount

    @NotNull(message = 'missing \'currency\' property')
    String currency

    @Override
    String toString() {

        return "${amount / 100} ${currency}"
    }
}
