/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ModelObject
/*     */ {
/*     */   private final Entity entity;
/*     */   protected ErrorReceiver errorReceiver;
/*     */   private String javaDoc;
/*     */   private Map _properties;
/*     */   
/*     */   public abstract void accept(ModelVisitor paramModelVisitor) throws Exception;
/*     */   
/*     */   protected ModelObject(Entity entity) {
/*  49 */     this.entity = entity;
/*     */   }
/*     */   
/*     */   public void setErrorReceiver(ErrorReceiver errorReceiver) {
/*  53 */     this.errorReceiver = errorReceiver;
/*     */   }
/*     */   
/*     */   public Entity getEntity() {
/*  57 */     return this.entity;
/*     */   }
/*     */   
/*     */   public Object getProperty(String key) {
/*  61 */     if (this._properties == null) {
/*  62 */       return null;
/*     */     }
/*  64 */     return this._properties.get(key);
/*     */   }
/*     */   
/*     */   public void setProperty(String key, Object value) {
/*  68 */     if (value == null) {
/*  69 */       removeProperty(key);
/*     */       
/*     */       return;
/*     */     } 
/*  73 */     if (this._properties == null) {
/*  74 */       this._properties = new HashMap<>();
/*     */     }
/*  76 */     this._properties.put(key, value);
/*     */   }
/*     */   
/*     */   public void removeProperty(String key) {
/*  80 */     if (this._properties != null) {
/*  81 */       this._properties.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterator getProperties() {
/*  86 */     if (this._properties == null) {
/*  87 */       return Collections.emptyList().iterator();
/*     */     }
/*  89 */     return this._properties.keySet().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Locator getLocator() {
/*  94 */     return this.entity.getLocator();
/*     */   }
/*     */   
/*     */   public Map getPropertiesMap() {
/*  98 */     return this._properties;
/*     */   }
/*     */   
/*     */   public void setPropertiesMap(Map m) {
/* 102 */     this._properties = m;
/*     */   }
/*     */   
/*     */   public String getJavaDoc() {
/* 106 */     return this.javaDoc;
/*     */   }
/*     */   
/*     */   public void setJavaDoc(String javaDoc) {
/* 110 */     this.javaDoc = javaDoc;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\ModelObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */