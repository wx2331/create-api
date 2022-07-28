/*    */ package com.sun.tools.internal.xjc.util;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XmlString;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import javax.xml.namespace.NamespaceContext;
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
/*    */ public final class NamespaceContextAdapter
/*    */   implements NamespaceContext
/*    */ {
/*    */   private XmlString xstr;
/*    */   
/*    */   public NamespaceContextAdapter(XmlString xstr) {
/* 46 */     this.xstr = xstr;
/*    */   }
/*    */   
/*    */   public String getNamespaceURI(String prefix) {
/* 50 */     return this.xstr.resolvePrefix(prefix);
/*    */   }
/*    */   
/*    */   public String getPrefix(String namespaceURI) {
/* 54 */     return null;
/*    */   }
/*    */   
/*    */   public Iterator getPrefixes(String namespaceURI) {
/* 58 */     return Collections.EMPTY_LIST.iterator();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\NamespaceContextAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */