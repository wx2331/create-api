/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JPackage
/*     */   implements JDeclaration, JGenerable, JClassContainer, JAnnotatable, Comparable<JPackage>, JDocCommentable
/*     */ {
/*     */   private String name;
/*     */   private final JCodeModel owner;
/*  64 */   private final Map<String, JDefinedClass> classes = new TreeMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private final Set<JResourceFile> resources = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, JDefinedClass> upperCaseClassMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private List<JAnnotationUse> annotations = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   private JDocComment jdoc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JPackage(String name, JCodeModel cw) {
/* 101 */     this.owner = cw;
/* 102 */     if (name.equals(".")) {
/* 103 */       String msg = "Package name . is not allowed";
/* 104 */       throw new IllegalArgumentException(msg);
/*     */     } 
/*     */     
/* 107 */     if (JCodeModel.isCaseSensitiveFileSystem) {
/* 108 */       this.upperCaseClassMap = null;
/*     */     } else {
/* 110 */       this.upperCaseClassMap = new HashMap<>();
/*     */     } 
/* 112 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public JClassContainer parentContainer() {
/* 117 */     return parent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage parent() {
/* 124 */     if (this.name.length() == 0) return null;
/*     */     
/* 126 */     int idx = this.name.lastIndexOf('.');
/* 127 */     return this.owner._package(this.name.substring(0, idx));
/*     */   }
/*     */   
/* 130 */   public boolean isClass() { return false; }
/* 131 */   public boolean isPackage() { return true; } public JPackage getPackage() {
/* 132 */     return this;
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
/*     */   public JDefinedClass _class(int mods, String name) throws JClassAlreadyExistsException {
/* 149 */     return _class(mods, name, ClassType.CLASS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(int mods, String name, boolean isInterface) throws JClassAlreadyExistsException {
/* 157 */     return _class(mods, name, isInterface ? ClassType.INTERFACE : ClassType.CLASS);
/*     */   }
/*     */   
/*     */   public JDefinedClass _class(int mods, String name, ClassType classTypeVal) throws JClassAlreadyExistsException {
/* 161 */     if (this.classes.containsKey(name)) {
/* 162 */       throw new JClassAlreadyExistsException((JDefinedClass)this.classes.get(name));
/*     */     }
/*     */     
/* 165 */     JDefinedClass c = new JDefinedClass(this, mods, name, classTypeVal);
/*     */     
/* 167 */     if (this.upperCaseClassMap != null) {
/* 168 */       JDefinedClass dc = this.upperCaseClassMap.get(name.toUpperCase());
/* 169 */       if (dc != null)
/* 170 */         throw new JClassAlreadyExistsException(dc); 
/* 171 */       this.upperCaseClassMap.put(name.toUpperCase(), c);
/*     */     } 
/* 173 */     this.classes.put(name, c);
/* 174 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(String name) throws JClassAlreadyExistsException {
/* 182 */     return _class(1, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _getClass(String name) {
/* 192 */     if (this.classes.containsKey(name)) {
/* 193 */       return this.classes.get(name);
/*     */     }
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(JPackage that) {
/* 202 */     return this.name.compareTo(that.name);
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
/*     */   public JDefinedClass _interface(int mods, String name) throws JClassAlreadyExistsException {
/* 217 */     return _class(mods, name, ClassType.INTERFACE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _interface(String name) throws JClassAlreadyExistsException {
/* 224 */     return _interface(1, name);
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
/*     */   public JDefinedClass _annotationTypeDeclaration(String name) throws JClassAlreadyExistsException {
/* 238 */     return _class(1, name, ClassType.ANNOTATION_TYPE_DECL);
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
/*     */   public JDefinedClass _enum(String name) throws JClassAlreadyExistsException {
/* 252 */     return _class(1, name, ClassType.ENUM);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JResourceFile addResourceFile(JResourceFile rsrc) {
/* 258 */     this.resources.add(rsrc);
/* 259 */     return rsrc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasResourceFile(String name) {
/* 266 */     for (JResourceFile r : this.resources) {
/* 267 */       if (r.name().equals(name))
/* 268 */         return true; 
/* 269 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JResourceFile> propertyFiles() {
/* 276 */     return this.resources.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/* 286 */     if (this.jdoc == null)
/* 287 */       this.jdoc = new JDocComment(owner()); 
/* 288 */     return this.jdoc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(JClass c) {
/* 295 */     if (c._package() != this) {
/* 296 */       throw new IllegalArgumentException("the specified class is not a member of this package, or it is a referenced class");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 301 */     this.classes.remove(c.name());
/* 302 */     if (this.upperCaseClassMap != null) {
/* 303 */       this.upperCaseClassMap.remove(c.name().toUpperCase());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass ref(String name) throws ClassNotFoundException {
/* 310 */     if (name.indexOf('.') >= 0) {
/* 311 */       throw new IllegalArgumentException("JClass name contains '.': " + name);
/*     */     }
/* 313 */     String n = "";
/* 314 */     if (!isUnnamed())
/* 315 */       n = this.name + '.'; 
/* 316 */     n = n + name;
/*     */     
/* 318 */     return this.owner.ref(Class.forName(n));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage subPackage(String pkg) {
/* 325 */     if (isUnnamed()) return owner()._package(pkg); 
/* 326 */     return owner()._package(this.name + '.' + pkg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JDefinedClass> classes() {
/* 334 */     return this.classes.values().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefined(String classLocalName) {
/* 341 */     Iterator<JDefinedClass> itr = classes();
/* 342 */     while (itr.hasNext()) {
/* 343 */       if (((JDefinedClass)itr.next()).name().equals(classLocalName)) {
/* 344 */         return true;
/*     */       }
/*     */     } 
/* 347 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isUnnamed() {
/* 353 */     return (this.name.length() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 364 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final JCodeModel owner() {
/* 370 */     return this.owner;
/*     */   }
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 374 */     if (isUnnamed())
/* 375 */       throw new IllegalArgumentException("the root package cannot be annotated"); 
/* 376 */     if (this.annotations == null)
/* 377 */       this.annotations = new ArrayList<>(); 
/* 378 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 379 */     this.annotations.add(a);
/* 380 */     return a;
/*     */   }
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 384 */     return annotate(this.owner.ref(clazz));
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 388 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */   
/*     */   public Collection<JAnnotationUse> annotations() {
/* 392 */     if (this.annotations == null)
/* 393 */       this.annotations = new ArrayList<>(); 
/* 394 */     return Collections.unmodifiableList(this.annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   File toPath(File dir) {
/* 401 */     if (this.name == null) return dir; 
/* 402 */     return new File(dir, this.name.replace('.', File.separatorChar));
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 406 */     if (this.name.length() != 0)
/* 407 */       f.p("package").p(this.name).p(';').nl(); 
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 411 */     f.p(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void build(CodeWriter src, CodeWriter res) throws IOException {
/* 418 */     for (JDefinedClass c : this.classes.values()) {
/* 419 */       if (c.isHidden()) {
/*     */         continue;
/*     */       }
/* 422 */       JFormatter f = createJavaSourceFileWriter(src, c.name());
/* 423 */       f.write(c);
/* 424 */       f.close();
/*     */     } 
/*     */ 
/*     */     
/* 428 */     if (this.annotations != null || this.jdoc != null) {
/* 429 */       JFormatter f = createJavaSourceFileWriter(src, "package-info");
/*     */       
/* 431 */       if (this.jdoc != null) {
/* 432 */         f.g(this.jdoc);
/*     */       }
/*     */       
/* 435 */       if (this.annotations != null)
/* 436 */         for (JAnnotationUse a : this.annotations) {
/* 437 */           f.g(a).nl();
/*     */         } 
/* 439 */       f.d(this);
/*     */       
/* 441 */       f.close();
/*     */     } 
/*     */ 
/*     */     
/* 445 */     for (JResourceFile rsrc : this.resources) {
/* 446 */       CodeWriter cw = rsrc.isResource() ? res : src;
/* 447 */       OutputStream os = new BufferedOutputStream(cw.openBinary(this, rsrc.name()));
/* 448 */       rsrc.build(os);
/* 449 */       os.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   int countArtifacts() {
/* 454 */     int r = 0;
/* 455 */     for (JDefinedClass c : this.classes.values()) {
/* 456 */       if (c.isHidden())
/*     */         continue; 
/* 458 */       r++;
/*     */     } 
/*     */     
/* 461 */     if (this.annotations != null || this.jdoc != null) {
/* 462 */       r++;
/*     */     }
/*     */     
/* 465 */     r += this.resources.size();
/*     */     
/* 467 */     return r;
/*     */   }
/*     */   
/*     */   private JFormatter createJavaSourceFileWriter(CodeWriter src, String className) throws IOException {
/* 471 */     Writer bw = new BufferedWriter(src.openSource(this, className + ".java"));
/* 472 */     return new JFormatter(new PrintWriter(bw));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JPackage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */