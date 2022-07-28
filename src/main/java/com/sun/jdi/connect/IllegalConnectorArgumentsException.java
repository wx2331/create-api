/*    */ package com.sun.jdi.connect;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ @Exported
/*    */ public class IllegalConnectorArgumentsException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -3042212603611350941L;
/*    */   List<String> names;
/*    */   
/*    */   public IllegalConnectorArgumentsException(String paramString1, String paramString2) {
/* 54 */     super(paramString1);
/* 55 */     this.names = new ArrayList<>(1);
/* 56 */     this.names.add(paramString2);
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
/*    */   public IllegalConnectorArgumentsException(String paramString, List<String> paramList) {
/* 68 */     super(paramString);
/*    */     
/* 70 */     this.names = new ArrayList<>(paramList);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> argumentNames() {
/* 79 */     return Collections.unmodifiableList(this.names);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\connect\IllegalConnectorArgumentsException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */