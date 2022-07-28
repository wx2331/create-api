/*    */ package com.sun.xml.internal.xsom;
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
/*    */ public final class XSVariety
/*    */ {
/* 35 */   public static final XSVariety ATOMIC = new XSVariety("atomic");
/* 36 */   public static final XSVariety UNION = new XSVariety("union"); private final String name;
/* 37 */   public static final XSVariety LIST = new XSVariety("list");
/*    */   private XSVariety(String _name) {
/* 39 */     this.name = _name;
/*    */   } public String toString() {
/* 41 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSVariety.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */