/*    */ package com.sun.tools.internal.xjc.util;
/*    */ 
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*    */ public abstract class ReadOnlyAdapter<OnTheWire, InMemory>
/*    */   extends XmlAdapter<OnTheWire, InMemory>
/*    */ {
/*    */   public final OnTheWire marshal(InMemory onTheWire) {
/* 40 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\ReadOnlyAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */