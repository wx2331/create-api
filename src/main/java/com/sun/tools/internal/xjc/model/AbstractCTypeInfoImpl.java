/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import javax.activation.MimeType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractCTypeInfoImpl
/*     */   implements CTypeInfo
/*     */ {
/*     */   private final CCustomizations customizations;
/*     */   private final XSComponent source;
/*     */   
/*     */   protected AbstractCTypeInfoImpl(Model model, XSComponent source, CCustomizations customizations) {
/*  55 */     if (customizations == null) {
/*  56 */       customizations = CCustomizations.EMPTY;
/*     */     } else {
/*  58 */       customizations.setParent(model, this);
/*  59 */     }  this.customizations = customizations;
/*  60 */     this.source = source;
/*     */   }
/*     */   
/*     */   public final boolean isCollection() {
/*  64 */     return false;
/*     */   }
/*     */   
/*     */   public final CAdapter getAdapterUse() {
/*  68 */     return null;
/*     */   }
/*     */   
/*     */   public final ID idUse() {
/*  72 */     return ID.NONE;
/*     */   }
/*     */   
/*     */   public final XSComponent getSchemaComponent() {
/*  76 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean canBeReferencedByIDREF() {
/*  85 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   public CCustomizations getCustomizations() {
/*  96 */     return this.customizations;
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression createConstant(Outline outline, XmlString lexical) {
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   public final Locatable getUpstream() {
/* 105 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public final Location getLocation() {
/* 109 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\AbstractCTypeInfoImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */