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
/*    */ public class RuntimeVisibleParameterAnnotations_attribute
/*    */   extends RuntimeParameterAnnotations_attribute
/*    */ {
/*    */   RuntimeVisibleParameterAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, Annotation.InvalidAnnotation {
/* 41 */     super(paramClassReader, paramInt1, paramInt2);
/*    */   }
/*    */
/*    */
/*    */   public RuntimeVisibleParameterAnnotations_attribute(ConstantPool paramConstantPool, Annotation[][] paramArrayOfAnnotation) throws ConstantPoolException {
/* 46 */     this(paramConstantPool.getUTF8Index("RuntimeVisibleParameterAnnotations"), paramArrayOfAnnotation);
/*    */   }
/*    */
/*    */   public RuntimeVisibleParameterAnnotations_attribute(int paramInt, Annotation[][] paramArrayOfAnnotation) {
/* 50 */     super(paramInt, paramArrayOfAnnotation);
/*    */   }
/*    */
/*    */   public <R, P> R accept(Visitor<R, P> paramVisitor, P paramP) {
/* 54 */     return paramVisitor.visitRuntimeVisibleParameterAnnotations(this, paramP);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\RuntimeVisibleParameterAnnotations_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
