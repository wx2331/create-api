/*     */ package com.sun.tools.classfile;
/*     */
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
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
/*     */ public class ConstantPool
/*     */ {
/*     */   public static final int CONSTANT_Utf8 = 1;
/*     */   public static final int CONSTANT_Integer = 3;
/*     */   public static final int CONSTANT_Float = 4;
/*     */   public static final int CONSTANT_Long = 5;
/*     */   public static final int CONSTANT_Double = 6;
/*     */   public static final int CONSTANT_Class = 7;
/*     */   public static final int CONSTANT_String = 8;
/*     */   public static final int CONSTANT_Fieldref = 9;
/*     */   public static final int CONSTANT_Methodref = 10;
/*     */   public static final int CONSTANT_InterfaceMethodref = 11;
/*     */   public static final int CONSTANT_NameAndType = 12;
/*     */   public static final int CONSTANT_MethodHandle = 15;
/*     */   public static final int CONSTANT_MethodType = 16;
/*     */   public static final int CONSTANT_InvokeDynamic = 18;
/*     */   private CPInfo[] pool;
/*     */
/*     */   public static class InvalidIndex
/*     */     extends ConstantPoolException
/*     */   {
/*     */     private static final long serialVersionUID = -4350294289300939730L;
/*     */
/*     */     InvalidIndex(int param1Int) {
/*  46 */       super(param1Int);
/*     */     }
/*     */
/*     */
/*     */
/*     */     public String getMessage() {
/*  52 */       return "invalid index #" + this.index;
/*     */     } }
/*     */
/*     */   public static class UnexpectedEntry extends ConstantPoolException {
/*     */     private static final long serialVersionUID = 6986335935377933211L;
/*     */
/*     */     UnexpectedEntry(int param1Int1, int param1Int2, int param1Int3) {
/*  59 */       super(param1Int1);
/*  60 */       this.expected_tag = param1Int2;
/*  61 */       this.found_tag = param1Int3;
/*     */     }
/*     */     public final int expected_tag;
/*     */     public final int found_tag;
/*     */
/*     */     public String getMessage() {
/*  67 */       return "unexpected entry at #" + this.index + " -- expected tag " + this.expected_tag + ", found " + this.found_tag;
/*     */     }
/*     */   }
/*     */
/*     */   public static class InvalidEntry
/*     */     extends ConstantPoolException {
/*     */     private static final long serialVersionUID = 1000087545585204447L;
/*     */     public final int tag;
/*     */
/*     */     InvalidEntry(int param1Int1, int param1Int2) {
/*  77 */       super(param1Int1);
/*  78 */       this.tag = param1Int2;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public String getMessage() {
/*  84 */       return "unexpected tag at #" + this.index + ": " + this.tag;
/*     */     }
/*     */   }
/*     */
/*     */   public static class EntryNotFound extends ConstantPoolException {
/*     */     private static final long serialVersionUID = 2885537606468581850L;
/*     */     public final Object value;
/*     */
/*     */     EntryNotFound(Object param1Object) {
/*  93 */       super(-1);
/*  94 */       this.value = param1Object;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public String getMessage() {
/* 100 */       return "value not found: " + this.value;
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
/*     */   public enum RefKind
/*     */   {
/* 122 */     REF_getField(1, "getfield"),
/* 123 */     REF_getStatic(2, "getstatic"),
/* 124 */     REF_putField(3, "putfield"),
/* 125 */     REF_putStatic(4, "putstatic"),
/* 126 */     REF_invokeVirtual(5, "invokevirtual"),
/* 127 */     REF_invokeStatic(6, "invokestatic"),
/* 128 */     REF_invokeSpecial(7, "invokespecial"),
/* 129 */     REF_newInvokeSpecial(8, "newinvokespecial"),
/* 130 */     REF_invokeInterface(9, "invokeinterface");
/*     */
/*     */     public final int tag;
/*     */     public final String name;
/*     */
/*     */     RefKind(int param1Int1, String param1String1) {
/* 136 */       this.tag = param1Int1;
/* 137 */       this.name = param1String1;
/*     */     }
/*     */
/*     */     static RefKind getRefkind(int param1Int) {
/* 141 */       switch (param1Int) {
/*     */         case 1:
/* 143 */           return REF_getField;
/*     */         case 2:
/* 145 */           return REF_getStatic;
/*     */         case 3:
/* 147 */           return REF_putField;
/*     */         case 4:
/* 149 */           return REF_putStatic;
/*     */         case 5:
/* 151 */           return REF_invokeVirtual;
/*     */         case 6:
/* 153 */           return REF_invokeStatic;
/*     */         case 7:
/* 155 */           return REF_invokeSpecial;
/*     */         case 8:
/* 157 */           return REF_newInvokeSpecial;
/*     */         case 9:
/* 159 */           return REF_invokeInterface;
/*     */       }
/* 161 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   ConstantPool(ClassReader paramClassReader) throws IOException, InvalidEntry {
/* 167 */     int i = paramClassReader.readUnsignedShort();
/* 168 */     this.pool = new CPInfo[i];
/* 169 */     for (byte b = 1; b < i; b++) {
/* 170 */       int j = paramClassReader.readUnsignedByte();
/* 171 */       switch (j) {
/*     */         case 7:
/* 173 */           this.pool[b] = new CONSTANT_Class_info(this, paramClassReader);
/*     */           break;
/*     */
/*     */         case 6:
/* 177 */           this.pool[b] = new CONSTANT_Double_info(paramClassReader);
/* 178 */           b++;
/*     */           break;
/*     */
/*     */         case 9:
/* 182 */           this.pool[b] = new CONSTANT_Fieldref_info(this, paramClassReader);
/*     */           break;
/*     */
/*     */         case 4:
/* 186 */           this.pool[b] = new CONSTANT_Float_info(paramClassReader);
/*     */           break;
/*     */
/*     */         case 3:
/* 190 */           this.pool[b] = new CONSTANT_Integer_info(paramClassReader);
/*     */           break;
/*     */
/*     */         case 11:
/* 194 */           this.pool[b] = new CONSTANT_InterfaceMethodref_info(this, paramClassReader);
/*     */           break;
/*     */
/*     */         case 18:
/* 198 */           this.pool[b] = new CONSTANT_InvokeDynamic_info(this, paramClassReader);
/*     */           break;
/*     */
/*     */         case 5:
/* 202 */           this.pool[b] = new CONSTANT_Long_info(paramClassReader);
/* 203 */           b++;
/*     */           break;
/*     */
/*     */         case 15:
/* 207 */           this.pool[b] = new CONSTANT_MethodHandle_info(this, paramClassReader);
/*     */           break;
/*     */
/*     */         case 16:
/* 211 */           this.pool[b] = new CONSTANT_MethodType_info(this, paramClassReader);
/*     */           break;
/*     */
/*     */         case 10:
/* 215 */           this.pool[b] = new CONSTANT_Methodref_info(this, paramClassReader);
/*     */           break;
/*     */
/*     */         case 12:
/* 219 */           this.pool[b] = new CONSTANT_NameAndType_info(this, paramClassReader);
/*     */           break;
/*     */
/*     */         case 8:
/* 223 */           this.pool[b] = new CONSTANT_String_info(this, paramClassReader);
/*     */           break;
/*     */
/*     */         case 1:
/* 227 */           this.pool[b] = new CONSTANT_Utf8_info(paramClassReader);
/*     */           break;
/*     */
/*     */         default:
/* 231 */           throw new InvalidEntry(b, j);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   public ConstantPool(CPInfo[] paramArrayOfCPInfo) {
/* 237 */     this.pool = paramArrayOfCPInfo;
/*     */   }
/*     */
/*     */   public int size() {
/* 241 */     return this.pool.length;
/*     */   }
/*     */
/*     */   public int byteLength() {
/* 245 */     int i = 2;
/* 246 */     for (int j = 1; j < size(); ) {
/* 247 */       CPInfo cPInfo = this.pool[j];
/* 248 */       i += cPInfo.byteLength();
/* 249 */       j += cPInfo.size();
/*     */     }
/* 251 */     return i;
/*     */   }
/*     */
/*     */   public CPInfo get(int paramInt) throws InvalidIndex {
/* 255 */     if (paramInt <= 0 || paramInt >= this.pool.length)
/* 256 */       throw new InvalidIndex(paramInt);
/* 257 */     CPInfo cPInfo = this.pool[paramInt];
/* 258 */     if (cPInfo == null)
/*     */     {
/*     */
/* 261 */       throw new InvalidIndex(paramInt);
/*     */     }
/* 263 */     return this.pool[paramInt];
/*     */   }
/*     */
/*     */   private CPInfo get(int paramInt1, int paramInt2) throws InvalidIndex, UnexpectedEntry {
/* 267 */     CPInfo cPInfo = get(paramInt1);
/* 268 */     if (cPInfo.getTag() != paramInt2)
/* 269 */       throw new UnexpectedEntry(paramInt1, paramInt2, cPInfo.getTag());
/* 270 */     return cPInfo;
/*     */   }
/*     */
/*     */   public CONSTANT_Utf8_info getUTF8Info(int paramInt) throws InvalidIndex, UnexpectedEntry {
/* 274 */     return (CONSTANT_Utf8_info)get(paramInt, 1);
/*     */   }
/*     */
/*     */   public CONSTANT_Class_info getClassInfo(int paramInt) throws InvalidIndex, UnexpectedEntry {
/* 278 */     return (CONSTANT_Class_info)get(paramInt, 7);
/*     */   }
/*     */
/*     */   public CONSTANT_NameAndType_info getNameAndTypeInfo(int paramInt) throws InvalidIndex, UnexpectedEntry {
/* 282 */     return (CONSTANT_NameAndType_info)get(paramInt, 12);
/*     */   }
/*     */
/*     */   public String getUTF8Value(int paramInt) throws InvalidIndex, UnexpectedEntry {
/* 286 */     return (getUTF8Info(paramInt)).value;
/*     */   }
/*     */
/*     */   public int getUTF8Index(String paramString) throws EntryNotFound {
/* 290 */     for (byte b = 1; b < this.pool.length; b++) {
/* 291 */       CPInfo cPInfo = this.pool[b];
/* 292 */       if (cPInfo instanceof CONSTANT_Utf8_info && ((CONSTANT_Utf8_info)cPInfo).value
/* 293 */         .equals(paramString))
/* 294 */         return b;
/*     */     }
/* 296 */     throw new EntryNotFound(paramString);
/*     */   }
/*     */
/*     */   public Iterable<CPInfo> entries() {
/* 300 */     return new Iterable<CPInfo>() {
/*     */         public Iterator<CPInfo> iterator() {
/* 302 */           return new Iterator<CPInfo>() { private CPInfo current;
/*     */
/*     */               public boolean hasNext() {
/* 305 */                 return (this.next < ConstantPool.this.pool.length);
/*     */               }
/*     */
/*     */               public CPInfo next() {
/* 309 */                 this.current = ConstantPool.this.pool[this.next];
/* 310 */                 switch (this.current.getTag())
/*     */                 { case 5:
/*     */                   case 6:
/* 313 */                     this.next += 2;
/*     */
/*     */
/*     */
/*     */
/* 318 */                     return this.current; }  this.next++; return this.current;
/*     */               }
/*     */
/*     */               public void remove() {
/* 322 */                 throw new UnsupportedOperationException();
/*     */               }
/*     */
/*     */
/* 326 */               private int next = 1; }
/*     */             ;
/*     */         }
/*     */       };
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
/*     */   public static abstract class CPInfo
/*     */   {
/*     */     protected final ConstantPool cp;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     CPInfo() {
/* 354 */       this.cp = null;
/*     */     }
/*     */
/*     */     CPInfo(ConstantPool param1ConstantPool) {
/* 358 */       this.cp = param1ConstantPool;
/*     */     }
/*     */
/*     */
/*     */     public abstract int getTag();
/*     */
/*     */
/*     */     public int size() {
/* 366 */       return 1;
/*     */     }
/*     */
/*     */     public abstract int byteLength();
/*     */
/*     */     public abstract <R, D> R accept(Visitor<R, D> param1Visitor, D param1D); }
/*     */
/*     */   public static abstract class CPRefInfo extends CPInfo { public final int tag;
/*     */     public final int class_index;
/*     */     public final int name_and_type_index;
/*     */
/*     */     protected CPRefInfo(ConstantPool param1ConstantPool, ClassReader param1ClassReader, int param1Int) throws IOException {
/* 378 */       super(param1ConstantPool);
/* 379 */       this.tag = param1Int;
/* 380 */       this.class_index = param1ClassReader.readUnsignedShort();
/* 381 */       this.name_and_type_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */     protected CPRefInfo(ConstantPool param1ConstantPool, int param1Int1, int param1Int2, int param1Int3) {
/* 385 */       super(param1ConstantPool);
/* 386 */       this.tag = param1Int1;
/* 387 */       this.class_index = param1Int2;
/* 388 */       this.name_and_type_index = param1Int3;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 392 */       return this.tag;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 396 */       return 5;
/*     */     }
/*     */
/*     */     public CONSTANT_Class_info getClassInfo() throws ConstantPoolException {
/* 400 */       return this.cp.getClassInfo(this.class_index);
/*     */     }
/*     */
/*     */     public String getClassName() throws ConstantPoolException {
/* 404 */       return this.cp.getClassInfo(this.class_index).getName();
/*     */     }
/*     */
/*     */     public CONSTANT_NameAndType_info getNameAndTypeInfo() throws ConstantPoolException {
/* 408 */       return this.cp.getNameAndTypeInfo(this.name_and_type_index);
/*     */     } }
/*     */
/*     */
/*     */   public static class CONSTANT_Class_info
/*     */     extends CPInfo
/*     */   {
/*     */     public final int name_index;
/*     */
/*     */     CONSTANT_Class_info(ConstantPool param1ConstantPool, ClassReader param1ClassReader) throws IOException {
/* 418 */       super(param1ConstantPool);
/* 419 */       this.name_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */     public CONSTANT_Class_info(ConstantPool param1ConstantPool, int param1Int) {
/* 423 */       super(param1ConstantPool);
/* 424 */       this.name_index = param1Int;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 428 */       return 7;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 432 */       return 3;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String getName() throws ConstantPoolException {
/* 442 */       return this.cp.getUTF8Value(this.name_index);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String getBaseName() throws ConstantPoolException {
/* 454 */       String str = getName();
/* 455 */       if (str.startsWith("[")) {
/* 456 */         int i = str.indexOf("[L");
/* 457 */         if (i == -1)
/* 458 */           return null;
/* 459 */         return str.substring(i + 2, str.length() - 1);
/*     */       }
/* 461 */       return str;
/*     */     }
/*     */
/*     */     public int getDimensionCount() throws ConstantPoolException {
/* 465 */       String str = getName();
/* 466 */       byte b = 0;
/* 467 */       while (str.charAt(b) == '[')
/* 468 */         b++;
/* 469 */       return b;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 474 */       return "CONSTANT_Class_info[name_index: " + this.name_index + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 478 */       return param1Visitor.visitClass(this, param1D);
/*     */     }
/*     */   }
/*     */
/*     */   public static class CONSTANT_Double_info extends CPInfo {
/*     */     public final double value;
/*     */
/*     */     CONSTANT_Double_info(ClassReader param1ClassReader) throws IOException {
/* 486 */       this.value = param1ClassReader.readDouble();
/*     */     }
/*     */
/*     */     public CONSTANT_Double_info(double param1Double) {
/* 490 */       this.value = param1Double;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 494 */       return 6;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 498 */       return 9;
/*     */     }
/*     */
/*     */
/*     */     public int size() {
/* 503 */       return 2;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 508 */       return "CONSTANT_Double_info[value: " + this.value + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 512 */       return param1Visitor.visitDouble(this, param1D);
/*     */     }
/*     */   }
/*     */
/*     */   public static class CONSTANT_Fieldref_info
/*     */     extends CPRefInfo
/*     */   {
/*     */     CONSTANT_Fieldref_info(ConstantPool param1ConstantPool, ClassReader param1ClassReader) throws IOException {
/* 520 */       super(param1ConstantPool, param1ClassReader, 9);
/*     */     }
/*     */
/*     */     public CONSTANT_Fieldref_info(ConstantPool param1ConstantPool, int param1Int1, int param1Int2) {
/* 524 */       super(param1ConstantPool, 9, param1Int1, param1Int2);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 529 */       return "CONSTANT_Fieldref_info[class_index: " + this.class_index + ", name_and_type_index: " + this.name_and_type_index + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 533 */       return param1Visitor.visitFieldref(this, param1D);
/*     */     } }
/*     */
/*     */   public static class CONSTANT_Float_info extends CPInfo { public final float value;
/*     */
/*     */     CONSTANT_Float_info(ClassReader param1ClassReader) throws IOException {
/* 539 */       this.value = param1ClassReader.readFloat();
/*     */     }
/*     */
/*     */     public CONSTANT_Float_info(float param1Float) {
/* 543 */       this.value = param1Float;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 547 */       return 4;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 551 */       return 5;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 556 */       return "CONSTANT_Float_info[value: " + this.value + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 560 */       return param1Visitor.visitFloat(this, param1D);
/*     */     } }
/*     */
/*     */
/*     */   public static class CONSTANT_Integer_info extends CPInfo {
/*     */     public final int value;
/*     */
/*     */     CONSTANT_Integer_info(ClassReader param1ClassReader) throws IOException {
/* 568 */       this.value = param1ClassReader.readInt();
/*     */     }
/*     */
/*     */     public CONSTANT_Integer_info(int param1Int) {
/* 572 */       this.value = param1Int;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 576 */       return 3;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 580 */       return 5;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 585 */       return "CONSTANT_Integer_info[value: " + this.value + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 589 */       return param1Visitor.visitInteger(this, param1D);
/*     */     }
/*     */   }
/*     */
/*     */   public static class CONSTANT_InterfaceMethodref_info
/*     */     extends CPRefInfo
/*     */   {
/*     */     CONSTANT_InterfaceMethodref_info(ConstantPool param1ConstantPool, ClassReader param1ClassReader) throws IOException {
/* 597 */       super(param1ConstantPool, param1ClassReader, 11);
/*     */     }
/*     */
/*     */     public CONSTANT_InterfaceMethodref_info(ConstantPool param1ConstantPool, int param1Int1, int param1Int2) {
/* 601 */       super(param1ConstantPool, 11, param1Int1, param1Int2);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 606 */       return "CONSTANT_InterfaceMethodref_info[class_index: " + this.class_index + ", name_and_type_index: " + this.name_and_type_index + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 610 */       return param1Visitor.visitInterfaceMethodref(this, param1D);
/*     */     } }
/*     */   public static class CONSTANT_InvokeDynamic_info extends CPInfo { public final int bootstrap_method_attr_index;
/*     */     public final int name_and_type_index;
/*     */
/*     */     CONSTANT_InvokeDynamic_info(ConstantPool param1ConstantPool, ClassReader param1ClassReader) throws IOException {
/* 616 */       super(param1ConstantPool);
/* 617 */       this.bootstrap_method_attr_index = param1ClassReader.readUnsignedShort();
/* 618 */       this.name_and_type_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */     public CONSTANT_InvokeDynamic_info(ConstantPool param1ConstantPool, int param1Int1, int param1Int2) {
/* 622 */       super(param1ConstantPool);
/* 623 */       this.bootstrap_method_attr_index = param1Int1;
/* 624 */       this.name_and_type_index = param1Int2;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 628 */       return 18;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 632 */       return 5;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 637 */       return "CONSTANT_InvokeDynamic_info[bootstrap_method_index: " + this.bootstrap_method_attr_index + ", name_and_type_index: " + this.name_and_type_index + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 641 */       return param1Visitor.visitInvokeDynamic(this, param1D);
/*     */     }
/*     */
/*     */     public CONSTANT_NameAndType_info getNameAndTypeInfo() throws ConstantPoolException {
/* 645 */       return this.cp.getNameAndTypeInfo(this.name_and_type_index);
/*     */     } }
/*     */
/*     */
/*     */   public static class CONSTANT_Long_info
/*     */     extends CPInfo {
/*     */     public final long value;
/*     */
/*     */     CONSTANT_Long_info(ClassReader param1ClassReader) throws IOException {
/* 654 */       this.value = param1ClassReader.readLong();
/*     */     }
/*     */
/*     */     public CONSTANT_Long_info(long param1Long) {
/* 658 */       this.value = param1Long;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 662 */       return 5;
/*     */     }
/*     */
/*     */
/*     */     public int size() {
/* 667 */       return 2;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 671 */       return 9;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 676 */       return "CONSTANT_Long_info[value: " + this.value + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 680 */       return param1Visitor.visitLong(this, param1D);
/*     */     } }
/*     */
/*     */   public static class CONSTANT_MethodHandle_info extends CPInfo {
/*     */     public final RefKind reference_kind;
/*     */     public final int reference_index;
/*     */
/*     */     CONSTANT_MethodHandle_info(ConstantPool param1ConstantPool, ClassReader param1ClassReader) throws IOException {
/* 688 */       super(param1ConstantPool);
/* 689 */       this.reference_kind = RefKind.getRefkind(param1ClassReader.readUnsignedByte());
/* 690 */       this.reference_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */     public CONSTANT_MethodHandle_info(ConstantPool param1ConstantPool, RefKind param1RefKind, int param1Int) {
/* 694 */       super(param1ConstantPool);
/* 695 */       this.reference_kind = param1RefKind;
/* 696 */       this.reference_index = param1Int;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 700 */       return 15;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 704 */       return 4;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 709 */       return "CONSTANT_MethodHandle_info[ref_kind: " + this.reference_kind + ", member_index: " + this.reference_index + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 713 */       return param1Visitor.visitMethodHandle(this, param1D);
/*     */     }
/*     */
/*     */     public CPRefInfo getCPRefInfo() throws ConstantPoolException {
/* 717 */       int i = 10;
/* 718 */       int j = this.cp.get(this.reference_index).getTag();
/*     */
/* 720 */       switch (j) {
/*     */         case 9:
/*     */         case 11:
/* 723 */           i = j; break;
/*     */       }
/* 725 */       return (CPRefInfo)this.cp.get(this.reference_index, i);
/*     */     }
/*     */   }
/*     */
/*     */   public static class CONSTANT_MethodType_info
/*     */     extends CPInfo {
/*     */     public final int descriptor_index;
/*     */
/*     */     CONSTANT_MethodType_info(ConstantPool param1ConstantPool, ClassReader param1ClassReader) throws IOException {
/* 734 */       super(param1ConstantPool);
/* 735 */       this.descriptor_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */     public CONSTANT_MethodType_info(ConstantPool param1ConstantPool, int param1Int) {
/* 739 */       super(param1ConstantPool);
/* 740 */       this.descriptor_index = param1Int;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 744 */       return 16;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 748 */       return 3;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 753 */       return "CONSTANT_MethodType_info[signature_index: " + this.descriptor_index + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 757 */       return param1Visitor.visitMethodType(this, param1D);
/*     */     }
/*     */
/*     */     public String getType() throws ConstantPoolException {
/* 761 */       return this.cp.getUTF8Value(this.descriptor_index);
/*     */     }
/*     */   }
/*     */
/*     */   public static class CONSTANT_Methodref_info
/*     */     extends CPRefInfo
/*     */   {
/*     */     CONSTANT_Methodref_info(ConstantPool param1ConstantPool, ClassReader param1ClassReader) throws IOException {
/* 769 */       super(param1ConstantPool, param1ClassReader, 10);
/*     */     }
/*     */
/*     */     public CONSTANT_Methodref_info(ConstantPool param1ConstantPool, int param1Int1, int param1Int2) {
/* 773 */       super(param1ConstantPool, 10, param1Int1, param1Int2);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 778 */       return "CONSTANT_Methodref_info[class_index: " + this.class_index + ", name_and_type_index: " + this.name_and_type_index + "]";
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 782 */       return param1Visitor.visitMethodref(this, param1D);
/*     */     } }
/*     */   public static class CONSTANT_NameAndType_info extends CPInfo { public final int name_index;
/*     */     public final int type_index;
/*     */
/*     */     CONSTANT_NameAndType_info(ConstantPool param1ConstantPool, ClassReader param1ClassReader) throws IOException {
/* 788 */       super(param1ConstantPool);
/* 789 */       this.name_index = param1ClassReader.readUnsignedShort();
/* 790 */       this.type_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */     public CONSTANT_NameAndType_info(ConstantPool param1ConstantPool, int param1Int1, int param1Int2) {
/* 794 */       super(param1ConstantPool);
/* 795 */       this.name_index = param1Int1;
/* 796 */       this.type_index = param1Int2;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 800 */       return 12;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 804 */       return 5;
/*     */     }
/*     */
/*     */     public String getName() throws ConstantPoolException {
/* 808 */       return this.cp.getUTF8Value(this.name_index);
/*     */     }
/*     */
/*     */     public String getType() throws ConstantPoolException {
/* 812 */       return this.cp.getUTF8Value(this.type_index);
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 816 */       return param1Visitor.visitNameAndType(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 821 */       return "CONSTANT_NameAndType_info[name_index: " + this.name_index + ", type_index: " + this.type_index + "]";
/*     */     } }
/*     */
/*     */
/*     */   public static class CONSTANT_String_info
/*     */     extends CPInfo {
/*     */     public final int string_index;
/*     */
/*     */     CONSTANT_String_info(ConstantPool param1ConstantPool, ClassReader param1ClassReader) throws IOException {
/* 830 */       super(param1ConstantPool);
/* 831 */       this.string_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */     public CONSTANT_String_info(ConstantPool param1ConstantPool, int param1Int) {
/* 835 */       super(param1ConstantPool);
/* 836 */       this.string_index = param1Int;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 840 */       return 8;
/*     */     }
/*     */
/*     */     public int byteLength() {
/* 844 */       return 3;
/*     */     }
/*     */
/*     */     public String getString() throws ConstantPoolException {
/* 848 */       return this.cp.getUTF8Value(this.string_index);
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 852 */       return param1Visitor.visitString(this, param1D);
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 857 */       return "CONSTANT_String_info[class_index: " + this.string_index + "]";
/*     */     }
/*     */   }
/*     */
/*     */   public static class CONSTANT_Utf8_info extends CPInfo {
/*     */     public final String value;
/*     */
/*     */     CONSTANT_Utf8_info(ClassReader param1ClassReader) throws IOException {
/* 865 */       this.value = param1ClassReader.readUTF();
/*     */     }
/*     */
/*     */     public CONSTANT_Utf8_info(String param1String) {
/* 869 */       this.value = param1String;
/*     */     }
/*     */
/*     */     public int getTag() {
/* 873 */       return 1;
/*     */     }
/*     */
/*     */     public int byteLength() {
/*     */       class SizeOutputStream extends OutputStream { int size;
/*     */
/*     */         public void write(int param2Int) {
/* 880 */           this.size++;
/*     */         } }
/*     */       ;
/*     */
/* 884 */       SizeOutputStream sizeOutputStream = new SizeOutputStream();
/* 885 */       DataOutputStream dataOutputStream = new DataOutputStream(sizeOutputStream);
/* 886 */       try { dataOutputStream.writeUTF(this.value); } catch (IOException iOException) {}
/* 887 */       return 1 + sizeOutputStream.size;
/*     */     }
/*     */
/*     */
/*     */     public String toString() {
/* 892 */       if (this.value.length() < 32 && isPrintableAscii(this.value)) {
/* 893 */         return "CONSTANT_Utf8_info[value: \"" + this.value + "\"]";
/*     */       }
/* 895 */       return "CONSTANT_Utf8_info[value: (" + this.value.length() + " chars)]";
/*     */     }
/*     */
/*     */     static boolean isPrintableAscii(String param1String) {
/* 899 */       for (byte b = 0; b < param1String.length(); b++) {
/* 900 */         char c = param1String.charAt(b);
/* 901 */         if (c < ' ' || c >= '')
/* 902 */           return false;
/*     */       }
/* 904 */       return true;
/*     */     }
/*     */
/*     */     public <R, D> R accept(Visitor<R, D> param1Visitor, D param1D) {
/* 908 */       return param1Visitor.visitUtf8(this, param1D);
/*     */     }
/*     */   }
/*     */
/*     */   public static interface Visitor<R, P> {
/*     */     R visitClass(CONSTANT_Class_info param1CONSTANT_Class_info, P param1P);
/*     */
/*     */     R visitDouble(CONSTANT_Double_info param1CONSTANT_Double_info, P param1P);
/*     */
/*     */     R visitFieldref(CONSTANT_Fieldref_info param1CONSTANT_Fieldref_info, P param1P);
/*     */
/*     */     R visitFloat(CONSTANT_Float_info param1CONSTANT_Float_info, P param1P);
/*     */
/*     */     R visitInteger(CONSTANT_Integer_info param1CONSTANT_Integer_info, P param1P);
/*     */
/*     */     R visitInterfaceMethodref(CONSTANT_InterfaceMethodref_info param1CONSTANT_InterfaceMethodref_info, P param1P);
/*     */
/*     */     R visitInvokeDynamic(CONSTANT_InvokeDynamic_info param1CONSTANT_InvokeDynamic_info, P param1P);
/*     */
/*     */     R visitLong(CONSTANT_Long_info param1CONSTANT_Long_info, P param1P);
/*     */
/*     */     R visitNameAndType(CONSTANT_NameAndType_info param1CONSTANT_NameAndType_info, P param1P);
/*     */
/*     */     R visitMethodref(CONSTANT_Methodref_info param1CONSTANT_Methodref_info, P param1P);
/*     */
/*     */     R visitMethodHandle(CONSTANT_MethodHandle_info param1CONSTANT_MethodHandle_info, P param1P);
/*     */
/*     */     R visitMethodType(CONSTANT_MethodType_info param1CONSTANT_MethodType_info, P param1P);
/*     */
/*     */     R visitString(CONSTANT_String_info param1CONSTANT_String_info, P param1P);
/*     */
/*     */     R visitUtf8(CONSTANT_Utf8_info param1CONSTANT_Utf8_info, P param1P);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\ConstantPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
