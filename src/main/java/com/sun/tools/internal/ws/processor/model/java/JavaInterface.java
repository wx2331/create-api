/*     */ package com.sun.tools.internal.ws.processor.model.java;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.ModelException;
/*     */ import com.sun.tools.internal.ws.util.ClassNameInfo;
/*     */ import java.util.ArrayList;
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
/*     */ public class JavaInterface
/*     */ {
/*     */   private String javadoc;
/*     */   private String name;
/*     */   private String realName;
/*     */   private String impl;
/*     */   
/*     */   public JavaInterface() {}
/*     */   
/*     */   public JavaInterface(String name) {
/*  44 */     this(name, null);
/*     */   }
/*     */   
/*     */   public JavaInterface(String name, String impl) {
/*  48 */     this.realName = name;
/*  49 */     this.name = name.replace('$', '.');
/*  50 */     this.impl = impl;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  54 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getFormalName() {
/*  58 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setFormalName(String s) {
/*  62 */     this.name = s;
/*     */   }
/*     */   
/*     */   public String getRealName() {
/*  66 */     return this.realName;
/*     */   }
/*     */   
/*     */   public void setRealName(String s) {
/*  70 */     this.realName = s;
/*     */   }
/*     */   
/*     */   public String getImpl() {
/*  74 */     return this.impl;
/*     */   }
/*     */   
/*     */   public void setImpl(String s) {
/*  78 */     this.impl = s;
/*     */   }
/*     */   
/*     */   public Iterator getMethods() {
/*  82 */     return this.methods.iterator();
/*     */   }
/*     */   
/*     */   public boolean hasMethod(JavaMethod method) {
/*  86 */     for (int i = 0; i < this.methods.size(); i++) {
/*  87 */       if (method.equals(this.methods.get(i))) {
/*  88 */         return true;
/*     */       }
/*     */     } 
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMethod(JavaMethod method) {
/*  96 */     if (hasMethod(method)) {
/*  97 */       throw new ModelException("model.uniqueness", new Object[0]);
/*     */     }
/*  99 */     this.methods.add(method);
/*     */   }
/*     */ 
/*     */   
/*     */   public List getMethodsList() {
/* 104 */     return this.methods;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMethodsList(List l) {
/* 109 */     this.methods = l;
/*     */   }
/*     */   
/*     */   public boolean hasInterface(String interfaceName) {
/* 113 */     for (int i = 0; i < this.interfaces.size(); i++) {
/* 114 */       if (interfaceName.equals(this.interfaces.get(i))) {
/* 115 */         return true;
/*     */       }
/*     */     } 
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInterface(String interfaceName) {
/* 124 */     if (hasInterface(interfaceName)) {
/*     */       return;
/*     */     }
/* 127 */     this.interfaces.add(interfaceName);
/*     */   }
/*     */   
/*     */   public Iterator getInterfaces() {
/* 131 */     return this.interfaces.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public List getInterfacesList() {
/* 136 */     return this.interfaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInterfacesList(List l) {
/* 141 */     this.interfaces = l;
/*     */   }
/*     */   
/*     */   public String getSimpleName() {
/* 145 */     return ClassNameInfo.getName(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJavaDoc() {
/* 154 */     return this.javadoc;
/*     */   }
/*     */   
/*     */   public void setJavaDoc(String javadoc) {
/* 158 */     this.javadoc = javadoc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   private List methods = new ArrayList();
/* 165 */   private List interfaces = new ArrayList();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\java\JavaInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */