/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueGen;
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
/*     */ public class ValueGen
/*     */   implements ValueGen, JavaGenerator
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, ValueEntry paramValueEntry, PrintWriter paramPrintWriter) {
/*  87 */     this.symbolTable = paramHashtable;
/*  88 */     this.v = paramValueEntry;
/*  89 */     init();
/*     */     
/*  91 */     openStream();
/*  92 */     if (this.stream == null)
/*     */       return; 
/*  94 */     generateTie();
/*  95 */     generateHelper();
/*  96 */     generateHolder();
/*  97 */     writeHeading();
/*  98 */     writeBody();
/*  99 */     writeClosing();
/* 100 */     closeStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 108 */     this.emit = ((Arguments)Compile.compiler.arguments).emit;
/* 109 */     this.factories = (Factories)Compile.compiler.factories();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void openStream() {
/* 117 */     this.stream = (PrintWriter)Util.stream((SymtabEntry)this.v, ".java");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateTie() {
/* 126 */     boolean bool = ((Arguments)Compile.compiler.arguments).TIEServer;
/* 127 */     if (this.v.supports().size() > 0 && bool) {
/*     */       
/* 129 */       Factories factories = (Factories)Compile.compiler.factories();
/* 130 */       factories.skeleton().generate(this.symbolTable, (SymtabEntry)this.v);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHelper() {
/* 139 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, (SymtabEntry)this.v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHolder() {
/* 147 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, (SymtabEntry)this.v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeading() {
/* 155 */     Util.writePackage(this.stream, (SymtabEntry)this.v);
/* 156 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */     
/* 158 */     if (this.v.comment() != null) {
/* 159 */       this.v.comment().generate("", this.stream);
/*     */     }
/* 161 */     if (this.v.isAbstract()) {
/*     */       
/* 163 */       writeAbstract();
/*     */       
/*     */       return;
/*     */     } 
/* 167 */     this.stream.print("public class " + this.v.name());
/*     */ 
/*     */     
/* 170 */     SymtabEntry symtabEntry = this.v.derivedFrom().elementAt(0);
/*     */ 
/*     */     
/* 173 */     String str = Util.javaName(symtabEntry);
/* 174 */     boolean bool = false;
/*     */     
/* 176 */     if (str.equals("java.io.Serializable")) {
/*     */ 
/*     */       
/* 179 */       this.stream.print(" implements org.omg.CORBA.portable.ValueBase");
/* 180 */       bool = true;
/*     */     }
/* 182 */     else if (!((ValueEntry)symtabEntry).isAbstract()) {
/* 183 */       this.stream.print(" extends " + str);
/*     */     } 
/*     */     
/* 186 */     for (byte b = 0; b < this.v.derivedFrom().size(); b++) {
/* 187 */       symtabEntry = this.v.derivedFrom().elementAt(b);
/* 188 */       if (((ValueEntry)symtabEntry).isAbstract()) {
/*     */         
/* 190 */         if (!bool) {
/*     */           
/* 192 */           this.stream.print(" implements ");
/* 193 */           bool = true;
/*     */         } else {
/*     */           
/* 196 */           this.stream.print(", ");
/* 197 */         }  this.stream.print(Util.javaName(symtabEntry));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 202 */     if (this.v.supports().size() > 0) {
/* 203 */       if (!bool) {
/*     */         
/* 205 */         this.stream.print(" implements ");
/* 206 */         bool = true;
/*     */       } else {
/*     */         
/* 209 */         this.stream.print(", ");
/*     */       } 
/* 211 */       InterfaceEntry interfaceEntry = this.v.supports().elementAt(0);
/*     */       
/* 213 */       if (interfaceEntry.isAbstract()) {
/* 214 */         this.stream.print(Util.javaName((SymtabEntry)interfaceEntry));
/*     */       } else {
/* 216 */         this.stream.print(Util.javaName((SymtabEntry)interfaceEntry) + "Operations");
/*     */       } 
/*     */     } 
/*     */     
/* 220 */     if (this.v.isCustom()) {
/* 221 */       if (!bool) {
/*     */         
/* 223 */         this.stream.print(" implements ");
/* 224 */         bool = true;
/*     */       } else {
/*     */         
/* 227 */         this.stream.print(", ");
/*     */       } 
/* 229 */       this.stream.print("org.omg.CORBA.CustomMarshal ");
/*     */     } 
/*     */     
/* 232 */     this.stream.println();
/* 233 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody() {
/* 241 */     writeMembers();
/* 242 */     writeInitializers();
/* 243 */     writeConstructor();
/* 244 */     writeTruncatable();
/* 245 */     writeMethods();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeClosing() {
/* 253 */     if (this.v.isAbstract()) {
/* 254 */       this.stream.println("} // interface " + this.v.name());
/*     */     } else {
/* 256 */       this.stream.println("} // class " + this.v.name());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeStream() {
/* 264 */     this.stream.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeConstructor() {
/* 273 */     if (!this.v.isAbstract() && !this.explicitDefaultInit) {
/* 274 */       this.stream.println("  protected " + this.v.name() + " () {}");
/* 275 */       this.stream.println();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeTruncatable() {
/* 285 */     if (!this.v.isAbstract()) {
/* 286 */       this.stream.println("  public String[] _truncatable_ids() {");
/* 287 */       this.stream.println("      return " + Util.helperName((SymtabEntry)this.v, true) + ".get_instance().get_truncatable_base_ids();");
/* 288 */       this.stream.println("  }");
/* 289 */       this.stream.println();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeMembers() {
/* 299 */     if (this.v.state() == null) {
/*     */       return;
/*     */     }
/* 302 */     for (byte b = 0; b < this.v.state().size(); b++) {
/*     */       
/* 304 */       InterfaceState interfaceState = this.v.state().elementAt(b);
/* 305 */       TypedefEntry typedefEntry = interfaceState.entry;
/* 306 */       Util.fillInfo((SymtabEntry)typedefEntry);
/*     */       
/* 308 */       if (typedefEntry.comment() != null) {
/* 309 */         typedefEntry.comment().generate(" ", this.stream);
/*     */       }
/* 311 */       String str = "  ";
/* 312 */       if (interfaceState.modifier == 2)
/* 313 */         str = "  public "; 
/* 314 */       Util.writeInitializer(str, typedefEntry.name(), "", (SymtabEntry)typedefEntry, this.stream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInitializers() {
/* 323 */     Vector<MethodEntry> vector = this.v.initializers();
/* 324 */     if (vector != null) {
/*     */       
/* 326 */       this.stream.println();
/* 327 */       for (byte b = 0; b < vector.size(); b++) {
/*     */         
/* 329 */         MethodEntry methodEntry = vector.elementAt(b);
/* 330 */         methodEntry.valueMethod(true);
/* 331 */         ((MethodGen)methodEntry.generator()).interfaceMethod(this.symbolTable, methodEntry, this.stream);
/* 332 */         if (methodEntry.parameters().isEmpty()) {
/* 333 */           this.explicitDefaultInit = true;
/*     */         }
/*     */       } 
/*     */     } 
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
/*     */   protected void writeMethods() {
/* 349 */     Enumeration<SymtabEntry> enumeration = this.v.contained().elements();
/* 350 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 352 */       SymtabEntry symtabEntry = enumeration.nextElement();
/* 353 */       if (symtabEntry instanceof MethodEntry) {
/*     */         
/* 355 */         MethodEntry methodEntry = (MethodEntry)symtabEntry;
/* 356 */         ((MethodGen)methodEntry.generator()).interfaceMethod(this.symbolTable, methodEntry, this.stream);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 361 */       if (symtabEntry instanceof TypedefEntry) {
/* 362 */         symtabEntry.type().generate(this.symbolTable, this.stream);
/*     */       }
/*     */ 
/*     */       
/* 366 */       symtabEntry.generate(this.symbolTable, this.stream);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 373 */     if (this.v.isAbstract()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 380 */     if (this.v.supports().size() > 0) {
/*     */       
/* 382 */       InterfaceEntry interfaceEntry = this.v.supports().elementAt(0);
/* 383 */       Enumeration<MethodEntry> enumeration1 = interfaceEntry.allMethods().elements();
/* 384 */       while (enumeration1.hasMoreElements()) {
/*     */         
/* 386 */         MethodEntry methodEntry1 = enumeration1.nextElement();
/*     */ 
/*     */ 
/*     */         
/* 390 */         MethodEntry methodEntry2 = (MethodEntry)methodEntry1.clone();
/* 391 */         methodEntry2.container((SymtabEntry)this.v);
/* 392 */         ((MethodGen)methodEntry2.generator()).interfaceMethod(this.symbolTable, methodEntry2, this.stream);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 398 */     for (byte b = 0; b < this.v.derivedFrom().size(); b++) {
/* 399 */       ValueEntry valueEntry = this.v.derivedFrom().elementAt(b);
/* 400 */       if (valueEntry.isAbstract()) {
/*     */         
/* 402 */         Enumeration<MethodEntry> enumeration1 = valueEntry.allMethods().elements();
/* 403 */         while (enumeration1.hasMoreElements()) {
/*     */           
/* 405 */           MethodEntry methodEntry1 = enumeration1.nextElement();
/*     */ 
/*     */ 
/*     */           
/* 409 */           MethodEntry methodEntry2 = (MethodEntry)methodEntry1.clone();
/* 410 */           methodEntry2.container((SymtabEntry)this.v);
/* 411 */           ((MethodGen)methodEntry2.generator()).interfaceMethod(this.symbolTable, methodEntry2, this.stream);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeStreamableMethods() {
/* 424 */     this.stream.println("  public void _read (org.omg.CORBA.portable.InputStream istream)");
/* 425 */     this.stream.println("  {");
/* 426 */     read(0, "    ", "this", (SymtabEntry)this.v, this.stream);
/* 427 */     this.stream.println("  }");
/* 428 */     this.stream.println();
/* 429 */     this.stream.println("  public void _write (org.omg.CORBA.portable.OutputStream ostream)");
/* 430 */     this.stream.println("  {");
/* 431 */     write(0, "    ", "this", (SymtabEntry)this.v, this.stream);
/* 432 */     this.stream.println("  }");
/* 433 */     this.stream.println();
/* 434 */     this.stream.println("  public org.omg.CORBA.TypeCode _type ()");
/* 435 */     this.stream.println("  {");
/* 436 */     this.stream.println("    return " + Util.helperName((SymtabEntry)this.v, false) + ".type ();");
/* 437 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 445 */     ValueEntry valueEntry = (ValueEntry)paramSymtabEntry;
/* 446 */     Vector<InterfaceState> vector = valueEntry.state();
/* 447 */     byte b1 = (vector == null) ? 0 : vector.size();
/* 448 */     String str1 = "_members" + paramInt++;
/* 449 */     String str2 = "_tcOf" + str1;
/*     */     
/* 451 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.ValueMember[] " + str1 + " = new org.omg.CORBA.ValueMember[" + b1 + "];");
/*     */ 
/*     */ 
/*     */     
/* 455 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + str2 + " = null;");
/*     */ 
/*     */     
/* 458 */     String str3 = "_id";
/*     */ 
/*     */     
/* 461 */     for (byte b2 = 0; b2 < b1; b2++) {
/*     */       String str4, str5;
/* 463 */       InterfaceState interfaceState = vector.elementAt(b2);
/* 464 */       TypedefEntry typedefEntry = interfaceState.entry;
/* 465 */       SymtabEntry symtabEntry = Util.typeOf((SymtabEntry)typedefEntry);
/* 466 */       if (hasRepId((SymtabEntry)typedefEntry)) {
/*     */         
/* 468 */         str4 = Util.helperName(symtabEntry, true) + ".id ()";
/* 469 */         if (symtabEntry instanceof ValueEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.ValueBoxEntry) {
/*     */           
/* 471 */           str5 = "\"\"";
/*     */         } else {
/*     */           
/* 474 */           String str = symtabEntry.repositoryID().ID();
/* 475 */           str5 = '"' + str.substring(str.lastIndexOf(':') + 1) + '"';
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 480 */         str4 = "\"\"";
/* 481 */         str5 = "\"\"";
/*     */       } 
/*     */ 
/*     */       
/* 485 */       paramPrintWriter.println(paramString1 + "// ValueMember instance for " + typedefEntry.name());
/* 486 */       paramInt = ((JavaGenerator)typedefEntry.generator()).type(paramInt, paramString1, paramTCOffsets, str2, (SymtabEntry)typedefEntry, paramPrintWriter);
/* 487 */       paramPrintWriter.println(paramString1 + str1 + "[" + b2 + "] = new org.omg.CORBA.ValueMember (" + '"' + typedefEntry
/* 488 */           .name() + "\", ");
/* 489 */       paramPrintWriter.println(paramString1 + "    " + str4 + ", ");
/* 490 */       paramPrintWriter.println(paramString1 + "    " + str3 + ", ");
/* 491 */       paramPrintWriter.println(paramString1 + "    " + str5 + ", ");
/* 492 */       paramPrintWriter.println(paramString1 + "    " + str2 + ", ");
/* 493 */       paramPrintWriter.println(paramString1 + "    null, ");
/* 494 */       paramPrintWriter.println(paramString1 + "    org.omg.CORBA." + ((interfaceState.modifier == 2) ? "PUBLIC_MEMBER" : "PRIVATE_MEMBER") + ".value);");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 499 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_value_tc (_id, " + '"' + paramSymtabEntry
/*     */         
/* 501 */         .name() + "\", " + 
/* 502 */         getValueModifier(valueEntry) + ", " + 
/* 503 */         getConcreteBaseTypeCode(valueEntry) + ", " + str1 + ");");
/*     */ 
/*     */ 
/*     */     
/* 507 */     return paramInt;
/*     */   }
/*     */   
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 511 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 512 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasRepId(SymtabEntry paramSymtabEntry) {
/* 520 */     SymtabEntry symtabEntry = Util.typeOf(paramSymtabEntry);
/* 521 */     return (!(symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) && !(symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry) && (!(symtabEntry instanceof TypedefEntry) || ((TypedefEntry)symtabEntry)
/*     */ 
/*     */       
/* 524 */       .arrayInfo().isEmpty()) && (!(symtabEntry instanceof TypedefEntry) || 
/* 525 */       !(paramSymtabEntry.type() instanceof com.sun.tools.corba.se.idl.SequenceEntry)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getValueModifier(ValueEntry paramValueEntry) {
/* 530 */     String str = "NONE";
/* 531 */     if (paramValueEntry.isCustom()) {
/* 532 */       str = "CUSTOM";
/* 533 */     } else if (paramValueEntry.isAbstract()) {
/* 534 */       str = "ABSTRACT";
/* 535 */     } else if (paramValueEntry.isSafe()) {
/* 536 */       str = "TRUNCATABLE";
/* 537 */     }  return "org.omg.CORBA.VM_" + str + ".value";
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getConcreteBaseTypeCode(ValueEntry paramValueEntry) {
/* 542 */     Vector vector = paramValueEntry.derivedFrom();
/* 543 */     if (!paramValueEntry.isAbstract()) {
/*     */       
/* 545 */       SymtabEntry symtabEntry = paramValueEntry.derivedFrom().elementAt(0);
/* 546 */       if (!"ValueBase".equals(symtabEntry.name()))
/* 547 */         return Util.helperName(symtabEntry, true) + ".type ()"; 
/*     */     } 
/* 549 */     return "null";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 557 */     if (((ValueEntry)paramSymtabEntry).isAbstract()) {
/*     */       
/* 559 */       paramPrintWriter.println("    throw new org.omg.CORBA.BAD_OPERATION (\"abstract value cannot be instantiated\");");
/*     */     }
/*     */     else {
/*     */       
/* 563 */       paramPrintWriter.println("    return (" + paramString + ") ((org.omg.CORBA_2_3.portable.InputStream) istream).read_value (get_instance());");
/*     */     } 
/* 565 */     paramPrintWriter.println("  }");
/* 566 */     paramPrintWriter.println();
/*     */ 
/*     */ 
/*     */     
/* 570 */     paramPrintWriter.println("  public java.io.Serializable read_value (org.omg.CORBA.portable.InputStream istream)");
/* 571 */     paramPrintWriter.println("  {");
/*     */ 
/*     */     
/* 574 */     if (((ValueEntry)paramSymtabEntry).isAbstract()) {
/*     */       
/* 576 */       paramPrintWriter.println("    throw new org.omg.CORBA.BAD_OPERATION (\"abstract value cannot be instantiated\");");
/*     */     
/*     */     }
/* 579 */     else if (((ValueEntry)paramSymtabEntry).isCustom()) {
/*     */       
/* 581 */       paramPrintWriter.println("    throw new org.omg.CORBA.BAD_OPERATION (\"custom values should use unmarshal()\");");
/*     */     }
/*     */     else {
/*     */       
/* 585 */       paramPrintWriter.println("    " + paramString + " value = new " + paramString + " ();");
/* 586 */       read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/* 587 */       paramPrintWriter.println("    return value;");
/*     */     } 
/* 589 */     paramPrintWriter.println("  }");
/* 590 */     paramPrintWriter.println();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 598 */     paramPrintWriter.println("  public static void read (org.omg.CORBA.portable.InputStream istream, " + paramString + " value)");
/* 599 */     paramPrintWriter.println("  {");
/* 600 */     read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 606 */     Vector<ValueEntry> vector = ((ValueEntry)paramSymtabEntry).derivedFrom();
/* 607 */     if (vector != null && vector.size() != 0) {
/*     */       
/* 609 */       ValueEntry valueEntry = vector.elementAt(0);
/* 610 */       if (valueEntry == null) {
/* 611 */         return paramInt;
/*     */       }
/* 613 */       if (!Util.javaQualifiedName((SymtabEntry)valueEntry).equals("java.io.Serializable")) {
/* 614 */         paramPrintWriter.println(paramString1 + Util.helperName((SymtabEntry)valueEntry, true) + ".read (istream, value);");
/*     */       }
/*     */     } 
/* 617 */     Vector vector1 = ((ValueEntry)paramSymtabEntry).state();
/* 618 */     byte b1 = (vector1 == null) ? 0 : vector1.size();
/*     */     
/* 620 */     for (byte b2 = 0; b2 < b1; b2++) {
/*     */       
/* 622 */       TypedefEntry typedefEntry = ((InterfaceState)vector1.elementAt(b2)).entry;
/* 623 */       String str = typedefEntry.name();
/* 624 */       SymtabEntry symtabEntry = typedefEntry.type();
/*     */       
/* 626 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof TypedefEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry || 
/*     */ 
/*     */ 
/*     */         
/* 630 */         !typedefEntry.arrayInfo().isEmpty()) {
/* 631 */         paramInt = ((JavaGenerator)typedefEntry.generator()).read(paramInt, paramString1, paramString2 + '.' + str, (SymtabEntry)typedefEntry, paramPrintWriter);
/* 632 */       } else if (symtabEntry instanceof ValueEntry) {
/*     */         
/* 634 */         String str1 = Util.javaQualifiedName(symtabEntry);
/* 635 */         if (symtabEntry instanceof com.sun.tools.corba.se.idl.ValueBoxEntry)
/*     */         {
/*     */           
/* 638 */           str1 = Util.javaName(symtabEntry); } 
/* 639 */         paramPrintWriter.println("    " + paramString2 + '.' + str + " = (" + str1 + ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + 
/* 640 */             Util.helperName(symtabEntry, true) + ".get_instance ());");
/*     */       }
/*     */       else {
/*     */         
/* 644 */         paramPrintWriter.println(paramString1 + paramString2 + '.' + str + " = " + 
/* 645 */             Util.helperName(symtabEntry, true) + ".read (istream);");
/*     */       } 
/*     */     } 
/* 648 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 655 */     paramPrintWriter.println("    ((org.omg.CORBA_2_3.portable.OutputStream) ostream).write_value (value, get_instance());");
/* 656 */     paramPrintWriter.println("  }");
/* 657 */     paramPrintWriter.println();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 662 */     if (!((ValueEntry)paramSymtabEntry).isCustom()) {
/*     */       
/* 664 */       paramPrintWriter.println("  public static void _write (org.omg.CORBA.portable.OutputStream ostream, " + Util.javaName(paramSymtabEntry) + " value)");
/* 665 */       paramPrintWriter.println("  {");
/* 666 */       write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/* 667 */       paramPrintWriter.println("  }");
/* 668 */       paramPrintWriter.println();
/*     */     } 
/*     */ 
/*     */     
/* 672 */     paramPrintWriter.println("  public void write_value (org.omg.CORBA.portable.OutputStream ostream, java.io.Serializable obj)");
/* 673 */     paramPrintWriter.println("  {");
/*     */ 
/*     */     
/* 676 */     if (((ValueEntry)paramSymtabEntry).isCustom()) {
/*     */       
/* 678 */       paramPrintWriter.println("    throw new org.omg.CORBA.BAD_OPERATION (\"custom values should use marshal()\");");
/*     */     } else {
/*     */       
/* 681 */       String str = Util.javaName(paramSymtabEntry);
/* 682 */       paramPrintWriter.println("    _write (ostream, (" + str + ") obj);");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 690 */     Vector<ValueEntry> vector = ((ValueEntry)paramSymtabEntry).derivedFrom();
/* 691 */     if (vector != null && vector.size() != 0) {
/*     */       
/* 693 */       ValueEntry valueEntry = vector.elementAt(0);
/* 694 */       if (valueEntry == null) {
/* 695 */         return paramInt;
/*     */       }
/*     */       
/* 698 */       if (!Util.javaQualifiedName((SymtabEntry)valueEntry).equals("java.io.Serializable")) {
/* 699 */         paramPrintWriter.println(paramString1 + Util.helperName((SymtabEntry)valueEntry, true) + "._write (ostream, value);");
/*     */       }
/*     */     } 
/* 702 */     Vector vector1 = ((ValueEntry)paramSymtabEntry).state();
/* 703 */     byte b1 = (vector1 == null) ? 0 : vector1.size();
/* 704 */     for (byte b2 = 0; b2 < b1; b2++) {
/*     */       
/* 706 */       TypedefEntry typedefEntry = ((InterfaceState)vector1.elementAt(b2)).entry;
/* 707 */       String str = typedefEntry.name();
/* 708 */       SymtabEntry symtabEntry = typedefEntry.type();
/*     */       
/* 710 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof TypedefEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry || 
/*     */ 
/*     */ 
/*     */         
/* 714 */         !typedefEntry.arrayInfo().isEmpty()) {
/* 715 */         paramInt = ((JavaGenerator)typedefEntry.generator()).write(paramInt, paramString1, paramString2 + '.' + str, (SymtabEntry)typedefEntry, paramPrintWriter);
/*     */       } else {
/* 717 */         paramPrintWriter.println(paramString1 + Util.helperName(symtabEntry, true) + ".write (ostream, " + paramString2 + '.' + str + ");");
/*     */       } 
/*     */     } 
/*     */     
/* 721 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeAbstract() {
/* 729 */     this.stream.print("public interface " + this.v.name());
/*     */ 
/*     */ 
/*     */     
/* 733 */     if (this.v.derivedFrom().size() == 0) {
/* 734 */       this.stream.print(" extends org.omg.CORBA.portable.ValueBase");
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 739 */       for (byte b = 0; b < this.v.derivedFrom().size(); b++) {
/*     */         
/* 741 */         if (b == 0) {
/* 742 */           this.stream.print(" extends ");
/*     */         } else {
/* 744 */           this.stream.print(", ");
/* 745 */         }  SymtabEntry symtabEntry = this.v.derivedFrom().elementAt(b);
/* 746 */         this.stream.print(Util.javaName(symtabEntry));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 751 */     if (this.v.supports().size() > 0) {
/*     */       
/* 753 */       this.stream.print(", ");
/* 754 */       SymtabEntry symtabEntry = this.v.supports().elementAt(0);
/* 755 */       this.stream.print(Util.javaName(symtabEntry));
/*     */     } 
/* 757 */     this.stream.println();
/* 758 */     this.stream.println("{");
/*     */   }
/*     */   
/* 761 */   protected int emit = 0;
/* 762 */   protected Factories factories = null;
/* 763 */   protected Hashtable symbolTable = null;
/* 764 */   protected ValueEntry v = null;
/* 765 */   protected PrintWriter stream = null;
/*     */   protected boolean explicitDefaultInit = false;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\ValueGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */