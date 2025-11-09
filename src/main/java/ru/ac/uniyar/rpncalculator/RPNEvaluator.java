package ru.ac.uniyar.rpncalculator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RPNEvaluator {
    private static final char UNARY_MINUS = '~';

    // Метод преобразования инфиксного выражения в постфиксное
    public static List<String> trans(String[] a, HashMap<Character, Integer> pri) {
        Stack<String> stack = new Stack<>();
        List<String> ans = new ArrayList<>();

        for (String token : a) {


            if (Pattern.matches("[0-9]+", token)) {
                ans.add(token);
            }
            else if (token.equals("(")) { // открывающая скобка — в стек
                stack.push(token);
            } else if (token.equals(")")) { // закрывающая — выгрузить до открывающей
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    ans.add(stack.pop());
                }
                if (!stack.isEmpty()) {
                    stack.pop(); // удалить "(" из стека
                }



            } else { // оператор
                while (!stack.isEmpty()
                        && pri.get(stack.peek().charAt(0)) >= pri.get(token.charAt(0))) {
                    ans.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            ans.add(stack.pop());
        }

        return ans;
    }

    // Метод для вычисления результата постфиксного выражения
    public static double Calc(List<String> postfixExpr) {
        Stack<Double> locals = new Stack<>();



        for (int i = 0; i < postfixExpr.size(); i++) {
            String token = postfixExpr.get(i);

            if (token.matches("[0-9]+")) {
                locals.push(Double.parseDouble(token));
            } else if (token.length() == 1 && "+-*/^~".indexOf(token.charAt(0)) != -1) {

                char c = token.charAt(0);

                if (c == UNARY_MINUS) { // унарный минус
                    double last = locals.isEmpty() ? 0 : locals.pop();
                    double result = Execute('-', 0, last);
                    locals.push(result);

                    continue;
                }

                double second = locals.isEmpty() ? 0 : locals.pop();
                double first = locals.isEmpty() ? 0 : locals.pop();
                double result = Execute(c, first, second);
                locals.push(result);

            }
        }

        return locals.isEmpty() ? 0 : locals.pop();
    }

    // Метод выполнения операции
    static double Execute(char op, double first, double second) {
        switch (op) {
            case '+':
                return first + second;
            case '-':
                return first - second;
            case '*':
                return first * second;
            case '/':
                return first / second;
            case '^':
                return Math.pow(first, second);
            default:
                return 0;
        }
    }
//    public static void main(String[] args) {
//        HashMap<Character, Integer> pri = new HashMap<>();
//        pri.put('(', 0);
//        pri.put('+', 1);
//        pri.put('-', 1);
//        pri.put('*', 2);
//        pri.put('/', 2);
//        pri.put('^', 3);
//        pri.put('~', 4);
//
//        Scanner sc = new Scanner(System.in);
//        // System.out.println("Введите выражение, разделённое пробелами:");
//        String ren = "( 25 + 4 ) * ~ 2";
//        String[] res = ren.split(" ");
//        List<String> ans = trans(res, pri);
//        System.out.println("Постфиксное выражение: " + ans);
//        double result = Calc(ans);
//        System.out.println("Результат: " + result);
//    }
}