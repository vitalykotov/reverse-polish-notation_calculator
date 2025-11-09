package ru.ac.uniyar.rpncalculator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RPNEvaluator {
    private static final char UNARY_MINUS = '~';
    private static final String NUMBER_PATTERN = "[0-9]+";
    private static final int SINGLE_CHAR = 1;
    // Метод преобразования инфиксного выражения в постфиксное
    public static List<String> trans(final String[] tokens, final Map<Character, Integer> priorityMap) {
        final Deque<String> stack = new ArrayDeque<>();
        final List<String> result = new ArrayList<>();

        for (final String token : tokens) {
            if (Pattern.matches(NUMBER_PATTERN, token)) {
                result.add(token);
            } else if ("(".equals(token)) { // открывающая скобка — в стек
                stack.push(token);
            } else if (")".equals(token)) { // закрывающая — выгрузить до открывающей
                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    result.add(stack.pop());
                }
                if (!stack.isEmpty()) {
                    stack.pop(); // удалить "(" из стека
                }
            } else { // оператор
                while (!stack.isEmpty()
                        && priorityMap.get(stack.peek().charAt(0)) >= priorityMap.get(token.charAt(0))) {
                    result.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    // Метод для вычисления результата постфиксного выражения
    public static double calc(final List<String> postfixExpression) {
        final Deque<Double> stack = new ArrayDeque<>();

        for (final String token : postfixExpression) {
            if (token.matches(NUMBER_PATTERN)) {
                stack.push(Double.parseDouble(token));
            } else if (token.length() == SINGLE_CHAR ) {
                final char operator = token.charAt(0);

                if (operator == UNARY_MINUS) { // унарный минус
                    final double operand = stack.isEmpty() ? 0 : stack.pop();
                    final double unaryResult = execute('-', 0, operand);
                    stack.push(unaryResult);
                } else {
                    final double secondOperand = stack.isEmpty() ? 0 : stack.pop();
                    final double firstOperand = stack.isEmpty() ? 0 : stack.pop();
                    final double binaryResult = execute(operator, firstOperand, secondOperand);
                    stack.push(binaryResult);
                }
            }
        }

        return stack.isEmpty() ? 0 : stack.pop();
    }

    // Метод выполнения операции
    private static double execute(final char operator, final double firstOperand, final double secondOperand) {
        double result;
        switch (operator) {
            case '+':
                result = firstOperand + secondOperand;
                break;
            case '-':
                result = firstOperand - secondOperand;
                break;
            case '*':
                result = firstOperand * secondOperand;
                break;
            case '/':
                result = firstOperand / secondOperand;
                break;
            case '^':
                result = Math.pow(firstOperand, secondOperand);
                break;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }

        return result;
    }
}