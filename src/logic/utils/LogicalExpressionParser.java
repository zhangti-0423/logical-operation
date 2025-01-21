package logic.utils;


import logic.core.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 逻辑表达式解析器
public class LogicalExpressionParser {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\s*(\\(|\\)|!|&&|\\|\\||\\w+)\\s*");

    /**
     * 逻辑表达式解析
     *
     * @param input        逻辑表达式，比如：A && ( B || C ) && !D
     * @param predicateMap 每个逻辑条件对应的实现
     * @return 逻辑表达式
     */
    public static Expression parse(String input, Map<String, Predicate<Context>> predicateMap) {
        List<String> tokens = tokenize(input);
        return parseExpression(tokens, predicateMap);
    }

    /**
     * 逻辑表达式拆分
     *
     * @param input 逻辑表达式，比如：A && ( B || C ) && !D
     * @return 逻辑表达式拆分后的元素，比如：[A, &&, (, B, ||, C, ), &&, !, D]
     */
    public static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(input);
        while (matcher.find()) {
            tokens.add(matcher.group().trim());
        }
        return tokens;
    }

    /**
     * 逻辑表达式解析
     *
     * @param tokens       逻辑表达式拆分后的元素
     * @param predicateMap 每个逻辑条件对应的实现
     * @return 逻辑表达式
     */
    private static Expression parseExpression(List<String> tokens, Map<String, Predicate<Context>> predicateMap) {
        Stack<Expression> expressionStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            switch (token) {
                case "(":
                case "!":
                    operatorStack.push(token);
                    break;
                case ")":
                    while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                        applyOperator(expressionStack, operatorStack.pop());
                    }
                    if (!operatorStack.isEmpty() && operatorStack.peek().equals("(")) {
                        operatorStack.pop();
                    }
                    break;
                case "&&":
                case "||":
                    while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(token)) {
                        applyOperator(expressionStack, operatorStack.pop());
                    }
                    operatorStack.push(token);
                    break;
                default:
                    expressionStack.push(new Literal(predicateMap.get(token)));
                    break;
            }
        }

        while (!operatorStack.isEmpty()) {
            applyOperator(expressionStack, operatorStack.pop());
        }

        return expressionStack.pop();
    }

    /**
     * 表达式处理
     *
     * @param expressionStack 表达式栈
     * @param operator        操作符
     */
    private static void applyOperator(Stack<Expression> expressionStack, String operator) {
        switch (operator) {
            case "!":
                Expression operand = expressionStack.pop();
                expressionStack.push(new NotExpression(operand));
                break;
            case "&&":
                Expression rightAnd = expressionStack.pop();
                Expression leftAnd = expressionStack.pop();
                expressionStack.push(new AndExpression(Arrays.asList(leftAnd, rightAnd)));
                break;
            case "||":
                Expression rightOr = expressionStack.pop();
                Expression leftOr = expressionStack.pop();
                expressionStack.push(new OrExpression(Arrays.asList(leftOr, rightOr)));
                break;
        }
    }

    /**
     * 设置操作符优先级
     *
     * @param operator 操作符
     * @return 优先级
     */
    private static int precedence(String operator) {
        return switch (operator) {
            case "!" -> 3;
            case "&&" -> 2;
            case "||" -> 1;
            default -> 0;
        };
    }
}