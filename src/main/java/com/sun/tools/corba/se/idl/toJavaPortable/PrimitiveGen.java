/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveGen;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
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
/*     */ public class PrimitiveGen
/*     */   implements PrimitiveGen, JavaGenerator
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, PrimitiveEntry paramPrimitiveEntry, PrintWriter paramPrintWriter) {}
/*     */   
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  71 */     return type(paramInt, paramString1, paramTCOffsets, paramString2, paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */   
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  75 */     paramTCOffsets.set(paramSymtabEntry);
/*  76 */     String str = "tk_null";
/*  77 */     if (paramSymtabEntry.name().equals("null")) {
/*  78 */       str = "tk_null";
/*  79 */     } else if (paramSymtabEntry.name().equals("void")) {
/*  80 */       str = "tk_void";
/*  81 */     } else if (paramSymtabEntry.name().equals("short")) {
/*  82 */       str = "tk_short";
/*  83 */     } else if (paramSymtabEntry.name().equals("long")) {
/*  84 */       str = "tk_long";
/*  85 */     } else if (paramSymtabEntry.name().equals("long long")) {
/*  86 */       str = "tk_longlong";
/*  87 */     } else if (paramSymtabEntry.name().equals("unsigned short")) {
/*  88 */       str = "tk_ushort";
/*  89 */     } else if (paramSymtabEntry.name().equals("unsigned long")) {
/*  90 */       str = "tk_ulong";
/*  91 */     } else if (paramSymtabEntry.name().equals("unsigned long long")) {
/*  92 */       str = "tk_ulonglong";
/*  93 */     } else if (paramSymtabEntry.name().equals("float")) {
/*  94 */       str = "tk_float";
/*  95 */     } else if (paramSymtabEntry.name().equals("double")) {
/*  96 */       str = "tk_double";
/*  97 */     } else if (paramSymtabEntry.name().equals("boolean")) {
/*  98 */       str = "tk_boolean";
/*  99 */     } else if (paramSymtabEntry.name().equals("char")) {
/* 100 */       str = "tk_char";
/* 101 */     } else if (paramSymtabEntry.name().equals("octet")) {
/* 102 */       str = "tk_octet";
/* 103 */     } else if (paramSymtabEntry.name().equals("any")) {
/* 104 */       str = "tk_any";
/* 105 */     } else if (paramSymtabEntry.name().equals("TypeCode")) {
/* 106 */       str = "tk_TypeCode";
/* 107 */     } else if (paramSymtabEntry.name().equals("wchar")) {
/* 108 */       str = "tk_wchar";
/* 109 */     } else if (paramSymtabEntry.name().equals("Principal")) {
/* 110 */       str = "tk_Principal";
/* 111 */     } else if (paramSymtabEntry.name().equals("wchar")) {
/* 112 */       str = "tk_wchar";
/* 113 */     }  paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind." + str + ");");
/* 114 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {}
/*     */ 
/*     */   
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 127 */     paramPrintWriter.println(paramString1 + paramString2 + " = istream.read_" + Util.collapseName(paramSymtabEntry.name()) + " ();");
/* 128 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 133 */     paramPrintWriter.println(paramString1 + "ostream.write_" + Util.collapseName(paramSymtabEntry.name()) + " (" + paramString2 + ");");
/* 134 */     return paramInt;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\PrimitiveGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */