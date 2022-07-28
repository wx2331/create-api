/*    */ package com.sun.tools.internal.xjc.reader.internalizer;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
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
/*    */ public final class LocatorTable
/*    */ {
/* 43 */   private final Map startLocations = new HashMap<>();
/*    */ 
/*    */   
/* 46 */   private final Map endLocations = new HashMap<>();
/*    */   
/*    */   public void storeStartLocation(Element e, Locator loc) {
/* 49 */     this.startLocations.put(e, new LocatorImpl(loc));
/*    */   }
/*    */   
/*    */   public void storeEndLocation(Element e, Locator loc) {
/* 53 */     this.endLocations.put(e, new LocatorImpl(loc));
/*    */   }
/*    */   
/*    */   public Locator getStartLocation(Element e) {
/* 57 */     return (Locator)this.startLocations.get(e);
/*    */   }
/*    */   
/*    */   public Locator getEndLocation(Element e) {
/* 61 */     return (Locator)this.endLocations.get(e);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\LocatorTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */