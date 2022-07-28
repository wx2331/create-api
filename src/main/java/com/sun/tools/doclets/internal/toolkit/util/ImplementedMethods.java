/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public class ImplementedMethods
/*     */ {
/*  46 */   private Map<MethodDoc, Type> interfaces = new HashMap<>();
/*  47 */   private List<MethodDoc> methlist = new ArrayList<>();
/*     */   private Configuration configuration;
/*     */   private final ClassDoc classdoc;
/*     */   private final MethodDoc method;
/*     */   
/*     */   public ImplementedMethods(MethodDoc paramMethodDoc, Configuration paramConfiguration) {
/*  53 */     this.method = paramMethodDoc;
/*  54 */     this.configuration = paramConfiguration;
/*  55 */     this.classdoc = paramMethodDoc.containingClass();
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
/*     */   public MethodDoc[] build(boolean paramBoolean) {
/*  71 */     buildImplementedMethodList(paramBoolean);
/*  72 */     return this.methlist.<MethodDoc>toArray(new MethodDoc[this.methlist.size()]);
/*     */   }
/*     */   
/*     */   public MethodDoc[] build() {
/*  76 */     return build(true);
/*     */   }
/*     */   
/*     */   public Type getMethodHolder(MethodDoc paramMethodDoc) {
/*  80 */     return this.interfaces.get(paramMethodDoc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildImplementedMethodList(boolean paramBoolean) {
/*  91 */     List<Type> list = Util.getAllInterfaces((Type)this.classdoc, this.configuration, paramBoolean);
/*  92 */     for (Type type : list) {
/*     */       
/*  94 */       MethodDoc methodDoc = Util.findMethod(type.asClassDoc(), this.method);
/*  95 */       if (methodDoc != null) {
/*  96 */         removeOverriddenMethod(methodDoc);
/*  97 */         if (!overridingMethodFound(methodDoc)) {
/*  98 */           this.methlist.add(methodDoc);
/*  99 */           this.interfaces.put(methodDoc, type);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeOverriddenMethod(MethodDoc paramMethodDoc) {
/* 113 */     ClassDoc classDoc = paramMethodDoc.overriddenClass();
/* 114 */     if (classDoc != null) {
/* 115 */       for (byte b = 0; b < this.methlist.size(); b++) {
/* 116 */         ClassDoc classDoc1 = ((MethodDoc)this.methlist.get(b)).containingClass();
/* 117 */         if (classDoc1 == classDoc || classDoc.subclassOf(classDoc1)) {
/* 118 */           this.methlist.remove(b);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
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
/*     */   private boolean overridingMethodFound(MethodDoc paramMethodDoc) {
/* 134 */     ClassDoc classDoc = paramMethodDoc.containingClass();
/* 135 */     for (byte b = 0; b < this.methlist.size(); b++) {
/* 136 */       MethodDoc methodDoc = this.methlist.get(b);
/* 137 */       if (classDoc == methodDoc.containingClass())
/*     */       {
/* 139 */         return true;
/*     */       }
/* 141 */       ClassDoc classDoc1 = methodDoc.overriddenClass();
/* 142 */       if (classDoc1 != null)
/*     */       {
/*     */         
/* 145 */         if (classDoc1 == classDoc || classDoc1.subclassOf(classDoc))
/* 146 */           return true; 
/*     */       }
/*     */     } 
/* 149 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\ImplementedMethods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */