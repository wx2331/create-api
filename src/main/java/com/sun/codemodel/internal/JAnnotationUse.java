/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
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
/*     */ public final class JAnnotationUse
/*     */   extends JAnnotationValue
/*     */ {
/*     */   private final JClass clazz;
/*     */   private Map<String, JAnnotationValue> memberValues;
/*     */   
/*     */   JAnnotationUse(JClass clazz) {
/*  55 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   public JClass getAnnotationClass() {
/*  59 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public Map<String, JAnnotationValue> getAnnotationMembers() {
/*  63 */     return Collections.unmodifiableMap(this.memberValues);
/*     */   }
/*     */   
/*     */   private JCodeModel owner() {
/*  67 */     return this.clazz.owner();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addValue(String name, JAnnotationValue annotationValue) {
/*  73 */     if (this.memberValues == null)
/*  74 */       this.memberValues = new LinkedHashMap<>(); 
/*  75 */     this.memberValues.put(name, annotationValue);
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
/*     */   public JAnnotationUse param(String name, boolean value) {
/*  92 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/*  93 */     return this;
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
/*     */   public JAnnotationUse param(String name, byte value) {
/* 109 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/* 110 */     return this;
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
/*     */   public JAnnotationUse param(String name, char value) {
/* 126 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/* 127 */     return this;
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
/*     */   public JAnnotationUse param(String name, double value) {
/* 143 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/* 144 */     return this;
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
/*     */   public JAnnotationUse param(String name, float value) {
/* 160 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/* 161 */     return this;
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
/*     */   public JAnnotationUse param(String name, long value) {
/* 177 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/* 178 */     return this;
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
/*     */   public JAnnotationUse param(String name, short value) {
/* 194 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/* 195 */     return this;
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
/*     */   public JAnnotationUse param(String name, int value) {
/* 211 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/* 212 */     return this;
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
/*     */   public JAnnotationUse param(String name, String value) {
/* 230 */     addValue(name, new JAnnotationStringValue(JExpr.lit(value)));
/* 231 */     return this;
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
/*     */   public JAnnotationUse annotationParam(String name, Class<? extends Annotation> value) {
/* 249 */     JAnnotationUse annotationUse = new JAnnotationUse(owner().ref(value));
/* 250 */     addValue(name, annotationUse);
/* 251 */     return annotationUse;
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
/*     */   public JAnnotationUse param(String name, final Enum<?> value) {
/* 267 */     addValue(name, new JAnnotationValue() {
/*     */           public void generate(JFormatter f) {
/* 269 */             f.t(JAnnotationUse.this.owner().ref(value.getDeclaringClass())).p('.').p(value.name());
/*     */           }
/*     */         });
/* 272 */     return this;
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
/*     */   public JAnnotationUse param(String name, JEnumConstant value) {
/* 288 */     addValue(name, new JAnnotationStringValue(value));
/* 289 */     return this;
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
/*     */   public JAnnotationUse param(String name, final Class<?> value) {
/* 314 */     addValue(name, new JAnnotationStringValue(new JExpressionImpl()
/*     */           {
/*     */             public void generate(JFormatter f) {
/* 317 */               f.p(value.getName().replace('$', '.'));
/* 318 */               f.p(".class");
/*     */             }
/*     */           }));
/* 321 */     return this;
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
/*     */   public JAnnotationUse param(String name, JType type) {
/* 334 */     JClass c = type.boxify();
/* 335 */     addValue(name, new JAnnotationStringValue(c.dotclass()));
/* 336 */     return this;
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
/*     */   public JAnnotationUse param(String name, JExpression value) {
/* 352 */     addValue(name, new JAnnotationStringValue(value));
/* 353 */     return this;
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
/*     */   public JAnnotationArrayMember paramArray(String name) {
/* 367 */     JAnnotationArrayMember arrayMember = new JAnnotationArrayMember(owner());
/* 368 */     addValue(name, arrayMember);
/* 369 */     return arrayMember;
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
/*     */   public JAnnotationUse annotate(Class<? extends Annotation> clazz) {
/* 399 */     JAnnotationUse annotationUse = new JAnnotationUse(owner().ref(clazz));
/* 400 */     return annotationUse;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 404 */     f.p('@').g(this.clazz);
/* 405 */     if (this.memberValues != null) {
/* 406 */       f.p('(');
/* 407 */       boolean first = true;
/*     */       
/* 409 */       if (isOptimizable()) {
/*     */         
/* 411 */         f.g(this.memberValues.get("value"));
/*     */       } else {
/* 413 */         for (Map.Entry<String, JAnnotationValue> mapEntry : this.memberValues.entrySet()) {
/* 414 */           if (!first) f.p(','); 
/* 415 */           f.p(mapEntry.getKey()).p('=').g(mapEntry.getValue());
/* 416 */           first = false;
/*     */         } 
/*     */       } 
/* 419 */       f.p(')');
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isOptimizable() {
/* 424 */     return (this.memberValues.size() == 1 && this.memberValues.containsKey("value"));
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JAnnotationUse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */