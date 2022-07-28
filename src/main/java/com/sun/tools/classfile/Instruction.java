/*     */ package com.sun.tools.classfile;
/*     */
/*     */ import java.util.Locale;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Instruction
/*     */ {
/*     */   private byte[] bytes;
/*     */   private int pc;
/*     */
/*     */   public enum Kind
/*     */   {
/*  45 */     NO_OPERANDS(1),
/*     */
/*  47 */     ATYPE(2),
/*     */
/*  49 */     BRANCH(3),
/*     */
/*  51 */     BRANCH_W(5),
/*     */
/*  53 */     BYTE(2),
/*     */
/*  55 */     CPREF(2),
/*     */
/*  57 */     CPREF_W(3),
/*     */
/*     */
/*  60 */     CPREF_W_UBYTE(4),
/*     */
/*     */
/*  63 */     CPREF_W_UBYTE_ZERO(5),
/*     */
/*     */
/*  66 */     DYNAMIC(-1),
/*     */
/*  68 */     LOCAL(2),
/*     */
/*     */
/*  71 */     LOCAL_BYTE(3),
/*     */
/*  73 */     SHORT(3),
/*     */
/*  75 */     WIDE_NO_OPERANDS(2),
/*     */
/*  77 */     WIDE_LOCAL(4),
/*     */
/*  79 */     WIDE_CPREF_W(4),
/*     */
/*     */
/*  82 */     WIDE_CPREF_W_SHORT(6),
/*     */
/*     */
/*  85 */     WIDE_LOCAL_SHORT(6),
/*     */
/*  87 */     UNKNOWN(1); public final int length;
/*     */
/*     */     Kind(int param1Int1) {
/*  90 */       this.length = param1Int1;
/*     */     }
/*     */   }
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public enum TypeKind
/*     */   {
/* 129 */     T_BOOLEAN(4, "boolean"),
/* 130 */     T_CHAR(5, "char"),
/* 131 */     T_FLOAT(6, "float"),
/* 132 */     T_DOUBLE(7, "double"),
/* 133 */     T_BYTE(8, "byte"),
/* 134 */     T_SHORT(9, "short"),
/* 135 */     T_INT(10, "int"),
/* 136 */     T_LONG(11, "long"); public final int value; public final String name;
/*     */     TypeKind(int param1Int1, String param1String1) {
/* 138 */       this.value = param1Int1;
/* 139 */       this.name = param1String1;
/*     */     }
/*     */
/*     */     public static TypeKind get(int param1Int) {
/* 143 */       switch (param1Int) { case 4:
/* 144 */           return T_BOOLEAN;
/* 145 */         case 5: return T_CHAR;
/* 146 */         case 6: return T_FLOAT;
/* 147 */         case 7: return T_DOUBLE;
/* 148 */         case 8: return T_BYTE;
/* 149 */         case 9: return T_SHORT;
/* 150 */         case 10: return T_INT;
/* 151 */         case 11: return T_LONG; }
/* 152 */        return null;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Instruction(byte[] paramArrayOfbyte, int paramInt) {
/* 162 */     this.bytes = paramArrayOfbyte;
/* 163 */     this.pc = paramInt;
/*     */   }
/*     */
/*     */
/*     */   public int getPC() {
/* 168 */     return this.pc;
/*     */   }
/*     */
/*     */
/*     */   public int getByte(int paramInt) {
/* 173 */     return this.bytes[this.pc + paramInt];
/*     */   }
/*     */
/*     */
/*     */   public int getUnsignedByte(int paramInt) {
/* 178 */     return getByte(paramInt) & 0xFF;
/*     */   }
/*     */
/*     */
/*     */   public int getShort(int paramInt) {
/* 183 */     return getByte(paramInt) << 8 | getUnsignedByte(paramInt + 1);
/*     */   }
/*     */
/*     */
/*     */   public int getUnsignedShort(int paramInt) {
/* 188 */     return getShort(paramInt) & 0xFFFF;
/*     */   }
/*     */
/*     */
/*     */   public int getInt(int paramInt) {
/* 193 */     return getShort(paramInt) << 16 | getUnsignedShort(paramInt + 2);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public Opcode getOpcode() {
/* 199 */     int i = getUnsignedByte(0);
/* 200 */     switch (i) {
/*     */       case 196:
/*     */       case 254:
/*     */       case 255:
/* 204 */         return Opcode.get(i, getUnsignedByte(1));
/*     */     }
/* 206 */     return Opcode.get(i);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public String getMnemonic() {
/* 212 */     Opcode opcode = getOpcode();
/* 213 */     if (opcode == null) {
/* 214 */       return "bytecode " + getUnsignedByte(0);
/*     */     }
/* 216 */     return opcode.toString().toLowerCase(Locale.US);
/*     */   }
/*     */
/*     */
/*     */   public int length() {
/*     */     int i, j, k;
/* 222 */     Opcode opcode = getOpcode();
/* 223 */     if (opcode == null) {
/* 224 */       return 1;
/*     */     }
/* 226 */     switch (opcode) {
/*     */       case NO_OPERANDS:
/* 228 */         i = align(this.pc + 1) - this.pc;
/* 229 */         j = getInt(i + 4);
/* 230 */         k = getInt(i + 8);
/* 231 */         return i + 12 + 4 * (k - j + 1);
/*     */
/*     */       case ATYPE:
/* 234 */         i = align(this.pc + 1) - this.pc;
/* 235 */         j = getInt(i + 4);
/* 236 */         return i + 8 + 8 * j;
/*     */     }
/*     */
/*     */
/* 240 */     return opcode.kind.length;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public Kind getKind() {
/* 246 */     Opcode opcode = getOpcode();
/* 247 */     return (opcode != null) ? opcode.kind : Kind.UNKNOWN; } public <R, P> R accept(KindVisitor<R, P> paramKindVisitor, P paramP) { int i; int j;
/*     */     int k;
/*     */     int m;
/*     */     int[] arrayOfInt1;
/*     */     int[] arrayOfInt2;
/*     */     byte b;
/* 253 */     switch (getKind()) {
/*     */       case NO_OPERANDS:
/* 255 */         return paramKindVisitor.visitNoOperands(this, paramP);
/*     */
/*     */       case ATYPE:
/* 258 */         return paramKindVisitor.visitArrayType(this,
/* 259 */             TypeKind.get(getUnsignedByte(1)), paramP);
/*     */
/*     */       case BRANCH:
/* 262 */         return paramKindVisitor.visitBranch(this, getShort(1), paramP);
/*     */
/*     */       case BRANCH_W:
/* 265 */         return paramKindVisitor.visitBranch(this, getInt(1), paramP);
/*     */
/*     */       case BYTE:
/* 268 */         return paramKindVisitor.visitValue(this, getByte(1), paramP);
/*     */
/*     */       case CPREF:
/* 271 */         return paramKindVisitor.visitConstantPoolRef(this, getUnsignedByte(1), paramP);
/*     */
/*     */       case CPREF_W:
/* 274 */         return paramKindVisitor.visitConstantPoolRef(this, getUnsignedShort(1), paramP);
/*     */
/*     */       case CPREF_W_UBYTE:
/*     */       case CPREF_W_UBYTE_ZERO:
/* 278 */         return paramKindVisitor.visitConstantPoolRefAndValue(this,
/* 279 */             getUnsignedShort(1), getUnsignedByte(3), paramP);
/*     */
/*     */       case DYNAMIC:
/* 282 */         switch (getOpcode()) {
/*     */           case NO_OPERANDS:
/* 284 */             i = align(this.pc + 1) - this.pc;
/* 285 */             j = getInt(i);
/* 286 */             k = getInt(i + 4);
/* 287 */             m = getInt(i + 8);
/* 288 */             arrayOfInt2 = new int[m - k + 1];
/* 289 */             for (b = 0; b < arrayOfInt2.length; b++)
/* 290 */               arrayOfInt2[b] = getInt(i + 12 + 4 * b);
/* 291 */             return paramKindVisitor.visitTableSwitch(this, j, k, m, arrayOfInt2, paramP);
/*     */
/*     */
/*     */           case ATYPE:
/* 295 */             i = align(this.pc + 1) - this.pc;
/* 296 */             j = getInt(i);
/* 297 */             k = getInt(i + 4);
/* 298 */             arrayOfInt1 = new int[k];
/* 299 */             arrayOfInt2 = new int[k];
/* 300 */             for (b = 0; b < k; b++) {
/* 301 */               arrayOfInt1[b] = getInt(i + 8 + b * 8);
/* 302 */               arrayOfInt2[b] = getInt(i + 12 + b * 8);
/*     */             }
/* 304 */             return paramKindVisitor.visitLookupSwitch(this, j, k, arrayOfInt1, arrayOfInt2, paramP);
/*     */         }
/*     */
/*     */
/* 308 */         throw new IllegalStateException();
/*     */
/*     */
/*     */
/*     */       case LOCAL:
/* 313 */         return paramKindVisitor.visitLocal(this, getUnsignedByte(1), paramP);
/*     */
/*     */       case LOCAL_BYTE:
/* 316 */         return paramKindVisitor.visitLocalAndValue(this,
/* 317 */             getUnsignedByte(1), getByte(2), paramP);
/*     */
/*     */       case SHORT:
/* 320 */         return paramKindVisitor.visitValue(this, getShort(1), paramP);
/*     */
/*     */       case WIDE_NO_OPERANDS:
/* 323 */         return paramKindVisitor.visitNoOperands(this, paramP);
/*     */
/*     */       case WIDE_LOCAL:
/* 326 */         return paramKindVisitor.visitLocal(this, getUnsignedShort(2), paramP);
/*     */
/*     */       case WIDE_CPREF_W:
/* 329 */         return paramKindVisitor.visitConstantPoolRef(this, getUnsignedShort(2), paramP);
/*     */
/*     */       case WIDE_CPREF_W_SHORT:
/* 332 */         return paramKindVisitor.visitConstantPoolRefAndValue(this,
/* 333 */             getUnsignedShort(2), getUnsignedByte(4), paramP);
/*     */
/*     */       case WIDE_LOCAL_SHORT:
/* 336 */         return paramKindVisitor.visitLocalAndValue(this,
/* 337 */             getUnsignedShort(2), getShort(4), paramP);
/*     */
/*     */       case UNKNOWN:
/* 340 */         return paramKindVisitor.visitUnknown(this, paramP);
/*     */     }
/*     */
/* 343 */     throw new IllegalStateException(); }
/*     */
/*     */
/*     */
/*     */   private static int align(int paramInt) {
/* 348 */     return paramInt + 3 & 0xFFFFFFFC;
/*     */   }
/*     */
/*     */   public static interface KindVisitor<R, P> {
/*     */     R visitNoOperands(Instruction param1Instruction, P param1P);
/*     */
/*     */     R visitArrayType(Instruction param1Instruction, TypeKind param1TypeKind, P param1P);
/*     */
/*     */     R visitBranch(Instruction param1Instruction, int param1Int, P param1P);
/*     */
/*     */     R visitConstantPoolRef(Instruction param1Instruction, int param1Int, P param1P);
/*     */
/*     */     R visitConstantPoolRefAndValue(Instruction param1Instruction, int param1Int1, int param1Int2, P param1P);
/*     */
/*     */     R visitLocal(Instruction param1Instruction, int param1Int, P param1P);
/*     */
/*     */     R visitLocalAndValue(Instruction param1Instruction, int param1Int1, int param1Int2, P param1P);
/*     */
/*     */     R visitLookupSwitch(Instruction param1Instruction, int param1Int1, int param1Int2, int[] param1ArrayOfint1, int[] param1ArrayOfint2, P param1P);
/*     */
/*     */     R visitTableSwitch(Instruction param1Instruction, int param1Int1, int param1Int2, int param1Int3, int[] param1ArrayOfint, P param1P);
/*     */
/*     */     R visitValue(Instruction param1Instruction, int param1Int, P param1P);
/*     */
/*     */     R visitUnknown(Instruction param1Instruction, P param1P);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Instruction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
