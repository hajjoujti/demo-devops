package com.formation.demo.rest;

import com.formation.demo.service.Calculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorRestTests {

    private final Calculator calculator = new Calculator();

    @Test
    void testSum() {
        assertEquals(5, calculator.sum(2, 3));
    }

}
