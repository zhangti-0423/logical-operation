package logic.core;

import java.util.function.Predicate;

// 表示逻辑运算表达式的一个条件
public class Literal implements Expression {

    /**
     * 逻辑表达式中每个条件的具体实现
     */
    private final Predicate<Context> predicate;

    public Literal(Predicate<Context> predicate) {
        this.predicate = predicate;
    }

    /**
     * 获取该条件的处理结果
     *
     * @param context 方法入参
     * @return 处理结果：true 或者 false
     */
    @Override
    public boolean evaluate(Context context) {
        return predicate.test(context);
    }
}
