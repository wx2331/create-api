/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ import sun.tools.javac.SourceClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Assembler
/*     */   implements Constants
/*     */ {
/*     */   static final int NOTREACHED = 0;
/*     */   static final int REACHED = 1;
/*     */   static final int NEEDED = 2;
/*  60 */   Label first = new Label();
/*  61 */   Instruction last = this.first;
/*     */   
/*     */   int maxdepth;
/*     */   
/*     */   int maxvar;
/*     */   
/*     */   int maxpc;
/*     */   
/*     */   public void add(Instruction paramInstruction) {
/*  70 */     if (paramInstruction != null) {
/*  71 */       this.last.next = paramInstruction;
/*  72 */       this.last = paramInstruction;
/*     */     } 
/*     */   }
/*     */   public void add(long paramLong, int paramInt) {
/*  76 */     add(new Instruction(paramLong, paramInt, null));
/*     */   }
/*     */   public void add(long paramLong, int paramInt, Object paramObject) {
/*  79 */     add(new Instruction(paramLong, paramInt, paramObject));
/*     */   }
/*     */   
/*     */   public void add(long paramLong, int paramInt, Object paramObject, boolean paramBoolean) {
/*  83 */     add(new Instruction(paramLong, paramInt, paramObject, paramBoolean));
/*     */   }
/*     */   
/*     */   public void add(boolean paramBoolean, long paramLong, int paramInt, Object paramObject) {
/*  87 */     add(new Instruction(paramBoolean, paramLong, paramInt, paramObject));
/*     */   }
/*     */   
/*     */   public void add(long paramLong, int paramInt, boolean paramBoolean) {
/*  91 */     add(new Instruction(paramLong, paramInt, paramBoolean));
/*     */   }
/*     */   
/*  94 */   static Vector<String> SourceClassList = new Vector<>();
/*     */   
/*  96 */   static Vector<String> TmpCovTable = new Vector<>();
/*     */   
/*  98 */   static int[] JcovClassCountArray = new int[9];
/*     */   
/* 100 */   static String JcovMagicLine = "JCOV-DATA-FILE-VERSION: 2.0";
/* 101 */   static String JcovClassLine = "CLASS: ";
/* 102 */   static String JcovSrcfileLine = "SRCFILE: ";
/* 103 */   static String JcovTimestampLine = "TIMESTAMP: ";
/* 104 */   static String JcovDataLine = "DATA: ";
/* 105 */   static String JcovHeadingLine = "#kind\tcount";
/*     */   
/* 107 */   static int[] arrayModifiers = new int[] { 1, 2, 4, 1024, 16, 512 };
/*     */   
/* 109 */   static int[] arrayModifiersOpc = new int[] { 121, 120, 122, 130, 128, 114 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void optimize(Environment paramEnvironment, Label paramLabel) {
/* 117 */     paramLabel.pc = 1;
/*     */     
/* 119 */     for (Instruction instruction = paramLabel.next; instruction != null; instruction = instruction.next) {
/* 120 */       SwitchData switchData; TryData tryData; Enumeration<Label> enumeration; switch (instruction.pc) {
/*     */         case 0:
/* 122 */           instruction.optimize(paramEnvironment);
/* 123 */           instruction.pc = 1;
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1:
/*     */           return;
/*     */       } 
/*     */       
/* 131 */       switch (instruction.opc) {
/*     */         case -2:
/*     */         case -1:
/* 134 */           if (instruction.pc == 1) {
/* 135 */             instruction.pc = 0;
/*     */           }
/*     */           break;
/*     */         
/*     */         case 153:
/*     */         case 154:
/*     */         case 155:
/*     */         case 156:
/*     */         case 157:
/*     */         case 158:
/*     */         case 159:
/*     */         case 160:
/*     */         case 161:
/*     */         case 162:
/*     */         case 163:
/*     */         case 164:
/*     */         case 165:
/*     */         case 166:
/*     */         case 198:
/*     */         case 199:
/* 155 */           optimize(paramEnvironment, (Label)instruction.value);
/*     */           break;
/*     */         
/*     */         case 167:
/* 159 */           optimize(paramEnvironment, (Label)instruction.value);
/*     */           return;
/*     */         
/*     */         case 168:
/* 163 */           optimize(paramEnvironment, (Label)instruction.value);
/*     */           break;
/*     */         
/*     */         case 169:
/*     */         case 172:
/*     */         case 173:
/*     */         case 174:
/*     */         case 175:
/*     */         case 176:
/*     */         case 177:
/*     */         case 191:
/*     */           return;
/*     */         
/*     */         case 170:
/*     */         case 171:
/* 178 */           switchData = (SwitchData)instruction.value;
/* 179 */           optimize(paramEnvironment, switchData.defaultLabel);
/* 180 */           for (enumeration = switchData.tab.elements(); enumeration.hasMoreElements();) {
/* 181 */             optimize(paramEnvironment, enumeration.nextElement());
/*     */           }
/*     */           return;
/*     */ 
/*     */         
/*     */         case -3:
/* 187 */           tryData = (TryData)instruction.value;
/* 188 */           (tryData.getEndLabel()).pc = 2;
/* 189 */           for (enumeration = (Enumeration)tryData.catches.elements(); enumeration.hasMoreElements(); ) {
/* 190 */             CatchData catchData = (CatchData)enumeration.nextElement();
/* 191 */             optimize(paramEnvironment, catchData.getLabel());
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean eliminate() {
/* 203 */     boolean bool = false;
/* 204 */     Instruction instruction1 = this.first;
/*     */     
/* 206 */     for (Instruction instruction2 = this.first.next; instruction2 != null; instruction2 = instruction2.next) {
/* 207 */       if (instruction2.pc != 0) {
/* 208 */         instruction1.next = instruction2;
/* 209 */         instruction1 = instruction2;
/* 210 */         instruction2.pc = 0;
/*     */       } else {
/* 212 */         bool = true;
/*     */       } 
/*     */     } 
/* 215 */     this.first.pc = 0;
/* 216 */     instruction1.next = null;
/* 217 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void optimize(Environment paramEnvironment) {
/*     */     do {
/* 227 */       optimize(paramEnvironment, this.first);
/*     */     
/*     */     }
/* 230 */     while (eliminate() && paramEnvironment.opt());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void collect(Environment paramEnvironment, MemberDefinition paramMemberDefinition, ConstantPool paramConstantPool) {
/* 239 */     if (paramMemberDefinition != null && paramEnvironment.debug_vars()) {
/*     */       
/* 241 */       Vector vector = paramMemberDefinition.getArguments();
/* 242 */       if (vector != null) {
/* 243 */         for (Enumeration<MemberDefinition> enumeration = vector.elements(); enumeration.hasMoreElements(); ) {
/* 244 */           MemberDefinition memberDefinition = enumeration.nextElement();
/* 245 */           paramConstantPool.put(memberDefinition.getName().toString());
/* 246 */           paramConstantPool.put(memberDefinition.getType().getTypeSignature());
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 252 */     for (Label label = this.first; label != null; instruction = label.next) {
/* 253 */       Instruction instruction; label.collect(paramConstantPool);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void balance(Label paramLabel, int paramInt) {
/* 261 */     for (Label label = paramLabel; label != null; instruction = label.next) {
/*     */       Instruction instruction; int i; SwitchData switchData; TryData tryData;
/*     */       Enumeration<Label> enumeration;
/* 264 */       paramInt += label.balance();
/* 265 */       if (paramInt < 0) {
/* 266 */         throw new CompilerError("stack under flow: " + label.toString() + " = " + paramInt);
/*     */       }
/* 268 */       if (paramInt > this.maxdepth) {
/* 269 */         this.maxdepth = paramInt;
/*     */       }
/* 271 */       switch (label.opc) {
/*     */         case -1:
/* 273 */           paramLabel = label;
/* 274 */           if (label.pc == 1) {
/* 275 */             if (paramLabel.depth != paramInt) {
/* 276 */               throw new CompilerError("stack depth error " + paramInt + "/" + paramLabel.depth + ": " + label
/*     */                   
/* 278 */                   .toString());
/*     */             }
/*     */             return;
/*     */           } 
/* 282 */           paramLabel.pc = 1;
/* 283 */           paramLabel.depth = paramInt;
/*     */           break;
/*     */         
/*     */         case 153:
/*     */         case 154:
/*     */         case 155:
/*     */         case 156:
/*     */         case 157:
/*     */         case 158:
/*     */         case 159:
/*     */         case 160:
/*     */         case 161:
/*     */         case 162:
/*     */         case 163:
/*     */         case 164:
/*     */         case 165:
/*     */         case 166:
/*     */         case 198:
/*     */         case 199:
/* 302 */           balance((Label)label.value, paramInt);
/*     */           break;
/*     */         
/*     */         case 167:
/* 306 */           balance((Label)label.value, paramInt);
/*     */           return;
/*     */         
/*     */         case 168:
/* 310 */           balance((Label)label.value, paramInt + 1);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 169:
/*     */         case 172:
/*     */         case 173:
/*     */         case 174:
/*     */         case 175:
/*     */         case 176:
/*     */         case 177:
/*     */         case 191:
/*     */           return;
/*     */         
/*     */         case 21:
/*     */         case 23:
/*     */         case 25:
/*     */         case 54:
/*     */         case 56:
/*     */         case 58:
/* 330 */           i = ((label.value instanceof Number) ? ((Number)label.value).intValue() : ((LocalVariable)label.value).slot) + 1;
/*     */           
/* 332 */           if (i > this.maxvar) {
/* 333 */             this.maxvar = i;
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case 22:
/*     */         case 24:
/*     */         case 55:
/*     */         case 57:
/* 342 */           i = ((label.value instanceof Number) ? ((Number)label.value).intValue() : ((LocalVariable)label.value).slot) + 2;
/*     */           
/* 344 */           if (i > this.maxvar) {
/* 345 */             this.maxvar = i;
/*     */           }
/*     */           break;
/*     */         
/*     */         case 132:
/* 350 */           i = ((int[])label.value)[0] + 1;
/* 351 */           if (i > this.maxvar) {
/* 352 */             this.maxvar = i + 1;
/*     */           }
/*     */           break;
/*     */         
/*     */         case 170:
/*     */         case 171:
/* 358 */           switchData = (SwitchData)label.value;
/* 359 */           balance(switchData.defaultLabel, paramInt);
/* 360 */           for (enumeration = switchData.tab.elements(); enumeration.hasMoreElements();) {
/* 361 */             balance(enumeration.nextElement(), paramInt);
/*     */           }
/*     */           return;
/*     */ 
/*     */         
/*     */         case -3:
/* 367 */           tryData = (TryData)label.value;
/* 368 */           for (enumeration = (Enumeration)tryData.catches.elements(); enumeration.hasMoreElements(); ) {
/* 369 */             CatchData catchData = (CatchData)enumeration.nextElement();
/* 370 */             balance(catchData.getLabel(), paramInt + 1);
/*     */           } 
/*     */           break;
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
/*     */   public void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, MemberDefinition paramMemberDefinition, ConstantPool paramConstantPool) throws IOException {
/* 386 */     if (paramMemberDefinition != null && paramMemberDefinition.getArguments() != null) {
/* 387 */       int k = 0;
/*     */       
/* 389 */       Vector vector = paramMemberDefinition.getArguments();
/* 390 */       for (Enumeration<MemberDefinition> enumeration = vector.elements(); enumeration.hasMoreElements(); ) {
/* 391 */         MemberDefinition memberDefinition = enumeration.nextElement();
/* 392 */         k += memberDefinition.getType().stackSize();
/*     */       } 
/* 394 */       this.maxvar = k;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 399 */       balance(this.first, 0);
/* 400 */     } catch (CompilerError compilerError) {
/* 401 */       System.out.println("ERROR: " + compilerError);
/* 402 */       listing(System.out);
/* 403 */       throw compilerError;
/*     */     } 
/*     */ 
/*     */     
/* 407 */     int i = 0, j = 0;
/* 408 */     for (Label label = this.first; label != null; instruction1 = label.next) {
/* 409 */       Instruction instruction1; label.pc = i;
/* 410 */       int k = label.size(paramConstantPool);
/* 411 */       if (i < 65536 && i + k >= 65536) {
/* 412 */         paramEnvironment.error(label.where, "warn.method.too.long");
/*     */       }
/* 414 */       i += k;
/*     */       
/* 416 */       if (label.opc == -3) {
/* 417 */         j += ((TryData)label.value).catches.size();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 422 */     paramDataOutputStream.writeShort(this.maxdepth);
/* 423 */     paramDataOutputStream.writeShort(this.maxvar);
/* 424 */     paramDataOutputStream.writeInt(this.maxpc = i);
/*     */ 
/*     */     
/* 427 */     for (Instruction instruction = this.first.next; instruction != null; instruction = instruction.next) {
/* 428 */       instruction.write(paramDataOutputStream, paramConstantPool);
/*     */     }
/*     */ 
/*     */     
/* 432 */     paramDataOutputStream.writeShort(j);
/* 433 */     if (j > 0)
/*     */     {
/* 435 */       writeExceptions(paramEnvironment, paramDataOutputStream, paramConstantPool, this.first, this.last);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void writeExceptions(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool, Instruction paramInstruction1, Instruction paramInstruction2) throws IOException {
/* 443 */     for (Instruction instruction = paramInstruction1; instruction != paramInstruction2.next; instruction = instruction.next) {
/* 444 */       if (instruction.opc == -3) {
/* 445 */         TryData tryData = (TryData)instruction.value;
/* 446 */         writeExceptions(paramEnvironment, paramDataOutputStream, paramConstantPool, instruction.next, tryData.getEndLabel());
/* 447 */         for (Enumeration<CatchData> enumeration = tryData.catches.elements(); enumeration.hasMoreElements(); ) {
/* 448 */           CatchData catchData = enumeration.nextElement();
/*     */           
/* 450 */           paramDataOutputStream.writeShort(instruction.pc);
/* 451 */           paramDataOutputStream.writeShort((tryData.getEndLabel()).pc);
/* 452 */           paramDataOutputStream.writeShort((catchData.getLabel()).pc);
/* 453 */           if (catchData.getType() != null) {
/* 454 */             paramDataOutputStream.writeShort(paramConstantPool.index(catchData.getType())); continue;
/*     */           } 
/* 456 */           paramDataOutputStream.writeShort(0);
/*     */         } 
/*     */         
/* 459 */         instruction = tryData.getEndLabel();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeCoverageTable(Environment paramEnvironment, ClassDefinition paramClassDefinition, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool, long paramLong) throws IOException {
/* 469 */     Vector<Cover> vector = new Vector();
/* 470 */     boolean bool1 = false;
/* 471 */     boolean bool2 = false;
/*     */     
/* 473 */     long l = ((SourceClass)paramClassDefinition).getWhere();
/* 474 */     Vector<Long> vector1 = new Vector();
/* 475 */     boolean bool3 = false;
/* 476 */     byte b1 = 0;
/*     */     
/* 478 */     for (Label label = this.first; label != null; instruction = label.next) {
/* 479 */       Instruction instruction; SwitchData switchData; int i; Enumeration<Integer> enumeration; long l1 = label.where >> 32L;
/* 480 */       if (l1 > 0L && label.opc != -1) {
/* 481 */         if (!bool2) {
/* 482 */           if (l == label.where) {
/* 483 */             vector.addElement(new Cover(2, paramLong, label.pc));
/*     */           } else {
/* 485 */             vector.addElement(new Cover(1, paramLong, label.pc));
/* 486 */           }  b1++;
/* 487 */           bool2 = true;
/*     */         } 
/* 489 */         if (!bool1 && !label.flagNoCovered) {
/* 490 */           boolean bool = false;
/* 491 */           for (Enumeration<Long> enumeration1 = vector1.elements(); enumeration1.hasMoreElements();) {
/* 492 */             if (((Long)enumeration1.nextElement()).longValue() == label.where) {
/* 493 */               bool = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 497 */           if (!bool) {
/* 498 */             vector.addElement(new Cover(3, label.where, label.pc));
/* 499 */             b1++;
/* 500 */             bool1 = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 504 */       switch (label.opc) {
/*     */         case -1:
/* 506 */           bool1 = false;
/*     */           break;
/*     */         case 153:
/*     */         case 154:
/*     */         case 155:
/*     */         case 156:
/*     */         case 157:
/*     */         case 158:
/*     */         case 159:
/*     */         case 160:
/*     */         case 161:
/*     */         case 162:
/*     */         case 163:
/*     */         case 164:
/*     */         case 165:
/*     */         case 166:
/*     */         case 198:
/*     */         case 199:
/* 524 */           if (label.flagCondInverted) {
/* 525 */             vector.addElement(new Cover(7, label.where, label.pc));
/* 526 */             vector.addElement(new Cover(8, label.where, label.pc));
/*     */           } else {
/* 528 */             vector.addElement(new Cover(8, label.where, label.pc));
/* 529 */             vector.addElement(new Cover(7, label.where, label.pc));
/*     */           } 
/* 531 */           b1 += 2;
/* 532 */           bool1 = false;
/*     */           break;
/*     */ 
/*     */         
/*     */         case 167:
/* 537 */           bool1 = false;
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case -3:
/* 553 */           vector1.addElement(Long.valueOf(label.where));
/* 554 */           bool1 = false;
/*     */           break;
/*     */ 
/*     */         
/*     */         case 170:
/* 559 */           switchData = (SwitchData)label.value;
/* 560 */           for (i = switchData.minValue; i <= switchData.maxValue; i++) {
/* 561 */             vector.addElement(new Cover(5, switchData.whereCase(new Integer(i)), label.pc));
/* 562 */             b1++;
/*     */           } 
/* 564 */           if (!switchData.getDefault()) {
/* 565 */             vector.addElement(new Cover(6, label.where, label.pc));
/* 566 */             b1++;
/*     */           } else {
/* 568 */             vector.addElement(new Cover(5, switchData.whereCase("default"), label.pc));
/* 569 */             b1++;
/*     */           } 
/* 571 */           bool1 = false;
/*     */           break;
/*     */         
/*     */         case 171:
/* 575 */           switchData = (SwitchData)label.value;
/* 576 */           for (enumeration = switchData.sortedKeys(); enumeration.hasMoreElements(); ) {
/* 577 */             Integer integer = enumeration.nextElement();
/* 578 */             vector.addElement(new Cover(5, switchData.whereCase(integer), label.pc));
/* 579 */             b1++;
/*     */           } 
/* 581 */           if (!switchData.getDefault()) {
/* 582 */             vector.addElement(new Cover(6, label.where, label.pc));
/* 583 */             b1++;
/*     */           } else {
/* 585 */             vector.addElement(new Cover(5, switchData.whereCase("default"), label.pc));
/* 586 */             b1++;
/*     */           } 
/* 588 */           bool1 = false;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 596 */     paramDataOutputStream.writeShort(b1);
/* 597 */     for (byte b2 = 0; b2 < b1; b2++) {
/* 598 */       Cover cover = vector.elementAt(b2);
/* 599 */       long l1 = cover.Addr >> 32L;
/* 600 */       long l2 = cover.Addr << 32L >> 32L;
/* 601 */       paramDataOutputStream.writeShort(cover.NumCommand);
/* 602 */       paramDataOutputStream.writeShort(cover.Type);
/* 603 */       paramDataOutputStream.writeInt((int)l1);
/* 604 */       paramDataOutputStream.writeInt((int)l2);
/*     */       
/* 606 */       if (cover.Type != 5 || cover.Addr != 0L) {
/* 607 */         JcovClassCountArray[cover.Type] = JcovClassCountArray[cover.Type] + 1;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNativeToJcovTab(Environment paramEnvironment, ClassDefinition paramClassDefinition) {
/* 618 */     JcovClassCountArray[1] = JcovClassCountArray[1] + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String createClassJcovElement(Environment paramEnvironment, ClassDefinition paramClassDefinition) {
/* 626 */     String str1 = Type.mangleInnerType(paramClassDefinition.getClassDeclaration().getName()).toString();
/*     */ 
/*     */ 
/*     */     
/* 630 */     SourceClassList.addElement(str1);
/* 631 */     String str2 = str1.replace('.', '/');
/* 632 */     String str3 = JcovClassLine + str2;
/*     */     
/* 634 */     str3 = str3 + " [";
/* 635 */     String str4 = "";
/*     */     
/* 637 */     for (byte b = 0; b < arrayModifiers.length; b++) {
/* 638 */       if ((paramClassDefinition.getModifiers() & arrayModifiers[b]) != 0) {
/* 639 */         str3 = str3 + str4 + opNames[arrayModifiersOpc[b]];
/* 640 */         str4 = " ";
/*     */       } 
/*     */     } 
/* 643 */     str3 = str3 + "]";
/*     */     
/* 645 */     return str3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void GenVecJCov(Environment paramEnvironment, ClassDefinition paramClassDefinition, long paramLong) {
/* 654 */     String str = ((SourceClass)paramClassDefinition).getAbsoluteName();
/*     */     
/* 656 */     TmpCovTable.addElement(createClassJcovElement(paramEnvironment, paramClassDefinition));
/* 657 */     TmpCovTable.addElement(JcovSrcfileLine + str);
/* 658 */     TmpCovTable.addElement(JcovTimestampLine + paramLong);
/* 659 */     TmpCovTable.addElement(JcovDataLine + "A");
/* 660 */     TmpCovTable.addElement(JcovHeadingLine);
/*     */     
/* 662 */     for (byte b = 1; b <= 8; b++) {
/* 663 */       if (JcovClassCountArray[b] != 0) {
/* 664 */         TmpCovTable.addElement(new String(b + "\t" + JcovClassCountArray[b]));
/* 665 */         JcovClassCountArray[b] = 0;
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
/*     */   public void GenJCov(Environment paramEnvironment) {
/*     */     try {
/* 679 */       File file = paramEnvironment.getcovFile();
/* 680 */       if (file.exists()) {
/* 681 */         DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
/*     */ 
/*     */         
/* 684 */         String str = null;
/* 685 */         boolean bool = true;
/*     */ 
/*     */         
/* 688 */         str = dataInputStream.readLine();
/* 689 */         if (str != null && str.startsWith(JcovMagicLine))
/*     */         {
/*     */           
/* 692 */           while ((str = dataInputStream.readLine()) != null) {
/* 693 */             if (str.startsWith(JcovClassLine)) {
/* 694 */               bool = true;
/* 695 */               for (Enumeration<String> enumeration1 = SourceClassList.elements(); enumeration1.hasMoreElements(); ) {
/* 696 */                 String str2 = str.substring(JcovClassLine.length());
/* 697 */                 int i = str2.indexOf(' ');
/*     */                 
/* 699 */                 if (i != -1) {
/* 700 */                   str2 = str2.substring(0, i);
/*     */                 }
/* 702 */                 String str1 = enumeration1.nextElement();
/* 703 */                 if (str1.compareTo(str2) == 0) {
/* 704 */                   bool = false;
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/* 709 */             if (bool)
/* 710 */               TmpCovTable.addElement(str); 
/*     */           } 
/*     */         }
/* 713 */         dataInputStream.close();
/*     */       } 
/* 715 */       PrintStream printStream = new PrintStream(new DataOutputStream(new FileOutputStream(file)));
/* 716 */       printStream.println(JcovMagicLine);
/* 717 */       for (Enumeration<String> enumeration = TmpCovTable.elements(); enumeration.hasMoreElements();) {
/* 718 */         printStream.println(enumeration.nextElement());
/*     */       }
/* 720 */       printStream.close();
/*     */     }
/* 722 */     catch (FileNotFoundException fileNotFoundException) {
/* 723 */       System.out.println("ERROR: " + fileNotFoundException);
/*     */     }
/* 725 */     catch (IOException iOException) {
/* 726 */       System.out.println("ERROR: " + iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLineNumberTable(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException {
/* 736 */     long l = -1L;
/* 737 */     byte b = 0;
/*     */     Label label;
/* 739 */     for (label = this.first; label != null; instruction = label.next) {
/* 740 */       Instruction instruction; long l1 = label.where >> 32L;
/* 741 */       if (l1 > 0L && l != l1) {
/* 742 */         l = l1;
/* 743 */         b++;
/*     */       } 
/*     */     } 
/*     */     
/* 747 */     l = -1L;
/* 748 */     paramDataOutputStream.writeShort(b);
/* 749 */     for (label = this.first; label != null; instruction = label.next) {
/* 750 */       Instruction instruction; long l1 = label.where >> 32L;
/* 751 */       if (l1 > 0L && l != l1) {
/* 752 */         l = l1;
/* 753 */         paramDataOutputStream.writeShort(label.pc);
/* 754 */         paramDataOutputStream.writeShort((int)l);
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
/*     */   void flowFields(Environment paramEnvironment, Label paramLabel, MemberDefinition[] paramArrayOfMemberDefinition) {
/* 766 */     if (paramLabel.locals != null) {
/*     */       
/* 768 */       MemberDefinition[] arrayOfMemberDefinition1 = paramLabel.locals;
/* 769 */       for (byte b = 0; b < this.maxvar; b++) {
/* 770 */         if (arrayOfMemberDefinition1[b] != paramArrayOfMemberDefinition[b]) {
/* 771 */           arrayOfMemberDefinition1[b] = null;
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 778 */     paramLabel.locals = new MemberDefinition[this.maxvar];
/* 779 */     System.arraycopy(paramArrayOfMemberDefinition, 0, paramLabel.locals, 0, this.maxvar);
/*     */     
/* 781 */     MemberDefinition[] arrayOfMemberDefinition = new MemberDefinition[this.maxvar];
/* 782 */     System.arraycopy(paramArrayOfMemberDefinition, 0, arrayOfMemberDefinition, 0, this.maxvar);
/* 783 */     paramArrayOfMemberDefinition = arrayOfMemberDefinition;
/*     */     
/* 785 */     for (Instruction instruction = paramLabel.next; instruction != null; instruction = instruction.next) {
/* 786 */       SwitchData switchData; Vector<CatchData> vector; Enumeration<Label> enumeration; switch (instruction.opc) { case 54: case 55: case 56: case 57: case 58: case 59: case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 68:
/*     */         case 69:
/*     */         case 70:
/*     */         case 71:
/*     */         case 72:
/*     */         case 73:
/*     */         case 74:
/*     */         case 75:
/*     */         case 76:
/*     */         case 77:
/*     */         case 78:
/* 797 */           if (instruction.value instanceof LocalVariable) {
/* 798 */             LocalVariable localVariable = (LocalVariable)instruction.value;
/* 799 */             paramArrayOfMemberDefinition[localVariable.slot] = localVariable.field;
/*     */           } 
/*     */           break;
/*     */         
/*     */         case -1:
/* 804 */           flowFields(paramEnvironment, (Label)instruction, paramArrayOfMemberDefinition); return;
/*     */         case 153: case 154: case 155: case 156: case 157: case 158: case 159: case 160: case 161:
/*     */         case 162:
/*     */         case 163:
/*     */         case 164:
/*     */         case 165:
/*     */         case 166:
/*     */         case 168:
/*     */         case 198:
/*     */         case 199:
/* 814 */           flowFields(paramEnvironment, (Label)instruction.value, paramArrayOfMemberDefinition);
/*     */           break;
/*     */         
/*     */         case 167:
/* 818 */           flowFields(paramEnvironment, (Label)instruction.value, paramArrayOfMemberDefinition); return;
/*     */         case 169: case 172: case 173:
/*     */         case 174:
/*     */         case 175:
/*     */         case 176:
/*     */         case 177:
/*     */         case 191:
/*     */           return;
/*     */         case 170:
/*     */         case 171:
/* 828 */           switchData = (SwitchData)instruction.value;
/* 829 */           flowFields(paramEnvironment, switchData.defaultLabel, paramArrayOfMemberDefinition);
/* 830 */           for (enumeration = switchData.tab.elements(); enumeration.hasMoreElements();) {
/* 831 */             flowFields(paramEnvironment, enumeration.nextElement(), paramArrayOfMemberDefinition);
/*     */           }
/*     */           return;
/*     */ 
/*     */         
/*     */         case -3:
/* 837 */           vector = ((TryData)instruction.value).catches;
/* 838 */           for (enumeration = (Enumeration)vector.elements(); enumeration.hasMoreElements(); ) {
/* 839 */             CatchData catchData = (CatchData)enumeration.nextElement();
/* 840 */             flowFields(paramEnvironment, catchData.getLabel(), paramArrayOfMemberDefinition);
/*     */           } 
/*     */           break; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLocalVariableTable(Environment paramEnvironment, MemberDefinition paramMemberDefinition, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool) throws IOException {
/* 854 */     MemberDefinition[] arrayOfMemberDefinition = new MemberDefinition[this.maxvar];
/* 855 */     byte b = 0;
/*     */ 
/*     */     
/* 858 */     if (paramMemberDefinition != null && paramMemberDefinition.getArguments() != null) {
/* 859 */       int i = 0;
/*     */       
/* 861 */       Vector vector = paramMemberDefinition.getArguments();
/* 862 */       for (Enumeration<MemberDefinition> enumeration = vector.elements(); enumeration.hasMoreElements(); ) {
/* 863 */         MemberDefinition memberDefinition = enumeration.nextElement();
/* 864 */         arrayOfMemberDefinition[i] = memberDefinition;
/* 865 */         i += memberDefinition.getType().stackSize();
/*     */       } 
/*     */     } 
/*     */     
/* 869 */     flowFields(paramEnvironment, this.first, arrayOfMemberDefinition);
/* 870 */     LocalVariableTable localVariableTable = new LocalVariableTable();
/*     */ 
/*     */     
/* 873 */     for (b = 0; b < this.maxvar; b++)
/* 874 */       arrayOfMemberDefinition[b] = null; 
/* 875 */     if (paramMemberDefinition != null && paramMemberDefinition.getArguments() != null) {
/* 876 */       int i = 0;
/*     */       
/* 878 */       Vector vector = paramMemberDefinition.getArguments();
/* 879 */       for (Enumeration<MemberDefinition> enumeration = vector.elements(); enumeration.hasMoreElements(); ) {
/* 880 */         MemberDefinition memberDefinition = enumeration.nextElement();
/* 881 */         arrayOfMemberDefinition[i] = memberDefinition;
/* 882 */         localVariableTable.define(memberDefinition, i, 0, this.maxpc);
/* 883 */         i += memberDefinition.getType().stackSize();
/*     */       } 
/*     */     } 
/*     */     
/* 887 */     int[] arrayOfInt = new int[this.maxvar];
/*     */     
/* 889 */     for (Label label = this.first; label != null; instruction = label.next) {
/* 890 */       Instruction instruction; int i; MemberDefinition[] arrayOfMemberDefinition1; switch (label.opc) { case 54: case 55: case 56: case 57: case 58: case 59: case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 68:
/*     */         case 69:
/*     */         case 70:
/*     */         case 71:
/*     */         case 72:
/*     */         case 73:
/*     */         case 74:
/*     */         case 75:
/*     */         case 76:
/*     */         case 77:
/*     */         case 78:
/* 901 */           if (label.value instanceof LocalVariable) {
/* 902 */             LocalVariable localVariable = (LocalVariable)label.value;
/* 903 */             int j = (label.next != null) ? label.next.pc : label.pc;
/* 904 */             if (arrayOfMemberDefinition[localVariable.slot] != null) {
/* 905 */               localVariableTable.define(arrayOfMemberDefinition[localVariable.slot], localVariable.slot, arrayOfInt[localVariable.slot], j);
/*     */             }
/* 907 */             arrayOfInt[localVariable.slot] = j;
/* 908 */             arrayOfMemberDefinition[localVariable.slot] = localVariable.field;
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case -1:
/* 914 */           for (b = 0; b < this.maxvar; b++) {
/* 915 */             if (arrayOfMemberDefinition[b] != null) {
/* 916 */               localVariableTable.define(arrayOfMemberDefinition[b], b, arrayOfInt[b], label.pc);
/*     */             }
/*     */           } 
/*     */           
/* 920 */           i = label.pc;
/* 921 */           arrayOfMemberDefinition1 = label.locals;
/* 922 */           if (arrayOfMemberDefinition1 == null) {
/* 923 */             for (b = 0; b < this.maxvar; b++)
/* 924 */               arrayOfMemberDefinition[b] = null; 
/*     */           } else {
/* 926 */             System.arraycopy(arrayOfMemberDefinition1, 0, arrayOfMemberDefinition, 0, this.maxvar);
/*     */           } 
/* 928 */           for (b = 0; b < this.maxvar; b++) {
/* 929 */             arrayOfInt[b] = i;
/*     */           }
/*     */           break; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 937 */     for (b = 0; b < this.maxvar; b++) {
/* 938 */       if (arrayOfMemberDefinition[b] != null) {
/* 939 */         localVariableTable.define(arrayOfMemberDefinition[b], b, arrayOfInt[b], this.maxpc);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 944 */     localVariableTable.write(paramEnvironment, paramDataOutputStream, paramConstantPool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean empty() {
/* 951 */     return (this.first == this.last);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listing(PrintStream paramPrintStream) {
/* 958 */     paramPrintStream.println("-- listing --");
/* 959 */     for (Label label = this.first; label != null; instruction = label.next) {
/* 960 */       Instruction instruction; paramPrintStream.println(label.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\Assembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */