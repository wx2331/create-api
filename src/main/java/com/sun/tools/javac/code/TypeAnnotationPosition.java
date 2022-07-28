/*     */ package com.sun.tools.javac.code;
/*     */
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class TypeAnnotationPosition
/*     */ {
/*     */   public enum TypePathEntryKind
/*     */   {
/*  44 */     ARRAY(0),
/*  45 */     INNER_TYPE(1),
/*  46 */     WILDCARD(2),
/*  47 */     TYPE_ARGUMENT(3);
/*     */
/*     */     public final int tag;
/*     */
/*     */     TypePathEntryKind(int param1Int1) {
/*  52 */       this.tag = param1Int1;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   public static class TypePathEntry
/*     */   {
/*     */     public static final int bytesPerEntry = 2;
/*     */
/*     */     public final TypePathEntryKind tag;
/*     */     public final int arg;
/*  63 */     public static final TypePathEntry ARRAY = new TypePathEntry(TypePathEntryKind.ARRAY);
/*  64 */     public static final TypePathEntry INNER_TYPE = new TypePathEntry(TypePathEntryKind.INNER_TYPE);
/*  65 */     public static final TypePathEntry WILDCARD = new TypePathEntry(TypePathEntryKind.WILDCARD);
/*     */
/*     */     private TypePathEntry(TypePathEntryKind param1TypePathEntryKind) {
/*  68 */       Assert.check((param1TypePathEntryKind == TypePathEntryKind.ARRAY || param1TypePathEntryKind == TypePathEntryKind.INNER_TYPE || param1TypePathEntryKind == TypePathEntryKind.WILDCARD), "Invalid TypePathEntryKind: " + param1TypePathEntryKind);
/*     */
/*     */
/*     */
/*  72 */       this.tag = param1TypePathEntryKind;
/*  73 */       this.arg = 0;
/*     */     }
/*     */
/*     */     public TypePathEntry(TypePathEntryKind param1TypePathEntryKind, int param1Int) {
/*  77 */       Assert.check((param1TypePathEntryKind == TypePathEntryKind.TYPE_ARGUMENT), "Invalid TypePathEntryKind: " + param1TypePathEntryKind);
/*     */
/*  79 */       this.tag = param1TypePathEntryKind;
/*  80 */       this.arg = param1Int;
/*     */     }
/*     */
/*     */     public static TypePathEntry fromBinary(int param1Int1, int param1Int2) {
/*  84 */       Assert.check((param1Int2 == 0 || param1Int1 == TypePathEntryKind.TYPE_ARGUMENT.tag), "Invalid TypePathEntry tag/arg: " + param1Int1 + "/" + param1Int2);
/*     */
/*  86 */       switch (param1Int1) {
/*     */         case 0:
/*  88 */           return ARRAY;
/*     */         case 1:
/*  90 */           return INNER_TYPE;
/*     */         case 2:
/*  92 */           return WILDCARD;
/*     */         case 3:
/*  94 */           return new TypePathEntry(TypePathEntryKind.TYPE_ARGUMENT, param1Int2);
/*     */       }
/*  96 */       Assert.error("Invalid TypePathEntryKind tag: " + param1Int1);
/*  97 */       return null;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public String toString() {
/* 103 */       return this.tag.toString() + ((this.tag == TypePathEntryKind.TYPE_ARGUMENT) ? ("(" + this.arg + ")") : "");
/*     */     }
/*     */
/*     */
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 109 */       if (!(param1Object instanceof TypePathEntry)) {
/* 110 */         return false;
/*     */       }
/* 112 */       TypePathEntry typePathEntry = (TypePathEntry)param1Object;
/* 113 */       return (this.tag == typePathEntry.tag && this.arg == typePathEntry.arg);
/*     */     }
/*     */
/*     */
/*     */     public int hashCode() {
/* 118 */       return this.tag.hashCode() * 17 + this.arg;
/*     */     }
/*     */   }
/*     */
/* 122 */   public TargetType type = TargetType.UNKNOWN;
/*     */
/*     */
/* 125 */   public List<TypePathEntry> location = List.nil();
/*     */
/*     */
/* 128 */   public int pos = -1;
/*     */
/*     */
/*     */   public boolean isValidOffset = false;
/*     */
/* 133 */   public int offset = -1;
/*     */
/*     */
/* 136 */   public int[] lvarOffset = null;
/* 137 */   public int[] lvarLength = null;
/* 138 */   public int[] lvarIndex = null;
/*     */
/*     */
/* 141 */   public int bound_index = Integer.MIN_VALUE;
/*     */
/*     */
/* 144 */   public int parameter_index = Integer.MIN_VALUE;
/*     */
/*     */
/* 147 */   public int type_index = Integer.MIN_VALUE;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 154 */   public int exception_index = Integer.MIN_VALUE;
/*     */
/*     */
/*     */
/*     */
/* 159 */   public JCTree.JCLambda onLambda = null;
/*     */
/*     */
/*     */
/*     */   public String toString() {
/*     */     byte b;
/* 165 */     StringBuilder stringBuilder = new StringBuilder();
/* 166 */     stringBuilder.append('[');
/* 167 */     stringBuilder.append(this.type);
/*     */
/* 169 */     switch (this.type) {
/*     */
/*     */
/*     */
/*     */       case INSTANCEOF:
/*     */       case NEW:
/*     */       case CONSTRUCTOR_REFERENCE:
/*     */       case METHOD_REFERENCE:
/* 177 */         stringBuilder.append(", offset = ");
/* 178 */         stringBuilder.append(this.offset);
/*     */         break;
/*     */
/*     */
/*     */       case LOCAL_VARIABLE:
/*     */       case RESOURCE_VARIABLE:
/* 184 */         if (this.lvarOffset == null) {
/* 185 */           stringBuilder.append(", lvarOffset is null!");
/*     */           break;
/*     */         }
/* 188 */         stringBuilder.append(", {");
/* 189 */         for (b = 0; b < this.lvarOffset.length; b++) {
/* 190 */           if (b != 0) stringBuilder.append("; ");
/* 191 */           stringBuilder.append("start_pc = ");
/* 192 */           stringBuilder.append(this.lvarOffset[b]);
/* 193 */           stringBuilder.append(", length = ");
/* 194 */           stringBuilder.append(this.lvarLength[b]);
/* 195 */           stringBuilder.append(", index = ");
/* 196 */           stringBuilder.append(this.lvarIndex[b]);
/*     */         }
/* 198 */         stringBuilder.append("}");
/*     */         break;
/*     */
/*     */
/*     */       case METHOD_RECEIVER:
/*     */         break;
/*     */
/*     */       case CLASS_TYPE_PARAMETER:
/*     */       case METHOD_TYPE_PARAMETER:
/* 207 */         stringBuilder.append(", param_index = ");
/* 208 */         stringBuilder.append(this.parameter_index);
/*     */         break;
/*     */
/*     */       case CLASS_TYPE_PARAMETER_BOUND:
/*     */       case METHOD_TYPE_PARAMETER_BOUND:
/* 213 */         stringBuilder.append(", param_index = ");
/* 214 */         stringBuilder.append(this.parameter_index);
/* 215 */         stringBuilder.append(", bound_index = ");
/* 216 */         stringBuilder.append(this.bound_index);
/*     */         break;
/*     */
/*     */       case CLASS_EXTENDS:
/* 220 */         stringBuilder.append(", type_index = ");
/* 221 */         stringBuilder.append(this.type_index);
/*     */         break;
/*     */
/*     */       case THROWS:
/* 225 */         stringBuilder.append(", type_index = ");
/* 226 */         stringBuilder.append(this.type_index);
/*     */         break;
/*     */
/*     */       case EXCEPTION_PARAMETER:
/* 230 */         stringBuilder.append(", exception_index = ");
/* 231 */         stringBuilder.append(this.exception_index);
/*     */         break;
/*     */
/*     */       case METHOD_FORMAL_PARAMETER:
/* 235 */         stringBuilder.append(", param_index = ");
/* 236 */         stringBuilder.append(this.parameter_index);
/*     */         break;
/*     */
/*     */
/*     */       case CAST:
/*     */       case CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT:
/*     */       case METHOD_INVOCATION_TYPE_ARGUMENT:
/*     */       case CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT:
/*     */       case METHOD_REFERENCE_TYPE_ARGUMENT:
/* 245 */         stringBuilder.append(", offset = ");
/* 246 */         stringBuilder.append(this.offset);
/* 247 */         stringBuilder.append(", type_index = ");
/* 248 */         stringBuilder.append(this.type_index);
/*     */         break;
/*     */
/*     */       case METHOD_RETURN:
/*     */       case FIELD:
/*     */         break;
/*     */       case UNKNOWN:
/* 255 */         stringBuilder.append(", position UNKNOWN!");
/*     */         break;
/*     */       default:
/* 258 */         Assert.error("Unknown target type: " + this.type);
/*     */         break;
/*     */     }
/*     */
/* 262 */     if (!this.location.isEmpty()) {
/* 263 */       stringBuilder.append(", location = (");
/* 264 */       stringBuilder.append(this.location);
/* 265 */       stringBuilder.append(")");
/*     */     }
/*     */
/* 268 */     stringBuilder.append(", pos = ");
/* 269 */     stringBuilder.append(this.pos);
/*     */
/* 271 */     if (this.onLambda != null) {
/* 272 */       stringBuilder.append(", onLambda hash = ");
/* 273 */       stringBuilder.append(this.onLambda.hashCode());
/*     */     }
/*     */
/* 276 */     stringBuilder.append(']');
/* 277 */     return stringBuilder.toString();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public boolean emitToClassfile() {
/* 286 */     return (!this.type.isLocal() || this.isValidOffset);
/*     */   }
/*     */
/*     */
/*     */   public boolean matchesPos(int paramInt) {
/* 291 */     return (this.pos == paramInt);
/*     */   }
/*     */
/*     */   public void updatePosOffset(int paramInt) {
/* 295 */     this.offset = paramInt;
/* 296 */     this.lvarOffset = new int[] { paramInt };
/* 297 */     this.isValidOffset = true;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static List<TypePathEntry> getTypePathFromBinary(List<Integer> paramList) {
/* 307 */     ListBuffer listBuffer = new ListBuffer();
/* 308 */     Iterator<Integer> iterator = paramList.iterator();
/* 309 */     while (iterator.hasNext()) {
/* 310 */       Integer integer1 = iterator.next();
/* 311 */       Assert.check(iterator.hasNext(), "Could not decode type path: " + paramList);
/* 312 */       Integer integer2 = iterator.next();
/* 313 */       listBuffer = listBuffer.append(TypePathEntry.fromBinary(integer1.intValue(), integer2.intValue()));
/*     */     }
/* 315 */     return listBuffer.toList();
/*     */   }
/*     */
/*     */   public static List<Integer> getBinaryFromTypePath(List<TypePathEntry> paramList) {
/* 319 */     ListBuffer listBuffer = new ListBuffer();
/* 320 */     for (TypePathEntry typePathEntry : paramList) {
/* 321 */       listBuffer = listBuffer.append(Integer.valueOf(typePathEntry.tag.tag));
/* 322 */       listBuffer = listBuffer.append(Integer.valueOf(typePathEntry.arg));
/*     */     }
/* 324 */     return listBuffer.toList();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\TypeAnnotationPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
