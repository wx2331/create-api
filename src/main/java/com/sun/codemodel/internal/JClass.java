/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JClass
/*     */   extends JType
/*     */ {
/*     */   private final JCodeModel _owner;
/*     */   
/*     */   protected JClass(JCodeModel _owner) {
/*  45 */     this._owner = _owner;
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
/*     */   public JClass outer() {
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final JCodeModel owner() {
/*  74 */     return this._owner;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public JTypeVar[] typeParams() {
/* 109 */     return EMPTY_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   protected static final JTypeVar[] EMPTY_ARRAY = new JTypeVar[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JClass arrayClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPrimitiveType getPrimitiveType() {
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass boxify() {
/* 139 */     return this;
/*     */   }
/*     */   public JType unboxify() {
/* 142 */     JPrimitiveType pt = getPrimitiveType();
/* 143 */     return (pt == null) ? this : pt;
/*     */   }
/*     */   
/*     */   public JClass erasure() {
/* 147 */     return this;
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
/*     */   public final boolean isAssignableFrom(JClass derived) {
/* 160 */     if (derived instanceof JNullType) return true;
/*     */     
/* 162 */     if (this == derived) return true;
/*     */ 
/*     */ 
/*     */     
/* 166 */     if (this == _package().owner().ref(Object.class)) return true;
/*     */     
/* 168 */     JClass b = derived._extends();
/* 169 */     if (b != null && isAssignableFrom(b)) {
/* 170 */       return true;
/*     */     }
/* 172 */     if (isInterface()) {
/* 173 */       Iterator<JClass> itfs = derived._implements();
/* 174 */       while (itfs.hasNext()) {
/* 175 */         if (isAssignableFrom(itfs.next()))
/* 176 */           return true; 
/*     */       } 
/*     */     } 
/* 179 */     return false;
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
/*     */   public final JClass getBaseClass(JClass baseType) {
/* 207 */     if (erasure().equals(baseType)) {
/* 208 */       return this;
/*     */     }
/* 210 */     JClass b = _extends();
/* 211 */     if (b != null) {
/* 212 */       JClass bc = b.getBaseClass(baseType);
/* 213 */       if (bc != null) {
/* 214 */         return bc;
/*     */       }
/*     */     } 
/* 217 */     Iterator<JClass> itfs = _implements();
/* 218 */     while (itfs.hasNext()) {
/* 219 */       JClass bc = ((JClass)itfs.next()).getBaseClass(baseType);
/* 220 */       if (bc != null) {
/* 221 */         return bc;
/*     */       }
/*     */     } 
/* 224 */     return null;
/*     */   }
/*     */   
/*     */   public final JClass getBaseClass(Class<?> baseType) {
/* 228 */     return getBaseClass(owner().ref(baseType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass array() {
/* 234 */     if (this.arrayClass == null)
/* 235 */       this.arrayClass = new JArrayClass(owner(), this); 
/* 236 */     return this.arrayClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass narrow(Class<?> clazz) {
/* 247 */     return narrow(owner().ref(clazz));
/*     */   }
/*     */   
/*     */   public JClass narrow(Class<?>... clazz) {
/* 251 */     JClass[] r = new JClass[clazz.length];
/* 252 */     for (int i = 0; i < clazz.length; i++)
/* 253 */       r[i] = owner().ref(clazz[i]); 
/* 254 */     return narrow(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass narrow(JClass clazz) {
/* 265 */     return new JNarrowedClass(this, clazz);
/*     */   }
/*     */   
/*     */   public JClass narrow(JType type) {
/* 269 */     return narrow(type.boxify());
/*     */   }
/*     */   
/*     */   public JClass narrow(JClass... clazz) {
/* 273 */     return new JNarrowedClass(this, Arrays.asList((Object[])clazz.clone()));
/*     */   }
/*     */   
/*     */   public JClass narrow(List<? extends JClass> clazz) {
/* 277 */     return new JNarrowedClass(this, new ArrayList<>(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JClass> getTypeParameters() {
/* 284 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isParameterized() {
/* 291 */     return (erasure() != this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JClass wildcard() {
/* 300 */     return new JTypeWildcard(this);
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
/*     */   public String toString() {
/* 318 */     return getClass().getName() + '(' + name() + ')';
/*     */   }
/*     */ 
/*     */   
/*     */   public final JExpression dotclass() {
/* 323 */     return JExpr.dotclass(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JInvocation staticInvoke(JMethod method) {
/* 328 */     return new JInvocation(this, method);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JInvocation staticInvoke(String method) {
/* 333 */     return new JInvocation(this, method);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JFieldRef staticRef(String field) {
/* 338 */     return new JFieldRef(this, field);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JFieldRef staticRef(JVar field) {
/* 343 */     return new JFieldRef(this, field);
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 347 */     f.t(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void printLink(JFormatter f) {
/* 354 */     f.p("{@link ").g(this).p('}');
/*     */   }
/*     */   
/*     */   public abstract String name();
/*     */   
/*     */   public abstract JPackage _package();
/*     */   
/*     */   public abstract JClass _extends();
/*     */   
/*     */   public abstract Iterator<JClass> _implements();
/*     */   
/*     */   public abstract boolean isInterface();
/*     */   
/*     */   public abstract boolean isAbstract();
/*     */   
/*     */   protected abstract JClass substituteParams(JTypeVar[] paramArrayOfJTypeVar, List<JClass> paramList);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */