/*    */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.ExceptionEntry;
/*    */ import com.sun.tools.corba.se.idl.ExceptionGen;
/*    */ import com.sun.tools.corba.se.idl.StructEntry;
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
/*    */ public class ExceptionGen
/*    */   extends StructGen
/*    */   implements ExceptionGen
/*    */ {
/*    */   public ExceptionGen() {
/* 55 */     super(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void generate(Hashtable paramHashtable, ExceptionEntry paramExceptionEntry, PrintWriter paramPrintWriter) {
/* 63 */     generate(paramHashtable, (StructEntry)paramExceptionEntry, paramPrintWriter);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\ExceptionGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */