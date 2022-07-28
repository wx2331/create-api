/*      */ package sun.rmi.rmic.iiop;
/*      */ 
/*      */ import com.sun.corba.se.impl.util.RepositoryId;
/*      */ import java.io.IOException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Vector;
/*      */ import sun.rmi.rmic.IndentingWriter;
/*      */ import sun.rmi.rmic.Names;
/*      */ import sun.tools.java.ClassNotFound;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Type
/*      */   implements Constants, ContextElement, Cloneable
/*      */ {
/*      */   private int typeCode;
/*      */   private int fullTypeCode;
/*      */   private Identifier id;
/*      */   private String name;
/*      */   private String packageName;
/*      */   private String qualifiedName;
/*      */   private String idlName;
/*      */   private String[] idlModuleNames;
/*      */   private String qualifiedIDLName;
/*      */   private String repositoryID;
/*      */   private Class ourClass;
/*  140 */   private int status = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   protected BatchEnvironment env;
/*      */ 
/*      */ 
/*      */   
/*      */   protected ContextStack stack;
/*      */ 
/*      */   
/*      */   protected boolean destroyed = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  156 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPackageName() {
/*  165 */     return this.packageName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getQualifiedName() {
/*  173 */     return this.qualifiedName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String getSignature();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getIDLName() {
/*  188 */     return this.idlName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getIDLModuleNames() {
/*  198 */     return this.idlModuleNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getQualifiedIDLName(boolean paramBoolean) {
/*  208 */     if (paramBoolean && (getIDLModuleNames()).length > 0) {
/*  209 */       return "::" + this.qualifiedIDLName;
/*      */     }
/*  211 */     return this.qualifiedIDLName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Identifier getIdentifier() {
/*  219 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getRepositoryID() {
/*  226 */     return this.repositoryID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getBoxedRepositoryID() {
/*  233 */     return RepositoryId.createForJavaType(this.ourClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class getClassInstance() {
/*  240 */     if (this.ourClass == null) {
/*  241 */       initClass();
/*      */     }
/*  243 */     return this.ourClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getStatus() {
/*  250 */     return this.status;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStatus(int paramInt) {
/*  257 */     this.status = paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BatchEnvironment getEnv() {
/*  264 */     return this.env;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTypeCode() {
/*  271 */     return this.typeCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFullTypeCode() {
/*  278 */     return this.fullTypeCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTypeCodeModifiers() {
/*  285 */     return this.fullTypeCode & 0xFF000000;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isType(int paramInt) {
/*  294 */     return ((this.fullTypeCode & paramInt) == paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean typeMatches(int paramInt) {
/*  302 */     return ((this.fullTypeCode & paramInt) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRootTypeCode() {
/*  311 */     if (isArray()) {
/*  312 */       return getElementType().getFullTypeCode();
/*      */     }
/*  314 */     return this.fullTypeCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInterface() {
/*  322 */     return ((this.fullTypeCode & 0x8000000) == 134217728);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isClass() {
/*  329 */     return ((this.fullTypeCode & 0x4000000) == 67108864);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInner() {
/*  336 */     return ((this.fullTypeCode & Integer.MIN_VALUE) == Integer.MIN_VALUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSpecialInterface() {
/*  344 */     return ((this.fullTypeCode & 0x20000000) == 536870912);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSpecialClass() {
/*  351 */     return ((this.fullTypeCode & 0x10000000) == 268435456);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCompound() {
/*  358 */     return ((this.fullTypeCode & 0x2000000) == 33554432);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrimitive() {
/*  365 */     return ((this.fullTypeCode & 0x1000000) == 16777216);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isArray() {
/*  372 */     return ((this.fullTypeCode & 0x40000) == 262144);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isConforming() {
/*  379 */     return ((this.fullTypeCode & 0x40000000) == 1073741824);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  386 */     return getQualifiedName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Type getElementType() {
/*  393 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getArrayDimension() {
/*  400 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getArrayBrackets() {
/*  407 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object paramObject) {
/*  415 */     String str1 = toString();
/*  416 */     String str2 = ((Type)paramObject).toString();
/*  417 */     return str1.equals(str2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Type[] collectMatching(int paramInt) {
/*  426 */     return collectMatching(paramInt, new HashSet(this.env.allTypes.size()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Type[] collectMatching(int paramInt, HashSet paramHashSet) {
/*  437 */     Vector vector = new Vector();
/*      */ 
/*      */ 
/*      */     
/*  441 */     addTypes(paramInt, paramHashSet, vector);
/*      */ 
/*      */ 
/*      */     
/*  445 */     Type[] arrayOfType = new Type[vector.size()];
/*  446 */     vector.copyInto((Object[])arrayOfType);
/*      */     
/*  448 */     return arrayOfType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String getTypeDescription();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTypeName(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/*  465 */     if (paramBoolean2) {
/*  466 */       if (paramBoolean1) {
/*  467 */         return getQualifiedIDLName(paramBoolean3);
/*      */       }
/*  469 */       return getIDLName();
/*      */     } 
/*      */     
/*  472 */     if (paramBoolean1) {
/*  473 */       return getQualifiedName();
/*      */     }
/*  475 */     return getName();
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
/*      */   public void print(IndentingWriter paramIndentingWriter, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  495 */     Type[] arrayOfType = collectMatching(paramInt);
/*  496 */     print(paramIndentingWriter, arrayOfType, paramBoolean1, paramBoolean2, paramBoolean3);
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
/*      */   public static void print(IndentingWriter paramIndentingWriter, Type[] paramArrayOfType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  513 */     for (byte b = 0; b < paramArrayOfType.length; b++) {
/*  514 */       paramArrayOfType[b].println(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
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
/*      */   public void print(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  530 */     printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
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
/*      */   public void println(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  545 */     print(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  546 */     paramIndentingWriter.pln();
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
/*      */   public void printTypeName(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*  563 */     paramIndentingWriter.p(getTypeName(paramBoolean1, paramBoolean2, paramBoolean3));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getElementName() {
/*  570 */     return getQualifiedName();
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
/*      */   protected void printPackageOpen(IndentingWriter paramIndentingWriter, boolean paramBoolean) throws IOException {
/*  585 */     if (paramBoolean) {
/*  586 */       String[] arrayOfString = getIDLModuleNames();
/*  587 */       for (byte b = 0; b < arrayOfString.length; b++) {
/*  588 */         paramIndentingWriter.plnI("module " + arrayOfString[b] + " {");
/*      */       }
/*      */     } else {
/*  591 */       String str = getPackageName();
/*  592 */       if (str != null) {
/*  593 */         paramIndentingWriter.pln("package " + str + ";");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static Type getType(sun.tools.java.Type paramType, ContextStack paramContextStack) {
/*  602 */     return getType(paramType.toString(), paramContextStack);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static Type getType(String paramString, ContextStack paramContextStack) {
/*  609 */     Type type = (Type)(paramContextStack.getEnv()).allTypes.get(paramString);
/*      */     
/*  611 */     if (type != null) {
/*  612 */       paramContextStack.traceExistingType(type);
/*      */     }
/*      */     
/*  615 */     return type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void removeType(String paramString, ContextStack paramContextStack) {
/*  622 */     Type type = (Type)(paramContextStack.getEnv()).allTypes.remove(paramString);
/*  623 */     (paramContextStack.getEnv()).invalidTypes.put(type, paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void removeType(sun.tools.java.Type paramType, ContextStack paramContextStack) {
/*  630 */     String str = paramType.toString();
/*  631 */     Type type = (Type)(paramContextStack.getEnv()).allTypes.remove(str);
/*  632 */     putInvalidType(type, str, paramContextStack);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void putType(sun.tools.java.Type paramType, Type paramType1, ContextStack paramContextStack) {
/*  639 */     (paramContextStack.getEnv()).allTypes.put(paramType.toString(), paramType1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void putType(String paramString, Type paramType, ContextStack paramContextStack) {
/*  646 */     (paramContextStack.getEnv()).allTypes.put(paramString, paramType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void putInvalidType(Type paramType, String paramString, ContextStack paramContextStack) {
/*  653 */     (paramContextStack.getEnv()).invalidTypes.put(paramType, paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeInvalidTypes() {
/*  661 */     if (this.env.invalidTypes.size() > 0) {
/*  662 */       this.env.invalidTypes.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void updateAllInvalidTypes(ContextStack paramContextStack) {
/*  670 */     BatchEnvironment batchEnvironment = paramContextStack.getEnv();
/*  671 */     if (batchEnvironment.invalidTypes.size() > 0) {
/*      */ 
/*      */ 
/*      */       
/*  675 */       for (Enumeration<Type> enumeration = batchEnvironment.allTypes.elements(); enumeration.hasMoreElements(); ) {
/*  676 */         Type type = enumeration.nextElement();
/*  677 */         type.swapInvalidTypes();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  682 */       batchEnvironment.invalidTypes.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int countTypes() {
/*  690 */     return this.env.allTypes.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void resetTypes() {
/*  697 */     this.env.reset();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void destroy() {
/*  704 */     if (!this.destroyed) {
/*  705 */       this.id = null;
/*  706 */       this.name = null;
/*  707 */       this.packageName = null;
/*  708 */       this.qualifiedName = null;
/*  709 */       this.idlName = null;
/*  710 */       this.idlModuleNames = null;
/*  711 */       this.qualifiedIDLName = null;
/*  712 */       this.repositoryID = null;
/*  713 */       this.ourClass = null;
/*  714 */       this.env = null;
/*  715 */       this.stack = null;
/*  716 */       this.destroyed = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void swapInvalidTypes() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Type getValidType(Type paramType) {
/*  730 */     if (paramType.getStatus() == 1) {
/*  731 */       return paramType;
/*      */     }
/*      */     
/*  734 */     String str = (String)this.env.invalidTypes.get(paramType);
/*  735 */     Type type = null;
/*  736 */     if (str != null) {
/*  737 */       type = (Type)this.env.allTypes.get(str);
/*      */     }
/*      */     
/*  740 */     if (type == null) {
/*  741 */       throw new Error("Failed to find valid type to swap for " + paramType + " mis-identified as " + paramType.getTypeDescription());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  746 */     return type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void printPackageClose(IndentingWriter paramIndentingWriter, boolean paramBoolean) throws IOException {
/*  756 */     if (paramBoolean) {
/*  757 */       String[] arrayOfString = getIDLModuleNames();
/*  758 */       for (byte b = 0; b < arrayOfString.length; b++) {
/*  759 */         paramIndentingWriter.pOln("};");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Type(ContextStack paramContextStack, int paramInt) {
/*  769 */     this.env = paramContextStack.getEnv();
/*  770 */     this.stack = paramContextStack;
/*  771 */     this.fullTypeCode = paramInt;
/*  772 */     this.typeCode = paramInt & 0xFFFFFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setTypeCode(int paramInt) {
/*  779 */     this.fullTypeCode = paramInt;
/*  780 */     this.typeCode = paramInt & 0xFFFFFF;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setNames(Identifier paramIdentifier, String[] paramArrayOfString, String paramString) {
/*  788 */     this.id = paramIdentifier;
/*  789 */     this.name = Names.mangleClass(paramIdentifier).getName().toString();
/*  790 */     this.packageName = null;
/*      */     
/*  792 */     if (paramIdentifier.isQualified()) {
/*  793 */       this.packageName = paramIdentifier.getQualifier().toString();
/*  794 */       this.qualifiedName = this.packageName + "." + this.name;
/*      */     } else {
/*  796 */       this.qualifiedName = this.name;
/*      */     } 
/*      */     
/*  799 */     setIDLNames(paramArrayOfString, paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setIDLNames(String[] paramArrayOfString, String paramString) {
/*  807 */     this.idlName = paramString;
/*      */     
/*  809 */     if (paramArrayOfString != null) {
/*  810 */       this.idlModuleNames = paramArrayOfString;
/*      */     } else {
/*  812 */       this.idlModuleNames = new String[0];
/*      */     } 
/*  814 */     this.qualifiedIDLName = IDLNames.getQualifiedName(paramArrayOfString, paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void classNotFound(ContextStack paramContextStack, ClassNotFound paramClassNotFound) {
/*  822 */     classNotFound(false, paramContextStack, paramClassNotFound);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void classNotFound(boolean paramBoolean, ContextStack paramContextStack, ClassNotFound paramClassNotFound) {
/*  831 */     if (!paramBoolean) paramContextStack.getEnv().error(0L, "rmic.class.not.found", paramClassNotFound.name); 
/*  832 */     paramContextStack.traceCallStack();
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
/*      */   protected static boolean failedConstraint(int paramInt, boolean paramBoolean, ContextStack paramContextStack, Object paramObject1, Object paramObject2, Object paramObject3) {
/*  851 */     String str = "rmic.iiop.constraint." + paramInt;
/*      */     
/*  853 */     if (!paramBoolean) {
/*  854 */       paramContextStack.getEnv().error(0L, str, (paramObject1 != null) ? paramObject1
/*  855 */           .toString() : null, (paramObject2 != null) ? paramObject2
/*  856 */           .toString() : null, (paramObject3 != null) ? paramObject3
/*  857 */           .toString() : null);
/*      */     } else {
/*  859 */       String str1 = paramContextStack.getEnv().errorString(str, paramObject1, paramObject2, paramObject3);
/*  860 */       paramContextStack.traceln(str1);
/*      */     } 
/*      */     
/*  863 */     return false;
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
/*      */   protected static boolean failedConstraint(int paramInt, boolean paramBoolean, ContextStack paramContextStack, Object paramObject1, Object paramObject2) {
/*  881 */     return failedConstraint(paramInt, paramBoolean, paramContextStack, paramObject1, paramObject2, null);
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
/*      */   protected static boolean failedConstraint(int paramInt, boolean paramBoolean, ContextStack paramContextStack, Object paramObject) {
/*  899 */     return failedConstraint(paramInt, paramBoolean, paramContextStack, paramObject, null, null);
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
/*      */   protected static boolean failedConstraint(int paramInt, boolean paramBoolean, ContextStack paramContextStack) {
/*  914 */     return failedConstraint(paramInt, paramBoolean, paramContextStack, null, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object clone() {
/*      */     try {
/*  922 */       return super.clone();
/*  923 */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/*  924 */       throw new Error("clone failed");
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
/*      */   protected boolean addTypes(int paramInt, HashSet<Type> paramHashSet, Vector<Type> paramVector) {
/*      */     boolean bool;
/*  940 */     if (paramHashSet.contains(this)) {
/*      */ 
/*      */ 
/*      */       
/*  944 */       bool = false;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  950 */       paramHashSet.add(this);
/*      */ 
/*      */ 
/*      */       
/*  954 */       if (typeMatches(paramInt))
/*      */       {
/*      */ 
/*      */         
/*  958 */         paramVector.addElement(this);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  963 */       bool = true;
/*      */     } 
/*      */     
/*  966 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract Class loadClass();
/*      */ 
/*      */   
/*      */   private boolean initClass() {
/*  975 */     if (this.ourClass == null) {
/*  976 */       this.ourClass = loadClass();
/*  977 */       if (this.ourClass == null) {
/*  978 */         failedConstraint(27, false, this.stack, getQualifiedName());
/*  979 */         return false;
/*      */       } 
/*      */     } 
/*  982 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean setRepositoryID() {
/*  993 */     if (!initClass()) {
/*  994 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  999 */     this.repositoryID = RepositoryId.createForAnyType(this.ourClass);
/* 1000 */     return true;
/*      */   }
/*      */   
/*      */   private Type() {}
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */