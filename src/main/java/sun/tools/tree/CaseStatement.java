/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
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
/*    */ public class CaseStatement
/*    */   extends Statement
/*    */ {
/*    */   Expression expr;
/*    */   
/*    */   public CaseStatement(long paramLong, Expression paramExpression) {
/* 45 */     super(96, paramLong);
/* 46 */     this.expr = paramExpression;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 53 */     if (this.expr != null) {
/* 54 */       this.expr.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 55 */       this.expr = convert(paramEnvironment, paramContext, Type.tInt, this.expr);
/* 56 */       this.expr = this.expr.inlineValue(paramEnvironment, paramContext);
/*    */     } 
/* 58 */     return paramVset.clearDeadEnd();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 65 */     return 6;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 72 */     super.print(paramPrintStream, paramInt);
/* 73 */     if (this.expr == null) {
/* 74 */       paramPrintStream.print("default");
/*    */     } else {
/* 76 */       paramPrintStream.print("case ");
/* 77 */       this.expr.print(paramPrintStream);
/*    */     } 
/* 79 */     paramPrintStream.print(":");
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\CaseStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */