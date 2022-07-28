/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import sun.jvmstat.monitor.Monitor;
/*    */ import sun.jvmstat.monitor.MonitoredVm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExpressionExecuter
/*    */   implements ExpressionEvaluator
/*    */ {
/* 40 */   private static final boolean debug = Boolean.getBoolean("ExpressionEvaluator.debug");
/*    */   private MonitoredVm vm;
/* 42 */   private HashMap<String, Object> map = new HashMap<>();
/*    */   
/*    */   ExpressionExecuter(MonitoredVm paramMonitoredVm) {
/* 45 */     this.vm = paramMonitoredVm;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object evaluate(Expression paramExpression) {
/* 52 */     if (paramExpression == null) {
/* 53 */       return null;
/*    */     }
/*    */     
/* 56 */     if (debug) {
/* 57 */       System.out.println("Evaluating expression: " + paramExpression);
/*    */     }
/*    */     
/* 60 */     if (paramExpression instanceof Literal) {
/* 61 */       return ((Literal)paramExpression).getValue();
/*    */     }
/*    */     
/* 64 */     if (paramExpression instanceof Identifier) {
/* 65 */       Identifier identifier = (Identifier)paramExpression;
/* 66 */       if (this.map.containsKey(identifier.getName())) {
/* 67 */         return this.map.get(identifier.getName());
/*    */       }
/*    */ 
/*    */       
/* 71 */       Monitor monitor = (Monitor)identifier.getValue();
/* 72 */       Object object = monitor.getValue();
/* 73 */       this.map.put(identifier.getName(), object);
/* 74 */       return object;
/*    */     } 
/*    */ 
/*    */     
/* 78 */     Expression expression1 = paramExpression.getLeft();
/* 79 */     Expression expression2 = paramExpression.getRight();
/*    */     
/* 81 */     Operator operator = paramExpression.getOperator();
/*    */     
/* 83 */     if (operator == null) {
/* 84 */       return evaluate(expression1);
/*    */     }
/* 86 */     Double double_1 = new Double(((Number)evaluate(expression1)).doubleValue());
/* 87 */     Double double_2 = new Double(((Number)evaluate(expression2)).doubleValue());
/* 88 */     double d = operator.eval(double_1.doubleValue(), double_2.doubleValue());
/* 89 */     if (debug) {
/* 90 */       System.out.println("Performed Operation: " + double_1 + operator + double_2 + " = " + d);
/*    */     }
/*    */     
/* 93 */     return new Double(d);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\ExpressionExecuter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */