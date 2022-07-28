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
/*    */ public class CharacterRangeTable_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public static final int CRT_STATEMENT = 1;
/*    */   public static final int CRT_BLOCK = 2;
/*    */   public static final int CRT_ASSIGNMENT = 4;
/*    */   public static final int CRT_FLOW_CONTROLLER = 8;
/*    */   public static final int CRT_FLOW_TARGET = 16;
/*    */   public static final int CRT_INVOKE = 32;
/*    */   public static final int CRT_CREATE = 64;
/*    */   public static final int CRT_BRANCH_TRUE = 128;
/*    */   public static final int CRT_BRANCH_FALSE = 256;
/*    */   public final Entry[] character_range_table;
/*    */
/*    */   CharacterRangeTable_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException {
/* 48 */     super(paramInt1, paramInt2);
/* 49 */     int i = paramClassReader.readUnsignedShort();
/* 50 */     this.character_range_table = new Entry[i];
/* 51 */     for (byte b = 0; b < i; b++) {
/* 52 */       this.character_range_table[b] = new Entry(paramClassReader);
/*    */     }
/*    */   }
/*    */
/*    */   public CharacterRangeTable_attribute(ConstantPool paramConstantPool, Entry[] paramArrayOfEntry) throws ConstantPoolException {
/* 57 */     this(paramConstantPool.getUTF8Index("CharacterRangeTable"), paramArrayOfEntry);
/*    */   }
/*    */
/*    */   public CharacterRangeTable_attribute(int paramInt, Entry[] paramArrayOfEntry) {
/* 61 */     super(paramInt, 2 + paramArrayOfEntry.length * Entry.length());
/* 62 */     this.character_range_table = paramArrayOfEntry;
/*    */   }
/*    */
/*    */   public <R, D> R accept(Visitor<R, D> paramVisitor, D paramD) {
/* 66 */     return paramVisitor.visitCharacterRangeTable(this, paramD);
/*    */   }
/*    */
/*    */   public static class Entry { public final int start_pc;
/*    */     public final int end_pc;
/*    */
/*    */     Entry(ClassReader param1ClassReader) throws IOException {
/* 73 */       this.start_pc = param1ClassReader.readUnsignedShort();
/* 74 */       this.end_pc = param1ClassReader.readUnsignedShort();
/* 75 */       this.character_range_start = param1ClassReader.readInt();
/* 76 */       this.character_range_end = param1ClassReader.readInt();
/* 77 */       this.flags = param1ClassReader.readUnsignedShort();
/*    */     }
/*    */     public final int character_range_start; public final int character_range_end; public final int flags;
/*    */     public static int length() {
/* 81 */       return 14;
/*    */     } }
/*    */
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\CharacterRangeTable_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
