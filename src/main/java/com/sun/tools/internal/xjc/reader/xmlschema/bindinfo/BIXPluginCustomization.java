/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.Model;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import java.util.Collection;
/*    */ import javax.xml.namespace.QName;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BIXPluginCustomization
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   public final Element element;
/*    */   private QName name;
/*    */   
/*    */   public BIXPluginCustomization(Element e, Locator _loc) {
/* 52 */     super(_loc);
/* 53 */     this.element = e;
/*    */   }
/*    */   
/*    */   public void onSetOwner() {
/* 57 */     super.onSetOwner();
/* 58 */     if (!((Model)Ring.get(Model.class)).options.pluginURIs.contains(this.element.getNamespaceURI()))
/* 59 */       markAsAcknowledged(); 
/*    */   }
/*    */   
/*    */   public final QName getName() {
/* 63 */     if (this.name == null)
/* 64 */       this.name = new QName(this.element.getNamespaceURI(), this.element.getLocalName()); 
/* 65 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIXPluginCustomization.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */