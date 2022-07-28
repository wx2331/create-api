/*    */ package com.sun.xml.internal.rngom.ast.util;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocatorImpl
/*    */   implements Locator, Location
/*    */ {
/*    */   private final String systemId;
/*    */   private final int lineNumber;
/*    */   private final int columnNumber;
/*    */   
/*    */   public LocatorImpl(String systemId, int lineNumber, int columnNumber) {
/* 60 */     this.systemId = systemId;
/* 61 */     this.lineNumber = lineNumber;
/* 62 */     this.columnNumber = columnNumber;
/*    */   }
/*    */   
/*    */   public String getPublicId() {
/* 66 */     return null;
/*    */   }
/*    */   
/*    */   public String getSystemId() {
/* 70 */     return this.systemId;
/*    */   }
/*    */   
/*    */   public int getLineNumber() {
/* 74 */     return this.lineNumber;
/*    */   }
/*    */   
/*    */   public int getColumnNumber() {
/* 78 */     return this.columnNumber;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\as\\util\LocatorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */