# Logical Concurrency

## 简介
对于复杂的逻辑表达式（包含与、或、非等操作），可以采用递归的方法来解析和求值，同时结合并行计算和短路求值来优化性能。该代码将实现如何设计一个能够处理复杂逻辑表达式的求值器。
## 示例
假设有一个逻辑表达式树，每个节点代表一个操作（与、或、非）或一个布尔值。因此可以使用递归方法来求值，并在可能的情况下进行短路求值。

表达式结构：定义了三种基本表达式类型：Literal（布尔值）、AndExpression（与操作）和 OrExpression（或操作），以及 NotExpression（非操作）。

并行计算：在 AndExpression 和 OrExpression 中，使用 CompletableFuture 将每个子表达式的计算任务提交给线程池执行，从而实现并行计算。
## 短路求值
- AndExpression 中，一旦发现一个子表达式的结果为 false，立即返回 false。
- OrExpression 中，一旦发现一个子表达式的结果为 true，立即返回 true。
- NotExpression 直接对子表达式的结果取反。
- 递归求值：每个表达式的 evaluate 方法都是递归调用其子表达式的 evaluate 方法，从而实现对整个表达式树的求值。
