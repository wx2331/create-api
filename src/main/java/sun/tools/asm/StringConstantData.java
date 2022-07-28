/*    */ package sun.tools.asm;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ final class StringConstantData
/*    */   extends ConstantPoolData
/*    */ {
/*    */   String str;
/*    */   
/*    */   StringConstantData(ConstantPool paramConstantPool, String paramString) {
/* 47 */     this.str = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException {
/* 54 */     paramDataOutputStream.writeByte(1);
/* 55 */     paramDataOutputStream.writeUTF(this.str);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int order() {
/* 62 */     return 4;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     return "StringConstantData[" + this.str + "]=" + this.str.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\StringConstantData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */