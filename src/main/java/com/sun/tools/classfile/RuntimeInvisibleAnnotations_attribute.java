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
/*    */ public class RuntimeInvisibleAnnotations_attribute
/*    */   extends RuntimeAnnotations_attribute
/*    */ {
/*    */   RuntimeInvisibleAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, AttributeException {
/* 41 */     super(paramClassReader, paramInt1, paramInt2);
/*    */   }
/*    */
/*    */
/*    */   public RuntimeInvisibleAnnotations_attribute(ConstantPool paramConstantPool, Annotation[] paramArrayOfAnnotation) throws ConstantPoolException {
/* 46 */     this(paramConstantPool.getUTF8Index("RuntimeInvisibleAnnotations"), paramArrayOfAnnotation);
/*    */   }
/*    */
/*    */   public RuntimeInvisibleAnnotations_attribute(int paramInt, Annotation[] paramArrayOfAnnotation) {
/* 50 */     super(paramInt, paramArrayOfAnnotation);
/*    */   }
/*    */
/*    */   public <R, P> R accept(Visitor<R, P> paramVisitor, P paramP) {
/* 54 */     return paramVisitor.visitRuntimeInvisibleAnnotations(this, paramP);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\RuntimeInvisibleAnnotations_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */