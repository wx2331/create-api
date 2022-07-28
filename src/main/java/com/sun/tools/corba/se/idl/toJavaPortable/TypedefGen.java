/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefGen;
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
/*     */ public class TypedefGen
/*     */   implements TypedefGen, JavaGenerator
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, TypedefEntry paramTypedefEntry, PrintWriter paramPrintWriter) {
/*  81 */     this.symbolTable = paramHashtable;
/*  82 */     this.t = paramTypedefEntry;
/*     */     
/*  84 */     if (paramTypedefEntry.arrayInfo().size() > 0 || paramTypedefEntry.type() instanceof com.sun.tools.corba.se.idl.SequenceEntry)
/*  85 */       generateHolder(); 
/*  86 */     generateHelper();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHolder() {
/*  94 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, (SymtabEntry)this.t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHelper() {
/* 102 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, (SymtabEntry)this.t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean inStruct(TypedefEntry paramTypedefEntry) {
/* 110 */     boolean bool = false;
/* 111 */     if (paramTypedefEntry.container() instanceof com.sun.tools.corba.se.idl.StructEntry || paramTypedefEntry.container() instanceof com.sun.tools.corba.se.idl.UnionEntry) {
/* 112 */       bool = true;
/* 113 */     } else if (paramTypedefEntry.container() instanceof InterfaceEntry) {
/*     */       
/* 115 */       InterfaceEntry interfaceEntry = (InterfaceEntry)paramTypedefEntry.container();
/* 116 */       if (interfaceEntry.state() != null) {
/*     */         
/* 118 */         Enumeration enumeration = interfaceEntry.state().elements();
/* 119 */         while (enumeration.hasMoreElements()) {
/* 120 */           if (((InterfaceState)enumeration.nextElement()).entry == paramTypedefEntry) {
/*     */             
/* 122 */             bool = true; break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 127 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 132 */     TypedefEntry typedefEntry = (TypedefEntry)paramSymtabEntry;
/* 133 */     boolean bool = inStruct(typedefEntry);
/* 134 */     if (bool) {
/* 135 */       paramTCOffsets.setMember(paramSymtabEntry);
/*     */     } else {
/* 137 */       paramTCOffsets.set(paramSymtabEntry);
/*     */     } 
/*     */     
/* 140 */     paramInt = ((JavaGenerator)typedefEntry.type().generator()).type(paramInt, paramString1, paramTCOffsets, paramString2, typedefEntry.type(), paramPrintWriter);
/*     */     
/* 142 */     if (bool && typedefEntry.arrayInfo().size() != 0) {
/* 143 */       paramTCOffsets.bumpCurrentOffset(4);
/*     */     }
/*     */     
/* 146 */     int i = typedefEntry.arrayInfo().size();
/* 147 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 149 */       String str = Util.parseExpression(typedefEntry.arrayInfo().elementAt(b));
/* 150 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_array_tc (" + str + ", " + paramString2 + " );");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 155 */     if (!bool)
/*     */     {
/*     */       
/* 158 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_alias_tc (" + Util.helperName((SymtabEntry)typedefEntry, true) + ".id (), \"" + Util.stripLeadingUnderscores(typedefEntry.name()) + "\", " + paramString2 + ");");
/*     */     }
/* 160 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 170 */     return helperType(paramInt, paramString1, paramTCOffsets, paramString2, paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 175 */     Util.writeInitializer("    ", "value", "", paramSymtabEntry, paramPrintWriter);
/* 176 */     read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/* 177 */     paramPrintWriter.println("    return value;");
/*     */   }
/*     */ 
/*     */   
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 182 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 187 */     TypedefEntry typedefEntry = (TypedefEntry)paramSymtabEntry;
/* 188 */     String str = Util.arrayInfo(typedefEntry.arrayInfo());
/* 189 */     if (!str.equals("")) {
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
/* 210 */       byte b1 = 0;
/* 211 */       String str1 = "";
/*     */ 
/*     */       
/*     */       try {
/* 215 */         str2 = (String)typedefEntry.dynamicVariable(Compile.typedefInfo);
/*     */       }
/* 217 */       catch (NoSuchFieldException noSuchFieldException) {
/*     */         
/* 219 */         str2 = typedefEntry.name();
/*     */       } 
/* 221 */       int i = str2.indexOf('[');
/* 222 */       String str3 = Util.sansArrayInfo(str2.substring(i)) + "[]";
/* 223 */       String str2 = str2.substring(0, i);
/*     */ 
/*     */       
/* 226 */       SymtabEntry symtabEntry = (SymtabEntry)Util.symbolTable.get(str2.replace('.', '/'));
/* 227 */       if (symtabEntry instanceof InterfaceEntry && ((InterfaceEntry)symtabEntry).state() != null)
/*     */       {
/*     */         
/* 230 */         str2 = Util.javaName(symtabEntry);
/*     */       }
/*     */       
/* 233 */       while (!str.equals("")) {
/*     */         
/* 235 */         int m = str.indexOf(']');
/* 236 */         String str4 = str.substring(1, m);
/* 237 */         int k = str3.indexOf(']');
/* 238 */         str3 = '[' + str4 + str3.substring(k + 2);
/* 239 */         paramPrintWriter.println(paramString1 + paramString2 + " = new " + str2 + str3 + ';');
/* 240 */         str1 = "_o" + paramInt++;
/* 241 */         paramPrintWriter.println(paramString1 + "for (int " + str1 + " = 0;" + str1 + " < (" + str4 + "); ++" + str1 + ')');
/* 242 */         paramPrintWriter.println(paramString1 + '{');
/* 243 */         b1++;
/* 244 */         str = str.substring(m + 1);
/* 245 */         paramString1 = paramString1 + "  ";
/* 246 */         paramString2 = paramString2 + '[' + str1 + ']';
/*     */       } 
/* 248 */       int j = str3.indexOf(']');
/* 249 */       if (typedefEntry.type() instanceof com.sun.tools.corba.se.idl.SequenceEntry || typedefEntry.type() instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || typedefEntry.type() instanceof com.sun.tools.corba.se.idl.StringEntry) {
/* 250 */         paramInt = ((JavaGenerator)typedefEntry.type().generator()).read(paramInt, paramString1, paramString2, typedefEntry.type(), paramPrintWriter);
/* 251 */       } else if (typedefEntry.type() instanceof InterfaceEntry && typedefEntry.type().fullName().equals("org/omg/CORBA/Object")) {
/* 252 */         paramPrintWriter.println(paramString1 + paramString2 + " = istream.read_Object ();");
/*     */       } else {
/* 254 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(typedefEntry.type(), true) + ".read (istream);");
/* 255 */       }  for (byte b2 = 0; b2 < b1; b2++)
/*     */       {
/* 257 */         paramString1 = paramString1.substring(2);
/* 258 */         paramPrintWriter.println(paramString1 + '}');
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 263 */       SymtabEntry symtabEntry = Util.typeOf(typedefEntry.type());
/* 264 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry) {
/* 265 */         paramInt = ((JavaGenerator)symtabEntry.generator()).read(paramInt, paramString1, paramString2, symtabEntry, paramPrintWriter);
/* 266 */       } else if (symtabEntry instanceof InterfaceEntry && symtabEntry.fullName().equals("org/omg/CORBA/Object")) {
/* 267 */         paramPrintWriter.println(paramString1 + paramString2 + " = istream.read_Object ();");
/*     */       } else {
/* 269 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(symtabEntry, true) + ".read (istream);");
/*     */       } 
/* 271 */     }  return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 276 */     TypedefEntry typedefEntry = (TypedefEntry)paramSymtabEntry;
/* 277 */     String str = Util.arrayInfo(typedefEntry.arrayInfo());
/* 278 */     if (!str.equals("")) {
/*     */       
/* 280 */       byte b1 = 0;
/* 281 */       String str1 = "";
/* 282 */       while (!str.equals("")) {
/*     */         
/* 284 */         int i = str.indexOf(']');
/* 285 */         String str2 = str.substring(1, i);
/* 286 */         paramPrintWriter.println(paramString1 + "if (" + paramString2 + ".length != (" + str2 + "))");
/* 287 */         paramPrintWriter.println(paramString1 + "  throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/* 288 */         str1 = "_i" + paramInt++;
/* 289 */         paramPrintWriter.println(paramString1 + "for (int " + str1 + " = 0;" + str1 + " < (" + str2 + "); ++" + str1 + ')');
/* 290 */         paramPrintWriter.println(paramString1 + '{');
/* 291 */         b1++;
/* 292 */         str = str.substring(i + 1);
/* 293 */         paramString1 = paramString1 + "  ";
/* 294 */         paramString2 = paramString2 + '[' + str1 + ']';
/*     */       } 
/* 296 */       if (typedefEntry.type() instanceof com.sun.tools.corba.se.idl.SequenceEntry || typedefEntry.type() instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || typedefEntry.type() instanceof com.sun.tools.corba.se.idl.StringEntry) {
/* 297 */         paramInt = ((JavaGenerator)typedefEntry.type().generator()).write(paramInt, paramString1, paramString2, typedefEntry.type(), paramPrintWriter);
/* 298 */       } else if (typedefEntry.type() instanceof InterfaceEntry && typedefEntry.type().fullName().equals("org/omg/CORBA/Object")) {
/* 299 */         paramPrintWriter.println(paramString1 + "ostream.write_Object (" + paramString2 + ");");
/*     */       } else {
/* 301 */         paramPrintWriter.println(paramString1 + Util.helperName(typedefEntry.type(), true) + ".write (ostream, " + paramString2 + ");");
/* 302 */       }  for (byte b2 = 0; b2 < b1; b2++)
/*     */       {
/* 304 */         paramString1 = paramString1.substring(2);
/* 305 */         paramPrintWriter.println(paramString1 + '}');
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 310 */       SymtabEntry symtabEntry = Util.typeOf(typedefEntry.type());
/* 311 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry) {
/* 312 */         paramInt = ((JavaGenerator)symtabEntry.generator()).write(paramInt, paramString1, paramString2, symtabEntry, paramPrintWriter);
/* 313 */       } else if (symtabEntry instanceof InterfaceEntry && symtabEntry.fullName().equals("org/omg/CORBA/Object")) {
/* 314 */         paramPrintWriter.println(paramString1 + "ostream.write_Object (" + paramString2 + ");");
/*     */       } else {
/* 316 */         paramPrintWriter.println(paramString1 + Util.helperName(symtabEntry, true) + ".write (ostream, " + paramString2 + ");");
/*     */       } 
/* 318 */     }  return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 324 */   protected Hashtable symbolTable = null;
/* 325 */   protected TypedefEntry t = null;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\TypedefGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */