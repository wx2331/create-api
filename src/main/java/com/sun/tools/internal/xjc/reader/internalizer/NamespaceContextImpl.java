/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
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
/*     */ final class NamespaceContextImpl
/*     */   implements NamespaceContext
/*     */ {
/*     */   private final Element e;
/*     */   
/*     */   public NamespaceContextImpl(Element e) {
/*  51 */     this.e = e;
/*     */   }
/*     */   
/*     */   public String getNamespaceURI(String prefix) {
/*  55 */     Node parent = this.e;
/*  56 */     String namespace = null;
/*  57 */     String prefixColon = prefix + ':';
/*     */     
/*  59 */     if (prefix.equals("xml")) {
/*  60 */       namespace = "http://www.w3.org/XML/1998/namespace";
/*     */     } else {
/*     */       int type;
/*     */       
/*  64 */       while (null != parent && null == namespace && ((
/*  65 */         type = parent.getNodeType()) == 1 || type == 5)) {
/*     */         
/*  67 */         if (type == 1) {
/*  68 */           if (parent.getNodeName().startsWith(prefixColon))
/*  69 */             return parent.getNamespaceURI(); 
/*  70 */           NamedNodeMap nnm = parent.getAttributes();
/*     */           
/*  72 */           for (int i = 0; i < nnm.getLength(); i++) {
/*  73 */             Node attr = nnm.item(i);
/*  74 */             String aname = attr.getNodeName();
/*  75 */             boolean isPrefix = aname.startsWith("xmlns:");
/*     */             
/*  77 */             if (isPrefix || aname.equals("xmlns")) {
/*  78 */               int index = aname.indexOf(':');
/*  79 */               String p = isPrefix ? aname.substring(index + 1) : "";
/*     */               
/*  81 */               if (p.equals(prefix)) {
/*  82 */                 namespace = attr.getNodeValue();
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/*  90 */         parent = parent.getParentNode();
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     if (prefix.equals(""))
/*  95 */       return ""; 
/*  96 */     return namespace;
/*     */   }
/*     */   
/*     */   public String getPrefix(String namespaceURI) {
/* 100 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Iterator getPrefixes(String namespaceURI) {
/* 104 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\NamespaceContextImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */