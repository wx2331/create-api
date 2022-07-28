/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlTransient;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlRootElement(name = "typesafeEnumClass")
/*     */ public final class BIEnum
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlAttribute(name = "map")
/*     */   private boolean map = true;
/*     */   @XmlAttribute(name = "name")
/*  64 */   public String className = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "ref")
/*     */   public String ref;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement
/*  77 */   public final String javadoc = null;
/*     */ 
/*     */   
/*     */   public boolean isMapped() {
/*  81 */     return this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlTransient
/*  90 */   public final Map<String, BIEnumMember> members = new HashMap<>();
/*     */   
/*     */   public QName getName() {
/*  93 */     return NAME;
/*     */   }
/*     */   public void setParent(BindInfo p) {
/*  96 */     super.setParent(p);
/*  97 */     for (BIEnumMember mem : this.members.values()) {
/*  98 */       mem.setParent(p);
/*     */     }
/*     */ 
/*     */     
/* 102 */     if (this.ref != null) {
/* 103 */       markAsAcknowledged();
/*     */     }
/*     */   }
/*     */   
/* 107 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "enum");
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(name = "typesafeEnumMember")
/*     */   private void setMembers(BIEnumMember2[] mems) {
/* 113 */     for (BIEnumMember2 e : mems)
/* 114 */       this.members.put(e.value, e); 
/*     */   }
/*     */   
/*     */   static class BIEnumMember2 extends BIEnumMember {
/*     */     @XmlAttribute(required = true)
/*     */     String value;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */