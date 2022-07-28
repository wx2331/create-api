/*    */ package com.sun.tools.classfile;
/*    */
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.Charset;
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
/*    */ public class SourceDebugExtension_attribute
/*    */   extends Attribute
/*    */ {
/* 42 */   private static final Charset UTF8 = Charset.forName("UTF-8");
/*    */
/*    */   SourceDebugExtension_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException {
/* 45 */     super(paramInt1, paramInt2);
/* 46 */     this.debug_extension = new byte[this.attribute_length];
/* 47 */     paramClassReader.readFully(this.debug_extension);
/*    */   }
/*    */   public final byte[] debug_extension;
/*    */
/*    */   public SourceDebugExtension_attribute(ConstantPool paramConstantPool, byte[] paramArrayOfbyte) throws ConstantPoolException {
/* 52 */     this(paramConstantPool.getUTF8Index("SourceDebugExtension"), paramArrayOfbyte);
/*    */   }
/*    */
/*    */   public SourceDebugExtension_attribute(int paramInt, byte[] paramArrayOfbyte) {
/* 56 */     super(paramInt, paramArrayOfbyte.length);
/* 57 */     this.debug_extension = paramArrayOfbyte;
/*    */   }
/*    */
/*    */   public String getValue() {
/* 61 */     return new String(this.debug_extension, UTF8);
/*    */   }
/*    */
/*    */   public <R, D> R accept(Visitor<R, D> paramVisitor, D paramD) {
/* 65 */     return paramVisitor.visitSourceDebugExtension(this, paramD);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\SourceDebugExtension_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
