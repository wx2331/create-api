/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
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
/*     */ 
/*     */ 
/*     */ public class MethodExpression
/*     */   extends NaryExpression
/*     */ {
/*     */   Identifier id;
/*     */   ClassDefinition clazz;
/*     */   MemberDefinition field;
/*     */   Expression implementation;
/*     */   private boolean isSuper;
/*     */   
/*     */   public MethodExpression(long paramLong, Expression paramExpression, Identifier paramIdentifier, Expression[] paramArrayOfExpression) {
/*  51 */     super(47, paramLong, Type.tError, paramExpression, paramArrayOfExpression);
/*  52 */     this.id = paramIdentifier;
/*     */   }
/*     */   public MethodExpression(long paramLong, Expression paramExpression, MemberDefinition paramMemberDefinition, Expression[] paramArrayOfExpression) {
/*  55 */     super(47, paramLong, paramMemberDefinition.getType().getReturnType(), paramExpression, paramArrayOfExpression);
/*  56 */     this.id = paramMemberDefinition.getName();
/*  57 */     this.field = paramMemberDefinition;
/*  58 */     this.clazz = paramMemberDefinition.getClassDefinition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodExpression(long paramLong, Expression paramExpression, MemberDefinition paramMemberDefinition, Expression[] paramArrayOfExpression, boolean paramBoolean) {
/*  68 */     this(paramLong, paramExpression, paramMemberDefinition, paramArrayOfExpression);
/*  69 */     this.isSuper = paramBoolean;
/*     */   }
/*     */   
/*     */   public Expression getImplementation() {
/*  73 */     if (this.implementation != null)
/*  74 */       return this.implementation; 
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable<ClassDeclaration, MethodExpression> paramHashtable) {
/*  82 */     ClassDeclaration classDeclaration = null;
/*  83 */     boolean bool = false;
/*  84 */     boolean bool1 = false;
/*     */ 
/*     */     
/*  87 */     MemberDefinition memberDefinition = null;
/*     */     
/*  89 */     ClassDefinition classDefinition1 = paramContext.field.getClassDefinition();
/*     */ 
/*     */ 
/*     */     
/*  93 */     Expression[] arrayOfExpression = this.args;
/*  94 */     if (this.id.equals(idInit)) {
/*  95 */       ClassDefinition classDefinition = classDefinition1;
/*     */       try {
/*  97 */         Expression expression = null;
/*  98 */         if (this.right instanceof SuperExpression) {
/*     */           
/* 100 */           classDefinition = classDefinition.getSuperClass().getClassDefinition(paramEnvironment);
/* 101 */           expression = ((SuperExpression)this.right).outerArg;
/* 102 */         } else if (this.right instanceof ThisExpression) {
/*     */           
/* 104 */           expression = ((ThisExpression)this.right).outerArg;
/*     */         } 
/*     */         
/* 107 */         arrayOfExpression = NewInstanceExpression.insertOuterLink(paramEnvironment, paramContext, this.where, classDefinition, expression, arrayOfExpression);
/* 108 */       } catch (ClassNotFound classNotFound) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 113 */     Type[] arrayOfType = new Type[arrayOfExpression.length];
/*     */ 
/*     */ 
/*     */     
/* 117 */     ClassDefinition classDefinition2 = classDefinition1;
/*     */     
/*     */     try {
/* 120 */       if (this.right == null) {
/* 121 */         bool1 = paramContext.field.isStatic();
/*     */         
/* 123 */         ClassDefinition classDefinition = classDefinition1;
/* 124 */         MemberDefinition memberDefinition1 = null;
/* 125 */         for (; classDefinition != null; classDefinition = classDefinition.getOuterClass()) {
/* 126 */           memberDefinition1 = classDefinition.findAnyMethod(paramEnvironment, this.id);
/* 127 */           if (memberDefinition1 != null) {
/*     */             break;
/*     */           }
/*     */         } 
/* 131 */         if (memberDefinition1 == null) {
/*     */           
/* 133 */           classDeclaration = paramContext.field.getClassDeclaration();
/*     */         } else {
/*     */           
/* 136 */           classDeclaration = classDefinition.getClassDeclaration();
/*     */ 
/*     */ 
/*     */           
/* 140 */           if (memberDefinition1.getClassDefinition() != classDefinition) {
/* 141 */             ClassDefinition classDefinition3 = classDefinition;
/* 142 */             while ((classDefinition3 = classDefinition3.getOuterClass()) != null) {
/* 143 */               MemberDefinition memberDefinition2 = classDefinition3.findAnyMethod(paramEnvironment, this.id);
/* 144 */               if (memberDefinition2 != null && memberDefinition2.getClassDefinition() == classDefinition3) {
/* 145 */                 paramEnvironment.error(this.where, "inherited.hides.method", this.id, classDefinition
/* 146 */                     .getClassDeclaration(), classDefinition3
/* 147 */                     .getClassDeclaration());
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 154 */         if (this.id.equals(idInit)) {
/* 155 */           int i = paramContext.getThisNumber();
/* 156 */           if (!paramContext.field.isConstructor()) {
/* 157 */             paramEnvironment.error(this.where, "invalid.constr.invoke");
/* 158 */             return paramVset.addVar(i);
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 192 */           if (!paramVset.isReallyDeadEnd() && paramVset.testVar(i)) {
/* 193 */             paramEnvironment.error(this.where, "constr.invoke.not.first");
/* 194 */             return paramVset;
/*     */           } 
/* 196 */           paramVset = paramVset.addVar(i);
/* 197 */           if (this.right instanceof SuperExpression) {
/*     */             
/* 199 */             paramVset = this.right.checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/*     */           } else {
/* 201 */             paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */           } 
/*     */         } else {
/* 204 */           paramVset = this.right.checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/* 205 */           if (this.right.type == Type.tPackage) {
/* 206 */             FieldExpression.reportFailedPackagePrefix(paramEnvironment, this.right);
/* 207 */             return paramVset;
/*     */           } 
/* 209 */           if (this.right instanceof TypeExpression) {
/* 210 */             bool1 = true;
/*     */           }
/*     */         } 
/* 213 */         if (this.right.type.isType(10)) {
/* 214 */           classDeclaration = paramEnvironment.getClassDeclaration(this.right.type);
/* 215 */         } else if (this.right.type.isType(9)) {
/* 216 */           bool = true;
/* 217 */           classDeclaration = paramEnvironment.getClassDeclaration(Type.tObject);
/*     */         } else {
/* 219 */           if (!this.right.type.isType(13)) {
/* 220 */             paramEnvironment.error(this.where, "invalid.method.invoke", this.right.type);
/*     */           }
/* 222 */           return paramVset;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 235 */         if (this.right instanceof FieldExpression) {
/* 236 */           Identifier identifier = ((FieldExpression)this.right).id;
/* 237 */           if (identifier == idThis) {
/* 238 */             classDefinition2 = ((FieldExpression)this.right).clazz;
/* 239 */           } else if (identifier == idSuper) {
/* 240 */             this.isSuper = true;
/* 241 */             classDefinition2 = ((FieldExpression)this.right).clazz;
/*     */           } 
/* 243 */         } else if (this.right instanceof SuperExpression) {
/* 244 */           this.isSuper = true;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 254 */         if (this.id != idInit)
/*     */         {
/*     */           
/* 257 */           if (!FieldExpression.isTypeAccessible(this.where, paramEnvironment, this.right.type, classDefinition2)) {
/*     */ 
/*     */ 
/*     */             
/* 261 */             ClassDeclaration classDeclaration1 = classDefinition2.getClassDeclaration();
/* 262 */             if (bool1) {
/* 263 */               paramEnvironment.error(this.where, "no.type.access", this.id, this.right.type
/* 264 */                   .toString(), classDeclaration1);
/*     */             } else {
/* 266 */               paramEnvironment.error(this.where, "cant.access.member.type", this.id, this.right.type
/* 267 */                   .toString(), classDeclaration1);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 274 */       boolean bool2 = false;
/*     */ 
/*     */       
/* 277 */       if (this.id.equals(idInit)) {
/* 278 */         paramVset = paramVset.clearVar(paramContext.getThisNumber());
/*     */       }
/*     */       
/* 281 */       for (byte b = 0; b < arrayOfExpression.length; b++) {
/* 282 */         paramVset = arrayOfExpression[b].checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 283 */         arrayOfType[b] = (arrayOfExpression[b]).type;
/* 284 */         bool2 = (bool2 || arrayOfType[b].isType(13)) ? true : false;
/*     */       } 
/*     */ 
/*     */       
/* 288 */       if (this.id.equals(idInit)) {
/* 289 */         paramVset = paramVset.addVar(paramContext.getThisNumber());
/*     */       }
/*     */ 
/*     */       
/* 293 */       if (bool2) {
/* 294 */         return paramVset;
/*     */       }
/*     */ 
/*     */       
/* 298 */       this.clazz = classDeclaration.getClassDefinition(paramEnvironment);
/*     */       
/* 300 */       if (this.field == null) {
/*     */         
/* 302 */         this.field = this.clazz.matchMethod(paramEnvironment, classDefinition2, this.id, arrayOfType);
/*     */         
/* 304 */         if (this.field == null) {
/* 305 */           if (this.id.equals(idInit)) {
/* 306 */             if (diagnoseMismatch(paramEnvironment, arrayOfExpression, arrayOfType))
/* 307 */               return paramVset; 
/* 308 */             String str1 = this.clazz.getName().getName().toString();
/* 309 */             str1 = Type.tMethod(Type.tError, arrayOfType).typeString(str1, false, false);
/* 310 */             paramEnvironment.error(this.where, "unmatched.constr", str1, classDeclaration);
/* 311 */             return paramVset;
/*     */           } 
/* 313 */           String str = this.id.toString();
/* 314 */           str = Type.tMethod(Type.tError, arrayOfType).typeString(str, false, false);
/* 315 */           if (this.clazz.findAnyMethod(paramEnvironment, this.id) == null) {
/* 316 */             if (paramContext.getField(paramEnvironment, this.id) != null) {
/* 317 */               paramEnvironment.error(this.where, "invalid.method", this.id, classDeclaration);
/*     */             } else {
/* 319 */               paramEnvironment.error(this.where, "undef.meth", str, classDeclaration);
/*     */             } 
/* 321 */           } else if (!diagnoseMismatch(paramEnvironment, arrayOfExpression, arrayOfType)) {
/*     */             
/* 323 */             paramEnvironment.error(this.where, "unmatched.meth", str, classDeclaration);
/*     */           } 
/* 325 */           return paramVset;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 330 */       this.type = this.field.getType().getReturnType();
/*     */ 
/*     */       
/* 333 */       if (bool1 && !this.field.isStatic()) {
/* 334 */         paramEnvironment.error(this.where, "no.static.meth.access", this.field, this.field
/* 335 */             .getClassDeclaration());
/* 336 */         return paramVset;
/*     */       } 
/*     */       
/* 339 */       if (this.field.isProtected() && this.right != null && !(this.right instanceof SuperExpression) && (!(this.right instanceof FieldExpression) || ((FieldExpression)this.right).id != idSuper) && 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 345 */         !classDefinition2.protectedAccess(paramEnvironment, this.field, this.right.type)) {
/* 346 */         paramEnvironment.error(this.where, "invalid.protected.method.use", this.field
/* 347 */             .getName(), this.field.getClassDeclaration(), this.right.type);
/*     */         
/* 349 */         return paramVset;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 357 */       if (this.right instanceof FieldExpression && ((FieldExpression)this.right).id == idSuper)
/*     */       {
/* 359 */         if (!this.field.isPrivate())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 365 */           if (classDefinition2 != classDefinition1) {
/* 366 */             memberDefinition = classDefinition2.getAccessMember(paramEnvironment, paramContext, this.field, true);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */       
/* 372 */       if (memberDefinition == null && this.field.isPrivate()) {
/* 373 */         ClassDefinition classDefinition = this.field.getClassDefinition();
/* 374 */         if (classDefinition != classDefinition1) {
/* 375 */           memberDefinition = classDefinition.getAccessMember(paramEnvironment, paramContext, this.field, false);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 380 */       if (this.field.isAbstract() && this.right != null && this.right.op == 83) {
/* 381 */         paramEnvironment.error(this.where, "invoke.abstract", this.field, this.field.getClassDeclaration());
/* 382 */         return paramVset;
/*     */       } 
/*     */       
/* 385 */       if (this.field.reportDeprecated(paramEnvironment)) {
/* 386 */         if (this.field.isConstructor()) {
/* 387 */           paramEnvironment.error(this.where, "warn.constr.is.deprecated", this.field);
/*     */         } else {
/* 389 */           paramEnvironment.error(this.where, "warn.meth.is.deprecated", this.field, this.field
/* 390 */               .getClassDefinition());
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 395 */       if (this.field.isConstructor() && paramContext.field.equals(this.field)) {
/* 396 */         paramEnvironment.error(this.where, "recursive.constr", this.field);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 410 */       if (classDefinition2 == classDefinition1) {
/* 411 */         ClassDefinition classDefinition = this.field.getClassDefinition();
/* 412 */         if (!this.field.isConstructor() && classDefinition
/* 413 */           .isPackagePrivate() && 
/*     */           
/* 415 */           !classDefinition.getName().getQualifier().equals(classDefinition2.getName().getQualifier()))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 428 */           this
/* 429 */             .field = MemberDefinition.makeProxyMember(this.field, this.clazz, paramEnvironment);
/*     */         }
/*     */       } 
/*     */       
/* 433 */       classDefinition2.addDependency(this.field.getClassDeclaration());
/* 434 */       if (classDefinition2 != classDefinition1) {
/* 435 */         classDefinition1.addDependency(this.field.getClassDeclaration());
/*     */       }
/*     */     }
/* 438 */     catch (ClassNotFound classNotFound) {
/* 439 */       paramEnvironment.error(this.where, "class.not.found", classNotFound.name, paramContext.field);
/* 440 */       return paramVset;
/*     */     }
/* 442 */     catch (AmbiguousMember ambiguousMember) {
/* 443 */       paramEnvironment.error(this.where, "ambig.field", this.id, ambiguousMember.field1, ambiguousMember.field2);
/* 444 */       return paramVset;
/*     */     } 
/*     */ 
/*     */     
/* 448 */     if (this.right == null && !this.field.isStatic()) {
/* 449 */       this.right = paramContext.findOuterLink(paramEnvironment, this.where, this.field);
/* 450 */       paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     } 
/*     */ 
/*     */     
/* 454 */     arrayOfType = this.field.getType().getArgumentTypes();
/* 455 */     for (byte b1 = 0; b1 < arrayOfExpression.length; b1++) {
/* 456 */       arrayOfExpression[b1] = convert(paramEnvironment, paramContext, arrayOfType[b1], arrayOfExpression[b1]);
/*     */     }
/*     */     
/* 459 */     if (this.field.isConstructor()) {
/* 460 */       MemberDefinition memberDefinition1 = this.field;
/* 461 */       if (memberDefinition != null) {
/* 462 */         memberDefinition1 = memberDefinition;
/*     */       }
/* 464 */       int i = arrayOfExpression.length;
/* 465 */       Expression[] arrayOfExpression1 = arrayOfExpression;
/* 466 */       if (i > this.args.length) {
/*     */         ThisExpression thisExpression;
/*     */ 
/*     */         
/* 470 */         if (this.right instanceof SuperExpression) {
/* 471 */           thisExpression = new SuperExpression(this.right.where, paramContext);
/* 472 */           ((SuperExpression)this.right).outerArg = arrayOfExpression[0];
/* 473 */         } else if (this.right instanceof ThisExpression) {
/* 474 */           thisExpression = new ThisExpression(this.right.where, paramContext);
/*     */         } else {
/* 476 */           throw new CompilerError("this.init");
/*     */         } 
/* 478 */         if (memberDefinition != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 483 */           arrayOfExpression1 = new Expression[i + 1];
/* 484 */           this.args = new Expression[i];
/* 485 */           arrayOfExpression1[0] = arrayOfExpression[0];
/* 486 */           arrayOfExpression1[1] = new NullExpression(this.where); this.args[0] = new NullExpression(this.where);
/* 487 */           for (byte b = 1; b < i; b++) {
/* 488 */             arrayOfExpression1[b + 1] = arrayOfExpression[b]; this.args[b] = arrayOfExpression[b];
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 493 */           for (byte b = 1; b < i; b++) {
/* 494 */             this.args[b - 1] = arrayOfExpression[b];
/*     */           }
/*     */         } 
/* 497 */         this.implementation = new MethodExpression(this.where, thisExpression, memberDefinition1, arrayOfExpression1);
/* 498 */         this.implementation.type = this.type;
/*     */       } else {
/*     */         
/* 501 */         if (memberDefinition != null) {
/*     */ 
/*     */           
/* 504 */           arrayOfExpression1 = new Expression[i + 1];
/* 505 */           arrayOfExpression1[0] = new NullExpression(this.where);
/* 506 */           for (byte b = 0; b < i; b++) {
/* 507 */             arrayOfExpression1[b + 1] = arrayOfExpression[b];
/*     */           }
/*     */         } 
/* 510 */         this.implementation = new MethodExpression(this.where, this.right, memberDefinition1, arrayOfExpression1);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 515 */       if (arrayOfExpression.length > this.args.length) {
/* 516 */         throw new CompilerError("method arg");
/*     */       }
/* 518 */       if (memberDefinition != null) {
/*     */         
/* 520 */         Expression[] arrayOfExpression1 = this.args;
/* 521 */         if (this.field.isStatic()) {
/* 522 */           MethodExpression methodExpression = new MethodExpression(this.where, null, memberDefinition, arrayOfExpression1);
/* 523 */           this.implementation = new CommaExpression(this.where, this.right, methodExpression);
/*     */         } else {
/*     */           
/* 526 */           int i = arrayOfExpression1.length;
/* 527 */           Expression[] arrayOfExpression2 = new Expression[i + 1];
/* 528 */           arrayOfExpression2[0] = this.right;
/* 529 */           for (byte b = 0; b < i; b++) {
/* 530 */             arrayOfExpression2[b + 1] = arrayOfExpression1[b];
/*     */           }
/* 532 */           this.implementation = new MethodExpression(this.where, null, memberDefinition, arrayOfExpression2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 538 */     if (paramContext.field.isConstructor() && this.field
/* 539 */       .isConstructor() && this.right != null && this.right.op == 83) {
/* 540 */       Expression expression = makeVarInits(paramEnvironment, paramContext);
/* 541 */       if (expression != null) {
/* 542 */         if (this.implementation == null)
/* 543 */           this.implementation = (Expression)clone(); 
/* 544 */         this.implementation = new CommaExpression(this.where, this.implementation, expression);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 549 */     ClassDeclaration[] arrayOfClassDeclaration = this.field.getExceptions(paramEnvironment);
/* 550 */     if (bool && this.field.getName() == idClone && (this.field
/* 551 */       .getType().getArgumentTypes()).length == 0) {
/*     */ 
/*     */ 
/*     */       
/* 555 */       arrayOfClassDeclaration = new ClassDeclaration[0];
/*     */       
/* 557 */       for (Context context = paramContext; context != null; context = context.prev) {
/* 558 */         if (context.node != null && context.node.op == 101) {
/* 559 */           ((TryStatement)context.node).arrayCloneWhere = this.where;
/*     */         }
/*     */       } 
/*     */     } 
/* 563 */     for (byte b2 = 0; b2 < arrayOfClassDeclaration.length; b2++) {
/* 564 */       if (paramHashtable.get(arrayOfClassDeclaration[b2]) == null) {
/* 565 */         paramHashtable.put(arrayOfClassDeclaration[b2], this);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 572 */     if (paramContext.field.isConstructor() && this.field
/* 573 */       .isConstructor() && this.right != null && this.right.op == 82) {
/* 574 */       ClassDefinition classDefinition = this.field.getClassDefinition();
/* 575 */       for (MemberDefinition memberDefinition1 = classDefinition.getFirstMember(); memberDefinition1 != null; memberDefinition1 = memberDefinition1.getNextMember()) {
/* 576 */         if (memberDefinition1.isVariable() && memberDefinition1.isBlankFinal() && !memberDefinition1.isStatic())
/*     */         {
/*     */           
/* 579 */           paramVset = paramVset.addVar(paramContext.getFieldNumber(memberDefinition1));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 584 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 591 */     return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
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
/*     */   boolean diagnoseMismatch(Environment paramEnvironment, Expression[] paramArrayOfExpression, Type[] paramArrayOfType) throws ClassNotFound {
/* 606 */     Type[] arrayOfType = new Type[1];
/* 607 */     boolean bool = false;
/* 608 */     int i = 0;
/* 609 */     while (i < paramArrayOfType.length) {
/* 610 */       int j = this.clazz.diagnoseMismatch(paramEnvironment, this.id, paramArrayOfType, i, arrayOfType);
/* 611 */       String str1 = this.id.equals(idInit) ? "constructor" : opNames[this.op];
/* 612 */       if (j == -2) {
/* 613 */         paramEnvironment.error(this.where, "wrong.number.args", str1);
/* 614 */         bool = true;
/*     */       } 
/* 616 */       if (j < 0)
/* 617 */         break;  int k = j >> 2;
/* 618 */       boolean bool1 = ((j & 0x2) != 0) ? true : false;
/* 619 */       boolean bool2 = ((j & 0x1) != 0) ? true : false;
/* 620 */       Type type = arrayOfType[0];
/*     */ 
/*     */ 
/*     */       
/* 624 */       String str2 = "" + type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 630 */       if (bool1) {
/* 631 */         paramEnvironment.error((paramArrayOfExpression[k]).where, "explicit.cast.needed", str1, paramArrayOfType[k], str2);
/*     */       } else {
/* 633 */         paramEnvironment.error((paramArrayOfExpression[k]).where, "incompatible.type", str1, paramArrayOfType[k], str2);
/* 634 */       }  bool = true;
/* 635 */       i = k + 1;
/*     */     } 
/* 637 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 643 */   static final int MAXINLINECOST = Statement.MAXINLINECOST;
/*     */ 
/*     */   
/*     */   private Expression inlineMethod(Environment paramEnvironment, Context paramContext, Statement paramStatement, boolean paramBoolean) {
/* 647 */     if (paramEnvironment.dump()) {
/* 648 */       System.out.println("INLINE METHOD " + this.field + " in " + paramContext.field);
/*     */     }
/* 650 */     LocalMember[] arrayOfLocalMember = LocalMember.copyArguments(paramContext, this.field);
/* 651 */     Statement[] arrayOfStatement = new Statement[arrayOfLocalMember.length + 2];
/*     */     
/* 653 */     byte b1 = 0;
/* 654 */     if (this.field.isStatic()) {
/* 655 */       arrayOfStatement[0] = new ExpressionStatement(this.where, this.right);
/*     */     } else {
/* 657 */       if (this.right != null && this.right.op == 83) {
/* 658 */         this.right = new ThisExpression(this.right.where, paramContext);
/*     */       }
/* 660 */       arrayOfStatement[0] = new VarDeclarationStatement(this.where, arrayOfLocalMember[b1++], this.right);
/*     */     } 
/* 662 */     for (byte b2 = 0; b2 < this.args.length; b2++) {
/* 663 */       arrayOfStatement[b2 + 1] = new VarDeclarationStatement(this.where, arrayOfLocalMember[b1++], this.args[b2]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 668 */     arrayOfStatement[arrayOfStatement.length - 1] = (paramStatement != null) ? paramStatement.copyInline(paramContext, paramBoolean) : null;
/*     */     
/* 670 */     LocalMember.doneWithArguments(paramContext, arrayOfLocalMember);
/*     */ 
/*     */     
/* 673 */     Type type = paramBoolean ? this.type : Type.tVoid;
/* 674 */     InlineMethodExpression inlineMethodExpression = new InlineMethodExpression(this.where, type, this.field, new CompoundStatement(this.where, arrayOfStatement));
/* 675 */     return paramBoolean ? inlineMethodExpression.inlineValue(paramEnvironment, paramContext) : inlineMethodExpression.inline(paramEnvironment, paramContext);
/*     */   }
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 679 */     if (this.implementation != null)
/* 680 */       return this.implementation.inline(paramEnvironment, paramContext); 
/*     */     try {
/* 682 */       if (this.right != null) {
/* 683 */         this.right = this.field.isStatic() ? this.right.inline(paramEnvironment, paramContext) : this.right.inlineValue(paramEnvironment, paramContext);
/*     */       }
/* 685 */       for (byte b = 0; b < this.args.length; b++) {
/* 686 */         this.args[b] = this.args[b].inlineValue(paramEnvironment, paramContext);
/*     */       }
/*     */ 
/*     */       
/* 690 */       ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/*     */       
/* 692 */       Expression expression = this;
/* 693 */       if (paramEnvironment.opt() && this.field.isInlineable(paramEnvironment, this.clazz.isFinal()) && (this.right == null || this.right.op == 82 || this.field
/*     */ 
/*     */ 
/*     */         
/* 697 */         .isStatic()) && classDefinition
/*     */ 
/*     */ 
/*     */         
/* 701 */         .permitInlinedAccess(paramEnvironment, this.field
/* 702 */           .getClassDeclaration()) && classDefinition
/* 703 */         .permitInlinedAccess(paramEnvironment, this.field) && (this.right == null || classDefinition
/* 704 */         .permitInlinedAccess(paramEnvironment, paramEnvironment
/* 705 */           .getClassDeclaration(this.right.type))) && (this.id == null || 
/*     */         
/* 707 */         !this.id.equals(idInit)) && 
/* 708 */         !paramContext.field.isInitializer() && paramContext.field.isMethod() && paramContext
/* 709 */         .getInlineMemberContext(this.field) == null) {
/* 710 */         Statement statement = (Statement)this.field.getValue(paramEnvironment);
/* 711 */         if (statement == null || statement
/* 712 */           .costInline(MAXINLINECOST, paramEnvironment, paramContext) < MAXINLINECOST) {
/* 713 */           expression = inlineMethod(paramEnvironment, paramContext, statement, false);
/*     */         }
/*     */       } 
/* 716 */       return expression;
/*     */     }
/* 718 */     catch (ClassNotFound classNotFound) {
/* 719 */       throw new CompilerError(classNotFound);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 724 */     if (this.implementation != null)
/* 725 */       return this.implementation.inlineValue(paramEnvironment, paramContext); 
/*     */     try {
/* 727 */       if (this.right != null) {
/* 728 */         this.right = this.field.isStatic() ? this.right.inline(paramEnvironment, paramContext) : this.right.inlineValue(paramEnvironment, paramContext);
/*     */       }
/* 730 */       if (this.field.getName().equals(idInit)) {
/* 731 */         ClassDefinition classDefinition1 = this.field.getClassDefinition();
/* 732 */         UplevelReference uplevelReference = classDefinition1.getReferencesFrozen();
/* 733 */         if (uplevelReference != null) {
/* 734 */           uplevelReference.willCodeArguments(paramEnvironment, paramContext);
/*     */         }
/*     */       } 
/* 737 */       for (byte b = 0; b < this.args.length; b++) {
/* 738 */         this.args[b] = this.args[b].inlineValue(paramEnvironment, paramContext);
/*     */       }
/*     */ 
/*     */       
/* 742 */       ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/*     */       
/* 744 */       if (paramEnvironment.opt() && this.field.isInlineable(paramEnvironment, this.clazz.isFinal()) && (this.right == null || this.right.op == 82 || this.field
/*     */ 
/*     */ 
/*     */         
/* 748 */         .isStatic()) && classDefinition
/*     */ 
/*     */ 
/*     */         
/* 752 */         .permitInlinedAccess(paramEnvironment, this.field
/* 753 */           .getClassDeclaration()) && classDefinition
/* 754 */         .permitInlinedAccess(paramEnvironment, this.field) && (this.right == null || classDefinition
/* 755 */         .permitInlinedAccess(paramEnvironment, paramEnvironment
/* 756 */           .getClassDeclaration(this.right.type))) && 
/*     */         
/* 758 */         !paramContext.field.isInitializer() && paramContext.field.isMethod() && paramContext
/* 759 */         .getInlineMemberContext(this.field) == null) {
/* 760 */         Statement statement = (Statement)this.field.getValue(paramEnvironment);
/* 761 */         if (statement == null || statement
/* 762 */           .costInline(MAXINLINECOST, paramEnvironment, paramContext) < MAXINLINECOST) {
/* 763 */           return inlineMethod(paramEnvironment, paramContext, statement, true);
/*     */         }
/*     */       } 
/* 766 */       return this;
/* 767 */     } catch (ClassNotFound classNotFound) {
/* 768 */       throw new CompilerError(classNotFound);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/* 773 */     if (this.implementation != null)
/* 774 */       return this.implementation.copyInline(paramContext); 
/* 775 */     return super.copyInline(paramContext);
/*     */   }
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 779 */     if (this.implementation != null) {
/* 780 */       return this.implementation.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/*     */ 
/*     */     
/* 784 */     if (this.right != null && this.right.op == 83) {
/* 785 */       return paramInt;
/*     */     }
/* 787 */     return super.costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression makeVarInits(Environment paramEnvironment, Context paramContext) {
/* 798 */     ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/* 799 */     Expression expression = null;
/* 800 */     for (MemberDefinition memberDefinition = classDefinition.getFirstMember(); memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) {
/* 801 */       if ((memberDefinition.isVariable() || memberDefinition.isInitializer()) && !memberDefinition.isStatic()) {
/*     */         try {
/* 803 */           memberDefinition.check(paramEnvironment);
/* 804 */         } catch (ClassNotFound classNotFound) {
/* 805 */           paramEnvironment.error(memberDefinition.getWhere(), "class.not.found", classNotFound.name, memberDefinition
/* 806 */               .getClassDefinition());
/*     */         } 
/* 808 */         Expression expression1 = null;
/* 809 */         if (memberDefinition.isUplevelValue()) {
/* 810 */           if (memberDefinition != classDefinition.findOuterMember()) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */           
/* 815 */           IdentifierExpression identifierExpression = new IdentifierExpression(this.where, memberDefinition.getName());
/* 816 */           if (!identifierExpression.bind(paramEnvironment, paramContext)) {
/* 817 */             throw new CompilerError("bind " + identifierExpression.id);
/*     */           }
/* 819 */           expression1 = identifierExpression;
/* 820 */         } else if (memberDefinition.isInitializer()) {
/* 821 */           Statement statement = (Statement)memberDefinition.getValue();
/* 822 */           InlineMethodExpression inlineMethodExpression = new InlineMethodExpression(this.where, Type.tVoid, memberDefinition, statement);
/*     */         } else {
/* 824 */           expression1 = (Expression)memberDefinition.getValue();
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 832 */         if (expression1 != null) {
/* 833 */           long l = memberDefinition.getWhere();
/* 834 */           expression1 = expression1.copyInline(paramContext);
/* 835 */           Expression expression2 = expression1;
/* 836 */           if (memberDefinition.isVariable()) {
/* 837 */             ThisExpression thisExpression = new ThisExpression(l, paramContext);
/* 838 */             FieldExpression fieldExpression = new FieldExpression(l, thisExpression, memberDefinition);
/* 839 */             expression2 = new AssignExpression(l, fieldExpression, expression1);
/*     */           } 
/* 841 */           expression = (expression == null) ? expression2 : new CommaExpression(l, expression, expression2);
/*     */         } 
/*     */       }  continue;
/*     */     } 
/* 845 */     return expression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 852 */     if (this.implementation != null)
/* 853 */       throw new CompilerError("codeValue"); 
/* 854 */     byte b = 0;
/* 855 */     if (this.field.isStatic()) {
/* 856 */       if (this.right != null) {
/* 857 */         this.right.code(paramEnvironment, paramContext, paramAssembler);
/*     */       }
/* 859 */     } else if (this.right == null) {
/* 860 */       paramAssembler.add(this.where, 25, new Integer(0));
/* 861 */     } else if (this.right.op == 83) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 866 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 867 */       if (idInit.equals(this.id)) {
/*     */         
/* 869 */         ClassDefinition classDefinition = this.field.getClassDefinition();
/* 870 */         UplevelReference uplevelReference = classDefinition.getReferencesFrozen();
/* 871 */         if (uplevelReference != null) {
/*     */ 
/*     */           
/* 874 */           if (uplevelReference.isClientOuterField())
/*     */           {
/* 876 */             this.args[b++].codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */           }
/* 878 */           uplevelReference.codeArguments(paramEnvironment, paramContext, paramAssembler, this.where, this.field);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 882 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 892 */     for (; b < this.args.length; b++) {
/* 893 */       this.args[b].codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/*     */     
/* 896 */     if (this.field.isStatic()) {
/* 897 */       paramAssembler.add(this.where, 184, this.field);
/* 898 */     } else if (this.field.isConstructor() || this.field.isPrivate() || this.isSuper) {
/* 899 */       paramAssembler.add(this.where, 183, this.field);
/* 900 */     } else if (this.field.getClassDefinition().isInterface()) {
/* 901 */       paramAssembler.add(this.where, 185, this.field);
/*     */     } else {
/* 903 */       paramAssembler.add(this.where, 182, this.field);
/*     */     } 
/*     */     
/* 906 */     if (this.right != null && this.right.op == 83 && idInit.equals(this.id)) {
/*     */       
/* 908 */       ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/* 909 */       UplevelReference uplevelReference = classDefinition.getReferencesFrozen();
/* 910 */       if (uplevelReference != null)
/*     */       {
/*     */         
/* 913 */         uplevelReference.codeInitialization(paramEnvironment, paramContext, paramAssembler, this.where, this.field);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression firstConstructor() {
/* 922 */     return this.id.equals(idInit) ? this : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 929 */     paramPrintStream.print("(" + opNames[this.op]);
/* 930 */     if (this.right != null) {
/* 931 */       paramPrintStream.print(" ");
/* 932 */       this.right.print(paramPrintStream);
/*     */     } 
/* 934 */     paramPrintStream.print(" " + ((this.id == null) ? (String)idInit : (String)this.id));
/* 935 */     for (byte b = 0; b < this.args.length; b++) {
/* 936 */       paramPrintStream.print(" ");
/* 937 */       if (this.args[b] != null) {
/* 938 */         this.args[b].print(paramPrintStream);
/*     */       } else {
/* 940 */         paramPrintStream.print("<null>");
/*     */       } 
/*     */     } 
/* 943 */     paramPrintStream.print(")");
/* 944 */     if (this.implementation != null) {
/* 945 */       paramPrintStream.print("/IMPL=");
/* 946 */       this.implementation.print(paramPrintStream);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\MethodExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */