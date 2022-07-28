/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import sun.jvmstat.monitor.MonitorException;
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
/*    */ 
/*    */ 
/*    */ public class SymbolResolutionClosure
/*    */   implements Closure
/*    */ {
/* 40 */   private static final boolean debug = Boolean.getBoolean("SymbolResolutionClosure.debug");
/*    */   
/*    */   private ExpressionEvaluator ee;
/*    */   
/*    */   public SymbolResolutionClosure(ExpressionEvaluator paramExpressionEvaluator) {
/* 45 */     this.ee = paramExpressionEvaluator;
/*    */   }
/*    */   
/*    */   public void visit(Object paramObject, boolean paramBoolean) throws MonitorException {
/* 49 */     if (!(paramObject instanceof ColumnFormat)) {
/*    */       return;
/*    */     }
/*    */     
/* 53 */     ColumnFormat columnFormat = (ColumnFormat)paramObject;
/* 54 */     Expression expression = columnFormat.getExpression();
/* 55 */     String str = expression.toString();
/* 56 */     expression = (Expression)this.ee.evaluate(expression);
/* 57 */     if (debug) {
/* 58 */       System.out.print("Expression: " + str + " resolved to " + expression
/* 59 */           .toString());
/*    */     }
/* 61 */     columnFormat.setExpression(expression);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\SymbolResolutionClosure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */