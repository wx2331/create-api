/*     */ package com.sun.tools.internal.jxc.model.nav;
/*     */ 
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.source.util.Trees;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.TypeParameterElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVariable;
/*     */ import javax.lang.model.type.TypeVisitor;
/*     */ import javax.lang.model.type.WildcardType;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleTypeVisitor6;
/*     */ import javax.lang.model.util.Types;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ApNavigator
/*     */   implements Navigator<TypeMirror, TypeElement, VariableElement, ExecutableElement>
/*     */ {
/*     */   private final ProcessingEnvironment env;
/*     */   private final PrimitiveType primitiveByte;
/*     */   
/*     */   public ApNavigator(ProcessingEnvironment env) {
/* 463 */     this.baseClassFinder = new SimpleTypeVisitor6<TypeMirror, TypeElement>()
/*     */       {
/*     */         public TypeMirror visitDeclared(DeclaredType t, TypeElement sup) {
/* 466 */           if (t.asElement().equals(sup)) {
/* 467 */             return t;
/*     */           }
/* 469 */           for (TypeMirror i : ApNavigator.this.env.getTypeUtils().directSupertypes(t)) {
/* 470 */             TypeMirror r = visitDeclared((DeclaredType)i, sup);
/* 471 */             if (r != null) {
/* 472 */               return r;
/*     */             }
/*     */           } 
/*     */           
/* 476 */           TypeMirror superclass = ((TypeElement)t.asElement()).getSuperclass();
/* 477 */           if (!superclass.getKind().equals(TypeKind.NONE)) {
/* 478 */             TypeMirror r = visitDeclared((DeclaredType)superclass, sup);
/* 479 */             if (r != null)
/* 480 */               return r; 
/*     */           } 
/* 482 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public TypeMirror visitTypeVariable(TypeVariable t, TypeElement typeElement) {
/* 489 */           for (TypeMirror typeMirror : ((TypeParameterElement)t.asElement()).getBounds()) {
/* 490 */             TypeMirror m = visit(typeMirror, typeElement);
/* 491 */             if (m != null)
/* 492 */               return m; 
/*     */           } 
/* 494 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public TypeMirror visitArray(ArrayType t, TypeElement typeElement) {
/* 502 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public TypeMirror visitWildcard(WildcardType t, TypeElement typeElement) {
/* 509 */           return visit(t.getExtendsBound(), typeElement);
/*     */         }
/*     */ 
/*     */         
/*     */         protected TypeMirror defaultAction(TypeMirror e, TypeElement typeElement) {
/* 514 */           return e;
/*     */         }
/*     */       };
/*     */     this.env = env;
/*     */     this.primitiveByte = env.getTypeUtils().getPrimitiveType(TypeKind.BYTE);
/*     */   }
/*     */   
/*     */   public TypeElement getSuperClass(TypeElement typeElement) {
/*     */     if (typeElement.getKind().equals(ElementKind.CLASS)) {
/*     */       TypeMirror sup = typeElement.getSuperclass();
/*     */       if (!sup.getKind().equals(TypeKind.NONE))
/*     */         return (TypeElement)((DeclaredType)sup).asElement(); 
/*     */       return null;
/*     */     } 
/*     */     return this.env.getElementUtils().getTypeElement(Object.class.getName());
/*     */   }
/*     */   
/*     */   public TypeMirror getBaseClass(TypeMirror type, TypeElement sup) {
/*     */     return this.baseClassFinder.visit(type, sup);
/*     */   }
/*     */   
/*     */   public String getClassName(TypeElement t) {
/*     */     return t.getQualifiedName().toString();
/*     */   }
/*     */   
/*     */   public String getTypeName(TypeMirror typeMirror) {
/*     */     return typeMirror.toString();
/*     */   }
/*     */   
/*     */   public String getClassShortName(TypeElement t) {
/*     */     return t.getSimpleName().toString();
/*     */   }
/*     */   
/*     */   public Collection<VariableElement> getDeclaredFields(TypeElement typeElement) {
/*     */     return ElementFilter.fieldsIn(typeElement.getEnclosedElements());
/*     */   }
/*     */   
/*     */   public VariableElement getDeclaredField(TypeElement clazz, String fieldName) {
/*     */     for (VariableElement fd : ElementFilter.fieldsIn(clazz.getEnclosedElements())) {
/*     */       if (fd.getSimpleName().toString().equals(fieldName))
/*     */         return fd; 
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   public Collection<ExecutableElement> getDeclaredMethods(TypeElement typeElement) {
/*     */     return ElementFilter.methodsIn(typeElement.getEnclosedElements());
/*     */   }
/*     */   
/*     */   public TypeElement getDeclaringClassForField(VariableElement f) {
/*     */     return (TypeElement)f.getEnclosingElement();
/*     */   }
/*     */   
/*     */   public TypeElement getDeclaringClassForMethod(ExecutableElement m) {
/*     */     return (TypeElement)m.getEnclosingElement();
/*     */   }
/*     */   
/*     */   public TypeMirror getFieldType(VariableElement f) {
/*     */     return f.asType();
/*     */   }
/*     */   
/*     */   public String getFieldName(VariableElement f) {
/*     */     return f.getSimpleName().toString();
/*     */   }
/*     */   
/*     */   public String getMethodName(ExecutableElement m) {
/*     */     return m.getSimpleName().toString();
/*     */   }
/*     */   
/*     */   public TypeMirror getReturnType(ExecutableElement m) {
/*     */     return m.getReturnType();
/*     */   }
/*     */   
/*     */   public TypeMirror[] getMethodParameters(ExecutableElement m) {
/*     */     Collection<? extends VariableElement> ps = m.getParameters();
/*     */     TypeMirror[] r = new TypeMirror[ps.size()];
/*     */     int i = 0;
/*     */     for (VariableElement p : ps)
/*     */       r[i++] = p.asType(); 
/*     */     return r;
/*     */   }
/*     */   
/*     */   public boolean isStaticMethod(ExecutableElement m) {
/*     */     return hasModifier(m, Modifier.STATIC);
/*     */   }
/*     */   
/*     */   public boolean isFinalMethod(ExecutableElement m) {
/*     */     return hasModifier(m, Modifier.FINAL);
/*     */   }
/*     */   
/*     */   private boolean hasModifier(Element d, Modifier mod) {
/*     */     return d.getModifiers().contains(mod);
/*     */   }
/*     */   
/*     */   public boolean isSubClassOf(TypeMirror sub, TypeMirror sup) {
/*     */     if (sup == DUMMY)
/*     */       return false; 
/*     */     return this.env.getTypeUtils().isSubtype(sub, sup);
/*     */   }
/*     */   
/*     */   private String getSourceClassName(Class clazz) {
/*     */     Class<?> d = clazz.getDeclaringClass();
/*     */     if (d == null)
/*     */       return clazz.getName(); 
/*     */     String shortName = clazz.getName().substring(d.getName().length() + 1);
/*     */     return getSourceClassName(d) + '.' + shortName;
/*     */   }
/*     */   
/*     */   public TypeMirror ref(Class c) {
/*     */     if (c.isArray())
/*     */       return this.env.getTypeUtils().getArrayType(ref(c.getComponentType())); 
/*     */     if (c.isPrimitive())
/*     */       return getPrimitive(c); 
/*     */     TypeElement t = this.env.getElementUtils().getTypeElement(getSourceClassName(c));
/*     */     if (t == null)
/*     */       return DUMMY; 
/*     */     return this.env.getTypeUtils().getDeclaredType(t, new TypeMirror[0]);
/*     */   }
/*     */   
/*     */   public TypeMirror use(TypeElement t) {
/*     */     assert t != null;
/*     */     return this.env.getTypeUtils().getDeclaredType(t, new TypeMirror[0]);
/*     */   }
/*     */   
/*     */   public TypeElement asDecl(TypeMirror m) {
/*     */     m = this.env.getTypeUtils().erasure(m);
/*     */     if (m.getKind().equals(TypeKind.DECLARED)) {
/*     */       DeclaredType d = (DeclaredType)m;
/*     */       return (TypeElement)d.asElement();
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   public TypeElement asDecl(Class c) {
/*     */     return this.env.getElementUtils().getTypeElement(getSourceClassName(c));
/*     */   }
/*     */   
/*     */   public TypeMirror erasure(TypeMirror t) {
/*     */     Types tu = this.env.getTypeUtils();
/*     */     t = tu.erasure(t);
/*     */     if (t.getKind().equals(TypeKind.DECLARED)) {
/*     */       DeclaredType dt = (DeclaredType)t;
/*     */       if (!dt.getTypeArguments().isEmpty())
/*     */         return tu.getDeclaredType((TypeElement)dt.asElement(), new TypeMirror[0]); 
/*     */     } 
/*     */     return t;
/*     */   }
/*     */   
/*     */   public boolean isAbstract(TypeElement clazz) {
/*     */     return hasModifier(clazz, Modifier.ABSTRACT);
/*     */   }
/*     */   
/*     */   public boolean isFinal(TypeElement clazz) {
/*     */     return hasModifier(clazz, Modifier.FINAL);
/*     */   }
/*     */   
/*     */   public VariableElement[] getEnumConstants(TypeElement clazz) {
/*     */     List<? extends Element> elements = this.env.getElementUtils().getAllMembers(clazz);
/*     */     Collection<VariableElement> constants = new ArrayList<>();
/*     */     for (Element element : elements) {
/*     */       if (element.getKind().equals(ElementKind.ENUM_CONSTANT))
/*     */         constants.add((VariableElement)element); 
/*     */     } 
/*     */     return constants.<VariableElement>toArray(new VariableElement[constants.size()]);
/*     */   }
/*     */   
/*     */   public TypeMirror getVoidType() {
/*     */     return this.env.getTypeUtils().getNoType(TypeKind.VOID);
/*     */   }
/*     */   
/*     */   public String getPackageName(TypeElement clazz) {
/*     */     return this.env.getElementUtils().getPackageOf(clazz).getQualifiedName().toString();
/*     */   }
/*     */   
/*     */   public TypeElement loadObjectFactory(TypeElement referencePoint, String packageName) {
/*     */     return this.env.getElementUtils().getTypeElement(packageName + ".ObjectFactory");
/*     */   }
/*     */   
/*     */   public boolean isBridgeMethod(ExecutableElement method) {
/*     */     return method.getModifiers().contains(Modifier.VOLATILE);
/*     */   }
/*     */   
/*     */   public boolean isOverriding(ExecutableElement method, TypeElement base) {
/*     */     Elements elements = this.env.getElementUtils();
/*     */     while (true) {
/*     */       for (ExecutableElement m : ElementFilter.methodsIn(elements.getAllMembers(base))) {
/*     */         if (elements.overrides(method, m, base))
/*     */           return true; 
/*     */       } 
/*     */       if (base.getSuperclass().getKind().equals(TypeKind.NONE))
/*     */         return false; 
/*     */       base = (TypeElement)this.env.getTypeUtils().asElement(base.getSuperclass());
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isInterface(TypeElement clazz) {
/*     */     return clazz.getKind().isInterface();
/*     */   }
/*     */   
/*     */   public boolean isTransient(VariableElement f) {
/*     */     return f.getModifiers().contains(Modifier.TRANSIENT);
/*     */   }
/*     */   
/*     */   public boolean isInnerClass(TypeElement clazz) {
/*     */     return (clazz.getEnclosingElement() != null && !clazz.getModifiers().contains(Modifier.STATIC));
/*     */   }
/*     */   
/*     */   public boolean isSameType(TypeMirror t1, TypeMirror t2) {
/*     */     return this.env.getTypeUtils().isSameType(t1, t2);
/*     */   }
/*     */   
/*     */   public boolean isArray(TypeMirror type) {
/*     */     return (type != null && type.getKind().equals(TypeKind.ARRAY));
/*     */   }
/*     */   
/*     */   public boolean isArrayButNotByteArray(TypeMirror t) {
/*     */     if (!isArray(t))
/*     */       return false; 
/*     */     ArrayType at = (ArrayType)t;
/*     */     TypeMirror ct = at.getComponentType();
/*     */     return !ct.equals(this.primitiveByte);
/*     */   }
/*     */   
/*     */   public TypeMirror getComponentType(TypeMirror t) {
/*     */     if (isArray(t)) {
/*     */       ArrayType at = (ArrayType)t;
/*     */       return at.getComponentType();
/*     */     } 
/*     */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */   public TypeMirror getTypeArgument(TypeMirror typeMirror, int i) {
/*     */     if (typeMirror != null && typeMirror.getKind().equals(TypeKind.DECLARED)) {
/*     */       DeclaredType declaredType = (DeclaredType)typeMirror;
/*     */       TypeMirror[] args = declaredType.getTypeArguments().<TypeMirror>toArray(new TypeMirror[declaredType.getTypeArguments().size()]);
/*     */       return args[i];
/*     */     } 
/*     */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */   public boolean isParameterizedType(TypeMirror typeMirror) {
/*     */     if (typeMirror != null && typeMirror.getKind().equals(TypeKind.DECLARED)) {
/*     */       DeclaredType d = (DeclaredType)typeMirror;
/*     */       return !d.getTypeArguments().isEmpty();
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPrimitive(TypeMirror t) {
/*     */     return t.getKind().isPrimitive();
/*     */   }
/*     */   
/*     */   private static final Map<Class, TypeKind> primitives = (Map)new HashMap<>();
/*     */   
/*     */   static {
/*     */     primitives.put(int.class, TypeKind.INT);
/*     */     primitives.put(byte.class, TypeKind.BYTE);
/*     */     primitives.put(float.class, TypeKind.FLOAT);
/*     */     primitives.put(boolean.class, TypeKind.BOOLEAN);
/*     */     primitives.put(short.class, TypeKind.SHORT);
/*     */     primitives.put(long.class, TypeKind.LONG);
/*     */     primitives.put(double.class, TypeKind.DOUBLE);
/*     */     primitives.put(char.class, TypeKind.CHAR);
/*     */   }
/*     */   
/*     */   public TypeMirror getPrimitive(Class<void> primitiveType) {
/*     */     assert primitiveType.isPrimitive();
/*     */     if (primitiveType == void.class)
/*     */       return getVoidType(); 
/*     */     return this.env.getTypeUtils().getPrimitiveType(primitives.get(primitiveType));
/*     */   }
/*     */   
/*     */   private static final TypeMirror DUMMY = new TypeMirror() {
/*     */       public <R, P> R accept(TypeVisitor<R, P> v, P p) {
/*     */         throw new IllegalStateException();
/*     */       }
/*     */       
/*     */       public TypeKind getKind() {
/*     */         throw new IllegalStateException();
/*     */       }
/*     */       
/*     */       public List<? extends AnnotationMirror> getAnnotationMirrors() {
/*     */         throw new IllegalStateException();
/*     */       }
/*     */       
/*     */       public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationType) {
/*     */         throw new IllegalStateException();
/*     */       }
/*     */       
/*     */       public <A extends java.lang.annotation.Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
/*     */         throw new IllegalStateException();
/*     */       }
/*     */     };
/*     */   
/*     */   private final SimpleTypeVisitor6<TypeMirror, TypeElement> baseClassFinder;
/*     */   
/*     */   public Location getClassLocation(TypeElement typeElement) {
/*     */     Trees trees = Trees.instance(this.env);
/*     */     return getLocation(typeElement.getQualifiedName().toString(), trees.getPath(typeElement));
/*     */   }
/*     */   
/*     */   public Location getFieldLocation(VariableElement variableElement) {
/*     */     return getLocation(variableElement);
/*     */   }
/*     */   
/*     */   public Location getMethodLocation(ExecutableElement executableElement) {
/*     */     return getLocation(executableElement);
/*     */   }
/*     */   
/*     */   public boolean hasDefaultConstructor(TypeElement t) {
/*     */     if (t == null || !t.getKind().equals(ElementKind.CLASS))
/*     */       return false; 
/*     */     for (ExecutableElement init : ElementFilter.constructorsIn(this.env.getElementUtils().getAllMembers(t))) {
/*     */       if (init.getParameters().isEmpty())
/*     */         return true; 
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   public boolean isStaticField(VariableElement f) {
/*     */     return hasModifier(f, Modifier.STATIC);
/*     */   }
/*     */   
/*     */   public boolean isPublicMethod(ExecutableElement m) {
/*     */     return hasModifier(m, Modifier.PUBLIC);
/*     */   }
/*     */   
/*     */   public boolean isPublicField(VariableElement f) {
/*     */     return hasModifier(f, Modifier.PUBLIC);
/*     */   }
/*     */   
/*     */   public boolean isEnum(TypeElement t) {
/*     */     return (t != null && t.getKind().equals(ElementKind.ENUM));
/*     */   }
/*     */   
/*     */   private Location getLocation(Element element) {
/*     */     Trees trees = Trees.instance(this.env);
/*     */     return getLocation(((TypeElement)element.getEnclosingElement()).getQualifiedName() + "." + element.getSimpleName(), trees.getPath(element));
/*     */   }
/*     */   
/*     */   private Location getLocation(final String name, final TreePath treePath) {
/*     */     return new Location() {
/*     */         public String toString() {
/*     */           if (treePath == null)
/*     */             return name + " (Unknown Source)"; 
/*     */           CompilationUnitTree compilationUnit = treePath.getCompilationUnit();
/*     */           Trees trees = Trees.instance(ApNavigator.this.env);
/*     */           long startPosition = trees.getSourcePositions().getStartPosition(compilationUnit, treePath.getLeaf());
/*     */           return name + "(" + compilationUnit.getSourceFile().getName() + ":" + compilationUnit.getLineMap().getLineNumber(startPosition) + ")";
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\model\nav\ApNavigator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */