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
/*    */ public class InnerClasses_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final int number_of_classes;
/*    */   public final Info[] classes;
/*    */
/*    */   InnerClasses_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException {
/* 42 */     super(paramInt1, paramInt2);
/* 43 */     this.number_of_classes = paramClassReader.readUnsignedShort();
/* 44 */     this.classes = new Info[this.number_of_classes];
/* 45 */     for (byte b = 0; b < this.number_of_classes; b++) {
/* 46 */       this.classes[b] = new Info(paramClassReader);
/*    */     }
/*    */   }
/*    */
/*    */   public InnerClasses_attribute(ConstantPool paramConstantPool, Info[] paramArrayOfInfo) throws ConstantPoolException {
/* 51 */     this(paramConstantPool.getUTF8Index("InnerClasses"), paramArrayOfInfo);
/*    */   }
/*    */
/*    */   public InnerClasses_attribute(int paramInt, Info[] paramArrayOfInfo) {
/* 55 */     super(paramInt, 2 + Info.length() * paramArrayOfInfo.length);
/* 56 */     this.number_of_classes = paramArrayOfInfo.length;
/* 57 */     this.classes = paramArrayOfInfo;
/*    */   }
/*    */
/*    */   public <R, D> R accept(Visitor<R, D> paramVisitor, D paramD) {
/* 61 */     return paramVisitor.visitInnerClasses(this, paramD);
/*    */   }
/*    */
/*    */   public static class Info {
/*    */     public final int inner_class_info_index;
/*    */     public final int outer_class_info_index;
/*    */
/*    */     Info(ClassReader param1ClassReader) throws IOException {
/* 69 */       this.inner_class_info_index = param1ClassReader.readUnsignedShort();
/* 70 */       this.outer_class_info_index = param1ClassReader.readUnsignedShort();
/* 71 */       this.inner_name_index = param1ClassReader.readUnsignedShort();
/* 72 */       this.inner_class_access_flags = new AccessFlags(param1ClassReader.readUnsignedShort());
/*    */     }
/*    */     public final int inner_name_index; public final AccessFlags inner_class_access_flags;
/*    */     public ConstantPool.CONSTANT_Class_info getInnerClassInfo(ConstantPool param1ConstantPool) throws ConstantPoolException {
/* 76 */       if (this.inner_class_info_index == 0)
/* 77 */         return null;
/* 78 */       return param1ConstantPool.getClassInfo(this.inner_class_info_index);
/*    */     }
/*    */
/*    */     public ConstantPool.CONSTANT_Class_info getOuterClassInfo(ConstantPool param1ConstantPool) throws ConstantPoolException {
/* 82 */       if (this.outer_class_info_index == 0)
/* 83 */         return null;
/* 84 */       return param1ConstantPool.getClassInfo(this.outer_class_info_index);
/*    */     }
/*    */
/*    */     public String getInnerName(ConstantPool param1ConstantPool) throws ConstantPoolException {
/* 88 */       if (this.inner_name_index == 0)
/* 89 */         return null;
/* 90 */       return param1ConstantPool.getUTF8Value(this.inner_name_index);
/*    */     }
/*    */
/*    */     public static int length() {
/* 94 */       return 8;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\InnerClasses_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
