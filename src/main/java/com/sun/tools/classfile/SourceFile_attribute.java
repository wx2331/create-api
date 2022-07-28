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
/*    */ public class SourceFile_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final int sourcefile_index;
/*    */
/*    */   SourceFile_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException {
/* 40 */     super(paramInt1, paramInt2);
/* 41 */     this.sourcefile_index = paramClassReader.readUnsignedShort();
/*    */   }
/*    */
/*    */
/*    */   public SourceFile_attribute(ConstantPool paramConstantPool, int paramInt) throws ConstantPoolException {
/* 46 */     this(paramConstantPool.getUTF8Index("SourceFile"), paramInt);
/*    */   }
/*    */
/*    */   public SourceFile_attribute(int paramInt1, int paramInt2) {
/* 50 */     super(paramInt1, 2);
/* 51 */     this.sourcefile_index = paramInt2;
/*    */   }
/*    */
/*    */   public String getSourceFile(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 55 */     return paramConstantPool.getUTF8Value(this.sourcefile_index);
/*    */   }
/*    */
/*    */   public <R, P> R accept(Visitor<R, P> paramVisitor, P paramP) {
/* 59 */     return paramVisitor.visitSourceFile(this, paramP);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\SourceFile_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
