/*    */ package sun.tools.asm;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.tree.StringExpression;
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
/*    */ 
/*    */ 
/*    */ final class StringExpressionConstantData
/*    */   extends ConstantPoolData
/*    */ {
/*    */   StringExpression str;
/*    */   
/*    */   StringExpressionConstantData(ConstantPool paramConstantPool, StringExpression paramStringExpression) {
/* 49 */     this.str = paramStringExpression;
/* 50 */     paramConstantPool.put(paramStringExpression.getValue());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException {
/* 57 */     paramDataOutputStream.writeByte(8);
/* 58 */     paramDataOutputStream.writeShort(paramConstantPool.index(this.str.getValue()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int order() {
/* 65 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return "StringExpressionConstantData[" + this.str.getValue() + "]=" + this.str.getValue().hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\StringExpressionConstantData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */