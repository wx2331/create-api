/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.StructEntry;
/*     */ import com.sun.tools.corba.se.idl.StructGen;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
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
/*     */ public class StructGen
/*     */   implements StructGen, JavaGenerator
/*     */ {
/*     */   public StructGen() {}
/*     */   
/*     */   protected StructGen(boolean paramBoolean) {
/*  86 */     this.thisIsReallyAnException = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(Hashtable paramHashtable, StructEntry paramStructEntry, PrintWriter paramPrintWriter) {
/*  94 */     this.symbolTable = paramHashtable;
/*  95 */     this.s = paramStructEntry;
/*     */ 
/*     */     
/*  98 */     openStream();
/*  99 */     if (this.stream == null)
/*     */       return; 
/* 101 */     generateHelper();
/* 102 */     generateHolder();
/* 103 */     writeHeading();
/* 104 */     writeBody();
/* 105 */     writeClosing();
/* 106 */     closeStream();
/* 107 */     generateContainedTypes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void openStream() {
/* 122 */     this.stream = (PrintWriter)Util.stream((SymtabEntry)this.s, ".java");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHelper() {
/* 130 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, (SymtabEntry)this.s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHolder() {
/* 138 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, (SymtabEntry)this.s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeading() {
/* 146 */     Util.writePackage(this.stream, (SymtabEntry)this.s);
/* 147 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */     
/* 149 */     if (this.s.comment() != null) {
/* 150 */       this.s.comment().generate("", this.stream);
/*     */     }
/* 152 */     this.stream.print("public final class " + this.s.name());
/* 153 */     if (this.thisIsReallyAnException) {
/* 154 */       this.stream.print(" extends org.omg.CORBA.UserException");
/*     */     } else {
/* 156 */       this.stream.print(" implements org.omg.CORBA.portable.IDLEntity");
/* 157 */     }  this.stream.println();
/* 158 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody() {
/* 166 */     writeMembers();
/* 167 */     writeCtors();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeClosing() {
/* 175 */     this.stream.println("} // class " + this.s.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeStream() {
/* 183 */     this.stream.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateContainedTypes() {
/* 192 */     Enumeration<SymtabEntry> enumeration = this.s.contained().elements();
/* 193 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 195 */       SymtabEntry symtabEntry = enumeration.nextElement();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 202 */       if (!(symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry)) {
/* 203 */         symtabEntry.generate(this.symbolTable, this.stream);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeMembers() {
/* 213 */     int i = this.s.members().size();
/* 214 */     this.memberIsPrimitive = new boolean[i];
/* 215 */     this.memberIsInterface = new boolean[i];
/* 216 */     this.memberIsTypedef = new boolean[i];
/* 217 */     for (byte b = 0; b < this.s.members().size(); b++) {
/*     */       
/* 219 */       SymtabEntry symtabEntry = this.s.members().elementAt(b);
/* 220 */       this.memberIsPrimitive[b] = symtabEntry.type() instanceof com.sun.tools.corba.se.idl.PrimitiveEntry;
/* 221 */       this.memberIsInterface[b] = symtabEntry.type() instanceof com.sun.tools.corba.se.idl.InterfaceEntry;
/* 222 */       this.memberIsTypedef[b] = symtabEntry.type() instanceof TypedefEntry;
/* 223 */       Util.fillInfo(symtabEntry);
/*     */       
/* 225 */       if (symtabEntry.comment() != null)
/* 226 */         symtabEntry.comment().generate("  ", this.stream); 
/* 227 */       Util.writeInitializer("  public ", symtabEntry.name(), "", symtabEntry, this.stream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeCtors() {
/* 237 */     this.stream.println();
/* 238 */     this.stream.println("  public " + this.s.name() + " ()");
/* 239 */     this.stream.println("  {");
/*     */     
/* 241 */     if (this.thisIsReallyAnException)
/* 242 */       this.stream.println("    super(" + this.s.name() + "Helper.id());"); 
/* 243 */     this.stream.println("  } // ctor");
/* 244 */     writeInitializationCtor(true);
/* 245 */     if (this.thisIsReallyAnException)
/*     */     {
/*     */       
/* 248 */       writeInitializationCtor(false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeInitializationCtor(boolean paramBoolean) {
/* 255 */     if (!paramBoolean || this.s.members().size() > 0) {
/*     */       
/* 257 */       this.stream.println();
/* 258 */       this.stream.print("  public " + this.s.name() + " (");
/* 259 */       boolean bool = true;
/* 260 */       if (!paramBoolean) {
/* 261 */         this.stream.print("String $reason");
/* 262 */         bool = false;
/*     */       } 
/*     */       byte b;
/* 265 */       for (b = 0; b < this.s.members().size(); b++) {
/*     */         
/* 267 */         SymtabEntry symtabEntry = this.s.members().elementAt(b);
/* 268 */         if (bool) {
/* 269 */           bool = false;
/*     */         } else {
/* 271 */           this.stream.print(", ");
/* 272 */         }  this.stream.print(Util.javaName(symtabEntry) + " _" + symtabEntry.name());
/*     */       } 
/* 274 */       this.stream.println(")");
/* 275 */       this.stream.println("  {");
/*     */       
/* 277 */       if (this.thisIsReallyAnException)
/* 278 */         if (paramBoolean) {
/* 279 */           this.stream.println("    super(" + this.s.name() + "Helper.id());");
/*     */         } else {
/* 281 */           this.stream.println("    super(" + this.s.name() + "Helper.id() + \"  \" + $reason);");
/*     */         }  
/* 283 */       for (b = 0; b < this.s.members().size(); b++) {
/*     */         
/* 285 */         SymtabEntry symtabEntry = this.s.members().elementAt(b);
/* 286 */         this.stream.println("    " + symtabEntry.name() + " = _" + symtabEntry.name() + ";");
/*     */       } 
/* 288 */       this.stream.println("  } // ctor");
/*     */     } 
/* 290 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 298 */     TCOffsets tCOffsets = new TCOffsets();
/* 299 */     tCOffsets.set(paramSymtabEntry);
/* 300 */     int i = tCOffsets.currentOffset();
/* 301 */     StructEntry structEntry = (StructEntry)paramSymtabEntry;
/* 302 */     String str1 = "_members" + paramInt++;
/* 303 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.StructMember[] " + str1 + " = new org.omg.CORBA.StructMember [" + structEntry.members().size() + "];");
/* 304 */     String str2 = "_tcOf" + str1;
/* 305 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + str2 + " = null;");
/* 306 */     for (byte b = 0; b < structEntry.members().size(); b++) {
/*     */       
/* 308 */       TypedefEntry typedefEntry = structEntry.members().elementAt(b);
/* 309 */       String str = typedefEntry.name();
/*     */       
/* 311 */       paramInt = ((JavaGenerator)typedefEntry.generator()).type(paramInt, paramString1, tCOffsets, str2, (SymtabEntry)typedefEntry, paramPrintWriter);
/* 312 */       paramPrintWriter.println(paramString1 + str1 + '[' + b + "] = new org.omg.CORBA.StructMember (");
/* 313 */       paramPrintWriter.println(paramString1 + "  \"" + Util.stripLeadingUnderscores(str) + "\",");
/* 314 */       paramPrintWriter.println(paramString1 + "  " + str2 + ',');
/* 315 */       paramPrintWriter.println(paramString1 + "  null);");
/* 316 */       int j = tCOffsets.currentOffset();
/* 317 */       tCOffsets = new TCOffsets();
/* 318 */       tCOffsets.set(paramSymtabEntry);
/* 319 */       tCOffsets.bumpCurrentOffset(j - i);
/*     */     } 
/*     */     
/* 322 */     paramTCOffsets.bumpCurrentOffset(tCOffsets.currentOffset());
/*     */ 
/*     */     
/* 325 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_" + (this.thisIsReallyAnException ? "exception" : "struct") + "_tc (" + Util.helperName((SymtabEntry)structEntry, true) + ".id (), \"" + Util.stripLeadingUnderscores(paramSymtabEntry.name()) + "\", " + str1 + ");");
/* 326 */     return paramInt;
/*     */   }
/*     */   
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 330 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 331 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 336 */     paramPrintWriter.println("    " + paramString + " value = new " + paramString + " ();");
/* 337 */     read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/* 338 */     paramPrintWriter.println("    return value;");
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 343 */     if (this.thisIsReallyAnException) {
/*     */       
/* 345 */       paramPrintWriter.println(paramString1 + "// read and discard the repository ID");
/* 346 */       paramPrintWriter.println(paramString1 + "istream.read_string ();");
/*     */     } 
/*     */     
/* 349 */     Enumeration<TypedefEntry> enumeration = ((StructEntry)paramSymtabEntry).members().elements();
/* 350 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 352 */       TypedefEntry typedefEntry = enumeration.nextElement();
/* 353 */       SymtabEntry symtabEntry = typedefEntry.type();
/*     */       
/* 355 */       if (!typedefEntry.arrayInfo().isEmpty() || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry || symtabEntry instanceof TypedefEntry) {
/*     */ 
/*     */         
/* 358 */         paramInt = ((JavaGenerator)typedefEntry.generator()).read(paramInt, paramString1, paramString2 + '.' + typedefEntry.name(), (SymtabEntry)typedefEntry, paramPrintWriter); continue;
/* 359 */       }  if (symtabEntry instanceof ValueBoxEntry) {
/*     */ 
/*     */         
/* 362 */         Vector vector = ((ValueBoxEntry)symtabEntry).state();
/* 363 */         TypedefEntry typedefEntry1 = ((InterfaceState)vector.elementAt(0)).entry;
/* 364 */         SymtabEntry symtabEntry1 = typedefEntry1.type();
/*     */         
/* 366 */         String str1 = null;
/* 367 */         String str2 = null;
/*     */         
/* 369 */         if (symtabEntry1 instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry1 instanceof com.sun.tools.corba.se.idl.StringEntry || 
/* 370 */           !typedefEntry1.arrayInfo().isEmpty()) {
/*     */           
/* 372 */           str1 = Util.javaName(symtabEntry1);
/*     */ 
/*     */ 
/*     */           
/* 376 */           str2 = Util.helperName(symtabEntry, true);
/*     */         }
/*     */         else {
/*     */           
/* 380 */           str1 = Util.javaName(symtabEntry);
/*     */ 
/*     */           
/* 383 */           str2 = Util.helperName(symtabEntry, true);
/*     */         } 
/*     */         
/* 386 */         if (Util.corbaLevel(2.4F, 99.0F)) {
/* 387 */           paramPrintWriter.println(paramString1 + paramString2 + '.' + typedefEntry.name() + " = (" + str1 + ") " + str2 + ".read (istream);"); continue;
/*     */         } 
/* 389 */         paramPrintWriter.println(paramString1 + paramString2 + '.' + typedefEntry.name() + " = (" + str1 + ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + str2 + ".get_instance ());");
/*     */         
/*     */         continue;
/*     */       } 
/* 393 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.ValueEntry && 
/* 394 */         !Util.corbaLevel(2.4F, 99.0F)) {
/*     */ 
/*     */         
/* 397 */         paramPrintWriter.println(paramString1 + paramString2 + '.' + typedefEntry.name() + " = (" + Util.javaName(symtabEntry) + ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + Util.helperName(symtabEntry, false) + ".get_instance ());");
/*     */         continue;
/*     */       } 
/* 400 */       paramPrintWriter.println(paramString1 + paramString2 + '.' + typedefEntry.name() + " = " + Util.helperName(typedefEntry.type(), true) + ".read (istream);");
/*     */     } 
/* 402 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 407 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 412 */     if (this.thisIsReallyAnException) {
/*     */       
/* 414 */       paramPrintWriter.println(paramString1 + "// write the repository ID");
/* 415 */       paramPrintWriter.println(paramString1 + "ostream.write_string (id ());");
/*     */     } 
/*     */     
/* 418 */     Vector<TypedefEntry> vector = ((StructEntry)paramSymtabEntry).members();
/* 419 */     for (byte b = 0; b < vector.size(); b++) {
/*     */       
/* 421 */       TypedefEntry typedefEntry = vector.elementAt(b);
/* 422 */       SymtabEntry symtabEntry = typedefEntry.type();
/*     */       
/* 424 */       if (!typedefEntry.arrayInfo().isEmpty() || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof TypedefEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry) {
/*     */ 
/*     */         
/* 427 */         paramInt = ((JavaGenerator)typedefEntry.generator()).write(paramInt, "    ", paramString2 + '.' + typedefEntry.name(), (SymtabEntry)typedefEntry, paramPrintWriter);
/*     */ 
/*     */       
/*     */       }
/* 431 */       else if ((symtabEntry instanceof com.sun.tools.corba.se.idl.ValueEntry || symtabEntry instanceof ValueBoxEntry) && 
/* 432 */         !Util.corbaLevel(2.4F, 99.0F)) {
/* 433 */         paramPrintWriter.println(paramString1 + "((org.omg.CORBA_2_3.portable.OutputStream)ostream).write_value ((java.io.Serializable) " + paramString2 + '.' + typedefEntry
/* 434 */             .name() + ", " + 
/* 435 */             Util.helperName(typedefEntry.type(), true) + ".get_instance ());");
/*     */       }
/*     */       else {
/*     */         
/* 439 */         paramPrintWriter.println(paramString1 + Util.helperName(typedefEntry.type(), true) + ".write (ostream, " + paramString2 + '.' + typedefEntry.name() + ");");
/*     */       } 
/* 441 */     }  return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 447 */   protected Hashtable symbolTable = null;
/* 448 */   protected StructEntry s = null;
/* 449 */   protected PrintWriter stream = null;
/*     */   protected boolean thisIsReallyAnException = false;
/*     */   private boolean[] memberIsPrimitive;
/*     */   private boolean[] memberIsInterface;
/*     */   private boolean[] memberIsTypedef;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\StructGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */