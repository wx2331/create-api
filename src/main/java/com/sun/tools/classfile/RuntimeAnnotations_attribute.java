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
/*    */ public abstract class RuntimeAnnotations_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final Annotation[] annotations;
/*    */   
/*    */   protected RuntimeAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, Annotation.InvalidAnnotation {
/* 41 */     super(paramInt1, paramInt2);
/* 42 */     int i = paramClassReader.readUnsignedShort();
/* 43 */     this.annotations = new Annotation[i];
/* 44 */     for (byte b = 0; b < this.annotations.length; b++)
/* 45 */       this.annotations[b] = new Annotation(paramClassReader); 
/*    */   }
/*    */   
/*    */   protected RuntimeAnnotations_attribute(int paramInt, Annotation[] paramArrayOfAnnotation) {
/* 49 */     super(paramInt, length(paramArrayOfAnnotation));
/* 50 */     this.annotations = paramArrayOfAnnotation;
/*    */   }
/*    */   
/*    */   private static int length(Annotation[] paramArrayOfAnnotation) {
/* 54 */     int i = 2;
/* 55 */     for (Annotation annotation : paramArrayOfAnnotation)
/* 56 */       i += annotation.length(); 
/* 57 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\RuntimeAnnotations_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */