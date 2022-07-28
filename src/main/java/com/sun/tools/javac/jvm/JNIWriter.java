/*     */ package com.sun.tools.javac.jvm;
/*     */
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.model.JavacElements;
/*     */ import com.sun.tools.javac.model.JavacTypes;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.NoType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVariable;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleTypeVisitor8;
/*     */ import javax.lang.model.util.Types;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.StandardLocation;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class JNIWriter
/*     */ {
/*  81 */   protected static final Context.Key<JNIWriter> jniWriterKey = new Context.Key();
/*     */
/*     */
/*     */   private final JavaFileManager fileManager;
/*     */
/*     */
/*     */   JavacElements elements;
/*     */
/*     */
/*     */   JavacTypes types;
/*     */
/*     */
/*     */   private final Log log;
/*     */
/*     */
/*     */   private boolean verbose;
/*     */
/*     */
/*     */   private boolean checkAll;
/*     */
/*     */
/*     */   private Mangle mangler;
/*     */
/*     */
/*     */   private Context context;
/*     */
/*     */   private Symtab syms;
/*     */
/*     */   private String lineSep;
/*     */
/* 111 */   private final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
/*     */
/*     */
/*     */   public static JNIWriter instance(Context paramContext) {
/* 115 */     JNIWriter jNIWriter = (JNIWriter)paramContext.get(jniWriterKey);
/* 116 */     if (jNIWriter == null)
/* 117 */       jNIWriter = new JNIWriter(paramContext);
/* 118 */     return jNIWriter;
/*     */   }
/*     */
/*     */
/*     */
/*     */   private JNIWriter(Context paramContext) {
/* 124 */     paramContext.put(jniWriterKey, this);
/* 125 */     this.fileManager = (JavaFileManager)paramContext.get(JavaFileManager.class);
/* 126 */     this.log = Log.instance(paramContext);
/*     */
/* 128 */     Options options = Options.instance(paramContext);
/* 129 */     this.verbose = options.isSet(Option.VERBOSE);
/* 130 */     this.checkAll = options.isSet("javah:full");
/*     */
/* 132 */     this.context = paramContext;
/* 133 */     this.syms = Symtab.instance(paramContext);
/*     */
/* 135 */     this.lineSep = System.getProperty("line.separator");
/*     */   }
/*     */
/*     */   private void lazyInit() {
/* 139 */     if (this.mangler == null) {
/* 140 */       this.elements = JavacElements.instance(this.context);
/* 141 */       this.types = JavacTypes.instance(this.context);
/* 142 */       this.mangler = new Mangle((Elements)this.elements, (Types)this.types);
/*     */     }
/*     */   }
/*     */
/*     */   public boolean needsHeader(Symbol.ClassSymbol paramClassSymbol) {
/* 147 */     if (paramClassSymbol.isLocal() || (paramClassSymbol.flags() & 0x1000L) != 0L) {
/* 148 */       return false;
/*     */     }
/* 150 */     if (this.checkAll) {
/* 151 */       return needsHeader(paramClassSymbol.outermostClass(), true);
/*     */     }
/* 153 */     return needsHeader(paramClassSymbol, false);
/*     */   }
/*     */
/*     */   private boolean needsHeader(Symbol.ClassSymbol paramClassSymbol, boolean paramBoolean) {
/* 157 */     if (paramClassSymbol.isLocal() || (paramClassSymbol.flags() & 0x1000L) != 0L)
/* 158 */       return false;
/*     */     Scope.Entry entry;
/* 160 */     for (entry = paramClassSymbol.members_field.elems; entry != null; entry = entry.sibling) {
/* 161 */       if (entry.sym.kind == 16 && (entry.sym.flags() & 0x100L) != 0L)
/* 162 */         return true;
/* 163 */       for (Attribute.Compound compound : entry.sym.getDeclarationAttributes()) {
/* 164 */         if (compound.type.tsym == this.syms.nativeHeaderType.tsym)
/* 165 */           return true;
/*     */       }
/*     */     }
/* 168 */     if (paramBoolean)
/* 169 */       for (entry = paramClassSymbol.members_field.elems; entry != null; entry = entry.sibling) {
/* 170 */         if (entry.sym.kind == 2 && needsHeader((Symbol.ClassSymbol)entry.sym, true)) {
/* 171 */           return true;
/*     */         }
/*     */       }
/* 174 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public FileObject write(Symbol.ClassSymbol paramClassSymbol) throws IOException {
/* 183 */     String str = paramClassSymbol.flatName().toString();
/*     */
/* 185 */     FileObject fileObject = this.fileManager.getFileForOutput(StandardLocation.NATIVE_HEADER_OUTPUT, "", str
/* 186 */         .replaceAll("[.$]", "_") + ".h", null);
/* 187 */     Writer writer = fileObject.openWriter();
/*     */     try {
/* 189 */       write(writer, paramClassSymbol);
/* 190 */       if (this.verbose)
/* 191 */         this.log.printVerbose("wrote.file", new Object[] { fileObject });
/* 192 */       writer.close();
/* 193 */       writer = null;
/*     */     } finally {
/* 195 */       if (writer != null) {
/*     */
/* 197 */         writer.close();
/* 198 */         fileObject.delete();
/* 199 */         fileObject = null;
/*     */       }
/*     */     }
/* 202 */     return fileObject;
/*     */   }
/*     */
/*     */
/*     */   public void write(Writer paramWriter, Symbol.ClassSymbol paramClassSymbol) throws IOException {
/* 207 */     lazyInit();
/*     */     try {
/* 209 */       String str = this.mangler.mangle((CharSequence)paramClassSymbol.fullname, 1);
/* 210 */       println(paramWriter, fileTop());
/* 211 */       println(paramWriter, includes());
/* 212 */       println(paramWriter, guardBegin(str));
/* 213 */       println(paramWriter, cppGuardBegin());
/*     */
/* 215 */       writeStatics(paramWriter, paramClassSymbol);
/* 216 */       writeMethods(paramWriter, paramClassSymbol, str);
/*     */
/* 218 */       println(paramWriter, cppGuardEnd());
/* 219 */       println(paramWriter, guardEnd(str));
/* 220 */     } catch (SignatureException signatureException) {
/* 221 */       throw new IOException(signatureException);
/*     */     }
/*     */   }
/*     */
/*     */   protected void writeStatics(Writer paramWriter, Symbol.ClassSymbol paramClassSymbol) throws IOException {
/* 226 */     List<VariableElement> list = getAllFields((TypeElement)paramClassSymbol);
/*     */
/* 228 */     for (VariableElement variableElement : list) {
/* 229 */       if (!variableElement.getModifiers().contains(Modifier.STATIC))
/*     */         continue;
/* 231 */       String str = null;
/* 232 */       str = defineForStatic((TypeElement)paramClassSymbol, variableElement);
/* 233 */       if (str != null) {
/* 234 */         println(paramWriter, str);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   List<VariableElement> getAllFields(TypeElement paramTypeElement) {
/* 243 */     ArrayList<VariableElement> arrayList = new ArrayList();
/* 244 */     TypeElement typeElement = null;
/* 245 */     Stack<TypeElement> stack = new Stack();
/*     */
/* 247 */     typeElement = paramTypeElement;
/*     */     while (true) {
/* 249 */       stack.push(typeElement);
/* 250 */       TypeElement typeElement1 = (TypeElement)this.types.asElement(typeElement.getSuperclass());
/* 251 */       if (typeElement1 == null)
/*     */         break;
/* 253 */       typeElement = typeElement1;
/*     */     }
/*     */
/* 256 */     while (!stack.empty()) {
/* 257 */       typeElement = stack.pop();
/* 258 */       arrayList.addAll(ElementFilter.fieldsIn(typeElement.getEnclosedElements()));
/*     */     }
/*     */
/* 261 */     return arrayList;
/*     */   }
/*     */
/*     */   protected String defineForStatic(TypeElement paramTypeElement, VariableElement paramVariableElement) {
/* 265 */     Name name1 = paramTypeElement.getQualifiedName();
/* 266 */     Name name2 = paramVariableElement.getSimpleName();
/*     */
/* 268 */     String str1 = this.mangler.mangle(name1, 1);
/* 269 */     String str2 = this.mangler.mangle(name2, 2);
/*     */
/* 271 */     Assert.check(paramVariableElement.getModifiers().contains(Modifier.STATIC));
/*     */
/* 273 */     if (paramVariableElement.getModifiers().contains(Modifier.FINAL)) {
/* 274 */       Object object = null;
/*     */
/* 276 */       object = paramVariableElement.getConstantValue();
/*     */
/* 278 */       if (object != null) {
/* 279 */         String str = null;
/* 280 */         if (object instanceof Integer || object instanceof Byte || object instanceof Short) {
/*     */
/*     */
/*     */
/* 284 */           str = object.toString() + "L";
/* 285 */         } else if (object instanceof Boolean) {
/* 286 */           str = ((Boolean)object).booleanValue() ? "1L" : "0L";
/* 287 */         } else if (object instanceof Character) {
/* 288 */           Character character = (Character)object;
/* 289 */           str = String.valueOf(character.charValue() & Character.MAX_VALUE) + "L";
/* 290 */         } else if (object instanceof Long) {
/*     */
/* 292 */           if (this.isWindows)
/* 293 */           { str = object.toString() + "i64"; }
/*     */           else
/* 295 */           { str = object.toString() + "LL"; }
/* 296 */         } else if (object instanceof Float) {
/*     */
/* 298 */           float f = ((Float)object).floatValue();
/* 299 */           if (Float.isInfinite(f))
/* 300 */           { str = ((f < 0.0F) ? "-" : "") + "Inff"; }
/*     */           else
/* 302 */           { str = object.toString() + "f"; }
/* 303 */         } else if (object instanceof Double) {
/*     */
/* 305 */           double d = ((Double)object).doubleValue();
/* 306 */           if (Double.isInfinite(d)) {
/* 307 */             str = ((d < 0.0D) ? "-" : "") + "InfD";
/*     */           } else {
/* 309 */             str = object.toString();
/*     */           }
/*     */         }
/* 312 */         if (str != null) {
/* 313 */           StringBuilder stringBuilder = new StringBuilder("#undef ");
/* 314 */           stringBuilder.append(str1); stringBuilder.append("_"); stringBuilder.append(str2); stringBuilder.append(this.lineSep);
/* 315 */           stringBuilder.append("#define "); stringBuilder.append(str1); stringBuilder.append("_");
/* 316 */           stringBuilder.append(str2); stringBuilder.append(" "); stringBuilder.append(str);
/* 317 */           return stringBuilder.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/* 323 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */   protected void writeMethods(Writer paramWriter, Symbol.ClassSymbol paramClassSymbol, String paramString) throws IOException, TypeSignature.SignatureException {
/* 329 */     List<ExecutableElement> list = ElementFilter.methodsIn(paramClassSymbol.getEnclosedElements());
/* 330 */     for (ExecutableElement executableElement : list) {
/* 331 */       if (executableElement.getModifiers().contains(Modifier.NATIVE)) {
/* 332 */         TypeMirror typeMirror = this.types.erasure(executableElement.getReturnType());
/* 333 */         String str = signature(executableElement);
/* 334 */         TypeSignature typeSignature = new TypeSignature((Elements)this.elements);
/* 335 */         Name name = executableElement.getSimpleName();
/* 336 */         boolean bool = false;
/* 337 */         for (ExecutableElement executableElement1 : list) {
/* 338 */           if (executableElement1 != executableElement && name
/* 339 */             .equals(executableElement1.getSimpleName()) && executableElement1
/* 340 */             .getModifiers().contains(Modifier.NATIVE)) {
/* 341 */             bool = true;
/*     */           }
/*     */         }
/* 344 */         println(paramWriter, "/*");
/* 345 */         println(paramWriter, " * Class:     " + paramString);
/* 346 */         println(paramWriter, " * Method:    " + this.mangler
/* 347 */             .mangle(name, 2));
/* 348 */         println(paramWriter, " * Signature: " + typeSignature.getTypeSignature(str, typeMirror));
/* 349 */         println(paramWriter, " */");
/* 350 */         println(paramWriter, "JNIEXPORT " + jniType(typeMirror) + " JNICALL " + this.mangler
/*     */
/* 352 */             .mangleMethod(executableElement, (TypeElement)paramClassSymbol, bool ? 8 : 7));
/*     */
/*     */
/*     */
/* 356 */         print(paramWriter, "  (JNIEnv *, ");
/* 357 */         List<? extends VariableElement> list1 = executableElement.getParameters();
/* 358 */         ArrayList<TypeMirror> arrayList = new ArrayList();
/* 359 */         for (VariableElement variableElement : list1) {
/* 360 */           arrayList.add(this.types.erasure(variableElement.asType()));
/*     */         }
/* 362 */         if (executableElement.getModifiers().contains(Modifier.STATIC)) {
/* 363 */           print(paramWriter, "jclass");
/*     */         } else {
/* 365 */           print(paramWriter, "jobject");
/*     */         }
/* 367 */         for (TypeMirror typeMirror1 : arrayList) {
/* 368 */           print(paramWriter, ", ");
/* 369 */           print(paramWriter, jniType(typeMirror1));
/*     */         }
/* 371 */         println(paramWriter, ");" + this.lineSep);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   String signature(ExecutableElement paramExecutableElement) {
/* 379 */     StringBuilder stringBuilder = new StringBuilder("(");
/* 380 */     String str = "";
/* 381 */     for (VariableElement variableElement : paramExecutableElement.getParameters()) {
/* 382 */       stringBuilder.append(str);
/* 383 */       stringBuilder.append(this.types.erasure(variableElement.asType()).toString());
/* 384 */       str = ",";
/*     */     }
/* 386 */     stringBuilder.append(")");
/* 387 */     return stringBuilder.toString();
/*     */   }
/*     */   protected final String jniType(TypeMirror paramTypeMirror) {
/*     */     TypeMirror typeMirror;
/* 391 */     Symbol.ClassSymbol classSymbol1 = this.elements.getTypeElement("java.lang.Throwable");
/* 392 */     Symbol.ClassSymbol classSymbol2 = this.elements.getTypeElement("java.lang.Class");
/* 393 */     Symbol.ClassSymbol classSymbol3 = this.elements.getTypeElement("java.lang.String");
/* 394 */     Element element = this.types.asElement(paramTypeMirror);
/*     */
/*     */
/* 397 */     switch (paramTypeMirror.getKind()) {
/*     */       case ARRAY:
/* 399 */         typeMirror = ((ArrayType)paramTypeMirror).getComponentType();
/* 400 */         switch (typeMirror.getKind()) { case BOOLEAN:
/* 401 */             return "jbooleanArray";
/* 402 */           case BYTE: return "jbyteArray";
/* 403 */           case CHAR: return "jcharArray";
/* 404 */           case SHORT: return "jshortArray";
/* 405 */           case INT: return "jintArray";
/* 406 */           case LONG: return "jlongArray";
/* 407 */           case FLOAT: return "jfloatArray";
/* 408 */           case DOUBLE: return "jdoubleArray";
/*     */           case ARRAY: case DECLARED:
/* 410 */             return "jobjectArray"; }
/* 411 */          throw new Error(typeMirror.toString());
/*     */
/*     */
/*     */       case VOID:
/* 415 */         return "void";
/* 416 */       case BOOLEAN: return "jboolean";
/* 417 */       case BYTE: return "jbyte";
/* 418 */       case CHAR: return "jchar";
/* 419 */       case SHORT: return "jshort";
/* 420 */       case INT: return "jint";
/* 421 */       case LONG: return "jlong";
/* 422 */       case FLOAT: return "jfloat";
/* 423 */       case DOUBLE: return "jdouble";
/*     */
/*     */       case DECLARED:
/* 426 */         if (element.equals(classSymbol3))
/* 427 */           return "jstring";
/* 428 */         if (this.types.isAssignable(paramTypeMirror, classSymbol1.asType()))
/* 429 */           return "jthrowable";
/* 430 */         if (this.types.isAssignable(paramTypeMirror, classSymbol2.asType())) {
/* 431 */           return "jclass";
/*     */         }
/* 433 */         return "jobject";
/*     */     }
/*     */
/*     */
/* 437 */     Assert.check(false, "jni unknown type");
/* 438 */     return null;
/*     */   }
/*     */
/*     */   protected String fileTop() {
/* 442 */     return "/* DO NOT EDIT THIS FILE - it is machine generated */";
/*     */   }
/*     */
/*     */   protected String includes() {
/* 446 */     return "#include <jni.h>";
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected String cppGuardBegin() {
/* 453 */     return "#ifdef __cplusplus" + this.lineSep + "extern \"C\" {" + this.lineSep + "#endif";
/*     */   }
/*     */
/*     */
/*     */
/*     */   protected String cppGuardEnd() {
/* 459 */     return "#ifdef __cplusplus" + this.lineSep + "}" + this.lineSep + "#endif";
/*     */   }
/*     */
/*     */
/*     */
/*     */   protected String guardBegin(String paramString) {
/* 465 */     return "/* Header for class " + paramString + " */" + this.lineSep + this.lineSep + "#ifndef _Included_" + paramString + this.lineSep + "#define _Included_" + paramString;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected String guardEnd(String paramString) {
/* 472 */     return "#endif";
/*     */   }
/*     */
/*     */   protected void print(Writer paramWriter, String paramString) throws IOException {
/* 476 */     paramWriter.write(paramString);
/*     */   }
/*     */
/*     */   protected void println(Writer paramWriter, String paramString) throws IOException {
/* 480 */     paramWriter.write(paramString);
/* 481 */     paramWriter.write(this.lineSep);
/*     */   }
/*     */
/*     */   private static class Mangle
/*     */   {
/*     */     private Elements elems;
/*     */     private Types types;
/*     */
/*     */     public static class Type
/*     */     {
/*     */       public static final int CLASS = 1;
/*     */       public static final int FIELDSTUB = 2;
/*     */       public static final int FIELD = 3;
/*     */       public static final int JNI = 4;
/*     */       public static final int SIGNATURE = 5;
/*     */       public static final int METHOD_JDK_1 = 6;
/*     */       public static final int METHOD_JNI_SHORT = 7;
/*     */       public static final int METHOD_JNI_LONG = 8;
/*     */     }
/*     */
/*     */     Mangle(Elements param1Elements, Types param1Types) {
/* 502 */       this.elems = param1Elements;
/* 503 */       this.types = param1Types;
/*     */     }
/*     */
/*     */     public final String mangle(CharSequence param1CharSequence, int param1Int) {
/* 507 */       StringBuilder stringBuilder = new StringBuilder(100);
/* 508 */       int i = param1CharSequence.length();
/*     */
/* 510 */       for (byte b = 0; b < i; b++) {
/* 511 */         char c = param1CharSequence.charAt(b);
/* 512 */         if (isalnum(c)) {
/* 513 */           stringBuilder.append(c);
/* 514 */         } else if (c == '.' && param1Int == 1) {
/*     */
/* 516 */           stringBuilder.append('_');
/* 517 */         } else if (c == '$' && param1Int == 1) {
/*     */
/* 519 */           stringBuilder.append('_');
/* 520 */           stringBuilder.append('_');
/* 521 */         } else if (c == '_' && param1Int == 2) {
/* 522 */           stringBuilder.append('_');
/* 523 */         } else if (c == '_' && param1Int == 1) {
/* 524 */           stringBuilder.append('_');
/* 525 */         } else if (param1Int == 4) {
/* 526 */           String str = null;
/* 527 */           if (c == '_') {
/* 528 */             str = "_1";
/* 529 */           } else if (c == '.') {
/* 530 */             str = "_";
/* 531 */           } else if (c == ';') {
/* 532 */             str = "_2";
/* 533 */           } else if (c == '[') {
/* 534 */             str = "_3";
/* 535 */           }  if (str != null) {
/* 536 */             stringBuilder.append(str);
/*     */           } else {
/* 538 */             stringBuilder.append(mangleChar(c));
/*     */           }
/* 540 */         } else if (param1Int == 5) {
/* 541 */           if (isprint(c)) {
/* 542 */             stringBuilder.append(c);
/*     */           } else {
/* 544 */             stringBuilder.append(mangleChar(c));
/*     */           }
/*     */         } else {
/* 547 */           stringBuilder.append(mangleChar(c));
/*     */         }
/*     */       }
/*     */
/* 551 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */
/*     */     public String mangleMethod(ExecutableElement param1ExecutableElement, TypeElement param1TypeElement, int param1Int) throws TypeSignature.SignatureException {
/* 556 */       StringBuilder stringBuilder = new StringBuilder(100);
/* 557 */       stringBuilder.append("Java_");
/*     */
/* 559 */       if (param1Int == 6) {
/* 560 */         stringBuilder.append(mangle(param1TypeElement.getQualifiedName(), 1));
/* 561 */         stringBuilder.append('_');
/* 562 */         stringBuilder.append(mangle(param1ExecutableElement.getSimpleName(), 3));
/*     */
/* 564 */         stringBuilder.append("_stub");
/* 565 */         return stringBuilder.toString();
/*     */       }
/*     */
/*     */
/* 569 */       stringBuilder.append(mangle(getInnerQualifiedName(param1TypeElement), 4));
/* 570 */       stringBuilder.append('_');
/* 571 */       stringBuilder.append(mangle(param1ExecutableElement.getSimpleName(), 4));
/*     */
/* 573 */       if (param1Int == 8) {
/* 574 */         stringBuilder.append("__");
/* 575 */         String str1 = signature(param1ExecutableElement);
/* 576 */         TypeSignature typeSignature = new TypeSignature(this.elems);
/* 577 */         String str2 = typeSignature.getTypeSignature(str1, param1ExecutableElement.getReturnType());
/* 578 */         str2 = str2.substring(1);
/* 579 */         str2 = str2.substring(0, str2.lastIndexOf(')'));
/* 580 */         str2 = str2.replace('/', '.');
/* 581 */         stringBuilder.append(mangle(str2, 4));
/*     */       }
/*     */
/* 584 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */     private String getInnerQualifiedName(TypeElement param1TypeElement) {
/* 588 */       return this.elems.getBinaryName(param1TypeElement).toString();
/*     */     }
/*     */
/*     */     public final String mangleChar(char param1Char) {
/* 592 */       String str = Integer.toHexString(param1Char);
/* 593 */       int i = 5 - str.length();
/* 594 */       char[] arrayOfChar = new char[6];
/* 595 */       arrayOfChar[0] = '_'; int j;
/* 596 */       for (j = 1; j <= i; j++)
/* 597 */         arrayOfChar[j] = '0';  byte b;
/* 598 */       for (j = i + 1, b = 0; j < 6; j++, b++)
/* 599 */         arrayOfChar[j] = str.charAt(b);
/* 600 */       return new String(arrayOfChar);
/*     */     }
/*     */
/*     */
/*     */     private String signature(ExecutableElement param1ExecutableElement) {
/* 605 */       StringBuilder stringBuilder = new StringBuilder();
/* 606 */       String str = "(";
/* 607 */       for (VariableElement variableElement : param1ExecutableElement.getParameters()) {
/* 608 */         stringBuilder.append(str);
/* 609 */         stringBuilder.append(this.types.erasure(variableElement.asType()).toString());
/* 610 */         str = ",";
/*     */       }
/* 612 */       stringBuilder.append(")");
/* 613 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */
/*     */     private static boolean isalnum(char param1Char) {
/* 618 */       return (param1Char <= '' && ((param1Char >= 'A' && param1Char <= 'Z') || (param1Char >= 'a' && param1Char <= 'z') || (param1Char >= '0' && param1Char <= '9')));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private static boolean isprint(char param1Char) {
/* 626 */       return (param1Char >= ' ' && param1Char <= '~');
/*     */     }
/*     */   }
/*     */   public static class Type { public static final int CLASS = 1; public static final int FIELDSTUB = 2; public static final int FIELD = 3; public static final int JNI = 4; public static final int SIGNATURE = 5; public static final int METHOD_JDK_1 = 6; public static final int METHOD_JNI_SHORT = 7;
/*     */     public static final int METHOD_JNI_LONG = 8; }
/*     */
/*     */   static class SignatureException extends Exception { private static final long serialVersionUID = 1L;
/*     */
/* 634 */     SignatureException(String param1String) { super(param1String); } } private static class TypeSignature { Elements elems; private static final String SIG_VOID = "V"; private static final String SIG_BOOLEAN = "Z"; private static final String SIG_BYTE = "B"; private static final String SIG_CHAR = "C"; private static final String SIG_SHORT = "S"; private static final String SIG_INT = "I"; private static final String SIG_LONG = "J"; private static final String SIG_FLOAT = "F"; private static final String SIG_DOUBLE = "D"; private static final String SIG_ARRAY = "["; private static final String SIG_CLASS = "L"; static class SignatureException extends Exception { SignatureException(String param2String) { super(param2String); }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */       private static final long serialVersionUID = 1L; }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public TypeSignature(Elements param1Elements) {
/* 657 */       this.elems = param1Elements;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public String getTypeSignature(String param1String) throws SignatureException {
/* 664 */       return getParamJVMSignature(param1String);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String getTypeSignature(String param1String, TypeMirror param1TypeMirror) throws SignatureException {
/* 672 */       String str1 = null;
/* 673 */       String str2 = null;
/* 674 */       ArrayList<String> arrayList = new ArrayList();
/* 675 */       String str3 = null;
/* 676 */       String str4 = null;
/* 677 */       String str5 = null;
/* 678 */       String str6 = null;
/* 679 */       int i = 0;
/*     */
/* 681 */       int j = -1;
/* 682 */       int k = -1;
/* 683 */       StringTokenizer stringTokenizer = null;
/* 684 */       boolean bool = false;
/*     */
/*     */
/* 687 */       if (param1String != null) {
/* 688 */         j = param1String.indexOf("(");
/* 689 */         k = param1String.indexOf(")");
/*     */       }
/*     */
/* 692 */       if (j != -1 && k != -1 && j + 1 < param1String
/* 693 */         .length() && k < param1String
/* 694 */         .length()) {
/* 695 */         str1 = param1String.substring(j + 1, k);
/*     */       }
/*     */
/*     */
/* 699 */       if (str1 != null) {
/* 700 */         if (str1.indexOf(",") != -1) {
/* 701 */           stringTokenizer = new StringTokenizer(str1, ",");
/* 702 */           if (stringTokenizer != null) {
/* 703 */             while (stringTokenizer.hasMoreTokens()) {
/* 704 */               arrayList.add(stringTokenizer.nextToken());
/*     */             }
/*     */           }
/*     */         } else {
/* 708 */           arrayList.add(str1);
/*     */         }
/*     */       }
/*     */
/*     */
/* 713 */       str2 = "(";
/*     */
/*     */
/* 716 */       while (arrayList.isEmpty() != true) {
/* 717 */         str3 = ((String)arrayList.remove(bool)).trim();
/* 718 */         str4 = getParamJVMSignature(str3);
/* 719 */         if (str4 != null) {
/* 720 */           str2 = str2 + str4;
/*     */         }
/*     */       }
/*     */
/* 724 */       str2 = str2 + ")";
/*     */
/*     */
/*     */
/* 728 */       str6 = "";
/* 729 */       if (param1TypeMirror != null) {
/* 730 */         i = dimensions(param1TypeMirror);
/*     */       }
/*     */
/*     */
/* 734 */       while (i-- > 0) {
/* 735 */         str6 = str6 + "[";
/*     */       }
/* 737 */       if (param1TypeMirror != null) {
/* 738 */         str5 = qualifiedTypeName(param1TypeMirror);
/* 739 */         str6 = str6 + getComponentType(str5);
/*     */       } else {
/* 741 */         System.out.println("Invalid return type.");
/*     */       }
/*     */
/* 744 */       str2 = str2 + str6;
/*     */
/* 746 */       return str2;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     private String getParamJVMSignature(String param1String) throws SignatureException {
/* 753 */       String str1 = "";
/* 754 */       String str2 = "";
/*     */
/* 756 */       if (param1String != null) {
/*     */
/* 758 */         if (param1String.indexOf("[]") != -1) {
/*     */
/* 760 */           int i = param1String.indexOf("[]");
/* 761 */           str2 = param1String.substring(0, i);
/* 762 */           String str = param1String.substring(i);
/* 763 */           if (str != null)
/* 764 */             while (str.indexOf("[]") != -1) {
/* 765 */               str1 = str1 + "[";
/* 766 */               int j = str.indexOf("]") + 1;
/* 767 */               if (j < str.length()) {
/* 768 */                 str = str.substring(j); continue;
/*     */               }
/* 770 */               str = "";
/*     */             }
/*     */         } else {
/* 773 */           str2 = param1String;
/*     */         }
/* 775 */         str1 = str1 + getComponentType(str2);
/*     */       }
/* 777 */       return str1;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private String getComponentType(String param1String) throws SignatureException {
/* 785 */       String str = "";
/*     */
/* 787 */       if (param1String != null) {
/* 788 */         if (param1String.equals("void")) { str = str + "V"; }
/* 789 */         else if (param1String.equals("boolean")) { str = str + "Z"; }
/* 790 */         else if (param1String.equals("byte")) { str = str + "B"; }
/* 791 */         else if (param1String.equals("char")) { str = str + "C"; }
/* 792 */         else if (param1String.equals("short")) { str = str + "S"; }
/* 793 */         else if (param1String.equals("int")) { str = str + "I"; }
/* 794 */         else if (param1String.equals("long")) { str = str + "J"; }
/* 795 */         else if (param1String.equals("float")) { str = str + "F"; }
/* 796 */         else if (param1String.equals("double")) { str = str + "D"; }
/*     */
/* 798 */         else if (!param1String.equals(""))
/* 799 */         { TypeElement typeElement = this.elems.getTypeElement(param1String);
/*     */
/* 801 */           if (typeElement == null) {
/* 802 */             throw new SignatureException(param1String);
/*     */           }
/* 804 */           String str1 = typeElement.getQualifiedName().toString();
/* 805 */           String str2 = str1.replace('.', '/');
/* 806 */           str = str + "L";
/* 807 */           str = str + str2;
/* 808 */           str = str + ";"; }
/*     */
/*     */       }
/*     */
/*     */
/* 813 */       return str;
/*     */     }
/*     */
/*     */     int dimensions(TypeMirror param1TypeMirror) {
/* 817 */       if (param1TypeMirror.getKind() != TypeKind.ARRAY)
/* 818 */         return 0;
/* 819 */       return 1 + dimensions(((ArrayType)param1TypeMirror).getComponentType());
/*     */     }
/*     */
/*     */
/*     */     String qualifiedTypeName(TypeMirror param1TypeMirror) {
/* 824 */       SimpleTypeVisitor8<Name, Void> simpleTypeVisitor8 = new SimpleTypeVisitor8<Name, Void>()
/*     */         {
/*     */           public Name visitArray(ArrayType param2ArrayType, Void param2Void) {
/* 827 */             return param2ArrayType.getComponentType().<Name, Void>accept(this, param2Void);
/*     */           }
/*     */
/*     */
/*     */           public Name visitDeclared(DeclaredType param2DeclaredType, Void param2Void) {
/* 832 */             return ((TypeElement)param2DeclaredType.asElement()).getQualifiedName();
/*     */           }
/*     */
/*     */
/*     */           public Name visitPrimitive(PrimitiveType param2PrimitiveType, Void param2Void) {
/* 837 */             return TypeSignature.this.elems.getName(param2PrimitiveType.toString());
/*     */           }
/*     */
/*     */
/*     */           public Name visitNoType(NoType param2NoType, Void param2Void) {
/* 842 */             if (param2NoType.getKind() == TypeKind.VOID)
/* 843 */               return TypeSignature.this.elems.getName("void");
/* 844 */             return defaultAction(param2NoType, param2Void);
/*     */           }
/*     */
/*     */
/*     */           public Name visitTypeVariable(TypeVariable param2TypeVariable, Void param2Void) {
/* 849 */             return param2TypeVariable.getUpperBound().<Name, Void>accept(this, param2Void);
/*     */           }
/*     */         };
/* 852 */       return ((Name)simpleTypeVisitor8.visit(param1TypeMirror)).toString();
/*     */     } }
/*     */
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\jvm\JNIWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
