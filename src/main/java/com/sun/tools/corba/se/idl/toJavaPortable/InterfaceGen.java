/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceGen;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InterfaceGen
/*     */   implements InterfaceGen, JavaGenerator
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, InterfaceEntry paramInterfaceEntry, PrintWriter paramPrintWriter) {
/*  90 */     if (!isPseudo(paramInterfaceEntry)) {
/*     */       
/*  92 */       this.symbolTable = paramHashtable;
/*  93 */       this.i = paramInterfaceEntry;
/*  94 */       init();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 103 */       if (!paramInterfaceEntry.isLocalSignature()) {
/*     */         
/* 105 */         if (!paramInterfaceEntry.isLocal()) {
/*     */ 
/*     */           
/* 108 */           generateSkeleton();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 116 */           Arguments arguments = (Arguments)Compile.compiler.arguments;
/* 117 */           if (arguments.TIEServer == true && arguments.emit == 3) {
/*     */ 
/*     */             
/* 120 */             arguments.TIEServer = false;
/*     */             
/* 122 */             generateSkeleton();
/*     */             
/* 124 */             arguments.TIEServer = true;
/*     */           } 
/* 126 */           generateStub();
/*     */         } 
/* 128 */         generateHolder();
/* 129 */         generateHelper();
/*     */       } 
/* 131 */       this.intfType = 1;
/* 132 */       generateInterface();
/* 133 */       this.intfType = 2;
/* 134 */       generateInterface();
/* 135 */       this.intfType = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 144 */     this.emit = ((Arguments)Compile.compiler.arguments).emit;
/* 145 */     this.factories = (Factories)Compile.compiler.factories();
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
/*     */   protected void generateSkeleton() {
/* 158 */     if (this.emit != 1) {
/* 159 */       this.factories.skeleton().generate(this.symbolTable, (SymtabEntry)this.i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateStub() {
/* 169 */     if (this.emit != 2) {
/* 170 */       this.factories.stub().generate(this.symbolTable, (SymtabEntry)this.i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHelper() {
/* 178 */     if (this.emit != 2) {
/* 179 */       this.factories.helper().generate(this.symbolTable, (SymtabEntry)this.i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHolder() {
/* 187 */     if (this.emit != 2) {
/* 188 */       this.factories.holder().generate(this.symbolTable, (SymtabEntry)this.i);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateInterface() {
/* 207 */     init();
/* 208 */     openStream();
/* 209 */     if (this.stream == null)
/*     */       return; 
/* 211 */     writeHeading();
/* 212 */     if (this.intfType == 2)
/* 213 */       writeOperationsBody(); 
/* 214 */     if (this.intfType == 1)
/* 215 */       writeSignatureBody(); 
/* 216 */     writeClosing();
/* 217 */     closeStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void openStream() {
/* 225 */     if (this.i.isAbstract() || this.intfType == 1) {
/* 226 */       this.stream = (PrintWriter)Util.stream((SymtabEntry)this.i, ".java");
/* 227 */     } else if (this.intfType == 2) {
/* 228 */       this.stream = (PrintWriter)Util.stream((SymtabEntry)this.i, "Operations.java");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeading() {
/* 236 */     Util.writePackage(this.stream, (SymtabEntry)this.i, (short)0);
/* 237 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */ 
/*     */     
/* 240 */     if (this.i.comment() != null) {
/* 241 */       this.i.comment().generate("", this.stream);
/*     */     }
/* 243 */     String str = this.i.name();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 271 */     if (this.intfType == 1) {
/* 272 */       writeSignatureHeading();
/* 273 */     } else if (this.intfType == 2) {
/* 274 */       writeOperationsHeading();
/*     */     } 
/*     */     
/* 277 */     this.stream.println();
/* 278 */     this.stream.println('{');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeSignatureHeading() {
/* 286 */     String str = this.i.name();
/* 287 */     this.stream.print("public interface " + str + " extends " + str + "Operations, ");
/* 288 */     boolean bool1 = true;
/* 289 */     boolean bool2 = false;
/* 290 */     for (byte b = 0; b < this.i.derivedFrom().size(); b++) {
/*     */       
/* 292 */       if (bool1) {
/* 293 */         bool1 = false;
/*     */       } else {
/* 295 */         this.stream.print(", ");
/* 296 */       }  InterfaceEntry interfaceEntry = this.i.derivedFrom().elementAt(b);
/* 297 */       this.stream.print(Util.javaName((SymtabEntry)interfaceEntry));
/* 298 */       if (!interfaceEntry.isAbstract()) {
/* 299 */         bool2 = true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 304 */     if (!bool2) {
/* 305 */       this.stream.print(", org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity ");
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 310 */     else if (this.i.derivedFrom().size() == 1) {
/* 311 */       this.stream.print(", org.omg.CORBA.portable.IDLEntity ");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeOperationsHeading() {
/* 320 */     this.stream.print("public interface " + this.i.name());
/* 321 */     if (!this.i.isAbstract()) {
/* 322 */       this.stream.print("Operations ");
/*     */ 
/*     */     
/*     */     }
/* 326 */     else if (this.i.derivedFrom().size() == 0) {
/* 327 */       this.stream.print(" extends org.omg.CORBA.portable.IDLEntity");
/*     */     } 
/*     */     
/* 330 */     boolean bool = true;
/* 331 */     for (byte b = 0; b < this.i.derivedFrom().size(); b++) {
/*     */       
/* 333 */       InterfaceEntry interfaceEntry = this.i.derivedFrom().elementAt(b);
/* 334 */       String str = Util.javaName((SymtabEntry)interfaceEntry);
/*     */ 
/*     */       
/* 337 */       if (!str.equals("org.omg.CORBA.Object")) {
/*     */ 
/*     */         
/* 340 */         if (bool) {
/*     */           
/* 342 */           bool = false;
/* 343 */           this.stream.print(" extends ");
/*     */         } else {
/*     */           
/* 346 */           this.stream.print(", ");
/*     */         } 
/*     */ 
/*     */         
/* 350 */         if (interfaceEntry.isAbstract() || this.i.isAbstract()) {
/* 351 */           this.stream.print(str);
/*     */         } else {
/* 353 */           this.stream.print(str + "Operations");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeOperationsBody() {
/* 364 */     Enumeration<SymtabEntry> enumeration = this.i.contained().elements();
/* 365 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 367 */       SymtabEntry symtabEntry = enumeration.nextElement();
/* 368 */       if (symtabEntry instanceof MethodEntry) {
/*     */         
/* 370 */         MethodEntry methodEntry = (MethodEntry)symtabEntry;
/* 371 */         ((MethodGen)methodEntry.generator()).interfaceMethod(this.symbolTable, methodEntry, this.stream);
/*     */         continue;
/*     */       } 
/* 374 */       if (!(symtabEntry instanceof com.sun.tools.corba.se.idl.ConstEntry)) {
/* 375 */         symtabEntry.generate(this.symbolTable, this.stream);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeSignatureBody() {
/* 385 */     Enumeration<SymtabEntry> enumeration = this.i.contained().elements();
/* 386 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 388 */       SymtabEntry symtabEntry = enumeration.nextElement();
/* 389 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.ConstEntry) {
/* 390 */         symtabEntry.generate(this.symbolTable, this.stream);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeClosing() {
/* 399 */     String str = this.i.name();
/* 400 */     if (!this.i.isAbstract() && this.intfType == 2)
/* 401 */       str = str + "Operations"; 
/* 402 */     this.stream.println("} // interface " + str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeStream() {
/* 410 */     this.stream.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 441 */     InterfaceEntry interfaceEntry = (InterfaceEntry)paramSymtabEntry;
/* 442 */     paramTCOffsets.set(paramSymtabEntry);
/* 443 */     if (paramSymtabEntry.fullName().equals("org/omg/CORBA/Object")) {
/* 444 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_objref);");
/*     */     } else {
/*     */       
/* 447 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_interface_tc (" + 
/*     */ 
/*     */           
/* 450 */           Util.helperName((SymtabEntry)interfaceEntry, true) + ".id (), " + '"' + 
/* 451 */           Util.stripLeadingUnderscores(paramSymtabEntry.name()) + "\");");
/* 452 */     }  return paramInt;
/*     */   }
/*     */   
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 456 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 457 */     return paramInt;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 479 */     InterfaceEntry interfaceEntry = (InterfaceEntry)paramSymtabEntry;
/* 480 */     if (interfaceEntry.isAbstract()) {
/* 481 */       paramPrintWriter.println("    return narrow (((org.omg.CORBA_2_3.portable.InputStream)istream).read_abstract_interface (_" + interfaceEntry.name() + "Stub.class));");
/*     */     } else {
/* 483 */       paramPrintWriter.println("    return narrow (istream.read_Object (_" + interfaceEntry.name() + "Stub.class));");
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
/*     */ 
/*     */   
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 499 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 521 */     InterfaceEntry interfaceEntry = (InterfaceEntry)paramSymtabEntry;
/* 522 */     if (paramSymtabEntry.fullName().equals("org/omg/CORBA/Object")) {
/* 523 */       paramPrintWriter.println(paramString1 + paramString2 + " = istream.read_Object (_" + interfaceEntry.name() + "Stub.class);");
/*     */     } else {
/* 525 */       paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, false) + ".narrow (istream.read_Object (_" + interfaceEntry.name() + "Stub.class));");
/* 526 */     }  return paramInt;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 548 */     InterfaceEntry interfaceEntry = (InterfaceEntry)paramSymtabEntry;
/* 549 */     if (interfaceEntry.isAbstract()) {
/* 550 */       paramPrintWriter.println(paramString1 + "((org.omg.CORBA_2_3.portable.OutputStream)ostream).write_abstract_interface ((java.lang.Object) " + paramString2 + ");");
/*     */     } else {
/* 552 */       paramPrintWriter.println(paramString1 + "ostream.write_Object ((org.omg.CORBA.Object) " + paramString2 + ");");
/* 553 */     }  return paramInt;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isPseudo(InterfaceEntry paramInterfaceEntry) {
/* 816 */     String str = paramInterfaceEntry.fullName();
/* 817 */     if (str.equalsIgnoreCase("CORBA/TypeCode"))
/* 818 */       return true; 
/* 819 */     if (str.equalsIgnoreCase("CORBA/Principal"))
/* 820 */       return true; 
/* 821 */     if (str.equalsIgnoreCase("CORBA/ORB"))
/* 822 */       return true; 
/* 823 */     if (str.equalsIgnoreCase("CORBA/Any"))
/* 824 */       return true; 
/* 825 */     if (str.equalsIgnoreCase("CORBA/Context"))
/* 826 */       return true; 
/* 827 */     if (str.equalsIgnoreCase("CORBA/ContextList"))
/* 828 */       return true; 
/* 829 */     if (str.equalsIgnoreCase("CORBA/DynamicImplementation"))
/* 830 */       return true; 
/* 831 */     if (str.equalsIgnoreCase("CORBA/Environment"))
/* 832 */       return true; 
/* 833 */     if (str.equalsIgnoreCase("CORBA/ExceptionList"))
/* 834 */       return true; 
/* 835 */     if (str.equalsIgnoreCase("CORBA/NVList"))
/* 836 */       return true; 
/* 837 */     if (str.equalsIgnoreCase("CORBA/NamedValue"))
/* 838 */       return true; 
/* 839 */     if (str.equalsIgnoreCase("CORBA/Request"))
/* 840 */       return true; 
/* 841 */     if (str.equalsIgnoreCase("CORBA/ServerRequest"))
/* 842 */       return true; 
/* 843 */     if (str.equalsIgnoreCase("CORBA/UserException"))
/* 844 */       return true; 
/* 845 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 851 */   protected int emit = 0;
/* 852 */   protected Factories factories = null;
/*     */   
/* 854 */   protected Hashtable symbolTable = null;
/* 855 */   protected InterfaceEntry i = null;
/* 856 */   protected PrintWriter stream = null;
/*     */   
/*     */   protected static final int SIGNATURE = 1;
/*     */   
/*     */   protected static final int OPERATIONS = 2;
/* 861 */   protected int intfType = 0;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\InterfaceGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */