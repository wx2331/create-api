/*      */ package sun.rmi.rmic.iiop;
/*      */
/*      */ import com.sun.corba.se.impl.util.PackagePrefixChecker;
/*      */ import com.sun.corba.se.impl.util.Utility;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import sun.rmi.rmic.BatchEnvironment;
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class StubGenerator
/*      */   extends Generator
/*      */ {
/*      */   private static final String DEFAULT_STUB_CLASS = "javax.rmi.CORBA.Stub";
/*      */   private static final String DEFAULT_TIE_CLASS = "org.omg.CORBA_2_3.portable.ObjectImpl";
/*      */   private static final String DEFAULT_POA_TIE_CLASS = "org.omg.PortableServer.Servant";
/*      */   protected boolean reverseIDs = false;
/*      */   protected boolean localStubs = true;
/*      */   protected boolean standardPackage = false;
/*      */   protected boolean useHash = true;
/*   74 */   protected String stubBaseClass = "javax.rmi.CORBA.Stub";
/*   75 */   protected String tieBaseClass = "org.omg.CORBA_2_3.portable.ObjectImpl";
/*   76 */   protected HashSet namesInUse = new HashSet();
/*   77 */   protected Hashtable classesInUse = new Hashtable<>();
/*   78 */   protected Hashtable imports = new Hashtable<>();
/*   79 */   protected int importCount = 0;
/*   80 */   protected String currentPackage = null;
/*   81 */   protected String currentClass = null;
/*      */   protected boolean castArray = false;
/*   83 */   protected Hashtable transactionalObjects = new Hashtable<>();
/*      */
/*      */
/*      */
/*      */
/*      */   protected boolean POATie = false;
/*      */
/*      */
/*      */
/*      */
/*      */   protected boolean emitPermissionCheck = false;
/*      */
/*      */
/*      */
/*      */
/*      */   public void generate(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile) {
/*   99 */     ((BatchEnvironment)paramBatchEnvironment)
/*  100 */       .setStandardPackage(this.standardPackage);
/*  101 */     super.generate(paramBatchEnvironment, paramClassDefinition, paramFile);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected boolean requireNewInstance() {
/*  111 */     return false;
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
/*      */   protected boolean parseNonConforming(ContextStack paramContextStack) {
/*  124 */     return paramContextStack.getEnv().getParseNonConforming();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected CompoundType getTopType(ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/*      */     ImplementationType implementationType;
/*  135 */     AbstractType abstractType = null;
/*      */
/*      */
/*      */
/*  139 */     if (paramClassDefinition.isInterface()) {
/*      */
/*      */
/*      */
/*  143 */       abstractType = AbstractType.forAbstract(paramClassDefinition, paramContextStack, true);
/*      */
/*  145 */       if (abstractType == null)
/*      */       {
/*      */
/*      */
/*  149 */         RemoteType remoteType = RemoteType.forRemote(paramClassDefinition, paramContextStack, false);
/*      */
/*      */       }
/*      */     }
/*      */     else {
/*      */
/*  155 */       implementationType = ImplementationType.forImplementation(paramClassDefinition, paramContextStack, false);
/*      */     }
/*      */
/*  158 */     return implementationType;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain) {
/*  169 */     Object object = new Object();
/*      */
/*      */
/*      */
/*  173 */     this.reverseIDs = false;
/*  174 */     this.localStubs = true;
/*  175 */     this.useHash = true;
/*  176 */     this.stubBaseClass = "javax.rmi.CORBA.Stub";
/*      */
/*  178 */     this.transactionalObjects = new Hashtable<>();
/*      */
/*      */
/*      */
/*  182 */     boolean bool = super.parseArgs(paramArrayOfString, paramMain);
/*  183 */     if (bool) {
/*  184 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  185 */         if (paramArrayOfString[b] != null) {
/*  186 */           String str = paramArrayOfString[b].toLowerCase();
/*  187 */           if (str.equals("-iiop")) {
/*  188 */             paramArrayOfString[b] = null;
/*  189 */           } else if (str.equals("-xreverseids")) {
/*  190 */             this.reverseIDs = true;
/*  191 */             paramArrayOfString[b] = null;
/*  192 */           } else if (str.equals("-nolocalstubs")) {
/*  193 */             this.localStubs = false;
/*  194 */             paramArrayOfString[b] = null;
/*  195 */           } else if (str.equals("-xnohash")) {
/*  196 */             this.useHash = false;
/*  197 */             paramArrayOfString[b] = null;
/*  198 */           } else if (paramArrayOfString[b].equals("-standardPackage")) {
/*  199 */             this.standardPackage = true;
/*  200 */             paramArrayOfString[b] = null;
/*  201 */           } else if (paramArrayOfString[b].equals("-emitPermissionCheck")) {
/*  202 */             this.emitPermissionCheck = true;
/*  203 */             paramArrayOfString[b] = null;
/*  204 */           } else if (str.equals("-xstubbase")) {
/*  205 */             paramArrayOfString[b] = null;
/*  206 */             if (++b < paramArrayOfString.length && paramArrayOfString[b] != null && !paramArrayOfString[b].startsWith("-")) {
/*  207 */               this.stubBaseClass = paramArrayOfString[b];
/*  208 */               paramArrayOfString[b] = null;
/*      */             } else {
/*  210 */               paramMain.error("rmic.option.requires.argument", "-Xstubbase");
/*  211 */               bool = false;
/*      */             }
/*  213 */           } else if (str.equals("-xtiebase")) {
/*  214 */             paramArrayOfString[b] = null;
/*  215 */             if (++b < paramArrayOfString.length && paramArrayOfString[b] != null && !paramArrayOfString[b].startsWith("-")) {
/*  216 */               this.tieBaseClass = paramArrayOfString[b];
/*  217 */               paramArrayOfString[b] = null;
/*      */             } else {
/*  219 */               paramMain.error("rmic.option.requires.argument", "-Xtiebase");
/*  220 */               bool = false;
/*      */             }
/*  222 */           } else if (str.equals("-transactional")) {
/*      */
/*      */
/*      */
/*  226 */             for (int i = b + 1; i < paramArrayOfString.length; i++) {
/*  227 */               if (paramArrayOfString[i].charAt(1) != '-') {
/*  228 */                 this.transactionalObjects.put(paramArrayOfString[i], object);
/*      */                 break;
/*      */               }
/*      */             }
/*  232 */             paramArrayOfString[b] = null;
/*  233 */           } else if (str.equals("-poa")) {
/*  234 */             this.POATie = true;
/*  235 */             paramArrayOfString[b] = null;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  240 */     if (this.POATie) {
/*  241 */       this.tieBaseClass = "org.omg.PortableServer.Servant";
/*      */     } else {
/*  243 */       this.tieBaseClass = "org.omg.CORBA_2_3.portable.ObjectImpl";
/*      */     }
/*  245 */     return bool;
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
/*      */   protected OutputType[] getOutputTypesFor(CompoundType paramCompoundType, HashSet paramHashSet) {
/*  267 */     int i = 69632;
/*  268 */     Type[] arrayOfType = paramCompoundType.collectMatching(i, paramHashSet);
/*  269 */     int j = arrayOfType.length;
/*  270 */     Vector<OutputType> vector = new Vector(j + 5);
/*  271 */     BatchEnvironment batchEnvironment = paramCompoundType.getEnv();
/*      */
/*      */
/*      */
/*  275 */     for (byte b = 0; b < arrayOfType.length; b++) {
/*      */
/*  277 */       Type type = arrayOfType[b];
/*  278 */       String str = type.getName();
/*  279 */       boolean bool = true;
/*      */
/*      */
/*      */
/*  283 */       if (type instanceof ImplementationType) {
/*      */
/*      */
/*      */
/*  287 */         vector.addElement(new OutputType(this, Utility.tieNameForCompiler(str), type));
/*      */
/*      */
/*      */
/*      */
/*  292 */         byte b1 = 0;
/*  293 */         InterfaceType[] arrayOfInterfaceType = ((CompoundType)type).getInterfaces();
/*  294 */         for (byte b2 = 0; b2 < arrayOfInterfaceType.length; b2++) {
/*  295 */           if (arrayOfInterfaceType[b2].isType(4096) &&
/*  296 */             !arrayOfInterfaceType[b2].isType(8192)) {
/*  297 */             b1++;
/*      */           }
/*      */         }
/*      */
/*  301 */         if (b1 <= 1)
/*      */         {
/*      */
/*      */
/*  305 */           bool = false;
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*  311 */       if (type instanceof AbstractType)
/*      */       {
/*      */
/*      */
/*  315 */         bool = false;
/*      */       }
/*      */
/*  318 */       if (bool)
/*      */       {
/*      */
/*      */
/*  322 */         vector.addElement(new OutputType(this, Utility.stubNameForCompiler(str), type));
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*  328 */     OutputType[] arrayOfOutputType = new OutputType[vector.size()];
/*  329 */     vector.copyInto((Object[])arrayOfOutputType);
/*  330 */     return arrayOfOutputType;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected String getFileNameExtensionFor(OutputType paramOutputType) {
/*  341 */     return ".java";
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
/*      */   protected void writeOutputFor(OutputType paramOutputType, HashSet paramHashSet, IndentingWriter paramIndentingWriter) throws IOException {
/*  355 */     String str = paramOutputType.getName();
/*  356 */     CompoundType compoundType = (CompoundType)paramOutputType.getType();
/*      */
/*      */
/*      */
/*  360 */     if (str.endsWith("_Stub")) {
/*      */
/*      */
/*      */
/*  364 */       writeStub(paramOutputType, paramIndentingWriter);
/*      */
/*      */     }
/*      */     else {
/*      */
/*      */
/*  370 */       writeTie(paramOutputType, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeStub(OutputType paramOutputType, IndentingWriter paramIndentingWriter) throws IOException {
/*  380 */     CompoundType compoundType = (CompoundType)paramOutputType.getType();
/*  381 */     RemoteType[] arrayOfRemoteType = getDirectRemoteInterfaces(compoundType);
/*      */
/*      */
/*      */
/*  385 */     paramIndentingWriter.pln("// Stub class generated by rmic, do not edit.");
/*  386 */     paramIndentingWriter.pln("// Contents subject to change without notice.");
/*  387 */     paramIndentingWriter.pln();
/*      */
/*      */
/*      */
/*  391 */     setStandardClassesInUse(compoundType, true);
/*      */
/*      */
/*      */
/*  395 */     addClassesInUse(compoundType, arrayOfRemoteType);
/*      */
/*      */
/*      */
/*  399 */     writePackageAndImports(paramIndentingWriter);
/*      */
/*      */
/*      */
/*      */
/*      */
/*  405 */     if (this.emitPermissionCheck) {
/*  406 */       paramIndentingWriter.pln("import java.security.AccessController;");
/*  407 */       paramIndentingWriter.pln("import java.security.PrivilegedAction;");
/*  408 */       paramIndentingWriter.pln("import java.io.SerializablePermission;");
/*  409 */       paramIndentingWriter.pln();
/*  410 */       paramIndentingWriter.pln();
/*      */     }
/*      */
/*      */
/*      */
/*  415 */     paramIndentingWriter.p("public class " + this.currentClass);
/*      */
/*  417 */     paramIndentingWriter.p(" extends " + getName(this.stubBaseClass));
/*  418 */     paramIndentingWriter.p(" implements ");
/*  419 */     if (arrayOfRemoteType.length > 0) {
/*  420 */       for (byte b = 0; b < arrayOfRemoteType.length; b++) {
/*  421 */         if (b > 0) {
/*  422 */           paramIndentingWriter.pln(",");
/*      */         }
/*  424 */         String str = testUtil(getName(arrayOfRemoteType[b]), compoundType);
/*  425 */         paramIndentingWriter.p(str);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*  433 */     if (!implementsRemote(compoundType)) {
/*  434 */       paramIndentingWriter.pln(",");
/*  435 */       paramIndentingWriter.p(getName("java.rmi.Remote"));
/*      */     }
/*      */
/*  438 */     paramIndentingWriter.plnI(" {");
/*  439 */     paramIndentingWriter.pln();
/*      */
/*      */
/*      */
/*  443 */     writeIds(paramIndentingWriter, compoundType, false);
/*  444 */     paramIndentingWriter.pln();
/*      */
/*  446 */     if (this.emitPermissionCheck) {
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
/*  477 */       paramIndentingWriter.pln();
/*  478 */       paramIndentingWriter.plnI("private transient boolean _instantiated = false;");
/*  479 */       paramIndentingWriter.pln();
/*  480 */       paramIndentingWriter.pO();
/*  481 */       paramIndentingWriter.plnI("private static Void checkPermission() {");
/*  482 */       paramIndentingWriter.plnI("SecurityManager sm = System.getSecurityManager();");
/*  483 */       paramIndentingWriter.pln("if (sm != null) {");
/*  484 */       paramIndentingWriter.pI();
/*  485 */       paramIndentingWriter.plnI("sm.checkPermission(new SerializablePermission(");
/*  486 */       paramIndentingWriter.plnI("\"enableSubclassImplementation\"));");
/*  487 */       paramIndentingWriter.pO();
/*  488 */       paramIndentingWriter.pO();
/*  489 */       paramIndentingWriter.pOln("}");
/*  490 */       paramIndentingWriter.pln("return null;");
/*  491 */       paramIndentingWriter.pO();
/*  492 */       paramIndentingWriter.pOln("}");
/*  493 */       paramIndentingWriter.pln();
/*  494 */       paramIndentingWriter.pO();
/*      */
/*  496 */       paramIndentingWriter.pI();
/*  497 */       paramIndentingWriter.plnI("private " + this.currentClass + "(Void ignore) {  }");
/*  498 */       paramIndentingWriter.pln();
/*  499 */       paramIndentingWriter.pO();
/*      */
/*  501 */       paramIndentingWriter.plnI("public " + this.currentClass + "() {");
/*  502 */       paramIndentingWriter.pln("this(checkPermission());");
/*  503 */       paramIndentingWriter.pln("_instantiated = true;");
/*  504 */       paramIndentingWriter.pOln("}");
/*  505 */       paramIndentingWriter.pln();
/*  506 */       paramIndentingWriter.plnI("private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {");
/*  507 */       paramIndentingWriter.plnI("checkPermission();");
/*  508 */       paramIndentingWriter.pO();
/*  509 */       paramIndentingWriter.pln("s.defaultReadObject();");
/*  510 */       paramIndentingWriter.pln("_instantiated = true;");
/*  511 */       paramIndentingWriter.pOln("}");
/*  512 */       paramIndentingWriter.pln();
/*      */     }
/*      */
/*      */
/*  516 */     if (!this.emitPermissionCheck) {
/*  517 */       paramIndentingWriter.pI();
/*      */     }
/*      */
/*      */
/*      */
/*  522 */     paramIndentingWriter.plnI("public String[] _ids() { ");
/*  523 */     paramIndentingWriter.pln("return (String[]) _type_ids.clone();");
/*  524 */     paramIndentingWriter.pOln("}");
/*      */
/*      */
/*      */
/*  528 */     CompoundType.Method[] arrayOfMethod = compoundType.getMethods();
/*  529 */     int i = arrayOfMethod.length;
/*  530 */     if (i > 0) {
/*  531 */       boolean bool = true;
/*  532 */       for (byte b = 0; b < i; b++) {
/*  533 */         if (!arrayOfMethod[b].isConstructor()) {
/*  534 */           if (bool) {
/*  535 */             bool = false;
/*      */           }
/*  537 */           paramIndentingWriter.pln();
/*  538 */           writeStubMethod(paramIndentingWriter, arrayOfMethod[b], compoundType);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*  545 */     writeCastArray(paramIndentingWriter);
/*      */
/*  547 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */
/*      */   void addClassInUse(String paramString) {
/*  551 */     String str1 = paramString;
/*  552 */     String str2 = null;
/*  553 */     int i = paramString.lastIndexOf('.');
/*  554 */     if (i > 0) {
/*  555 */       str1 = paramString.substring(i + 1);
/*  556 */       str2 = paramString.substring(0, i);
/*      */     }
/*  558 */     addClassInUse(str1, paramString, str2);
/*      */   }
/*      */
/*      */   void addClassInUse(Type paramType) {
/*  562 */     if (!paramType.isPrimitive()) {
/*  563 */       String str3; Identifier identifier = paramType.getIdentifier();
/*  564 */       String str1 = IDLNames.replace(identifier.getName().toString(), ". ", ".");
/*  565 */       String str2 = paramType.getPackageName();
/*      */
/*  567 */       if (str2 != null) {
/*  568 */         str3 = str2 + "." + str1;
/*      */       } else {
/*  570 */         str3 = str1;
/*      */       }
/*  572 */       addClassInUse(str1, str3, str2);
/*      */     }
/*      */   }
/*      */
/*      */   void addClassInUse(Type[] paramArrayOfType) {
/*  577 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/*  578 */       addClassInUse(paramArrayOfType[b]);
/*      */     }
/*      */   }
/*      */
/*      */   void addStubInUse(Type paramType) {
/*  583 */     if (paramType.getIdentifier() != idCorbaObject && paramType
/*  584 */       .isType(2048)) {
/*  585 */       String str3, str1 = getStubNameFor(paramType, false);
/*  586 */       String str2 = paramType.getPackageName();
/*      */
/*  588 */       if (str2 == null) {
/*  589 */         str3 = str1;
/*      */       } else {
/*  591 */         str3 = str2 + "." + str1;
/*      */       }
/*  593 */       addClassInUse(str1, str3, str2);
/*      */     }
/*  595 */     if (paramType.isType(4096) || paramType
/*  596 */       .isType(524288)) {
/*  597 */       addClassInUse("javax.rmi.PortableRemoteObject");
/*      */     }
/*      */   }
/*      */
/*      */   String getStubNameFor(Type paramType, boolean paramBoolean) {
/*      */     String str1;
/*      */     String str2;
/*  604 */     if (paramBoolean) {
/*  605 */       str2 = paramType.getQualifiedName();
/*      */     } else {
/*  607 */       str2 = paramType.getName();
/*      */     }
/*  609 */     if (((CompoundType)paramType).isCORBAObject()) {
/*  610 */       str1 = Utility.idlStubName(str2);
/*      */     } else {
/*  612 */       str1 = Utility.stubNameForCompiler(str2);
/*      */     }
/*  614 */     return str1;
/*      */   }
/*      */
/*      */   void addStubInUse(Type[] paramArrayOfType) {
/*  618 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/*  619 */       addStubInUse(paramArrayOfType[b]);
/*      */     }
/*      */   }
/*      */
/*  623 */   private static final String NO_IMPORT = new String();
/*      */
/*      */   static final String SINGLE_SLASH = "\\";
/*      */   static final String DOUBLE_SLASH = "\\\\";
/*      */
/*      */   void addClassInUse(String paramString1, String paramString2, String paramString3) {
/*  629 */     String str = (String)this.classesInUse.get(paramString2);
/*      */
/*  631 */     if (str == null) {
/*      */
/*      */
/*      */
/*      */
/*  636 */       String str1 = (String)this.imports.get(paramString1);
/*  637 */       String str2 = null;
/*      */
/*  639 */       if (paramString3 == null) {
/*      */
/*      */
/*      */
/*  643 */         str2 = paramString1;
/*      */       }
/*  645 */       else if (paramString3.equals("java.lang")) {
/*      */
/*      */
/*      */
/*  649 */         str2 = paramString1;
/*      */
/*      */
/*      */
/*  653 */         if (str2.endsWith("_Stub")) str2 = Util.packagePrefix() + paramString2;
/*      */
/*  655 */       } else if (this.currentPackage != null && paramString3.equals(this.currentPackage)) {
/*      */
/*      */
/*      */
/*  659 */         str2 = paramString1;
/*      */
/*      */
/*      */
/*      */
/*  664 */         if (str1 != null)
/*      */         {
/*      */
/*      */
/*  668 */           str2 = paramString2;
/*      */
/*      */         }
/*      */       }
/*  672 */       else if (str1 != null) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  679 */         str2 = paramString2;
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
/*      */       }
/*  699 */       else if (paramString2.equals("org.omg.CORBA.Object")) {
/*      */
/*      */
/*      */
/*  703 */         str2 = paramString2;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       }
/*  712 */       else if (paramString1.indexOf('.') != -1) {
/*  713 */         str2 = paramString2;
/*      */       } else {
/*  715 */         str2 = paramString1;
/*  716 */         this.imports.put(paramString1, paramString2);
/*  717 */         this.importCount++;
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*  723 */       this.classesInUse.put(paramString2, str2);
/*      */     }
/*      */   }
/*      */
/*      */   String getName(Type paramType) {
/*  728 */     if (paramType.isPrimitive()) {
/*  729 */       return paramType.getName() + paramType.getArrayBrackets();
/*      */     }
/*  731 */     Identifier identifier = paramType.getIdentifier();
/*  732 */     String str = IDLNames.replace(identifier.toString(), ". ", ".");
/*  733 */     return getName(str) + paramType.getArrayBrackets();
/*      */   }
/*      */
/*      */
/*      */   String getExceptionName(Type paramType) {
/*  738 */     Identifier identifier = paramType.getIdentifier();
/*  739 */     return IDLNames.replace(identifier.toString(), ". ", ".");
/*      */   }
/*      */
/*      */   String getName(String paramString) {
/*  743 */     return (String)this.classesInUse.get(paramString);
/*      */   }
/*      */
/*      */   String getName(Identifier paramIdentifier) {
/*  747 */     return getName(paramIdentifier.toString());
/*      */   }
/*      */
/*      */   String getStubName(Type paramType) {
/*  751 */     String str = getStubNameFor(paramType, true);
/*  752 */     return getName(str);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void setStandardClassesInUse(CompoundType paramCompoundType, boolean paramBoolean) throws IOException {
/*  760 */     this.currentPackage = paramCompoundType.getPackageName();
/*  761 */     this.imports.clear();
/*  762 */     this.classesInUse.clear();
/*  763 */     this.namesInUse.clear();
/*  764 */     this.importCount = 0;
/*  765 */     this.castArray = false;
/*      */
/*      */
/*      */
/*  769 */     addClassInUse(paramCompoundType);
/*      */
/*      */
/*      */
/*  773 */     if (paramBoolean) {
/*  774 */       this.currentClass = Utility.stubNameForCompiler(paramCompoundType.getName());
/*      */     } else {
/*  776 */       this.currentClass = Utility.tieNameForCompiler(paramCompoundType.getName());
/*      */     }
/*      */
/*      */
/*      */
/*  781 */     if (this.currentPackage == null) {
/*  782 */       addClassInUse(this.currentClass, this.currentClass, this.currentPackage);
/*      */     } else {
/*  784 */       addClassInUse(this.currentClass, this.currentPackage + "." + this.currentClass, this.currentPackage);
/*      */     }
/*      */
/*      */
/*      */
/*  789 */     addClassInUse("javax.rmi.CORBA.Util");
/*  790 */     addClassInUse(idRemote.toString());
/*  791 */     addClassInUse(idRemoteException.toString());
/*  792 */     addClassInUse(idOutputStream.toString());
/*  793 */     addClassInUse(idInputStream.toString());
/*  794 */     addClassInUse(idSystemException.toString());
/*  795 */     addClassInUse(idJavaIoSerializable.toString());
/*  796 */     addClassInUse(idCorbaORB.toString());
/*  797 */     addClassInUse(idReplyHandler.toString());
/*      */
/*      */
/*      */
/*  801 */     if (paramBoolean) {
/*  802 */       addClassInUse(this.stubBaseClass);
/*  803 */       addClassInUse("java.rmi.UnexpectedException");
/*  804 */       addClassInUse(idRemarshalException.toString());
/*  805 */       addClassInUse(idApplicationException.toString());
/*  806 */       if (this.localStubs) {
/*  807 */         addClassInUse("org.omg.CORBA.portable.ServantObject");
/*      */       }
/*      */     } else {
/*  810 */       addClassInUse(paramCompoundType);
/*  811 */       addClassInUse(this.tieBaseClass);
/*  812 */       addClassInUse(idTieInterface.toString());
/*  813 */       addClassInUse(idBadMethodException.toString());
/*  814 */       addClassInUse(idPortableUnknownException.toString());
/*  815 */       addClassInUse(idJavaLangThrowable.toString());
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   void addClassesInUse(CompoundType paramCompoundType, RemoteType[] paramArrayOfRemoteType) {
/*  823 */     CompoundType.Method[] arrayOfMethod = paramCompoundType.getMethods();
/*  824 */     for (byte b = 0; b < arrayOfMethod.length; b++) {
/*  825 */       addClassInUse(arrayOfMethod[b].getReturnType());
/*  826 */       addStubInUse(arrayOfMethod[b].getReturnType());
/*  827 */       addClassInUse(arrayOfMethod[b].getArguments());
/*  828 */       addStubInUse(arrayOfMethod[b].getArguments());
/*  829 */       addClassInUse((Type[])arrayOfMethod[b].getExceptions());
/*      */
/*  831 */       addClassInUse((Type[])arrayOfMethod[b].getImplExceptions());
/*      */     }
/*      */
/*      */
/*      */
/*  836 */     if (paramArrayOfRemoteType != null) {
/*  837 */       addClassInUse((Type[])paramArrayOfRemoteType);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   void writePackageAndImports(IndentingWriter paramIndentingWriter) throws IOException {
/*  845 */     if (this.currentPackage != null) {
/*  846 */       paramIndentingWriter.pln("package " +
/*  847 */           Util.correctPackageName(this.currentPackage, false, this.standardPackage) + ";");
/*      */
/*      */
/*  850 */       paramIndentingWriter.pln();
/*      */     }
/*      */
/*      */
/*      */
/*  855 */     String[] arrayOfString = new String[this.importCount];
/*  856 */     byte b1 = 0;
/*  857 */     for (Enumeration<String> enumeration = this.imports.elements(); enumeration.hasMoreElements(); ) {
/*  858 */       String str = enumeration.nextElement();
/*  859 */       if (str != NO_IMPORT) {
/*  860 */         arrayOfString[b1++] = str;
/*      */       }
/*      */     }
/*      */
/*  864 */     Arrays.sort(arrayOfString, new StringComparator());
/*      */
/*      */
/*      */
/*  868 */     for (byte b2 = 0; b2 < this.importCount; b2++) {
/*      */
/*  870 */       if (Util.isOffendingPackage(arrayOfString[b2]) && arrayOfString[b2]
/*  871 */         .endsWith("_Stub") &&
/*  872 */         String.valueOf(arrayOfString[b2].charAt(arrayOfString[b2].lastIndexOf(".") + 1)).equals("_")) {
/*      */
/*  874 */         paramIndentingWriter.pln("import " + PackagePrefixChecker.packagePrefix() + arrayOfString[b2] + ";");
/*      */       } else {
/*  876 */         paramIndentingWriter.pln("import " + arrayOfString[b2] + ";");
/*      */       }
/*      */     }
/*  879 */     paramIndentingWriter.pln();
/*      */
/*      */
/*  882 */     if (this.currentPackage != null && Util.isOffendingPackage(this.currentPackage)) {
/*  883 */       paramIndentingWriter.pln("import " + this.currentPackage + ".*  ;");
/*      */     }
/*  885 */     paramIndentingWriter.pln();
/*      */   }
/*      */
/*      */
/*      */   boolean implementsRemote(CompoundType paramCompoundType) {
/*  890 */     boolean bool = (paramCompoundType.isType(4096) && !paramCompoundType.isType(8192));
/*      */
/*      */
/*      */
/*      */
/*  895 */     if (!bool) {
/*  896 */       InterfaceType[] arrayOfInterfaceType = paramCompoundType.getInterfaces();
/*  897 */       for (byte b = 0; b < arrayOfInterfaceType.length; b++) {
/*  898 */         bool = implementsRemote(arrayOfInterfaceType[b]);
/*  899 */         if (bool) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */
/*  905 */     return bool;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void writeStubMethod(IndentingWriter paramIndentingWriter, CompoundType.Method paramMethod, CompoundType paramCompoundType) throws IOException {
/*  913 */     String str1 = paramMethod.getName();
/*  914 */     String str2 = paramMethod.getIDLName();
/*      */
/*  916 */     Type[] arrayOfType = paramMethod.getArguments();
/*  917 */     String[] arrayOfString = paramMethod.getArgumentNames();
/*  918 */     Type type = paramMethod.getReturnType();
/*  919 */     ValueType[] arrayOfValueType = getStubExceptions(paramMethod, false);
/*  920 */     boolean bool = false;
/*      */
/*  922 */     addNamesInUse(paramMethod);
/*  923 */     addNameInUse("_type_ids");
/*      */
/*  925 */     String str3 = testUtil(getName(type), type);
/*  926 */     paramIndentingWriter.p("public " + str3 + " " + str1 + "("); byte b;
/*  927 */     for (b = 0; b < arrayOfType.length; b++) {
/*  928 */       if (b > 0)
/*  929 */         paramIndentingWriter.p(", ");
/*  930 */       paramIndentingWriter.p(getName(arrayOfType[b]) + " " + arrayOfString[b]);
/*      */     }
/*      */
/*  933 */     paramIndentingWriter.p(")");
/*  934 */     if (arrayOfValueType.length > 0) {
/*  935 */       paramIndentingWriter.p(" throws ");
/*  936 */       for (b = 0; b < arrayOfValueType.length; b++) {
/*  937 */         if (b > 0) {
/*  938 */           paramIndentingWriter.p(", ");
/*      */         }
/*      */
/*  941 */         paramIndentingWriter.p(getExceptionName(arrayOfValueType[b]));
/*      */       }
/*      */     }
/*      */
/*  945 */     paramIndentingWriter.plnI(" {");
/*      */
/*      */
/*  948 */     if (this.emitPermissionCheck) {
/*  949 */       paramIndentingWriter.pln("if ((System.getSecurityManager() != null) && (!_instantiated)) {");
/*  950 */       paramIndentingWriter.plnI("    throw new java.io.IOError(new java.io.IOException(\"InvalidObject \"));");
/*  951 */       paramIndentingWriter.pOln("}");
/*  952 */       paramIndentingWriter.pln();
/*      */     }
/*      */
/*      */
/*  956 */     if (this.localStubs) {
/*  957 */       writeLocalStubMethodBody(paramIndentingWriter, paramMethod, paramCompoundType);
/*      */     } else {
/*  959 */       writeNonLocalStubMethodBody(paramIndentingWriter, paramMethod, paramCompoundType);
/*      */     }
/*      */
/*      */
/*      */
/*  964 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void writeLocalStubMethodBody(IndentingWriter paramIndentingWriter, CompoundType.Method paramMethod, CompoundType paramCompoundType) throws IOException {
/*  973 */     String[] arrayOfString1 = paramMethod.getArgumentNames();
/*  974 */     Type type = paramMethod.getReturnType();
/*  975 */     ValueType[] arrayOfValueType = getStubExceptions(paramMethod, false);
/*  976 */     String str2 = paramMethod.getName();
/*  977 */     String str3 = paramMethod.getIDLName();
/*      */
/*  979 */     paramIndentingWriter.plnI("if (!Util.isLocal(this)) {");
/*  980 */     writeNonLocalStubMethodBody(paramIndentingWriter, paramMethod, paramCompoundType);
/*  981 */     paramIndentingWriter.pOlnI("} else {");
/*  982 */     String str4 = getVariableName("so");
/*      */
/*  984 */     paramIndentingWriter.pln("ServantObject " + str4 + " = _servant_preinvoke(\"" + str3 + "\"," + getName(paramCompoundType) + ".class);");
/*  985 */     paramIndentingWriter.plnI("if (" + str4 + " == null) {");
/*  986 */     if (!type.isType(1)) {
/*  987 */       paramIndentingWriter.p("return ");
/*      */     }
/*  989 */     paramIndentingWriter.p(str2 + "(");
/*  990 */     for (byte b1 = 0; b1 < arrayOfString1.length; b1++) {
/*  991 */       if (b1 > 0)
/*  992 */         paramIndentingWriter.p(", ");
/*  993 */       paramIndentingWriter.p(arrayOfString1[b1]);
/*      */     }
/*  995 */     paramIndentingWriter.pln(");");
/*  996 */     if (type.isType(1)) {
/*  997 */       paramIndentingWriter.pln("return ;");
/*      */     }
/*      */
/* 1000 */     paramIndentingWriter.pOln("}");
/* 1001 */     paramIndentingWriter.plnI("try {");
/*      */
/*      */
/*      */
/*      */
/* 1006 */     String[] arrayOfString2 = writeCopyArguments(paramMethod, paramIndentingWriter);
/*      */
/*      */
/*      */
/* 1010 */     boolean bool = mustCopy(type);
/* 1011 */     String str5 = null;
/* 1012 */     if (!type.isType(1)) {
/* 1013 */       if (bool) {
/* 1014 */         str5 = getVariableName("result");
/* 1015 */         String str = testUtil(getName(type), type);
/* 1016 */         paramIndentingWriter.p(str + " " + str5 + " = ");
/*      */       } else {
/* 1018 */         paramIndentingWriter.p("return ");
/*      */       }
/*      */     }
/* 1021 */     String str1 = testUtil(getName(paramCompoundType), paramCompoundType);
/* 1022 */     paramIndentingWriter.p("((" + str1 + ")" + str4 + ".servant)." + str2 + "(");
/*      */
/* 1024 */     for (byte b2 = 0; b2 < arrayOfString2.length; b2++) {
/* 1025 */       if (b2 > 0)
/* 1026 */         paramIndentingWriter.p(", ");
/* 1027 */       paramIndentingWriter.p(arrayOfString2[b2]);
/*      */     }
/*      */
/* 1030 */     if (bool) {
/* 1031 */       paramIndentingWriter.pln(");");
/* 1032 */       str1 = testUtil(getName(type), type);
/* 1033 */       paramIndentingWriter.pln("return (" + str1 + ")Util.copyObject(" + str5 + ",_orb());");
/*      */     } else {
/* 1035 */       paramIndentingWriter.pln(");");
/*      */     }
/*      */
/* 1038 */     String str6 = getVariableName("ex");
/* 1039 */     String str7 = getVariableName("exCopy");
/* 1040 */     paramIndentingWriter.pOlnI("} catch (Throwable " + str6 + ") {");
/*      */
/* 1042 */     paramIndentingWriter.pln("Throwable " + str7 + " = (Throwable)Util.copyObject(" + str6 + ",_orb());");
/* 1043 */     for (byte b3 = 0; b3 < arrayOfValueType.length; b3++) {
/* 1044 */       if (arrayOfValueType[b3].getIdentifier() != idRemoteException && arrayOfValueType[b3]
/* 1045 */         .isType(32768)) {
/*      */
/* 1047 */         paramIndentingWriter.plnI("if (" + str7 + " instanceof " + getExceptionName(arrayOfValueType[b3]) + ") {");
/* 1048 */         paramIndentingWriter.pln("throw (" + getExceptionName(arrayOfValueType[b3]) + ")" + str7 + ";");
/* 1049 */         paramIndentingWriter.pOln("}");
/*      */       }
/*      */     }
/*      */
/* 1053 */     paramIndentingWriter.pln("throw Util.wrapException(" + str7 + ");");
/* 1054 */     paramIndentingWriter.pOlnI("} finally {");
/* 1055 */     paramIndentingWriter.pln("_servant_postinvoke(" + str4 + ");");
/* 1056 */     paramIndentingWriter.pOln("}");
/* 1057 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void writeNonLocalStubMethodBody(IndentingWriter paramIndentingWriter, CompoundType.Method paramMethod, CompoundType paramCompoundType) throws IOException {
/* 1065 */     String str1 = paramMethod.getName();
/* 1066 */     String str2 = paramMethod.getIDLName();
/*      */
/* 1068 */     Type[] arrayOfType = paramMethod.getArguments();
/* 1069 */     String[] arrayOfString = paramMethod.getArgumentNames();
/* 1070 */     Type type = paramMethod.getReturnType();
/* 1071 */     ValueType[] arrayOfValueType = getStubExceptions(paramMethod, true);
/*      */
/* 1073 */     String str3 = getVariableName("in");
/* 1074 */     String str4 = getVariableName("out");
/* 1075 */     String str5 = getVariableName("ex");
/*      */
/*      */
/*      */
/*      */
/* 1080 */     boolean bool = false; byte b1;
/* 1081 */     for (b1 = 0; b1 < arrayOfValueType.length; b1++) {
/* 1082 */       if (arrayOfValueType[b1].getIdentifier() != idRemoteException && arrayOfValueType[b1]
/* 1083 */         .isType(32768) &&
/* 1084 */         needNewReadStreamClass(arrayOfValueType[b1])) {
/* 1085 */         bool = true;
/*      */         break;
/*      */       }
/*      */     }
/* 1089 */     if (!bool) {
/* 1090 */       for (b1 = 0; b1 < arrayOfType.length; b1++) {
/* 1091 */         if (needNewReadStreamClass(arrayOfType[b1])) {
/* 1092 */           bool = true;
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1097 */     if (!bool) {
/* 1098 */       bool = needNewReadStreamClass(type);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 1104 */     b1 = 0;
/* 1105 */     for (byte b2 = 0; b2 < arrayOfType.length; b2++) {
/* 1106 */       if (needNewWriteStreamClass(arrayOfType[b2])) {
/* 1107 */         b1 = 1;
/*      */
/*      */
/*      */         break;
/*      */       }
/*      */     }
/*      */
/* 1114 */     paramIndentingWriter.plnI("try {");
/* 1115 */     if (bool) {
/* 1116 */       paramIndentingWriter.pln(idExtInputStream + " " + str3 + " = null;");
/*      */     } else {
/* 1118 */       paramIndentingWriter.pln(idInputStream + " " + str3 + " = null;");
/*      */     }
/* 1120 */     paramIndentingWriter.plnI("try {");
/*      */
/* 1122 */     String str6 = "null";
/*      */
/* 1124 */     if (b1 != 0) {
/* 1125 */       paramIndentingWriter.plnI(idExtOutputStream + " " + str4 + " = ");
/* 1126 */       paramIndentingWriter.pln("(" + idExtOutputStream + ")");
/* 1127 */       paramIndentingWriter.pln("_request(\"" + str2 + "\", true);");
/* 1128 */       paramIndentingWriter.pO();
/*      */     } else {
/* 1130 */       paramIndentingWriter.pln("OutputStream " + str4 + " = _request(\"" + str2 + "\", true);");
/*      */     }
/*      */
/* 1133 */     if (arrayOfType.length > 0) {
/* 1134 */       writeMarshalArguments(paramIndentingWriter, str4, arrayOfType, arrayOfString);
/* 1135 */       paramIndentingWriter.pln();
/*      */     }
/* 1137 */     str6 = str4;
/*      */
/* 1139 */     if (type.isType(1)) {
/* 1140 */       paramIndentingWriter.pln("_invoke(" + str6 + ");");
/*      */     } else {
/* 1142 */       if (bool) {
/* 1143 */         paramIndentingWriter.plnI(str3 + " = (" + idExtInputStream + ")_invoke(" + str6 + ");");
/* 1144 */         paramIndentingWriter.pO();
/*      */       } else {
/* 1146 */         paramIndentingWriter.pln(str3 + " = _invoke(" + str6 + ");");
/*      */       }
/* 1148 */       paramIndentingWriter.p("return ");
/* 1149 */       writeUnmarshalArgument(paramIndentingWriter, str3, type, (String)null);
/* 1150 */       paramIndentingWriter.pln();
/*      */     }
/*      */
/*      */
/*      */
/* 1155 */     paramIndentingWriter.pOlnI("} catch (" + getName(idApplicationException) + " " + str5 + ") {");
/* 1156 */     if (bool) {
/* 1157 */       paramIndentingWriter.pln(str3 + " = (" + idExtInputStream + ") " + str5 + ".getInputStream();");
/*      */     } else {
/* 1159 */       paramIndentingWriter.pln(str3 + " = " + str5 + ".getInputStream();");
/*      */     }
/*      */
/* 1162 */     boolean bool1 = false;
/* 1163 */     boolean bool2 = false; byte b3;
/* 1164 */     for (b3 = 0; b3 < arrayOfValueType.length; b3++) {
/* 1165 */       if (arrayOfValueType[b3].getIdentifier() != idRemoteException) {
/*      */
/*      */
/*      */
/* 1169 */         if (arrayOfValueType[b3].isIDLEntityException() && !arrayOfValueType[b3].isCORBAUserException()) {
/*      */
/*      */
/*      */
/* 1173 */           if (!bool2 && !bool1) {
/* 1174 */             paramIndentingWriter.pln("String $_id = " + str5 + ".getId();");
/* 1175 */             bool2 = true;
/*      */           }
/*      */
/* 1178 */           String str = IDLNames.replace(arrayOfValueType[b3].getQualifiedIDLName(false), "::", ".");
/* 1179 */           str = str + "Helper";
/* 1180 */           paramIndentingWriter.plnI("if ($_id.equals(" + str + ".id())) {");
/* 1181 */           paramIndentingWriter.pln("throw " + str + ".read(" + str3 + ");");
/*      */
/*      */         }
/*      */         else {
/*      */
/*      */
/* 1187 */           if (!bool2 && !bool1) {
/* 1188 */             paramIndentingWriter.pln("String $_id = " + str3 + ".read_string();");
/* 1189 */             bool2 = true;
/* 1190 */             bool1 = true;
/* 1191 */           } else if (bool2 && !bool1) {
/* 1192 */             paramIndentingWriter.pln("$_id = " + str3 + ".read_string();");
/* 1193 */             bool1 = true;
/*      */           }
/* 1195 */           paramIndentingWriter.plnI("if ($_id.equals(\"" + getExceptionRepositoryID(arrayOfValueType[b3]) + "\")) {");
/*      */
/* 1197 */           paramIndentingWriter.pln("throw (" + getExceptionName(arrayOfValueType[b3]) + ") " + str3 + ".read_value(" + getExceptionName(arrayOfValueType[b3]) + ".class);");
/*      */         }
/* 1199 */         paramIndentingWriter.pOln("}");
/*      */       }
/*      */     }
/* 1202 */     if (!bool2 && !bool1) {
/* 1203 */       paramIndentingWriter.pln("String $_id = " + str3 + ".read_string();");
/* 1204 */       bool2 = true;
/* 1205 */       bool1 = true;
/* 1206 */     } else if (bool2 && !bool1) {
/* 1207 */       paramIndentingWriter.pln("$_id = " + str3 + ".read_string();");
/* 1208 */       bool1 = true;
/*      */     }
/* 1210 */     paramIndentingWriter.pln("throw new UnexpectedException($_id);");
/*      */
/*      */
/*      */
/* 1214 */     paramIndentingWriter.pOlnI("} catch (" + getName(idRemarshalException) + " " + str5 + ") {");
/* 1215 */     if (!type.isType(1)) {
/* 1216 */       paramIndentingWriter.p("return ");
/*      */     }
/* 1218 */     paramIndentingWriter.p(str1 + "(");
/* 1219 */     for (b3 = 0; b3 < arrayOfType.length; b3++) {
/* 1220 */       if (b3 > 0) {
/* 1221 */         paramIndentingWriter.p(",");
/*      */       }
/* 1223 */       paramIndentingWriter.p(arrayOfString[b3]);
/*      */     }
/* 1225 */     paramIndentingWriter.pln(");");
/*      */
/*      */
/*      */
/* 1229 */     paramIndentingWriter.pOlnI("} finally {");
/* 1230 */     paramIndentingWriter.pln("_releaseReply(" + str3 + ");");
/*      */
/* 1232 */     paramIndentingWriter.pOln("}");
/*      */
/*      */
/*      */
/* 1236 */     paramIndentingWriter.pOlnI("} catch (SystemException " + str5 + ") {");
/* 1237 */     paramIndentingWriter.pln("throw Util.mapSystemException(" + str5 + ");");
/* 1238 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   void allocateResult(IndentingWriter paramIndentingWriter, Type paramType) throws IOException {
/* 1245 */     if (!paramType.isType(1)) {
/* 1246 */       String str = testUtil(getName(paramType), paramType);
/* 1247 */       paramIndentingWriter.p(str + " result = ");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   int getTypeCode(Type paramType) {
/* 1253 */     int i = paramType.getTypeCode();
/*      */
/*      */
/*      */
/*      */
/* 1258 */     if (paramType instanceof CompoundType && ((CompoundType)paramType)
/* 1259 */       .isAbstractBase()) {
/* 1260 */       i = 8192;
/*      */     }
/*      */
/* 1263 */     return i;
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
/*      */   void writeMarshalArgument(IndentingWriter paramIndentingWriter, String paramString1, Type paramType, String paramString2) throws IOException {
/* 1275 */     int i = getTypeCode(paramType);
/*      */
/* 1277 */     switch (i) {
/*      */       case 2:
/* 1279 */         paramIndentingWriter.p(paramString1 + ".write_boolean(" + paramString2 + ");");
/*      */         return;
/*      */       case 4:
/* 1282 */         paramIndentingWriter.p(paramString1 + ".write_octet(" + paramString2 + ");");
/*      */         return;
/*      */       case 8:
/* 1285 */         paramIndentingWriter.p(paramString1 + ".write_wchar(" + paramString2 + ");");
/*      */         return;
/*      */       case 16:
/* 1288 */         paramIndentingWriter.p(paramString1 + ".write_short(" + paramString2 + ");");
/*      */         return;
/*      */       case 32:
/* 1291 */         paramIndentingWriter.p(paramString1 + ".write_long(" + paramString2 + ");");
/*      */         return;
/*      */       case 64:
/* 1294 */         paramIndentingWriter.p(paramString1 + ".write_longlong(" + paramString2 + ");");
/*      */         return;
/*      */       case 128:
/* 1297 */         paramIndentingWriter.p(paramString1 + ".write_float(" + paramString2 + ");");
/*      */         return;
/*      */       case 256:
/* 1300 */         paramIndentingWriter.p(paramString1 + ".write_double(" + paramString2 + ");");
/*      */         return;
/*      */       case 512:
/* 1303 */         paramIndentingWriter.p(paramString1 + ".write_value(" + paramString2 + "," + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 1024:
/* 1306 */         paramIndentingWriter.p("Util.writeAny(" + paramString1 + "," + paramString2 + ");");
/*      */         return;
/*      */       case 2048:
/* 1309 */         paramIndentingWriter.p(paramString1 + ".write_Object(" + paramString2 + ");");
/*      */         return;
/*      */       case 4096:
/* 1312 */         paramIndentingWriter.p("Util.writeRemoteObject(" + paramString1 + "," + paramString2 + ");");
/*      */         return;
/*      */       case 8192:
/* 1315 */         paramIndentingWriter.p("Util.writeAbstractObject(" + paramString1 + "," + paramString2 + ");");
/*      */         return;
/*      */       case 16384:
/* 1318 */         paramIndentingWriter.p(paramString1 + ".write_value((Serializable)" + paramString2 + "," + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 32768:
/* 1321 */         paramIndentingWriter.p(paramString1 + ".write_value(" + paramString2 + "," + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 65536:
/* 1324 */         paramIndentingWriter.p(paramString1 + ".write_value((Serializable)" + paramString2 + "," + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 131072:
/* 1327 */         paramIndentingWriter.p(paramString1 + ".write_value((Serializable)" + paramString2 + "," + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 262144:
/* 1330 */         this.castArray = true;
/* 1331 */         paramIndentingWriter.p(paramString1 + ".write_value(cast_array(" + paramString2 + ")," + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 524288:
/* 1334 */         paramIndentingWriter.p("Util.writeRemoteObject(" + paramString1 + "," + paramString2 + ");");
/*      */         return;
/*      */     }
/* 1337 */     throw new Error("unexpected type code: " + i);
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
/*      */   void writeUnmarshalArgument(IndentingWriter paramIndentingWriter, String paramString1, Type paramType, String paramString2) throws IOException {
/*      */     String str;
/* 1352 */     int i = getTypeCode(paramType);
/*      */
/* 1354 */     if (paramString2 != null) {
/* 1355 */       paramIndentingWriter.p(paramString2 + " = ");
/*      */     }
/*      */
/* 1358 */     switch (i) {
/*      */       case 2:
/* 1360 */         paramIndentingWriter.p(paramString1 + ".read_boolean();");
/*      */         return;
/*      */       case 4:
/* 1363 */         paramIndentingWriter.p(paramString1 + ".read_octet();");
/*      */         return;
/*      */       case 8:
/* 1366 */         paramIndentingWriter.p(paramString1 + ".read_wchar();");
/*      */         return;
/*      */       case 16:
/* 1369 */         paramIndentingWriter.p(paramString1 + ".read_short();");
/*      */         return;
/*      */       case 32:
/* 1372 */         paramIndentingWriter.p(paramString1 + ".read_long();");
/*      */         return;
/*      */       case 64:
/* 1375 */         paramIndentingWriter.p(paramString1 + ".read_longlong();");
/*      */         return;
/*      */       case 128:
/* 1378 */         paramIndentingWriter.p(paramString1 + ".read_float();");
/*      */         return;
/*      */       case 256:
/* 1381 */         paramIndentingWriter.p(paramString1 + ".read_double();");
/*      */         return;
/*      */       case 512:
/* 1384 */         paramIndentingWriter.p("(String) " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 1024:
/* 1387 */         if (paramType.getIdentifier() != idJavaLangObject) {
/* 1388 */           paramIndentingWriter.p("(" + getName(paramType) + ") ");
/*      */         }
/* 1390 */         paramIndentingWriter.p("Util.readAny(" + paramString1 + ");");
/*      */         return;
/*      */       case 2048:
/* 1393 */         if (paramType.getIdentifier() == idCorbaObject) {
/* 1394 */           paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_Object();");
/*      */         } else {
/* 1396 */           paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_Object(" + getStubName(paramType) + ".class);");
/*      */         }
/*      */         return;
/*      */       case 4096:
/* 1400 */         str = testUtil(getName(paramType), paramType);
/* 1401 */         paramIndentingWriter.p("(" + str + ") PortableRemoteObject.narrow(" + paramString1 + ".read_Object(), " + str + ".class);");
/*      */         return;
/*      */
/*      */       case 8192:
/* 1405 */         paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_abstract_interface();");
/*      */         return;
/*      */       case 16384:
/* 1408 */         paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 32768:
/* 1411 */         paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 65536:
/* 1414 */         paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 131072:
/* 1417 */         paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 262144:
/* 1420 */         paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/*      */         return;
/*      */       case 524288:
/* 1423 */         paramIndentingWriter.p("(" + getName(paramType) + ") PortableRemoteObject.narrow(" + paramString1 + ".read_Object(), " +
/* 1424 */             getName(paramType) + ".class);");
/*      */         return;
/*      */     }
/*      */
/* 1428 */     throw new Error("unexpected type code: " + i);
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
/*      */   String[] getAllRemoteRepIDs(CompoundType paramCompoundType) {
/*      */     String[] arrayOfString;
/* 1450 */     Type[] arrayOfType = collectAllRemoteInterfaces(paramCompoundType);
/*      */
/* 1452 */     int i = arrayOfType.length;
/* 1453 */     boolean bool = paramCompoundType instanceof ImplementationType;
/* 1454 */     InterfaceType[] arrayOfInterfaceType = paramCompoundType.getInterfaces();
/* 1455 */     int j = countRemote((Type[])arrayOfInterfaceType, false);
/* 1456 */     byte b1 = 0;
/*      */
/*      */
/*      */
/*      */
/* 1461 */     if (bool && j > 1) {
/*      */
/*      */
/*      */
/* 1465 */       arrayOfString = new String[i + 1];
/* 1466 */       arrayOfString[0] = getRepositoryID(paramCompoundType);
/* 1467 */       b1 = 1;
/*      */
/*      */     }
/*      */     else {
/*      */
/*      */
/* 1473 */       arrayOfString = new String[i];
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1479 */       if (i > 1) {
/*      */
/*      */
/*      */
/* 1483 */         String str = null;
/*      */
/* 1485 */         if (bool) {
/*      */
/*      */
/*      */
/*      */
/* 1490 */           for (byte b3 = 0; b3 < arrayOfInterfaceType.length; b3++) {
/* 1491 */             if (arrayOfInterfaceType[b3].isType(4096)) {
/* 1492 */               str = arrayOfInterfaceType[b3].getRepositoryID();
/*      */
/*      */
/*      */
/*      */
/*      */               break;
/*      */             }
/*      */           }
/*      */         } else {
/* 1501 */           str = paramCompoundType.getRepositoryID();
/*      */         }
/*      */
/*      */
/*      */
/*      */
/* 1507 */         for (byte b = 0; b < i; b++) {
/* 1508 */           if (arrayOfType[b].getRepositoryID() == str) {
/*      */
/*      */
/*      */
/* 1512 */             if (b > 0) {
/* 1513 */               Type type = arrayOfType[0];
/* 1514 */               arrayOfType[0] = arrayOfType[b];
/* 1515 */               arrayOfType[b] = type;
/*      */             }
/*      */
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     byte b2;
/*      */
/* 1526 */     for (b2 = 0; b2 < arrayOfType.length; b2++) {
/* 1527 */       arrayOfString[b1++] = getRepositoryID(arrayOfType[b2]);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1535 */     if (this.reverseIDs) {
/* 1536 */       b2 = 0;
/* 1537 */       int k = arrayOfString.length - 1;
/* 1538 */       while (b2 < k) {
/* 1539 */         String str = arrayOfString[b2];
/* 1540 */         arrayOfString[b2++] = arrayOfString[k];
/* 1541 */         arrayOfString[k--] = str;
/*      */       }
/*      */     }
/*      */
/* 1545 */     return arrayOfString;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   Type[] collectAllRemoteInterfaces(CompoundType paramCompoundType) {
/* 1552 */     Vector vector = new Vector();
/*      */
/*      */
/*      */
/*      */
/* 1557 */     addRemoteInterfaces(vector, paramCompoundType);
/*      */
/*      */
/*      */
/* 1561 */     Type[] arrayOfType = new Type[vector.size()];
/* 1562 */     vector.copyInto((Object[])arrayOfType);
/*      */
/* 1564 */     return arrayOfType;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void addRemoteInterfaces(Vector<CompoundType> paramVector, CompoundType paramCompoundType) {
/* 1572 */     if (paramCompoundType != null) {
/* 1573 */       if (paramCompoundType.isInterface() && !paramVector.contains(paramCompoundType)) {
/* 1574 */         paramVector.addElement(paramCompoundType);
/*      */       }
/*      */
/* 1577 */       InterfaceType[] arrayOfInterfaceType = paramCompoundType.getInterfaces();
/* 1578 */       for (byte b = 0; b < arrayOfInterfaceType.length; b++) {
/*      */
/* 1580 */         if (arrayOfInterfaceType[b].isType(4096)) {
/* 1581 */           addRemoteInterfaces(paramVector, arrayOfInterfaceType[b]);
/*      */         }
/*      */       }
/*      */
/* 1585 */       addRemoteInterfaces(paramVector, paramCompoundType.getSuperclass());
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   RemoteType[] getDirectRemoteInterfaces(CompoundType paramCompoundType) {
/* 1596 */     InterfaceType[] arrayOfInterfaceType2, arrayOfInterfaceType1 = paramCompoundType.getInterfaces();
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
/* 1609 */     if (paramCompoundType instanceof ImplementationType) {
/*      */
/*      */
/*      */
/*      */
/* 1614 */       arrayOfInterfaceType2 = arrayOfInterfaceType1;
/*      */
/*      */     }
/*      */     else {
/*      */
/*      */
/* 1620 */       arrayOfInterfaceType2 = new InterfaceType[1];
/* 1621 */       arrayOfInterfaceType2[0] = (InterfaceType)paramCompoundType;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 1627 */     int i = countRemote((Type[])arrayOfInterfaceType2, false);
/*      */
/* 1629 */     if (i == 0) {
/* 1630 */       throw new CompilerError("iiop.StubGenerator: No remote interfaces!");
/*      */     }
/*      */
/* 1633 */     RemoteType[] arrayOfRemoteType = new RemoteType[i];
/* 1634 */     byte b1 = 0;
/* 1635 */     for (byte b2 = 0; b2 < arrayOfInterfaceType2.length; b2++) {
/* 1636 */       if (arrayOfInterfaceType2[b2].isType(4096)) {
/* 1637 */         arrayOfRemoteType[b1++] = (RemoteType)arrayOfInterfaceType2[b2];
/*      */       }
/*      */     }
/*      */
/* 1641 */     return arrayOfRemoteType;
/*      */   }
/*      */
/*      */   int countRemote(Type[] paramArrayOfType, boolean paramBoolean) {
/* 1645 */     byte b1 = 0;
/* 1646 */     for (byte b2 = 0; b2 < paramArrayOfType.length; b2++) {
/* 1647 */       if (paramArrayOfType[b2].isType(4096) && (paramBoolean ||
/* 1648 */         !paramArrayOfType[b2].isType(8192))) {
/* 1649 */         b1++;
/*      */       }
/*      */     }
/*      */
/* 1653 */     return b1;
/*      */   }
/*      */
/*      */   void writeCastArray(IndentingWriter paramIndentingWriter) throws IOException {
/* 1657 */     if (this.castArray) {
/* 1658 */       paramIndentingWriter.pln();
/* 1659 */       paramIndentingWriter.pln("// This method is required as a work-around for");
/* 1660 */       paramIndentingWriter.pln("// a bug in the JDK 1.1.6 verifier.");
/* 1661 */       paramIndentingWriter.pln();
/* 1662 */       paramIndentingWriter.plnI("private " + getName(idJavaIoSerializable) + " cast_array(Object obj) {");
/* 1663 */       paramIndentingWriter.pln("return (" + getName(idJavaIoSerializable) + ")obj;");
/* 1664 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */
/*      */   void writeIds(IndentingWriter paramIndentingWriter, CompoundType paramCompoundType, boolean paramBoolean) throws IOException {
/* 1669 */     paramIndentingWriter.plnI("private static final String[] _type_ids = {");
/*      */
/* 1671 */     String[] arrayOfString = getAllRemoteRepIDs(paramCompoundType);
/*      */
/* 1673 */     if (arrayOfString.length > 0) {
/* 1674 */       for (byte b = 0; b < arrayOfString.length; b++) {
/* 1675 */         if (b > 0)
/* 1676 */           paramIndentingWriter.pln(", ");
/* 1677 */         paramIndentingWriter.p("\"" + arrayOfString[b] + "\"");
/*      */       }
/*      */     } else {
/*      */
/* 1681 */       paramIndentingWriter.pln("\"\"");
/*      */     }
/* 1683 */     String str = paramCompoundType.getQualifiedName();
/* 1684 */     boolean bool = (paramBoolean && this.transactionalObjects.containsKey(str)) ? true : false;
/*      */
/* 1686 */     if (bool) {
/*      */
/* 1688 */       paramIndentingWriter.pln(", ");
/* 1689 */       paramIndentingWriter.pln("\"IDL:omg.org/CosTransactions/TransactionalObject:1.0\"");
/* 1690 */     } else if (arrayOfString.length > 0) {
/* 1691 */       paramIndentingWriter.pln();
/*      */     }
/* 1693 */     paramIndentingWriter.pOln("};");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected void writeTie(OutputType paramOutputType, IndentingWriter paramIndentingWriter) throws IOException {
/* 1703 */     CompoundType compoundType = (CompoundType)paramOutputType.getType();
/* 1704 */     RemoteType[] arrayOfRemoteType = null;
/*      */
/*      */
/* 1707 */     paramIndentingWriter.pln("// Tie class generated by rmic, do not edit.");
/* 1708 */     paramIndentingWriter.pln("// Contents subject to change without notice.");
/* 1709 */     paramIndentingWriter.pln();
/*      */
/*      */
/* 1712 */     setStandardClassesInUse(compoundType, false);
/*      */
/*      */
/* 1715 */     addClassesInUse(compoundType, arrayOfRemoteType);
/*      */
/*      */
/* 1718 */     writePackageAndImports(paramIndentingWriter);
/*      */
/*      */
/* 1721 */     paramIndentingWriter.p("public class " + this.currentClass + " extends " +
/* 1722 */         getName(this.tieBaseClass) + " implements Tie");
/*      */
/*      */
/*      */
/*      */
/* 1727 */     if (!implementsRemote(compoundType)) {
/* 1728 */       paramIndentingWriter.pln(",");
/* 1729 */       paramIndentingWriter.p(getName("java.rmi.Remote"));
/*      */     }
/*      */
/* 1732 */     paramIndentingWriter.plnI(" {");
/*      */
/*      */
/* 1735 */     paramIndentingWriter.pln();
/* 1736 */     paramIndentingWriter.pln("volatile private " + getName(compoundType) + " target = null;");
/* 1737 */     paramIndentingWriter.pln();
/*      */
/*      */
/* 1740 */     writeIds(paramIndentingWriter, compoundType, true);
/*      */
/*      */
/* 1743 */     paramIndentingWriter.pln();
/* 1744 */     paramIndentingWriter.plnI("public void setTarget(Remote target) {");
/* 1745 */     paramIndentingWriter.pln("this.target = (" + getName(compoundType) + ") target;");
/* 1746 */     paramIndentingWriter.pOln("}");
/*      */
/*      */
/* 1749 */     paramIndentingWriter.pln();
/* 1750 */     paramIndentingWriter.plnI("public Remote getTarget() {");
/* 1751 */     paramIndentingWriter.pln("return target;");
/* 1752 */     paramIndentingWriter.pOln("}");
/*      */
/*      */
/* 1755 */     paramIndentingWriter.pln();
/* 1756 */     write_tie_thisObject_method(paramIndentingWriter, idCorbaObject);
/*      */
/*      */
/* 1759 */     paramIndentingWriter.pln();
/* 1760 */     write_tie_deactivate_method(paramIndentingWriter);
/*      */
/*      */
/* 1763 */     paramIndentingWriter.pln();
/* 1764 */     paramIndentingWriter.plnI("public ORB orb() {");
/* 1765 */     paramIndentingWriter.pln("return _orb();");
/* 1766 */     paramIndentingWriter.pOln("}");
/*      */
/*      */
/* 1769 */     paramIndentingWriter.pln();
/* 1770 */     write_tie_orb_method(paramIndentingWriter);
/*      */
/*      */
/* 1773 */     paramIndentingWriter.pln();
/* 1774 */     write_tie__ids_method(paramIndentingWriter);
/*      */
/*      */
/* 1777 */     CompoundType.Method[] arrayOfMethod = compoundType.getMethods();
/*      */
/*      */
/*      */
/*      */
/* 1782 */     addNamesInUse(arrayOfMethod);
/* 1783 */     addNameInUse("target");
/* 1784 */     addNameInUse("_type_ids");
/*      */
/*      */
/* 1787 */     paramIndentingWriter.pln();
/*      */
/* 1789 */     String str1 = getVariableName("in");
/* 1790 */     String str2 = getVariableName("_in");
/* 1791 */     String str3 = getVariableName("ex");
/* 1792 */     String str4 = getVariableName("method");
/* 1793 */     String str5 = getVariableName("reply");
/*      */
/* 1795 */     paramIndentingWriter.plnI("public OutputStream  _invoke(String " + str4 + ", InputStream " + str2 + ", ResponseHandler " + str5 + ") throws SystemException {");
/*      */
/*      */
/* 1798 */     if (arrayOfMethod.length > 0) {
/* 1799 */       paramIndentingWriter.plnI("try {");
/* 1800 */       paramIndentingWriter.pln(getName(compoundType) + " target = this.target;");
/* 1801 */       paramIndentingWriter.plnI("if (target == null) {");
/* 1802 */       paramIndentingWriter.pln("throw new java.io.IOException();");
/* 1803 */       paramIndentingWriter.pOln("}");
/* 1804 */       paramIndentingWriter.plnI(idExtInputStream + " " + str1 + " = ");
/* 1805 */       paramIndentingWriter.pln("(" + idExtInputStream + ") " + str2 + ";");
/* 1806 */       paramIndentingWriter.pO();
/*      */
/*      */
/*      */
/*      */
/* 1811 */       StaticStringsHash staticStringsHash = getStringsHash(arrayOfMethod);
/*      */
/* 1813 */       if (staticStringsHash != null) {
/* 1814 */         paramIndentingWriter.plnI("switch (" + str4 + "." + staticStringsHash.method + ") {");
/* 1815 */         for (byte b = 0; b < staticStringsHash.buckets.length; b++) {
/* 1816 */           paramIndentingWriter.plnI("case " + staticStringsHash.keys[b] + ": ");
/* 1817 */           for (byte b1 = 0; b1 < (staticStringsHash.buckets[b]).length; b1++) {
/* 1818 */             CompoundType.Method method = arrayOfMethod[staticStringsHash.buckets[b][b1]];
/* 1819 */             if (b1 > 0) {
/* 1820 */               paramIndentingWriter.pO("} else ");
/*      */             }
/* 1822 */             paramIndentingWriter.plnI("if (" + str4 + ".equals(\"" + method.getIDLName() + "\")) {");
/* 1823 */             writeTieMethod(paramIndentingWriter, compoundType, method);
/*      */           }
/* 1825 */           paramIndentingWriter.pOln("}");
/* 1826 */           paramIndentingWriter.pO();
/*      */         }
/*      */       } else {
/* 1829 */         for (byte b = 0; b < arrayOfMethod.length; b++) {
/* 1830 */           CompoundType.Method method = arrayOfMethod[b];
/* 1831 */           if (b > 0) {
/* 1832 */             paramIndentingWriter.pO("} else ");
/*      */           }
/*      */
/* 1835 */           paramIndentingWriter.plnI("if (" + str4 + ".equals(\"" + method.getIDLName() + "\")) {");
/* 1836 */           writeTieMethod(paramIndentingWriter, compoundType, method);
/*      */         }
/*      */       }
/*      */
/* 1840 */       if (staticStringsHash != null) {
/* 1841 */         paramIndentingWriter.pI();
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1848 */       if (staticStringsHash != null) {
/* 1849 */         paramIndentingWriter.pO();
/*      */       }
/* 1851 */       paramIndentingWriter.pOln("}");
/* 1852 */       paramIndentingWriter.pln("throw new " + getName(idBadMethodException) + "();");
/*      */
/* 1854 */       paramIndentingWriter.pOlnI("} catch (" + getName(idSystemException) + " " + str3 + ") {");
/* 1855 */       paramIndentingWriter.pln("throw " + str3 + ";");
/*      */
/* 1857 */       paramIndentingWriter.pOlnI("} catch (" + getName(idJavaLangThrowable) + " " + str3 + ") {");
/* 1858 */       paramIndentingWriter.pln("throw new " + getName(idPortableUnknownException) + "(" + str3 + ");");
/* 1859 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */     else {
/*      */
/* 1863 */       paramIndentingWriter.pln("throw new " + getName(idBadMethodException) + "();");
/*      */     }
/*      */
/* 1866 */     paramIndentingWriter.pOln("}");
/*      */
/*      */
/*      */
/* 1870 */     writeCastArray(paramIndentingWriter);
/*      */
/*      */
/* 1873 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */   public void catchWrongPolicy(IndentingWriter paramIndentingWriter) throws IOException {
/* 1876 */     paramIndentingWriter.pln("");
/*      */   }
/*      */   public void catchServantNotActive(IndentingWriter paramIndentingWriter) throws IOException {
/* 1879 */     paramIndentingWriter.pln("");
/*      */   }
/*      */   public void catchObjectNotActive(IndentingWriter paramIndentingWriter) throws IOException {
/* 1882 */     paramIndentingWriter.pln("");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public void write_tie_thisObject_method(IndentingWriter paramIndentingWriter, Identifier paramIdentifier) throws IOException {
/* 1889 */     if (this.POATie) {
/* 1890 */       paramIndentingWriter.plnI("public " + paramIdentifier + " thisObject() {");
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
/* 1902 */       paramIndentingWriter.pln("return _this_object();");
/* 1903 */       paramIndentingWriter.pOln("}");
/*      */     } else {
/* 1905 */       paramIndentingWriter.plnI("public " + paramIdentifier + " thisObject() {");
/* 1906 */       paramIndentingWriter.pln("return this;");
/* 1907 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void write_tie_deactivate_method(IndentingWriter paramIndentingWriter) throws IOException {
/* 1914 */     if (this.POATie) {
/* 1915 */       paramIndentingWriter.plnI("public void deactivate() {");
/* 1916 */       paramIndentingWriter.pln("try{");
/* 1917 */       paramIndentingWriter.pln("_poa().deactivate_object(_poa().servant_to_id(this));");
/* 1918 */       paramIndentingWriter.pln("}catch (org.omg.PortableServer.POAPackage.WrongPolicy exception){");
/* 1919 */       catchWrongPolicy(paramIndentingWriter);
/* 1920 */       paramIndentingWriter.pln("}catch (org.omg.PortableServer.POAPackage.ObjectNotActive exception){");
/* 1921 */       catchObjectNotActive(paramIndentingWriter);
/* 1922 */       paramIndentingWriter.pln("}catch (org.omg.PortableServer.POAPackage.ServantNotActive exception){");
/* 1923 */       catchServantNotActive(paramIndentingWriter);
/* 1924 */       paramIndentingWriter.pln("}");
/* 1925 */       paramIndentingWriter.pOln("}");
/*      */     } else {
/* 1927 */       paramIndentingWriter.plnI("public void deactivate() {");
/* 1928 */       paramIndentingWriter.pln("_orb().disconnect(this);");
/* 1929 */       paramIndentingWriter.pln("_set_delegate(null);");
/* 1930 */       paramIndentingWriter.pln("target = null;");
/* 1931 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void write_tie_orb_method(IndentingWriter paramIndentingWriter) throws IOException {
/* 1938 */     if (this.POATie) {
/* 1939 */       paramIndentingWriter.plnI("public void orb(ORB orb) {");
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1949 */       paramIndentingWriter.pln("try {");
/* 1950 */       paramIndentingWriter.pln("    ((org.omg.CORBA_2_3.ORB)orb).set_delegate(this);");
/* 1951 */       paramIndentingWriter.pln("}");
/* 1952 */       paramIndentingWriter.pln("catch(ClassCastException e) {");
/* 1953 */       paramIndentingWriter.pln("    throw new org.omg.CORBA.BAD_PARAM");
/* 1954 */       paramIndentingWriter.pln("        (\"POA Servant requires an instance of org.omg.CORBA_2_3.ORB\");");
/* 1955 */       paramIndentingWriter.pln("}");
/* 1956 */       paramIndentingWriter.pOln("}");
/*      */     } else {
/* 1958 */       paramIndentingWriter.plnI("public void orb(ORB orb) {");
/* 1959 */       paramIndentingWriter.pln("orb.connect(this);");
/* 1960 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void write_tie__ids_method(IndentingWriter paramIndentingWriter) throws IOException {
/* 1967 */     if (this.POATie) {
/* 1968 */       paramIndentingWriter.plnI("public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId){");
/* 1969 */       paramIndentingWriter.pln("return (String[]) _type_ids.clone();");
/* 1970 */       paramIndentingWriter.pOln("}");
/*      */     } else {
/* 1972 */       paramIndentingWriter.plnI("public String[] _ids() { ");
/* 1973 */       paramIndentingWriter.pln("return (String[]) _type_ids.clone();");
/* 1974 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   StaticStringsHash getStringsHash(CompoundType.Method[] paramArrayOfMethod) {
/* 1980 */     if (this.useHash && paramArrayOfMethod.length > 1) {
/* 1981 */       String[] arrayOfString = new String[paramArrayOfMethod.length];
/* 1982 */       for (byte b = 0; b < arrayOfString.length; b++) {
/* 1983 */         arrayOfString[b] = paramArrayOfMethod[b].getIDLName();
/*      */       }
/* 1985 */       return new StaticStringsHash(arrayOfString);
/*      */     }
/* 1987 */     return null;
/*      */   }
/*      */
/*      */   static boolean needNewReadStreamClass(Type paramType) {
/* 1991 */     if (paramType.isType(8192)) {
/* 1992 */       return true;
/*      */     }
/*      */
/*      */
/* 1996 */     if (paramType instanceof CompoundType && ((CompoundType)paramType)
/* 1997 */       .isAbstractBase()) {
/* 1998 */       return true;
/*      */     }
/* 2000 */     return needNewWriteStreamClass(paramType);
/*      */   }
/*      */
/*      */   static boolean needNewWriteStreamClass(Type paramType) {
/* 2004 */     switch (paramType.getTypeCode()) { case 1:
/*      */       case 2:
/*      */       case 4:
/*      */       case 8:
/*      */       case 16:
/*      */       case 32:
/*      */       case 64:
/*      */       case 128:
/*      */       case 256:
/* 2013 */         return false;
/*      */       case 512:
/* 2015 */         return true;
/* 2016 */       case 1024: return false;
/* 2017 */       case 2048: return false;
/* 2018 */       case 4096: return false;
/* 2019 */       case 8192: return false;
/* 2020 */       case 16384: return true;
/* 2021 */       case 32768: return true;
/* 2022 */       case 65536: return true;
/* 2023 */       case 131072: return true;
/* 2024 */       case 262144: return true;
/* 2025 */       case 524288: return false; }
/*      */
/* 2027 */     throw new Error("unexpected type code: " + paramType.getTypeCode());
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
/*      */   String[] writeCopyArguments(CompoundType.Method paramMethod, IndentingWriter paramIndentingWriter) throws IOException {
/* 2039 */     Type[] arrayOfType = paramMethod.getArguments();
/* 2040 */     String[] arrayOfString1 = paramMethod.getArgumentNames();
/*      */
/*      */
/*      */
/* 2044 */     String[] arrayOfString2 = new String[arrayOfString1.length]; byte b1;
/* 2045 */     for (b1 = 0; b1 < arrayOfString2.length; b1++) {
/* 2046 */       arrayOfString2[b1] = arrayOfString1[b1];
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2056 */     b1 = 0;
/* 2057 */     boolean[] arrayOfBoolean = new boolean[arrayOfType.length];
/* 2058 */     byte b2 = 0;
/* 2059 */     byte b3 = 0;
/*      */
/*      */     byte b4;
/* 2062 */     for (b4 = 0; b4 < arrayOfType.length; b4++) {
/* 2063 */       if (mustCopy(arrayOfType[b4])) {
/* 2064 */         arrayOfBoolean[b4] = true;
/* 2065 */         b2++;
/* 2066 */         b3 = b4;
/* 2067 */         if (arrayOfType[b4].getTypeCode() != 4096 && arrayOfType[b4]
/* 2068 */           .getTypeCode() != 65536) {
/* 2069 */           b1 = 1;
/*      */         }
/*      */       } else {
/* 2072 */         arrayOfBoolean[b4] = false;
/*      */       }
/*      */     }
/*      */
/*      */
/* 2077 */     if (b2 > 0) {
/*      */
/*      */
/* 2080 */       if (b1 != 0)
/*      */       {
/*      */
/*      */
/* 2084 */         for (b4 = 0; b4 < arrayOfType.length; b4++) {
/* 2085 */           if (arrayOfType[b4].getTypeCode() == 512) {
/* 2086 */             arrayOfBoolean[b4] = true;
/* 2087 */             b2++;
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/* 2094 */       if (b2 > 1) {
/*      */
/* 2096 */         String str = getVariableName("copies");
/* 2097 */         paramIndentingWriter.p("Object[] " + str + " = Util.copyObjects(new Object[]{");
/* 2098 */         boolean bool = true; byte b5;
/* 2099 */         for (b5 = 0; b5 < arrayOfType.length; b5++) {
/* 2100 */           if (arrayOfBoolean[b5]) {
/* 2101 */             if (!bool) {
/* 2102 */               paramIndentingWriter.p(",");
/*      */             }
/* 2104 */             bool = false;
/* 2105 */             paramIndentingWriter.p(arrayOfString1[b5]);
/*      */           }
/*      */         }
/* 2108 */         paramIndentingWriter.pln("},_orb());");
/*      */
/*      */
/*      */
/*      */
/* 2113 */         b5 = 0;
/* 2114 */         for (byte b6 = 0; b6 < arrayOfType.length; b6++) {
/* 2115 */           if (arrayOfBoolean[b6]) {
/* 2116 */             arrayOfString2[b6] = getVariableName(arrayOfString2[b6] + "Copy");
/* 2117 */             paramIndentingWriter.pln(getName(arrayOfType[b6]) + " " + arrayOfString2[b6] + " = (" + getName(arrayOfType[b6]) + ") " + str + "[" + b5++ + "];");
/*      */           }
/*      */
/*      */         }
/*      */
/*      */       } else {
/*      */
/* 2124 */         arrayOfString2[b3] = getVariableName(arrayOfString2[b3] + "Copy");
/* 2125 */         paramIndentingWriter.pln(getName(arrayOfType[b3]) + " " + arrayOfString2[b3] + " = (" +
/* 2126 */             getName(arrayOfType[b3]) + ") Util.copyObject(" + arrayOfString1[b3] + ",_orb());");
/*      */       }
/*      */     }
/*      */
/*      */
/* 2131 */     return arrayOfString2;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   String getRepositoryID(Type paramType) {
/* 2138 */     return IDLNames.replace(paramType.getRepositoryID(), "\\", "\\\\");
/*      */   }
/*      */
/*      */   String getExceptionRepositoryID(Type paramType) {
/* 2142 */     ClassType classType = (ClassType)paramType;
/* 2143 */     return IDLNames.getIDLRepositoryID(classType.getQualifiedIDLExceptionName(false));
/*      */   }
/*      */
/*      */   String getVariableName(String paramString) {
/* 2147 */     while (this.namesInUse.contains(paramString)) {
/* 2148 */       paramString = "$" + paramString;
/*      */     }
/*      */
/* 2151 */     return paramString;
/*      */   }
/*      */
/*      */   void addNamesInUse(CompoundType.Method[] paramArrayOfMethod) {
/* 2155 */     for (byte b = 0; b < paramArrayOfMethod.length; b++) {
/* 2156 */       addNamesInUse(paramArrayOfMethod[b]);
/*      */     }
/*      */   }
/*      */
/*      */   void addNamesInUse(CompoundType.Method paramMethod) {
/* 2161 */     String[] arrayOfString = paramMethod.getArgumentNames();
/* 2162 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 2163 */       addNameInUse(arrayOfString[b]);
/*      */     }
/*      */   }
/*      */
/*      */   void addNameInUse(String paramString) {
/* 2168 */     this.namesInUse.add(paramString);
/*      */   }
/*      */
/*      */   static boolean mustCopy(Type paramType) {
/* 2172 */     switch (paramType.getTypeCode()) { case 1:
/*      */       case 2:
/*      */       case 4:
/*      */       case 8:
/*      */       case 16:
/*      */       case 32:
/*      */       case 64:
/*      */       case 128:
/*      */       case 256:
/*      */       case 512:
/* 2182 */         return false;
/*      */       case 1024:
/* 2184 */         return true;
/*      */       case 2048:
/* 2186 */         return false;
/*      */       case 4096:
/*      */       case 8192:
/*      */       case 16384:
/*      */       case 32768:
/*      */       case 65536:
/*      */       case 131072:
/*      */       case 262144:
/*      */       case 524288:
/* 2195 */         return true; }
/*      */
/* 2197 */     throw new Error("unexpected type code: " + paramType.getTypeCode());
/*      */   }
/*      */
/*      */
/*      */
/*      */   ValueType[] getStubExceptions(CompoundType.Method paramMethod, boolean paramBoolean) {
/* 2203 */     ValueType[] arrayOfValueType = paramMethod.getFilteredStubExceptions(paramMethod.getExceptions());
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 2210 */     if (paramBoolean) {
/* 2211 */       Arrays.sort(arrayOfValueType, new UserExceptionComparator());
/*      */     }
/*      */
/* 2214 */     return arrayOfValueType;
/*      */   }
/*      */
/*      */   ValueType[] getTieExceptions(CompoundType.Method paramMethod) {
/* 2218 */     return paramMethod.getUniqueCatchList(paramMethod.getImplExceptions());
/*      */   }
/*      */
/*      */
/*      */   void writeTieMethod(IndentingWriter paramIndentingWriter, CompoundType paramCompoundType, CompoundType.Method paramMethod) throws IOException {
/* 2223 */     String str1 = paramMethod.getName();
/* 2224 */     Type[] arrayOfType = paramMethod.getArguments();
/* 2225 */     String[] arrayOfString = paramMethod.getArgumentNames();
/* 2226 */     Type type = paramMethod.getReturnType();
/* 2227 */     ValueType[] arrayOfValueType = getTieExceptions(paramMethod);
/* 2228 */     String str2 = getVariableName("in");
/* 2229 */     String str3 = getVariableName("ex");
/* 2230 */     String str4 = getVariableName("out");
/* 2231 */     String str5 = getVariableName("reply");
/*      */     byte b1;
/* 2233 */     for (b1 = 0; b1 < arrayOfType.length; b1++) {
/* 2234 */       paramIndentingWriter.p(getName(arrayOfType[b1]) + " " + arrayOfString[b1] + " = ");
/* 2235 */       writeUnmarshalArgument(paramIndentingWriter, str2, arrayOfType[b1], (String)null);
/* 2236 */       paramIndentingWriter.pln();
/*      */     }
/*      */
/* 2239 */     b1 = (arrayOfValueType != null) ? 1 : 0;
/* 2240 */     boolean bool = !type.isType(1) ? true : false;
/*      */
/* 2242 */     if (b1 != 0 && bool) {
/* 2243 */       String str = testUtil(getName(type), type);
/* 2244 */       paramIndentingWriter.pln(str + " result;");
/*      */     }
/*      */
/* 2247 */     if (b1 != 0) {
/* 2248 */       paramIndentingWriter.plnI("try {");
/*      */     }
/* 2250 */     if (bool) {
/* 2251 */       if (b1 != 0) {
/* 2252 */         paramIndentingWriter.p("result = ");
/*      */       } else {
/* 2254 */         paramIndentingWriter.p(getName(type) + " result = ");
/*      */       }
/*      */     }
/*      */
/* 2258 */     paramIndentingWriter.p("target." + str1 + "("); byte b2;
/* 2259 */     for (b2 = 0; b2 < arrayOfString.length; b2++) {
/* 2260 */       if (b2 > 0)
/* 2261 */         paramIndentingWriter.p(", ");
/* 2262 */       paramIndentingWriter.p(arrayOfString[b2]);
/*      */     }
/* 2264 */     paramIndentingWriter.pln(");");
/*      */
/* 2266 */     if (b1 != 0) {
/* 2267 */       for (b2 = 0; b2 < arrayOfValueType.length; b2++) {
/* 2268 */         paramIndentingWriter.pOlnI("} catch (" + getName(arrayOfValueType[b2]) + " " + str3 + ") {");
/*      */
/*      */
/*      */
/* 2272 */         if (arrayOfValueType[b2].isIDLEntityException() && !arrayOfValueType[b2].isCORBAUserException()) {
/*      */
/*      */
/*      */
/* 2276 */           String str = IDLNames.replace(arrayOfValueType[b2].getQualifiedIDLName(false), "::", ".");
/* 2277 */           str = str + "Helper";
/* 2278 */           paramIndentingWriter.pln(idOutputStream + " " + str4 + " = " + str5 + ".createExceptionReply();");
/* 2279 */           paramIndentingWriter.pln(str + ".write(" + str4 + "," + str3 + ");");
/*      */
/*      */         }
/*      */         else {
/*      */
/*      */
/* 2285 */           paramIndentingWriter.pln("String id = \"" + getExceptionRepositoryID(arrayOfValueType[b2]) + "\";");
/* 2286 */           paramIndentingWriter.plnI(idExtOutputStream + " " + str4 + " = ");
/* 2287 */           paramIndentingWriter.pln("(" + idExtOutputStream + ") " + str5 + ".createExceptionReply();");
/* 2288 */           paramIndentingWriter.pOln(str4 + ".write_string(id);");
/* 2289 */           paramIndentingWriter.pln(str4 + ".write_value(" + str3 + "," + getName(arrayOfValueType[b2]) + ".class);");
/*      */         }
/*      */
/* 2292 */         paramIndentingWriter.pln("return " + str4 + ";");
/*      */       }
/* 2294 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */
/* 2297 */     if (needNewWriteStreamClass(type)) {
/* 2298 */       paramIndentingWriter.plnI(idExtOutputStream + " " + str4 + " = ");
/* 2299 */       paramIndentingWriter.pln("(" + idExtOutputStream + ") " + str5 + ".createReply();");
/* 2300 */       paramIndentingWriter.pO();
/*      */     } else {
/* 2302 */       paramIndentingWriter.pln("OutputStream " + str4 + " = " + str5 + ".createReply();");
/*      */     }
/*      */
/* 2305 */     if (bool) {
/* 2306 */       writeMarshalArgument(paramIndentingWriter, str4, type, "result");
/* 2307 */       paramIndentingWriter.pln();
/*      */     }
/*      */
/* 2310 */     paramIndentingWriter.pln("return " + str4 + ";");
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
/*      */   void writeMarshalArguments(IndentingWriter paramIndentingWriter, String paramString, Type[] paramArrayOfType, String[] paramArrayOfString) throws IOException {
/* 2324 */     if (paramArrayOfType.length != paramArrayOfString.length) {
/* 2325 */       throw new Error("paramter type and name arrays different sizes");
/*      */     }
/*      */
/* 2328 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/* 2329 */       writeMarshalArgument(paramIndentingWriter, paramString, paramArrayOfType[b], paramArrayOfString[b]);
/* 2330 */       if (b != paramArrayOfType.length - 1) {
/* 2331 */         paramIndentingWriter.pln();
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   String testUtil(String paramString, Type paramType) {
/* 2342 */     if (paramString.equals("Util")) {
/* 2343 */       return paramType.getPackageName() + "." + paramString;
/*      */     }
/*      */
/* 2346 */     return paramString;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\StubGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
