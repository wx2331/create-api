/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.AttributeEntry;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValueGen24
/*     */   extends ValueGen
/*     */ {
/*     */   protected void writeConstructor() {}
/*     */   
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  90 */     paramPrintWriter.println("    ((org.omg.CORBA_2_3.portable.OutputStream) ostream).write_value (value, id ());");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 100 */     paramPrintWriter.println("    return (" + paramString + ")((org.omg.CORBA_2_3.portable.InputStream) istream).read_value (id ());");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInitializers() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeTruncatable() {
/* 117 */     if (!this.v.isAbstract()) {
/* 118 */       this.stream.println("  private static String[] _truncatable_ids = {");
/* 119 */       this.stream.print("    " + Util.helperName((SymtabEntry)this.v, true) + ".id ()");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 124 */       ValueEntry valueEntry = this.v;
/* 125 */       while (valueEntry.isSafe()) {
/*     */         
/* 127 */         this.stream.println(",");
/* 128 */         ValueEntry valueEntry1 = valueEntry.derivedFrom().elementAt(0);
/* 129 */         this.stream.print("    \"" + Util.stripLeadingUnderscoresFromID(valueEntry1.repositoryID().ID()) + "\"");
/* 130 */         valueEntry = valueEntry1;
/*     */       } 
/* 132 */       this.stream.println();
/* 133 */       this.stream.println("  };");
/* 134 */       this.stream.println();
/* 135 */       this.stream.println("  public String[] _truncatable_ids() {");
/* 136 */       this.stream.println("    return _truncatable_ids;");
/* 137 */       this.stream.println("  }");
/* 138 */       this.stream.println();
/*     */     } 
/*     */   }
/*     */   
/*     */   class ImplStreamWriter
/*     */   {
/*     */     private boolean isImplementsWritten = false;
/*     */     
/*     */     public void writeClassName(String param1String) {
/* 147 */       if (!this.isImplementsWritten) {
/* 148 */         ValueGen24.this.stream.print(" implements ");
/* 149 */         this.isImplementsWritten = true;
/*     */       } else {
/* 151 */         ValueGen24.this.stream.print(", ");
/*     */       } 
/* 153 */       ValueGen24.this.stream.print(param1String);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeading() {
/* 163 */     ImplStreamWriter implStreamWriter = new ImplStreamWriter();
/*     */     
/* 165 */     Util.writePackage(this.stream, (SymtabEntry)this.v);
/* 166 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */     
/* 168 */     if (this.v.comment() != null) {
/* 169 */       this.v.comment().generate("", this.stream);
/*     */     }
/* 171 */     if (this.v.isAbstract()) {
/* 172 */       writeAbstract();
/*     */       return;
/*     */     } 
/* 175 */     this.stream.print("public abstract class " + this.v.name());
/*     */ 
/*     */     
/* 178 */     SymtabEntry symtabEntry = this.v.derivedFrom().elementAt(0);
/*     */ 
/*     */     
/* 181 */     String str = Util.javaName(symtabEntry);
/* 182 */     boolean bool = false;
/*     */     
/* 184 */     if (str.equals("java.io.Serializable")) {
/* 185 */       if (this.v.isCustom())
/* 186 */       { implStreamWriter.writeClassName("org.omg.CORBA.portable.CustomValue");
/* 187 */         bool = true; }
/*     */       else
/* 189 */       { implStreamWriter.writeClassName("org.omg.CORBA.portable.StreamableValue"); } 
/* 190 */     } else if (!((ValueEntry)symtabEntry).isAbstract()) {
/* 191 */       this.stream.print(" extends " + str);
/*     */     } 
/*     */     
/* 194 */     for (byte b = 0; b < this.v.derivedFrom().size(); b++) {
/* 195 */       symtabEntry = this.v.derivedFrom().elementAt(b);
/* 196 */       if (((ValueEntry)symtabEntry).isAbstract()) {
/* 197 */         implStreamWriter.writeClassName(Util.javaName(symtabEntry));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 202 */     Enumeration<InterfaceEntry> enumeration = this.v.supports().elements();
/* 203 */     while (enumeration.hasMoreElements()) {
/* 204 */       InterfaceEntry interfaceEntry = enumeration.nextElement();
/* 205 */       String str1 = Util.javaName((SymtabEntry)interfaceEntry);
/* 206 */       if (!interfaceEntry.isAbstract())
/* 207 */         str1 = str1 + "Operations"; 
/* 208 */       implStreamWriter.writeClassName(str1);
/*     */     } 
/*     */ 
/*     */     
/* 212 */     if (this.v.isCustom() && !bool) {
/* 213 */       implStreamWriter.writeClassName("org.omg.CORBA.portable.CustomValue");
/*     */     }
/* 215 */     this.stream.println();
/* 216 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeMembers() {
/* 225 */     if (this.v.state() == null) {
/*     */       return;
/*     */     }
/* 228 */     for (byte b = 0; b < this.v.state().size(); b++) {
/*     */       
/* 230 */       InterfaceState interfaceState = this.v.state().elementAt(b);
/* 231 */       TypedefEntry typedefEntry = interfaceState.entry;
/* 232 */       Util.fillInfo((SymtabEntry)typedefEntry);
/*     */       
/* 234 */       if (typedefEntry.comment() != null) {
/* 235 */         typedefEntry.comment().generate(" ", this.stream);
/*     */       }
/* 237 */       String str = "  ";
/* 238 */       if (interfaceState.modifier == 2) {
/* 239 */         str = "  public ";
/*     */       } else {
/* 241 */         str = "  protected ";
/* 242 */       }  Util.writeInitializer(str, typedefEntry.name(), "", (SymtabEntry)typedefEntry, this.stream);
/*     */     } 
/* 244 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeMethods() {
/* 259 */     Enumeration<SymtabEntry> enumeration = this.v.contained().elements();
/* 260 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 262 */       SymtabEntry symtabEntry = enumeration.nextElement();
/* 263 */       if (symtabEntry instanceof AttributeEntry) {
/*     */         
/* 265 */         AttributeEntry attributeEntry = (AttributeEntry)symtabEntry;
/* 266 */         ((AttributeGen24)attributeEntry.generator()).abstractMethod(this.symbolTable, (MethodEntry)attributeEntry, this.stream); continue;
/*     */       } 
/* 268 */       if (symtabEntry instanceof MethodEntry) {
/*     */         
/* 270 */         MethodEntry methodEntry = (MethodEntry)symtabEntry;
/* 271 */         ((MethodGen24)methodEntry.generator()).abstractMethod(this.symbolTable, methodEntry, this.stream);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 276 */       if (symtabEntry instanceof TypedefEntry) {
/* 277 */         symtabEntry.type().generate(this.symbolTable, this.stream);
/*     */       }
/*     */ 
/*     */       
/* 281 */       symtabEntry.generate(this.symbolTable, this.stream);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     if (this.v.isAbstract()) {
/*     */       return;
/*     */     }
/*     */     
/* 292 */     if (!this.v.isCustom() && !this.v.isAbstract()) {
/* 293 */       writeStreamableMethods();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 302 */     Vector<ValueEntry> vector = ((ValueEntry)paramSymtabEntry).derivedFrom();
/* 303 */     if (vector != null && vector.size() != 0) {
/*     */       
/* 305 */       ValueEntry valueEntry = vector.elementAt(0);
/* 306 */       if (valueEntry == null) {
/* 307 */         return paramInt;
/*     */       }
/*     */       
/* 310 */       if (!valueEntry.isAbstract() && !Util.javaQualifiedName((SymtabEntry)valueEntry).equals("java.io.Serializable")) {
/* 311 */         paramPrintWriter.println(paramString1 + "super._read (istream);");
/*     */       }
/*     */     } 
/* 314 */     Vector vector1 = ((ValueEntry)paramSymtabEntry).state();
/* 315 */     byte b1 = (vector1 == null) ? 0 : vector1.size();
/*     */     
/* 317 */     for (byte b2 = 0; b2 < b1; b2++) {
/*     */       
/* 319 */       TypedefEntry typedefEntry = ((InterfaceState)vector1.elementAt(b2)).entry;
/* 320 */       String str = typedefEntry.name();
/* 321 */       SymtabEntry symtabEntry = typedefEntry.type();
/*     */       
/* 323 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof TypedefEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry || 
/*     */ 
/*     */ 
/*     */         
/* 327 */         !typedefEntry.arrayInfo().isEmpty()) {
/* 328 */         paramInt = ((JavaGenerator)typedefEntry.generator()).read(paramInt, paramString1, paramString2 + '.' + str, (SymtabEntry)typedefEntry, paramPrintWriter);
/*     */       } else {
/* 330 */         paramPrintWriter.println(paramString1 + paramString2 + '.' + str + " = " + 
/* 331 */             Util.helperName(symtabEntry, true) + ".read (istream);");
/*     */       } 
/*     */     } 
/* 334 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 343 */     Vector<ValueEntry> vector = ((ValueEntry)paramSymtabEntry).derivedFrom();
/* 344 */     if (vector != null && vector.size() != 0) {
/*     */       
/* 346 */       ValueEntry valueEntry = vector.elementAt(0);
/* 347 */       if (valueEntry == null) {
/* 348 */         return paramInt;
/*     */       }
/* 350 */       if (!valueEntry.isAbstract() && !Util.javaQualifiedName((SymtabEntry)valueEntry).equals("java.io.Serializable")) {
/* 351 */         paramPrintWriter.println(paramString1 + "super._write (ostream);");
/*     */       }
/*     */     } 
/* 354 */     Vector vector1 = ((ValueEntry)paramSymtabEntry).state();
/* 355 */     byte b1 = (vector1 == null) ? 0 : vector1.size();
/* 356 */     for (byte b2 = 0; b2 < b1; b2++) {
/*     */       
/* 358 */       TypedefEntry typedefEntry = ((InterfaceState)vector1.elementAt(b2)).entry;
/* 359 */       String str = typedefEntry.name();
/* 360 */       SymtabEntry symtabEntry = typedefEntry.type();
/*     */       
/* 362 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof TypedefEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry || 
/*     */ 
/*     */ 
/*     */         
/* 366 */         !typedefEntry.arrayInfo().isEmpty()) {
/* 367 */         paramInt = ((JavaGenerator)typedefEntry.generator()).write(paramInt, paramString1, paramString2 + '.' + str, (SymtabEntry)typedefEntry, paramPrintWriter);
/*     */       } else {
/* 369 */         paramPrintWriter.println(paramString1 + Util.helperName(symtabEntry, true) + ".write (ostream, " + paramString2 + '.' + str + ");");
/*     */       } 
/*     */     } 
/*     */     
/* 373 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(Hashtable paramHashtable, ValueEntry paramValueEntry, PrintWriter paramPrintWriter) {
/* 381 */     this.symbolTable = paramHashtable;
/* 382 */     this.v = paramValueEntry;
/* 383 */     init();
/*     */     
/* 385 */     openStream();
/* 386 */     if (this.stream == null)
/*     */       return; 
/* 388 */     generateTie();
/* 389 */     generateHelper();
/* 390 */     generateHolder();
/* 391 */     if (!paramValueEntry.isAbstract()) {
/* 392 */       generateValueFactory();
/* 393 */       generateDefaultFactory();
/*     */     } 
/* 395 */     writeHeading();
/* 396 */     writeBody();
/* 397 */     writeClosing();
/* 398 */     closeStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateValueFactory() {
/* 406 */     ((Factories)Compile.compiler.factories()).valueFactory().generate(this.symbolTable, (SymtabEntry)this.v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateDefaultFactory() {
/* 414 */     ((Factories)Compile.compiler.factories()).defaultFactory().generate(this.symbolTable, (SymtabEntry)this.v);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\ValueGen24.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */