/*      */ package sun.rmi.rmic;
/*      */ 
/*      */ import com.sun.corba.se.impl.util.Utility;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import sun.tools.java.ClassDeclaration;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.ClassFile;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.Environment;
/*      */ import sun.tools.java.Identifier;
/*      */ import sun.tools.java.MemberDefinition;
/*      */ import sun.tools.java.Type;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RMIGenerator
/*      */   implements RMIConstants, Generator
/*      */ {
/*      */   private BatchEnvironment env;
/*      */   private RemoteClass remoteClass;
/*      */   private int version;
/*      */   private RemoteClass.Method[] remoteMethods;
/*   64 */   private static final Hashtable<String, Integer> versionOptions = new Hashtable<>(); private Identifier remoteClassName; private Identifier stubClassName; private Identifier skeletonClassName;
/*      */   static {
/*   66 */     versionOptions.put("-v1.1", new Integer(1));
/*   67 */     versionOptions.put("-vcompat", new Integer(2));
/*   68 */     versionOptions.put("-v1.2", new Integer(3));
/*      */   }
/*      */   private ClassDefinition cdef; private File destDir;
/*      */   private File stubFile;
/*      */   private File skeletonFile;
/*      */   
/*      */   public RMIGenerator() {
/*   75 */     this.version = 3;
/*      */   }
/*      */ 
/*      */   
/*      */   private String[] methodFieldNames;
/*      */   
/*      */   private ClassDefinition defException;
/*      */   private ClassDefinition defRemoteException;
/*      */   private ClassDefinition defRuntimeException;
/*      */   
/*      */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain) {
/*   86 */     String str = null;
/*   87 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*   88 */       if (paramArrayOfString[b] != null) {
/*   89 */         String str1 = paramArrayOfString[b].toLowerCase();
/*   90 */         if (versionOptions.containsKey(str1)) {
/*   91 */           if (str != null && 
/*   92 */             !str.equals(str1)) {
/*      */             
/*   94 */             paramMain.error("rmic.cannot.use.both", str, str1);
/*      */             
/*   96 */             return false;
/*      */           } 
/*   98 */           str = str1;
/*   99 */           this.version = ((Integer)versionOptions.get(str1)).intValue();
/*  100 */           paramArrayOfString[b] = null;
/*      */         } 
/*      */       } 
/*      */     } 
/*  104 */     return true;
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
/*      */   public void generate(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile) {
/*      */     RMIGenerator rMIGenerator;
/*  118 */     RemoteClass remoteClass = RemoteClass.forClass(paramBatchEnvironment, paramClassDefinition);
/*  119 */     if (remoteClass == null) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/*  124 */       rMIGenerator = new RMIGenerator(paramBatchEnvironment, paramClassDefinition, paramFile, remoteClass, this.version);
/*  125 */     } catch (ClassNotFound classNotFound) {
/*  126 */       paramBatchEnvironment.error(0L, "rmic.class.not.found", classNotFound.name);
/*      */       return;
/*      */     } 
/*  129 */     rMIGenerator.generate();
/*      */   }
/*      */   
/*      */   private void generate() {
/*  133 */     this.env.addGeneratedFile(this.stubFile);
/*      */     
/*      */     try {
/*  136 */       IndentingWriter indentingWriter = new IndentingWriter(new OutputStreamWriter(new FileOutputStream(this.stubFile)));
/*      */       
/*  138 */       writeStub(indentingWriter);
/*  139 */       indentingWriter.close();
/*  140 */       if (this.env.verbose()) {
/*  141 */         this.env.output(Main.getText("rmic.wrote", this.stubFile.getPath()));
/*      */       }
/*  143 */       this.env.parseFile(new ClassFile(this.stubFile));
/*  144 */     } catch (IOException iOException) {
/*  145 */       this.env.error(0L, "cant.write", this.stubFile.toString());
/*      */       
/*      */       return;
/*      */     } 
/*  149 */     if (this.version == 1 || this.version == 2) {
/*      */ 
/*      */       
/*  152 */       this.env.addGeneratedFile(this.skeletonFile);
/*      */       
/*      */       try {
/*  155 */         IndentingWriter indentingWriter = new IndentingWriter(new OutputStreamWriter(new FileOutputStream(this.skeletonFile)));
/*      */ 
/*      */         
/*  158 */         writeSkeleton(indentingWriter);
/*  159 */         indentingWriter.close();
/*  160 */         if (this.env.verbose()) {
/*  161 */           this.env.output(Main.getText("rmic.wrote", this.skeletonFile
/*  162 */                 .getPath()));
/*      */         }
/*  164 */         this.env.parseFile(new ClassFile(this.skeletonFile));
/*  165 */       } catch (IOException iOException) {
/*  166 */         this.env.error(0L, "cant.write", this.stubFile.toString());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } else {
/*  178 */       File file1 = Util.getOutputDirectoryFor(this.remoteClassName, this.destDir, this.env);
/*  179 */       File file2 = new File(file1, this.skeletonClassName.getName().toString() + ".class");
/*      */       
/*  181 */       this.skeletonFile.delete();
/*  182 */       file2.delete();
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
/*      */   protected static File sourceFileForClass(Identifier paramIdentifier1, Identifier paramIdentifier2, File paramFile, BatchEnvironment paramBatchEnvironment) {
/*  196 */     File file = Util.getOutputDirectoryFor(paramIdentifier1, paramFile, paramBatchEnvironment);
/*  197 */     String str1 = Names.mangleClass(paramIdentifier2).getName().toString();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  203 */     if (str1.endsWith("_Skel")) {
/*  204 */       String str = paramIdentifier1.getName().toString();
/*  205 */       File file1 = new File(file, Utility.tieName(str) + ".class");
/*  206 */       if (file1.exists())
/*      */       {
/*      */ 
/*      */         
/*  210 */         if (!(paramBatchEnvironment.getMain()).iiopGeneration)
/*      */         {
/*      */ 
/*      */           
/*  214 */           paramBatchEnvironment.error(0L, "warn.rmic.tie.found", str, file1
/*      */               
/*  216 */               .getAbsolutePath());
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/*  221 */     String str2 = str1 + ".java";
/*  222 */     return new File(file, str2);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private RMIGenerator(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile, RemoteClass paramRemoteClass, int paramInt) throws ClassNotFound {
/*  271 */     this.destDir = paramFile;
/*  272 */     this.cdef = paramClassDefinition;
/*  273 */     this.env = paramBatchEnvironment;
/*  274 */     this.remoteClass = paramRemoteClass;
/*  275 */     this.version = paramInt;
/*      */     
/*  277 */     this.remoteMethods = paramRemoteClass.getRemoteMethods();
/*      */     
/*  279 */     this.remoteClassName = paramRemoteClass.getName();
/*  280 */     this.stubClassName = Names.stubFor(this.remoteClassName);
/*  281 */     this.skeletonClassName = Names.skeletonFor(this.remoteClassName);
/*      */     
/*  283 */     this.methodFieldNames = nameMethodFields(this.remoteMethods);
/*      */     
/*  285 */     this.stubFile = sourceFileForClass(this.remoteClassName, this.stubClassName, paramFile, paramBatchEnvironment);
/*  286 */     this.skeletonFile = sourceFileForClass(this.remoteClassName, this.skeletonClassName, paramFile, paramBatchEnvironment);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  292 */     this
/*      */       
/*  294 */       .defException = paramBatchEnvironment.getClassDeclaration(idJavaLangException).getClassDefinition((Environment)paramBatchEnvironment);
/*  295 */     this
/*      */       
/*  297 */       .defRemoteException = paramBatchEnvironment.getClassDeclaration(idRemoteException).getClassDefinition((Environment)paramBatchEnvironment);
/*  298 */     this
/*      */       
/*  300 */       .defRuntimeException = paramBatchEnvironment.getClassDeclaration(idJavaLangRuntimeException).getClassDefinition((Environment)paramBatchEnvironment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeStub(IndentingWriter paramIndentingWriter) throws IOException {
/*  311 */     paramIndentingWriter.pln("// Stub class generated by rmic, do not edit.");
/*  312 */     paramIndentingWriter.pln("// Contents subject to change without notice.");
/*  313 */     paramIndentingWriter.pln();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  319 */     if (this.remoteClassName.isQualified()) {
/*  320 */       paramIndentingWriter.pln("package " + this.remoteClassName.getQualifier() + ";");
/*  321 */       paramIndentingWriter.pln();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  327 */     paramIndentingWriter.plnI("public final class " + 
/*  328 */         Names.mangleClass(this.stubClassName.getName()));
/*  329 */     paramIndentingWriter.pln("extends " + idRemoteStub);
/*  330 */     ClassDefinition[] arrayOfClassDefinition = this.remoteClass.getRemoteInterfaces();
/*  331 */     if (arrayOfClassDefinition.length > 0) {
/*  332 */       paramIndentingWriter.p("implements ");
/*  333 */       for (byte b = 0; b < arrayOfClassDefinition.length; b++) {
/*  334 */         if (b > 0)
/*  335 */           paramIndentingWriter.p(", "); 
/*  336 */         paramIndentingWriter.p(arrayOfClassDefinition[b].getName().toString());
/*      */       } 
/*  338 */       paramIndentingWriter.pln();
/*      */     } 
/*  340 */     paramIndentingWriter.pOlnI("{");
/*      */     
/*  342 */     if (this.version == 1 || this.version == 2) {
/*      */ 
/*      */       
/*  345 */       writeOperationsArray(paramIndentingWriter);
/*  346 */       paramIndentingWriter.pln();
/*  347 */       writeInterfaceHash(paramIndentingWriter);
/*  348 */       paramIndentingWriter.pln();
/*      */     } 
/*      */     
/*  351 */     if (this.version == 2 || this.version == 3) {
/*      */ 
/*      */       
/*  354 */       paramIndentingWriter.pln("private static final long serialVersionUID = 2;");
/*      */       
/*  356 */       paramIndentingWriter.pln();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  365 */       if (this.methodFieldNames.length > 0) {
/*  366 */         if (this.version == 2) {
/*  367 */           paramIndentingWriter.pln("private static boolean useNewInvoke;");
/*      */         }
/*  369 */         writeMethodFieldDeclarations(paramIndentingWriter);
/*  370 */         paramIndentingWriter.pln();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  376 */         paramIndentingWriter.plnI("static {");
/*  377 */         paramIndentingWriter.plnI("try {");
/*  378 */         if (this.version == 2) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  388 */           paramIndentingWriter.plnI(idRemoteRef + ".class.getMethod(\"invoke\",");
/*  389 */           paramIndentingWriter.plnI("new java.lang.Class[] {");
/*  390 */           paramIndentingWriter.pln(idRemote + ".class,");
/*  391 */           paramIndentingWriter.pln("java.lang.reflect.Method.class,");
/*  392 */           paramIndentingWriter.pln("java.lang.Object[].class,");
/*  393 */           paramIndentingWriter.pln("long.class");
/*  394 */           paramIndentingWriter.pOln("});");
/*  395 */           paramIndentingWriter.pO();
/*  396 */           paramIndentingWriter.pln("useNewInvoke = true;");
/*      */         } 
/*  398 */         writeMethodFieldInitializers(paramIndentingWriter);
/*  399 */         paramIndentingWriter.pOlnI("} catch (java.lang.NoSuchMethodException e) {");
/*  400 */         if (this.version == 2) {
/*  401 */           paramIndentingWriter.pln("useNewInvoke = false;");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  412 */           paramIndentingWriter.plnI("throw new java.lang.NoSuchMethodError(");
/*  413 */           paramIndentingWriter.pln("\"stub class initialization failed\");");
/*  414 */           paramIndentingWriter.pO();
/*      */         } 
/*  416 */         paramIndentingWriter.pOln("}");
/*  417 */         paramIndentingWriter.pOln("}");
/*  418 */         paramIndentingWriter.pln();
/*      */       } 
/*      */     } 
/*      */     
/*  422 */     writeStubConstructors(paramIndentingWriter);
/*  423 */     paramIndentingWriter.pln();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  428 */     if (this.remoteMethods.length > 0) {
/*  429 */       paramIndentingWriter.pln("// methods from remote interfaces");
/*  430 */       for (byte b = 0; b < this.remoteMethods.length; b++) {
/*  431 */         paramIndentingWriter.pln();
/*  432 */         writeStubMethod(paramIndentingWriter, b);
/*      */       } 
/*      */     } 
/*      */     
/*  436 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeStubConstructors(IndentingWriter paramIndentingWriter) throws IOException {
/*  445 */     paramIndentingWriter.pln("// constructors");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  452 */     if (this.version == 1 || this.version == 2) {
/*      */ 
/*      */       
/*  455 */       paramIndentingWriter.plnI("public " + Names.mangleClass(this.stubClassName.getName()) + "() {");
/*      */       
/*  457 */       paramIndentingWriter.pln("super();");
/*  458 */       paramIndentingWriter.pOln("}");
/*      */     } 
/*      */     
/*  461 */     paramIndentingWriter.plnI("public " + Names.mangleClass(this.stubClassName.getName()) + "(" + idRemoteRef + " ref) {");
/*      */     
/*  463 */     paramIndentingWriter.pln("super(ref);");
/*  464 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeStubMethod(IndentingWriter paramIndentingWriter, int paramInt) throws IOException {
/*  473 */     RemoteClass.Method method = this.remoteMethods[paramInt];
/*  474 */     Identifier identifier = method.getName();
/*  475 */     Type type1 = method.getType();
/*  476 */     Type[] arrayOfType = type1.getArgumentTypes();
/*  477 */     String[] arrayOfString = nameParameters(arrayOfType);
/*  478 */     Type type2 = type1.getReturnType();
/*  479 */     ClassDeclaration[] arrayOfClassDeclaration = method.getExceptions();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  485 */     paramIndentingWriter.pln("// implementation of " + type1
/*  486 */         .typeString(identifier.toString(), true, false));
/*  487 */     paramIndentingWriter.p("public " + type2 + " " + identifier + "("); byte b;
/*  488 */     for (b = 0; b < arrayOfType.length; b++) {
/*  489 */       if (b > 0)
/*  490 */         paramIndentingWriter.p(", "); 
/*  491 */       paramIndentingWriter.p(arrayOfType[b] + " " + arrayOfString[b]);
/*      */     } 
/*  493 */     paramIndentingWriter.plnI(")");
/*  494 */     if (arrayOfClassDeclaration.length > 0) {
/*  495 */       paramIndentingWriter.p("throws ");
/*  496 */       for (b = 0; b < arrayOfClassDeclaration.length; b++) {
/*  497 */         if (b > 0)
/*  498 */           paramIndentingWriter.p(", "); 
/*  499 */         paramIndentingWriter.p(arrayOfClassDeclaration[b].getName().toString());
/*      */       } 
/*  501 */       paramIndentingWriter.pln();
/*      */     } 
/*  503 */     paramIndentingWriter.pOlnI("{");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  522 */     Vector<ClassDefinition> vector = computeUniqueCatchList(arrayOfClassDeclaration);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  529 */     if (vector.size() > 0) {
/*  530 */       paramIndentingWriter.plnI("try {");
/*      */     }
/*      */     
/*  533 */     if (this.version == 2) {
/*  534 */       paramIndentingWriter.plnI("if (useNewInvoke) {");
/*      */     }
/*  536 */     if (this.version == 2 || this.version == 3) {
/*      */ 
/*      */       
/*  539 */       if (!type2.isType(11)) {
/*  540 */         paramIndentingWriter.p("Object $result = ");
/*      */       }
/*  542 */       paramIndentingWriter.p("ref.invoke(this, " + this.methodFieldNames[paramInt] + ", ");
/*  543 */       if (arrayOfType.length > 0) {
/*  544 */         paramIndentingWriter.p("new java.lang.Object[] {");
/*  545 */         for (byte b1 = 0; b1 < arrayOfType.length; b1++) {
/*  546 */           if (b1 > 0)
/*  547 */             paramIndentingWriter.p(", "); 
/*  548 */           paramIndentingWriter.p(wrapArgumentCode(arrayOfType[b1], arrayOfString[b1]));
/*      */         } 
/*  550 */         paramIndentingWriter.p("}");
/*      */       } else {
/*  552 */         paramIndentingWriter.p("null");
/*      */       } 
/*  554 */       paramIndentingWriter.pln(", " + method.getMethodHash() + "L);");
/*  555 */       if (!type2.isType(11)) {
/*  556 */         paramIndentingWriter.pln("return " + 
/*  557 */             unwrapArgumentCode(type2, "$result") + ";");
/*      */       }
/*      */     } 
/*  560 */     if (this.version == 2) {
/*  561 */       paramIndentingWriter.pOlnI("} else {");
/*      */     }
/*  563 */     if (this.version == 1 || this.version == 2) {
/*      */ 
/*      */       
/*  566 */       paramIndentingWriter.pln(idRemoteCall + " call = ref.newCall((" + idRemoteObject + ") this, operations, " + paramInt + ", interfaceHash);");
/*      */ 
/*      */       
/*  569 */       if (arrayOfType.length > 0) {
/*  570 */         paramIndentingWriter.plnI("try {");
/*  571 */         paramIndentingWriter.pln("java.io.ObjectOutput out = call.getOutputStream();");
/*  572 */         writeMarshalArguments(paramIndentingWriter, "out", arrayOfType, arrayOfString);
/*  573 */         paramIndentingWriter.pOlnI("} catch (java.io.IOException e) {");
/*  574 */         paramIndentingWriter.pln("throw new " + idMarshalException + "(\"error marshalling arguments\", e);");
/*      */         
/*  576 */         paramIndentingWriter.pOln("}");
/*      */       } 
/*      */       
/*  579 */       paramIndentingWriter.pln("ref.invoke(call);");
/*      */       
/*  581 */       if (type2.isType(11)) {
/*  582 */         paramIndentingWriter.pln("ref.done(call);");
/*      */       } else {
/*  584 */         paramIndentingWriter.pln(type2 + " $result;");
/*  585 */         paramIndentingWriter.plnI("try {");
/*  586 */         paramIndentingWriter.pln("java.io.ObjectInput in = call.getInputStream();");
/*      */         
/*  588 */         boolean bool = writeUnmarshalArgument(paramIndentingWriter, "in", type2, "$result");
/*  589 */         paramIndentingWriter.pln(";");
/*  590 */         paramIndentingWriter.pOlnI("} catch (java.io.IOException e) {");
/*  591 */         paramIndentingWriter.pln("throw new " + idUnmarshalException + "(\"error unmarshalling return\", e);");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  597 */         if (bool) {
/*  598 */           paramIndentingWriter.pOlnI("} catch (java.lang.ClassNotFoundException e) {");
/*  599 */           paramIndentingWriter.pln("throw new " + idUnmarshalException + "(\"error unmarshalling return\", e);");
/*      */         } 
/*      */         
/*  602 */         paramIndentingWriter.pOlnI("} finally {");
/*  603 */         paramIndentingWriter.pln("ref.done(call);");
/*  604 */         paramIndentingWriter.pOln("}");
/*  605 */         paramIndentingWriter.pln("return $result;");
/*      */       } 
/*      */     } 
/*  608 */     if (this.version == 2) {
/*  609 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  617 */     if (vector.size() > 0) {
/*  618 */       Enumeration<ClassDefinition> enumeration = vector.elements();
/*  619 */       while (enumeration.hasMoreElements()) {
/*      */         
/*  621 */         ClassDefinition classDefinition = enumeration.nextElement();
/*  622 */         paramIndentingWriter.pOlnI("} catch (" + classDefinition.getName() + " e) {");
/*  623 */         paramIndentingWriter.pln("throw e;");
/*      */       } 
/*  625 */       paramIndentingWriter.pOlnI("} catch (java.lang.Exception e) {");
/*  626 */       paramIndentingWriter.pln("throw new " + idUnexpectedException + "(\"undeclared checked exception\", e);");
/*      */       
/*  628 */       paramIndentingWriter.pOln("}");
/*      */     } 
/*      */     
/*  631 */     paramIndentingWriter.pOln("}");
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
/*      */   private Vector<ClassDefinition> computeUniqueCatchList(ClassDeclaration[] paramArrayOfClassDeclaration) {
/*  654 */     Vector<ClassDefinition> vector = new Vector();
/*      */     
/*  656 */     vector.addElement(this.defRuntimeException);
/*  657 */     vector.addElement(this.defRemoteException);
/*      */ 
/*      */ 
/*      */     
/*  661 */     for (byte b = 0; b < paramArrayOfClassDeclaration.length; b++) {
/*  662 */       ClassDeclaration classDeclaration = paramArrayOfClassDeclaration[b];
/*      */       try {
/*  664 */         if (this.defException.subClassOf((Environment)this.env, classDeclaration)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  671 */           vector.clear(); break;
/*      */         } 
/*  673 */         if (this.defException.superClassOf((Environment)this.env, classDeclaration))
/*      */         
/*      */         { 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  684 */           byte b1 = 0; while (true) { if (b1 < vector.size()) {
/*  685 */               ClassDefinition classDefinition = vector.elementAt(b1);
/*  686 */               if (classDefinition.superClassOf((Environment)this.env, classDeclaration)) {
/*      */                 break;
/*      */               }
/*      */ 
/*      */ 
/*      */               
/*  692 */               if (classDefinition.subClassOf((Environment)this.env, classDeclaration)) {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  697 */                 vector.removeElementAt(b1); continue;
/*      */               } 
/*  699 */               b1++;
/*      */               
/*      */               continue;
/*      */             } 
/*  703 */             vector.addElement(classDeclaration.getClassDefinition((Environment)this.env)); break; }  } 
/*  704 */       } catch (ClassNotFound classNotFound) {
/*  705 */         this.env.error(0L, "class.not.found", classNotFound.name, classDeclaration.getName());
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  713 */     return vector;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeSkeleton(IndentingWriter paramIndentingWriter) throws IOException {
/*  720 */     if (this.version == 3) {
/*  721 */       throw new Error("should not generate skeleton for version");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  727 */     paramIndentingWriter.pln("// Skeleton class generated by rmic, do not edit.");
/*  728 */     paramIndentingWriter.pln("// Contents subject to change without notice.");
/*  729 */     paramIndentingWriter.pln();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  735 */     if (this.remoteClassName.isQualified()) {
/*  736 */       paramIndentingWriter.pln("package " + this.remoteClassName.getQualifier() + ";");
/*  737 */       paramIndentingWriter.pln();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  743 */     paramIndentingWriter.plnI("public final class " + 
/*  744 */         Names.mangleClass(this.skeletonClassName.getName()));
/*  745 */     paramIndentingWriter.pln("implements " + idSkeleton);
/*  746 */     paramIndentingWriter.pOlnI("{");
/*      */     
/*  748 */     writeOperationsArray(paramIndentingWriter);
/*  749 */     paramIndentingWriter.pln();
/*      */     
/*  751 */     writeInterfaceHash(paramIndentingWriter);
/*  752 */     paramIndentingWriter.pln();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  757 */     paramIndentingWriter.plnI("public " + idOperation + "[] getOperations() {");
/*  758 */     paramIndentingWriter.pln("return (" + idOperation + "[]) operations.clone();");
/*  759 */     paramIndentingWriter.pOln("}");
/*  760 */     paramIndentingWriter.pln();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  765 */     paramIndentingWriter.plnI("public void dispatch(" + idRemote + " obj, " + idRemoteCall + " call, int opnum, long hash)");
/*      */     
/*  767 */     paramIndentingWriter.pln("throws java.lang.Exception");
/*  768 */     paramIndentingWriter.pOlnI("{");
/*      */     
/*  770 */     if (this.version == 2) {
/*  771 */       paramIndentingWriter.plnI("if (opnum < 0) {");
/*  772 */       if (this.remoteMethods.length > 0) {
/*  773 */         for (byte b1 = 0; b1 < this.remoteMethods.length; b1++) {
/*  774 */           if (b1 > 0)
/*  775 */             paramIndentingWriter.pO("} else "); 
/*  776 */           paramIndentingWriter.plnI("if (hash == " + this.remoteMethods[b1]
/*  777 */               .getMethodHash() + "L) {");
/*  778 */           paramIndentingWriter.pln("opnum = " + b1 + ";");
/*      */         } 
/*  780 */         paramIndentingWriter.pOlnI("} else {");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  787 */       paramIndentingWriter.pln("throw new " + idUnmarshalException + "(\"invalid method hash\");");
/*      */       
/*  789 */       if (this.remoteMethods.length > 0) {
/*  790 */         paramIndentingWriter.pOln("}");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  797 */       paramIndentingWriter.pOlnI("} else {");
/*      */     } 
/*      */     
/*  800 */     paramIndentingWriter.plnI("if (hash != interfaceHash)");
/*  801 */     paramIndentingWriter.pln("throw new " + idSkeletonMismatchException + "(\"interface hash mismatch\");");
/*      */     
/*  803 */     paramIndentingWriter.pO();
/*      */     
/*  805 */     if (this.version == 2) {
/*  806 */       paramIndentingWriter.pOln("}");
/*      */     }
/*  808 */     paramIndentingWriter.pln();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  813 */     paramIndentingWriter.pln(this.remoteClassName + " server = (" + this.remoteClassName + ") obj;");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  818 */     paramIndentingWriter.plnI("switch (opnum) {");
/*  819 */     for (byte b = 0; b < this.remoteMethods.length; b++) {
/*  820 */       writeSkeletonDispatchCase(paramIndentingWriter, b);
/*      */     }
/*  822 */     paramIndentingWriter.pOlnI("default:");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  828 */     paramIndentingWriter.pln("throw new " + idUnmarshalException + "(\"invalid method number\");");
/*      */     
/*  830 */     paramIndentingWriter.pOln("}");
/*      */     
/*  832 */     paramIndentingWriter.pOln("}");
/*      */     
/*  834 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeSkeletonDispatchCase(IndentingWriter paramIndentingWriter, int paramInt) throws IOException {
/*  844 */     RemoteClass.Method method = this.remoteMethods[paramInt];
/*  845 */     Identifier identifier = method.getName();
/*  846 */     Type type1 = method.getType();
/*  847 */     Type[] arrayOfType = type1.getArgumentTypes();
/*  848 */     String[] arrayOfString = nameParameters(arrayOfType);
/*  849 */     Type type2 = type1.getReturnType();
/*      */     
/*  851 */     paramIndentingWriter.pOlnI("case " + paramInt + ": // " + type1
/*  852 */         .typeString(identifier.toString(), true, false));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  858 */     paramIndentingWriter.pOlnI("{");
/*      */     
/*  860 */     if (arrayOfType.length > 0) {
/*      */ 
/*      */ 
/*      */       
/*  864 */       for (byte b1 = 0; b1 < arrayOfType.length; b1++) {
/*  865 */         paramIndentingWriter.pln(arrayOfType[b1] + " " + arrayOfString[b1] + ";");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  871 */       paramIndentingWriter.plnI("try {");
/*  872 */       paramIndentingWriter.pln("java.io.ObjectInput in = call.getInputStream();");
/*  873 */       boolean bool = writeUnmarshalArguments(paramIndentingWriter, "in", arrayOfType, arrayOfString);
/*      */       
/*  875 */       paramIndentingWriter.pOlnI("} catch (java.io.IOException e) {");
/*  876 */       paramIndentingWriter.pln("throw new " + idUnmarshalException + "(\"error unmarshalling arguments\", e);");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  882 */       if (bool) {
/*  883 */         paramIndentingWriter.pOlnI("} catch (java.lang.ClassNotFoundException e) {");
/*  884 */         paramIndentingWriter.pln("throw new " + idUnmarshalException + "(\"error unmarshalling arguments\", e);");
/*      */       } 
/*      */       
/*  887 */       paramIndentingWriter.pOlnI("} finally {");
/*  888 */       paramIndentingWriter.pln("call.releaseInputStream();");
/*  889 */       paramIndentingWriter.pOln("}");
/*      */     } else {
/*  891 */       paramIndentingWriter.pln("call.releaseInputStream();");
/*      */     } 
/*      */     
/*  894 */     if (!type2.isType(11))
/*      */     {
/*      */ 
/*      */       
/*  898 */       paramIndentingWriter.p(type2 + " $result = ");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  904 */     paramIndentingWriter.p("server." + identifier + "(");
/*  905 */     for (byte b = 0; b < arrayOfString.length; b++) {
/*  906 */       if (b > 0)
/*  907 */         paramIndentingWriter.p(", "); 
/*  908 */       paramIndentingWriter.p(arrayOfString[b]);
/*      */     } 
/*  910 */     paramIndentingWriter.pln(");");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  918 */     paramIndentingWriter.plnI("try {");
/*  919 */     if (!type2.isType(11)) {
/*  920 */       paramIndentingWriter.p("java.io.ObjectOutput out = ");
/*      */     }
/*  922 */     paramIndentingWriter.pln("call.getResultStream(true);");
/*  923 */     if (!type2.isType(11)) {
/*  924 */       writeMarshalArgument(paramIndentingWriter, "out", type2, "$result");
/*  925 */       paramIndentingWriter.pln(";");
/*      */     } 
/*  927 */     paramIndentingWriter.pOlnI("} catch (java.io.IOException e) {");
/*  928 */     paramIndentingWriter.pln("throw new " + idMarshalException + "(\"error marshalling return\", e);");
/*      */     
/*  930 */     paramIndentingWriter.pOln("}");
/*      */     
/*  932 */     paramIndentingWriter.pln("break;");
/*      */     
/*  934 */     paramIndentingWriter.pOlnI("}");
/*  935 */     paramIndentingWriter.pln();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeOperationsArray(IndentingWriter paramIndentingWriter) throws IOException {
/*  944 */     paramIndentingWriter.plnI("private static final " + idOperation + "[] operations = {");
/*  945 */     for (byte b = 0; b < this.remoteMethods.length; b++) {
/*  946 */       if (b > 0)
/*  947 */         paramIndentingWriter.pln(","); 
/*  948 */       paramIndentingWriter.p("new " + idOperation + "(\"" + this.remoteMethods[b]
/*  949 */           .getOperationString() + "\")");
/*      */     } 
/*  951 */     paramIndentingWriter.pln();
/*  952 */     paramIndentingWriter.pOln("};");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeInterfaceHash(IndentingWriter paramIndentingWriter) throws IOException {
/*  961 */     paramIndentingWriter.pln("private static final long interfaceHash = " + this.remoteClass
/*  962 */         .getInterfaceHash() + "L;");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeMethodFieldDeclarations(IndentingWriter paramIndentingWriter) throws IOException {
/*  972 */     for (byte b = 0; b < this.methodFieldNames.length; b++) {
/*  973 */       paramIndentingWriter.pln("private static java.lang.reflect.Method " + this.methodFieldNames[b] + ";");
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
/*      */   private void writeMethodFieldInitializers(IndentingWriter paramIndentingWriter) throws IOException {
/*  985 */     for (byte b = 0; b < this.methodFieldNames.length; b++) {
/*  986 */       paramIndentingWriter.p(this.methodFieldNames[b] + " = ");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  994 */       RemoteClass.Method method = this.remoteMethods[b];
/*  995 */       MemberDefinition memberDefinition = method.getMemberDefinition();
/*  996 */       Identifier identifier = method.getName();
/*  997 */       Type type = method.getType();
/*  998 */       Type[] arrayOfType = type.getArgumentTypes();
/*      */       
/* 1000 */       paramIndentingWriter.p(memberDefinition.getClassDefinition().getName() + ".class.getMethod(\"" + identifier + "\", new java.lang.Class[] {");
/*      */       
/* 1002 */       for (byte b1 = 0; b1 < arrayOfType.length; b1++) {
/* 1003 */         if (b1 > 0)
/* 1004 */           paramIndentingWriter.p(", "); 
/* 1005 */         paramIndentingWriter.p(arrayOfType[b1] + ".class");
/*      */       } 
/* 1007 */       paramIndentingWriter.pln("});");
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
/*      */   private static String[] nameMethodFields(RemoteClass.Method[] paramArrayOfMethod) {
/* 1026 */     String[] arrayOfString = new String[paramArrayOfMethod.length];
/* 1027 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 1028 */       arrayOfString[b] = "$method_" + paramArrayOfMethod[b].getName() + "_" + b;
/*      */     }
/* 1030 */     return arrayOfString;
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
/*      */   private static String[] nameParameters(Type[] paramArrayOfType) {
/* 1043 */     String[] arrayOfString = new String[paramArrayOfType.length];
/* 1044 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 1045 */       arrayOfString[b] = "$param_" + 
/* 1046 */         generateNameFromType(paramArrayOfType[b]) + "_" + (b + 1);
/*      */     }
/* 1048 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String generateNameFromType(Type paramType) {
/* 1056 */     int i = paramType.getTypeCode();
/* 1057 */     switch (i) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/* 1066 */         return paramType.toString();
/*      */       case 9:
/* 1068 */         return "arrayOf_" + generateNameFromType(paramType.getElementType());
/*      */       case 10:
/* 1070 */         return Names.mangleClass(paramType.getClassName().getName()).toString();
/*      */     } 
/* 1072 */     throw new Error("unexpected type code: " + i);
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
/*      */   private static void writeMarshalArgument(IndentingWriter paramIndentingWriter, String paramString1, Type paramType, String paramString2) throws IOException {
/* 1089 */     int i = paramType.getTypeCode();
/* 1090 */     switch (i) {
/*      */       case 0:
/* 1092 */         paramIndentingWriter.p(paramString1 + ".writeBoolean(" + paramString2 + ")");
/*      */         return;
/*      */       case 1:
/* 1095 */         paramIndentingWriter.p(paramString1 + ".writeByte(" + paramString2 + ")");
/*      */         return;
/*      */       case 2:
/* 1098 */         paramIndentingWriter.p(paramString1 + ".writeChar(" + paramString2 + ")");
/*      */         return;
/*      */       case 3:
/* 1101 */         paramIndentingWriter.p(paramString1 + ".writeShort(" + paramString2 + ")");
/*      */         return;
/*      */       case 4:
/* 1104 */         paramIndentingWriter.p(paramString1 + ".writeInt(" + paramString2 + ")");
/*      */         return;
/*      */       case 5:
/* 1107 */         paramIndentingWriter.p(paramString1 + ".writeLong(" + paramString2 + ")");
/*      */         return;
/*      */       case 6:
/* 1110 */         paramIndentingWriter.p(paramString1 + ".writeFloat(" + paramString2 + ")");
/*      */         return;
/*      */       case 7:
/* 1113 */         paramIndentingWriter.p(paramString1 + ".writeDouble(" + paramString2 + ")");
/*      */         return;
/*      */       case 9:
/*      */       case 10:
/* 1117 */         paramIndentingWriter.p(paramString1 + ".writeObject(" + paramString2 + ")");
/*      */         return;
/*      */     } 
/* 1120 */     throw new Error("unexpected type code: " + i);
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
/*      */   private static void writeMarshalArguments(IndentingWriter paramIndentingWriter, String paramString, Type[] paramArrayOfType, String[] paramArrayOfString) throws IOException {
/* 1134 */     if (paramArrayOfType.length != paramArrayOfString.length) {
/* 1135 */       throw new Error("parameter type and name arrays different sizes");
/*      */     }
/*      */     
/* 1138 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/* 1139 */       writeMarshalArgument(paramIndentingWriter, paramString, paramArrayOfType[b], paramArrayOfString[b]);
/* 1140 */       paramIndentingWriter.pln(";");
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
/*      */   private static boolean writeUnmarshalArgument(IndentingWriter paramIndentingWriter, String paramString1, Type paramType, String paramString2) throws IOException {
/* 1159 */     boolean bool = false;
/*      */     
/* 1161 */     if (paramString2 != null) {
/* 1162 */       paramIndentingWriter.p(paramString2 + " = ");
/*      */     }
/*      */     
/* 1165 */     int i = paramType.getTypeCode();
/* 1166 */     switch (paramType.getTypeCode()) {
/*      */       case 0:
/* 1168 */         paramIndentingWriter.p(paramString1 + ".readBoolean()");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1199 */         return bool;case 1: paramIndentingWriter.p(paramString1 + ".readByte()"); return bool;case 2: paramIndentingWriter.p(paramString1 + ".readChar()"); return bool;case 3: paramIndentingWriter.p(paramString1 + ".readShort()"); return bool;case 4: paramIndentingWriter.p(paramString1 + ".readInt()"); return bool;case 5: paramIndentingWriter.p(paramString1 + ".readLong()"); return bool;case 6: paramIndentingWriter.p(paramString1 + ".readFloat()"); return bool;case 7: paramIndentingWriter.p(paramString1 + ".readDouble()"); return bool;case 9: case 10: paramIndentingWriter.p("(" + paramType + ") " + paramString1 + ".readObject()"); bool = true; return bool;
/*      */     } 
/*      */     throw new Error("unexpected type code: " + i);
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
/*      */   private static boolean writeUnmarshalArguments(IndentingWriter paramIndentingWriter, String paramString, Type[] paramArrayOfType, String[] paramArrayOfString) throws IOException {
/* 1215 */     if (paramArrayOfType.length != paramArrayOfString.length) {
/* 1216 */       throw new Error("parameter type and name arrays different sizes");
/*      */     }
/*      */     
/* 1219 */     boolean bool = false;
/* 1220 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/* 1221 */       if (writeUnmarshalArgument(paramIndentingWriter, paramString, paramArrayOfType[b], paramArrayOfString[b])) {
/* 1222 */         bool = true;
/*      */       }
/* 1224 */       paramIndentingWriter.pln(";");
/*      */     } 
/* 1226 */     return bool;
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
/*      */   private static String wrapArgumentCode(Type paramType, String paramString) {
/* 1239 */     int i = paramType.getTypeCode();
/* 1240 */     switch (i) {
/*      */       case 0:
/* 1242 */         return "(" + paramString + " ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE)";
/*      */       
/*      */       case 1:
/* 1245 */         return "new java.lang.Byte(" + paramString + ")";
/*      */       case 2:
/* 1247 */         return "new java.lang.Character(" + paramString + ")";
/*      */       case 3:
/* 1249 */         return "new java.lang.Short(" + paramString + ")";
/*      */       case 4:
/* 1251 */         return "new java.lang.Integer(" + paramString + ")";
/*      */       case 5:
/* 1253 */         return "new java.lang.Long(" + paramString + ")";
/*      */       case 6:
/* 1255 */         return "new java.lang.Float(" + paramString + ")";
/*      */       case 7:
/* 1257 */         return "new java.lang.Double(" + paramString + ")";
/*      */       case 9:
/*      */       case 10:
/* 1260 */         return paramString;
/*      */     } 
/* 1262 */     throw new Error("unexpected type code: " + i);
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
/*      */   private static String unwrapArgumentCode(Type paramType, String paramString) {
/* 1277 */     int i = paramType.getTypeCode();
/* 1278 */     switch (i) {
/*      */       case 0:
/* 1280 */         return "((java.lang.Boolean) " + paramString + ").booleanValue()";
/*      */       case 1:
/* 1282 */         return "((java.lang.Byte) " + paramString + ").byteValue()";
/*      */       case 2:
/* 1284 */         return "((java.lang.Character) " + paramString + ").charValue()";
/*      */       case 3:
/* 1286 */         return "((java.lang.Short) " + paramString + ").shortValue()";
/*      */       case 4:
/* 1288 */         return "((java.lang.Integer) " + paramString + ").intValue()";
/*      */       case 5:
/* 1290 */         return "((java.lang.Long) " + paramString + ").longValue()";
/*      */       case 6:
/* 1292 */         return "((java.lang.Float) " + paramString + ").floatValue()";
/*      */       case 7:
/* 1294 */         return "((java.lang.Double) " + paramString + ").doubleValue()";
/*      */       case 9:
/*      */       case 10:
/* 1297 */         return "((" + paramType + ") " + paramString + ")";
/*      */     } 
/* 1299 */     throw new Error("unexpected type code: " + i);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\RMIGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */