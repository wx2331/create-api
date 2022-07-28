/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*    */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.activation.MimeType;
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
/*    */ 
/*    */ abstract class CSingleTypePropertyInfo
/*    */   extends CPropertyInfo
/*    */ {
/*    */   protected final TypeUse type;
/*    */   private final QName schemaType;
/*    */   
/*    */   protected CSingleTypePropertyInfo(String name, TypeUse type, QName typeName, XSComponent source, CCustomizations customizations, Locator locator) {
/* 57 */     super(name, type.isCollection(), source, customizations, locator);
/* 58 */     this.type = type;
/*    */     
/* 60 */     if (needsExplicitTypeName(type, typeName)) {
/* 61 */       this.schemaType = typeName;
/*    */     } else {
/* 63 */       this.schemaType = null;
/*    */     } 
/*    */   }
/*    */   public QName getSchemaType() {
/* 67 */     return this.schemaType;
/*    */   }
/*    */   
/*    */   public final ID id() {
/* 71 */     return this.type.idUse();
/*    */   }
/*    */   
/*    */   public final MimeType getExpectedMimeType() {
/* 75 */     return this.type.getExpectedMimeType();
/*    */   }
/*    */   
/*    */   public final List<? extends CTypeInfo> ref() {
/* 79 */     return Collections.singletonList(getTarget());
/*    */   }
/*    */   
/*    */   public final CNonElement getTarget() {
/* 83 */     CNonElement r = this.type.getInfo();
/* 84 */     assert r != null;
/* 85 */     return r;
/*    */   }
/*    */   
/*    */   public final CAdapter getAdapter() {
/* 89 */     return this.type.getAdapterUse();
/*    */   }
/*    */   
/*    */   public final CSingleTypePropertyInfo getSource() {
/* 93 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CSingleTypePropertyInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */