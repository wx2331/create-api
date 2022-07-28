/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.internal.JExpression;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*    */ import com.sun.tools.internal.xjc.model.nav.NType;
/*    */ import com.sun.tools.internal.xjc.model.nav.NavigatorImpl;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import com.sun.xml.internal.bind.v2.model.core.WildcardTypeInfo;
/*    */ import com.sun.xml.internal.xsom.XmlString;
/*    */ import javax.activation.MimeType;
/*    */ import org.w3c.dom.Element;
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
/*    */ public final class CWildcardTypeInfo
/*    */   extends AbstractCTypeInfoImpl
/*    */   implements WildcardTypeInfo<NType, NClass>
/*    */ {
/*    */   private CWildcardTypeInfo() {
/* 48 */     super(null, null, null);
/*    */   }
/*    */   
/* 51 */   public static final CWildcardTypeInfo INSTANCE = new CWildcardTypeInfo();
/*    */   
/*    */   public JType toType(Outline o, Aspect aspect) {
/* 54 */     return (JType)o.getCodeModel().ref(Element.class);
/*    */   }
/*    */   
/*    */   public NType getType() {
/* 58 */     return (NType)NavigatorImpl.create(Element.class);
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 62 */     return Model.EMPTY_LOCATOR;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CWildcardTypeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */