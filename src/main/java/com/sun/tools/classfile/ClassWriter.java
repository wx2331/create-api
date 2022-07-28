/*     */ package com.sun.tools.classfile;
/*     */
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class ClassWriter
/*     */ {
/*  51 */   protected AttributeWriter attributeWriter = new AttributeWriter();
/*  52 */   protected ConstantPoolWriter constantPoolWriter = new ConstantPoolWriter();
/*  53 */   protected ClassOutputStream out = new ClassOutputStream();
/*     */
/*     */
/*     */   protected ClassFile classFile;
/*     */
/*     */
/*     */   public void write(ClassFile paramClassFile, File paramFile) throws IOException {
/*  60 */     FileOutputStream fileOutputStream = new FileOutputStream(paramFile);
/*     */     try {
/*  62 */       write(paramClassFile, fileOutputStream);
/*     */     } finally {
/*  64 */       fileOutputStream.close();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void write(ClassFile paramClassFile, OutputStream paramOutputStream) throws IOException {
/*  72 */     this.classFile = paramClassFile;
/*  73 */     this.out.reset();
/*  74 */     write();
/*  75 */     this.out.writeTo(paramOutputStream);
/*     */   }
/*     */
/*     */   protected void write() throws IOException {
/*  79 */     writeHeader();
/*  80 */     writeConstantPool();
/*  81 */     writeAccessFlags(this.classFile.access_flags);
/*  82 */     writeClassInfo();
/*  83 */     writeFields();
/*  84 */     writeMethods();
/*  85 */     writeAttributes(this.classFile.attributes);
/*     */   }
/*     */
/*     */   protected void writeHeader() {
/*  89 */     this.out.writeInt(this.classFile.magic);
/*  90 */     this.out.writeShort(this.classFile.minor_version);
/*  91 */     this.out.writeShort(this.classFile.major_version);
/*     */   }
/*     */
/*     */   protected void writeAccessFlags(AccessFlags paramAccessFlags) {
/*  95 */     this.out.writeShort(paramAccessFlags.flags);
/*     */   }
/*     */
/*     */   protected void writeAttributes(Attributes paramAttributes) {
/*  99 */     int i = paramAttributes.size();
/* 100 */     this.out.writeShort(i);
/* 101 */     for (Attribute attribute : paramAttributes)
/* 102 */       this.attributeWriter.write(attribute, this.out);
/*     */   }
/*     */
/*     */   protected void writeClassInfo() {
/* 106 */     this.out.writeShort(this.classFile.this_class);
/* 107 */     this.out.writeShort(this.classFile.super_class);
/* 108 */     int[] arrayOfInt = this.classFile.interfaces;
/* 109 */     this.out.writeShort(arrayOfInt.length);
/* 110 */     for (int i : arrayOfInt)
/* 111 */       this.out.writeShort(i);
/*     */   }
/*     */
/*     */   protected void writeDescriptor(Descriptor paramDescriptor) {
/* 115 */     this.out.writeShort(paramDescriptor.index);
/*     */   }
/*     */
/*     */   protected void writeConstantPool() {
/* 119 */     ConstantPool constantPool = this.classFile.constant_pool;
/* 120 */     int i = constantPool.size();
/* 121 */     this.out.writeShort(i);
/* 122 */     for (ConstantPool.CPInfo cPInfo : constantPool.entries())
/* 123 */       this.constantPoolWriter.write(cPInfo, this.out);
/*     */   }
/*     */
/*     */   protected void writeFields() throws IOException {
/* 127 */     Field[] arrayOfField = this.classFile.fields;
/* 128 */     this.out.writeShort(arrayOfField.length);
/* 129 */     for (Field field : arrayOfField)
/* 130 */       writeField(field);
/*     */   }
/*     */
/*     */   protected void writeField(Field paramField) throws IOException {
/* 134 */     writeAccessFlags(paramField.access_flags);
/* 135 */     this.out.writeShort(paramField.name_index);
/* 136 */     writeDescriptor(paramField.descriptor);
/* 137 */     writeAttributes(paramField.attributes);
/*     */   }
/*     */
/*     */   protected void writeMethods() throws IOException {
/* 141 */     Method[] arrayOfMethod = this.classFile.methods;
/* 142 */     this.out.writeShort(arrayOfMethod.length);
/* 143 */     for (Method method : arrayOfMethod) {
/* 144 */       writeMethod(method);
/*     */     }
/*     */   }
/*     */
/*     */   protected void writeMethod(Method paramMethod) throws IOException {
/* 149 */     writeAccessFlags(paramMethod.access_flags);
/* 150 */     this.out.writeShort(paramMethod.name_index);
/* 151 */     writeDescriptor(paramMethod.descriptor);
/* 152 */     writeAttributes(paramMethod.attributes);
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
/*     */   protected static class ClassOutputStream
/*     */     extends ByteArrayOutputStream
/*     */   {
/* 168 */     private DataOutputStream d = new DataOutputStream(this);
/*     */
/*     */
/*     */     public void writeByte(int param1Int) {
/*     */       try {
/* 173 */         this.d.writeByte(param1Int);
/* 174 */       } catch (IOException iOException) {}
/*     */     }
/*     */
/*     */
/*     */     public void writeShort(int param1Int) {
/*     */       try {
/* 180 */         this.d.writeShort(param1Int);
/* 181 */       } catch (IOException iOException) {}
/*     */     }
/*     */
/*     */
/*     */     public void writeInt(int param1Int) {
/*     */       try {
/* 187 */         this.d.writeInt(param1Int);
/* 188 */       } catch (IOException iOException) {}
/*     */     }
/*     */
/*     */
/*     */     public void writeLong(long param1Long) {
/*     */       try {
/* 194 */         this.d.writeLong(param1Long);
/* 195 */       } catch (IOException iOException) {}
/*     */     }
/*     */
/*     */
/*     */     public void writeFloat(float param1Float) {
/*     */       try {
/* 201 */         this.d.writeFloat(param1Float);
/* 202 */       } catch (IOException iOException) {}
/*     */     }
/*     */
/*     */
/*     */     public void writeDouble(double param1Double) {
/*     */       try {
/* 208 */         this.d.writeDouble(param1Double);
/* 209 */       } catch (IOException iOException) {}
/*     */     }
/*     */
/*     */
/*     */     public void writeUTF(String param1String) {
/*     */       try {
/* 215 */         this.d.writeUTF(param1String);
/* 216 */       } catch (IOException iOException) {}
/*     */     }
/*     */
/*     */
/*     */     public void writeTo(ClassOutputStream param1ClassOutputStream) {
/*     */       try {
/* 222 */         writeTo(param1ClassOutputStream);
/* 223 */       } catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected static class ConstantPoolWriter
/*     */     implements ConstantPool.Visitor<Integer, ClassOutputStream>
/*     */   {
/*     */     protected int write(ConstantPool.CPInfo param1CPInfo, ClassOutputStream param1ClassOutputStream) {
/* 236 */       param1ClassOutputStream.writeByte(param1CPInfo.getTag());
/* 237 */       return ((Integer)param1CPInfo.<Integer, ClassOutputStream>accept(this, param1ClassOutputStream)).intValue();
/*     */     }
/*     */
/*     */     public Integer visitClass(ConstantPool.CONSTANT_Class_info param1CONSTANT_Class_info, ClassOutputStream param1ClassOutputStream) {
/* 241 */       param1ClassOutputStream.writeShort(param1CONSTANT_Class_info.name_index);
/* 242 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitDouble(ConstantPool.CONSTANT_Double_info param1CONSTANT_Double_info, ClassOutputStream param1ClassOutputStream) {
/* 246 */       param1ClassOutputStream.writeDouble(param1CONSTANT_Double_info.value);
/* 247 */       return Integer.valueOf(2);
/*     */     }
/*     */
/*     */     public Integer visitFieldref(ConstantPool.CONSTANT_Fieldref_info param1CONSTANT_Fieldref_info, ClassOutputStream param1ClassOutputStream) {
/* 251 */       writeRef(param1CONSTANT_Fieldref_info, param1ClassOutputStream);
/* 252 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitFloat(ConstantPool.CONSTANT_Float_info param1CONSTANT_Float_info, ClassOutputStream param1ClassOutputStream) {
/* 256 */       param1ClassOutputStream.writeFloat(param1CONSTANT_Float_info.value);
/* 257 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitInteger(ConstantPool.CONSTANT_Integer_info param1CONSTANT_Integer_info, ClassOutputStream param1ClassOutputStream) {
/* 261 */       param1ClassOutputStream.writeInt(param1CONSTANT_Integer_info.value);
/* 262 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info param1CONSTANT_InterfaceMethodref_info, ClassOutputStream param1ClassOutputStream) {
/* 266 */       writeRef(param1CONSTANT_InterfaceMethodref_info, param1ClassOutputStream);
/* 267 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info param1CONSTANT_InvokeDynamic_info, ClassOutputStream param1ClassOutputStream) {
/* 271 */       param1ClassOutputStream.writeShort(param1CONSTANT_InvokeDynamic_info.bootstrap_method_attr_index);
/* 272 */       param1ClassOutputStream.writeShort(param1CONSTANT_InvokeDynamic_info.name_and_type_index);
/* 273 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitLong(ConstantPool.CONSTANT_Long_info param1CONSTANT_Long_info, ClassOutputStream param1ClassOutputStream) {
/* 277 */       param1ClassOutputStream.writeLong(param1CONSTANT_Long_info.value);
/* 278 */       return Integer.valueOf(2);
/*     */     }
/*     */
/*     */     public Integer visitNameAndType(ConstantPool.CONSTANT_NameAndType_info param1CONSTANT_NameAndType_info, ClassOutputStream param1ClassOutputStream) {
/* 282 */       param1ClassOutputStream.writeShort(param1CONSTANT_NameAndType_info.name_index);
/* 283 */       param1ClassOutputStream.writeShort(param1CONSTANT_NameAndType_info.type_index);
/* 284 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info param1CONSTANT_MethodHandle_info, ClassOutputStream param1ClassOutputStream) {
/* 288 */       param1ClassOutputStream.writeByte(param1CONSTANT_MethodHandle_info.reference_kind.tag);
/* 289 */       param1ClassOutputStream.writeShort(param1CONSTANT_MethodHandle_info.reference_index);
/* 290 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitMethodType(ConstantPool.CONSTANT_MethodType_info param1CONSTANT_MethodType_info, ClassOutputStream param1ClassOutputStream) {
/* 294 */       param1ClassOutputStream.writeShort(param1CONSTANT_MethodType_info.descriptor_index);
/* 295 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitMethodref(ConstantPool.CONSTANT_Methodref_info param1CONSTANT_Methodref_info, ClassOutputStream param1ClassOutputStream) {
/* 299 */       return writeRef(param1CONSTANT_Methodref_info, param1ClassOutputStream);
/*     */     }
/*     */
/*     */     public Integer visitString(ConstantPool.CONSTANT_String_info param1CONSTANT_String_info, ClassOutputStream param1ClassOutputStream) {
/* 303 */       param1ClassOutputStream.writeShort(param1CONSTANT_String_info.string_index);
/* 304 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     public Integer visitUtf8(ConstantPool.CONSTANT_Utf8_info param1CONSTANT_Utf8_info, ClassOutputStream param1ClassOutputStream) {
/* 308 */       param1ClassOutputStream.writeUTF(param1CONSTANT_Utf8_info.value);
/* 309 */       return Integer.valueOf(1);
/*     */     }
/*     */
/*     */     protected Integer writeRef(ConstantPool.CPRefInfo param1CPRefInfo, ClassOutputStream param1ClassOutputStream) {
/* 313 */       param1ClassOutputStream.writeShort(param1CPRefInfo.class_index);
/* 314 */       param1ClassOutputStream.writeShort(param1CPRefInfo.name_and_type_index);
/* 315 */       return Integer.valueOf(1);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   protected static class AttributeWriter
/*     */     implements Attribute.Visitor<Void, ClassOutputStream>
/*     */   {
/*     */     public void write(Attributes param1Attributes, ClassOutputStream param1ClassOutputStream) {
/* 324 */       int i = param1Attributes.size();
/* 325 */       param1ClassOutputStream.writeShort(i);
/* 326 */       for (Attribute attribute : param1Attributes) {
/* 327 */         write(attribute, param1ClassOutputStream);
/*     */       }
/*     */     }
/*     */
/*     */     public void write(Attribute param1Attribute, ClassOutputStream param1ClassOutputStream) {
/* 332 */       param1ClassOutputStream.writeShort(param1Attribute.attribute_name_index);
/* 333 */       this.sharedOut.reset();
/* 334 */       param1Attribute.accept(this, this.sharedOut);
/* 335 */       param1ClassOutputStream.writeInt(this.sharedOut.size());
/* 336 */       this.sharedOut.writeTo(param1ClassOutputStream);
/*     */     }
/*     */
/* 339 */     protected ClassOutputStream sharedOut = new ClassOutputStream();
/* 340 */     protected AnnotationWriter annotationWriter = new AnnotationWriter();
/*     */
/*     */     public Void visitDefault(DefaultAttribute param1DefaultAttribute, ClassOutputStream param1ClassOutputStream) {
/* 343 */       param1ClassOutputStream.write(param1DefaultAttribute.info, 0, param1DefaultAttribute.info.length);
/* 344 */       return null;
/*     */     }
/*     */     protected StackMapTableWriter stackMapWriter;
/*     */     public Void visitAnnotationDefault(AnnotationDefault_attribute param1AnnotationDefault_attribute, ClassOutputStream param1ClassOutputStream) {
/* 348 */       this.annotationWriter.write(param1AnnotationDefault_attribute.default_value, param1ClassOutputStream);
/* 349 */       return null;
/*     */     }
/*     */
/*     */     public Void visitBootstrapMethods(BootstrapMethods_attribute param1BootstrapMethods_attribute, ClassOutputStream param1ClassOutputStream) {
/* 353 */       param1ClassOutputStream.writeShort(param1BootstrapMethods_attribute.bootstrap_method_specifiers.length);
/* 354 */       for (BootstrapMethods_attribute.BootstrapMethodSpecifier bootstrapMethodSpecifier : param1BootstrapMethods_attribute.bootstrap_method_specifiers) {
/* 355 */         param1ClassOutputStream.writeShort(bootstrapMethodSpecifier.bootstrap_method_ref);
/* 356 */         int i = bootstrapMethodSpecifier.bootstrap_arguments.length;
/* 357 */         param1ClassOutputStream.writeShort(i);
/* 358 */         for (int j : bootstrapMethodSpecifier.bootstrap_arguments) {
/* 359 */           param1ClassOutputStream.writeShort(j);
/*     */         }
/*     */       }
/* 362 */       return null;
/*     */     }
/*     */
/*     */     public Void visitCharacterRangeTable(CharacterRangeTable_attribute param1CharacterRangeTable_attribute, ClassOutputStream param1ClassOutputStream) {
/* 366 */       param1ClassOutputStream.writeShort(param1CharacterRangeTable_attribute.character_range_table.length);
/* 367 */       for (CharacterRangeTable_attribute.Entry entry : param1CharacterRangeTable_attribute.character_range_table)
/* 368 */         writeCharacterRangeTableEntry(entry, param1ClassOutputStream);
/* 369 */       return null;
/*     */     }
/*     */
/*     */     protected void writeCharacterRangeTableEntry(CharacterRangeTable_attribute.Entry param1Entry, ClassOutputStream param1ClassOutputStream) {
/* 373 */       param1ClassOutputStream.writeShort(param1Entry.start_pc);
/* 374 */       param1ClassOutputStream.writeShort(param1Entry.end_pc);
/* 375 */       param1ClassOutputStream.writeInt(param1Entry.character_range_start);
/* 376 */       param1ClassOutputStream.writeInt(param1Entry.character_range_end);
/* 377 */       param1ClassOutputStream.writeShort(param1Entry.flags);
/*     */     }
/*     */
/*     */     public Void visitCode(Code_attribute param1Code_attribute, ClassOutputStream param1ClassOutputStream) {
/* 381 */       param1ClassOutputStream.writeShort(param1Code_attribute.max_stack);
/* 382 */       param1ClassOutputStream.writeShort(param1Code_attribute.max_locals);
/* 383 */       param1ClassOutputStream.writeInt(param1Code_attribute.code.length);
/* 384 */       param1ClassOutputStream.write(param1Code_attribute.code, 0, param1Code_attribute.code.length);
/* 385 */       param1ClassOutputStream.writeShort(param1Code_attribute.exception_table.length);
/* 386 */       for (Code_attribute.Exception_data exception_data : param1Code_attribute.exception_table)
/* 387 */         writeExceptionTableEntry(exception_data, param1ClassOutputStream);
/* 388 */       (new AttributeWriter()).write(param1Code_attribute.attributes, param1ClassOutputStream);
/* 389 */       return null;
/*     */     }
/*     */
/*     */     protected void writeExceptionTableEntry(Code_attribute.Exception_data param1Exception_data, ClassOutputStream param1ClassOutputStream) {
/* 393 */       param1ClassOutputStream.writeShort(param1Exception_data.start_pc);
/* 394 */       param1ClassOutputStream.writeShort(param1Exception_data.end_pc);
/* 395 */       param1ClassOutputStream.writeShort(param1Exception_data.handler_pc);
/* 396 */       param1ClassOutputStream.writeShort(param1Exception_data.catch_type);
/*     */     }
/*     */
/*     */     public Void visitCompilationID(CompilationID_attribute param1CompilationID_attribute, ClassOutputStream param1ClassOutputStream) {
/* 400 */       param1ClassOutputStream.writeShort(param1CompilationID_attribute.compilationID_index);
/* 401 */       return null;
/*     */     }
/*     */
/*     */     public Void visitConstantValue(ConstantValue_attribute param1ConstantValue_attribute, ClassOutputStream param1ClassOutputStream) {
/* 405 */       param1ClassOutputStream.writeShort(param1ConstantValue_attribute.constantvalue_index);
/* 406 */       return null;
/*     */     }
/*     */
/*     */     public Void visitDeprecated(Deprecated_attribute param1Deprecated_attribute, ClassOutputStream param1ClassOutputStream) {
/* 410 */       return null;
/*     */     }
/*     */
/*     */     public Void visitEnclosingMethod(EnclosingMethod_attribute param1EnclosingMethod_attribute, ClassOutputStream param1ClassOutputStream) {
/* 414 */       param1ClassOutputStream.writeShort(param1EnclosingMethod_attribute.class_index);
/* 415 */       param1ClassOutputStream.writeShort(param1EnclosingMethod_attribute.method_index);
/* 416 */       return null;
/*     */     }
/*     */
/*     */     public Void visitExceptions(Exceptions_attribute param1Exceptions_attribute, ClassOutputStream param1ClassOutputStream) {
/* 420 */       param1ClassOutputStream.writeShort(param1Exceptions_attribute.exception_index_table.length);
/* 421 */       for (int i : param1Exceptions_attribute.exception_index_table)
/* 422 */         param1ClassOutputStream.writeShort(i);
/* 423 */       return null;
/*     */     }
/*     */
/*     */     public Void visitInnerClasses(InnerClasses_attribute param1InnerClasses_attribute, ClassOutputStream param1ClassOutputStream) {
/* 427 */       param1ClassOutputStream.writeShort(param1InnerClasses_attribute.classes.length);
/* 428 */       for (InnerClasses_attribute.Info info : param1InnerClasses_attribute.classes)
/* 429 */         writeInnerClassesInfo(info, param1ClassOutputStream);
/* 430 */       return null;
/*     */     }
/*     */
/*     */     protected void writeInnerClassesInfo(InnerClasses_attribute.Info param1Info, ClassOutputStream param1ClassOutputStream) {
/* 434 */       param1ClassOutputStream.writeShort(param1Info.inner_class_info_index);
/* 435 */       param1ClassOutputStream.writeShort(param1Info.outer_class_info_index);
/* 436 */       param1ClassOutputStream.writeShort(param1Info.inner_name_index);
/* 437 */       writeAccessFlags(param1Info.inner_class_access_flags, param1ClassOutputStream);
/*     */     }
/*     */
/*     */     public Void visitLineNumberTable(LineNumberTable_attribute param1LineNumberTable_attribute, ClassOutputStream param1ClassOutputStream) {
/* 441 */       param1ClassOutputStream.writeShort(param1LineNumberTable_attribute.line_number_table.length);
/* 442 */       for (LineNumberTable_attribute.Entry entry : param1LineNumberTable_attribute.line_number_table)
/* 443 */         writeLineNumberTableEntry(entry, param1ClassOutputStream);
/* 444 */       return null;
/*     */     }
/*     */
/*     */     protected void writeLineNumberTableEntry(LineNumberTable_attribute.Entry param1Entry, ClassOutputStream param1ClassOutputStream) {
/* 448 */       param1ClassOutputStream.writeShort(param1Entry.start_pc);
/* 449 */       param1ClassOutputStream.writeShort(param1Entry.line_number);
/*     */     }
/*     */
/*     */     public Void visitLocalVariableTable(LocalVariableTable_attribute param1LocalVariableTable_attribute, ClassOutputStream param1ClassOutputStream) {
/* 453 */       param1ClassOutputStream.writeShort(param1LocalVariableTable_attribute.local_variable_table.length);
/* 454 */       for (LocalVariableTable_attribute.Entry entry : param1LocalVariableTable_attribute.local_variable_table)
/* 455 */         writeLocalVariableTableEntry(entry, param1ClassOutputStream);
/* 456 */       return null;
/*     */     }
/*     */
/*     */     protected void writeLocalVariableTableEntry(LocalVariableTable_attribute.Entry param1Entry, ClassOutputStream param1ClassOutputStream) {
/* 460 */       param1ClassOutputStream.writeShort(param1Entry.start_pc);
/* 461 */       param1ClassOutputStream.writeShort(param1Entry.length);
/* 462 */       param1ClassOutputStream.writeShort(param1Entry.name_index);
/* 463 */       param1ClassOutputStream.writeShort(param1Entry.descriptor_index);
/* 464 */       param1ClassOutputStream.writeShort(param1Entry.index);
/*     */     }
/*     */
/*     */     public Void visitLocalVariableTypeTable(LocalVariableTypeTable_attribute param1LocalVariableTypeTable_attribute, ClassOutputStream param1ClassOutputStream) {
/* 468 */       param1ClassOutputStream.writeShort(param1LocalVariableTypeTable_attribute.local_variable_table.length);
/* 469 */       for (LocalVariableTypeTable_attribute.Entry entry : param1LocalVariableTypeTable_attribute.local_variable_table)
/* 470 */         writeLocalVariableTypeTableEntry(entry, param1ClassOutputStream);
/* 471 */       return null;
/*     */     }
/*     */
/*     */     protected void writeLocalVariableTypeTableEntry(LocalVariableTypeTable_attribute.Entry param1Entry, ClassOutputStream param1ClassOutputStream) {
/* 475 */       param1ClassOutputStream.writeShort(param1Entry.start_pc);
/* 476 */       param1ClassOutputStream.writeShort(param1Entry.length);
/* 477 */       param1ClassOutputStream.writeShort(param1Entry.name_index);
/* 478 */       param1ClassOutputStream.writeShort(param1Entry.signature_index);
/* 479 */       param1ClassOutputStream.writeShort(param1Entry.index);
/*     */     }
/*     */
/*     */     public Void visitMethodParameters(MethodParameters_attribute param1MethodParameters_attribute, ClassOutputStream param1ClassOutputStream) {
/* 483 */       param1ClassOutputStream.writeByte(param1MethodParameters_attribute.method_parameter_table.length);
/* 484 */       for (MethodParameters_attribute.Entry entry : param1MethodParameters_attribute.method_parameter_table) {
/* 485 */         param1ClassOutputStream.writeShort(entry.name_index);
/* 486 */         param1ClassOutputStream.writeShort(entry.flags);
/*     */       }
/* 488 */       return null;
/*     */     }
/*     */
/*     */     public Void visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations_attribute param1RuntimeVisibleAnnotations_attribute, ClassOutputStream param1ClassOutputStream) {
/* 492 */       this.annotationWriter.write(param1RuntimeVisibleAnnotations_attribute.annotations, param1ClassOutputStream);
/* 493 */       return null;
/*     */     }
/*     */
/*     */     public Void visitRuntimeInvisibleAnnotations(RuntimeInvisibleAnnotations_attribute param1RuntimeInvisibleAnnotations_attribute, ClassOutputStream param1ClassOutputStream) {
/* 497 */       this.annotationWriter.write(param1RuntimeInvisibleAnnotations_attribute.annotations, param1ClassOutputStream);
/* 498 */       return null;
/*     */     }
/*     */
/*     */     public Void visitRuntimeVisibleTypeAnnotations(RuntimeVisibleTypeAnnotations_attribute param1RuntimeVisibleTypeAnnotations_attribute, ClassOutputStream param1ClassOutputStream) {
/* 502 */       this.annotationWriter.write(param1RuntimeVisibleTypeAnnotations_attribute.annotations, param1ClassOutputStream);
/* 503 */       return null;
/*     */     }
/*     */
/*     */     public Void visitRuntimeInvisibleTypeAnnotations(RuntimeInvisibleTypeAnnotations_attribute param1RuntimeInvisibleTypeAnnotations_attribute, ClassOutputStream param1ClassOutputStream) {
/* 507 */       this.annotationWriter.write(param1RuntimeInvisibleTypeAnnotations_attribute.annotations, param1ClassOutputStream);
/* 508 */       return null;
/*     */     }
/*     */
/*     */     public Void visitRuntimeVisibleParameterAnnotations(RuntimeVisibleParameterAnnotations_attribute param1RuntimeVisibleParameterAnnotations_attribute, ClassOutputStream param1ClassOutputStream) {
/* 512 */       param1ClassOutputStream.writeByte(param1RuntimeVisibleParameterAnnotations_attribute.parameter_annotations.length);
/* 513 */       for (Annotation[] arrayOfAnnotation : param1RuntimeVisibleParameterAnnotations_attribute.parameter_annotations)
/* 514 */         this.annotationWriter.write(arrayOfAnnotation, param1ClassOutputStream);
/* 515 */       return null;
/*     */     }
/*     */
/*     */     public Void visitRuntimeInvisibleParameterAnnotations(RuntimeInvisibleParameterAnnotations_attribute param1RuntimeInvisibleParameterAnnotations_attribute, ClassOutputStream param1ClassOutputStream) {
/* 519 */       param1ClassOutputStream.writeByte(param1RuntimeInvisibleParameterAnnotations_attribute.parameter_annotations.length);
/* 520 */       for (Annotation[] arrayOfAnnotation : param1RuntimeInvisibleParameterAnnotations_attribute.parameter_annotations)
/* 521 */         this.annotationWriter.write(arrayOfAnnotation, param1ClassOutputStream);
/* 522 */       return null;
/*     */     }
/*     */
/*     */     public Void visitSignature(Signature_attribute param1Signature_attribute, ClassOutputStream param1ClassOutputStream) {
/* 526 */       param1ClassOutputStream.writeShort(param1Signature_attribute.signature_index);
/* 527 */       return null;
/*     */     }
/*     */
/*     */     public Void visitSourceDebugExtension(SourceDebugExtension_attribute param1SourceDebugExtension_attribute, ClassOutputStream param1ClassOutputStream) {
/* 531 */       param1ClassOutputStream.write(param1SourceDebugExtension_attribute.debug_extension, 0, param1SourceDebugExtension_attribute.debug_extension.length);
/* 532 */       return null;
/*     */     }
/*     */
/*     */     public Void visitSourceFile(SourceFile_attribute param1SourceFile_attribute, ClassOutputStream param1ClassOutputStream) {
/* 536 */       param1ClassOutputStream.writeShort(param1SourceFile_attribute.sourcefile_index);
/* 537 */       return null;
/*     */     }
/*     */
/*     */     public Void visitSourceID(SourceID_attribute param1SourceID_attribute, ClassOutputStream param1ClassOutputStream) {
/* 541 */       param1ClassOutputStream.writeShort(param1SourceID_attribute.sourceID_index);
/* 542 */       return null;
/*     */     }
/*     */
/*     */     public Void visitStackMap(StackMap_attribute param1StackMap_attribute, ClassOutputStream param1ClassOutputStream) {
/* 546 */       if (this.stackMapWriter == null) {
/* 547 */         this.stackMapWriter = new StackMapTableWriter();
/*     */       }
/* 549 */       param1ClassOutputStream.writeShort(param1StackMap_attribute.entries.length);
/* 550 */       for (StackMap_attribute.stack_map_frame stack_map_frame : param1StackMap_attribute.entries)
/* 551 */         this.stackMapWriter.write(stack_map_frame, param1ClassOutputStream);
/* 552 */       return null;
/*     */     }
/*     */
/*     */     public Void visitStackMapTable(StackMapTable_attribute param1StackMapTable_attribute, ClassOutputStream param1ClassOutputStream) {
/* 556 */       if (this.stackMapWriter == null) {
/* 557 */         this.stackMapWriter = new StackMapTableWriter();
/*     */       }
/* 559 */       param1ClassOutputStream.writeShort(param1StackMapTable_attribute.entries.length);
/* 560 */       for (StackMapTable_attribute.stack_map_frame stack_map_frame : param1StackMapTable_attribute.entries)
/* 561 */         this.stackMapWriter.write(stack_map_frame, param1ClassOutputStream);
/* 562 */       return null;
/*     */     }
/*     */
/*     */     public Void visitSynthetic(Synthetic_attribute param1Synthetic_attribute, ClassOutputStream param1ClassOutputStream) {
/* 566 */       return null;
/*     */     }
/*     */
/*     */     protected void writeAccessFlags(AccessFlags param1AccessFlags, ClassOutputStream param1ClassOutputStream) {
/* 570 */       this.sharedOut.writeShort(param1AccessFlags.flags);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected static class StackMapTableWriter
/*     */     implements StackMapTable_attribute.stack_map_frame.Visitor<Void, ClassOutputStream>
/*     */   {
/*     */     public void write(StackMapTable_attribute.stack_map_frame param1stack_map_frame, ClassOutputStream param1ClassOutputStream) {
/* 583 */       param1ClassOutputStream.write(param1stack_map_frame.frame_type);
/* 584 */       param1stack_map_frame.accept(this, param1ClassOutputStream);
/*     */     }
/*     */
/*     */     public Void visit_same_frame(StackMapTable_attribute.same_frame param1same_frame, ClassOutputStream param1ClassOutputStream) {
/* 588 */       return null;
/*     */     }
/*     */
/*     */     public Void visit_same_locals_1_stack_item_frame(StackMapTable_attribute.same_locals_1_stack_item_frame param1same_locals_1_stack_item_frame, ClassOutputStream param1ClassOutputStream) {
/* 592 */       writeVerificationTypeInfo(param1same_locals_1_stack_item_frame.stack[0], param1ClassOutputStream);
/* 593 */       return null;
/*     */     }
/*     */
/*     */     public Void visit_same_locals_1_stack_item_frame_extended(StackMapTable_attribute.same_locals_1_stack_item_frame_extended param1same_locals_1_stack_item_frame_extended, ClassOutputStream param1ClassOutputStream) {
/* 597 */       param1ClassOutputStream.writeShort(param1same_locals_1_stack_item_frame_extended.offset_delta);
/* 598 */       writeVerificationTypeInfo(param1same_locals_1_stack_item_frame_extended.stack[0], param1ClassOutputStream);
/* 599 */       return null;
/*     */     }
/*     */
/*     */     public Void visit_chop_frame(StackMapTable_attribute.chop_frame param1chop_frame, ClassOutputStream param1ClassOutputStream) {
/* 603 */       param1ClassOutputStream.writeShort(param1chop_frame.offset_delta);
/* 604 */       return null;
/*     */     }
/*     */
/*     */     public Void visit_same_frame_extended(StackMapTable_attribute.same_frame_extended param1same_frame_extended, ClassOutputStream param1ClassOutputStream) {
/* 608 */       param1ClassOutputStream.writeShort(param1same_frame_extended.offset_delta);
/* 609 */       return null;
/*     */     }
/*     */
/*     */     public Void visit_append_frame(StackMapTable_attribute.append_frame param1append_frame, ClassOutputStream param1ClassOutputStream) {
/* 613 */       param1ClassOutputStream.writeShort(param1append_frame.offset_delta);
/* 614 */       for (StackMapTable_attribute.verification_type_info verification_type_info : param1append_frame.locals)
/* 615 */         writeVerificationTypeInfo(verification_type_info, param1ClassOutputStream);
/* 616 */       return null;
/*     */     }
/*     */
/*     */     public Void visit_full_frame(StackMapTable_attribute.full_frame param1full_frame, ClassOutputStream param1ClassOutputStream) {
/* 620 */       param1ClassOutputStream.writeShort(param1full_frame.offset_delta);
/* 621 */       param1ClassOutputStream.writeShort(param1full_frame.locals.length);
/* 622 */       for (StackMapTable_attribute.verification_type_info verification_type_info : param1full_frame.locals)
/* 623 */         writeVerificationTypeInfo(verification_type_info, param1ClassOutputStream);
/* 624 */       param1ClassOutputStream.writeShort(param1full_frame.stack.length);
/* 625 */       for (StackMapTable_attribute.verification_type_info verification_type_info : param1full_frame.stack)
/* 626 */         writeVerificationTypeInfo(verification_type_info, param1ClassOutputStream);
/* 627 */       return null;
/*     */     }
/*     */     protected void writeVerificationTypeInfo(StackMapTable_attribute.verification_type_info param1verification_type_info, ClassOutputStream param1ClassOutputStream) {
/*     */       StackMapTable_attribute.Object_variable_info object_variable_info;
/*     */       StackMapTable_attribute.Uninitialized_variable_info uninitialized_variable_info;
/* 632 */       param1ClassOutputStream.write(param1verification_type_info.tag);
/* 633 */       switch (param1verification_type_info.tag) {
/*     */         case 0:
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/*     */         case 5:
/*     */         case 6:
/*     */           return;
/*     */
/*     */         case 7:
/* 644 */           object_variable_info = (StackMapTable_attribute.Object_variable_info)param1verification_type_info;
/* 645 */           param1ClassOutputStream.writeShort(object_variable_info.cpool_index);
/*     */
/*     */
/*     */         case 8:
/* 649 */           uninitialized_variable_info = (StackMapTable_attribute.Uninitialized_variable_info)param1verification_type_info;
/* 650 */           param1ClassOutputStream.writeShort(uninitialized_variable_info.offset);
/*     */       }
/*     */
/*     */
/* 654 */       throw new Error();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected static class AnnotationWriter
/*     */     implements Annotation.element_value.Visitor<Void, ClassOutputStream>
/*     */   {
/*     */     public void write(Annotation[] param1ArrayOfAnnotation, ClassOutputStream param1ClassOutputStream) {
/* 665 */       param1ClassOutputStream.writeShort(param1ArrayOfAnnotation.length);
/* 666 */       for (Annotation annotation : param1ArrayOfAnnotation)
/* 667 */         write(annotation, param1ClassOutputStream);
/*     */     }
/*     */
/*     */     public void write(TypeAnnotation[] param1ArrayOfTypeAnnotation, ClassOutputStream param1ClassOutputStream) {
/* 671 */       param1ClassOutputStream.writeShort(param1ArrayOfTypeAnnotation.length);
/* 672 */       for (TypeAnnotation typeAnnotation : param1ArrayOfTypeAnnotation)
/* 673 */         write(typeAnnotation, param1ClassOutputStream);
/*     */     }
/*     */
/*     */     public void write(Annotation param1Annotation, ClassOutputStream param1ClassOutputStream) {
/* 677 */       param1ClassOutputStream.writeShort(param1Annotation.type_index);
/* 678 */       param1ClassOutputStream.writeShort(param1Annotation.element_value_pairs.length);
/* 679 */       for (Annotation.element_value_pair element_value_pair : param1Annotation.element_value_pairs)
/* 680 */         write(element_value_pair, param1ClassOutputStream);
/*     */     }
/*     */
/*     */     public void write(TypeAnnotation param1TypeAnnotation, ClassOutputStream param1ClassOutputStream) {
/* 684 */       write(param1TypeAnnotation.position, param1ClassOutputStream);
/* 685 */       write(param1TypeAnnotation.annotation, param1ClassOutputStream);
/*     */     }
/*     */
/*     */     public void write(Annotation.element_value_pair param1element_value_pair, ClassOutputStream param1ClassOutputStream) {
/* 689 */       param1ClassOutputStream.writeShort(param1element_value_pair.element_name_index);
/* 690 */       write(param1element_value_pair.value, param1ClassOutputStream);
/*     */     }
/*     */
/*     */     public void write(Annotation.element_value param1element_value, ClassOutputStream param1ClassOutputStream) {
/* 694 */       param1ClassOutputStream.writeByte(param1element_value.tag);
/* 695 */       param1element_value.accept(this, param1ClassOutputStream);
/*     */     }
/*     */
/*     */     public Void visitPrimitive(Annotation.Primitive_element_value param1Primitive_element_value, ClassOutputStream param1ClassOutputStream) {
/* 699 */       param1ClassOutputStream.writeShort(param1Primitive_element_value.const_value_index);
/* 700 */       return null;
/*     */     }
/*     */
/*     */     public Void visitEnum(Annotation.Enum_element_value param1Enum_element_value, ClassOutputStream param1ClassOutputStream) {
/* 704 */       param1ClassOutputStream.writeShort(param1Enum_element_value.type_name_index);
/* 705 */       param1ClassOutputStream.writeShort(param1Enum_element_value.const_name_index);
/* 706 */       return null;
/*     */     }
/*     */
/*     */     public Void visitClass(Annotation.Class_element_value param1Class_element_value, ClassOutputStream param1ClassOutputStream) {
/* 710 */       param1ClassOutputStream.writeShort(param1Class_element_value.class_info_index);
/* 711 */       return null;
/*     */     }
/*     */
/*     */     public Void visitAnnotation(Annotation.Annotation_element_value param1Annotation_element_value, ClassOutputStream param1ClassOutputStream) {
/* 715 */       write(param1Annotation_element_value.annotation_value, param1ClassOutputStream);
/* 716 */       return null;
/*     */     }
/*     */
/*     */     public Void visitArray(Annotation.Array_element_value param1Array_element_value, ClassOutputStream param1ClassOutputStream) {
/* 720 */       param1ClassOutputStream.writeShort(param1Array_element_value.num_values);
/* 721 */       for (Annotation.element_value element_value : param1Array_element_value.values)
/* 722 */         write(element_value, param1ClassOutputStream);
/* 723 */       return null;
/*     */     }
/*     */
/*     */     private void write(TypeAnnotation.Position param1Position, ClassOutputStream param1ClassOutputStream) {
/*     */       // Byte code:
/*     */       //   0: aload_2
/*     */       //   1: aload_1
/*     */       //   2: getfield type : Lcom/sun/tools/classfile/TypeAnnotation$TargetType;
/*     */       //   5: invokevirtual targetTypeValue : ()I
/*     */       //   8: invokevirtual writeByte : (I)V
/*     */       //   11: getstatic com/sun/tools/classfile/ClassWriter$1.$SwitchMap$com$sun$tools$classfile$TypeAnnotation$TargetType : [I
/*     */       //   14: aload_1
/*     */       //   15: getfield type : Lcom/sun/tools/classfile/TypeAnnotation$TargetType;
/*     */       //   18: invokevirtual ordinal : ()I
/*     */       //   21: iaload
/*     */       //   22: tableswitch default -> 315, 1 -> 128, 2 -> 128, 3 -> 128, 4 -> 128, 5 -> 139, 6 -> 139, 7 -> 206, 8 -> 217, 9 -> 220, 10 -> 220, 11 -> 231, 12 -> 231, 13 -> 250, 14 -> 261, 15 -> 272, 16 -> 283, 17 -> 283, 18 -> 283, 19 -> 283, 20 -> 283, 21 -> 302, 22 -> 302, 23 -> 305
/*     */       //   128: aload_2
/*     */       //   129: aload_1
/*     */       //   130: getfield offset : I
/*     */       //   133: invokevirtual writeShort : (I)V
/*     */       //   136: goto -> 342
/*     */       //   139: aload_1
/*     */       //   140: getfield lvarOffset : [I
/*     */       //   143: arraylength
/*     */       //   144: istore_3
/*     */       //   145: aload_2
/*     */       //   146: iload_3
/*     */       //   147: invokevirtual writeShort : (I)V
/*     */       //   150: iconst_0
/*     */       //   151: istore #4
/*     */       //   153: iload #4
/*     */       //   155: iload_3
/*     */       //   156: if_icmpge -> 203
/*     */       //   159: aload_2
/*     */       //   160: iconst_1
/*     */       //   161: invokevirtual writeShort : (I)V
/*     */       //   164: aload_2
/*     */       //   165: aload_1
/*     */       //   166: getfield lvarOffset : [I
/*     */       //   169: iload #4
/*     */       //   171: iaload
/*     */       //   172: invokevirtual writeShort : (I)V
/*     */       //   175: aload_2
/*     */       //   176: aload_1
/*     */       //   177: getfield lvarLength : [I
/*     */       //   180: iload #4
/*     */       //   182: iaload
/*     */       //   183: invokevirtual writeShort : (I)V
/*     */       //   186: aload_2
/*     */       //   187: aload_1
/*     */       //   188: getfield lvarIndex : [I
/*     */       //   191: iload #4
/*     */       //   193: iaload
/*     */       //   194: invokevirtual writeShort : (I)V
/*     */       //   197: iinc #4, 1
/*     */       //   200: goto -> 153
/*     */       //   203: goto -> 342
/*     */       //   206: aload_2
/*     */       //   207: aload_1
/*     */       //   208: getfield exception_index : I
/*     */       //   211: invokevirtual writeShort : (I)V
/*     */       //   214: goto -> 342
/*     */       //   217: goto -> 342
/*     */       //   220: aload_2
/*     */       //   221: aload_1
/*     */       //   222: getfield parameter_index : I
/*     */       //   225: invokevirtual writeByte : (I)V
/*     */       //   228: goto -> 342
/*     */       //   231: aload_2
/*     */       //   232: aload_1
/*     */       //   233: getfield parameter_index : I
/*     */       //   236: invokevirtual writeByte : (I)V
/*     */       //   239: aload_2
/*     */       //   240: aload_1
/*     */       //   241: getfield bound_index : I
/*     */       //   244: invokevirtual writeByte : (I)V
/*     */       //   247: goto -> 342
/*     */       //   250: aload_2
/*     */       //   251: aload_1
/*     */       //   252: getfield type_index : I
/*     */       //   255: invokevirtual writeShort : (I)V
/*     */       //   258: goto -> 342
/*     */       //   261: aload_2
/*     */       //   262: aload_1
/*     */       //   263: getfield type_index : I
/*     */       //   266: invokevirtual writeShort : (I)V
/*     */       //   269: goto -> 342
/*     */       //   272: aload_2
/*     */       //   273: aload_1
/*     */       //   274: getfield parameter_index : I
/*     */       //   277: invokevirtual writeByte : (I)V
/*     */       //   280: goto -> 342
/*     */       //   283: aload_2
/*     */       //   284: aload_1
/*     */       //   285: getfield offset : I
/*     */       //   288: invokevirtual writeShort : (I)V
/*     */       //   291: aload_2
/*     */       //   292: aload_1
/*     */       //   293: getfield type_index : I
/*     */       //   296: invokevirtual writeByte : (I)V
/*     */       //   299: goto -> 342
/*     */       //   302: goto -> 342
/*     */       //   305: new java/lang/AssertionError
/*     */       //   308: dup
/*     */       //   309: ldc 'ClassWriter: UNKNOWN target type should never occur!'
/*     */       //   311: invokespecial <init> : (Ljava/lang/Object;)V
/*     */       //   314: athrow
/*     */       //   315: new java/lang/AssertionError
/*     */       //   318: dup
/*     */       //   319: new java/lang/StringBuilder
/*     */       //   322: dup
/*     */       //   323: invokespecial <init> : ()V
/*     */       //   326: ldc 'ClassWriter: Unknown target type for position: '
/*     */       //   328: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   331: aload_1
/*     */       //   332: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   335: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   338: invokespecial <init> : (Ljava/lang/Object;)V
/*     */       //   341: athrow
/*     */       //   342: aload_2
/*     */       //   343: aload_1
/*     */       //   344: getfield location : Ljava/util/List;
/*     */       //   347: invokeinterface size : ()I
/*     */       //   352: i2b
/*     */       //   353: invokevirtual writeByte : (I)V
/*     */       //   356: aload_1
/*     */       //   357: getfield location : Ljava/util/List;
/*     */       //   360: invokestatic getBinaryFromTypePath : (Ljava/util/List;)Ljava/util/List;
/*     */       //   363: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   368: astore_3
/*     */       //   369: aload_3
/*     */       //   370: invokeinterface hasNext : ()Z
/*     */       //   375: ifeq -> 402
/*     */       //   378: aload_3
/*     */       //   379: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   384: checkcast java/lang/Integer
/*     */       //   387: invokevirtual intValue : ()I
/*     */       //   390: istore #4
/*     */       //   392: aload_2
/*     */       //   393: iload #4
/*     */       //   395: i2b
/*     */       //   396: invokevirtual writeByte : (I)V
/*     */       //   399: goto -> 369
/*     */       //   402: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #728	-> 0
/*     */       //   #729	-> 11
/*     */       //   #737	-> 128
/*     */       //   #738	-> 136
/*     */       //   #743	-> 139
/*     */       //   #744	-> 145
/*     */       //   #745	-> 150
/*     */       //   #746	-> 159
/*     */       //   #747	-> 164
/*     */       //   #748	-> 175
/*     */       //   #749	-> 186
/*     */       //   #745	-> 197
/*     */       //   #751	-> 203
/*     */       //   #754	-> 206
/*     */       //   #755	-> 214
/*     */       //   #759	-> 217
/*     */       //   #763	-> 220
/*     */       //   #764	-> 228
/*     */       //   #768	-> 231
/*     */       //   #769	-> 239
/*     */       //   #770	-> 247
/*     */       //   #773	-> 250
/*     */       //   #774	-> 258
/*     */       //   #777	-> 261
/*     */       //   #778	-> 269
/*     */       //   #781	-> 272
/*     */       //   #782	-> 280
/*     */       //   #790	-> 283
/*     */       //   #791	-> 291
/*     */       //   #792	-> 299
/*     */       //   #796	-> 302
/*     */       //   #798	-> 305
/*     */       //   #800	-> 315
/*     */       //   #805	-> 342
/*     */       //   #806	-> 356
/*     */       //   #807	-> 392
/*     */       //   #809	-> 402
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\ClassWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
