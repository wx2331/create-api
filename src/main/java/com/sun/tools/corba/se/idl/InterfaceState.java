/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InterfaceState
/*    */ {
/*    */   public static final int Private = 0;
/*    */   public static final int Protected = 1;
/*    */   public static final int Public = 2;
/*    */   public int modifier;
/*    */   public TypedefEntry entry;
/*    */   
/*    */   public InterfaceState(int paramInt, TypedefEntry paramTypedefEntry) {
/* 59 */     this.modifier = 2;
/* 60 */     this.entry = null;
/*    */     this.modifier = paramInt;
/*    */     this.entry = paramTypedefEntry;
/*    */     if (this.modifier < 0 || this.modifier > 2)
/*    */       this.modifier = 2; 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\InterfaceState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */