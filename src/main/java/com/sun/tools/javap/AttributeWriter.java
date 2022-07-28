/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.AccessFlags;
/*     */ import com.sun.tools.classfile.AnnotationDefault_attribute;
/*     */ import com.sun.tools.classfile.Attribute;
/*     */ import com.sun.tools.classfile.Attributes;
/*     */ import com.sun.tools.classfile.BootstrapMethods_attribute;
/*     */ import com.sun.tools.classfile.CharacterRangeTable_attribute;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.CompilationID_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.ConstantValue_attribute;
/*     */ import com.sun.tools.classfile.DefaultAttribute;
/*     */ import com.sun.tools.classfile.Deprecated_attribute;
/*     */ import com.sun.tools.classfile.EnclosingMethod_attribute;
/*     */ import com.sun.tools.classfile.Exceptions_attribute;
/*     */ import com.sun.tools.classfile.InnerClasses_attribute;
/*     */ import com.sun.tools.classfile.LineNumberTable_attribute;
/*     */ import com.sun.tools.classfile.LocalVariableTable_attribute;
/*     */ import com.sun.tools.classfile.LocalVariableTypeTable_attribute;
/*     */ import com.sun.tools.classfile.MethodParameters_attribute;
/*     */ import com.sun.tools.classfile.RuntimeInvisibleAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeInvisibleParameterAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeInvisibleTypeAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeVisibleAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeVisibleParameterAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeVisibleTypeAnnotations_attribute;
/*     */ import com.sun.tools.classfile.Signature_attribute;
/*     */ import com.sun.tools.classfile.SourceDebugExtension_attribute;
/*     */ import com.sun.tools.classfile.SourceFile_attribute;
/*     */ import com.sun.tools.classfile.SourceID_attribute;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute;
/*     */ import com.sun.tools.classfile.StackMap_attribute;
/*     */ import com.sun.tools.classfile.Synthetic_attribute;
/*     */ import com.sun.tools.javac.util.StringUtils;
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
/*     */ public class AttributeWriter
/*     */   extends BasicWriter
/*     */   implements Attribute.Visitor<Void, Void>
/*     */ {
/*     */   private static final String format = "%-31s%s";
/*     */   private AnnotationWriter annotationWriter;
/*     */   private CodeWriter codeWriter;
/*     */   private ConstantWriter constantWriter;
/*     */   private Options options;
/*     */   private ConstantPool constant_pool;
/*     */   private Object owner;
/*     */   
/*     */   public static AttributeWriter instance(Context paramContext) {
/*  79 */     AttributeWriter attributeWriter = paramContext.<AttributeWriter>get(AttributeWriter.class);
/*  80 */     if (attributeWriter == null)
/*  81 */       attributeWriter = new AttributeWriter(paramContext); 
/*  82 */     return attributeWriter;
/*     */   }
/*     */   
/*     */   protected AttributeWriter(Context paramContext) {
/*  86 */     super(paramContext);
/*  87 */     paramContext.put(AttributeWriter.class, this);
/*  88 */     this.annotationWriter = AnnotationWriter.instance(paramContext);
/*  89 */     this.codeWriter = CodeWriter.instance(paramContext);
/*  90 */     this.constantWriter = ConstantWriter.instance(paramContext);
/*  91 */     this.options = Options.instance(paramContext);
/*     */   }
/*     */   
/*     */   public void write(Object paramObject, Attribute paramAttribute, ConstantPool paramConstantPool) {
/*  95 */     if (paramAttribute != null) {
/*     */       
/*  97 */       paramObject.getClass();
/*  98 */       paramConstantPool.getClass();
/*  99 */       this.constant_pool = paramConstantPool;
/* 100 */       this.owner = paramObject;
/* 101 */       paramAttribute.accept(this, null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void write(Object paramObject, Attributes paramAttributes, ConstantPool paramConstantPool) {
/* 106 */     if (paramAttributes != null) {
/*     */       
/* 108 */       paramObject.getClass();
/* 109 */       paramConstantPool.getClass();
/* 110 */       this.constant_pool = paramConstantPool;
/* 111 */       this.owner = paramObject;
/* 112 */       for (Attribute attribute : paramAttributes)
/* 113 */         attribute.accept(this, null); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Void visitDefault(DefaultAttribute paramDefaultAttribute, Void paramVoid) {
/* 118 */     if (paramDefaultAttribute.reason != null) {
/* 119 */       report(paramDefaultAttribute.reason);
/*     */     }
/* 121 */     byte[] arrayOfByte = paramDefaultAttribute.info;
/* 122 */     byte b1 = 0;
/* 123 */     byte b2 = 0;
/* 124 */     print("  ");
/*     */     try {
/* 126 */       print(paramDefaultAttribute.getName(this.constant_pool));
/* 127 */     } catch (ConstantPoolException constantPoolException) {
/* 128 */       report(constantPoolException);
/* 129 */       print("attribute name = #" + paramDefaultAttribute.attribute_name_index);
/*     */     } 
/* 131 */     print(": ");
/* 132 */     println("length = 0x" + toHex(paramDefaultAttribute.info.length));
/*     */     
/* 134 */     print("   ");
/*     */     
/* 136 */     while (b1 < arrayOfByte.length) {
/* 137 */       print(toHex(arrayOfByte[b1], 2));
/*     */       
/* 139 */       b2++;
/* 140 */       if (b2 == 16) {
/* 141 */         println();
/* 142 */         print("   ");
/* 143 */         b2 = 0;
/*     */       } else {
/* 145 */         print(" ");
/*     */       } 
/* 147 */       b1++;
/*     */     } 
/* 149 */     println();
/* 150 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitAnnotationDefault(AnnotationDefault_attribute paramAnnotationDefault_attribute, Void paramVoid) {
/* 154 */     println("AnnotationDefault:");
/* 155 */     indent(1);
/* 156 */     print("default_value: ");
/* 157 */     this.annotationWriter.write(paramAnnotationDefault_attribute.default_value);
/* 158 */     indent(-1);
/* 159 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitBootstrapMethods(BootstrapMethods_attribute paramBootstrapMethods_attribute, Void paramVoid) {
/* 163 */     println("BootstrapMethods:");
/* 164 */     for (byte b = 0; b < paramBootstrapMethods_attribute.bootstrap_method_specifiers.length; b++) {
/* 165 */       BootstrapMethods_attribute.BootstrapMethodSpecifier bootstrapMethodSpecifier = paramBootstrapMethods_attribute.bootstrap_method_specifiers[b];
/* 166 */       indent(1);
/* 167 */       print(b + ": #" + bootstrapMethodSpecifier.bootstrap_method_ref + " ");
/* 168 */       println(this.constantWriter.stringValue(bootstrapMethodSpecifier.bootstrap_method_ref));
/* 169 */       indent(1);
/* 170 */       println("Method arguments:");
/* 171 */       indent(1);
/* 172 */       for (byte b1 = 0; b1 < bootstrapMethodSpecifier.bootstrap_arguments.length; b1++) {
/* 173 */         print("#" + bootstrapMethodSpecifier.bootstrap_arguments[b1] + " ");
/* 174 */         println(this.constantWriter.stringValue(bootstrapMethodSpecifier.bootstrap_arguments[b1]));
/*     */       } 
/* 176 */       indent(-3);
/*     */     } 
/* 178 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitCharacterRangeTable(CharacterRangeTable_attribute paramCharacterRangeTable_attribute, Void paramVoid) {
/* 182 */     println("CharacterRangeTable:");
/* 183 */     indent(1);
/* 184 */     for (byte b = 0; b < paramCharacterRangeTable_attribute.character_range_table.length; b++) {
/* 185 */       CharacterRangeTable_attribute.Entry entry = paramCharacterRangeTable_attribute.character_range_table[b];
/* 186 */       print(String.format("    %2d, %2d, %6x, %6x, %4x", new Object[] {
/* 187 */               Integer.valueOf(entry.start_pc), Integer.valueOf(entry.end_pc), 
/* 188 */               Integer.valueOf(entry.character_range_start), Integer.valueOf(entry.character_range_end), 
/* 189 */               Integer.valueOf(entry.flags) }));
/* 190 */       tab();
/* 191 */       print(String.format("// %2d, %2d, %4d:%02d, %4d:%02d", new Object[] {
/* 192 */               Integer.valueOf(entry.start_pc), Integer.valueOf(entry.end_pc), 
/* 193 */               Integer.valueOf(entry.character_range_start >> 10), Integer.valueOf(entry.character_range_start & 0x3FF), 
/* 194 */               Integer.valueOf(entry.character_range_end >> 10), Integer.valueOf(entry.character_range_end & 0x3FF) }));
/* 195 */       if ((entry.flags & 0x1) != 0)
/* 196 */         print(", statement"); 
/* 197 */       if ((entry.flags & 0x2) != 0)
/* 198 */         print(", block"); 
/* 199 */       if ((entry.flags & 0x4) != 0)
/* 200 */         print(", assignment"); 
/* 201 */       if ((entry.flags & 0x8) != 0)
/* 202 */         print(", flow-controller"); 
/* 203 */       if ((entry.flags & 0x10) != 0)
/* 204 */         print(", flow-target"); 
/* 205 */       if ((entry.flags & 0x20) != 0)
/* 206 */         print(", invoke"); 
/* 207 */       if ((entry.flags & 0x40) != 0)
/* 208 */         print(", create"); 
/* 209 */       if ((entry.flags & 0x80) != 0)
/* 210 */         print(", branch-true"); 
/* 211 */       if ((entry.flags & 0x100) != 0)
/* 212 */         print(", branch-false"); 
/* 213 */       println();
/*     */     } 
/* 215 */     indent(-1);
/* 216 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitCode(Code_attribute paramCode_attribute, Void paramVoid) {
/* 220 */     this.codeWriter.write(paramCode_attribute, this.constant_pool);
/* 221 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitCompilationID(CompilationID_attribute paramCompilationID_attribute, Void paramVoid) {
/* 225 */     this.constantWriter.write(paramCompilationID_attribute.compilationID_index);
/* 226 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitConstantValue(ConstantValue_attribute paramConstantValue_attribute, Void paramVoid) {
/* 230 */     print("ConstantValue: ");
/* 231 */     this.constantWriter.write(paramConstantValue_attribute.constantvalue_index);
/* 232 */     println();
/* 233 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitDeprecated(Deprecated_attribute paramDeprecated_attribute, Void paramVoid) {
/* 237 */     println("Deprecated: true");
/* 238 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitEnclosingMethod(EnclosingMethod_attribute paramEnclosingMethod_attribute, Void paramVoid) {
/* 242 */     print("EnclosingMethod: #" + paramEnclosingMethod_attribute.class_index + ".#" + paramEnclosingMethod_attribute.method_index);
/* 243 */     tab();
/* 244 */     print("// " + getJavaClassName(paramEnclosingMethod_attribute));
/* 245 */     if (paramEnclosingMethod_attribute.method_index != 0)
/* 246 */       print("." + getMethodName(paramEnclosingMethod_attribute)); 
/* 247 */     println();
/* 248 */     return null;
/*     */   }
/*     */   
/*     */   private String getJavaClassName(EnclosingMethod_attribute paramEnclosingMethod_attribute) {
/*     */     try {
/* 253 */       return getJavaName(paramEnclosingMethod_attribute.getClassName(this.constant_pool));
/* 254 */     } catch (ConstantPoolException constantPoolException) {
/* 255 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getMethodName(EnclosingMethod_attribute paramEnclosingMethod_attribute) {
/*     */     try {
/* 261 */       return paramEnclosingMethod_attribute.getMethodName(this.constant_pool);
/* 262 */     } catch (ConstantPoolException constantPoolException) {
/* 263 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Void visitExceptions(Exceptions_attribute paramExceptions_attribute, Void paramVoid) {
/* 268 */     println("Exceptions:");
/* 269 */     indent(1);
/* 270 */     print("throws ");
/* 271 */     for (byte b = 0; b < paramExceptions_attribute.number_of_exceptions; b++) {
/* 272 */       if (b > 0)
/* 273 */         print(", "); 
/* 274 */       print(getJavaException(paramExceptions_attribute, b));
/*     */     } 
/* 276 */     println();
/* 277 */     indent(-1);
/* 278 */     return null;
/*     */   }
/*     */   
/*     */   private String getJavaException(Exceptions_attribute paramExceptions_attribute, int paramInt) {
/*     */     try {
/* 283 */       return getJavaName(paramExceptions_attribute.getException(paramInt, this.constant_pool));
/* 284 */     } catch (ConstantPoolException constantPoolException) {
/* 285 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Void visitInnerClasses(InnerClasses_attribute paramInnerClasses_attribute, Void paramVoid) {
/* 290 */     boolean bool = true;
/* 291 */     for (byte b = 0; b < paramInnerClasses_attribute.classes.length; b++) {
/* 292 */       InnerClasses_attribute.Info info = paramInnerClasses_attribute.classes[b];
/*     */       
/* 294 */       AccessFlags accessFlags = info.inner_class_access_flags;
/* 295 */       if (this.options.checkAccess(accessFlags)) {
/* 296 */         if (bool) {
/* 297 */           writeInnerClassHeader();
/* 298 */           bool = false;
/*     */         } 
/* 300 */         print("   ");
/* 301 */         for (String str : accessFlags.getInnerClassModifiers())
/* 302 */           print(str + " "); 
/* 303 */         if (info.inner_name_index != 0) {
/* 304 */           print("#" + info.inner_name_index + "= ");
/*     */         }
/* 306 */         print("#" + info.inner_class_info_index);
/* 307 */         if (info.outer_class_info_index != 0) {
/* 308 */           print(" of #" + info.outer_class_info_index);
/*     */         }
/* 310 */         print("; //");
/* 311 */         if (info.inner_name_index != 0) {
/* 312 */           print(getInnerName(this.constant_pool, info) + "=");
/*     */         }
/* 314 */         this.constantWriter.write(info.inner_class_info_index);
/* 315 */         if (info.outer_class_info_index != 0) {
/* 316 */           print(" of ");
/* 317 */           this.constantWriter.write(info.outer_class_info_index);
/*     */         } 
/* 319 */         println();
/*     */       } 
/*     */     } 
/* 322 */     if (!bool)
/* 323 */       indent(-1); 
/* 324 */     return null;
/*     */   }
/*     */   
/*     */   String getInnerName(ConstantPool paramConstantPool, InnerClasses_attribute.Info paramInfo) {
/*     */     try {
/* 329 */       return paramInfo.getInnerName(paramConstantPool);
/* 330 */     } catch (ConstantPoolException constantPoolException) {
/* 331 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeInnerClassHeader() {
/* 336 */     println("InnerClasses:");
/* 337 */     indent(1);
/*     */   }
/*     */   
/*     */   public Void visitLineNumberTable(LineNumberTable_attribute paramLineNumberTable_attribute, Void paramVoid) {
/* 341 */     println("LineNumberTable:");
/* 342 */     indent(1);
/* 343 */     for (LineNumberTable_attribute.Entry entry : paramLineNumberTable_attribute.line_number_table) {
/* 344 */       println("line " + entry.line_number + ": " + entry.start_pc);
/*     */     }
/* 346 */     indent(-1);
/* 347 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitLocalVariableTable(LocalVariableTable_attribute paramLocalVariableTable_attribute, Void paramVoid) {
/* 351 */     println("LocalVariableTable:");
/* 352 */     indent(1);
/* 353 */     println("Start  Length  Slot  Name   Signature");
/* 354 */     for (LocalVariableTable_attribute.Entry entry : paramLocalVariableTable_attribute.local_variable_table) {
/* 355 */       println(String.format("%5d %7d %5d %5s   %s", new Object[] {
/* 356 */               Integer.valueOf(entry.start_pc), Integer.valueOf(entry.length), Integer.valueOf(entry.index), this.constantWriter
/* 357 */               .stringValue(entry.name_index), this.constantWriter
/* 358 */               .stringValue(entry.descriptor_index) }));
/*     */     } 
/* 360 */     indent(-1);
/* 361 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitLocalVariableTypeTable(LocalVariableTypeTable_attribute paramLocalVariableTypeTable_attribute, Void paramVoid) {
/* 365 */     println("LocalVariableTypeTable:");
/* 366 */     indent(1);
/* 367 */     println("Start  Length  Slot  Name   Signature");
/* 368 */     for (LocalVariableTypeTable_attribute.Entry entry : paramLocalVariableTypeTable_attribute.local_variable_table) {
/* 369 */       println(String.format("%5d %7d %5d %5s   %s", new Object[] {
/* 370 */               Integer.valueOf(entry.start_pc), Integer.valueOf(entry.length), Integer.valueOf(entry.index), this.constantWriter
/* 371 */               .stringValue(entry.name_index), this.constantWriter
/* 372 */               .stringValue(entry.signature_index) }));
/*     */     } 
/* 374 */     indent(-1);
/* 375 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Void visitMethodParameters(MethodParameters_attribute paramMethodParameters_attribute, Void paramVoid) {
/* 383 */     String str = String.format("%-31s%s", new Object[] { "Name", "Flags" });
/* 384 */     println("MethodParameters:");
/* 385 */     indent(1);
/* 386 */     println(str);
/*     */     
/* 388 */     for (MethodParameters_attribute.Entry entry : paramMethodParameters_attribute.method_parameter_table) {
/*     */ 
/*     */       
/* 391 */       String str1 = (entry.name_index != 0) ? this.constantWriter.stringValue(entry.name_index) : "<no name>";
/* 392 */       String str2 = ((0 != (entry.flags & 0x10)) ? "final " : "") + ((0 != (entry.flags & 0x8000)) ? "mandated " : "") + ((0 != (entry.flags & 0x1000)) ? "synthetic" : "");
/*     */ 
/*     */ 
/*     */       
/* 396 */       println(String.format("%-31s%s", new Object[] { str1, str2 }));
/*     */     } 
/* 398 */     indent(-1);
/* 399 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations_attribute paramRuntimeVisibleAnnotations_attribute, Void paramVoid) {
/* 403 */     println("RuntimeVisibleAnnotations:");
/* 404 */     indent(1);
/* 405 */     for (byte b = 0; b < paramRuntimeVisibleAnnotations_attribute.annotations.length; b++) {
/* 406 */       print(b + ": ");
/* 407 */       this.annotationWriter.write(paramRuntimeVisibleAnnotations_attribute.annotations[b]);
/* 408 */       println();
/*     */     } 
/* 410 */     indent(-1);
/* 411 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitRuntimeInvisibleAnnotations(RuntimeInvisibleAnnotations_attribute paramRuntimeInvisibleAnnotations_attribute, Void paramVoid) {
/* 415 */     println("RuntimeInvisibleAnnotations:");
/* 416 */     indent(1);
/* 417 */     for (byte b = 0; b < paramRuntimeInvisibleAnnotations_attribute.annotations.length; b++) {
/* 418 */       print(b + ": ");
/* 419 */       this.annotationWriter.write(paramRuntimeInvisibleAnnotations_attribute.annotations[b]);
/* 420 */       println();
/*     */     } 
/* 422 */     indent(-1);
/* 423 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitRuntimeVisibleTypeAnnotations(RuntimeVisibleTypeAnnotations_attribute paramRuntimeVisibleTypeAnnotations_attribute, Void paramVoid) {
/* 427 */     println("RuntimeVisibleTypeAnnotations:");
/* 428 */     indent(1);
/* 429 */     for (byte b = 0; b < paramRuntimeVisibleTypeAnnotations_attribute.annotations.length; b++) {
/* 430 */       print(b + ": ");
/* 431 */       this.annotationWriter.write(paramRuntimeVisibleTypeAnnotations_attribute.annotations[b]);
/* 432 */       println();
/*     */     } 
/* 434 */     indent(-1);
/* 435 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitRuntimeInvisibleTypeAnnotations(RuntimeInvisibleTypeAnnotations_attribute paramRuntimeInvisibleTypeAnnotations_attribute, Void paramVoid) {
/* 439 */     println("RuntimeInvisibleTypeAnnotations:");
/* 440 */     indent(1);
/* 441 */     for (byte b = 0; b < paramRuntimeInvisibleTypeAnnotations_attribute.annotations.length; b++) {
/* 442 */       print(b + ": ");
/* 443 */       this.annotationWriter.write(paramRuntimeInvisibleTypeAnnotations_attribute.annotations[b]);
/* 444 */       println();
/*     */     } 
/* 446 */     indent(-1);
/* 447 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitRuntimeVisibleParameterAnnotations(RuntimeVisibleParameterAnnotations_attribute paramRuntimeVisibleParameterAnnotations_attribute, Void paramVoid) {
/* 451 */     println("RuntimeVisibleParameterAnnotations:");
/* 452 */     indent(1);
/* 453 */     for (byte b = 0; b < paramRuntimeVisibleParameterAnnotations_attribute.parameter_annotations.length; b++) {
/* 454 */       println("parameter " + b + ": ");
/* 455 */       indent(1);
/* 456 */       for (byte b1 = 0; b1 < (paramRuntimeVisibleParameterAnnotations_attribute.parameter_annotations[b]).length; b1++) {
/* 457 */         print(b1 + ": ");
/* 458 */         this.annotationWriter.write(paramRuntimeVisibleParameterAnnotations_attribute.parameter_annotations[b][b1]);
/* 459 */         println();
/*     */       } 
/* 461 */       indent(-1);
/*     */     } 
/* 463 */     indent(-1);
/* 464 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitRuntimeInvisibleParameterAnnotations(RuntimeInvisibleParameterAnnotations_attribute paramRuntimeInvisibleParameterAnnotations_attribute, Void paramVoid) {
/* 468 */     println("RuntimeInvisibleParameterAnnotations:");
/* 469 */     indent(1);
/* 470 */     for (byte b = 0; b < paramRuntimeInvisibleParameterAnnotations_attribute.parameter_annotations.length; b++) {
/* 471 */       println(b + ": ");
/* 472 */       indent(1);
/* 473 */       for (byte b1 = 0; b1 < (paramRuntimeInvisibleParameterAnnotations_attribute.parameter_annotations[b]).length; b1++) {
/* 474 */         print(b1 + ": ");
/* 475 */         this.annotationWriter.write(paramRuntimeInvisibleParameterAnnotations_attribute.parameter_annotations[b][b1]);
/* 476 */         println();
/*     */       } 
/* 478 */       indent(-1);
/*     */     } 
/* 480 */     indent(-1);
/* 481 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitSignature(Signature_attribute paramSignature_attribute, Void paramVoid) {
/* 485 */     print("Signature: #" + paramSignature_attribute.signature_index);
/* 486 */     tab();
/* 487 */     println("// " + getSignature(paramSignature_attribute));
/* 488 */     return null;
/*     */   }
/*     */   
/*     */   String getSignature(Signature_attribute paramSignature_attribute) {
/*     */     try {
/* 493 */       return paramSignature_attribute.getSignature(this.constant_pool);
/* 494 */     } catch (ConstantPoolException constantPoolException) {
/* 495 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Void visitSourceDebugExtension(SourceDebugExtension_attribute paramSourceDebugExtension_attribute, Void paramVoid) {
/* 500 */     println("SourceDebugExtension:");
/* 501 */     indent(1);
/* 502 */     for (String str : paramSourceDebugExtension_attribute.getValue().split("[\r\n]+")) {
/* 503 */       println(str);
/*     */     }
/* 505 */     indent(-1);
/* 506 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitSourceFile(SourceFile_attribute paramSourceFile_attribute, Void paramVoid) {
/* 510 */     println("SourceFile: \"" + getSourceFile(paramSourceFile_attribute) + "\"");
/* 511 */     return null;
/*     */   }
/*     */   
/*     */   private String getSourceFile(SourceFile_attribute paramSourceFile_attribute) {
/*     */     try {
/* 516 */       return paramSourceFile_attribute.getSourceFile(this.constant_pool);
/* 517 */     } catch (ConstantPoolException constantPoolException) {
/* 518 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Void visitSourceID(SourceID_attribute paramSourceID_attribute, Void paramVoid) {
/* 523 */     this.constantWriter.write(paramSourceID_attribute.sourceID_index);
/* 524 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitStackMap(StackMap_attribute paramStackMap_attribute, Void paramVoid) {
/* 528 */     println("StackMap: number_of_entries = " + paramStackMap_attribute.number_of_entries);
/* 529 */     indent(1);
/* 530 */     StackMapTableWriter stackMapTableWriter = new StackMapTableWriter();
/* 531 */     for (StackMap_attribute.stack_map_frame stack_map_frame : paramStackMap_attribute.entries) {
/* 532 */       stackMapTableWriter.write((StackMapTable_attribute.stack_map_frame)stack_map_frame);
/*     */     }
/* 534 */     indent(-1);
/* 535 */     return null;
/*     */   }
/*     */   
/*     */   public Void visitStackMapTable(StackMapTable_attribute paramStackMapTable_attribute, Void paramVoid) {
/* 539 */     println("StackMapTable: number_of_entries = " + paramStackMapTable_attribute.number_of_entries);
/* 540 */     indent(1);
/* 541 */     StackMapTableWriter stackMapTableWriter = new StackMapTableWriter();
/* 542 */     for (StackMapTable_attribute.stack_map_frame stack_map_frame : paramStackMapTable_attribute.entries) {
/* 543 */       stackMapTableWriter.write(stack_map_frame);
/*     */     }
/* 545 */     indent(-1);
/* 546 */     return null;
/*     */   }
/*     */   
/*     */   class StackMapTableWriter
/*     */     implements StackMapTable_attribute.stack_map_frame.Visitor<Void, Void> {
/*     */     public void write(StackMapTable_attribute.stack_map_frame param1stack_map_frame) {
/* 552 */       param1stack_map_frame.accept(this, null);
/*     */     }
/*     */     
/*     */     public Void visit_same_frame(StackMapTable_attribute.same_frame param1same_frame, Void param1Void) {
/* 556 */       printHeader((StackMapTable_attribute.stack_map_frame)param1same_frame, "/* same */");
/* 557 */       return null;
/*     */     }
/*     */     
/*     */     public Void visit_same_locals_1_stack_item_frame(StackMapTable_attribute.same_locals_1_stack_item_frame param1same_locals_1_stack_item_frame, Void param1Void) {
/* 561 */       printHeader((StackMapTable_attribute.stack_map_frame)param1same_locals_1_stack_item_frame, "/* same_locals_1_stack_item */");
/* 562 */       AttributeWriter.this.indent(1);
/* 563 */       printMap("stack", param1same_locals_1_stack_item_frame.stack);
/* 564 */       AttributeWriter.this.indent(-1);
/* 565 */       return null;
/*     */     }
/*     */     
/*     */     public Void visit_same_locals_1_stack_item_frame_extended(StackMapTable_attribute.same_locals_1_stack_item_frame_extended param1same_locals_1_stack_item_frame_extended, Void param1Void) {
/* 569 */       printHeader((StackMapTable_attribute.stack_map_frame)param1same_locals_1_stack_item_frame_extended, "/* same_locals_1_stack_item_frame_extended */");
/* 570 */       AttributeWriter.this.indent(1);
/* 571 */       AttributeWriter.this.println("offset_delta = " + param1same_locals_1_stack_item_frame_extended.offset_delta);
/* 572 */       printMap("stack", param1same_locals_1_stack_item_frame_extended.stack);
/* 573 */       AttributeWriter.this.indent(-1);
/* 574 */       return null;
/*     */     }
/*     */     
/*     */     public Void visit_chop_frame(StackMapTable_attribute.chop_frame param1chop_frame, Void param1Void) {
/* 578 */       printHeader((StackMapTable_attribute.stack_map_frame)param1chop_frame, "/* chop */");
/* 579 */       AttributeWriter.this.indent(1);
/* 580 */       AttributeWriter.this.println("offset_delta = " + param1chop_frame.offset_delta);
/* 581 */       AttributeWriter.this.indent(-1);
/* 582 */       return null;
/*     */     }
/*     */     
/*     */     public Void visit_same_frame_extended(StackMapTable_attribute.same_frame_extended param1same_frame_extended, Void param1Void) {
/* 586 */       printHeader((StackMapTable_attribute.stack_map_frame)param1same_frame_extended, "/* same_frame_extended */");
/* 587 */       AttributeWriter.this.indent(1);
/* 588 */       AttributeWriter.this.println("offset_delta = " + param1same_frame_extended.offset_delta);
/* 589 */       AttributeWriter.this.indent(-1);
/* 590 */       return null;
/*     */     }
/*     */     
/*     */     public Void visit_append_frame(StackMapTable_attribute.append_frame param1append_frame, Void param1Void) {
/* 594 */       printHeader((StackMapTable_attribute.stack_map_frame)param1append_frame, "/* append */");
/* 595 */       AttributeWriter.this.indent(1);
/* 596 */       AttributeWriter.this.println("offset_delta = " + param1append_frame.offset_delta);
/* 597 */       printMap("locals", param1append_frame.locals);
/* 598 */       AttributeWriter.this.indent(-1);
/* 599 */       return null;
/*     */     }
/*     */     
/*     */     public Void visit_full_frame(StackMapTable_attribute.full_frame param1full_frame, Void param1Void) {
/* 603 */       if (param1full_frame instanceof StackMap_attribute.stack_map_frame) {
/* 604 */         printHeader((StackMapTable_attribute.stack_map_frame)param1full_frame, "offset = " + param1full_frame.offset_delta);
/* 605 */         AttributeWriter.this.indent(1);
/*     */       } else {
/* 607 */         printHeader((StackMapTable_attribute.stack_map_frame)param1full_frame, "/* full_frame */");
/* 608 */         AttributeWriter.this.indent(1);
/* 609 */         AttributeWriter.this.println("offset_delta = " + param1full_frame.offset_delta);
/*     */       } 
/* 611 */       printMap("locals", param1full_frame.locals);
/* 612 */       printMap("stack", param1full_frame.stack);
/* 613 */       AttributeWriter.this.indent(-1);
/* 614 */       return null;
/*     */     }
/*     */     
/*     */     void printHeader(StackMapTable_attribute.stack_map_frame param1stack_map_frame, String param1String) {
/* 618 */       AttributeWriter.this.print("frame_type = " + param1stack_map_frame.frame_type + " ");
/* 619 */       AttributeWriter.this.println(param1String);
/*     */     }
/*     */     
/*     */     void printMap(String param1String, StackMapTable_attribute.verification_type_info[] param1ArrayOfverification_type_info) {
/* 623 */       AttributeWriter.this.print(param1String + " = [");
/* 624 */       for (byte b = 0; b < param1ArrayOfverification_type_info.length; b++) {
/* 625 */         StackMapTable_attribute.verification_type_info verification_type_info1 = param1ArrayOfverification_type_info[b];
/* 626 */         int i = verification_type_info1.tag;
/* 627 */         switch (i) {
/*     */           case 7:
/* 629 */             AttributeWriter.this.print(" ");
/* 630 */             AttributeWriter.this.constantWriter.write(((StackMapTable_attribute.Object_variable_info)verification_type_info1).cpool_index);
/*     */             break;
/*     */           case 8:
/* 633 */             AttributeWriter.this.print(" " + mapTypeName(i));
/* 634 */             AttributeWriter.this.print(" " + ((StackMapTable_attribute.Uninitialized_variable_info)verification_type_info1).offset);
/*     */             break;
/*     */           default:
/* 637 */             AttributeWriter.this.print(" " + mapTypeName(i)); break;
/*     */         } 
/* 639 */         AttributeWriter.this.print((b == param1ArrayOfverification_type_info.length - 1) ? " " : ",");
/*     */       } 
/* 641 */       AttributeWriter.this.println("]");
/*     */     }
/*     */     
/*     */     String mapTypeName(int param1Int) {
/* 645 */       switch (param1Int) {
/*     */         case 0:
/* 647 */           return "top";
/*     */         
/*     */         case 1:
/* 650 */           return "int";
/*     */         
/*     */         case 2:
/* 653 */           return "float";
/*     */         
/*     */         case 4:
/* 656 */           return "long";
/*     */         
/*     */         case 3:
/* 659 */           return "double";
/*     */         
/*     */         case 5:
/* 662 */           return "null";
/*     */         
/*     */         case 6:
/* 665 */           return "this";
/*     */         
/*     */         case 7:
/* 668 */           return "CP";
/*     */         
/*     */         case 8:
/* 671 */           return "uninitialized";
/*     */       } 
/*     */       
/* 674 */       AttributeWriter.this.report("unrecognized verification_type_info tag: " + param1Int);
/* 675 */       return "[tag:" + param1Int + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Void visitSynthetic(Synthetic_attribute paramSynthetic_attribute, Void paramVoid) {
/* 681 */     println("Synthetic: true");
/* 682 */     return null;
/*     */   }
/*     */   
/*     */   static String getJavaName(String paramString) {
/* 686 */     return paramString.replace('/', '.');
/*     */   }
/*     */   
/*     */   String toHex(byte paramByte, int paramInt) {
/* 690 */     return toHex(paramByte & 0xFF, paramInt);
/*     */   }
/*     */   
/*     */   static String toHex(int paramInt) {
/* 694 */     return StringUtils.toUpperCase(Integer.toString(paramInt, 16));
/*     */   }
/*     */   
/*     */   static String toHex(int paramInt1, int paramInt2) {
/* 698 */     String str = StringUtils.toUpperCase(Integer.toHexString(paramInt1));
/* 699 */     while (str.length() < paramInt2)
/* 700 */       str = "0" + str; 
/* 701 */     return StringUtils.toUpperCase(str);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\AttributeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */