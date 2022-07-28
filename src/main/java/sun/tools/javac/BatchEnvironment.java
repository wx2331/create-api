/*      */ package sun.tools.javac;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import sun.tools.java.BinaryClass;
/*      */ import sun.tools.java.ClassDeclaration;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.ClassFile;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.ClassPath;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Environment;
/*      */ import sun.tools.java.Identifier;
/*      */ import sun.tools.java.IdentifierToken;
/*      */ import sun.tools.java.MemberDefinition;
/*      */ import sun.tools.java.Package;
/*      */ import sun.tools.java.Type;
/*      */ import sun.tools.tree.Node;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Deprecated
/*      */ public class BatchEnvironment
/*      */   extends Environment
/*      */   implements ErrorConsumer
/*      */ {
/*      */   OutputStream out;
/*      */   protected ClassPath sourcePath;
/*      */   protected ClassPath binaryPath;
/*   64 */   Hashtable packages = new Hashtable<>(31);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   69 */   Vector classesOrdered = new Vector();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   74 */   Hashtable classes = new Hashtable<>(351);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int flags;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   89 */   public short majorVersion = 45;
/*   90 */   public short minorVersion = 3;
/*      */ 
/*      */ 
/*      */   
/*      */   public File covFile;
/*      */ 
/*      */ 
/*      */   
/*      */   public int nerrors;
/*      */ 
/*      */ 
/*      */   
/*      */   public int nwarnings;
/*      */ 
/*      */ 
/*      */   
/*      */   public int ndeprecations;
/*      */ 
/*      */   
/*  109 */   Vector deprecationFiles = new Vector();
/*      */   
/*      */   ErrorConsumer errorConsumer;
/*      */   
/*      */   private Set exemptPackages;
/*      */   
/*      */   String errorFileName;
/*      */   ErrorMessage errors;
/*      */   private int errorsPushed;
/*      */   public int errorLimit;
/*      */   private boolean hitErrorLimit;
/*      */   
/*      */   public BatchEnvironment(ClassPath paramClassPath) {
/*  122 */     this(System.out, paramClassPath);
/*      */   }
/*      */   
/*      */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath) {
/*  126 */     this(paramOutputStream, paramClassPath, (ErrorConsumer)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath, ErrorConsumer paramErrorConsumer) {
/*  131 */     this(paramOutputStream, paramClassPath, paramClassPath, paramErrorConsumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BatchEnvironment(ClassPath paramClassPath1, ClassPath paramClassPath2) {
/*  140 */     this(System.out, paramClassPath1, paramClassPath2);
/*      */   }
/*      */ 
/*      */   
/*      */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath1, ClassPath paramClassPath2) {
/*  145 */     this(paramOutputStream, paramClassPath1, paramClassPath2, (ErrorConsumer)null);
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
/*      */   static BatchEnvironment create(OutputStream paramOutputStream, String paramString1, String paramString2, String paramString3, String paramString4) {
/*  165 */     ClassPath[] arrayOfClassPath = classPaths(paramString1, paramString2, paramString3, paramString4);
/*      */     
/*  167 */     return new BatchEnvironment(paramOutputStream, arrayOfClassPath[0], arrayOfClassPath[1]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static ClassPath[] classPaths(String paramString1, String paramString2, String paramString3, String paramString4) {
/*  177 */     StringBuffer stringBuffer = new StringBuffer();
/*      */     
/*  179 */     if (paramString2 == null) {
/*      */ 
/*      */ 
/*      */       
/*  183 */       paramString2 = System.getProperty("env.class.path");
/*  184 */       if (paramString2 == null) {
/*  185 */         paramString2 = ".";
/*      */       }
/*      */     } 
/*  188 */     if (paramString1 == null) {
/*  189 */       paramString1 = paramString2;
/*      */     }
/*  191 */     if (paramString3 == null) {
/*  192 */       paramString3 = System.getProperty("sun.boot.class.path");
/*  193 */       if (paramString3 == null) {
/*  194 */         paramString3 = paramString2;
/*      */       }
/*      */     } 
/*  197 */     appendPath(stringBuffer, paramString3);
/*      */     
/*  199 */     if (paramString4 == null) {
/*  200 */       paramString4 = System.getProperty("java.ext.dirs");
/*      */     }
/*  202 */     if (paramString4 != null) {
/*  203 */       StringTokenizer stringTokenizer = new StringTokenizer(paramString4, File.pathSeparator);
/*      */       
/*  205 */       while (stringTokenizer.hasMoreTokens()) {
/*  206 */         String str = stringTokenizer.nextToken();
/*  207 */         File file = new File(str);
/*  208 */         if (!str.endsWith(File.separator)) {
/*  209 */           str = str + File.separator;
/*      */         }
/*  211 */         if (file.isDirectory()) {
/*  212 */           String[] arrayOfString = file.list();
/*  213 */           for (byte b = 0; b < arrayOfString.length; b++) {
/*  214 */             String str1 = arrayOfString[b];
/*  215 */             if (str1.endsWith(".jar")) {
/*  216 */               appendPath(stringBuffer, str + str1);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  223 */     appendPath(stringBuffer, paramString2);
/*      */     
/*  225 */     ClassPath classPath1 = new ClassPath(paramString1);
/*  226 */     ClassPath classPath2 = new ClassPath(stringBuffer.toString());
/*      */     
/*  228 */     return new ClassPath[] { classPath1, classPath2 };
/*      */   }
/*      */   
/*      */   private static void appendPath(StringBuffer paramStringBuffer, String paramString) {
/*  232 */     if (paramString.length() > 0) {
/*  233 */       if (paramStringBuffer.length() > 0) {
/*  234 */         paramStringBuffer.append(File.pathSeparator);
/*      */       }
/*  236 */       paramStringBuffer.append(paramString);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFlags() {
/*  244 */     return this.flags;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getMajorVersion() {
/*  251 */     return this.majorVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getMinorVersion() {
/*  258 */     return this.minorVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getcovFile() {
/*  266 */     return this.covFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration getClasses() {
/*  275 */     return this.classesOrdered.elements();
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
/*      */   public boolean isExemptPackage(Identifier paramIdentifier) {
/*  290 */     if (this.exemptPackages == null)
/*      */     {
/*      */       
/*  293 */       setExemptPackages();
/*      */     }
/*      */     
/*  296 */     return this.exemptPackages.contains(paramIdentifier);
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
/*      */   private void setExemptPackages() {
/*  320 */     this.exemptPackages = new HashSet(101);
/*      */ 
/*      */     
/*  323 */     for (Enumeration<ClassDeclaration> enumeration = getClasses(); enumeration.hasMoreElements(); ) {
/*  324 */       ClassDeclaration classDeclaration = enumeration.nextElement();
/*  325 */       if (classDeclaration.getStatus() == 4) {
/*  326 */         SourceClass sourceClass = (SourceClass)classDeclaration.getClassDefinition();
/*  327 */         if (sourceClass.isLocal()) {
/*      */           continue;
/*      */         }
/*  330 */         Identifier identifier = sourceClass.getImports().getCurrentPackage();
/*      */ 
/*      */ 
/*      */         
/*  334 */         while (identifier != idNull && this.exemptPackages.add(identifier)) {
/*  335 */           identifier = identifier.getQualifier();
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  348 */     if (!this.exemptPackages.contains(idJavaLang)) {
/*      */       
/*  350 */       this.exemptPackages.add(idJavaLang);
/*      */       
/*      */       try {
/*  353 */         if (!getPackage(idJavaLang).exists()) {
/*      */           
/*  355 */           error(0L, "package.not.found.strong", idJavaLang);
/*      */           return;
/*      */         } 
/*  358 */       } catch (IOException iOException) {
/*      */ 
/*      */         
/*  361 */         error(0L, "io.exception.package", idJavaLang);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassDeclaration getClassDeclaration(Identifier paramIdentifier) {
/*  388 */     return getClassDeclaration(Type.tClass(paramIdentifier));
/*      */   }
/*      */   
/*      */   public ClassDeclaration getClassDeclaration(Type paramType) {
/*  392 */     ClassDeclaration classDeclaration = (ClassDeclaration)this.classes.get(paramType);
/*  393 */     if (classDeclaration == null) {
/*  394 */       this.classes.put(paramType, classDeclaration = new ClassDeclaration(paramType.getClassName()));
/*  395 */       this.classesOrdered.addElement(classDeclaration);
/*      */     } 
/*  397 */     return classDeclaration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean classExists(Identifier paramIdentifier) {
/*  405 */     if (paramIdentifier.isInner()) {
/*  406 */       paramIdentifier = paramIdentifier.getTopName();
/*      */     }
/*  408 */     Type type = Type.tClass(paramIdentifier);
/*      */     try {
/*  410 */       ClassDeclaration classDeclaration = (ClassDeclaration)this.classes.get(type);
/*  411 */       return (classDeclaration != null) ? classDeclaration.getName().equals(paramIdentifier) : 
/*  412 */         getPackage(paramIdentifier.getQualifier()).classExists(paramIdentifier.getName());
/*  413 */     } catch (IOException iOException) {
/*  414 */       return true;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Package getPackage(Identifier paramIdentifier) throws IOException {
/*  451 */     Package package_ = (Package)this.packages.get(paramIdentifier);
/*  452 */     if (package_ == null) {
/*  453 */       this.packages.put(paramIdentifier, package_ = new Package(this.sourcePath, this.binaryPath, paramIdentifier));
/*      */     }
/*  455 */     return package_;
/*      */   }
/*      */ 
/*      */   
/*      */   public void parseFile(ClassFile paramClassFile) throws FileNotFoundException {
/*      */     InputStream inputStream;
/*      */     BatchParser batchParser;
/*  462 */     long l = System.currentTimeMillis();
/*      */ 
/*      */ 
/*      */     
/*  466 */     dtEnter("parseFile: PARSING SOURCE " + paramClassFile);
/*      */     
/*  468 */     Environment environment = new Environment(this, paramClassFile);
/*      */     
/*      */     try {
/*  471 */       inputStream = paramClassFile.getInputStream();
/*  472 */       environment.setCharacterEncoding(getCharacterEncoding());
/*      */       
/*  474 */       batchParser = new BatchParser(environment, inputStream);
/*  475 */     } catch (IOException iOException) {
/*  476 */       dtEvent("parseFile: IO EXCEPTION " + paramClassFile);
/*  477 */       throw new FileNotFoundException();
/*      */     } 
/*      */     
/*      */     try {
/*  481 */       batchParser.parseFile();
/*  482 */     } catch (Exception exception) {
/*  483 */       throw new CompilerError(exception);
/*      */     } 
/*      */     
/*      */     try {
/*  487 */       inputStream.close();
/*  488 */     } catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */     
/*  492 */     if (verbose()) {
/*  493 */       l = System.currentTimeMillis() - l;
/*  494 */       output(Main.getText("benv.parsed_in", paramClassFile.getPath(), 
/*  495 */             Long.toString(l)));
/*      */     } 
/*      */     
/*  498 */     if (batchParser.classes.size() == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  505 */       batchParser.imports.resolve(environment);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  530 */       Enumeration<ClassDefinition> enumeration = batchParser.classes.elements();
/*      */ 
/*      */       
/*  533 */       ClassDefinition classDefinition1 = enumeration.nextElement();
/*  534 */       if (classDefinition1.isInnerClass()) {
/*  535 */         throw new CompilerError("BatchEnvironment, first is inner");
/*      */       }
/*      */       
/*  538 */       ClassDefinition classDefinition2 = classDefinition1;
/*      */       
/*  540 */       while (enumeration.hasMoreElements()) {
/*  541 */         ClassDefinition classDefinition = enumeration.nextElement();
/*      */         
/*  543 */         if (classDefinition.isInnerClass()) {
/*      */           continue;
/*      */         }
/*  546 */         classDefinition2.addDependency(classDefinition.getClassDeclaration());
/*  547 */         classDefinition.addDependency(classDefinition2.getClassDeclaration());
/*  548 */         classDefinition2 = classDefinition;
/*      */       } 
/*      */ 
/*      */       
/*  552 */       if (classDefinition2 != classDefinition1) {
/*  553 */         classDefinition2.addDependency(classDefinition1.getClassDeclaration());
/*  554 */         classDefinition1.addDependency(classDefinition2.getClassDeclaration());
/*      */       } 
/*      */     } 
/*      */     
/*  558 */     dtExit("parseFile: SOURCE PARSED " + paramClassFile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   BinaryClass loadFile(ClassFile paramClassFile) throws IOException {
/*  565 */     long l = System.currentTimeMillis();
/*  566 */     InputStream inputStream = paramClassFile.getInputStream();
/*  567 */     BinaryClass binaryClass = null;
/*      */     
/*  569 */     dtEnter("loadFile: LOADING CLASSFILE " + paramClassFile);
/*      */     
/*      */     try {
/*  572 */       DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
/*      */       
/*  574 */       binaryClass = BinaryClass.load(new Environment(this, paramClassFile), dataInputStream, 
/*  575 */           loadFileFlags());
/*  576 */     } catch (ClassFormatError classFormatError) {
/*  577 */       error(0L, "class.format", paramClassFile.getPath(), classFormatError.getMessage());
/*  578 */       dtExit("loadFile: CLASS FORMAT ERROR " + paramClassFile);
/*  579 */       return null;
/*  580 */     } catch (EOFException eOFException) {
/*      */ 
/*      */ 
/*      */       
/*  584 */       error(0L, "truncated.class", paramClassFile.getPath());
/*  585 */       return null;
/*      */     } 
/*      */     
/*  588 */     inputStream.close();
/*  589 */     if (verbose()) {
/*  590 */       l = System.currentTimeMillis() - l;
/*  591 */       output(Main.getText("benv.loaded_in", paramClassFile.getPath(), 
/*  592 */             Long.toString(l)));
/*      */     } 
/*      */     
/*  595 */     dtExit("loadFile: CLASSFILE LOADED " + paramClassFile);
/*      */     
/*  597 */     return binaryClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int loadFileFlags() {
/*  604 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean needsCompilation(Hashtable<ClassDeclaration, ClassDeclaration> paramHashtable, ClassDeclaration paramClassDeclaration) {
/*  611 */     switch (paramClassDeclaration.getStatus()) {
/*      */       
/*      */       case 0:
/*  614 */         dtEnter("needsCompilation: UNDEFINED " + paramClassDeclaration.getName());
/*  615 */         loadDefinition(paramClassDeclaration);
/*  616 */         return needsCompilation(paramHashtable, paramClassDeclaration);
/*      */       
/*      */       case 1:
/*  619 */         dtEnter("needsCompilation: UNDECIDED " + paramClassDeclaration.getName());
/*  620 */         if (paramHashtable.get(paramClassDeclaration) == null) {
/*  621 */           paramHashtable.put(paramClassDeclaration, paramClassDeclaration);
/*      */           
/*  623 */           BinaryClass binaryClass = (BinaryClass)paramClassDeclaration.getClassDefinition();
/*  624 */           for (Enumeration<ClassDeclaration> enumeration = binaryClass.getDependencies(); enumeration.hasMoreElements(); ) {
/*  625 */             ClassDeclaration classDeclaration = enumeration.nextElement();
/*  626 */             if (needsCompilation(paramHashtable, classDeclaration)) {
/*      */               
/*  628 */               paramClassDeclaration.setDefinition((ClassDefinition)binaryClass, 3);
/*  629 */               dtExit("needsCompilation: YES (source) " + paramClassDeclaration.getName());
/*  630 */               return true;
/*      */             } 
/*      */           } 
/*      */         } 
/*  634 */         dtExit("needsCompilation: NO (undecided) " + paramClassDeclaration.getName());
/*  635 */         return false;
/*      */ 
/*      */       
/*      */       case 2:
/*  639 */         dtEnter("needsCompilation: BINARY " + paramClassDeclaration.getName());
/*  640 */         dtExit("needsCompilation: NO (binary) " + paramClassDeclaration.getName());
/*      */         
/*  642 */         return false;
/*      */     } 
/*      */ 
/*      */     
/*  646 */     dtExit("needsCompilation: YES " + paramClassDeclaration.getName());
/*  647 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void loadDefinition(ClassDeclaration paramClassDeclaration) {
/*      */     Identifier identifier;
/*      */     Hashtable<Object, Object> hashtable;
/*      */     ClassFile classFile1;
/*      */     Package package_;
/*      */     ClassFile classFile2, classFile3;
/*      */     BinaryClass binaryClass;
/*  658 */     dtEnter("loadDefinition: ENTER " + paramClassDeclaration
/*  659 */         .getName() + ", status " + paramClassDeclaration.getStatus());
/*  660 */     switch (paramClassDeclaration.getStatus()) {
/*      */       
/*      */       case 0:
/*  663 */         dtEvent("loadDefinition: STATUS IS UNDEFINED");
/*  664 */         identifier = paramClassDeclaration.getName();
/*      */         
/*      */         try {
/*  667 */           package_ = getPackage(identifier.getQualifier());
/*  668 */         } catch (IOException iOException) {
/*      */ 
/*      */           
/*  671 */           paramClassDeclaration.setDefinition(null, 7);
/*      */           
/*  673 */           error(0L, "io.exception", paramClassDeclaration);
/*      */           
/*  675 */           dtExit("loadDefinition: IO EXCEPTION (package)");
/*      */           return;
/*      */         } 
/*  678 */         classFile2 = package_.getBinaryFile(identifier.getName());
/*  679 */         if (classFile2 == null) {
/*      */           
/*  681 */           paramClassDeclaration.setDefinition(null, 3);
/*      */           
/*  683 */           dtExit("loadDefinition: MUST BE SOURCE (no binary) " + paramClassDeclaration
/*  684 */               .getName());
/*      */           
/*      */           return;
/*      */         } 
/*  688 */         classFile3 = package_.getSourceFile(identifier.getName());
/*  689 */         if (classFile3 == null) {
/*      */           
/*  691 */           dtEvent("loadDefinition: NO SOURCE " + paramClassDeclaration.getName());
/*  692 */           BinaryClass binaryClass1 = null;
/*      */           try {
/*  694 */             binaryClass1 = loadFile(classFile2);
/*  695 */           } catch (IOException iOException) {
/*      */ 
/*      */             
/*  698 */             paramClassDeclaration.setDefinition(null, 7);
/*      */             
/*  700 */             error(0L, "io.exception", classFile2);
/*      */             
/*  702 */             dtExit("loadDefinition: IO EXCEPTION (binary)");
/*      */             return;
/*      */           } 
/*  705 */           if (binaryClass1 != null && !binaryClass1.getName().equals(identifier)) {
/*  706 */             error(0L, "wrong.class", classFile2.getPath(), paramClassDeclaration, binaryClass1);
/*  707 */             binaryClass1 = null;
/*      */             
/*  709 */             dtEvent("loadDefinition: WRONG CLASS (binary)");
/*      */           } 
/*  711 */           if (binaryClass1 == null) {
/*      */             
/*  713 */             paramClassDeclaration.setDefinition(null, 7);
/*      */             
/*  715 */             dtExit("loadDefinition: NOT FOUND (source or binary)");
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  720 */           if (binaryClass1.getSource() != null) {
/*  721 */             classFile3 = new ClassFile(new File((String)binaryClass1.getSource()));
/*      */             
/*  723 */             classFile3 = package_.getSourceFile(classFile3.getName());
/*  724 */             if (classFile3 != null && classFile3.exists()) {
/*      */               
/*  726 */               dtEvent("loadDefinition: FILENAME IN BINARY " + classFile3);
/*      */               
/*  728 */               if (classFile3.lastModified() > classFile2.lastModified()) {
/*      */                 
/*  730 */                 paramClassDeclaration.setDefinition((ClassDefinition)binaryClass1, 3);
/*      */                 
/*  732 */                 dtEvent("loadDefinition: SOURCE IS NEWER " + classFile3);
/*      */                 
/*  734 */                 binaryClass1.loadNested(this);
/*      */                 
/*  736 */                 dtExit("loadDefinition: MUST BE SOURCE " + paramClassDeclaration
/*  737 */                     .getName());
/*      */                 return;
/*      */               } 
/*  740 */               if (dependencies()) {
/*  741 */                 paramClassDeclaration.setDefinition((ClassDefinition)binaryClass1, 1);
/*      */                 
/*  743 */                 dtEvent("loadDefinition: UNDECIDED " + paramClassDeclaration
/*  744 */                     .getName());
/*      */               } else {
/*  746 */                 paramClassDeclaration.setDefinition((ClassDefinition)binaryClass1, 2);
/*      */                 
/*  748 */                 dtEvent("loadDefinition: MUST BE BINARY " + paramClassDeclaration
/*  749 */                     .getName());
/*      */               } 
/*  751 */               binaryClass1.loadNested(this);
/*      */               
/*  753 */               dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  754 */                   .getName() + ", status " + paramClassDeclaration.getStatus());
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*      */           
/*  760 */           paramClassDeclaration.setDefinition((ClassDefinition)binaryClass1, 2);
/*      */           
/*  762 */           dtEvent("loadDefinition: MUST BE BINARY (no source) " + paramClassDeclaration
/*  763 */               .getName());
/*  764 */           binaryClass1.loadNested(this);
/*      */           
/*  766 */           dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  767 */               .getName() + ", status " + paramClassDeclaration.getStatus());
/*      */           return;
/*      */         } 
/*  770 */         binaryClass = null;
/*      */         try {
/*  772 */           if (classFile3.lastModified() > classFile2.lastModified()) {
/*      */             
/*  774 */             paramClassDeclaration.setDefinition(null, 3);
/*      */             
/*  776 */             dtEvent("loadDefinition: MUST BE SOURCE (younger than binary) " + paramClassDeclaration
/*  777 */                 .getName());
/*      */             return;
/*      */           } 
/*  780 */           binaryClass = loadFile(classFile2);
/*  781 */         } catch (IOException iOException) {
/*  782 */           error(0L, "io.exception", classFile2);
/*      */           
/*  784 */           dtEvent("loadDefinition: IO EXCEPTION (binary)");
/*      */         } 
/*  786 */         if (binaryClass != null && !binaryClass.getName().equals(identifier)) {
/*  787 */           error(0L, "wrong.class", classFile2.getPath(), paramClassDeclaration, binaryClass);
/*  788 */           binaryClass = null;
/*      */           
/*  790 */           dtEvent("loadDefinition: WRONG CLASS (binary)");
/*      */         } 
/*  792 */         if (binaryClass != null) {
/*  793 */           Identifier identifier1 = binaryClass.getName();
/*  794 */           if (identifier1.equals(paramClassDeclaration.getName())) {
/*  795 */             if (dependencies()) {
/*  796 */               paramClassDeclaration.setDefinition((ClassDefinition)binaryClass, 1);
/*      */               
/*  798 */               dtEvent("loadDefinition: UNDECIDED " + identifier1);
/*      */             } else {
/*  800 */               paramClassDeclaration.setDefinition((ClassDefinition)binaryClass, 2);
/*      */               
/*  802 */               dtEvent("loadDefinition: MUST BE BINARY " + identifier1);
/*      */             } 
/*      */           } else {
/*  805 */             paramClassDeclaration.setDefinition(null, 7);
/*      */             
/*  807 */             dtEvent("loadDefinition: NOT FOUND (source or binary)");
/*  808 */             if (dependencies()) {
/*  809 */               getClassDeclaration(identifier1).setDefinition((ClassDefinition)binaryClass, 1);
/*      */               
/*  811 */               dtEvent("loadDefinition: UNDECIDED " + identifier1);
/*      */             } else {
/*  813 */               getClassDeclaration(identifier1).setDefinition((ClassDefinition)binaryClass, 2);
/*      */               
/*  815 */               dtEvent("loadDefinition: MUST BE BINARY " + identifier1);
/*      */             } 
/*      */           } 
/*      */         } else {
/*  819 */           paramClassDeclaration.setDefinition(null, 7);
/*      */           
/*  821 */           dtEvent("loadDefinition: NOT FOUND (source or binary)");
/*      */         } 
/*  823 */         if (binaryClass != null && binaryClass == paramClassDeclaration.getClassDefinition())
/*  824 */           binaryClass.loadNested(this); 
/*  825 */         dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  826 */             .getName() + ", status " + paramClassDeclaration.getStatus());
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/*  831 */         dtEvent("loadDefinition: STATUS IS UNDECIDED");
/*  832 */         hashtable = new Hashtable<>();
/*  833 */         if (!needsCompilation(hashtable, paramClassDeclaration))
/*      */         {
/*  835 */           for (Enumeration<ClassDeclaration> enumeration = hashtable.keys(); enumeration.hasMoreElements(); ) {
/*  836 */             ClassDeclaration classDeclaration = enumeration.nextElement();
/*  837 */             if (classDeclaration.getStatus() == 1) {
/*      */               
/*  839 */               classDeclaration.setDefinition(classDeclaration.getClassDefinition(), 2);
/*      */               
/*  841 */               dtEvent("loadDefinition: MUST BE BINARY " + classDeclaration);
/*      */             } 
/*      */           } 
/*      */         }
/*  845 */         dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  846 */             .getName() + ", status " + paramClassDeclaration.getStatus());
/*      */         return;
/*      */ 
/*      */       
/*      */       case 3:
/*  851 */         dtEvent("loadDefinition: STATUS IS SOURCE");
/*  852 */         hashtable = null;
/*  853 */         package_ = null;
/*  854 */         if (paramClassDeclaration.getClassDefinition() != null) {
/*      */           
/*      */           try {
/*  857 */             package_ = getPackage(paramClassDeclaration.getName().getQualifier());
/*  858 */             classFile1 = package_.getSourceFile((String)paramClassDeclaration.getClassDefinition().getSource());
/*  859 */           } catch (IOException iOException) {
/*  860 */             error(0L, "io.exception", paramClassDeclaration);
/*      */             
/*  862 */             dtEvent("loadDefinition: IO EXCEPTION (package)");
/*      */           } 
/*  864 */           if (classFile1 == null) {
/*  865 */             String str = (String)paramClassDeclaration.getClassDefinition().getSource();
/*  866 */             classFile1 = new ClassFile(new File(str));
/*      */           } 
/*      */         } else {
/*      */           
/*  870 */           Identifier identifier1 = paramClassDeclaration.getName();
/*      */           try {
/*  872 */             package_ = getPackage(identifier1.getQualifier());
/*  873 */             classFile1 = package_.getSourceFile(identifier1.getName());
/*  874 */           } catch (IOException iOException) {
/*  875 */             error(0L, "io.exception", paramClassDeclaration);
/*      */             
/*  877 */             dtEvent("loadDefinition: IO EXCEPTION (package)");
/*      */           } 
/*  879 */           if (classFile1 == null) {
/*      */             
/*  881 */             paramClassDeclaration.setDefinition(null, 7);
/*      */             
/*  883 */             dtExit("loadDefinition: SOURCE NOT FOUND " + paramClassDeclaration
/*  884 */                 .getName() + ", status " + paramClassDeclaration.getStatus());
/*      */             return;
/*      */           } 
/*      */         } 
/*      */         try {
/*  889 */           parseFile(classFile1);
/*  890 */         } catch (FileNotFoundException fileNotFoundException) {
/*  891 */           error(0L, "io.exception", classFile1);
/*  892 */           dtEvent("loadDefinition: IO EXCEPTION (source)");
/*      */         } 
/*  894 */         if (paramClassDeclaration.getClassDefinition() == null || paramClassDeclaration.getStatus() == 3) {
/*      */           
/*  896 */           error(0L, "wrong.source", classFile1.getPath(), paramClassDeclaration, package_);
/*  897 */           paramClassDeclaration.setDefinition(null, 7);
/*      */           
/*  899 */           dtEvent("loadDefinition: WRONG CLASS (source) " + paramClassDeclaration
/*  900 */               .getName());
/*      */         } 
/*  902 */         dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  903 */             .getName() + ", status " + paramClassDeclaration.getStatus());
/*      */         return;
/*      */     } 
/*      */     
/*  907 */     dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  908 */         .getName() + ", status " + paramClassDeclaration.getStatus());
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
/*      */   public ClassDefinition makeClassDefinition(Environment paramEnvironment, long paramLong, IdentifierToken paramIdentifierToken1, String paramString, int paramInt, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken, ClassDefinition paramClassDefinition) {
/*  922 */     Identifier identifier2, identifier1 = paramIdentifierToken1.getName();
/*  923 */     long l = paramIdentifierToken1.getWhere();
/*      */ 
/*      */     
/*  926 */     String str = null;
/*  927 */     ClassDefinition classDefinition = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  939 */     Identifier identifier3 = null;
/*      */     
/*  941 */     if (identifier1.isQualified() || identifier1.isInner()) {
/*  942 */       identifier2 = identifier1;
/*  943 */     } else if ((paramInt & 0x30000) != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  948 */       classDefinition = paramClassDefinition.getTopClass();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  955 */       for (byte b = 1;; b++) {
/*  956 */         str = b + (identifier1.equals(idNull) ? "" : ("$" + identifier1));
/*  957 */         if (classDefinition.getLocalClass(str) == null) {
/*      */           break;
/*      */         }
/*      */       } 
/*  961 */       Identifier identifier = classDefinition.getName();
/*  962 */       identifier2 = Identifier.lookupInner(identifier, Identifier.lookup(str));
/*      */       
/*  964 */       if ((paramInt & 0x10000) != 0) {
/*  965 */         identifier3 = idNull;
/*      */       } else {
/*      */         
/*  968 */         identifier3 = identifier1;
/*      */       } 
/*  970 */     } else if (paramClassDefinition != null) {
/*      */       
/*  972 */       identifier2 = Identifier.lookupInner(paramClassDefinition.getName(), identifier1);
/*      */     } else {
/*  974 */       identifier2 = identifier1;
/*      */     } 
/*      */ 
/*      */     
/*  978 */     ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(identifier2);
/*      */ 
/*      */     
/*  981 */     if (classDeclaration.isDefined()) {
/*  982 */       paramEnvironment.error(l, "class.multidef", classDeclaration
/*  983 */           .getName(), classDeclaration.getClassDefinition().getSource());
/*      */       
/*  985 */       classDeclaration = new ClassDeclaration(identifier2);
/*      */     } 
/*      */     
/*  988 */     if (paramIdentifierToken2 == null && !identifier2.equals(idJavaLangObject)) {
/*  989 */       paramIdentifierToken2 = new IdentifierToken(idJavaLangObject);
/*      */     }
/*      */     
/*  992 */     SourceClass sourceClass = new SourceClass(paramEnvironment, paramLong, classDeclaration, paramString, paramInt, paramIdentifierToken2, paramArrayOfIdentifierToken, (SourceClass)paramClassDefinition, identifier3);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  997 */     if (paramClassDefinition != null) {
/*      */       
/*  999 */       paramClassDefinition.addMember(paramEnvironment, new SourceMember(sourceClass));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1006 */       if ((paramInt & 0x30000) != 0) {
/* 1007 */         classDefinition.addLocalClass(sourceClass, str);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1015 */     return sourceClass;
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
/*      */   public MemberDefinition makeMemberDefinition(Environment paramEnvironment, long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, Identifier paramIdentifier, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Object paramObject) {
/* 1028 */     dtEvent("makeMemberDefinition: " + paramIdentifier + " IN " + paramClassDefinition);
/* 1029 */     Vector<IdentifierToken> vector = null;
/* 1030 */     if (paramArrayOfIdentifierToken1 != null) {
/* 1031 */       vector = new Vector(paramArrayOfIdentifierToken1.length);
/* 1032 */       for (byte b = 0; b < paramArrayOfIdentifierToken1.length; b++) {
/* 1033 */         vector.addElement(paramArrayOfIdentifierToken1[b]);
/*      */       }
/*      */     } 
/* 1036 */     SourceMember sourceMember = new SourceMember(paramLong, paramClassDefinition, paramString, paramInt, paramType, paramIdentifier, vector, paramArrayOfIdentifierToken2, (Node)paramObject);
/*      */     
/* 1038 */     paramClassDefinition.addMember(paramEnvironment, sourceMember);
/* 1039 */     return sourceMember;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void shutdown() {
/*      */     try {
/* 1047 */       if (this.sourcePath != null) {
/* 1048 */         this.sourcePath.close();
/*      */       }
/* 1050 */       if (this.binaryPath != null && this.binaryPath != this.sourcePath) {
/* 1051 */         this.binaryPath.close();
/*      */       }
/* 1053 */     } catch (IOException iOException) {
/* 1054 */       output(Main.getText("benv.failed_to_close_class_path", iOException
/* 1055 */             .toString()));
/*      */     } 
/* 1057 */     this.sourcePath = null;
/* 1058 */     this.binaryPath = null;
/*      */     
/* 1060 */     super.shutdown();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String errorString(String paramString, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 1068 */     String str = null;
/*      */     
/* 1070 */     if (paramString.startsWith("warn.")) {
/* 1071 */       str = "javac.err." + paramString.substring(5);
/*      */     } else {
/* 1073 */       str = "javac.err." + paramString;
/*      */     } 
/* 1075 */     return Main.getText(str, (paramObject1 != null) ? paramObject1
/* 1076 */         .toString() : null, (paramObject2 != null) ? paramObject2
/* 1077 */         .toString() : null, (paramObject3 != null) ? paramObject3
/* 1078 */         .toString() : null);
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
/*      */   protected boolean insertError(long paramLong, String paramString) {
/* 1107 */     if (this.errors == null || this.errors.where > paramLong) {
/*      */ 
/*      */ 
/*      */       
/* 1111 */       ErrorMessage errorMessage = new ErrorMessage(paramLong, paramString);
/* 1112 */       errorMessage.next = this.errors;
/* 1113 */       this.errors = errorMessage;
/*      */     } else {
/* 1115 */       if (this.errors.where == paramLong && this.errors.message
/* 1116 */         .equals(paramString))
/*      */       {
/*      */         
/* 1119 */         return false;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1124 */       ErrorMessage errorMessage1 = this.errors;
/*      */       
/*      */       ErrorMessage errorMessage2;
/* 1127 */       while ((errorMessage2 = errorMessage1.next) != null && errorMessage2.where < paramLong)
/*      */       {
/* 1129 */         errorMessage1 = errorMessage2;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1135 */       while ((errorMessage2 = errorMessage1.next) != null && errorMessage2.where == paramLong) {
/*      */         
/* 1137 */         if (errorMessage2.message.equals(paramString))
/*      */         {
/*      */           
/* 1140 */           return false;
/*      */         }
/* 1142 */         errorMessage1 = errorMessage2;
/*      */       } 
/*      */ 
/*      */       
/* 1146 */       ErrorMessage errorMessage3 = new ErrorMessage(paramLong, paramString);
/* 1147 */       errorMessage3.next = errorMessage1.next;
/* 1148 */       errorMessage1.next = errorMessage3;
/*      */     } 
/*      */ 
/*      */     
/* 1152 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath1, ClassPath paramClassPath2, ErrorConsumer paramErrorConsumer) {
/* 1160 */     this.errorLimit = 100;
/*      */     this.out = paramOutputStream;
/*      */     this.sourcePath = paramClassPath1;
/*      */     this.binaryPath = paramClassPath2;
/*      */     this.errorConsumer = (paramErrorConsumer == null) ? this : paramErrorConsumer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void pushError(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4) {
/* 1170 */     int i = this.errorLimit + this.nwarnings;
/* 1171 */     if (++this.errorsPushed >= i && this.errorLimit >= 0) {
/* 1172 */       if (!this.hitErrorLimit) {
/* 1173 */         this.hitErrorLimit = true;
/* 1174 */         output(errorString("too.many.errors", new Integer(this.errorLimit), (Object)null, (Object)null));
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 1179 */     if (paramString1.endsWith(".java")) {
/* 1180 */       output(paramString1 + ":" + paramInt + ": " + paramString2);
/* 1181 */       output(paramString3);
/* 1182 */       output(paramString4);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1187 */       output(paramString1 + ": " + paramString2);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void flushErrors() {
/* 1192 */     if (this.errors == null) {
/*      */       return;
/*      */     }
/*      */     
/* 1196 */     boolean bool = false;
/*      */     
/* 1198 */     char[] arrayOfChar = null;
/* 1199 */     int i = 0;
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1204 */       FileInputStream fileInputStream = new FileInputStream(this.errorFileName);
/* 1205 */       arrayOfChar = new char[fileInputStream.available()];
/*      */ 
/*      */       
/* 1208 */       InputStreamReader inputStreamReader = (getCharacterEncoding() != null) ? new InputStreamReader(fileInputStream, getCharacterEncoding()) : new InputStreamReader(fileInputStream);
/*      */       
/* 1210 */       i = inputStreamReader.read(arrayOfChar);
/* 1211 */       inputStreamReader.close();
/* 1212 */       bool = true;
/* 1213 */     } catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1218 */     for (ErrorMessage errorMessage = this.errors; errorMessage != null; errorMessage = errorMessage.next) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1224 */       int j = (int)(errorMessage.where >>> 32L);
/* 1225 */       int k = (int)(errorMessage.where & 0xFFFFFFFFL);
/* 1226 */       if (k > i) k = i;
/*      */       
/* 1228 */       String str1 = "";
/* 1229 */       String str2 = "";
/* 1230 */       if (bool) {
/*      */         int m;
/* 1232 */         for (m = k; m > 0 && arrayOfChar[m - 1] != '\n' && arrayOfChar[m - 1] != '\r'; m--); int n;
/* 1233 */         for (n = k; n < i && arrayOfChar[n] != '\n' && arrayOfChar[n] != '\r'; n++);
/* 1234 */         str1 = new String(arrayOfChar, m, n - m);
/*      */         
/* 1236 */         char[] arrayOfChar1 = new char[k - m + 1];
/* 1237 */         for (n = m; n < k; n++) {
/* 1238 */           arrayOfChar1[n - m] = (arrayOfChar[n] == '\t') ? '\t' : ' ';
/*      */         }
/* 1240 */         arrayOfChar1[k - m] = '^';
/* 1241 */         str2 = new String(arrayOfChar1);
/*      */       } 
/*      */       
/* 1244 */       this.errorConsumer.pushError(this.errorFileName, j, errorMessage.message, str1, str2);
/*      */     } 
/*      */     
/* 1247 */     this.errors = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reportError(Object paramObject, long paramLong, String paramString1, String paramString2) {
/* 1255 */     if (paramObject == null) {
/* 1256 */       if (this.errorFileName != null) {
/* 1257 */         flushErrors();
/* 1258 */         this.errorFileName = null;
/*      */       } 
/* 1260 */       if (paramString1.startsWith("warn.")) {
/* 1261 */         if (warnings()) {
/* 1262 */           this.nwarnings++;
/* 1263 */           output(paramString2);
/*      */         } 
/*      */         return;
/*      */       } 
/* 1267 */       output("error: " + paramString2);
/* 1268 */       this.nerrors++;
/* 1269 */       this.flags |= 0x10000;
/*      */     }
/* 1271 */     else if (paramObject instanceof String) {
/* 1272 */       String str = (String)paramObject;
/*      */ 
/*      */       
/* 1275 */       if (!str.equals(this.errorFileName)) {
/* 1276 */         flushErrors();
/* 1277 */         this.errorFileName = str;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1282 */       if (paramString1.startsWith("warn.")) {
/* 1283 */         if (paramString1.indexOf("is.deprecated") >= 0) {
/*      */ 
/*      */           
/* 1286 */           if (!this.deprecationFiles.contains(paramObject)) {
/* 1287 */             this.deprecationFiles.addElement(paramObject);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1293 */           if (deprecation()) {
/* 1294 */             if (insertError(paramLong, paramString2)) {
/* 1295 */               this.ndeprecations++;
/*      */             }
/*      */           } else {
/* 1298 */             this.ndeprecations++;
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/* 1304 */         else if (warnings()) {
/* 1305 */           if (insertError(paramLong, paramString2)) {
/* 1306 */             this.nwarnings++;
/*      */           }
/*      */         } else {
/* 1309 */           this.nwarnings++;
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1315 */       else if (insertError(paramLong, paramString2)) {
/* 1316 */         this.nerrors++;
/* 1317 */         this.flags |= 0x10000;
/*      */       }
/*      */     
/* 1320 */     } else if (paramObject instanceof ClassFile) {
/* 1321 */       reportError(((ClassFile)paramObject).getPath(), paramLong, paramString1, paramString2);
/*      */     }
/* 1323 */     else if (paramObject instanceof Identifier) {
/* 1324 */       reportError(paramObject.toString(), paramLong, paramString1, paramString2);
/*      */     }
/* 1326 */     else if (paramObject instanceof ClassDeclaration) {
/*      */       try {
/* 1328 */         reportError(((ClassDeclaration)paramObject).getClassDefinition(this), paramLong, paramString1, paramString2);
/* 1329 */       } catch (ClassNotFound classNotFound) {
/* 1330 */         reportError(((ClassDeclaration)paramObject).getName(), paramLong, paramString1, paramString2);
/*      */       } 
/* 1332 */     } else if (paramObject instanceof ClassDefinition) {
/* 1333 */       ClassDefinition classDefinition = (ClassDefinition)paramObject;
/* 1334 */       if (!paramString1.startsWith("warn.")) {
/* 1335 */         classDefinition.setError();
/*      */       }
/* 1337 */       reportError(classDefinition.getSource(), paramLong, paramString1, paramString2);
/*      */     }
/* 1339 */     else if (paramObject instanceof MemberDefinition) {
/* 1340 */       reportError(((MemberDefinition)paramObject).getClassDeclaration(), paramLong, paramString1, paramString2);
/*      */     } else {
/*      */       
/* 1343 */       output(paramObject + ":error=" + paramString1 + ":" + paramString2);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Object paramObject1, long paramLong, String paramString, Object paramObject2, Object paramObject3, Object paramObject4) {
/* 1351 */     if (this.errorsPushed >= this.errorLimit + this.nwarnings) {
/*      */       return;
/*      */     }
/*      */     
/* 1355 */     if (System.getProperty("javac.dump.stack") != null) {
/* 1356 */       output("javac.err." + paramString + ": " + errorString(paramString, paramObject2, paramObject3, paramObject4));
/* 1357 */       (new Exception("Stack trace")).printStackTrace(new PrintStream(this.out));
/*      */     } 
/* 1359 */     reportError(paramObject1, paramLong, paramString, errorString(paramString, paramObject2, paramObject3, paramObject4));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void output(String paramString) {
/* 1367 */     PrintStream printStream = (this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true);
/*      */ 
/*      */     
/* 1370 */     printStream.println(paramString);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\javac\BatchEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */