/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxGen;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
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
/*     */ public class ValueBoxGen
/*     */   implements ValueBoxGen, JavaGenerator
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, ValueBoxEntry paramValueBoxEntry, PrintWriter paramPrintWriter) {
/*  78 */     this.symbolTable = paramHashtable;
/*  79 */     this.v = paramValueBoxEntry;
/*     */     
/*  81 */     TypedefEntry typedefEntry = ((InterfaceState)paramValueBoxEntry.state().elementAt(0)).entry;
/*  82 */     SymtabEntry symtabEntry = typedefEntry.type();
/*     */     
/*  84 */     if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*     */       
/*  86 */       openStream();
/*  87 */       if (this.stream == null)
/*     */         return; 
/*  89 */       writeHeading();
/*  90 */       writeBody();
/*  91 */       writeClosing();
/*  92 */       closeStream();
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  99 */       Enumeration<SymtabEntry> enumeration = paramValueBoxEntry.contained().elements();
/* 100 */       while (enumeration.hasMoreElements()) {
/*     */         
/* 102 */         SymtabEntry symtabEntry1 = enumeration.nextElement();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 107 */         if (symtabEntry1.type() != null)
/* 108 */           symtabEntry1.type().generate(paramHashtable, this.stream); 
/*     */       } 
/*     */     } 
/* 111 */     generateHelper();
/* 112 */     generateHolder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void openStream() {
/* 120 */     this.stream = (PrintWriter)Util.stream((SymtabEntry)this.v, ".java");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHelper() {
/* 128 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, (SymtabEntry)this.v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHolder() {
/* 136 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, (SymtabEntry)this.v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeading() {
/* 144 */     Util.writePackage(this.stream, (SymtabEntry)this.v);
/* 145 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/* 146 */     if (this.v.comment() != null) {
/* 147 */       this.v.comment().generate("", this.stream);
/*     */     }
/* 149 */     this.stream.println("public class " + this.v.name() + " implements org.omg.CORBA.portable.ValueBase");
/* 150 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody() {
/* 158 */     InterfaceState interfaceState = this.v.state().elementAt(0);
/* 159 */     TypedefEntry typedefEntry = interfaceState.entry;
/* 160 */     Util.fillInfo((SymtabEntry)typedefEntry);
/* 161 */     if (typedefEntry.comment() != null)
/* 162 */       typedefEntry.comment().generate(" ", this.stream); 
/* 163 */     this.stream.println("  public " + Util.javaName((SymtabEntry)typedefEntry) + " value;");
/* 164 */     this.stream.println("  public " + this.v.name() + " (" + Util.javaName((SymtabEntry)typedefEntry) + " initial)");
/* 165 */     this.stream.println("  {");
/* 166 */     this.stream.println("    value = initial;");
/* 167 */     this.stream.println("  }");
/* 168 */     this.stream.println();
/* 169 */     writeTruncatable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeTruncatable() {
/* 179 */     this.stream.println("  public String[] _truncatable_ids() {");
/* 180 */     this.stream.println("      return " + Util.helperName((SymtabEntry)this.v, true) + ".get_instance().get_truncatable_base_ids();");
/* 181 */     this.stream.println("  }");
/* 182 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeClosing() {
/* 190 */     this.stream.println("} // class " + this.v.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeStream() {
/* 198 */     this.stream.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeStreamableMethods() {
/* 206 */     this.stream.println("  public void _read (org.omg.CORBA.portable.InputStream istream)");
/* 207 */     this.stream.println("  {");
/* 208 */     streamableRead("this", (SymtabEntry)this.v, this.stream);
/* 209 */     this.stream.println("  }");
/* 210 */     this.stream.println();
/* 211 */     this.stream.println("  public void _write (org.omg.CORBA.portable.OutputStream ostream)");
/* 212 */     this.stream.println("  {");
/* 213 */     write(0, "    ", "this", (SymtabEntry)this.v, this.stream);
/* 214 */     this.stream.println("  }");
/* 215 */     this.stream.println();
/* 216 */     this.stream.println("  public org.omg.CORBA.TypeCode _type ()");
/* 217 */     this.stream.println("  {");
/* 218 */     this.stream.println("    return " + Util.helperName((SymtabEntry)this.v, false) + ".type ();");
/* 219 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 227 */     ValueEntry valueEntry = (ValueEntry)paramSymtabEntry;
/* 228 */     TypedefEntry typedefEntry = ((InterfaceState)valueEntry.state().elementAt(0)).entry;
/* 229 */     SymtabEntry symtabEntry = Util.typeOf((SymtabEntry)typedefEntry);
/* 230 */     paramInt = ((JavaGenerator)symtabEntry.generator()).type(paramInt, paramString1, paramTCOffsets, paramString2, symtabEntry, paramPrintWriter);
/* 231 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_value_box_tc (_id, " + '"' + paramSymtabEntry
/*     */         
/* 233 */         .name() + "\", " + paramString2 + ");");
/*     */ 
/*     */     
/* 236 */     return paramInt;
/*     */   }
/*     */   
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 240 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 241 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 246 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 253 */     paramPrintWriter.println("    return (" + paramString + ") ((org.omg.CORBA_2_3.portable.InputStream) istream).read_value (get_instance());");
/* 254 */     paramPrintWriter.println("  }");
/* 255 */     paramPrintWriter.println();
/*     */ 
/*     */ 
/*     */     
/* 259 */     paramPrintWriter.println("  public java.io.Serializable read_value (org.omg.CORBA.portable.InputStream istream)");
/* 260 */     paramPrintWriter.println("  {");
/*     */ 
/*     */     
/* 263 */     String str = "    ";
/* 264 */     Vector vector = ((ValueBoxEntry)paramSymtabEntry).state();
/* 265 */     TypedefEntry typedefEntry = ((InterfaceState)vector.elementAt(0)).entry;
/* 266 */     SymtabEntry symtabEntry = typedefEntry.type();
/* 267 */     if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof TypedefEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry || 
/*     */ 
/*     */ 
/*     */       
/* 271 */       !typedefEntry.arrayInfo().isEmpty()) {
/* 272 */       paramPrintWriter.println(str + Util.javaName(symtabEntry) + " tmp;");
/* 273 */       ((JavaGenerator)typedefEntry.generator()).read(0, str, "tmp", (SymtabEntry)typedefEntry, paramPrintWriter);
/*     */     }
/* 275 */     else if (symtabEntry instanceof ValueEntry || symtabEntry instanceof ValueBoxEntry) {
/* 276 */       paramPrintWriter.println(str + Util.javaQualifiedName(symtabEntry) + " tmp = (" + 
/* 277 */           Util.javaQualifiedName(symtabEntry) + ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + Util.helperName(symtabEntry, true) + ".get_instance ());");
/*     */     } else {
/* 279 */       paramPrintWriter.println(str + Util.javaName(symtabEntry) + " tmp = " + 
/* 280 */           Util.helperName(symtabEntry, true) + ".read (istream);");
/* 281 */     }  if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/* 282 */       paramPrintWriter.println(str + "return new " + paramString + " (tmp);");
/*     */     } else {
/* 284 */       paramPrintWriter.println(str + "return tmp;");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 291 */     paramPrintWriter.println("    ((org.omg.CORBA_2_3.portable.OutputStream) ostream).write_value (value, get_instance());");
/* 292 */     paramPrintWriter.println("  }");
/* 293 */     paramPrintWriter.println();
/*     */ 
/*     */ 
/*     */     
/* 297 */     paramPrintWriter.println("  public void write_value (org.omg.CORBA.portable.OutputStream ostream, java.io.Serializable obj)");
/* 298 */     paramPrintWriter.println("  {");
/*     */     
/* 300 */     String str = Util.javaName(paramSymtabEntry);
/* 301 */     paramPrintWriter.println("    " + str + " value  = (" + str + ") obj;");
/* 302 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 307 */     Vector vector = ((ValueEntry)paramSymtabEntry).state();
/* 308 */     TypedefEntry typedefEntry = ((InterfaceState)vector.elementAt(0)).entry;
/* 309 */     SymtabEntry symtabEntry = typedefEntry.type();
/*     */     
/* 311 */     if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || !typedefEntry.arrayInfo().isEmpty()) {
/* 312 */       paramInt = ((JavaGenerator)typedefEntry.generator()).write(paramInt, paramString1, paramString2 + ".value", (SymtabEntry)typedefEntry, paramPrintWriter);
/* 313 */     } else if (symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry || symtabEntry instanceof TypedefEntry || !typedefEntry.arrayInfo().isEmpty()) {
/* 314 */       paramInt = ((JavaGenerator)typedefEntry.generator()).write(paramInt, paramString1, paramString2, (SymtabEntry)typedefEntry, paramPrintWriter);
/* 315 */     } else if (symtabEntry instanceof ValueEntry || symtabEntry instanceof ValueBoxEntry) {
/* 316 */       paramPrintWriter.println(paramString1 + "((org.omg.CORBA_2_3.portable.OutputStream)ostream).write_value ((java.io.Serializable) value, " + 
/*     */           
/* 318 */           Util.helperName(symtabEntry, true) + ".get_instance ());");
/*     */     } else {
/*     */       
/* 321 */       paramPrintWriter.println(paramString1 + Util.helperName(symtabEntry, true) + ".write (ostream, " + paramString2 + ");");
/* 322 */     }  return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeAbstract() {}
/*     */ 
/*     */   
/*     */   protected void streamableRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 331 */     Vector vector = ((ValueBoxEntry)paramSymtabEntry).state();
/* 332 */     TypedefEntry typedefEntry = ((InterfaceState)vector.elementAt(0)).entry;
/* 333 */     SymtabEntry symtabEntry = typedefEntry.type();
/* 334 */     if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof TypedefEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry || 
/* 335 */       !typedefEntry.arrayInfo().isEmpty()) {
/*     */       
/* 337 */       TypedefEntry typedefEntry1 = ((InterfaceState)vector.elementAt(0)).entry;
/* 338 */       ((JavaGenerator)typedefEntry.generator()).read(0, "    ", paramString + ".value", (SymtabEntry)typedefEntry, paramPrintWriter);
/*     */     }
/* 340 */     else if (symtabEntry instanceof ValueEntry || symtabEntry instanceof ValueBoxEntry) {
/* 341 */       paramPrintWriter.println("    " + paramString + ".value = (" + Util.javaQualifiedName(symtabEntry) + ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + Util.helperName(symtabEntry, true) + ".get_instance ());");
/*     */     } else {
/* 343 */       paramPrintWriter.println("    " + paramString + ".value = " + Util.helperName(symtabEntry, true) + ".read (istream);");
/*     */     } 
/*     */   }
/* 346 */   protected Hashtable symbolTable = null;
/* 347 */   protected ValueBoxEntry v = null;
/* 348 */   protected PrintWriter stream = null;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\ValueBoxGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */