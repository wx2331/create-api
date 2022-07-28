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
/*    */ public class StackMap_attribute
/*    */   extends Attribute
/*    */ {
/*    */   public final int number_of_entries;
/*    */   public final stack_map_frame[] entries;
/*    */
/*    */   StackMap_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, StackMapTable_attribute.InvalidStackMap {
/* 39 */     super(paramInt1, paramInt2);
/* 40 */     this.number_of_entries = paramClassReader.readUnsignedShort();
/* 41 */     this.entries = new stack_map_frame[this.number_of_entries];
/* 42 */     for (byte b = 0; b < this.number_of_entries; b++) {
/* 43 */       this.entries[b] = new stack_map_frame(paramClassReader);
/*    */     }
/*    */   }
/*    */
/*    */   public StackMap_attribute(ConstantPool paramConstantPool, stack_map_frame[] paramArrayOfstack_map_frame) throws ConstantPoolException {
/* 48 */     this(paramConstantPool.getUTF8Index("StackMap"), paramArrayOfstack_map_frame);
/*    */   }
/*    */
/*    */   public StackMap_attribute(int paramInt, stack_map_frame[] paramArrayOfstack_map_frame) {
/* 52 */     super(paramInt, StackMapTable_attribute.length((StackMapTable_attribute.stack_map_frame[])paramArrayOfstack_map_frame));
/* 53 */     this.number_of_entries = paramArrayOfstack_map_frame.length;
/* 54 */     this.entries = paramArrayOfstack_map_frame;
/*    */   }
/*    */
/*    */   public <R, D> R accept(Visitor<R, D> paramVisitor, D paramD) {
/* 58 */     return paramVisitor.visitStackMap(this, paramD);
/*    */   }
/*    */
/*    */
/*    */
/*    */   public static class stack_map_frame
/*    */     extends StackMapTable_attribute.full_frame
/*    */   {
/*    */     stack_map_frame(ClassReader param1ClassReader) throws IOException, StackMapTable_attribute.InvalidStackMap {
/* 67 */       super(255, param1ClassReader);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\StackMap_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
