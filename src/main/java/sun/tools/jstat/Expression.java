/*    */ package sun.tools.jstat;
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
/*    */ public class Expression
/*    */ {
/*    */   private static int nextOrdinal;
/* 38 */   private boolean debug = Boolean.getBoolean("Expression.debug");
/*    */   private Expression left;
/*    */   private Expression right;
/*    */   private Operator operator;
/* 42 */   private int ordinal = nextOrdinal++;
/*    */   
/*    */   Expression() {
/* 45 */     if (this.debug) {
/* 46 */       System.out.println("Expression " + this.ordinal + " created");
/*    */     }
/*    */   }
/*    */   
/*    */   void setLeft(Expression paramExpression) {
/* 51 */     if (this.debug) {
/* 52 */       System.out.println("Setting left on " + this.ordinal + " to " + paramExpression);
/*    */     }
/* 54 */     this.left = paramExpression;
/*    */   }
/*    */   
/*    */   Expression getLeft() {
/* 58 */     return this.left;
/*    */   }
/*    */   
/*    */   void setRight(Expression paramExpression) {
/* 62 */     if (this.debug) {
/* 63 */       System.out.println("Setting right on " + this.ordinal + " to " + paramExpression);
/*    */     }
/* 65 */     this.right = paramExpression;
/*    */   }
/*    */   
/*    */   Expression getRight() {
/* 69 */     return this.right;
/*    */   }
/*    */   
/*    */   void setOperator(Operator paramOperator) {
/* 73 */     if (this.debug) {
/* 74 */       System.out.println("Setting operator on " + this.ordinal + " to " + paramOperator);
/*    */     }
/* 76 */     this.operator = paramOperator;
/*    */   }
/*    */   
/*    */   Operator getOperator() {
/* 80 */     return this.operator;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 84 */     StringBuilder stringBuilder = new StringBuilder();
/* 85 */     stringBuilder.append('(');
/* 86 */     if (this.left != null) {
/* 87 */       stringBuilder.append(this.left.toString());
/*    */     }
/* 89 */     if (this.operator != null) {
/* 90 */       stringBuilder.append(this.operator.toString());
/* 91 */       if (this.right != null) {
/* 92 */         stringBuilder.append(this.right.toString());
/*    */       }
/*    */     } 
/* 95 */     stringBuilder.append(')');
/* 96 */     return stringBuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Expression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */