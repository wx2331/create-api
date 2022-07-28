/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaType;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ public abstract class AbstractType
/*     */ {
/*     */   private QName name;
/*     */   private JavaType javaType;
/*     */   
/*     */   protected AbstractType() {}
/*     */   
/*     */   protected AbstractType(QName name) {
/*  45 */     this(name, null, null);
/*     */   }
/*     */   
/*     */   protected AbstractType(QName name, String version) {
/*  49 */     this(name, null, version);
/*     */   }
/*     */   
/*     */   protected AbstractType(QName name, JavaType javaType) {
/*  53 */     this(name, javaType, null);
/*     */   }
/*     */   
/*     */   protected AbstractType(QName name, JavaType javaType, String version) {
/*  57 */     this.name = name;
/*  58 */     this.javaType = javaType;
/*  59 */     this.version = version;
/*     */   }
/*     */   
/*     */   public QName getName() {
/*  63 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(QName name) {
/*  67 */     this.name = name;
/*     */   }
/*     */   
/*     */   public JavaType getJavaType() {
/*  71 */     return this.javaType;
/*     */   }
/*     */   
/*     */   public void setJavaType(JavaType javaType) {
/*  75 */     this.javaType = javaType;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/*  79 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(String version) {
/*  83 */     this.version = version;
/*     */   }
/*     */   
/*     */   public boolean isNillable() {
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSOAPType() {
/*  91 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isLiteralType() {
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   public Object getProperty(String key) {
/*  99 */     if (this.properties == null) {
/* 100 */       return null;
/*     */     }
/* 102 */     return this.properties.get(key);
/*     */   }
/*     */   
/*     */   public void setProperty(String key, Object value) {
/* 106 */     if (value == null) {
/* 107 */       removeProperty(key);
/*     */       
/*     */       return;
/*     */     } 
/* 111 */     if (this.properties == null) {
/* 112 */       this.properties = new HashMap<>();
/*     */     }
/* 114 */     this.properties.put(key, value);
/*     */   }
/*     */   
/*     */   public void removeProperty(String key) {
/* 118 */     if (this.properties != null) {
/* 119 */       this.properties.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterator getProperties() {
/* 124 */     if (this.properties == null) {
/* 125 */       return Collections.emptyList().iterator();
/*     */     }
/* 127 */     return this.properties.keySet().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map getPropertiesMap() {
/* 133 */     return this.properties;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertiesMap(Map m) {
/* 138 */     this.properties = m;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 143 */   private String version = null;
/*     */   private Map properties;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\AbstractType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */