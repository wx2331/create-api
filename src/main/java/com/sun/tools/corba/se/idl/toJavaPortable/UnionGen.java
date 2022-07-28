/*      */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*      */ 
/*      */ import com.sun.tools.corba.se.idl.EnumEntry;
/*      */ import com.sun.tools.corba.se.idl.GenFileStream;
/*      */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*      */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*      */ import com.sun.tools.corba.se.idl.UnionBranch;
/*      */ import com.sun.tools.corba.se.idl.UnionEntry;
/*      */ import com.sun.tools.corba.se.idl.UnionGen;
/*      */ import com.sun.tools.corba.se.idl.constExpr.EvaluationException;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
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
/*      */ public class UnionGen
/*      */   implements UnionGen, JavaGenerator
/*      */ {
/*      */   public void generate(Hashtable paramHashtable, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter) {
/*   84 */     this.symbolTable = paramHashtable;
/*   85 */     this.u = paramUnionEntry;
/*   86 */     init();
/*      */     
/*   88 */     openStream();
/*   89 */     if (this.stream == null)
/*      */       return; 
/*   91 */     generateHelper();
/*   92 */     generateHolder();
/*   93 */     writeHeading();
/*   94 */     writeBody();
/*   95 */     writeClosing();
/*   96 */     closeStream();
/*   97 */     generateContainedTypes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void init() {
/*  105 */     this.utype = Util.typeOf(this.u.type());
/*  106 */     this.unionIsEnum = this.utype instanceof EnumEntry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void openStream() {
/*  114 */     this.stream = (PrintWriter)Util.stream((SymtabEntry)this.u, ".java");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void generateHelper() {
/*  122 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, (SymtabEntry)this.u);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void generateHolder() {
/*  130 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, (SymtabEntry)this.u);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeHeading() {
/*  139 */     if (this.unionIsEnum) {
/*  140 */       this.typePackage = Util.javaQualifiedName(this.utype) + '.';
/*      */     } else {
/*  142 */       this.typePackage = "";
/*      */     } 
/*  144 */     Util.writePackage(this.stream, (SymtabEntry)this.u);
/*  145 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*      */     
/*  147 */     String str = this.u.name();
/*  148 */     this.stream.println("public final class " + this.u.name() + " implements org.omg.CORBA.portable.IDLEntity");
/*  149 */     this.stream.println("{");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeBody() {
/*  158 */     int i = this.u.branches().size() + 1;
/*  159 */     Enumeration<UnionBranch> enumeration = this.u.branches().elements();
/*  160 */     byte b = 0;
/*  161 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  163 */       UnionBranch unionBranch = enumeration.nextElement();
/*  164 */       Util.fillInfo((SymtabEntry)unionBranch.typedef);
/*      */ 
/*      */       
/*  167 */       this.stream.println("  private " + Util.javaName((SymtabEntry)unionBranch.typedef) + " ___" + unionBranch.typedef.name() + ";");
/*  168 */       b++;
/*      */     } 
/*  170 */     this.stream.println("  private " + Util.javaName(this.utype) + " __discriminator;");
/*  171 */     this.stream.println("  private boolean __uninitialized = true;");
/*      */ 
/*      */     
/*  174 */     this.stream.println();
/*  175 */     this.stream.println("  public " + this.u.name() + " ()");
/*  176 */     this.stream.println("  {");
/*  177 */     this.stream.println("  }");
/*      */ 
/*      */     
/*  180 */     this.stream.println();
/*  181 */     this.stream.println("  public " + Util.javaName(this.utype) + " " + safeName(this.u, "discriminator") + " ()");
/*  182 */     this.stream.println("  {");
/*  183 */     this.stream.println("    if (__uninitialized)");
/*  184 */     this.stream.println("      throw new org.omg.CORBA.BAD_OPERATION ();");
/*  185 */     this.stream.println("    return __discriminator;");
/*  186 */     this.stream.println("  }");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  192 */     enumeration = this.u.branches().elements();
/*  193 */     b = 0;
/*  194 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  196 */       UnionBranch unionBranch = enumeration.nextElement();
/*  197 */       writeBranchMethods(this.stream, this.u, unionBranch, b++);
/*      */     } 
/*  199 */     if (this.u.defaultBranch() == null && !coversAll(this.u)) {
/*      */       
/*  201 */       this.stream.println();
/*  202 */       this.stream.println("  public void _default ()");
/*  203 */       this.stream.println("  {");
/*  204 */       this.stream.println("    __discriminator = " + defaultDiscriminator(this.u) + ';');
/*  205 */       this.stream.println("    __uninitialized = false;");
/*  206 */       this.stream.println("  }");
/*      */       
/*  208 */       this.stream.println();
/*  209 */       this.stream.println("  public void _default (" + Util.javaName(this.utype) + " discriminator)");
/*      */       
/*  211 */       this.stream.println("  {");
/*  212 */       this.stream.println("    verifyDefault( discriminator ) ;");
/*  213 */       this.stream.println("    __discriminator = discriminator ;");
/*  214 */       this.stream.println("    __uninitialized = false;");
/*  215 */       this.stream.println("  }");
/*      */       
/*  217 */       writeVerifyDefault();
/*      */     } 
/*  219 */     this.stream.println();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeClosing() {
/*  227 */     this.stream.println("} // class " + this.u.name());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void closeStream() {
/*  235 */     this.stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void generateContainedTypes() {
/*  243 */     Enumeration<SymtabEntry> enumeration = this.u.contained().elements();
/*  244 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  246 */       SymtabEntry symtabEntry = enumeration.nextElement();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  252 */       if (!(symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry)) {
/*  253 */         symtabEntry.generate(this.symbolTable, this.stream);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void writeVerifyDefault() {
/*  259 */     Vector vector = vectorizeLabels(this.u.branches(), true);
/*      */     
/*  261 */     if (Util.javaName(this.utype).equals("boolean")) {
/*  262 */       this.stream.println("");
/*  263 */       this.stream.println("  private void verifyDefault (boolean discriminator)");
/*  264 */       this.stream.println("  {");
/*  265 */       if (vector.contains("true")) {
/*  266 */         this.stream.println("    if ( discriminator )");
/*      */       } else {
/*  268 */         this.stream.println("    if ( !discriminator )");
/*  269 */       }  this.stream.println("        throw new org.omg.CORBA.BAD_OPERATION();");
/*  270 */       this.stream.println("  }");
/*      */       
/*      */       return;
/*      */     } 
/*  274 */     this.stream.println("");
/*  275 */     this.stream.println("  private void verifyDefault( " + Util.javaName(this.utype) + " value )");
/*      */     
/*  277 */     this.stream.println("  {");
/*      */     
/*  279 */     if (this.unionIsEnum) {
/*  280 */       this.stream.println("    switch (value.value()) {");
/*      */     } else {
/*  282 */       this.stream.println("    switch (value) {");
/*      */     } 
/*  284 */     Enumeration<String> enumeration = vector.elements();
/*  285 */     while (enumeration.hasMoreElements()) {
/*  286 */       String str = enumeration.nextElement();
/*  287 */       this.stream.println("      case " + str + ":");
/*      */     } 
/*      */     
/*  290 */     this.stream.println("        throw new org.omg.CORBA.BAD_OPERATION() ;");
/*  291 */     this.stream.println("");
/*  292 */     this.stream.println("      default:");
/*  293 */     this.stream.println("        return;");
/*  294 */     this.stream.println("    }");
/*  295 */     this.stream.println("  }");
/*      */   }
/*      */ 
/*      */   
/*      */   private String defaultDiscriminator(UnionEntry paramUnionEntry) {
/*  300 */     Vector vector = vectorizeLabels(paramUnionEntry.branches(), false);
/*  301 */     String str = null;
/*  302 */     SymtabEntry symtabEntry = Util.typeOf(paramUnionEntry.type());
/*  303 */     if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry && symtabEntry.name().equals("boolean")) {
/*      */ 
/*      */ 
/*      */       
/*  307 */       if (vector.contains("true"))
/*  308 */       { str = "false"; }
/*      */       else
/*  310 */       { str = "true"; } 
/*  311 */     } else if (symtabEntry.name().equals("char")) {
/*      */ 
/*      */       
/*  314 */       byte b = 0;
/*  315 */       String str1 = "'\\u0000'";
/*  316 */       while (b != '￿' && vector.contains(str1)) {
/*  317 */         if (++b / 16 == 0) {
/*  318 */           str1 = "'\\u000" + b + "'"; continue;
/*  319 */         }  if (b / 256 == 0) {
/*  320 */           str1 = "\\u00" + b + "'"; continue;
/*  321 */         }  if (b / 4096 == 0) {
/*  322 */           str1 = "\\u0" + b + "'"; continue;
/*      */         } 
/*  324 */         str1 = "\\u" + b + "'";
/*  325 */       }  str = str1;
/*  326 */     } else if (symtabEntry instanceof EnumEntry) {
/*  327 */       Enumeration enumeration = vector.elements();
/*  328 */       EnumEntry enumEntry = (EnumEntry)symtabEntry;
/*  329 */       Vector<String> vector1 = (Vector)enumEntry.elements().clone();
/*      */ 
/*      */       
/*  332 */       while (enumeration.hasMoreElements()) {
/*  333 */         vector1.removeElement(enumeration.nextElement());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  339 */       if (vector1.size() == 0)
/*  340 */       { str = this.typePackage + (String)enumEntry.elements().lastElement(); }
/*      */       else
/*  342 */       { str = this.typePackage + (String)vector1.firstElement(); } 
/*  343 */     } else if (symtabEntry.name().equals("octet")) {
/*  344 */       short s = -128;
/*  345 */       while (s != 127 && vector.contains(Integer.toString(s)))
/*  346 */         s = (short)(s + 1); 
/*  347 */       str = Integer.toString(s);
/*  348 */     } else if (symtabEntry.name().equals("short")) {
/*  349 */       short s = -32768;
/*  350 */       while (s != Short.MAX_VALUE && vector.contains(Integer.toString(s)))
/*  351 */         s = (short)(s + 1); 
/*  352 */       str = Integer.toString(s);
/*  353 */     } else if (symtabEntry.name().equals("long")) {
/*  354 */       int i = Integer.MIN_VALUE;
/*  355 */       while (i != Integer.MAX_VALUE && vector.contains(Integer.toString(i)))
/*  356 */         i++; 
/*  357 */       str = Integer.toString(i);
/*  358 */     } else if (symtabEntry.name().equals("long long")) {
/*  359 */       long l = Long.MIN_VALUE;
/*  360 */       while (l != Long.MAX_VALUE && vector.contains(Long.toString(l)))
/*  361 */         l++; 
/*  362 */       str = Long.toString(l);
/*  363 */     } else if (symtabEntry.name().equals("unsigned short")) {
/*  364 */       short s = 0;
/*  365 */       while (s != Short.MAX_VALUE && vector.contains(Integer.toString(s)))
/*  366 */         s = (short)(s + 1); 
/*  367 */       str = Integer.toString(s);
/*  368 */     } else if (symtabEntry.name().equals("unsigned long")) {
/*  369 */       byte b = 0;
/*  370 */       while (b != Integer.MAX_VALUE && vector.contains(Integer.toString(b)))
/*  371 */         b++; 
/*  372 */       str = Integer.toString(b);
/*  373 */     } else if (symtabEntry.name().equals("unsigned long long")) {
/*  374 */       long l = 0L;
/*  375 */       while (l != Long.MAX_VALUE && vector.contains(Long.toString(l)))
/*  376 */         l++; 
/*  377 */       str = Long.toString(l);
/*      */     } 
/*      */     
/*  380 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Vector vectorizeLabels(Vector paramVector, boolean paramBoolean) {
/*  388 */     Vector<String> vector = new Vector();
/*  389 */     Enumeration<UnionBranch> enumeration = paramVector.elements();
/*  390 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  392 */       UnionBranch unionBranch = enumeration.nextElement();
/*  393 */       Enumeration<Expression> enumeration1 = unionBranch.labels.elements();
/*  394 */       while (enumeration1.hasMoreElements()) {
/*      */         String str;
/*  396 */         Expression expression = enumeration1.nextElement();
/*      */ 
/*      */         
/*  399 */         if (this.unionIsEnum)
/*  400 */         { if (paramBoolean) {
/*  401 */             str = this.typePackage + "_" + Util.parseExpression(expression);
/*      */           } else {
/*  403 */             str = this.typePackage + Util.parseExpression(expression);
/*      */           }  }
/*  405 */         else { str = Util.parseExpression(expression); }
/*      */         
/*  407 */         vector.addElement(str);
/*      */       } 
/*      */     } 
/*  410 */     return vector;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String safeName(UnionEntry paramUnionEntry, String paramString) {
/*  418 */     Enumeration enumeration = paramUnionEntry.branches().elements();
/*  419 */     while (enumeration.hasMoreElements()) {
/*  420 */       if (((UnionBranch)enumeration.nextElement()).typedef.name().equals(paramString)) {
/*      */         
/*  422 */         paramString = '_' + paramString; break;
/*      */       } 
/*      */     } 
/*  425 */     return paramString;
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
/*      */   private boolean coversAll(UnionEntry paramUnionEntry) {
/*  438 */     SymtabEntry symtabEntry = Util.typeOf(paramUnionEntry.type());
/*      */     
/*  440 */     boolean bool = false;
/*  441 */     if (symtabEntry.name().equals("boolean")) {
/*  442 */       if (paramUnionEntry.branches().size() == 2)
/*  443 */         bool = true; 
/*  444 */     } else if (symtabEntry instanceof EnumEntry) {
/*  445 */       Vector vector = vectorizeLabels(paramUnionEntry.branches(), true);
/*  446 */       if (vector.size() == ((EnumEntry)symtabEntry).elements().size()) {
/*  447 */         bool = true;
/*      */       }
/*      */     } 
/*  450 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeBranchMethods(PrintWriter paramPrintWriter, UnionEntry paramUnionEntry, UnionBranch paramUnionBranch, int paramInt) {
/*  459 */     paramPrintWriter.println();
/*      */ 
/*      */     
/*  462 */     paramPrintWriter.println("  public " + Util.javaName((SymtabEntry)paramUnionBranch.typedef) + " " + paramUnionBranch.typedef.name() + " ()");
/*  463 */     paramPrintWriter.println("  {");
/*  464 */     paramPrintWriter.println("    if (__uninitialized)");
/*  465 */     paramPrintWriter.println("      throw new org.omg.CORBA.BAD_OPERATION ();");
/*  466 */     paramPrintWriter.println("    verify" + paramUnionBranch.typedef.name() + " (__discriminator);");
/*  467 */     paramPrintWriter.println("    return ___" + paramUnionBranch.typedef.name() + ";");
/*  468 */     paramPrintWriter.println("  }");
/*      */ 
/*      */     
/*  471 */     paramPrintWriter.println();
/*      */ 
/*      */     
/*  474 */     paramPrintWriter.println("  public void " + paramUnionBranch.typedef.name() + " (" + Util.javaName((SymtabEntry)paramUnionBranch.typedef) + " value)");
/*  475 */     paramPrintWriter.println("  {");
/*  476 */     if (paramUnionBranch.labels.size() == 0) {
/*      */ 
/*      */       
/*  479 */       paramPrintWriter.println("    __discriminator = " + defaultDiscriminator(paramUnionEntry) + ";");
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  484 */     else if (this.unionIsEnum) {
/*  485 */       paramPrintWriter.println("    __discriminator = " + this.typePackage + Util.parseExpression(paramUnionBranch.labels.firstElement()) + ";");
/*      */     } else {
/*  487 */       paramPrintWriter.println("    __discriminator = " + cast(paramUnionBranch.labels.firstElement(), paramUnionEntry.type()) + ";");
/*      */     } 
/*  489 */     paramPrintWriter.println("    ___" + paramUnionBranch.typedef.name() + " = value;");
/*  490 */     paramPrintWriter.println("    __uninitialized = false;");
/*  491 */     paramPrintWriter.println("  }");
/*      */     
/*  493 */     SymtabEntry symtabEntry = Util.typeOf(paramUnionEntry.type());
/*      */ 
/*      */ 
/*      */     
/*  497 */     if (paramUnionBranch.labels.size() > 0 || paramUnionBranch.isDefault) {
/*      */       
/*  499 */       paramPrintWriter.println();
/*      */ 
/*      */       
/*  502 */       paramPrintWriter.println("  public void " + paramUnionBranch.typedef.name() + " (" + Util.javaName(symtabEntry) + " discriminator, " + Util.javaName((SymtabEntry)paramUnionBranch.typedef) + " value)");
/*  503 */       paramPrintWriter.println("  {");
/*  504 */       paramPrintWriter.println("    verify" + paramUnionBranch.typedef.name() + " (discriminator);");
/*  505 */       paramPrintWriter.println("    __discriminator = discriminator;");
/*  506 */       paramPrintWriter.println("    ___" + paramUnionBranch.typedef.name() + " = value;");
/*  507 */       paramPrintWriter.println("    __uninitialized = false;");
/*  508 */       paramPrintWriter.println("  }");
/*      */     } 
/*      */ 
/*      */     
/*  512 */     paramPrintWriter.println();
/*  513 */     paramPrintWriter.println("  private void verify" + paramUnionBranch.typedef.name() + " (" + Util.javaName(symtabEntry) + " discriminator)");
/*  514 */     paramPrintWriter.println("  {");
/*      */     
/*  516 */     boolean bool = true;
/*      */     
/*  518 */     if (!paramUnionBranch.isDefault || paramUnionEntry.branches().size() != 1) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  525 */       paramPrintWriter.print("    if (");
/*  526 */       if (paramUnionBranch.isDefault) {
/*      */         
/*  528 */         Enumeration<UnionBranch> enumeration = paramUnionEntry.branches().elements();
/*  529 */         while (enumeration.hasMoreElements()) {
/*      */           
/*  531 */           UnionBranch unionBranch = enumeration.nextElement();
/*  532 */           if (unionBranch != paramUnionBranch) {
/*      */             
/*  534 */             Enumeration<Expression> enumeration1 = unionBranch.labels.elements();
/*  535 */             while (enumeration1.hasMoreElements())
/*      */             {
/*  537 */               Expression expression = enumeration1.nextElement();
/*  538 */               if (!bool)
/*  539 */                 paramPrintWriter.print(" || "); 
/*  540 */               if (this.unionIsEnum) {
/*  541 */                 paramPrintWriter.print("discriminator == " + this.typePackage + Util.parseExpression(expression));
/*      */               } else {
/*  543 */                 paramPrintWriter.print("discriminator == " + Util.parseExpression(expression));
/*  544 */               }  bool = false;
/*      */             }
/*      */           
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/*  551 */         Enumeration<Expression> enumeration = paramUnionBranch.labels.elements();
/*  552 */         while (enumeration.hasMoreElements()) {
/*      */           
/*  554 */           Expression expression = enumeration.nextElement();
/*  555 */           if (!bool)
/*  556 */             paramPrintWriter.print(" && "); 
/*  557 */           if (this.unionIsEnum) {
/*  558 */             paramPrintWriter.print("discriminator != " + this.typePackage + Util.parseExpression(expression));
/*      */           } else {
/*  560 */             paramPrintWriter.print("discriminator != " + Util.parseExpression(expression));
/*  561 */           }  bool = false;
/*      */         } 
/*      */       } 
/*  564 */       paramPrintWriter.println(")");
/*  565 */       paramPrintWriter.println("      throw new org.omg.CORBA.BAD_OPERATION ();");
/*      */     } 
/*  567 */     paramPrintWriter.println("  }");
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
/*      */ 
/*      */ 
/*      */   
/*      */   private int unionLabelSize(UnionEntry paramUnionEntry) {
/*  583 */     int i = 0;
/*  584 */     Vector<UnionBranch> vector = paramUnionEntry.branches();
/*  585 */     for (byte b = 0; b < vector.size(); b++) {
/*  586 */       UnionBranch unionBranch = vector.get(b);
/*  587 */       int j = unionBranch.labels.size();
/*  588 */       i += (j == 0) ? 1 : j;
/*      */     } 
/*  590 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  596 */     TCOffsets tCOffsets = new TCOffsets();
/*  597 */     UnionEntry unionEntry = (UnionEntry)paramSymtabEntry;
/*  598 */     String str1 = "_disTypeCode" + paramInt;
/*  599 */     String str2 = "_members" + paramInt;
/*      */ 
/*      */     
/*  602 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + str1 + ';');
/*  603 */     paramInt = ((JavaGenerator)unionEntry.type().generator()).type(paramInt + 1, paramString1, tCOffsets, str1, unionEntry
/*  604 */         .type(), paramPrintWriter);
/*  605 */     paramTCOffsets.bumpCurrentOffset(tCOffsets.currentOffset());
/*      */     
/*  607 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.UnionMember[] " + str2 + " = new org.omg.CORBA.UnionMember [" + 
/*  608 */         unionLabelSize(unionEntry) + "];");
/*  609 */     String str3 = "_tcOf" + str2;
/*  610 */     String str4 = "_anyOf" + str2;
/*  611 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + str3 + ';');
/*  612 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.Any " + str4 + ';');
/*      */     
/*  614 */     tCOffsets = new TCOffsets();
/*  615 */     tCOffsets.set(paramSymtabEntry);
/*  616 */     int i = tCOffsets.currentOffset();
/*  617 */     for (byte b = 0; b < unionEntry.branches().size(); b++) {
/*  618 */       UnionBranch unionBranch = unionEntry.branches().elementAt(b);
/*  619 */       TypedefEntry typedefEntry = unionBranch.typedef;
/*  620 */       Vector vector = unionBranch.labels;
/*  621 */       String str = Util.stripLeadingUnderscores(typedefEntry.name());
/*      */       
/*  623 */       if (vector.size() == 0) {
/*  624 */         paramPrintWriter.println();
/*  625 */         paramPrintWriter.println(paramString1 + "// Branch for " + str + " (Default case)");
/*      */         
/*  627 */         SymtabEntry symtabEntry = Util.typeOf(unionEntry.type());
/*  628 */         paramPrintWriter.println(paramString1 + str4 + " = org.omg.CORBA.ORB.init ().create_any ();");
/*      */         
/*  630 */         paramPrintWriter.println(paramString1 + str4 + ".insert_octet ((byte)0); // default member label");
/*      */ 
/*      */         
/*  633 */         tCOffsets.bumpCurrentOffset(4);
/*  634 */         paramInt = ((JavaGenerator)typedefEntry.generator()).type(paramInt, paramString1, tCOffsets, str3, (SymtabEntry)typedefEntry, paramPrintWriter);
/*  635 */         int j = tCOffsets.currentOffset();
/*  636 */         tCOffsets = new TCOffsets();
/*  637 */         tCOffsets.set(paramSymtabEntry);
/*  638 */         tCOffsets.bumpCurrentOffset(j - i);
/*      */ 
/*      */         
/*  641 */         paramPrintWriter.println(paramString1 + str2 + '[' + b + "] = new org.omg.CORBA.UnionMember (");
/*  642 */         paramPrintWriter.println(paramString1 + "  \"" + str + "\",");
/*  643 */         paramPrintWriter.println(paramString1 + "  " + str4 + ',');
/*  644 */         paramPrintWriter.println(paramString1 + "  " + str3 + ',');
/*  645 */         paramPrintWriter.println(paramString1 + "  null);");
/*      */       } else {
/*  647 */         Enumeration<Expression> enumeration = vector.elements();
/*  648 */         while (enumeration.hasMoreElements()) {
/*  649 */           Expression expression = enumeration.nextElement();
/*  650 */           String str5 = Util.parseExpression(expression);
/*      */           
/*  652 */           paramPrintWriter.println();
/*  653 */           paramPrintWriter.println(paramString1 + "// Branch for " + str + " (case label " + str5 + ")");
/*      */ 
/*      */           
/*  656 */           SymtabEntry symtabEntry = Util.typeOf(unionEntry.type());
/*      */ 
/*      */           
/*  659 */           paramPrintWriter.println(paramString1 + str4 + " = org.omg.CORBA.ORB.init ().create_any ();");
/*      */           
/*  661 */           if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*  662 */             paramPrintWriter.println(paramString1 + str4 + ".insert_" + 
/*  663 */                 Util.collapseName(symtabEntry.name()) + " ((" + Util.javaName(symtabEntry) + ')' + str5 + ");");
/*      */           } else {
/*      */             
/*  666 */             String str6 = Util.javaName(symtabEntry);
/*  667 */             paramPrintWriter.println(paramString1 + Util.helperName(symtabEntry, false) + ".insert (" + str4 + ", " + str6 + '.' + str5 + ");");
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  672 */           tCOffsets.bumpCurrentOffset(4);
/*  673 */           paramInt = ((JavaGenerator)typedefEntry.generator()).type(paramInt, paramString1, tCOffsets, str3, (SymtabEntry)typedefEntry, paramPrintWriter);
/*  674 */           int j = tCOffsets.currentOffset();
/*  675 */           tCOffsets = new TCOffsets();
/*  676 */           tCOffsets.set(paramSymtabEntry);
/*  677 */           tCOffsets.bumpCurrentOffset(j - i);
/*      */ 
/*      */           
/*  680 */           paramPrintWriter.println(paramString1 + str2 + '[' + b + "] = new org.omg.CORBA.UnionMember (");
/*  681 */           paramPrintWriter.println(paramString1 + "  \"" + str + "\",");
/*  682 */           paramPrintWriter.println(paramString1 + "  " + str4 + ',');
/*  683 */           paramPrintWriter.println(paramString1 + "  " + str3 + ',');
/*  684 */           paramPrintWriter.println(paramString1 + "  null);");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  689 */     paramTCOffsets.bumpCurrentOffset(tCOffsets.currentOffset());
/*      */ 
/*      */     
/*  692 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_union_tc (" + 
/*  693 */         Util.helperName((SymtabEntry)unionEntry, true) + ".id (), \"" + paramSymtabEntry.name() + "\", " + str1 + ", " + str2 + ");");
/*      */     
/*  695 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  701 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/*  702 */     return paramInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  707 */     paramPrintWriter.println("    " + paramString + " value = new " + paramString + " ();");
/*  708 */     read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*  709 */     paramPrintWriter.println("    return value;");
/*      */   }
/*      */ 
/*      */   
/*      */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  714 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  720 */     UnionEntry unionEntry = (UnionEntry)paramSymtabEntry;
/*  721 */     String str = "_dis" + paramInt++;
/*  722 */     SymtabEntry symtabEntry = Util.typeOf(unionEntry.type());
/*  723 */     Util.writeInitializer(paramString1, str, "", symtabEntry, paramPrintWriter);
/*      */     
/*  725 */     if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*  726 */       paramInt = ((JavaGenerator)symtabEntry.generator()).read(paramInt, paramString1, str, symtabEntry, paramPrintWriter);
/*      */     } else {
/*  728 */       paramPrintWriter.println(paramString1 + str + " = " + Util.helperName(symtabEntry, true) + ".read (istream);");
/*      */     } 
/*  730 */     if (symtabEntry.name().equals("boolean")) {
/*  731 */       paramInt = readBoolean(str, paramInt, paramString1, paramString2, unionEntry, paramPrintWriter);
/*      */     } else {
/*  733 */       paramInt = readNonBoolean(str, paramInt, paramString1, paramString2, unionEntry, paramPrintWriter);
/*      */     } 
/*  735 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int readBoolean(String paramString1, int paramInt, String paramString2, String paramString3, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter) {
/*  741 */     UnionBranch unionBranch2, unionBranch1 = paramUnionEntry.branches().firstElement();
/*      */ 
/*      */     
/*  744 */     if (paramUnionEntry.branches().size() == 2) {
/*  745 */       unionBranch2 = paramUnionEntry.branches().lastElement();
/*      */     } else {
/*  747 */       unionBranch2 = null;
/*      */     } 
/*  749 */     boolean bool = false;
/*  750 */     boolean bool1 = false;
/*      */     try {
/*  752 */       if (paramUnionEntry.branches().size() == 1 && (paramUnionEntry
/*  753 */         .defaultBranch() != null || unionBranch1.labels.size() == 2)) {
/*  754 */         bool1 = true;
/*      */       } else {
/*  756 */         Expression expression = unionBranch1.labels.firstElement();
/*  757 */         Boolean bool2 = (Boolean)expression.evaluate();
/*  758 */         bool = bool2.booleanValue();
/*      */       } 
/*  760 */     } catch (EvaluationException evaluationException) {}
/*      */ 
/*      */ 
/*      */     
/*  764 */     if (bool1) {
/*      */ 
/*      */       
/*  767 */       paramInt = readBranch(paramInt, paramString2, unionBranch1.typedef.name(), "", unionBranch1.typedef, paramPrintWriter);
/*      */     } else {
/*      */       
/*  770 */       if (!bool) {
/*  771 */         UnionBranch unionBranch = unionBranch1;
/*  772 */         unionBranch1 = unionBranch2;
/*  773 */         unionBranch2 = unionBranch;
/*      */       } 
/*      */       
/*  776 */       paramPrintWriter.println(paramString2 + "if (" + paramString1 + ')');
/*      */       
/*  778 */       if (unionBranch1 == null) {
/*  779 */         paramPrintWriter.println(paramString2 + "  value._default(" + paramString1 + ");");
/*      */       } else {
/*  781 */         paramPrintWriter.println(paramString2 + '{');
/*  782 */         paramInt = readBranch(paramInt, paramString2 + "  ", unionBranch1.typedef.name(), paramString1, unionBranch1.typedef, paramPrintWriter);
/*      */         
/*  784 */         paramPrintWriter.println(paramString2 + '}');
/*      */       } 
/*      */       
/*  787 */       paramPrintWriter.println(paramString2 + "else");
/*      */       
/*  789 */       if (unionBranch2 == null) {
/*  790 */         paramPrintWriter.println(paramString2 + "  value._default(" + paramString1 + ");");
/*      */       } else {
/*  792 */         paramPrintWriter.println(paramString2 + '{');
/*  793 */         paramInt = readBranch(paramInt, paramString2 + "  ", unionBranch2.typedef.name(), paramString1, unionBranch2.typedef, paramPrintWriter);
/*      */         
/*  795 */         paramPrintWriter.println(paramString2 + '}');
/*      */       } 
/*      */     } 
/*      */     
/*  799 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int readNonBoolean(String paramString1, int paramInt, String paramString2, String paramString3, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter) {
/*  805 */     SymtabEntry symtabEntry = Util.typeOf(paramUnionEntry.type());
/*      */     
/*  807 */     if (symtabEntry instanceof EnumEntry) {
/*  808 */       paramPrintWriter.println(paramString2 + "switch (" + paramString1 + ".value ())");
/*      */     } else {
/*  810 */       paramPrintWriter.println(paramString2 + "switch (" + paramString1 + ')');
/*      */     } 
/*  812 */     paramPrintWriter.println(paramString2 + '{');
/*  813 */     String str = Util.javaQualifiedName(symtabEntry) + '.';
/*      */     
/*  815 */     Enumeration<UnionBranch> enumeration = paramUnionEntry.branches().elements();
/*  816 */     while (enumeration.hasMoreElements()) {
/*  817 */       UnionBranch unionBranch = enumeration.nextElement();
/*  818 */       Enumeration<Expression> enumeration1 = unionBranch.labels.elements();
/*      */       
/*  820 */       while (enumeration1.hasMoreElements()) {
/*  821 */         Expression expression = enumeration1.nextElement();
/*      */         
/*  823 */         if (symtabEntry instanceof EnumEntry) {
/*  824 */           String str1 = Util.parseExpression(expression);
/*  825 */           paramPrintWriter.println(paramString2 + "  case " + str + '_' + str1 + ':'); continue;
/*      */         } 
/*  827 */         paramPrintWriter.println(paramString2 + "  case " + cast(expression, symtabEntry) + ':');
/*      */       } 
/*      */       
/*  830 */       if (!unionBranch.typedef.equals(paramUnionEntry.defaultBranch())) {
/*  831 */         paramInt = readBranch(paramInt, paramString2 + "    ", unionBranch.typedef.name(), 
/*  832 */             (unionBranch.labels.size() > 1) ? paramString1 : "", unionBranch.typedef, paramPrintWriter);
/*      */         
/*  834 */         paramPrintWriter.println(paramString2 + "    break;");
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  840 */     if (!coversAll(paramUnionEntry)) {
/*  841 */       paramPrintWriter.println(paramString2 + "  default:");
/*      */       
/*  843 */       if (paramUnionEntry.defaultBranch() == null) {
/*      */ 
/*      */         
/*  846 */         paramPrintWriter.println(paramString2 + "    value._default( " + paramString1 + " ) ;");
/*      */       } else {
/*  848 */         paramInt = readBranch(paramInt, paramString2 + "    ", paramUnionEntry.defaultBranch().name(), paramString1, paramUnionEntry
/*  849 */             .defaultBranch(), paramPrintWriter);
/*      */       } 
/*      */       
/*  852 */       paramPrintWriter.println(paramString2 + "    break;");
/*      */     } 
/*      */     
/*  855 */     paramPrintWriter.println(paramString2 + '}');
/*      */     
/*  857 */     return paramInt;
/*      */   }
/*      */ 
/*      */   
/*      */   private int readBranch(int paramInt, String paramString1, String paramString2, String paramString3, TypedefEntry paramTypedefEntry, PrintWriter paramPrintWriter) {
/*  862 */     SymtabEntry symtabEntry = paramTypedefEntry.type();
/*  863 */     Util.writeInitializer(paramString1, '_' + paramString2, "", (SymtabEntry)paramTypedefEntry, paramPrintWriter);
/*      */     
/*  865 */     if (!paramTypedefEntry.arrayInfo().isEmpty() || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry) {
/*      */ 
/*      */ 
/*      */       
/*  869 */       paramInt = ((JavaGenerator)paramTypedefEntry.generator()).read(paramInt, paramString1, '_' + paramString2, (SymtabEntry)paramTypedefEntry, paramPrintWriter);
/*      */     } else {
/*  871 */       paramPrintWriter.println(paramString1 + '_' + paramString2 + " = " + Util.helperName(symtabEntry, true) + ".read (istream);");
/*      */     } 
/*      */     
/*  874 */     paramPrintWriter.print(paramString1 + "value." + paramString2 + " (");
/*  875 */     if (paramString3 == "") {
/*  876 */       paramPrintWriter.println("_" + paramString2 + ");");
/*      */     } else {
/*  878 */       paramPrintWriter.println(paramString3 + ", _" + paramString2 + ");");
/*      */     } 
/*  880 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  888 */     UnionEntry unionEntry = (UnionEntry)paramSymtabEntry;
/*  889 */     SymtabEntry symtabEntry = Util.typeOf(unionEntry.type());
/*  890 */     if (symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/*  891 */       paramInt = ((JavaGenerator)symtabEntry.generator()).write(paramInt, paramString1, paramString2 + ".discriminator ()", symtabEntry, paramPrintWriter);
/*      */     } else {
/*  893 */       paramPrintWriter.println(paramString1 + Util.helperName(symtabEntry, true) + ".write (ostream, " + paramString2 + ".discriminator ());");
/*  894 */     }  if (symtabEntry.name().equals("boolean")) {
/*  895 */       paramInt = writeBoolean(paramString2 + ".discriminator ()", paramInt, paramString1, paramString2, unionEntry, paramPrintWriter);
/*      */     } else {
/*  897 */       paramInt = writeNonBoolean(paramString2 + ".discriminator ()", paramInt, paramString1, paramString2, unionEntry, paramPrintWriter);
/*  898 */     }  return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int writeBoolean(String paramString1, int paramInt, String paramString2, String paramString3, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter) {
/*      */     UnionBranch unionBranch2;
/*  906 */     SymtabEntry symtabEntry = Util.typeOf(paramUnionEntry.type());
/*  907 */     UnionBranch unionBranch1 = paramUnionEntry.branches().firstElement();
/*      */     
/*  909 */     if (paramUnionEntry.branches().size() == 2) {
/*  910 */       unionBranch2 = paramUnionEntry.branches().lastElement();
/*      */     } else {
/*  912 */       unionBranch2 = null;
/*  913 */     }  boolean bool = false;
/*  914 */     boolean bool1 = false;
/*      */     
/*      */     try {
/*  917 */       if (paramUnionEntry.branches().size() == 1 && (paramUnionEntry.defaultBranch() != null || unionBranch1.labels.size() == 2)) {
/*  918 */         bool1 = true;
/*      */       } else {
/*  920 */         bool = ((Boolean)((Expression)unionBranch1.labels.firstElement()).evaluate()).booleanValue();
/*      */       } 
/*  922 */     } catch (EvaluationException evaluationException) {}
/*      */ 
/*      */     
/*  925 */     if (bool1) {
/*      */ 
/*      */ 
/*      */       
/*  929 */       paramInt = writeBranch(paramInt, paramString2, paramString3, unionBranch1.typedef, paramPrintWriter);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  934 */       if (!bool) {
/*      */         
/*  936 */         UnionBranch unionBranch = unionBranch1;
/*  937 */         unionBranch1 = unionBranch2;
/*  938 */         unionBranch2 = unionBranch;
/*      */       } 
/*  940 */       if (unionBranch1 != null && unionBranch2 != null) {
/*  941 */         paramPrintWriter.println(paramString2 + "if (" + paramString1 + ')');
/*  942 */         paramPrintWriter.println(paramString2 + '{');
/*  943 */         paramInt = writeBranch(paramInt, paramString2 + "  ", paramString3, unionBranch1.typedef, paramPrintWriter);
/*  944 */         paramPrintWriter.println(paramString2 + '}');
/*  945 */         paramPrintWriter.println(paramString2 + "else");
/*  946 */         paramPrintWriter.println(paramString2 + '{');
/*  947 */         paramInt = writeBranch(paramInt, paramString2 + "  ", paramString3, unionBranch2.typedef, paramPrintWriter);
/*  948 */         paramPrintWriter.println(paramString2 + '}');
/*  949 */       } else if (unionBranch1 != null) {
/*  950 */         paramPrintWriter.println(paramString2 + "if (" + paramString1 + ')');
/*  951 */         paramPrintWriter.println(paramString2 + '{');
/*  952 */         paramInt = writeBranch(paramInt, paramString2 + "  ", paramString3, unionBranch1.typedef, paramPrintWriter);
/*  953 */         paramPrintWriter.println(paramString2 + '}');
/*      */       } else {
/*  955 */         paramPrintWriter.println(paramString2 + "if (!" + paramString1 + ')');
/*  956 */         paramPrintWriter.println(paramString2 + '{');
/*  957 */         paramInt = writeBranch(paramInt, paramString2 + "  ", paramString3, unionBranch2.typedef, paramPrintWriter);
/*  958 */         paramPrintWriter.println(paramString2 + '}');
/*      */       } 
/*      */     } 
/*  961 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int writeNonBoolean(String paramString1, int paramInt, String paramString2, String paramString3, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter) {
/*  969 */     SymtabEntry symtabEntry = Util.typeOf(paramUnionEntry.type());
/*  970 */     if (symtabEntry instanceof EnumEntry) {
/*  971 */       paramPrintWriter.println(paramString2 + "switch (" + paramString3 + ".discriminator ().value ())");
/*      */     } else {
/*  973 */       paramPrintWriter.println(paramString2 + "switch (" + paramString3 + ".discriminator ())");
/*  974 */     }  paramPrintWriter.println(paramString2 + "{");
/*  975 */     String str = Util.javaQualifiedName(symtabEntry) + '.';
/*  976 */     Enumeration<UnionBranch> enumeration = paramUnionEntry.branches().elements();
/*  977 */     while (enumeration.hasMoreElements()) {
/*      */       
/*  979 */       UnionBranch unionBranch = enumeration.nextElement();
/*  980 */       Enumeration<Expression> enumeration1 = unionBranch.labels.elements();
/*  981 */       while (enumeration1.hasMoreElements()) {
/*      */         
/*  983 */         Expression expression = enumeration1.nextElement();
/*  984 */         if (symtabEntry instanceof EnumEntry) {
/*      */           
/*  986 */           String str1 = Util.parseExpression(expression);
/*  987 */           paramPrintWriter.println(paramString2 + "  case " + str + '_' + str1 + ":");
/*      */           continue;
/*      */         } 
/*  990 */         paramPrintWriter.println(paramString2 + "  case " + cast(expression, symtabEntry) + ':');
/*      */       } 
/*  992 */       if (!unionBranch.typedef.equals(paramUnionEntry.defaultBranch())) {
/*      */         
/*  994 */         paramInt = writeBranch(paramInt, paramString2 + "    ", paramString3, unionBranch.typedef, paramPrintWriter);
/*  995 */         paramPrintWriter.println(paramString2 + "    break;");
/*      */       } 
/*      */     } 
/*  998 */     if (paramUnionEntry.defaultBranch() != null) {
/*  999 */       paramPrintWriter.println(paramString2 + "  default:");
/* 1000 */       paramInt = writeBranch(paramInt, paramString2 + "    ", paramString3, paramUnionEntry.defaultBranch(), paramPrintWriter);
/* 1001 */       paramPrintWriter.println(paramString2 + "    break;");
/*      */     } 
/* 1003 */     paramPrintWriter.println(paramString2 + "}");
/* 1004 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int writeBranch(int paramInt, String paramString1, String paramString2, TypedefEntry paramTypedefEntry, PrintWriter paramPrintWriter) {
/* 1012 */     SymtabEntry symtabEntry = paramTypedefEntry.type();
/* 1013 */     if (!paramTypedefEntry.arrayInfo().isEmpty() || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry) {
/* 1014 */       paramInt = ((JavaGenerator)paramTypedefEntry.generator()).write(paramInt, paramString1, paramString2 + '.' + paramTypedefEntry.name() + " ()", (SymtabEntry)paramTypedefEntry, paramPrintWriter);
/*      */     } else {
/* 1016 */       paramPrintWriter.println(paramString1 + Util.helperName(symtabEntry, true) + ".write (ostream, " + paramString2 + '.' + paramTypedefEntry.name() + " ());");
/* 1017 */     }  return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String cast(Expression paramExpression, SymtabEntry paramSymtabEntry) {
/* 1028 */     String str = Util.parseExpression(paramExpression);
/* 1029 */     if (paramSymtabEntry.name().indexOf("short") >= 0) {
/*      */       
/* 1031 */       if (paramExpression.value() instanceof Long) {
/*      */         
/* 1033 */         long l = ((Long)paramExpression.value()).longValue();
/* 1034 */         if (l > 32767L) {
/* 1035 */           str = "(short)(" + str + ')';
/*      */         }
/* 1037 */       } else if (paramExpression.value() instanceof Integer) {
/*      */         
/* 1039 */         int i = ((Integer)paramExpression.value()).intValue();
/* 1040 */         if (i > 32767) {
/* 1041 */           str = "(short)(" + str + ')';
/*      */         }
/*      */       } 
/* 1044 */     } else if (paramSymtabEntry.name().indexOf("long") >= 0) {
/*      */       
/* 1046 */       if (paramExpression.value() instanceof Long) {
/*      */         
/* 1048 */         long l = ((Long)paramExpression.value()).longValue();
/*      */ 
/*      */ 
/*      */         
/* 1052 */         if (l > 2147483647L || l == -2147483648L) {
/* 1053 */           str = "(int)(" + str + ')';
/*      */         }
/* 1055 */       } else if (paramExpression.value() instanceof Integer) {
/*      */         
/* 1057 */         int i = ((Integer)paramExpression.value()).intValue();
/*      */ 
/*      */ 
/*      */         
/* 1061 */         if (i > Integer.MAX_VALUE || i == Integer.MIN_VALUE)
/* 1062 */           str = "(int)(" + str + ')'; 
/*      */       } 
/*      */     } 
/* 1065 */     return str;
/*      */   }
/*      */   
/* 1068 */   protected Hashtable symbolTable = null;
/* 1069 */   protected UnionEntry u = null;
/* 1070 */   protected PrintWriter stream = null;
/* 1071 */   protected SymtabEntry utype = null;
/*      */   protected boolean unionIsEnum;
/* 1073 */   protected String typePackage = "";
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\UnionGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */