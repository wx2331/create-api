/*    */ package sun.tools.tree;
/*    */ 
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
/*    */ class ConstantExpression
/*    */   extends Expression
/*    */ {
/*    */   public ConstantExpression(int paramInt, long paramLong, Type paramType) {
/* 40 */     super(paramInt, paramLong, paramType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isConstant() {
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ConstantExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */