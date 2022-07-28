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
/*    */ public abstract class RuntimeTypeAnnotations_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final TypeAnnotation[] annotations;
/*    */   
/*    */   protected RuntimeTypeAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, Annotation.InvalidAnnotation {
/* 41 */     super(paramInt1, paramInt2);
/* 42 */     int i = paramClassReader.readUnsignedShort();
/* 43 */     this.annotations = new TypeAnnotation[i];
/* 44 */     for (byte b = 0; b < this.annotations.length; b++)
/* 45 */       this.annotations[b] = new TypeAnnotation(paramClassReader); 
/*    */   }
/*    */   
/*    */   protected RuntimeTypeAnnotations_attribute(int paramInt, TypeAnnotation[] paramArrayOfTypeAnnotation) {
/* 49 */     super(paramInt, length(paramArrayOfTypeAnnotation));
/* 50 */     this.annotations = paramArrayOfTypeAnnotation;
/*    */   }
/*    */   
/*    */   private static int length(TypeAnnotation[] paramArrayOfTypeAnnotation) {
/* 54 */     int i = 2;
/* 55 */     for (TypeAnnotation typeAnnotation : paramArrayOfTypeAnnotation)
/* 56 */       i += typeAnnotation.length(); 
/* 57 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\RuntimeTypeAnnotations_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */