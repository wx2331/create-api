/*    */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*    */ 
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
/*    */ class DOMLocator
/*    */ {
/*    */   private static final String locationNamespace = "http://www.sun.com/xmlns/jaxb/dom-location";
/*    */   private static final String systemId = "systemid";
/*    */   private static final String column = "column";
/*    */   private static final String line = "line";
/*    */   
/*    */   public static void setLocationInfo(Element e, Locator loc) {
/* 40 */     e.setAttributeNS("http://www.sun.com/xmlns/jaxb/dom-location", "loc:systemid", loc.getSystemId());
/* 41 */     e.setAttributeNS("http://www.sun.com/xmlns/jaxb/dom-location", "loc:column", Integer.toString(loc.getLineNumber()));
/* 42 */     e.setAttributeNS("http://www.sun.com/xmlns/jaxb/dom-location", "loc:line", Integer.toString(loc.getColumnNumber()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Locator getLocationInfo(final Element e) {
/* 53 */     if (DOMUtil.getAttribute(e, "http://www.sun.com/xmlns/jaxb/dom-location", "systemid") == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     return new Locator() {
/*    */         public int getLineNumber() {
/* 58 */           return Integer.parseInt(DOMUtil.getAttribute(e, "http://www.sun.com/xmlns/jaxb/dom-location", "line"));
/*    */         }
/*    */         public int getColumnNumber() {
/* 61 */           return Integer.parseInt(DOMUtil.getAttribute(e, "http://www.sun.com/xmlns/jaxb/dom-location", "column"));
/*    */         }
/*    */         public String getSystemId() {
/* 64 */           return DOMUtil.getAttribute(e, "http://www.sun.com/xmlns/jaxb/dom-location", "systemid");
/*    */         }
/*    */         public String getPublicId() {
/* 67 */           return null;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\DOMLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */