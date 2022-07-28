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
/*    */ public class RepositoryID
/*    */ {
/*    */   private String _id;
/*    */   
/*    */   public RepositoryID() {
/* 45 */     this._id = "";
/*    */   }
/*    */ 
/*    */   
/*    */   public RepositoryID(String paramString) {
/* 50 */     this._id = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String ID() {
/* 55 */     return this._id;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object clone() {
/* 60 */     return new RepositoryID(this._id);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return ID();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean hasValidForm(String paramString) {
/* 76 */     return (paramString != null && paramString.indexOf(':') > 0);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\RepositoryID.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */