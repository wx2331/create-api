/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.AccessFlags;
/*     */ import com.sun.tools.classfile.Attribute;
/*     */ import com.sun.tools.classfile.Attributes;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.ConstantValue_attribute;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.DescriptorException;
/*     */ import com.sun.tools.classfile.Exceptions_attribute;
/*     */ import com.sun.tools.classfile.Field;
/*     */ import com.sun.tools.classfile.Method;
/*     */ import com.sun.tools.classfile.Signature;
/*     */ import com.sun.tools.classfile.Signature_attribute;
/*     */ import com.sun.tools.classfile.SourceFile_attribute;
/*     */ import com.sun.tools.classfile.Type;
/*     */ import java.net.URI;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
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
/*     */ public class ClassWriter
/*     */   extends BasicWriter
/*     */ {
/*     */   private Options options;
/*     */   private AttributeWriter attrWriter;
/*     */   private CodeWriter codeWriter;
/*     */   private ConstantWriter constantWriter;
/*     */   private ClassFile classFile;
/*     */   private URI uri;
/*     */   private long lastModified;
/*     */   private String digestName;
/*     */   private byte[] digest;
/*     */   private int size;
/*     */   private ConstantPool constant_pool;
/*     */   private Method method;
/*     */   
/*     */   static ClassWriter instance(Context paramContext) {
/*  71 */     ClassWriter classWriter = paramContext.<ClassWriter>get(ClassWriter.class);
/*  72 */     if (classWriter == null)
/*  73 */       classWriter = new ClassWriter(paramContext); 
/*  74 */     return classWriter;
/*     */   }
/*     */   
/*     */   protected ClassWriter(Context paramContext) {
/*  78 */     super(paramContext);
/*  79 */     paramContext.put(ClassWriter.class, this);
/*  80 */     this.options = Options.instance(paramContext);
/*  81 */     this.attrWriter = AttributeWriter.instance(paramContext);
/*  82 */     this.codeWriter = CodeWriter.instance(paramContext);
/*  83 */     this.constantWriter = ConstantWriter.instance(paramContext);
/*     */   }
/*     */   
/*     */   void setDigest(String paramString, byte[] paramArrayOfbyte) {
/*  87 */     this.digestName = paramString;
/*  88 */     this.digest = paramArrayOfbyte;
/*     */   }
/*     */   
/*     */   void setFile(URI paramURI) {
/*  92 */     this.uri = paramURI;
/*     */   }
/*     */   
/*     */   void setFileSize(int paramInt) {
/*  96 */     this.size = paramInt;
/*     */   }
/*     */   
/*     */   void setLastModified(long paramLong) {
/* 100 */     this.lastModified = paramLong;
/*     */   }
/*     */   
/*     */   protected ClassFile getClassFile() {
/* 104 */     return this.classFile;
/*     */   }
/*     */   
/*     */   protected void setClassFile(ClassFile paramClassFile) {
/* 108 */     this.classFile = paramClassFile;
/* 109 */     this.constant_pool = this.classFile.constant_pool;
/*     */   }
/*     */   
/*     */   protected Method getMethod() {
/* 113 */     return this.method;
/*     */   }
/*     */   
/*     */   protected void setMethod(Method paramMethod) {
/* 117 */     this.method = paramMethod;
/*     */   }
/*     */   
/*     */   public void write(ClassFile paramClassFile) {
/* 121 */     setClassFile(paramClassFile);
/*     */     
/* 123 */     if (this.options.sysInfo || this.options.verbose) {
/* 124 */       if (this.uri != null)
/* 125 */         if (this.uri.getScheme().equals("file")) {
/* 126 */           println("Classfile " + this.uri.getPath());
/*     */         } else {
/* 128 */           println("Classfile " + this.uri);
/*     */         }  
/* 130 */       indent(1);
/* 131 */       if (this.lastModified != -1L) {
/* 132 */         Date date = new Date(this.lastModified);
/* 133 */         DateFormat dateFormat = DateFormat.getDateInstance();
/* 134 */         if (this.size > 0) {
/* 135 */           println("Last modified " + dateFormat.format(date) + "; size " + this.size + " bytes");
/*     */         } else {
/* 137 */           println("Last modified " + dateFormat.format(date));
/*     */         } 
/* 139 */       } else if (this.size > 0) {
/* 140 */         println("Size " + this.size + " bytes");
/*     */       } 
/* 142 */       if (this.digestName != null && this.digest != null) {
/* 143 */         StringBuilder stringBuilder = new StringBuilder();
/* 144 */         for (byte b : this.digest) {
/* 145 */           stringBuilder.append(String.format("%02x", new Object[] { Byte.valueOf(b) }));
/* 146 */         }  println(this.digestName + " checksum " + stringBuilder);
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     Attribute attribute = paramClassFile.getAttribute("SourceFile");
/* 151 */     if (attribute instanceof SourceFile_attribute) {
/* 152 */       println("Compiled from \"" + getSourceFile((SourceFile_attribute)attribute) + "\"");
/*     */     }
/*     */     
/* 155 */     if (this.options.sysInfo || this.options.verbose) {
/* 156 */       indent(-1);
/*     */     }
/*     */     
/* 159 */     String str = getJavaName(this.classFile);
/* 160 */     AccessFlags accessFlags = paramClassFile.access_flags;
/*     */     
/* 162 */     writeModifiers(accessFlags.getClassModifiers());
/*     */     
/* 164 */     if (this.classFile.isClass()) {
/* 165 */       print("class ");
/* 166 */     } else if (this.classFile.isInterface()) {
/* 167 */       print("interface ");
/*     */     } 
/* 169 */     print(str);
/*     */     
/* 171 */     Signature_attribute signature_attribute = getSignature(paramClassFile.attributes);
/* 172 */     if (signature_attribute == null) {
/*     */       
/* 174 */       if (this.classFile.isClass() && this.classFile.super_class != 0) {
/* 175 */         String str1 = getJavaSuperclassName(paramClassFile);
/* 176 */         if (!str1.equals("java.lang.Object")) {
/* 177 */           print(" extends ");
/* 178 */           print(str1);
/*     */         } 
/*     */       } 
/* 181 */       for (byte b = 0; b < this.classFile.interfaces.length; b++) {
/* 182 */         print((b == 0) ? (this.classFile.isClass() ? " implements " : " extends ") : ",");
/* 183 */         print(getJavaInterfaceName(this.classFile, b));
/*     */       } 
/*     */     } else {
/*     */       try {
/* 187 */         Type type = signature_attribute.getParsedSignature().getType(this.constant_pool);
/* 188 */         JavaTypePrinter javaTypePrinter = new JavaTypePrinter(this.classFile.isInterface());
/*     */ 
/*     */         
/* 191 */         if (type instanceof Type.ClassSigType) {
/* 192 */           print(javaTypePrinter.print(type));
/* 193 */         } else if (this.options.verbose || !type.isObject()) {
/* 194 */           print(" extends ");
/* 195 */           print(javaTypePrinter.print(type));
/*     */         } 
/* 197 */       } catch (ConstantPoolException constantPoolException) {
/* 198 */         print(report(constantPoolException));
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     if (this.options.verbose) {
/* 203 */       println();
/* 204 */       indent(1);
/* 205 */       println("minor version: " + paramClassFile.minor_version);
/* 206 */       println("major version: " + paramClassFile.major_version);
/* 207 */       writeList("flags: ", accessFlags.getClassFlags(), "\n");
/* 208 */       indent(-1);
/* 209 */       this.constantWriter.writeConstantPool();
/*     */     } else {
/* 211 */       print(" ");
/*     */     } 
/*     */     
/* 214 */     println("{");
/* 215 */     indent(1);
/* 216 */     writeFields();
/* 217 */     writeMethods();
/* 218 */     indent(-1);
/* 219 */     println("}");
/*     */     
/* 221 */     if (this.options.verbose)
/* 222 */       this.attrWriter.write(paramClassFile, paramClassFile.attributes, this.constant_pool); 
/*     */   }
/*     */   
/*     */   class JavaTypePrinter
/*     */     implements Type.Visitor<StringBuilder, StringBuilder> {
/*     */     boolean isInterface;
/*     */     
/*     */     JavaTypePrinter(boolean param1Boolean) {
/* 230 */       this.isInterface = param1Boolean;
/*     */     }
/*     */     
/*     */     String print(Type param1Type) {
/* 234 */       return ((StringBuilder)param1Type.accept(this, new StringBuilder())).toString();
/*     */     }
/*     */     
/*     */     String printTypeArgs(List<? extends Type.TypeParamType> param1List) {
/* 238 */       StringBuilder stringBuilder = new StringBuilder();
/* 239 */       appendIfNotEmpty(stringBuilder, "<", (List)param1List, "> ");
/* 240 */       return stringBuilder.toString();
/*     */     }
/*     */     
/*     */     public StringBuilder visitSimpleType(Type.SimpleType param1SimpleType, StringBuilder param1StringBuilder) {
/* 244 */       param1StringBuilder.append(ClassWriter.getJavaName(param1SimpleType.name));
/* 245 */       return param1StringBuilder;
/*     */     }
/*     */     
/*     */     public StringBuilder visitArrayType(Type.ArrayType param1ArrayType, StringBuilder param1StringBuilder) {
/* 249 */       append(param1StringBuilder, param1ArrayType.elemType);
/* 250 */       param1StringBuilder.append("[]");
/* 251 */       return param1StringBuilder;
/*     */     }
/*     */     
/*     */     public StringBuilder visitMethodType(Type.MethodType param1MethodType, StringBuilder param1StringBuilder) {
/* 255 */       appendIfNotEmpty(param1StringBuilder, "<", param1MethodType.typeParamTypes, "> ");
/* 256 */       append(param1StringBuilder, param1MethodType.returnType);
/* 257 */       append(param1StringBuilder, " (", param1MethodType.paramTypes, ")");
/* 258 */       appendIfNotEmpty(param1StringBuilder, " throws ", param1MethodType.throwsTypes, "");
/* 259 */       return param1StringBuilder;
/*     */     }
/*     */     
/*     */     public StringBuilder visitClassSigType(Type.ClassSigType param1ClassSigType, StringBuilder param1StringBuilder) {
/* 263 */       appendIfNotEmpty(param1StringBuilder, "<", param1ClassSigType.typeParamTypes, ">");
/* 264 */       if (this.isInterface) {
/* 265 */         appendIfNotEmpty(param1StringBuilder, " extends ", param1ClassSigType.superinterfaceTypes, "");
/*     */       } else {
/* 267 */         if (param1ClassSigType.superclassType != null && (
/* 268 */           ClassWriter.this.options.verbose || !param1ClassSigType.superclassType.isObject())) {
/* 269 */           param1StringBuilder.append(" extends ");
/* 270 */           append(param1StringBuilder, param1ClassSigType.superclassType);
/*     */         } 
/* 272 */         appendIfNotEmpty(param1StringBuilder, " implements ", param1ClassSigType.superinterfaceTypes, "");
/*     */       } 
/* 274 */       return param1StringBuilder;
/*     */     }
/*     */     
/*     */     public StringBuilder visitClassType(Type.ClassType param1ClassType, StringBuilder param1StringBuilder) {
/* 278 */       if (param1ClassType.outerType != null) {
/* 279 */         append(param1StringBuilder, (Type)param1ClassType.outerType);
/* 280 */         param1StringBuilder.append(".");
/*     */       } 
/* 282 */       param1StringBuilder.append(ClassWriter.getJavaName(param1ClassType.name));
/* 283 */       appendIfNotEmpty(param1StringBuilder, "<", param1ClassType.typeArgs, ">");
/* 284 */       return param1StringBuilder;
/*     */     }
/*     */     
/*     */     public StringBuilder visitTypeParamType(Type.TypeParamType param1TypeParamType, StringBuilder param1StringBuilder) {
/* 288 */       param1StringBuilder.append(param1TypeParamType.name);
/* 289 */       String str = " extends ";
/* 290 */       if (param1TypeParamType.classBound != null && (
/* 291 */         ClassWriter.this.options.verbose || !param1TypeParamType.classBound.isObject())) {
/* 292 */         param1StringBuilder.append(str);
/* 293 */         append(param1StringBuilder, param1TypeParamType.classBound);
/* 294 */         str = " & ";
/*     */       } 
/* 296 */       if (param1TypeParamType.interfaceBounds != null) {
/* 297 */         for (Type type : param1TypeParamType.interfaceBounds) {
/* 298 */           param1StringBuilder.append(str);
/* 299 */           append(param1StringBuilder, type);
/* 300 */           str = " & ";
/*     */         } 
/*     */       }
/* 303 */       return param1StringBuilder;
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
/*     */     public StringBuilder visitWildcardType(Type.WildcardType param1WildcardType, StringBuilder param1StringBuilder) {
/*     */       // Byte code:
/*     */       //   0: getstatic com/sun/tools/javap/ClassWriter$1.$SwitchMap$com$sun$tools$classfile$Type$WildcardType$Kind : [I
/*     */       //   3: aload_1
/*     */       //   4: getfield kind : Lcom/sun/tools/classfile/Type$WildcardType$Kind;
/*     */       //   7: invokevirtual ordinal : ()I
/*     */       //   10: iaload
/*     */       //   11: tableswitch default -> 84, 1 -> 36, 2 -> 46, 3 -> 65
/*     */       //   36: aload_2
/*     */       //   37: ldc '?'
/*     */       //   39: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   42: pop
/*     */       //   43: goto -> 92
/*     */       //   46: aload_2
/*     */       //   47: ldc '? extends '
/*     */       //   49: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   52: pop
/*     */       //   53: aload_0
/*     */       //   54: aload_2
/*     */       //   55: aload_1
/*     */       //   56: getfield boundType : Lcom/sun/tools/classfile/Type;
/*     */       //   59: invokespecial append : (Ljava/lang/StringBuilder;Lcom/sun/tools/classfile/Type;)V
/*     */       //   62: goto -> 92
/*     */       //   65: aload_2
/*     */       //   66: ldc '? super '
/*     */       //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   71: pop
/*     */       //   72: aload_0
/*     */       //   73: aload_2
/*     */       //   74: aload_1
/*     */       //   75: getfield boundType : Lcom/sun/tools/classfile/Type;
/*     */       //   78: invokespecial append : (Ljava/lang/StringBuilder;Lcom/sun/tools/classfile/Type;)V
/*     */       //   81: goto -> 92
/*     */       //   84: new java/lang/AssertionError
/*     */       //   87: dup
/*     */       //   88: invokespecial <init> : ()V
/*     */       //   91: athrow
/*     */       //   92: aload_2
/*     */       //   93: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #307	-> 0
/*     */       //   #309	-> 36
/*     */       //   #310	-> 43
/*     */       //   #312	-> 46
/*     */       //   #313	-> 53
/*     */       //   #314	-> 62
/*     */       //   #316	-> 65
/*     */       //   #317	-> 72
/*     */       //   #318	-> 81
/*     */       //   #320	-> 84
/*     */       //   #322	-> 92
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void append(StringBuilder param1StringBuilder, Type param1Type) {
/* 326 */       param1Type.accept(this, param1StringBuilder);
/*     */     }
/*     */     
/*     */     private void append(StringBuilder param1StringBuilder, String param1String1, List<? extends Type> param1List, String param1String2) {
/* 330 */       param1StringBuilder.append(param1String1);
/* 331 */       String str = "";
/* 332 */       for (Type type : param1List) {
/* 333 */         param1StringBuilder.append(str);
/* 334 */         append(param1StringBuilder, type);
/* 335 */         str = ", ";
/*     */       } 
/* 337 */       param1StringBuilder.append(param1String2);
/*     */     }
/*     */     
/*     */     private void appendIfNotEmpty(StringBuilder param1StringBuilder, String param1String1, List<? extends Type> param1List, String param1String2) {
/* 341 */       if (!isEmpty(param1List))
/* 342 */         append(param1StringBuilder, param1String1, param1List, param1String2); 
/*     */     }
/*     */     
/*     */     private boolean isEmpty(List<? extends Type> param1List) {
/* 346 */       return (param1List == null || param1List.isEmpty());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writeFields() {
/* 351 */     for (Field field : this.classFile.fields) {
/* 352 */       writeField(field);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writeField(Field paramField) {
/* 357 */     if (!this.options.checkAccess(paramField.access_flags)) {
/*     */       return;
/*     */     }
/* 360 */     AccessFlags accessFlags = paramField.access_flags;
/* 361 */     writeModifiers(accessFlags.getFieldModifiers());
/* 362 */     Signature_attribute signature_attribute = getSignature(paramField.attributes);
/* 363 */     if (signature_attribute == null) {
/* 364 */       print(getJavaFieldType(paramField.descriptor));
/*     */     } else {
/*     */       try {
/* 367 */         Type type = signature_attribute.getParsedSignature().getType(this.constant_pool);
/* 368 */         print(getJavaName(type.toString()));
/* 369 */       } catch (ConstantPoolException constantPoolException) {
/*     */ 
/*     */         
/* 372 */         print(getJavaFieldType(paramField.descriptor));
/*     */       } 
/*     */     } 
/* 375 */     print(" ");
/* 376 */     print(getFieldName(paramField));
/* 377 */     if (this.options.showConstants) {
/* 378 */       Attribute attribute = paramField.attributes.get("ConstantValue");
/* 379 */       if (attribute instanceof ConstantValue_attribute) {
/* 380 */         print(" = ");
/* 381 */         ConstantValue_attribute constantValue_attribute = (ConstantValue_attribute)attribute;
/* 382 */         print(getConstantValue(paramField.descriptor, constantValue_attribute.constantvalue_index));
/*     */       } 
/*     */     } 
/* 385 */     print(";");
/* 386 */     println();
/*     */     
/* 388 */     indent(1);
/*     */     
/* 390 */     boolean bool = false;
/*     */     
/* 392 */     if (this.options.showDescriptors) {
/* 393 */       println("descriptor: " + getValue(paramField.descriptor));
/*     */     }
/* 395 */     if (this.options.verbose) {
/* 396 */       writeList("flags: ", accessFlags.getFieldFlags(), "\n");
/*     */     }
/* 398 */     if (this.options.showAllAttrs) {
/* 399 */       for (Attribute attribute : paramField.attributes)
/* 400 */         this.attrWriter.write(paramField, attribute, this.constant_pool); 
/* 401 */       bool = true;
/*     */     } 
/*     */     
/* 404 */     indent(-1);
/*     */     
/* 406 */     if (bool || this.options.showDisassembled || this.options.showLineAndLocalVariableTables)
/* 407 */       println(); 
/*     */   }
/*     */   
/*     */   protected void writeMethods() {
/* 411 */     for (Method method : this.classFile.methods)
/* 412 */       writeMethod(method); 
/* 413 */     setPendingNewline(false); } protected void writeMethod(Method paramMethod) {
/*     */     Signature signature;
/*     */     Object object;
/*     */     Collection collection;
/* 417 */     if (!this.options.checkAccess(paramMethod.access_flags)) {
/*     */       return;
/*     */     }
/* 420 */     this.method = paramMethod;
/*     */     
/* 422 */     AccessFlags accessFlags = paramMethod.access_flags;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 428 */     Signature_attribute signature_attribute = getSignature(paramMethod.attributes);
/* 429 */     if (signature_attribute == null) {
/* 430 */       Descriptor descriptor = paramMethod.descriptor;
/* 431 */       object = null;
/* 432 */       collection = null;
/*     */     } else {
/* 434 */       Signature signature1 = signature_attribute.getParsedSignature();
/* 435 */       signature = signature1;
/*     */       try {
/* 437 */         object = signature1.getType(this.constant_pool);
/* 438 */         collection = ((Type.MethodType)object).throwsTypes;
/* 439 */         if (collection != null && collection.isEmpty())
/* 440 */           collection = null; 
/* 441 */       } catch (ConstantPoolException constantPoolException) {
/*     */ 
/*     */         
/* 444 */         object = null;
/* 445 */         collection = null;
/*     */       } 
/*     */     } 
/*     */     
/* 449 */     writeModifiers(accessFlags.getMethodModifiers());
/* 450 */     if (object != null) {
/* 451 */       print((new JavaTypePrinter(false)).printTypeArgs(((Type.MethodType)object).typeParamTypes));
/*     */     }
/* 453 */     if (getName(paramMethod).equals("<init>")) {
/* 454 */       print(getJavaName(this.classFile));
/* 455 */       print(getJavaParameterTypes((Descriptor)signature, accessFlags));
/* 456 */     } else if (getName(paramMethod).equals("<clinit>")) {
/* 457 */       print("{}");
/*     */     } else {
/* 459 */       print(getJavaReturnType((Descriptor)signature));
/* 460 */       print(" ");
/* 461 */       print(getName(paramMethod));
/* 462 */       print(getJavaParameterTypes((Descriptor)signature, accessFlags));
/*     */     } 
/*     */     
/* 465 */     Attribute attribute1 = paramMethod.attributes.get("Exceptions");
/* 466 */     if (attribute1 != null) {
/* 467 */       if (attribute1 instanceof Exceptions_attribute) {
/* 468 */         Exceptions_attribute exceptions_attribute = (Exceptions_attribute)attribute1;
/* 469 */         print(" throws ");
/* 470 */         if (collection != null) {
/* 471 */           writeList("", collection, "");
/*     */         } else {
/* 473 */           for (byte b = 0; b < exceptions_attribute.number_of_exceptions; b++) {
/* 474 */             if (b > 0)
/* 475 */               print(", "); 
/* 476 */             print(getJavaException(exceptions_attribute, b));
/*     */           } 
/*     */         } 
/*     */       } else {
/* 480 */         report("Unexpected or invalid value for Exceptions attribute");
/*     */       } 
/*     */     }
/*     */     
/* 484 */     println(";");
/*     */     
/* 486 */     indent(1);
/*     */     
/* 488 */     if (this.options.showDescriptors) {
/* 489 */       println("descriptor: " + getValue(paramMethod.descriptor));
/*     */     }
/*     */     
/* 492 */     if (this.options.verbose) {
/* 493 */       writeList("flags: ", accessFlags.getMethodFlags(), "\n");
/*     */     }
/*     */     
/* 496 */     Code_attribute code_attribute = null;
/* 497 */     Attribute attribute2 = paramMethod.attributes.get("Code");
/* 498 */     if (attribute2 != null) {
/* 499 */       if (attribute2 instanceof Code_attribute) {
/* 500 */         code_attribute = (Code_attribute)attribute2;
/*     */       } else {
/* 502 */         report("Unexpected or invalid value for Code attribute");
/*     */       } 
/*     */     }
/* 505 */     if (this.options.showAllAttrs) {
/* 506 */       Attribute[] arrayOfAttribute = paramMethod.attributes.attrs;
/* 507 */       for (Attribute attribute : arrayOfAttribute)
/* 508 */         this.attrWriter.write(paramMethod, attribute, this.constant_pool); 
/* 509 */     } else if (code_attribute != null) {
/* 510 */       if (this.options.showDisassembled) {
/* 511 */         println("Code:");
/* 512 */         this.codeWriter.writeInstrs(code_attribute);
/* 513 */         this.codeWriter.writeExceptionTable(code_attribute);
/*     */       } 
/*     */       
/* 516 */       if (this.options.showLineAndLocalVariableTables) {
/* 517 */         this.attrWriter.write(code_attribute, code_attribute.attributes.get("LineNumberTable"), this.constant_pool);
/* 518 */         this.attrWriter.write(code_attribute, code_attribute.attributes.get("LocalVariableTable"), this.constant_pool);
/*     */       } 
/*     */     } 
/*     */     
/* 522 */     indent(-1);
/*     */ 
/*     */ 
/*     */     
/* 526 */     setPendingNewline((this.options.showDisassembled || this.options.showAllAttrs || this.options.showDescriptors || this.options.showLineAndLocalVariableTables || this.options.verbose));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void writeModifiers(Collection<String> paramCollection) {
/* 535 */     for (String str : paramCollection) {
/* 536 */       print(str);
/* 537 */       print(" ");
/*     */     } 
/*     */   }
/*     */   
/*     */   void writeList(String paramString1, Collection<?> paramCollection, String paramString2) {
/* 542 */     print(paramString1);
/* 543 */     String str = "";
/* 544 */     for (Object object : paramCollection) {
/* 545 */       print(str);
/* 546 */       print(object);
/* 547 */       str = ", ";
/*     */     } 
/* 549 */     print(paramString2);
/*     */   }
/*     */   
/*     */   void writeListIfNotEmpty(String paramString1, List<?> paramList, String paramString2) {
/* 553 */     if (paramList != null && paramList.size() > 0)
/* 554 */       writeList(paramString1, paramList, paramString2); 
/*     */   }
/*     */   
/*     */   Signature_attribute getSignature(Attributes paramAttributes) {
/* 558 */     return (Signature_attribute)paramAttributes.get("Signature");
/*     */   }
/*     */   
/*     */   String adjustVarargs(AccessFlags paramAccessFlags, String paramString) {
/* 562 */     if (paramAccessFlags.is(128)) {
/* 563 */       int i = paramString.lastIndexOf("[]");
/* 564 */       if (i > 0) {
/* 565 */         return paramString.substring(0, i) + "..." + paramString.substring(i + 2);
/*     */       }
/*     */     } 
/* 568 */     return paramString;
/*     */   }
/*     */   
/*     */   String getJavaName(ClassFile paramClassFile) {
/*     */     try {
/* 573 */       return getJavaName(paramClassFile.getName());
/* 574 */     } catch (ConstantPoolException constantPoolException) {
/* 575 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   String getJavaSuperclassName(ClassFile paramClassFile) {
/*     */     try {
/* 581 */       return getJavaName(paramClassFile.getSuperclassName());
/* 582 */     } catch (ConstantPoolException constantPoolException) {
/* 583 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   String getJavaInterfaceName(ClassFile paramClassFile, int paramInt) {
/*     */     try {
/* 589 */       return getJavaName(paramClassFile.getInterfaceName(paramInt));
/* 590 */     } catch (ConstantPoolException constantPoolException) {
/* 591 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   String getJavaFieldType(Descriptor paramDescriptor) {
/*     */     try {
/* 597 */       return getJavaName(paramDescriptor.getFieldType(this.constant_pool));
/* 598 */     } catch (ConstantPoolException constantPoolException) {
/* 599 */       return report(constantPoolException);
/* 600 */     } catch (DescriptorException descriptorException) {
/* 601 */       return report(descriptorException);
/*     */     } 
/*     */   }
/*     */   
/*     */   String getJavaReturnType(Descriptor paramDescriptor) {
/*     */     try {
/* 607 */       return getJavaName(paramDescriptor.getReturnType(this.constant_pool));
/* 608 */     } catch (ConstantPoolException constantPoolException) {
/* 609 */       return report(constantPoolException);
/* 610 */     } catch (DescriptorException descriptorException) {
/* 611 */       return report(descriptorException);
/*     */     } 
/*     */   }
/*     */   
/*     */   String getJavaParameterTypes(Descriptor paramDescriptor, AccessFlags paramAccessFlags) {
/*     */     try {
/* 617 */       return getJavaName(adjustVarargs(paramAccessFlags, paramDescriptor.getParameterTypes(this.constant_pool)));
/* 618 */     } catch (ConstantPoolException constantPoolException) {
/* 619 */       return report(constantPoolException);
/* 620 */     } catch (DescriptorException descriptorException) {
/* 621 */       return report(descriptorException);
/*     */     } 
/*     */   }
/*     */   
/*     */   String getJavaException(Exceptions_attribute paramExceptions_attribute, int paramInt) {
/*     */     try {
/* 627 */       return getJavaName(paramExceptions_attribute.getException(paramInt, this.constant_pool));
/* 628 */     } catch (ConstantPoolException constantPoolException) {
/* 629 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   String getValue(Descriptor paramDescriptor) {
/*     */     try {
/* 635 */       return paramDescriptor.getValue(this.constant_pool);
/* 636 */     } catch (ConstantPoolException constantPoolException) {
/* 637 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   String getFieldName(Field paramField) {
/*     */     try {
/* 643 */       return paramField.getName(this.constant_pool);
/* 644 */     } catch (ConstantPoolException constantPoolException) {
/* 645 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   String getName(Method paramMethod) {
/*     */     try {
/* 651 */       return paramMethod.getName(this.constant_pool);
/* 652 */     } catch (ConstantPoolException constantPoolException) {
/* 653 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */   
/*     */   static String getJavaName(String paramString) {
/* 658 */     return paramString.replace('/', '.');
/*     */   }
/*     */   
/*     */   String getSourceFile(SourceFile_attribute paramSourceFile_attribute) {
/*     */     try {
/* 663 */       return paramSourceFile_attribute.getSourceFile(this.constant_pool);
/* 664 */     } catch (ConstantPoolException constantPoolException) {
/* 665 */       return report(constantPoolException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getConstantValue(Descriptor paramDescriptor, int paramInt) {
/*     */     try {
/*     */       ConstantPool.CONSTANT_Integer_info cONSTANT_Integer_info;
/*     */       ConstantPool.CONSTANT_String_info cONSTANT_String_info;
/*     */       String str;
/* 680 */       ConstantPool.CPInfo cPInfo = this.constant_pool.get(paramInt);
/*     */       
/* 682 */       switch (cPInfo.getTag()) {
/*     */         case 3:
/* 684 */           cONSTANT_Integer_info = (ConstantPool.CONSTANT_Integer_info)cPInfo;
/*     */           
/* 686 */           str = paramDescriptor.getValue(this.constant_pool);
/* 687 */           if (str.equals("C"))
/* 688 */             return getConstantCharValue((char)cONSTANT_Integer_info.value); 
/* 689 */           if (str.equals("Z")) {
/* 690 */             return String.valueOf((cONSTANT_Integer_info.value == 1));
/*     */           }
/* 692 */           return String.valueOf(cONSTANT_Integer_info.value);
/*     */ 
/*     */ 
/*     */         
/*     */         case 8:
/* 697 */           cONSTANT_String_info = (ConstantPool.CONSTANT_String_info)cPInfo;
/*     */           
/* 699 */           return getConstantStringValue(cONSTANT_String_info.getString());
/*     */       } 
/*     */ 
/*     */       
/* 703 */       return this.constantWriter.stringValue(cPInfo);
/*     */     }
/* 705 */     catch (ConstantPoolException constantPoolException) {
/* 706 */       return "#" + paramInt;
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getConstantCharValue(char paramChar) {
/* 711 */     StringBuilder stringBuilder = new StringBuilder();
/* 712 */     stringBuilder.append('\'');
/* 713 */     stringBuilder.append(esc(paramChar, '\''));
/* 714 */     stringBuilder.append('\'');
/* 715 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   private String getConstantStringValue(String paramString) {
/* 719 */     StringBuilder stringBuilder = new StringBuilder();
/* 720 */     stringBuilder.append("\"");
/* 721 */     for (byte b = 0; b < paramString.length(); b++) {
/* 722 */       stringBuilder.append(esc(paramString.charAt(b), '"'));
/*     */     }
/* 724 */     stringBuilder.append("\"");
/* 725 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   private String esc(char paramChar1, char paramChar2) {
/* 729 */     if (' ' <= paramChar1 && paramChar1 <= '~' && paramChar1 != paramChar2)
/* 730 */       return String.valueOf(paramChar1); 
/* 731 */     switch (paramChar1) { case '\b':
/* 732 */         return "\\b";
/* 733 */       case '\n': return "\\n";
/* 734 */       case '\t': return "\\t";
/* 735 */       case '\f': return "\\f";
/* 736 */       case '\r': return "\\r";
/* 737 */       case '\\': return "\\\\";
/* 738 */       case '\'': return "\\'";
/* 739 */       case '"': return "\\\""; }
/* 740 */      return String.format("\\u%04x", new Object[] { Integer.valueOf(paramChar1) });
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\ClassWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */