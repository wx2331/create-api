/*     */ package com.sun.tools.classfile;
/*     */
/*     */ import java.io.IOException;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class StackMapTable_attribute
/*     */   extends Attribute
/*     */ {
/*     */   public final int number_of_entries;
/*     */   public final stack_map_frame[] entries;
/*     */
/*     */   static class InvalidStackMap
/*     */     extends AttributeException
/*     */   {
/*     */     private static final long serialVersionUID = -5659038410855089780L;
/*     */
/*     */     InvalidStackMap(String param1String) {
/*  42 */       super(param1String);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   StackMapTable_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2) throws IOException, InvalidStackMap {
/*  48 */     super(paramInt1, paramInt2);
/*  49 */     this.number_of_entries = paramClassReader.readUnsignedShort();
/*  50 */     this.entries = new stack_map_frame[this.number_of_entries];
/*  51 */     for (byte b = 0; b < this.number_of_entries; b++) {
/*  52 */       this.entries[b] = stack_map_frame.read(paramClassReader);
/*     */     }
/*     */   }
/*     */
/*     */   public StackMapTable_attribute(ConstantPool paramConstantPool, stack_map_frame[] paramArrayOfstack_map_frame) throws ConstantPoolException {
/*  57 */     this(paramConstantPool.getUTF8Index("StackMapTable"), paramArrayOfstack_map_frame);
/*     */   }
/*     */
/*     */   public StackMapTable_attribute(int paramInt, stack_map_frame[] paramArrayOfstack_map_frame) {
/*  61 */     super(paramInt, length(paramArrayOfstack_map_frame));
/*  62 */     this.number_of_entries = paramArrayOfstack_map_frame.length;
/*  63 */     this.entries = paramArrayOfstack_map_frame;
/*     */   }
/*     */
/*     */   public <R, D> R accept(Visitor<R, D> paramVisitor, D paramD) {
/*  67 */     return paramVisitor.visitStackMapTable(this, paramD);
/*     */   }
/*     */
/*     */   static int length(stack_map_frame[] paramArrayOfstack_map_frame) {
/*  71 */     int i = 2;
/*  72 */     for (stack_map_frame stack_map_frame1 : paramArrayOfstack_map_frame)
/*  73 */       i += stack_map_frame1.length();
/*  74 */     return i;
/*     */   }
/*     */
/*     */
/*     */   public static abstract class stack_map_frame
/*     */   {
/*     */     public final int frame_type;
/*     */
/*     */     static stack_map_frame read(ClassReader param1ClassReader) throws IOException, InvalidStackMap {
/*  83 */       int i = param1ClassReader.readUnsignedByte();
/*  84 */       if (i <= 63)
/*  85 */         return new same_frame(i);
/*  86 */       if (i <= 127)
/*  87 */         return new same_locals_1_stack_item_frame(i, param1ClassReader);
/*  88 */       if (i <= 246)
/*  89 */         throw new Error("unknown frame_type " + i);
/*  90 */       if (i == 247)
/*  91 */         return new same_locals_1_stack_item_frame_extended(i, param1ClassReader);
/*  92 */       if (i <= 250)
/*  93 */         return new chop_frame(i, param1ClassReader);
/*  94 */       if (i == 251)
/*  95 */         return new same_frame_extended(i, param1ClassReader);
/*  96 */       if (i <= 254) {
/*  97 */         return new append_frame(i, param1ClassReader);
/*     */       }
/*  99 */       return new full_frame(i, param1ClassReader);
/*     */     }
/*     */
/*     */     protected stack_map_frame(int param1Int) {
/* 103 */       this.frame_type = param1Int;
/*     */     } public abstract int getOffsetDelta();
/*     */     public abstract <R, D> R accept(Visitor<R, D> param1Visitor, D param1D);
/*     */     public int length() {
/* 107 */       return 1;
/*     */     }
/*     */
/*     */     public static interface Visitor<R, P> {
/*     */       R visit_same_frame(same_frame param2same_frame, P param2P);
/*     */
/*     */       R visit_same_locals_1_stack_item_frame(same_locals_1_stack_item_frame param2same_locals_1_stack_item_frame, P param2P);
/*     */
/*     */       R visit_same_locals_1_stack_item_frame_extended(same_locals_1_stack_item_frame_extended param2same_locals_1_stack_item_frame_extended, P param2P);
/*     */
/*     */       R visit_chop_frame(chop_frame param2chop_frame, P param2P);
/*     */
/*     */       R visit_same_frame_extended(same_frame_extended param2same_frame_extended, P param2P);
/*     */
/*     */       R visit_append_frame(append_frame param2append_frame, P param2P);
/*     */
/*     */       R visit_full_frame(full_frame param2full_frame, P param2P);
/*     */     }
/*     */   }
/*     */
/*     */   public static class same_frame extends stack_map_frame {
/*     */     same_frame(int param1Int) {
/* 129 */       super(param1Int);
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 133 */       return param1Visitor.visit_same_frame(this, param1D);
/*     */     }
/*     */
/*     */     public int getOffsetDelta() {
/* 137 */       return this.frame_type;
/*     */     } }
/*     */
/*     */   public static class same_locals_1_stack_item_frame extends stack_map_frame {
/*     */     public final verification_type_info[] stack;
/*     */
/*     */     same_locals_1_stack_item_frame(int param1Int, ClassReader param1ClassReader) throws IOException, InvalidStackMap {
/* 144 */       super(param1Int);
/* 145 */       this.stack = new verification_type_info[1];
/* 146 */       this.stack[0] = verification_type_info.read(param1ClassReader);
/*     */     }
/*     */
/*     */
/*     */     public int length() {
/* 151 */       return super.length() + this.stack[0].length();
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 155 */       return param1Visitor.visit_same_locals_1_stack_item_frame(this, param1D);
/*     */     }
/*     */
/*     */     public int getOffsetDelta() {
/* 159 */       return this.frame_type - 64;
/*     */     }
/*     */   }
/*     */
/*     */   public static class same_locals_1_stack_item_frame_extended extends stack_map_frame {
/*     */     public final int offset_delta;
/*     */     public final verification_type_info[] stack;
/*     */
/*     */     same_locals_1_stack_item_frame_extended(int param1Int, ClassReader param1ClassReader) throws IOException, InvalidStackMap {
/* 168 */       super(param1Int);
/* 169 */       this.offset_delta = param1ClassReader.readUnsignedShort();
/* 170 */       this.stack = new verification_type_info[1];
/* 171 */       this.stack[0] = verification_type_info.read(param1ClassReader);
/*     */     }
/*     */
/*     */
/*     */     public int length() {
/* 176 */       return super.length() + 2 + this.stack[0].length();
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 180 */       return param1Visitor.visit_same_locals_1_stack_item_frame_extended(this, param1D);
/*     */     }
/*     */
/*     */     public int getOffsetDelta() {
/* 184 */       return this.offset_delta;
/*     */     }
/*     */   }
/*     */
/*     */   public static class chop_frame
/*     */     extends stack_map_frame {
/*     */     public final int offset_delta;
/*     */
/*     */     chop_frame(int param1Int, ClassReader param1ClassReader) throws IOException {
/* 193 */       super(param1Int);
/* 194 */       this.offset_delta = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */
/*     */     public int length() {
/* 199 */       return super.length() + 2;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 203 */       return param1Visitor.visit_chop_frame(this, param1D);
/*     */     }
/*     */
/*     */     public int getOffsetDelta() {
/* 207 */       return this.offset_delta;
/*     */     }
/*     */   }
/*     */
/*     */   public static class same_frame_extended extends stack_map_frame {
/*     */     public final int offset_delta;
/*     */
/*     */     same_frame_extended(int param1Int, ClassReader param1ClassReader) throws IOException {
/* 215 */       super(param1Int);
/* 216 */       this.offset_delta = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */
/*     */     public int length() {
/* 221 */       return super.length() + 2;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 225 */       return param1Visitor.visit_same_frame_extended(this, param1D);
/*     */     }
/*     */
/*     */     public int getOffsetDelta() {
/* 229 */       return this.offset_delta;
/*     */     }
/*     */   }
/*     */
/*     */   public static class append_frame extends stack_map_frame {
/*     */     public final int offset_delta;
/*     */     public final verification_type_info[] locals;
/*     */
/*     */     append_frame(int param1Int, ClassReader param1ClassReader) throws IOException, InvalidStackMap {
/* 238 */       super(param1Int);
/* 239 */       this.offset_delta = param1ClassReader.readUnsignedShort();
/* 240 */       this.locals = new verification_type_info[param1Int - 251];
/* 241 */       for (byte b = 0; b < this.locals.length; b++) {
/* 242 */         this.locals[b] = verification_type_info.read(param1ClassReader);
/*     */       }
/*     */     }
/*     */
/*     */     public int length() {
/* 247 */       int i = super.length() + 2;
/* 248 */       for (verification_type_info verification_type_info1 : this.locals)
/* 249 */         i += verification_type_info1.length();
/* 250 */       return i;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 254 */       return param1Visitor.visit_append_frame(this, param1D);
/*     */     }
/*     */
/*     */     public int getOffsetDelta() {
/* 258 */       return this.offset_delta;
/*     */     } }
/*     */
/*     */   public static class full_frame extends stack_map_frame { public final int offset_delta;
/*     */     public final int number_of_locals;
/*     */     public final verification_type_info[] locals;
/*     */     public final int number_of_stack_items;
/*     */     public final verification_type_info[] stack;
/*     */
/*     */     full_frame(int param1Int, ClassReader param1ClassReader) throws IOException, InvalidStackMap {
/* 268 */       super(param1Int);
/* 269 */       this.offset_delta = param1ClassReader.readUnsignedShort();
/* 270 */       this.number_of_locals = param1ClassReader.readUnsignedShort();
/* 271 */       this.locals = new verification_type_info[this.number_of_locals]; byte b;
/* 272 */       for (b = 0; b < this.locals.length; b++)
/* 273 */         this.locals[b] = verification_type_info.read(param1ClassReader);
/* 274 */       this.number_of_stack_items = param1ClassReader.readUnsignedShort();
/* 275 */       this.stack = new verification_type_info[this.number_of_stack_items];
/* 276 */       for (b = 0; b < this.stack.length; b++) {
/* 277 */         this.stack[b] = verification_type_info.read(param1ClassReader);
/*     */       }
/*     */     }
/*     */
/*     */     public int length() {
/* 282 */       int i = super.length() + 2;
/* 283 */       for (verification_type_info verification_type_info1 : this.locals)
/* 284 */         i += verification_type_info1.length();
/* 285 */       i += 2;
/* 286 */       for (verification_type_info verification_type_info1 : this.stack)
/* 287 */         i += verification_type_info1.length();
/* 288 */       return i;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 292 */       return param1Visitor.visit_full_frame(this, param1D);
/*     */     }
/*     */
/*     */     public int getOffsetDelta() {
/* 296 */       return this.offset_delta;
/*     */     } }
/*     */
/*     */
/*     */
/*     */   public static class verification_type_info
/*     */   {
/*     */     public static final int ITEM_Top = 0;
/*     */
/*     */     public static final int ITEM_Integer = 1;
/*     */
/*     */     public static final int ITEM_Float = 2;
/*     */
/*     */     public static final int ITEM_Long = 4;
/*     */
/*     */     public static final int ITEM_Double = 3;
/*     */     public static final int ITEM_Null = 5;
/*     */     public static final int ITEM_UninitializedThis = 6;
/*     */     public static final int ITEM_Object = 7;
/*     */     public static final int ITEM_Uninitialized = 8;
/*     */     public final int tag;
/*     */
/*     */     static verification_type_info read(ClassReader param1ClassReader) throws IOException, InvalidStackMap {
/* 319 */       int i = param1ClassReader.readUnsignedByte();
/* 320 */       switch (i) {
/*     */         case 0:
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/*     */         case 5:
/*     */         case 6:
/* 328 */           return new verification_type_info(i);
/*     */
/*     */         case 7:
/* 331 */           return new Object_variable_info(param1ClassReader);
/*     */
/*     */         case 8:
/* 334 */           return new Uninitialized_variable_info(param1ClassReader);
/*     */       }
/*     */
/* 337 */       throw new InvalidStackMap("unrecognized verification_type_info tag");
/*     */     }
/*     */
/*     */
/*     */     protected verification_type_info(int param1Int) {
/* 342 */       this.tag = param1Int;
/*     */     }
/*     */
/*     */     public int length() {
/* 346 */       return 1;
/*     */     }
/*     */   }
/*     */
/*     */   public static class Object_variable_info extends verification_type_info {
/*     */     public final int cpool_index;
/*     */
/*     */     Object_variable_info(ClassReader param1ClassReader) throws IOException {
/* 354 */       super(7);
/* 355 */       this.cpool_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */
/*     */     public int length() {
/* 360 */       return super.length() + 2;
/*     */     }
/*     */   }
/*     */
/*     */   public static class Uninitialized_variable_info extends verification_type_info {
/*     */     public final int offset;
/*     */
/*     */     Uninitialized_variable_info(ClassReader param1ClassReader) throws IOException {
/* 368 */       super(8);
/* 369 */       this.offset = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */
/*     */     public int length() {
/* 374 */       return super.length() + 2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\StackMapTable_attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
