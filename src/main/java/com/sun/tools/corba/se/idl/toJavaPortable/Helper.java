/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Helper
/*     */   implements AuxGen
/*     */ {
/*     */   protected Hashtable symbolTable;
/*     */   protected SymtabEntry entry;
/*     */   protected GenFileStream stream;
/*     */   protected String helperClass;
/*     */   protected String helperType;
/*     */   
/*     */   public void generate(Hashtable paramHashtable, SymtabEntry paramSymtabEntry) {
/*  91 */     this.symbolTable = paramHashtable;
/*  92 */     this.entry = paramSymtabEntry;
/*  93 */     init();
/*     */     
/*  95 */     openStream();
/*  96 */     if (this.stream == null)
/*     */       return; 
/*  98 */     writeHeading();
/*  99 */     writeBody();
/* 100 */     writeClosing();
/* 101 */     closeStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 109 */     this.helperClass = this.entry.name() + "Helper";
/* 110 */     if (this.entry instanceof ValueBoxEntry) {
/*     */       
/* 112 */       ValueBoxEntry valueBoxEntry = (ValueBoxEntry)this.entry;
/* 113 */       TypedefEntry typedefEntry = ((InterfaceState)valueBoxEntry.state().elementAt(0)).entry;
/* 114 */       SymtabEntry symtabEntry = typedefEntry.type();
/*     */       
/* 116 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/* 117 */         this.helperType = Util.javaName(this.entry);
/*     */       } else {
/* 119 */         this.helperType = Util.javaName(symtabEntry);
/*     */       } 
/*     */     } else {
/* 122 */       this.helperType = Util.javaName(this.entry);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void openStream() {
/* 130 */     this.stream = Util.stream(this.entry, "Helper.java");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeading() {
/* 139 */     Util.writePackage((PrintWriter)this.stream, this.entry, (short)2);
/* 140 */     Util.writeProlog((PrintWriter)this.stream, this.stream.name());
/*     */ 
/*     */     
/* 143 */     if (this.entry.comment() != null) {
/* 144 */       this.entry.comment().generate("", (PrintWriter)this.stream);
/*     */     }
/* 146 */     this.stream.print("public final class " + this.helperClass);
/* 147 */     if (this.entry instanceof ValueEntry) {
/* 148 */       this.stream.println(" implements org.omg.CORBA.portable.ValueHelper");
/*     */     } else {
/* 150 */       this.stream.println();
/* 151 */     }  this.stream.println('{');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody() {
/* 159 */     writeInstVars();
/* 160 */     writeCtors();
/* 161 */     writeInsert();
/* 162 */     writeExtract();
/* 163 */     writeType();
/* 164 */     writeID();
/* 165 */     writeRead();
/* 166 */     writeWrite();
/* 167 */     if (this.entry instanceof InterfaceEntry && !(this.entry instanceof ValueEntry)) {
/* 168 */       writeNarrow();
/* 169 */       writeUncheckedNarrow();
/*     */     } 
/* 171 */     writeHelperInterface();
/* 172 */     if (this.entry instanceof ValueEntry) {
/* 173 */       writeValueHelperInterface();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHelperInterface() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeValueHelperInterface() {
/* 188 */     writeGetID();
/* 189 */     writeGetType();
/* 190 */     writeGetInstance();
/* 191 */     writeGetClass();
/* 192 */     writeGetSafeBaseIds();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeClosing() {
/* 200 */     this.stream.println('}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeStream() {
/* 208 */     this.stream.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInstVars() {
/* 216 */     this.stream.println("  private static String  _id = \"" + Util.stripLeadingUnderscoresFromID(this.entry.repositoryID().ID()) + "\";");
/* 217 */     if (this.entry instanceof ValueEntry) {
/*     */       
/* 219 */       this.stream.println();
/* 220 */       this.stream.println("  private static " + this.helperClass + " helper = new " + this.helperClass + " ();");
/* 221 */       this.stream.println();
/* 222 */       this.stream.println("  private static String[] _truncatable_ids = {");
/* 223 */       this.stream.print("    _id");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 228 */       ValueEntry valueEntry = (ValueEntry)this.entry;
/* 229 */       while (valueEntry.isSafe()) {
/*     */         
/* 231 */         this.stream.println(",");
/* 232 */         ValueEntry valueEntry1 = valueEntry.derivedFrom().elementAt(0);
/* 233 */         this.stream.print("    \"" + Util.stripLeadingUnderscoresFromID(valueEntry1.repositoryID().ID()) + "\"");
/* 234 */         valueEntry = valueEntry1;
/*     */       } 
/* 236 */       this.stream.println("   };");
/*     */     } 
/* 238 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeCtors() {
/* 246 */     this.stream.println("  public " + this.helperClass + "()");
/* 247 */     this.stream.println("  {");
/* 248 */     this.stream.println("  }");
/* 249 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInsert() {
/* 257 */     this.stream.println("  public static void insert (org.omg.CORBA.Any a, " + this.helperType + " that)");
/* 258 */     this.stream.println("  {");
/* 259 */     this.stream.println("    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();");
/* 260 */     this.stream.println("    a.type (type ());");
/* 261 */     this.stream.println("    write (out, that);");
/* 262 */     this.stream.println("    a.read_value (out.create_input_stream (), type ());");
/* 263 */     this.stream.println("  }");
/* 264 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeExtract() {
/* 272 */     this.stream.println("  public static " + this.helperType + " extract (org.omg.CORBA.Any a)");
/* 273 */     this.stream.println("  {");
/* 274 */     this.stream.println("    return read (a.create_input_stream ());");
/* 275 */     this.stream.println("  }");
/* 276 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeType() {
/* 284 */     boolean bool = (this.entry instanceof ValueEntry || this.entry instanceof ValueBoxEntry || this.entry instanceof com.sun.tools.corba.se.idl.StructEntry) ? true : false;
/*     */ 
/*     */     
/* 287 */     this.stream.println("  private static org.omg.CORBA.TypeCode __typeCode = null;");
/* 288 */     if (bool)
/* 289 */       this.stream.println("  private static boolean __active = false;"); 
/* 290 */     this.stream.println("  synchronized public static org.omg.CORBA.TypeCode type ()");
/* 291 */     this.stream.println("  {");
/* 292 */     this.stream.println("    if (__typeCode == null)");
/* 293 */     this.stream.println("    {");
/* 294 */     if (bool) {
/* 295 */       this.stream.println("      synchronized (org.omg.CORBA.TypeCode.class)");
/* 296 */       this.stream.println("      {");
/* 297 */       this.stream.println("        if (__typeCode == null)");
/* 298 */       this.stream.println("        {");
/* 299 */       this.stream.println("          if (__active)");
/* 300 */       this.stream.println("          {");
/* 301 */       this.stream.println("            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );");
/* 302 */       this.stream.println("          }");
/* 303 */       this.stream.println("          __active = true;");
/* 304 */       ((JavaGenerator)this.entry.generator()).helperType(0, "          ", new TCOffsets(), "__typeCode", this.entry, (PrintWriter)this.stream);
/*     */     } else {
/*     */       
/* 307 */       ((JavaGenerator)this.entry.generator()).helperType(0, "      ", new TCOffsets(), "__typeCode", this.entry, (PrintWriter)this.stream);
/*     */     } 
/*     */ 
/*     */     
/* 311 */     if (bool) {
/* 312 */       this.stream.println("          __active = false;");
/* 313 */       this.stream.println("        }");
/* 314 */       this.stream.println("      }");
/*     */     } 
/* 316 */     this.stream.println("    }");
/* 317 */     this.stream.println("    return __typeCode;");
/* 318 */     this.stream.println("  }");
/* 319 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeID() {
/* 327 */     this.stream.println("  public static String id ()");
/* 328 */     this.stream.println("  {");
/* 329 */     this.stream.println("    return _id;");
/* 330 */     this.stream.println("  }");
/* 331 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRead() {
/* 340 */     int i = 0;
/*     */     
/* 342 */     if (this.entry instanceof InterfaceEntry) {
/* 343 */       InterfaceEntry interfaceEntry = (InterfaceEntry)this.entry;
/*     */ 
/*     */ 
/*     */       
/* 347 */       i = interfaceEntry.isLocal() | interfaceEntry.isLocalServant();
/*     */     } 
/*     */     
/* 350 */     this.stream.println("  public static " + this.helperType + " read (org.omg.CORBA.portable.InputStream istream)");
/* 351 */     this.stream.println("  {");
/* 352 */     if (i == 0) {
/* 353 */       ((JavaGenerator)this.entry.generator()).helperRead(this.helperType, this.entry, (PrintWriter)this.stream);
/*     */     } else {
/* 355 */       this.stream.println("      throw new org.omg.CORBA.MARSHAL ();");
/*     */     } 
/* 357 */     this.stream.println("  }");
/* 358 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeWrite() {
/* 367 */     int i = 0;
/*     */     
/* 369 */     if (this.entry instanceof InterfaceEntry) {
/* 370 */       InterfaceEntry interfaceEntry = (InterfaceEntry)this.entry;
/*     */ 
/*     */ 
/*     */       
/* 374 */       i = interfaceEntry.isLocal() | interfaceEntry.isLocalServant();
/*     */     } 
/*     */     
/* 377 */     this.stream.println("  public static void write (org.omg.CORBA.portable.OutputStream ostream, " + this.helperType + " value)");
/* 378 */     this.stream.println("  {");
/* 379 */     if (i == 0) {
/* 380 */       ((JavaGenerator)this.entry.generator()).helperWrite(this.entry, (PrintWriter)this.stream);
/*     */     } else {
/* 382 */       this.stream.println("      throw new org.omg.CORBA.MARSHAL ();");
/*     */     } 
/* 384 */     this.stream.println("  }");
/* 385 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeNarrow() {
/* 394 */     writeRemoteNarrow();
/* 395 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRemoteNarrow() {
/* 403 */     InterfaceEntry interfaceEntry = (InterfaceEntry)this.entry;
/*     */ 
/*     */     
/* 406 */     if (interfaceEntry.isLocal()) {
/* 407 */       writeRemoteNarrowForLocal(false);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 412 */     if (interfaceEntry.isAbstract()) {
/* 413 */       writeRemoteNarrowForAbstract(false);
/*     */       
/*     */       return;
/*     */     } 
/* 417 */     for (byte b = 0; b < interfaceEntry.derivedFrom().size(); b++) {
/* 418 */       SymtabEntry symtabEntry = interfaceEntry.derivedFrom().elementAt(b);
/* 419 */       if (((InterfaceEntry)symtabEntry).isAbstract()) {
/* 420 */         writeRemoteNarrowForAbstract(true);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 426 */     this.stream.println("  public static " + this.helperType + " narrow (org.omg.CORBA.Object obj)");
/* 427 */     this.stream.println("  {");
/* 428 */     this.stream.println("    if (obj == null)");
/* 429 */     this.stream.println("      return null;");
/* 430 */     this.stream.println("    else if (obj instanceof " + this.helperType + ')');
/* 431 */     this.stream.println("      return (" + this.helperType + ")obj;");
/* 432 */     this.stream.println("    else if (!obj._is_a (id ()))");
/* 433 */     this.stream.println("      throw new org.omg.CORBA.BAD_PARAM ();");
/* 434 */     this.stream.println("    else");
/* 435 */     this.stream.println("    {");
/* 436 */     this.stream.println("      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();");
/* 437 */     String str = stubName((InterfaceEntry)this.entry);
/* 438 */     this.stream.println("      " + str + " stub = new " + str + " ();");
/* 439 */     this.stream.println("      stub._set_delegate(delegate);");
/* 440 */     this.stream.println("      return stub;");
/* 441 */     this.stream.println("    }");
/* 442 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeRemoteNarrowForLocal(boolean paramBoolean) {
/* 450 */     this.stream.println("  public static " + this.helperType + " narrow (org.omg.CORBA.Object obj)");
/* 451 */     this.stream.println("  {");
/* 452 */     this.stream.println("    if (obj == null)");
/* 453 */     this.stream.println("      return null;");
/* 454 */     this.stream.println("    else if (obj instanceof " + this.helperType + ')');
/* 455 */     this.stream.println("      return (" + this.helperType + ")obj;");
/* 456 */     this.stream.println("    else");
/* 457 */     this.stream.println("      throw new org.omg.CORBA.BAD_PARAM ();");
/* 458 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeRemoteNarrowForAbstract(boolean paramBoolean) {
/* 466 */     this.stream.print("  public static " + this.helperType + " narrow (java.lang.Object obj)");
/* 467 */     this.stream.println("  {");
/* 468 */     this.stream.println("    if (obj == null)");
/* 469 */     this.stream.println("      return null;");
/* 470 */     if (paramBoolean) {
/*     */       
/* 472 */       this.stream.println("    else if (obj instanceof org.omg.CORBA.Object)");
/* 473 */       this.stream.println("      return narrow ((org.omg.CORBA.Object) obj);");
/*     */     }
/*     */     else {
/*     */       
/* 477 */       this.stream.println("    else if (obj instanceof " + this.helperType + ')');
/* 478 */       this.stream.println("      return (" + this.helperType + ")obj;");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 485 */     if (!paramBoolean) {
/* 486 */       String str = stubName((InterfaceEntry)this.entry);
/*     */       
/* 488 */       this.stream.println("    else if ((obj instanceof org.omg.CORBA.portable.ObjectImpl) &&");
/* 489 */       this.stream.println("             (((org.omg.CORBA.Object)obj)._is_a (id ()))) {");
/* 490 */       this.stream.println("      org.omg.CORBA.portable.ObjectImpl impl = (org.omg.CORBA.portable.ObjectImpl)obj ;");
/* 491 */       this.stream.println("      org.omg.CORBA.portable.Delegate delegate = impl._get_delegate() ;");
/* 492 */       this.stream.println("      " + str + " stub = new " + str + " ();");
/* 493 */       this.stream.println("      stub._set_delegate(delegate);");
/* 494 */       this.stream.println("      return stub;");
/* 495 */       this.stream.println("    }");
/*     */     } 
/*     */ 
/*     */     
/* 499 */     this.stream.println("    throw new org.omg.CORBA.BAD_PARAM ();");
/* 500 */     this.stream.println("  }");
/* 501 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeUncheckedNarrow() {
/* 510 */     writeUncheckedRemoteNarrow();
/* 511 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeUncheckedRemoteNarrow() {
/* 519 */     InterfaceEntry interfaceEntry = (InterfaceEntry)this.entry;
/*     */ 
/*     */     
/* 522 */     if (interfaceEntry.isLocal()) {
/* 523 */       writeRemoteUncheckedNarrowForLocal(false);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 528 */     if (interfaceEntry.isAbstract()) {
/* 529 */       writeRemoteUncheckedNarrowForAbstract(false);
/*     */       
/*     */       return;
/*     */     } 
/* 533 */     for (byte b = 0; b < interfaceEntry.derivedFrom().size(); b++) {
/* 534 */       SymtabEntry symtabEntry = interfaceEntry.derivedFrom().elementAt(b);
/* 535 */       if (((InterfaceEntry)symtabEntry).isAbstract()) {
/* 536 */         writeRemoteUncheckedNarrowForAbstract(true);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 542 */     this.stream.println("  public static " + this.helperType + " unchecked_narrow (org.omg.CORBA.Object obj)");
/* 543 */     this.stream.println("  {");
/* 544 */     this.stream.println("    if (obj == null)");
/* 545 */     this.stream.println("      return null;");
/* 546 */     this.stream.println("    else if (obj instanceof " + this.helperType + ')');
/* 547 */     this.stream.println("      return (" + this.helperType + ")obj;");
/* 548 */     this.stream.println("    else");
/* 549 */     this.stream.println("    {");
/* 550 */     this.stream.println("      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();");
/* 551 */     String str = stubName((InterfaceEntry)this.entry);
/* 552 */     this.stream.println("      " + str + " stub = new " + str + " ();");
/* 553 */     this.stream.println("      stub._set_delegate(delegate);");
/* 554 */     this.stream.println("      return stub;");
/* 555 */     this.stream.println("    }");
/* 556 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeRemoteUncheckedNarrowForLocal(boolean paramBoolean) {
/* 564 */     this.stream.println("  public static " + this.helperType + " unchecked_narrow (org.omg.CORBA.Object obj)");
/* 565 */     this.stream.println("  {");
/* 566 */     this.stream.println("    if (obj == null)");
/* 567 */     this.stream.println("      return null;");
/* 568 */     this.stream.println("    else if (obj instanceof " + this.helperType + ')');
/* 569 */     this.stream.println("      return (" + this.helperType + ")obj;");
/* 570 */     this.stream.println("    else");
/* 571 */     this.stream.println("      throw new org.omg.CORBA.BAD_PARAM ();");
/* 572 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeRemoteUncheckedNarrowForAbstract(boolean paramBoolean) {
/* 580 */     this.stream.print("  public static " + this.helperType + " unchecked_narrow (java.lang.Object obj)");
/* 581 */     this.stream.println("  {");
/* 582 */     this.stream.println("    if (obj == null)");
/* 583 */     this.stream.println("      return null;");
/* 584 */     if (paramBoolean) {
/*     */       
/* 586 */       this.stream.println("    else if (obj instanceof org.omg.CORBA.Object)");
/* 587 */       this.stream.println("      return unchecked_narrow ((org.omg.CORBA.Object) obj);");
/*     */     }
/*     */     else {
/*     */       
/* 591 */       this.stream.println("    else if (obj instanceof " + this.helperType + ')');
/* 592 */       this.stream.println("      return (" + this.helperType + ")obj;");
/*     */     } 
/*     */     
/* 595 */     if (!paramBoolean) {
/* 596 */       String str = stubName((InterfaceEntry)this.entry);
/*     */       
/* 598 */       this.stream.println("    else if (obj instanceof org.omg.CORBA.portable.ObjectImpl) {");
/* 599 */       this.stream.println("      org.omg.CORBA.portable.ObjectImpl impl = (org.omg.CORBA.portable.ObjectImpl)obj ;");
/* 600 */       this.stream.println("      org.omg.CORBA.portable.Delegate delegate = impl._get_delegate() ;");
/* 601 */       this.stream.println("      " + str + " stub = new " + str + " ();");
/* 602 */       this.stream.println("      stub._set_delegate(delegate);");
/* 603 */       this.stream.println("      return stub;");
/* 604 */       this.stream.println("    }");
/*     */     } 
/*     */     
/* 607 */     this.stream.println("    throw new org.omg.CORBA.BAD_PARAM ();");
/* 608 */     this.stream.println("  }");
/* 609 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeGetID() {
/* 618 */     if (!Util.IDLEntity(this.entry))
/*     */       return; 
/* 620 */     this.stream.println("  public String get_id ()");
/* 621 */     this.stream.println("  {");
/* 622 */     this.stream.println("    return _id;");
/* 623 */     this.stream.println("  }");
/* 624 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeGetType() {
/* 632 */     if (!Util.IDLEntity(this.entry))
/*     */       return; 
/* 634 */     this.stream.println("  public org.omg.CORBA.TypeCode get_type ()");
/* 635 */     this.stream.println("  {");
/* 636 */     this.stream.println("    return type ();");
/* 637 */     this.stream.println("  }");
/* 638 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeGetClass() {
/* 646 */     this.stream.println("  public Class get_class ()");
/* 647 */     this.stream.println("  {");
/* 648 */     this.stream.println("    return " + this.helperType + ".class;");
/* 649 */     this.stream.println("  }");
/* 650 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeGetInstance() {
/* 658 */     this.stream.println("  public static org.omg.CORBA.portable.ValueHelper get_instance ()");
/* 659 */     this.stream.println("  {");
/* 660 */     this.stream.println("    return helper;");
/* 661 */     this.stream.println("  }");
/* 662 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeGetSafeBaseIds() {
/* 670 */     this.stream.println("  public String[] get_truncatable_base_ids ()");
/* 671 */     this.stream.println("  {");
/* 672 */     this.stream.println("    return _truncatable_ids;");
/* 673 */     this.stream.println("  }");
/* 674 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String stubName(InterfaceEntry paramInterfaceEntry) {
/*     */     String str;
/* 683 */     if (paramInterfaceEntry.container().name().equals("")) {
/* 684 */       str = '_' + paramInterfaceEntry.name() + "Stub";
/*     */     } else {
/*     */       
/* 687 */       str = Util.containerFullName(paramInterfaceEntry.container()) + "._" + paramInterfaceEntry.name() + "Stub";
/*     */     } 
/* 689 */     return str.replace('/', '.');
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\Helper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */