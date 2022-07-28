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
/*    */ public class AnnotationDefault_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final Annotation.element_value default_value;
/*    */
/*    */   AnnotationDefault_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, Annotation.InvalidAnnotation {
/* 41 */     super(paramInt1, paramInt2);
/* 42 */     this.default_value = Annotation.element_value.read(paramClassReader);
/*    */   }
/*    */
/*    */
/*    */   public AnnotationDefault_attribute(ConstantPool paramConstantPool, Annotation.element_value paramelement_value) throws ConstantPoolException {
/* 47 */     this(paramConstantPool.getUTF8Index("AnnotationDefault"), paramelement_value);
/*    */   }
/*    */
/*    */   public AnnotationDefault_attribute(int paramInt, Annotation.element_value paramelement_value) {
/* 51 */     super(paramInt, paramelement_value.length());
/* 52 */     this.default_value = paramelement_value;
/*    */   }
/*    */
/*    */   public <R, D> R accept(Visitor<R, D> paramVisitor, D paramD) {
/* 56 */     return paramVisitor.visitAnnotationDefault(this, paramD);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\AnnotationDefault_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
