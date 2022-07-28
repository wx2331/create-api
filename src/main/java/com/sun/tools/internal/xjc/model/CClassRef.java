/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIClass;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIEnum;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public final class CClassRef
/*     */   extends AbstractCElement
/*     */   implements NClass, CClass
/*     */ {
/*     */   private final String fullyQualifiedClassName;
/*     */   private JClass clazz;
/*     */   
/*     */   public CClassRef(Model model, XSComponent source, BIClass decl, CCustomizations customizations) {
/*  54 */     super(model, source, decl.getLocation(), customizations);
/*  55 */     this.fullyQualifiedClassName = decl.getExistingClassRef();
/*  56 */     assert this.fullyQualifiedClassName != null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CClassRef(Model model, XSComponent source, BIEnum decl, CCustomizations customizations) {
/*  65 */     super(model, source, decl.getLocation(), customizations);
/*  66 */     this.fullyQualifiedClassName = decl.ref;
/*  67 */     assert this.fullyQualifiedClassName != null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAbstract() {}
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   public NType getType() {
/*  80 */     return (NType)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass toType(Outline o, Aspect aspect) {
/*  89 */     if (this.clazz == null)
/*  90 */       this.clazz = o.getCodeModel().ref(this.fullyQualifiedClassName); 
/*  91 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public String fullName() {
/*  95 */     return this.fullyQualifiedClassName;
/*     */   }
/*     */   
/*     */   public QName getTypeName() {
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public CNonElement getInfo() {
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CElement getSubstitutionHead() {
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   public CClassInfo getScope() {
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isBoxedType() {
/* 126 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSimpleType() {
/* 130 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CClassRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */