/*     */ package com.sun.tools.javadoc;
/*     */
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.doclint.DocLint;
/*     */ import com.sun.tools.javac.api.BasicJavacTask;
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.comp.Check;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.tools.JavaFileManager;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class DocEnv
/*     */ {
/*  65 */   protected static final Context.Key<DocEnv> docEnvKey = new Context.Key(); private Messager messager; DocLocale doclocale; Symtab syms; JavadocClassReader reader; JavadocEnter enter; private final Names names; private String encoding; final Symbol externalizableSym; protected ModifierFilter showAccess;
/*     */   boolean breakiterator;
/*     */
/*     */   public static DocEnv instance(Context paramContext) {
/*  69 */     DocEnv docEnv = (DocEnv)paramContext.get(docEnvKey);
/*  70 */     if (docEnv == null)
/*  71 */       docEnv = new DocEnv(paramContext);
/*  72 */     return docEnv;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   boolean quiet = false;
/*     */
/*     */
/*     */
/*     */
/*     */   Check chk;
/*     */
/*     */
/*     */
/*     */
/*     */   Types types;
/*     */
/*     */
/*     */
/*     */
/*     */   JavaFileManager fileManager;
/*     */
/*     */
/*     */
/*     */
/*     */   Context context;
/*     */
/*     */
/*     */
/*     */
/*     */   DocLint doclint;
/*     */
/*     */
/*     */
/*     */
/*     */   JavaScriptScanner javaScriptScanner;
/*     */
/*     */
/*     */
/*     */
/* 114 */   WeakHashMap<JCTree, TreePath> treePaths = new WeakHashMap<>();
/*     */
/*     */
/*     */
/*     */
/*     */   boolean docClasses = false;
/*     */
/*     */
/*     */
/*     */
/*     */   protected boolean legacyDoclet = true;
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean silent = false;
/*     */
/*     */
/*     */
/*     */
/*     */   protected Source source;
/*     */
/*     */
/*     */
/*     */
/*     */   protected Map<Symbol.PackageSymbol, PackageDocImpl> packageMap;
/*     */
/*     */
/*     */
/*     */
/*     */   protected Map<Symbol.ClassSymbol, ClassDocImpl> classMap;
/*     */
/*     */
/*     */
/*     */
/*     */   protected Map<Symbol.VarSymbol, FieldDocImpl> fieldMap;
/*     */
/*     */
/*     */
/*     */
/*     */   protected Map<Symbol.MethodSymbol, ExecutableMemberDocImpl> methodMap;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void setSilent(boolean paramBoolean) {
/* 161 */     this.silent = paramBoolean;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public ClassDocImpl lookupClass(String paramString) {
/* 168 */     Symbol.ClassSymbol classSymbol = getClassSymbol(paramString);
/* 169 */     if (classSymbol != null) {
/* 170 */       return getClassDoc(classSymbol);
/*     */     }
/* 172 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ClassDocImpl loadClass(String paramString) {
/*     */     try {
/* 181 */       Symbol.ClassSymbol classSymbol = this.reader.loadClass(this.names.fromString(paramString));
/* 182 */       return getClassDoc(classSymbol);
/* 183 */     } catch (Symbol.CompletionFailure completionFailure) {
/* 184 */       this.chk.completionError(null, completionFailure);
/* 185 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public PackageDocImpl lookupPackage(String paramString) {
/* 197 */     Symbol.PackageSymbol packageSymbol = (Symbol.PackageSymbol)this.syms.packages.get(this.names.fromString(paramString));
/* 198 */     Symbol.ClassSymbol classSymbol = getClassSymbol(paramString);
/* 199 */     if (packageSymbol != null && classSymbol == null) {
/* 200 */       return getPackageDoc(packageSymbol);
/*     */     }
/* 202 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   Symbol.ClassSymbol getClassSymbol(String paramString) {
/* 212 */     int i = paramString.length();
/* 213 */     char[] arrayOfChar = paramString.toCharArray();
/* 214 */     int j = paramString.length();
/*     */     while (true) {
/* 216 */       Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)this.syms.classes.get(this.names.fromChars(arrayOfChar, 0, i));
/* 217 */       if (classSymbol != null)
/* 218 */         return classSymbol;
/* 219 */       j = paramString.substring(0, j).lastIndexOf('.');
/* 220 */       if (j < 0)
/* 221 */         break;  arrayOfChar[j] = '$';
/*     */     }
/* 223 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void setLocale(String paramString) {
/* 231 */     this.doclocale = new DocLocale(this, paramString, this.breakiterator);
/*     */
/* 233 */     this.messager.setLocale(this.doclocale.locale);
/*     */   }
/*     */
/*     */
/*     */   public boolean shouldDocument(Symbol.VarSymbol paramVarSymbol) {
/* 238 */     long l = paramVarSymbol.flags();
/*     */
/* 240 */     if ((l & 0x1000L) != 0L) {
/* 241 */       return false;
/*     */     }
/*     */
/* 244 */     return this.showAccess.checkModifier(translateModifiers(l));
/*     */   }
/*     */
/*     */
/*     */   public boolean shouldDocument(Symbol.MethodSymbol paramMethodSymbol) {
/* 249 */     long l = paramMethodSymbol.flags();
/*     */
/* 251 */     if ((l & 0x1000L) != 0L) {
/* 252 */       return false;
/*     */     }
/*     */
/* 255 */     return this.showAccess.checkModifier(translateModifiers(l));
/*     */   }
/*     */
/*     */
/*     */   public boolean shouldDocument(Symbol.ClassSymbol paramClassSymbol) {
/* 260 */     return ((paramClassSymbol.flags_field & 0x1000L) == 0L && (this.docClasses ||
/*     */
/* 262 */       (getClassDoc(paramClassSymbol)).tree != null) &&
/* 263 */       isVisible(paramClassSymbol));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected boolean isVisible(Symbol.ClassSymbol paramClassSymbol) {
/* 282 */     long l = paramClassSymbol.flags_field;
/* 283 */     if (!this.showAccess.checkModifier(translateModifiers(l))) {
/* 284 */       return false;
/*     */     }
/* 286 */     Symbol.ClassSymbol classSymbol = paramClassSymbol.owner.enclClass();
/* 287 */     return (classSymbol == null || (l & 0x8L) != 0L || isVisible(classSymbol));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void printError(String paramString) {
/* 298 */     if (this.silent)
/*     */       return;
/* 300 */     this.messager.printError(paramString);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void error(DocImpl paramDocImpl, String paramString) {
/* 309 */     if (this.silent)
/*     */       return;
/* 311 */     this.messager.error((paramDocImpl == null) ? null : paramDocImpl.position(), paramString, new Object[0]);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void error(SourcePosition paramSourcePosition, String paramString) {
/* 320 */     if (this.silent)
/*     */       return;
/* 322 */     this.messager.error(paramSourcePosition, paramString, new Object[0]);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void printError(SourcePosition paramSourcePosition, String paramString) {
/* 331 */     if (this.silent)
/*     */       return;
/* 333 */     this.messager.printError(paramSourcePosition, paramString);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void error(DocImpl paramDocImpl, String paramString1, String paramString2) {
/* 343 */     if (this.silent)
/*     */       return;
/* 345 */     this.messager.error((paramDocImpl == null) ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void error(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3) {
/* 356 */     if (this.silent)
/*     */       return;
/* 358 */     this.messager.error((paramDocImpl == null) ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void error(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3, String paramString4) {
/* 370 */     if (this.silent)
/*     */       return;
/* 372 */     this.messager.error((paramDocImpl == null) ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3, paramString4 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void printWarning(String paramString) {
/* 381 */     if (this.silent)
/*     */       return;
/* 383 */     this.messager.printWarning(paramString);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void warning(DocImpl paramDocImpl, String paramString) {
/* 392 */     if (this.silent)
/*     */       return;
/* 394 */     this.messager.warning((paramDocImpl == null) ? null : paramDocImpl.position(), paramString, new Object[0]);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void printWarning(SourcePosition paramSourcePosition, String paramString) {
/* 403 */     if (this.silent)
/*     */       return;
/* 405 */     this.messager.printWarning(paramSourcePosition, paramString);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void warning(DocImpl paramDocImpl, String paramString1, String paramString2) {
/* 415 */     if (this.silent) {
/*     */       return;
/*     */     }
/* 418 */     if (this.doclint != null && paramDocImpl != null && paramString1.startsWith("tag"))
/*     */       return;
/* 420 */     this.messager.warning((paramDocImpl == null) ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void warning(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3) {
/* 431 */     if (this.silent)
/*     */       return;
/* 433 */     this.messager.warning((paramDocImpl == null) ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void warning(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3, String paramString4) {
/* 445 */     if (this.silent)
/*     */       return;
/* 447 */     this.messager.warning((paramDocImpl == null) ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3, paramString4 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void warning(DocImpl paramDocImpl, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {
/* 460 */     if (this.silent)
/*     */       return;
/* 462 */     this.messager.warning((paramDocImpl == null) ? null : paramDocImpl.position(), paramString1, new Object[] { paramString2, paramString3, paramString4, paramString5 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void printNotice(String paramString) {
/* 471 */     if (this.silent || this.quiet)
/*     */       return;
/* 473 */     this.messager.printNotice(paramString);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void notice(String paramString) {
/* 483 */     if (this.silent || this.quiet)
/*     */       return;
/* 485 */     this.messager.notice(paramString, new Object[0]);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void printNotice(SourcePosition paramSourcePosition, String paramString) {
/* 494 */     if (this.silent || this.quiet)
/*     */       return;
/* 496 */     this.messager.printNotice(paramSourcePosition, paramString);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void notice(String paramString1, String paramString2) {
/* 506 */     if (this.silent || this.quiet)
/*     */       return;
/* 508 */     this.messager.notice(paramString1, new Object[] { paramString2 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void notice(String paramString1, String paramString2, String paramString3) {
/* 519 */     if (this.silent || this.quiet)
/*     */       return;
/* 521 */     this.messager.notice(paramString1, new Object[] { paramString2, paramString3 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void notice(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 533 */     if (this.silent || this.quiet)
/*     */       return;
/* 535 */     this.messager.notice(paramString1, new Object[] { paramString2, paramString3, paramString4 });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void exit() {
/* 545 */     this.messager.exit();
/*     */   }
/*     */   public PackageDocImpl getPackageDoc(Symbol.PackageSymbol paramPackageSymbol) { PackageDocImpl packageDocImpl = this.packageMap.get(paramPackageSymbol); if (packageDocImpl != null) return packageDocImpl;  packageDocImpl = new PackageDocImpl(this, paramPackageSymbol); this.packageMap.put(paramPackageSymbol, packageDocImpl); return packageDocImpl; } void makePackageDoc(Symbol.PackageSymbol paramPackageSymbol, TreePath paramTreePath) { PackageDocImpl packageDocImpl = this.packageMap.get(paramPackageSymbol); if (packageDocImpl != null) { if (paramTreePath != null)
/* 548 */         packageDocImpl.setTreePath(paramTreePath);  } else { packageDocImpl = new PackageDocImpl(this, paramPackageSymbol, paramTreePath); this.packageMap.put(paramPackageSymbol, packageDocImpl); }  } protected DocEnv(Context paramContext) { this.packageMap = new HashMap<>();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 575 */     this.classMap = new HashMap<>();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 617 */     this.fieldMap = new HashMap<>();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 642 */     this.methodMap = new HashMap<>(); paramContext.put(docEnvKey, this); this.context = paramContext; this.messager = Messager.instance0(paramContext); this.syms = Symtab.instance(paramContext); this.reader = JavadocClassReader.instance0(paramContext); this.enter = JavadocEnter.instance0(paramContext); this.names = Names.instance(paramContext); this.externalizableSym = (Symbol)this.reader.enterClass(this.names.fromString("java.io.Externalizable")); this.chk = Check.instance(paramContext); this.types = Types.instance(paramContext); this.fileManager = (JavaFileManager)paramContext.get(JavaFileManager.class); if (this.fileManager instanceof JavacFileManager)
/*     */       ((JavacFileManager)this.fileManager).setSymbolFileEnabled(false);  this.doclocale = new DocLocale(this, "", this.breakiterator); this.source = Source.instance(paramContext); }
/*     */   public ClassDocImpl getClassDoc(Symbol.ClassSymbol paramClassSymbol) { ClassDocImpl classDocImpl = this.classMap.get(paramClassSymbol); if (classDocImpl != null)
/*     */       return classDocImpl;  if (isAnnotationType(paramClassSymbol)) { classDocImpl = new AnnotationTypeDocImpl(this, paramClassSymbol); } else { classDocImpl = new ClassDocImpl(this, paramClassSymbol); }  this.classMap.put(paramClassSymbol, classDocImpl); return classDocImpl; }
/*     */   protected void makeClassDoc(Symbol.ClassSymbol paramClassSymbol, TreePath paramTreePath) { ClassDocImpl classDocImpl = this.classMap.get(paramClassSymbol); if (classDocImpl != null) { if (paramTreePath != null)
/*     */         classDocImpl.setTreePath(paramTreePath);  return; }  if (isAnnotationType((JCTree.JCClassDecl)paramTreePath.getLeaf())) { classDocImpl = new AnnotationTypeDocImpl(this, paramClassSymbol, paramTreePath); }
/*     */     else { classDocImpl = new ClassDocImpl(this, paramClassSymbol, paramTreePath); }
/* 649 */      this.classMap.put(paramClassSymbol, classDocImpl); } protected void makeMethodDoc(Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath) { MethodDocImpl methodDocImpl = (MethodDocImpl)this.methodMap.get(paramMethodSymbol);
/* 650 */     if (methodDocImpl != null) {
/* 651 */       if (paramTreePath != null) methodDocImpl.setTreePath(paramTreePath);
/*     */     } else {
/* 653 */       methodDocImpl = new MethodDocImpl(this, paramMethodSymbol, paramTreePath);
/* 654 */       this.methodMap.put(paramMethodSymbol, methodDocImpl);
/*     */     }  } protected static boolean isAnnotationType(Symbol.ClassSymbol paramClassSymbol) { return ClassDocImpl.isAnnotationType(paramClassSymbol); }
/*     */   protected static boolean isAnnotationType(JCTree.JCClassDecl paramJCClassDecl) { return ((paramJCClassDecl.mods.flags & 0x2000L) != 0L); }
/*     */   public FieldDocImpl getFieldDoc(Symbol.VarSymbol paramVarSymbol) { FieldDocImpl fieldDocImpl = this.fieldMap.get(paramVarSymbol); if (fieldDocImpl != null)
/*     */       return fieldDocImpl;  fieldDocImpl = new FieldDocImpl(this, paramVarSymbol); this.fieldMap.put(paramVarSymbol, fieldDocImpl); return fieldDocImpl; }
/*     */   protected void makeFieldDoc(Symbol.VarSymbol paramVarSymbol, TreePath paramTreePath) { FieldDocImpl fieldDocImpl = this.fieldMap.get(paramVarSymbol); if (fieldDocImpl != null) { if (paramTreePath != null)
/*     */         fieldDocImpl.setTreePath(paramTreePath);  }
/*     */     else { fieldDocImpl = new FieldDocImpl(this, paramVarSymbol, paramTreePath); this.fieldMap.put(paramVarSymbol, fieldDocImpl); }
/*     */      }
/* 663 */   public MethodDocImpl getMethodDoc(Symbol.MethodSymbol paramMethodSymbol) { assert !paramMethodSymbol.isConstructor() : "not expecting a constructor symbol";
/* 664 */     MethodDocImpl methodDocImpl = (MethodDocImpl)this.methodMap.get(paramMethodSymbol);
/* 665 */     if (methodDocImpl != null) return methodDocImpl;
/* 666 */     methodDocImpl = new MethodDocImpl(this, paramMethodSymbol);
/* 667 */     this.methodMap.put(paramMethodSymbol, methodDocImpl);
/* 668 */     return methodDocImpl; }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected void makeConstructorDoc(Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath) {
/* 676 */     ConstructorDocImpl constructorDocImpl = (ConstructorDocImpl)this.methodMap.get(paramMethodSymbol);
/* 677 */     if (constructorDocImpl != null) {
/* 678 */       if (paramTreePath != null) constructorDocImpl.setTreePath(paramTreePath);
/*     */     } else {
/* 680 */       constructorDocImpl = new ConstructorDocImpl(this, paramMethodSymbol, paramTreePath);
/* 681 */       this.methodMap.put(paramMethodSymbol, constructorDocImpl);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public ConstructorDocImpl getConstructorDoc(Symbol.MethodSymbol paramMethodSymbol) {
/* 690 */     assert paramMethodSymbol.isConstructor() : "expecting a constructor symbol";
/* 691 */     ConstructorDocImpl constructorDocImpl = (ConstructorDocImpl)this.methodMap.get(paramMethodSymbol);
/* 692 */     if (constructorDocImpl != null) return constructorDocImpl;
/* 693 */     constructorDocImpl = new ConstructorDocImpl(this, paramMethodSymbol);
/* 694 */     this.methodMap.put(paramMethodSymbol, constructorDocImpl);
/* 695 */     return constructorDocImpl;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected void makeAnnotationTypeElementDoc(Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath) {
/* 704 */     AnnotationTypeElementDocImpl annotationTypeElementDocImpl = (AnnotationTypeElementDocImpl)this.methodMap.get(paramMethodSymbol);
/* 705 */     if (annotationTypeElementDocImpl != null) {
/* 706 */       if (paramTreePath != null) annotationTypeElementDocImpl.setTreePath(paramTreePath);
/*     */     } else {
/* 708 */       annotationTypeElementDocImpl = new AnnotationTypeElementDocImpl(this, paramMethodSymbol, paramTreePath);
/*     */
/* 710 */       this.methodMap.put(paramMethodSymbol, annotationTypeElementDocImpl);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public AnnotationTypeElementDocImpl getAnnotationTypeElementDoc(Symbol.MethodSymbol paramMethodSymbol) {
/* 722 */     AnnotationTypeElementDocImpl annotationTypeElementDocImpl = (AnnotationTypeElementDocImpl)this.methodMap.get(paramMethodSymbol);
/* 723 */     if (annotationTypeElementDocImpl != null) return annotationTypeElementDocImpl;
/* 724 */     annotationTypeElementDocImpl = new AnnotationTypeElementDocImpl(this, paramMethodSymbol);
/* 725 */     this.methodMap.put(paramMethodSymbol, annotationTypeElementDocImpl);
/* 726 */     return annotationTypeElementDocImpl;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   ParameterizedTypeImpl getParameterizedType(Type.ClassType paramClassType) {
/* 737 */     return new ParameterizedTypeImpl(this, (Type)paramClassType);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   TreePath getTreePath(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 746 */     TreePath treePath = this.treePaths.get(paramJCCompilationUnit);
/* 747 */     if (treePath == null)
/* 748 */       this.treePaths.put(paramJCCompilationUnit, treePath = new TreePath((CompilationUnitTree)paramJCCompilationUnit));
/* 749 */     return treePath;
/*     */   }
/*     */
/*     */   TreePath getTreePath(JCTree.JCCompilationUnit paramJCCompilationUnit, JCTree.JCClassDecl paramJCClassDecl) {
/* 753 */     TreePath treePath = this.treePaths.get(paramJCClassDecl);
/* 754 */     if (treePath == null)
/* 755 */       this.treePaths.put(paramJCClassDecl, treePath = new TreePath(getTreePath(paramJCCompilationUnit), (Tree)paramJCClassDecl));
/* 756 */     return treePath;
/*     */   }
/*     */
/*     */   TreePath getTreePath(JCTree.JCCompilationUnit paramJCCompilationUnit, JCTree.JCClassDecl paramJCClassDecl, JCTree paramJCTree) {
/* 760 */     return new TreePath(getTreePath(paramJCCompilationUnit, paramJCClassDecl), (Tree)paramJCTree);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void setEncoding(String paramString) {
/* 767 */     this.encoding = paramString;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getEncoding() {
/* 774 */     return this.encoding;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   static int translateModifiers(long paramLong) {
/* 782 */     int i = 0;
/* 783 */     if ((paramLong & 0x400L) != 0L)
/* 784 */       i |= 0x400;
/* 785 */     if ((paramLong & 0x10L) != 0L)
/* 786 */       i |= 0x10;
/* 787 */     if ((paramLong & 0x200L) != 0L)
/* 788 */       i |= 0x200;
/* 789 */     if ((paramLong & 0x100L) != 0L)
/* 790 */       i |= 0x100;
/* 791 */     if ((paramLong & 0x2L) != 0L)
/* 792 */       i |= 0x2;
/* 793 */     if ((paramLong & 0x4L) != 0L)
/* 794 */       i |= 0x4;
/* 795 */     if ((paramLong & 0x1L) != 0L)
/* 796 */       i |= 0x1;
/* 797 */     if ((paramLong & 0x8L) != 0L)
/* 798 */       i |= 0x8;
/* 799 */     if ((paramLong & 0x20L) != 0L)
/* 800 */       i |= 0x20;
/* 801 */     if ((paramLong & 0x80L) != 0L)
/* 802 */       i |= 0x80;
/* 803 */     if ((paramLong & 0x40L) != 0L)
/* 804 */       i |= 0x40;
/* 805 */     return i;
/*     */   }
/*     */
/*     */   void initDoclint(Collection<String> paramCollection1, Collection<String> paramCollection2) {
/* 809 */     ArrayList<String> arrayList = new ArrayList();
/*     */
/* 811 */     for (String str1 : paramCollection1) {
/* 812 */       arrayList.add((str1 == null) ? "-Xmsgs" : ("-Xmsgs:" + str1));
/*     */     }
/*     */
/* 815 */     if (arrayList.isEmpty()) {
/* 816 */       arrayList.add("-Xmsgs");
/* 817 */     } else if (arrayList.size() == 1 && ((String)arrayList
/* 818 */       .get(0)).equals("-Xmsgs:none")) {
/*     */       return;
/*     */     }
/*     */
/* 822 */     String str = "";
/* 823 */     StringBuilder stringBuilder = new StringBuilder();
/* 824 */     for (String str1 : paramCollection2) {
/* 825 */       stringBuilder.append(str);
/* 826 */       stringBuilder.append(str1);
/* 827 */       str = ",";
/*     */     }
/* 829 */     arrayList.add("-XcustomTags:" + stringBuilder.toString());
/*     */
/* 831 */     JavacTask javacTask = BasicJavacTask.instance(this.context);
/* 832 */     this.doclint = new DocLint();
/*     */
/* 834 */     arrayList.add("-XimplicitHeaders:2");
/* 835 */     this.doclint.init(javacTask, arrayList.<String>toArray(new String[arrayList.size()]), false);
/*     */   }
/*     */
/*     */   JavaScriptScanner initJavaScriptScanner(boolean paramBoolean) {
/* 839 */     if (paramBoolean) {
/* 840 */       this.javaScriptScanner = null;
/*     */     } else {
/* 842 */       this.javaScriptScanner = new JavaScriptScanner();
/*     */     }
/* 844 */     return this.javaScriptScanner;
/*     */   }
/*     */
/*     */   boolean showTagMessages() {
/* 848 */     return (this.doclint == null);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\DocEnv.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
