/*     */ package sun.tools.javac;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.IdentifierToken;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ import sun.tools.tree.Context;
/*     */ import sun.tools.tree.Expression;
/*     */ import sun.tools.tree.ExpressionStatement;
/*     */ import sun.tools.tree.LocalMember;
/*     */ import sun.tools.tree.MethodExpression;
/*     */ import sun.tools.tree.Node;
/*     */ import sun.tools.tree.NullExpression;
/*     */ import sun.tools.tree.Statement;
/*     */ import sun.tools.tree.SuperExpression;
/*     */ import sun.tools.tree.UplevelReference;
/*     */ import sun.tools.tree.Vset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class SourceMember
/*     */   extends MemberDefinition
/*     */   implements Constants
/*     */ {
/*     */   Vector args;
/*     */   MemberDefinition abstractSource;
/*     */   int status;
/*     */   static final int PARSED = 0;
/*     */   static final int CHECKING = 1;
/*     */   static final int CHECKED = 2;
/*     */   static final int INLINING = 3;
/*     */   static final int INLINED = 4;
/*     */   static final int ERROR = 5;
/*     */   LocalMember outerThisArg;
/*     */   public boolean resolved;
/*     */   
/*     */   public Vector getArguments() {
/*  68 */     return this.args;
/*     */   } void createArgumentFields(Vector paramVector) { if (isMethod()) { this.args = new Vector(); if (isConstructor() || (!isStatic() && !isInitializer()))
/*     */         this.args.addElement(((SourceClass)this.clazz).getThisArgument());  if (paramVector != null) { Enumeration<Object> enumeration = paramVector.elements(); Type[] arrayOfType = getType().getArgumentTypes(); for (byte b = 0; b < arrayOfType.length; b++) { Identifier identifier2; int i; long l; Identifier identifier1 = (Identifier)enumeration.nextElement(); if (identifier1 instanceof LocalMember) { this.args = paramVector; return; }
/*     */            if (identifier1 instanceof Identifier) {
/*     */             identifier2 = identifier1; i = 0; l = getWhere();
/*     */           } else {
/*     */             IdentifierToken identifierToken = (IdentifierToken)identifier1; identifier2 = identifierToken.getName(); i = identifierToken.getModifiers(); l = identifierToken.getWhere();
/*     */           }  this.args.addElement(new LocalMember(l, this.clazz, i, arrayOfType[b], identifier2)); }
/*     */          }
/*     */        }
/*     */      } public LocalMember getOuterThisArg() { return this.outerThisArg; }
/*  79 */   public SourceMember(long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, Identifier paramIdentifier, Vector paramVector, IdentifierToken[] paramArrayOfIdentifierToken, Node paramNode) { super(paramLong, paramClassDefinition, paramInt, paramType, paramIdentifier, paramArrayOfIdentifierToken, paramNode);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     this.outerThisArg = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     this.resolved = false; this.documentation = paramString; this.args = paramVector; if (ClassDefinition.containsDeprecated(this.documentation)) this.modifiers |= 0x40000;  } void addOuterThis() { UplevelReference uplevelReference = this.clazz.getReferences(); while (uplevelReference != null && !uplevelReference.isClientOuterField()) uplevelReference = uplevelReference.getNext();  if (uplevelReference == null) return;  Type[] arrayOfType1 = this.type.getArgumentTypes(); Type[] arrayOfType2 = new Type[arrayOfType1.length + 1]; LocalMember localMember = uplevelReference.getLocalArgument(); this.outerThisArg = localMember; this.args.insertElementAt(localMember, 1); arrayOfType2[0] = localMember.getType(); for (byte b = 0; b < arrayOfType1.length; b++) arrayOfType2[b + 1] = arrayOfType1[b];  this.type = Type.tMethod(this.type.getReturnType(), arrayOfType2); } public SourceMember(ClassDefinition paramClassDefinition) { super(paramClassDefinition); this.outerThisArg = null; this.resolved = false; }
/*     */   void addUplevelArguments() { UplevelReference uplevelReference1 = this.clazz.getReferences(); this.clazz.getReferencesFrozen(); byte b1 = 0; for (UplevelReference uplevelReference2 = uplevelReference1; uplevelReference2 != null; uplevelReference2 = uplevelReference2.getNext()) { if (!uplevelReference2.isClientOuterField()) b1++;  }  if (b1 == 0) return;  Type[] arrayOfType1 = this.type.getArgumentTypes(); Type[] arrayOfType2 = new Type[arrayOfType1.length + b1]; byte b2 = 0; for (UplevelReference uplevelReference3 = uplevelReference1; uplevelReference3 != null; uplevelReference3 = uplevelReference3.getNext()) { if (!uplevelReference3.isClientOuterField()) { LocalMember localMember = uplevelReference3.getLocalArgument(); this.args.insertElementAt(localMember, 1 + b2); arrayOfType2[b2] = localMember.getType(); b2++; }  }  for (byte b3 = 0; b3 < arrayOfType1.length; b3++) arrayOfType2[b2 + b3] = arrayOfType1[b3];  this.type = Type.tMethod(this.type.getReturnType(), arrayOfType2); }
/*     */   public SourceMember(MemberDefinition paramMemberDefinition, ClassDefinition paramClassDefinition, Environment paramEnvironment) { this(paramMemberDefinition.getWhere(), paramClassDefinition, paramMemberDefinition.getDocumentation(), paramMemberDefinition.getModifiers() | 0x400, paramMemberDefinition.getType(), paramMemberDefinition.getName(), (Vector)null, paramMemberDefinition.getExceptionIds(), (Node)null); this.args = paramMemberDefinition.getArguments(); this.abstractSource = paramMemberDefinition; this.exp = paramMemberDefinition.getExceptions(paramEnvironment); }
/* 325 */   public ClassDeclaration[] getExceptions(Environment paramEnvironment) { if (!isMethod() || this.exp != null) return this.exp;  if (this.expIds == null) { this.exp = new ClassDeclaration[0]; return this.exp; }  paramEnvironment = ((SourceClass)getClassDefinition()).setupEnv(paramEnvironment); this.exp = new ClassDeclaration[this.expIds.length]; for (byte b = 0; b < this.exp.length; b++) { Identifier identifier1 = this.expIds[b].getName(); Identifier identifier2 = getClassDefinition().resolveName(paramEnvironment, identifier1); this.exp[b] = paramEnvironment.getClassDeclaration(identifier2); }  return this.exp; } public void setExceptions(ClassDeclaration[] paramArrayOfClassDeclaration) { this.exp = paramArrayOfClassDeclaration; } public void resolveTypeStructure(Environment paramEnvironment) { paramEnvironment.dtEnter("SourceMember.resolveTypeStructure: " + this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     if (this.resolved) {
/* 332 */       paramEnvironment.dtEvent("SourceMember.resolveTypeStructure: OK " + this);
/*     */ 
/*     */ 
/*     */       
/* 336 */       throw new CompilerError("multiple member type resolution");
/*     */     } 
/*     */     
/* 339 */     paramEnvironment.dtEvent("SourceMember.resolveTypeStructure: RESOLVING " + this);
/* 340 */     this.resolved = true;
/*     */ 
/*     */     
/* 343 */     super.resolveTypeStructure(paramEnvironment);
/* 344 */     if (isInnerClass()) {
/* 345 */       ClassDefinition classDefinition = getInnerClass();
/* 346 */       if (classDefinition instanceof SourceClass && !classDefinition.isLocal()) {
/* 347 */         ((SourceClass)classDefinition).resolveTypeStructure(paramEnvironment);
/*     */       }
/* 349 */       this.type = this.innerClass.getType();
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 356 */       this.type = paramEnvironment.resolveNames(getClassDefinition(), this.type, isSynthetic());
/*     */ 
/*     */       
/* 359 */       getExceptions(paramEnvironment);
/*     */       
/* 361 */       if (isMethod()) {
/* 362 */         Vector vector = this.args; this.args = null;
/* 363 */         createArgumentFields(vector);
/*     */         
/* 365 */         if (isConstructor()) {
/* 366 */           addOuterThis();
/*     */         }
/*     */       } 
/*     */     } 
/* 370 */     paramEnvironment.dtExit("SourceMember.resolveTypeStructure: " + this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDeclaration getDefiningClassDeclaration() {
/* 377 */     if (this.abstractSource == null) {
/* 378 */       return super.getDefiningClassDeclaration();
/*     */     }
/* 380 */     return this.abstractSource.getDefiningClassDeclaration();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean reportDeprecated(Environment paramEnvironment) {
/* 389 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void check(Environment paramEnvironment) throws ClassNotFound {
/* 400 */     paramEnvironment.dtEnter("SourceMember.check: " + 
/* 401 */         getName() + ", status = " + this.status);
/*     */     
/* 403 */     if (this.status == 0) {
/* 404 */       if (isSynthetic() && getValue() == null) {
/*     */         
/* 406 */         this.status = 2;
/*     */         
/* 408 */         paramEnvironment.dtExit("SourceMember.check: BREAKING CYCLE");
/*     */         return;
/*     */       } 
/* 411 */       paramEnvironment.dtEvent("SourceMember.check: CHECKING CLASS");
/* 412 */       this.clazz.check(paramEnvironment);
/* 413 */       if (this.status == 0) {
/* 414 */         if (getClassDefinition().getError()) {
/* 415 */           this.status = 5;
/*     */         } else {
/*     */           
/* 418 */           paramEnvironment.dtExit("SourceMember.check: CHECK FAILED");
/* 419 */           throw new CompilerError("check failed");
/*     */         } 
/*     */       }
/*     */     } 
/* 423 */     paramEnvironment.dtExit("SourceMember.check: DONE " + 
/* 424 */         getName() + ", status = " + this.status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset) throws ClassNotFound {
/* 434 */     paramEnvironment.dtEvent("SourceMember.check: MEMBER " + 
/* 435 */         getName() + ", status = " + this.status);
/* 436 */     if (this.status == 0) {
/* 437 */       if (isInnerClass()) {
/*     */         
/* 439 */         ClassDefinition classDefinition = getInnerClass();
/* 440 */         if (classDefinition instanceof SourceClass && !classDefinition.isLocal() && classDefinition
/* 441 */           .isInsideLocal()) {
/* 442 */           this.status = 1;
/* 443 */           paramVset = ((SourceClass)classDefinition).checkInsideClass(paramEnvironment, paramContext, paramVset);
/*     */         } 
/* 445 */         this.status = 2;
/* 446 */         return paramVset;
/*     */       } 
/* 448 */       if (paramEnvironment.dump()) {
/* 449 */         System.out.println("[check field " + getClassDeclaration().getName() + "." + getName() + "]");
/* 450 */         if (getValue() != null) {
/* 451 */           getValue().print(System.out);
/* 452 */           System.out.println();
/*     */         } 
/*     */       } 
/* 455 */       paramEnvironment = new Environment(paramEnvironment, this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 461 */       paramEnvironment.resolve(this.where, getClassDefinition(), getType());
/*     */ 
/*     */ 
/*     */       
/* 465 */       if (isMethod()) {
/*     */         
/* 467 */         ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(idJavaLangThrowable);
/* 468 */         ClassDeclaration[] arrayOfClassDeclaration = getExceptions(paramEnvironment);
/* 469 */         for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/*     */           ClassDefinition classDefinition;
/* 471 */           long l = getWhere();
/* 472 */           if (this.expIds != null && b < this.expIds.length) {
/* 473 */             l = IdentifierToken.getWhere(this.expIds[b], l);
/*     */           }
/*     */           try {
/* 476 */             classDefinition = arrayOfClassDeclaration[b].getClassDefinition(paramEnvironment);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 482 */             paramEnvironment.resolveByName(l, getClassDefinition(), classDefinition.getName());
/*     */           }
/* 484 */           catch (ClassNotFound classNotFound) {
/* 485 */             paramEnvironment.error(l, "class.not.found", classNotFound.name, "throws");
/*     */             break;
/*     */           } 
/* 488 */           classDefinition.noteUsedBy(getClassDefinition(), l, paramEnvironment);
/*     */           
/* 490 */           if (!getClassDefinition().canAccess(paramEnvironment, classDefinition.getClassDeclaration())) {
/* 491 */             paramEnvironment.error(l, "cant.access.class", classDefinition);
/* 492 */           } else if (!classDefinition.subClassOf(paramEnvironment, classDeclaration)) {
/* 493 */             paramEnvironment.error(l, "throws.not.throwable", classDefinition);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 498 */       this.status = 1;
/*     */       
/* 500 */       if (isMethod() && this.args != null) {
/* 501 */         int i = this.args.size();
/*     */         
/* 503 */         for (byte b = 0; b < i; b++) {
/* 504 */           LocalMember localMember = this.args.elementAt(b);
/* 505 */           Identifier identifier = localMember.getName();
/* 506 */           for (int j = b + 1; j < i; j++) {
/* 507 */             LocalMember localMember1 = this.args.elementAt(j);
/* 508 */             Identifier identifier1 = localMember1.getName();
/* 509 */             if (identifier.equals(identifier1)) {
/* 510 */               paramEnvironment.error(localMember1.getWhere(), "duplicate.argument", identifier);
/*     */               
/*     */               // Byte code: goto -> 537
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 518 */       if (getValue() != null) {
/* 519 */         paramContext = new Context(paramContext, this);
/*     */         
/* 521 */         if (isMethod()) {
/* 522 */           Statement statement = (Statement)getValue();
/*     */ 
/*     */ 
/*     */           
/* 526 */           for (Enumeration<LocalMember> enumeration = this.args.elements(); enumeration.hasMoreElements(); ) {
/* 527 */             LocalMember localMember = enumeration.nextElement();
/* 528 */             paramVset.addVar(paramContext.declare(paramEnvironment, localMember));
/*     */           } 
/*     */           
/* 531 */           if (isConstructor()) {
/*     */ 
/*     */             
/* 534 */             paramVset.clearVar(paramContext.getThisNumber());
/*     */ 
/*     */ 
/*     */             
/* 538 */             Expression expression = statement.firstConstructor();
/* 539 */             if (expression == null && 
/* 540 */               getClassDefinition().getSuperClass() != null) {
/* 541 */               expression = getDefaultSuperCall(paramEnvironment);
/* 542 */               ExpressionStatement expressionStatement = new ExpressionStatement(this.where, expression);
/*     */               
/* 544 */               statement = Statement.insertStatement((Statement)expressionStatement, statement);
/* 545 */               setValue((Node)statement);
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 550 */           ClassDeclaration[] arrayOfClassDeclaration = getExceptions(paramEnvironment);
/* 551 */           byte b = (arrayOfClassDeclaration.length > 3) ? 17 : 7;
/* 552 */           Hashtable<Object, Object> hashtable = new Hashtable<>(b);
/*     */           
/* 554 */           paramVset = statement.checkMethod(paramEnvironment, paramContext, paramVset, hashtable);
/*     */ 
/*     */           
/* 557 */           ClassDeclaration classDeclaration1 = paramEnvironment.getClassDeclaration(idJavaLangError);
/*     */           
/* 559 */           ClassDeclaration classDeclaration2 = paramEnvironment.getClassDeclaration(idJavaLangRuntimeException);
/*     */           
/* 561 */           for (Enumeration<ClassDeclaration> enumeration1 = hashtable.keys(); enumeration1.hasMoreElements(); ) {
/* 562 */             ClassDeclaration classDeclaration = enumeration1.nextElement();
/* 563 */             ClassDefinition classDefinition = classDeclaration.getClassDefinition(paramEnvironment);
/* 564 */             if (classDefinition.subClassOf(paramEnvironment, classDeclaration1) || classDefinition
/* 565 */               .subClassOf(paramEnvironment, classDeclaration2)) {
/*     */               continue;
/*     */             }
/*     */             
/* 569 */             boolean bool = false;
/* 570 */             if (!isInitializer()) {
/* 571 */               for (byte b1 = 0; b1 < arrayOfClassDeclaration.length; b1++) {
/* 572 */                 if (classDefinition.subClassOf(paramEnvironment, arrayOfClassDeclaration[b1])) {
/* 573 */                   bool = true;
/*     */                 }
/*     */               } 
/*     */             }
/* 577 */             if (!bool) {
/* 578 */               String str; Node node = (Node)hashtable.get(classDeclaration);
/* 579 */               long l = node.getWhere();
/*     */ 
/*     */               
/* 582 */               if (isConstructor()) {
/* 583 */                 if (l == 
/* 584 */                   getClassDefinition().getWhere()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 593 */                   str = "def.constructor.exception";
/*     */                 } else {
/*     */                   
/* 596 */                   str = "constructor.exception";
/*     */                 } 
/* 598 */               } else if (isInitializer()) {
/*     */                 
/* 600 */                 str = "initializer.exception";
/*     */               } else {
/*     */                 
/* 603 */                 str = "uncaught.exception";
/*     */               } 
/* 605 */               paramEnvironment.error(l, str, classDeclaration.getName());
/*     */             } 
/*     */           } 
/*     */         } else {
/* 609 */           Hashtable<Object, Object> hashtable = new Hashtable<>(3);
/* 610 */           Expression expression = (Expression)getValue();
/*     */           
/* 612 */           paramVset = expression.checkInitializer(paramEnvironment, paramContext, paramVset, 
/* 613 */               getType(), hashtable);
/* 614 */           setValue((Node)expression.convert(paramEnvironment, paramContext, getType(), expression));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 622 */           if (isStatic() && isFinal() && !this.clazz.isTopLevel() && 
/* 623 */             !((Expression)getValue()).isConstant()) {
/* 624 */             paramEnvironment.error(this.where, "static.inner.field", getName(), this);
/* 625 */             setValue(null);
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 633 */           ClassDeclaration classDeclaration1 = paramEnvironment.getClassDeclaration(idJavaLangThrowable);
/*     */           
/* 635 */           ClassDeclaration classDeclaration2 = paramEnvironment.getClassDeclaration(idJavaLangError);
/*     */           
/* 637 */           ClassDeclaration classDeclaration3 = paramEnvironment.getClassDeclaration(idJavaLangRuntimeException);
/*     */           
/* 639 */           for (Enumeration<ClassDeclaration> enumeration = hashtable.keys(); enumeration.hasMoreElements(); ) {
/* 640 */             ClassDeclaration classDeclaration = enumeration.nextElement();
/* 641 */             ClassDefinition classDefinition = classDeclaration.getClassDefinition(paramEnvironment);
/*     */             
/* 643 */             if (!classDefinition.subClassOf(paramEnvironment, classDeclaration2) && 
/* 644 */               !classDefinition.subClassOf(paramEnvironment, classDeclaration3) && classDefinition
/* 645 */               .subClassOf(paramEnvironment, classDeclaration1)) {
/* 646 */               Node node = (Node)hashtable.get(classDeclaration);
/* 647 */               paramEnvironment.error(node.getWhere(), "initializer.exception", classDeclaration
/* 648 */                   .getName());
/*     */             } 
/*     */           } 
/*     */         } 
/* 652 */         if (paramEnvironment.dump()) {
/* 653 */           getValue().print(System.out);
/* 654 */           System.out.println();
/*     */         } 
/*     */       } 
/* 657 */       this.status = getClassDefinition().getError() ? 5 : 2;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 662 */     if (isInitializer() && paramVset.isDeadEnd()) {
/* 663 */       paramEnvironment.error(this.where, "init.no.normal.completion");
/* 664 */       paramVset = paramVset.clearDeadEnd();
/*     */     } 
/*     */     
/* 667 */     return paramVset;
/*     */   }
/*     */ 
/*     */   
/*     */   private Expression getDefaultSuperCall(Environment paramEnvironment) {
/* 672 */     SuperExpression superExpression = null;
/* 673 */     ClassDefinition classDefinition1 = getClassDefinition().getSuperClass().getClassDefinition();
/*     */ 
/*     */ 
/*     */     
/* 677 */     ClassDefinition classDefinition2 = (classDefinition1 == null) ? null : (classDefinition1.isTopLevel() ? null : classDefinition1.getOuterClass());
/* 678 */     ClassDefinition classDefinition3 = getClassDefinition();
/* 679 */     if (classDefinition2 != null && !Context.outerLinkExists(paramEnvironment, classDefinition2, classDefinition3)) {
/* 680 */       superExpression = new SuperExpression(this.where, (Expression)new NullExpression(this.where));
/* 681 */       paramEnvironment.error(this.where, "no.default.outer.arg", classDefinition2, getClassDefinition());
/*     */     } 
/* 683 */     if (superExpression == null) {
/* 684 */       superExpression = new SuperExpression(this.where);
/*     */     }
/* 686 */     return (Expression)new MethodExpression(this.where, (Expression)superExpression, idInit, new Expression[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void inline(Environment paramEnvironment) throws ClassNotFound {
/* 693 */     switch (this.status) {
/*     */       case 0:
/* 695 */         check(paramEnvironment);
/* 696 */         inline(paramEnvironment);
/*     */         break;
/*     */       
/*     */       case 2:
/* 700 */         if (paramEnvironment.dump()) {
/* 701 */           System.out.println("[inline field " + getClassDeclaration().getName() + "." + getName() + "]");
/*     */         }
/* 703 */         this.status = 3;
/* 704 */         paramEnvironment = new Environment(paramEnvironment, this);
/*     */         
/* 706 */         if (isMethod())
/* 707 */         { if (!isNative() && !isAbstract()) {
/* 708 */             Statement statement = (Statement)getValue();
/* 709 */             Context context = new Context((Context)null, this);
/* 710 */             for (Enumeration<LocalMember> enumeration = this.args.elements(); enumeration.hasMoreElements(); ) {
/* 711 */               LocalMember localMember = enumeration.nextElement();
/* 712 */               context.declare(paramEnvironment, localMember);
/*     */             } 
/* 714 */             setValue((Node)statement.inline(paramEnvironment, context));
/*     */           }  }
/* 716 */         else { if (isInnerClass()) {
/*     */             
/* 718 */             ClassDefinition classDefinition = getInnerClass();
/* 719 */             if (classDefinition instanceof SourceClass && !classDefinition.isLocal() && classDefinition
/* 720 */               .isInsideLocal()) {
/* 721 */               this.status = 3;
/* 722 */               ((SourceClass)classDefinition).inlineLocalClass(paramEnvironment);
/*     */             } 
/* 724 */             this.status = 4;
/*     */             break;
/*     */           } 
/* 727 */           if (getValue() != null) {
/* 728 */             Context context = new Context((Context)null, this);
/* 729 */             if (!isStatic()) {
/*     */               
/* 731 */               Context context1 = new Context(context, this);
/*     */               
/* 733 */               LocalMember localMember = ((SourceClass)this.clazz).getThisArgument();
/* 734 */               context1.declare(paramEnvironment, localMember);
/* 735 */               setValue((Node)((Expression)getValue())
/* 736 */                   .inlineValue(paramEnvironment, context1));
/*     */             } else {
/* 738 */               setValue((Node)((Expression)getValue())
/* 739 */                   .inlineValue(paramEnvironment, context));
/*     */             } 
/*     */           }  }
/*     */         
/* 743 */         if (paramEnvironment.dump()) {
/* 744 */           System.out.println("[inlined field " + getClassDeclaration().getName() + "." + getName() + "]");
/* 745 */           if (getValue() != null) {
/* 746 */             getValue().print(System.out);
/* 747 */             System.out.println();
/*     */           } else {
/* 749 */             System.out.println("<empty>");
/*     */           } 
/*     */         } 
/* 752 */         this.status = 4;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getValue(Environment paramEnvironment) throws ClassNotFound {
/* 761 */     Node node = getValue();
/* 762 */     if (node != null && this.status != 4) {
/*     */       
/* 764 */       paramEnvironment = ((SourceClass)this.clazz).setupEnv(paramEnvironment);
/* 765 */       inline(paramEnvironment);
/* 766 */       node = (this.status == 4) ? getValue() : null;
/*     */     } 
/* 768 */     return node;
/*     */   }
/*     */   
/*     */   public boolean isInlineable(Environment paramEnvironment, boolean paramBoolean) throws ClassNotFound {
/* 772 */     if (super.isInlineable(paramEnvironment, paramBoolean)) {
/* 773 */       getValue(paramEnvironment);
/* 774 */       return (this.status == 4 && !getClassDefinition().getError());
/*     */     } 
/* 776 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getInitialValue() {
/* 784 */     if (isMethod() || getValue() == null || !isFinal() || this.status != 4) {
/* 785 */       return null;
/*     */     }
/* 787 */     return ((Expression)getValue()).getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Assembler paramAssembler) throws ClassNotFound {
/* 794 */     switch (this.status) {
/*     */       case 0:
/* 796 */         check(paramEnvironment);
/* 797 */         code(paramEnvironment, paramAssembler);
/*     */         return;
/*     */       
/*     */       case 2:
/* 801 */         inline(paramEnvironment);
/* 802 */         code(paramEnvironment, paramAssembler);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 807 */         if (paramEnvironment.dump()) {
/* 808 */           System.out.println("[code field " + getClassDeclaration().getName() + "." + getName() + "]");
/*     */         }
/* 810 */         if (isMethod() && !isNative() && !isAbstract()) {
/* 811 */           paramEnvironment = new Environment(paramEnvironment, this);
/* 812 */           Context context = new Context((Context)null, this);
/* 813 */           Statement statement = (Statement)getValue();
/*     */           
/* 815 */           for (Enumeration<LocalMember> enumeration = this.args.elements(); enumeration.hasMoreElements(); ) {
/* 816 */             LocalMember localMember = enumeration.nextElement();
/* 817 */             context.declare(paramEnvironment, localMember);
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
/* 839 */           if (statement != null) {
/* 840 */             statement.code(paramEnvironment, context, paramAssembler);
/*     */           }
/* 842 */           if (getType().getReturnType().isType(11) && !isInitializer()) {
/* 843 */             paramAssembler.add(getWhere(), 177, true);
/*     */           }
/*     */         } 
/*     */         return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void codeInit(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) throws ClassNotFound {
/* 851 */     if (isMethod()) {
/*     */       return;
/*     */     }
/* 854 */     switch (this.status) {
/*     */       case 0:
/* 856 */         check(paramEnvironment);
/* 857 */         codeInit(paramEnvironment, paramContext, paramAssembler);
/*     */         return;
/*     */       
/*     */       case 2:
/* 861 */         inline(paramEnvironment);
/* 862 */         codeInit(paramEnvironment, paramContext, paramAssembler);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 867 */         if (paramEnvironment.dump()) {
/* 868 */           System.out.println("[code initializer  " + getClassDeclaration().getName() + "." + getName() + "]");
/*     */         }
/* 870 */         if (getValue() != null) {
/* 871 */           Expression expression = (Expression)getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 877 */           if (isStatic()) {
/* 878 */             if (getInitialValue() == null)
/*     */             {
/* 880 */               expression.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 881 */               paramAssembler.add(getWhere(), 179, this);
/*     */             
/*     */             }
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 888 */             paramAssembler.add(getWhere(), 25, new Integer(0));
/* 889 */             expression.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 890 */             paramAssembler.add(getWhere(), 181, this);
/*     */           } 
/*     */         } 
/*     */         return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 901 */     super.print(paramPrintStream);
/* 902 */     if (getValue() != null) {
/* 903 */       getValue().print(paramPrintStream);
/* 904 */       paramPrintStream.println();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\javac\SourceMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */