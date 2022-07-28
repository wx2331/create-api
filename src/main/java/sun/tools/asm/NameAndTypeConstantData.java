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
/*    */ final class NameAndTypeConstantData
/*    */   extends ConstantPoolData
/*    */ {
/*    */   String name;
/*    */   String type;
/*    */   
/*    */   NameAndTypeConstantData(ConstantPool paramConstantPool, NameAndTypeData paramNameAndTypeData) {
/* 48 */     this.name = paramNameAndTypeData.field.getName().toString();
/* 49 */     this.type = paramNameAndTypeData.field.getType().getTypeSignature();
/* 50 */     paramConstantPool.put(this.name);
/* 51 */     paramConstantPool.put(this.type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException {
/* 58 */     paramDataOutputStream.writeByte(12);
/* 59 */     paramDataOutputStream.writeShort(paramConstantPool.index(this.name));
/* 60 */     paramDataOutputStream.writeShort(paramConstantPool.index(this.type));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int order() {
/* 67 */     return 3;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\NameAndTypeConstantData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */