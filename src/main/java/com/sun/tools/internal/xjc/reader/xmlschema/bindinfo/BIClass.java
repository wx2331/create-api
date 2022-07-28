/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import java.util.Collection;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlRootElement(name = "class")
/*     */ public final class BIClass
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlAttribute(name = "name")
/*     */   private String className;
/*     */   @XmlAttribute(name = "implClass")
/*     */   private String userSpecifiedImplClass;
/*     */   @XmlAttribute(name = "ref")
/*     */   private String ref;
/*     */   @XmlAttribute(name = "recursive", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/*     */   private String recursive;
/*     */   @XmlElement
/*     */   private String javadoc;
/*     */   
/*     */   @Nullable
/*     */   public String getClassName() {
/*  67 */     if (this.className == null) return null;
/*     */     
/*  69 */     BIGlobalBinding gb = getBuilder().getGlobalBinding();
/*  70 */     NameConverter nc = (getBuilder()).model.getNameConverter();
/*     */     
/*  72 */     if (gb.isJavaNamingConventionEnabled()) return nc.toClassName(this.className);
/*     */ 
/*     */     
/*  75 */     return this.className;
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
/*     */   public String getUserSpecifiedImplClass() {
/*  87 */     return this.userSpecifiedImplClass;
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
/*     */   public String getExistingClassRef() {
/* 104 */     return this.ref;
/*     */   }
/*     */   
/*     */   public String getRecursive() {
/* 108 */     return this.recursive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJavadoc() {
/* 117 */     return this.javadoc;
/*     */   } public QName getName() {
/* 119 */     return NAME;
/*     */   }
/*     */   public void setParent(BindInfo p) {
/* 122 */     super.setParent(p);
/*     */ 
/*     */     
/* 125 */     if (this.ref != null) {
/* 126 */       markAsAcknowledged();
/*     */     }
/*     */   }
/*     */   
/* 130 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "class");
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */