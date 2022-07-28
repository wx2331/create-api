/*      */ package sun.rmi.rmic.iiop;
/*      */
/*      */ import java.io.IOException;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import sun.rmi.rmic.IndentingWriter;
/*      */ import sun.rmi.rmic.Main;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Identifier;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class IDLGenerator
/*      */   extends Generator
/*      */ {
/*      */   private boolean valueMethods = true;
/*      */   private boolean factory = true;
/*   59 */   private Hashtable ifHash = new Hashtable<>();
/*   60 */   private Hashtable imHash = new Hashtable<>();
/*      */
/*      */
/*      */
/*      */   private boolean isThrown = true;
/*      */
/*      */
/*      */
/*      */   private boolean isException = true;
/*      */
/*      */
/*      */
/*      */   private boolean isForward = true;
/*      */
/*      */
/*      */
/*      */   private boolean forValuetype = true;
/*      */
/*      */
/*      */
/*      */   protected boolean requireNewInstance() {
/*   81 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected boolean parseNonConforming(ContextStack paramContextStack) {
/*   89 */     return this.valueMethods;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected CompoundType getTopType(ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/*  100 */     return CompoundType.forCompound(paramClassDefinition, paramContextStack);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected Identifier getOutputId(OutputType paramOutputType) {
/*  113 */     Identifier identifier = super.getOutputId(paramOutputType);
/*      */
/*  115 */     Type type = paramOutputType.getType();
/*  116 */     String str = paramOutputType.getName();
/*      */
/*  118 */     if (identifier == idJavaLangClass) {
/*  119 */       if (type.isArray()) {
/*  120 */         return Identifier.lookup("org.omg.boxedRMI.javax.rmi.CORBA." + str);
/*      */       }
/*  122 */       return idClassDesc;
/*      */     }
/*  124 */     if (identifier == idJavaLangString && type
/*  125 */       .isArray()) {
/*  126 */       return Identifier.lookup("org.omg.boxedRMI.CORBA." + str);
/*      */     }
/*  128 */     if ("org.omg.CORBA.Object".equals(type.getQualifiedName()) && type
/*  129 */       .isArray()) {
/*  130 */       return Identifier.lookup("org.omg.boxedRMI." + str);
/*      */     }
/*  132 */     if (type.isArray()) {
/*  133 */       ArrayType arrayType = (ArrayType)type;
/*  134 */       Type type1 = arrayType.getElementType();
/*  135 */       if (type1.isCompound()) {
/*  136 */         CompoundType compoundType = (CompoundType)type1;
/*  137 */         String str1 = compoundType.getQualifiedName();
/*  138 */         if (compoundType.isIDLEntity())
/*  139 */           return Identifier.lookup(getQualifiedName(arrayType));
/*      */       }
/*  141 */       return Identifier.lookup(idBoxedRMI, identifier);
/*      */     }
/*      */
/*  144 */     if (type.isCompound()) {
/*  145 */       CompoundType compoundType = (CompoundType)type;
/*  146 */       String str1 = compoundType.getQualifiedName();
/*  147 */       if (compoundType.isBoxed()) {
/*  148 */         return Identifier.lookup(getQualifiedName(compoundType));
/*      */       }
/*      */     }
/*  151 */     return identifier;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected String getFileNameExtensionFor(OutputType paramOutputType) {
/*  164 */     return ".idl";
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain) {
/*  176 */     boolean bool = super.parseArgs(paramArrayOfString, paramMain);
/*      */
/*      */
/*  179 */     if (bool)
/*      */     {
/*  181 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  182 */         if (paramArrayOfString[b] != null) {
/*  183 */           if (paramArrayOfString[b].equalsIgnoreCase("-idl")) {
/*  184 */             this.idl = true;
/*  185 */             paramArrayOfString[b] = null; continue;
/*      */           }
/*  187 */           if (paramArrayOfString[b].equalsIgnoreCase("-valueMethods")) {
/*  188 */             this.valueMethods = true;
/*  189 */             paramArrayOfString[b] = null; continue;
/*      */           }
/*  191 */           if (paramArrayOfString[b].equalsIgnoreCase("-noValueMethods")) {
/*  192 */             this.valueMethods = false;
/*  193 */             paramArrayOfString[b] = null; continue;
/*      */           }
/*  195 */           if (paramArrayOfString[b].equalsIgnoreCase("-init")) {
/*  196 */             this.factory = false;
/*  197 */             paramArrayOfString[b] = null; continue;
/*      */           }
/*  199 */           if (paramArrayOfString[b].equalsIgnoreCase("-factory")) {
/*  200 */             this.factory = true;
/*  201 */             paramArrayOfString[b] = null; continue;
/*      */           }
/*  203 */           if (paramArrayOfString[b].equalsIgnoreCase("-idlfile")) {
/*  204 */             paramArrayOfString[b] = null;
/*  205 */             if (++b < paramArrayOfString.length && paramArrayOfString[b] != null && !paramArrayOfString[b].startsWith("-")) {
/*  206 */               String str = paramArrayOfString[b];
/*  207 */               paramArrayOfString[b] = null;
/*  208 */               if (++b < paramArrayOfString.length && paramArrayOfString[b] != null && !paramArrayOfString[b].startsWith("-")) {
/*  209 */                 String str1 = paramArrayOfString[b];
/*  210 */                 paramArrayOfString[b] = null;
/*  211 */                 this.ifHash.put(str, str1);
/*      */                 continue;
/*      */               }
/*      */             }
/*  215 */             paramMain.error("rmic.option.requires.argument", "-idlfile");
/*  216 */             bool = false; continue;
/*      */           }
/*  218 */           if (paramArrayOfString[b].equalsIgnoreCase("-idlmodule")) {
/*  219 */             paramArrayOfString[b] = null;
/*  220 */             if (++b < paramArrayOfString.length && paramArrayOfString[b] != null && !paramArrayOfString[b].startsWith("-")) {
/*  221 */               String str = paramArrayOfString[b];
/*  222 */               paramArrayOfString[b] = null;
/*  223 */               if (++b < paramArrayOfString.length && paramArrayOfString[b] != null && !paramArrayOfString[b].startsWith("-")) {
/*  224 */                 String str1 = paramArrayOfString[b];
/*  225 */                 paramArrayOfString[b] = null;
/*  226 */                 this.imHash.put(str, str1);
/*      */                 continue;
/*      */               }
/*      */             }
/*  230 */             paramMain.error("rmic.option.requires.argument", "-idlmodule");
/*  231 */             bool = false;
/*      */           }
/*      */         }
/*      */
/*      */         continue;
/*      */       }
/*      */     }
/*  238 */     return bool;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected OutputType[] getOutputTypesFor(CompoundType paramCompoundType, HashSet paramHashSet) {
/*  254 */     Vector<Type> vector = getAllReferencesFor(paramCompoundType);
/*  255 */     Vector<OutputType> vector1 = new Vector();
/*  256 */     for (byte b = 0; b < vector.size(); b++) {
/*  257 */       Type type = vector.elementAt(b);
/*  258 */       if (type.isArray()) {
/*  259 */         ArrayType arrayType = (ArrayType)type;
/*  260 */         int i = arrayType.getArrayDimension();
/*  261 */         Type type1 = arrayType.getElementType();
/*  262 */         String str = unEsc(type1.getIDLName()).replace(' ', '_');
/*  263 */         for (byte b1 = 0; b1 < i; b1++) {
/*  264 */           String str1 = "seq" + (b1 + 1) + "_" + str;
/*  265 */           vector1.addElement(new OutputType(this, str1, arrayType));
/*      */         }
/*      */
/*  268 */       } else if (type.isCompound()) {
/*  269 */         String str = unEsc(type.getIDLName());
/*  270 */         vector1.addElement(new OutputType(this, str.replace(' ', '_'), type));
/*  271 */         if (type.isClass()) {
/*  272 */           ClassType classType = (ClassType)type;
/*  273 */           if (classType.isException()) {
/*  274 */             str = unEsc(classType.getIDLExceptionName());
/*  275 */             vector1.addElement(new OutputType(this, str.replace(' ', '_'), type));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  280 */     OutputType[] arrayOfOutputType = new OutputType[vector1.size()];
/*  281 */     vector1.copyInto((Object[])arrayOfOutputType);
/*  282 */     return arrayOfOutputType;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected Vector getAllReferencesFor(CompoundType paramCompoundType) {
/*      */     int i;
/*  293 */     Hashtable<Object, Object> hashtable1 = new Hashtable<>();
/*  294 */     Hashtable<Object, Object> hashtable2 = new Hashtable<>();
/*  295 */     Hashtable<Object, Object> hashtable3 = new Hashtable<>();
/*      */
/*  297 */     hashtable1.put(paramCompoundType.getQualifiedName(), paramCompoundType);
/*  298 */     accumulateReferences(hashtable1, hashtable2, hashtable3);
/*      */     do {
/*  300 */       i = hashtable1.size();
/*  301 */       accumulateReferences(hashtable1, hashtable2, hashtable3);
/*      */     }
/*  303 */     while (i < hashtable1.size());
/*      */
/*  305 */     Vector<CompoundType> vector = new Vector();
/*  306 */     Enumeration<CompoundType> enumeration = hashtable1.elements();
/*  307 */     while (enumeration.hasMoreElements()) {
/*  308 */       CompoundType compoundType = enumeration.nextElement();
/*  309 */       vector.addElement(compoundType);
/*      */     }
/*  311 */     enumeration = hashtable2.elements();
/*  312 */     while (enumeration.hasMoreElements()) {
/*  313 */       CompoundType compoundType = enumeration.nextElement();
/*  314 */       vector.addElement(compoundType);
/*      */     }
/*  316 */     enumeration = hashtable3.elements();
/*      */
/*  318 */     label29: while (enumeration.hasMoreElements()) {
/*  319 */       ArrayType arrayType = (ArrayType)enumeration.nextElement();
/*  320 */       int j = arrayType.getArrayDimension();
/*  321 */       Type type = arrayType.getElementType();
/*  322 */       Enumeration<ArrayType> enumeration1 = hashtable3.elements();
/*  323 */       while (enumeration1.hasMoreElements()) {
/*  324 */         ArrayType arrayType1 = enumeration1.nextElement();
/*  325 */         if (type == arrayType1.getElementType() && j < arrayType1
/*  326 */           .getArrayDimension())
/*      */           continue label29;
/*      */       }
/*  329 */       vector.addElement(arrayType);
/*      */     }
/*  331 */     return vector;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void accumulateReferences(Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3) {
/*  347 */     Enumeration<CompoundType> enumeration = paramHashtable1.elements();
/*  348 */     while (enumeration.hasMoreElements()) {
/*  349 */       CompoundType compoundType = enumeration.nextElement();
/*  350 */       Vector vector1 = getData(compoundType);
/*  351 */       Vector vector2 = getMethods(compoundType);
/*  352 */       getInterfaces(compoundType, paramHashtable1);
/*  353 */       getInheritance(compoundType, paramHashtable1);
/*  354 */       getMethodReferences(vector2, paramHashtable1, paramHashtable2, paramHashtable3, paramHashtable1);
/*  355 */       getMemberReferences(vector1, paramHashtable1, paramHashtable2, paramHashtable3);
/*      */     }
/*  357 */     enumeration = paramHashtable3.elements();
/*  358 */     while (enumeration.hasMoreElements()) {
/*  359 */       ArrayType arrayType = (ArrayType)enumeration.nextElement();
/*  360 */       Type type = arrayType.getElementType();
/*  361 */       addReference(type, paramHashtable1, paramHashtable2, paramHashtable3);
/*      */     }
/*  363 */     enumeration = paramHashtable1.elements();
/*  364 */     while (enumeration.hasMoreElements()) {
/*  365 */       CompoundType compoundType = enumeration.nextElement();
/*  366 */       if (!isIDLGeneratedFor(compoundType)) {
/*  367 */         paramHashtable1.remove(compoundType.getQualifiedName());
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected boolean isIDLGeneratedFor(CompoundType paramCompoundType) {
/*  386 */     if (paramCompoundType.isCORBAObject()) return false;
/*  387 */     if (paramCompoundType.isIDLEntity()) {
/*  388 */       if (paramCompoundType.isBoxed()) return true;
/*  389 */       if ("org.omg.CORBA.portable.IDLEntity"
/*  390 */         .equals(paramCompoundType.getQualifiedName())) return true;
/*  391 */       if (paramCompoundType.isCORBAUserException()) return true;
/*  392 */       return false;
/*  393 */     }  Hashtable<Object, Object> hashtable = new Hashtable<>();
/*  394 */     getInterfaces(paramCompoundType, hashtable);
/*  395 */     if (paramCompoundType.getTypeCode() == 65536) {
/*  396 */       if (hashtable.size() < 2) return false;
/*  397 */       return true;
/*  398 */     }  return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeOutputFor(OutputType paramOutputType, HashSet paramHashSet, IndentingWriter paramIndentingWriter) throws IOException {
/*  416 */     Type type = paramOutputType.getType();
/*  417 */     if (type.isArray()) {
/*  418 */       writeSequence(paramOutputType, paramIndentingWriter);
/*      */       return;
/*      */     }
/*  421 */     if (isSpecialReference(type)) {
/*  422 */       writeSpecial(type, paramIndentingWriter);
/*      */       return;
/*      */     }
/*  425 */     if (type.isCompound()) {
/*  426 */       CompoundType compoundType = (CompoundType)type;
/*  427 */       if (compoundType.isIDLEntity() && compoundType.isBoxed()) {
/*  428 */         writeBoxedIDL(compoundType, paramIndentingWriter);
/*      */         return;
/*      */       }
/*      */     }
/*  432 */     if (type.isClass()) {
/*  433 */       ClassType classType = (ClassType)type;
/*  434 */       if (classType.isException()) {
/*  435 */         String str1 = unEsc(classType.getIDLExceptionName());
/*  436 */         String str2 = paramOutputType.getName();
/*  437 */         if (str2.equals(str1.replace(' ', '_'))) {
/*  438 */           writeException(classType, paramIndentingWriter);
/*      */           return;
/*      */         }
/*      */       }
/*      */     }
/*  443 */     switch (type.getTypeCode()) {
/*      */       case 65536:
/*  445 */         writeImplementation((ImplementationType)type, paramIndentingWriter);
/*      */         return;
/*      */       case 16384:
/*      */       case 131072:
/*  449 */         writeNCType((CompoundType)type, paramIndentingWriter);
/*      */         return;
/*      */       case 4096:
/*      */       case 8192:
/*  453 */         writeRemote((RemoteType)type, paramIndentingWriter);
/*      */         return;
/*      */       case 32768:
/*  456 */         writeValue((ValueType)type, paramIndentingWriter);
/*      */         return;
/*      */     }
/*  459 */     throw new CompilerError("IDLGenerator got unexpected type code: " + type
/*      */
/*  461 */         .getTypeCode());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeImplementation(ImplementationType paramImplementationType, IndentingWriter paramIndentingWriter) throws IOException {
/*  475 */     Hashtable<Object, Object> hashtable1 = new Hashtable<>();
/*  476 */     Hashtable<Object, Object> hashtable2 = new Hashtable<>();
/*  477 */     getInterfaces(paramImplementationType, hashtable1);
/*      */
/*  479 */     writeBanner(paramImplementationType, 0, !this.isException, paramIndentingWriter);
/*  480 */     writeInheritedIncludes(hashtable1, paramIndentingWriter);
/*  481 */     writeIfndef(paramImplementationType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  482 */     writeIncOrb(paramIndentingWriter);
/*  483 */     writeModule1(paramImplementationType, paramIndentingWriter);
/*  484 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  485 */     paramIndentingWriter.p("interface " + paramImplementationType.getIDLName());
/*  486 */     writeInherits(hashtable1, !this.forValuetype, paramIndentingWriter);
/*      */
/*  488 */     paramIndentingWriter.pln(" {");
/*  489 */     paramIndentingWriter.pln("};");
/*      */
/*  491 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  492 */     writeModule2(paramImplementationType, paramIndentingWriter);
/*  493 */     writeEpilog(paramImplementationType, hashtable2, paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeNCType(CompoundType paramCompoundType, IndentingWriter paramIndentingWriter) throws IOException {
/*  508 */     Vector<CompoundType.Member> vector = getConstants(paramCompoundType);
/*  509 */     Vector<CompoundType.Method> vector1 = getMethods(paramCompoundType);
/*  510 */     Hashtable<Object, Object> hashtable1 = new Hashtable<>();
/*  511 */     Hashtable<Object, Object> hashtable2 = new Hashtable<>();
/*  512 */     Hashtable<Object, Object> hashtable3 = new Hashtable<>();
/*  513 */     Hashtable<Object, Object> hashtable4 = new Hashtable<>();
/*  514 */     Hashtable<Object, Object> hashtable5 = new Hashtable<>();
/*  515 */     getInterfaces(paramCompoundType, hashtable1);
/*  516 */     getInheritance(paramCompoundType, hashtable1);
/*  517 */     getMethodReferences(vector1, hashtable2, hashtable3, hashtable4, hashtable5);
/*      */
/*  519 */     writeProlog(paramCompoundType, hashtable2, hashtable3, hashtable4, hashtable5, hashtable1, paramIndentingWriter);
/*  520 */     writeModule1(paramCompoundType, paramIndentingWriter);
/*  521 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  522 */     paramIndentingWriter.p("abstract valuetype " + paramCompoundType.getIDLName());
/*  523 */     writeInherits(hashtable1, !this.forValuetype, paramIndentingWriter);
/*      */
/*  525 */     paramIndentingWriter.pln(" {");
/*  526 */     if (vector.size() + vector1.size() > 0) {
/*  527 */       paramIndentingWriter.pln(); paramIndentingWriter.pI(); byte b;
/*  528 */       for (b = 0; b < vector.size(); b++)
/*  529 */         writeConstant(vector.elementAt(b), paramIndentingWriter);
/*  530 */       for (b = 0; b < vector1.size(); b++)
/*  531 */         writeMethod(vector1.elementAt(b), paramIndentingWriter);
/*  532 */       paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*      */     }
/*  534 */     paramIndentingWriter.pln("};");
/*      */
/*  536 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  537 */     writeModule2(paramCompoundType, paramIndentingWriter);
/*  538 */     writeEpilog(paramCompoundType, hashtable2, paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeRemote(RemoteType paramRemoteType, IndentingWriter paramIndentingWriter) throws IOException {
/*  554 */     Vector<CompoundType.Member> vector = getConstants(paramRemoteType);
/*  555 */     Vector<CompoundType.Method> vector1 = getMethods(paramRemoteType);
/*  556 */     Hashtable<Object, Object> hashtable1 = new Hashtable<>();
/*  557 */     Hashtable<Object, Object> hashtable2 = new Hashtable<>();
/*  558 */     Hashtable<Object, Object> hashtable3 = new Hashtable<>();
/*  559 */     Hashtable<Object, Object> hashtable4 = new Hashtable<>();
/*  560 */     Hashtable<Object, Object> hashtable5 = new Hashtable<>();
/*  561 */     getInterfaces(paramRemoteType, hashtable1);
/*  562 */     getMethodReferences(vector1, hashtable2, hashtable3, hashtable4, hashtable5);
/*      */
/*  564 */     writeProlog(paramRemoteType, hashtable2, hashtable3, hashtable4, hashtable5, hashtable1, paramIndentingWriter);
/*  565 */     writeModule1(paramRemoteType, paramIndentingWriter);
/*  566 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  567 */     if (paramRemoteType.getTypeCode() == 8192) paramIndentingWriter.p("abstract ");
/*  568 */     paramIndentingWriter.p("interface " + paramRemoteType.getIDLName());
/*  569 */     writeInherits(hashtable1, !this.forValuetype, paramIndentingWriter);
/*      */
/*  571 */     paramIndentingWriter.pln(" {");
/*  572 */     if (vector.size() + vector1.size() > 0) {
/*  573 */       paramIndentingWriter.pln(); paramIndentingWriter.pI(); byte b;
/*  574 */       for (b = 0; b < vector.size(); b++)
/*  575 */         writeConstant(vector.elementAt(b), paramIndentingWriter);
/*  576 */       for (b = 0; b < vector1.size(); b++)
/*  577 */         writeMethod(vector1.elementAt(b), paramIndentingWriter);
/*  578 */       paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*      */     }
/*  580 */     paramIndentingWriter.pln("};");
/*      */
/*  582 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  583 */     writeRepositoryID(paramRemoteType, paramIndentingWriter);
/*  584 */     paramIndentingWriter.pln();
/*  585 */     writeModule2(paramRemoteType, paramIndentingWriter);
/*  586 */     writeEpilog(paramRemoteType, hashtable2, paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeValue(ValueType paramValueType, IndentingWriter paramIndentingWriter) throws IOException {
/*  600 */     Vector<CompoundType.Member> vector1 = getData(paramValueType);
/*  601 */     Vector<CompoundType.Member> vector2 = getConstants(paramValueType);
/*  602 */     Vector<CompoundType.Method> vector = getMethods(paramValueType);
/*  603 */     Hashtable<Object, Object> hashtable1 = new Hashtable<>();
/*  604 */     Hashtable<Object, Object> hashtable2 = new Hashtable<>();
/*  605 */     Hashtable<Object, Object> hashtable3 = new Hashtable<>();
/*  606 */     Hashtable<Object, Object> hashtable4 = new Hashtable<>();
/*  607 */     Hashtable<Object, Object> hashtable5 = new Hashtable<>();
/*  608 */     getInterfaces(paramValueType, hashtable1);
/*  609 */     getInheritance(paramValueType, hashtable1);
/*  610 */     getMethodReferences(vector, hashtable2, hashtable3, hashtable4, hashtable5);
/*  611 */     getMemberReferences(vector1, hashtable2, hashtable3, hashtable4);
/*      */
/*  613 */     writeProlog(paramValueType, hashtable2, hashtable3, hashtable4, hashtable5, hashtable1, paramIndentingWriter);
/*  614 */     writeModule1(paramValueType, paramIndentingWriter);
/*  615 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  616 */     if (paramValueType.isCustom()) paramIndentingWriter.p("custom ");
/*  617 */     paramIndentingWriter.p("valuetype " + paramValueType.getIDLName());
/*  618 */     writeInherits(hashtable1, this.forValuetype, paramIndentingWriter);
/*      */
/*  620 */     paramIndentingWriter.pln(" {");
/*  621 */     if (vector2.size() + vector1.size() + vector.size() > 0) {
/*  622 */       paramIndentingWriter.pln(); paramIndentingWriter.pI(); byte b;
/*  623 */       for (b = 0; b < vector2.size(); b++)
/*  624 */         writeConstant(vector2.elementAt(b), paramIndentingWriter);
/*  625 */       for (b = 0; b < vector1.size(); b++) {
/*  626 */         CompoundType.Member member = vector1.elementAt(b);
/*  627 */         if (member.getType().isPrimitive())
/*  628 */           writeData(member, paramIndentingWriter);
/*      */       }
/*  630 */       for (b = 0; b < vector1.size(); b++) {
/*  631 */         CompoundType.Member member = vector1.elementAt(b);
/*  632 */         if (!member.getType().isPrimitive())
/*  633 */           writeData(member, paramIndentingWriter);
/*      */       }
/*  635 */       for (b = 0; b < vector.size(); b++)
/*  636 */         writeMethod(vector.elementAt(b), paramIndentingWriter);
/*  637 */       paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*      */     }
/*  639 */     paramIndentingWriter.pln("};");
/*      */
/*  641 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  642 */     writeRepositoryID(paramValueType, paramIndentingWriter);
/*  643 */     paramIndentingWriter.pln();
/*  644 */     writeModule2(paramValueType, paramIndentingWriter);
/*  645 */     writeEpilog(paramValueType, hashtable2, paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeProlog(CompoundType paramCompoundType, Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3, Hashtable paramHashtable4, Hashtable paramHashtable5, IndentingWriter paramIndentingWriter) throws IOException {
/*  668 */     writeBanner(paramCompoundType, 0, !this.isException, paramIndentingWriter);
/*  669 */     writeForwardReferences(paramHashtable1, paramIndentingWriter);
/*  670 */     writeIncludes(paramHashtable4, this.isThrown, paramIndentingWriter);
/*  671 */     writeInheritedIncludes(paramHashtable5, paramIndentingWriter);
/*  672 */     writeIncludes(paramHashtable2, !this.isThrown, paramIndentingWriter);
/*  673 */     writeBoxedRMIIncludes(paramHashtable3, paramIndentingWriter);
/*  674 */     writeIDLEntityIncludes(paramHashtable1, paramIndentingWriter);
/*  675 */     writeIncOrb(paramIndentingWriter);
/*  676 */     writeIfndef(paramCompoundType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeEpilog(CompoundType paramCompoundType, Hashtable paramHashtable, IndentingWriter paramIndentingWriter) throws IOException {
/*  691 */     writeIncludes(paramHashtable, !this.isThrown, paramIndentingWriter);
/*  692 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeSpecial(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/*  706 */     String str = paramType.getQualifiedName();
/*  707 */     if ("java.io.Serializable".equals(str)) {
/*  708 */       writeJavaIoSerializable(paramType, paramIndentingWriter);
/*  709 */     } else if ("java.io.Externalizable".equals(str)) {
/*  710 */       writeJavaIoExternalizable(paramType, paramIndentingWriter);
/*  711 */     } else if ("java.lang.Object".equals(str)) {
/*  712 */       writeJavaLangObject(paramType, paramIndentingWriter);
/*  713 */     } else if ("java.rmi.Remote".equals(str)) {
/*  714 */       writeJavaRmiRemote(paramType, paramIndentingWriter);
/*  715 */     } else if ("org.omg.CORBA.portable.IDLEntity".equals(str)) {
/*  716 */       writeIDLEntity(paramType, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeJavaIoSerializable(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/*  731 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  732 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  733 */     writeModule1(paramType, paramIndentingWriter);
/*  734 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  735 */     paramIndentingWriter.pln("typedef any Serializable;");
/*  736 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  737 */     writeModule2(paramType, paramIndentingWriter);
/*  738 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeJavaIoExternalizable(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/*  752 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  753 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  754 */     writeModule1(paramType, paramIndentingWriter);
/*  755 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  756 */     paramIndentingWriter.pln("typedef any Externalizable;");
/*  757 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  758 */     writeModule2(paramType, paramIndentingWriter);
/*  759 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeJavaLangObject(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/*  773 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  774 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  775 */     writeModule1(paramType, paramIndentingWriter);
/*  776 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  777 */     paramIndentingWriter.pln("typedef any _Object;");
/*  778 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  779 */     writeModule2(paramType, paramIndentingWriter);
/*  780 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeJavaRmiRemote(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/*  794 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  795 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  796 */     writeModule1(paramType, paramIndentingWriter);
/*  797 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  798 */     paramIndentingWriter.pln("typedef Object Remote;");
/*  799 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  800 */     writeModule2(paramType, paramIndentingWriter);
/*  801 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeIDLEntity(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/*  816 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  817 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  818 */     writeModule1(paramType, paramIndentingWriter);
/*  819 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  820 */     paramIndentingWriter.pln("typedef any IDLEntity;");
/*  821 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  822 */     writeModule2(paramType, paramIndentingWriter);
/*  823 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void getInterfaces(CompoundType paramCompoundType, Hashtable<String, InterfaceType> paramHashtable) {
/*  835 */     InterfaceType[] arrayOfInterfaceType = paramCompoundType.getInterfaces();
/*      */
/*  837 */     for (byte b = 0; b < arrayOfInterfaceType.length; b++) {
/*  838 */       String str = arrayOfInterfaceType[b].getQualifiedName();
/*  839 */       switch (paramCompoundType.getTypeCode()) {
/*      */         case 32768:
/*      */         case 131072:
/*  842 */           if ("java.io.Externalizable".equals(str) || "java.io.Serializable"
/*  843 */             .equals(str) || "org.omg.CORBA.portable.IDLEntity"
/*  844 */             .equals(str)) {
/*      */             break;
/*      */           }
/*      */
/*      */
/*      */
/*      */
/*      */
/*  852 */           paramHashtable.put(str, arrayOfInterfaceType[b]); break;default: if ("java.rmi.Remote".equals(str)) break;  paramHashtable.put(str, arrayOfInterfaceType[b]);
/*      */           break;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void getInheritance(CompoundType paramCompoundType, Hashtable<String, ClassType> paramHashtable) {
/*  865 */     ClassType classType = paramCompoundType.getSuperclass();
/*  866 */     if (classType == null)
/*  867 */       return;  String str = classType.getQualifiedName();
/*  868 */     switch (paramCompoundType.getTypeCode()) {
/*      */       case 32768:
/*      */       case 131072:
/*  871 */         if ("java.lang.Object".equals(str))
/*      */           return;  break;
/*      */       default:
/*      */         return;
/*      */     }
/*  876 */     paramHashtable.put(str, classType);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void getMethodReferences(Vector<CompoundType.Method> paramVector, Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3, Hashtable paramHashtable4) {
/*  894 */     for (byte b = 0; b < paramVector.size(); b++) {
/*  895 */       CompoundType.Method method = paramVector.elementAt(b);
/*  896 */       Type[] arrayOfType = method.getArguments();
/*  897 */       Type type = method.getReturnType();
/*  898 */       getExceptions(method, paramHashtable4);
/*  899 */       for (byte b1 = 0; b1 < arrayOfType.length; b1++)
/*  900 */         addReference(arrayOfType[b1], paramHashtable1, paramHashtable2, paramHashtable3);
/*  901 */       addReference(type, paramHashtable1, paramHashtable2, paramHashtable3);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void getMemberReferences(Vector<CompoundType.Member> paramVector, Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3) {
/*  918 */     for (byte b = 0; b < paramVector.size(); b++) {
/*  919 */       CompoundType.Member member = paramVector.elementAt(b);
/*  920 */       Type type = member.getType();
/*  921 */       addReference(type, paramHashtable1, paramHashtable2, paramHashtable3);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void addReference(Type paramType, Hashtable<String, Type> paramHashtable1, Hashtable<String, Type> paramHashtable2, Hashtable<String, Type> paramHashtable3) {
/*  940 */     String str = paramType.getQualifiedName();
/*  941 */     switch (paramType.getTypeCode()) {
/*      */       case 4096:
/*      */       case 8192:
/*      */       case 16384:
/*      */       case 32768:
/*      */       case 131072:
/*  947 */         paramHashtable1.put(str, paramType);
/*      */         return;
/*      */       case 2048:
/*  950 */         if ("org.omg.CORBA.Object".equals(str))
/*  951 */           return;  paramHashtable1.put(str, paramType);
/*      */         return;
/*      */       case 262144:
/*  954 */         paramHashtable3.put(str + paramType.getArrayDimension(), paramType);
/*      */         return;
/*      */     }
/*  957 */     if (isSpecialReference(paramType)) {
/*  958 */       paramHashtable2.put(str, paramType);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected boolean isSpecialReference(Type paramType) {
/*  975 */     String str = paramType.getQualifiedName();
/*  976 */     if ("java.io.Serializable".equals(str)) return true;
/*  977 */     if ("java.io.Externalizable".equals(str)) return true;
/*  978 */     if ("java.lang.Object".equals(str)) return true;
/*  979 */     if ("java.rmi.Remote".equals(str)) return true;
/*  980 */     if ("org.omg.CORBA.portable.IDLEntity".equals(str)) return true;
/*  981 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void getExceptions(CompoundType.Method paramMethod, Hashtable<String, ValueType> paramHashtable) {
/*  995 */     ValueType[] arrayOfValueType = paramMethod.getExceptions();
/*  996 */     for (byte b = 0; b < arrayOfValueType.length; b++) {
/*  997 */       ValueType valueType = arrayOfValueType[b];
/*  998 */       if (valueType.isCheckedException() &&
/*  999 */         !valueType.isRemoteExceptionOrSubclass()) {
/* 1000 */         paramHashtable.put(valueType.getQualifiedName(), valueType);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected Vector getMethods(CompoundType paramCompoundType) {
/* 1014 */     Vector<CompoundType.Method> vector = new Vector();
/* 1015 */     int i = paramCompoundType.getTypeCode();
/* 1016 */     switch (i) { case 4096: case 8192:
/*      */         break;
/*      */       case 16384:
/*      */       case 32768:
/*      */       case 131072:
/* 1021 */         if (this.valueMethods)
/* 1022 */           break; default: return vector; }
/*      */
/* 1024 */     Identifier identifier = paramCompoundType.getIdentifier();
/* 1025 */     CompoundType.Method[] arrayOfMethod = paramCompoundType.getMethods();
/*      */
/* 1027 */     for (byte b = 0; b < arrayOfMethod.length; b++) {
/* 1028 */       if (arrayOfMethod[b].isPrivate() || arrayOfMethod[b]
/* 1029 */         .isInherited())
/*      */         continue;
/* 1031 */       if (i == 32768) {
/* 1032 */         String str = arrayOfMethod[b].getName();
/* 1033 */         if ("readObject".equals(str) || "writeObject"
/* 1034 */           .equals(str) || "readExternal"
/* 1035 */           .equals(str) || "writeExternal"
/* 1036 */           .equals(str))
/*      */           continue;
/*      */       }
/* 1039 */       if ((i != 131072 && i != 16384) ||
/*      */
/* 1041 */         !arrayOfMethod[b].isConstructor())
/*      */       {
/* 1043 */         vector.addElement(arrayOfMethod[b]); }  continue;
/*      */     }
/* 1045 */     return vector;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected Vector getConstants(CompoundType paramCompoundType) {
/* 1058 */     Vector<CompoundType.Member> vector = new Vector();
/* 1059 */     CompoundType.Member[] arrayOfMember = paramCompoundType.getMembers();
/* 1060 */     for (byte b = 0; b < arrayOfMember.length; b++) {
/* 1061 */       Type type = arrayOfMember[b].getType();
/* 1062 */       String str = arrayOfMember[b].getValue();
/* 1063 */       if (arrayOfMember[b].isPublic() && arrayOfMember[b]
/* 1064 */         .isFinal() && arrayOfMember[b]
/* 1065 */         .isStatic() && (type
/* 1066 */         .isPrimitive() || "String".equals(type.getName())) && str != null)
/*      */       {
/* 1068 */         vector.addElement(arrayOfMember[b]); }
/*      */     }
/* 1070 */     return vector;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected Vector getData(CompoundType paramCompoundType) {
/* 1085 */     Vector<CompoundType.Member> vector = new Vector();
/* 1086 */     if (paramCompoundType.getTypeCode() != 32768) return vector;
/* 1087 */     ValueType valueType = (ValueType)paramCompoundType;
/* 1088 */     CompoundType.Member[] arrayOfMember = valueType.getMembers();
/* 1089 */     boolean bool = !valueType.isCustom() ? true : false;
/* 1090 */     for (byte b = 0; b < arrayOfMember.length; b++) {
/* 1091 */       if (!arrayOfMember[b].isStatic() &&
/* 1092 */         !arrayOfMember[b].isTransient() && (arrayOfMember[b]
/* 1093 */         .isPublic() || bool)) {
/*      */
/* 1095 */         String str = arrayOfMember[b].getName(); byte b1;
/* 1096 */         for (b1 = 0; b1 < vector.size(); b1++) {
/* 1097 */           CompoundType.Member member = vector.elementAt(b1);
/* 1098 */           if (str.compareTo(member.getName()) < 0)
/*      */             break;
/* 1100 */         }  vector.insertElementAt(arrayOfMember[b], b1);
/*      */       }
/*      */     }
/* 1103 */     return vector;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeForwardReferences(Hashtable paramHashtable, IndentingWriter paramIndentingWriter) throws IOException {
/* 1117 */     Enumeration<Type> enumeration = paramHashtable.elements();
/*      */
/* 1119 */     while (enumeration.hasMoreElements()) {
/* 1120 */       Type type = enumeration.nextElement();
/* 1121 */       if (type.isCompound()) {
/* 1122 */         CompoundType compoundType = (CompoundType)type;
/* 1123 */         if (compoundType.isIDLEntity())
/*      */           continue;
/*      */       }
/* 1126 */       writeForwardReference(type, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeForwardReference(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/* 1140 */     String str = paramType.getQualifiedName();
/* 1141 */     if (!"java.lang.String".equals(str) &&
/* 1142 */       "org.omg.CORBA.Object".equals(str))
/*      */       return;
/* 1144 */     writeIfndef(paramType, 0, !this.isException, this.isForward, paramIndentingWriter);
/* 1145 */     writeModule1(paramType, paramIndentingWriter);
/* 1146 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/* 1147 */     switch (paramType.getTypeCode()) { case 16384:
/*      */       case 131072:
/* 1149 */         paramIndentingWriter.p("abstract valuetype "); break;
/* 1150 */       case 8192: paramIndentingWriter.p("abstract interface "); break;
/* 1151 */       case 32768: paramIndentingWriter.p("valuetype "); break;
/*      */       case 2048: case 4096:
/* 1153 */         paramIndentingWriter.p("interface ");
/*      */         break; }
/*      */
/* 1156 */     paramIndentingWriter.pln(paramType.getIDLName() + ";");
/* 1157 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1158 */     writeModule2(paramType, paramIndentingWriter);
/* 1159 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeForwardReference(ArrayType paramArrayType, int paramInt, IndentingWriter paramIndentingWriter) throws IOException {
/* 1177 */     Type type = paramArrayType.getElementType();
/* 1178 */     if (paramInt < 1) {
/* 1179 */       if (type.isCompound()) {
/* 1180 */         CompoundType compoundType = (CompoundType)type;
/* 1181 */         writeForwardReference(type, paramIndentingWriter);
/*      */       }
/*      */       return;
/*      */     }
/* 1185 */     String str = unEsc(type.getIDLName()).replace(' ', '_');
/*      */
/* 1187 */     writeIfndef(paramArrayType, paramInt, !this.isException, this.isForward, paramIndentingWriter);
/* 1188 */     writeModule1(paramArrayType, paramIndentingWriter);
/* 1189 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/* 1190 */     switch (type.getTypeCode()) { case 16384:
/*      */       case 131072:
/* 1192 */         paramIndentingWriter.p("abstract valuetype "); break;
/* 1193 */       case 8192: paramIndentingWriter.p("abstract interface "); break;
/* 1194 */       case 32768: paramIndentingWriter.p("valuetype "); break;
/*      */       case 2048: case 4096:
/* 1196 */         paramIndentingWriter.p("interface ");
/*      */         break; }
/*      */
/* 1199 */     paramIndentingWriter.pln("seq" + paramInt + "_" + str + ";");
/* 1200 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1201 */     writeModule2(paramArrayType, paramIndentingWriter);
/* 1202 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeIDLEntityIncludes(Hashtable paramHashtable, IndentingWriter paramIndentingWriter) throws IOException {
/* 1215 */     Enumeration<Type> enumeration = paramHashtable.elements();
/* 1216 */     while (enumeration.hasMoreElements()) {
/* 1217 */       Type type = enumeration.nextElement();
/* 1218 */       if (type.isCompound()) {
/* 1219 */         CompoundType compoundType = (CompoundType)type;
/* 1220 */         if (compoundType.isIDLEntity()) {
/* 1221 */           writeInclude(compoundType, 0, !this.isThrown, paramIndentingWriter);
/* 1222 */           paramHashtable.remove(compoundType.getQualifiedName());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeIncludes(Hashtable paramHashtable, boolean paramBoolean, IndentingWriter paramIndentingWriter) throws IOException {
/* 1240 */     Enumeration<CompoundType> enumeration = paramHashtable.elements();
/* 1241 */     while (enumeration.hasMoreElements()) {
/* 1242 */       CompoundType compoundType = enumeration.nextElement();
/* 1243 */       writeInclude(compoundType, 0, paramBoolean, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeBoxedRMIIncludes(Hashtable paramHashtable, IndentingWriter paramIndentingWriter) throws IOException {
/* 1258 */     Enumeration<ArrayType> enumeration = paramHashtable.elements();
/*      */
/* 1260 */     label18: while (enumeration.hasMoreElements()) {
/* 1261 */       ArrayType arrayType = enumeration.nextElement();
/* 1262 */       int i = arrayType.getArrayDimension();
/* 1263 */       Type type = arrayType.getElementType();
/*      */
/* 1265 */       Enumeration<ArrayType> enumeration1 = paramHashtable.elements();
/* 1266 */       while (enumeration1.hasMoreElements()) {
/* 1267 */         ArrayType arrayType1 = enumeration1.nextElement();
/* 1268 */         if (type == arrayType1.getElementType() && i < arrayType1
/* 1269 */           .getArrayDimension())
/*      */           continue label18;
/*      */       }
/* 1272 */       writeInclude(arrayType, i, !this.isThrown, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeInheritedIncludes(Hashtable paramHashtable, IndentingWriter paramIndentingWriter) throws IOException {
/* 1286 */     Enumeration<CompoundType> enumeration = paramHashtable.elements();
/* 1287 */     while (enumeration.hasMoreElements()) {
/* 1288 */       CompoundType compoundType = enumeration.nextElement();
/* 1289 */       writeInclude(compoundType, 0, !this.isThrown, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeInclude(Type paramType, int paramInt, boolean paramBoolean, IndentingWriter paramIndentingWriter) throws IOException {
/*      */     String str, arrayOfString[];
/* 1310 */     if (paramType.isCompound()) {
/* 1311 */       CompoundType compoundType = (CompoundType)paramType;
/* 1312 */       String str1 = compoundType.getQualifiedName();
/* 1313 */       if ("java.lang.String".equals(str1)) {
/* 1314 */         writeIncOrb(paramIndentingWriter);
/*      */         return;
/*      */       }
/* 1317 */       if ("org.omg.CORBA.Object".equals(str1))
/*      */         return;
/* 1319 */       arrayOfString = getIDLModuleNames(compoundType);
/* 1320 */       str = unEsc(compoundType.getIDLName());
/*      */
/* 1322 */       if (compoundType.isException())
/* 1323 */         if (compoundType.isIDLEntityException()) {
/* 1324 */           if (compoundType.isCORBAUserException())
/* 1325 */           { if (paramBoolean) str = unEsc(compoundType.getIDLExceptionName());  }
/*      */           else
/* 1327 */           { str = compoundType.getName(); }
/* 1328 */         } else if (paramBoolean) {
/* 1329 */           str = unEsc(compoundType.getIDLExceptionName());
/*      */         }
/* 1331 */     } else if (paramType.isArray()) {
/* 1332 */       Type type = paramType.getElementType();
/* 1333 */       if (paramInt > 0) {
/* 1334 */         arrayOfString = getIDLModuleNames(paramType);
/* 1335 */         str = "seq" + paramInt + "_" + unEsc(type.getIDLName().replace(' ', '_'));
/*      */       } else {
/*      */
/* 1338 */         if (!type.isCompound())
/* 1339 */           return;  CompoundType compoundType = (CompoundType)type;
/* 1340 */         arrayOfString = getIDLModuleNames(compoundType);
/* 1341 */         str = unEsc(compoundType.getIDLName());
/* 1342 */         writeInclude(compoundType, arrayOfString, str, paramIndentingWriter); return;
/*      */       }
/*      */     } else {
/*      */       return;
/*      */     }
/* 1347 */     writeInclude(paramType, arrayOfString, str, paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeInclude(Type paramType, String[] paramArrayOfString, String paramString, IndentingWriter paramIndentingWriter) throws IOException {
/* 1365 */     if (paramType.isCompound()) {
/* 1366 */       CompoundType compoundType = (CompoundType)paramType;
/*      */
/* 1368 */       if (this.ifHash.size() > 0 && compoundType
/* 1369 */         .isIDLEntity()) {
/* 1370 */         String str = paramType.getQualifiedName();
/*      */
/* 1372 */         Enumeration<String> enumeration = this.ifHash.keys();
/* 1373 */         while (enumeration.hasMoreElements()) {
/* 1374 */           String str1 = enumeration.nextElement();
/* 1375 */           if (str.startsWith(str1)) {
/* 1376 */             String str2 = (String)this.ifHash.get(str1);
/* 1377 */             paramIndentingWriter.pln("#include \"" + str2 + "\"");
/*      */
/*      */             return;
/*      */           }
/*      */         }
/*      */       }
/* 1383 */     } else if (!paramType.isArray()) {
/*      */       return;
/*      */     }
/* 1386 */     paramIndentingWriter.p("#include \"");
/* 1387 */     for (byte b = 0; b < paramArrayOfString.length; ) { paramIndentingWriter.p(paramArrayOfString[b] + "/"); b++; }
/* 1388 */      paramIndentingWriter.p(paramString + ".idl\"");
/* 1389 */     paramIndentingWriter.pln();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected String getQualifiedName(Type paramType) {
/* 1401 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 1402 */     int i = arrayOfString.length;
/* 1403 */     StringBuffer stringBuffer = new StringBuffer();
/* 1404 */     for (byte b = 0; b < i; b++)
/* 1405 */       stringBuffer.append(arrayOfString[b] + ".");
/* 1406 */     stringBuffer.append(paramType.getIDLName());
/* 1407 */     return stringBuffer.toString();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected String getQualifiedIDLName(Type paramType) {
/* 1418 */     if (paramType.isPrimitive())
/* 1419 */       return paramType.getIDLName();
/* 1420 */     if (!paramType.isArray() && "org.omg.CORBA.Object"
/* 1421 */       .equals(paramType.getQualifiedName())) {
/* 1422 */       return paramType.getIDLName();
/*      */     }
/* 1424 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 1425 */     int i = arrayOfString.length;
/* 1426 */     if (i > 0) {
/* 1427 */       StringBuffer stringBuffer = new StringBuffer();
/* 1428 */       for (byte b = 0; b < i; b++)
/* 1429 */         stringBuffer.append("::" + arrayOfString[b]);
/* 1430 */       stringBuffer.append("::" + paramType.getIDLName());
/* 1431 */       return stringBuffer.toString();
/*      */     }
/* 1433 */     return paramType.getIDLName();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected String[] getIDLModuleNames(Type paramType) {
/*      */     CompoundType compoundType;
/* 1448 */     String[] arrayOfString1 = paramType.getIDLModuleNames();
/*      */
/* 1450 */     if (paramType.isCompound())
/* 1451 */     { compoundType = (CompoundType)paramType;
/* 1452 */       if (!compoundType.isIDLEntity) return arrayOfString1;
/* 1453 */       if ("org.omg.CORBA.portable.IDLEntity"
/* 1454 */         .equals(paramType.getQualifiedName())) {
/* 1455 */         return arrayOfString1;
/*      */       } }
/* 1457 */     else if (paramType.isArray())
/* 1458 */     { Type type = paramType.getElementType();
/* 1459 */       if (type.isCompound()) {
/* 1460 */         compoundType = (CompoundType)type;
/* 1461 */         if (!compoundType.isIDLEntity) return arrayOfString1;
/* 1462 */         if ("org.omg.CORBA.portable.IDLEntity"
/* 1463 */           .equals(paramType.getQualifiedName()))
/* 1464 */           return arrayOfString1;
/*      */       } else {
/* 1466 */         return arrayOfString1;
/*      */       }  }
/* 1468 */     else { return arrayOfString1; }
/*      */
/*      */
/* 1471 */     Vector<String> vector = new Vector();
/* 1472 */     if (!translateJavaPackage(compoundType, vector)) {
/* 1473 */       stripJavaPackage(compoundType, vector);
/*      */     }
/* 1475 */     if (compoundType.isBoxed()) {
/* 1476 */       vector.insertElementAt("org", 0);
/* 1477 */       vector.insertElementAt("omg", 1);
/* 1478 */       vector.insertElementAt("boxedIDL", 2);
/*      */     }
/* 1480 */     if (paramType.isArray()) {
/* 1481 */       vector.insertElementAt("org", 0);
/* 1482 */       vector.insertElementAt("omg", 1);
/* 1483 */       vector.insertElementAt("boxedRMI", 2);
/*      */     }
/* 1485 */     String[] arrayOfString2 = new String[vector.size()];
/* 1486 */     vector.copyInto((Object[])arrayOfString2);
/* 1487 */     return arrayOfString2;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected boolean translateJavaPackage(CompoundType paramCompoundType, Vector<String> paramVector) {
/* 1502 */     paramVector.removeAllElements();
/* 1503 */     boolean bool = false;
/* 1504 */     String str1 = null;
/* 1505 */     if (!paramCompoundType.isIDLEntity()) return bool;
/*      */
/* 1507 */     String str2 = paramCompoundType.getPackageName();
/* 1508 */     if (str2 == null) return bool;
/* 1509 */     StringTokenizer stringTokenizer = new StringTokenizer(str2, ".");
/* 1510 */     for (; stringTokenizer.hasMoreTokens(); paramVector.addElement(stringTokenizer.nextToken()));
/*      */
/* 1512 */     if (this.imHash.size() > 0) {
/* 1513 */       Enumeration<String> enumeration = this.imHash.keys();
/*      */
/*      */
/* 1516 */       label50: while (enumeration.hasMoreElements()) {
/* 1517 */         String str3 = enumeration.nextElement();
/* 1518 */         StringTokenizer stringTokenizer1 = new StringTokenizer(str3, ".");
/* 1519 */         int i = paramVector.size();
/*      */         byte b1;
/* 1521 */         for (b1 = 0; b1 < i && stringTokenizer1.hasMoreTokens(); b1++) {
/* 1522 */           if (!paramVector.elementAt(b1).equals(stringTokenizer1.nextToken()))
/*      */             continue label50;
/*      */         }
/* 1525 */         if (stringTokenizer1.hasMoreTokens()) {
/* 1526 */           str1 = stringTokenizer1.nextToken();
/* 1527 */           if (!paramCompoundType.getName().equals(str1) || stringTokenizer1
/* 1528 */             .hasMoreTokens()) {
/*      */             continue;
/*      */           }
/*      */         }
/* 1532 */         bool = true;
/* 1533 */         for (byte b2 = 0; b2 < b1; b2++) {
/* 1534 */           paramVector.removeElementAt(0);
/*      */         }
/* 1536 */         String str4 = (String)this.imHash.get(str3);
/* 1537 */         StringTokenizer stringTokenizer2 = new StringTokenizer(str4, "::");
/*      */
/* 1539 */         int j = stringTokenizer2.countTokens();
/* 1540 */         byte b3 = 0;
/* 1541 */         if (str1 != null) j--;
/* 1542 */         for (b3 = 0; b3 < j; b3++)
/* 1543 */           paramVector.insertElementAt(stringTokenizer2.nextToken(), b3);
/* 1544 */         if (str1 != null) {
/* 1545 */           String str = stringTokenizer2.nextToken();
/* 1546 */           if (!paramCompoundType.getName().equals(str))
/* 1547 */             paramVector.insertElementAt(str, b3);
/*      */         }
/*      */       }
/*      */     }
/* 1551 */     return bool;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void stripJavaPackage(CompoundType paramCompoundType, Vector<String> paramVector) {
/* 1567 */     paramVector.removeAllElements();
/* 1568 */     if (!paramCompoundType.isIDLEntity())
/*      */       return;
/* 1570 */     String str1 = paramCompoundType.getRepositoryID().substring(4);
/* 1571 */     StringTokenizer stringTokenizer1 = new StringTokenizer(str1, "/");
/* 1572 */     if (stringTokenizer1.countTokens() < 2)
/*      */       return;
/* 1574 */     while (stringTokenizer1.hasMoreTokens())
/* 1575 */       paramVector.addElement(stringTokenizer1.nextToken());
/* 1576 */     paramVector.removeElementAt(paramVector.size() - 1);
/*      */
/* 1578 */     String str2 = paramCompoundType.getPackageName();
/* 1579 */     if (str2 == null)
/* 1580 */       return;  Vector<String> vector = new Vector();
/* 1581 */     StringTokenizer stringTokenizer2 = new StringTokenizer(str2, ".");
/* 1582 */     for (; stringTokenizer2.hasMoreTokens(); vector.addElement(stringTokenizer2.nextToken()));
/*      */
/* 1584 */     int i = paramVector.size() - 1;
/* 1585 */     int j = vector.size() - 1;
/* 1586 */     while (i >= 0 && j >= 0) {
/* 1587 */       String str3 = paramVector.elementAt(i);
/* 1588 */       String str4 = vector.elementAt(j);
/* 1589 */       if (!str4.equals(str3))
/* 1590 */         break;  i--; j--;
/*      */     }
/* 1592 */     for (byte b = 0; b <= i; b++) {
/* 1593 */       paramVector.removeElementAt(0);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeSequence(OutputType paramOutputType, IndentingWriter paramIndentingWriter) throws IOException {
/* 1610 */     ArrayType arrayType = (ArrayType)paramOutputType.getType();
/* 1611 */     Type type = arrayType.getElementType();
/* 1612 */     String str1 = paramOutputType.getName();
/* 1613 */     int i = Integer.parseInt(str1.substring(3, str1.indexOf("_")));
/* 1614 */     String str2 = unEsc(type.getIDLName()).replace(' ', '_');
/* 1615 */     String str3 = getQualifiedIDLName(type);
/* 1616 */     String str4 = type.getQualifiedName();
/*      */
/* 1618 */     String str5 = arrayType.getRepositoryID();
/* 1619 */     int j = str5.indexOf('[');
/* 1620 */     int k = str5.lastIndexOf('[') + 1;
/*      */
/*      */
/* 1623 */     StringBuffer stringBuffer = new StringBuffer(str5.substring(0, j) + str5.substring(k));
/* 1624 */     for (byte b = 0; b < i; ) { stringBuffer.insert(j, '['); b++; }
/*      */
/* 1626 */     String str6 = "seq" + i + "_" + str2;
/* 1627 */     boolean bool1 = false;
/* 1628 */     if (type.isCompound()) {
/* 1629 */       CompoundType compoundType = (CompoundType)type;
/* 1630 */       bool1 = (compoundType.isIDLEntity() || compoundType.isCORBAObject()) ? true : false;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1638 */     boolean bool2 = (type.isCompound() && !isSpecialReference(type) && i == 1 && !bool1 && !"org.omg.CORBA.Object".equals(str4) && !"java.lang.String".equals(str4)) ? true : false;
/*      */
/* 1640 */     writeBanner(arrayType, i, !this.isException, paramIndentingWriter);
/* 1641 */     if (i == 1 && "java.lang.String".equals(str4))
/* 1642 */       writeIncOrb(paramIndentingWriter);
/* 1643 */     if ((i != 1 || !"org.omg.CORBA.Object".equals(str4)) && (
/* 1644 */       isSpecialReference(type) || i > 1 || bool1))
/* 1645 */       writeInclude(arrayType, i - 1, !this.isThrown, paramIndentingWriter);
/* 1646 */     writeIfndef(arrayType, i, !this.isException, !this.isForward, paramIndentingWriter);
/* 1647 */     if (bool2)
/* 1648 */       writeForwardReference(arrayType, i - 1, paramIndentingWriter);
/* 1649 */     writeModule1(arrayType, paramIndentingWriter);
/* 1650 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/* 1651 */     paramIndentingWriter.p("valuetype " + str6);
/* 1652 */     paramIndentingWriter.p(" sequence<");
/* 1653 */     if (i == 1) { paramIndentingWriter.p(str3); }
/*      */     else
/* 1655 */     { paramIndentingWriter.p("seq" + (i - 1) + "_");
/* 1656 */       paramIndentingWriter.p(str2); }
/*      */
/* 1658 */     paramIndentingWriter.pln(">;");
/* 1659 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1660 */     paramIndentingWriter.pln("#pragma ID " + str6 + " \"" + stringBuffer + "\"");
/* 1661 */     paramIndentingWriter.pln();
/* 1662 */     writeModule2(arrayType, paramIndentingWriter);
/* 1663 */     if (bool2)
/* 1664 */       writeInclude(arrayType, i - 1, !this.isThrown, paramIndentingWriter);
/* 1665 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeBoxedIDL(CompoundType paramCompoundType, IndentingWriter paramIndentingWriter) throws IOException {
/* 1678 */     String[] arrayOfString1 = getIDLModuleNames(paramCompoundType);
/* 1679 */     int i = arrayOfString1.length;
/* 1680 */     String[] arrayOfString2 = new String[i - 3];
/* 1681 */     for (byte b1 = 0; b1 < i - 3; ) { arrayOfString2[b1] = arrayOfString1[b1 + 3]; b1++; }
/* 1682 */      String str = unEsc(paramCompoundType.getIDLName());
/*      */
/* 1684 */     writeBanner(paramCompoundType, 0, !this.isException, paramIndentingWriter);
/* 1685 */     writeInclude(paramCompoundType, arrayOfString2, str, paramIndentingWriter);
/* 1686 */     writeIfndef(paramCompoundType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/* 1687 */     writeModule1(paramCompoundType, paramIndentingWriter);
/* 1688 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*      */
/* 1690 */     paramIndentingWriter.p("valuetype " + str + " ");
/* 1691 */     for (byte b2 = 0; b2 < arrayOfString2.length; b2++)
/* 1692 */       paramIndentingWriter.p("::" + arrayOfString2[b2]);
/* 1693 */     paramIndentingWriter.pln("::" + str + ";");
/*      */
/* 1695 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1696 */     writeRepositoryID(paramCompoundType, paramIndentingWriter);
/* 1697 */     paramIndentingWriter.pln();
/* 1698 */     writeModule2(paramCompoundType, paramIndentingWriter);
/* 1699 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeException(ClassType paramClassType, IndentingWriter paramIndentingWriter) throws IOException {
/* 1712 */     writeBanner(paramClassType, 0, this.isException, paramIndentingWriter);
/* 1713 */     writeIfndef(paramClassType, 0, this.isException, !this.isForward, paramIndentingWriter);
/* 1714 */     writeForwardReference(paramClassType, paramIndentingWriter);
/* 1715 */     writeModule1(paramClassType, paramIndentingWriter);
/* 1716 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*      */
/* 1718 */     paramIndentingWriter.pln("exception " + paramClassType.getIDLExceptionName() + " {");
/* 1719 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/* 1720 */     paramIndentingWriter.pln(paramClassType.getIDLName() + " value;");
/* 1721 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1722 */     paramIndentingWriter.pln("};");
/*      */
/* 1724 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1725 */     writeModule2(paramClassType, paramIndentingWriter);
/* 1726 */     writeInclude(paramClassType, 0, !this.isThrown, paramIndentingWriter);
/* 1727 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeRepositoryID(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/* 1740 */     String str = paramType.getRepositoryID();
/* 1741 */     if (paramType.isCompound()) {
/* 1742 */       CompoundType compoundType = (CompoundType)paramType;
/* 1743 */       if (compoundType.isBoxed()) {
/* 1744 */         str = compoundType.getBoxedRepositoryID();
/*      */       }
/*      */     }
/* 1747 */     paramIndentingWriter.pln("#pragma ID " + paramType.getIDLName() + " \"" + str + "\"");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeInherits(Hashtable paramHashtable, boolean paramBoolean, IndentingWriter paramIndentingWriter) throws IOException {
/* 1765 */     int i = paramHashtable.size();
/* 1766 */     int j = 0;
/* 1767 */     byte b = 0;
/* 1768 */     if (i < 1)
/* 1769 */       return;  Enumeration<CompoundType> enumeration = paramHashtable.elements();
/*      */
/* 1771 */     if (paramBoolean)
/* 1772 */       while (enumeration.hasMoreElements()) {
/* 1773 */         CompoundType compoundType = enumeration.nextElement();
/* 1774 */         if (compoundType.getTypeCode() == 8192) b++;
/*      */       }
/* 1776 */     j = i - b;
/*      */
/* 1778 */     if (j > 0) {
/* 1779 */       paramIndentingWriter.p(": ");
/* 1780 */       enumeration = paramHashtable.elements();
/* 1781 */       while (enumeration.hasMoreElements()) {
/* 1782 */         CompoundType compoundType = enumeration.nextElement();
/* 1783 */         if (compoundType.isClass()) {
/* 1784 */           paramIndentingWriter.p(getQualifiedIDLName(compoundType));
/* 1785 */           if (j > 1) { paramIndentingWriter.p(", "); break; }
/* 1786 */            if (i > 1) paramIndentingWriter.p(" ");
/*      */           break;
/*      */         }
/*      */       }
/* 1790 */       byte b1 = 0;
/* 1791 */       enumeration = paramHashtable.elements();
/* 1792 */       while (enumeration.hasMoreElements()) {
/* 1793 */         CompoundType compoundType = enumeration.nextElement();
/* 1794 */         if (!compoundType.isClass() && compoundType
/* 1795 */           .getTypeCode() != 8192) {
/* 1796 */           if (b1++ > 0) paramIndentingWriter.p(", ");
/* 1797 */           paramIndentingWriter.p(getQualifiedIDLName(compoundType));
/*      */         }
/*      */       }
/*      */     }
/* 1801 */     if (b > 0) {
/* 1802 */       paramIndentingWriter.p(" supports ");
/* 1803 */       byte b1 = 0;
/* 1804 */       enumeration = paramHashtable.elements();
/* 1805 */       while (enumeration.hasMoreElements()) {
/* 1806 */         CompoundType compoundType = enumeration.nextElement();
/* 1807 */         if (compoundType.getTypeCode() == 8192) {
/* 1808 */           if (b1++ > 0) paramIndentingWriter.p(", ");
/* 1809 */           paramIndentingWriter.p(getQualifiedIDLName(compoundType));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeConstant(CompoundType.Member paramMember, IndentingWriter paramIndentingWriter) throws IOException {
/* 1825 */     Type type = paramMember.getType();
/* 1826 */     paramIndentingWriter.p("const ");
/* 1827 */     paramIndentingWriter.p(getQualifiedIDLName(type));
/* 1828 */     paramIndentingWriter.p(" " + paramMember.getIDLName() + " = " + paramMember.getValue());
/* 1829 */     paramIndentingWriter.pln(";");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeData(CompoundType.Member paramMember, IndentingWriter paramIndentingWriter) throws IOException {
/* 1843 */     if (paramMember.isInnerClassDeclaration())
/* 1844 */       return;  Type type = paramMember.getType();
/* 1845 */     if (paramMember.isPublic())
/* 1846 */     { paramIndentingWriter.p("public "); }
/* 1847 */     else { paramIndentingWriter.p("private "); }
/* 1848 */      paramIndentingWriter.pln(getQualifiedIDLName(type) + " " + paramMember
/* 1849 */         .getIDLName() + ";");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeAttribute(CompoundType.Method paramMethod, IndentingWriter paramIndentingWriter) throws IOException {
/* 1863 */     if (paramMethod.getAttributeKind() == 5)
/* 1864 */       return;  Type type = paramMethod.getReturnType();
/* 1865 */     if (!paramMethod.isReadWriteAttribute()) paramIndentingWriter.p("readonly ");
/* 1866 */     paramIndentingWriter.p("attribute " + getQualifiedIDLName(type) + " ");
/* 1867 */     paramIndentingWriter.pln(paramMethod.getAttributeName() + ";");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeMethod(CompoundType.Method paramMethod, IndentingWriter paramIndentingWriter) throws IOException {
/* 1881 */     if (paramMethod.isAttribute()) {
/* 1882 */       writeAttribute(paramMethod, paramIndentingWriter);
/*      */       return;
/*      */     }
/* 1885 */     Type[] arrayOfType = paramMethod.getArguments();
/* 1886 */     String[] arrayOfString = paramMethod.getArgumentNames();
/* 1887 */     Type type = paramMethod.getReturnType();
/* 1888 */     Hashtable<Object, Object> hashtable = new Hashtable<>();
/* 1889 */     getExceptions(paramMethod, hashtable);
/*      */
/* 1891 */     if (paramMethod.isConstructor())
/* 1892 */     { if (this.factory) { paramIndentingWriter.p("factory " + paramMethod.getIDLName() + "("); }
/* 1893 */       else { paramIndentingWriter.p("init("); }
/*      */        }
/* 1895 */     else { paramIndentingWriter.p(getQualifiedIDLName(type));
/* 1896 */       paramIndentingWriter.p(" " + paramMethod.getIDLName() + "("); }
/*      */
/* 1898 */     paramIndentingWriter.pI();
/*      */     byte b;
/* 1900 */     for (b = 0; b < arrayOfType.length; b++) {
/* 1901 */       if (b > 0) { paramIndentingWriter.pln(","); }
/* 1902 */       else { paramIndentingWriter.pln(); }
/* 1903 */        paramIndentingWriter.p("in ");
/* 1904 */       paramIndentingWriter.p(getQualifiedIDLName(arrayOfType[b]));
/* 1905 */       paramIndentingWriter.p(" " + arrayOfString[b]);
/*      */     }
/* 1907 */     paramIndentingWriter.pO();
/* 1908 */     paramIndentingWriter.p(" )");
/*      */
/* 1910 */     if (hashtable.size() > 0) {
/* 1911 */       paramIndentingWriter.pln(" raises (");
/* 1912 */       paramIndentingWriter.pI();
/* 1913 */       b = 0;
/* 1914 */       Enumeration<ValueType> enumeration = hashtable.elements();
/* 1915 */       while (enumeration.hasMoreElements()) {
/* 1916 */         ValueType valueType = enumeration.nextElement();
/* 1917 */         if (b > 0) paramIndentingWriter.pln(",");
/* 1918 */         if (valueType.isIDLEntityException())
/* 1919 */         { if (valueType.isCORBAUserException()) {
/* 1920 */             paramIndentingWriter.p("::org::omg::CORBA::UserEx");
/*      */           } else {
/* 1922 */             String[] arrayOfString1 = getIDLModuleNames(valueType);
/* 1923 */             for (byte b1 = 0; b1 < arrayOfString1.length; b1++)
/* 1924 */               paramIndentingWriter.p("::" + arrayOfString1[b1]);
/* 1925 */             paramIndentingWriter.p("::" + valueType.getName());
/*      */           }  }
/* 1927 */         else { paramIndentingWriter.p(valueType.getQualifiedIDLExceptionName(true)); }
/* 1928 */          b++;
/*      */       }
/* 1930 */       paramIndentingWriter.pO();
/* 1931 */       paramIndentingWriter.p(" )");
/*      */     }
/*      */
/* 1934 */     paramIndentingWriter.pln(";");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected String unEsc(String paramString) {
/* 1945 */     if (paramString.startsWith("_")) return paramString.substring(1);
/* 1946 */     return paramString;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeBanner(Type paramType, int paramInt, boolean paramBoolean, IndentingWriter paramIndentingWriter) throws IOException {
/* 1963 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 1964 */     String str1 = unEsc(paramType.getIDLName());
/* 1965 */     if (paramBoolean && paramType.isClass()) {
/* 1966 */       ClassType classType = (ClassType)paramType;
/* 1967 */       str1 = unEsc(classType.getIDLExceptionName());
/*      */     }
/* 1969 */     if (paramInt > 0 && paramType.isArray()) {
/* 1970 */       Type type = paramType.getElementType();
/* 1971 */       str1 = "seq" + paramInt + "_" + unEsc(type.getIDLName().replace(' ', '_'));
/*      */     }
/*      */
/* 1974 */     paramIndentingWriter.pln("/**");
/* 1975 */     paramIndentingWriter.p(" * ");
/* 1976 */     for (byte b = 0; b < arrayOfString.length; b++)
/* 1977 */       paramIndentingWriter.p(arrayOfString[b] + "/");
/* 1978 */     paramIndentingWriter.pln(str1 + ".idl");
/* 1979 */     paramIndentingWriter.pln(" * Generated by rmic -idl. Do not edit");
/*      */
/*      */
/* 1982 */     String str2 = DateFormat.getDateTimeInstance(0, 0, Locale.getDefault()).format(new Date());
/* 1983 */     String str3 = "o'clock";
/* 1984 */     int i = str2.indexOf(str3);
/* 1985 */     paramIndentingWriter.p(" * ");
/* 1986 */     if (i > -1)
/* 1987 */     { paramIndentingWriter.pln(str2.substring(0, i) + str2.substring(i + str3.length())); }
/* 1988 */     else { paramIndentingWriter.pln(str2); }
/* 1989 */      paramIndentingWriter.pln(" */");
/* 1990 */     paramIndentingWriter.pln();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeIncOrb(IndentingWriter paramIndentingWriter) throws IOException {
/* 2001 */     paramIndentingWriter.pln("#include \"orb.idl\"");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeIfndef(Type paramType, int paramInt, boolean paramBoolean1, boolean paramBoolean2, IndentingWriter paramIndentingWriter) throws IOException {
/* 2020 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 2021 */     String str = unEsc(paramType.getIDLName());
/* 2022 */     if (paramBoolean1 && paramType.isClass()) {
/* 2023 */       ClassType classType = (ClassType)paramType;
/* 2024 */       str = unEsc(classType.getIDLExceptionName());
/*      */     }
/* 2026 */     if (paramInt > 0 && paramType.isArray()) {
/* 2027 */       Type type = paramType.getElementType();
/* 2028 */       str = "seq" + paramInt + "_" + unEsc(type.getIDLName().replace(' ', '_'));
/*      */     }
/* 2030 */     paramIndentingWriter.pln();
/* 2031 */     paramIndentingWriter.p("#ifndef __"); byte b;
/* 2032 */     for (b = 0; b < arrayOfString.length; ) { paramIndentingWriter.p(arrayOfString[b] + "_"); b++; }
/* 2033 */      paramIndentingWriter.pln(str + "__");
/* 2034 */     if (!paramBoolean2) {
/* 2035 */       paramIndentingWriter.p("#define __");
/* 2036 */       for (b = 0; b < arrayOfString.length; ) { paramIndentingWriter.p(arrayOfString[b] + "_"); b++; }
/* 2037 */        paramIndentingWriter.pln(str + "__");
/* 2038 */       paramIndentingWriter.pln();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeEndif(IndentingWriter paramIndentingWriter) throws IOException {
/* 2050 */     paramIndentingWriter.pln("#endif");
/* 2051 */     paramIndentingWriter.pln();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeModule1(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/* 2064 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 2065 */     paramIndentingWriter.pln();
/* 2066 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 2067 */       paramIndentingWriter.pln("module " + arrayOfString[b] + " {");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeModule2(Type paramType, IndentingWriter paramIndentingWriter) throws IOException {
/* 2079 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 2080 */     for (byte b = 0; b < arrayOfString.length; ) { paramIndentingWriter.pln("};"); b++; }
/* 2081 */      paramIndentingWriter.pln();
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\IDLGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
