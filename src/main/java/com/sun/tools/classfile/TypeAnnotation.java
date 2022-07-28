/*     */ package com.sun.tools.classfile;
/*     */
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
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
/*     */ public class TypeAnnotation
/*     */ {
/*     */   public final ConstantPool constant_pool;
/*     */   public final Position position;
/*     */   public final Annotation annotation;
/*     */
/*     */   TypeAnnotation(ClassReader paramClassReader) throws IOException, Annotation.InvalidAnnotation {
/*  44 */     this.constant_pool = paramClassReader.getConstantPool();
/*  45 */     this.position = read_position(paramClassReader);
/*  46 */     this.annotation = new Annotation(paramClassReader);
/*     */   }
/*     */
/*     */
/*     */   public TypeAnnotation(ConstantPool paramConstantPool, Annotation paramAnnotation, Position paramPosition) {
/*  51 */     this.constant_pool = paramConstantPool;
/*  52 */     this.position = paramPosition;
/*  53 */     this.annotation = paramAnnotation;
/*     */   }
/*     */
/*     */   public int length() {
/*  57 */     int i = this.annotation.length();
/*  58 */     i += position_length(this.position);
/*  59 */     return i;
/*     */   }
/*     */
/*     */
/*     */   public String toString() {
/*     */     try {
/*  65 */       return "@" + this.constant_pool.getUTF8Value(this.annotation.type_index).toString().substring(1) + " pos: " + this.position
/*  66 */         .toString();
/*  67 */     } catch (Exception exception) {
/*  68 */       exception.printStackTrace();
/*  69 */       return exception.toString();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static Position read_position(ClassReader paramClassReader) throws IOException, Annotation.InvalidAnnotation {
/*  79 */     int k, i = paramClassReader.readUnsignedByte();
/*  80 */     if (!TargetType.isValidTargetTypeValue(i)) {
/*  81 */       throw new Annotation.InvalidAnnotation("TypeAnnotation: Invalid type annotation target type value: " + String.format("0x%02X", new Object[] { Integer.valueOf(i) }));
/*     */     }
/*  83 */     TargetType targetType = TargetType.fromTargetTypeValue(i);
/*     */
/*  85 */     Position position = new Position();
/*  86 */     position.type = targetType;
/*     */
/*  88 */     switch (targetType) {
/*     */
/*     */
/*     */
/*     */       case INSTANCEOF:
/*     */       case NEW:
/*     */       case CONSTRUCTOR_REFERENCE:
/*     */       case METHOD_REFERENCE:
/*  96 */         position.offset = paramClassReader.readUnsignedShort();
/*     */         break;
/*     */
/*     */
/*     */       case LOCAL_VARIABLE:
/*     */       case RESOURCE_VARIABLE:
/* 102 */         j = paramClassReader.readUnsignedShort();
/* 103 */         position.lvarOffset = new int[j];
/* 104 */         position.lvarLength = new int[j];
/* 105 */         position.lvarIndex = new int[j];
/* 106 */         for (k = 0; k < j; k++) {
/* 107 */           position.lvarOffset[k] = paramClassReader.readUnsignedShort();
/* 108 */           position.lvarLength[k] = paramClassReader.readUnsignedShort();
/* 109 */           position.lvarIndex[k] = paramClassReader.readUnsignedShort();
/*     */         }
/*     */         break;
/*     */
/*     */       case EXCEPTION_PARAMETER:
/* 114 */         position.exception_index = paramClassReader.readUnsignedShort();
/*     */         break;
/*     */
/*     */
/*     */       case METHOD_RECEIVER:
/*     */         break;
/*     */
/*     */       case CLASS_TYPE_PARAMETER:
/*     */       case METHOD_TYPE_PARAMETER:
/* 123 */         position.parameter_index = paramClassReader.readUnsignedByte();
/*     */         break;
/*     */
/*     */       case CLASS_TYPE_PARAMETER_BOUND:
/*     */       case METHOD_TYPE_PARAMETER_BOUND:
/* 128 */         position.parameter_index = paramClassReader.readUnsignedByte();
/* 129 */         position.bound_index = paramClassReader.readUnsignedByte();
/*     */         break;
/*     */
/*     */       case CLASS_EXTENDS:
/* 133 */         k = paramClassReader.readUnsignedShort();
/* 134 */         if (k == 65535)
/* 135 */           k = -1;
/* 136 */         position.type_index = k;
/*     */         break;
/*     */
/*     */       case THROWS:
/* 140 */         position.type_index = paramClassReader.readUnsignedShort();
/*     */         break;
/*     */
/*     */       case METHOD_FORMAL_PARAMETER:
/* 144 */         position.parameter_index = paramClassReader.readUnsignedByte();
/*     */         break;
/*     */
/*     */
/*     */       case CAST:
/*     */       case CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT:
/*     */       case METHOD_INVOCATION_TYPE_ARGUMENT:
/*     */       case CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT:
/*     */       case METHOD_REFERENCE_TYPE_ARGUMENT:
/* 153 */         position.offset = paramClassReader.readUnsignedShort();
/* 154 */         position.type_index = paramClassReader.readUnsignedByte();
/*     */         break;
/*     */
/*     */       case METHOD_RETURN:
/*     */       case FIELD:
/*     */         break;
/*     */       case UNKNOWN:
/* 161 */         throw new AssertionError("TypeAnnotation: UNKNOWN target type should never occur!");
/*     */       default:
/* 163 */         throw new AssertionError("TypeAnnotation: Unknown target type: " + targetType);
/*     */     }
/*     */
/*     */
/* 167 */     int j = paramClassReader.readUnsignedByte();
/* 168 */     ArrayList<Integer> arrayList = new ArrayList(j);
/* 169 */     for (byte b = 0; b < j * 2; b++)
/* 170 */       arrayList.add(Integer.valueOf(paramClassReader.readUnsignedByte()));
/* 171 */     position.location = Position.getTypePathFromBinary(arrayList);
/*     */
/* 173 */     return position;
/*     */   }
/*     */
/*     */   private static int position_length(Position paramPosition) {
/* 177 */     int j, i = 0;
/* 178 */     i++;
/* 179 */     switch (paramPosition.type) {
/*     */
/*     */
/*     */
/*     */       case INSTANCEOF:
/*     */       case NEW:
/*     */       case CONSTRUCTOR_REFERENCE:
/*     */       case METHOD_REFERENCE:
/* 187 */         i += 2;
/*     */
/*     */
/*     */
/*     */       case LOCAL_VARIABLE:
/*     */       case RESOURCE_VARIABLE:
/* 193 */         i += 2;
/* 194 */         j = paramPosition.lvarOffset.length;
/* 195 */         i += 2 * j;
/* 196 */         i += 2 * j;
/* 197 */         i += 2 * j;
/*     */
/*     */
/*     */       case EXCEPTION_PARAMETER:
/* 201 */         i += 2;
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
/*     */       case METHOD_RECEIVER:
/* 251 */         i++;
/* 252 */         i += 2 * paramPosition.location.size();
/*     */
/*     */
/* 255 */         return i;case CLASS_TYPE_PARAMETER: case METHOD_TYPE_PARAMETER: i++;case CLASS_TYPE_PARAMETER_BOUND: case METHOD_TYPE_PARAMETER_BOUND: i++; i++;case CLASS_EXTENDS: i += 2;case THROWS: i += 2;case METHOD_FORMAL_PARAMETER: i++;case CAST: case CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT: case METHOD_INVOCATION_TYPE_ARGUMENT: case CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT: case METHOD_REFERENCE_TYPE_ARGUMENT: i += 2; i++;case METHOD_RETURN: case FIELD: i++; i += 2 * paramPosition.location.size(); return i;
/*     */       case UNKNOWN:
/*     */         throw new AssertionError("TypeAnnotation: UNKNOWN target type should never occur!");
/*     */     }
/*     */     throw new AssertionError("TypeAnnotation: Unknown target type: " + paramPosition.type);
/*     */   }
/* 261 */   public static class Position { public enum TypePathEntryKind { ARRAY(0),
/* 262 */       INNER_TYPE(1),
/* 263 */       WILDCARD(2),
/* 264 */       TYPE_ARGUMENT(3);
/*     */
/*     */       public final int tag;
/*     */
/*     */       TypePathEntryKind(int param2Int1) {
/* 269 */         this.tag = param2Int1;
/*     */       } }
/*     */
/*     */
/*     */
/*     */     public static class TypePathEntry
/*     */     {
/*     */       public static final int bytesPerEntry = 2;
/*     */
/*     */       public final TypePathEntryKind tag;
/*     */       public final int arg;
/* 280 */       public static final TypePathEntry ARRAY = new TypePathEntry(TypePathEntryKind.ARRAY);
/* 281 */       public static final TypePathEntry INNER_TYPE = new TypePathEntry(TypePathEntryKind.INNER_TYPE);
/* 282 */       public static final TypePathEntry WILDCARD = new TypePathEntry(TypePathEntryKind.WILDCARD);
/*     */
/*     */       private TypePathEntry(TypePathEntryKind param2TypePathEntryKind) {
/* 285 */         if (param2TypePathEntryKind != TypePathEntryKind.ARRAY && param2TypePathEntryKind != TypePathEntryKind.INNER_TYPE && param2TypePathEntryKind != TypePathEntryKind.WILDCARD)
/*     */         {
/*     */
/* 288 */           throw new AssertionError("Invalid TypePathEntryKind: " + param2TypePathEntryKind);
/*     */         }
/* 290 */         this.tag = param2TypePathEntryKind;
/* 291 */         this.arg = 0;
/*     */       }
/*     */
/*     */       public TypePathEntry(TypePathEntryKind param2TypePathEntryKind, int param2Int) {
/* 295 */         if (param2TypePathEntryKind != TypePathEntryKind.TYPE_ARGUMENT) {
/* 296 */           throw new AssertionError("Invalid TypePathEntryKind: " + param2TypePathEntryKind);
/*     */         }
/* 298 */         this.tag = param2TypePathEntryKind;
/* 299 */         this.arg = param2Int;
/*     */       }
/*     */
/*     */       public static TypePathEntry fromBinary(int param2Int1, int param2Int2) {
/* 303 */         if (param2Int2 != 0 && param2Int1 != TypePathEntryKind.TYPE_ARGUMENT.tag) {
/* 304 */           throw new AssertionError("Invalid TypePathEntry tag/arg: " + param2Int1 + "/" + param2Int2);
/*     */         }
/* 306 */         switch (param2Int1) {
/*     */           case 0:
/* 308 */             return ARRAY;
/*     */           case 1:
/* 310 */             return INNER_TYPE;
/*     */           case 2:
/* 312 */             return WILDCARD;
/*     */           case 3:
/* 314 */             return new TypePathEntry(TypePathEntryKind.TYPE_ARGUMENT, param2Int2);
/*     */         }
/* 316 */         throw new AssertionError("Invalid TypePathEntryKind tag: " + param2Int1);
/*     */       }
/*     */
/*     */
/*     */
/*     */       public String toString() {
/* 322 */         return this.tag.toString() + ((this.tag == TypePathEntryKind.TYPE_ARGUMENT) ? ("(" + this.arg + ")") : "");
/*     */       }
/*     */
/*     */
/*     */
/*     */       public boolean equals(Object param2Object) {
/* 328 */         if (!(param2Object instanceof TypePathEntry)) {
/* 329 */           return false;
/*     */         }
/* 331 */         TypePathEntry typePathEntry = (TypePathEntry)param2Object;
/* 332 */         return (this.tag == typePathEntry.tag && this.arg == typePathEntry.arg);
/*     */       }
/*     */
/*     */
/*     */       public int hashCode() {
/* 337 */         return this.tag.hashCode() * 17 + this.arg;
/*     */       }
/*     */     }
/*     */
/* 341 */     public TargetType type = TargetType.UNKNOWN;
/*     */
/*     */
/*     */
/* 345 */     public List<TypePathEntry> location = new ArrayList<>(0);
/*     */
/*     */
/* 348 */     public int pos = -1;
/*     */
/*     */     public boolean isValidOffset = false;
/*     */
/* 352 */     public int offset = -1;
/*     */
/*     */
/* 355 */     public int[] lvarOffset = null;
/* 356 */     public int[] lvarLength = null;
/* 357 */     public int[] lvarIndex = null;
/*     */
/*     */
/* 360 */     public int bound_index = Integer.MIN_VALUE;
/*     */
/*     */
/* 363 */     public int parameter_index = Integer.MIN_VALUE;
/*     */
/*     */
/* 366 */     public int type_index = Integer.MIN_VALUE;
/*     */
/*     */
/* 369 */     public int exception_index = Integer.MIN_VALUE;
/*     */
/*     */
/*     */
/*     */     public String toString() {
/*     */       byte b;
/* 375 */       StringBuilder stringBuilder = new StringBuilder();
/* 376 */       stringBuilder.append('[');
/* 377 */       stringBuilder.append(this.type);
/*     */
/* 379 */       switch (this.type) {
/*     */
/*     */
/*     */
/*     */         case INSTANCEOF:
/*     */         case NEW:
/*     */         case CONSTRUCTOR_REFERENCE:
/*     */         case METHOD_REFERENCE:
/* 387 */           stringBuilder.append(", offset = ");
/* 388 */           stringBuilder.append(this.offset);
/*     */           break;
/*     */
/*     */
/*     */         case LOCAL_VARIABLE:
/*     */         case RESOURCE_VARIABLE:
/* 394 */           if (this.lvarOffset == null) {
/* 395 */             stringBuilder.append(", lvarOffset is null!");
/*     */             break;
/*     */           }
/* 398 */           stringBuilder.append(", {");
/* 399 */           for (b = 0; b < this.lvarOffset.length; b++) {
/* 400 */             if (b != 0) stringBuilder.append("; ");
/* 401 */             stringBuilder.append("start_pc = ");
/* 402 */             stringBuilder.append(this.lvarOffset[b]);
/* 403 */             stringBuilder.append(", length = ");
/* 404 */             stringBuilder.append(this.lvarLength[b]);
/* 405 */             stringBuilder.append(", index = ");
/* 406 */             stringBuilder.append(this.lvarIndex[b]);
/*     */           }
/* 408 */           stringBuilder.append("}");
/*     */           break;
/*     */
/*     */
/*     */         case METHOD_RECEIVER:
/*     */           break;
/*     */
/*     */         case CLASS_TYPE_PARAMETER:
/*     */         case METHOD_TYPE_PARAMETER:
/* 417 */           stringBuilder.append(", param_index = ");
/* 418 */           stringBuilder.append(this.parameter_index);
/*     */           break;
/*     */
/*     */         case CLASS_TYPE_PARAMETER_BOUND:
/*     */         case METHOD_TYPE_PARAMETER_BOUND:
/* 423 */           stringBuilder.append(", param_index = ");
/* 424 */           stringBuilder.append(this.parameter_index);
/* 425 */           stringBuilder.append(", bound_index = ");
/* 426 */           stringBuilder.append(this.bound_index);
/*     */           break;
/*     */
/*     */         case CLASS_EXTENDS:
/* 430 */           stringBuilder.append(", type_index = ");
/* 431 */           stringBuilder.append(this.type_index);
/*     */           break;
/*     */
/*     */         case THROWS:
/* 435 */           stringBuilder.append(", type_index = ");
/* 436 */           stringBuilder.append(this.type_index);
/*     */           break;
/*     */
/*     */         case EXCEPTION_PARAMETER:
/* 440 */           stringBuilder.append(", exception_index = ");
/* 441 */           stringBuilder.append(this.exception_index);
/*     */           break;
/*     */
/*     */         case METHOD_FORMAL_PARAMETER:
/* 445 */           stringBuilder.append(", param_index = ");
/* 446 */           stringBuilder.append(this.parameter_index);
/*     */           break;
/*     */
/*     */
/*     */         case CAST:
/*     */         case CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT:
/*     */         case METHOD_INVOCATION_TYPE_ARGUMENT:
/*     */         case CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT:
/*     */         case METHOD_REFERENCE_TYPE_ARGUMENT:
/* 455 */           stringBuilder.append(", offset = ");
/* 456 */           stringBuilder.append(this.offset);
/* 457 */           stringBuilder.append(", type_index = ");
/* 458 */           stringBuilder.append(this.type_index);
/*     */           break;
/*     */
/*     */         case METHOD_RETURN:
/*     */         case FIELD:
/*     */           break;
/*     */         case UNKNOWN:
/* 465 */           stringBuilder.append(", position UNKNOWN!");
/*     */           break;
/*     */         default:
/* 468 */           throw new AssertionError("Unknown target type: " + this.type);
/*     */       }
/*     */
/*     */
/* 472 */       if (!this.location.isEmpty()) {
/* 473 */         stringBuilder.append(", location = (");
/* 474 */         stringBuilder.append(this.location);
/* 475 */         stringBuilder.append(")");
/*     */       }
/*     */
/* 478 */       stringBuilder.append(", pos = ");
/* 479 */       stringBuilder.append(this.pos);
/*     */
/* 481 */       stringBuilder.append(']');
/* 482 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean emitToClassfile() {
/* 491 */       return (!this.type.isLocal() || this.isValidOffset);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public static List<TypePathEntry> getTypePathFromBinary(List<Integer> param1List) {
/* 501 */       ArrayList<TypePathEntry> arrayList = new ArrayList(param1List.size() / 2);
/* 502 */       byte b = 0;
/* 503 */       while (b < param1List.size()) {
/* 504 */         if (b + 1 == param1List.size()) {
/* 505 */           throw new AssertionError("Could not decode type path: " + param1List);
/*     */         }
/* 507 */         arrayList.add(TypePathEntry.fromBinary(((Integer)param1List.get(b)).intValue(), ((Integer)param1List.get(b + 1)).intValue()));
/* 508 */         b += 2;
/*     */       }
/* 510 */       return arrayList;
/*     */     }
/*     */
/*     */     public static List<Integer> getBinaryFromTypePath(List<TypePathEntry> param1List) {
/* 514 */       ArrayList<Integer> arrayList = new ArrayList(param1List.size() * 2);
/* 515 */       for (TypePathEntry typePathEntry : param1List) {
/* 516 */         arrayList.add(Integer.valueOf(typePathEntry.tag.tag));
/* 517 */         arrayList.add(Integer.valueOf(typePathEntry.arg));
/*     */       }
/* 519 */       return arrayList;
/*     */     } }
/*     */
/*     */   public static class TypePathEntry {
/*     */     public static final int bytesPerEntry = 2;
/*     */     public final Position.TypePathEntryKind tag; public final int arg; public static final TypePathEntry ARRAY = new TypePathEntry(Position.TypePathEntryKind.ARRAY); public static final TypePathEntry INNER_TYPE = new TypePathEntry(Position.TypePathEntryKind.INNER_TYPE); public static final TypePathEntry WILDCARD = new TypePathEntry(Position.TypePathEntryKind.WILDCARD); private TypePathEntry(Position.TypePathEntryKind param1TypePathEntryKind) { if (param1TypePathEntryKind != Position.TypePathEntryKind.ARRAY && param1TypePathEntryKind != Position.TypePathEntryKind.INNER_TYPE && param1TypePathEntryKind != Position.TypePathEntryKind.WILDCARD) throw new AssertionError("Invalid TypePathEntryKind: " + param1TypePathEntryKind);  this.tag = param1TypePathEntryKind; this.arg = 0; } public TypePathEntry(Position.TypePathEntryKind param1TypePathEntryKind, int param1Int) { if (param1TypePathEntryKind != Position.TypePathEntryKind.TYPE_ARGUMENT) throw new AssertionError("Invalid TypePathEntryKind: " + param1TypePathEntryKind);  this.tag = param1TypePathEntryKind; this.arg = param1Int; } public static TypePathEntry fromBinary(int param1Int1, int param1Int2) { if (param1Int2 != 0 && param1Int1 != Position.TypePathEntryKind.TYPE_ARGUMENT.tag) throw new AssertionError("Invalid TypePathEntry tag/arg: " + param1Int1 + "/" + param1Int2);  switch (param1Int1) { case 0: return ARRAY;case 1: return INNER_TYPE;case 2: return WILDCARD;case 3: return new TypePathEntry(Position.TypePathEntryKind.TYPE_ARGUMENT, param1Int2); }  throw new AssertionError("Invalid TypePathEntryKind tag: " + param1Int1); } public String toString() { return this.tag.toString() + ((this.tag == Position.TypePathEntryKind.TYPE_ARGUMENT) ? ("(" + this.arg + ")") : ""); } public boolean equals(Object param1Object) { if (!(param1Object instanceof TypePathEntry))
/*     */         return false;  TypePathEntry typePathEntry = (TypePathEntry)param1Object; return (this.tag == typePathEntry.tag && this.arg == typePathEntry.arg); } public int hashCode() { return this.tag.hashCode() * 17 + this.arg; }
/*     */   } public enum TargetType {
/* 527 */     CLASS_TYPE_PARAMETER(0),
/*     */
/*     */
/* 530 */     METHOD_TYPE_PARAMETER(1),
/*     */
/*     */
/* 533 */     CLASS_EXTENDS(16),
/*     */
/*     */
/* 536 */     CLASS_TYPE_PARAMETER_BOUND(17),
/*     */
/*     */
/* 539 */     METHOD_TYPE_PARAMETER_BOUND(18),
/*     */
/*     */
/* 542 */     FIELD(19),
/*     */
/*     */
/* 545 */     METHOD_RETURN(20),
/*     */
/*     */
/* 548 */     METHOD_RECEIVER(21),
/*     */
/*     */
/* 551 */     METHOD_FORMAL_PARAMETER(22),
/*     */
/*     */
/* 554 */     THROWS(23),
/*     */
/*     */
/* 557 */     LOCAL_VARIABLE(64, true),
/*     */
/*     */
/* 560 */     RESOURCE_VARIABLE(65, true),
/*     */
/*     */
/* 563 */     EXCEPTION_PARAMETER(66, true),
/*     */
/*     */
/* 566 */     INSTANCEOF(67, true),
/*     */
/*     */
/* 569 */     NEW(68, true),
/*     */
/*     */
/* 572 */     CONSTRUCTOR_REFERENCE(69, true),
/*     */
/*     */
/* 575 */     METHOD_REFERENCE(70, true),
/*     */
/*     */
/* 578 */     CAST(71, true),
/*     */
/*     */
/* 581 */     CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT(72, true),
/*     */
/*     */
/* 584 */     METHOD_INVOCATION_TYPE_ARGUMENT(73, true),
/*     */
/*     */
/* 587 */     CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT(74, true),
/*     */
/*     */
/* 590 */     METHOD_REFERENCE_TYPE_ARGUMENT(75, true),
/*     */
/*     */
/* 593 */     UNKNOWN(255);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private static final int MAXIMUM_TARGET_TYPE_VALUE = 75;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private final int targetTypeValue;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private final boolean isLocal;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 630 */     private static final TargetType[] targets = new TargetType[76];
/* 631 */     TargetType(int param1Int1, boolean param1Boolean) { if (param1Int1 < 0 || param1Int1 > 255) throw new AssertionError("Attribute type value needs to be an unsigned byte: " + String.format("0x%02X", new Object[] { Integer.valueOf(param1Int1) }));  this.targetTypeValue = param1Int1; this.isLocal = param1Boolean; } public boolean isLocal() { return this.isLocal; } public int targetTypeValue() { return this.targetTypeValue; } static { TargetType[] arrayOfTargetType = values();
/* 632 */       for (TargetType targetType : arrayOfTargetType) {
/* 633 */         if (targetType.targetTypeValue != UNKNOWN.targetTypeValue)
/* 634 */           targets[targetType.targetTypeValue] = targetType;
/*     */       }
/* 636 */       for (byte b = 0; b <= 75; b++) {
/* 637 */         if (targets[b] == null)
/* 638 */           targets[b] = UNKNOWN;
/*     */       }  }
/*     */
/*     */
/*     */     public static boolean isValidTargetTypeValue(int param1Int) {
/* 643 */       if (param1Int == UNKNOWN.targetTypeValue)
/* 644 */         return true;
/* 645 */       return (param1Int >= 0 && param1Int < targets.length);
/*     */     }
/*     */
/*     */     public static TargetType fromTargetTypeValue(int param1Int) {
/* 649 */       if (param1Int == UNKNOWN.targetTypeValue) {
/* 650 */         return UNKNOWN;
/*     */       }
/* 652 */       if (param1Int < 0 || param1Int >= targets.length)
/* 653 */         throw new AssertionError("Unknown TargetType: " + param1Int);
/* 654 */       return targets[param1Int];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\TypeAnnotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
