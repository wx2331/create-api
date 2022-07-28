/*    */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import java.util.StringTokenizer;
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
/*    */ public final class BIInterface
/*    */ {
/*    */   private final Element dom;
/*    */   private final String name;
/*    */   private final String[] members;
/*    */   private final String[] fields;
/*    */   
/*    */   BIInterface(Element e) {
/* 39 */     this.dom = e;
/* 40 */     this.name = DOMUtil.getAttribute(e, "name");
/* 41 */     this.members = parseTokens(DOMUtil.getAttribute(e, "members"));
/*    */     
/* 43 */     if (DOMUtil.getAttribute(e, "properties") != null) {
/* 44 */       this.fields = parseTokens(DOMUtil.getAttribute(e, "properties"));
/* 45 */       throw new AssertionError("//interface/@properties is not supported");
/*    */     } 
/* 47 */     this.fields = new String[0];
/*    */   }
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
/*    */   public String name() {
/* 60 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] members() {
/* 69 */     return this.members;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] fields() {
/* 75 */     return this.fields;
/*    */   }
/*    */ 
/*    */   
/*    */   public Locator getSourceLocation() {
/* 80 */     return DOMLocator.getLocationInfo(this.dom);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String[] parseTokens(String value) {
/* 87 */     StringTokenizer tokens = new StringTokenizer(value);
/*    */     
/* 89 */     String[] r = new String[tokens.countTokens()];
/* 90 */     int i = 0;
/* 91 */     while (tokens.hasMoreTokens()) {
/* 92 */       r[i++] = tokens.nextToken();
/*    */     }
/* 94 */     return r;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\BIInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */