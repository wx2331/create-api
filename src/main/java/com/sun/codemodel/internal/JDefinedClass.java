/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDefinedClass
/*     */   extends JClass
/*     */   implements JDeclaration, JClassContainer, JGenerifiable, JAnnotatable, JDocCommentable
/*     */ {
/*  57 */   private String name = null;
/*     */ 
/*     */   
/*     */   private JMods mods;
/*     */ 
/*     */   
/*     */   private JClass superClass;
/*     */ 
/*     */   
/*  66 */   private final Set<JClass> interfaces = new TreeSet<>();
/*     */ 
/*     */   
/*  69 */   final Map<String, JFieldVar> fields = new LinkedHashMap<>();
/*     */ 
/*     */   
/*  72 */   private JBlock init = null;
/*     */ 
/*     */   
/*  75 */   private JDocComment jdoc = null;
/*     */ 
/*     */   
/*  78 */   private final List<JMethod> constructors = new ArrayList<>();
/*     */ 
/*     */   
/*  81 */   private final List<JMethod> methods = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, JDefinedClass> classes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hideFile = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object metadata;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String directBlock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   private JClassContainer outer = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassType classType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   private final Map<String, JEnumConstant> enumConstantsByName = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   private List<JAnnotationUse> annotations = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   private final JGenerifiableImpl generifiable = new JGenerifiableImpl() {
/*     */       protected JCodeModel owner() {
/* 153 */         return JDefinedClass.this.owner();
/*     */       }
/*     */     };
/*     */   
/*     */   JDefinedClass(JClassContainer parent, int mods, String name, ClassType classTypeval) {
/* 158 */     this(mods, name, parent, parent.owner(), classTypeval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JDefinedClass(JCodeModel owner, int mods, String name) {
/* 168 */     this(mods, name, (JClassContainer)null, owner);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JDefinedClass(int mods, String name, JClassContainer parent, JCodeModel owner) {
/* 176 */     this(mods, name, parent, owner, ClassType.CLASS);
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
/*     */   private JDefinedClass(int mods, String name, JClassContainer parent, JCodeModel owner, ClassType classTypeVal) {
/* 194 */     super(owner);
/*     */     
/* 196 */     if (name != null) {
/* 197 */       if (name.trim().length() == 0) {
/* 198 */         throw new IllegalArgumentException("JClass name empty");
/*     */       }
/* 200 */       if (!Character.isJavaIdentifierStart(name.charAt(0))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 206 */         String msg = "JClass name " + name + " contains illegal character for beginning of identifier: " + name.charAt(0);
/* 207 */         throw new IllegalArgumentException(msg);
/*     */       } 
/* 209 */       for (int i = 1; i < name.length(); i++) {
/* 210 */         if (!Character.isJavaIdentifierPart(name.charAt(i))) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 215 */           String msg = "JClass name " + name + " contains illegal character " + name.charAt(i);
/* 216 */           throw new IllegalArgumentException(msg);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 221 */     this.classType = classTypeVal;
/* 222 */     if (isInterface()) {
/* 223 */       this.mods = JMods.forInterface(mods);
/*     */     } else {
/* 225 */       this.mods = JMods.forClass(mods);
/*     */     } 
/* 227 */     this.name = name;
/*     */     
/* 229 */     this.outer = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isAnonymous() {
/* 236 */     return (this.name == null);
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
/*     */   public JDefinedClass _extends(JClass superClass) {
/* 248 */     if (this.classType == ClassType.INTERFACE) {
/* 249 */       if (superClass.isInterface())
/* 250 */         return _implements(superClass); 
/* 251 */       throw new IllegalArgumentException("unable to set the super class for an interface");
/* 252 */     }  if (superClass == null) {
/* 253 */       throw new NullPointerException();
/*     */     }
/* 255 */     for (JClass o = superClass.outer(); o != null; o = o.outer()) {
/* 256 */       if (this == o) {
/* 257 */         throw new IllegalArgumentException("Illegal class inheritance loop.  Outer class " + this.name + " may not subclass from inner class: " + o
/* 258 */             .name());
/*     */       }
/*     */     } 
/*     */     
/* 262 */     this.superClass = superClass;
/* 263 */     return this;
/*     */   }
/*     */   
/*     */   public JDefinedClass _extends(Class<?> superClass) {
/* 267 */     return _extends(owner().ref(superClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass _extends() {
/* 274 */     if (this.superClass == null)
/* 275 */       this.superClass = owner().ref(Object.class); 
/* 276 */     return this.superClass;
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
/*     */   public JDefinedClass _implements(JClass iface) {
/* 288 */     this.interfaces.add(iface);
/* 289 */     return this;
/*     */   }
/*     */   
/*     */   public JDefinedClass _implements(Class<?> iface) {
/* 293 */     return _implements(owner().ref(iface));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JClass> _implements() {
/* 301 */     return this.interfaces.iterator();
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
/*     */   public String name() {
/* 314 */     return this.name;
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
/*     */   public JEnumConstant enumConstant(String name) {
/* 328 */     JEnumConstant ec = this.enumConstantsByName.get(name);
/* 329 */     if (null == ec) {
/* 330 */       ec = new JEnumConstant(this, name);
/* 331 */       this.enumConstantsByName.put(name, ec);
/*     */     } 
/* 333 */     return ec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String fullName() {
/* 340 */     if (this.outer instanceof JDefinedClass) {
/* 341 */       return ((JDefinedClass)this.outer).fullName() + '.' + name();
/*     */     }
/* 343 */     JPackage p = _package();
/* 344 */     if (p.isUnnamed()) {
/* 345 */       return name();
/*     */     }
/* 347 */     return p.name() + '.' + name();
/*     */   }
/*     */ 
/*     */   
/*     */   public String binaryName() {
/* 352 */     if (this.outer instanceof JDefinedClass) {
/* 353 */       return ((JDefinedClass)this.outer).binaryName() + '$' + name();
/*     */     }
/* 355 */     return fullName();
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/* 359 */     return (this.classType == ClassType.INTERFACE);
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 363 */     return this.mods.isAbstract();
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
/*     */   public JFieldVar field(int mods, JType type, String name) {
/* 381 */     return field(mods, type, name, (JExpression)null);
/*     */   }
/*     */   
/*     */   public JFieldVar field(int mods, Class<?> type, String name) {
/* 385 */     return field(mods, owner()._ref(type), name);
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
/*     */   public JFieldVar field(int mods, JType type, String name, JExpression init) {
/* 407 */     JFieldVar f = new JFieldVar(this, JMods.forField(mods), type, name, init);
/*     */     
/* 409 */     if (this.fields.containsKey(name)) {
/* 410 */       throw new IllegalArgumentException("trying to create the same field twice: " + name);
/*     */     }
/*     */     
/* 413 */     this.fields.put(name, f);
/* 414 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnnotationTypeDeclaration() {
/* 422 */     return (this.classType == ClassType.ANNOTATION_TYPE_DECL);
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
/*     */   public JDefinedClass _annotationTypeDeclaration(String name) throws JClassAlreadyExistsException {
/* 438 */     return _class(1, name, ClassType.ANNOTATION_TYPE_DECL);
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
/* 452 */     return _class(1, name, ClassType.ENUM);
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
/*     */   public JDefinedClass _enum(int mods, String name) throws JClassAlreadyExistsException {
/* 468 */     return _class(mods, name, ClassType.ENUM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassType getClassType() {
/* 476 */     return this.classType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JFieldVar field(int mods, Class<?> type, String name, JExpression init) {
/* 484 */     return field(mods, owner()._ref(type), name, init);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, JFieldVar> fields() {
/* 494 */     return Collections.unmodifiableMap(this.fields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeField(JFieldVar field) {
/* 504 */     if (this.fields.remove(field.name()) != field) {
/* 505 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock init() {
/* 515 */     if (this.init == null)
/* 516 */       this.init = new JBlock(); 
/* 517 */     return this.init;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMethod constructor(int mods) {
/* 527 */     JMethod c = new JMethod(mods, this);
/* 528 */     this.constructors.add(c);
/* 529 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JMethod> constructors() {
/* 536 */     return this.constructors.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMethod getConstructor(JType[] argTypes) {
/* 547 */     for (JMethod m : this.constructors) {
/* 548 */       if (m.hasSignature(argTypes))
/* 549 */         return m; 
/*     */     } 
/* 551 */     return null;
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
/*     */   public JMethod method(int mods, JType type, String name) {
/* 570 */     JMethod m = new JMethod(this, mods, type, name);
/* 571 */     this.methods.add(m);
/* 572 */     return m;
/*     */   }
/*     */   
/*     */   public JMethod method(int mods, Class<?> type, String name) {
/* 576 */     return method(mods, owner()._ref(type), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<JMethod> methods() {
/* 583 */     return this.methods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMethod getMethod(String name, JType[] argTypes) {
/* 594 */     for (JMethod m : this.methods) {
/* 595 */       if (!m.name().equals(name)) {
/*     */         continue;
/*     */       }
/* 598 */       if (m.hasSignature(argTypes))
/* 599 */         return m; 
/*     */     } 
/* 601 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isClass() {
/* 605 */     return true;
/*     */   }
/*     */   public boolean isPackage() {
/* 608 */     return false;
/*     */   } public JPackage getPackage() {
/* 610 */     return parentContainer().getPackage();
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
/*     */   public JDefinedClass _class(int mods, String name) throws JClassAlreadyExistsException {
/* 625 */     return _class(mods, name, ClassType.CLASS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(int mods, String name, boolean isInterface) throws JClassAlreadyExistsException {
/* 634 */     return _class(mods, name, isInterface ? ClassType.INTERFACE : ClassType.CLASS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(int mods, String name, ClassType classTypeVal) throws JClassAlreadyExistsException {
/*     */     String NAME;
/* 641 */     if (JCodeModel.isCaseSensitiveFileSystem) {
/* 642 */       NAME = name.toUpperCase();
/*     */     } else {
/* 644 */       NAME = name;
/*     */     } 
/* 646 */     if (getClasses().containsKey(NAME)) {
/* 647 */       throw new JClassAlreadyExistsException((JDefinedClass)getClasses().get(NAME));
/*     */     }
/*     */     
/* 650 */     JDefinedClass c = new JDefinedClass(this, mods, name, classTypeVal);
/* 651 */     getClasses().put(NAME, c);
/* 652 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _class(String name) throws JClassAlreadyExistsException {
/* 661 */     return _class(1, name);
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
/*     */   public JDefinedClass _interface(int mods, String name) throws JClassAlreadyExistsException {
/* 677 */     return _class(mods, name, ClassType.INTERFACE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass _interface(String name) throws JClassAlreadyExistsException {
/* 685 */     return _interface(1, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/* 695 */     if (this.jdoc == null)
/* 696 */       this.jdoc = new JDocComment(owner()); 
/* 697 */     return this.jdoc;
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
/*     */   public void hide() {
/* 709 */     this.hideFile = true;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/* 713 */     return this.hideFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Iterator<JDefinedClass> classes() {
/* 721 */     if (this.classes == null) {
/* 722 */       return Collections.<JDefinedClass>emptyList().iterator();
/*     */     }
/* 724 */     return this.classes.values().iterator();
/*     */   }
/*     */   
/*     */   private Map<String, JDefinedClass> getClasses() {
/* 728 */     if (this.classes == null)
/* 729 */       this.classes = new TreeMap<>(); 
/* 730 */     return this.classes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JClass[] listClasses() {
/* 738 */     if (this.classes == null) {
/* 739 */       return new JClass[0];
/*     */     }
/* 741 */     return (JClass[])this.classes.values().toArray((Object[])new JClass[this.classes.values().size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public JClass outer() {
/* 746 */     if (this.outer.isClass()) {
/* 747 */       return (JClass)this.outer;
/*     */     }
/* 749 */     return null;
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 753 */     if (this.jdoc != null) {
/* 754 */       f.nl().g(this.jdoc);
/*     */     }
/* 756 */     if (this.annotations != null) {
/* 757 */       for (JAnnotationUse annotation : this.annotations) {
/* 758 */         f.g(annotation).nl();
/*     */       }
/*     */     }
/* 761 */     f.g(this.mods).p(this.classType.declarationToken).id(this.name).d(this.generifiable);
/*     */     
/* 763 */     if (this.superClass != null && this.superClass != owner().ref(Object.class)) {
/* 764 */       f.nl().i().p("extends").g(this.superClass).nl().o();
/*     */     }
/* 766 */     if (!this.interfaces.isEmpty()) {
/* 767 */       if (this.superClass == null)
/* 768 */         f.nl(); 
/* 769 */       f.i().p((this.classType == ClassType.INTERFACE) ? "extends" : "implements");
/* 770 */       f.g((Collection)this.interfaces);
/* 771 */       f.nl().o();
/*     */     } 
/* 773 */     declareBody(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void declareBody(JFormatter f) {
/* 780 */     f.p('{').nl().nl().i();
/* 781 */     boolean first = true;
/*     */     
/* 783 */     if (!this.enumConstantsByName.isEmpty()) {
/* 784 */       for (JEnumConstant c : this.enumConstantsByName.values()) {
/* 785 */         if (!first) f.p(',').nl(); 
/* 786 */         f.d(c);
/* 787 */         first = false;
/*     */       } 
/* 789 */       f.p(';').nl();
/*     */     } 
/*     */     
/* 792 */     for (JFieldVar field : this.fields.values())
/* 793 */       f.d(field); 
/* 794 */     if (this.init != null)
/* 795 */       f.nl().p("static").s(this.init); 
/* 796 */     for (JMethod m : this.constructors) {
/* 797 */       f.nl().d(m);
/*     */     }
/* 799 */     for (JMethod m : this.methods) {
/* 800 */       f.nl().d(m);
/*     */     }
/* 802 */     if (this.classes != null) {
/* 803 */       for (JDefinedClass dc : this.classes.values()) {
/* 804 */         f.nl().d(dc);
/*     */       }
/*     */     }
/* 807 */     if (this.directBlock != null)
/* 808 */       f.p(this.directBlock); 
/* 809 */     f.nl().o().p('}').nl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void direct(String string) {
/* 820 */     if (this.directBlock == null) {
/* 821 */       this.directBlock = string;
/*     */     } else {
/* 823 */       this.directBlock += string;
/*     */     } 
/*     */   }
/*     */   public final JPackage _package() {
/* 827 */     JClassContainer p = this.outer;
/* 828 */     while (!(p instanceof JPackage))
/* 829 */       p = p.parentContainer(); 
/* 830 */     return (JPackage)p;
/*     */   }
/*     */   
/*     */   public final JClassContainer parentContainer() {
/* 834 */     return this.outer;
/*     */   }
/*     */   
/*     */   public JTypeVar generify(String name) {
/* 838 */     return this.generifiable.generify(name);
/*     */   }
/*     */   public JTypeVar generify(String name, Class<?> bound) {
/* 841 */     return this.generifiable.generify(name, bound);
/*     */   }
/*     */   public JTypeVar generify(String name, JClass bound) {
/* 844 */     return this.generifiable.generify(name, bound);
/*     */   }
/*     */   
/*     */   public JTypeVar[] typeParams() {
/* 848 */     return this.generifiable.typeParams();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 854 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 862 */     return annotate(owner().ref(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnnotationUse annotate(JClass clazz) {
/* 870 */     if (this.annotations == null)
/* 871 */       this.annotations = new ArrayList<>(); 
/* 872 */     JAnnotationUse a = new JAnnotationUse(clazz);
/* 873 */     this.annotations.add(a);
/* 874 */     return a;
/*     */   }
/*     */   
/*     */   public <W extends JAnnotationWriter> W annotate2(Class<W> clazz) {
/* 878 */     return (W)TypedAnnotationWriter.create(clazz, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<JAnnotationUse> annotations() {
/* 885 */     if (this.annotations == null)
/* 886 */       this.annotations = new ArrayList<>(); 
/* 887 */     return Collections.unmodifiableCollection(this.annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMods mods() {
/* 896 */     return this.mods;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JDefinedClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */