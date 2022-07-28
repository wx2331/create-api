/*    */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*    */ 
/*    */ import com.sun.tools.corba.se.idl.ModuleEntry;
/*    */ import com.sun.tools.corba.se.idl.ModuleGen;
/*    */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Enumeration;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModuleGen
/*    */   implements ModuleGen
/*    */ {
/*    */   public void generate(Hashtable paramHashtable, ModuleEntry paramModuleEntry, PrintWriter paramPrintWriter) {
/* 66 */     String str = Util.containerFullName((SymtabEntry)paramModuleEntry);
/* 67 */     Util.mkdir(str);
/*    */ 
/*    */     
/* 70 */     Enumeration<SymtabEntry> enumeration = paramModuleEntry.contained().elements();
/* 71 */     while (enumeration.hasMoreElements()) {
/*    */       
/* 73 */       SymtabEntry symtabEntry = enumeration.nextElement();
/* 74 */       if (symtabEntry.emit())
/* 75 */         symtabEntry.generate(paramHashtable, paramPrintWriter); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\ModuleGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */