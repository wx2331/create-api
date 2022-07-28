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
/*    */ public class StringExpression
/*    */   extends ConstantExpression
/*    */ {
/*    */   String value;
/*    */   
/*    */   public StringExpression(long paramLong, String paramString) {
/* 45 */     super(69, paramLong, Type.tString);
/* 46 */     this.value = paramString;
/*    */   }
/*    */   
/*    */   public boolean equals(String paramString) {
/* 50 */     return this.value.equals(paramString);
/*    */   }
/*    */   public boolean isNonNull() {
/* 53 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 60 */     paramAssembler.add(this.where, 18, this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 67 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return this.value.hashCode() ^ 0xC8D;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 81 */     if (paramObject != null && paramObject instanceof StringExpression) {
/* 82 */       return this.value.equals(((StringExpression)paramObject).value);
/*    */     }
/* 84 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void print(PrintStream paramPrintStream) {
/* 91 */     paramPrintStream.print("\"" + this.value + "\"");
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\StringExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */