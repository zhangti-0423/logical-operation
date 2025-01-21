package logic.demo;


import logic.core.Context;
import logic.core.Expression;
import logic.utils.LogicalExpressionParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class DemoMulti {

    public static void main(String[] args) {
        String input = "A && ( B || C ) && !D";

        Map<String, Predicate<Context>> predicateMap = new HashMap<>();
        predicateMap.put("A", new ConditionA("factA"));
        predicateMap.put("B", new ConditionB());
        predicateMap.put("C", new ConditionC());
        predicateMap.put("D", new ConditionD());

        Expression expr = LogicalExpressionParser.parse(input, predicateMap);

        Context facts = new Context();
        facts.addFact("demo", "im demo");

        boolean result = expr.evaluate(facts);
        System.out.println("表达式结果: " + result);
    }
}

class ConditionA implements Predicate<Context> {

    /**
     * 逻辑A用到的属性
     */
    private String factA;

    public ConditionA(String factA) {
        this.factA = factA;
    }

    @Override
    public boolean test(Context context) {
        try {
            boolean res = true;
            long time = 1;
            System.out.println("公共入参：" + context.getFact("demo"));
            System.out.println("线程A开始" + Thread.currentThread() + " 线程A独有属性：" + factA);
            TimeUnit.SECONDS.sleep(time);
            System.out.println("线程A结束" + Thread.currentThread() + " 结果为：" + res + " 执行时间为：" + time);
            return res;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}

class ConditionB implements Predicate<Context> {

    @Override
    public boolean test(Context context) {
        try {
            boolean res = false;
            long time = 2;
            System.out.println("公共入参：" + context.getFact("demo"));
            System.out.println("线程B开始" + Thread.currentThread());
            TimeUnit.SECONDS.sleep(time);
            System.out.println("线程B结束" + Thread.currentThread() + " 结果为：" + res + " 执行时间为：" + time);
            return res;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}

class ConditionC implements Predicate<Context> {

    @Override
    public boolean test(Context context) {
        try {
            boolean res = false;
            long time = 3;
            System.out.println("公共入参：" + context.getFact("demo"));
            System.out.println("线程C开始" + Thread.currentThread());
            TimeUnit.SECONDS.sleep(time);
            System.out.println("线程C结束" + Thread.currentThread() + " 结果为：" + res + " 执行时间为：" + time);
            return res;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}

class ConditionD implements Predicate<Context> {

    @Override
    public boolean test(Context context) {
        try {
            boolean res = false;
            long time = 4;
            System.out.println("公共入参：" + context.getFact("demo"));
            System.out.println("线程D开始" + Thread.currentThread());
            TimeUnit.SECONDS.sleep(time);
            System.out.println("线程D结束" + Thread.currentThread() + " 结果为：" + res + " 执行时间为：" + time);
            return res;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}