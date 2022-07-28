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
/*    */ public class LocalVariableTypeTable_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final int local_variable_table_length;
/*    */   public final Entry[] local_variable_table;
/*    */
/*    */   LocalVariableTypeTable_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException {
/* 40 */     super(paramInt1, paramInt2);
/* 41 */     this.local_variable_table_length = paramClassReader.readUnsignedShort();
/* 42 */     this.local_variable_table = new Entry[this.local_variable_table_length];
/* 43 */     for (byte b = 0; b < this.local_variable_table_length; b++) {
/* 44 */       this.local_variable_table[b] = new Entry(paramClassReader);
/*    */     }
/*    */   }
/*    */
/*    */   public LocalVariableTypeTable_attribute(ConstantPool paramConstantPool, Entry[] paramArrayOfEntry) throws ConstantPoolException {
/* 49 */     this(paramConstantPool.getUTF8Index("LocalVariableTypeTable"), paramArrayOfEntry);
/*    */   }
/*    */
/*    */   public LocalVariableTypeTable_attribute(int paramInt, Entry[] paramArrayOfEntry) {
/* 53 */     super(paramInt, 2 + paramArrayOfEntry.length * Entry.length());
/* 54 */     this.local_variable_table_length = paramArrayOfEntry.length;
/* 55 */     this.local_variable_table = paramArrayOfEntry;
/*    */   }
/*    */
/*    */   public <R, D> R accept(Visitor<R, D> paramVisitor, D paramD) {
/* 59 */     return paramVisitor.visitLocalVariableTypeTable(this, paramD);
/*    */   }
/*    */
/*    */   public static class Entry {
/*    */     public final int start_pc;
/*    */     public final int length;
/*    */
/*    */     Entry(ClassReader param1ClassReader) throws IOException {
/* 67 */       this.start_pc = param1ClassReader.readUnsignedShort();
/* 68 */       this.length = param1ClassReader.readUnsignedShort();
/* 69 */       this.name_index = param1ClassReader.readUnsignedShort();
/* 70 */       this.signature_index = param1ClassReader.readUnsignedShort();
/* 71 */       this.index = param1ClassReader.readUnsignedShort();
/*    */     }
/*    */     public final int name_index; public final int signature_index; public final int index;
/*    */     public static int length() {
/* 75 */       return 10;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\LocalVariableTypeTable_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
