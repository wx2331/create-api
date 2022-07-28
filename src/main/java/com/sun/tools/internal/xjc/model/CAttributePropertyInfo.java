/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*    */ import com.sun.tools.internal.xjc.model.nav.NType;
/*    */ import com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*    */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CAttributePropertyInfo
/*    */   extends CSingleTypePropertyInfo
/*    */   implements AttributePropertyInfo<NType, NClass>
/*    */ {
/*    */   private final QName attName;
/*    */   private final boolean isRequired;
/*    */   
/*    */   public CAttributePropertyInfo(String name, XSComponent source, CCustomizations customizations, Locator locator, QName attName, TypeUse type, @Nullable QName typeName, boolean required) {
/* 58 */     super(name, type, typeName, source, customizations, locator);
/* 59 */     this.isRequired = required;
/* 60 */     this.attName = attName;
/*    */   }
/*    */   
/*    */   public boolean isRequired() {
/* 64 */     return this.isRequired;
/*    */   }
/*    */   
/*    */   public QName getXmlName() {
/* 68 */     return this.attName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isUnboxable() {
/* 76 */     if (!this.isRequired) return false; 
/* 77 */     return super.isUnboxable();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOptionalPrimitive() {
/* 82 */     return (!this.isRequired && super.isUnboxable());
/*    */   }
/*    */   
/*    */   public <V> V accept(CPropertyVisitor<V> visitor) {
/* 86 */     return visitor.onAttribute(this);
/*    */   }
/*    */   
/*    */   public final PropertyKind kind() {
/* 90 */     return PropertyKind.ATTRIBUTE;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CAttributePropertyInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */