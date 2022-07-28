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
/*    */
/*    */
/*    */
/*    */ public class RuntimeVisibleTypeAnnotations_attribute
/*    */   extends RuntimeTypeAnnotations_attribute
/*    */ {
/*    */   RuntimeVisibleTypeAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, Annotation.InvalidAnnotation {
/* 41 */     super(paramClassReader, paramInt1, paramInt2);
/*    */   }
/*    */
/*    */
/*    */   public RuntimeVisibleTypeAnnotations_attribute(ConstantPool paramConstantPool, TypeAnnotation[] paramArrayOfTypeAnnotation) throws ConstantPoolException {
/* 46 */     this(paramConstantPool.getUTF8Index("RuntimeVisibleTypeAnnotations"), paramArrayOfTypeAnnotation);
/*    */   }
/*    */
/*    */   public RuntimeVisibleTypeAnnotations_attribute(int paramInt, TypeAnnotation[] paramArrayOfTypeAnnotation) {
/* 50 */     super(paramInt, paramArrayOfTypeAnnotation);
/*    */   }
/*    */
/*    */   public <R, P> R accept(Visitor<R, P> paramVisitor, P paramP) {
/* 54 */     return paramVisitor.visitRuntimeVisibleTypeAnnotations(this, paramP);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\RuntimeVisibleTypeAnnotations_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
