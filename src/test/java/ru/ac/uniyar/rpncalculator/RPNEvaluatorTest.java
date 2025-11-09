package ru.ac.uniyar.rpncalculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

public class RPNEvaluatorTest {
    RPNEvaluator evaluator = new RPNEvaluator();
    private HashMap<Character, Integer> pri;

    @BeforeEach
    void setUp() {
        pri = new HashMap<>();
        pri.put('(', 0);
        pri.put('+', 1);
        pri.put('-', 1);
        pri.put('*', 2);
        pri.put('/', 2);
        pri.put('^', 3);
        pri.put('~', 4);
    }


    @Test
    public void testTransVariousExpressions() {
        // Numbers only
        assertEquals(List.of("1", "2", "3"), RPNEvaluator.trans(new String[]{"1", "2", "3"}, pri));


        assertEquals(List.of("1", "2", "+"), RPNEvaluator.trans(new String[]{"(", "1", "+", "2", ")"}, pri));
        assertEquals(List.of("1", "2", "3", "*", "+"), RPNEvaluator.trans(new String[]{"1", "+", "2", "*", "3"}, pri));
        assertEquals(List.of("1", "2", "+", "3", "-"), RPNEvaluator.trans(new String[]{"1", "+", "2", "-", "3"}, pri));
        assertEquals(List.of("2", "3", "^"), RPNEvaluator.trans(new String[]{"2", "^", "3"}, pri));


        assertEquals(List.of("5", "~"), RPNEvaluator.trans(new String[]{"~", "5"}, pri));


        assertEquals(List.of("1", "2", "+", "3", "~", "*"), RPNEvaluator.trans(new String[]{"(", "1", "+", "2", ")", "*", "~", "3"}, pri));


        assertEquals(List.of("1", "2", "+", "3", "*"), RPNEvaluator.trans(new String[]{"(", "(", "1", "+", "2", ")", "*", "3", ")"}, pri));


        assertEquals(Collections.emptyList(), RPNEvaluator.trans(new String[]{}, pri));
        assertEquals(Collections.emptyList(), RPNEvaluator.trans(new String[]{")"}, pri));


        assertEquals(List.of("1", "2", "+"), RPNEvaluator.trans(new String[]{"+", "1", "2"}, pri));
        assertEquals(List.of("1", "2", "$"), RPNEvaluator.trans(new String[]{"1", "2", "$"}, pri));
    }


    @Test
    public void testCalcVariousExpressions() {
        // Basic arithmetic
        assertEquals(4.0, RPNEvaluator.calc(List.of("2", "2", "+")), 1e-9);
        assertEquals(2.0, RPNEvaluator.calc(List.of("5", "3", "-")), 1e-9);
        assertEquals(15.0, RPNEvaluator.calc(List.of("3", "5", "*")), 1e-9);
        assertEquals(2.0, RPNEvaluator.calc(List.of("10", "5", "/")), 1e-9);
        assertEquals(8.0, RPNEvaluator.calc(List.of("2", "3", "^")), 1e-9);


        assertEquals(-5.0, RPNEvaluator.calc(List.of("5", "~")), 1e-9);
        assertEquals(-9.0, RPNEvaluator.calc(List.of("1", "2", "+", "3", "~", "*")), 1e-9);


        assertEquals(7.0, RPNEvaluator.calc(List.of("7")), 1e-9);
        assertEquals(0.0, RPNEvaluator.calc(Collections.emptyList()), 1e-9);


        assertEquals(0.0, RPNEvaluator.calc(List.of("~")), 1e-9);
        assertEquals(0.0, RPNEvaluator.calc(List.of("+")), 1e-9);

    }




    @Test
    public void testFullIntegrationExamples() {
        String expr1 = "( 25 + 4 ) * ~ 2";
        List<String> postfix1 = RPNEvaluator.trans(expr1.split(" "), pri);
        assertEquals(-58.0, RPNEvaluator.calc(postfix1), 1e-9);

        String expr2 = "2 + 3 * 4 ^ 2";
        List<String> postfix2 = RPNEvaluator.trans(expr2.split(" "), pri);
        assertEquals(50.0, RPNEvaluator.calc(postfix2), 1e-9);

        String expr3 = "~ 5 + ~ 3";
        List<String> postfix3 = RPNEvaluator.trans(expr3.split(" "), pri);
        assertEquals(-8.0, RPNEvaluator.calc(postfix3), 1e-9);
    }

    @Test
    public void testCalcThrowsExceptionForUnknownOperator() {
        assertThrows(IllegalArgumentException.class, () -> {
            RPNEvaluator.calc(List.of("5", "3", "$"));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            RPNEvaluator.calc(List.of("2", "4", "#"));
        });
    }

    @Test
    public void testCalcWithMultiCharToken() {
        List<String> expr = Arrays.asList("5", "3", "abc");
        double result = RPNEvaluator.calc(expr);
        assertEquals(3.0, result, 1e-9, "Multi-character tokens should be ignored, leaving last number in stack");
    }

    @Test
    public void testCalcWithEmptyStringToken() {
        List<String> expr = Arrays.asList("5", "", "3");
        double result = RPNEvaluator.calc(expr);
        assertEquals(3.0, result, 1e-9, "Empty tokens should be ignored, leaving last number in stack");
    }
    @Test
    void testDemonstrateFailure() {
        String[] tokens = {"2", "+", "3"};
        List<String> result = RPNEvaluator.trans(tokens, pri);
        assertEquals(List.of("2", "3", "+", "ERROR"), result,
                "Этот тест должен упасть - в ожидании лишний элемент 'ERROR'");
    }
}
