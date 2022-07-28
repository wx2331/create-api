/*    */ package com.sun.tools.classfile;
/*    */
/*    */ import java.io.IOException;
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
/*    */ public class EnclosingMethod_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final int class_index;
/*    */   public final int method_index;
/*    */
/*    */   EnclosingMethod_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException {
/* 41 */     super(paramInt1, paramInt2);
/* 42 */     this.class_index = paramClassReader.readUnsignedShort();
/* 43 */     this.method_index = paramClassReader.readUnsignedShort();
/*    */   }
/*    */
/*    */
/*    */   public EnclosingMethod_attribute(ConstantPool paramConstantPool, int paramInt1, int paramInt2) throws ConstantPoolException {
/* 48 */     this(paramConstantPool.getUTF8Index("EnclosingMethod"), paramInt1, paramInt2);
/*    */   }
/*    */
/*    */   public EnclosingMethod_attribute(int paramInt1, int paramInt2, int paramInt3) {
/* 52 */     super(paramInt1, 4);
/* 53 */     this.class_index = paramInt2;
/* 54 */     this.method_index = paramInt3;
/*    */   }
/*    */
/*    */   public String getClassName(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 58 */     return paramConstantPool.getClassInfo(this.class_index).getName();
/*    */   }
/*    */
/*    */   public String getMethodName(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 62 */     if (this.method_index == 0)
/* 63 */       return "";
/* 64 */     return paramConstantPool.getNameAndTypeInfo(this.method_index).getName();
/*    */   }
/*    */
/*    */   public <R, D> R accept(Visitor<R, D> paramVisitor, D paramD) {
/* 68 */     return paramVisitor.visitEnclosingMethod(this, paramD);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\EnclosingMethod_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
