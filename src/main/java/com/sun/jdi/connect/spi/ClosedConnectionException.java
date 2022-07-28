/*    */ package com.sun.jdi.connect.spi;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import jdk.Exported;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Exported
/*    */ public class ClosedConnectionException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 3877032124297204774L;
/*    */   
/*    */   public ClosedConnectionException() {}
/*    */   
/*    */   public ClosedConnectionException(String paramString) {
/* 65 */     super(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\connect\spi\ClosedConnectionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */