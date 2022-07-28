/*     */ package com.sun.tools.internal.xjc.model.nav;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NavigatorImpl
/*     */   implements Navigator<NType, NClass, Void, Void>
/*     */ {
/*  45 */   public static final NavigatorImpl theInstance = new NavigatorImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NClass getSuperClass(NClass nClass) {
/*  51 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getBaseClass(NType nt, NClass base) {
/*  55 */     if (nt instanceof EagerNType) {
/*  56 */       EagerNType ent = (EagerNType)nt;
/*  57 */       if (base instanceof EagerNClass) {
/*  58 */         EagerNClass enc = (EagerNClass)base;
/*  59 */         return create(Utils.REFLECTION_NAVIGATOR.getBaseClass(ent.t, enc.c));
/*     */       } 
/*     */       
/*  62 */       return null;
/*     */     } 
/*  64 */     if (nt instanceof NClassByJClass) {
/*  65 */       NClassByJClass nnt = (NClassByJClass)nt;
/*  66 */       if (base instanceof EagerNClass) {
/*  67 */         EagerNClass enc = (EagerNClass)base;
/*  68 */         return ref(nnt.clazz.getBaseClass(enc.c));
/*     */       } 
/*     */     } 
/*     */     
/*  72 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getClassName(NClass nClass) {
/*  76 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getTypeName(NType type) {
/*  80 */     return type.fullName();
/*     */   }
/*     */   
/*     */   public String getClassShortName(NClass nClass) {
/*  84 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Collection<? extends Void> getDeclaredFields(NClass nClass) {
/*  88 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Void getDeclaredField(NClass clazz, String fieldName) {
/*  92 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Collection<? extends Void> getDeclaredMethods(NClass nClass) {
/*  96 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NClass getDeclaringClassForField(Void aVoid) {
/* 100 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NClass getDeclaringClassForMethod(Void aVoid) {
/* 104 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getFieldType(Void aVoid) {
/* 108 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getFieldName(Void aVoid) {
/* 112 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getMethodName(Void aVoid) {
/* 116 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getReturnType(Void aVoid) {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType[] getMethodParameters(Void aVoid) {
/* 124 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isStaticMethod(Void aVoid) {
/* 128 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isFinalMethod(Void aVoid) {
/* 132 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isSubClassOf(NType sub, NType sup) {
/* 136 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NClass ref(Class c) {
/* 140 */     return create(c);
/*     */   }
/*     */   
/*     */   public NClass ref(JClass c) {
/* 144 */     if (c == null) return null; 
/* 145 */     return new NClassByJClass(c);
/*     */   }
/*     */   
/*     */   public NType use(NClass nc) {
/* 149 */     return nc;
/*     */   }
/*     */   
/*     */   public NClass asDecl(NType nt) {
/* 153 */     if (nt instanceof NClass) {
/* 154 */       return (NClass)nt;
/*     */     }
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   public NClass asDecl(Class c) {
/* 160 */     return ref(c);
/*     */   }
/*     */   
/*     */   public boolean isArray(NType nType) {
/* 164 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isArrayButNotByteArray(NType t) {
/* 168 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NType getComponentType(NType nType) {
/* 173 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getTypeArgument(NType nt, int i) {
/* 177 */     if (nt instanceof EagerNType) {
/* 178 */       EagerNType ent = (EagerNType)nt;
/* 179 */       return create(Utils.REFLECTION_NAVIGATOR.getTypeArgument(ent.t, i));
/*     */     } 
/* 181 */     if (nt instanceof NClassByJClass) {
/* 182 */       NClassByJClass nnt = (NClassByJClass)nt;
/* 183 */       return ref(nnt.clazz.getTypeParameters().get(i));
/*     */     } 
/*     */     
/* 186 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isParameterizedType(NType nt) {
/* 190 */     if (nt instanceof EagerNType) {
/* 191 */       EagerNType ent = (EagerNType)nt;
/* 192 */       return Utils.REFLECTION_NAVIGATOR.isParameterizedType(ent.t);
/*     */     } 
/* 194 */     if (nt instanceof NClassByJClass) {
/* 195 */       NClassByJClass nnt = (NClassByJClass)nt;
/* 196 */       return nnt.clazz.isParameterized();
/*     */     } 
/*     */     
/* 199 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isPrimitive(NType type) {
/* 203 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getPrimitive(Class primitiveType) {
/* 207 */     return create(primitiveType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final NType create(Type t) {
/* 212 */     if (t == null) return null; 
/* 213 */     if (t instanceof Class) {
/* 214 */       return create((Class)t);
/*     */     }
/* 216 */     return new EagerNType(t);
/*     */   }
/*     */   
/*     */   public static NClass create(Class c) {
/* 220 */     if (c == null) return null; 
/* 221 */     return new EagerNClass(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NType createParameterizedType(NClass rawType, NType... args) {
/* 229 */     return new NParameterizedType(rawType, args);
/*     */   }
/*     */   
/*     */   public static NType createParameterizedType(Class rawType, NType... args) {
/* 233 */     return new NParameterizedType(create(rawType), args);
/*     */   }
/*     */ 
/*     */   
/*     */   public Location getClassLocation(final NClass c) {
/* 238 */     return new Location()
/*     */       {
/*     */         public String toString() {
/* 241 */           return c.fullName();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Location getFieldLocation(Void v) {
/* 247 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public Location getMethodLocation(Void v) {
/* 251 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean hasDefaultConstructor(NClass nClass) {
/* 255 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isStaticField(Void aVoid) {
/* 259 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean isPublicMethod(Void aVoid) {
/* 263 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean isPublicField(Void aVoid) {
/* 267 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean isEnum(NClass c) {
/* 271 */     return isSubClassOf(c, create(Enum.class));
/*     */   }
/*     */   
/*     */   public <T> NType erasure(NType type) {
/* 275 */     if (type instanceof NParameterizedType) {
/* 276 */       NParameterizedType pt = (NParameterizedType)type;
/* 277 */       return pt.rawType;
/*     */     } 
/* 279 */     return type;
/*     */   }
/*     */   
/*     */   public boolean isAbstract(NClass clazz) {
/* 283 */     return clazz.isAbstract();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal(NClass clazz) {
/* 291 */     return false;
/*     */   }
/*     */   
/*     */   public Void[] getEnumConstants(NClass clazz) {
/* 295 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getVoidType() {
/* 299 */     return ref(void.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPackageName(NClass clazz) {
/* 304 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NClass loadObjectFactory(NClass referencePoint, String pkg) {
/* 309 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isBridgeMethod(Void method) {
/* 313 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isOverriding(Void method, NClass clazz) {
/* 317 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isInterface(NClass clazz) {
/* 321 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isTransient(Void f) {
/* 325 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isInnerClass(NClass clazz) {
/* 329 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSameType(NType t1, NType t2) {
/* 334 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\nav\NavigatorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */