/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
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
/*    */ public class NaryExpression
/*    */   extends UnaryExpression
/*    */ {
/*    */   Expression[] args;
/*    */   
/*    */   NaryExpression(int paramInt, long paramLong, Type paramType, Expression paramExpression, Expression[] paramArrayOfExpression) {
/* 44 */     super(paramInt, paramLong, paramType, paramExpression);
/* 45 */     this.args = paramArrayOfExpression;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression copyInline(Context paramContext) {
/* 52 */     NaryExpression naryExpression = (NaryExpression)clone();
/* 53 */     if (this.right != null) {
/* 54 */       naryExpression.right = this.right.copyInline(paramContext);
/*    */     }
/* 56 */     naryExpression.args = new Expression[this.args.length];
/* 57 */     for (byte b = 0; b < this.args.length; b++) {
/* 58 */       if (this.args[b] != null) {
/* 59 */         naryExpression.args[b] = this.args[b].copyInline(paramContext);
/*    */       }
/*    */     } 
/* 62 */     return naryExpression;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 69 */     int i = 3;
/* 70 */     if (this.right != null)
/* 71 */       i += this.right.costInline(paramInt, paramEnvironment, paramContext); 
/* 72 */     for (byte b = 0; b < this.args.length && i < paramInt; b++) {
/* 73 */       if (this.args[b] != null) {
/* 74 */         i += this.args[b].costInline(paramInt, paramEnvironment, paramContext);
/*    */       }
/*    */     } 
/* 77 */     return i;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void print(PrintStream paramPrintStream) {
/* 84 */     paramPrintStream.print("(" + opNames[this.op] + "#" + hashCode());
/* 85 */     if (this.right != null) {
/* 86 */       paramPrintStream.print(" ");
/* 87 */       this.right.print(paramPrintStream);
/*    */     } 
/* 89 */     for (byte b = 0; b < this.args.length; b++) {
/* 90 */       paramPrintStream.print(" ");
/* 91 */       if (this.args[b] != null) {
/* 92 */         this.args[b].print(paramPrintStream);
/*    */       } else {
/* 94 */         paramPrintStream.print("<null>");
/*    */       } 
/*    */     } 
/* 97 */     paramPrintStream.print(")");
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\NaryExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */