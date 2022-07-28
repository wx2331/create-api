/*    */ package com.sun.tools.internal.ws.util.xml;
/*    */ 
/*    */ import com.sun.tools.internal.ws.util.WSDLParseException;
/*    */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.w3c.dom.Element;
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
/*    */ public class XmlUtil
/*    */   extends XmlUtil
/*    */ {
/*    */   public static boolean matchesTagNS(Element e, String tag, String nsURI) {
/*    */     try {
/* 38 */       return (e.getLocalName().equals(tag) && e
/* 39 */         .getNamespaceURI().equals(nsURI));
/* 40 */     } catch (NullPointerException npe) {
/*    */ 
/*    */       
/* 43 */       throw new WSDLParseException("null.namespace.found", new Object[] { e
/*    */             
/* 45 */             .getLocalName() });
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean matchesTagNS(Element e, QName name) {
/*    */     try {
/* 53 */       return (e.getLocalName().equals(name.getLocalPart()) && e
/* 54 */         .getNamespaceURI().equals(name.getNamespaceURI()));
/* 55 */     } catch (NullPointerException npe) {
/*    */ 
/*    */       
/* 58 */       throw new WSDLParseException("null.namespace.found", new Object[] { e
/*    */             
/* 60 */             .getLocalName() });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\w\\util\xml\XmlUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */