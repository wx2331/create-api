/*      */ package sun.tools.tree;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.Hashtable;
/*      */ import sun.tools.asm.Assembler;
/*      */ import sun.tools.java.AmbiguousClass;
/*      */ import sun.tools.java.AmbiguousMember;
/*      */ import sun.tools.java.ClassDeclaration;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Environment;
/*      */ import sun.tools.java.Identifier;
/*      */ import sun.tools.java.MemberDefinition;
/*      */ import sun.tools.java.Type;
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
/*      */ public class FieldExpression
/*      */   extends UnaryExpression
/*      */ {
/*      */   Identifier id;
/*      */   MemberDefinition field;
/*      */   Expression implementation;
/*      */   ClassDefinition clazz;
/*      */   private ClassDefinition superBase;
/*      */   
/*      */   public FieldExpression(long paramLong, Expression paramExpression, Identifier paramIdentifier) {
/*   55 */     super(46, paramLong, Type.tError, paramExpression);
/*   56 */     this.id = paramIdentifier;
/*      */   }
/*      */   public FieldExpression(long paramLong, Expression paramExpression, MemberDefinition paramMemberDefinition) {
/*   59 */     super(46, paramLong, paramMemberDefinition.getType(), paramExpression);
/*   60 */     this.id = paramMemberDefinition.getName();
/*   61 */     this.field = paramMemberDefinition;
/*      */   }
/*      */   
/*      */   public Expression getImplementation() {
/*   65 */     if (this.implementation != null)
/*   66 */       return this.implementation; 
/*   67 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isQualSuper() {
/*   75 */     return (this.superBase != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Identifier toIdentifier(Expression paramExpression) {
/*   82 */     StringBuffer stringBuffer = new StringBuffer();
/*   83 */     while (paramExpression.op == 46) {
/*   84 */       FieldExpression fieldExpression = (FieldExpression)paramExpression;
/*   85 */       if (fieldExpression.id == idThis || fieldExpression.id == idClass) {
/*   86 */         return null;
/*      */       }
/*   88 */       stringBuffer.insert(0, fieldExpression.id);
/*   89 */       stringBuffer.insert(0, '.');
/*   90 */       paramExpression = fieldExpression.right;
/*      */     } 
/*   92 */     if (paramExpression.op != 60) {
/*   93 */       return null;
/*      */     }
/*   95 */     stringBuffer.insert(0, ((IdentifierExpression)paramExpression).id);
/*   96 */     return Identifier.lookup(stringBuffer.toString());
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
/*      */   Type toType(Environment paramEnvironment, Context paramContext) {
/*  205 */     Identifier identifier = toIdentifier(this);
/*  206 */     if (identifier == null) {
/*  207 */       paramEnvironment.error(this.where, "invalid.type.expr");
/*  208 */       return Type.tError;
/*      */     } 
/*  210 */     Type type = Type.tClass(paramContext.resolveName(paramEnvironment, identifier));
/*  211 */     if (paramEnvironment.resolve(this.where, paramContext.field.getClassDefinition(), type)) {
/*  212 */       return type;
/*      */     }
/*  214 */     return Type.tError;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vset checkAmbigName(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression) {
/*  224 */     if (this.id == idThis || this.id == idClass) {
/*  225 */       paramUnaryExpression = null;
/*      */     }
/*  227 */     return checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable, paramUnaryExpression, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  236 */     paramVset = checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable, (UnaryExpression)null, false);
/*  237 */     if (this.id == idSuper && this.type != Type.tError)
/*      */     {
/*      */       
/*  240 */       paramEnvironment.error(this.where, "undef.var.super", idSuper);
/*      */     }
/*  242 */     return paramVset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void reportFailedPackagePrefix(Environment paramEnvironment, Expression paramExpression) {
/*  252 */     reportFailedPackagePrefix(paramEnvironment, paramExpression, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void reportFailedPackagePrefix(Environment paramEnvironment, Expression paramExpression, boolean paramBoolean) {
/*  259 */     Expression expression = paramExpression;
/*  260 */     while (expression instanceof UnaryExpression)
/*  261 */       expression = ((UnaryExpression)expression).right; 
/*  262 */     IdentifierExpression identifierExpression = (IdentifierExpression)expression;
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  267 */       paramEnvironment.resolve(identifierExpression.id);
/*  268 */     } catch (AmbiguousClass ambiguousClass) {
/*  269 */       paramEnvironment.error(paramExpression.where, "ambig.class", ambiguousClass.name1, ambiguousClass.name2);
/*      */       return;
/*  271 */     } catch (ClassNotFound classNotFound) {}
/*      */ 
/*      */     
/*  274 */     if (expression == paramExpression) {
/*  275 */       if (paramBoolean) {
/*  276 */         paramEnvironment.error(identifierExpression.where, "undef.class", identifierExpression.id);
/*      */       } else {
/*  278 */         paramEnvironment.error(identifierExpression.where, "undef.var.or.class", identifierExpression.id);
/*      */       }
/*      */     
/*  281 */     } else if (paramBoolean) {
/*  282 */       paramEnvironment.error(identifierExpression.where, "undef.class.or.package", identifierExpression.id);
/*      */     } else {
/*  284 */       paramEnvironment.error(identifierExpression.where, "undef.var.class.or.package", identifierExpression.id);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Expression implementFieldAccess(Environment paramEnvironment, Context paramContext, Expression paramExpression, boolean paramBoolean) {
/*  295 */     ClassDefinition classDefinition = accessBase(paramEnvironment, paramContext);
/*  296 */     if (classDefinition != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  306 */       if (this.field.isFinal()) {
/*  307 */         Expression expression = (Expression)this.field.getValue();
/*      */ 
/*      */ 
/*      */         
/*  311 */         if (expression != null && expression.isConstant() && !paramBoolean) {
/*  312 */           return expression.copyInline(paramContext);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  317 */       MemberDefinition memberDefinition = classDefinition.getAccessMember(paramEnvironment, paramContext, this.field, isQualSuper());
/*      */ 
/*      */       
/*  320 */       if (!paramBoolean) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  330 */         if (this.field.isStatic()) {
/*  331 */           Expression[] arrayOfExpression1 = new Expression[0];
/*  332 */           MethodExpression methodExpression = new MethodExpression(this.where, null, memberDefinition, arrayOfExpression1);
/*      */           
/*  334 */           return new CommaExpression(this.where, paramExpression, methodExpression);
/*      */         } 
/*  336 */         Expression[] arrayOfExpression = { paramExpression };
/*  337 */         return new MethodExpression(this.where, null, memberDefinition, arrayOfExpression);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  342 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassDefinition accessBase(Environment paramEnvironment, Context paramContext) {
/*  350 */     if (this.field.isPrivate()) {
/*  351 */       ClassDefinition classDefinition1 = this.field.getClassDefinition();
/*  352 */       ClassDefinition classDefinition2 = paramContext.field.getClassDefinition();
/*  353 */       if (classDefinition1 == classDefinition2)
/*      */       {
/*      */         
/*  356 */         return null;
/*      */       }
/*      */       
/*  359 */       return classDefinition1;
/*  360 */     }  if (this.field.isProtected()) {
/*  361 */       if (this.superBase == null)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  366 */         return null;
/*      */       }
/*  368 */       ClassDefinition classDefinition1 = this.field.getClassDefinition();
/*  369 */       ClassDefinition classDefinition2 = paramContext.field.getClassDefinition();
/*  370 */       if (classDefinition1.inSamePackage(classDefinition2))
/*      */       {
/*  372 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  381 */       return this.superBase;
/*      */     } 
/*      */     
/*  384 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isTypeAccessible(long paramLong, Environment paramEnvironment, Type paramType, ClassDefinition paramClassDefinition) {
/*  395 */     switch (paramType.getTypeCode()) {
/*      */       case 10:
/*      */         
/*  398 */         try { Identifier identifier = paramType.getClassName();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  403 */           ClassDefinition classDefinition = paramEnvironment.getClassDefinition(paramType);
/*  404 */           return paramClassDefinition.canAccess(paramEnvironment, classDefinition.getClassDeclaration()); }
/*  405 */         catch (ClassNotFound classNotFound)
/*  406 */         { return true; } 
/*      */       case 9:
/*  408 */         return isTypeAccessible(paramLong, paramEnvironment, paramType.getElementType(), paramClassDefinition);
/*      */     } 
/*  410 */     return true;
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
/*      */   private Vset checkCommon(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression, boolean paramBoolean) {
/*  423 */     if (this.id == idClass) {
/*      */       String str2;
/*      */       
/*  426 */       Type type = this.right.toType(paramEnvironment, paramContext);
/*      */       
/*  428 */       if (!type.isType(10) && !type.isType(9)) {
/*  429 */         Identifier identifier; TypeExpression typeExpression; if (type.isType(13)) {
/*  430 */           this.type = Type.tClassDesc;
/*  431 */           return paramVset;
/*      */         } 
/*  433 */         String str = null;
/*  434 */         switch (type.getTypeCode()) { case 11:
/*  435 */             str = "Void";
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
/*  448 */             identifier = Identifier.lookup(idJavaLang + "." + str);
/*  449 */             typeExpression = new TypeExpression(this.where, Type.tClass(identifier));
/*  450 */             this.implementation = new FieldExpression(this.where, typeExpression, idTYPE);
/*  451 */             paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  452 */             this.type = this.implementation.type;
/*  453 */             return paramVset;case 0: str = "Boolean"; identifier = Identifier.lookup(idJavaLang + "." + str); typeExpression = new TypeExpression(this.where, Type.tClass(identifier)); this.implementation = new FieldExpression(this.where, typeExpression, idTYPE); paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); this.type = this.implementation.type; return paramVset;case 1: str = "Byte"; identifier = Identifier.lookup(idJavaLang + "." + str); typeExpression = new TypeExpression(this.where, Type.tClass(identifier)); this.implementation = new FieldExpression(this.where, typeExpression, idTYPE); paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); this.type = this.implementation.type; return paramVset;case 2: str = "Character"; identifier = Identifier.lookup(idJavaLang + "." + str); typeExpression = new TypeExpression(this.where, Type.tClass(identifier)); this.implementation = new FieldExpression(this.where, typeExpression, idTYPE); paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); this.type = this.implementation.type; return paramVset;case 3: str = "Short"; identifier = Identifier.lookup(idJavaLang + "." + str); typeExpression = new TypeExpression(this.where, Type.tClass(identifier)); this.implementation = new FieldExpression(this.where, typeExpression, idTYPE); paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); this.type = this.implementation.type; return paramVset;case 4: str = "Integer"; identifier = Identifier.lookup(idJavaLang + "." + str); typeExpression = new TypeExpression(this.where, Type.tClass(identifier)); this.implementation = new FieldExpression(this.where, typeExpression, idTYPE); paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); this.type = this.implementation.type; return paramVset;case 6: str = "Float"; identifier = Identifier.lookup(idJavaLang + "." + str); typeExpression = new TypeExpression(this.where, Type.tClass(identifier)); this.implementation = new FieldExpression(this.where, typeExpression, idTYPE); paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); this.type = this.implementation.type; return paramVset;case 5: str = "Long"; identifier = Identifier.lookup(idJavaLang + "." + str); typeExpression = new TypeExpression(this.where, Type.tClass(identifier)); this.implementation = new FieldExpression(this.where, typeExpression, idTYPE); paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); this.type = this.implementation.type; return paramVset;case 7: str = "Double"; identifier = Identifier.lookup(idJavaLang + "." + str); typeExpression = new TypeExpression(this.where, Type.tClass(identifier)); this.implementation = new FieldExpression(this.where, typeExpression, idTYPE); paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); this.type = this.implementation.type; return paramVset; }
/*      */          paramEnvironment.error(this.right.where, "invalid.type.expr");
/*      */         return paramVset;
/*      */       } 
/*  457 */       if (type.isVoidArray()) {
/*  458 */         this.type = Type.tClassDesc;
/*  459 */         paramEnvironment.error(this.right.where, "void.array");
/*  460 */         return paramVset;
/*      */       } 
/*      */ 
/*      */       
/*  464 */       long l = paramContext.field.getWhere();
/*  465 */       ClassDefinition classDefinition1 = paramContext.field.getClassDefinition();
/*  466 */       MemberDefinition memberDefinition = classDefinition1.getClassLiteralLookup(l);
/*      */       
/*  468 */       String str1 = type.getTypeSignature();
/*      */       
/*  470 */       if (type.isType(10)) {
/*      */ 
/*      */ 
/*      */         
/*  474 */         str2 = str1.substring(1, str1.length() - 1).replace('/', '.');
/*      */       }
/*      */       else {
/*      */         
/*  478 */         str2 = str1.replace('/', '.');
/*      */       } 
/*      */       
/*  481 */       if (classDefinition1.isInterface()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  487 */         this
/*  488 */           .implementation = makeClassLiteralInlineRef(paramEnvironment, paramContext, memberDefinition, str2);
/*      */       }
/*      */       else {
/*      */         
/*  492 */         ClassDefinition classDefinition2 = memberDefinition.getClassDefinition();
/*      */         
/*  494 */         MemberDefinition memberDefinition1 = getClassLiteralCache(paramEnvironment, paramContext, str2, classDefinition2);
/*  495 */         this
/*  496 */           .implementation = makeClassLiteralCacheRef(paramEnvironment, paramContext, memberDefinition, memberDefinition1, str2);
/*      */       } 
/*      */       
/*  499 */       paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  500 */       this.type = this.implementation.type;
/*  501 */       return paramVset;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  506 */     if (this.field != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  515 */       this.implementation = implementFieldAccess(paramEnvironment, paramContext, this.right, paramBoolean);
/*  516 */       return (this.right == null) ? paramVset : this.right
/*  517 */         .checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/*      */     } 
/*      */ 
/*      */     
/*  521 */     paramVset = this.right.checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/*  522 */     if (this.right.type == Type.tPackage) {
/*      */       
/*  524 */       if (paramUnaryExpression == null) {
/*  525 */         reportFailedPackagePrefix(paramEnvironment, this.right);
/*  526 */         return paramVset;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  532 */       Identifier identifier = toIdentifier(this);
/*  533 */       if (identifier != null && paramEnvironment.classExists(identifier)) {
/*  534 */         paramUnaryExpression.right = new TypeExpression(this.where, Type.tClass(identifier));
/*      */         
/*  536 */         ClassDefinition classDefinition1 = paramContext.field.getClassDefinition();
/*  537 */         paramEnvironment.resolve(this.where, classDefinition1, paramUnaryExpression.right.type);
/*  538 */         return paramVset;
/*      */       } 
/*      */ 
/*      */       
/*  542 */       this.type = Type.tPackage;
/*  543 */       return paramVset;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  548 */     ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/*  549 */     boolean bool = this.right instanceof TypeExpression;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  555 */       if (!this.right.type.isType(10)) {
/*  556 */         if (this.right.type.isType(9) && this.id.equals(idLength)) {
/*      */ 
/*      */           
/*  559 */           if (!isTypeAccessible(this.where, paramEnvironment, this.right.type, classDefinition)) {
/*  560 */             ClassDeclaration classDeclaration = classDefinition.getClassDeclaration();
/*  561 */             if (bool) {
/*  562 */               paramEnvironment.error(this.where, "no.type.access", this.id, this.right.type
/*  563 */                   .toString(), classDeclaration);
/*      */             } else {
/*  565 */               paramEnvironment.error(this.where, "cant.access.member.type", this.id, this.right.type
/*  566 */                   .toString(), classDeclaration);
/*      */             } 
/*      */           } 
/*  569 */           this.type = Type.tInt;
/*  570 */           this.implementation = new LengthExpression(this.where, this.right);
/*  571 */           return paramVset;
/*      */         } 
/*  573 */         if (!this.right.type.isType(13)) {
/*  574 */           paramEnvironment.error(this.where, "invalid.field.reference", this.id, this.right.type);
/*      */         }
/*  576 */         return paramVset;
/*      */       } 
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
/*  594 */       ClassDefinition classDefinition1 = classDefinition;
/*  595 */       if (this.right instanceof FieldExpression) {
/*  596 */         Identifier identifier = ((FieldExpression)this.right).id;
/*  597 */         if (identifier == idThis) {
/*  598 */           classDefinition1 = ((FieldExpression)this.right).clazz;
/*  599 */         } else if (identifier == idSuper) {
/*  600 */           classDefinition1 = ((FieldExpression)this.right).clazz;
/*  601 */           this.superBase = classDefinition1;
/*      */         } 
/*      */       } 
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
/*  618 */       this.clazz = paramEnvironment.getClassDefinition(this.right.type);
/*  619 */       if (this.id == idThis || this.id == idSuper) {
/*  620 */         if (!bool) {
/*  621 */           paramEnvironment.error(this.right.where, "invalid.type.expr");
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  630 */         if (paramContext.field.isSynthetic()) {
/*  631 */           throw new CompilerError("synthetic qualified this");
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  640 */         this.implementation = paramContext.findOuterLink(paramEnvironment, this.where, this.clazz, null, true);
/*  641 */         paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  642 */         if (this.id == idSuper) {
/*  643 */           this.type = this.clazz.getSuperClass().getType();
/*      */         } else {
/*  645 */           this.type = this.clazz.getType();
/*      */         } 
/*  647 */         return paramVset;
/*      */       } 
/*      */ 
/*      */       
/*  651 */       this.field = this.clazz.getVariable(paramEnvironment, this.id, classDefinition1);
/*      */       
/*  653 */       if (this.field == null && bool && paramUnaryExpression != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  663 */         this.field = this.clazz.getInnerClass(paramEnvironment, this.id);
/*  664 */         if (this.field != null) {
/*  665 */           return checkInnerClass(paramEnvironment, paramContext, paramVset, paramHashtable, paramUnaryExpression);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  672 */       if (this.field == null) {
/*  673 */         if ((this.field = this.clazz.findAnyMethod(paramEnvironment, this.id)) != null) {
/*  674 */           paramEnvironment.error(this.where, "invalid.field", this.id, this.field
/*  675 */               .getClassDeclaration());
/*      */         } else {
/*  677 */           paramEnvironment.error(this.where, "no.such.field", this.id, this.clazz);
/*      */         } 
/*  679 */         return paramVset;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  685 */       if (!isTypeAccessible(this.where, paramEnvironment, this.right.type, classDefinition1)) {
/*  686 */         ClassDeclaration classDeclaration = classDefinition1.getClassDeclaration();
/*  687 */         if (bool) {
/*  688 */           paramEnvironment.error(this.where, "no.type.access", this.id, this.right.type
/*  689 */               .toString(), classDeclaration);
/*      */         } else {
/*  691 */           paramEnvironment.error(this.where, "cant.access.member.type", this.id, this.right.type
/*  692 */               .toString(), classDeclaration);
/*      */         } 
/*      */       } 
/*      */       
/*  696 */       this.type = this.field.getType();
/*      */       
/*  698 */       if (!classDefinition1.canAccess(paramEnvironment, this.field)) {
/*  699 */         paramEnvironment.error(this.where, "no.field.access", this.id, this.clazz, classDefinition1
/*  700 */             .getClassDeclaration());
/*  701 */         return paramVset;
/*      */       } 
/*      */       
/*  704 */       if (bool && !this.field.isStatic()) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  709 */         paramEnvironment.error(this.where, "no.static.field.access", this.id, this.clazz);
/*  710 */         return paramVset;
/*      */       } 
/*      */       
/*  713 */       this.implementation = implementFieldAccess(paramEnvironment, paramContext, this.right, paramBoolean);
/*      */ 
/*      */ 
/*      */       
/*  717 */       if (this.field.isProtected() && !(this.right instanceof SuperExpression) && (!(this.right instanceof FieldExpression) || ((FieldExpression)this.right).id != idSuper) && 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  722 */         !classDefinition1.protectedAccess(paramEnvironment, this.field, this.right.type)) {
/*  723 */         paramEnvironment.error(this.where, "invalid.protected.field.use", this.field
/*  724 */             .getName(), this.field.getClassDeclaration(), this.right.type);
/*      */         
/*  726 */         return paramVset;
/*      */       } 
/*      */       
/*  729 */       if (!this.field.isStatic() && this.right.op == 82 && 
/*  730 */         !paramVset.testVar(paramContext.getThisNumber())) {
/*  731 */         paramEnvironment.error(this.where, "access.inst.before.super", this.id);
/*      */       }
/*      */       
/*  734 */       if (this.field.reportDeprecated(paramEnvironment)) {
/*  735 */         paramEnvironment.error(this.where, "warn.field.is.deprecated", this.id, this.field
/*  736 */             .getClassDefinition());
/*      */       }
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
/*  749 */       if (classDefinition1 == classDefinition) {
/*  750 */         ClassDefinition classDefinition2 = this.field.getClassDefinition();
/*  751 */         if (classDefinition2.isPackagePrivate() && 
/*      */           
/*  753 */           !classDefinition2.getName().getQualifier().equals(classDefinition1.getName().getQualifier()))
/*      */         {
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
/*  766 */           this
/*  767 */             .field = MemberDefinition.makeProxyMember(this.field, this.clazz, paramEnvironment);
/*      */         }
/*      */       } 
/*      */       
/*  771 */       classDefinition1.addDependency(this.field.getClassDeclaration());
/*      */     }
/*  773 */     catch (ClassNotFound classNotFound) {
/*  774 */       paramEnvironment.error(this.where, "class.not.found", classNotFound.name, paramContext.field);
/*      */     }
/*  776 */     catch (AmbiguousMember ambiguousMember) {
/*  777 */       paramEnvironment.error(this.where, "ambig.field", this.id, ambiguousMember.field1
/*  778 */           .getClassDeclaration(), ambiguousMember.field2.getClassDeclaration());
/*      */     } 
/*  780 */     return paramVset;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public FieldUpdater getAssigner(Environment paramEnvironment, Context paramContext) {
/*  799 */     if (this.field == null)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  804 */       return null;
/*      */     }
/*  806 */     ClassDefinition classDefinition = accessBase(paramEnvironment, paramContext);
/*  807 */     if (classDefinition != null) {
/*  808 */       MemberDefinition memberDefinition = classDefinition.getUpdateMember(paramEnvironment, paramContext, this.field, isQualSuper());
/*      */       
/*  810 */       Expression expression = (this.right == null) ? null : this.right.copyInline(paramContext);
/*      */       
/*  812 */       return new FieldUpdater(this.where, this.field, expression, null, memberDefinition);
/*      */     } 
/*  814 */     return null;
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
/*      */   public FieldUpdater getUpdater(Environment paramEnvironment, Context paramContext) {
/*  830 */     if (this.field == null)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  835 */       return null;
/*      */     }
/*  837 */     ClassDefinition classDefinition = accessBase(paramEnvironment, paramContext);
/*  838 */     if (classDefinition != null) {
/*  839 */       MemberDefinition memberDefinition1 = classDefinition.getAccessMember(paramEnvironment, paramContext, this.field, isQualSuper());
/*  840 */       MemberDefinition memberDefinition2 = classDefinition.getUpdateMember(paramEnvironment, paramContext, this.field, isQualSuper());
/*      */       
/*  842 */       Expression expression = (this.right == null) ? null : this.right.copyInline(paramContext);
/*  843 */       return new FieldUpdater(this.where, this.field, expression, memberDefinition1, memberDefinition2);
/*      */     } 
/*  845 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Vset checkInnerClass(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression) {
/*  855 */     ClassDefinition classDefinition1 = this.field.getInnerClass();
/*  856 */     this.type = classDefinition1.getType();
/*      */     
/*  858 */     if (!classDefinition1.isTopLevel()) {
/*  859 */       paramEnvironment.error(this.where, "inner.static.ref", classDefinition1.getName());
/*      */     }
/*      */     
/*  862 */     TypeExpression typeExpression = new TypeExpression(this.where, this.type);
/*      */ 
/*      */     
/*  865 */     ClassDefinition classDefinition2 = paramContext.field.getClassDefinition();
/*      */     try {
/*  867 */       if (!classDefinition2.canAccess(paramEnvironment, this.field)) {
/*  868 */         ClassDefinition classDefinition = paramEnvironment.getClassDefinition(this.right.type);
/*      */ 
/*      */         
/*  871 */         paramEnvironment.error(this.where, "no.type.access", this.id, classDefinition, classDefinition2
/*  872 */             .getClassDeclaration());
/*  873 */         return paramVset;
/*      */       } 
/*      */       
/*  876 */       if (this.field.isProtected() && !(this.right instanceof SuperExpression) && (!(this.right instanceof FieldExpression) || ((FieldExpression)this.right).id != idSuper) && 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  881 */         !classDefinition2.protectedAccess(paramEnvironment, this.field, this.right.type)) {
/*  882 */         paramEnvironment.error(this.where, "invalid.protected.field.use", this.field
/*  883 */             .getName(), this.field.getClassDeclaration(), this.right.type);
/*      */         
/*  885 */         return paramVset;
/*      */       } 
/*      */       
/*  888 */       classDefinition1.noteUsedBy(classDefinition2, this.where, paramEnvironment);
/*      */     }
/*  890 */     catch (ClassNotFound classNotFound) {
/*  891 */       paramEnvironment.error(this.where, "class.not.found", classNotFound.name, paramContext.field);
/*      */     } 
/*      */     
/*  894 */     classDefinition2.addDependency(this.field.getClassDeclaration());
/*  895 */     if (paramUnaryExpression == null)
/*      */     {
/*  897 */       return typeExpression.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); } 
/*  898 */     paramUnaryExpression.right = typeExpression;
/*  899 */     return paramVset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vset checkLHS(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  907 */     boolean bool = (this.field != null) ? true : false;
/*      */ 
/*      */     
/*  910 */     checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable, (UnaryExpression)null, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  915 */     if (this.implementation != null)
/*      */     {
/*  917 */       return super.checkLHS(paramEnvironment, paramContext, paramVset, paramHashtable);
/*      */     }
/*      */     
/*  920 */     if (this.field != null && this.field.isFinal() && !bool) {
/*  921 */       if (this.field.isBlankFinal()) {
/*  922 */         if (this.field.isStatic()) {
/*  923 */           if (this.right != null) {
/*  924 */             paramEnvironment.error(this.where, "qualified.static.final.assign");
/*      */           
/*      */           }
/*      */         
/*      */         }
/*  929 */         else if (this.right != null && this.right.op != 82) {
/*  930 */           paramEnvironment.error(this.where, "bad.qualified.final.assign", this.field.getName());
/*      */ 
/*      */           
/*  933 */           return paramVset;
/*      */         } 
/*      */         
/*  936 */         paramVset = checkFinalAssign(paramEnvironment, paramContext, paramVset, this.where, this.field);
/*      */       } else {
/*  938 */         paramEnvironment.error(this.where, "assign.to.final", this.id);
/*      */       } 
/*      */     }
/*  941 */     return paramVset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vset checkAssignOp(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, Expression paramExpression) {
/*  951 */     checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable, (UnaryExpression)null, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  956 */     if (this.implementation != null) {
/*  957 */       return super.checkLHS(paramEnvironment, paramContext, paramVset, paramHashtable);
/*      */     }
/*  959 */     if (this.field != null && this.field.isFinal()) {
/*  960 */       paramEnvironment.error(this.where, "assign.to.final", this.id);
/*      */     }
/*  962 */     return paramVset;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Vset checkFinalAssign(Environment paramEnvironment, Context paramContext, Vset paramVset, long paramLong, MemberDefinition paramMemberDefinition) {
/*  982 */     if (paramMemberDefinition.isBlankFinal() && paramMemberDefinition
/*  983 */       .getClassDefinition() == paramContext.field.getClassDefinition()) {
/*  984 */       int i = paramContext.getFieldNumber(paramMemberDefinition);
/*  985 */       if (i >= 0 && paramVset.testVarUnassigned(i)) {
/*      */         
/*  987 */         paramVset = paramVset.addVar(i);
/*      */       } else {
/*      */         
/*  990 */         Identifier identifier = paramMemberDefinition.getName();
/*  991 */         paramEnvironment.error(paramLong, "assign.to.blank.final", identifier);
/*      */       } 
/*      */     } else {
/*      */       
/*  995 */       Identifier identifier = paramMemberDefinition.getName();
/*  996 */       paramEnvironment.error(paramLong, "assign.to.final", identifier);
/*      */     } 
/*  998 */     return paramVset;
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
/*      */   private static MemberDefinition getClassLiteralCache(Environment paramEnvironment, Context paramContext, String paramString, ClassDefinition paramClassDefinition) {
/*      */     String str;
/*      */     MemberDefinition memberDefinition;
/* 1013 */     if (!paramString.startsWith("[")) {
/* 1014 */       str = "class$" + paramString.replace('.', '$');
/*      */     } else {
/* 1016 */       str = "array$" + paramString.substring(1);
/* 1017 */       str = str.replace('[', '$');
/* 1018 */       if (paramString.endsWith(";")) {
/*      */         
/* 1020 */         str = str.substring(0, str.length() - 1);
/* 1021 */         str = str.replace('.', '$');
/*      */       } 
/*      */     } 
/*      */     
/* 1025 */     Identifier identifier = Identifier.lookup(str);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1035 */       memberDefinition = paramClassDefinition.getVariable(paramEnvironment, identifier, paramClassDefinition);
/* 1036 */     } catch (ClassNotFound classNotFound) {
/* 1037 */       return null;
/* 1038 */     } catch (AmbiguousMember ambiguousMember) {
/* 1039 */       return null;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1047 */     if (memberDefinition != null && memberDefinition.getClassDefinition() == paramClassDefinition) {
/* 1048 */       return memberDefinition;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1055 */     return paramEnvironment.makeMemberDefinition(paramEnvironment, paramClassDefinition.getWhere(), paramClassDefinition, null, 524296, Type.tClassDesc, identifier, null, null, null);
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
/*      */   private Expression makeClassLiteralCacheRef(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition1, MemberDefinition paramMemberDefinition2, String paramString) {
/* 1068 */     TypeExpression typeExpression1 = new TypeExpression(this.where, paramMemberDefinition2.getClassDefinition().getType());
/* 1069 */     FieldExpression fieldExpression = new FieldExpression(this.where, typeExpression1, paramMemberDefinition2);
/*      */     
/* 1071 */     NotEqualExpression notEqualExpression = new NotEqualExpression(this.where, fieldExpression.copyInline(paramContext), new NullExpression(this.where));
/*      */ 
/*      */     
/* 1074 */     TypeExpression typeExpression2 = new TypeExpression(this.where, paramMemberDefinition1.getClassDefinition().getType());
/* 1075 */     StringExpression stringExpression = new StringExpression(this.where, paramString);
/* 1076 */     Expression[] arrayOfExpression = { stringExpression };
/* 1077 */     MethodExpression methodExpression = new MethodExpression(this.where, typeExpression2, paramMemberDefinition1, arrayOfExpression);
/*      */     
/* 1079 */     AssignExpression assignExpression = new AssignExpression(this.where, fieldExpression.copyInline(paramContext), methodExpression);
/*      */     
/* 1081 */     return new ConditionalExpression(this.where, notEqualExpression, fieldExpression, assignExpression);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Expression makeClassLiteralInlineRef(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, String paramString) {
/* 1088 */     TypeExpression typeExpression = new TypeExpression(this.where, paramMemberDefinition.getClassDefinition().getType());
/* 1089 */     StringExpression stringExpression = new StringExpression(this.where, paramString);
/* 1090 */     Expression[] arrayOfExpression = { stringExpression };
/* 1091 */     return new MethodExpression(this.where, typeExpression, paramMemberDefinition, arrayOfExpression);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isConstant() {
/* 1101 */     if (this.implementation != null)
/* 1102 */       return this.implementation.isConstant(); 
/* 1103 */     if (this.field != null && (this.right == null || this.right instanceof TypeExpression || (this.right.op == 82 && this.right.where == this.where)))
/*      */     {
/*      */       
/* 1106 */       return this.field.isConstant();
/*      */     }
/* 1108 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 1115 */     if (this.implementation != null) {
/* 1116 */       return this.implementation.inline(paramEnvironment, paramContext);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1126 */     Expression expression = inlineValue(paramEnvironment, paramContext);
/* 1127 */     if (expression instanceof FieldExpression) {
/* 1128 */       FieldExpression fieldExpression = (FieldExpression)expression;
/* 1129 */       if (fieldExpression.right != null && fieldExpression.right.op == 82) {
/* 1130 */         return null;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1136 */     return expression;
/*      */   }
/*      */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 1139 */     if (this.implementation != null)
/* 1140 */       return this.implementation.inlineValue(paramEnvironment, paramContext); 
/*      */     try {
/* 1142 */       if (this.field == null) {
/* 1143 */         return this;
/*      */       }
/*      */       
/* 1146 */       if (this.field.isFinal()) {
/* 1147 */         Expression expression = (Expression)this.field.getValue(paramEnvironment);
/* 1148 */         if (expression != null && expression.isConstant()) {
/*      */           
/* 1150 */           expression = expression.copyInline(paramContext);
/* 1151 */           expression.where = this.where;
/* 1152 */           return (new CommaExpression(this.where, this.right, expression)).inlineValue(paramEnvironment, paramContext);
/*      */         } 
/*      */       } 
/*      */       
/* 1156 */       if (this.right != null) {
/* 1157 */         if (this.field.isStatic()) {
/* 1158 */           Expression expression = this.right.inline(paramEnvironment, paramContext);
/* 1159 */           this.right = null;
/* 1160 */           if (expression != null) {
/* 1161 */             return new CommaExpression(this.where, expression, this);
/*      */           }
/*      */         } else {
/* 1164 */           this.right = this.right.inlineValue(paramEnvironment, paramContext);
/*      */         } 
/*      */       }
/* 1167 */       return this;
/*      */     }
/* 1169 */     catch (ClassNotFound classNotFound) {
/* 1170 */       throw new CompilerError(classNotFound);
/*      */     } 
/*      */   }
/*      */   public Expression inlineLHS(Environment paramEnvironment, Context paramContext) {
/* 1174 */     if (this.implementation != null)
/* 1175 */       return this.implementation.inlineLHS(paramEnvironment, paramContext); 
/* 1176 */     if (this.right != null) {
/* 1177 */       if (this.field.isStatic()) {
/* 1178 */         Expression expression = this.right.inline(paramEnvironment, paramContext);
/* 1179 */         this.right = null;
/* 1180 */         if (expression != null) {
/* 1181 */           return new CommaExpression(this.where, expression, this);
/*      */         }
/*      */       } else {
/* 1184 */         this.right = this.right.inlineValue(paramEnvironment, paramContext);
/*      */       } 
/*      */     }
/* 1187 */     return this;
/*      */   }
/*      */   
/*      */   public Expression copyInline(Context paramContext) {
/* 1191 */     if (this.implementation != null)
/* 1192 */       return this.implementation.copyInline(paramContext); 
/* 1193 */     return super.copyInline(paramContext);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 1200 */     if (this.implementation != null)
/* 1201 */       return this.implementation.costInline(paramInt, paramEnvironment, paramContext); 
/* 1202 */     if (paramContext == null) {
/* 1203 */       return 3 + ((this.right == null) ? 0 : this.right
/* 1204 */         .costInline(paramInt, paramEnvironment, paramContext));
/*      */     }
/*      */     
/* 1207 */     ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/*      */ 
/*      */     
/*      */     try {
/* 1211 */       if (classDefinition.permitInlinedAccess(paramEnvironment, this.field.getClassDeclaration()) && classDefinition
/* 1212 */         .permitInlinedAccess(paramEnvironment, this.field)) {
/* 1213 */         if (this.right == null) {
/* 1214 */           return 3;
/*      */         }
/* 1216 */         ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(this.right.type);
/* 1217 */         if (classDefinition.permitInlinedAccess(paramEnvironment, classDeclaration)) {
/* 1218 */           return 3 + this.right.costInline(paramInt, paramEnvironment, paramContext);
/*      */         }
/*      */       }
/*      */     
/* 1222 */     } catch (ClassNotFound classNotFound) {}
/*      */     
/* 1224 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int codeLValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 1231 */     if (this.implementation != null)
/* 1232 */       throw new CompilerError("codeLValue"); 
/* 1233 */     if (this.field.isStatic()) {
/* 1234 */       if (this.right != null) {
/* 1235 */         this.right.code(paramEnvironment, paramContext, paramAssembler);
/* 1236 */         return 1;
/*      */       } 
/* 1238 */       return 0;
/*      */     } 
/* 1240 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 1241 */     return 1;
/*      */   }
/*      */   void codeLoad(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 1244 */     if (this.field == null) {
/* 1245 */       throw new CompilerError("should not be null");
/*      */     }
/* 1247 */     if (this.field.isStatic()) {
/* 1248 */       paramAssembler.add(this.where, 178, this.field);
/*      */     } else {
/* 1250 */       paramAssembler.add(this.where, 180, this.field);
/*      */     } 
/*      */   }
/*      */   void codeStore(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 1254 */     if (this.field.isStatic()) {
/* 1255 */       paramAssembler.add(this.where, 179, this.field);
/*      */     } else {
/* 1257 */       paramAssembler.add(this.where, 181, this.field);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 1262 */     codeLValue(paramEnvironment, paramContext, paramAssembler);
/* 1263 */     codeLoad(paramEnvironment, paramContext, paramAssembler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void print(PrintStream paramPrintStream) {
/* 1270 */     paramPrintStream.print("(");
/* 1271 */     if (this.right != null) {
/* 1272 */       this.right.print(paramPrintStream);
/*      */     } else {
/* 1274 */       paramPrintStream.print("<empty>");
/*      */     } 
/* 1276 */     paramPrintStream.print("." + this.id + ")");
/* 1277 */     if (this.implementation != null) {
/* 1278 */       paramPrintStream.print("/IMPL=");
/* 1279 */       this.implementation.print(paramPrintStream);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\FieldExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */