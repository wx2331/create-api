/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.AmbiguousClass;
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
/*     */ public class NewInstanceExpression
/*     */   extends NaryExpression
/*     */ {
/*     */   MemberDefinition field;
/*     */   Expression outerArg;
/*     */   ClassDefinition body;
/*  44 */   MemberDefinition implMethod = null;
/*     */   
/*     */   final int MAXINLINECOST;
/*     */ 
/*     */   
/*     */   public NewInstanceExpression(long paramLong, Expression paramExpression, Expression[] paramArrayOfExpression) {
/*  50 */     super(42, paramLong, Type.tError, paramExpression, paramArrayOfExpression);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 376 */     this.MAXINLINECOST = Statement.MAXINLINECOST;
/*     */   }
/*     */   public NewInstanceExpression(long paramLong, Expression paramExpression1, Expression[] paramArrayOfExpression, Expression paramExpression2, ClassDefinition paramClassDefinition) { this(paramLong, paramExpression1, paramArrayOfExpression); this.outerArg = paramExpression2; this.body = paramClassDefinition; }
/* 379 */   public Expression getOuterArg() { return this.outerArg; } int precedence() { return 100; } public Expression copyInline(Context paramContext) { NewInstanceExpression newInstanceExpression = (NewInstanceExpression)super.copyInline(paramContext);
/* 380 */     if (this.outerArg != null) {
/* 381 */       newInstanceExpression.outerArg = this.outerArg.copyInline(paramContext);
/*     */     }
/* 383 */     return newInstanceExpression; }
/*     */   public Expression order() { if (this.outerArg != null && opPrecedence[46] > this.outerArg.precedence()) { UnaryExpression unaryExpression = (UnaryExpression)this.outerArg; this.outerArg = unaryExpression.right; unaryExpression.right = order(); return unaryExpression; }  return this; }
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable<ClassDeclaration, NewInstanceExpression> paramHashtable) { ClassDefinition classDefinition = null; Expression expression = null; try { if (this.outerArg != null) { paramVset = this.outerArg.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); expression = this.outerArg; Identifier identifier = FieldExpression.toIdentifier(this.right); if (identifier != null && identifier.isQualified()) paramEnvironment.error(this.where, "unqualified.name.required", identifier);  if (identifier == null || !this.outerArg.type.isType(10)) { if (!this.outerArg.type.isType(13)) paramEnvironment.error(this.where, "invalid.field.reference", idNew, this.outerArg.type);  this.outerArg = null; } else { ClassDefinition classDefinition1 = paramEnvironment.getClassDefinition(this.outerArg.type); Identifier identifier1 = classDefinition1.resolveInnerClass(paramEnvironment, identifier); this.right = new TypeExpression(this.right.where, Type.tClass(identifier1)); paramEnvironment.resolve(this.right.where, paramContext.field.getClassDefinition(), this.right.type); }  }  if (!(this.right instanceof TypeExpression)) this.right = new TypeExpression(this.right.where, this.right.toType(paramEnvironment, paramContext));  if (this.right.type.isType(10)) classDefinition = paramEnvironment.getClassDefinition(this.right.type);  } catch (AmbiguousClass ambiguousClass) { paramEnvironment.error(this.where, "ambig.class", ambiguousClass.name1, ambiguousClass.name2); } catch (ClassNotFound classNotFound) { paramEnvironment.error(this.where, "class.not.found", classNotFound.name, paramContext.field); }  Type type = this.right.type; boolean bool = type.isType(13); if (!type.isType(10) && !bool) { paramEnvironment.error(this.where, "invalid.arg.type", type, opNames[this.op]); bool = true; }  if (classDefinition == null) { this.type = Type.tError; return paramVset; }  Expression[] arrayOfExpression = this.args; arrayOfExpression = insertOuterLink(paramEnvironment, paramContext, this.where, classDefinition, this.outerArg, arrayOfExpression); if (arrayOfExpression.length > this.args.length) { this.outerArg = arrayOfExpression[0]; } else if (this.outerArg != null) { this.outerArg = new CommaExpression(this.outerArg.where, this.outerArg, null); }  Type[] arrayOfType = new Type[arrayOfExpression.length]; byte b1; for (b1 = 0; b1 < arrayOfExpression.length; b1++) { if (arrayOfExpression[b1] != expression) paramVset = arrayOfExpression[b1].checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);  arrayOfType[b1] = (arrayOfExpression[b1]).type; bool = (bool || arrayOfType[b1].isType(13)); }  try { if (bool) { this.type = Type.tError; return paramVset; }  ClassDefinition classDefinition1 = paramContext.field.getClassDefinition(); ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(type); if (this.body != null) { Identifier identifier = classDefinition1.getName().getQualifier(); ClassDefinition classDefinition2 = null; if (classDefinition.isInterface()) { classDefinition2 = paramEnvironment.getClassDefinition(idJavaLangObject); } else { classDefinition2 = classDefinition; }  MemberDefinition memberDefinition = classDefinition2.matchAnonConstructor(paramEnvironment, identifier, arrayOfType); if (memberDefinition != null) { paramEnvironment.dtEvent("NewInstanceExpression.checkValue: ANON CLASS " + this.body + " SUPER " + classDefinition); paramVset = this.body.checkLocalClass(paramEnvironment, paramContext, paramVset, classDefinition, arrayOfExpression, memberDefinition.getType().getArgumentTypes()); type = this.body.getClassDeclaration().getType(); classDefinition = this.body; }  } else { if (classDefinition.isInterface()) { paramEnvironment.error(this.where, "new.intf", classDeclaration); return paramVset; }  if (classDefinition.mustBeAbstract(paramEnvironment)) { paramEnvironment.error(this.where, "new.abstract", classDeclaration); return paramVset; }  }  this.field = classDefinition.matchMethod(paramEnvironment, classDefinition1, idInit, arrayOfType); if (this.field == null) { MemberDefinition memberDefinition = classDefinition.findAnyMethod(paramEnvironment, idInit); if (memberDefinition != null && (new MethodExpression(this.where, this.right, memberDefinition, arrayOfExpression)).diagnoseMismatch(paramEnvironment, arrayOfExpression, arrayOfType)) return paramVset;  String str = classDeclaration.getName().getName().toString(); str = Type.tMethod(Type.tError, arrayOfType).typeString(str, false, false); paramEnvironment.error(this.where, "unmatched.constr", str, classDeclaration); return paramVset; }  if (this.field.isPrivate()) { ClassDefinition classDefinition2 = this.field.getClassDefinition(); if (classDefinition2 != classDefinition1) this.implMethod = classDefinition2.getAccessMember(paramEnvironment, paramContext, this.field, false);  }  if (classDefinition.mustBeAbstract(paramEnvironment)) { paramEnvironment.error(this.where, "new.abstract", classDeclaration); return paramVset; }  if (this.field.reportDeprecated(paramEnvironment)) paramEnvironment.error(this.where, "warn.constr.is.deprecated", this.field, this.field.getClassDefinition());  if (this.field.isProtected() && !classDefinition1.getName().getQualifier().equals(this.field.getClassDeclaration().getName().getQualifier())) paramEnvironment.error(this.where, "invalid.protected.constructor.use", classDefinition1);  } catch (ClassNotFound classNotFound) { paramEnvironment.error(this.where, "class.not.found", classNotFound.name, opNames[this.op]); return paramVset; } catch (AmbiguousMember ambiguousMember) { paramEnvironment.error(this.where, "ambig.constr", ambiguousMember.field1, ambiguousMember.field2); return paramVset; }  arrayOfType = this.field.getType().getArgumentTypes(); for (b1 = 0; b1 < arrayOfExpression.length; b1++) arrayOfExpression[b1] = convert(paramEnvironment, paramContext, arrayOfType[b1], arrayOfExpression[b1]);  if (arrayOfExpression.length > this.args.length) { this.outerArg = arrayOfExpression[0]; for (b1 = 1; b1 < arrayOfExpression.length; b1++) this.args[b1 - 1] = arrayOfExpression[b1];  }  ClassDeclaration[] arrayOfClassDeclaration = this.field.getExceptions(paramEnvironment); for (byte b2 = 0; b2 < arrayOfClassDeclaration.length; b2++) { if (paramHashtable.get(arrayOfClassDeclaration[b2]) == null) paramHashtable.put(arrayOfClassDeclaration[b2], this);  }  this.type = type; return paramVset; }
/*     */   public static Expression[] insertOuterLink(Environment paramEnvironment, Context paramContext, long paramLong, ClassDefinition paramClassDefinition, Expression paramExpression, Expression[] paramArrayOfExpression) { if (!paramClassDefinition.isTopLevel() && !paramClassDefinition.isLocal()) { Expression[] arrayOfExpression = new Expression[1 + paramArrayOfExpression.length]; System.arraycopy(paramArrayOfExpression, 0, arrayOfExpression, 1, paramArrayOfExpression.length); try { if (paramExpression == null) paramExpression = paramContext.findOuterLink(paramEnvironment, paramLong, paramClassDefinition.findAnyMethod(paramEnvironment, idInit));  } catch (ClassNotFound classNotFound) {} arrayOfExpression[0] = paramExpression; paramArrayOfExpression = arrayOfExpression; }  return paramArrayOfExpression; }
/* 387 */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) { return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); } Expression inlineNewInstance(Environment paramEnvironment, Context paramContext, Statement paramStatement) { if (paramEnvironment.dump()) {
/* 388 */       System.out.println("INLINE NEW INSTANCE " + this.field + " in " + paramContext.field);
/*     */     }
/* 390 */     LocalMember[] arrayOfLocalMember = LocalMember.copyArguments(paramContext, this.field);
/* 391 */     Statement[] arrayOfStatement = new Statement[arrayOfLocalMember.length + 2];
/*     */     
/* 393 */     byte b1 = 1;
/* 394 */     if (this.outerArg != null && !this.outerArg.type.isType(11)) {
/* 395 */       b1 = 2;
/* 396 */       arrayOfStatement[1] = new VarDeclarationStatement(this.where, arrayOfLocalMember[1], this.outerArg);
/* 397 */     } else if (this.outerArg != null) {
/* 398 */       arrayOfStatement[0] = new ExpressionStatement(this.where, this.outerArg);
/*     */     } 
/* 400 */     for (byte b2 = 0; b2 < this.args.length; b2++) {
/* 401 */       arrayOfStatement[b2 + b1] = new VarDeclarationStatement(this.where, arrayOfLocalMember[b2 + b1], this.args[b2]);
/*     */     }
/*     */     
/* 404 */     arrayOfStatement[arrayOfStatement.length - 1] = (paramStatement != null) ? paramStatement.copyInline(paramContext, false) : null;
/*     */ 
/*     */     
/* 407 */     LocalMember.doneWithArguments(paramContext, arrayOfLocalMember);
/*     */     
/* 409 */     return (new InlineNewInstanceExpression(this.where, this.type, this.field, new CompoundStatement(this.where, arrayOfStatement))).inline(paramEnvironment, paramContext); }
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 413 */     return inlineValue(paramEnvironment, paramContext);
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 416 */     if (this.body != null) {
/* 417 */       this.body.inlineLocalClass(paramEnvironment);
/*     */     }
/* 419 */     ClassDefinition classDefinition = this.field.getClassDefinition();
/* 420 */     UplevelReference uplevelReference = classDefinition.getReferencesFrozen();
/* 421 */     if (uplevelReference != null) {
/* 422 */       uplevelReference.willCodeArguments(paramEnvironment, paramContext);
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 427 */       if (this.outerArg != null)
/* 428 */         if (this.outerArg.type.isType(11)) {
/* 429 */           this.outerArg = this.outerArg.inline(paramEnvironment, paramContext);
/*     */         } else {
/* 431 */           this.outerArg = this.outerArg.inlineValue(paramEnvironment, paramContext);
/*     */         }  
/* 433 */       for (byte b = 0; b < this.args.length; b++) {
/* 434 */         this.args[b] = this.args[b].inlineValue(paramEnvironment, paramContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 448 */     catch (ClassNotFound classNotFound) {
/* 449 */       throw new CompilerError(classNotFound);
/*     */     } 
/* 451 */     if (this.outerArg != null && this.outerArg.type.isType(11)) {
/* 452 */       Expression expression = this.outerArg;
/* 453 */       this.outerArg = null;
/* 454 */       return new CommaExpression(this.where, expression, this);
/*     */     } 
/* 456 */     return this;
/*     */   }
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 460 */     if (this.body != null) {
/* 461 */       return paramInt;
/*     */     }
/* 463 */     if (paramContext == null) {
/* 464 */       return 2 + super.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/*     */     
/* 467 */     ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/*     */ 
/*     */     
/*     */     try {
/* 471 */       if (classDefinition.permitInlinedAccess(paramEnvironment, this.field.getClassDeclaration()) && classDefinition
/* 472 */         .permitInlinedAccess(paramEnvironment, this.field)) {
/* 473 */         return 2 + super.costInline(paramInt, paramEnvironment, paramContext);
/*     */       }
/* 475 */     } catch (ClassNotFound classNotFound) {}
/*     */     
/* 477 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 485 */     codeCommon(paramEnvironment, paramContext, paramAssembler, false);
/*     */   }
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 488 */     codeCommon(paramEnvironment, paramContext, paramAssembler, true);
/*     */   }
/*     */   
/*     */   private void codeCommon(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean) {
/* 492 */     paramAssembler.add(this.where, 187, this.field.getClassDeclaration());
/* 493 */     if (paramBoolean) {
/* 494 */       paramAssembler.add(this.where, 89);
/*     */     }
/*     */     
/* 497 */     ClassDefinition classDefinition = this.field.getClassDefinition();
/* 498 */     UplevelReference uplevelReference = classDefinition.getReferencesFrozen();
/*     */     
/* 500 */     if (uplevelReference != null) {
/* 501 */       uplevelReference.codeArguments(paramEnvironment, paramContext, paramAssembler, this.where, this.field);
/*     */     }
/*     */     
/* 504 */     if (this.outerArg != null) {
/* 505 */       MemberDefinition memberDefinition; this.outerArg.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 506 */       switch (this.outerArg.op) {
/*     */         case 49:
/*     */         case 82:
/*     */         case 83:
/*     */           break;
/*     */         
/*     */         case 46:
/* 513 */           memberDefinition = ((FieldExpression)this.outerArg).field;
/* 514 */           if (memberDefinition != null && memberDefinition.isNeverNull()) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         default:
/*     */           try {
/* 523 */             ClassDefinition classDefinition1 = paramEnvironment.getClassDefinition(idJavaLangObject);
/* 524 */             MemberDefinition memberDefinition1 = classDefinition1.getFirstMatch(idGetClass);
/* 525 */             paramAssembler.add(this.where, 89);
/* 526 */             paramAssembler.add(this.where, 182, memberDefinition1);
/* 527 */             paramAssembler.add(this.where, 87);
/* 528 */           } catch (ClassNotFound classNotFound) {}
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 533 */     if (this.implMethod != null)
/*     */     {
/*     */       
/* 536 */       paramAssembler.add(this.where, 1);
/*     */     }
/*     */     
/* 539 */     for (byte b = 0; b < this.args.length; b++) {
/* 540 */       this.args[b].codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/* 542 */     paramAssembler.add(this.where, 183, (this.implMethod != null) ? this.implMethod : this.field);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\NewInstanceExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */