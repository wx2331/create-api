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
/*    */ final class NumberConstantData
/*    */   extends ConstantPoolData
/*    */ {
/*    */   Number num;
/*    */   
/*    */   NumberConstantData(ConstantPool paramConstantPool, Number paramNumber) {
/* 47 */     this.num = paramNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException {
/* 54 */     if (this.num instanceof Integer) {
/* 55 */       paramDataOutputStream.writeByte(3);
/* 56 */       paramDataOutputStream.writeInt(this.num.intValue());
/* 57 */     } else if (this.num instanceof Long) {
/* 58 */       paramDataOutputStream.writeByte(5);
/* 59 */       paramDataOutputStream.writeLong(this.num.longValue());
/* 60 */     } else if (this.num instanceof Float) {
/* 61 */       paramDataOutputStream.writeByte(4);
/* 62 */       paramDataOutputStream.writeFloat(this.num.floatValue());
/* 63 */     } else if (this.num instanceof Double) {
/* 64 */       paramDataOutputStream.writeByte(6);
/* 65 */       paramDataOutputStream.writeDouble(this.num.doubleValue());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   int order() {
/* 72 */     return (width() == 1) ? 0 : 3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int width() {
/* 79 */     return (this.num instanceof Double || this.num instanceof Long) ? 2 : 1;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\NumberConstantData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */