/*     */ package com.sun.tools.javap;
/*     */
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ConstantWriter
/*     */   extends BasicWriter
/*     */ {
/*     */   StringValueVisitor stringValueVisitor;
/*     */   private ClassWriter classWriter;
/*     */   private Options options;
/*     */
/*     */   public static ConstantWriter instance(Context paramContext) {
/*  44 */     ConstantWriter constantWriter = paramContext.<ConstantWriter>get(ConstantWriter.class);
/*  45 */     if (constantWriter == null)
/*  46 */       constantWriter = new ConstantWriter(paramContext);
/*  47 */     return constantWriter;
/*     */   }
/*     */
/*     */   protected ConstantWriter(Context paramContext) {
/*  51 */     super(paramContext);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 255 */     this.stringValueVisitor = new StringValueVisitor(); paramContext.put(ConstantWriter.class, this); this.classWriter = ClassWriter.instance(paramContext); this.options = Options.instance(paramContext);
/*     */   } protected void writeConstantPool() { ConstantPool constantPool = (this.classWriter.getClassFile()).constant_pool; writeConstantPool(constantPool); } protected void writeConstantPool(ConstantPool paramConstantPool) { ConstantPool.Visitor<Integer, Void> visitor = new ConstantPool.Visitor<Integer, Void>() {
/*     */         public Integer visitClass(ConstantPool.CONSTANT_Class_info param1CONSTANT_Class_info, Void param1Void) { ConstantWriter.this.print("#" + param1CONSTANT_Class_info.name_index); ConstantWriter.this.tab(); ConstantWriter.this.println("// " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_Class_info)); return Integer.valueOf(1); } public Integer visitDouble(ConstantPool.CONSTANT_Double_info param1CONSTANT_Double_info, Void param1Void) { ConstantWriter.this.println(ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_Double_info)); return Integer.valueOf(2); } public Integer visitFieldref(ConstantPool.CONSTANT_Fieldref_info param1CONSTANT_Fieldref_info, Void param1Void) { ConstantWriter.this.print("#" + param1CONSTANT_Fieldref_info.class_index + ".#" + param1CONSTANT_Fieldref_info.name_and_type_index); ConstantWriter.this.tab(); ConstantWriter.this.println("// " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_Fieldref_info)); return Integer.valueOf(1); } public Integer visitFloat(ConstantPool.CONSTANT_Float_info param1CONSTANT_Float_info, Void param1Void) { ConstantWriter.this.println(ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_Float_info)); return Integer.valueOf(1); } public Integer visitInteger(ConstantPool.CONSTANT_Integer_info param1CONSTANT_Integer_info, Void param1Void) { ConstantWriter.this.println(ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_Integer_info)); return Integer.valueOf(1); } public Integer visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info param1CONSTANT_InterfaceMethodref_info, Void param1Void) { ConstantWriter.this.print("#" + param1CONSTANT_InterfaceMethodref_info.class_index + ".#" + param1CONSTANT_InterfaceMethodref_info.name_and_type_index); ConstantWriter.this.tab(); ConstantWriter.this.println("// " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_InterfaceMethodref_info)); return Integer.valueOf(1); } public Integer visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info param1CONSTANT_InvokeDynamic_info, Void param1Void) { ConstantWriter.this.print("#" + param1CONSTANT_InvokeDynamic_info.bootstrap_method_attr_index + ":#" + param1CONSTANT_InvokeDynamic_info.name_and_type_index); ConstantWriter.this.tab(); ConstantWriter.this.println("// " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_InvokeDynamic_info)); return Integer.valueOf(1); } public Integer visitLong(ConstantPool.CONSTANT_Long_info param1CONSTANT_Long_info, Void param1Void) { ConstantWriter.this.println(ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_Long_info)); return Integer.valueOf(2); } public Integer visitNameAndType(ConstantPool.CONSTANT_NameAndType_info param1CONSTANT_NameAndType_info, Void param1Void) { ConstantWriter.this.print("#" + param1CONSTANT_NameAndType_info.name_index + ":#" + param1CONSTANT_NameAndType_info.type_index); ConstantWriter.this.tab(); ConstantWriter.this.println("// " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_NameAndType_info)); return Integer.valueOf(1); } public Integer visitMethodref(ConstantPool.CONSTANT_Methodref_info param1CONSTANT_Methodref_info, Void param1Void) { ConstantWriter.this.print("#" + param1CONSTANT_Methodref_info.class_index + ".#" + param1CONSTANT_Methodref_info.name_and_type_index); ConstantWriter.this.tab(); ConstantWriter.this.println("// " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_Methodref_info)); return Integer.valueOf(1); } public Integer visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info param1CONSTANT_MethodHandle_info, Void param1Void) { ConstantWriter.this.print("#" + param1CONSTANT_MethodHandle_info.reference_kind.tag + ":#" + param1CONSTANT_MethodHandle_info.reference_index); ConstantWriter.this.tab(); ConstantWriter.this.println("// " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_MethodHandle_info)); return Integer.valueOf(1); } public Integer visitMethodType(ConstantPool.CONSTANT_MethodType_info param1CONSTANT_MethodType_info, Void param1Void) { ConstantWriter.this.print("#" + param1CONSTANT_MethodType_info.descriptor_index); ConstantWriter.this.tab(); ConstantWriter.this.println("//  " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_MethodType_info)); return Integer.valueOf(1); } public Integer visitString(ConstantPool.CONSTANT_String_info param1CONSTANT_String_info, Void param1Void) { ConstantWriter.this.print("#" + param1CONSTANT_String_info.string_index); ConstantWriter.this.tab(); ConstantWriter.this.println("// " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_String_info)); return Integer.valueOf(1); } public Integer visitUtf8(ConstantPool.CONSTANT_Utf8_info param1CONSTANT_Utf8_info, Void param1Void) { ConstantWriter.this.println(ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_Utf8_info)); return Integer.valueOf(1); }
/*     */       }; println("Constant pool:"); indent(1); int i = String.valueOf(paramConstantPool.size()).length() + 1; int j = 1; while (j < paramConstantPool.size()) { print(String.format("%" + i + "s", new Object[] { "#" + j })); try { ConstantPool.CPInfo cPInfo = paramConstantPool.get(j); print(String.format(" = %-18s ", new Object[] { cpTagName(cPInfo) })); j += ((Integer)cPInfo.accept(visitor, null)).intValue(); } catch (ConstantPool.InvalidIndex invalidIndex) {} }  indent(-1); } protected void write(int paramInt) { ConstantPool.CPInfo cPInfo; ConstantPool.CPRefInfo cPRefInfo; ClassFile classFile = this.classWriter.getClassFile(); if (paramInt == 0) { print("#0"); return; }  try { cPInfo = classFile.constant_pool.get(paramInt); } catch (ConstantPoolException constantPoolException) { print("#" + paramInt); return; }  int i = cPInfo.getTag(); switch (i) { case 9: case 10: case 11: cPRefInfo = (ConstantPool.CPRefInfo)cPInfo; try { if (cPRefInfo.class_index == classFile.this_class) cPInfo = classFile.constant_pool.get(cPRefInfo.name_and_type_index);  } catch (ConstantPool.InvalidIndex invalidIndex) {} break; }  print(tagName(i) + " " + stringValue(cPInfo)); } String cpTagName(ConstantPool.CPInfo paramCPInfo) { String str = paramCPInfo.getClass().getSimpleName(); return str.replace("CONSTANT_", "").replace("_info", ""); } String tagName(int paramInt) { switch (paramInt) { case 1: return "Utf8";case 3: return "int";case 4: return "float";case 5: return "long";case 6: return "double";case 7: return "class";case 8: return "String";case 9: return "Field";case 15: return "MethodHandle";case 16: return "MethodType";case 10: return "Method";case 11: return "InterfaceMethod";case 18: return "InvokeDynamic";case 12: return "NameAndType"; }  return "(unknown tag " + paramInt + ")"; } String stringValue(int paramInt) { ClassFile classFile = this.classWriter.getClassFile(); try { return stringValue(classFile.constant_pool.get(paramInt)); } catch (ConstantPool.InvalidIndex invalidIndex) { return report((ConstantPoolException)invalidIndex); }  } String stringValue(ConstantPool.CPInfo paramCPInfo) { return this.stringValueVisitor.visit(paramCPInfo); } private class StringValueVisitor implements ConstantPool.Visitor<String, Void> {
/* 259 */     public String visit(ConstantPool.CPInfo param1CPInfo) { return (String)param1CPInfo.accept(this, null); }
/*     */
/*     */     private StringValueVisitor() {}
/*     */     public String visitClass(ConstantPool.CONSTANT_Class_info param1CONSTANT_Class_info, Void param1Void) {
/* 263 */       return getCheckedName(param1CONSTANT_Class_info);
/*     */     }
/*     */
/*     */     String getCheckedName(ConstantPool.CONSTANT_Class_info param1CONSTANT_Class_info) {
/*     */       try {
/* 268 */         return ConstantWriter.checkName(param1CONSTANT_Class_info.getName());
/* 269 */       } catch (ConstantPoolException constantPoolException) {
/* 270 */         return ConstantWriter.this.report(constantPoolException);
/*     */       }
/*     */     }
/*     */
/*     */     public String visitDouble(ConstantPool.CONSTANT_Double_info param1CONSTANT_Double_info, Void param1Void) {
/* 275 */       return param1CONSTANT_Double_info.value + "d";
/*     */     }
/*     */
/*     */     public String visitFieldref(ConstantPool.CONSTANT_Fieldref_info param1CONSTANT_Fieldref_info, Void param1Void) {
/* 279 */       return visitRef((ConstantPool.CPRefInfo)param1CONSTANT_Fieldref_info, param1Void);
/*     */     }
/*     */
/*     */     public String visitFloat(ConstantPool.CONSTANT_Float_info param1CONSTANT_Float_info, Void param1Void) {
/* 283 */       return param1CONSTANT_Float_info.value + "f";
/*     */     }
/*     */
/*     */     public String visitInteger(ConstantPool.CONSTANT_Integer_info param1CONSTANT_Integer_info, Void param1Void) {
/* 287 */       return String.valueOf(param1CONSTANT_Integer_info.value);
/*     */     }
/*     */
/*     */     public String visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info param1CONSTANT_InterfaceMethodref_info, Void param1Void) {
/* 291 */       return visitRef((ConstantPool.CPRefInfo)param1CONSTANT_InterfaceMethodref_info, param1Void);
/*     */     }
/*     */
/*     */     public String visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info param1CONSTANT_InvokeDynamic_info, Void param1Void) {
/*     */       try {
/* 296 */         String str = ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_InvokeDynamic_info.getNameAndTypeInfo());
/* 297 */         return "#" + param1CONSTANT_InvokeDynamic_info.bootstrap_method_attr_index + ":" + str;
/* 298 */       } catch (ConstantPoolException constantPoolException) {
/* 299 */         return ConstantWriter.this.report(constantPoolException);
/*     */       }
/*     */     }
/*     */
/*     */     public String visitLong(ConstantPool.CONSTANT_Long_info param1CONSTANT_Long_info, Void param1Void) {
/* 304 */       return param1CONSTANT_Long_info.value + "l";
/*     */     }
/*     */
/*     */     public String visitNameAndType(ConstantPool.CONSTANT_NameAndType_info param1CONSTANT_NameAndType_info, Void param1Void) {
/* 308 */       return getCheckedName(param1CONSTANT_NameAndType_info) + ":" + getType(param1CONSTANT_NameAndType_info);
/*     */     }
/*     */
/*     */     String getCheckedName(ConstantPool.CONSTANT_NameAndType_info param1CONSTANT_NameAndType_info) {
/*     */       try {
/* 313 */         return ConstantWriter.checkName(param1CONSTANT_NameAndType_info.getName());
/* 314 */       } catch (ConstantPoolException constantPoolException) {
/* 315 */         return ConstantWriter.this.report(constantPoolException);
/*     */       }
/*     */     }
/*     */
/*     */     String getType(ConstantPool.CONSTANT_NameAndType_info param1CONSTANT_NameAndType_info) {
/*     */       try {
/* 321 */         return param1CONSTANT_NameAndType_info.getType();
/* 322 */       } catch (ConstantPoolException constantPoolException) {
/* 323 */         return ConstantWriter.this.report(constantPoolException);
/*     */       }
/*     */     }
/*     */
/*     */     public String visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info param1CONSTANT_MethodHandle_info, Void param1Void) {
/*     */       try {
/* 329 */         return param1CONSTANT_MethodHandle_info.reference_kind.name + " " + ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CONSTANT_MethodHandle_info.getCPRefInfo());
/* 330 */       } catch (ConstantPoolException constantPoolException) {
/* 331 */         return ConstantWriter.this.report(constantPoolException);
/*     */       }
/*     */     }
/*     */
/*     */     public String visitMethodType(ConstantPool.CONSTANT_MethodType_info param1CONSTANT_MethodType_info, Void param1Void) {
/*     */       try {
/* 337 */         return param1CONSTANT_MethodType_info.getType();
/* 338 */       } catch (ConstantPoolException constantPoolException) {
/* 339 */         return ConstantWriter.this.report(constantPoolException);
/*     */       }
/*     */     }
/*     */
/*     */     public String visitMethodref(ConstantPool.CONSTANT_Methodref_info param1CONSTANT_Methodref_info, Void param1Void) {
/* 344 */       return visitRef((ConstantPool.CPRefInfo)param1CONSTANT_Methodref_info, param1Void);
/*     */     }
/*     */
/*     */     public String visitString(ConstantPool.CONSTANT_String_info param1CONSTANT_String_info, Void param1Void) {
/*     */       try {
/* 349 */         ClassFile classFile = ConstantWriter.this.classWriter.getClassFile();
/* 350 */         int i = param1CONSTANT_String_info.string_index;
/* 351 */         return ConstantWriter.this.stringValue((ConstantPool.CPInfo)classFile.constant_pool.getUTF8Info(i));
/* 352 */       } catch (ConstantPoolException constantPoolException) {
/* 353 */         return ConstantWriter.this.report(constantPoolException);
/*     */       }
/*     */     }
/*     */
/*     */     public String visitUtf8(ConstantPool.CONSTANT_Utf8_info param1CONSTANT_Utf8_info, Void param1Void) {
/* 358 */       String str = param1CONSTANT_Utf8_info.value;
/* 359 */       StringBuilder stringBuilder = new StringBuilder();
/* 360 */       for (byte b = 0; b < str.length(); b++) {
/* 361 */         char c = str.charAt(b);
/* 362 */         switch (c) {
/*     */           case '\t':
/* 364 */             stringBuilder.append('\\').append('t');
/*     */             break;
/*     */           case '\n':
/* 367 */             stringBuilder.append('\\').append('n');
/*     */             break;
/*     */           case '\r':
/* 370 */             stringBuilder.append('\\').append('r');
/*     */             break;
/*     */           case '"':
/* 373 */             stringBuilder.append('\\').append('"');
/*     */             break;
/*     */           default:
/* 376 */             stringBuilder.append(c); break;
/*     */         }
/*     */       }
/* 379 */       return stringBuilder.toString();
/*     */     }
/*     */
/*     */     String visitRef(ConstantPool.CPRefInfo param1CPRefInfo, Void param1Void) {
/* 383 */       String str2, str1 = getCheckedClassName(param1CPRefInfo);
/*     */
/*     */       try {
/* 386 */         str2 = ConstantWriter.this.stringValue((ConstantPool.CPInfo)param1CPRefInfo.getNameAndTypeInfo());
/* 387 */       } catch (ConstantPoolException constantPoolException) {
/* 388 */         str2 = ConstantWriter.this.report(constantPoolException);
/*     */       }
/* 390 */       return str1 + "." + str2;
/*     */     }
/*     */
/*     */     String getCheckedClassName(ConstantPool.CPRefInfo param1CPRefInfo) {
/*     */       try {
/* 395 */         return ConstantWriter.checkName(param1CPRefInfo.getClassName());
/* 396 */       } catch (ConstantPoolException constantPoolException) {
/* 397 */         return ConstantWriter.this.report(constantPoolException);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   private static String checkName(String paramString) {
/* 404 */     if (paramString == null) {
/* 405 */       return "null";
/*     */     }
/* 407 */     int i = paramString.length();
/* 408 */     if (i == 0) {
/* 409 */       return "\"\"";
/*     */     }
/* 411 */     int j = 47;
/*     */     int k;
/* 413 */     for (k = 0; k < i; k += Character.charCount(m)) {
/* 414 */       int m = paramString.codePointAt(k);
/* 415 */       if ((j == 47 && !Character.isJavaIdentifierStart(m)) || (m != 47 &&
/* 416 */         !Character.isJavaIdentifierPart(m))) {
/* 417 */         return "\"" + addEscapes(paramString) + "\"";
/*     */       }
/* 419 */       j = m;
/*     */     }
/*     */
/* 422 */     return paramString;
/*     */   }
/*     */
/*     */
/*     */   private static String addEscapes(String paramString) {
/* 427 */     String str1 = "\\\"\n\t";
/* 428 */     String str2 = "\\\"nt";
/* 429 */     StringBuilder stringBuilder = null;
/* 430 */     int i = 0;
/* 431 */     int j = paramString.length();
/* 432 */     for (byte b = 0; b < j; b++) {
/* 433 */       char c = paramString.charAt(b);
/* 434 */       int k = str1.indexOf(c);
/* 435 */       if (k >= 0) {
/* 436 */         if (stringBuilder == null)
/* 437 */           stringBuilder = new StringBuilder(j * 2);
/* 438 */         if (i < b)
/* 439 */           stringBuilder.append(paramString, i, b);
/* 440 */         stringBuilder.append('\\');
/* 441 */         stringBuilder.append(str2.charAt(k));
/* 442 */         i = b + 1;
/*     */       }
/*     */     }
/* 445 */     if (stringBuilder == null)
/* 446 */       return paramString;
/* 447 */     if (i < j)
/* 448 */       stringBuilder.append(paramString, i, j);
/* 449 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\ConstantWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
