/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.model.AnnotationProxyMaker;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import javax.lang.model.AnnotatedConstruct;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AnnoConstruct
/*     */   implements AnnotatedConstruct
/*     */ {
/*     */   protected <A extends Annotation> Attribute.Compound getAttribute(Class<A> paramClass) {
/*  59 */     String str = paramClass.getName();
/*     */     
/*  61 */     for (Attribute.Compound compound : getAnnotationMirrors()) {
/*  62 */       if (str.equals(compound.type.tsym.flatName().toString())) {
/*  63 */         return compound;
/*     */       }
/*     */     } 
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <A extends Annotation> A[] getInheritedAnnotations(Class<A> paramClass) {
/*  72 */     return (A[])Array.newInstance(paramClass, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A[] getAnnotationsByType(Class<A> paramClass) {
/*  79 */     if (!paramClass.isAnnotation()) {
/*  80 */       throw new IllegalArgumentException("Not an annotation type: " + paramClass);
/*     */     }
/*     */ 
/*     */     
/*  84 */     Class<? extends Annotation> clazz = getContainer(paramClass);
/*  85 */     if (clazz == null) {
/*  86 */       Annotation annotation = (Annotation)getAnnotation((Class)paramClass);
/*  87 */       boolean bool = (annotation == null) ? false : true;
/*     */ 
/*     */       
/*  90 */       Annotation[] arrayOfAnnotation1 = (Annotation[])Array.newInstance(paramClass, bool);
/*  91 */       if (annotation != null)
/*  92 */         arrayOfAnnotation1[0] = annotation; 
/*  93 */       return (A[])arrayOfAnnotation1;
/*     */     } 
/*     */ 
/*     */     
/*  97 */     String str1 = paramClass.getName();
/*  98 */     String str2 = clazz.getName();
/*  99 */     byte b1 = -1, b2 = -1;
/* 100 */     Attribute.Compound compound1 = null, compound2 = null;
/*     */     
/* 102 */     byte b3 = -1;
/* 103 */     for (Attribute.Compound compound : getAnnotationMirrors()) {
/* 104 */       b3++;
/* 105 */       if (compound.type.tsym.flatName().contentEquals(str1)) {
/* 106 */         b1 = b3;
/* 107 */         compound1 = compound; continue;
/* 108 */       }  if (str2 != null && compound.type.tsym
/* 109 */         .flatName().contentEquals(str2)) {
/* 110 */         b2 = b3;
/* 111 */         compound2 = compound;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 116 */     if (compound1 == null && compound2 == null && paramClass
/* 117 */       .isAnnotationPresent((Class)Inherited.class)) {
/* 118 */       return getInheritedAnnotations(paramClass);
/*     */     }
/* 120 */     Attribute.Compound[] arrayOfCompound = unpackContained(compound2);
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (compound1 == null && arrayOfCompound.length == 0 && paramClass
/* 125 */       .isAnnotationPresent((Class)Inherited.class)) {
/* 126 */       return getInheritedAnnotations(paramClass);
/*     */     }
/* 128 */     int i = ((compound1 == null) ? 0 : 1) + arrayOfCompound.length;
/*     */     
/* 130 */     Annotation[] arrayOfAnnotation = (Annotation[])Array.newInstance(paramClass, i);
/*     */ 
/*     */     
/* 133 */     byte b4 = -1;
/* 134 */     int j = arrayOfAnnotation.length;
/* 135 */     if (b1 >= 0 && b2 >= 0)
/* 136 */     { if (b1 < b2) {
/* 137 */         arrayOfAnnotation[0] = AnnotationProxyMaker.generateAnnotation(compound1, paramClass);
/* 138 */         b4 = 1;
/*     */       } else {
/* 140 */         arrayOfAnnotation[arrayOfAnnotation.length - 1] = AnnotationProxyMaker.generateAnnotation(compound1, paramClass);
/* 141 */         b4 = 0;
/* 142 */         j--;
/*     */       }  }
/* 144 */     else { if (b1 >= 0) {
/* 145 */         arrayOfAnnotation[0] = AnnotationProxyMaker.generateAnnotation(compound1, paramClass);
/* 146 */         return (A[])arrayOfAnnotation;
/*     */       } 
/*     */       
/* 149 */       b4 = 0; }
/*     */ 
/*     */     
/* 152 */     for (byte b = 0; b + b4 < j; b++) {
/* 153 */       arrayOfAnnotation[b4 + b] = AnnotationProxyMaker.generateAnnotation(arrayOfCompound[b], paramClass);
/*     */     }
/* 155 */     return (A[])arrayOfAnnotation;
/*     */   }
/*     */ 
/*     */   
/*     */   private Attribute.Compound[] unpackContained(Attribute.Compound paramCompound) {
/* 160 */     Attribute[] arrayOfAttribute = null;
/* 161 */     if (paramCompound != null)
/* 162 */       arrayOfAttribute = unpackAttributes(paramCompound); 
/* 163 */     ListBuffer listBuffer = new ListBuffer();
/* 164 */     if (arrayOfAttribute != null)
/* 165 */       for (Attribute attribute : arrayOfAttribute) {
/* 166 */         if (attribute instanceof Attribute.Compound)
/* 167 */           listBuffer = listBuffer.append(attribute); 
/*     */       }  
/* 169 */     return (Attribute.Compound[])listBuffer.toArray((Object[])new Attribute.Compound[listBuffer.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> paramClass) {
/* 175 */     if (!paramClass.isAnnotation()) {
/* 176 */       throw new IllegalArgumentException("Not an annotation type: " + paramClass);
/*     */     }
/* 178 */     Attribute.Compound compound = getAttribute(paramClass);
/* 179 */     return (compound == null) ? null : (A)AnnotationProxyMaker.generateAnnotation(compound, paramClass);
/*     */   }
/*     */ 
/*     */   
/* 183 */   private static final Class<? extends Annotation> REPEATABLE_CLASS = initRepeatable();
/* 184 */   private static final Method VALUE_ELEMENT_METHOD = initValueElementMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<? extends Annotation> initRepeatable() {
/*     */     try {
/* 191 */       return Class.forName("java.lang.annotation.Repeatable").asSubclass(Annotation.class);
/* 192 */     } catch (ClassNotFoundException|SecurityException classNotFoundException) {
/* 193 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Method initValueElementMethod() {
/* 198 */     if (REPEATABLE_CLASS == null) {
/* 199 */       return null;
/*     */     }
/* 201 */     Method method = null;
/*     */     try {
/* 203 */       method = REPEATABLE_CLASS.getMethod("value", new Class[0]);
/* 204 */       if (method != null)
/* 205 */         method.setAccessible(true); 
/* 206 */       return method;
/* 207 */     } catch (NoSuchMethodException noSuchMethodException) {
/* 208 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<? extends Annotation> getContainer(Class<? extends Annotation> paramClass) {
/* 218 */     if (REPEATABLE_CLASS != null && VALUE_ELEMENT_METHOD != null) {
/*     */ 
/*     */       
/* 221 */       Object object = paramClass.getAnnotation((Class)REPEATABLE_CLASS);
/* 222 */       if (object != null) {
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 227 */           Class<? extends Annotation> clazz = (Class)VALUE_ELEMENT_METHOD.invoke(object, new Object[0]);
/* 228 */           if (clazz == null) {
/* 229 */             return null;
/*     */           }
/* 231 */           return clazz;
/* 232 */         } catch (ClassCastException|IllegalAccessException|java.lang.reflect.InvocationTargetException classCastException) {
/* 233 */           return null;
/*     */         } 
/*     */       }
/*     */     } 
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Attribute[] unpackAttributes(Attribute.Compound paramCompound) {
/* 246 */     return ((Attribute.Array)paramCompound.member(paramCompound.type.tsym.name.table.names.value)).values;
/*     */   }
/*     */   
/*     */   public abstract List<? extends Attribute.Compound> getAnnotationMirrors();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\AnnoConstruct.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */