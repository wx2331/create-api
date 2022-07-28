/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Environment
/*     */   implements Constants
/*     */ {
/*     */   Environment env;
/*     */   String encoding;
/*     */   Object source;
/*     */   
/*     */   public Environment(Environment paramEnvironment, Object paramObject) {
/*  73 */     if (paramEnvironment != null && paramEnvironment.env != null && paramEnvironment.getClass() == getClass())
/*  74 */       paramEnvironment = paramEnvironment.env; 
/*  75 */     this.env = paramEnvironment;
/*  76 */     this.source = paramObject;
/*     */   }
/*     */   public Environment() {
/*  79 */     this(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExemptPackage(Identifier paramIdentifier) {
/*  87 */     return this.env.isExemptPackage(paramIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDeclaration getClassDeclaration(Identifier paramIdentifier) {
/*  94 */     return this.env.getClassDeclaration(paramIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClassDefinition getClassDefinition(Identifier paramIdentifier) throws ClassNotFound {
/* 104 */     if (paramIdentifier.isInner()) {
/* 105 */       ClassDefinition classDefinition = getClassDefinition(paramIdentifier.getTopName());
/* 106 */       Identifier identifier = paramIdentifier.getFlatName();
/*     */       
/* 108 */       label24: while (identifier.isQualified()) {
/* 109 */         identifier = identifier.getTail();
/* 110 */         Identifier identifier1 = identifier.getHead();
/*     */         
/* 112 */         String str = identifier1.toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 123 */         if (str.length() > 0 && 
/* 124 */           Character.isDigit(str.charAt(0))) {
/* 125 */           ClassDefinition classDefinition1 = classDefinition.getLocalClass(str);
/* 126 */           if (classDefinition1 != null) {
/* 127 */             classDefinition = classDefinition1;
/*     */             continue;
/*     */           } 
/*     */         } else {
/* 131 */           MemberDefinition memberDefinition = classDefinition.getFirstMatch(identifier1);
/* 132 */           for (; memberDefinition != null; memberDefinition = memberDefinition.getNextMatch()) {
/* 133 */             if (memberDefinition.isInnerClass()) {
/* 134 */               classDefinition = memberDefinition.getInnerClass();
/*     */               continue label24;
/*     */             } 
/*     */           } 
/*     */         } 
/* 139 */         throw new ClassNotFound(Identifier.lookupInner(classDefinition.getName(), identifier1));
/*     */       } 
/*     */       
/* 142 */       return classDefinition;
/*     */     } 
/* 144 */     return getClassDeclaration(paramIdentifier).getClassDefinition(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDeclaration getClassDeclaration(Type paramType) {
/* 153 */     return getClassDeclaration(paramType.getClassName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClassDefinition getClassDefinition(Type paramType) throws ClassNotFound {
/* 161 */     return getClassDefinition(paramType.getClassName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean classExists(Identifier paramIdentifier) {
/* 170 */     return this.env.classExists(paramIdentifier);
/*     */   }
/*     */   
/*     */   public final boolean classExists(Type paramType) {
/* 174 */     return (!paramType.isType(10) || classExists(paramType.getClassName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Package getPackage(Identifier paramIdentifier) throws IOException {
/* 181 */     return this.env.getPackage(paramIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadDefinition(ClassDeclaration paramClassDeclaration) {
/* 188 */     this.env.loadDefinition(paramClassDeclaration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getSource() {
/* 195 */     return this.source;
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
/*     */   public boolean resolve(long paramLong, ClassDefinition paramClassDefinition, Type paramType) {
/*     */     boolean bool;
/*     */     Type[] arrayOfType;
/*     */     int i;
/* 232 */     switch (paramType.getTypeCode()) {
/*     */       
/*     */       case 10:
/*     */         try {
/* 236 */           Identifier identifier = paramType.getClassName();
/* 237 */           if (!identifier.isQualified() && !identifier.isInner() && !classExists(identifier)) {
/* 238 */             resolve(identifier);
/*     */           }
/* 240 */           ClassDefinition classDefinition = getQualifiedClassDefinition(paramLong, identifier, paramClassDefinition, false);
/* 241 */           if (!paramClassDefinition.canAccess(this, classDefinition.getClassDeclaration())) {
/*     */ 
/*     */             
/* 244 */             error(paramLong, "cant.access.class", classDefinition);
/* 245 */             return true;
/*     */           } 
/* 247 */           classDefinition.noteUsedBy(paramClassDefinition, paramLong, this.env);
/* 248 */         } catch (AmbiguousClass ambiguousClass) {
/* 249 */           error(paramLong, "ambig.class", ambiguousClass.name1, ambiguousClass.name2);
/* 250 */           return false;
/* 251 */         } catch (ClassNotFound classNotFound) {
/*     */ 
/*     */           
/*     */           try {
/* 255 */             if (classNotFound.name.isInner() && 
/* 256 */               getPackage(classNotFound.name.getTopName()).exists()) {
/* 257 */               this.env.error(paramLong, "class.and.package", classNotFound.name
/* 258 */                   .getTopName());
/*     */             }
/* 260 */           } catch (IOException iOException) {
/* 261 */             this.env.error(paramLong, "io.exception", "package check");
/*     */           } 
/*     */ 
/*     */           
/* 265 */           error(paramLong, "class.not.found.no.context", classNotFound.name);
/* 266 */           return false;
/*     */         } 
/* 268 */         return true;
/*     */ 
/*     */       
/*     */       case 9:
/* 272 */         return resolve(paramLong, paramClassDefinition, paramType.getElementType());
/*     */       
/*     */       case 12:
/* 275 */         bool = resolve(paramLong, paramClassDefinition, paramType.getReturnType());
/* 276 */         arrayOfType = paramType.getArgumentTypes();
/* 277 */         for (i = arrayOfType.length; i-- > 0;) {
/* 278 */           bool &= resolve(paramLong, paramClassDefinition, arrayOfType[i]);
/*     */         }
/* 280 */         return bool;
/*     */     } 
/* 282 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean resolveByName(long paramLong, ClassDefinition paramClassDefinition, Identifier paramIdentifier) {
/* 291 */     return resolveByName(paramLong, paramClassDefinition, paramIdentifier, false);
/*     */   }
/*     */   
/*     */   public boolean resolveExtendsByName(long paramLong, ClassDefinition paramClassDefinition, Identifier paramIdentifier) {
/* 295 */     return resolveByName(paramLong, paramClassDefinition, paramIdentifier, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean resolveByName(long paramLong, ClassDefinition paramClassDefinition, Identifier paramIdentifier, boolean paramBoolean) {
/*     */     try {
/* 302 */       if (!paramIdentifier.isQualified() && !paramIdentifier.isInner() && !classExists(paramIdentifier)) {
/* 303 */         resolve(paramIdentifier);
/*     */       }
/* 305 */       ClassDefinition classDefinition = getQualifiedClassDefinition(paramLong, paramIdentifier, paramClassDefinition, paramBoolean);
/* 306 */       ClassDeclaration classDeclaration = classDefinition.getClassDeclaration();
/* 307 */       if ((paramBoolean || !paramClassDefinition.canAccess(this, classDeclaration)) && (!paramBoolean || 
/*     */         
/* 309 */         !paramClassDefinition.extendsCanAccess(this, classDeclaration))) {
/* 310 */         error(paramLong, "cant.access.class", classDefinition);
/* 311 */         return true;
/*     */       } 
/* 313 */     } catch (AmbiguousClass ambiguousClass) {
/* 314 */       error(paramLong, "ambig.class", ambiguousClass.name1, ambiguousClass.name2);
/* 315 */       return false;
/* 316 */     } catch (ClassNotFound classNotFound) {
/*     */ 
/*     */       
/*     */       try {
/* 320 */         if (classNotFound.name.isInner() && 
/* 321 */           getPackage(classNotFound.name.getTopName()).exists()) {
/* 322 */           this.env.error(paramLong, "class.and.package", classNotFound.name
/* 323 */               .getTopName());
/*     */         }
/* 325 */       } catch (IOException iOException) {
/* 326 */         this.env.error(paramLong, "io.exception", "package check");
/*     */       } 
/* 328 */       error(paramLong, "class.not.found", classNotFound.name, "type name");
/* 329 */       return false;
/*     */     } 
/* 331 */     return true;
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
/*     */   public final ClassDefinition getQualifiedClassDefinition(long paramLong, Identifier paramIdentifier, ClassDefinition paramClassDefinition, boolean paramBoolean) throws ClassNotFound {
/* 344 */     if (paramIdentifier.isInner()) {
/* 345 */       ClassDefinition classDefinition = getClassDefinition(paramIdentifier.getTopName());
/* 346 */       Identifier identifier = paramIdentifier.getFlatName();
/*     */       
/* 348 */       while (identifier.isQualified()) {
/* 349 */         identifier = identifier.getTail();
/* 350 */         Identifier identifier1 = identifier.getHead();
/*     */         
/* 352 */         String str = identifier1.toString();
/*     */ 
/*     */         
/* 355 */         if (str.length() > 0 && 
/* 356 */           Character.isDigit(str.charAt(0))) {
/* 357 */           ClassDefinition classDefinition1 = classDefinition.getLocalClass(str);
/* 358 */           if (classDefinition1 != null) {
/* 359 */             classDefinition = classDefinition1;
/*     */             continue;
/*     */           } 
/*     */         } else {
/* 363 */           MemberDefinition memberDefinition = classDefinition.getFirstMatch(identifier1); while (true) {
/* 364 */             memberDefinition = memberDefinition.getNextMatch();
/*     */           }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 408 */         throw new ClassNotFound(Identifier.lookupInner(classDefinition.getName(), identifier1));
/*     */       } 
/*     */       
/* 411 */       return classDefinition;
/*     */     } 
/* 413 */     return getClassDeclaration(paramIdentifier).getClassDefinition(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type resolveNames(ClassDefinition paramClassDefinition, Type paramType, boolean paramBoolean) {
/*     */     Identifier identifier1;
/*     */     Type type1;
/*     */     Identifier identifier2;
/*     */     Type type2, arrayOfType1[], arrayOfType2[];
/*     */     boolean bool;
/*     */     int i;
/* 430 */     dtEvent("Environment.resolveNames: " + paramClassDefinition + ", " + paramType);
/* 431 */     switch (paramType.getTypeCode()) {
/*     */       case 10:
/* 433 */         identifier1 = paramType.getClassName();
/*     */         
/* 435 */         if (paramBoolean) {
/* 436 */           identifier2 = resolvePackageQualifiedName(identifier1);
/*     */         } else {
/* 438 */           identifier2 = paramClassDefinition.resolveName(this, identifier1);
/*     */         } 
/* 440 */         if (identifier1 != identifier2) {
/* 441 */           paramType = Type.tClass(identifier2);
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 447 */         paramType = Type.tArray(resolveNames(paramClassDefinition, paramType.getElementType(), paramBoolean));
/*     */         break;
/*     */       
/*     */       case 12:
/* 451 */         type1 = paramType.getReturnType();
/* 452 */         type2 = resolveNames(paramClassDefinition, type1, paramBoolean);
/* 453 */         arrayOfType1 = paramType.getArgumentTypes();
/* 454 */         arrayOfType2 = new Type[arrayOfType1.length];
/* 455 */         bool = (type1 != type2) ? true : false;
/* 456 */         for (i = arrayOfType1.length; i-- > 0; ) {
/* 457 */           Type type3 = arrayOfType1[i];
/* 458 */           Type type4 = resolveNames(paramClassDefinition, type3, paramBoolean);
/* 459 */           arrayOfType2[i] = type4;
/* 460 */           if (type3 != type4) {
/* 461 */             bool = true;
/*     */           }
/*     */         } 
/* 464 */         if (bool) {
/* 465 */           paramType = Type.tMethod(type2, arrayOfType2);
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 470 */     return paramType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Identifier resolveName(Identifier paramIdentifier) {
/* 481 */     if (paramIdentifier.isQualified()) {
/*     */ 
/*     */ 
/*     */       
/* 485 */       Identifier identifier = resolveName(paramIdentifier.getHead());
/*     */       
/* 487 */       if (identifier.hasAmbigPrefix())
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 492 */         return identifier;
/*     */       }
/*     */       
/* 495 */       if (!classExists(identifier)) {
/* 496 */         return resolvePackageQualifiedName(paramIdentifier);
/*     */       }
/*     */       try {
/* 499 */         return getClassDefinition(identifier)
/* 500 */           .resolveInnerClass(this, paramIdentifier.getTail());
/* 501 */       } catch (ClassNotFound classNotFound) {
/*     */         
/* 503 */         return Identifier.lookupInner(identifier, paramIdentifier.getTail());
/*     */       } 
/*     */     } 
/*     */     try {
/* 507 */       return resolve(paramIdentifier);
/* 508 */     } catch (AmbiguousClass ambiguousClass) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 516 */       if (paramIdentifier.hasAmbigPrefix()) {
/* 517 */         return paramIdentifier;
/*     */       }
/* 519 */       return paramIdentifier.addAmbigPrefix();
/*     */     }
/* 521 */     catch (ClassNotFound classNotFound) {
/*     */       
/* 523 */       Imports imports = getImports();
/* 524 */       if (imports != null) {
/* 525 */         return imports.forceResolve(this, paramIdentifier);
/*     */       }
/* 527 */       return paramIdentifier;
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
/*     */   public final Identifier resolvePackageQualifiedName(Identifier paramIdentifier) {
/* 542 */     Identifier identifier = null;
/*     */     
/* 544 */     while (!classExists(paramIdentifier)) {
/*     */ 
/*     */       
/* 547 */       if (!paramIdentifier.isQualified()) {
/* 548 */         paramIdentifier = (identifier == null) ? paramIdentifier : Identifier.lookup(paramIdentifier, identifier);
/* 549 */         identifier = null;
/*     */         break;
/*     */       } 
/* 552 */       Identifier identifier1 = paramIdentifier.getName();
/* 553 */       identifier = (identifier == null) ? identifier1 : Identifier.lookup(identifier1, identifier);
/* 554 */       paramIdentifier = paramIdentifier.getQualifier();
/*     */     } 
/* 556 */     if (identifier != null)
/* 557 */       paramIdentifier = Identifier.lookupInner(paramIdentifier, identifier); 
/* 558 */     return paramIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Identifier resolve(Identifier paramIdentifier) throws ClassNotFound {
/* 565 */     if (this.env == null) return paramIdentifier; 
/* 566 */     return this.env.resolve(paramIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Imports getImports() {
/* 573 */     if (this.env == null) return null; 
/* 574 */     return this.env.getImports();
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
/*     */   public ClassDefinition makeClassDefinition(Environment paramEnvironment, long paramLong, IdentifierToken paramIdentifierToken1, String paramString, int paramInt, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken, ClassDefinition paramClassDefinition) {
/* 586 */     if (this.env == null) return null; 
/* 587 */     return this.env.makeClassDefinition(paramEnvironment, paramLong, paramIdentifierToken1, paramString, paramInt, paramIdentifierToken2, paramArrayOfIdentifierToken, paramClassDefinition);
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
/*     */   public MemberDefinition makeMemberDefinition(Environment paramEnvironment, long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, Identifier paramIdentifier, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Object paramObject) {
/* 602 */     if (this.env == null) return null; 
/* 603 */     return this.env.makeMemberDefinition(paramEnvironment, paramLong, paramClassDefinition, paramString, paramInt, paramType, paramIdentifier, paramArrayOfIdentifierToken1, paramArrayOfIdentifierToken2, paramObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isApplicable(MemberDefinition paramMemberDefinition, Type[] paramArrayOfType) throws ClassNotFound {
/* 612 */     Type type = paramMemberDefinition.getType();
/* 613 */     if (!type.isType(12))
/* 614 */       return false; 
/* 615 */     Type[] arrayOfType = type.getArgumentTypes();
/* 616 */     if (paramArrayOfType.length != arrayOfType.length)
/* 617 */       return false; 
/* 618 */     for (int i = paramArrayOfType.length; --i >= 0;) {
/* 619 */       if (!isMoreSpecific(paramArrayOfType[i], arrayOfType[i]))
/* 620 */         return false; 
/* 621 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMoreSpecific(MemberDefinition paramMemberDefinition1, MemberDefinition paramMemberDefinition2) throws ClassNotFound {
/* 630 */     Type type1 = paramMemberDefinition1.getClassDeclaration().getType();
/* 631 */     Type type2 = paramMemberDefinition2.getClassDeclaration().getType();
/* 632 */     return (isMoreSpecific(type1, type2) && 
/* 633 */       isApplicable(paramMemberDefinition2, paramMemberDefinition1.getType().getArgumentTypes()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMoreSpecific(Type paramType1, Type paramType2) throws ClassNotFound {
/* 644 */     return implicitCast(paramType1, paramType2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean implicitCast(Type paramType1, Type paramType2) throws ClassNotFound {
/* 652 */     if (paramType1 == paramType2) {
/* 653 */       return true;
/*     */     }
/* 655 */     int i = paramType2.getTypeCode();
/*     */     
/* 657 */     switch (paramType1.getTypeCode()) {
/*     */       case 1:
/* 659 */         if (i == 3)
/* 660 */           return true; 
/*     */       case 2:
/*     */       case 3:
/* 663 */         if (i == 4) return true; 
/*     */       case 4:
/* 665 */         if (i == 5) return true; 
/*     */       case 5:
/* 667 */         if (i == 6) return true; 
/*     */       case 6:
/* 669 */         if (i == 7) return true;
/*     */       
/*     */       default:
/* 672 */         return false;
/*     */       
/*     */       case 8:
/* 675 */         return paramType2.inMask(1792);
/*     */       
/*     */       case 9:
/* 678 */         if (!paramType2.isType(9)) {
/* 679 */           return (paramType2 == Type.tObject || paramType2 == Type.tCloneable || paramType2 == Type.tSerializable);
/*     */         }
/*     */ 
/*     */         
/*     */         do {
/* 684 */           paramType1 = paramType1.getElementType();
/* 685 */           paramType2 = paramType2.getElementType();
/* 686 */         } while (paramType1.isType(9) && paramType2.isType(9));
/* 687 */         if (paramType1.inMask(1536) && paramType2
/* 688 */           .inMask(1536)) {
/* 689 */           return isMoreSpecific(paramType1, paramType2);
/*     */         }
/* 691 */         return (paramType1.getTypeCode() == paramType2.getTypeCode());
/*     */       
/*     */       case 10:
/*     */         break;
/*     */     } 
/* 696 */     if (i == 10) {
/* 697 */       ClassDefinition classDefinition1 = getClassDefinition(paramType1);
/* 698 */       ClassDefinition classDefinition2 = getClassDefinition(paramType2);
/* 699 */       return classDefinition2.implementedBy(this, classDefinition1
/* 700 */           .getClassDeclaration());
/*     */     } 
/* 702 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean explicitCast(Type paramType1, Type paramType2) throws ClassNotFound {
/* 713 */     if (implicitCast(paramType1, paramType2)) {
/* 714 */       return true;
/*     */     }
/* 716 */     if (paramType1.inMask(254)) {
/* 717 */       return paramType2.inMask(254);
/*     */     }
/* 719 */     if (paramType1.isType(10) && paramType2.isType(10)) {
/* 720 */       ClassDefinition classDefinition1 = getClassDefinition(paramType1);
/* 721 */       ClassDefinition classDefinition2 = getClassDefinition(paramType2);
/* 722 */       if (classDefinition2.isFinal()) {
/* 723 */         return classDefinition1.implementedBy(this, classDefinition2
/* 724 */             .getClassDeclaration());
/*     */       }
/* 726 */       if (classDefinition1.isFinal()) {
/* 727 */         return classDefinition2.implementedBy(this, classDefinition1
/* 728 */             .getClassDeclaration());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 736 */       if (classDefinition2.isInterface() && classDefinition1.isInterface()) {
/* 737 */         return classDefinition2.couldImplement(classDefinition1);
/*     */       }
/*     */       
/* 740 */       return (classDefinition2.isInterface() || classDefinition1
/* 741 */         .isInterface() || classDefinition1
/* 742 */         .superClassOf(this, classDefinition2.getClassDeclaration()));
/*     */     } 
/* 744 */     if (paramType2.isType(9))
/* 745 */       if (paramType1.isType(9)) {
/* 746 */         Type type1 = paramType1.getElementType();
/* 747 */         Type type2 = paramType2.getElementType();
/* 748 */         while (type1.getTypeCode() == 9 && type2
/* 749 */           .getTypeCode() == 9) {
/* 750 */           type1 = type1.getElementType();
/* 751 */           type2 = type2.getElementType();
/*     */         } 
/* 753 */         if (type1.inMask(1536) && type2
/* 754 */           .inMask(1536)) {
/* 755 */           return explicitCast(type1, type2);
/*     */         }
/* 757 */       } else if (paramType1 == Type.tObject || paramType1 == Type.tCloneable || paramType1 == Type.tSerializable) {
/*     */         
/* 759 */         return true;
/*     */       }  
/* 761 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFlags() {
/* 768 */     return this.env.getFlags();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean debug_lines() {
/* 778 */     return ((getFlags() & 0x1000) != 0);
/*     */   }
/*     */   public final boolean debug_vars() {
/* 781 */     return ((getFlags() & 0x2000) != 0);
/*     */   }
/*     */   public final boolean debug_source() {
/* 784 */     return ((getFlags() & 0x40000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean opt() {
/* 793 */     return ((getFlags() & 0x4000) != 0);
/*     */   }
/*     */   public final boolean opt_interclass() {
/* 796 */     return ((getFlags() & 0x8000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean verbose() {
/* 803 */     return ((getFlags() & 0x1) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean dump() {
/* 810 */     return ((getFlags() & 0x2) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean warnings() {
/* 817 */     return ((getFlags() & 0x4) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean dependencies() {
/* 824 */     return ((getFlags() & 0x20) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean print_dependencies() {
/* 831 */     return ((getFlags() & 0x400) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean deprecation() {
/* 838 */     return ((getFlags() & 0x200) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean version12() {
/* 846 */     return ((getFlags() & 0x800) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean strictdefault() {
/* 853 */     return ((getFlags() & 0x20000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 860 */     if (this.env != null) {
/* 861 */       this.env.shutdown();
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
/*     */   public void error(Object paramObject1, long paramLong, String paramString, Object paramObject2, Object paramObject3, Object paramObject4) {
/* 875 */     this.env.error(paramObject1, paramLong, paramString, paramObject2, paramObject3, paramObject4);
/*     */   }
/*     */   public final void error(long paramLong, String paramString, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 878 */     error(this.source, paramLong, paramString, paramObject1, paramObject2, paramObject3);
/*     */   }
/*     */   public final void error(long paramLong, String paramString, Object paramObject1, Object paramObject2) {
/* 881 */     error(this.source, paramLong, paramString, paramObject1, paramObject2, null);
/*     */   }
/*     */   public final void error(long paramLong, String paramString, Object paramObject) {
/* 884 */     error(this.source, paramLong, paramString, paramObject, null, null);
/*     */   }
/*     */   public final void error(long paramLong, String paramString) {
/* 887 */     error(this.source, paramLong, paramString, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void output(String paramString) {
/* 895 */     this.env.output(paramString);
/*     */   }
/*     */   
/* 898 */   private static boolean debugging = (System.getProperty("javac.debug") != null);
/*     */   
/*     */   public static void debugOutput(Object paramObject) {
/* 901 */     if (debugging) {
/* 902 */       System.out.println(paramObject.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharacterEncoding(String paramString) {
/* 909 */     this.encoding = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharacterEncoding() {
/* 916 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getMajorVersion() {
/* 923 */     if (this.env == null) return 45; 
/* 924 */     return this.env.getMajorVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getMinorVersion() {
/* 931 */     if (this.env == null) return 3; 
/* 932 */     return this.env.getMinorVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean coverage() {
/* 940 */     return ((getFlags() & 0x40) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean covdata() {
/* 947 */     return ((getFlags() & 0x80) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getcovFile() {
/* 954 */     return this.env.getcovFile();
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
/* 973 */   private static boolean dependtrace = (System.getProperty("javac.trace.depend") != null);
/*     */   
/*     */   public void dtEnter(String paramString) {
/* 976 */     if (dependtrace) System.out.println(">>> " + paramString); 
/*     */   }
/*     */   
/*     */   public void dtExit(String paramString) {
/* 980 */     if (dependtrace) System.out.println("<<< " + paramString); 
/*     */   }
/*     */   
/*     */   public void dtEvent(String paramString) {
/* 984 */     if (dependtrace) System.out.println(paramString);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 994 */   private static boolean dumpmodifiers = (System.getProperty("javac.dump.modifiers") != null);
/*     */   public boolean dumpModifiers() {
/* 996 */     return dumpmodifiers;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\Environment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */