/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*    */ import com.sun.tools.internal.xjc.model.nav.NType;
/*    */ import com.sun.xml.internal.bind.v2.model.core.EnumConstant;
/*    */ import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
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
/*    */ 
/*    */ public final class CEnumConstant
/*    */   implements EnumConstant<NType, NClass>, CCustomizable
/*    */ {
/*    */   public final String name;
/*    */   public final String javadoc;
/*    */   private final String lexical;
/*    */   private CEnumLeafInfo parent;
/*    */   private final XSComponent source;
/*    */   private final CCustomizations customizations;
/*    */   private final Locator locator;
/*    */   
/*    */   public CEnumConstant(String name, String javadoc, String lexical, XSComponent source, CCustomizations customizations, Locator loc) {
/* 60 */     assert name != null;
/* 61 */     this.name = name;
/* 62 */     this.javadoc = javadoc;
/* 63 */     this.lexical = lexical;
/* 64 */     this.source = source;
/* 65 */     this.customizations = customizations;
/* 66 */     this.locator = loc;
/*    */   }
/*    */   
/*    */   public CEnumLeafInfo getEnclosingClass() {
/* 70 */     return this.parent;
/*    */   }
/*    */   
/*    */   void setParent(CEnumLeafInfo parent) {
/* 74 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public String getLexicalValue() {
/* 78 */     return this.lexical;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 82 */     return this.name;
/*    */   }
/*    */   
/*    */   public XSComponent getSchemaComponent() {
/* 86 */     return this.source;
/*    */   }
/*    */   
/*    */   public CCustomizations getCustomizations() {
/* 90 */     return this.customizations;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 94 */     return this.locator;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CEnumConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */