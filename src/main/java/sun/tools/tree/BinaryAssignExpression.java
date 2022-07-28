/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import sun.tools.java.Environment;
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
/*    */ 
/*    */ public class BinaryAssignExpression
/*    */   extends BinaryExpression
/*    */ {
/*    */   Expression implementation;
/*    */   
/*    */   BinaryAssignExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2) {
/* 45 */     super(paramInt, paramLong, paramExpression1.type, paramExpression1, paramExpression2);
/*    */   }
/*    */   
/*    */   public Expression getImplementation() {
/* 49 */     if (this.implementation != null)
/* 50 */       return this.implementation; 
/* 51 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression order() {
/* 58 */     if (precedence() >= this.left.precedence()) {
/* 59 */       UnaryExpression unaryExpression = (UnaryExpression)this.left;
/* 60 */       this.left = unaryExpression.right;
/* 61 */       unaryExpression.right = order();
/* 62 */       return unaryExpression;
/*    */     } 
/* 64 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 71 */     return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 78 */     if (this.implementation != null)
/* 79 */       return this.implementation.inline(paramEnvironment, paramContext); 
/* 80 */     return inlineValue(paramEnvironment, paramContext);
/*    */   }
/*    */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 83 */     if (this.implementation != null)
/* 84 */       return this.implementation.inlineValue(paramEnvironment, paramContext); 
/* 85 */     this.left = this.left.inlineLHS(paramEnvironment, paramContext);
/* 86 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 87 */     return this;
/*    */   }
/*    */   
/*    */   public Expression copyInline(Context paramContext) {
/* 91 */     if (this.implementation != null)
/* 92 */       return this.implementation.copyInline(paramContext); 
/* 93 */     return super.copyInline(paramContext);
/*    */   }
/*    */   
/*    */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 97 */     if (this.implementation != null)
/* 98 */       return this.implementation.costInline(paramInt, paramEnvironment, paramContext); 
/* 99 */     return super.costInline(paramInt, paramEnvironment, paramContext);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\BinaryAssignExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */