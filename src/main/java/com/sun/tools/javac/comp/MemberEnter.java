/*      */ package com.sun.tools.javac.comp;
/*      */
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.DeferredLintHandler;
/*      */ import com.sun.tools.javac.code.Kinds;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeAnnotations;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.jvm.ClassReader;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.FatalError;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.tools.JavaFileObject;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ public class MemberEnter
/*      */   extends JCTree.Visitor
/*      */   implements Symbol.Completer
/*      */ {
/*   65 */   protected static final Context.Key<MemberEnter> memberEnterKey = new Context.Key(); static final boolean checkClash = true;
/*      */   private final Names names;
/*      */   private final Enter enter;
/*      */   private final Log log;
/*      */   private final Check chk;
/*      */   private final Attr attr;
/*      */   private final Symtab syms;
/*      */   private final TreeMaker make;
/*      */   private final ClassReader reader;
/*      */   private final Todo todo;
/*      */   private final Annotate annotate;
/*      */   private final TypeAnnotations typeAnnotations;
/*      */   private final Types types;
/*      */   private final JCDiagnostic.Factory diags;
/*      */   private final Source source;
/*      */   private final Target target;
/*      */   private final DeferredLintHandler deferredLintHandler;
/*      */   private final Lint lint;
/*      */   private final TypeEnvs typeEnvs;
/*      */   boolean allowTypeAnnos;
/*      */   boolean allowRepeatedAnnos;
/*      */   ListBuffer<Env<AttrContext>> halfcompleted;
/*      */   boolean isFirst;
/*      */   boolean completionEnabled;
/*      */   protected Env<AttrContext> env;
/*      */
/*      */   public static MemberEnter instance(Context paramContext) {
/*   92 */     MemberEnter memberEnter = (MemberEnter)paramContext.get(memberEnterKey);
/*   93 */     if (memberEnter == null)
/*   94 */       memberEnter = new MemberEnter(paramContext);
/*   95 */     return memberEnter;
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
/*      */   protected MemberEnter(Context paramContext) {
/*  131 */     this.halfcompleted = new ListBuffer();
/*      */
/*      */
/*      */
/*      */
/*  136 */     this.isFirst = true;
/*      */
/*      */
/*      */
/*      */
/*      */
/*  142 */     this.completionEnabled = true; paramContext.put(memberEnterKey, this); this.names = Names.instance(paramContext); this.enter = Enter.instance(paramContext); this.log = Log.instance(paramContext); this.chk = Check.instance(paramContext); this.attr = Attr.instance(paramContext); this.syms = Symtab.instance(paramContext);
/*      */     this.make = TreeMaker.instance(paramContext);
/*      */     this.reader = ClassReader.instance(paramContext);
/*      */     this.todo = Todo.instance(paramContext);
/*      */     this.annotate = Annotate.instance(paramContext);
/*      */     this.typeAnnotations = TypeAnnotations.instance(paramContext);
/*      */     this.types = Types.instance(paramContext);
/*      */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*      */     this.source = Source.instance(paramContext);
/*      */     this.target = Target.instance(paramContext);
/*      */     this.deferredLintHandler = DeferredLintHandler.instance(paramContext);
/*      */     this.lint = Lint.instance(paramContext);
/*      */     this.typeEnvs = TypeEnvs.instance(paramContext);
/*      */     this.allowTypeAnnos = this.source.allowTypeAnnotations();
/*  156 */     this.allowRepeatedAnnos = this.source.allowRepeatedAnnotations(); } private void importAll(int paramInt, Symbol.TypeSymbol paramTypeSymbol, Env<AttrContext> paramEnv) { if (paramTypeSymbol.kind == 1 && (paramTypeSymbol.members()).elems == null && !paramTypeSymbol.exists()) {
/*      */
/*  158 */       if (((Symbol.PackageSymbol)paramTypeSymbol).fullname.equals(this.names.java_lang)) {
/*  159 */         JCDiagnostic jCDiagnostic = this.diags.fragment("fatal.err.no.java.lang", new Object[0]);
/*  160 */         throw new FatalError(jCDiagnostic);
/*      */       }
/*  162 */       this.log.error(JCDiagnostic.DiagnosticFlag.RESOLVE_ERROR, paramInt, "doesnt.exist", new Object[] { paramTypeSymbol });
/*      */     }
/*      */
/*  165 */     paramEnv.toplevel.starImportScope.importAll(paramTypeSymbol.members()); }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void importStaticAll(int paramInt, final Symbol.TypeSymbol tsym, Env<AttrContext> paramEnv) {
/*  176 */     final JavaFileObject sourcefile = paramEnv.toplevel.sourcefile;
/*  177 */     final Scope.StarImportScope toScope = paramEnv.toplevel.starImportScope;
/*  178 */     final Symbol.PackageSymbol packge = paramEnv.toplevel.packge;
/*  179 */     final Symbol.TypeSymbol origin = tsym;
/*      */
/*      */
/*  182 */     (new Object() {
/*  183 */         Set<Symbol> processed = new HashSet<>();
/*      */         void importFrom(Symbol.TypeSymbol param1TypeSymbol) {
/*  185 */           if (param1TypeSymbol == null || !this.processed.add(param1TypeSymbol)) {
/*      */             return;
/*      */           }
/*      */
/*  189 */           importFrom((MemberEnter.this.types.supertype(param1TypeSymbol.type)).tsym);
/*  190 */           for (Type type : MemberEnter.this.types.interfaces(param1TypeSymbol.type)) {
/*  191 */             importFrom(type.tsym);
/*      */           }
/*  193 */           Scope scope = param1TypeSymbol.members();
/*  194 */           for (Scope.Entry entry = scope.elems; entry != null; entry = entry.sibling) {
/*  195 */             Symbol symbol = entry.sym;
/*  196 */             if (symbol.kind == 2 && (symbol
/*  197 */               .flags() & 0x8L) != 0L && MemberEnter.this
/*  198 */               .staticImportAccessible(symbol, packge) && symbol
/*  199 */               .isMemberOf(origin, MemberEnter.this.types) &&
/*  200 */               !toScope.includes(symbol))
/*  201 */               toScope.enter(symbol, scope, origin.members(), true);
/*      */           }
/*      */         }
/*  204 */       }).importFrom(tsym);
/*      */
/*      */
/*  207 */     this.annotate.earlier(new Annotate.Worker() {
/*  208 */           Set<Symbol> processed = new HashSet<>();
/*      */
/*      */           public String toString() {
/*  211 */             return "import static " + tsym + ".* in " + sourcefile;
/*      */           }
/*      */           void importFrom(Symbol.TypeSymbol param1TypeSymbol) {
/*  214 */             if (param1TypeSymbol == null || !this.processed.add(param1TypeSymbol)) {
/*      */               return;
/*      */             }
/*      */
/*  218 */             importFrom((MemberEnter.this.types.supertype(param1TypeSymbol.type)).tsym);
/*  219 */             for (Type type : MemberEnter.this.types.interfaces(param1TypeSymbol.type)) {
/*  220 */               importFrom(type.tsym);
/*      */             }
/*  222 */             Scope scope = param1TypeSymbol.members();
/*  223 */             for (Scope.Entry entry = scope.elems; entry != null; entry = entry.sibling) {
/*  224 */               Symbol symbol = entry.sym;
/*  225 */               if (symbol.isStatic() && symbol.kind != 2 && MemberEnter.this
/*  226 */                 .staticImportAccessible(symbol, packge) &&
/*  227 */                 !toScope.includes(symbol) && symbol
/*  228 */                 .isMemberOf(origin, MemberEnter.this.types))
/*  229 */                 toScope.enter(symbol, scope, origin.members(), true);
/*      */             }
/*      */           }
/*      */
/*      */           public void run() {
/*  234 */             importFrom(tsym);
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */
/*      */   boolean staticImportAccessible(Symbol paramSymbol, Symbol.PackageSymbol paramPackageSymbol) {
/*  241 */     int i = (int)(paramSymbol.flags() & 0x7L);
/*  242 */     switch (i)
/*      */
/*      */     { default:
/*  245 */         return true;
/*      */       case 2:
/*  247 */         return false;
/*      */       case 0:
/*      */       case 4:
/*  250 */         break; }  return (paramSymbol.packge() == paramPackageSymbol);
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
/*      */   private void importNamedStatic(final JCDiagnostic.DiagnosticPosition pos, final Symbol.TypeSymbol tsym, final Name name, final Env<AttrContext> env) {
/*  265 */     if (tsym.kind != 2) {
/*  266 */       this.log.error(JCDiagnostic.DiagnosticFlag.RECOVERABLE, pos, "static.imp.only.classes.and.interfaces", new Object[0]);
/*      */
/*      */       return;
/*      */     }
/*  270 */     final Scope.ImportScope toScope = env.toplevel.namedImportScope;
/*  271 */     final Symbol.PackageSymbol packge = env.toplevel.packge;
/*  272 */     final Symbol.TypeSymbol origin = tsym;
/*      */
/*      */
/*  275 */     (new Object() {
/*  276 */         Set<Symbol> processed = new HashSet<>();
/*      */         void importFrom(Symbol.TypeSymbol param1TypeSymbol) {
/*  278 */           if (param1TypeSymbol == null || !this.processed.add(param1TypeSymbol)) {
/*      */             return;
/*      */           }
/*      */
/*  282 */           importFrom((MemberEnter.this.types.supertype(param1TypeSymbol.type)).tsym);
/*  283 */           for (Type type : MemberEnter.this.types.interfaces(param1TypeSymbol.type)) {
/*  284 */             importFrom(type.tsym);
/*      */           }
/*  286 */           Scope.Entry entry = param1TypeSymbol.members().lookup(name);
/*  287 */           for (; entry.scope != null;
/*  288 */             entry = entry.next()) {
/*  289 */             Symbol symbol = entry.sym;
/*  290 */             if (symbol.isStatic() && symbol.kind == 2 && MemberEnter.this
/*      */
/*  292 */               .staticImportAccessible(symbol, packge) && symbol
/*  293 */               .isMemberOf(origin, MemberEnter.this.types) && MemberEnter.this
/*  294 */               .chk.checkUniqueStaticImport(pos, symbol, toScope))
/*  295 */               toScope.enter(symbol, symbol.owner.members(), origin.members(), true);
/*      */           }
/*      */         }
/*  298 */       }).importFrom(tsym);
/*      */
/*      */
/*  301 */     this.annotate.earlier(new Annotate.Worker() {
/*  302 */           Set<Symbol> processed = new HashSet<>();
/*      */           boolean found = false;
/*      */
/*      */           public String toString() {
/*  306 */             return "import static " + tsym + "." + name;
/*      */           }
/*      */           void importFrom(Symbol.TypeSymbol param1TypeSymbol) {
/*  309 */             if (param1TypeSymbol == null || !this.processed.add(param1TypeSymbol)) {
/*      */               return;
/*      */             }
/*      */
/*  313 */             importFrom((MemberEnter.this.types.supertype(param1TypeSymbol.type)).tsym);
/*  314 */             for (Type type : MemberEnter.this.types.interfaces(param1TypeSymbol.type)) {
/*  315 */               importFrom(type.tsym);
/*      */             }
/*  317 */             Scope.Entry entry = param1TypeSymbol.members().lookup(name);
/*  318 */             for (; entry.scope != null;
/*  319 */               entry = entry.next()) {
/*  320 */               Symbol symbol = entry.sym;
/*  321 */               if (symbol.isStatic() && MemberEnter.this
/*  322 */                 .staticImportAccessible(symbol, packge) && symbol
/*  323 */                 .isMemberOf(origin, MemberEnter.this.types)) {
/*  324 */                 this.found = true;
/*  325 */                 if (symbol.kind != 2)
/*  326 */                   toScope.enter(symbol, symbol.owner.members(), origin.members(), true);
/*      */               }
/*      */             }
/*      */           }
/*      */
/*      */           public void run() {
/*  332 */             JavaFileObject javaFileObject = MemberEnter.this.log.useSource(env.toplevel.sourcefile);
/*      */             try {
/*  334 */               importFrom(tsym);
/*  335 */               if (!this.found) {
/*  336 */                 MemberEnter.this.log.error(pos, "cant.resolve.location", new Object[] { Kinds.KindName.STATIC, this.val$name,
/*      */
/*  338 */                       List.nil(), List.nil(),
/*  339 */                       Kinds.typeKindName(this.val$tsym.type), this.val$tsym.type });
/*      */               }
/*      */             } finally {
/*      */
/*  343 */               MemberEnter.this.log.useSource(javaFileObject);
/*      */             }
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void importNamed(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Env<AttrContext> paramEnv) {
/*  355 */     if (paramSymbol.kind == 2 && this.chk
/*  356 */       .checkUniqueImport(paramDiagnosticPosition, paramSymbol, (Scope)paramEnv.toplevel.namedImportScope)) {
/*  357 */       paramEnv.toplevel.namedImportScope.enter(paramSymbol, paramSymbol.owner.members());
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
/*      */   Type signature(Symbol.MethodSymbol paramMethodSymbol, List<JCTree.JCTypeParameter> paramList, List<JCTree.JCVariableDecl> paramList1, JCTree paramJCTree, JCTree.JCVariableDecl paramJCVariableDecl, List<JCTree.JCExpression> paramList2, Env<AttrContext> paramEnv) {
/*      */     Type type;
/*  379 */     List<Type> list = this.enter.classEnter(paramList, paramEnv);
/*  380 */     this.attr.attribTypeVariables(paramList, paramEnv);
/*      */
/*      */
/*  383 */     ListBuffer listBuffer1 = new ListBuffer(); List<JCTree.JCVariableDecl> list1;
/*  384 */     for (list1 = paramList1; list1.nonEmpty(); list1 = list1.tail) {
/*  385 */       memberEnter((JCTree)list1.head, paramEnv);
/*  386 */       listBuffer1.append(((JCTree.JCVariableDecl)list1.head).vartype.type);
/*      */     }
/*      */
/*      */
/*  390 */     list1 = (paramJCTree == null) ? (List<JCTree.JCVariableDecl>)this.syms.voidType : (List<JCTree.JCVariableDecl>)this.attr.attribType(paramJCTree, paramEnv);
/*      */
/*      */
/*      */
/*  394 */     if (paramJCVariableDecl != null) {
/*  395 */       memberEnter((JCTree)paramJCVariableDecl, paramEnv);
/*  396 */       type = paramJCVariableDecl.vartype.type;
/*      */     } else {
/*  398 */       type = null;
/*      */     }
/*      */
/*      */
/*  402 */     ListBuffer listBuffer2 = new ListBuffer();
/*  403 */     for (List<JCTree.JCExpression> list2 = paramList2; list2.nonEmpty(); list2 = list2.tail) {
/*  404 */       Type type1 = this.attr.attribType((JCTree)list2.head, paramEnv);
/*  405 */       if (!type1.hasTag(TypeTag.TYPEVAR)) {
/*  406 */         type1 = this.chk.checkClassType(((JCTree.JCExpression)list2.head).pos(), type1);
/*  407 */       } else if (type1.tsym.owner == paramMethodSymbol) {
/*      */
/*  409 */         type1.tsym.flags_field |= 0x800000000000L;
/*      */       }
/*  411 */       listBuffer2.append(type1);
/*      */     }
/*      */
/*      */
/*  415 */     Type.MethodType methodType = new Type.MethodType(listBuffer1.toList(), (Type)list1, listBuffer2.toList(), (Symbol.TypeSymbol)this.syms.methodClass);
/*      */
/*  417 */     methodType.recvtype = type;
/*      */
/*  419 */     return list.isEmpty() ? (Type)methodType : (Type)new Type.ForAll(list, (Type)methodType);
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
/*      */   protected void memberEnter(JCTree paramJCTree, Env<AttrContext> paramEnv) {
/*  434 */     Env<AttrContext> env = this.env;
/*      */     try {
/*  436 */       this.env = paramEnv;
/*  437 */       paramJCTree.accept(this);
/*  438 */     } catch (Symbol.CompletionFailure completionFailure) {
/*  439 */       this.chk.completionError(paramJCTree.pos(), completionFailure);
/*      */     } finally {
/*  441 */       this.env = env;
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   void memberEnter(List<? extends JCTree> paramList, Env<AttrContext> paramEnv) {
/*  448 */     for (List<? extends JCTree> list = paramList; list.nonEmpty(); list = list.tail) {
/*  449 */       memberEnter((JCTree)list.head, paramEnv);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   void finishClass(JCTree.JCClassDecl paramJCClassDecl, Env<AttrContext> paramEnv) {
/*  455 */     if ((paramJCClassDecl.mods.flags & 0x4000L) != 0L && (
/*  456 */       (this.types.supertype(paramJCClassDecl.sym.type)).tsym.flags() & 0x4000L) == 0L) {
/*  457 */       addEnumMembers(paramJCClassDecl, paramEnv);
/*      */     }
/*  459 */     memberEnter(paramJCClassDecl.defs, paramEnv);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private void addEnumMembers(JCTree.JCClassDecl paramJCClassDecl, Env<AttrContext> paramEnv) {
/*  466 */     JCTree.JCExpression jCExpression = this.make.Type((Type)new Type.ArrayType(paramJCClassDecl.sym.type, (Symbol.TypeSymbol)this.syms.arrayClass));
/*      */
/*      */
/*      */
/*  470 */     JCTree.JCMethodDecl jCMethodDecl1 = this.make.MethodDef(this.make.Modifiers(9L), this.names.values, jCExpression,
/*      */
/*      */
/*  473 */         List.nil(),
/*  474 */         List.nil(),
/*  475 */         List.nil(), null, null);
/*      */
/*      */
/*  478 */     memberEnter((JCTree)jCMethodDecl1, paramEnv);
/*      */
/*      */
/*      */
/*  482 */     JCTree.JCMethodDecl jCMethodDecl2 = this.make.MethodDef(this.make.Modifiers(9L), this.names.valueOf, this.make
/*      */
/*  484 */         .Type(paramJCClassDecl.sym.type),
/*  485 */         List.nil(),
/*  486 */         List.of(this.make.VarDef(this.make.Modifiers(8589967360L), this.names
/*      */
/*  488 */             .fromString("name"), this.make
/*  489 */             .Type(this.syms.stringType), null)),
/*  490 */         List.nil(), null, null);
/*      */
/*      */
/*  493 */     memberEnter((JCTree)jCMethodDecl2, paramEnv);
/*      */   }
/*      */
/*      */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/*  497 */     if (paramJCCompilationUnit.starImportScope.elems != null) {
/*      */       return;
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*  504 */     if (paramJCCompilationUnit.pid != null) {
/*  505 */       Symbol.PackageSymbol packageSymbol = paramJCCompilationUnit.packge;
/*  506 */       while (((Symbol)packageSymbol).owner != this.syms.rootPackage) {
/*  507 */         ((Symbol)packageSymbol).owner.complete();
/*  508 */         if (this.syms.classes.get(packageSymbol.getQualifiedName()) != null) {
/*  509 */           this.log.error(paramJCCompilationUnit.pos, "pkg.clashes.with.class.of.same.name", new Object[] { packageSymbol });
/*      */         }
/*      */
/*      */
/*  513 */         Symbol symbol = ((Symbol)packageSymbol).owner;
/*      */       }
/*      */     }
/*      */
/*      */
/*  518 */     annotateLater(paramJCCompilationUnit.packageAnnotations, this.env, (Symbol)paramJCCompilationUnit.packge, null);
/*      */
/*  520 */     JCDiagnostic.DiagnosticPosition diagnosticPosition = this.deferredLintHandler.immediate();
/*  521 */     Lint lint = this.chk.setLint(this.lint);
/*      */
/*      */
/*      */     try {
/*  525 */       importAll(paramJCCompilationUnit.pos, (Symbol.TypeSymbol)this.reader.enterPackage(this.names.java_lang), this.env);
/*      */
/*      */
/*  528 */       memberEnter(paramJCCompilationUnit.defs, this.env);
/*      */     } finally {
/*  530 */       this.chk.setLint(lint);
/*  531 */       this.deferredLintHandler.setPos(diagnosticPosition);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void visitImport(JCTree.JCImport paramJCImport) {
/*  537 */     JCTree.JCFieldAccess jCFieldAccess = (JCTree.JCFieldAccess)paramJCImport.qualid;
/*  538 */     Name name = TreeInfo.name((JCTree)jCFieldAccess);
/*      */
/*      */
/*      */
/*  542 */     Env<AttrContext> env = this.env.dup((JCTree)paramJCImport);
/*      */
/*  544 */     Symbol.TypeSymbol typeSymbol = (this.attr.attribImportQualifier(paramJCImport, env)).tsym;
/*  545 */     if (name == this.names.asterisk) {
/*      */
/*  547 */       this.chk.checkCanonical((JCTree)jCFieldAccess.selected);
/*  548 */       if (paramJCImport.staticImport) {
/*  549 */         importStaticAll(paramJCImport.pos, typeSymbol, this.env);
/*      */       } else {
/*  551 */         importAll(paramJCImport.pos, typeSymbol, this.env);
/*      */       }
/*      */
/*  554 */     } else if (paramJCImport.staticImport) {
/*  555 */       importNamedStatic(paramJCImport.pos(), typeSymbol, name, env);
/*  556 */       this.chk.checkCanonical((JCTree)jCFieldAccess.selected);
/*      */     } else {
/*  558 */       Symbol.TypeSymbol typeSymbol1 = (attribImportType((JCTree)jCFieldAccess, env)).tsym;
/*  559 */       this.chk.checkCanonical((JCTree)jCFieldAccess);
/*  560 */       importNamed(paramJCImport.pos(), (Symbol)typeSymbol1, this.env);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/*  566 */     Scope scope = this.enter.enterScope(this.env);
/*  567 */     Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(0L, paramJCMethodDecl.name, null, scope.owner);
/*  568 */     methodSymbol.flags_field = this.chk.checkFlags(paramJCMethodDecl.pos(), paramJCMethodDecl.mods.flags, (Symbol)methodSymbol, (JCTree)paramJCMethodDecl);
/*  569 */     paramJCMethodDecl.sym = methodSymbol;
/*      */
/*      */
/*  572 */     if ((paramJCMethodDecl.mods.flags & 0x80000000000L) != 0L) {
/*  573 */       (methodSymbol.enclClass()).flags_field |= 0x80000000000L;
/*      */     }
/*      */
/*  576 */     Env<AttrContext> env = methodEnv(paramJCMethodDecl, this.env);
/*      */
/*  578 */     JCDiagnostic.DiagnosticPosition diagnosticPosition = this.deferredLintHandler.setPos(paramJCMethodDecl.pos());
/*      */
/*      */     try {
/*  581 */       methodSymbol.type = signature(methodSymbol, paramJCMethodDecl.typarams, paramJCMethodDecl.params, (JCTree)paramJCMethodDecl.restype, paramJCMethodDecl.recvparam, paramJCMethodDecl.thrown, env);
/*      */
/*      */     }
/*      */     finally {
/*      */
/*  586 */       this.deferredLintHandler.setPos(diagnosticPosition);
/*      */     }
/*      */
/*  589 */     if (this.types.isSignaturePolymorphic(methodSymbol)) {
/*  590 */       methodSymbol.flags_field |= 0x400000000000L;
/*      */     }
/*      */
/*      */
/*  594 */     ListBuffer listBuffer = new ListBuffer();
/*  595 */     JCTree.JCVariableDecl jCVariableDecl = null;
/*  596 */     for (List list = paramJCMethodDecl.params; list.nonEmpty(); list = list.tail) {
/*  597 */       JCTree.JCVariableDecl jCVariableDecl1 = jCVariableDecl = (JCTree.JCVariableDecl)list.head;
/*  598 */       listBuffer.append(Assert.checkNonNull(jCVariableDecl1.sym));
/*      */     }
/*  600 */     methodSymbol.params = listBuffer.toList();
/*      */
/*      */
/*  603 */     if (jCVariableDecl != null && (jCVariableDecl.mods.flags & 0x400000000L) != 0L) {
/*  604 */       methodSymbol.flags_field |= 0x400000000L;
/*      */     }
/*  606 */     ((AttrContext)env.info).scope.leave();
/*  607 */     if (this.chk.checkUnique(paramJCMethodDecl.pos(), (Symbol)methodSymbol, scope)) {
/*  608 */       scope.enter((Symbol)methodSymbol);
/*      */     }
/*      */
/*  611 */     annotateLater(paramJCMethodDecl.mods.annotations, env, (Symbol)methodSymbol, paramJCMethodDecl.pos());
/*      */
/*      */
/*  614 */     typeAnnotate((JCTree)paramJCMethodDecl, env, (Symbol)methodSymbol, paramJCMethodDecl.pos());
/*      */
/*  616 */     if (paramJCMethodDecl.defaultValue != null) {
/*  617 */       annotateDefaultValueLater(paramJCMethodDecl.defaultValue, env, methodSymbol);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Env<AttrContext> methodEnv(JCTree.JCMethodDecl paramJCMethodDecl, Env<AttrContext> paramEnv) {
/*  626 */     Env<AttrContext> env = paramEnv.dup((JCTree)paramJCMethodDecl, ((AttrContext)paramEnv.info).dup(((AttrContext)paramEnv.info).scope.dupUnshared()));
/*  627 */     env.enclMethod = paramJCMethodDecl;
/*  628 */     ((AttrContext)env.info).scope.owner = (Symbol)paramJCMethodDecl.sym;
/*  629 */     if (paramJCMethodDecl.sym.type != null) {
/*      */
/*  631 */       this.attr.getClass(); ((AttrContext)env.info).returnResult = new Attr.ResultInfo(this.attr, 12, paramJCMethodDecl.sym.type.getReturnType());
/*      */     }
/*  633 */     if ((paramJCMethodDecl.mods.flags & 0x8L) != 0L) ((AttrContext)env.info).staticLevel++;
/*  634 */     return env;
/*      */   }
/*      */
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/*  638 */     Env<AttrContext> env = this.env;
/*  639 */     if ((paramJCVariableDecl.mods.flags & 0x8L) != 0L || (((AttrContext)this.env.info).scope.owner
/*  640 */       .flags() & 0x200L) != 0L) {
/*  641 */       env = this.env.dup((JCTree)paramJCVariableDecl, ((AttrContext)this.env.info).dup());
/*  642 */       ((AttrContext)env.info).staticLevel++;
/*      */     }
/*  644 */     JCDiagnostic.DiagnosticPosition diagnosticPosition = this.deferredLintHandler.setPos(paramJCVariableDecl.pos());
/*      */     try {
/*  646 */       if (TreeInfo.isEnumInit((JCTree)paramJCVariableDecl)) {
/*  647 */         this.attr.attribIdentAsEnumType(env, (JCTree.JCIdent)paramJCVariableDecl.vartype);
/*      */       } else {
/*  649 */         this.attr.attribType((JCTree)paramJCVariableDecl.vartype, env);
/*  650 */         if (TreeInfo.isReceiverParam((JCTree)paramJCVariableDecl))
/*  651 */           checkReceiver(paramJCVariableDecl, env);
/*      */       }
/*      */     } finally {
/*  654 */       this.deferredLintHandler.setPos(diagnosticPosition);
/*      */     }
/*      */
/*  657 */     if ((paramJCVariableDecl.mods.flags & 0x400000000L) != 0L) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  664 */       Type.ArrayType arrayType = (Type.ArrayType)paramJCVariableDecl.vartype.type.unannotatedType();
/*  665 */       paramJCVariableDecl.vartype.type = (Type)arrayType.makeVarargs();
/*      */     }
/*  667 */     Scope scope = this.enter.enterScope(this.env);
/*  668 */     Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(0L, paramJCVariableDecl.name, paramJCVariableDecl.vartype.type, scope.owner);
/*      */
/*  670 */     varSymbol.flags_field = this.chk.checkFlags(paramJCVariableDecl.pos(), paramJCVariableDecl.mods.flags, (Symbol)varSymbol, (JCTree)paramJCVariableDecl);
/*  671 */     paramJCVariableDecl.sym = varSymbol;
/*  672 */     if (paramJCVariableDecl.init != null) {
/*  673 */       varSymbol.flags_field |= 0x40000L;
/*  674 */       if ((varSymbol.flags_field & 0x10L) != 0L &&
/*  675 */         needsLazyConstValue((JCTree)paramJCVariableDecl.init)) {
/*  676 */         Env<AttrContext> env1 = getInitEnv(paramJCVariableDecl, this.env);
/*  677 */         ((AttrContext)env1.info).enclVar = (Symbol)varSymbol;
/*  678 */         varSymbol.setLazyConstValue(initEnv(paramJCVariableDecl, env1), this.attr, paramJCVariableDecl);
/*      */       }
/*      */     }
/*  681 */     if (this.chk.checkUnique(paramJCVariableDecl.pos(), (Symbol)varSymbol, scope)) {
/*  682 */       this.chk.checkTransparentVar(paramJCVariableDecl.pos(), varSymbol, scope);
/*  683 */       scope.enter((Symbol)varSymbol);
/*      */     }
/*  685 */     annotateLater(paramJCVariableDecl.mods.annotations, env, (Symbol)varSymbol, paramJCVariableDecl.pos());
/*  686 */     typeAnnotate((JCTree)paramJCVariableDecl.vartype, this.env, (Symbol)varSymbol, paramJCVariableDecl.pos());
/*  687 */     varSymbol.pos = paramJCVariableDecl.pos;
/*      */   }
/*      */
/*      */   void checkType(JCTree paramJCTree, Type paramType, String paramString) {
/*  691 */     if (!paramJCTree.type.isErroneous() && !this.types.isSameType(paramJCTree.type, paramType))
/*  692 */       this.log.error((JCDiagnostic.DiagnosticPosition)paramJCTree, paramString, new Object[] { paramType, paramJCTree.type });
/*      */   }
/*      */
/*      */   void checkReceiver(JCTree.JCVariableDecl paramJCVariableDecl, Env<AttrContext> paramEnv) {
/*  696 */     this.attr.attribExpr((JCTree)paramJCVariableDecl.nameexpr, paramEnv);
/*  697 */     Symbol.MethodSymbol methodSymbol = paramEnv.enclMethod.sym;
/*  698 */     if (methodSymbol.isConstructor()) {
/*  699 */       Type type = methodSymbol.owner.owner.type;
/*  700 */       if (type.hasTag(TypeTag.METHOD))
/*      */       {
/*  702 */         type = methodSymbol.owner.owner.owner.type;
/*      */       }
/*  704 */       if (type.hasTag(TypeTag.CLASS)) {
/*  705 */         checkType((JCTree)paramJCVariableDecl.vartype, type, "incorrect.constructor.receiver.type");
/*  706 */         checkType((JCTree)paramJCVariableDecl.nameexpr, type, "incorrect.constructor.receiver.name");
/*      */       } else {
/*  708 */         this.log.error((JCDiagnostic.DiagnosticPosition)paramJCVariableDecl, "receiver.parameter.not.applicable.constructor.toplevel.class", new Object[0]);
/*      */       }
/*      */     } else {
/*  711 */       checkType((JCTree)paramJCVariableDecl.vartype, methodSymbol.owner.type, "incorrect.receiver.type");
/*  712 */       checkType((JCTree)paramJCVariableDecl.nameexpr, methodSymbol.owner.type, "incorrect.receiver.name");
/*      */     }
/*      */   }
/*      */
/*      */   public boolean needsLazyConstValue(JCTree paramJCTree) {
/*  717 */     InitTreeVisitor initTreeVisitor = new InitTreeVisitor();
/*  718 */     paramJCTree.accept(initTreeVisitor);
/*  719 */     return initTreeVisitor.result;
/*      */   }
/*      */
/*      */
/*      */   static class InitTreeVisitor
/*      */     extends JCTree.Visitor
/*      */   {
/*      */     private boolean result = true;
/*      */
/*      */
/*      */     public void visitTree(JCTree param1JCTree) {}
/*      */
/*      */
/*      */     public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/*  733 */       this.result = false;
/*      */     }
/*      */
/*      */
/*      */     public void visitNewArray(JCTree.JCNewArray param1JCNewArray) {
/*  738 */       this.result = false;
/*      */     }
/*      */
/*      */
/*      */     public void visitLambda(JCTree.JCLambda param1JCLambda) {
/*  743 */       this.result = false;
/*      */     }
/*      */
/*      */
/*      */     public void visitReference(JCTree.JCMemberReference param1JCMemberReference) {
/*  748 */       this.result = false;
/*      */     }
/*      */
/*      */
/*      */     public void visitApply(JCTree.JCMethodInvocation param1JCMethodInvocation) {
/*  753 */       this.result = false;
/*      */     }
/*      */
/*      */
/*      */     public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) {
/*  758 */       param1JCFieldAccess.selected.accept(this);
/*      */     }
/*      */
/*      */
/*      */     public void visitConditional(JCTree.JCConditional param1JCConditional) {
/*  763 */       param1JCConditional.cond.accept(this);
/*  764 */       param1JCConditional.truepart.accept(this);
/*  765 */       param1JCConditional.falsepart.accept(this);
/*      */     }
/*      */
/*      */
/*      */     public void visitParens(JCTree.JCParens param1JCParens) {
/*  770 */       param1JCParens.expr.accept(this);
/*      */     }
/*      */
/*      */
/*      */     public void visitTypeCast(JCTree.JCTypeCast param1JCTypeCast) {
/*  775 */       param1JCTypeCast.expr.accept(this);
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
/*      */   Env<AttrContext> initEnv(JCTree.JCVariableDecl paramJCVariableDecl, Env<AttrContext> paramEnv) {
/*  788 */     Env<AttrContext> env = paramEnv.dupto(new AttrContextEnv((JCTree)paramJCVariableDecl, ((AttrContext)paramEnv.info).dup()));
/*  789 */     if (paramJCVariableDecl.sym.owner.kind == 2) {
/*  790 */       ((AttrContext)env.info).scope = ((AttrContext)paramEnv.info).scope.dupUnshared();
/*  791 */       ((AttrContext)env.info).scope.owner = (Symbol)paramJCVariableDecl.sym;
/*      */     }
/*  793 */     if ((paramJCVariableDecl.mods.flags & 0x8L) != 0L || ((paramEnv.enclClass.sym
/*  794 */       .flags() & 0x200L) != 0L && paramEnv.enclMethod == null))
/*  795 */       ((AttrContext)env.info).staticLevel++;
/*  796 */     return env;
/*      */   }
/*      */
/*      */
/*      */
/*      */   public void visitTree(JCTree paramJCTree) {}
/*      */
/*      */
/*      */   public void visitErroneous(JCTree.JCErroneous paramJCErroneous) {
/*  805 */     if (paramJCErroneous.errs != null)
/*  806 */       memberEnter(paramJCErroneous.errs, this.env);
/*      */   }
/*      */
/*      */   public Env<AttrContext> getMethodEnv(JCTree.JCMethodDecl paramJCMethodDecl, Env<AttrContext> paramEnv) {
/*  810 */     Env<AttrContext> env = methodEnv(paramJCMethodDecl, paramEnv);
/*  811 */     ((AttrContext)env.info).lint = ((AttrContext)env.info).lint.augment((Symbol)paramJCMethodDecl.sym); List list;
/*  812 */     for (list = paramJCMethodDecl.typarams; list.nonEmpty(); list = list.tail)
/*  813 */       ((AttrContext)env.info).scope.enterIfAbsent((Symbol)((JCTree.JCTypeParameter)list.head).type.tsym);
/*  814 */     for (list = paramJCMethodDecl.params; list.nonEmpty(); list = list.tail)
/*  815 */       ((AttrContext)env.info).scope.enterIfAbsent((Symbol)((JCTree.JCVariableDecl)list.head).sym);
/*  816 */     return env;
/*      */   }
/*      */
/*      */   public Env<AttrContext> getInitEnv(JCTree.JCVariableDecl paramJCVariableDecl, Env<AttrContext> paramEnv) {
/*  820 */     return initEnv(paramJCVariableDecl, paramEnv);
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   Type attribImportType(JCTree paramJCTree, Env<AttrContext> paramEnv) {
/*  829 */     Assert.check(this.completionEnabled);
/*      */
/*      */
/*      */     try {
/*  833 */       this.completionEnabled = false;
/*  834 */       return this.attr.attribType(paramJCTree, paramEnv);
/*      */     } finally {
/*  836 */       this.completionEnabled = true;
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
/*      */   void annotateLater(final List<JCTree.JCAnnotation> annotations, final Env<AttrContext> localEnv, final Symbol s, final JCDiagnostic.DiagnosticPosition deferPos) {
/*  849 */     if (annotations.isEmpty()) {
/*      */       return;
/*      */     }
/*  852 */     if (s.kind != 1) {
/*  853 */       s.resetAnnotations();
/*      */     }
/*  855 */     this.annotate.normal(new Annotate.Worker()
/*      */         {
/*      */           public String toString() {
/*  858 */             return "annotate " + annotations + " onto " + s + " in " + s.owner;
/*      */           }
/*      */
/*      */
/*      */           public void run() {
/*  863 */             Assert.check((s.kind == 1 || s.annotationsPendingCompletion()));
/*  864 */             JavaFileObject javaFileObject = MemberEnter.this.log.useSource(localEnv.toplevel.sourcefile);
/*      */
/*      */
/*      */
/*  868 */             JCDiagnostic.DiagnosticPosition diagnosticPosition = (deferPos != null) ? MemberEnter.this.deferredLintHandler.setPos(deferPos) : MemberEnter.this.deferredLintHandler.immediate();
/*  869 */             Lint lint = (deferPos != null) ? null : MemberEnter.this.chk.setLint(MemberEnter.this.lint);
/*      */             try {
/*  871 */               if (s.hasAnnotations() && annotations
/*  872 */                 .nonEmpty())
/*  873 */                 MemberEnter.this.log.error(((JCTree.JCAnnotation)annotations.head).pos, "already.annotated", new Object[] {
/*      */
/*  875 */                       Kinds.kindName(this.val$s), this.val$s });
/*  876 */               MemberEnter.this.actualEnterAnnotations(annotations, localEnv, s);
/*      */             } finally {
/*  878 */               if (lint != null)
/*  879 */                 MemberEnter.this.chk.setLint(lint);
/*  880 */               MemberEnter.this.deferredLintHandler.setPos(diagnosticPosition);
/*  881 */               MemberEnter.this.log.useSource(javaFileObject);
/*      */             }
/*      */           }
/*      */         });
/*      */
/*  886 */     this.annotate.validate(new Annotate.Worker()
/*      */         {
/*      */           public void run() {
/*  889 */             JavaFileObject javaFileObject = MemberEnter.this.log.useSource(localEnv.toplevel.sourcefile);
/*      */             try {
/*  891 */               MemberEnter.this.chk.validateAnnotations(annotations, s);
/*      */             } finally {
/*  893 */               MemberEnter.this.log.useSource(javaFileObject);
/*      */             }
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private boolean hasDeprecatedAnnotation(List<JCTree.JCAnnotation> paramList) {
/*  904 */     for (List<JCTree.JCAnnotation> list = paramList; !list.isEmpty(); list = list.tail) {
/*  905 */       JCTree.JCAnnotation jCAnnotation = (JCTree.JCAnnotation)list.head;
/*  906 */       if (jCAnnotation.annotationType.type == this.syms.deprecatedType && jCAnnotation.args.isEmpty())
/*  907 */         return true;
/*      */     }
/*  909 */     return false;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private void actualEnterAnnotations(List<JCTree.JCAnnotation> paramList, Env<AttrContext> paramEnv, Symbol paramSymbol) {
/*  916 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*      */
/*  918 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*      */
/*      */
/*  921 */     for (List<JCTree.JCAnnotation> list = paramList; !list.isEmpty(); list = list.tail) {
/*  922 */       JCTree.JCAnnotation jCAnnotation = (JCTree.JCAnnotation)list.head;
/*  923 */       Attribute.Compound compound = this.annotate.enterAnnotation(jCAnnotation, this.syms.annotationType, paramEnv);
/*      */
/*      */
/*  926 */       if (compound != null) {
/*      */
/*      */
/*      */
/*  930 */         if (linkedHashMap.containsKey(jCAnnotation.type.tsym)) {
/*  931 */           if (!this.allowRepeatedAnnos) {
/*  932 */             this.log.error(jCAnnotation.pos(), "repeatable.annotations.not.supported.in.source", new Object[0]);
/*  933 */             this.allowRepeatedAnnos = true;
/*      */           }
/*  935 */           ListBuffer listBuffer = (ListBuffer)linkedHashMap.get(jCAnnotation.type.tsym);
/*  936 */           listBuffer = listBuffer.append(compound);
/*  937 */           linkedHashMap.put(jCAnnotation.type.tsym, listBuffer);
/*  938 */           hashMap.put(compound, jCAnnotation.pos());
/*      */         } else {
/*  940 */           linkedHashMap.put(jCAnnotation.type.tsym, ListBuffer.of(compound));
/*  941 */           hashMap.put(compound, jCAnnotation.pos());
/*      */         }
/*      */
/*      */
/*  945 */         if (!compound.type.isErroneous() && paramSymbol.owner.kind != 16 && this.types
/*      */
/*  947 */           .isSameType(compound.type, this.syms.deprecatedType)) {
/*  948 */           paramSymbol.flags_field |= 0x20000L;
/*      */         }
/*      */       }
/*      */     }
/*  952 */     this.annotate.getClass(); paramSymbol.setDeclarationAttributesWithCompletion(new Annotate.AnnotateRepeatedContext(this.annotate, paramEnv, (Map)linkedHashMap, (Map)hashMap, this.log, false));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void annotateDefaultValueLater(final JCTree.JCExpression defaultValue, final Env<AttrContext> localEnv, final Symbol.MethodSymbol m) {
/*  960 */     this.annotate.normal(new Annotate.Worker()
/*      */         {
/*      */           public String toString() {
/*  963 */             return "annotate " + m.owner + "." + m + " default " + defaultValue;
/*      */           }
/*      */
/*      */
/*      */
/*      */           public void run() {
/*  969 */             JavaFileObject javaFileObject = MemberEnter.this.log.useSource(localEnv.toplevel.sourcefile);
/*      */             try {
/*  971 */               MemberEnter.this.enterDefaultValue(defaultValue, localEnv, m);
/*      */             } finally {
/*  973 */               MemberEnter.this.log.useSource(javaFileObject);
/*      */             }
/*      */           }
/*      */         });
/*  977 */     this.annotate.validate(new Annotate.Worker()
/*      */         {
/*      */           public void run() {
/*  980 */             JavaFileObject javaFileObject = MemberEnter.this.log.useSource(localEnv.toplevel.sourcefile);
/*      */
/*      */
/*      */             try {
/*  984 */               MemberEnter.this.chk.validateAnnotationTree((JCTree)defaultValue);
/*      */             } finally {
/*  986 */               MemberEnter.this.log.useSource(javaFileObject);
/*      */             }
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private void enterDefaultValue(JCTree.JCExpression paramJCExpression, Env<AttrContext> paramEnv, Symbol.MethodSymbol paramMethodSymbol) {
/*  996 */     paramMethodSymbol.defaultValue = this.annotate.enterAttributeValue(paramMethodSymbol.type.getReturnType(), paramJCExpression, paramEnv);
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
/*      */   public void complete(Symbol paramSymbol) throws Symbol.CompletionFailure {
/* 1010 */     if (!this.completionEnabled) {
/*      */
/* 1012 */       Assert.check(((paramSymbol.flags() & 0x1000000L) == 0L));
/* 1013 */       paramSymbol.completer = this;
/*      */
/*      */       return;
/*      */     }
/* 1017 */     Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)paramSymbol;
/* 1018 */     Type.ClassType classType = (Type.ClassType)classSymbol.type;
/* 1019 */     Env<AttrContext> env = this.typeEnvs.get((Symbol.TypeSymbol)classSymbol);
/* 1020 */     JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)env.tree;
/* 1021 */     boolean bool = this.isFirst;
/* 1022 */     this.isFirst = false;
/*      */     try {
/* 1024 */       this.annotate.enterStart();
/*      */
/* 1026 */       JavaFileObject javaFileObject = this.log.useSource(env.toplevel.sourcefile);
/* 1027 */       JCDiagnostic.DiagnosticPosition diagnosticPosition = this.deferredLintHandler.setPos(jCClassDecl.pos());
/*      */
/*      */       try {
/* 1030 */         this.halfcompleted.append(env);
/*      */
/*      */
/* 1033 */         classSymbol.flags_field |= 0x10000000L;
/*      */
/*      */
/*      */
/* 1037 */         if (classSymbol.owner.kind == 1) {
/* 1038 */           memberEnter((JCTree)env.toplevel, env.enclosing(JCTree.Tag.TOPLEVEL));
/* 1039 */           this.todo.append(env);
/*      */         }
/*      */
/* 1042 */         if (classSymbol.owner.kind == 2) {
/* 1043 */           classSymbol.owner.complete();
/*      */         }
/*      */
/* 1046 */         Env<AttrContext> env1 = baseEnv(jCClassDecl, env);
/*      */
/* 1048 */         if (jCClassDecl.extending != null)
/* 1049 */           typeAnnotate((JCTree)jCClassDecl.extending, env1, paramSymbol, jCClassDecl.pos());
/* 1050 */         for (JCTree.JCExpression jCExpression : jCClassDecl.implementing)
/* 1051 */           typeAnnotate((JCTree)jCExpression, env1, paramSymbol, jCClassDecl.pos());
/* 1052 */         this.annotate.flush();
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1059 */         Type type = (Type)((jCClassDecl.extending != null) ? this.attr.attribBase((JCTree)jCClassDecl.extending, env1, true, false, true) : (((jCClassDecl.mods.flags & 0x4000L) != 0L) ? this.attr.attribBase((JCTree)enumBase(jCClassDecl.pos, classSymbol), env1, true, false, false) : ((classSymbol.fullname == this.names.java_lang_Object) ? Type.noType : this.syms.objectType)));
/*      */
/*      */
/*      */
/*      */
/* 1064 */         classType.supertype_field = modelMissingTypes(type, jCClassDecl.extending, false);
/*      */
/*      */
/* 1067 */         ListBuffer listBuffer1 = new ListBuffer();
/* 1068 */         ListBuffer listBuffer2 = null;
/* 1069 */         HashSet<Type> hashSet = new HashSet();
/* 1070 */         List list = jCClassDecl.implementing;
/* 1071 */         for (JCTree.JCExpression jCExpression : list) {
/* 1072 */           Type type1 = this.attr.attribBase((JCTree)jCExpression, env1, false, true, true);
/* 1073 */           if (type1.hasTag(TypeTag.CLASS)) {
/* 1074 */             listBuffer1.append(type1);
/* 1075 */             if (listBuffer2 != null) listBuffer2.append(type1);
/* 1076 */             this.chk.checkNotRepeated(jCExpression.pos(), this.types.erasure(type1), hashSet); continue;
/*      */           }
/* 1078 */           if (listBuffer2 == null)
/* 1079 */             listBuffer2 = (new ListBuffer()).appendList(listBuffer1);
/* 1080 */           listBuffer2.append(modelMissingTypes(type1, jCExpression, true));
/*      */         }
/*      */
/* 1083 */         if ((classSymbol.flags_field & 0x2000L) != 0L) {
/* 1084 */           classType.interfaces_field = List.of(this.syms.annotationType);
/* 1085 */           classType.all_interfaces_field = classType.interfaces_field;
/*      */         } else {
/* 1087 */           classType.interfaces_field = listBuffer1.toList();
/* 1088 */           classType
/* 1089 */             .all_interfaces_field = (listBuffer2 == null) ? classType.interfaces_field : listBuffer2.toList();
/*      */         }
/*      */
/* 1092 */         if (classSymbol.fullname == this.names.java_lang_Object) {
/* 1093 */           if (jCClassDecl.extending != null) {
/* 1094 */             this.chk.checkNonCyclic(jCClassDecl.extending.pos(), type);
/*      */
/* 1096 */             classType.supertype_field = (Type)Type.noType;
/*      */           }
/* 1098 */           else if (jCClassDecl.implementing.nonEmpty()) {
/* 1099 */             this.chk.checkNonCyclic(((JCTree.JCExpression)jCClassDecl.implementing.head).pos(), (Type)classType.interfaces_field.head);
/*      */
/* 1101 */             classType.interfaces_field = List.nil();
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1109 */         this.attr.attribAnnotationTypes(jCClassDecl.mods.annotations, env1);
/* 1110 */         if (hasDeprecatedAnnotation(jCClassDecl.mods.annotations))
/* 1111 */           classSymbol.flags_field |= 0x20000L;
/* 1112 */         annotateLater(jCClassDecl.mods.annotations, env1, (Symbol)classSymbol, jCClassDecl.pos());
/*      */
/*      */
/* 1115 */         this.chk.checkNonCyclicDecl(jCClassDecl);
/*      */
/* 1117 */         this.attr.attribTypeVariables(jCClassDecl.typarams, env1);
/*      */
/* 1119 */         for (JCTree.JCTypeParameter jCTypeParameter : jCClassDecl.typarams) {
/* 1120 */           typeAnnotate((JCTree)jCTypeParameter, env1, paramSymbol, jCClassDecl.pos());
/*      */         }
/*      */
/* 1123 */         if ((classSymbol.flags() & 0x200L) == 0L &&
/* 1124 */           !TreeInfo.hasConstructors(jCClassDecl.defs)) {
/* 1125 */           List<Type> list1 = List.nil();
/* 1126 */           List<Type> list2 = List.nil();
/* 1127 */           List<Type> list3 = List.nil();
/* 1128 */           long l = 0L;
/* 1129 */           boolean bool1 = false;
/* 1130 */           boolean bool2 = true;
/* 1131 */           JCTree.JCNewClass jCNewClass = null;
/* 1132 */           if (classSymbol.name.isEmpty()) {
/* 1133 */             jCNewClass = (JCTree.JCNewClass)env.next.tree;
/* 1134 */             if (jCNewClass.constructor != null) {
/* 1135 */               bool2 = (jCNewClass.constructor.kind != 63) ? true : false;
/* 1136 */               Type type1 = this.types.memberType(classSymbol.type, jCNewClass.constructor);
/*      */
/* 1138 */               list1 = type1.getParameterTypes();
/* 1139 */               list2 = type1.getTypeArguments();
/* 1140 */               l = jCNewClass.constructor.flags() & 0x400000000L;
/* 1141 */               if (jCNewClass.encl != null) {
/* 1142 */                 list1 = list1.prepend(jCNewClass.encl.type);
/* 1143 */                 bool1 = true;
/*      */               }
/* 1145 */               list3 = type1.getThrownTypes();
/*      */             }
/*      */           }
/* 1148 */           if (bool2) {
/* 1149 */             Symbol.MethodSymbol methodSymbol = (jCNewClass != null) ? (Symbol.MethodSymbol)jCNewClass.constructor : null;
/*      */
/* 1151 */             JCTree jCTree = DefaultConstructor(this.make.at(jCClassDecl.pos), classSymbol, methodSymbol, list2, list1, list3, l, bool1);
/*      */
/*      */
/*      */
/* 1155 */             jCClassDecl.defs = jCClassDecl.defs.prepend(jCTree);
/*      */           }
/*      */         }
/*      */
/*      */
/* 1160 */         Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(262160L, this.names._this, classSymbol.type, (Symbol)classSymbol);
/*      */
/* 1162 */         varSymbol.pos = 0;
/* 1163 */         ((AttrContext)env.info).scope.enter((Symbol)varSymbol);
/*      */
/* 1165 */         if ((classSymbol.flags_field & 0x200L) == 0L && classType.supertype_field
/* 1166 */           .hasTag(TypeTag.CLASS)) {
/* 1167 */           Symbol.VarSymbol varSymbol1 = new Symbol.VarSymbol(262160L, this.names._super, classType.supertype_field, (Symbol)classSymbol);
/*      */
/*      */
/* 1170 */           varSymbol1.pos = 0;
/* 1171 */           ((AttrContext)env.info).scope.enter((Symbol)varSymbol1);
/*      */         }
/*      */
/*      */
/*      */
/*      */
/* 1177 */         if (classSymbol.owner.kind == 1 && classSymbol.owner != this.syms.unnamedPackage && this.reader
/*      */
/* 1179 */           .packageExists(classSymbol.fullname)) {
/* 1180 */           this.log.error(jCClassDecl.pos, "clash.with.pkg.of.same.name", new Object[] { Kinds.kindName(paramSymbol), classSymbol });
/*      */         }
/* 1182 */         if (classSymbol.owner.kind == 1 && (classSymbol.flags_field & 0x1L) == 0L &&
/* 1183 */           !env.toplevel.sourcefile.isNameCompatible(classSymbol.name.toString(), JavaFileObject.Kind.SOURCE)) {
/* 1184 */           classSymbol.flags_field |= 0x100000000000L;
/*      */         }
/* 1186 */       } catch (Symbol.CompletionFailure completionFailure) {
/* 1187 */         this.chk.completionError(jCClassDecl.pos(), completionFailure);
/*      */       } finally {
/* 1189 */         this.deferredLintHandler.setPos(diagnosticPosition);
/* 1190 */         this.log.useSource(javaFileObject);
/*      */       }
/*      */
/*      */
/*      */
/* 1195 */       if (bool) {
/*      */         try {
/* 1197 */           while (this.halfcompleted.nonEmpty()) {
/* 1198 */             Env<AttrContext> env1 = (Env)this.halfcompleted.next();
/* 1199 */             finish(env1);
/* 1200 */             if (this.allowTypeAnnos) {
/* 1201 */               this.typeAnnotations.organizeTypeAnnotationsSignatures(env1, (JCTree.JCClassDecl)env1.tree);
/* 1202 */               this.typeAnnotations.validateTypeAnnotationsSignatures(env1, (JCTree.JCClassDecl)env1.tree);
/*      */             }
/*      */           }
/*      */         } finally {
/* 1206 */           this.isFirst = true;
/*      */         }
/*      */       }
/*      */     } finally {
/* 1210 */       this.annotate.enterDone();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private void actualEnterTypeAnnotations(List<JCTree.JCAnnotation> paramList, Env<AttrContext> paramEnv, Symbol paramSymbol) {
/* 1220 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*      */
/* 1222 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*      */
/*      */
/* 1225 */     for (List<JCTree.JCAnnotation> list = paramList; !list.isEmpty(); list = list.tail) {
/* 1226 */       JCTree.JCAnnotation jCAnnotation = (JCTree.JCAnnotation)list.head;
/* 1227 */       Attribute.TypeCompound typeCompound = this.annotate.enterTypeAnnotation(jCAnnotation, this.syms.annotationType, paramEnv);
/*      */
/*      */
/* 1230 */       if (typeCompound != null)
/*      */       {
/*      */
/*      */
/* 1234 */         if (linkedHashMap.containsKey(jCAnnotation.type.tsym)) {
/* 1235 */           if (this.source.allowRepeatedAnnotations()) {
/* 1236 */             ListBuffer listBuffer = (ListBuffer)linkedHashMap.get(jCAnnotation.type.tsym);
/* 1237 */             listBuffer = listBuffer.append(typeCompound);
/* 1238 */             linkedHashMap.put(jCAnnotation.type.tsym, listBuffer);
/* 1239 */             hashMap.put(typeCompound, jCAnnotation.pos());
/*      */           } else {
/* 1241 */             this.log.error(jCAnnotation.pos(), "repeatable.annotations.not.supported.in.source", new Object[0]);
/*      */           }
/*      */         } else {
/* 1244 */           linkedHashMap.put(jCAnnotation.type.tsym, ListBuffer.of(typeCompound));
/* 1245 */           hashMap.put(typeCompound, jCAnnotation.pos());
/*      */         }
/*      */       }
/*      */     }
/* 1249 */     if (paramSymbol != null) {
/* 1250 */       this.annotate.getClass(); paramSymbol.appendTypeAttributesWithCompletion(new Annotate.AnnotateRepeatedContext(this.annotate, paramEnv, (Map)linkedHashMap, (Map)hashMap, this.log, true));
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   public void typeAnnotate(JCTree paramJCTree, Env<AttrContext> paramEnv, Symbol paramSymbol, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 1256 */     if (this.allowTypeAnnos) {
/* 1257 */       paramJCTree.accept((JCTree.Visitor)new TypeAnnotate(paramEnv, paramSymbol, paramDiagnosticPosition));
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private class TypeAnnotate
/*      */     extends TreeScanner
/*      */   {
/*      */     private Env<AttrContext> env;
/*      */
/*      */     private Symbol sym;
/*      */     private JCDiagnostic.DiagnosticPosition deferPos;
/*      */
/*      */     public TypeAnnotate(Env<AttrContext> param1Env, Symbol param1Symbol, JCDiagnostic.DiagnosticPosition param1DiagnosticPosition) {
/* 1271 */       this.env = param1Env;
/* 1272 */       this.sym = param1Symbol;
/* 1273 */       this.deferPos = param1DiagnosticPosition;
/*      */     }
/*      */
/*      */     void annotateTypeLater(final List<JCTree.JCAnnotation> annotations) {
/* 1277 */       if (annotations.isEmpty()) {
/*      */         return;
/*      */       }
/*      */
/* 1281 */       final JCDiagnostic.DiagnosticPosition deferPos = this.deferPos;
/*      */
/* 1283 */       MemberEnter.this.annotate.normal(new Annotate.Worker()
/*      */           {
/*      */             public String toString() {
/* 1286 */               return "type annotate " + annotations + " onto " + TypeAnnotate.this.sym + " in " + TypeAnnotate.this.sym.owner;
/*      */             }
/*      */
/*      */             public void run() {
/* 1290 */               JavaFileObject javaFileObject = MemberEnter.this.log.useSource(TypeAnnotate.this.env.toplevel.sourcefile);
/* 1291 */               JCDiagnostic.DiagnosticPosition diagnosticPosition = null;
/*      */
/* 1293 */               if (deferPos != null) {
/* 1294 */                 diagnosticPosition = MemberEnter.this.deferredLintHandler.setPos(deferPos);
/*      */               }
/*      */               try {
/* 1297 */                 MemberEnter.this.actualEnterTypeAnnotations(annotations, TypeAnnotate.this.env, TypeAnnotate.this.sym);
/*      */               } finally {
/* 1299 */                 if (diagnosticPosition != null)
/* 1300 */                   MemberEnter.this.deferredLintHandler.setPos(diagnosticPosition);
/* 1301 */                 MemberEnter.this.log.useSource(javaFileObject);
/*      */               }
/*      */             }
/*      */           });
/*      */     }
/*      */
/*      */
/*      */     public void visitAnnotatedType(JCTree.JCAnnotatedType param1JCAnnotatedType) {
/* 1309 */       annotateTypeLater(param1JCAnnotatedType.annotations);
/* 1310 */       super.visitAnnotatedType(param1JCAnnotatedType);
/*      */     }
/*      */
/*      */
/*      */     public void visitTypeParameter(JCTree.JCTypeParameter param1JCTypeParameter) {
/* 1315 */       annotateTypeLater(param1JCTypeParameter.annotations);
/* 1316 */       super.visitTypeParameter(param1JCTypeParameter);
/*      */     }
/*      */
/*      */
/*      */     public void visitNewArray(JCTree.JCNewArray param1JCNewArray) {
/* 1321 */       annotateTypeLater(param1JCNewArray.annotations);
/* 1322 */       for (List<JCTree.JCAnnotation> list : (Iterable<List<JCTree.JCAnnotation>>)param1JCNewArray.dimAnnotations)
/* 1323 */         annotateTypeLater(list);
/* 1324 */       super.visitNewArray(param1JCNewArray);
/*      */     }
/*      */
/*      */
/*      */     public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) {
/* 1329 */       scan((JCTree)param1JCMethodDecl.mods);
/* 1330 */       scan((JCTree)param1JCMethodDecl.restype);
/* 1331 */       scan(param1JCMethodDecl.typarams);
/* 1332 */       scan((JCTree)param1JCMethodDecl.recvparam);
/* 1333 */       scan(param1JCMethodDecl.params);
/* 1334 */       scan(param1JCMethodDecl.thrown);
/* 1335 */       scan((JCTree)param1JCMethodDecl.defaultValue);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) {
/* 1342 */       JCDiagnostic.DiagnosticPosition diagnosticPosition = this.deferPos;
/* 1343 */       this.deferPos = param1JCVariableDecl.pos();
/*      */       try {
/* 1345 */         if (this.sym != null && this.sym.kind == 4) {
/*      */
/*      */
/* 1348 */           scan((JCTree)param1JCVariableDecl.mods);
/* 1349 */           scan((JCTree)param1JCVariableDecl.vartype);
/*      */         }
/* 1351 */         scan((JCTree)param1JCVariableDecl.init);
/*      */       } finally {
/* 1353 */         this.deferPos = diagnosticPosition;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) {}
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/* 1366 */       if (param1JCNewClass.def == null)
/*      */       {
/*      */
/* 1369 */         super.visitNewClass(param1JCNewClass);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private Env<AttrContext> baseEnv(JCTree.JCClassDecl paramJCClassDecl, Env<AttrContext> paramEnv) {
/* 1376 */     Scope scope = new Scope((Symbol)paramJCClassDecl.sym);
/*      */
/* 1378 */     for (Scope.Entry entry = ((AttrContext)paramEnv.outer.info).scope.elems; entry != null; entry = entry.sibling) {
/* 1379 */       if (entry.sym.isLocal()) {
/* 1380 */         scope.enter(entry.sym);
/*      */       }
/*      */     }
/*      */
/* 1384 */     if (paramJCClassDecl.typarams != null) {
/* 1385 */       List list = paramJCClassDecl.typarams;
/* 1386 */       for (; list.nonEmpty();
/* 1387 */         list = list.tail)
/* 1388 */         scope.enter((Symbol)((JCTree.JCTypeParameter)list.head).type.tsym);
/* 1389 */     }  Env<AttrContext> env1 = paramEnv.outer;
/* 1390 */     Env<AttrContext> env2 = env1.dup((JCTree)paramJCClassDecl, ((AttrContext)env1.info).dup(scope));
/* 1391 */     env2.baseClause = true;
/* 1392 */     env2.outer = env1;
/* 1393 */     ((AttrContext)env2.info).isSelfCall = false;
/* 1394 */     return env2;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private void finish(Env<AttrContext> paramEnv) {
/* 1401 */     JavaFileObject javaFileObject = this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */     try {
/* 1403 */       JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)paramEnv.tree;
/* 1404 */       finishClass(jCClassDecl, paramEnv);
/*      */     } finally {
/* 1406 */       this.log.useSource(javaFileObject);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private JCTree.JCExpression enumBase(int paramInt, Symbol.ClassSymbol paramClassSymbol) {
/* 1416 */     return (JCTree.JCExpression)this.make.at(paramInt).TypeApply(this.make.QualIdent((Symbol)this.syms.enumSym),
/* 1417 */         List.of(this.make.Type(paramClassSymbol.type)));
/*      */   }
/*      */
/*      */
/*      */   Type modelMissingTypes(Type paramType, final JCTree.JCExpression tree, final boolean interfaceExpected) {
/* 1422 */     if (!paramType.hasTag(TypeTag.ERROR)) {
/* 1423 */       return paramType;
/*      */     }
/* 1425 */     return (Type)new Type.ErrorType(paramType.getOriginalType(), paramType.tsym)
/*      */       {
/*      */         private Type modelType;
/*      */
/*      */         public Type getModelType() {
/* 1430 */           if (this.modelType == null)
/* 1431 */             this.modelType = (new Synthesizer(getOriginalType(), interfaceExpected)).visit((JCTree)tree);
/* 1432 */           return this.modelType;
/*      */         }
/*      */       };
/*      */   }
/*      */
/*      */   private class Synthesizer extends JCTree.Visitor {
/*      */     Type originalType;
/*      */     boolean interfaceExpected;
/* 1440 */     List<Symbol.ClassSymbol> synthesizedSymbols = List.nil();
/*      */     Type result;
/*      */
/*      */     Synthesizer(Type param1Type, boolean param1Boolean) {
/* 1444 */       this.originalType = param1Type;
/* 1445 */       this.interfaceExpected = param1Boolean;
/*      */     }
/*      */
/*      */     Type visit(JCTree param1JCTree) {
/* 1449 */       param1JCTree.accept(this);
/* 1450 */       return this.result;
/*      */     }
/*      */
/*      */     List<Type> visit(List<? extends JCTree> param1List) {
/* 1454 */       ListBuffer listBuffer = new ListBuffer();
/* 1455 */       for (JCTree jCTree : param1List)
/* 1456 */         listBuffer.append(visit(jCTree));
/* 1457 */       return listBuffer.toList();
/*      */     }
/*      */
/*      */
/*      */     public void visitTree(JCTree param1JCTree) {
/* 1462 */       this.result = MemberEnter.this.syms.errType;
/*      */     }
/*      */
/*      */
/*      */     public void visitIdent(JCTree.JCIdent param1JCIdent) {
/* 1467 */       if (!param1JCIdent.type.hasTag(TypeTag.ERROR)) {
/* 1468 */         this.result = param1JCIdent.type;
/*      */       } else {
/* 1470 */         this.result = (synthesizeClass(param1JCIdent.name, (Symbol)MemberEnter.this.syms.unnamedPackage)).type;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitSelect(JCTree.JCFieldAccess param1JCFieldAccess) {
/* 1476 */       if (!param1JCFieldAccess.type.hasTag(TypeTag.ERROR)) {
/* 1477 */         this.result = param1JCFieldAccess.type;
/*      */       } else {
/*      */         Type type;
/* 1480 */         boolean bool = this.interfaceExpected;
/*      */         try {
/* 1482 */           this.interfaceExpected = false;
/* 1483 */           type = visit((JCTree)param1JCFieldAccess.selected);
/*      */         } finally {
/* 1485 */           this.interfaceExpected = bool;
/*      */         }
/* 1487 */         Symbol.ClassSymbol classSymbol = synthesizeClass(param1JCFieldAccess.name, (Symbol)type.tsym);
/* 1488 */         this.result = classSymbol.type;
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public void visitTypeApply(JCTree.JCTypeApply param1JCTypeApply) {
/* 1494 */       if (!param1JCTypeApply.type.hasTag(TypeTag.ERROR)) {
/* 1495 */         this.result = param1JCTypeApply.type;
/*      */       } else {
/* 1497 */         Type.ClassType classType = (Type.ClassType)visit((JCTree)param1JCTypeApply.clazz);
/* 1498 */         if (this.synthesizedSymbols.contains(classType.tsym))
/* 1499 */           synthesizeTyparams((Symbol.ClassSymbol)classType.tsym, param1JCTypeApply.arguments.size());
/* 1500 */         final List<Type> actuals = visit(param1JCTypeApply.arguments);
/* 1501 */         this.result = (Type)new Type.ErrorType(param1JCTypeApply.type, classType.tsym)
/*      */           {
/*      */             public List<Type> getTypeArguments() {
/* 1504 */               return actuals;
/*      */             }
/*      */           };
/*      */       }
/*      */     }
/*      */
/*      */     Symbol.ClassSymbol synthesizeClass(Name param1Name, Symbol param1Symbol) {
/* 1511 */       boolean bool = this.interfaceExpected ? true : false;
/* 1512 */       Symbol.ClassSymbol classSymbol = new Symbol.ClassSymbol(bool, param1Name, param1Symbol);
/* 1513 */       classSymbol.members_field = (Scope)new Scope.ErrorScope((Symbol)classSymbol);
/* 1514 */       classSymbol.type = (Type)new Type.ErrorType(this.originalType, (Symbol.TypeSymbol)classSymbol)
/*      */         {
/*      */           public List<Type> getTypeArguments() {
/* 1517 */             return this.typarams_field;
/*      */           }
/*      */         };
/* 1520 */       this.synthesizedSymbols = this.synthesizedSymbols.prepend(classSymbol);
/* 1521 */       return classSymbol;
/*      */     }
/*      */
/*      */     void synthesizeTyparams(Symbol.ClassSymbol param1ClassSymbol, int param1Int) {
/* 1525 */       Type.ClassType classType = (Type.ClassType)param1ClassSymbol.type;
/* 1526 */       Assert.check(classType.typarams_field.isEmpty());
/* 1527 */       if (param1Int == 1) {
/* 1528 */         Type.TypeVar typeVar = new Type.TypeVar(MemberEnter.this.names.fromString("T"), (Symbol)param1ClassSymbol, MemberEnter.this.syms.botType);
/* 1529 */         classType.typarams_field = classType.typarams_field.prepend(typeVar);
/*      */       } else {
/* 1531 */         for (int i = param1Int; i > 0; i--) {
/* 1532 */           Type.TypeVar typeVar = new Type.TypeVar(MemberEnter.this.names.fromString("T" + i), (Symbol)param1ClassSymbol, MemberEnter.this.syms.botType);
/* 1533 */           classType.typarams_field = classType.typarams_field.prepend(typeVar);
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   JCTree DefaultConstructor(TreeMaker paramTreeMaker, Symbol.ClassSymbol paramClassSymbol, Symbol.MethodSymbol paramMethodSymbol, List<Type> paramList1, List<Type> paramList2, List<Type> paramList3, long paramLong, boolean paramBoolean) {
/* 1572 */     if ((paramClassSymbol.flags() & 0x4000L) != 0L &&
/* 1573 */       (this.types.supertype(paramClassSymbol.type)).tsym == this.syms.enumSym) {
/*      */
/* 1575 */       paramLong = paramLong & 0xFFFFFFFFFFFFFFF8L | 0x2L | 0x1000000000L;
/*      */     } else {
/* 1577 */       paramLong |= paramClassSymbol.flags() & 0x7L | 0x1000000000L;
/* 1578 */     }  if (paramClassSymbol.name.isEmpty()) {
/* 1579 */       paramLong |= 0x20000000L;
/*      */     }
/* 1581 */     Type.MethodType methodType = new Type.MethodType(paramList2, null, paramList3, (Symbol.TypeSymbol)paramClassSymbol);
/* 1582 */     Type type = (Type)(paramList1.nonEmpty() ? new Type.ForAll(paramList1, (Type)methodType) : methodType);
/*      */
/*      */
/* 1585 */     Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(paramLong, this.names.init, type, (Symbol)paramClassSymbol);
/*      */
/* 1587 */     methodSymbol.params = createDefaultConstructorParams(paramTreeMaker, paramMethodSymbol, methodSymbol, paramList2, paramBoolean);
/*      */
/* 1589 */     List<JCTree.JCVariableDecl> list = paramTreeMaker.Params(paramList2, (Symbol)methodSymbol);
/* 1590 */     List list1 = List.nil();
/* 1591 */     if (paramClassSymbol.type != this.syms.objectType) {
/* 1592 */       list1 = list1.prepend(SuperCall(paramTreeMaker, paramList1, list, paramBoolean));
/*      */     }
/* 1594 */     return (JCTree)paramTreeMaker.MethodDef(methodSymbol, paramTreeMaker.Block(0L, list1));
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private List<Symbol.VarSymbol> createDefaultConstructorParams(TreeMaker paramTreeMaker, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, List<Type> paramList, boolean paramBoolean) {
/* 1604 */     List<Symbol.VarSymbol> list = null;
/* 1605 */     List<Type> list1 = paramList;
/* 1606 */     if (paramBoolean) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1617 */       list = List.nil();
/* 1618 */       Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(8589934592L, paramTreeMaker.paramName(0), (Type)paramList.head, (Symbol)paramMethodSymbol2);
/* 1619 */       list = list.append(varSymbol);
/* 1620 */       list1 = list1.tail;
/*      */     }
/* 1622 */     if (paramMethodSymbol1 != null && paramMethodSymbol1.params != null && paramMethodSymbol1.params
/* 1623 */       .nonEmpty() && list1.nonEmpty()) {
/* 1624 */       list = (list == null) ? List.nil() : list;
/* 1625 */       List list2 = paramMethodSymbol1.params;
/* 1626 */       while (list2.nonEmpty() && list1.nonEmpty()) {
/* 1627 */         Symbol.VarSymbol varSymbol = new Symbol.VarSymbol(((Symbol.VarSymbol)list2.head).flags() | 0x200000000L, ((Symbol.VarSymbol)list2.head).name, (Type)list1.head, (Symbol)paramMethodSymbol2);
/*      */
/* 1629 */         list = list.append(varSymbol);
/* 1630 */         list2 = list2.tail;
/* 1631 */         list1 = list1.tail;
/*      */       }
/*      */     }
/* 1634 */     return list;
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
/*      */   JCTree.JCExpressionStatement SuperCall(TreeMaker paramTreeMaker, List<Type> paramList, List<JCTree.JCVariableDecl> paramList1, boolean paramBoolean) {
/*      */     JCTree.JCIdent jCIdent;
/* 1657 */     if (paramBoolean) {
/* 1658 */       JCTree.JCFieldAccess jCFieldAccess = paramTreeMaker.Select(paramTreeMaker.Ident((JCTree.JCVariableDecl)paramList1.head), this.names._super);
/* 1659 */       paramList1 = paramList1.tail;
/*      */     } else {
/* 1661 */       jCIdent = paramTreeMaker.Ident(this.names._super);
/*      */     }
/* 1663 */     List list = paramList.nonEmpty() ? paramTreeMaker.Types(paramList) : null;
/* 1664 */     return paramTreeMaker.Exec((JCTree.JCExpression)paramTreeMaker.Apply(list, (JCTree.JCExpression)jCIdent, paramTreeMaker.Idents(paramList1)));
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\MemberEnter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
