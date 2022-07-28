/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.java.AmbiguousMember;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Context
/*     */   implements Constants
/*     */ {
/*     */   Context prev;
/*     */   Node node;
/*     */   int varNumber;
/*     */   LocalMember locals;
/*     */   LocalMember classes;
/*     */   MemberDefinition field;
/*     */   int scopeNumber;
/*     */   int frameNumber;
/*     */   
/*     */   public Context(Context paramContext, MemberDefinition paramMemberDefinition) {
/*  52 */     this.field = paramMemberDefinition;
/*  53 */     if (paramContext == null) {
/*  54 */       this.frameNumber = 1;
/*  55 */       this.scopeNumber = 2;
/*  56 */       this.varNumber = 0;
/*     */     } else {
/*  58 */       this.prev = paramContext;
/*  59 */       this.locals = paramContext.locals;
/*  60 */       this.classes = paramContext.classes;
/*  61 */       if (paramMemberDefinition != null && (paramMemberDefinition
/*  62 */         .isVariable() || paramMemberDefinition.isInitializer())) {
/*     */ 
/*     */ 
/*     */         
/*  66 */         this.frameNumber = paramContext.frameNumber;
/*  67 */         paramContext.scopeNumber++;
/*     */       } else {
/*  69 */         this.frameNumber = paramContext.scopeNumber + 1;
/*  70 */         this.scopeNumber = this.frameNumber + 1;
/*     */       } 
/*  72 */       this.varNumber = paramContext.varNumber;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Context(Context paramContext, ClassDefinition paramClassDefinition) {
/*  80 */     this(paramContext, (MemberDefinition)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Context(Context paramContext, Node paramNode) {
/*  87 */     if (paramContext == null) {
/*  88 */       this.frameNumber = 1;
/*  89 */       this.scopeNumber = 2;
/*  90 */       this.varNumber = 0;
/*     */     } else {
/*  92 */       this.prev = paramContext;
/*  93 */       this.locals = paramContext.locals;
/*     */ 
/*     */       
/*  96 */       this.classes = paramContext.classes;
/*  97 */       this.varNumber = paramContext.varNumber;
/*  98 */       this.field = paramContext.field;
/*  99 */       this.frameNumber = paramContext.frameNumber;
/* 100 */       paramContext.scopeNumber++;
/* 101 */       this.node = paramNode;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Context(Context paramContext) {
/* 106 */     this(paramContext, (Node)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int declare(Environment paramEnvironment, LocalMember paramLocalMember) {
/* 114 */     paramLocalMember.scopeNumber = this.scopeNumber;
/* 115 */     if (this.field == null && idThis.equals(paramLocalMember.getName())) {
/* 116 */       paramLocalMember.scopeNumber++;
/*     */     }
/* 118 */     if (paramLocalMember.isInnerClass()) {
/* 119 */       paramLocalMember.prev = this.classes;
/* 120 */       this.classes = paramLocalMember;
/* 121 */       return 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     paramLocalMember.prev = this.locals;
/* 149 */     this.locals = paramLocalMember;
/* 150 */     paramLocalMember.number = this.varNumber;
/* 151 */     this.varNumber += paramLocalMember.getType().stackSize();
/* 152 */     return paramLocalMember.number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalMember getLocalField(Identifier paramIdentifier) {
/* 160 */     for (LocalMember localMember = this.locals; localMember != null; localMember = localMember.prev) {
/* 161 */       if (paramIdentifier.equals(localMember.getName())) {
/* 162 */         return localMember;
/*     */       }
/*     */     } 
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScopeNumber(ClassDefinition paramClassDefinition) {
/* 175 */     for (Context context = this; context != null; context = context.prev) {
/* 176 */       if (context.field != null && 
/* 177 */         context.field.getClassDefinition() == paramClassDefinition) {
/* 178 */         return context.frameNumber;
/*     */       }
/*     */     } 
/* 181 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MemberDefinition getFieldCommon(Environment paramEnvironment, Identifier paramIdentifier, boolean paramBoolean) throws AmbiguousMember, ClassNotFound {
/* 191 */     LocalMember localMember = getLocalField(paramIdentifier);
/* 192 */     byte b = (localMember == null) ? -2 : localMember.scopeNumber;
/*     */     
/* 194 */     ClassDefinition classDefinition1 = this.field.getClassDefinition();
/*     */ 
/*     */     
/* 197 */     ClassDefinition classDefinition2 = classDefinition1;
/* 198 */     while (classDefinition2 != null) {
/*     */       
/* 200 */       MemberDefinition memberDefinition = classDefinition2.getVariable(paramEnvironment, paramIdentifier, classDefinition1);
/* 201 */       if (memberDefinition == null || getScopeNumber(classDefinition2) <= b || (
/* 202 */         paramBoolean && memberDefinition.getClassDefinition() != classDefinition2)) {
/*     */         classDefinition2 = classDefinition2.getOuterClass(); continue;
/*     */       } 
/* 205 */       return memberDefinition;
/*     */     } 
/*     */ 
/*     */     
/* 209 */     return localMember;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int declareFieldNumber(MemberDefinition paramMemberDefinition) {
/* 217 */     return declare((Environment)null, new LocalMember(paramMemberDefinition));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFieldNumber(MemberDefinition paramMemberDefinition) {
/* 225 */     for (LocalMember localMember = this.locals; localMember != null; localMember = localMember.prev) {
/* 226 */       if (localMember.getMember() == paramMemberDefinition) {
/* 227 */         return localMember.number;
/*     */       }
/*     */     } 
/* 230 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberDefinition getElement(int paramInt) {
/* 238 */     for (LocalMember localMember = this.locals; localMember != null; localMember = localMember.prev) {
/* 239 */       if (localMember.number == paramInt) {
/* 240 */         MemberDefinition memberDefinition = localMember.getMember();
/* 241 */         return (memberDefinition != null) ? memberDefinition : localMember;
/*     */       } 
/*     */     } 
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalMember getLocalClass(Identifier paramIdentifier) {
/* 252 */     for (LocalMember localMember = this.classes; localMember != null; localMember = localMember.prev) {
/* 253 */       if (paramIdentifier.equals(localMember.getName())) {
/* 254 */         return localMember;
/*     */       }
/*     */     } 
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MemberDefinition getClassCommon(Environment paramEnvironment, Identifier paramIdentifier, boolean paramBoolean) throws ClassNotFound {
/* 263 */     LocalMember localMember = getLocalClass(paramIdentifier);
/* 264 */     byte b = (localMember == null) ? -2 : localMember.scopeNumber;
/*     */ 
/*     */     
/* 267 */     ClassDefinition classDefinition = this.field.getClassDefinition();
/* 268 */     while (classDefinition != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 277 */       MemberDefinition memberDefinition = classDefinition.getInnerClass(paramEnvironment, paramIdentifier);
/* 278 */       if (memberDefinition == null || getScopeNumber(classDefinition) <= b || (
/* 279 */         paramBoolean && memberDefinition.getClassDefinition() != classDefinition)) {
/*     */         classDefinition = classDefinition.getOuterClass(); continue;
/*     */       } 
/* 282 */       return memberDefinition;
/*     */     } 
/*     */ 
/*     */     
/* 286 */     return localMember;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MemberDefinition getField(Environment paramEnvironment, Identifier paramIdentifier) throws AmbiguousMember, ClassNotFound {
/* 294 */     return getFieldCommon(paramEnvironment, paramIdentifier, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MemberDefinition getApparentField(Environment paramEnvironment, Identifier paramIdentifier) throws AmbiguousMember, ClassNotFound {
/* 303 */     return getFieldCommon(paramEnvironment, paramIdentifier, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInScope(LocalMember paramLocalMember) {
/* 310 */     for (LocalMember localMember = this.locals; localMember != null; localMember = localMember.prev) {
/* 311 */       if (paramLocalMember == localMember) {
/* 312 */         return true;
/*     */       }
/*     */     } 
/* 315 */     return false;
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
/*     */   public UplevelReference noteReference(Environment paramEnvironment, LocalMember paramLocalMember) {
/* 333 */     byte b = !isInScope(paramLocalMember) ? -1 : paramLocalMember.scopeNumber;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 339 */     UplevelReference uplevelReference = null;
/* 340 */     int i = -1;
/* 341 */     for (Context context = this; context != null; context = context.prev) {
/* 342 */       if (i != context.frameNumber) {
/*     */ 
/*     */         
/* 345 */         i = context.frameNumber;
/* 346 */         if (b >= i) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 351 */         ClassDefinition classDefinition = context.field.getClassDefinition();
/* 352 */         UplevelReference uplevelReference1 = classDefinition.getReference(paramLocalMember);
/* 353 */         uplevelReference1.noteReference(paramEnvironment, context);
/*     */ 
/*     */         
/* 356 */         if (uplevelReference == null)
/* 357 */           uplevelReference = uplevelReference1; 
/*     */       } 
/*     */     } 
/* 360 */     return uplevelReference;
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
/*     */   public Expression makeReference(Environment paramEnvironment, LocalMember paramLocalMember) {
/* 373 */     UplevelReference uplevelReference = noteReference(paramEnvironment, paramLocalMember);
/*     */ 
/*     */     
/* 376 */     if (uplevelReference != null)
/* 377 */       return uplevelReference.makeLocalReference(paramEnvironment, this); 
/* 378 */     if (idThis.equals(paramLocalMember.getName())) {
/* 379 */       return new ThisExpression(0L, paramLocalMember);
/*     */     }
/* 381 */     return new IdentifierExpression(0L, paramLocalMember);
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
/*     */   public Expression findOuterLink(Environment paramEnvironment, long paramLong, MemberDefinition paramMemberDefinition) {
/* 396 */     ClassDefinition classDefinition1 = paramMemberDefinition.getClassDefinition();
/*     */ 
/*     */ 
/*     */     
/* 400 */     ClassDefinition classDefinition2 = paramMemberDefinition.isStatic() ? null : (!paramMemberDefinition.isConstructor() ? classDefinition1 : (classDefinition1.isTopLevel() ? null : classDefinition1.getOuterClass()));
/* 401 */     if (classDefinition2 == null) {
/* 402 */       return null;
/*     */     }
/* 404 */     return findOuterLink(paramEnvironment, paramLong, classDefinition2, paramMemberDefinition, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean match(Environment paramEnvironment, ClassDefinition paramClassDefinition1, ClassDefinition paramClassDefinition2) {
/*     */     try {
/* 410 */       return (paramClassDefinition1 == paramClassDefinition2 || paramClassDefinition2
/* 411 */         .implementedBy(paramEnvironment, paramClassDefinition1.getClassDeclaration()));
/* 412 */     } catch (ClassNotFound classNotFound) {
/* 413 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression findOuterLink(Environment paramEnvironment, long paramLong, ClassDefinition paramClassDefinition, MemberDefinition paramMemberDefinition, boolean paramBoolean) {
/*     */     FieldExpression fieldExpression;
/* 421 */     if (this.field.isStatic()) {
/* 422 */       if (paramMemberDefinition == null) {
/*     */         
/* 424 */         Identifier identifier = paramClassDefinition.getName().getFlatName().getName();
/* 425 */         paramEnvironment.error(paramLong, "undef.var", Identifier.lookup(identifier, idThis));
/* 426 */       } else if (paramMemberDefinition.isConstructor()) {
/* 427 */         paramEnvironment.error(paramLong, "no.outer.arg", paramClassDefinition, paramMemberDefinition.getClassDeclaration());
/* 428 */       } else if (paramMemberDefinition.isMethod()) {
/* 429 */         paramEnvironment.error(paramLong, "no.static.meth.access", paramMemberDefinition, paramMemberDefinition
/* 430 */             .getClassDeclaration());
/*     */       } else {
/* 432 */         paramEnvironment.error(paramLong, "no.static.field.access", paramMemberDefinition.getName(), paramMemberDefinition
/* 433 */             .getClassDeclaration());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 442 */       ThisExpression thisExpression = new ThisExpression(paramLong, this);
/* 443 */       thisExpression.type = paramClassDefinition.getType();
/* 444 */       return thisExpression;
/*     */     } 
/*     */ 
/*     */     
/* 448 */     LocalMember localMember1 = this.locals;
/*     */ 
/*     */     
/* 451 */     ThisExpression thisExpression1 = null;
/*     */ 
/*     */     
/* 454 */     LocalMember localMember2 = null;
/*     */ 
/*     */     
/* 457 */     ClassDefinition classDefinition1 = null;
/*     */ 
/*     */     
/* 460 */     ClassDefinition classDefinition2 = null;
/* 461 */     if (this.field.isConstructor()) {
/* 462 */       classDefinition2 = this.field.getClassDefinition();
/*     */     }
/*     */     
/* 465 */     if (!this.field.isMethod()) {
/* 466 */       classDefinition1 = this.field.getClassDefinition();
/* 467 */       thisExpression1 = new ThisExpression(paramLong, this);
/*     */     } 
/*     */     while (true) {
/*     */       IdentifierExpression identifierExpression;
/* 471 */       if (thisExpression1 == null) {
/*     */         
/* 473 */         while (localMember1 != null && !idThis.equals(localMember1.getName())) {
/* 474 */           localMember1 = localMember1.prev;
/*     */         }
/* 476 */         if (localMember1 == null) {
/*     */           break;
/*     */         }
/* 479 */         thisExpression1 = new ThisExpression(paramLong, localMember1);
/* 480 */         classDefinition1 = localMember1.getClassDefinition();
/* 481 */         localMember2 = localMember1;
/* 482 */         localMember1 = localMember1.prev;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 488 */       if (classDefinition1 == paramClassDefinition || (!paramBoolean && 
/* 489 */         match(paramEnvironment, classDefinition1, paramClassDefinition))) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 495 */       MemberDefinition memberDefinition = classDefinition1.findOuterMember();
/* 496 */       if (memberDefinition == null) {
/* 497 */         thisExpression1 = null;
/*     */         continue;
/*     */       } 
/* 500 */       ClassDefinition classDefinition = classDefinition1;
/* 501 */       classDefinition1 = classDefinition.getOuterClass();
/*     */       
/* 503 */       if (classDefinition == classDefinition2) {
/*     */ 
/*     */ 
/*     */         
/* 507 */         Identifier identifier = memberDefinition.getName();
/* 508 */         IdentifierExpression identifierExpression1 = new IdentifierExpression(paramLong, identifier);
/* 509 */         identifierExpression1.bind(paramEnvironment, this);
/* 510 */         identifierExpression = identifierExpression1; continue;
/*     */       } 
/* 512 */       fieldExpression = new FieldExpression(paramLong, identifierExpression, memberDefinition);
/*     */     } 
/*     */     
/* 515 */     if (fieldExpression != null)
/*     */     {
/*     */ 
/*     */       
/* 519 */       return fieldExpression;
/*     */     }
/*     */     
/* 522 */     if (paramMemberDefinition == null) {
/*     */       
/* 524 */       Identifier identifier = paramClassDefinition.getName().getFlatName().getName();
/* 525 */       paramEnvironment.error(paramLong, "undef.var", Identifier.lookup(identifier, idThis));
/* 526 */     } else if (paramMemberDefinition.isConstructor()) {
/* 527 */       paramEnvironment.error(paramLong, "no.outer.arg", paramClassDefinition, paramMemberDefinition.getClassDefinition());
/*     */     } else {
/* 529 */       paramEnvironment.error(paramLong, "no.static.field.access", paramMemberDefinition, this.field);
/*     */     } 
/*     */ 
/*     */     
/* 533 */     ThisExpression thisExpression2 = new ThisExpression(paramLong, this);
/* 534 */     thisExpression2.type = paramClassDefinition.getType();
/* 535 */     return thisExpression2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean outerLinkExists(Environment paramEnvironment, ClassDefinition paramClassDefinition1, ClassDefinition paramClassDefinition2) {
/* 544 */     while (!match(paramEnvironment, paramClassDefinition2, paramClassDefinition1)) {
/* 545 */       if (paramClassDefinition2.isTopLevel()) {
/* 546 */         return false;
/*     */       }
/* 548 */       paramClassDefinition2 = paramClassDefinition2.getOuterClass();
/*     */     } 
/* 550 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDefinition findScope(Environment paramEnvironment, ClassDefinition paramClassDefinition) {
/* 557 */     ClassDefinition classDefinition = this.field.getClassDefinition();
/* 558 */     while (classDefinition != null && !match(paramEnvironment, classDefinition, paramClassDefinition)) {
/* 559 */       classDefinition = classDefinition.getOuterClass();
/*     */     }
/* 561 */     return classDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Identifier resolveName(Environment paramEnvironment, Identifier paramIdentifier) {
/* 571 */     if (paramIdentifier.isQualified()) {
/*     */ 
/*     */ 
/*     */       
/* 575 */       Identifier identifier = resolveName(paramEnvironment, paramIdentifier.getHead());
/*     */       
/* 577 */       if (identifier.hasAmbigPrefix())
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 582 */         return identifier;
/*     */       }
/*     */       
/* 585 */       if (!paramEnvironment.classExists(identifier)) {
/* 586 */         return paramEnvironment.resolvePackageQualifiedName(paramIdentifier);
/*     */       }
/*     */       try {
/* 589 */         return paramEnvironment.getClassDefinition(identifier)
/* 590 */           .resolveInnerClass(paramEnvironment, paramIdentifier.getTail());
/* 591 */       } catch (ClassNotFound classNotFound) {
/*     */         
/* 593 */         return Identifier.lookupInner(identifier, paramIdentifier.getTail());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 599 */       MemberDefinition memberDefinition = getClassCommon(paramEnvironment, paramIdentifier, false);
/* 600 */       if (memberDefinition != null) {
/* 601 */         return memberDefinition.getInnerClass().getName();
/*     */       }
/* 603 */     } catch (ClassNotFound classNotFound) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 608 */     return paramEnvironment.resolveName(paramIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Identifier getApparentClassName(Environment paramEnvironment, Identifier paramIdentifier) {
/* 619 */     if (paramIdentifier.isQualified()) {
/*     */ 
/*     */ 
/*     */       
/* 623 */       Identifier identifier1 = getApparentClassName(paramEnvironment, paramIdentifier.getHead());
/* 624 */       return (identifier1 == null) ? idNull : 
/* 625 */         Identifier.lookup(identifier1, paramIdentifier
/* 626 */           .getTail());
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 631 */       MemberDefinition memberDefinition = getClassCommon(paramEnvironment, paramIdentifier, true);
/* 632 */       if (memberDefinition != null) {
/* 633 */         return memberDefinition.getInnerClass().getName();
/*     */       }
/* 635 */     } catch (ClassNotFound classNotFound) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 640 */     Identifier identifier = this.field.getClassDefinition().getTopClass().getName();
/* 641 */     if (identifier.getName().equals(paramIdentifier)) {
/* 642 */       return identifier;
/*     */     }
/* 644 */     return idNull;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkBackBranch(Environment paramEnvironment, Statement paramStatement, Vset paramVset1, Vset paramVset2) {
/* 655 */     for (LocalMember localMember = this.locals; localMember != null; localMember = localMember.prev) {
/* 656 */       if (localMember.isBlankFinal() && paramVset1
/* 657 */         .testVarUnassigned(localMember.number) && 
/* 658 */         !paramVset2.testVarUnassigned(localMember.number)) {
/* 659 */         paramEnvironment.error(paramStatement.where, "assign.to.blank.final.in.loop", localMember
/* 660 */             .getName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canReach(Environment paramEnvironment, MemberDefinition paramMemberDefinition) {
/* 670 */     return this.field.canReach(paramEnvironment, paramMemberDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Context getLabelContext(Identifier paramIdentifier) {
/* 679 */     for (Context context = this; context != null; context = context.prev) {
/* 680 */       if (context.node != null && context.node instanceof Statement && (
/* 681 */         (Statement)context.node).hasLabel(paramIdentifier)) {
/* 682 */         return context;
/*     */       }
/*     */     } 
/* 685 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Context getBreakContext(Identifier paramIdentifier) {
/* 693 */     if (paramIdentifier != null) {
/* 694 */       return getLabelContext(paramIdentifier);
/*     */     }
/* 696 */     for (Context context = this; context != null; context = context.prev) {
/* 697 */       if (context.node != null) {
/* 698 */         switch (context.node.op) {
/*     */           case 92:
/*     */           case 93:
/*     */           case 94:
/*     */           case 95:
/* 703 */             return context;
/*     */         } 
/*     */       }
/*     */     } 
/* 707 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Context getContinueContext(Identifier paramIdentifier) {
/* 715 */     if (paramIdentifier != null) {
/* 716 */       return getLabelContext(paramIdentifier);
/*     */     }
/* 718 */     for (Context context = this; context != null; context = context.prev) {
/* 719 */       if (context.node != null) {
/* 720 */         switch (context.node.op) {
/*     */           case 92:
/*     */           case 93:
/*     */           case 94:
/* 724 */             return context;
/*     */         } 
/*     */       }
/*     */     } 
/* 728 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CheckContext getReturnContext() {
/* 736 */     for (Context context = this; context != null; context = context.prev) {
/*     */       
/* 738 */       if (context.node != null && context.node.op == 47) {
/* 739 */         return (CheckContext)context;
/*     */       }
/*     */     } 
/* 742 */     return null;
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
/*     */   public CheckContext getTryExitContext() {
/* 756 */     Context context = this;
/* 757 */     for (; context != null && context.node != null && context.node.op != 47; 
/* 758 */       context = context.prev) {
/* 759 */       if (context.node.op == 101) {
/* 760 */         return (CheckContext)context;
/*     */       }
/*     */     } 
/* 763 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Context getInlineContext() {
/* 770 */     for (Context context = this; context != null; context = context.prev) {
/* 771 */       if (context.node != null) {
/* 772 */         switch (context.node.op) {
/*     */           case 150:
/*     */           case 151:
/* 775 */             return context;
/*     */         } 
/*     */       }
/*     */     } 
/* 779 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Context getInlineMemberContext(MemberDefinition paramMemberDefinition) {
/* 786 */     for (Context context = this; context != null; context = context.prev) {
/* 787 */       if (context.node != null)
/* 788 */         switch (context.node.op) {
/*     */           case 150:
/* 790 */             if (((InlineMethodExpression)context.node).field.equals(paramMemberDefinition)) {
/* 791 */               return context;
/*     */             }
/*     */             break;
/*     */           case 151:
/* 795 */             if (((InlineNewInstanceExpression)context.node).field.equals(paramMemberDefinition)) {
/* 796 */               return context;
/*     */             }
/*     */             break;
/*     */         }  
/*     */     } 
/* 801 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Vset removeAdditionalVars(Vset paramVset) {
/* 809 */     return paramVset.removeAdditionalVars(this.varNumber);
/*     */   }
/*     */   
/*     */   public final int getVarNumber() {
/* 813 */     return this.varNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getThisNumber() {
/* 820 */     LocalMember localMember = getLocalField(idThis);
/* 821 */     if (localMember != null && localMember
/* 822 */       .getClassDefinition() == this.field.getClassDefinition()) {
/* 823 */       return localMember.number;
/*     */     }
/*     */     
/* 826 */     return this.varNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MemberDefinition getField() {
/* 833 */     return this.field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Environment newEnvironment(Environment paramEnvironment, Context paramContext) {
/* 843 */     return new ContextEnvironment(paramEnvironment, paramContext);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\Context.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */