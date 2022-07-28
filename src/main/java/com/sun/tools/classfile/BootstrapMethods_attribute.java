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
/*    */
/*    */ public class BootstrapMethods_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final BootstrapMethodSpecifier[] bootstrap_method_specifiers;
/*    */
/*    */   BootstrapMethods_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, AttributeException {
/* 44 */     super(paramInt1, paramInt2);
/* 45 */     int i = paramClassReader.readUnsignedShort();
/* 46 */     this.bootstrap_method_specifiers = new BootstrapMethodSpecifier[i];
/* 47 */     for (byte b = 0; b < this.bootstrap_method_specifiers.length; b++)
/* 48 */       this.bootstrap_method_specifiers[b] = new BootstrapMethodSpecifier(paramClassReader);
/*    */   }
/*    */
/*    */   public BootstrapMethods_attribute(int paramInt, BootstrapMethodSpecifier[] paramArrayOfBootstrapMethodSpecifier) {
/* 52 */     super(paramInt, length(paramArrayOfBootstrapMethodSpecifier));
/* 53 */     this.bootstrap_method_specifiers = paramArrayOfBootstrapMethodSpecifier;
/*    */   }
/*    */
/*    */   public static int length(BootstrapMethodSpecifier[] paramArrayOfBootstrapMethodSpecifier) {
/* 57 */     int i = 2;
/* 58 */     for (BootstrapMethodSpecifier bootstrapMethodSpecifier : paramArrayOfBootstrapMethodSpecifier)
/* 59 */       i += bootstrapMethodSpecifier.length();
/* 60 */     return i;
/*    */   }
/*    */
/*    */
/*    */   public <R, P> R accept(Visitor<R, P> paramVisitor, P paramP) {
/* 65 */     return paramVisitor.visitBootstrapMethods(this, paramP);
/*    */   }
/*    */
/*    */   public static class BootstrapMethodSpecifier {
/*    */     public int bootstrap_method_ref;
/*    */     public int[] bootstrap_arguments;
/*    */
/*    */     public BootstrapMethodSpecifier(int param1Int, int[] param1ArrayOfint) {
/* 73 */       this.bootstrap_method_ref = param1Int;
/* 74 */       this.bootstrap_arguments = param1ArrayOfint;
/*    */     }
/*    */     BootstrapMethodSpecifier(ClassReader param1ClassReader) throws IOException {
/* 77 */       this.bootstrap_method_ref = param1ClassReader.readUnsignedShort();
/* 78 */       int i = param1ClassReader.readUnsignedShort();
/* 79 */       this.bootstrap_arguments = new int[i];
/* 80 */       for (byte b = 0; b < this.bootstrap_arguments.length; b++) {
/* 81 */         this.bootstrap_arguments[b] = param1ClassReader.readUnsignedShort();
/*    */       }
/*    */     }
/*    */
/*    */
/*    */     int length() {
/* 87 */       return 4 + this.bootstrap_arguments.length * 2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\BootstrapMethods_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
