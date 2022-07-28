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
/*    */ public class Method
/*    */ {
/*    */   public final AccessFlags access_flags;
/*    */   public final int name_index;
/*    */   public final Descriptor descriptor;
/*    */   public final Attributes attributes;
/*    */   
/*    */   Method(ClassReader paramClassReader) throws IOException {
/* 38 */     this.access_flags = new AccessFlags(paramClassReader);
/* 39 */     this.name_index = paramClassReader.readUnsignedShort();
/* 40 */     this.descriptor = new Descriptor(paramClassReader);
/* 41 */     this.attributes = new Attributes(paramClassReader);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Method(AccessFlags paramAccessFlags, int paramInt, Descriptor paramDescriptor, Attributes paramAttributes) {
/* 47 */     this.access_flags = paramAccessFlags;
/* 48 */     this.name_index = paramInt;
/* 49 */     this.descriptor = paramDescriptor;
/* 50 */     this.attributes = paramAttributes;
/*    */   }
/*    */   
/*    */   public int byteLength() {
/* 54 */     return 6 + this.attributes.byteLength();
/*    */   }
/*    */   
/*    */   public String getName(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 58 */     return paramConstantPool.getUTF8Value(this.name_index);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Method.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */