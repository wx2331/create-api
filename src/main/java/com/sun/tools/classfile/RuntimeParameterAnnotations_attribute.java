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
/*    */ public abstract class RuntimeParameterAnnotations_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final Annotation[][] parameter_annotations;
/*    */   
/*    */   RuntimeParameterAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, Annotation.InvalidAnnotation {
/* 41 */     super(paramInt1, paramInt2);
/* 42 */     int i = paramClassReader.readUnsignedByte();
/* 43 */     this.parameter_annotations = new Annotation[i][];
/* 44 */     for (byte b = 0; b < this.parameter_annotations.length; b++) {
/* 45 */       int j = paramClassReader.readUnsignedShort();
/* 46 */       Annotation[] arrayOfAnnotation = new Annotation[j];
/* 47 */       for (byte b1 = 0; b1 < j; b1++)
/* 48 */         arrayOfAnnotation[b1] = new Annotation(paramClassReader); 
/* 49 */       this.parameter_annotations[b] = arrayOfAnnotation;
/*    */     } 
/*    */   }
/*    */   
/*    */   protected RuntimeParameterAnnotations_attribute(int paramInt, Annotation[][] paramArrayOfAnnotation) {
/* 54 */     super(paramInt, length(paramArrayOfAnnotation));
/* 55 */     this.parameter_annotations = paramArrayOfAnnotation;
/*    */   }
/*    */   
/*    */   private static int length(Annotation[][] paramArrayOfAnnotation) {
/* 59 */     int i = 1;
/* 60 */     for (Annotation[] arrayOfAnnotation : paramArrayOfAnnotation) {
/* 61 */       i += true;
/* 62 */       for (Annotation annotation : arrayOfAnnotation)
/* 63 */         i += annotation.length(); 
/*    */     } 
/* 65 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\RuntimeParameterAnnotations_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */