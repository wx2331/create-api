/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.SimpleTypeVisitor8;
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
/*     */ public class LLNI
/*     */   extends Gen
/*     */ {
/*  60 */   protected final char innerDelim = '$';
/*     */   protected Set<String> doneHandleTypes;
/*     */   List<VariableElement> fields;
/*     */   List<ExecutableElement> methods;
/*     */   private boolean doubleAlign;
/*  65 */   private int padFieldNum = 0;
/*     */   
/*     */   LLNI(boolean paramBoolean, Util paramUtil) {
/*  68 */     super(paramUtil);
/*  69 */     this.doubleAlign = paramBoolean;
/*     */   }
/*     */   
/*     */   protected String getIncludes() {
/*  73 */     return "";
/*     */   }
/*     */   
/*     */   protected void write(OutputStream paramOutputStream, TypeElement paramTypeElement) throws Util.Exit {
/*     */     try {
/*  78 */       String str = mangleClassName(paramTypeElement.getQualifiedName().toString());
/*  79 */       PrintWriter printWriter = wrapWriter(paramOutputStream);
/*  80 */       this.fields = ElementFilter.fieldsIn(paramTypeElement.getEnclosedElements());
/*  81 */       this.methods = ElementFilter.methodsIn(paramTypeElement.getEnclosedElements());
/*  82 */       generateDeclsForClass(printWriter, paramTypeElement, str);
/*     */     }
/*  84 */     catch (SignatureException signatureException) {
/*  85 */       this.util.error("llni.sigerror", new Object[] { signatureException.getMessage() });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateDeclsForClass(PrintWriter paramPrintWriter, TypeElement paramTypeElement, String paramString) throws TypeSignature.SignatureException, Util.Exit {
/*  92 */     this.doneHandleTypes = new HashSet<>();
/*     */ 
/*     */     
/*  95 */     genHandleType((PrintWriter)null, "java.lang.Class");
/*  96 */     genHandleType((PrintWriter)null, "java.lang.ClassLoader");
/*  97 */     genHandleType((PrintWriter)null, "java.lang.Object");
/*  98 */     genHandleType((PrintWriter)null, "java.lang.String");
/*  99 */     genHandleType((PrintWriter)null, "java.lang.Thread");
/* 100 */     genHandleType((PrintWriter)null, "java.lang.ThreadGroup");
/* 101 */     genHandleType((PrintWriter)null, "java.lang.Throwable");
/*     */     
/* 103 */     paramPrintWriter.println("/* LLNI Header for class " + paramTypeElement.getQualifiedName() + " */" + this.lineSep);
/* 104 */     paramPrintWriter.println("#ifndef _Included_" + paramString);
/* 105 */     paramPrintWriter.println("#define _Included_" + paramString);
/* 106 */     paramPrintWriter.println("#include \"typedefs.h\"");
/* 107 */     paramPrintWriter.println("#include \"llni.h\"");
/* 108 */     paramPrintWriter.println("#include \"jni.h\"" + this.lineSep);
/*     */     
/* 110 */     forwardDecls(paramPrintWriter, paramTypeElement);
/* 111 */     structSectionForClass(paramPrintWriter, paramTypeElement, paramString);
/* 112 */     methodSectionForClass(paramPrintWriter, paramTypeElement, paramString);
/* 113 */     paramPrintWriter.println("#endif");
/*     */   }
/*     */   
/*     */   protected void genHandleType(PrintWriter paramPrintWriter, String paramString) {
/* 117 */     String str = mangleClassName(paramString);
/* 118 */     if (!this.doneHandleTypes.contains(str)) {
/* 119 */       this.doneHandleTypes.add(str);
/* 120 */       if (paramPrintWriter != null) {
/* 121 */         paramPrintWriter.println("#ifndef DEFINED_" + str);
/* 122 */         paramPrintWriter.println("    #define DEFINED_" + str);
/* 123 */         paramPrintWriter.println("    GEN_HANDLE_TYPES(" + str + ");");
/* 124 */         paramPrintWriter.println("#endif" + this.lineSep);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String mangleClassName(String paramString) {
/* 130 */     return paramString.replace('.', '_')
/* 131 */       .replace('/', '_')
/* 132 */       .replace('$', '_');
/*     */   }
/*     */ 
/*     */   
/*     */   protected void forwardDecls(PrintWriter paramPrintWriter, TypeElement paramTypeElement) throws TypeSignature.SignatureException {
/* 137 */     TypeElement typeElement1 = this.elems.getTypeElement("java.lang.Object");
/* 138 */     if (paramTypeElement.equals(typeElement1)) {
/*     */       return;
/*     */     }
/* 141 */     genHandleType(paramPrintWriter, paramTypeElement.getQualifiedName().toString());
/* 142 */     TypeElement typeElement2 = (TypeElement)this.types.asElement(paramTypeElement.getSuperclass());
/*     */     
/* 144 */     if (typeElement2 != null) {
/* 145 */       String str = typeElement2.getQualifiedName().toString();
/* 146 */       forwardDecls(paramPrintWriter, typeElement2);
/*     */     } 
/*     */     
/* 149 */     for (VariableElement variableElement : this.fields) {
/*     */       
/* 151 */       if (!variableElement.getModifiers().contains(Modifier.STATIC)) {
/* 152 */         TypeMirror typeMirror = this.types.erasure(variableElement.asType());
/* 153 */         TypeSignature typeSignature = new TypeSignature(this.elems);
/* 154 */         String str1 = typeSignature.qualifiedTypeName(typeMirror);
/* 155 */         String str2 = typeSignature.getTypeSignature(str1);
/*     */         
/* 157 */         if (str2.charAt(0) != '[') {
/* 158 */           forwardDeclsFromSig(paramPrintWriter, str2);
/*     */         }
/*     */       } 
/*     */     } 
/* 162 */     for (ExecutableElement executableElement : this.methods) {
/*     */       
/* 164 */       if (executableElement.getModifiers().contains(Modifier.NATIVE)) {
/* 165 */         TypeMirror typeMirror = this.types.erasure(executableElement.getReturnType());
/* 166 */         String str1 = signature(executableElement);
/* 167 */         TypeSignature typeSignature = new TypeSignature(this.elems);
/* 168 */         String str2 = typeSignature.getTypeSignature(str1, typeMirror);
/*     */         
/* 170 */         if (str2.charAt(0) != '[') {
/* 171 */           forwardDeclsFromSig(paramPrintWriter, str2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void forwardDeclsFromSig(PrintWriter paramPrintWriter, String paramString) {
/* 178 */     int i = paramString.length();
/* 179 */     int j = (paramString.charAt(0) == '(') ? 1 : 0;
/*     */ 
/*     */     
/* 182 */     while (j < i) {
/* 183 */       if (paramString.charAt(j) == 'L') {
/* 184 */         int k = j + 1;
/* 185 */         for (; paramString.charAt(k) != ';'; k++);
/* 186 */         genHandleType(paramPrintWriter, paramString.substring(j + 1, k));
/* 187 */         j = k + 1; continue;
/*     */       } 
/* 189 */       j++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void structSectionForClass(PrintWriter paramPrintWriter, TypeElement paramTypeElement, String paramString) {
/* 197 */     String str = paramTypeElement.getQualifiedName().toString();
/*     */     
/* 199 */     if (paramString.equals("java_lang_Object")) {
/* 200 */       paramPrintWriter.println("/* struct java_lang_Object is defined in typedefs.h. */");
/* 201 */       paramPrintWriter.println();
/*     */       return;
/*     */     } 
/* 204 */     paramPrintWriter.println("#if !defined(__i386)");
/* 205 */     paramPrintWriter.println("#pragma pack(4)");
/* 206 */     paramPrintWriter.println("#endif");
/* 207 */     paramPrintWriter.println();
/* 208 */     paramPrintWriter.println("struct " + paramString + " {");
/* 209 */     paramPrintWriter.println("    ObjHeader h;");
/* 210 */     paramPrintWriter.print(fieldDefs(paramTypeElement, paramString));
/*     */     
/* 212 */     if (str.equals("java.lang.Class")) {
/* 213 */       paramPrintWriter.println("    Class *LLNI_mask(cClass);  /* Fake field; don't access (see oobj.h) */");
/*     */     }
/* 215 */     paramPrintWriter.println("};" + this.lineSep + this.lineSep + "#pragma pack()");
/* 216 */     paramPrintWriter.println();
/*     */   }
/*     */   
/*     */   private static class FieldDefsRes
/*     */   {
/*     */     public String className;
/*     */     public FieldDefsRes parent;
/*     */     public String s;
/*     */     public int byteSize;
/*     */     public boolean bottomMost;
/*     */     public boolean printedOne = false;
/*     */     
/*     */     FieldDefsRes(TypeElement param1TypeElement, FieldDefsRes param1FieldDefsRes, boolean param1Boolean) {
/* 229 */       this.className = param1TypeElement.getQualifiedName().toString();
/* 230 */       this.parent = param1FieldDefsRes;
/* 231 */       this.bottomMost = param1Boolean;
/* 232 */       boolean bool = false;
/* 233 */       if (param1FieldDefsRes == null) { this.s = ""; }
/* 234 */       else { this.s = param1FieldDefsRes.s; }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean doField(FieldDefsRes paramFieldDefsRes, VariableElement paramVariableElement, String paramString, boolean paramBoolean) {
/* 242 */     String str = addStructMember(paramVariableElement, paramString, paramBoolean);
/* 243 */     if (str != null) {
/* 244 */       if (!paramFieldDefsRes.printedOne) {
/* 245 */         if (paramFieldDefsRes.bottomMost) {
/* 246 */           if (paramFieldDefsRes.s.length() != 0)
/* 247 */             paramFieldDefsRes.s += "    /* local members: */" + this.lineSep; 
/*     */         } else {
/* 249 */           paramFieldDefsRes.s += "    /* inherited members from " + paramFieldDefsRes.className + ": */" + this.lineSep;
/*     */         } 
/*     */         
/* 252 */         paramFieldDefsRes.printedOne = true;
/*     */       } 
/* 254 */       paramFieldDefsRes.s += str;
/* 255 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private int doTwoWordFields(FieldDefsRes paramFieldDefsRes, TypeElement paramTypeElement, int paramInt, String paramString, boolean paramBoolean) {
/* 264 */     boolean bool = true;
/* 265 */     List<VariableElement> list = ElementFilter.fieldsIn(paramTypeElement.getEnclosedElements());
/*     */     
/* 267 */     for (VariableElement variableElement : list) {
/* 268 */       TypeKind typeKind = variableElement.asType().getKind();
/* 269 */       boolean bool1 = (typeKind == TypeKind.LONG || typeKind == TypeKind.DOUBLE) ? true : false;
/* 270 */       if (bool1 && doField(paramFieldDefsRes, variableElement, paramString, (bool && paramBoolean))) {
/* 271 */         paramInt += 8; bool = false;
/*     */       } 
/*     */     } 
/* 274 */     return paramInt;
/*     */   }
/*     */   
/*     */   String fieldDefs(TypeElement paramTypeElement, String paramString) {
/* 278 */     FieldDefsRes fieldDefsRes = fieldDefs(paramTypeElement, paramString, true);
/* 279 */     return fieldDefsRes.s;
/*     */   }
/*     */ 
/*     */   
/*     */   FieldDefsRes fieldDefs(TypeElement paramTypeElement, String paramString, boolean paramBoolean) {
/*     */     FieldDefsRes fieldDefsRes;
/*     */     int i;
/* 286 */     boolean bool = false;
/*     */     
/* 288 */     TypeElement typeElement = (TypeElement)this.types.asElement(paramTypeElement.getSuperclass());
/*     */     
/* 290 */     if (typeElement != null) {
/* 291 */       String str = typeElement.getQualifiedName().toString();
/*     */       
/* 293 */       fieldDefsRes = new FieldDefsRes(paramTypeElement, fieldDefs(typeElement, paramString, false), paramBoolean);
/*     */       
/* 295 */       i = fieldDefsRes.parent.byteSize;
/*     */     } else {
/* 297 */       fieldDefsRes = new FieldDefsRes(paramTypeElement, null, paramBoolean);
/* 298 */       i = 0;
/*     */     } 
/*     */     
/* 301 */     List<VariableElement> list = ElementFilter.fieldsIn(paramTypeElement.getEnclosedElements());
/*     */     
/* 303 */     for (VariableElement variableElement : list) {
/*     */       
/* 305 */       if (this.doubleAlign && !bool && i % 8 == 0) {
/* 306 */         i = doTwoWordFields(fieldDefsRes, paramTypeElement, i, paramString, false);
/* 307 */         bool = true;
/*     */       } 
/*     */       
/* 310 */       TypeKind typeKind = variableElement.asType().getKind();
/* 311 */       boolean bool1 = (typeKind == TypeKind.LONG || typeKind == TypeKind.DOUBLE) ? true : false;
/*     */       
/* 313 */       if ((!this.doubleAlign || !bool1) && 
/* 314 */         doField(fieldDefsRes, variableElement, paramString, false)) i += 4;
/*     */     
/*     */     } 
/*     */ 
/*     */     
/* 319 */     if (this.doubleAlign && !bool) {
/* 320 */       if (i % 8 != 0) i += 4; 
/* 321 */       i = doTwoWordFields(fieldDefsRes, paramTypeElement, i, paramString, true);
/*     */     } 
/*     */     
/* 324 */     fieldDefsRes.byteSize = i;
/* 325 */     return fieldDefsRes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String addStructMember(VariableElement paramVariableElement, String paramString, boolean paramBoolean) {
/* 331 */     String str = null;
/*     */     
/* 333 */     if (paramVariableElement.getModifiers().contains(Modifier.STATIC)) {
/* 334 */       str = addStaticStructMember(paramVariableElement, paramString);
/*     */     }
/*     */     else {
/*     */       
/* 338 */       TypeMirror typeMirror = this.types.erasure(paramVariableElement.asType());
/* 339 */       if (paramBoolean) str = "    java_int padWord" + this.padFieldNum++ + ";" + this.lineSep; 
/* 340 */       str = "    " + llniType(typeMirror, false, false) + " " + llniFieldName(paramVariableElement);
/* 341 */       if (isLongOrDouble(typeMirror)) str = str + "[2]"; 
/* 342 */       str = str + ";" + this.lineSep;
/*     */     } 
/* 344 */     return str;
/*     */   }
/*     */ 
/*     */   
/* 348 */   private static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String addStaticStructMember(VariableElement paramVariableElement, String paramString) {
/* 354 */     String str = null;
/* 355 */     Object object = null;
/*     */     
/* 357 */     if (!paramVariableElement.getModifiers().contains(Modifier.STATIC))
/* 358 */       return str; 
/* 359 */     if (!paramVariableElement.getModifiers().contains(Modifier.FINAL)) {
/* 360 */       return str;
/*     */     }
/* 362 */     object = paramVariableElement.getConstantValue();
/*     */     
/* 364 */     if (object != null) {
/*     */ 
/*     */       
/* 367 */       String str1 = paramString + "_" + paramVariableElement.getSimpleName();
/* 368 */       String str2 = null;
/* 369 */       long l = 0L;
/*     */       
/* 371 */       if (object instanceof Byte || object instanceof Short || object instanceof Integer)
/*     */       
/*     */       { 
/* 374 */         str2 = "L";
/* 375 */         l = ((Number)object).intValue(); }
/*     */       
/* 377 */       else if (object instanceof Long)
/*     */       
/* 379 */       { str2 = isWindows ? "i64" : "LL";
/* 380 */         l = ((Long)object).longValue(); }
/*     */       
/* 382 */       else if (object instanceof Float) { str2 = "f"; }
/* 383 */       else if (object instanceof Double) { str2 = ""; }
/* 384 */       else if (object instanceof Character)
/* 385 */       { str2 = "L";
/* 386 */         Character character = (Character)object;
/* 387 */         l = (character.charValue() & Character.MAX_VALUE); }
/*     */       
/* 389 */       if (str2 != null)
/*     */       {
/*     */ 
/*     */         
/* 393 */         if ((str2.equals("L") && l == -2147483648L) || (str2
/* 394 */           .equals("LL") && l == Long.MIN_VALUE)) {
/* 395 */           str = "    #undef  " + str1 + this.lineSep + "    #define " + str1 + " (" + (l + 1L) + str2 + "-1)" + this.lineSep;
/*     */         
/*     */         }
/* 398 */         else if (str2.equals("L") || str2.endsWith("LL")) {
/* 399 */           str = "    #undef  " + str1 + this.lineSep + "    #define " + str1 + " " + l + str2 + this.lineSep;
/*     */         } else {
/*     */           
/* 402 */           str = "    #undef  " + str1 + this.lineSep + "    #define " + str1 + " " + object + str2 + this.lineSep;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 407 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void methodSectionForClass(PrintWriter paramPrintWriter, TypeElement paramTypeElement, String paramString) throws TypeSignature.SignatureException, Util.Exit {
/* 413 */     String str = methodDecls(paramTypeElement, paramString);
/*     */     
/* 415 */     if (str.length() != 0) {
/* 416 */       paramPrintWriter.println("/* Native method declarations: */" + this.lineSep);
/* 417 */       paramPrintWriter.println("#ifdef __cplusplus");
/* 418 */       paramPrintWriter.println("extern \"C\" {");
/* 419 */       paramPrintWriter.println("#endif" + this.lineSep);
/* 420 */       paramPrintWriter.println(str);
/* 421 */       paramPrintWriter.println("#ifdef __cplusplus");
/* 422 */       paramPrintWriter.println("}");
/* 423 */       paramPrintWriter.println("#endif");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String methodDecls(TypeElement paramTypeElement, String paramString) throws TypeSignature.SignatureException, Util.Exit {
/* 430 */     String str = "";
/* 431 */     for (ExecutableElement executableElement : this.methods) {
/* 432 */       if (executableElement.getModifiers().contains(Modifier.NATIVE))
/* 433 */         str = str + methodDecl(executableElement, paramTypeElement, paramString); 
/*     */     } 
/* 435 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String methodDecl(ExecutableElement paramExecutableElement, TypeElement paramTypeElement, String paramString) throws TypeSignature.SignatureException, Util.Exit {
/* 441 */     String str1 = null;
/*     */     
/* 443 */     TypeMirror typeMirror = this.types.erasure(paramExecutableElement.getReturnType());
/* 444 */     String str2 = signature(paramExecutableElement);
/* 445 */     TypeSignature typeSignature = new TypeSignature(this.elems);
/* 446 */     String str3 = typeSignature.getTypeSignature(str2, typeMirror);
/* 447 */     boolean bool = needLongName(paramExecutableElement, paramTypeElement);
/*     */     
/* 449 */     if (str3.charAt(0) != '(') {
/* 450 */       this.util.error("invalid.method.signature", new Object[] { str3 });
/*     */     }
/*     */ 
/*     */     
/* 454 */     str1 = "JNIEXPORT " + jniType(typeMirror) + " JNICALL" + this.lineSep + jniMethodName(paramExecutableElement, paramString, bool) + "(JNIEnv *, " + cRcvrDecl(paramExecutableElement, paramString);
/* 455 */     List<? extends VariableElement> list = paramExecutableElement.getParameters();
/* 456 */     ArrayList<TypeMirror> arrayList = new ArrayList();
/* 457 */     for (VariableElement variableElement : list) {
/* 458 */       arrayList.add(this.types.erasure(variableElement.asType()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 465 */     for (TypeMirror typeMirror1 : arrayList)
/* 466 */       str1 = str1 + ", " + jniType(typeMirror1); 
/* 467 */     str1 = str1 + ");" + this.lineSep;
/* 468 */     return str1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean needLongName(ExecutableElement paramExecutableElement, TypeElement paramTypeElement) {
/* 473 */     Name name = paramExecutableElement.getSimpleName();
/* 474 */     for (ExecutableElement executableElement : this.methods) {
/* 475 */       if (executableElement != paramExecutableElement && executableElement
/* 476 */         .getModifiers().contains(Modifier.NATIVE) && name
/* 477 */         .equals(executableElement.getSimpleName()))
/* 478 */         return true; 
/*     */     } 
/* 480 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String jniMethodName(ExecutableElement paramExecutableElement, String paramString, boolean paramBoolean) throws TypeSignature.SignatureException {
/* 486 */     String str = "Java_" + paramString + "_" + paramExecutableElement.getSimpleName();
/*     */     
/* 488 */     if (paramBoolean) {
/* 489 */       TypeMirror typeMirror = this.types.erasure(paramExecutableElement.getReturnType());
/* 490 */       List<? extends VariableElement> list = paramExecutableElement.getParameters();
/* 491 */       ArrayList<TypeMirror> arrayList = new ArrayList();
/* 492 */       for (VariableElement variableElement : list) {
/* 493 */         arrayList.add(this.types.erasure(variableElement.asType()));
/*     */       }
/*     */       
/* 496 */       str = str + "__";
/* 497 */       for (TypeMirror typeMirror1 : arrayList) {
/* 498 */         String str1 = typeMirror1.toString();
/* 499 */         TypeSignature typeSignature = new TypeSignature(this.elems);
/* 500 */         String str2 = typeSignature.getTypeSignature(str1);
/* 501 */         str = str + nameToIdentifier(str2);
/*     */       } 
/*     */     } 
/* 504 */     return str;
/*     */   }
/*     */   
/*     */   protected final String jniType(TypeMirror paramTypeMirror) throws Util.Exit {
/*     */     TypeMirror typeMirror;
/* 509 */     TypeElement typeElement1 = this.elems.getTypeElement("java.lang.Throwable");
/* 510 */     TypeElement typeElement2 = this.elems.getTypeElement("java.lang.Class");
/* 511 */     TypeElement typeElement3 = this.elems.getTypeElement("java.lang.String");
/* 512 */     Element element = this.types.asElement(paramTypeMirror);
/*     */     
/* 514 */     switch (paramTypeMirror.getKind()) {
/*     */       case ARRAY:
/* 516 */         typeMirror = ((ArrayType)paramTypeMirror).getComponentType();
/* 517 */         switch (typeMirror.getKind()) { case BOOLEAN:
/* 518 */             return "jbooleanArray";
/* 519 */           case BYTE: return "jbyteArray";
/* 520 */           case CHAR: return "jcharArray";
/* 521 */           case SHORT: return "jshortArray";
/* 522 */           case INT: return "jintArray";
/* 523 */           case LONG: return "jlongArray";
/* 524 */           case FLOAT: return "jfloatArray";
/* 525 */           case DOUBLE: return "jdoubleArray";
/*     */           case ARRAY: case DECLARED:
/* 527 */             return "jobjectArray"; }
/* 528 */          throw new Error(typeMirror.toString());
/*     */ 
/*     */       
/*     */       case VOID:
/* 532 */         return "void";
/* 533 */       case BOOLEAN: return "jboolean";
/* 534 */       case BYTE: return "jbyte";
/* 535 */       case CHAR: return "jchar";
/* 536 */       case SHORT: return "jshort";
/* 537 */       case INT: return "jint";
/* 538 */       case LONG: return "jlong";
/* 539 */       case FLOAT: return "jfloat";
/* 540 */       case DOUBLE: return "jdouble";
/*     */       
/*     */       case DECLARED:
/* 543 */         if (element.equals(typeElement3))
/* 544 */           return "jstring"; 
/* 545 */         if (this.types.isAssignable(paramTypeMirror, typeElement1.asType()))
/* 546 */           return "jthrowable"; 
/* 547 */         if (this.types.isAssignable(paramTypeMirror, typeElement2.asType())) {
/* 548 */           return "jclass";
/*     */         }
/* 550 */         return "jobject";
/*     */     } 
/*     */ 
/*     */     
/* 554 */     this.util.bug("jni.unknown.type");
/* 555 */     return null;
/*     */   } protected String llniType(TypeMirror paramTypeMirror, boolean paramBoolean1, boolean paramBoolean2) {
/*     */     TypeMirror typeMirror;
/*     */     TypeElement typeElement;
/* 559 */     String str = null;
/*     */     
/* 561 */     switch (paramTypeMirror.getKind()) {
/*     */       case ARRAY:
/* 563 */         typeMirror = ((ArrayType)paramTypeMirror).getComponentType();
/* 564 */         switch (typeMirror.getKind()) { case BOOLEAN:
/* 565 */             str = "IArrayOfBoolean"; break;
/* 566 */           case BYTE: str = "IArrayOfByte"; break;
/* 567 */           case CHAR: str = "IArrayOfChar"; break;
/* 568 */           case SHORT: str = "IArrayOfShort"; break;
/* 569 */           case INT: str = "IArrayOfInt"; break;
/* 570 */           case LONG: str = "IArrayOfLong"; break;
/* 571 */           case FLOAT: str = "IArrayOfFloat"; break;
/* 572 */           case DOUBLE: str = "IArrayOfDouble"; break;
/*     */           case ARRAY: case DECLARED:
/* 574 */             str = "IArrayOfRef"; break;
/* 575 */           default: throw new Error(typeMirror.getKind() + " " + typeMirror); }
/*     */         
/* 577 */         if (!paramBoolean1) str = "DEREFERENCED_" + str;
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
/* 615 */         return str;case VOID: str = "void"; return str;case BOOLEAN: case BYTE: case CHAR: case SHORT: case INT: str = "java_int"; return str;case LONG: str = paramBoolean2 ? "java_long" : "val32 /* java_long */"; return str;case FLOAT: str = "java_float"; return str;case DOUBLE: str = paramBoolean2 ? "java_double" : "val32 /* java_double */"; return str;case DECLARED: typeElement = (TypeElement)this.types.asElement(paramTypeMirror); str = "I" + mangleClassName(typeElement.getQualifiedName().toString()); if (!paramBoolean1) str = "DEREFERENCED_" + str;  return str;
/*     */     } 
/*     */     throw new Error(paramTypeMirror.getKind() + " " + paramTypeMirror);
/*     */   } protected final String cRcvrDecl(Element paramElement, String paramString) {
/* 619 */     return paramElement.getModifiers().contains(Modifier.STATIC) ? "jclass" : "jobject";
/*     */   }
/*     */   
/*     */   protected String maskName(String paramString) {
/* 623 */     return "LLNI_mask(" + paramString + ")";
/*     */   }
/*     */   
/*     */   protected String llniFieldName(VariableElement paramVariableElement) {
/* 627 */     return maskName(paramVariableElement.getSimpleName().toString());
/*     */   }
/*     */   
/*     */   protected final boolean isLongOrDouble(TypeMirror paramTypeMirror) {
/* 631 */     SimpleTypeVisitor8<Boolean, Void> simpleTypeVisitor8 = new SimpleTypeVisitor8<Boolean, Void>() {
/*     */         public Boolean defaultAction(TypeMirror param1TypeMirror, Void param1Void) {
/* 633 */           return Boolean.valueOf(false);
/*     */         }
/*     */         public Boolean visitArray(ArrayType param1ArrayType, Void param1Void) {
/* 636 */           return visit(param1ArrayType.getComponentType(), param1Void);
/*     */         }
/*     */         public Boolean visitPrimitive(PrimitiveType param1PrimitiveType, Void param1Void) {
/* 639 */           TypeKind typeKind = param1PrimitiveType.getKind();
/* 640 */           return Boolean.valueOf((typeKind == TypeKind.LONG || typeKind == TypeKind.DOUBLE));
/*     */         }
/*     */       };
/* 643 */     return ((Boolean)simpleTypeVisitor8.visit(paramTypeMirror, null)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String nameToIdentifier(String paramString) {
/* 649 */     int i = paramString.length();
/* 650 */     StringBuilder stringBuilder = new StringBuilder(i);
/* 651 */     for (byte b = 0; b < i; b++) {
/* 652 */       char c = paramString.charAt(b);
/* 653 */       if (isASCIILetterOrDigit(c)) {
/* 654 */         stringBuilder.append(c);
/* 655 */       } else if (c == '/') {
/* 656 */         stringBuilder.append('_');
/* 657 */       } else if (c == '.') {
/* 658 */         stringBuilder.append('_');
/* 659 */       } else if (c == '_') {
/* 660 */         stringBuilder.append("_1");
/* 661 */       } else if (c == ';') {
/* 662 */         stringBuilder.append("_2");
/* 663 */       } else if (c == '[') {
/* 664 */         stringBuilder.append("_3");
/*     */       } else {
/* 666 */         stringBuilder.append("_0" + c);
/*     */       } 
/* 668 */     }  return new String(stringBuilder);
/*     */   }
/*     */   
/*     */   protected final boolean isASCIILetterOrDigit(char paramChar) {
/* 672 */     if ((paramChar >= 'A' && paramChar <= 'Z') || (paramChar >= 'a' && paramChar <= 'z') || (paramChar >= '0' && paramChar <= '9'))
/*     */     {
/*     */       
/* 675 */       return true;
/*     */     }
/* 677 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\LLNI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */