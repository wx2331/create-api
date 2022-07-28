/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JCommentPart
/*     */   extends ArrayList<Object>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public JCommentPart append(Object o) {
/*  57 */     add(o);
/*  58 */     return this;
/*     */   }
/*     */   
/*     */   public boolean add(Object o) {
/*  62 */     flattenAppend(o);
/*  63 */     return true;
/*     */   }
/*     */   
/*     */   private void flattenAppend(Object value) {
/*  67 */     if (value == null)
/*  68 */       return;  if (value instanceof Object[]) {
/*  69 */       for (Object o : (Object[])value) {
/*  70 */         flattenAppend(o);
/*     */       }
/*  72 */     } else if (value instanceof java.util.Collection) {
/*  73 */       for (Object o : value)
/*  74 */         flattenAppend(o); 
/*     */     } else {
/*  76 */       super.add(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void format(JFormatter f, String indent) {
/*  83 */     if (!f.isPrinting()) {
/*     */ 
/*     */       
/*  86 */       for (Object o : this) {
/*  87 */         if (o instanceof JClass)
/*  88 */           f.g((JClass)o); 
/*     */       } 
/*     */       return;
/*     */     } 
/*  92 */     if (!isEmpty()) {
/*  93 */       f.p(indent);
/*     */     }
/*  95 */     Iterator<Object> itr = iterator();
/*  96 */     while (itr.hasNext()) {
/*  97 */       Object o = itr.next();
/*     */       
/*  99 */       if (o instanceof String) {
/*     */         
/* 101 */         String s = (String)o; int idx;
/* 102 */         while ((idx = s.indexOf('\n')) != -1) {
/* 103 */           String line = s.substring(0, idx);
/* 104 */           if (line.length() > 0)
/* 105 */             f.p(escape(line)); 
/* 106 */           s = s.substring(idx + 1);
/* 107 */           f.nl().p(indent);
/*     */         } 
/* 109 */         if (s.length() != 0)
/* 110 */           f.p(escape(s));  continue;
/*     */       } 
/* 112 */       if (o instanceof JClass) {
/*     */         
/* 114 */         ((JClass)o).printLink(f); continue;
/*     */       } 
/* 116 */       if (o instanceof JType) {
/* 117 */         f.g((JType)o); continue;
/*     */       } 
/* 119 */       throw new IllegalStateException();
/*     */     } 
/*     */     
/* 122 */     if (!isEmpty()) {
/* 123 */       f.nl();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String escape(String s) {
/*     */     while (true) {
/* 131 */       int idx = s.indexOf("*/");
/* 132 */       if (idx < 0) return s;
/*     */       
/* 134 */       s = s.substring(0, idx + 1) + "<!---->" + s.substring(idx + 1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JCommentPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */