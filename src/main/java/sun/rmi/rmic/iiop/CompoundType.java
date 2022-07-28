/*      */ package sun.rmi.rmic.iiop;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Vector;
/*      */ import sun.rmi.rmic.IndentingWriter;
/*      */ import sun.tools.java.ClassDeclaration;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Environment;
/*      */ import sun.tools.java.Identifier;
/*      */ import sun.tools.java.MemberDefinition;
/*      */ import sun.tools.java.Type;
/*      */ import sun.tools.tree.IntegerExpression;
/*      */ import sun.tools.tree.LocalMember;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class CompoundType
/*      */   extends Type
/*      */ {
/*      */   protected Method[] methods;
/*      */   protected InterfaceType[] interfaces;
/*      */   protected Member[] members;
/*      */   protected ClassDefinition classDef;
/*      */   protected ClassDeclaration classDecl;
/*      */   protected boolean isCORBAObject = false;
/*      */   protected boolean isIDLEntity = false;
/*      */   protected boolean isAbstractBase = false;
/*      */   protected boolean isValueBase = false;
/*      */   protected boolean isCORBAUserException = false;
/*      */   protected boolean isException = false;
/*      */   protected boolean isCheckedException = false;
/*      */   protected boolean isRemoteExceptionOrSubclass = false;
/*      */   protected String idlExceptionName;
/*      */   protected String qualifiedIDLExceptionName;
/*      */   
/*      */   public boolean isCORBAObject() {
/*   87 */     return this.isCORBAObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIDLEntity() {
/*   95 */     return this.isIDLEntity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isValueBase() {
/*  103 */     return this.isValueBase;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAbstractBase() {
/*  111 */     return this.isAbstractBase;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isException() {
/*  118 */     return this.isException;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCheckedException() {
/*  126 */     return this.isCheckedException;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRemoteExceptionOrSubclass() {
/*  135 */     return this.isRemoteExceptionOrSubclass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCORBAUserException() {
/*  143 */     return this.isCORBAUserException;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIDLEntityException() {
/*  151 */     return (isIDLEntity() && isException());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBoxed() {
/*  159 */     return (isIDLEntity() && !isValueBase() && 
/*  160 */       !isAbstractBase() && !isCORBAObject() && 
/*  161 */       !isIDLEntityException());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getIDLExceptionName() {
/*  170 */     return this.idlExceptionName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getQualifiedIDLExceptionName(boolean paramBoolean) {
/*  180 */     if (this.qualifiedIDLExceptionName != null && paramBoolean && (
/*      */       
/*  182 */       getIDLModuleNames()).length > 0) {
/*  183 */       return "::" + this.qualifiedIDLExceptionName;
/*      */     }
/*  185 */     return this.qualifiedIDLExceptionName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSignature() {
/*  194 */     String str = this.classDecl.getType().getTypeSignature();
/*  195 */     if (str.endsWith(";")) {
/*  196 */       str = str.substring(0, str.length() - 1);
/*      */     }
/*  198 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassDeclaration getClassDeclaration() {
/*  205 */     return this.classDecl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassDefinition getClassDefinition() {
/*  212 */     return this.classDef;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassType getSuperclass() {
/*  220 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InterfaceType[] getInterfaces() {
/*  229 */     if (this.interfaces != null) {
/*  230 */       return (InterfaceType[])this.interfaces.clone();
/*      */     }
/*  232 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Method[] getMethods() {
/*  240 */     if (this.methods != null) {
/*  241 */       return (Method[])this.methods.clone();
/*      */     }
/*  243 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Member[] getMembers() {
/*  251 */     if (this.members != null) {
/*  252 */       return (Member[])this.members.clone();
/*      */     }
/*  254 */     return null;
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
/*      */   public static CompoundType forCompound(ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/*  266 */     CompoundType compoundType = null;
/*      */     
/*      */     try {
/*  269 */       compoundType = (CompoundType)makeType(paramClassDefinition.getType(), paramClassDefinition, paramContextStack);
/*  270 */     } catch (ClassCastException classCastException) {}
/*      */     
/*  272 */     return compoundType;
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
/*      */   protected void destroy() {
/*  284 */     if (!this.destroyed) {
/*  285 */       super.destroy();
/*      */       
/*  287 */       if (this.methods != null) {
/*  288 */         for (byte b = 0; b < this.methods.length; b++) {
/*  289 */           if (this.methods[b] != null) this.methods[b].destroy(); 
/*      */         } 
/*  291 */         this.methods = null;
/*      */       } 
/*      */       
/*  294 */       if (this.interfaces != null) {
/*  295 */         for (byte b = 0; b < this.interfaces.length; b++) {
/*  296 */           if (this.interfaces[b] != null) this.interfaces[b].destroy(); 
/*      */         } 
/*  298 */         this.interfaces = null;
/*      */       } 
/*      */       
/*  301 */       if (this.members != null) {
/*  302 */         for (byte b = 0; b < this.members.length; b++) {
/*  303 */           if (this.members[b] != null) this.members[b].destroy(); 
/*      */         } 
/*  305 */         this.members = null;
/*      */       } 
/*      */       
/*  308 */       this.classDef = null;
/*  309 */       this.classDecl = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Class loadClass() {
/*  318 */     Class<?> clazz = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  328 */       this.env.getMain().compileAllClasses(this.env);
/*  329 */     } catch (Exception exception) {
/*  330 */       for (Enumeration<ClassDeclaration> enumeration = this.env.getClasses(); enumeration.hasMoreElements();) {
/*  331 */         classDeclaration = enumeration.nextElement();
/*      */       }
/*  333 */       failedConstraint(26, false, this.stack, "required classes");
/*  334 */       this.env.flushErrors();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  343 */       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*  344 */       clazz = classLoader.loadClass(getQualifiedName());
/*  345 */     } catch (ClassNotFoundException classNotFoundException) {
/*      */       
/*      */       try {
/*  348 */         clazz = this.env.classPathLoader.loadClass(getQualifiedName());
/*  349 */       } catch (NullPointerException nullPointerException) {
/*      */       
/*  351 */       } catch (ClassNotFoundException classNotFoundException1) {}
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
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  366 */     if (clazz == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  373 */       if (this.env.loader == null) {
/*  374 */         File file = this.env.getMain().getDestinationDir();
/*  375 */         if (file == null) {
/*  376 */           file = new File(".");
/*      */         }
/*  378 */         this.env.loader = new DirectoryLoader(file);
/*      */       } 
/*      */       
/*      */       try {
/*  382 */         clazz = this.env.loader.loadClass(getQualifiedName());
/*  383 */       } catch (Exception exception) {}
/*      */     } 
/*      */     
/*  386 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean printExtends(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  396 */     ClassType classType = getSuperclass();
/*      */     
/*  398 */     if (classType != null && (!paramBoolean2 || (
/*  399 */       !classType.isType(1024) && !classType.isType(2048)))) {
/*  400 */       paramIndentingWriter.p(" extends ");
/*  401 */       classType.printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  402 */       return true;
/*      */     } 
/*  404 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void printImplements(IndentingWriter paramIndentingWriter, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  415 */     InterfaceType[] arrayOfInterfaceType = getInterfaces();
/*      */     
/*  417 */     String str = " implements";
/*      */     
/*  419 */     if (isInterface()) {
/*  420 */       str = " extends";
/*      */     }
/*      */     
/*  423 */     if (paramBoolean2) {
/*  424 */       str = ":";
/*      */     }
/*      */     
/*  427 */     for (byte b = 0; b < arrayOfInterfaceType.length; b++) {
/*  428 */       if (!paramBoolean2 || (!arrayOfInterfaceType[b].isType(1024) && !arrayOfInterfaceType[b].isType(2048))) {
/*  429 */         if (b == 0) {
/*  430 */           paramIndentingWriter.p(paramString + str + " ");
/*      */         } else {
/*  432 */           paramIndentingWriter.p(", ");
/*      */         } 
/*  434 */         arrayOfInterfaceType[b].printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
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
/*      */   protected void printMembers(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  446 */     Member[] arrayOfMember = getMembers();
/*      */     
/*  448 */     for (byte b = 0; b < arrayOfMember.length; b++) {
/*  449 */       if (!arrayOfMember[b].isInnerClassDeclaration()) {
/*  450 */         String str2; Type type = arrayOfMember[b].getType();
/*  451 */         String str1 = arrayOfMember[b].getVisibility();
/*      */ 
/*      */         
/*  454 */         if (paramBoolean2) {
/*  455 */           str2 = arrayOfMember[b].getIDLName();
/*      */         } else {
/*  457 */           str2 = arrayOfMember[b].getName();
/*      */         } 
/*      */         
/*  460 */         String str3 = arrayOfMember[b].getValue();
/*      */         
/*  462 */         paramIndentingWriter.p(str1);
/*  463 */         if (str1.length() > 0) {
/*  464 */           paramIndentingWriter.p(" ");
/*      */         }
/*  466 */         type.printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  467 */         paramIndentingWriter.p(" " + str2);
/*      */         
/*  469 */         if (str3 != null) {
/*  470 */           paramIndentingWriter.pln(" = " + str3 + ";");
/*      */         } else {
/*  472 */           paramIndentingWriter.pln(";");
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
/*      */   protected void printMethods(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  485 */     Method[] arrayOfMethod = getMethods();
/*      */     
/*  487 */     for (byte b = 0; b < arrayOfMethod.length; b++) {
/*  488 */       Method method = arrayOfMethod[b];
/*  489 */       printMethod(method, paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
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
/*      */   protected void printMethod(Method paramMethod, IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*      */     ValueType[] arrayOfValueType;
/*  504 */     String str = paramMethod.getVisibility();
/*      */     
/*  506 */     paramIndentingWriter.p(str);
/*  507 */     if (str.length() > 0) {
/*  508 */       paramIndentingWriter.p(" ");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  513 */     paramMethod.getReturnType().printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*      */ 
/*      */ 
/*      */     
/*  517 */     if (paramBoolean2) {
/*  518 */       paramIndentingWriter.p(" " + paramMethod.getIDLName());
/*      */     } else {
/*  520 */       paramIndentingWriter.p(" " + paramMethod.getName());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  525 */     paramIndentingWriter.p(" (");
/*  526 */     Type[] arrayOfType = paramMethod.getArguments();
/*  527 */     String[] arrayOfString = paramMethod.getArgumentNames();
/*      */     
/*  529 */     for (byte b1 = 0; b1 < arrayOfType.length; b1++) {
/*  530 */       if (b1 > 0) {
/*  531 */         paramIndentingWriter.p(", ");
/*      */       }
/*      */       
/*  534 */       if (paramBoolean2) {
/*  535 */         paramIndentingWriter.p("in ");
/*      */       }
/*      */       
/*  538 */       arrayOfType[b1].printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  539 */       paramIndentingWriter.p(" " + arrayOfString[b1]);
/*      */     } 
/*  541 */     paramIndentingWriter.p(")");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  547 */     if (isType(65536)) {
/*  548 */       arrayOfValueType = paramMethod.getImplExceptions();
/*      */     } else {
/*  550 */       arrayOfValueType = paramMethod.getExceptions();
/*      */     } 
/*      */     
/*  553 */     for (byte b2 = 0; b2 < arrayOfValueType.length; b2++) {
/*  554 */       if (b2 == 0) {
/*  555 */         if (paramBoolean2) {
/*  556 */           paramIndentingWriter.p(" raises (");
/*      */         } else {
/*  558 */           paramIndentingWriter.p(" throws ");
/*      */         } 
/*      */       } else {
/*  561 */         paramIndentingWriter.p(", ");
/*      */       } 
/*      */       
/*  564 */       if (paramBoolean2) {
/*  565 */         if (paramBoolean1) {
/*  566 */           paramIndentingWriter.p(arrayOfValueType[b2].getQualifiedIDLExceptionName(paramBoolean3));
/*      */         } else {
/*  568 */           paramIndentingWriter.p(arrayOfValueType[b2].getIDLExceptionName());
/*      */         } 
/*  570 */         paramIndentingWriter.p(" [a.k.a. ");
/*  571 */         arrayOfValueType[b2].printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  572 */         paramIndentingWriter.p("]");
/*      */       } else {
/*  574 */         arrayOfValueType[b2].printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*      */       } 
/*      */     } 
/*      */     
/*  578 */     if (paramBoolean2 && arrayOfValueType.length > 0) {
/*  579 */       paramIndentingWriter.p(")");
/*      */     }
/*      */     
/*  582 */     if (paramMethod.isInherited()) {
/*  583 */       paramIndentingWriter.p(" // Inherited from ");
/*  584 */       paramIndentingWriter.p(paramMethod.getDeclaredBy());
/*      */     } 
/*      */     
/*  587 */     paramIndentingWriter.pln(";");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CompoundType(ContextStack paramContextStack, int paramInt, ClassDefinition paramClassDefinition) {
/*  595 */     super(paramContextStack, paramInt);
/*  596 */     this.classDef = paramClassDefinition;
/*  597 */     this.classDecl = paramClassDefinition.getClassDeclaration();
/*  598 */     this.interfaces = new InterfaceType[0];
/*  599 */     this.methods = new Method[0];
/*  600 */     this.members = new Member[0];
/*      */ 
/*      */ 
/*      */     
/*  604 */     if (paramClassDefinition.isInnerClass()) {
/*  605 */       setTypeCode(paramInt | Integer.MIN_VALUE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  610 */     setFlags();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setFlags() {
/*      */     try {
/*  619 */       this.isCORBAObject = this.env.defCorbaObject.implementedBy((Environment)this.env, this.classDecl);
/*  620 */       this.isIDLEntity = this.env.defIDLEntity.implementedBy((Environment)this.env, this.classDecl);
/*  621 */       this.isValueBase = this.env.defValueBase.implementedBy((Environment)this.env, this.classDecl);
/*  622 */       this.isAbstractBase = (isInterface() && this.isIDLEntity && !this.isValueBase && !this.isCORBAObject);
/*      */ 
/*      */ 
/*      */       
/*  626 */       this.isCORBAUserException = (this.classDecl.getName() == idCorbaUserException);
/*      */ 
/*      */ 
/*      */       
/*  630 */       if (this.env.defThrowable.implementedBy((Environment)this.env, this.classDecl)) {
/*      */ 
/*      */ 
/*      */         
/*  634 */         this.isException = true;
/*      */ 
/*      */ 
/*      */         
/*  638 */         if (this.env.defRuntimeException.implementedBy((Environment)this.env, this.classDecl) || this.env.defError
/*  639 */           .implementedBy((Environment)this.env, this.classDecl)) {
/*  640 */           this.isCheckedException = false;
/*      */         } else {
/*  642 */           this.isCheckedException = true;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  647 */         if (this.env.defRemoteException.implementedBy((Environment)this.env, this.classDecl)) {
/*  648 */           this.isRemoteExceptionOrSubclass = true;
/*      */         } else {
/*  650 */           this.isRemoteExceptionOrSubclass = false;
/*      */         } 
/*      */       } else {
/*  653 */         this.isException = false;
/*      */       } 
/*  655 */     } catch (ClassNotFound classNotFound) {
/*  656 */       classNotFound(this.stack, classNotFound);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CompoundType(ContextStack paramContextStack, ClassDefinition paramClassDefinition, int paramInt) {
/*  666 */     super(paramContextStack, paramInt);
/*  667 */     this.classDef = paramClassDefinition;
/*  668 */     this.classDecl = paramClassDefinition.getClassDeclaration();
/*      */ 
/*      */ 
/*      */     
/*  672 */     if (paramClassDefinition.isInnerClass()) {
/*  673 */       setTypeCode(paramInt | Integer.MIN_VALUE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  678 */     setFlags();
/*      */ 
/*      */ 
/*      */     
/*  682 */     Identifier identifier = paramClassDefinition.getName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  690 */       String str = IDLNames.getClassOrInterfaceName(identifier, this.env);
/*  691 */       String[] arrayOfString = IDLNames.getModuleNames(identifier, isBoxed(), this.env);
/*      */       
/*  693 */       setNames(identifier, arrayOfString, str);
/*      */ 
/*      */ 
/*      */       
/*  697 */       if (isException()) {
/*      */ 
/*      */ 
/*      */         
/*  701 */         this.isException = true;
/*  702 */         this.idlExceptionName = IDLNames.getExceptionName(getIDLName());
/*  703 */         this
/*  704 */           .qualifiedIDLExceptionName = IDLNames.getQualifiedName(getIDLModuleNames(), this.idlExceptionName);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  709 */       this.interfaces = null;
/*  710 */       this.methods = null;
/*  711 */       this.members = null;
/*      */     }
/*  713 */     catch (Exception exception) {
/*  714 */       failedConstraint(7, false, paramContextStack, identifier.toString(), exception.getMessage());
/*  715 */       throw new CompilerError("");
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
/*      */   protected boolean initialize(Vector paramVector1, Vector paramVector2, Vector paramVector3, ContextStack paramContextStack, boolean paramBoolean) {
/*  728 */     boolean bool = true;
/*      */ 
/*      */ 
/*      */     
/*  732 */     if (paramVector1 != null && paramVector1.size() > 0) {
/*  733 */       this.interfaces = new InterfaceType[paramVector1.size()];
/*  734 */       paramVector1.copyInto((Object[])this.interfaces);
/*      */     } else {
/*  736 */       this.interfaces = new InterfaceType[0];
/*      */     } 
/*      */     
/*  739 */     if (paramVector2 != null && paramVector2.size() > 0) {
/*  740 */       this.methods = new Method[paramVector2.size()];
/*  741 */       paramVector2.copyInto((Object[])this.methods);
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  746 */         IDLNames.setMethodNames(this, this.methods, this.env);
/*  747 */       } catch (Exception exception) {
/*  748 */         failedConstraint(13, paramBoolean, paramContextStack, getQualifiedName(), exception.getMessage());
/*  749 */         bool = false;
/*      */       } 
/*      */     } else {
/*      */       
/*  753 */       this.methods = new Method[0];
/*      */     } 
/*      */     
/*  756 */     if (paramVector3 != null && paramVector3.size() > 0) {
/*  757 */       this.members = new Member[paramVector3.size()];
/*  758 */       paramVector3.copyInto((Object[])this.members);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  763 */       for (byte b = 0; b < this.members.length; b++) {
/*  764 */         if (this.members[b].isInnerClassDeclaration()) {
/*      */           try {
/*  766 */             this.members[b].init(paramContextStack, this);
/*  767 */           } catch (CompilerError compilerError) {
/*  768 */             return false;
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  776 */         IDLNames.setMemberNames(this, this.members, this.methods, this.env);
/*  777 */       } catch (Exception exception) {
/*  778 */         byte b1 = this.classDef.isInterface() ? 19 : 20;
/*  779 */         failedConstraint(b1, paramBoolean, paramContextStack, getQualifiedName(), exception.getMessage());
/*  780 */         bool = false;
/*      */       } 
/*      */     } else {
/*      */       
/*  784 */       this.members = new Member[0];
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  789 */     if (bool) {
/*  790 */       bool = setRepositoryID();
/*      */     }
/*      */     
/*  793 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static Type makeType(Type paramType, ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/*  803 */     if (paramContextStack.anyErrors()) return null;
/*      */ 
/*      */ 
/*      */     
/*  807 */     String str = paramType.toString();
/*      */     
/*  809 */     Type type = getType(str, paramContextStack);
/*      */     
/*  811 */     if (type != null) {
/*  812 */       return type;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  817 */     type = getType(str + paramContextStack.getContextCodeString(), paramContextStack);
/*      */     
/*  819 */     if (type != null) {
/*  820 */       return type;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  825 */     BatchEnvironment batchEnvironment = paramContextStack.getEnv();
/*  826 */     int i = paramType.getTypeCode();
/*  827 */     switch (i) {
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*  839 */         type = PrimitiveType.forPrimitive(paramType, paramContextStack);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  958 */         return type;case 9: type = ArrayType.forArray(paramType, paramContextStack); return type;case 10: try { ClassDefinition classDefinition = paramClassDefinition; if (classDefinition == null) classDefinition = batchEnvironment.getClassDeclaration(paramType).getClassDefinition((Environment)batchEnvironment);  if (classDefinition.isInterface()) { type = SpecialInterfaceType.forSpecial(classDefinition, paramContextStack); if (type == null) if (batchEnvironment.defRemote.implementedBy((Environment)batchEnvironment, classDefinition.getClassDeclaration())) { boolean bool = paramContextStack.isParentAValue(); type = RemoteType.forRemote(classDefinition, paramContextStack, bool); if (type == null && bool) type = NCInterfaceType.forNCInterface(classDefinition, paramContextStack);  } else { type = AbstractType.forAbstract(classDefinition, paramContextStack, true); if (type == null) type = NCInterfaceType.forNCInterface(classDefinition, paramContextStack);  }   } else { type = SpecialClassType.forSpecial(classDefinition, paramContextStack); if (type == null) { ClassDeclaration classDeclaration = classDefinition.getClassDeclaration(); if (batchEnvironment.defRemote.implementedBy((Environment)batchEnvironment, classDeclaration)) { boolean bool = paramContextStack.isParentAValue(); type = ImplementationType.forImplementation(classDefinition, paramContextStack, bool); if (type == null && bool) type = NCClassType.forNCClass(classDefinition, paramContextStack);  } else { if (batchEnvironment.defSerializable.implementedBy((Environment)batchEnvironment, classDeclaration)) type = ValueType.forValue(classDefinition, paramContextStack, true);  if (type == null) type = NCClassType.forNCClass(classDefinition, paramContextStack);  }  }  }  } catch (ClassNotFound classNotFound) { classNotFound(paramContextStack, classNotFound); }  return type;
/*      */     } 
/*      */     throw new CompilerError("Unknown typecode (" + i + ") for " + paramType.getTypeSignature());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isRemoteException(ClassType paramClassType, BatchEnvironment paramBatchEnvironment) {
/*  966 */     Type type = paramClassType.getClassDeclaration().getType();
/*      */     
/*  968 */     if (type.equals(paramBatchEnvironment.typeRemoteException) || type
/*  969 */       .equals(paramBatchEnvironment.typeIOException) || type
/*  970 */       .equals(paramBatchEnvironment.typeException) || type
/*  971 */       .equals(paramBatchEnvironment.typeThrowable))
/*      */     {
/*  973 */       return true;
/*      */     }
/*  975 */     return false;
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
/*      */   protected boolean isConformingRemoteMethod(Method paramMethod, boolean paramBoolean) throws ClassNotFound {
/*  987 */     boolean bool = false;
/*  988 */     ValueType[] arrayOfValueType = paramMethod.getExceptions();
/*      */     byte b;
/*  990 */     for (b = 0; b < arrayOfValueType.length; b++) {
/*      */ 
/*      */ 
/*      */       
/*  994 */       if (isRemoteException(arrayOfValueType[b], this.env)) {
/*      */ 
/*      */ 
/*      */         
/*  998 */         bool = true;
/*      */ 
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/* 1005 */     if (!bool)
/*      */     {
/*      */ 
/*      */       
/* 1009 */       failedConstraint(5, paramBoolean, this.stack, paramMethod.getEnclosing(), paramMethod.toString());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1015 */     b = !isIDLEntityException(paramMethod.getReturnType(), paramMethod, paramBoolean) ? 1 : 0;
/* 1016 */     if (b != 0) {
/* 1017 */       Type[] arrayOfType = paramMethod.getArguments();
/* 1018 */       for (byte b1 = 0; b1 < arrayOfType.length; b1++) {
/* 1019 */         if (isIDLEntityException(arrayOfType[b1], paramMethod, paramBoolean)) {
/* 1020 */           b = 0;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1026 */     return (bool && b != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isIDLEntityException(Type paramType, Method paramMethod, boolean paramBoolean) throws ClassNotFound {
/* 1031 */     if (paramType.isArray()) {
/* 1032 */       paramType = paramType.getElementType();
/*      */     }
/* 1034 */     if (paramType.isCompound() && (
/* 1035 */       (CompoundType)paramType).isIDLEntityException()) {
/* 1036 */       failedConstraint(18, paramBoolean, this.stack, paramMethod.getEnclosing(), paramMethod.toString());
/* 1037 */       return true;
/*      */     } 
/*      */     
/* 1040 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void swapInvalidTypes() {
/*      */     byte b;
/* 1050 */     for (b = 0; b < this.interfaces.length; b++) {
/* 1051 */       if (this.interfaces[b].getStatus() != 1) {
/* 1052 */         this.interfaces[b] = (InterfaceType)getValidType(this.interfaces[b]);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1058 */     for (b = 0; b < this.methods.length; b++) {
/* 1059 */       this.methods[b].swapInvalidTypes();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1064 */     for (b = 0; b < this.members.length; b++) {
/* 1065 */       this.members[b].swapInvalidTypes();
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
/*      */   protected boolean addTypes(int paramInt, HashSet paramHashSet, Vector paramVector) {
/* 1079 */     boolean bool = super.addTypes(paramInt, paramHashSet, paramVector);
/*      */ 
/*      */ 
/*      */     
/* 1083 */     if (bool) {
/*      */ 
/*      */ 
/*      */       
/* 1087 */       ClassType classType = getSuperclass();
/*      */       
/* 1089 */       if (classType != null) {
/* 1090 */         classType.addTypes(paramInt, paramHashSet, paramVector);
/*      */       }
/*      */ 
/*      */       
/*      */       byte b;
/*      */       
/* 1096 */       for (b = 0; b < this.interfaces.length; b++)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1101 */         this.interfaces[b].addTypes(paramInt, paramHashSet, paramVector);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1107 */       for (b = 0; b < this.methods.length; b++) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1112 */         this.methods[b].getReturnType().addTypes(paramInt, paramHashSet, paramVector);
/*      */ 
/*      */ 
/*      */         
/* 1116 */         Type[] arrayOfType = this.methods[b].getArguments();
/*      */ 
/*      */         
/* 1119 */         for (byte b1 = 0; b1 < arrayOfType.length; b1++) {
/*      */           
/* 1121 */           Type type = arrayOfType[b1];
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1126 */           type.addTypes(paramInt, paramHashSet, paramVector);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1131 */         ValueType[] arrayOfValueType = this.methods[b].getExceptions();
/*      */ 
/*      */         
/* 1134 */         for (byte b2 = 0; b2 < arrayOfValueType.length; b2++) {
/*      */           
/* 1136 */           ValueType valueType = arrayOfValueType[b2];
/*      */ 
/*      */ 
/*      */           
/* 1140 */           valueType.addTypes(paramInt, paramHashSet, paramVector);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1147 */       for (b = 0; b < this.members.length; b++) {
/*      */         
/* 1149 */         Type type = this.members[b].getType();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1154 */         type.addTypes(paramInt, paramHashSet, paramVector);
/*      */       } 
/*      */     } 
/*      */     
/* 1158 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isConformingConstantType(MemberDefinition paramMemberDefinition) {
/* 1165 */     return isConformingConstantType(paramMemberDefinition.getType(), paramMemberDefinition);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isConformingConstantType(Type paramType, MemberDefinition paramMemberDefinition) {
/* 1175 */     boolean bool = true;
/* 1176 */     int i = paramType.getTypeCode();
/* 1177 */     switch (i) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/* 1210 */         return bool;
/*      */       case 10:
/*      */         if (paramType.getClassName() != idJavaLangString) {
/*      */           failedConstraint(3, false, this.stack, paramMemberDefinition.getClassDefinition(), paramMemberDefinition.getName());
/*      */           bool = false;
/*      */         } 
/*      */       case 9:
/*      */         failedConstraint(3, false, this.stack, paramMemberDefinition.getClassDefinition(), paramMemberDefinition.getName());
/*      */         bool = false;
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
/*      */   protected Vector updateParentClassMethods(ClassDefinition paramClassDefinition, Vector<Method> paramVector, boolean paramBoolean, ContextStack paramContextStack) throws ClassNotFound {
/* 1231 */     ClassDeclaration classDeclaration = paramClassDefinition.getSuperClass((Environment)this.env);
/*      */     
/* 1233 */     while (classDeclaration != null) {
/*      */       
/* 1235 */       ClassDefinition classDefinition = classDeclaration.getClassDefinition((Environment)this.env);
/* 1236 */       Identifier identifier = classDeclaration.getName();
/*      */       
/* 1238 */       if (identifier == idJavaLangObject) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/* 1243 */       MemberDefinition memberDefinition = classDefinition.getFirstMember();
/* 1244 */       for (; memberDefinition != null; 
/* 1245 */         memberDefinition = memberDefinition.getNextMember()) {
/*      */         
/* 1247 */         if (memberDefinition.isMethod() && 
/* 1248 */           !memberDefinition.isInitializer() && 
/* 1249 */           !memberDefinition.isConstructor() && 
/* 1250 */           !memberDefinition.isPrivate()) {
/*      */           Method method;
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/* 1256 */             method = new Method(this, memberDefinition, paramBoolean, paramContextStack);
/* 1257 */           } catch (Exception exception) {
/*      */             
/* 1259 */             return null;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1264 */           int i = paramVector.indexOf(method);
/* 1265 */           if (i >= 0) {
/*      */ 
/*      */ 
/*      */             
/* 1269 */             Method method1 = paramVector.elementAt(i);
/* 1270 */             method1.setDeclaredBy(identifier);
/*      */           } else {
/* 1272 */             paramVector.addElement(method);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1278 */       classDeclaration = classDefinition.getSuperClass((Environment)this.env);
/*      */     } 
/*      */     
/* 1281 */     return paramVector;
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
/*      */   protected Vector addAllMethods(ClassDefinition paramClassDefinition, Vector<Method> paramVector, boolean paramBoolean1, boolean paramBoolean2, ContextStack paramContextStack) throws ClassNotFound {
/* 1305 */     ClassDeclaration[] arrayOfClassDeclaration = paramClassDefinition.getInterfaces();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1311 */     for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/*      */       
/* 1313 */       Vector vector = addAllMethods(arrayOfClassDeclaration[b].getClassDefinition((Environment)this.env), paramVector, paramBoolean1, paramBoolean2, paramContextStack);
/*      */ 
/*      */       
/* 1316 */       if (vector == null) {
/* 1317 */         return null;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1324 */     MemberDefinition memberDefinition = paramClassDefinition.getFirstMember();
/* 1325 */     for (; memberDefinition != null; 
/* 1326 */       memberDefinition = memberDefinition.getNextMember()) {
/*      */       
/* 1328 */       if (memberDefinition.isMethod() && 
/* 1329 */         !memberDefinition.isInitializer() && 
/* 1330 */         !memberDefinition.isPrivate()) {
/*      */         Method method;
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1336 */           method = new Method(this, memberDefinition, paramBoolean2, paramContextStack);
/* 1337 */         } catch (Exception exception) {
/*      */           
/* 1339 */           return null;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1344 */         if (!paramVector.contains(method)) {
/*      */ 
/*      */ 
/*      */           
/* 1348 */           paramVector.addElement(method);
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 1355 */           if (paramBoolean1 && paramClassDefinition != this.classDef && 
/* 1356 */             !paramContextStack.isParentAValue() && !paramContextStack.getContext().isValue()) {
/*      */ 
/*      */ 
/*      */             
/* 1360 */             Method method3 = paramVector.elementAt(paramVector.indexOf(method));
/* 1361 */             ClassDefinition classDefinition = method3.getMemberDefinition().getClassDefinition();
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1366 */             if (paramClassDefinition != classDefinition && 
/* 1367 */               !inheritsFrom(paramClassDefinition, classDefinition) && 
/* 1368 */               !inheritsFrom(classDefinition, paramClassDefinition)) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1373 */               String str = classDefinition.getName() + " and " + paramClassDefinition.getName();
/* 1374 */               failedConstraint(6, paramBoolean2, paramContextStack, this.classDef, str, method);
/* 1375 */               return null;
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1382 */           int i = paramVector.indexOf(method);
/* 1383 */           Method method1 = paramVector.get(i);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1388 */           Method method2 = method.mergeWith(method1);
/*      */ 
/*      */           
/* 1391 */           paramVector.set(i, method2);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1396 */     return paramVector;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean inheritsFrom(ClassDefinition paramClassDefinition1, ClassDefinition paramClassDefinition2) {
/* 1402 */     if (paramClassDefinition1 == paramClassDefinition2) {
/* 1403 */       return true;
/*      */     }
/*      */     
/* 1406 */     if (paramClassDefinition1.getSuperClass() != null) {
/* 1407 */       ClassDefinition classDefinition = paramClassDefinition1.getSuperClass().getClassDefinition();
/* 1408 */       if (inheritsFrom(classDefinition, paramClassDefinition2)) {
/* 1409 */         return true;
/*      */       }
/*      */     } 
/* 1412 */     ClassDeclaration[] arrayOfClassDeclaration = paramClassDefinition1.getInterfaces();
/* 1413 */     for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/* 1414 */       ClassDefinition classDefinition = arrayOfClassDeclaration[b].getClassDefinition();
/* 1415 */       if (inheritsFrom(classDefinition, paramClassDefinition2))
/* 1416 */         return true; 
/*      */     } 
/* 1418 */     return false;
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
/*      */   protected Vector addRemoteInterfaces(Vector<NCInterfaceType> paramVector, boolean paramBoolean, ContextStack paramContextStack) throws ClassNotFound {
/* 1431 */     ClassDefinition classDefinition = getClassDefinition();
/* 1432 */     ClassDeclaration[] arrayOfClassDeclaration = classDefinition.getInterfaces();
/*      */     
/* 1434 */     paramContextStack.setNewContextCode(10);
/*      */     
/* 1436 */     for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/*      */       NCInterfaceType nCInterfaceType;
/* 1438 */       ClassDefinition classDefinition1 = arrayOfClassDeclaration[b].getClassDefinition((Environment)this.env);
/*      */ 
/*      */ 
/*      */       
/* 1442 */       SpecialInterfaceType specialInterfaceType = SpecialInterfaceType.forSpecial(classDefinition1, paramContextStack);
/*      */       
/* 1444 */       if (specialInterfaceType == null)
/*      */       {
/*      */ 
/*      */         
/* 1448 */         if (this.env.defRemote.implementedBy((Environment)this.env, arrayOfClassDeclaration[b])) {
/*      */ 
/*      */ 
/*      */           
/* 1452 */           RemoteType remoteType = RemoteType.forRemote(classDefinition1, paramContextStack, false);
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 1458 */           AbstractType abstractType = AbstractType.forAbstract(classDefinition1, paramContextStack, true);
/*      */           
/* 1460 */           if (abstractType == null && paramBoolean)
/*      */           {
/*      */ 
/*      */             
/* 1464 */             nCInterfaceType = NCInterfaceType.forNCInterface(classDefinition1, paramContextStack);
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/* 1469 */       if (nCInterfaceType != null) {
/* 1470 */         paramVector.addElement(nCInterfaceType);
/*      */       } else {
/* 1472 */         return null;
/*      */       } 
/*      */     } 
/*      */     
/* 1476 */     return paramVector;
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
/*      */   protected Vector addNonRemoteInterfaces(Vector<NCInterfaceType> paramVector, ContextStack paramContextStack) throws ClassNotFound {
/* 1488 */     ClassDefinition classDefinition = getClassDefinition();
/* 1489 */     ClassDeclaration[] arrayOfClassDeclaration = classDefinition.getInterfaces();
/*      */     
/* 1491 */     paramContextStack.setNewContextCode(10);
/*      */     
/* 1493 */     for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/*      */       NCInterfaceType nCInterfaceType;
/* 1495 */       ClassDefinition classDefinition1 = arrayOfClassDeclaration[b].getClassDefinition((Environment)this.env);
/*      */ 
/*      */ 
/*      */       
/* 1499 */       SpecialInterfaceType specialInterfaceType = SpecialInterfaceType.forSpecial(classDefinition1, paramContextStack);
/*      */       
/* 1501 */       if (specialInterfaceType == null) {
/*      */ 
/*      */ 
/*      */         
/* 1505 */         AbstractType abstractType = AbstractType.forAbstract(classDefinition1, paramContextStack, true);
/*      */         
/* 1507 */         if (abstractType == null)
/*      */         {
/*      */ 
/*      */           
/* 1511 */           nCInterfaceType = NCInterfaceType.forNCInterface(classDefinition1, paramContextStack);
/*      */         }
/*      */       } 
/*      */       
/* 1515 */       if (nCInterfaceType != null) {
/* 1516 */         paramVector.addElement(nCInterfaceType);
/*      */       } else {
/* 1518 */         return null;
/*      */       } 
/*      */     } 
/*      */     
/* 1522 */     return paramVector;
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
/*      */   protected boolean addAllMembers(Vector<Member> paramVector, boolean paramBoolean1, boolean paramBoolean2, ContextStack paramContextStack) {
/* 1535 */     boolean bool = true;
/*      */ 
/*      */ 
/*      */     
/* 1539 */     MemberDefinition memberDefinition = getClassDefinition().getFirstMember();
/* 1540 */     for (; memberDefinition != null && bool; 
/* 1541 */       memberDefinition = memberDefinition.getNextMember()) {
/*      */       
/* 1543 */       if (!memberDefinition.isMethod()) {
/*      */         
/*      */         try {
/* 1546 */           String str = null;
/*      */ 
/*      */ 
/*      */           
/* 1550 */           memberDefinition.getValue((Environment)this.env);
/*      */ 
/*      */ 
/*      */           
/* 1554 */           Node node = memberDefinition.getValue();
/*      */           
/* 1556 */           if (node != null)
/*      */           {
/*      */ 
/*      */             
/* 1560 */             if (memberDefinition.getType().getTypeCode() == 2) {
/* 1561 */               Integer integer = (Integer)((IntegerExpression)node).getValue();
/* 1562 */               str = "L'" + String.valueOf((char)integer.intValue()) + "'";
/*      */             } else {
/* 1564 */               str = node.toString();
/*      */             } 
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1570 */           if (paramBoolean1 && memberDefinition.getInnerClass() == null)
/*      */           {
/*      */ 
/*      */             
/* 1574 */             if (str == null || !isConformingConstantType(memberDefinition)) {
/* 1575 */               failedConstraint(3, paramBoolean2, paramContextStack, memberDefinition.getClassDefinition(), memberDefinition.getName());
/* 1576 */               bool = false;
/*      */ 
/*      */               
/*      */               break;
/*      */             } 
/*      */           }
/*      */           
/*      */           try {
/* 1584 */             Member member = new Member(memberDefinition, str, paramContextStack, this);
/* 1585 */             paramVector.addElement(member);
/* 1586 */           } catch (CompilerError compilerError) {
/* 1587 */             bool = false;
/*      */           }
/*      */         
/* 1590 */         } catch (ClassNotFound classNotFound) {
/* 1591 */           classNotFound(paramContextStack, classNotFound);
/* 1592 */           bool = false;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1597 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean addConformingConstants(Vector<Member> paramVector, boolean paramBoolean, ContextStack paramContextStack) {
/* 1607 */     boolean bool = true;
/*      */ 
/*      */ 
/*      */     
/* 1611 */     MemberDefinition memberDefinition = getClassDefinition().getFirstMember();
/* 1612 */     for (; memberDefinition != null && bool; 
/* 1613 */       memberDefinition = memberDefinition.getNextMember()) {
/*      */       
/* 1615 */       if (!memberDefinition.isMethod()) {
/*      */         
/*      */         try {
/* 1618 */           String str = null;
/*      */ 
/*      */ 
/*      */           
/* 1622 */           memberDefinition.getValue((Environment)this.env);
/*      */ 
/*      */ 
/*      */           
/* 1626 */           Node node = memberDefinition.getValue();
/*      */           
/* 1628 */           if (node != null) {
/* 1629 */             str = node.toString();
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1635 */           if (str != null) {
/*      */ 
/*      */ 
/*      */             
/* 1639 */             if (!isConformingConstantType(memberDefinition)) {
/* 1640 */               failedConstraint(3, paramBoolean, paramContextStack, memberDefinition.getClassDefinition(), memberDefinition.getName());
/* 1641 */               bool = false;
/*      */ 
/*      */               
/*      */               break;
/*      */             } 
/*      */             
/*      */             try {
/* 1648 */               Member member = new Member(memberDefinition, str, paramContextStack, this);
/* 1649 */               paramVector.addElement(member);
/* 1650 */             } catch (CompilerError compilerError) {
/* 1651 */               bool = false;
/*      */             } 
/*      */           } 
/* 1654 */         } catch (ClassNotFound classNotFound) {
/* 1655 */           classNotFound(paramContextStack, classNotFound);
/* 1656 */           bool = false;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1661 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ValueType[] getMethodExceptions(MemberDefinition paramMemberDefinition, boolean paramBoolean, ContextStack paramContextStack) throws Exception {
/* 1668 */     boolean bool = true;
/* 1669 */     paramContextStack.setNewContextCode(5);
/* 1670 */     ClassDeclaration[] arrayOfClassDeclaration = paramMemberDefinition.getExceptions((Environment)this.env);
/* 1671 */     ValueType[] arrayOfValueType = new ValueType[arrayOfClassDeclaration.length];
/*      */     
/*      */     try {
/* 1674 */       for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/* 1675 */         ClassDefinition classDefinition = arrayOfClassDeclaration[b].getClassDefinition((Environment)this.env);
/*      */         try {
/* 1677 */           ValueType valueType = ValueType.forValue(classDefinition, paramContextStack, false);
/* 1678 */           if (valueType != null) {
/* 1679 */             arrayOfValueType[b] = valueType;
/*      */           } else {
/* 1681 */             bool = false;
/*      */           } 
/* 1683 */         } catch (ClassCastException classCastException) {
/* 1684 */           failedConstraint(22, paramBoolean, paramContextStack, getQualifiedName());
/* 1685 */           throw new CompilerError("Method: exception " + classDefinition.getName() + " not a class type!");
/* 1686 */         } catch (NullPointerException nullPointerException) {
/* 1687 */           failedConstraint(23, paramBoolean, paramContextStack, getQualifiedName());
/* 1688 */           throw new CompilerError("Method: caught null pointer exception");
/*      */         } 
/*      */       } 
/* 1691 */     } catch (ClassNotFound classNotFound) {
/* 1692 */       classNotFound(paramBoolean, paramContextStack, classNotFound);
/* 1693 */       bool = false;
/*      */     } 
/*      */     
/* 1696 */     if (!bool) {
/* 1697 */       throw new Exception();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1703 */     byte b1 = 0; byte b2;
/* 1704 */     for (b2 = 0; b2 < arrayOfValueType.length; b2++) {
/* 1705 */       for (byte b = 0; b < arrayOfValueType.length; b++) {
/* 1706 */         if (b2 != b && arrayOfValueType[b2] != null && arrayOfValueType[b2] == arrayOfValueType[b]) {
/* 1707 */           arrayOfValueType[b] = null;
/* 1708 */           b1++;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1712 */     if (b1 > 0) {
/* 1713 */       b2 = 0;
/* 1714 */       ValueType[] arrayOfValueType1 = new ValueType[arrayOfValueType.length - b1];
/* 1715 */       for (byte b = 0; b < arrayOfValueType.length; b++) {
/* 1716 */         if (arrayOfValueType[b] != null) {
/* 1717 */           arrayOfValueType1[b2++] = arrayOfValueType[b];
/*      */         }
/*      */       } 
/* 1720 */       arrayOfValueType = arrayOfValueType1;
/*      */     } 
/*      */     
/* 1723 */     return arrayOfValueType;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static String getVisibilityString(MemberDefinition paramMemberDefinition) {
/* 1728 */     String str1 = "";
/* 1729 */     String str2 = "";
/*      */     
/* 1731 */     if (paramMemberDefinition.isPublic()) {
/* 1732 */       str1 = str1 + "public";
/* 1733 */       str2 = " ";
/* 1734 */     } else if (paramMemberDefinition.isProtected()) {
/* 1735 */       str1 = str1 + "protected";
/* 1736 */       str2 = " ";
/* 1737 */     } else if (paramMemberDefinition.isPrivate()) {
/* 1738 */       str1 = str1 + "private";
/* 1739 */       str2 = " ";
/*      */     } 
/*      */     
/* 1742 */     if (paramMemberDefinition.isStatic()) {
/* 1743 */       str1 = str1 + str2;
/* 1744 */       str1 = str1 + "static";
/* 1745 */       str2 = " ";
/*      */     } 
/*      */     
/* 1748 */     if (paramMemberDefinition.isFinal()) {
/* 1749 */       str1 = str1 + str2;
/* 1750 */       str1 = str1 + "final";
/* 1751 */       str2 = " ";
/*      */     } 
/*      */     
/* 1754 */     return str1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean assertNotImpl(Type paramType, boolean paramBoolean1, ContextStack paramContextStack, CompoundType paramCompoundType, boolean paramBoolean2) {
/* 1763 */     if (paramType.isType(65536)) {
/* 1764 */       byte b = paramBoolean2 ? 28 : 21;
/* 1765 */       failedConstraint(b, paramBoolean1, paramContextStack, paramType, paramCompoundType.getName());
/* 1766 */       return false;
/*      */     } 
/* 1768 */     return true;
/*      */   }
/*      */   
/*      */   public class Method
/*      */     implements ContextElement, Cloneable
/*      */   {
/*      */     private MemberDefinition memberDef;
/*      */     private CompoundType enclosing;
/*      */     private ValueType[] exceptions;
/*      */     private ValueType[] implExceptions;
/*      */     private Type returnType;
/*      */     private Type[] arguments;
/*      */     private String[] argumentNames;
/*      */     private String vis;
/*      */     private String name;
/*      */     private String idlName;
/*      */     
/*      */     public boolean isInherited() {
/* 1786 */       return (this.declaredBy != this.enclosing.getIdentifier());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isAttribute() {
/* 1794 */       return (this.attributeKind != 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isReadWriteAttribute() {
/* 1801 */       return (this.attributeKind == 3 || this.attributeKind == 4);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getAttributeKind() {
/* 1809 */       return this.attributeKind;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getAttributeName() {
/* 1817 */       return this.attributeName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getAttributePairIndex() {
/* 1826 */       return this.attributePairIndex;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getElementName() {
/* 1833 */       return this.memberDef.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object param1Object) {
/* 1840 */       Method method = (Method)param1Object;
/*      */       
/* 1842 */       if (getName().equals(method.getName()) && this.arguments.length == method.arguments.length) {
/*      */ 
/*      */         
/* 1845 */         for (byte b = 0; b < this.arguments.length; b++) {
/* 1846 */           if (!this.arguments[b].equals(method.arguments[b])) {
/* 1847 */             return false;
/*      */           }
/*      */         } 
/* 1850 */         return true;
/*      */       } 
/* 1852 */       return false;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1856 */       return getName().hashCode() ^ Arrays.hashCode((Object[])this.arguments);
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
/*      */     public Method mergeWith(Method param1Method) {
/* 1868 */       if (!equals(param1Method)) {
/* 1869 */         CompoundType.this.env.error(0L, "attempt to merge method failed:", getName(), this.enclosing
/* 1870 */             .getClassDefinition().getName());
/*      */       }
/*      */       
/* 1873 */       Vector vector = new Vector();
/*      */       try {
/* 1875 */         collectCompatibleExceptions(param1Method.exceptions, this.exceptions, vector);
/*      */         
/* 1877 */         collectCompatibleExceptions(this.exceptions, param1Method.exceptions, vector);
/*      */       }
/* 1879 */       catch (ClassNotFound classNotFound) {
/* 1880 */         CompoundType.this.env.error(0L, "class.not.found", classNotFound.name, this.enclosing
/* 1881 */             .getClassDefinition().getName());
/* 1882 */         return null;
/*      */       } 
/*      */       
/* 1885 */       Method method = (Method)clone();
/* 1886 */       method.exceptions = new ValueType[vector.size()];
/* 1887 */       vector.copyInto((Object[])method.exceptions);
/* 1888 */       method.implExceptions = method.exceptions;
/*      */       
/* 1890 */       return method;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void collectCompatibleExceptions(ValueType[] param1ArrayOfValueType1, ValueType[] param1ArrayOfValueType2, Vector<ValueType> param1Vector) throws ClassNotFound {
/* 1901 */       for (byte b = 0; b < param1ArrayOfValueType1.length; b++) {
/* 1902 */         ClassDefinition classDefinition = param1ArrayOfValueType1[b].getClassDefinition();
/* 1903 */         if (!param1Vector.contains(param1ArrayOfValueType1[b])) {
/* 1904 */           for (byte b1 = 0; b1 < param1ArrayOfValueType2.length; b1++) {
/* 1905 */             if (classDefinition.subClassOf((Environment)this.enclosing
/* 1906 */                 .getEnv(), param1ArrayOfValueType2[b1]
/* 1907 */                 .getClassDeclaration())) {
/* 1908 */               param1Vector.addElement(param1ArrayOfValueType1[b]);
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CompoundType getEnclosing() {
/* 1920 */       return this.enclosing;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Identifier getDeclaredBy() {
/* 1928 */       return this.declaredBy;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getVisibility() {
/* 1935 */       return this.vis;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isPublic() {
/* 1942 */       return this.memberDef.isPublic();
/*      */     }
/*      */     
/*      */     public boolean isProtected() {
/* 1946 */       return this.memberDef.isPrivate();
/*      */     }
/*      */     
/*      */     public boolean isPrivate() {
/* 1950 */       return this.memberDef.isPrivate();
/*      */     }
/*      */     
/*      */     public boolean isStatic() {
/* 1954 */       return this.memberDef.isStatic();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/* 1961 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getIDLName() {
/* 1969 */       return this.idlName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type getType() {
/* 1976 */       return this.memberDef.getType();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isConstructor() {
/* 1983 */       return this.memberDef.isConstructor();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isNormalMethod() {
/* 1991 */       return (!this.memberDef.isConstructor() && this.attributeKind == 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type getReturnType() {
/* 1998 */       return this.returnType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type[] getArguments() {
/* 2005 */       return (Type[])this.arguments.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] getArgumentNames() {
/* 2012 */       return this.argumentNames;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public MemberDefinition getMemberDefinition() {
/* 2019 */       return this.memberDef;
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
/*      */     
/*      */     public ValueType[] getExceptions() {
/* 2032 */       return (ValueType[])this.exceptions.clone();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ValueType[] getImplExceptions() {
/* 2040 */       return (ValueType[])this.implExceptions.clone();
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
/*      */     public ValueType[] getUniqueCatchList(ValueType[] param1ArrayOfValueType) {
/* 2052 */       ValueType[] arrayOfValueType = param1ArrayOfValueType;
/* 2053 */       int i = param1ArrayOfValueType.length;
/*      */       
/*      */       try {
/*      */         byte b;
/*      */         
/* 2058 */         for (b = 0; b < param1ArrayOfValueType.length; b++) {
/* 2059 */           ClassDeclaration classDeclaration = param1ArrayOfValueType[b].getClassDeclaration();
/* 2060 */           if (CompoundType.this.env.defRemoteException.superClassOf((Environment)CompoundType.this.env, classDeclaration) || CompoundType.this.env.defRuntimeException
/* 2061 */             .superClassOf((Environment)CompoundType.this.env, classDeclaration) || CompoundType.this.env.defError
/* 2062 */             .superClassOf((Environment)CompoundType.this.env, classDeclaration)) {
/* 2063 */             param1ArrayOfValueType[b] = null;
/* 2064 */             i--;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 2069 */         for (b = 0; b < param1ArrayOfValueType.length; b++) {
/* 2070 */           if (param1ArrayOfValueType[b] != null) {
/* 2071 */             ClassDefinition classDefinition = param1ArrayOfValueType[b].getClassDefinition();
/* 2072 */             for (byte b1 = 0; b1 < param1ArrayOfValueType.length; b1++) {
/* 2073 */               if (b1 != b && param1ArrayOfValueType[b] != null && param1ArrayOfValueType[b1] != null && classDefinition
/* 2074 */                 .superClassOf((Environment)CompoundType.this.env, param1ArrayOfValueType[b1].getClassDeclaration())) {
/* 2075 */                 param1ArrayOfValueType[b1] = null;
/* 2076 */                 i--;
/*      */               }
/*      */             
/*      */             } 
/*      */           } 
/*      */         } 
/* 2082 */       } catch (ClassNotFound classNotFound) {
/* 2083 */         Type.classNotFound(CompoundType.this.stack, classNotFound);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2088 */       if (i < param1ArrayOfValueType.length) {
/* 2089 */         ValueType[] arrayOfValueType1 = new ValueType[i];
/* 2090 */         byte b1 = 0;
/* 2091 */         for (byte b2 = 0; b2 < param1ArrayOfValueType.length; b2++) {
/* 2092 */           if (param1ArrayOfValueType[b2] != null) {
/* 2093 */             arrayOfValueType1[b1++] = param1ArrayOfValueType[b2];
/*      */           }
/*      */         } 
/* 2096 */         param1ArrayOfValueType = arrayOfValueType1;
/*      */       } 
/*      */       
/* 2099 */       if (param1ArrayOfValueType.length == 0) {
/* 2100 */         return null;
/*      */       }
/* 2102 */       return param1ArrayOfValueType;
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
/*      */     
/*      */     public ValueType[] getFilteredStubExceptions(ValueType[] param1ArrayOfValueType) {
/* 2115 */       ValueType[] arrayOfValueType = param1ArrayOfValueType;
/* 2116 */       int i = param1ArrayOfValueType.length;
/*      */ 
/*      */       
/*      */       try {
/* 2120 */         for (byte b = 0; b < param1ArrayOfValueType.length; b++) {
/* 2121 */           ClassDeclaration classDeclaration = param1ArrayOfValueType[b].getClassDeclaration();
/* 2122 */           if ((CompoundType.this.env.defRemoteException.superClassOf((Environment)CompoundType.this.env, classDeclaration) && 
/* 2123 */             !CompoundType.this.env.defRemoteException.getClassDeclaration().equals(classDeclaration)) || CompoundType.this.env.defRuntimeException
/* 2124 */             .superClassOf((Environment)CompoundType.this.env, classDeclaration) || CompoundType.this.env.defError
/* 2125 */             .superClassOf((Environment)CompoundType.this.env, classDeclaration)) {
/* 2126 */             param1ArrayOfValueType[b] = null;
/* 2127 */             i--;
/*      */           }
/*      */         
/*      */         } 
/* 2131 */       } catch (ClassNotFound classNotFound) {
/* 2132 */         Type.classNotFound(CompoundType.this.stack, classNotFound);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2137 */       if (i < param1ArrayOfValueType.length) {
/* 2138 */         ValueType[] arrayOfValueType1 = new ValueType[i];
/* 2139 */         byte b1 = 0;
/* 2140 */         for (byte b2 = 0; b2 < param1ArrayOfValueType.length; b2++) {
/* 2141 */           if (param1ArrayOfValueType[b2] != null) {
/* 2142 */             arrayOfValueType1[b1++] = param1ArrayOfValueType[b2];
/*      */           }
/*      */         } 
/* 2145 */         param1ArrayOfValueType = arrayOfValueType1;
/*      */       } 
/*      */       
/* 2148 */       return param1ArrayOfValueType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2156 */       if (this.stringRep == null) {
/*      */         
/* 2158 */         StringBuffer stringBuffer = new StringBuffer(this.returnType.toString());
/*      */ 
/*      */ 
/*      */         
/* 2162 */         stringBuffer.append(" ");
/* 2163 */         stringBuffer.append(getName());
/* 2164 */         stringBuffer.append(" (");
/*      */         
/*      */         byte b;
/*      */         
/* 2168 */         for (b = 0; b < this.arguments.length; b++) {
/* 2169 */           if (b > 0) {
/* 2170 */             stringBuffer.append(", ");
/*      */           }
/* 2172 */           stringBuffer.append(this.arguments[b]);
/* 2173 */           stringBuffer.append(" ");
/* 2174 */           stringBuffer.append(this.argumentNames[b]);
/*      */         } 
/*      */         
/* 2177 */         stringBuffer.append(")");
/*      */ 
/*      */ 
/*      */         
/* 2181 */         for (b = 0; b < this.exceptions.length; b++) {
/* 2182 */           if (b == 0) {
/* 2183 */             stringBuffer.append(" throws ");
/*      */           } else {
/* 2185 */             stringBuffer.append(", ");
/*      */           } 
/* 2187 */           stringBuffer.append(this.exceptions[b]);
/*      */         } 
/*      */         
/* 2190 */         stringBuffer.append(";");
/*      */         
/* 2192 */         this.stringRep = stringBuffer.toString();
/*      */       } 
/*      */       
/* 2195 */       return this.stringRep;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttributeKind(int param1Int) {
/* 2203 */       this.attributeKind = param1Int;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttributePairIndex(int param1Int) {
/* 2210 */       this.attributePairIndex = param1Int;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttributeName(String param1String) {
/* 2217 */       this.attributeName = param1String;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setIDLName(String param1String) {
/* 2224 */       this.idlName = param1String;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setImplExceptions(ValueType[] param1ArrayOfValueType) {
/* 2231 */       this.implExceptions = param1ArrayOfValueType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setDeclaredBy(Identifier param1Identifier) {
/* 2238 */       this.declaredBy = param1Identifier;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void swapInvalidTypes() {
/* 2248 */       if (this.returnType.getStatus() != 1) {
/* 2249 */         this.returnType = CompoundType.this.getValidType(this.returnType);
/*      */       }
/*      */       
/*      */       byte b;
/*      */       
/* 2254 */       for (b = 0; b < this.arguments.length; b++) {
/* 2255 */         if (this.arguments[b].getStatus() != 1) {
/* 2256 */           this.arguments[b] = CompoundType.this.getValidType(this.arguments[b]);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2262 */       for (b = 0; b < this.exceptions.length; b++) {
/* 2263 */         if (this.exceptions[b].getStatus() != 1) {
/* 2264 */           this.exceptions[b] = (ValueType)CompoundType.this.getValidType(this.exceptions[b]);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2270 */       for (b = 0; b < this.implExceptions.length; b++) {
/* 2271 */         if (this.implExceptions[b].getStatus() != 1) {
/* 2272 */           this.implExceptions[b] = (ValueType)CompoundType.this.getValidType(this.implExceptions[b]);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void destroy() {
/* 2281 */       if (this.memberDef != null) {
/* 2282 */         this.memberDef = null;
/* 2283 */         this.enclosing = null;
/* 2284 */         if (this.exceptions != null) {
/* 2285 */           for (byte b = 0; b < this.exceptions.length; b++) {
/* 2286 */             if (this.exceptions[b] != null) this.exceptions[b].destroy(); 
/* 2287 */             this.exceptions[b] = null;
/*      */           } 
/* 2289 */           this.exceptions = null;
/*      */         } 
/*      */         
/* 2292 */         if (this.implExceptions != null) {
/* 2293 */           for (byte b = 0; b < this.implExceptions.length; b++) {
/* 2294 */             if (this.implExceptions[b] != null) this.implExceptions[b].destroy(); 
/* 2295 */             this.implExceptions[b] = null;
/*      */           } 
/* 2297 */           this.implExceptions = null;
/*      */         } 
/*      */         
/* 2300 */         if (this.returnType != null) this.returnType.destroy(); 
/* 2301 */         this.returnType = null;
/*      */         
/* 2303 */         if (this.arguments != null) {
/* 2304 */           for (byte b = 0; b < this.arguments.length; b++) {
/* 2305 */             if (this.arguments[b] != null) this.arguments[b].destroy(); 
/* 2306 */             this.arguments[b] = null;
/*      */           } 
/* 2308 */           this.arguments = null;
/*      */         } 
/*      */         
/* 2311 */         if (this.argumentNames != null) {
/* 2312 */           for (byte b = 0; b < this.argumentNames.length; b++) {
/* 2313 */             this.argumentNames[b] = null;
/*      */           }
/* 2315 */           this.argumentNames = null;
/*      */         } 
/*      */         
/* 2318 */         this.vis = null;
/* 2319 */         this.name = null;
/* 2320 */         this.idlName = null;
/* 2321 */         this.stringRep = null;
/* 2322 */         this.attributeName = null;
/* 2323 */         this.declaredBy = null;
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
/*      */ 
/*      */     
/* 2337 */     private String stringRep = null;
/* 2338 */     private int attributeKind = 0;
/* 2339 */     private String attributeName = null;
/* 2340 */     private int attributePairIndex = -1;
/* 2341 */     private Identifier declaredBy = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String makeArgName(int param1Int, Type param1Type) {
/* 2347 */       return "arg" + param1Int;
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
/*      */     public Method(CompoundType param1CompoundType1, MemberDefinition param1MemberDefinition, boolean param1Boolean, ContextStack param1ContextStack) throws Exception {
/* 2359 */       this.enclosing = param1CompoundType1;
/* 2360 */       this.memberDef = param1MemberDefinition;
/* 2361 */       this.vis = CompoundType.getVisibilityString(param1MemberDefinition);
/* 2362 */       this.idlName = null;
/* 2363 */       boolean bool = true;
/* 2364 */       this.declaredBy = param1MemberDefinition.getClassDeclaration().getName();
/*      */ 
/*      */ 
/*      */       
/* 2368 */       this.name = param1MemberDefinition.getName().toString();
/*      */ 
/*      */ 
/*      */       
/* 2372 */       param1ContextStack.setNewContextCode(2);
/* 2373 */       param1ContextStack.push(this);
/*      */ 
/*      */ 
/*      */       
/* 2377 */       param1ContextStack.setNewContextCode(3);
/* 2378 */       Type type1 = param1MemberDefinition.getType();
/* 2379 */       Type type2 = type1.getReturnType();
/*      */       
/* 2381 */       if (type2 == Type.tVoid) {
/* 2382 */         this.returnType = PrimitiveType.forPrimitive(type2, param1ContextStack);
/*      */       } else {
/* 2384 */         this.returnType = CompoundType.makeType(type2, (ClassDefinition)null, param1ContextStack);
/* 2385 */         if (this.returnType == null || 
/* 2386 */           !CompoundType.this.assertNotImpl(this.returnType, param1Boolean, param1ContextStack, param1CompoundType1, false)) {
/* 2387 */           bool = false;
/* 2388 */           Type.failedConstraint(24, param1Boolean, param1ContextStack, param1CompoundType1.getName());
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2394 */       param1ContextStack.setNewContextCode(4);
/* 2395 */       Type[] arrayOfType = param1MemberDefinition.getType().getArgumentTypes();
/* 2396 */       this.arguments = new Type[arrayOfType.length];
/* 2397 */       this.argumentNames = new String[arrayOfType.length];
/* 2398 */       Vector<LocalMember> vector = param1MemberDefinition.getArguments();
/*      */       
/* 2400 */       for (byte b = 0; b < arrayOfType.length; b++) {
/* 2401 */         Type type = null;
/*      */         try {
/* 2403 */           type = CompoundType.makeType(arrayOfType[b], (ClassDefinition)null, param1ContextStack);
/* 2404 */         } catch (Exception exception) {}
/*      */ 
/*      */         
/* 2407 */         if (type != null) {
/* 2408 */           if (!CompoundType.this.assertNotImpl(type, param1Boolean, param1ContextStack, param1CompoundType1, false)) {
/* 2409 */             bool = false;
/*      */           } else {
/* 2411 */             this.arguments[b] = type;
/* 2412 */             if (vector != null) {
/* 2413 */               LocalMember localMember = vector.elementAt(b + 1);
/* 2414 */               this.argumentNames[b] = localMember.getName().toString();
/*      */             } else {
/* 2416 */               this.argumentNames[b] = makeArgName(b, type);
/*      */             } 
/*      */           } 
/*      */         } else {
/* 2420 */           bool = false;
/* 2421 */           Type.failedConstraint(25, false, param1ContextStack, param1CompoundType1.getQualifiedName(), this.name);
/*      */         } 
/*      */       } 
/*      */       
/* 2425 */       if (!bool) {
/* 2426 */         param1ContextStack.pop(false);
/* 2427 */         throw new Exception();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 2433 */         this.exceptions = param1CompoundType1.getMethodExceptions(param1MemberDefinition, param1Boolean, param1ContextStack);
/* 2434 */         this.implExceptions = this.exceptions;
/* 2435 */         param1ContextStack.pop(true);
/* 2436 */       } catch (Exception exception) {
/* 2437 */         param1ContextStack.pop(false);
/* 2438 */         throw new Exception();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Object clone() {
/*      */       try {
/* 2447 */         return super.clone();
/* 2448 */       } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 2449 */         throw new Error("clone failed");
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public class Member
/*      */     implements ContextElement, Cloneable
/*      */   {
/*      */     private Type type;
/*      */     private String vis;
/*      */     private String value;
/*      */     private String name;
/*      */     private String idlName;
/*      */     private boolean innerClassDecl;
/*      */     private boolean constant;
/*      */     private MemberDefinition member;
/*      */     private boolean forceTransient;
/*      */     
/*      */     public String getElementName() {
/* 2468 */       return "\"" + getName() + "\"";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Type getType() {
/* 2475 */       return this.type;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/* 2482 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getIDLName() {
/* 2490 */       return this.idlName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getVisibility() {
/* 2497 */       return this.vis;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isPublic() {
/* 2504 */       return this.member.isPublic();
/*      */     }
/*      */     
/*      */     public boolean isPrivate() {
/* 2508 */       return this.member.isPrivate();
/*      */     }
/*      */     
/*      */     public boolean isStatic() {
/* 2512 */       return this.member.isStatic();
/*      */     }
/*      */     
/*      */     public boolean isFinal() {
/* 2516 */       return this.member.isFinal();
/*      */     }
/*      */     
/*      */     public boolean isTransient() {
/* 2520 */       if (this.forceTransient) return true; 
/* 2521 */       return this.member.isTransient();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getValue() {
/* 2528 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isInnerClassDeclaration() {
/* 2536 */       return this.innerClassDecl;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isConstant() {
/* 2543 */       return this.constant;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2551 */       String str = this.type.toString();
/*      */       
/* 2553 */       if (this.value != null) {
/* 2554 */         str = str + " = " + this.value;
/*      */       }
/*      */       
/* 2557 */       return str;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void swapInvalidTypes() {
/* 2564 */       if (this.type.getStatus() != 1) {
/* 2565 */         this.type = CompoundType.this.getValidType(this.type);
/*      */       }
/*      */     }
/*      */     
/*      */     protected void setTransient() {
/* 2570 */       if (!isTransient()) {
/* 2571 */         this.forceTransient = true;
/* 2572 */         if (this.vis.length() > 0) {
/* 2573 */           this.vis += " transient";
/*      */         } else {
/* 2575 */           this.vis = "transient";
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     protected MemberDefinition getMemberDefinition() {
/* 2581 */       return this.member;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void destroy() {
/* 2588 */       if (this.type != null) {
/* 2589 */         this.type.destroy();
/* 2590 */         this.type = null;
/* 2591 */         this.vis = null;
/* 2592 */         this.value = null;
/* 2593 */         this.name = null;
/* 2594 */         this.idlName = null;
/* 2595 */         this.member = null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Member(MemberDefinition param1MemberDefinition, String param1String, ContextStack param1ContextStack, CompoundType param1CompoundType1) {
/* 2616 */       this.member = param1MemberDefinition;
/* 2617 */       this.value = param1String;
/* 2618 */       this.forceTransient = false;
/* 2619 */       this.innerClassDecl = (param1MemberDefinition.getInnerClass() != null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2625 */       if (!this.innerClassDecl) {
/* 2626 */         init(param1ContextStack, param1CompoundType1);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void init(ContextStack param1ContextStack, CompoundType param1CompoundType) {
/* 2632 */       this.constant = false;
/* 2633 */       this.name = this.member.getName().toString();
/* 2634 */       this.vis = CompoundType.getVisibilityString(this.member);
/* 2635 */       this.idlName = null;
/*      */ 
/*      */ 
/*      */       
/* 2639 */       byte b = 6;
/* 2640 */       param1ContextStack.setNewContextCode(b);
/*      */ 
/*      */ 
/*      */       
/* 2644 */       if (this.member.isVariable()) {
/* 2645 */         if (this.value != null && this.member.isConstant()) {
/* 2646 */           b = 7;
/* 2647 */           this.constant = true;
/* 2648 */         } else if (this.member.isStatic()) {
/* 2649 */           b = 8;
/* 2650 */         } else if (this.member.isTransient()) {
/* 2651 */           b = 9;
/*      */         } 
/*      */       }
/*      */       
/* 2655 */       param1ContextStack.setNewContextCode(b);
/* 2656 */       param1ContextStack.push(this);
/*      */       
/* 2658 */       this.type = CompoundType.makeType(this.member.getType(), (ClassDefinition)null, param1ContextStack);
/*      */       
/* 2660 */       if (this.type == null || (!this.innerClassDecl && 
/*      */         
/* 2662 */         !this.member.isStatic() && 
/* 2663 */         !this.member.isTransient() && 
/* 2664 */         !CompoundType.this.assertNotImpl(this.type, false, param1ContextStack, param1CompoundType, true))) {
/* 2665 */         param1ContextStack.pop(false);
/* 2666 */         throw new CompilerError("");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2671 */       if (this.constant && this.type.isPrimitive()) {
/* 2672 */         if (this.type.isType(64) || this.type.isType(128) || this.type.isType(256)) {
/* 2673 */           int i = this.value.length();
/* 2674 */           char c = this.value.charAt(i - 1);
/* 2675 */           if (!Character.isDigit(c)) {
/* 2676 */             this.value = this.value.substring(0, i - 1);
/*      */           }
/* 2678 */         } else if (this.type.isType(2)) {
/* 2679 */           this.value = this.value.toUpperCase();
/*      */         } 
/*      */       }
/* 2682 */       if (this.constant && this.type.isType(512)) {
/* 2683 */         this.value = "L" + this.value;
/*      */       }
/* 2685 */       param1ContextStack.pop(true);
/*      */     }
/*      */     
/*      */     public void setIDLName(String param1String) {
/* 2689 */       this.idlName = param1String;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Object clone() {
/*      */       try {
/* 2697 */         return super.clone();
/* 2698 */       } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 2699 */         throw new Error("clone failed");
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\CompoundType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */