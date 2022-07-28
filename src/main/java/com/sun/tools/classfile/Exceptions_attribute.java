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
/*    */ public class Exceptions_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final int number_of_exceptions;
/*    */   public final int[] exception_index_table;
/*    */
/*    */   Exceptions_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException {
/* 40 */     super(paramInt1, paramInt2);
/* 41 */     this.number_of_exceptions = paramClassReader.readUnsignedShort();
/* 42 */     this.exception_index_table = new int[this.number_of_exceptions];
/* 43 */     for (byte b = 0; b < this.number_of_exceptions; b++) {
/* 44 */       this.exception_index_table[b] = paramClassReader.readUnsignedShort();
/*    */     }
/*    */   }
/*    */
/*    */   public Exceptions_attribute(ConstantPool paramConstantPool, int[] paramArrayOfint) throws ConstantPoolException {
/* 49 */     this(paramConstantPool.getUTF8Index("Exceptions"), paramArrayOfint);
/*    */   }
/*    */
/*    */   public Exceptions_attribute(int paramInt, int[] paramArrayOfint) {
/* 53 */     super(paramInt, 2 + 2 * paramArrayOfint.length);
/* 54 */     this.number_of_exceptions = paramArrayOfint.length;
/* 55 */     this.exception_index_table = paramArrayOfint;
/*    */   }
/*    */
/*    */   public String getException(int paramInt, ConstantPool paramConstantPool) throws ConstantPoolException {
/* 59 */     int i = this.exception_index_table[paramInt];
/* 60 */     return paramConstantPool.getClassInfo(i).getName();
/*    */   }
/*    */
/*    */   public <R, D> R accept(Visitor<R, D> paramVisitor, D paramD) {
/* 64 */     return paramVisitor.visitExceptions(this, paramD);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Exceptions_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
