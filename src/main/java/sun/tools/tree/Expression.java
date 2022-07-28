/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Instruction;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.AmbiguousMember;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Expression
/*     */   extends Node
/*     */ {
/*     */   Type type;
/*     */   
/*     */   Expression(int paramInt, long paramLong, Type paramType) {
/*  47 */     super(paramInt, paramLong);
/*  48 */     this.type = paramType;
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
/*     */   public Expression getImplementation() {
/*  63 */     return this;
/*     */   }
/*     */   
/*     */   public Type getType() {
/*  67 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int precedence() {
/*  74 */     return (this.op < opPrecedence.length) ? opPrecedence[this.op] : 100;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression order() {
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue() {
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(int paramInt) {
/* 106 */     return false;
/*     */   }
/*     */   public boolean equals(boolean paramBoolean) {
/* 109 */     return false;
/*     */   }
/*     */   public boolean equals(Identifier paramIdentifier) {
/* 112 */     return false;
/*     */   }
/*     */   public boolean equals(String paramString) {
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNull() {
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNonNull() {
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equalsDefault() {
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Type toType(Environment paramEnvironment, Context paramContext) {
/* 144 */     paramEnvironment.error(this.where, "invalid.type.expr");
/* 145 */     return Type.tError;
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
/*     */   public boolean fitsType(Environment paramEnvironment, Context paramContext, Type paramType) {
/*     */     try {
/* 170 */       if (paramEnvironment.isMoreSpecific(this.type, paramType)) {
/* 171 */         return true;
/*     */       }
/* 173 */       if (this.type.isType(4) && isConstant() && paramContext != null) {
/*     */         
/* 175 */         Expression expression = inlineValue(paramEnvironment, paramContext);
/* 176 */         if (expression != this && expression instanceof ConstantExpression) {
/* 177 */           return expression.fitsType(paramEnvironment, paramContext, paramType);
/*     */         }
/*     */       } 
/* 180 */       return false;
/* 181 */     } catch (ClassNotFound classNotFound) {
/* 182 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean fitsType(Environment paramEnvironment, Type paramType) {
/* 189 */     return fitsType(paramEnvironment, (Context)null, paramType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 196 */     return paramVset;
/*     */   }
/*     */   public Vset checkInitializer(Environment paramEnvironment, Context paramContext, Vset paramVset, Type paramType, Hashtable paramHashtable) {
/* 199 */     return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 202 */     throw new CompilerError("check failed");
/*     */   }
/*     */ 
/*     */   
/*     */   public Vset checkLHS(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 207 */     paramEnvironment.error(this.where, "invalid.lhs.assignment");
/* 208 */     this.type = Type.tError;
/* 209 */     return paramVset;
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
/*     */   public FieldUpdater getAssigner(Environment paramEnvironment, Context paramContext) {
/* 227 */     throw new CompilerError("getAssigner lhs");
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
/*     */   public FieldUpdater getUpdater(Environment paramEnvironment, Context paramContext) {
/* 242 */     throw new CompilerError("getUpdater lhs");
/*     */   }
/*     */ 
/*     */   
/*     */   public Vset checkAssignOp(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, Expression paramExpression) {
/* 247 */     if (paramExpression instanceof IncDecExpression) {
/* 248 */       paramEnvironment.error(this.where, "invalid.arg", opNames[paramExpression.op]);
/*     */     } else {
/* 250 */       paramEnvironment.error(this.where, "invalid.lhs.assignment");
/* 251 */     }  this.type = Type.tError;
/* 252 */     return paramVset;
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
/*     */   public Vset checkAmbigName(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression) {
/* 271 */     return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConditionVars checkCondition(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 281 */     ConditionVars conditionVars = new ConditionVars();
/* 282 */     checkCondition(paramEnvironment, paramContext, paramVset, paramHashtable, conditionVars);
/* 283 */     return conditionVars;
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
/*     */   public void checkCondition(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, ConditionVars paramConditionVars) {
/* 299 */     paramConditionVars.vsTrue = paramConditionVars.vsFalse = checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     
/* 301 */     paramConditionVars.vsFalse = paramConditionVars.vsFalse.copy();
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
/*     */   Expression eval() {
/* 314 */     return this;
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
/*     */   Expression simplify() {
/* 332 */     return this;
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
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 349 */     return null;
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 352 */     return this;
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
/*     */   protected StringBuffer inlineValueSB(Environment paramEnvironment, Context paramContext, StringBuffer paramStringBuffer) {
/* 372 */     Expression expression = inlineValue(paramEnvironment, paramContext);
/* 373 */     Object object = expression.getValue();
/*     */     
/* 375 */     if (object == null && !expression.isNull())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 387 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 394 */     if (this.type == Type.tChar) {
/* 395 */       paramStringBuffer.append((char)((Integer)object).intValue());
/* 396 */     } else if (this.type == Type.tBoolean) {
/* 397 */       paramStringBuffer.append((((Integer)object).intValue() != 0));
/*     */     } else {
/* 399 */       paramStringBuffer.append(object);
/*     */     } 
/*     */     
/* 402 */     return paramStringBuffer;
/*     */   }
/*     */   
/*     */   public Expression inlineLHS(Environment paramEnvironment, Context paramContext) {
/* 406 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 415 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeBranch(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Label paramLabel, boolean paramBoolean) {
/* 422 */     if (this.type.isType(0)) {
/* 423 */       codeValue(paramEnvironment, paramContext, paramAssembler);
/* 424 */       paramAssembler.add(this.where, paramBoolean ? 154 : 153, paramLabel, paramBoolean);
/*     */     } else {
/* 426 */       throw new CompilerError("codeBranch " + opNames[this.op]);
/*     */     } 
/*     */   }
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 430 */     if (this.type.isType(0)) {
/* 431 */       Label label1 = new Label();
/* 432 */       Label label2 = new Label();
/*     */       
/* 434 */       codeBranch(paramEnvironment, paramContext, paramAssembler, label1, true);
/* 435 */       paramAssembler.add(true, this.where, 18, new Integer(0));
/* 436 */       paramAssembler.add(true, this.where, 167, label2);
/* 437 */       paramAssembler.add((Instruction)label1);
/* 438 */       paramAssembler.add(true, this.where, 18, new Integer(1));
/* 439 */       paramAssembler.add((Instruction)label2);
/*     */     } else {
/* 441 */       throw new CompilerError("codeValue");
/*     */     } 
/*     */   }
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 445 */     codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     
/* 447 */     switch (this.type.getTypeCode()) {
/*     */       case 11:
/*     */         return;
/*     */       
/*     */       case 5:
/*     */       case 7:
/* 453 */         paramAssembler.add(this.where, 88);
/*     */     } 
/*     */ 
/*     */     
/* 457 */     paramAssembler.add(this.where, 87);
/*     */   }
/*     */ 
/*     */   
/*     */   int codeLValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 462 */     print(System.out);
/* 463 */     throw new CompilerError("invalid lhs");
/*     */   }
/*     */   void codeLoad(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 466 */     print(System.out);
/* 467 */     throw new CompilerError("invalid load");
/*     */   }
/*     */   void codeStore(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 470 */     print(System.out);
/* 471 */     throw new CompilerError("invalid store");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void ensureString(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) throws ClassNotFound, AmbiguousMember {
/* 480 */     if (this.type == Type.tString && isNonNull()) {
/*     */       return;
/*     */     }
/*     */     
/* 484 */     ClassDefinition classDefinition1 = paramContext.field.getClassDefinition();
/* 485 */     ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(Type.tString);
/* 486 */     ClassDefinition classDefinition2 = classDeclaration.getClassDefinition(paramEnvironment);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 494 */     if (this.type.inMask(1792)) {
/*     */       
/* 496 */       if (this.type != Type.tString) {
/*     */ 
/*     */ 
/*     */         
/* 500 */         Type[] arrayOfType = { Type.tObject };
/*     */         
/* 502 */         MemberDefinition memberDefinition = classDefinition2.matchMethod(paramEnvironment, classDefinition1, idValueOf, arrayOfType);
/* 503 */         paramAssembler.add(this.where, 184, memberDefinition);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 513 */       if (!this.type.inMask(768)) {
/* 514 */         Type[] arrayOfType = { Type.tString };
/*     */         
/* 516 */         MemberDefinition memberDefinition = classDefinition2.matchMethod(paramEnvironment, classDefinition1, idValueOf, arrayOfType);
/* 517 */         paramAssembler.add(this.where, 184, memberDefinition);
/*     */       } 
/*     */     } else {
/*     */       
/* 521 */       Type[] arrayOfType = { this.type };
/*     */       
/* 523 */       MemberDefinition memberDefinition = classDefinition2.matchMethod(paramEnvironment, classDefinition1, idValueOf, arrayOfType);
/* 524 */       paramAssembler.add(this.where, 184, memberDefinition);
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
/*     */   void codeAppend(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, ClassDeclaration paramClassDeclaration, boolean paramBoolean) throws ClassNotFound, AmbiguousMember {
/* 538 */     ClassDefinition classDefinition1 = paramContext.field.getClassDefinition();
/* 539 */     ClassDefinition classDefinition2 = paramClassDeclaration.getClassDefinition(paramEnvironment);
/*     */     
/* 541 */     if (paramBoolean) {
/*     */       MemberDefinition memberDefinition;
/* 543 */       paramAssembler.add(this.where, 187, paramClassDeclaration);
/* 544 */       paramAssembler.add(this.where, 89);
/* 545 */       if (equals("")) {
/*     */         
/* 547 */         memberDefinition = classDefinition2.matchMethod(paramEnvironment, classDefinition1, idInit);
/*     */       } else {
/*     */         
/* 550 */         codeValue(paramEnvironment, paramContext, paramAssembler);
/* 551 */         ensureString(paramEnvironment, paramContext, paramAssembler);
/* 552 */         Type[] arrayOfType = { Type.tString };
/* 553 */         memberDefinition = classDefinition2.matchMethod(paramEnvironment, classDefinition1, idInit, arrayOfType);
/*     */       } 
/* 555 */       paramAssembler.add(this.where, 183, memberDefinition);
/*     */     } else {
/*     */       
/* 558 */       codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 568 */       Type[] arrayOfType = { (this.type.inMask(1792) && this.type != Type.tString) ? Type.tObject : this.type };
/*     */ 
/*     */       
/* 571 */       MemberDefinition memberDefinition = classDefinition2.matchMethod(paramEnvironment, classDefinition1, idAppend, arrayOfType);
/* 572 */       paramAssembler.add(this.where, 182, memberDefinition);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeDup(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, int paramInt1, int paramInt2) {
/* 580 */     switch (paramInt1) {
/*     */       case 0:
/*     */         return;
/*     */       
/*     */       case 1:
/* 585 */         switch (paramInt2) {
/*     */           case 0:
/* 587 */             paramAssembler.add(this.where, 89);
/*     */             return;
/*     */           case 1:
/* 590 */             paramAssembler.add(this.where, 90);
/*     */             return;
/*     */           case 2:
/* 593 */             paramAssembler.add(this.where, 91);
/*     */             return;
/*     */         } 
/*     */         
/*     */         break;
/*     */       case 2:
/* 599 */         switch (paramInt2) {
/*     */           case 0:
/* 601 */             paramAssembler.add(this.where, 92);
/*     */             return;
/*     */           case 1:
/* 604 */             paramAssembler.add(this.where, 93);
/*     */             return;
/*     */           case 2:
/* 607 */             paramAssembler.add(this.where, 94);
/*     */             return;
/*     */         } 
/*     */         
/*     */         break;
/*     */     } 
/* 613 */     throw new CompilerError("can't dup: " + paramInt1 + ", " + paramInt2);
/*     */   }
/*     */   
/*     */   void codeConversion(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, Type paramType1, Type paramType2) {
/* 617 */     int i = paramType1.getTypeCode();
/* 618 */     int j = paramType2.getTypeCode();
/*     */     
/* 620 */     switch (j) {
/*     */       case 0:
/* 622 */         if (i != 0) {
/*     */           break;
/*     */         }
/*     */         return;
/*     */       case 1:
/* 627 */         if (i != 1) {
/* 628 */           codeConversion(paramEnvironment, paramContext, paramAssembler, paramType1, Type.tInt);
/* 629 */           paramAssembler.add(this.where, 145);
/*     */         } 
/*     */         return;
/*     */       case 2:
/* 633 */         if (i != 2) {
/* 634 */           codeConversion(paramEnvironment, paramContext, paramAssembler, paramType1, Type.tInt);
/* 635 */           paramAssembler.add(this.where, 146);
/*     */         } 
/*     */         return;
/*     */       case 3:
/* 639 */         if (i != 3) {
/* 640 */           codeConversion(paramEnvironment, paramContext, paramAssembler, paramType1, Type.tInt);
/* 641 */           paramAssembler.add(this.where, 147);
/*     */         } 
/*     */         return;
/*     */       case 4:
/* 645 */         switch (i) {
/*     */           case 1:
/*     */           case 2:
/*     */           case 3:
/*     */           case 4:
/*     */             return;
/*     */           case 5:
/* 652 */             paramAssembler.add(this.where, 136);
/*     */             return;
/*     */           case 6:
/* 655 */             paramAssembler.add(this.where, 139);
/*     */             return;
/*     */           case 7:
/* 658 */             paramAssembler.add(this.where, 142);
/*     */             return;
/*     */         } 
/*     */         break;
/*     */       case 5:
/* 663 */         switch (i) {
/*     */           case 1:
/*     */           case 2:
/*     */           case 3:
/*     */           case 4:
/* 668 */             paramAssembler.add(this.where, 133);
/*     */             return;
/*     */           case 5:
/*     */             return;
/*     */           case 6:
/* 673 */             paramAssembler.add(this.where, 140);
/*     */             return;
/*     */           case 7:
/* 676 */             paramAssembler.add(this.where, 143);
/*     */             return;
/*     */         } 
/*     */         break;
/*     */       case 6:
/* 681 */         switch (i) {
/*     */           case 1:
/*     */           case 2:
/*     */           case 3:
/*     */           case 4:
/* 686 */             paramAssembler.add(this.where, 134);
/*     */             return;
/*     */           case 5:
/* 689 */             paramAssembler.add(this.where, 137);
/*     */             return;
/*     */           case 6:
/*     */             return;
/*     */           case 7:
/* 694 */             paramAssembler.add(this.where, 144);
/*     */             return;
/*     */         } 
/*     */         break;
/*     */       case 7:
/* 699 */         switch (i) {
/*     */           case 1:
/*     */           case 2:
/*     */           case 3:
/*     */           case 4:
/* 704 */             paramAssembler.add(this.where, 135);
/*     */             return;
/*     */           case 5:
/* 707 */             paramAssembler.add(this.where, 138);
/*     */             return;
/*     */           case 6:
/* 710 */             paramAssembler.add(this.where, 141);
/*     */             return;
/*     */           
/*     */           case 7:
/*     */             return;
/*     */         } 
/*     */         break;
/*     */       case 10:
/* 718 */         switch (i) {
/*     */           case 8:
/*     */             return;
/*     */           case 9:
/*     */           case 10:
/*     */             try {
/* 724 */               if (!paramEnvironment.implicitCast(paramType1, paramType2)) {
/* 725 */                 paramAssembler.add(this.where, 192, paramEnvironment.getClassDeclaration(paramType2));
/*     */               }
/* 727 */             } catch (ClassNotFound classNotFound) {
/* 728 */               throw new CompilerError(classNotFound);
/*     */             } 
/*     */             return;
/*     */         } 
/*     */         
/*     */         break;
/*     */       
/*     */       case 9:
/* 736 */         switch (i) {
/*     */           case 8:
/*     */             return;
/*     */           case 9:
/*     */           case 10:
/*     */             try {
/* 742 */               if (!paramEnvironment.implicitCast(paramType1, paramType2)) {
/* 743 */                 paramAssembler.add(this.where, 192, paramType2);
/*     */               }
/*     */               return;
/* 746 */             } catch (ClassNotFound classNotFound) {
/* 747 */               throw new CompilerError(classNotFound);
/*     */             } 
/*     */         } 
/*     */         break;
/*     */     } 
/* 752 */     throw new CompilerError("codeConversion: " + i + ", " + j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression firstConstructor() {
/* 759 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/* 766 */     return (Expression)clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 773 */     paramPrintStream.print(opNames[this.op]);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\Expression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */