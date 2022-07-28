/*     */ package sun.rmi.rmic;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.DigestOutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RemoteClass
/*     */   implements RMIConstants
/*     */ {
/*     */   private BatchEnvironment env;
/*     */   private ClassDefinition implClassDef;
/*     */   private ClassDefinition[] remoteInterfaces;
/*     */   private Method[] remoteMethods;
/*     */   private long interfaceHash;
/*     */   private ClassDefinition defRemote;
/*     */   private ClassDefinition defException;
/*     */   private ClassDefinition defRemoteException;
/*     */   
/*     */   public static RemoteClass forClass(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition) {
/*  69 */     RemoteClass remoteClass = new RemoteClass(paramBatchEnvironment, paramClassDefinition);
/*  70 */     if (remoteClass.initialize()) {
/*  71 */       return remoteClass;
/*     */     }
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDefinition getClassDefinition() {
/*  81 */     return this.implClassDef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Identifier getName() {
/*  88 */     return this.implClassDef.getName();
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
/*     */   public ClassDefinition[] getRemoteInterfaces() {
/* 106 */     return (ClassDefinition[])this.remoteInterfaces.clone();
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
/*     */   public Method[] getRemoteMethods() {
/* 121 */     return (Method[])this.remoteMethods.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInterfaceHash() {
/* 129 */     return this.interfaceHash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     return "remote class " + this.implClassDef.getName().toString();
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
/*     */   private RemoteClass(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition) {
/* 165 */     this.env = paramBatchEnvironment;
/* 166 */     this.implClassDef = paramClassDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean initialize() {
/* 177 */     if (this.implClassDef.isInterface()) {
/* 178 */       this.env.error(0L, "rmic.cant.make.stubs.for.interface", this.implClassDef
/* 179 */           .getName());
/* 180 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 188 */       this
/* 189 */         .defRemote = this.env.getClassDeclaration(idRemote).getClassDefinition((Environment)this.env);
/* 190 */       this
/*     */         
/* 192 */         .defException = this.env.getClassDeclaration(idJavaLangException).getClassDefinition((Environment)this.env);
/* 193 */       this
/*     */         
/* 195 */         .defRemoteException = this.env.getClassDeclaration(idRemoteException).getClassDefinition((Environment)this.env);
/* 196 */     } catch (ClassNotFound classNotFound) {
/* 197 */       this.env.error(0L, "rmic.class.not.found", classNotFound.name);
/* 198 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     Vector<ClassDefinition> vector = new Vector();
/*     */     
/* 209 */     ClassDefinition classDefinition = this.implClassDef;
/* 210 */     while (classDefinition != null) {
/*     */       
/*     */       try {
/* 213 */         ClassDeclaration[] arrayOfClassDeclaration = classDefinition.getInterfaces();
/* 214 */         for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/*     */           
/* 216 */           ClassDefinition classDefinition1 = arrayOfClassDeclaration[b].getClassDefinition((Environment)this.env);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 221 */           if (!vector.contains(classDefinition1) && this.defRemote
/* 222 */             .implementedBy((Environment)this.env, arrayOfClassDeclaration[b])) {
/*     */             
/* 224 */             vector.addElement(classDefinition1);
/*     */             
/* 226 */             if (this.env.verbose()) {
/* 227 */               System.out.println("[found remote interface: " + classDefinition1
/* 228 */                   .getName() + "]");
/*     */             }
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 238 */         if (classDefinition == this.implClassDef && vector.isEmpty()) {
/* 239 */           if (this.defRemote.implementedBy((Environment)this.env, this.implClassDef
/* 240 */               .getClassDeclaration())) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 247 */             this.env.error(0L, "rmic.must.implement.remote.directly", this.implClassDef
/* 248 */                 .getName());
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 254 */             this.env.error(0L, "rmic.must.implement.remote", this.implClassDef
/* 255 */                 .getName());
/*     */           } 
/* 257 */           return false;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 264 */         classDefinition = (classDefinition.getSuperClass() != null) ? classDefinition.getSuperClass().getClassDefinition((Environment)this.env) : null;
/*     */       
/*     */       }
/* 267 */       catch (ClassNotFound classNotFound) {
/* 268 */         this.env.error(0L, "class.not.found", classNotFound.name, classDefinition.getName());
/* 269 */         return false;
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 310 */     Hashtable<Object, Object> hashtable = new Hashtable<>();
/* 311 */     boolean bool = false;
/*     */     
/* 313 */     Enumeration<ClassDefinition> enumeration = vector.elements();
/* 314 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 316 */       ClassDefinition classDefinition1 = enumeration.nextElement();
/* 317 */       if (!collectRemoteMethods(classDefinition1, (Hashtable)hashtable))
/* 318 */         bool = true; 
/*     */     } 
/* 320 */     if (bool) {
/* 321 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     this.remoteInterfaces = new ClassDefinition[vector.size()];
/* 328 */     vector.copyInto((Object[])this.remoteInterfaces);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     String[] arrayOfString = new String[hashtable.size()];
/* 338 */     byte b1 = 0;
/* 339 */     Enumeration<Method> enumeration1 = hashtable.elements();
/* 340 */     while (enumeration1.hasMoreElements()) {
/*     */       
/* 342 */       Method method = enumeration1.nextElement();
/* 343 */       String str = method.getNameAndDescriptor();
/*     */       byte b;
/* 345 */       for (b = b1; b && 
/* 346 */         str.compareTo(arrayOfString[b - 1]) < 0; b--)
/*     */       {
/*     */         
/* 349 */         arrayOfString[b] = arrayOfString[b - 1];
/*     */       }
/* 351 */       arrayOfString[b] = str;
/* 352 */       b1++;
/*     */     } 
/* 354 */     this.remoteMethods = new Method[hashtable.size()];
/* 355 */     for (byte b2 = 0; b2 < this.remoteMethods.length; b2++) {
/* 356 */       this.remoteMethods[b2] = (Method)hashtable.get(arrayOfString[b2]);
/*     */       
/* 358 */       if (this.env.verbose()) {
/* 359 */         System.out.print("[found remote method <" + b2 + ">: " + this.remoteMethods[b2]
/* 360 */             .getOperationString());
/*     */         
/* 362 */         ClassDeclaration[] arrayOfClassDeclaration = this.remoteMethods[b2].getExceptions();
/* 363 */         if (arrayOfClassDeclaration.length > 0)
/* 364 */           System.out.print(" throws "); 
/* 365 */         for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/* 366 */           if (b > 0)
/* 367 */             System.out.print(", "); 
/* 368 */           System.out.print(arrayOfClassDeclaration[b].getName());
/*     */         } 
/* 370 */         System.out.println("]");
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 379 */     this.interfaceHash = computeInterfaceHash();
/*     */     
/* 381 */     return true;
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
/*     */   private boolean collectRemoteMethods(ClassDefinition paramClassDefinition, Hashtable<String, Method> paramHashtable) {
/* 393 */     if (!paramClassDefinition.isInterface()) {
/* 394 */       throw new Error("expected interface, not class: " + paramClassDefinition
/* 395 */           .getName());
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
/* 422 */     boolean bool = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 428 */     MemberDefinition memberDefinition = paramClassDefinition.getFirstMember();
/* 429 */     label68: for (; memberDefinition != null; 
/* 430 */       memberDefinition = memberDefinition.getNextMember()) {
/*     */       
/* 432 */       if (memberDefinition.isMethod() && 
/* 433 */         !memberDefinition.isConstructor() && !memberDefinition.isInitializer()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 438 */         ClassDeclaration[] arrayOfClassDeclaration = memberDefinition.getExceptions((Environment)this.env);
/* 439 */         boolean bool1 = false;
/* 440 */         for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/*     */ 
/*     */           
/*     */           try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 459 */             if (this.defRemoteException.subClassOf((Environment)this.env, arrayOfClassDeclaration[b])) {
/*     */ 
/*     */               
/* 462 */               bool1 = true;
/*     */               break;
/*     */             } 
/* 465 */           } catch (ClassNotFound classNotFound) {
/* 466 */             this.env.error(0L, "class.not.found", classNotFound.name, paramClassDefinition
/* 467 */                 .getName());
/*     */ 
/*     */ 
/*     */             
/*     */             continue label68;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 476 */         if (!bool1) {
/* 477 */           this.env.error(0L, "rmic.must.throw.remoteexception", paramClassDefinition
/* 478 */               .getName(), memberDefinition.toString());
/* 479 */           bool = true;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 490 */           MemberDefinition memberDefinition1 = this.implClassDef.findMethod((Environment)this.env, memberDefinition
/* 491 */               .getName(), memberDefinition.getType());
/* 492 */           if (memberDefinition1 != null) {
/* 493 */             arrayOfClassDeclaration = memberDefinition1.getExceptions((Environment)this.env);
/* 494 */             for (byte b1 = 0; b1 < arrayOfClassDeclaration.length; b1++) {
/* 495 */               if (!this.defException.superClassOf((Environment)this.env, arrayOfClassDeclaration[b1])) {
/*     */ 
/*     */                 
/* 498 */                 this.env.error(0L, "rmic.must.only.throw.exception", memberDefinition1
/* 499 */                     .toString(), arrayOfClassDeclaration[b1]
/* 500 */                     .getName());
/* 501 */                 bool = true;
/*     */                 continue label68;
/*     */               } 
/*     */             } 
/*     */           } 
/* 506 */         } catch (ClassNotFound classNotFound) {
/* 507 */           this.env.error(0L, "class.not.found", classNotFound.name, this.implClassDef
/* 508 */               .getName());
/*     */ 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */         
/* 516 */         Method method1 = new Method(memberDefinition);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 531 */         String str = method1.getNameAndDescriptor();
/* 532 */         Method method2 = paramHashtable.get(str);
/* 533 */         if (method2 != null) {
/* 534 */           method1 = method1.mergeWith(method2);
/* 535 */           if (method1 == null) {
/* 536 */             bool = true;
/*     */             continue;
/*     */           } 
/*     */         } 
/* 540 */         paramHashtable.put(str, method1);
/*     */       } 
/*     */ 
/*     */       
/*     */       continue;
/*     */     } 
/*     */     
/*     */     try {
/* 548 */       ClassDeclaration[] arrayOfClassDeclaration = paramClassDefinition.getInterfaces();
/* 549 */       for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/*     */         
/* 551 */         ClassDefinition classDefinition = arrayOfClassDeclaration[b].getClassDefinition((Environment)this.env);
/* 552 */         if (!collectRemoteMethods(classDefinition, paramHashtable))
/* 553 */           bool = true; 
/*     */       } 
/* 555 */     } catch (ClassNotFound classNotFound) {
/* 556 */       this.env.error(0L, "class.not.found", classNotFound.name, paramClassDefinition.getName());
/* 557 */       return false;
/*     */     } 
/*     */     
/* 560 */     return !bool;
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
/*     */   private long computeInterfaceHash() {
/* 580 */     long l = 0L;
/* 581 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */     try {
/* 583 */       MessageDigest messageDigest = MessageDigest.getInstance("SHA");
/* 584 */       DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(byteArrayOutputStream, messageDigest));
/*     */ 
/*     */       
/* 587 */       dataOutputStream.writeInt(1);
/* 588 */       for (byte b1 = 0; b1 < this.remoteMethods.length; b1++) {
/* 589 */         MemberDefinition memberDefinition = this.remoteMethods[b1].getMemberDefinition();
/* 590 */         Identifier identifier = memberDefinition.getName();
/* 591 */         Type type = memberDefinition.getType();
/*     */         
/* 593 */         dataOutputStream.writeUTF(identifier.toString());
/*     */         
/* 595 */         dataOutputStream.writeUTF(type.getTypeSignature());
/*     */         
/* 597 */         ClassDeclaration[] arrayOfClassDeclaration = memberDefinition.getExceptions((Environment)this.env);
/* 598 */         sortClassDeclarations(arrayOfClassDeclaration);
/* 599 */         for (byte b = 0; b < arrayOfClassDeclaration.length; b++) {
/* 600 */           dataOutputStream.writeUTF(Names.mangleClass(arrayOfClassDeclaration[b]
/* 601 */                 .getName()).toString());
/*     */         }
/*     */       } 
/* 604 */       dataOutputStream.flush();
/*     */ 
/*     */       
/* 607 */       byte[] arrayOfByte = messageDigest.digest();
/* 608 */       for (byte b2 = 0; b2 < Math.min(8, arrayOfByte.length); b2++) {
/* 609 */         l += (arrayOfByte[b2] & 0xFF) << b2 * 8;
/*     */       }
/* 611 */     } catch (IOException iOException) {
/* 612 */       throw new Error("unexpected exception computing intetrface hash: " + iOException);
/*     */     }
/* 614 */     catch (NoSuchAlgorithmException noSuchAlgorithmException) {
/* 615 */       throw new Error("unexpected exception computing intetrface hash: " + noSuchAlgorithmException);
/*     */     } 
/*     */ 
/*     */     
/* 619 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sortClassDeclarations(ClassDeclaration[] paramArrayOfClassDeclaration) {
/* 629 */     for (byte b = 1; b < paramArrayOfClassDeclaration.length; b++) {
/* 630 */       ClassDeclaration classDeclaration = paramArrayOfClassDeclaration[b];
/* 631 */       String str = Names.mangleClass(classDeclaration.getName()).toString();
/*     */       byte b1;
/* 633 */       for (b1 = b; b1 > 0 && 
/* 634 */         str.compareTo(
/* 635 */           Names.mangleClass(paramArrayOfClassDeclaration[b1 - 1].getName()).toString()) < 0; b1--)
/*     */       {
/*     */ 
/*     */         
/* 639 */         paramArrayOfClassDeclaration[b1] = paramArrayOfClassDeclaration[b1 - 1];
/*     */       }
/* 641 */       paramArrayOfClassDeclaration[b1] = classDeclaration;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class Method
/*     */     implements Cloneable
/*     */   {
/*     */     private MemberDefinition memberDef;
/*     */ 
/*     */     
/*     */     private long methodHash;
/*     */ 
/*     */     
/*     */     private ClassDeclaration[] exceptions;
/*     */ 
/*     */     
/*     */     public MemberDefinition getMemberDefinition() {
/* 660 */       return this.memberDef;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Identifier getName() {
/* 667 */       return this.memberDef.getName();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Type getType() {
/* 674 */       return this.memberDef.getType();
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
/*     */     public ClassDeclaration[] getExceptions() {
/* 687 */       return (ClassDeclaration[])this.exceptions.clone();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getMethodHash() {
/* 695 */       return this.methodHash;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 702 */       return this.memberDef.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getOperationString() {
/* 710 */       return this.memberDef.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getNameAndDescriptor() {
/* 720 */       return this.memberDef.getName().toString() + this.memberDef
/* 721 */         .getType().getTypeSignature();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Method(MemberDefinition param1MemberDefinition) {
/* 758 */       this.memberDef = param1MemberDefinition;
/* 759 */       this.exceptions = param1MemberDefinition.getExceptions((Environment)RemoteClass.this.env);
/* 760 */       this.methodHash = computeMethodHash();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object clone() {
/*     */       try {
/* 768 */         return super.clone();
/* 769 */       } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 770 */         throw new Error("clone failed");
/*     */       } 
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
/*     */     private Method mergeWith(Method param1Method) {
/* 784 */       if (!getName().equals(param1Method.getName()) || 
/* 785 */         !getType().equals(param1Method.getType()))
/*     */       {
/* 787 */         throw new Error("attempt to merge method \"" + param1Method
/* 788 */             .getNameAndDescriptor() + "\" with \"" + 
/* 789 */             getNameAndDescriptor());
/*     */       }
/*     */       
/* 792 */       Vector<ClassDeclaration> vector = new Vector();
/*     */       
/*     */       try {
/* 795 */         collectCompatibleExceptions(param1Method.exceptions, this.exceptions, vector);
/*     */         
/* 797 */         collectCompatibleExceptions(this.exceptions, param1Method.exceptions, vector);
/*     */       }
/* 799 */       catch (ClassNotFound classNotFound) {
/* 800 */         RemoteClass.this.env.error(0L, "class.not.found", classNotFound.name, RemoteClass.this
/* 801 */             .getClassDefinition().getName());
/* 802 */         return null;
/*     */       } 
/*     */       
/* 805 */       Method method = (Method)clone();
/* 806 */       method.exceptions = new ClassDeclaration[vector.size()];
/* 807 */       vector.copyInto((Object[])method.exceptions);
/*     */       
/* 809 */       return method;
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
/*     */     private void collectCompatibleExceptions(ClassDeclaration[] param1ArrayOfClassDeclaration1, ClassDeclaration[] param1ArrayOfClassDeclaration2, Vector<ClassDeclaration> param1Vector) throws ClassNotFound {
/* 821 */       for (byte b = 0; b < param1ArrayOfClassDeclaration1.length; b++) {
/* 822 */         ClassDefinition classDefinition = param1ArrayOfClassDeclaration1[b].getClassDefinition((Environment)RemoteClass.this.env);
/* 823 */         if (!param1Vector.contains(param1ArrayOfClassDeclaration1[b])) {
/* 824 */           for (byte b1 = 0; b1 < param1ArrayOfClassDeclaration2.length; b1++) {
/* 825 */             if (classDefinition.subClassOf((Environment)RemoteClass.this.env, param1ArrayOfClassDeclaration2[b1])) {
/* 826 */               param1Vector.addElement(param1ArrayOfClassDeclaration1[b]);
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
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
/*     */     private long computeMethodHash() {
/* 843 */       long l = 0L;
/* 844 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */       try {
/* 846 */         MessageDigest messageDigest = MessageDigest.getInstance("SHA");
/* 847 */         DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(byteArrayOutputStream, messageDigest));
/*     */ 
/*     */         
/* 850 */         String str = getNameAndDescriptor();
/*     */         
/* 852 */         if (RemoteClass.this.env.verbose()) {
/* 853 */           System.out.println("[string used for method hash: \"" + str + "\"]");
/*     */         }
/*     */ 
/*     */         
/* 857 */         dataOutputStream.writeUTF(str);
/*     */ 
/*     */         
/* 860 */         dataOutputStream.flush();
/* 861 */         byte[] arrayOfByte = messageDigest.digest();
/* 862 */         for (byte b = 0; b < Math.min(8, arrayOfByte.length); b++) {
/* 863 */           l += (arrayOfByte[b] & 0xFF) << b * 8;
/*     */         }
/* 865 */       } catch (IOException iOException) {
/* 866 */         throw new Error("unexpected exception computing intetrface hash: " + iOException);
/*     */       }
/* 868 */       catch (NoSuchAlgorithmException noSuchAlgorithmException) {
/* 869 */         throw new Error("unexpected exception computing intetrface hash: " + noSuchAlgorithmException);
/*     */       } 
/*     */ 
/*     */       
/* 873 */       return l;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\RemoteClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */