/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TypedAnnotationWriter<A extends Annotation, W extends JAnnotationWriter<A>>
/*     */   implements InvocationHandler, JAnnotationWriter<A>
/*     */ {
/*     */   private final JAnnotationUse use;
/*     */   private final Class<A> annotation;
/*     */   private final Class<W> writerType;
/*     */   private Map<String, JAnnotationArrayMember> arrays;
/*     */   
/*     */   public TypedAnnotationWriter(Class<A> annotation, Class<W> writer, JAnnotationUse use) {
/*  67 */     this.annotation = annotation;
/*  68 */     this.writerType = writer;
/*  69 */     this.use = use;
/*     */   }
/*     */   
/*     */   public JAnnotationUse getAnnotationUse() {
/*  73 */     return this.use;
/*     */   }
/*     */   
/*     */   public Class<A> getAnnotationType() {
/*  77 */     return this.annotation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*  83 */     if (method.getDeclaringClass() == JAnnotationWriter.class) {
/*     */       try {
/*  85 */         return method.invoke(this, args);
/*  86 */       } catch (InvocationTargetException e) {
/*  87 */         throw e.getTargetException();
/*     */       } 
/*     */     }
/*     */     
/*  91 */     String name = method.getName();
/*  92 */     Object arg = null;
/*  93 */     if (args != null && args.length > 0) {
/*  94 */       arg = args[0];
/*     */     }
/*     */     
/*  97 */     Method m = this.annotation.getDeclaredMethod(name, new Class[0]);
/*  98 */     Class<?> rt = m.getReturnType();
/*     */ 
/*     */     
/* 101 */     if (rt.isArray()) {
/* 102 */       return addArrayValue(proxy, name, rt.getComponentType(), method.getReturnType(), arg);
/*     */     }
/*     */ 
/*     */     
/* 106 */     if (Annotation.class.isAssignableFrom(rt)) {
/* 107 */       Class<? extends Annotation> r = (Class)rt;
/* 108 */       return (new TypedAnnotationWriter((Class)r, (Class)method
/* 109 */           .getReturnType(), this.use.annotationParam(name, r))).createProxy();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 114 */     if (arg instanceof JType) {
/* 115 */       JType targ = (JType)arg;
/* 116 */       checkType(Class.class, rt);
/* 117 */       if (m.getDefaultValue() != null)
/*     */       {
/* 119 */         if (targ.equals(targ.owner().ref((Class)m.getDefaultValue())))
/* 120 */           return proxy; 
/*     */       }
/* 122 */       this.use.param(name, targ);
/* 123 */       return proxy;
/*     */     } 
/*     */ 
/*     */     
/* 127 */     checkType(arg.getClass(), rt);
/* 128 */     if (m.getDefaultValue() != null && m.getDefaultValue().equals(arg))
/*     */     {
/* 130 */       return proxy;
/*     */     }
/* 132 */     if (arg instanceof String) {
/* 133 */       this.use.param(name, (String)arg);
/* 134 */       return proxy;
/*     */     } 
/* 136 */     if (arg instanceof Boolean) {
/* 137 */       this.use.param(name, ((Boolean)arg).booleanValue());
/* 138 */       return proxy;
/*     */     } 
/* 140 */     if (arg instanceof Integer) {
/* 141 */       this.use.param(name, ((Integer)arg).intValue());
/* 142 */       return proxy;
/*     */     } 
/* 144 */     if (arg instanceof Class) {
/* 145 */       this.use.param(name, (Class)arg);
/* 146 */       return proxy;
/*     */     } 
/* 148 */     if (arg instanceof Enum) {
/* 149 */       this.use.param(name, (Enum)arg);
/* 150 */       return proxy;
/*     */     } 
/*     */     
/* 153 */     throw new IllegalArgumentException("Unable to handle this method call " + method.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   private Object addArrayValue(Object proxy, String name, Class<?> itemType, Class<?> expectedReturnType, Object arg) {
/* 158 */     if (this.arrays == null)
/* 159 */       this.arrays = new HashMap<>(); 
/* 160 */     JAnnotationArrayMember m = this.arrays.get(name);
/* 161 */     if (m == null) {
/* 162 */       m = this.use.paramArray(name);
/* 163 */       this.arrays.put(name, m);
/*     */     } 
/*     */ 
/*     */     
/* 167 */     if (Annotation.class.isAssignableFrom(itemType)) {
/* 168 */       Class<? extends Annotation> r = (Class)itemType;
/* 169 */       if (!JAnnotationWriter.class.isAssignableFrom(expectedReturnType))
/* 170 */         throw new IllegalArgumentException("Unexpected return type " + expectedReturnType); 
/* 171 */       return (new TypedAnnotationWriter((Class)r, (Class)expectedReturnType, m.annotate(r))).createProxy();
/*     */     } 
/*     */ 
/*     */     
/* 175 */     if (arg instanceof JType) {
/* 176 */       checkType(Class.class, itemType);
/* 177 */       m.param((JType)arg);
/* 178 */       return proxy;
/*     */     } 
/* 180 */     checkType(arg.getClass(), itemType);
/* 181 */     if (arg instanceof String) {
/* 182 */       m.param((String)arg);
/* 183 */       return proxy;
/*     */     } 
/* 185 */     if (arg instanceof Boolean) {
/* 186 */       m.param(((Boolean)arg).booleanValue());
/* 187 */       return proxy;
/*     */     } 
/* 189 */     if (arg instanceof Integer) {
/* 190 */       m.param(((Integer)arg).intValue());
/* 191 */       return proxy;
/*     */     } 
/* 193 */     if (arg instanceof Class) {
/* 194 */       m.param((Class)arg);
/* 195 */       return proxy;
/*     */     } 
/*     */ 
/*     */     
/* 199 */     throw new IllegalArgumentException("Unable to handle this method call ");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkType(Class<?> actual, Class<?> expected) {
/* 208 */     if (expected == actual || expected.isAssignableFrom(actual)) {
/*     */       return;
/*     */     }
/* 211 */     if (expected == JCodeModel.boxToPrimitive.get(actual)) {
/*     */       return;
/*     */     }
/* 214 */     throw new IllegalArgumentException("Expected " + expected + " but found " + actual);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private W createProxy() {
/* 222 */     return (W)Proxy.newProxyInstance(
/* 223 */         SecureLoader.getClassClassLoader(this.writerType), new Class[] { this.writerType }, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <W extends JAnnotationWriter<?>> W create(Class<W> w, JAnnotatable annotatable) {
/* 231 */     Class<? extends Annotation> a = findAnnotationType(w);
/* 232 */     return (W)(new TypedAnnotationWriter<>((Class)a, w, annotatable.annotate(a))).createProxy();
/*     */   }
/*     */   
/*     */   private static Class<? extends Annotation> findAnnotationType(Class<?> clazz) {
/* 236 */     for (Type t : clazz.getGenericInterfaces()) {
/* 237 */       if (t instanceof ParameterizedType) {
/* 238 */         ParameterizedType p = (ParameterizedType)t;
/* 239 */         if (p.getRawType() == JAnnotationWriter.class)
/* 240 */           return (Class<? extends Annotation>)p.getActualTypeArguments()[0]; 
/*     */       } 
/* 242 */       if (t instanceof Class) {
/*     */         
/* 244 */         Class<? extends Annotation> r = findAnnotationType((Class)t);
/* 245 */         if (r != null) return r; 
/*     */       } 
/*     */     } 
/* 248 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\TypedAnnotationWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */