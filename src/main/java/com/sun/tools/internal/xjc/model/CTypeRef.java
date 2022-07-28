/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeRef;
/*     */ import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
/*     */ public final class CTypeRef
/*     */   implements TypeRef<NType, NClass>
/*     */ {
/*     */   @XmlJavaTypeAdapter(RuntimeUtil.ToStringAdapter.class)
/*     */   private final CNonElement type;
/*     */   private final QName elementName;
/*     */   @Nullable
/*     */   final QName typeName;
/*     */   private final boolean nillable;
/*     */   public final XmlString defaultValue;
/*     */   
/*     */   public CTypeRef(CNonElement type, XSElementDecl decl) {
/*  70 */     this(type, BGMBuilder.getName((XSDeclaration)decl), getSimpleTypeName(decl), decl.isNillable(), decl.getDefaultValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getTypeName() {
/*  75 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public static QName getSimpleTypeName(XSElementDecl decl) {
/*  79 */     if (decl == null || !decl.getType().isSimpleType())
/*  80 */       return null; 
/*  81 */     return resolveSimpleTypeName(decl.getType());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static QName resolveSimpleTypeName(XSType declType) {
/* 102 */     QName name = BGMBuilder.getName((XSDeclaration)declType);
/* 103 */     QName result = null;
/* 104 */     if (name != null && !"http://www.w3.org/2001/XMLSchema".equals(name.getNamespaceURI())) {
/* 105 */       result = resolveSimpleTypeName(declType.getBaseType());
/*     */     }
/* 107 */     else if (!"anySimpleType".equals(declType.getName())) {
/* 108 */       result = name;
/*     */     } 
/*     */     
/* 111 */     return result;
/*     */   }
/*     */   
/*     */   public CTypeRef(CNonElement type, QName elementName, QName typeName, boolean nillable, XmlString defaultValue) {
/* 115 */     assert type != null;
/* 116 */     assert elementName != null;
/*     */     
/* 118 */     this.type = type;
/* 119 */     this.elementName = elementName;
/* 120 */     this.typeName = typeName;
/* 121 */     this.nillable = nillable;
/* 122 */     this.defaultValue = defaultValue;
/*     */   }
/*     */   
/*     */   public CNonElement getTarget() {
/* 126 */     return this.type;
/*     */   }
/*     */   
/*     */   public QName getTagName() {
/* 130 */     return this.elementName;
/*     */   }
/*     */   
/*     */   public boolean isNillable() {
/* 134 */     return this.nillable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultValue() {
/* 144 */     if (this.defaultValue != null) {
/* 145 */       return this.defaultValue.value;
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLeaf() {
/* 152 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyInfo<NType, NClass> getSource() {
/* 157 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CTypeRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */