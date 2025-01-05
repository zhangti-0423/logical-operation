package logic.core;

// 逻辑“非”操作
public class NotExpression implements Expression {

    /**
     * 参与逻辑“非”运算的条件
     */
    private final Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean evaluate(Context context) {
        return !expression.evaluate(context);
    }
}
