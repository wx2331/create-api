/*    */ package com.sun.tools.classfile;
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
/*    */ public class DefaultAttribute
/*    */   extends Attribute
/*    */ {
/*    */   public final byte[] info;
/*    */   public final String reason;
/*    */
/*    */   DefaultAttribute(ClassReader paramClassReader, int paramInt, byte[] paramArrayOfbyte) {
/* 36 */     this(paramClassReader, paramInt, paramArrayOfbyte, (String)null);
/*    */   }
/*    */
/*    */   DefaultAttribute(ClassReader paramClassReader, int paramInt, byte[] paramArrayOfbyte, String paramString) {
/* 40 */     super(paramInt, paramArrayOfbyte.length);
/* 41 */     this.info = paramArrayOfbyte;
/* 42 */     this.reason = paramString;
/*    */   }
/*    */
/*    */   public DefaultAttribute(ConstantPool paramConstantPool, int paramInt, byte[] paramArrayOfbyte) {
/* 46 */     this(paramConstantPool, paramInt, paramArrayOfbyte, (String)null);
/*    */   }
/*    */
/*    */
/*    */   public DefaultAttribute(ConstantPool paramConstantPool, int paramInt, byte[] paramArrayOfbyte, String paramString) {
/* 51 */     super(paramInt, paramArrayOfbyte.length);
/* 52 */     this.info = paramArrayOfbyte;
/* 53 */     this.reason = paramString;
/*    */   }
/*    */
/*    */   public <R, P> R accept(Visitor<R, P> paramVisitor, P paramP) {
/* 57 */     return paramVisitor.visitDefault(this, paramP);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\DefaultAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
