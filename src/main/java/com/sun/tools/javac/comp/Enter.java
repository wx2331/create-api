/*     */ package com.sun.tools.javac.comp;
/*     */
/*     */ import com.sun.tools.javac.code.Lint;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.jvm.ClassReader;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.tree.TreeMaker;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import javax.tools.JavaFileManager;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Enter
/*     */   extends JCTree.Visitor
/*     */ {
/*  93 */   protected static final Context.Key<Enter> enterKey = new Context.Key();
/*     */
/*     */   Log log;
/*     */
/*     */   Symtab syms;
/*     */
/*     */   Check chk;
/*     */
/*     */   TreeMaker make;
/*     */
/*     */   ClassReader reader;
/*     */
/*     */   Annotate annotate;
/*     */
/*     */   MemberEnter memberEnter;
/*     */
/*     */   Types types;
/*     */   Lint lint;
/*     */
/*     */   public static Enter instance(Context paramContext) {
/* 113 */     Enter enter = (Enter)paramContext.get(enterKey);
/* 114 */     if (enter == null)
/* 115 */       enter = new Enter(paramContext);
/* 116 */     return enter;
/*     */   }
/*     */   Names names; JavaFileManager fileManager; Option.PkgInfo pkginfoOpt; TypeEnvs typeEnvs; private final Todo todo; ListBuffer<Symbol.ClassSymbol> uncompleted; private JCTree.JCClassDecl predefClassDef; protected Env<AttrContext> env; Type result;
/*     */   protected Enter(Context paramContext) {
/* 120 */     paramContext.put(enterKey, this);
/*     */
/* 122 */     this.log = Log.instance(paramContext);
/* 123 */     this.reader = ClassReader.instance(paramContext);
/* 124 */     this.make = TreeMaker.instance(paramContext);
/* 125 */     this.syms = Symtab.instance(paramContext);
/* 126 */     this.chk = Check.instance(paramContext);
/* 127 */     this.memberEnter = MemberEnter.instance(paramContext);
/* 128 */     this.types = Types.instance(paramContext);
/* 129 */     this.annotate = Annotate.instance(paramContext);
/* 130 */     this.lint = Lint.instance(paramContext);
/* 131 */     this.names = Names.instance(paramContext);
/*     */
/* 133 */     this.predefClassDef = this.make.ClassDef(this.make
/* 134 */         .Modifiers(1L), this.syms.predefClass.name,
/*     */
/* 136 */         List.nil(), null,
/*     */
/* 138 */         List.nil(),
/* 139 */         List.nil());
/* 140 */     this.predefClassDef.sym = this.syms.predefClass;
/* 141 */     this.todo = Todo.instance(paramContext);
/* 142 */     this.fileManager = (JavaFileManager)paramContext.get(JavaFileManager.class);
/*     */
/* 144 */     Options options = Options.instance(paramContext);
/* 145 */     this.pkginfoOpt = Option.PkgInfo.get(options);
/* 146 */     this.typeEnvs = TypeEnvs.instance(paramContext);
/*     */   }
/*     */
/*     */
/*     */
/*     */   public Env<AttrContext> getEnv(Symbol.TypeSymbol paramTypeSymbol) {
/* 152 */     return this.typeEnvs.get(paramTypeSymbol);
/*     */   }
/*     */
/*     */   public Env<AttrContext> getClassEnv(Symbol.TypeSymbol paramTypeSymbol) {
/* 156 */     Env<AttrContext> env1 = getEnv(paramTypeSymbol);
/* 157 */     Env<AttrContext> env2 = env1;
/* 158 */     while (((AttrContext)env2.info).lint == null)
/* 159 */       env2 = env2.next;
/* 160 */     ((AttrContext)env1.info).lint = ((AttrContext)env2.info).lint.augment((Symbol)paramTypeSymbol);
/* 161 */     return env1;
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
/*     */   public Env<AttrContext> classEnv(JCTree.JCClassDecl paramJCClassDecl, Env<AttrContext> paramEnv) {
/* 193 */     Env<AttrContext> env = paramEnv.dup((JCTree)paramJCClassDecl, ((AttrContext)paramEnv.info).dup(new Scope((Symbol)paramJCClassDecl.sym)));
/* 194 */     env.enclClass = paramJCClassDecl;
/* 195 */     env.outer = paramEnv;
/* 196 */     ((AttrContext)env.info).isSelfCall = false;
/* 197 */     ((AttrContext)env.info).lint = null;
/*     */
/* 199 */     return env;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   Env<AttrContext> topLevelEnv(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 206 */     Env<AttrContext> env = new Env<>((JCTree)paramJCCompilationUnit, new AttrContext());
/* 207 */     env.toplevel = paramJCCompilationUnit;
/* 208 */     env.enclClass = this.predefClassDef;
/* 209 */     paramJCCompilationUnit.namedImportScope = new Scope.ImportScope((Symbol)paramJCCompilationUnit.packge);
/* 210 */     paramJCCompilationUnit.starImportScope = new Scope.StarImportScope((Symbol)paramJCCompilationUnit.packge);
/* 211 */     ((AttrContext)env.info).scope = (Scope)paramJCCompilationUnit.namedImportScope;
/* 212 */     ((AttrContext)env.info).lint = this.lint;
/* 213 */     return env;
/*     */   }
/*     */
/*     */   public Env<AttrContext> getTopLevelEnv(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 217 */     Env<AttrContext> env = new Env<>((JCTree)paramJCCompilationUnit, new AttrContext());
/* 218 */     env.toplevel = paramJCCompilationUnit;
/* 219 */     env.enclClass = this.predefClassDef;
/* 220 */     ((AttrContext)env.info).scope = (Scope)paramJCCompilationUnit.namedImportScope;
/* 221 */     ((AttrContext)env.info).lint = this.lint;
/* 222 */     return env;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   Scope enterScope(Env<AttrContext> paramEnv) {
/* 231 */     return paramEnv.tree.hasTag(JCTree.Tag.CLASSDEF) ? ((JCTree.JCClassDecl)paramEnv.tree).sym.members_field : ((AttrContext)paramEnv.info).scope;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   Type classEnter(JCTree paramJCTree, Env<AttrContext> paramEnv) {
/* 255 */     Env<AttrContext> env = this.env;
/*     */     try {
/* 257 */       this.env = paramEnv;
/* 258 */       paramJCTree.accept(this);
/* 259 */       return this.result;
/* 260 */     } catch (Symbol.CompletionFailure completionFailure) {
/* 261 */       return this.chk.completionError(paramJCTree.pos(), completionFailure);
/*     */     } finally {
/* 263 */       this.env = env;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */   <T extends JCTree> List<Type> classEnter(List<T> paramList, Env<AttrContext> paramEnv) {
/* 270 */     ListBuffer listBuffer = new ListBuffer();
/* 271 */     for (List<T> list = paramList; list.nonEmpty(); list = list.tail) {
/* 272 */       Type type = classEnter((JCTree)list.head, paramEnv);
/* 273 */       if (type != null)
/* 274 */         listBuffer.append(type);
/*     */     }
/* 276 */     return listBuffer.toList();
/*     */   }
/*     */
/*     */
/*     */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit) {
/* 281 */     JavaFileObject javaFileObject = this.log.useSource(paramJCCompilationUnit.sourcefile);
/* 282 */     boolean bool = false;
/* 283 */     boolean bool1 = paramJCCompilationUnit.sourcefile.isNameCompatible("package-info", JavaFileObject.Kind.SOURCE);
/*     */
/* 285 */     if (paramJCCompilationUnit.pid != null) {
/* 286 */       paramJCCompilationUnit.packge = this.reader.enterPackage(TreeInfo.fullName((JCTree)paramJCCompilationUnit.pid));
/* 287 */       if (paramJCCompilationUnit.packageAnnotations.nonEmpty() || this.pkginfoOpt == Option.PkgInfo.ALWAYS || paramJCCompilationUnit.docComments != null)
/*     */       {
/*     */
/* 290 */         if (bool1) {
/* 291 */           bool = true;
/* 292 */         } else if (paramJCCompilationUnit.packageAnnotations.nonEmpty()) {
/* 293 */           this.log.error(((JCTree.JCAnnotation)paramJCCompilationUnit.packageAnnotations.head).pos(), "pkg.annotations.sb.in.package-info.java", new Object[0]);
/*     */         }
/*     */       }
/*     */     } else {
/*     */
/* 298 */       paramJCCompilationUnit.packge = this.syms.unnamedPackage;
/*     */     }
/* 300 */     paramJCCompilationUnit.packge.complete();
/* 301 */     Env<AttrContext> env = topLevelEnv(paramJCCompilationUnit);
/*     */
/*     */
/* 304 */     if (bool1) {
/* 305 */       Env<AttrContext> env1 = this.typeEnvs.get((Symbol.TypeSymbol)paramJCCompilationUnit.packge);
/* 306 */       if (env1 == null) {
/* 307 */         this.typeEnvs.put((Symbol.TypeSymbol)paramJCCompilationUnit.packge, env);
/*     */       } else {
/* 309 */         JCTree.JCCompilationUnit jCCompilationUnit = env1.toplevel;
/* 310 */         if (!this.fileManager.isSameFile(paramJCCompilationUnit.sourcefile, jCCompilationUnit.sourcefile)) {
/* 311 */           this.log.warning((paramJCCompilationUnit.pid != null) ? paramJCCompilationUnit.pid.pos() : null, "pkg-info.already.seen", new Object[] { paramJCCompilationUnit.packge });
/*     */
/*     */
/*     */
/* 315 */           if (bool || (jCCompilationUnit.packageAnnotations.isEmpty() && paramJCCompilationUnit.docComments != null && paramJCCompilationUnit.docComments
/*     */
/* 317 */             .hasComment((JCTree)paramJCCompilationUnit))) {
/* 318 */             this.typeEnvs.put((Symbol.TypeSymbol)paramJCCompilationUnit.packge, env);
/*     */           }
/*     */         }
/*     */       }
/*     */
/* 323 */       for (Symbol.PackageSymbol packageSymbol = paramJCCompilationUnit.packge; packageSymbol != null && ((Symbol)packageSymbol).kind == 1; symbol = ((Symbol)packageSymbol).owner) {
/* 324 */         Symbol symbol; ((Symbol)packageSymbol).flags_field |= 0x800000L;
/*     */       }
/* 326 */       Name name = this.names.package_info;
/* 327 */       Symbol.ClassSymbol classSymbol = this.reader.enterClass(name, (Symbol.TypeSymbol)paramJCCompilationUnit.packge);
/* 328 */       classSymbol.flatname = this.names.fromString(paramJCCompilationUnit.packge + "." + name);
/* 329 */       classSymbol.sourcefile = paramJCCompilationUnit.sourcefile;
/* 330 */       classSymbol.completer = null;
/* 331 */       classSymbol.members_field = new Scope((Symbol)classSymbol);
/* 332 */       paramJCCompilationUnit.packge.package_info = classSymbol;
/*     */     }
/* 334 */     classEnter(paramJCCompilationUnit.defs, env);
/* 335 */     if (bool) {
/* 336 */       this.todo.append(env);
/*     */     }
/* 338 */     this.log.useSource(javaFileObject);
/* 339 */     this.result = null;
/*     */   }
/*     */
/*     */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/*     */     Symbol.ClassSymbol classSymbol;
/* 344 */     Symbol symbol = ((AttrContext)this.env.info).scope.owner;
/* 345 */     Scope scope = enterScope(this.env);
/*     */
/* 347 */     if (symbol.kind == 1) {
/*     */
/* 349 */       Symbol.PackageSymbol packageSymbol1 = (Symbol.PackageSymbol)symbol;
/* 350 */       for (Symbol.PackageSymbol packageSymbol2 = packageSymbol1; packageSymbol2 != null && ((Symbol)packageSymbol2).kind == 1; symbol1 = ((Symbol)packageSymbol2).owner) {
/* 351 */         Symbol symbol1; ((Symbol)packageSymbol2).flags_field |= 0x800000L;
/* 352 */       }  classSymbol = this.reader.enterClass(paramJCClassDecl.name, (Symbol.TypeSymbol)packageSymbol1);
/* 353 */       packageSymbol1.members().enterIfAbsent((Symbol)classSymbol);
/* 354 */       if ((paramJCClassDecl.mods.flags & 0x1L) != 0L && !classNameMatchesFileName(classSymbol, this.env)) {
/* 355 */         this.log.error(paramJCClassDecl.pos(), "class.public.should.be.in.file", new Object[] { paramJCClassDecl.name });
/*     */       }
/*     */     } else {
/*     */
/* 359 */       if (!paramJCClassDecl.name.isEmpty() &&
/* 360 */         !this.chk.checkUniqueClassName(paramJCClassDecl.pos(), paramJCClassDecl.name, scope)) {
/* 361 */         this.result = null;
/*     */         return;
/*     */       }
/* 364 */       if (symbol.kind == 2) {
/*     */
/* 366 */         classSymbol = this.reader.enterClass(paramJCClassDecl.name, (Symbol.TypeSymbol)symbol);
/* 367 */         if ((symbol.flags_field & 0x200L) != 0L) {
/* 368 */           paramJCClassDecl.mods.flags |= 0x9L;
/*     */         }
/*     */       } else {
/*     */
/* 372 */         classSymbol = this.reader.defineClass(paramJCClassDecl.name, symbol);
/* 373 */         classSymbol.flatname = this.chk.localClassName(classSymbol);
/* 374 */         if (!classSymbol.name.isEmpty())
/* 375 */           this.chk.checkTransparentClass(paramJCClassDecl.pos(), classSymbol, ((AttrContext)this.env.info).scope);
/*     */       }
/*     */     }
/* 378 */     paramJCClassDecl.sym = classSymbol;
/*     */
/*     */
/* 381 */     if (this.chk.compiled.get(classSymbol.flatname) != null) {
/* 382 */       duplicateClass(paramJCClassDecl.pos(), classSymbol);
/* 383 */       this.result = this.types.createErrorType(paramJCClassDecl.name, (Symbol.TypeSymbol)symbol, (Type)Type.noType);
/* 384 */       paramJCClassDecl.sym = (Symbol.ClassSymbol)this.result.tsym;
/*     */       return;
/*     */     }
/* 387 */     this.chk.compiled.put(classSymbol.flatname, classSymbol);
/* 388 */     scope.enter((Symbol)classSymbol);
/*     */
/*     */
/*     */
/* 392 */     Env<AttrContext> env = classEnv(paramJCClassDecl, this.env);
/* 393 */     this.typeEnvs.put((Symbol.TypeSymbol)classSymbol, env);
/*     */
/*     */
/* 396 */     classSymbol.completer = this.memberEnter;
/* 397 */     classSymbol.flags_field = this.chk.checkFlags(paramJCClassDecl.pos(), paramJCClassDecl.mods.flags, (Symbol)classSymbol, (JCTree)paramJCClassDecl);
/* 398 */     classSymbol.sourcefile = this.env.toplevel.sourcefile;
/* 399 */     classSymbol.members_field = new Scope((Symbol)classSymbol);
/*     */
/* 401 */     Type.ClassType classType = (Type.ClassType)classSymbol.type;
/* 402 */     if (symbol.kind != 1 && (classSymbol.flags_field & 0x8L) == 0L) {
/*     */
/*     */
/*     */
/*     */
/* 407 */       Symbol symbol1 = symbol;
/* 408 */       while ((symbol1.kind & 0x14) != 0 && (symbol1.flags_field & 0x8L) == 0L)
/*     */       {
/* 410 */         symbol1 = symbol1.owner;
/*     */       }
/* 412 */       if (symbol1.kind == 2) {
/* 413 */         classType.setEnclosingType(symbol1.type);
/*     */       }
/*     */     }
/*     */
/*     */
/* 418 */     classType.typarams_field = classEnter(paramJCClassDecl.typarams, env);
/*     */
/*     */
/*     */
/* 422 */     if (!classSymbol.isLocal() && this.uncompleted != null) this.uncompleted.append(classSymbol);
/*     */
/*     */
/*     */
/* 426 */     classEnter(paramJCClassDecl.defs, env);
/*     */
/* 428 */     this.result = classSymbol.type;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private static boolean classNameMatchesFileName(Symbol.ClassSymbol paramClassSymbol, Env<AttrContext> paramEnv) {
/* 435 */     return paramEnv.toplevel.sourcefile.isNameCompatible(paramClassSymbol.name.toString(), JavaFileObject.Kind.SOURCE);
/*     */   }
/*     */
/*     */
/*     */
/*     */   protected void duplicateClass(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol) {
/* 441 */     this.log.error(paramDiagnosticPosition, "duplicate.class", new Object[] { paramClassSymbol.fullname });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) {
/* 450 */     Type.TypeVar typeVar = (paramJCTypeParameter.type != null) ? (Type.TypeVar)paramJCTypeParameter.type : new Type.TypeVar(paramJCTypeParameter.name, ((AttrContext)this.env.info).scope.owner, this.syms.botType);
/*     */
/*     */
/* 453 */     paramJCTypeParameter.type = (Type)typeVar;
/* 454 */     if (this.chk.checkUnique(paramJCTypeParameter.pos(), (Symbol)typeVar.tsym, ((AttrContext)this.env.info).scope)) {
/* 455 */       ((AttrContext)this.env.info).scope.enter((Symbol)typeVar.tsym);
/*     */     }
/* 457 */     this.result = (Type)typeVar;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void visitTree(JCTree paramJCTree) {
/* 464 */     this.result = null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void main(List<JCTree.JCCompilationUnit> paramList) {
/* 471 */     complete(paramList, null);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void complete(List<JCTree.JCCompilationUnit> paramList, Symbol.ClassSymbol paramClassSymbol) {
/* 480 */     this.annotate.enterStart();
/* 481 */     ListBuffer<Symbol.ClassSymbol> listBuffer = this.uncompleted;
/* 482 */     if (this.memberEnter.completionEnabled) this.uncompleted = new ListBuffer();
/*     */
/*     */
/*     */     try {
/* 486 */       classEnter(paramList, (Env<AttrContext>)null);
/*     */
/*     */
/* 489 */       if (this.memberEnter.completionEnabled) {
/* 490 */         while (this.uncompleted.nonEmpty()) {
/* 491 */           Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol)this.uncompleted.next();
/* 492 */           if (paramClassSymbol == null || paramClassSymbol == classSymbol || listBuffer == null) {
/* 493 */             classSymbol.complete();
/*     */             continue;
/*     */           }
/* 496 */           listBuffer.append(classSymbol);
/*     */         }
/*     */
/*     */
/*     */
/* 501 */         for (JCTree.JCCompilationUnit jCCompilationUnit : paramList) {
/* 502 */           if (jCCompilationUnit.starImportScope.elems == null) {
/* 503 */             JavaFileObject javaFileObject = this.log.useSource(jCCompilationUnit.sourcefile);
/* 504 */             Env<AttrContext> env = topLevelEnv(jCCompilationUnit);
/* 505 */             this.memberEnter.memberEnter((JCTree)jCCompilationUnit, env);
/* 506 */             this.log.useSource(javaFileObject);
/*     */           }
/*     */         }
/*     */       }
/*     */     } finally {
/* 511 */       this.uncompleted = listBuffer;
/* 512 */       this.annotate.enterDone();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\Enter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
