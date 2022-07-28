/*    */ package sun.tools.asm;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import sun.tools.java.ClassDeclaration;
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
/*    */ 
/*    */ 
/*    */ final class ClassConstantData
/*    */   extends ConstantPoolData
/*    */ {
/*    */   String name;
/*    */   
/*    */   ClassConstantData(ConstantPool paramConstantPool, ClassDeclaration paramClassDeclaration) {
/* 48 */     String str = paramClassDeclaration.getType().getTypeSignature();
/*    */ 
/*    */     
/* 51 */     this.name = str.substring(1, str.length() - 1);
/* 52 */     paramConstantPool.put(this.name);
/*    */   }
/*    */ 
/*    */   
/*    */   ClassConstantData(ConstantPool paramConstantPool, Type paramType) {
/* 57 */     this.name = paramType.getTypeSignature();
/* 58 */     paramConstantPool.put(this.name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException {
/* 65 */     paramDataOutputStream.writeByte(7);
/* 66 */     paramDataOutputStream.writeShort(paramConstantPool.index(this.name));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int order() {
/* 73 */     return 1;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 77 */     return "ClassConstantData[" + this.name + "]";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\ClassConstantData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */