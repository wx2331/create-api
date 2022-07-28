/*    */ package com.sun.tools.internal.ws.wsdl.parser;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import javax.xml.namespace.NamespaceContext;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
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
/*    */ public class NamespaceContextImpl
/*    */   implements NamespaceContext
/*    */ {
/*    */   private final Element e;
/*    */   
/*    */   public NamespaceContextImpl(Element e) {
/* 47 */     this.e = e;
/*    */   }
/*    */   
/*    */   public String getNamespaceURI(String prefix) {
/* 51 */     Node parent = this.e;
/* 52 */     String namespace = null;
/*    */     
/* 54 */     if (prefix.equals("xml")) {
/* 55 */       namespace = "http://www.w3.org/XML/1998/namespace";
/*    */     } else {
/*    */       int type;
/*    */       
/* 59 */       while (null != parent && null == namespace && ((
/* 60 */         type = parent.getNodeType()) == 1 || type == 5)) {
/*    */         
/* 62 */         if (type == 1) {
/* 63 */           if (parent.getNodeName().indexOf(prefix + ':') == 0)
/* 64 */             return parent.getNamespaceURI(); 
/* 65 */           NamedNodeMap nnm = parent.getAttributes();
/*    */           
/* 67 */           for (int i = 0; i < nnm.getLength(); i++) {
/* 68 */             Node attr = nnm.item(i);
/* 69 */             String aname = attr.getNodeName();
/* 70 */             boolean isPrefix = aname.startsWith("xmlns:");
/*    */             
/* 72 */             if (isPrefix || aname.equals("xmlns")) {
/* 73 */               int index = aname.indexOf(':');
/* 74 */               String p = isPrefix ? aname.substring(index + 1) : "";
/*    */               
/* 76 */               if (p.equals(prefix)) {
/* 77 */                 namespace = attr.getNodeValue();
/*    */                 
/*    */                 break;
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         } 
/*    */         
/* 85 */         parent = parent.getParentNode();
/*    */       } 
/*    */     } 
/*    */     
/* 89 */     return namespace;
/*    */   }
/*    */   
/*    */   public String getPrefix(String namespaceURI) {
/* 93 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public Iterator getPrefixes(String namespaceURI) {
/* 97 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\NamespaceContextImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */