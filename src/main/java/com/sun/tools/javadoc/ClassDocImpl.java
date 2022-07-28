/*      */ package com.sun.tools.javadoc;
/*      */
/*      */ import com.sun.javadoc.AnnotatedType;
/*      */ import com.sun.javadoc.AnnotationTypeDoc;
/*      */ import com.sun.javadoc.ClassDoc;
/*      */ import com.sun.javadoc.ConstructorDoc;
/*      */ import com.sun.javadoc.FieldDoc;
/*      */ import com.sun.javadoc.MethodDoc;
/*      */ import com.sun.javadoc.PackageDoc;
/*      */ import com.sun.javadoc.ParamTag;
/*      */ import com.sun.javadoc.ParameterizedType;
/*      */ import com.sun.javadoc.SourcePosition;
/*      */ import com.sun.javadoc.Type;
/*      */ import com.sun.javadoc.TypeVariable;
/*      */ import com.sun.javadoc.WildcardType;
/*      */ import com.sun.source.util.TreePath;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.net.URI;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import javax.tools.FileObject;
/*      */ import javax.tools.StandardJavaFileManager;
/*      */ import javax.tools.StandardLocation;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class ClassDocImpl
/*      */   extends ProgramElementDocImpl
/*      */   implements ClassDoc
/*      */ {
/*      */   public final Type.ClassType type;
/*      */   protected final Symbol.ClassSymbol tsym;
/*      */   boolean isIncluded = false;
/*      */   private SerializedForm serializedForm;
/*      */   private String name;
/*      */   private String qualifiedName;
/*      */   private String simpleTypeName;
/*      */
/*      */   public ClassDocImpl(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol) {
/*  100 */     this(paramDocEnv, paramClassSymbol, (TreePath)null);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public ClassDocImpl(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, TreePath paramTreePath) {
/*  107 */     super(paramDocEnv, (Symbol)paramClassSymbol, paramTreePath);
/*  108 */     this.type = (Type.ClassType)paramClassSymbol.type;
/*  109 */     this.tsym = paramClassSymbol;
/*      */   }
/*      */
/*      */   public Type getElementType() {
/*  113 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   protected long getFlags() {
/*  120 */     return getFlags(this.tsym);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   static long getFlags(Symbol.ClassSymbol paramClassSymbol) {
/*      */     try {
/*  128 */       return paramClassSymbol.flags();
/*  129 */     } catch (Symbol.CompletionFailure completionFailure) {
/*      */
/*      */
/*      */
/*      */
/*  134 */       return getFlags(paramClassSymbol);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   static boolean isAnnotationType(Symbol.ClassSymbol paramClassSymbol) {
/*  142 */     return ((getFlags(paramClassSymbol) & 0x2000L) != 0L);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   protected Symbol.ClassSymbol getContainingClass() {
/*  149 */     return this.tsym.owner.enclClass();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isClass() {
/*  157 */     return !Modifier.isInterface(getModifiers());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isOrdinaryClass() {
/*  166 */     if (isEnum() || isInterface() || isAnnotationType()) {
/*  167 */       return false;
/*      */     }
/*  169 */     for (Type.ClassType classType = this.type; classType.hasTag(TypeTag.CLASS); type = this.env.types.supertype((Type)classType)) {
/*  170 */       Type type; if (((Type)classType).tsym == this.env.syms.errorType.tsym || ((Type)classType).tsym == this.env.syms.exceptionType.tsym)
/*      */       {
/*  172 */         return false;
/*      */       }
/*      */     }
/*  175 */     return true;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isEnum() {
/*  184 */     return ((getFlags() & 0x4000L) != 0L && !this.env.legacyDoclet);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isInterface() {
/*  195 */     return Modifier.isInterface(getModifiers());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isException() {
/*  203 */     if (isEnum() || isInterface() || isAnnotationType()) {
/*  204 */       return false;
/*      */     }
/*  206 */     for (Type.ClassType classType = this.type; classType.hasTag(TypeTag.CLASS); type = this.env.types.supertype((Type)classType)) {
/*  207 */       Type type; if (((Type)classType).tsym == this.env.syms.exceptionType.tsym) {
/*  208 */         return true;
/*      */       }
/*      */     }
/*  211 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isError() {
/*  219 */     if (isEnum() || isInterface() || isAnnotationType()) {
/*  220 */       return false;
/*      */     }
/*  222 */     for (Type.ClassType classType = this.type; classType.hasTag(TypeTag.CLASS); type = this.env.types.supertype((Type)classType)) {
/*  223 */       Type type; if (((Type)classType).tsym == this.env.syms.errorType.tsym) {
/*  224 */         return true;
/*      */       }
/*      */     }
/*  227 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isThrowable() {
/*  234 */     if (isEnum() || isInterface() || isAnnotationType()) {
/*  235 */       return false;
/*      */     }
/*  237 */     for (Type.ClassType classType = this.type; classType.hasTag(TypeTag.CLASS); type = this.env.types.supertype((Type)classType)) {
/*  238 */       Type type; if (((Type)classType).tsym == this.env.syms.throwableType.tsym) {
/*  239 */         return true;
/*      */       }
/*      */     }
/*  242 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isAbstract() {
/*  249 */     return Modifier.isAbstract(getModifiers());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isSynthetic() {
/*  256 */     return ((getFlags() & 0x1000L) != 0L);
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
/*      */   public boolean isIncluded() {
/*  268 */     if (this.isIncluded) {
/*  269 */       return true;
/*      */     }
/*  271 */     if (this.env.shouldDocument(this.tsym)) {
/*      */
/*      */
/*      */
/*  275 */       if (containingPackage().isIncluded()) {
/*  276 */         return this.isIncluded = true;
/*      */       }
/*  278 */       ClassDoc classDoc = containingClass();
/*  279 */       if (classDoc != null && classDoc.isIncluded()) {
/*  280 */         return this.isIncluded = true;
/*      */       }
/*      */     }
/*  283 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public PackageDoc containingPackage() {
/*  291 */     PackageDocImpl packageDocImpl = this.env.getPackageDoc(this.tsym.packge());
/*  292 */     if (!packageDocImpl.setDocPath) {
/*      */       FileObject fileObject;
/*      */       try {
/*  295 */         StandardLocation standardLocation = this.env.fileManager.hasLocation(StandardLocation.SOURCE_PATH) ? StandardLocation.SOURCE_PATH : StandardLocation.CLASS_PATH;
/*      */
/*      */
/*  298 */         fileObject = this.env.fileManager.getFileForInput(standardLocation, packageDocImpl
/*  299 */             .qualifiedName(), "package.html");
/*  300 */       } catch (IOException iOException) {
/*  301 */         fileObject = null;
/*      */       }
/*      */
/*  304 */       if (fileObject == null) {
/*      */
/*      */
/*  307 */         SourcePosition sourcePosition = position();
/*  308 */         if (this.env.fileManager instanceof StandardJavaFileManager && sourcePosition instanceof SourcePositionImpl) {
/*      */
/*  310 */           URI uRI = ((SourcePositionImpl)sourcePosition).filename.toUri();
/*  311 */           if ("file".equals(uRI.getScheme())) {
/*  312 */             File file1 = new File(uRI);
/*  313 */             File file2 = file1.getParentFile();
/*  314 */             if (file2 != null) {
/*  315 */               File file = new File(file2, "package.html");
/*  316 */               if (file.exists()) {
/*  317 */                 StandardJavaFileManager standardJavaFileManager = (StandardJavaFileManager)this.env.fileManager;
/*  318 */                 fileObject = standardJavaFileManager.getJavaFileObjects(new File[] { file }).iterator().next();
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*  326 */       packageDocImpl.setDocPath(fileObject);
/*      */     }
/*  328 */     return packageDocImpl;
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
/*      */   public String name() {
/*  343 */     if (this.name == null) {
/*  344 */       this.name = getClassName(this.tsym, false);
/*      */     }
/*  346 */     return this.name;
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
/*      */   public String qualifiedName() {
/*  361 */     if (this.qualifiedName == null) {
/*  362 */       this.qualifiedName = getClassName(this.tsym, true);
/*      */     }
/*  364 */     return this.qualifiedName;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public String typeName() {
/*  375 */     return name();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public String qualifiedTypeName() {
/*  385 */     return qualifiedName();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public String simpleTypeName() {
/*  392 */     if (this.simpleTypeName == null) {
/*  393 */       this.simpleTypeName = this.tsym.name.toString();
/*      */     }
/*  395 */     return this.simpleTypeName;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public String toString() {
/*  406 */     return classToString(this.env, this.tsym, true);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   static String getClassName(Symbol.ClassSymbol paramClassSymbol, boolean paramBoolean) {
/*  414 */     if (paramBoolean) {
/*  415 */       return paramClassSymbol.getQualifiedName().toString();
/*      */     }
/*  417 */     String str = "";
/*  418 */     for (; paramClassSymbol != null; paramClassSymbol = paramClassSymbol.owner.enclClass()) {
/*  419 */       str = paramClassSymbol.name + (str.equals("") ? "" : ".") + str;
/*      */     }
/*  421 */     return str;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   static String classToString(DocEnv paramDocEnv, Symbol.ClassSymbol paramClassSymbol, boolean paramBoolean) {
/*  432 */     StringBuilder stringBuilder = new StringBuilder();
/*  433 */     if (!paramClassSymbol.isInner()) {
/*  434 */       stringBuilder.append(getClassName(paramClassSymbol, paramBoolean));
/*      */     } else {
/*      */
/*  437 */       Symbol.ClassSymbol classSymbol = paramClassSymbol.owner.enclClass();
/*  438 */       stringBuilder.append(classToString(paramDocEnv, classSymbol, paramBoolean))
/*  439 */         .append('.')
/*  440 */         .append((CharSequence)paramClassSymbol.name);
/*      */     }
/*  442 */     stringBuilder.append(TypeMaker.typeParametersString(paramDocEnv, (Symbol)paramClassSymbol, paramBoolean));
/*  443 */     return stringBuilder.toString();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   static boolean isGeneric(Symbol.ClassSymbol paramClassSymbol) {
/*  451 */     return paramClassSymbol.type.allparams().nonEmpty();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public TypeVariable[] typeParameters() {
/*  459 */     if (this.env.legacyDoclet) {
/*  460 */       return new TypeVariable[0];
/*      */     }
/*  462 */     TypeVariable[] arrayOfTypeVariable = new TypeVariable[this.type.getTypeArguments().length()];
/*  463 */     TypeMaker.getTypes(this.env, this.type.getTypeArguments(), (Type[])arrayOfTypeVariable);
/*  464 */     return arrayOfTypeVariable;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public ParamTag[] typeParamTags() {
/*  471 */     return this.env.legacyDoclet ? new ParamTag[0] :
/*      */
/*  473 */       comment().typeParamTags();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public String modifiers() {
/*  482 */     return Modifier.toString(modifierSpecifier());
/*      */   }
/*      */
/*      */
/*      */   public int modifierSpecifier() {
/*  487 */     int i = getModifiers();
/*  488 */     return (isInterface() || isAnnotationType()) ? (i & 0xFFFFFBFF) : i;
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
/*      */   public ClassDoc superclass() {
/*  500 */     if (isInterface() || isAnnotationType()) return null;
/*  501 */     if (this.tsym == this.env.syms.objectType.tsym) return null;
/*  502 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)(this.env.types.supertype((Type)this.type)).tsym;
/*  503 */     if (classSymbol == null || classSymbol == this.tsym) classSymbol = (Symbol.ClassSymbol)this.env.syms.objectType.tsym;
/*  504 */     return this.env.getClassDoc(classSymbol);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type superclassType() {
/*  513 */     if (isInterface() || isAnnotationType() || this.tsym == this.env.syms.objectType.tsym)
/*      */     {
/*  515 */       return null; }
/*  516 */     Type type = this.env.types.supertype((Type)this.type);
/*  517 */     return TypeMaker.getType(this.env,
/*  518 */         type.hasTag(TypeTag.NONE) ? this.env.syms.objectType : type);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean subclassOf(ClassDoc paramClassDoc) {
/*  528 */     return this.tsym.isSubClass((Symbol)((ClassDocImpl)paramClassDoc).tsym, this.env.types);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public ClassDoc[] interfaces() {
/*  539 */     ListBuffer listBuffer = new ListBuffer();
/*  540 */     for (Type type : this.env.types.interfaces((Type)this.type)) {
/*  541 */       listBuffer.append(this.env.getClassDoc((Symbol.ClassSymbol)type.tsym));
/*      */     }
/*      */
/*  544 */     return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public Type[] interfaceTypes() {
/*  555 */     return TypeMaker.getTypes(this.env, this.env.types.interfaces((Type)this.type));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public FieldDoc[] fields(boolean paramBoolean) {
/*  563 */     return fields(paramBoolean, false);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public FieldDoc[] fields() {
/*  570 */     return fields(true, false);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public FieldDoc[] enumConstants() {
/*  577 */     return fields(false, true);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private FieldDoc[] fields(boolean paramBoolean1, boolean paramBoolean2) {
/*  586 */     List list = List.nil();
/*  587 */     for (Scope.Entry entry = (this.tsym.members()).elems; entry != null; entry = entry.sibling) {
/*  588 */       if (entry.sym != null && entry.sym.kind == 4) {
/*  589 */         Symbol.VarSymbol varSymbol = (Symbol.VarSymbol)entry.sym;
/*  590 */         boolean bool = ((varSymbol.flags() & 0x4000L) != 0L && !this.env.legacyDoclet);
/*      */
/*  592 */         if (bool == paramBoolean2 && (!paramBoolean1 || this.env
/*  593 */           .shouldDocument(varSymbol))) {
/*  594 */           list = list.prepend(this.env.getFieldDoc(varSymbol));
/*      */         }
/*      */       }
/*      */     }
/*  598 */     return (FieldDoc[])list.toArray((Object[])new FieldDocImpl[list.length()]);
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
/*      */   public MethodDoc[] methods(boolean paramBoolean) {
/*  610 */     Names names = this.tsym.name.table.names;
/*  611 */     List list = List.nil();
/*  612 */     for (Scope.Entry entry = (this.tsym.members()).elems; entry != null; entry = entry.sibling) {
/*  613 */       if (entry.sym != null && entry.sym.kind == 16 && entry.sym.name != names.init && entry.sym.name != names.clinit) {
/*      */
/*      */
/*      */
/*  617 */         Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)entry.sym;
/*  618 */         if (!paramBoolean || this.env.shouldDocument(methodSymbol)) {
/*  619 */           list = list.prepend(this.env.getMethodDoc(methodSymbol));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*  624 */     return (MethodDoc[])list.toArray((Object[])new MethodDocImpl[list.length()]);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public MethodDoc[] methods() {
/*  634 */     return methods(true);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public ConstructorDoc[] constructors(boolean paramBoolean) {
/*  645 */     Names names = this.tsym.name.table.names;
/*  646 */     List list = List.nil();
/*  647 */     for (Scope.Entry entry = (this.tsym.members()).elems; entry != null; entry = entry.sibling) {
/*  648 */       if (entry.sym != null && entry.sym.kind == 16 && entry.sym.name == names.init) {
/*      */
/*  650 */         Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)entry.sym;
/*  651 */         if (!paramBoolean || this.env.shouldDocument(methodSymbol)) {
/*  652 */           list = list.prepend(this.env.getConstructorDoc(methodSymbol));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*  657 */     return (ConstructorDoc[])list.toArray((Object[])new ConstructorDocImpl[list.length()]);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public ConstructorDoc[] constructors() {
/*  667 */     return constructors(true);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void addAllClasses(ListBuffer<ClassDocImpl> paramListBuffer, boolean paramBoolean) {
/*      */     try {
/*  676 */       if (isSynthetic())
/*      */         return;
/*  678 */       if (!JavadocTool.isValidClassName(this.tsym.name.toString()))
/*  679 */         return;  if (paramBoolean && !this.env.shouldDocument(this.tsym))
/*  680 */         return;  if (paramListBuffer.contains(this))
/*  681 */         return;  paramListBuffer.append(this);
/*  682 */       List list = List.nil();
/*  683 */       for (Scope.Entry entry = (this.tsym.members()).elems; entry != null;
/*  684 */         entry = entry.sibling) {
/*  685 */         if (entry.sym != null && entry.sym.kind == 2) {
/*  686 */           Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)entry.sym;
/*  687 */           ClassDocImpl classDocImpl = this.env.getClassDoc(classSymbol);
/*  688 */           if (!classDocImpl.isSynthetic() &&
/*  689 */             classDocImpl != null) list = list.prepend(classDocImpl);
/*      */
/*      */         }
/*      */       }
/*  693 */       for (; list.nonEmpty(); list = list.tail) {
/*  694 */         ((ClassDocImpl)list.head).addAllClasses(paramListBuffer, paramBoolean);
/*      */       }
/*  696 */     } catch (Symbol.CompletionFailure completionFailure) {}
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
/*      */   public ClassDoc[] innerClasses(boolean paramBoolean) {
/*  710 */     ListBuffer listBuffer = new ListBuffer();
/*  711 */     for (Scope.Entry entry = (this.tsym.members()).elems; entry != null; entry = entry.sibling) {
/*  712 */       if (entry.sym != null && entry.sym.kind == 2) {
/*  713 */         Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)entry.sym;
/*  714 */         if ((classSymbol.flags_field & 0x1000L) == 0L && (
/*  715 */           !paramBoolean || this.env.isVisible(classSymbol))) {
/*  716 */           listBuffer.prepend(this.env.getClassDoc(classSymbol));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*  721 */     return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public ClassDoc[] innerClasses() {
/*  732 */     return innerClasses(true);
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
/*      */   public ClassDoc findClass(String paramString) {
/*  745 */     ClassDoc classDoc = searchClass(paramString);
/*  746 */     if (classDoc == null) {
/*  747 */       ClassDocImpl classDocImpl = (ClassDocImpl)containingClass();
/*      */
/*  749 */       while (classDocImpl != null && classDocImpl.containingClass() != null) {
/*  750 */         classDocImpl = (ClassDocImpl)classDocImpl.containingClass();
/*      */       }
/*      */
/*  753 */       classDoc = (classDocImpl == null) ? null : classDocImpl.searchClass(paramString);
/*      */     }
/*  755 */     return classDoc;
/*      */   }
/*      */
/*      */   private ClassDoc searchClass(String paramString) {
/*  759 */     Names names = this.tsym.name.table.names;
/*      */
/*      */
/*  762 */     ClassDocImpl classDocImpl = this.env.lookupClass(paramString);
/*  763 */     if (classDocImpl != null) {
/*  764 */       return classDocImpl;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*  770 */     for (ClassDoc classDoc1 : innerClasses()) {
/*  771 */       if (classDoc1.name().equals(paramString) || classDoc1
/*      */
/*      */
/*      */
/*      */
/*  776 */         .name().endsWith("." + paramString)) {
/*  777 */         return classDoc1;
/*      */       }
/*  779 */       ClassDoc classDoc2 = ((ClassDocImpl)classDoc1).searchClass(paramString);
/*  780 */       if (classDoc2 != null) {
/*  781 */         return classDoc2;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*  787 */     ClassDoc classDoc = containingPackage().findClass(paramString);
/*  788 */     if (classDoc != null) {
/*  789 */       return classDoc;
/*      */     }
/*      */
/*      */
/*  793 */     if (this.tsym.completer != null) {
/*  794 */       this.tsym.complete();
/*      */     }
/*      */
/*      */
/*      */
/*  799 */     if (this.tsym.sourcefile != null) {
/*      */
/*      */
/*      */
/*  803 */       Env env = this.env.enter.getEnv((Symbol.TypeSymbol)this.tsym);
/*  804 */       if (env == null) return null;
/*      */
/*  806 */       Scope.ImportScope importScope = env.toplevel.namedImportScope; Scope.Entry entry;
/*  807 */       for (entry = importScope.lookup(names.fromString(paramString)); entry.scope != null; entry = entry.next()) {
/*  808 */         if (entry.sym.kind == 2) {
/*  809 */           return this.env.getClassDoc((Symbol.ClassSymbol)entry.sym);
/*      */         }
/*      */       }
/*      */
/*      */
/*  814 */       Scope.StarImportScope starImportScope = env.toplevel.starImportScope;
/*  815 */       for (entry = starImportScope.lookup(names.fromString(paramString)); entry.scope != null; entry = entry.next()) {
/*  816 */         if (entry.sym.kind == 2) {
/*  817 */           return this.env.getClassDoc((Symbol.ClassSymbol)entry.sym);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*  823 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */   private boolean hasParameterTypes(Symbol.MethodSymbol paramMethodSymbol, String[] paramArrayOfString) {
/*  829 */     if (paramArrayOfString == null)
/*      */     {
/*  831 */       return true;
/*      */     }
/*      */
/*  834 */     byte b = 0;
/*  835 */     List list = paramMethodSymbol.type.getParameterTypes();
/*      */
/*  837 */     if (paramArrayOfString.length != list.length()) {
/*  838 */       return false;
/*      */     }
/*      */
/*  841 */     for (Type type : list) {
/*  842 */       String str = paramArrayOfString[b++];
/*      */
/*  844 */       if (b == paramArrayOfString.length) {
/*  845 */         str = str.replace("...", "[]");
/*      */       }
/*  847 */       if (!hasTypeName(this.env.types.erasure(type), str)) {
/*  848 */         return false;
/*      */       }
/*      */     }
/*  851 */     return true;
/*      */   }
/*      */
/*      */   private boolean hasTypeName(Type paramType, String paramString) {
/*  855 */     return (paramString
/*  856 */       .equals(TypeMaker.getTypeName(paramType, true)) || paramString
/*      */
/*  858 */       .equals(TypeMaker.getTypeName(paramType, false)) || (
/*      */
/*  860 */       qualifiedName() + "." + paramString).equals(TypeMaker.getTypeName(paramType, true)));
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
/*      */   public MethodDocImpl findMethod(String paramString, String[] paramArrayOfString) {
/*  877 */     return searchMethod(paramString, paramArrayOfString, new HashSet<>());
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private MethodDocImpl searchMethod(String paramString, String[] paramArrayOfString, Set<ClassDocImpl> paramSet) {
/*  884 */     Names names = this.tsym.name.table.names;
/*      */
/*  886 */     if (names.init.contentEquals(paramString)) {
/*  887 */       return null;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*  893 */     if (paramSet.contains(this)) {
/*  894 */       return null;
/*      */     }
/*  896 */     paramSet.add(this);
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  916 */     Scope.Entry entry = this.tsym.members().lookup(names.fromString(paramString));
/*      */
/*      */
/*      */
/*      */
/*      */
/*  922 */     if (paramArrayOfString == null) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*  928 */       Symbol.MethodSymbol methodSymbol = null;
/*  929 */       for (; entry.scope != null; entry = entry.next()) {
/*  930 */         if (entry.sym.kind == 16)
/*      */         {
/*  932 */           if (entry.sym.name.toString().equals(paramString)) {
/*  933 */             methodSymbol = (Symbol.MethodSymbol)entry.sym;
/*      */           }
/*      */         }
/*      */       }
/*  937 */       if (methodSymbol != null) {
/*  938 */         return this.env.getMethodDoc(methodSymbol);
/*      */       }
/*      */     } else {
/*  941 */       for (; entry.scope != null; entry = entry.next()) {
/*  942 */         if (entry.sym != null && entry.sym.kind == 16)
/*      */         {
/*      */
/*  945 */           if (hasParameterTypes((Symbol.MethodSymbol)entry.sym, paramArrayOfString)) {
/*  946 */             return this.env.getMethodDoc((Symbol.MethodSymbol)entry.sym);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*  956 */     ClassDocImpl classDocImpl = (ClassDocImpl)superclass();
/*  957 */     if (classDocImpl != null) {
/*  958 */       MethodDocImpl methodDocImpl = classDocImpl.searchMethod(paramString, paramArrayOfString, paramSet);
/*  959 */       if (methodDocImpl != null) {
/*  960 */         return methodDocImpl;
/*      */       }
/*      */     }
/*      */
/*      */
/*  965 */     ClassDoc[] arrayOfClassDoc = interfaces();
/*  966 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/*  967 */       classDocImpl = (ClassDocImpl)arrayOfClassDoc[b];
/*  968 */       MethodDocImpl methodDocImpl = classDocImpl.searchMethod(paramString, paramArrayOfString, paramSet);
/*  969 */       if (methodDocImpl != null) {
/*  970 */         return methodDocImpl;
/*      */       }
/*      */     }
/*      */
/*      */
/*  975 */     classDocImpl = (ClassDocImpl)containingClass();
/*  976 */     if (classDocImpl != null) {
/*  977 */       MethodDocImpl methodDocImpl = classDocImpl.searchMethod(paramString, paramArrayOfString, paramSet);
/*  978 */       if (methodDocImpl != null) {
/*  979 */         return methodDocImpl;
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
/*  991 */     return null;
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
/*      */   public ConstructorDoc findConstructor(String paramString, String[] paramArrayOfString) {
/* 1003 */     Names names = this.tsym.name.table.names;
/* 1004 */     for (Scope.Entry entry = this.tsym.members().lookup(names.fromString("<init>")); entry.scope != null; entry = entry.next()) {
/* 1005 */       if (entry.sym.kind == 16 &&
/* 1006 */         hasParameterTypes((Symbol.MethodSymbol)entry.sym, paramArrayOfString)) {
/* 1007 */         return this.env.getConstructorDoc((Symbol.MethodSymbol)entry.sym);
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
/* 1020 */     return null;
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
/*      */   public FieldDoc findField(String paramString) {
/* 1036 */     return searchField(paramString, new HashSet<>());
/*      */   }
/*      */
/*      */   private FieldDocImpl searchField(String paramString, Set<ClassDocImpl> paramSet) {
/* 1040 */     Names names = this.tsym.name.table.names;
/* 1041 */     if (paramSet.contains(this)) {
/* 1042 */       return null;
/*      */     }
/* 1044 */     paramSet.add(this);
/*      */
/* 1046 */     for (Scope.Entry entry = this.tsym.members().lookup(names.fromString(paramString)); entry.scope != null; entry = entry.next()) {
/* 1047 */       if (entry.sym.kind == 4)
/*      */       {
/* 1049 */         return this.env.getFieldDoc((Symbol.VarSymbol)entry.sym);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/* 1056 */     ClassDocImpl classDocImpl = (ClassDocImpl)containingClass();
/* 1057 */     if (classDocImpl != null) {
/* 1058 */       FieldDocImpl fieldDocImpl = classDocImpl.searchField(paramString, paramSet);
/* 1059 */       if (fieldDocImpl != null) {
/* 1060 */         return fieldDocImpl;
/*      */       }
/*      */     }
/*      */
/*      */
/* 1065 */     classDocImpl = (ClassDocImpl)superclass();
/* 1066 */     if (classDocImpl != null) {
/* 1067 */       FieldDocImpl fieldDocImpl = classDocImpl.searchField(paramString, paramSet);
/* 1068 */       if (fieldDocImpl != null) {
/* 1069 */         return fieldDocImpl;
/*      */       }
/*      */     }
/*      */
/*      */
/* 1074 */     ClassDoc[] arrayOfClassDoc = interfaces();
/* 1075 */     for (byte b = 0; b < arrayOfClassDoc.length; b++) {
/* 1076 */       classDocImpl = (ClassDocImpl)arrayOfClassDoc[b];
/* 1077 */       FieldDocImpl fieldDocImpl = classDocImpl.searchField(paramString, paramSet);
/* 1078 */       if (fieldDocImpl != null) {
/* 1079 */         return fieldDocImpl;
/*      */       }
/*      */     }
/*      */
/* 1083 */     return null;
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
/*      */   @Deprecated
/*      */   public ClassDoc[] importedClasses() {
/* 1100 */     if (this.tsym.sourcefile == null) return new ClassDoc[0];
/*      */
/* 1102 */     ListBuffer listBuffer = new ListBuffer();
/*      */
/* 1104 */     Env env = this.env.enter.getEnv((Symbol.TypeSymbol)this.tsym);
/* 1105 */     if (env == null) return (ClassDoc[])new ClassDocImpl[0];
/*      */
/* 1107 */     Name name = this.tsym.name.table.names.asterisk;
/* 1108 */     for (JCTree jCTree : env.toplevel.defs) {
/* 1109 */       if (jCTree.hasTag(JCTree.Tag.IMPORT)) {
/* 1110 */         JCTree jCTree1 = ((JCTree.JCImport)jCTree).qualid;
/* 1111 */         if (TreeInfo.name(jCTree1) != name && (jCTree1.type.tsym.kind & 0x2) != 0)
/*      */         {
/* 1113 */           listBuffer.append(this.env
/* 1114 */               .getClassDoc((Symbol.ClassSymbol)jCTree1.type.tsym));
/*      */         }
/*      */       }
/*      */     }
/*      */
/* 1119 */     return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]);
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
/*      */   @Deprecated
/*      */   public PackageDoc[] importedPackages() {
/* 1138 */     if (this.tsym.sourcefile == null) return new PackageDoc[0];
/*      */
/* 1140 */     ListBuffer listBuffer = new ListBuffer();
/*      */
/*      */
/* 1143 */     Names names = this.tsym.name.table.names;
/* 1144 */     listBuffer.append(this.env.getPackageDoc(this.env.reader.enterPackage(names.java_lang)));
/*      */
/* 1146 */     Env env = this.env.enter.getEnv((Symbol.TypeSymbol)this.tsym);
/* 1147 */     if (env == null) return (PackageDoc[])new PackageDocImpl[0];
/*      */
/* 1149 */     for (JCTree jCTree : env.toplevel.defs) {
/* 1150 */       if (jCTree.hasTag(JCTree.Tag.IMPORT)) {
/* 1151 */         JCTree jCTree1 = ((JCTree.JCImport)jCTree).qualid;
/* 1152 */         if (TreeInfo.name(jCTree1) == names.asterisk) {
/* 1153 */           JCTree.JCFieldAccess jCFieldAccess = (JCTree.JCFieldAccess)jCTree1;
/* 1154 */           Symbol.TypeSymbol typeSymbol = jCFieldAccess.selected.type.tsym;
/* 1155 */           PackageDocImpl packageDocImpl = this.env.getPackageDoc(typeSymbol.packge());
/* 1156 */           if (!listBuffer.contains(packageDocImpl)) {
/* 1157 */             listBuffer.append(packageDocImpl);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1162 */     return (PackageDoc[])listBuffer.toArray((Object[])new PackageDocImpl[listBuffer.length()]);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public String dimension() {
/* 1170 */     return "";
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public ClassDoc asClassDoc() {
/* 1177 */     return this;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public AnnotationTypeDoc asAnnotationTypeDoc() {
/* 1184 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public ParameterizedType asParameterizedType() {
/* 1191 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public TypeVariable asTypeVariable() {
/* 1198 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public WildcardType asWildcardType() {
/* 1205 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public AnnotatedType asAnnotatedType() {
/* 1212 */     return null;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isPrimitive() {
/* 1219 */     return false;
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
/*      */   public boolean isSerializable() {
/*      */     try {
/* 1235 */       return this.env.types.isSubtype((Type)this.type, this.env.syms.serializableType);
/* 1236 */     } catch (Symbol.CompletionFailure completionFailure) {
/*      */
/* 1238 */       return false;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean isExternalizable() {
/*      */     try {
/* 1248 */       return this.env.types.isSubtype((Type)this.type, this.env.externalizableSym.type);
/* 1249 */     } catch (Symbol.CompletionFailure completionFailure) {
/*      */
/* 1251 */       return false;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public MethodDoc[] serializationMethods() {
/* 1262 */     if (this.serializedForm == null) {
/* 1263 */       this.serializedForm = new SerializedForm(this.env, this.tsym, this);
/*      */     }
/*      */
/* 1266 */     return this.serializedForm.methods();
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
/*      */   public FieldDoc[] serializableFields() {
/* 1287 */     if (this.serializedForm == null) {
/* 1288 */       this.serializedForm = new SerializedForm(this.env, this.tsym, this);
/*      */     }
/*      */
/* 1291 */     return this.serializedForm.fields();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public boolean definesSerializableFields() {
/* 1302 */     if (!isSerializable() || isExternalizable()) {
/* 1303 */       return false;
/*      */     }
/* 1305 */     if (this.serializedForm == null) {
/* 1306 */       this.serializedForm = new SerializedForm(this.env, this.tsym, this);
/*      */     }
/*      */
/* 1309 */     return this.serializedForm.definesSerializableFields();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   boolean isRuntimeException() {
/* 1319 */     return this.tsym.isSubClass((Symbol)this.env.syms.runtimeExceptionType.tsym, this.env.types);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public SourcePosition position() {
/* 1328 */     if (this.tsym.sourcefile == null) return null;
/* 1329 */     return SourcePositionImpl.make(this.tsym.sourcefile, (this.tree == null) ? -1 : this.tree.pos, this.lineMap);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\ClassDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
