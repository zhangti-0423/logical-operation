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
    public static Expression parseExpression(List<String> tokens, Map<String, Predicate<Context>> predicateMap) {
        Stack<Expression> expressionStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            switch (token) {
                case "&&":
                case "||":
                case "!":
                    operatorStack.push(token);
                    break;
                case "(":
                    operatorStack.push(token);
                    break;
                case ")":
                    while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                        String operator = operatorStack.pop();
                        if (Objects.equals(operator, "!")) {
                            Expression expr = expressionStack.pop();
                            expressionStack.push(new NotExpression(expr));
                        } else {
                            List<Expression> subExpressions = new ArrayList<>();
                            subExpressions.add(expressionStack.pop());
                            subExpressions.add(expressionStack.pop());
                            if (Objects.equals(operator, "&&")) {
                                expressionStack.push(new AndExpression(subExpressions));
                            } else if (Objects.equals(operator, "||")) {
                                expressionStack.push(new OrExpression(subExpressions));
                            }
                        }
                    }
                    operatorStack.pop();
                    break;
                default:
                    expressionStack.push(new Literal(predicateMap.get(token)));
                    break;
            }
        }

        while (!operatorStack.isEmpty()) {
            String operator = operatorStack.pop();
            if (Objects.equals(operator, "!")) {
                Expression expr = expressionStack.pop();
                expressionStack.push(new NotExpression(expr));
            } else {
                List<Expression> subExpressions = new ArrayList<>();
                subExpressions.add(expressionStack.pop());
                subExpressions.add(expressionStack.pop());
                if (Objects.equals(operator, "&&")) {
                    expressionStack.push(new AndExpression(subExpressions));
                } else if (Objects.equals(operator, "||")) {
                    expressionStack.push(new OrExpression(subExpressions));
                }
            }
        }

        return expressionStack.pop();
    }
}