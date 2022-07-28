/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.comp.Attr;
/*     */ import com.sun.tools.javac.comp.AttrContext;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.main.CommandLine;
/*     */ import com.sun.tools.javac.main.JavaCompiler;
/*     */ import com.sun.tools.javac.main.Main;
/*     */ import com.sun.tools.javac.model.JavacElements;
/*     */ import com.sun.tools.javac.model.JavacTypes;
/*     */ import com.sun.tools.javac.parser.JavacParser;
/*     */ import com.sun.tools.javac.parser.ParserFactory;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.annotation.processing.Processor;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.Types;
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
/*     */ public class JavacTaskImpl
/*     */   extends BasicJavacTask
/*     */ {
/*     */   private Main compilerMain;
/*     */   private JavaCompiler compiler;
/*     */   private Locale locale;
/*     */   private String[] args;
/*     */   private String[] classNames;
/*     */   private List<JavaFileObject> fileObjects;
/*     */   private Map<JavaFileObject, JCTree.JCCompilationUnit> notYetEntered;
/*     */   private ListBuffer<Env<AttrContext>> genList;
/*  76 */   private final AtomicBoolean used = new AtomicBoolean();
/*     */   
/*     */   private Iterable<? extends Processor> processors;
/*  79 */   private Main.Result result = null;
/*     */   private boolean parsed;
/*     */   JavacTaskImpl(Main paramMain, Iterable<String> paramIterable1, Context paramContext, Iterable<String> paramIterable2, Iterable<? extends JavaFileObject> paramIterable) { this(paramMain, toArray(paramIterable1), toArray(paramIterable2), paramContext, toList(paramIterable)); }
/*     */   private static String[] toArray(Iterable<String> paramIterable) { ListBuffer listBuffer = new ListBuffer(); if (paramIterable != null) for (String str : paramIterable) listBuffer.append(str);   return (String[])listBuffer.toArray((Object[])new String[listBuffer.length()]); }
/*     */   private static List<JavaFileObject> toList(Iterable<? extends JavaFileObject> paramIterable) { if (paramIterable == null)
/*     */       return List.nil();  ListBuffer listBuffer = new ListBuffer(); for (JavaFileObject javaFileObject : paramIterable)
/*     */       listBuffer.append(javaFileObject);  return listBuffer.toList(); }
/*  86 */   public Main.Result doCall() { if (!this.used.getAndSet(true)) { initContext(); this.notYetEntered = new HashMap<>(); this.compilerMain.setAPIMode(true); this.result = this.compilerMain.compile(this.args, this.classNames, this.context, this.fileObjects, this.processors); cleanup(); return this.result; }  throw new IllegalStateException("multiple calls to method 'call'"); } public Boolean call() { return Boolean.valueOf(doCall().isOK()); } JavacTaskImpl(Main paramMain, String[] paramArrayOfString1, String[] paramArrayOfString2, Context paramContext, List<JavaFileObject> paramList) { super(null, false);
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
/* 249 */     this.parsed = false; this.compilerMain = paramMain; this.args = paramArrayOfString1; this.classNames = paramArrayOfString2;
/*     */     this.context = paramContext;
/*     */     this.fileObjects = paramList;
/*     */     setLocale(Locale.getDefault());
/*     */     paramMain.getClass();
/*     */     paramArrayOfString1.getClass();
/*     */     paramList.getClass(); }
/*     */   public void setProcessors(Iterable<? extends Processor> paramIterable) { paramIterable.getClass();
/*     */     if (this.used.get())
/*     */       throw new IllegalStateException(); 
/* 259 */     this.processors = paramIterable; } public Iterable<? extends TypeElement> enter() throws IOException { return enter(null); } public void setLocale(Locale paramLocale) { if (this.used.get())
/*     */       throw new IllegalStateException();  this.locale = paramLocale; }
/*     */   private void prepareCompiler() throws IOException { if (this.used.getAndSet(true)) {
/*     */       if (this.compiler == null)
/*     */         throw new IllegalStateException(); 
/*     */     } else {
/*     */       initContext(); this.compilerMain.log = Log.instance(this.context); this.compilerMain.setOptions(Options.instance(this.context)); this.compilerMain.filenames = new LinkedHashSet(); Collection collection = this.compilerMain.processArgs(CommandLine.parse(this.args), this.classNames); if (collection != null && !collection.isEmpty())
/*     */         throw new IllegalArgumentException("Malformed arguments " + toString(collection, " "));  this.compiler = JavaCompiler.instance(this.context); this.compiler.keepComments = true; this.compiler.genEndPos = true; this.compiler.initProcessAnnotations(this.processors); this.notYetEntered = new HashMap<>(); for (JavaFileObject javaFileObject : this.fileObjects)
/*     */         this.notYetEntered.put(javaFileObject, null); 
/*     */       this.genList = new ListBuffer();
/*     */       this.args = null;
/*     */       this.classNames = null;
/*     */     }  }
/*     */   <T> String toString(Iterable<T> paramIterable, String paramString) { // Byte code:
/*     */     //   0: ldc ''
/*     */     //   2: astore_3
/*     */     //   3: new java/lang/StringBuilder
/*     */     //   6: dup
/*     */     //   7: invokespecial <init> : ()V
/*     */     //   10: astore #4
/*     */     //   12: aload_1
/*     */     //   13: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   18: astore #5
/*     */     //   20: aload #5
/*     */     //   22: invokeinterface hasNext : ()Z
/*     */     //   27: ifeq -> 62
/*     */     //   30: aload #5
/*     */     //   32: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   37: astore #6
/*     */     //   39: aload #4
/*     */     //   41: aload_3
/*     */     //   42: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   45: pop
/*     */     //   46: aload #4
/*     */     //   48: aload #6
/*     */     //   50: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   53: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   56: pop
/*     */     //   57: aload_2
/*     */     //   58: astore_3
/*     */     //   59: goto -> 20
/*     */     //   62: aload #4
/*     */     //   64: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   67: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #184	-> 0
/*     */     //   #185	-> 3
/*     */     //   #186	-> 12
/*     */     //   #187	-> 39
/*     */     //   #188	-> 46
/*     */     //   #189	-> 57
/*     */     //   #190	-> 59
/*     */     //   #191	-> 62 }
/* 273 */   public Iterable<? extends TypeElement> enter(Iterable<? extends CompilationUnitTree> paramIterable) throws IOException { if (paramIterable == null && this.notYetEntered != null && this.notYetEntered.isEmpty()) {
/* 274 */       return (Iterable<? extends TypeElement>)List.nil();
/*     */     }
/* 276 */     prepareCompiler();
/*     */     
/* 278 */     ListBuffer listBuffer = null;
/*     */     
/* 280 */     if (paramIterable == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 285 */       if (this.notYetEntered.size() > 0) {
/* 286 */         if (!this.parsed)
/* 287 */           parse(); 
/* 288 */         for (JavaFileObject javaFileObject : this.fileObjects) {
/* 289 */           JCTree.JCCompilationUnit jCCompilationUnit = this.notYetEntered.remove(javaFileObject);
/* 290 */           if (jCCompilationUnit != null) {
/* 291 */             if (listBuffer == null)
/* 292 */               listBuffer = new ListBuffer(); 
/* 293 */             listBuffer.append(jCCompilationUnit);
/*     */           } 
/*     */         } 
/* 296 */         this.notYetEntered.clear();
/*     */       } 
/*     */     } else {
/*     */       
/* 300 */       for (CompilationUnitTree compilationUnitTree : paramIterable) {
/* 301 */         if (compilationUnitTree instanceof JCTree.JCCompilationUnit) {
/* 302 */           if (listBuffer == null)
/* 303 */             listBuffer = new ListBuffer(); 
/* 304 */           listBuffer.append(compilationUnitTree);
/* 305 */           this.notYetEntered.remove(compilationUnitTree.getSourceFile());
/*     */           continue;
/*     */         } 
/* 308 */         throw new IllegalArgumentException(compilationUnitTree.toString());
/*     */       } 
/*     */     } 
/*     */     
/* 312 */     if (listBuffer == null) {
/* 313 */       return (Iterable<? extends TypeElement>)List.nil();
/*     */     }
/*     */     try {
/* 316 */       List list = this.compiler.enterTrees(listBuffer.toList());
/*     */       
/* 318 */       if (this.notYetEntered.isEmpty()) {
/* 319 */         this.compiler = this.compiler.processAnnotations(list);
/*     */       }
/* 321 */       ListBuffer listBuffer1 = new ListBuffer();
/* 322 */       for (JCTree.JCCompilationUnit jCCompilationUnit : list) {
/* 323 */         for (JCTree jCTree : jCCompilationUnit.defs) {
/* 324 */           if (jCTree.hasTag(JCTree.Tag.CLASSDEF)) {
/* 325 */             JCTree.JCClassDecl jCClassDecl = (JCTree.JCClassDecl)jCTree;
/* 326 */             if (jCClassDecl.sym != null)
/* 327 */               listBuffer1.append(jCClassDecl.sym); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 331 */       return (Iterable<? extends TypeElement>)listBuffer1.toList();
/*     */     } finally {
/*     */       
/* 334 */       this.compiler.log.flush();
/*     */     }  } private void initContext() { this.context.put(JavacTask.class, this); this.context.put(Locale.class, this.locale); }
/*     */   void cleanup() { if (this.compiler != null)
/*     */       this.compiler.close();  this.compiler = null;
/*     */     this.compilerMain = null;
/*     */     this.args = null;
/*     */     this.classNames = null;
/*     */     this.context = null;
/*     */     this.fileObjects = null;
/*     */     this.notYetEntered = null; }
/* 344 */   public Iterable<? extends Element> analyze() throws IOException { return analyze(null); } public JavaFileObject asJavaFileObject(File paramFile) { JavacFileManager javacFileManager = (JavacFileManager)this.context.get(JavaFileManager.class); return javacFileManager.getRegularFile(paramFile); }
/*     */   public Iterable<? extends CompilationUnitTree> parse() throws IOException { try {
/*     */       prepareCompiler();
/*     */       List list = this.compiler.parseFiles((Iterable)this.fileObjects);
/*     */       for (JCTree.JCCompilationUnit jCCompilationUnit : list) {
/*     */         JavaFileObject javaFileObject = jCCompilationUnit.getSourceFile();
/*     */         if (this.notYetEntered.containsKey(javaFileObject))
/*     */           this.notYetEntered.put(javaFileObject, jCCompilationUnit); 
/*     */       } 
/*     */       return (Iterable<? extends CompilationUnitTree>)list;
/*     */     } finally {
/*     */       this.parsed = true;
/*     */       if (this.compiler != null && this.compiler.log != null)
/*     */         this.compiler.log.flush(); 
/*     */     }  }
/* 359 */   public Iterable<? extends Element> analyze(Iterable<? extends TypeElement> paramIterable) throws IOException { enter(null);
/*     */     
/* 361 */     final ListBuffer<Element> results = new ListBuffer();
/*     */     try {
/* 363 */       if (paramIterable == null) {
/* 364 */         handleFlowResults(this.compiler.flow(this.compiler.attribute((Queue)this.compiler.todo)), listBuffer);
/*     */       } else {
/* 366 */         Filter filter = new Filter() {
/*     */             public void process(Env<AttrContext> param1Env) {
/* 368 */               JavacTaskImpl.this.handleFlowResults(JavacTaskImpl.this.compiler.flow(JavacTaskImpl.this.compiler.attribute(param1Env)), results);
/*     */             }
/*     */           };
/* 371 */         filter.run((Queue<Env<AttrContext>>)this.compiler.todo, paramIterable);
/*     */       } 
/*     */     } finally {
/* 374 */       this.compiler.log.flush();
/*     */     } 
/* 376 */     return (Iterable<? extends Element>)listBuffer; }
/*     */ 
/*     */   
/*     */   private void handleFlowResults(Queue<Env<AttrContext>> paramQueue, ListBuffer<Element> paramListBuffer) {
/* 380 */     for (Env<AttrContext> env : paramQueue) {
/* 381 */       JCTree.JCClassDecl jCClassDecl; JCTree.JCCompilationUnit jCCompilationUnit; switch (env.tree.getTag()) {
/*     */         case CLASSDEF:
/* 383 */           jCClassDecl = (JCTree.JCClassDecl)env.tree;
/* 384 */           if (jCClassDecl.sym != null) {
/* 385 */             paramListBuffer.append(jCClassDecl.sym);
/*     */           }
/*     */         case TOPLEVEL:
/* 388 */           jCCompilationUnit = (JCTree.JCCompilationUnit)env.tree;
/* 389 */           if (jCCompilationUnit.packge != null) {
/* 390 */             paramListBuffer.append(jCCompilationUnit.packge);
/*     */           }
/*     */       } 
/*     */     } 
/* 394 */     this.genList.addAll(paramQueue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<? extends JavaFileObject> generate() throws IOException {
/* 404 */     return generate(null);
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
/*     */   public Iterable<? extends JavaFileObject> generate(Iterable<? extends TypeElement> paramIterable) throws IOException {
/* 417 */     final ListBuffer results = new ListBuffer();
/*     */     try {
/* 419 */       analyze(null);
/*     */       
/* 421 */       if (paramIterable == null) {
/* 422 */         this.compiler.generate(this.compiler.desugar((Queue)this.genList), (Queue)listBuffer);
/* 423 */         this.genList.clear();
/*     */       } else {
/*     */         
/* 426 */         Filter filter = new Filter() {
/*     */             public void process(Env<AttrContext> param1Env) {
/* 428 */               JavacTaskImpl.this.compiler.generate(JavacTaskImpl.this.compiler.desugar((Queue)ListBuffer.of(param1Env)), (Queue)results);
/*     */             }
/*     */           };
/* 431 */         filter.run((Queue<Env<AttrContext>>)this.genList, paramIterable);
/*     */       } 
/* 433 */       if (this.genList.isEmpty()) {
/* 434 */         this.compiler.reportDeferredDiagnostics();
/* 435 */         cleanup();
/*     */       } 
/*     */     } finally {
/*     */       
/* 439 */       if (this.compiler != null)
/* 440 */         this.compiler.log.flush(); 
/*     */     } 
/* 442 */     return (Iterable<? extends JavaFileObject>)listBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeMirror getTypeMirror(Iterable<? extends Tree> paramIterable) {
/* 447 */     Tree tree = null;
/* 448 */     for (Tree tree1 : paramIterable)
/* 449 */       tree = tree1; 
/* 450 */     return (TypeMirror)((JCTree)tree).type;
/*     */   }
/*     */   
/*     */   public JavacElements getElements() {
/* 454 */     if (this.context == null)
/* 455 */       throw new IllegalStateException(); 
/* 456 */     return JavacElements.instance(this.context);
/*     */   }
/*     */   
/*     */   public JavacTypes getTypes() {
/* 460 */     if (this.context == null)
/* 461 */       throw new IllegalStateException(); 
/* 462 */     return JavacTypes.instance(this.context);
/*     */   }
/*     */   
/*     */   public Iterable<? extends Tree> pathFor(CompilationUnitTree paramCompilationUnitTree, Tree paramTree) {
/* 466 */     return (Iterable<? extends Tree>)TreeInfo.pathFor((JCTree)paramTree, (JCTree.JCCompilationUnit)paramCompilationUnitTree).reverse();
/*     */   }
/*     */   
/*     */   abstract class Filter {
/*     */     void run(Queue<Env<AttrContext>> param1Queue, Iterable<? extends TypeElement> param1Iterable) {
/* 471 */       HashSet<TypeElement> hashSet = new HashSet();
/* 472 */       for (TypeElement typeElement : param1Iterable) {
/* 473 */         hashSet.add(typeElement);
/*     */       }
/* 475 */       ListBuffer listBuffer = new ListBuffer();
/* 476 */       while (param1Queue.peek() != null) {
/* 477 */         Env<AttrContext> env = param1Queue.remove();
/* 478 */         Symbol.ClassSymbol classSymbol = env.enclClass.sym;
/* 479 */         if (classSymbol != null && hashSet.contains(classSymbol.outermostClass())) {
/* 480 */           process(env); continue;
/*     */         } 
/* 482 */         listBuffer = listBuffer.append(env);
/*     */       } 
/*     */       
/* 485 */       param1Queue.addAll((Collection<? extends Env<AttrContext>>)listBuffer);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void process(Env<AttrContext> param1Env);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Type parseType(String paramString, TypeElement paramTypeElement) {
/* 496 */     if (paramString == null || paramString.equals(""))
/* 497 */       throw new IllegalArgumentException(); 
/* 498 */     this.compiler = JavaCompiler.instance(this.context);
/* 499 */     JavaFileObject javaFileObject = this.compiler.log.useSource(null);
/* 500 */     ParserFactory parserFactory = ParserFactory.instance(this.context);
/* 501 */     Attr attr = Attr.instance(this.context);
/*     */     try {
/* 503 */       CharBuffer charBuffer = CharBuffer.wrap((paramString + "\000").toCharArray(), 0, paramString.length());
/* 504 */       JavacParser javacParser = parserFactory.newParser(charBuffer, false, false, false);
/* 505 */       JCTree.JCExpression jCExpression = javacParser.parseType();
/* 506 */       return attr.attribType((JCTree)jCExpression, (Symbol.TypeSymbol)paramTypeElement);
/*     */     } finally {
/* 508 */       this.compiler.log.useSource(javaFileObject);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\JavacTaskImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */