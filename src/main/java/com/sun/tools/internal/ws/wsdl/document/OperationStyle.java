/*    */ package com.sun.tools.internal.ws.wsdl.document;
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
/*    */ public final class OperationStyle
/*    */ {
/* 35 */   public static final OperationStyle ONE_WAY = new OperationStyle();
/* 36 */   public static final OperationStyle REQUEST_RESPONSE = new OperationStyle();
/* 37 */   public static final OperationStyle SOLICIT_RESPONSE = new OperationStyle();
/* 38 */   public static final OperationStyle NOTIFICATION = new OperationStyle();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\OperationStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */