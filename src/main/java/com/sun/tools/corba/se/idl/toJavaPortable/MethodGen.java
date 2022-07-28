/*      */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*      */ 
/*      */ import com.sun.tools.corba.se.idl.ExceptionEntry;
/*      */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*      */ import com.sun.tools.corba.se.idl.InterfaceState;
/*      */ import com.sun.tools.corba.se.idl.MethodEntry;
/*      */ import com.sun.tools.corba.se.idl.MethodGen;
/*      */ import com.sun.tools.corba.se.idl.ParameterEntry;
/*      */ import com.sun.tools.corba.se.idl.StringEntry;
/*      */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*      */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*      */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*      */ import com.sun.tools.corba.se.idl.ValueEntry;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MethodGen
/*      */   implements MethodGen
/*      */ {
/*      */   private static final String ONE_INDENT = "    ";
/*      */   private static final String TWO_INDENT = "        ";
/*      */   private static final String THREE_INDENT = "            ";
/*      */   private static final String FOUR_INDENT = "                ";
/*      */   private static final String FIVE_INDENT = "                    ";
/*      */   private static final int ATTRIBUTE_METHOD_PREFIX_LENGTH = 5;
/*      */   
/*      */   public void generate(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter) {}
/*      */   
/*      */   protected void interfaceMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter) {
/*  108 */     this.symbolTable = paramHashtable;
/*  109 */     this.m = paramMethodEntry;
/*  110 */     this.stream = paramPrintWriter;
/*  111 */     if (paramMethodEntry.comment() != null)
/*  112 */       paramMethodEntry.comment().generate("", paramPrintWriter); 
/*  113 */     paramPrintWriter.print("  ");
/*  114 */     SymtabEntry symtabEntry = paramMethodEntry.container();
/*  115 */     boolean bool = false;
/*  116 */     boolean bool1 = false;
/*  117 */     if (symtabEntry instanceof ValueEntry) {
/*      */       
/*  119 */       bool = ((ValueEntry)symtabEntry).isAbstract();
/*  120 */       bool1 = true;
/*      */     } 
/*  122 */     if (bool1 && !bool)
/*  123 */       paramPrintWriter.print("public "); 
/*  124 */     writeMethodSignature();
/*  125 */     if (bool1 && !bool) {
/*      */       
/*  127 */       paramPrintWriter.println();
/*  128 */       paramPrintWriter.println("  {");
/*  129 */       paramPrintWriter.println("  }");
/*  130 */       paramPrintWriter.println();
/*      */     } else {
/*      */       
/*  133 */       paramPrintWriter.println(";");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void stub(String paramString, boolean paramBoolean, Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt) {
/*  142 */     this.localOptimization = ((Arguments)Compile.compiler.arguments).LocalOptimization;
/*      */     
/*  144 */     this.isAbstract = paramBoolean;
/*  145 */     this.symbolTable = paramHashtable;
/*  146 */     this.m = paramMethodEntry;
/*  147 */     this.stream = paramPrintWriter;
/*  148 */     this.methodIndex = paramInt;
/*  149 */     if (paramMethodEntry.comment() != null)
/*  150 */       paramMethodEntry.comment().generate("  ", paramPrintWriter); 
/*  151 */     paramPrintWriter.print("  public ");
/*  152 */     writeMethodSignature();
/*  153 */     paramPrintWriter.println();
/*  154 */     paramPrintWriter.println("  {");
/*  155 */     writeStubBody(paramString);
/*  156 */     paramPrintWriter.println("  } // " + paramMethodEntry.name());
/*  157 */     paramPrintWriter.println();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void localstub(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt, InterfaceEntry paramInterfaceEntry) {
/*  165 */     this.symbolTable = paramHashtable;
/*  166 */     this.m = paramMethodEntry;
/*  167 */     this.stream = paramPrintWriter;
/*  168 */     this.methodIndex = paramInt;
/*  169 */     if (paramMethodEntry.comment() != null)
/*  170 */       paramMethodEntry.comment().generate("  ", paramPrintWriter); 
/*  171 */     paramPrintWriter.print("  public ");
/*  172 */     writeMethodSignature();
/*  173 */     paramPrintWriter.println();
/*  174 */     paramPrintWriter.println("  {");
/*  175 */     writeLocalStubBody(paramInterfaceEntry);
/*  176 */     paramPrintWriter.println("  } // " + paramMethodEntry.name());
/*  177 */     paramPrintWriter.println();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void skeleton(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt) {
/*  184 */     this.symbolTable = paramHashtable;
/*  185 */     this.m = paramMethodEntry;
/*  186 */     this.stream = paramPrintWriter;
/*  187 */     this.methodIndex = paramInt;
/*  188 */     if (paramMethodEntry.comment() != null)
/*  189 */       paramMethodEntry.comment().generate("  ", paramPrintWriter); 
/*  190 */     paramPrintWriter.print("  public ");
/*  191 */     writeMethodSignature();
/*  192 */     paramPrintWriter.println();
/*  193 */     paramPrintWriter.println("  {");
/*  194 */     writeSkeletonBody();
/*  195 */     paramPrintWriter.println("  } // " + paramMethodEntry.name());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void dispatchSkeleton(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt) {
/*  203 */     this.symbolTable = paramHashtable;
/*  204 */     this.m = paramMethodEntry;
/*  205 */     this.stream = paramPrintWriter;
/*  206 */     this.methodIndex = paramInt;
/*  207 */     if (paramMethodEntry.comment() != null)
/*  208 */       paramMethodEntry.comment().generate("  ", paramPrintWriter); 
/*  209 */     writeDispatchCall();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isValueInitializer() {
/*  219 */     MethodEntry methodEntry = null;
/*  220 */     if (this.m.container() instanceof ValueEntry) {
/*      */       
/*  222 */       Enumeration<MethodEntry> enumeration = ((ValueEntry)this.m.container()).initializers().elements();
/*  223 */       while (methodEntry != this.m && enumeration.hasMoreElements())
/*  224 */         methodEntry = enumeration.nextElement(); 
/*      */     } 
/*  226 */     return (methodEntry == this.m && null != this.m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeMethodSignature() {
/*  234 */     boolean bool = isValueInitializer();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  246 */     if (this.m.type() == null) {
/*      */       
/*  248 */       if (!bool) {
/*  249 */         this.stream.print("void");
/*      */       
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  255 */       this.stream.print(Util.javaName(this.m.type()));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  262 */     if (bool) {
/*  263 */       this.stream.print(' ' + this.m.container().name() + " (");
/*      */     } else {
/*  265 */       this.stream.print(' ' + this.m.name() + " (");
/*      */     } 
/*      */     
/*  268 */     boolean bool1 = true;
/*  269 */     Enumeration<ParameterEntry> enumeration = this.m.parameters().elements();
/*  270 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  272 */       if (bool1) {
/*  273 */         bool1 = false;
/*      */       } else {
/*  275 */         this.stream.print(", ");
/*  276 */       }  ParameterEntry parameterEntry = enumeration.nextElement();
/*      */       
/*  278 */       writeParmType(parameterEntry.type(), parameterEntry.passType());
/*      */ 
/*      */       
/*  281 */       this.stream.print(' ' + parameterEntry.name());
/*      */     } 
/*      */ 
/*      */     
/*  285 */     if (this.m.contexts().size() > 0) {
/*      */       
/*  287 */       if (!bool1)
/*  288 */         this.stream.print(", "); 
/*  289 */       this.stream.print("org.omg.CORBA.Context $context");
/*      */     } 
/*      */ 
/*      */     
/*  293 */     if (this.m.exceptions().size() > 0) {
/*      */       
/*  295 */       this.stream.print(") throws ");
/*  296 */       enumeration = this.m.exceptions().elements();
/*  297 */       bool1 = true;
/*  298 */       while (enumeration.hasMoreElements()) {
/*      */         
/*  300 */         if (bool1) {
/*  301 */           bool1 = false;
/*      */         } else {
/*  303 */           this.stream.print(", ");
/*  304 */         }  this.stream.print(Util.javaName((SymtabEntry)enumeration.nextElement()));
/*      */       } 
/*      */     } else {
/*      */       
/*  308 */       this.stream.print(')');
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeParmType(SymtabEntry paramSymtabEntry, int paramInt) {
/*  316 */     if (paramInt != 0) {
/*      */       
/*  318 */       paramSymtabEntry = Util.typeOf(paramSymtabEntry);
/*  319 */       this.stream.print(Util.holderName(paramSymtabEntry));
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  324 */       this.stream.print(Util.javaName(paramSymtabEntry));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeDispatchCall() {
/*  332 */     String str1 = "       ";
/*  333 */     String str2 = this.m.fullName();
/*  334 */     if (this.m instanceof com.sun.tools.corba.se.idl.AttributeEntry) {
/*      */ 
/*      */       
/*  337 */       int i = str2.lastIndexOf('/') + 1;
/*  338 */       if (this.m.type() == null) {
/*  339 */         str2 = str2.substring(0, i) + "_set_" + this.m.name();
/*      */       } else {
/*  341 */         str2 = str2.substring(0, i) + "_get_" + this.m.name();
/*      */       } 
/*  343 */     }  this.stream.println(str1 + "case " + this.methodIndex + ":  // " + str2);
/*  344 */     this.stream.println(str1 + "{");
/*  345 */     str1 = str1 + "  ";
/*  346 */     if (this.m.exceptions().size() > 0) {
/*      */       
/*  348 */       this.stream.println(str1 + "try {");
/*  349 */       str1 = str1 + "  ";
/*      */     } 
/*      */ 
/*      */     
/*  353 */     SymtabEntry symtabEntry = Util.typeOf(this.m.type());
/*  354 */     Enumeration<ParameterEntry> enumeration = this.m.parameters().elements();
/*  355 */     enumeration = this.m.parameters().elements();
/*  356 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  358 */       ParameterEntry parameterEntry = enumeration.nextElement();
/*  359 */       String str3 = parameterEntry.name();
/*  360 */       String str4 = '_' + str3;
/*  361 */       SymtabEntry symtabEntry1 = parameterEntry.type();
/*  362 */       int i = parameterEntry.passType();
/*      */       
/*  364 */       if (i == 0) {
/*  365 */         Util.writeInitializer(str1, str3, "", symtabEntry1, writeInputStreamRead("in", symtabEntry1), this.stream);
/*      */         
/*      */         continue;
/*      */       } 
/*  369 */       String str5 = Util.holderName(symtabEntry1);
/*  370 */       this.stream.println(str1 + str5 + ' ' + str3 + " = new " + str5 + " ();");
/*  371 */       if (i == 1) {
/*      */         
/*  373 */         if (symtabEntry1 instanceof ValueBoxEntry) {
/*      */           
/*  375 */           ValueBoxEntry valueBoxEntry = (ValueBoxEntry)symtabEntry1;
/*  376 */           TypedefEntry typedefEntry = ((InterfaceState)valueBoxEntry.state().elementAt(0)).entry;
/*  377 */           SymtabEntry symtabEntry2 = typedefEntry.type();
/*  378 */           if (symtabEntry2 instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*  379 */             this.stream.println(str1 + str3 + ".value = (" + writeInputStreamRead("in", parameterEntry.type()) + ").value;"); continue;
/*      */           } 
/*  381 */           this.stream.println(str1 + str3 + ".value = " + writeInputStreamRead("in", parameterEntry.type()) + ";");
/*      */           continue;
/*      */         } 
/*  384 */         this.stream.println(str1 + str3 + ".value = " + writeInputStreamRead("in", parameterEntry.type()) + ";");
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  390 */     if (this.m.contexts().size() > 0)
/*      */     {
/*  392 */       this.stream.println(str1 + "org.omg.CORBA.Context $context = in.read_Context ();");
/*      */     }
/*      */ 
/*      */     
/*  396 */     if (symtabEntry != null) {
/*  397 */       Util.writeInitializer(str1, "$result", "", symtabEntry, this.stream);
/*      */     }
/*      */     
/*  400 */     writeMethodCall(str1);
/*      */     
/*  402 */     enumeration = this.m.parameters().elements();
/*  403 */     boolean bool = true;
/*  404 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  406 */       ParameterEntry parameterEntry = enumeration.nextElement();
/*  407 */       if (bool) {
/*  408 */         bool = false;
/*      */       } else {
/*  410 */         this.stream.print(", ");
/*  411 */       }  this.stream.print(parameterEntry.name());
/*      */     } 
/*      */ 
/*      */     
/*  415 */     if (this.m.contexts().size() > 0) {
/*      */       
/*  417 */       if (!bool)
/*  418 */         this.stream.print(", "); 
/*  419 */       this.stream.print("$context");
/*      */     } 
/*      */     
/*  422 */     this.stream.println(");");
/*      */ 
/*      */     
/*  425 */     writeCreateReply(str1);
/*      */ 
/*      */     
/*  428 */     if (symtabEntry != null)
/*      */     {
/*  430 */       writeOutputStreamWrite(str1, "out", "$result", symtabEntry, this.stream);
/*      */     }
/*      */ 
/*      */     
/*  434 */     enumeration = this.m.parameters().elements();
/*  435 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  437 */       ParameterEntry parameterEntry = enumeration.nextElement();
/*  438 */       int i = parameterEntry.passType();
/*  439 */       if (i != 0)
/*      */       {
/*  441 */         writeOutputStreamWrite(str1, "out", parameterEntry.name() + ".value", parameterEntry.type(), this.stream);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  446 */     if (this.m.exceptions().size() > 0) {
/*      */       
/*  448 */       Enumeration<ExceptionEntry> enumeration1 = this.m.exceptions().elements();
/*  449 */       while (enumeration1.hasMoreElements()) {
/*      */         
/*  451 */         str1 = "         ";
/*  452 */         ExceptionEntry exceptionEntry = enumeration1.nextElement();
/*  453 */         String str = Util.javaQualifiedName((SymtabEntry)exceptionEntry);
/*  454 */         this.stream.println(str1 + "} catch (" + str + " $ex) {");
/*  455 */         str1 = str1 + "  ";
/*  456 */         this.stream.println(str1 + "out = $rh.createExceptionReply ();");
/*  457 */         this.stream.println(str1 + Util.helperName((SymtabEntry)exceptionEntry, true) + ".write (out, $ex);");
/*      */       } 
/*      */       
/*  460 */       str1 = "         ";
/*  461 */       this.stream.println(str1 + "}");
/*      */     } 
/*      */     
/*  464 */     this.stream.println("         break;");
/*  465 */     this.stream.println("       }");
/*  466 */     this.stream.println();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeStubBody(String paramString) {
/*  475 */     String str = Util.stripLeadingUnderscores(this.m.name());
/*  476 */     if (this.m instanceof com.sun.tools.corba.se.idl.AttributeEntry)
/*      */     {
/*  478 */       if (this.m.type() == null) {
/*  479 */         str = "_set_" + str;
/*      */       } else {
/*  481 */         str = "_get_" + str;
/*      */       }  } 
/*  483 */     if (this.localOptimization && !this.isAbstract) {
/*  484 */       this.stream.println("    while(true) {");
/*  485 */       this.stream.println("        if(!this._is_local()) {");
/*      */     } 
/*  487 */     this.stream.println("            org.omg.CORBA.portable.InputStream $in = null;");
/*      */     
/*  489 */     this.stream.println("            try {");
/*  490 */     this.stream.println("                org.omg.CORBA.portable.OutputStream $out = _request (\"" + str + "\", " + (
/*  491 */         !this.m.oneway() ? 1 : 0) + ");");
/*      */ 
/*      */ 
/*      */     
/*  495 */     Enumeration<ParameterEntry> enumeration1 = this.m.parameters().elements();
/*  496 */     while (enumeration1.hasMoreElements()) {
/*      */       
/*  498 */       ParameterEntry parameterEntry = enumeration1.nextElement();
/*  499 */       SymtabEntry symtabEntry1 = Util.typeOf(parameterEntry.type());
/*  500 */       if (symtabEntry1 instanceof StringEntry && (
/*  501 */         parameterEntry.passType() == 0 || parameterEntry
/*  502 */         .passType() == 1)) {
/*      */         
/*  504 */         StringEntry stringEntry = (StringEntry)symtabEntry1;
/*  505 */         if (stringEntry.maxSize() != null) {
/*      */           
/*  507 */           this.stream.print("            if (" + parameterEntry.name());
/*  508 */           if (parameterEntry.passType() == 1)
/*  509 */             this.stream.print(".value"); 
/*  510 */           this.stream.print(" == null || " + parameterEntry.name());
/*  511 */           if (parameterEntry.passType() == 1)
/*  512 */             this.stream.print(".value"); 
/*  513 */           this.stream.println(".length () > (" + 
/*  514 */               Util.parseExpression(stringEntry.maxSize()) + "))");
/*  515 */           this.stream.println("            throw new org.omg.CORBA.BAD_PARAM (0, org.omg.CORBA.CompletionStatus.COMPLETED_NO);");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  524 */     enumeration1 = this.m.parameters().elements();
/*  525 */     while (enumeration1.hasMoreElements()) {
/*      */       
/*  527 */       ParameterEntry parameterEntry = enumeration1.nextElement();
/*  528 */       if (parameterEntry.passType() == 0) {
/*  529 */         writeOutputStreamWrite("                ", "$out", parameterEntry.name(), parameterEntry.type(), this.stream); continue;
/*      */       } 
/*  531 */       if (parameterEntry.passType() == 1) {
/*  532 */         writeOutputStreamWrite("                ", "$out", parameterEntry.name() + ".value", parameterEntry
/*  533 */             .type(), this.stream);
/*      */       }
/*      */     } 
/*      */     
/*  537 */     if (this.m.contexts().size() > 0) {
/*      */       
/*  539 */       this.stream.println("                org.omg.CORBA.ContextList $contextList =_orb ().create_context_list ();");
/*      */ 
/*      */       
/*  542 */       for (byte b = 0; b < this.m.contexts().size(); b++)
/*      */       {
/*  544 */         this.stream.println("                $contextList.add (\"" + this.m
/*  545 */             .contexts().elementAt(b) + "\");");
/*      */       }
/*  547 */       this.stream.println("                $out.write_Context ($context, $contextList);");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  552 */     this.stream.println("                $in = _invoke ($out);");
/*      */     
/*  554 */     SymtabEntry symtabEntry = this.m.type();
/*  555 */     if (symtabEntry != null) {
/*  556 */       Util.writeInitializer("                ", "$result", "", symtabEntry, 
/*  557 */           writeInputStreamRead("$in", symtabEntry), this.stream);
/*      */     }
/*      */     
/*  560 */     enumeration1 = this.m.parameters().elements();
/*  561 */     while (enumeration1.hasMoreElements()) {
/*      */       
/*  563 */       ParameterEntry parameterEntry = enumeration1.nextElement();
/*  564 */       if (parameterEntry.passType() != 0) {
/*      */         
/*  566 */         if (parameterEntry.type() instanceof ValueBoxEntry) {
/*      */           
/*  568 */           ValueBoxEntry valueBoxEntry = (ValueBoxEntry)parameterEntry.type();
/*      */           
/*  570 */           TypedefEntry typedefEntry = ((InterfaceState)valueBoxEntry.state().elementAt(0)).entry;
/*  571 */           SymtabEntry symtabEntry1 = typedefEntry.type();
/*  572 */           if (symtabEntry1 instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*  573 */             this.stream.println("                " + parameterEntry.name() + ".value = (" + 
/*  574 */                 writeInputStreamRead("$in", parameterEntry.type()) + ").value;");
/*      */             continue;
/*      */           } 
/*  577 */           this.stream.println("                " + parameterEntry.name() + ".value = " + 
/*  578 */               writeInputStreamRead("$in", parameterEntry.type()) + ";");
/*      */           continue;
/*      */         } 
/*  581 */         this.stream.println("                " + parameterEntry.name() + ".value = " + 
/*  582 */             writeInputStreamRead("$in", parameterEntry.type()) + ";");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  587 */     enumeration1 = this.m.parameters().elements();
/*  588 */     while (enumeration1.hasMoreElements()) {
/*      */       
/*  590 */       ParameterEntry parameterEntry = enumeration1.nextElement();
/*  591 */       SymtabEntry symtabEntry1 = Util.typeOf(parameterEntry.type());
/*  592 */       if (symtabEntry1 instanceof StringEntry && (
/*  593 */         parameterEntry.passType() == 2 || parameterEntry
/*  594 */         .passType() == 1)) {
/*      */         
/*  596 */         StringEntry stringEntry = (StringEntry)symtabEntry1;
/*  597 */         if (stringEntry.maxSize() != null) {
/*      */           
/*  599 */           this.stream.print("                if (" + parameterEntry.name() + ".value.length ()");
/*      */           
/*  601 */           this.stream.println("         > (" + 
/*  602 */               Util.parseExpression(stringEntry.maxSize()) + "))");
/*  603 */           this.stream.println("                    throw new org.omg.CORBA.MARSHAL(0,org.omg.CORBA.CompletionStatus.COMPLETED_NO);");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  608 */     if (symtabEntry instanceof StringEntry) {
/*      */       
/*  610 */       StringEntry stringEntry = (StringEntry)symtabEntry;
/*  611 */       if (stringEntry.maxSize() != null) {
/*      */         
/*  613 */         this.stream.println("                if ($result.length () > (" + 
/*  614 */             Util.parseExpression(stringEntry.maxSize()) + "))");
/*  615 */         this.stream.println("                    throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_NO);");
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  622 */     if (symtabEntry != null) {
/*  623 */       this.stream.println("                return $result;");
/*      */     } else {
/*  625 */       this.stream.println("                return;");
/*      */     } 
/*      */ 
/*      */     
/*  629 */     this.stream.println("            } catch (org.omg.CORBA.portable.ApplicationException $ex) {");
/*      */     
/*  631 */     this.stream.println("                $in = $ex.getInputStream ();");
/*  632 */     this.stream.println("                String _id = $ex.getId ();");
/*      */     
/*  634 */     if (this.m.exceptions().size() > 0) {
/*      */       
/*  636 */       Enumeration<ExceptionEntry> enumeration = this.m.exceptions().elements();
/*  637 */       boolean bool1 = true;
/*  638 */       while (enumeration.hasMoreElements()) {
/*      */         
/*  640 */         ExceptionEntry exceptionEntry = enumeration.nextElement();
/*  641 */         if (bool1) {
/*      */           
/*  643 */           this.stream.print("                if ");
/*  644 */           bool1 = false;
/*      */         } else {
/*      */           
/*  647 */           this.stream.print("                else if ");
/*      */         } 
/*  649 */         this.stream.println("(_id.equals (\"" + exceptionEntry.repositoryID().ID() + "\"))");
/*  650 */         this.stream.println("                    throw " + 
/*  651 */             Util.helperName((SymtabEntry)exceptionEntry, false) + ".read ($in);");
/*      */       } 
/*  653 */       this.stream.println("                else");
/*  654 */       this.stream.println("                    throw new org.omg.CORBA.MARSHAL (_id);");
/*      */     } else {
/*      */       
/*  657 */       this.stream.println("                throw new org.omg.CORBA.MARSHAL (_id);");
/*      */     } 
/*  659 */     this.stream.println("            } catch (org.omg.CORBA.portable.RemarshalException $rm) {");
/*      */     
/*  661 */     this.stream.print("                ");
/*  662 */     if (this.m.type() != null)
/*  663 */       this.stream.print("return "); 
/*  664 */     this.stream.print(this.m.name() + " (");
/*      */ 
/*      */     
/*  667 */     boolean bool = true;
/*  668 */     Enumeration<ParameterEntry> enumeration2 = this.m.parameters().elements();
/*  669 */     while (enumeration2.hasMoreElements()) {
/*      */       
/*  671 */       if (bool) {
/*  672 */         bool = false;
/*      */       } else {
/*  674 */         this.stream.print(", ");
/*  675 */       }  ParameterEntry parameterEntry = enumeration2.nextElement();
/*  676 */       this.stream.print(parameterEntry.name());
/*      */     } 
/*      */     
/*  679 */     if (this.m.contexts().size() > 0) {
/*      */       
/*  681 */       if (!bool)
/*  682 */         this.stream.print(", "); 
/*  683 */       this.stream.print("$context");
/*      */     } 
/*      */     
/*  686 */     this.stream.println("        );");
/*  687 */     this.stream.println("            } finally {");
/*  688 */     this.stream.println("                _releaseReply ($in);");
/*  689 */     this.stream.println("            }");
/*  690 */     if (this.localOptimization && !this.isAbstract) {
/*  691 */       this.stream.println("        }");
/*  692 */       writeStubBodyForLocalInvocation(paramString, str);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeStubBodyForLocalInvocation(String paramString1, String paramString2) {
/*  706 */     this.stream.println("        else {");
/*  707 */     this.stream.println("            org.omg.CORBA.portable.ServantObject _so =");
/*      */     
/*  709 */     this.stream.println("                _servant_preinvoke(\"" + paramString2 + "\", _opsClass);");
/*      */     
/*  711 */     this.stream.println("            if (_so == null ) {");
/*  712 */     this.stream.println("                continue;");
/*  713 */     this.stream.println("            }");
/*  714 */     this.stream.println("            " + paramString1 + "Operations _self =");
/*  715 */     this.stream.println("                (" + paramString1 + "Operations) _so.servant;");
/*  716 */     this.stream.println("            try {");
/*  717 */     Enumeration<ParameterEntry> enumeration = this.m.parameters().elements();
/*  718 */     if (this.m instanceof com.sun.tools.corba.se.idl.AttributeEntry)
/*      */     {
/*      */ 
/*      */       
/*  722 */       paramString2 = paramString2.substring(5);
/*      */     }
/*  724 */     boolean bool = (this.m.type() == null) ? true : false;
/*  725 */     if (!bool) {
/*  726 */       this.stream.println("                " + Util.javaName(this.m.type()) + " $result;");
/*      */     }
/*      */     
/*  729 */     if (!isValueInitializer()) {
/*  730 */       if (bool) {
/*  731 */         this.stream.print("                _self." + paramString2 + "( ");
/*      */       } else {
/*  733 */         this.stream.print("                $result = _self." + paramString2 + "( ");
/*      */       } 
/*      */       
/*  736 */       while (enumeration.hasMoreElements()) {
/*  737 */         ParameterEntry parameterEntry = enumeration.nextElement();
/*  738 */         if (enumeration.hasMoreElements()) {
/*  739 */           this.stream.print(" " + parameterEntry.name() + ","); continue;
/*      */         } 
/*  741 */         this.stream.print(" " + parameterEntry.name());
/*      */       } 
/*      */       
/*  744 */       this.stream.print(");");
/*  745 */       this.stream.println(" ");
/*  746 */       if (bool) {
/*  747 */         this.stream.println("                return;");
/*      */       } else {
/*  749 */         this.stream.println("                return $result;");
/*      */       } 
/*      */     } 
/*  752 */     this.stream.println(" ");
/*  753 */     this.stream.println("            }");
/*  754 */     this.stream.println("            finally {");
/*  755 */     this.stream.println("                _servant_postinvoke(_so);");
/*  756 */     this.stream.println("            }");
/*  757 */     this.stream.println("        }");
/*  758 */     this.stream.println("    }");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeLocalStubBody(InterfaceEntry paramInterfaceEntry) {
/*  765 */     String str1 = Util.stripLeadingUnderscores(this.m.name());
/*  766 */     if (this.m instanceof com.sun.tools.corba.se.idl.AttributeEntry)
/*      */     {
/*  768 */       if (this.m.type() == null) {
/*  769 */         str1 = "_set_" + str1;
/*      */       } else {
/*  771 */         str1 = "_get_" + str1;
/*      */       } 
/*      */     }
/*  774 */     this.stream.println("      org.omg.CORBA.portable.ServantObject $so = _servant_preinvoke (\"" + str1 + "\", _opsClass);");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  779 */     String str2 = paramInterfaceEntry.name() + "Operations";
/*  780 */     this.stream.println("      " + str2 + "  $self = (" + str2 + ") $so.servant;");
/*  781 */     this.stream.println();
/*  782 */     this.stream.println("      try {");
/*  783 */     this.stream.print("         ");
/*  784 */     if (this.m.type() != null)
/*  785 */       this.stream.print("return "); 
/*  786 */     this.stream.print("$self." + this.m.name() + " (");
/*      */ 
/*      */     
/*  789 */     boolean bool = true;
/*  790 */     Enumeration<ParameterEntry> enumeration = this.m.parameters().elements();
/*  791 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  793 */       if (bool) {
/*  794 */         bool = false;
/*      */       } else {
/*  796 */         this.stream.print(", ");
/*  797 */       }  ParameterEntry parameterEntry = enumeration.nextElement();
/*  798 */       this.stream.print(parameterEntry.name());
/*      */     } 
/*      */     
/*  801 */     if (this.m.contexts().size() > 0) {
/*      */       
/*  803 */       if (!bool)
/*  804 */         this.stream.print(", "); 
/*  805 */       this.stream.print("$context");
/*      */     } 
/*      */     
/*  808 */     this.stream.println(");");
/*      */ 
/*      */     
/*  811 */     this.stream.println("      } finally {");
/*  812 */     this.stream.println("          _servant_postinvoke ($so);");
/*  813 */     this.stream.println("      }");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeInsert(String paramString1, String paramString2, String paramString3, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  825 */     String str = paramSymtabEntry.name();
/*  826 */     if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*      */ 
/*      */       
/*  829 */       if (str.equals("long long")) {
/*  830 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_longlong (" + paramString2 + ");");
/*  831 */       } else if (str.equals("unsigned short")) {
/*  832 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_ushort (" + paramString2 + ");");
/*  833 */       } else if (str.equals("unsigned long")) {
/*  834 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_ulong (" + paramString2 + ");");
/*  835 */       } else if (str.equals("unsigned long long")) {
/*  836 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_ulonglong (" + paramString2 + ");");
/*      */       } else {
/*  838 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_" + str + " (" + paramString2 + ");");
/*      */       } 
/*  840 */     } else if (paramSymtabEntry instanceof StringEntry) {
/*  841 */       paramPrintWriter.println(paramString1 + paramString3 + ".insert_" + str + " (" + paramString2 + ");");
/*      */     } else {
/*  843 */       paramPrintWriter.println(paramString1 + Util.helperName(paramSymtabEntry, true) + ".insert (" + paramString3 + ", " + paramString2 + ");");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeType(String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  851 */     if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*      */ 
/*      */       
/*  854 */       if (paramSymtabEntry.name().equals("long long")) {
/*  855 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_longlong));");
/*  856 */       } else if (paramSymtabEntry.name().equals("unsigned short")) {
/*  857 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_ushort));");
/*  858 */       } else if (paramSymtabEntry.name().equals("unsigned long")) {
/*  859 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_ulong));");
/*  860 */       } else if (paramSymtabEntry.name().equals("unsigned long long")) {
/*  861 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_ulonglong));");
/*      */       } else {
/*  863 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_" + paramSymtabEntry.name() + "));");
/*      */       } 
/*  865 */     } else if (paramSymtabEntry instanceof StringEntry) {
/*      */       
/*  867 */       StringEntry stringEntry = (StringEntry)paramSymtabEntry;
/*  868 */       Expression expression = stringEntry.maxSize();
/*  869 */       if (expression == null) {
/*  870 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().create_" + paramSymtabEntry.name() + "_tc (" + Util.parseExpression(expression) + "));");
/*      */       } else {
/*  872 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().create_" + paramSymtabEntry.name() + "_tc (0));");
/*      */       } 
/*      */     } else {
/*  875 */       paramPrintWriter.println(paramString1 + paramString2 + '(' + Util.helperName(paramSymtabEntry, true) + ".type ());");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeExtract(String paramString1, String paramString2, String paramString3, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  883 */     if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*      */       
/*  885 */       if (paramSymtabEntry.name().equals("long long")) {
/*  886 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_longlong ();");
/*  887 */       } else if (paramSymtabEntry.name().equals("unsigned short")) {
/*  888 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_ushort ();");
/*  889 */       } else if (paramSymtabEntry.name().equals("unsigned long")) {
/*  890 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_ulong ();");
/*  891 */       } else if (paramSymtabEntry.name().equals("unsigned long long")) {
/*  892 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_ulonglong ();");
/*      */       } else {
/*  894 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_" + paramSymtabEntry.name() + " ();");
/*      */       } 
/*  896 */     } else if (paramSymtabEntry instanceof StringEntry) {
/*  897 */       paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_" + paramSymtabEntry.name() + " ();");
/*      */     } else {
/*  899 */       paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".extract (" + paramString3 + ");");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String writeExtract(String paramString, SymtabEntry paramSymtabEntry) {
/*      */     String str;
/*  908 */     if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*      */       
/*  910 */       if (paramSymtabEntry.name().equals("long long")) {
/*  911 */         str = paramString + ".extract_longlong ()";
/*  912 */       } else if (paramSymtabEntry.name().equals("unsigned short")) {
/*  913 */         str = paramString + ".extract_ushort ()";
/*  914 */       } else if (paramSymtabEntry.name().equals("unsigned long")) {
/*  915 */         str = paramString + ".extract_ulong ()";
/*  916 */       } else if (paramSymtabEntry.name().equals("unsigned long long")) {
/*  917 */         str = paramString + ".extract_ulonglong ()";
/*      */       } else {
/*  919 */         str = paramString + ".extract_" + paramSymtabEntry.name() + " ()";
/*      */       } 
/*  921 */     } else if (paramSymtabEntry instanceof StringEntry) {
/*  922 */       str = paramString + ".extract_" + paramSymtabEntry.name() + " ()";
/*      */     } else {
/*  924 */       str = Util.helperName(paramSymtabEntry, true) + ".extract (" + paramString + ')';
/*  925 */     }  return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeSkeletonBody() {
/*  933 */     SymtabEntry symtabEntry = Util.typeOf(this.m.type());
/*      */ 
/*      */     
/*  936 */     this.stream.print("    ");
/*  937 */     if (symtabEntry != null)
/*  938 */       this.stream.print("return "); 
/*  939 */     this.stream.print("_impl." + this.m.name() + '(');
/*      */ 
/*      */     
/*  942 */     Enumeration<ParameterEntry> enumeration = this.m.parameters().elements();
/*  943 */     boolean bool = true;
/*  944 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  946 */       ParameterEntry parameterEntry = enumeration.nextElement();
/*  947 */       if (bool) {
/*  948 */         bool = false;
/*      */       } else {
/*  950 */         this.stream.print(", ");
/*  951 */       }  this.stream.print(parameterEntry.name());
/*      */     } 
/*  953 */     if (this.m.contexts().size() != 0) {
/*      */       
/*  955 */       if (!bool)
/*  956 */         this.stream.print(", "); 
/*  957 */       this.stream.print("$context");
/*      */     } 
/*      */     
/*  960 */     this.stream.println(");");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String passType(int paramInt) {
/*  969 */     switch (paramInt)
/*      */     
/*      */     { case 1:
/*  972 */         str = "org.omg.CORBA.ARG_INOUT.value";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  982 */         return str;case 2: str = "org.omg.CORBA.ARG_OUT.value"; return str; }  String str = "org.omg.CORBA.ARG_IN.value"; return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void serverMethodName(String paramString) {
/*  993 */     this.realName = (paramString == null) ? "" : paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeOutputStreamWrite(String paramString1, String paramString2, String paramString3, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 1001 */     String str = paramSymtabEntry.name();
/* 1002 */     paramPrintWriter.print(paramString1);
/* 1003 */     if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*      */       
/* 1005 */       if (str.equals("long long")) {
/* 1006 */         paramPrintWriter.println(paramString2 + ".write_longlong (" + paramString3 + ");");
/* 1007 */       } else if (str.equals("unsigned short")) {
/* 1008 */         paramPrintWriter.println(paramString2 + ".write_ushort (" + paramString3 + ");");
/* 1009 */       } else if (str.equals("unsigned long")) {
/* 1010 */         paramPrintWriter.println(paramString2 + ".write_ulong (" + paramString3 + ");");
/* 1011 */       } else if (str.equals("unsigned long long")) {
/* 1012 */         paramPrintWriter.println(paramString2 + ".write_ulonglong (" + paramString3 + ");");
/*      */       } else {
/* 1014 */         paramPrintWriter.println(paramString2 + ".write_" + str + " (" + paramString3 + ");");
/*      */       } 
/* 1016 */     } else if (paramSymtabEntry instanceof StringEntry) {
/* 1017 */       paramPrintWriter.println(paramString2 + ".write_" + str + " (" + paramString3 + ");");
/* 1018 */     } else if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry) {
/* 1019 */       paramPrintWriter.println(paramString2 + ".write_" + paramSymtabEntry.type().name() + " (" + paramString3 + ");");
/* 1020 */     } else if (paramSymtabEntry instanceof ValueBoxEntry) {
/*      */       
/* 1022 */       ValueBoxEntry valueBoxEntry = (ValueBoxEntry)paramSymtabEntry;
/* 1023 */       TypedefEntry typedefEntry = ((InterfaceState)valueBoxEntry.state().elementAt(0)).entry;
/* 1024 */       SymtabEntry symtabEntry = typedefEntry.type();
/*      */ 
/*      */       
/* 1027 */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry && paramString3.endsWith(".value")) {
/* 1028 */         paramPrintWriter.println(Util.helperName(paramSymtabEntry, true) + ".write (" + paramString2 + ",  new " + 
/* 1029 */             Util.javaQualifiedName(paramSymtabEntry) + " (" + paramString3 + "));");
/*      */       } else {
/* 1031 */         paramPrintWriter.println(Util.helperName(paramSymtabEntry, true) + ".write (" + paramString2 + ", " + paramString3 + ");");
/*      */       } 
/* 1033 */     } else if (paramSymtabEntry instanceof ValueEntry) {
/* 1034 */       paramPrintWriter.println(Util.helperName(paramSymtabEntry, true) + ".write (" + paramString2 + ", " + paramString3 + ");");
/*      */     } else {
/* 1036 */       paramPrintWriter.println(Util.helperName(paramSymtabEntry, true) + ".write (" + paramString2 + ", " + paramString3 + ");");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String writeInputStreamRead(String paramString, SymtabEntry paramSymtabEntry) {
/* 1044 */     String str = "";
/* 1045 */     if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*      */       
/* 1047 */       if (paramSymtabEntry.name().equals("long long")) {
/* 1048 */         str = paramString + ".read_longlong ()";
/* 1049 */       } else if (paramSymtabEntry.name().equals("unsigned short")) {
/* 1050 */         str = paramString + ".read_ushort ()";
/* 1051 */       } else if (paramSymtabEntry.name().equals("unsigned long")) {
/* 1052 */         str = paramString + ".read_ulong ()";
/* 1053 */       } else if (paramSymtabEntry.name().equals("unsigned long long")) {
/* 1054 */         str = paramString + ".read_ulonglong ()";
/*      */       } else {
/* 1056 */         str = paramString + ".read_" + paramSymtabEntry.name() + " ()";
/*      */       } 
/* 1058 */     } else if (paramSymtabEntry instanceof StringEntry) {
/* 1059 */       str = paramString + ".read_" + paramSymtabEntry.name() + " ()";
/*      */     } else {
/* 1061 */       str = Util.helperName(paramSymtabEntry, true) + ".read (" + paramString + ')';
/* 1062 */     }  return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeMethodCall(String paramString) {
/* 1070 */     SymtabEntry symtabEntry = Util.typeOf(this.m.type());
/* 1071 */     if (symtabEntry == null) {
/* 1072 */       this.stream.print(paramString + "this." + this.m.name() + " (");
/*      */     } else {
/* 1074 */       this.stream.print(paramString + "$result = this." + this.m.name() + " (");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeCreateReply(String paramString) {
/* 1081 */     this.stream.println(paramString + "out = $rh.createReply();");
/*      */   }
/*      */   
/* 1084 */   protected int methodIndex = 0;
/* 1085 */   protected String realName = "";
/* 1086 */   protected Hashtable symbolTable = null;
/* 1087 */   protected MethodEntry m = null;
/* 1088 */   protected PrintWriter stream = null;
/*      */   protected boolean localOptimization = false;
/*      */   protected boolean isAbstract = false;
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\MethodGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */