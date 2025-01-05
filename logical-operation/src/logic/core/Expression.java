package logic.core;

// 逻辑运算表达式抽象类
public interface Expression {

    /**
     * 执行逻辑表达式
     *
     * @param context 方法入参
     * @return 运算结果：true 或者 false
     */
    boolean evaluate(Context context);
}
