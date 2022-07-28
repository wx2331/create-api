/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.asm.Assembler;
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
/*    */ public class DoubleExpression
/*    */   extends ConstantExpression
/*    */ {
/*    */   double value;
/*    */   
/*    */   public DoubleExpression(long paramLong, double paramDouble) {
/* 45 */     super(68, paramLong, Type.tDouble);
/* 46 */     this.value = paramDouble;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 53 */     return new Double(this.value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(int paramInt) {
/* 60 */     return (this.value == paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equalsDefault() {
/* 68 */     return (Double.doubleToLongBits(this.value) == 0L);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 75 */     paramAssembler.add(this.where, 20, new Double(this.value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void print(PrintStream paramPrintStream) {
/* 82 */     paramPrintStream.print(this.value + "D");
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\DoubleExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */