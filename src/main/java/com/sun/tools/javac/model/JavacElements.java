/*     */ package com.sun.tools.javac.model;
/*     */
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.comp.AttrContext;
/*     */ import com.sun.tools.javac.comp.Enter;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.main.JavaCompiler;
/*     */ import com.sun.tools.javac.processing.PrintingProcessor;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.tree.TreeScanner;
/*     */ import com.sun.tools.javac.util.Constants;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.io.Writer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.tools.JavaFileObject;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class JavacElements
/*     */   implements Elements
/*     */ {
/*     */   private JavaCompiler javaCompiler;
/*     */   private Symtab syms;
/*     */   private Names names;
/*     */   private Types types;
/*     */   private Enter enter;
/*     */
/*     */   public static JavacElements instance(Context paramContext) {
/*  70 */     JavacElements javacElements = (JavacElements)paramContext.get(JavacElements.class);
/*  71 */     if (javacElements == null)
/*  72 */       javacElements = new JavacElements(paramContext);
/*  73 */     return javacElements;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   protected JavacElements(Context paramContext) {
/*  80 */     setContext(paramContext);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void setContext(Context paramContext) {
/*  88 */     paramContext.put(JavacElements.class, this);
/*  89 */     this.javaCompiler = JavaCompiler.instance(paramContext);
/*  90 */     this.syms = Symtab.instance(paramContext);
/*  91 */     this.names = Names.instance(paramContext);
/*  92 */     this.types = Types.instance(paramContext);
/*  93 */     this.enter = Enter.instance(paramContext);
/*     */   }
/*     */
/*     */   public Symbol.PackageSymbol getPackageElement(CharSequence paramCharSequence) {
/*  97 */     String str = paramCharSequence.toString();
/*  98 */     if (str.equals(""))
/*  99 */       return this.syms.unnamedPackage;
/* 100 */     return SourceVersion.isName(str) ?
/* 101 */       nameToSymbol(str, Symbol.PackageSymbol.class) : null;
/*     */   }
/*     */
/*     */
/*     */   public Symbol.ClassSymbol getTypeElement(CharSequence paramCharSequence) {
/* 106 */     String str = paramCharSequence.toString();
/* 107 */     return SourceVersion.isName(str) ?
/* 108 */       nameToSymbol(str, Symbol.ClassSymbol.class) : null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private <S extends Symbol> S nameToSymbol(String paramString, Class<S> paramClass) {
/* 117 */     Name name = this.names.fromString(paramString);
/*     */
/*     */
/*     */
/* 121 */     Symbol symbol = (paramClass == Symbol.ClassSymbol.class) ? (Symbol)this.syms.classes.get(name) : (Symbol)this.syms.packages.get(name);
/*     */
/*     */     try {
/* 124 */       if (symbol == null) {
/* 125 */         symbol = this.javaCompiler.resolveIdent(paramString);
/*     */       }
/* 127 */       symbol.complete();
/*     */
/* 129 */       return (symbol.kind != 63 && symbol
/* 130 */         .exists() && paramClass
/* 131 */         .isInstance(symbol) && name
/* 132 */         .equals(symbol.getQualifiedName())) ? paramClass
/* 133 */         .cast(symbol) : null;
/*     */     }
/* 135 */     catch (Symbol.CompletionFailure completionFailure) {
/* 136 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */   public JavacSourcePosition getSourcePosition(Element paramElement) {
/* 141 */     Pair<JCTree, JCTree.JCCompilationUnit> pair = getTreeAndTopLevel(paramElement);
/* 142 */     if (pair == null)
/* 143 */       return null;
/* 144 */     JCTree jCTree = (JCTree)pair.fst;
/* 145 */     JCTree.JCCompilationUnit jCCompilationUnit = (JCTree.JCCompilationUnit)pair.snd;
/* 146 */     JavaFileObject javaFileObject = jCCompilationUnit.sourcefile;
/* 147 */     if (javaFileObject == null)
/* 148 */       return null;
/* 149 */     return new JavacSourcePosition(javaFileObject, jCTree.pos, jCCompilationUnit.lineMap);
/*     */   }
/*     */
/*     */   public JavacSourcePosition getSourcePosition(Element paramElement, AnnotationMirror paramAnnotationMirror) {
/* 153 */     Pair<JCTree, JCTree.JCCompilationUnit> pair = getTreeAndTopLevel(paramElement);
/* 154 */     if (pair == null)
/* 155 */       return null;
/* 156 */     JCTree jCTree1 = (JCTree)pair.fst;
/* 157 */     JCTree.JCCompilationUnit jCCompilationUnit = (JCTree.JCCompilationUnit)pair.snd;
/* 158 */     JavaFileObject javaFileObject = jCCompilationUnit.sourcefile;
/* 159 */     if (javaFileObject == null) {
/* 160 */       return null;
/*     */     }
/* 162 */     JCTree jCTree2 = matchAnnoToTree(paramAnnotationMirror, paramElement, jCTree1);
/* 163 */     if (jCTree2 == null)
/* 164 */       return null;
/* 165 */     return new JavacSourcePosition(javaFileObject, jCTree2.pos, jCCompilationUnit.lineMap);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public JavacSourcePosition getSourcePosition(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue) {
/* 172 */     return getSourcePosition(paramElement, paramAnnotationMirror);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private JCTree matchAnnoToTree(AnnotationMirror paramAnnotationMirror, Element paramElement, JCTree paramJCTree) {
/* 181 */     Symbol symbol = cast(Symbol.class, paramElement);
/*     */     class Vis extends JCTree.Visitor {
/* 183 */       List<JCTree.JCAnnotation> result = null;
/*     */       public void visitTopLevel(JCTree.JCCompilationUnit param1JCCompilationUnit) {
/* 185 */         this.result = param1JCCompilationUnit.packageAnnotations;
/*     */       }
/*     */       public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {
/* 188 */         this.result = param1JCClassDecl.mods.annotations;
/*     */       }
/*     */       public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/* 191 */         this.result = param1JCMethodDecl.mods.annotations;
/*     */       }
/*     */       public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/* 194 */         this.result = param1JCVariableDecl.mods.annotations;
/*     */       }
/*     */     };
/* 197 */     Vis vis = new Vis();
/* 198 */     paramJCTree.accept(vis);
/* 199 */     if (vis.result == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     List<Attribute.Compound> list = symbol.getRawAttributes();
/* 203 */     return matchAnnoToTree(cast(Attribute.Compound.class, paramAnnotationMirror), list, vis.result);
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
/*     */   private JCTree matchAnnoToTree(Attribute.Compound paramCompound, List<Attribute.Compound> paramList, List<JCTree.JCAnnotation> paramList1) {
/* 216 */     for (Attribute.Compound compound : paramList) {
/* 217 */       for (JCTree.JCAnnotation jCAnnotation : paramList1) {
/* 218 */         JCTree jCTree = matchAnnoToTree(paramCompound, (Attribute)compound, (JCTree)jCAnnotation);
/* 219 */         if (jCTree != null)
/* 220 */           return jCTree;
/*     */       }
/*     */     }
/* 223 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private JCTree matchAnnoToTree(final Attribute.Compound findme, Attribute paramAttribute, final JCTree tree) {
/* 234 */     if (paramAttribute == findme)
/* 235 */       return (tree.type.tsym == findme.type.tsym) ? tree : null;
/*     */     class Vis implements Attribute.Visitor {
/*     */       Vis() {
/* 238 */         this.result = null;
/*     */       }
/*     */       JCTree result;
/*     */       public void visitConstant(Attribute.Constant param1Constant) {}
/*     */       public void visitClass(Attribute.Class param1Class) {}
/*     */       public void visitCompound(Attribute.Compound param1Compound) {
/* 244 */         for (Pair pair : param1Compound.values) {
/* 245 */           JCTree.JCExpression jCExpression = JavacElements.this.scanForAssign((Symbol.MethodSymbol)pair.fst, tree);
/* 246 */           if (jCExpression != null) {
/* 247 */             JCTree jCTree = JavacElements.this.matchAnnoToTree(findme, (Attribute)pair.snd, (JCTree)jCExpression);
/* 248 */             if (jCTree != null) {
/* 249 */               this.result = jCTree;
/*     */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       public void visitArray(Attribute.Array param1Array) {
/* 256 */         if (tree.hasTag(JCTree.Tag.NEWARRAY) &&
/* 257 */           (JavacElements.this.types.elemtype(param1Array.type)).tsym == findme.type.tsym) {
/* 258 */           List list = ((JCTree.JCNewArray)tree).elems;
/* 259 */           for (Attribute attribute : param1Array.values) {
/* 260 */             if (attribute == findme) {
/* 261 */               this.result = (JCTree)list.head;
/*     */               return;
/*     */             }
/* 264 */             list = list.tail;
/*     */           }
/*     */         }
/*     */       }
/*     */
/*     */       public void visitEnum(Attribute.Enum param1Enum) {}
/*     */
/*     */       public void visitError(Attribute.Error param1Error) {}
/*     */     };
/* 273 */     Vis vis = new Vis();
/* 274 */     paramAttribute.accept(vis);
/* 275 */     return vis.result;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private JCTree.JCExpression scanForAssign(final Symbol.MethodSymbol sym, final JCTree tree) {
/*     */     class TS
/*     */       extends TreeScanner
/*     */     {
/* 285 */       JCTree.JCExpression result = null;
/*     */       public void scan(JCTree param1JCTree) {
/* 287 */         if (param1JCTree != null && this.result == null)
/* 288 */           param1JCTree.accept((JCTree.Visitor)this);
/*     */       }
/*     */       public void visitAnnotation(JCTree.JCAnnotation param1JCAnnotation) {
/* 291 */         if (param1JCAnnotation == tree)
/* 292 */           scan(param1JCAnnotation.args);
/*     */       }
/*     */       public void visitAssign(JCTree.JCAssign param1JCAssign) {
/* 295 */         if (param1JCAssign.lhs.hasTag(JCTree.Tag.IDENT)) {
/* 296 */           JCTree.JCIdent jCIdent = (JCTree.JCIdent)param1JCAssign.lhs;
/* 297 */           if (jCIdent.sym == sym)
/* 298 */             this.result = param1JCAssign.rhs;
/*     */         }
/*     */       }
/*     */     };
/* 302 */     TS tS = new TS();
/* 303 */     tree.accept((JCTree.Visitor)tS);
/* 304 */     return tS.result;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public JCTree getTree(Element paramElement) {
/* 312 */     Pair<JCTree, JCTree.JCCompilationUnit> pair = getTreeAndTopLevel(paramElement);
/* 313 */     return (pair != null) ? (JCTree)pair.fst : null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String getDocComment(Element paramElement) {
/* 321 */     Pair<JCTree, JCTree.JCCompilationUnit> pair = getTreeAndTopLevel(paramElement);
/* 322 */     if (pair == null)
/* 323 */       return null;
/* 324 */     JCTree jCTree = (JCTree)pair.fst;
/* 325 */     JCTree.JCCompilationUnit jCCompilationUnit = (JCTree.JCCompilationUnit)pair.snd;
/* 326 */     if (jCCompilationUnit.docComments == null)
/* 327 */       return null;
/* 328 */     return jCCompilationUnit.docComments.getCommentText(jCTree);
/*     */   }
/*     */
/*     */   public PackageElement getPackageOf(Element paramElement) {
/* 332 */     return (PackageElement)((Symbol)cast(Symbol.class, paramElement)).packge();
/*     */   }
/*     */
/*     */   public boolean isDeprecated(Element paramElement) {
/* 336 */     Symbol symbol = cast(Symbol.class, paramElement);
/* 337 */     return ((symbol.flags() & 0x20000L) != 0L);
/*     */   }
/*     */
/*     */   public Name getBinaryName(TypeElement paramTypeElement) {
/* 341 */     return ((Symbol.TypeSymbol)cast(Symbol.TypeSymbol.class, paramTypeElement)).flatName();
/*     */   }
/*     */
/*     */
/*     */   public Map<Symbol.MethodSymbol, Attribute> getElementValuesWithDefaults(AnnotationMirror paramAnnotationMirror) {
/* 346 */     Attribute.Compound compound = cast(Attribute.Compound.class, paramAnnotationMirror);
/* 347 */     DeclaredType declaredType = paramAnnotationMirror.getAnnotationType();
/* 348 */     Map<Symbol.MethodSymbol, Attribute> map = compound.getElementValues();
/*     */
/*     */
/* 351 */     for (ExecutableElement executableElement : ElementFilter.methodsIn(declaredType.asElement().getEnclosedElements())) {
/* 352 */       Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol)executableElement;
/* 353 */       Attribute attribute = methodSymbol.getDefaultValue();
/* 354 */       if (attribute != null && !map.containsKey(methodSymbol)) {
/* 355 */         map.put(methodSymbol, attribute);
/*     */       }
/*     */     }
/* 358 */     return map;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public FilteredMemberList getAllMembers(TypeElement paramTypeElement) {
/* 365 */     Symbol symbol = cast(Symbol.class, paramTypeElement);
/* 366 */     Scope scope = symbol.members().dupUnshared();
/* 367 */     List list = this.types.closure(symbol.asType());
/* 368 */     for (Type type : list)
/* 369 */       addMembers(scope, type);
/* 370 */     return new FilteredMemberList(scope);
/*     */   }
/*     */
/*     */   private void addMembers(Scope paramScope, Type paramType) {
/*     */     Scope.Entry entry;
/* 375 */     label31: for (entry = (paramType.asElement().members()).elems; entry != null; entry = entry.sibling) {
/* 376 */       Scope.Entry entry1 = paramScope.lookup(entry.sym.getSimpleName());
/* 377 */       while (entry1.scope != null) {
/* 378 */         if (entry1.sym.kind == entry.sym.kind && (entry1.sym
/* 379 */           .flags() & 0x1000L) == 0L)
/*     */         {
/* 381 */           if (entry1.sym.getKind() == ElementKind.METHOD &&
/* 382 */             overrides((ExecutableElement)entry1.sym, (ExecutableElement)entry.sym, (TypeElement)paramType.asElement())) {
/*     */             continue label31;
/*     */           }
/*     */         }
/* 386 */         entry1 = entry1.next();
/*     */       }
/* 388 */       boolean bool1 = (entry.sym.getEnclosingElement() != paramScope.owner) ? true : false;
/* 389 */       ElementKind elementKind = entry.sym.getKind();
/* 390 */       boolean bool2 = (elementKind == ElementKind.CONSTRUCTOR || elementKind == ElementKind.INSTANCE_INIT || elementKind == ElementKind.STATIC_INIT) ? true : false;
/*     */
/*     */
/* 393 */       if (!bool1 || (!bool2 && entry.sym.isInheritedIn(paramScope.owner, this.types))) {
/* 394 */         paramScope.enter(entry.sym);
/*     */       }
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
/*     */   public List<Attribute.Compound> getAllAnnotationMirrors(Element paramElement) {
/* 407 */     Symbol symbol = cast(Symbol.class, paramElement);
/* 408 */     List<Attribute.Compound> list = symbol.getAnnotationMirrors();
/* 409 */     while (symbol.getKind() == ElementKind.CLASS) {
/* 410 */       Type type = ((Symbol.ClassSymbol)symbol).getSuperclass();
/* 411 */       if (!type.hasTag(TypeTag.CLASS) || type.isErroneous() || type.tsym == this.syms.objectType.tsym) {
/*     */         break;
/*     */       }
/*     */
/* 415 */       Symbol.TypeSymbol typeSymbol = type.tsym;
/* 416 */       List<Attribute.Compound> list1 = list;
/* 417 */       List list2 = typeSymbol.getAnnotationMirrors();
/* 418 */       for (Attribute.Compound compound : list2) {
/* 419 */         if (isInherited(compound.type) &&
/* 420 */           !containsAnnoOfType(list1, compound.type)) {
/* 421 */           list = list.prepend(compound);
/*     */         }
/*     */       }
/*     */     }
/* 425 */     return list;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean isInherited(Type paramType) {
/* 432 */     return (paramType.tsym.attribute((Symbol)this.syms.inheritedType.tsym) != null);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static boolean containsAnnoOfType(List<Attribute.Compound> paramList, Type paramType) {
/* 441 */     for (Attribute.Compound compound : paramList) {
/* 442 */       if (compound.type.tsym == paramType.tsym)
/* 443 */         return true;
/*     */     }
/* 445 */     return false;
/*     */   }
/*     */
/*     */   public boolean hides(Element paramElement1, Element paramElement2) {
/* 449 */     Symbol symbol1 = cast(Symbol.class, paramElement1);
/* 450 */     Symbol symbol2 = cast(Symbol.class, paramElement2);
/*     */
/*     */
/*     */
/* 454 */     if (symbol1 == symbol2 || symbol1.kind != symbol2.kind || symbol1.name != symbol2.name)
/*     */     {
/*     */
/* 457 */       return false;
/*     */     }
/*     */
/*     */
/*     */
/* 462 */     if (symbol1.kind == 16 && (
/* 463 */       !symbol1.isStatic() ||
/* 464 */       !this.types.isSubSignature(symbol1.type, symbol2.type))) {
/* 465 */       return false;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 472 */     Symbol.ClassSymbol classSymbol1 = symbol1.owner.enclClass();
/* 473 */     Symbol.ClassSymbol classSymbol2 = symbol2.owner.enclClass();
/* 474 */     if (classSymbol1 == null || classSymbol2 == null ||
/* 475 */       !classSymbol1.isSubClass((Symbol)classSymbol2, this.types)) {
/* 476 */       return false;
/*     */     }
/*     */
/*     */
/*     */
/* 481 */     return symbol2.isInheritedIn((Symbol)classSymbol1, this.types);
/*     */   }
/*     */
/*     */
/*     */   public boolean overrides(ExecutableElement paramExecutableElement1, ExecutableElement paramExecutableElement2, TypeElement paramTypeElement) {
/* 486 */     Symbol.MethodSymbol methodSymbol1 = cast(Symbol.MethodSymbol.class, paramExecutableElement1);
/* 487 */     Symbol.MethodSymbol methodSymbol2 = cast(Symbol.MethodSymbol.class, paramExecutableElement2);
/* 488 */     Symbol.ClassSymbol classSymbol = cast(Symbol.ClassSymbol.class, paramTypeElement);
/*     */
/* 490 */     return (methodSymbol1.name == methodSymbol2.name && methodSymbol1 != methodSymbol2 &&
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 497 */       !methodSymbol1.isStatic() && methodSymbol2
/*     */
/*     */
/* 500 */       .isMemberOf((Symbol.TypeSymbol)classSymbol, this.types) && methodSymbol1
/*     */
/*     */
/* 503 */       .overrides((Symbol)methodSymbol2, (Symbol.TypeSymbol)classSymbol, this.types, false));
/*     */   }
/*     */
/*     */   public String getConstantExpression(Object paramObject) {
/* 507 */     return Constants.format(paramObject);
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
/*     */   public void printElements(Writer paramWriter, Element... paramVarArgs) {
/* 520 */     for (Element element : paramVarArgs)
/* 521 */       ((PrintingProcessor.PrintingElementVisitor)(new PrintingProcessor.PrintingElementVisitor(paramWriter, this)).visit(element)).flush();
/*     */   }
/*     */
/*     */   public Name getName(CharSequence paramCharSequence) {
/* 525 */     return this.names.fromString(paramCharSequence.toString());
/*     */   }
/*     */
/*     */
/*     */   public boolean isFunctionalInterface(TypeElement paramTypeElement) {
/* 530 */     if (paramTypeElement.getKind() != ElementKind.INTERFACE) {
/* 531 */       return false;
/*     */     }
/* 533 */     Symbol.TypeSymbol typeSymbol = cast(Symbol.TypeSymbol.class, paramTypeElement);
/* 534 */     return this.types.isFunctionalInterface(typeSymbol);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Pair<JCTree, JCTree.JCCompilationUnit> getTreeAndTopLevel(Element paramElement) {
/* 543 */     Symbol symbol = cast(Symbol.class, paramElement);
/* 544 */     Env<AttrContext> env = getEnterEnv(symbol);
/* 545 */     if (env == null)
/* 546 */       return null;
/* 547 */     JCTree jCTree = TreeInfo.declarationFor(symbol, env.tree);
/* 548 */     if (jCTree == null || env.toplevel == null)
/* 549 */       return null;
/* 550 */     return new Pair(jCTree, env.toplevel);
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
/*     */   public Pair<JCTree, JCTree.JCCompilationUnit> getTreeAndTopLevel(Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue) {
/* 564 */     if (paramElement == null) {
/* 565 */       return null;
/*     */     }
/* 567 */     Pair<JCTree, JCTree.JCCompilationUnit> pair = getTreeAndTopLevel(paramElement);
/* 568 */     if (pair == null) {
/* 569 */       return null;
/*     */     }
/* 571 */     if (paramAnnotationMirror == null) {
/* 572 */       return pair;
/*     */     }
/* 574 */     JCTree jCTree = matchAnnoToTree(paramAnnotationMirror, paramElement, (JCTree)pair.fst);
/* 575 */     if (jCTree == null) {
/* 576 */       return pair;
/*     */     }
/*     */
/*     */
/*     */
/* 581 */     return new Pair(jCTree, pair.snd);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Env<AttrContext> getEnterEnv(Symbol paramSymbol) {
/* 591 */     Symbol.TypeSymbol typeSymbol = (Symbol.TypeSymbol)((paramSymbol.kind != 1) ? paramSymbol.enclClass() : paramSymbol);
/*     */
/* 593 */     return (typeSymbol != null) ? this.enter
/* 594 */       .getEnv(typeSymbol) : null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static <T> T cast(Class<T> paramClass, Object paramObject) {
/* 604 */     if (!paramClass.isInstance(paramObject))
/* 605 */       throw new IllegalArgumentException(paramObject.toString());
/* 606 */     return paramClass.cast(paramObject);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\model\JavacElements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
