/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*    */ import com.sun.tools.internal.xjc.model.nav.NType;
/*    */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*    */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*    */ import com.sun.xml.internal.bind.v2.model.core.ValuePropertyInfo;
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
/*    */ public final class CValuePropertyInfo
/*    */   extends CSingleTypePropertyInfo
/*    */   implements ValuePropertyInfo<NType, NClass>
/*    */ {
/*    */   public CValuePropertyInfo(String name, XSComponent source, CCustomizations customizations, Locator locator, TypeUse type, QName typeName) {
/* 45 */     super(name, type, typeName, source, customizations, locator);
/*    */   }
/*    */   
/*    */   public final PropertyKind kind() {
/* 49 */     return PropertyKind.VALUE;
/*    */   }
/*    */   
/*    */   public <V> V accept(CPropertyVisitor<V> visitor) {
/* 53 */     return visitor.onValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CValuePropertyInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */