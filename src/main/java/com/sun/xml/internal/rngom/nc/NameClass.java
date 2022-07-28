/*     */ package com.sun.xml.internal.rngom.nc;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NameClass
/*     */   implements ParsedNameClass, Serializable
/*     */ {
/*     */   static final int SPECIFICITY_NONE = -1;
/*     */   static final int SPECIFICITY_ANY_NAME = 0;
/*     */   static final int SPECIFICITY_NS_NAME = 1;
/*     */   static final int SPECIFICITY_NAME = 2;
/*     */   
/*     */   public abstract boolean contains(QName paramQName);
/*     */   
/*     */   public abstract int containsSpecificity(QName paramQName);
/*     */   
/*     */   public abstract <V> V accept(NameClassVisitor<V> paramNameClassVisitor);
/*     */   
/*     */   public abstract boolean isOpen();
/*     */   
/*     */   public Set<QName> listNames() {
/*  93 */     final Set<QName> names = new HashSet<>();
/*  94 */     accept(new NameClassWalker()
/*     */         {
/*     */           public Void visitName(QName name) {
/*  97 */             names.add(name);
/*  98 */             return null;
/*     */           }
/*     */         });
/* 101 */     return names;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasOverlapWith(NameClass nc2) {
/* 109 */     return OverlapDetector.overlap(this, nc2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public static final NameClass ANY = new AnyNameClass();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public static final NameClass NULL = new NullNameClass();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\nc\NameClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */