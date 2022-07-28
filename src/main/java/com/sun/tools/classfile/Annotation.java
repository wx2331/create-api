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
/*     */ public class Annotation
/*     */ {
/*     */   public final int type_index;
/*     */   public final int num_element_value_pairs;
/*     */   public final element_value_pair[] element_value_pairs;
/*     */
/*     */   static class InvalidAnnotation
/*     */     extends AttributeException
/*     */   {
/*     */     private static final long serialVersionUID = -4620480740735772708L;
/*     */
/*     */     InvalidAnnotation(String param1String) {
/*  42 */       super(param1String);
/*     */     }
/*     */   }
/*     */
/*     */   Annotation(ClassReader paramClassReader) throws IOException, InvalidAnnotation {
/*  47 */     this.type_index = paramClassReader.readUnsignedShort();
/*  48 */     this.num_element_value_pairs = paramClassReader.readUnsignedShort();
/*  49 */     this.element_value_pairs = new element_value_pair[this.num_element_value_pairs];
/*  50 */     for (byte b = 0; b < this.element_value_pairs.length; b++) {
/*  51 */       this.element_value_pairs[b] = new element_value_pair(paramClassReader);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public Annotation(ConstantPool paramConstantPool, int paramInt, element_value_pair[] paramArrayOfelement_value_pair) {
/*  57 */     this.type_index = paramInt;
/*  58 */     this.num_element_value_pairs = paramArrayOfelement_value_pair.length;
/*  59 */     this.element_value_pairs = paramArrayOfelement_value_pair;
/*     */   }
/*     */
/*     */   public int length() {
/*  63 */     int i = 4;
/*  64 */     for (element_value_pair element_value_pair1 : this.element_value_pairs)
/*  65 */       i += element_value_pair1.length();
/*  66 */     return i;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public static abstract class element_value
/*     */   {
/*     */     public final int tag;
/*     */
/*     */
/*     */
/*     */     public static element_value read(ClassReader param1ClassReader) throws IOException, InvalidAnnotation {
/*  79 */       int i = param1ClassReader.readUnsignedByte();
/*  80 */       switch (i) {
/*     */         case 66:
/*     */         case 67:
/*     */         case 68:
/*     */         case 70:
/*     */         case 73:
/*     */         case 74:
/*     */         case 83:
/*     */         case 90:
/*     */         case 115:
/*  90 */           return new Primitive_element_value(param1ClassReader, i);
/*     */
/*     */         case 101:
/*  93 */           return new Enum_element_value(param1ClassReader, i);
/*     */
/*     */         case 99:
/*  96 */           return new Class_element_value(param1ClassReader, i);
/*     */
/*     */         case 64:
/*  99 */           return new Annotation_element_value(param1ClassReader, i);
/*     */
/*     */         case 91:
/* 102 */           return new Array_element_value(param1ClassReader, i);
/*     */       }
/*     */
/* 105 */       throw new InvalidAnnotation("unrecognized tag: " + i);
/*     */     }
/*     */     public abstract int length();
/*     */     public abstract <R, P> R accept(Visitor<R, P> param1Visitor, P param1P);
/*     */     protected element_value(int param1Int) {
/* 110 */       this.tag = param1Int;
/*     */     }
/*     */
/*     */     public static interface Visitor<R, P>
/*     */     {
/*     */       R visitPrimitive(Primitive_element_value param2Primitive_element_value, P param2P);
/*     */
/*     */       R visitEnum(Enum_element_value param2Enum_element_value, P param2P);
/*     */
/*     */       R visitClass(Class_element_value param2Class_element_value, P param2P);
/*     */
/*     */       R visitAnnotation(Annotation_element_value param2Annotation_element_value, P param2P);
/*     */
/*     */       R visitArray(Array_element_value param2Array_element_value, P param2P);
/*     */     }
/*     */   }
/*     */
/*     */   public static class Primitive_element_value
/*     */     extends element_value {
/*     */     Primitive_element_value(ClassReader param1ClassReader, int param1Int) throws IOException {
/* 130 */       super(param1Int);
/* 131 */       this.const_value_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */     public final int const_value_index;
/*     */
/*     */     public int length() {
/* 136 */       return 2;
/*     */     }
/*     */
/*     */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/* 140 */       return param1Visitor.visitPrimitive(this, param1P);
/*     */     }
/*     */   }
/*     */
/*     */   public static class Enum_element_value extends element_value {
/*     */     public final int type_name_index;
/*     */     public final int const_name_index;
/*     */
/*     */     Enum_element_value(ClassReader param1ClassReader, int param1Int) throws IOException {
/* 149 */       super(param1Int);
/* 150 */       this.type_name_index = param1ClassReader.readUnsignedShort();
/* 151 */       this.const_name_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */
/*     */     public int length() {
/* 156 */       return 4;
/*     */     }
/*     */
/*     */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/* 160 */       return param1Visitor.visitEnum(this, param1P);
/*     */     }
/*     */   }
/*     */
/*     */   public static class Class_element_value
/*     */     extends element_value {
/*     */     public final int class_info_index;
/*     */
/*     */     Class_element_value(ClassReader param1ClassReader, int param1Int) throws IOException {
/* 169 */       super(param1Int);
/* 170 */       this.class_info_index = param1ClassReader.readUnsignedShort();
/*     */     }
/*     */
/*     */
/*     */     public int length() {
/* 175 */       return 2;
/*     */     }
/*     */
/*     */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/* 179 */       return param1Visitor.visitClass(this, param1P);
/*     */     }
/*     */   }
/*     */
/*     */   public static class Annotation_element_value
/*     */     extends element_value {
/*     */     public final Annotation annotation_value;
/*     */
/*     */     Annotation_element_value(ClassReader param1ClassReader, int param1Int) throws IOException, InvalidAnnotation {
/* 188 */       super(param1Int);
/* 189 */       this.annotation_value = new Annotation(param1ClassReader);
/*     */     }
/*     */
/*     */
/*     */     public int length() {
/* 194 */       return this.annotation_value.length();
/*     */     }
/*     */
/*     */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/* 198 */       return param1Visitor.visitAnnotation(this, param1P);
/*     */     }
/*     */   }
/*     */
/*     */   public static class Array_element_value extends element_value {
/*     */     public final int num_values;
/*     */     public final element_value[] values;
/*     */
/*     */     Array_element_value(ClassReader param1ClassReader, int param1Int) throws IOException, InvalidAnnotation {
/* 207 */       super(param1Int);
/* 208 */       this.num_values = param1ClassReader.readUnsignedShort();
/* 209 */       this.values = new element_value[this.num_values];
/* 210 */       for (byte b = 0; b < this.values.length; b++) {
/* 211 */         this.values[b] = element_value.read(param1ClassReader);
/*     */       }
/*     */     }
/*     */
/*     */     public int length() {
/* 216 */       int i = 2;
/* 217 */       for (byte b = 0; b < this.values.length; b++)
/* 218 */         i += this.values[b].length();
/* 219 */       return i;
/*     */     }
/*     */
/*     */     public <R, P> R accept(Visitor<R, P> param1Visitor, P param1P) {
/* 223 */       return param1Visitor.visitArray(this, param1P);
/*     */     }
/*     */   }
/*     */
/*     */   public static class element_value_pair
/*     */   {
/*     */     public final int element_name_index;
/*     */     public final element_value value;
/*     */
/*     */     element_value_pair(ClassReader param1ClassReader) throws IOException, InvalidAnnotation {
/* 233 */       this.element_name_index = param1ClassReader.readUnsignedShort();
/* 234 */       this.value = element_value.read(param1ClassReader);
/*     */     }
/*     */
/*     */     public int length() {
/* 238 */       return 2 + this.value.length();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Annotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
