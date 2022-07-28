/*    */ package com.sun.tools.corba.se.idl;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ForwardValueEntry
/*    */   extends ForwardEntry
/*    */ {
/*    */   static ForwardValueGen forwardValueGen;
/*    */   
/*    */   protected ForwardValueEntry() {}
/*    */   
/*    */   protected ForwardValueEntry(ForwardValueEntry paramForwardValueEntry) {
/* 57 */     super(paramForwardValueEntry);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ForwardValueEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/* 62 */     super(paramSymtabEntry, paramIDLID);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object clone() {
/* 67 */     return new ForwardValueEntry(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter) {
/* 78 */     forwardValueGen.generate(paramHashtable, this, paramPrintWriter);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Generator generator() {
/* 86 */     return forwardValueGen;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\ForwardValueEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */