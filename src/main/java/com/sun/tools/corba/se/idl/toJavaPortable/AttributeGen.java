/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.AttributeEntry;
/*     */ import com.sun.tools.corba.se.idl.AttributeGen;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.ParameterEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
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
/*     */ public class AttributeGen
/*     */   extends MethodGen
/*     */   implements AttributeGen
/*     */ {
/*     */   private boolean unique(InterfaceEntry paramInterfaceEntry, String paramString) {
/*  68 */     Enumeration<SymtabEntry> enumeration = paramInterfaceEntry.methods().elements();
/*  69 */     while (enumeration.hasMoreElements()) {
/*     */       
/*  71 */       SymtabEntry symtabEntry = enumeration.nextElement();
/*  72 */       if (paramString.equals(symtabEntry.name())) {
/*  73 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  77 */     Enumeration<InterfaceEntry> enumeration1 = paramInterfaceEntry.derivedFrom().elements();
/*  78 */     while (enumeration1.hasMoreElements()) {
/*  79 */       if (!unique(enumeration1.nextElement(), paramString)) {
/*  80 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(Hashtable paramHashtable, AttributeEntry paramAttributeEntry, PrintWriter paramPrintWriter) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void interfaceMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter) {
/* 100 */     AttributeEntry attributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/*     */     
/* 103 */     super.interfaceMethod(paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter);
/*     */ 
/*     */     
/* 106 */     if (!attributeEntry.readOnly()) {
/*     */       
/* 108 */       setupForSetMethod();
/* 109 */       super.interfaceMethod(paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter);
/* 110 */       clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void stub(String paramString, boolean paramBoolean, Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt) {
/* 119 */     AttributeEntry attributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/*     */     
/* 122 */     super.stub(paramString, paramBoolean, paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter, paramInt);
/*     */ 
/*     */     
/* 125 */     if (!attributeEntry.readOnly()) {
/*     */       
/* 127 */       setupForSetMethod();
/* 128 */       super.stub(paramString, paramBoolean, paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter, paramInt + 1);
/* 129 */       clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void skeleton(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt) {
/* 138 */     AttributeEntry attributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/*     */     
/* 141 */     super.skeleton(paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter, paramInt);
/*     */ 
/*     */     
/* 144 */     if (!attributeEntry.readOnly()) {
/*     */       
/* 146 */       setupForSetMethod();
/* 147 */       super.skeleton(paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter, paramInt + 1);
/* 148 */       clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dispatchSkeleton(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt) {
/* 157 */     AttributeEntry attributeEntry = (AttributeEntry)paramMethodEntry;
/*     */ 
/*     */     
/* 160 */     super.dispatchSkeleton(paramHashtable, (MethodEntry)attributeEntry, paramPrintWriter, paramInt);
/*     */ 
/*     */     
/* 163 */     if (!attributeEntry.readOnly()) {
/*     */       
/* 165 */       setupForSetMethod();
/* 166 */       super.dispatchSkeleton(paramHashtable, paramMethodEntry, paramPrintWriter, paramInt + 1);
/* 167 */       clear();
/*     */     } 
/*     */   }
/*     */   
/* 171 */   private SymtabEntry realType = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setupForSetMethod() {
/* 178 */     ParameterEntry parameterEntry = Compile.compiler.factory.parameterEntry();
/* 179 */     parameterEntry.type(this.m.type());
/* 180 */     parameterEntry.name("new" + Util.capitalize(this.m.name()));
/* 181 */     this.m.parameters().addElement(parameterEntry);
/* 182 */     this.realType = this.m.type();
/* 183 */     this.m.type(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clear() {
/* 192 */     this.m.parameters().removeAllElements();
/* 193 */     this.m.type(this.realType);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\AttributeGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */