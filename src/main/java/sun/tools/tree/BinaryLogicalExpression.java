/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
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
/*    */ public abstract class BinaryLogicalExpression
/*    */   extends BinaryExpression
/*    */ {
/*    */   public BinaryLogicalExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 42 */     super(paramInt, paramLong, Type.tBoolean, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 50 */     ConditionVars conditionVars = new ConditionVars();
/*    */ 
/*    */     
/* 53 */     checkCondition(paramEnvironment, paramContext, paramVset, paramHashtable, conditionVars);
/*    */     
/* 55 */     return conditionVars.vsTrue.join(conditionVars.vsFalse);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract void checkCondition(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, ConditionVars paramConditionVars);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 71 */     this.left = this.left.inlineValue(paramEnvironment, paramContext);
/* 72 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 73 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BinaryLogicalExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */