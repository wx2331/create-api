/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import java.util.Collection;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
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
/*     */ @XmlRootElement(name = "schemaBindings")
/*     */ public final class BISchemaBinding
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlType(propOrder = {})
/*     */   private static final class NameRules
/*     */   {
/*     */     private NameRules() {}
/*     */
/*     */     @XmlElement
/*  57 */     NamingRule typeName = BISchemaBinding.defaultNamingRule;
/*     */     @XmlElement
/*  59 */     NamingRule elementName = BISchemaBinding.defaultNamingRule;
/*     */     @XmlElement
/*  61 */     NamingRule attributeName = BISchemaBinding.defaultNamingRule;
/*     */     @XmlElement
/*  63 */     NamingRule modelGroupName = BISchemaBinding.defaultNamingRule;
/*     */     @XmlElement
/*  65 */     NamingRule anonymousTypeName = BISchemaBinding.defaultNamingRule; }
/*     */
/*     */   @XmlElement
/*  68 */   private NameRules nameXmlTransform = new NameRules();
/*     */
/*     */   private static final class PackageInfo {
/*     */     @XmlAttribute
/*     */     String name;
/*     */     @XmlElement
/*     */     String javadoc;
/*     */
/*     */     private PackageInfo() {} }
/*     */   @XmlElement(name = "package")
/*  78 */   private PackageInfo packageInfo = new PackageInfo();
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   @XmlAttribute(name = "map")
/*     */   public boolean map = true;
/*     */
/*     */
/*     */
/*     */
/*     */
/*  92 */   private static final NamingRule defaultNamingRule = new NamingRule("", "");
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static final class NamingRule
/*     */   {
/*     */     @XmlAttribute
/* 103 */     private String prefix = "";
/*     */     @XmlAttribute
/* 105 */     private String suffix = "";
/*     */
/*     */
/*     */     public NamingRule(String _prefix, String _suffix) {
/* 109 */       this.prefix = _prefix;
/* 110 */       this.suffix = _suffix;
/*     */     }
/*     */
/*     */
/*     */     public NamingRule() {}
/*     */
/*     */
/*     */     public String mangle(String originalName) {
/* 118 */       return this.prefix + originalName + this.suffix;
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
/*     */
/*     */
/*     */   public String mangleClassName(String name, XSComponent cmp) {
/* 133 */     if (cmp instanceof com.sun.xml.internal.xsom.XSType)
/* 134 */       return this.nameXmlTransform.typeName.mangle(name);
/* 135 */     if (cmp instanceof com.sun.xml.internal.xsom.XSElementDecl)
/* 136 */       return this.nameXmlTransform.elementName.mangle(name);
/* 137 */     if (cmp instanceof com.sun.xml.internal.xsom.XSAttributeDecl)
/* 138 */       return this.nameXmlTransform.attributeName.mangle(name);
/* 139 */     if (cmp instanceof com.sun.xml.internal.xsom.XSModelGroup || cmp instanceof com.sun.xml.internal.xsom.XSModelGroupDecl) {
/* 140 */       return this.nameXmlTransform.modelGroupName.mangle(name);
/*     */     }
/*     */
/* 143 */     return name;
/*     */   }
/*     */
/*     */   public String mangleAnonymousTypeClassName(String name) {
/* 147 */     return this.nameXmlTransform.anonymousTypeName.mangle(name);
/*     */   }
/*     */
/*     */   public String getPackageName() {
/* 151 */     return this.packageInfo.name;
/*     */   } public String getJavadoc() {
/* 153 */     return this.packageInfo.javadoc;
/*     */   } public QName getName() {
/* 155 */     return NAME;
/* 156 */   } public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "schemaBinding");
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BISchemaBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
