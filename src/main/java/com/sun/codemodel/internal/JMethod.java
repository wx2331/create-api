/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import com.sun.codemodel.internal.util.ClassNameComparator;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JMethod
/*     */   extends JGenerifiableImpl
/*     */   implements JDeclaration, JAnnotatable, JDocCommentable
/*     */ {
/*     */   private JMods mods;
/*  51 */   private JType type = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private String name = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private final List<JVar> params = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<JClass> _throws;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private JBlock body = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private JDefinedClass outer;
/*     */ 
/*     */   
/*  79 */   private JDocComment jdoc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   private JVar varParam = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   private List<JAnnotationUse> annotations = null;
/*     */ 
/*     */   
/*     */   private boolean isConstructor() {
/*  94 */     return (this.type == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   private JExpression defaultValue = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JMethod(JDefinedClass outer, int mods, JType type, String name) {
/* 116 */     this.mods = JMods.forMethod(mods);
/* 117 */     this.type = type;
/* 118 */     this.name = name;
/* 119 */     this.outer = outer;
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
/*     */   JMethod(int mods, JDefinedClass _class) {
/* 132 */     this.mods = JMods.forMethod(mods);
/* 133 */     this.type = null;
/* 134 */     this.name = _class.name();
/* 135 */     this.outer = _class;
/*     */   }
/*     */   
/*     */   private Set<JClass> getThrows() {
/* 139 */     if (this._throws == null)
/* 140 */       this._throws = new TreeSet<>(ClassNameComparator.theInstance); 
/* 141 */     return this._throws;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMethod _throws(JClass exception) {
/* 152 */     getThrows().add(exception);
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public JMethod _throws(Class<? extends Throwable> exception) {
/* 157 */     return _throws(this.outer.owner().ref(exception));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JVar> params() {
/* 166 */     return Collections.unmodifiableList(this.params);
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
/*     */   public JVar param(int mods, JType type, String name) {
/* 182 */     JVar v = new JVar(JMods.forVar(mods), type, name, null);
/* 183 */     this.params.add(v);
/* 184 */     return v;
/*     */   }
/*     */   
/*     */   public JVar param(JType type, String name) {
/* 188 */     return param(0, type, name);
/*     */   }
/*     */   
/*     */   public JVar param(int mods, Class<?> type, String name) {
/* 192 */     return param(mods, this.outer.owner()._ref(type), name);
/*     */   }
/*     */   
/*     */   public JVar param(Class<?> type, String name) {
/* 196 */     return param(this.outer.owner()._ref(type), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar varParam(Class<?> type, String name) {
/* 203 */     return varParam(this.outer.owner()._ref(type), name);
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
/*     */   public JVar varParam(JType type, String name) {
/* 224 */     if (!hasVarArgs()) {
/*     */       
/* 226 */       this
/*     */ 
/*     */         
/* 229 */         .varParam = new JVar(JMods.forVar(0), type.array(), name, null);
/*     */ 
/*     */       
/* 232 */       return this.varParam;
/*     */     } 
/* 234 */     throw new IllegalStateException("Cannot have two varargs in a method,\nCheck if varParam method of JMethod is invoked more than once");
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
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 249 */     if (this.annotations == null)
/* 250 */       this.annotations = new ArrayList<>(); 
/* 251 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 252 */     this.annotations.add(a);
/* 253 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 263 */     return annotate(owner().ref(clazz));
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 267 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */   
/*     */   public Collection<JAnnotationUse> annotations() {
/* 271 */     if (this.annotations == null)
/* 272 */       this.annotations = new ArrayList<>(); 
/* 273 */     return Collections.unmodifiableList(this.annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasVarArgs() {
/* 281 */     return (this.varParam != null);
/*     */   }
/*     */   
/*     */   public String name() {
/* 285 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void name(String n) {
/* 292 */     this.name = n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType type() {
/* 299 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void type(JType t) {
/* 306 */     this.type = t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType[] listParamTypes() {
/* 315 */     JType[] r = new JType[this.params.size()];
/* 316 */     for (int i = 0; i < r.length; i++)
/* 317 */       r[i] = ((JVar)this.params.get(i)).type(); 
/* 318 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType listVarParamType() {
/* 327 */     if (this.varParam != null) {
/* 328 */       return this.varParam.type();
/*     */     }
/* 330 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar[] listParams() {
/* 339 */     return this.params.<JVar>toArray(new JVar[this.params.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar listVarParam() {
/* 348 */     return this.varParam;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSignature(JType[] argTypes) {
/* 355 */     JVar[] p = listParams();
/* 356 */     if (p.length != argTypes.length) {
/* 357 */       return false;
/*     */     }
/* 359 */     for (int i = 0; i < p.length; i++) {
/* 360 */       if (!p[i].type().equals(argTypes[i]))
/* 361 */         return false; 
/*     */     } 
/* 363 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock body() {
/* 372 */     if (this.body == null)
/* 373 */       this.body = new JBlock(); 
/* 374 */     return this.body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void declareDefaultValue(JExpression value) {
/* 384 */     this.defaultValue = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/* 394 */     if (this.jdoc == null)
/* 395 */       this.jdoc = new JDocComment(owner()); 
/* 396 */     return this.jdoc;
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 400 */     if (this.jdoc != null) {
/* 401 */       f.g(this.jdoc);
/*     */     }
/* 403 */     if (this.annotations != null) {
/* 404 */       for (JAnnotationUse a : this.annotations) {
/* 405 */         f.g(a).nl();
/*     */       }
/*     */     }
/* 408 */     f.g(this.mods);
/*     */ 
/*     */     
/* 411 */     super.declare(f);
/*     */     
/* 413 */     if (!isConstructor())
/* 414 */       f.g(this.type); 
/* 415 */     f.id(this.name).p('(').i();
/*     */ 
/*     */     
/* 418 */     boolean first = true;
/* 419 */     for (JVar var : this.params) {
/* 420 */       if (!first)
/* 421 */         f.p(','); 
/* 422 */       if (var.isAnnotated())
/* 423 */         f.nl(); 
/* 424 */       f.b(var);
/* 425 */       first = false;
/*     */     } 
/* 427 */     if (hasVarArgs()) {
/* 428 */       if (!first)
/* 429 */         f.p(','); 
/* 430 */       f.g(this.varParam.type().elementType());
/* 431 */       f.p("... ");
/* 432 */       f.id(this.varParam.name());
/*     */     } 
/*     */     
/* 435 */     f.o().p(')');
/* 436 */     if (this._throws != null && !this._throws.isEmpty()) {
/* 437 */       f.nl().i().p("throws").g((Collection)this._throws).nl().o();
/*     */     }
/*     */     
/* 440 */     if (this.defaultValue != null) {
/* 441 */       f.p("default ");
/* 442 */       f.g(this.defaultValue);
/*     */     } 
/* 444 */     if (this.body != null) {
/* 445 */       f.s(this.body);
/*     */     }
/* 447 */     else if (!this.outer.isInterface() && !this.outer.isAnnotationTypeDeclaration() && !this.mods.isAbstract() && !this.mods.isNative()) {
/*     */       
/* 449 */       f.s(new JBlock());
/*     */     } else {
/* 451 */       f.p(';').nl();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMods mods() {
/* 461 */     return this.mods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMods getMods() {
/* 468 */     return this.mods;
/*     */   }
/*     */   
/*     */   protected JCodeModel owner() {
/* 472 */     return this.outer.owner();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */