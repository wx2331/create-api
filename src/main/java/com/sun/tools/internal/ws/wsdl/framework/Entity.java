/*     */ package com.sun.tools.internal.ws.wsdl.framework;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ public abstract class Entity
/*     */   implements Elemental
/*     */ {
/*     */   private final Locator locator;
/*     */   protected ErrorReceiver errorReceiver;
/*     */   private Map _properties;
/*     */   
/*     */   public Entity(Locator locator) {
/*  45 */     this.locator = locator;
/*     */   }
/*     */   
/*     */   public void setErrorReceiver(ErrorReceiver errorReceiver) {
/*  49 */     this.errorReceiver = errorReceiver;
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/*  53 */     return this.locator;
/*     */   }
/*     */   
/*     */   public Object getProperty(String key) {
/*  57 */     if (this._properties == null)
/*  58 */       return null; 
/*  59 */     return this._properties.get(key);
/*     */   }
/*     */   
/*     */   public void setProperty(String key, Object value) {
/*  63 */     if (value == null) {
/*  64 */       removeProperty(key);
/*     */       
/*     */       return;
/*     */     } 
/*  68 */     if (this._properties == null) {
/*  69 */       this._properties = new HashMap<>();
/*     */     }
/*  71 */     this._properties.put(key, value);
/*     */   }
/*     */   
/*     */   public void removeProperty(String key) {
/*  75 */     if (this._properties != null) {
/*  76 */       this._properties.remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {}
/*     */ 
/*     */   
/*     */   public void withAllQNamesDo(QNameAction action) {
/*  85 */     action.perform(getElementName());
/*     */   }
/*     */ 
/*     */   
/*     */   public void withAllEntityReferencesDo(EntityReferenceAction action) {}
/*     */ 
/*     */   
/*     */   public abstract void validateThis();
/*     */   
/*     */   protected void failValidation(String key) {
/*  95 */     throw new ValidationException(key, new Object[] { getElementName().getLocalPart() });
/*     */   }
/*     */   
/*     */   protected void failValidation(String key, String arg) {
/*  99 */     throw new ValidationException(key, new Object[] { arg, 
/*     */           
/* 101 */           getElementName().getLocalPart() });
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\Entity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */