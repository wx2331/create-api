/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.AttributeEntry;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AttributeGen24
/*     */   extends MethodGenClone24
/*     */ {
/*     */   protected void abstractMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter) {
/*  71 */     AttributeEntry attributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/*     */     
/*  74 */     super.abstractMethod(paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter);
/*     */ 
/*     */     
/*  77 */     if (!attributeEntry.readOnly()) {
/*     */       
/*  79 */       setupForSetMethod();
/*  80 */       super.abstractMethod(paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter);
/*  81 */       clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void interfaceMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter) {
/*  90 */     AttributeEntry attributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/*     */     
/*  93 */     super.interfaceMethod(paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter);
/*     */ 
/*     */     
/*  96 */     if (!attributeEntry.readOnly()) {
/*     */       
/*  98 */       setupForSetMethod();
/*  99 */       super.interfaceMethod(paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter);
/* 100 */       clear();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\AttributeGen24.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */