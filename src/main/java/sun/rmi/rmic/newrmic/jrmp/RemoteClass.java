/*     */ package sun.rmi.rmic.newrmic.jrmp;
/*     */
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.Type;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.DigestOutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.rmi.rmic.newrmic.BatchEnvironment;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ final class RemoteClass
/*     */ {
/*     */   private final BatchEnvironment env;
/*     */   private final ClassDoc implClass;
/*     */   private ClassDoc[] remoteInterfaces;
/*     */   private Method[] remoteMethods;
/*     */   private long interfaceHash;
/*     */
/*     */   static RemoteClass forClass(BatchEnvironment paramBatchEnvironment, ClassDoc paramClassDoc) {
/*  86 */     RemoteClass remoteClass = new RemoteClass(paramBatchEnvironment, paramClassDoc);
/*  87 */     if (remoteClass.init()) {
/*  88 */       return remoteClass;
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private RemoteClass(BatchEnvironment paramBatchEnvironment, ClassDoc paramClassDoc) {
/*  99 */     this.env = paramBatchEnvironment;
/* 100 */     this.implClass = paramClassDoc;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   ClassDoc classDoc() {
/* 107 */     return this.implClass;
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
/*     */   ClassDoc[] remoteInterfaces() {
/* 124 */     return (ClassDoc[])this.remoteInterfaces.clone();
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
/*     */   Method[] remoteMethods() {
/* 139 */     return (Method[])this.remoteMethods.clone();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   long interfaceHash() {
/* 148 */     return this.interfaceHash;
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
/*     */   private boolean init() {
/* 160 */     if (this.implClass.isInterface()) {
/* 161 */       this.env.error("rmic.cant.make.stubs.for.interface", new String[] { this.implClass
/* 162 */             .qualifiedName() });
/* 163 */       return false;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 172 */     ArrayList<ClassDoc> arrayList = new ArrayList();
/* 173 */     for (ClassDoc classDoc = this.implClass; classDoc != null; classDoc = classDoc.superclass()) {
/* 174 */       for (ClassDoc classDoc1 : classDoc.interfaces()) {
/*     */
/*     */
/*     */
/*     */
/* 179 */         if (!arrayList.contains(classDoc1) && classDoc1
/* 180 */           .subclassOf(this.env.docRemote())) {
/*     */
/* 182 */           arrayList.add(classDoc1);
/* 183 */           if (this.env.verbose()) {
/* 184 */             this.env.output("[found remote interface: " + classDoc1
/* 185 */                 .qualifiedName() + "]");
/*     */           }
/*     */         }
/*     */       }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 194 */       if (classDoc == this.implClass && arrayList.isEmpty()) {
/* 195 */         if (this.implClass.subclassOf(this.env.docRemote())) {
/*     */
/*     */
/*     */
/*     */
/*     */
/* 201 */           this.env.error("rmic.must.implement.remote.directly", new String[] { this.implClass
/* 202 */                 .qualifiedName() });
/*     */
/*     */         }
/*     */         else {
/*     */
/*     */
/* 208 */           this.env.error("rmic.must.implement.remote", new String[] { this.implClass
/* 209 */                 .qualifiedName() });
/*     */         }
/* 211 */         return false;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 219 */     this
/* 220 */       .remoteInterfaces = arrayList.<ClassDoc>toArray(
/* 221 */         new ClassDoc[arrayList.size()]);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 228 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 229 */     boolean bool = false;
/* 230 */     for (ClassDoc classDoc1 : arrayList) {
/* 231 */       if (!collectRemoteMethods(classDoc1, (Map)hashMap))
/*     */       {
/*     */
/*     */
/*     */
/* 236 */         bool = true;
/*     */       }
/*     */     }
/* 239 */     if (bool) {
/* 240 */       return false;
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
/* 251 */     String[] arrayOfString = (String[])hashMap.keySet().toArray((Object[])new String[hashMap.size()]);
/* 252 */     Arrays.sort((Object[])arrayOfString);
/* 253 */     this.remoteMethods = new Method[hashMap.size()];
/* 254 */     for (byte b = 0; b < this.remoteMethods.length; b++) {
/* 255 */       this.remoteMethods[b] = (Method)hashMap.get(arrayOfString[b]);
/* 256 */       if (this.env.verbose()) {
/*     */
/* 258 */         String str = "[found remote method <" + b + ">: " + this.remoteMethods[b].operationString();
/* 259 */         ClassDoc[] arrayOfClassDoc = this.remoteMethods[b].exceptionTypes();
/* 260 */         if (arrayOfClassDoc.length > 0) {
/* 261 */           str = str + " throws ";
/* 262 */           for (byte b1 = 0; b1 < arrayOfClassDoc.length; b1++) {
/* 263 */             if (b1 > 0) {
/* 264 */               str = str + ", ";
/*     */             }
/* 266 */             str = str + arrayOfClassDoc[b1].qualifiedName();
/*     */           }
/*     */         }
/*     */
/* 270 */         str = str + "\n\tname and descriptor = \"" + this.remoteMethods[b].nameAndDescriptor();
/*     */
/* 272 */         str = str + "\n\tmethod hash = " + this.remoteMethods[b].methodHash() + "]";
/* 273 */         this.env.output(str);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 282 */     this.interfaceHash = computeInterfaceHash();
/*     */
/* 284 */     return true;
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
/*     */   private boolean collectRemoteMethods(ClassDoc paramClassDoc, Map<String, Method> paramMap) {
/* 296 */     if (!paramClassDoc.isInterface()) {
/* 297 */       throw new AssertionError(paramClassDoc
/* 298 */           .qualifiedName() + " not an interface");
/*     */     }
/*     */
/* 301 */     boolean bool = false;
/*     */
/*     */
/*     */
/*     */
/*     */
/* 307 */     label49: for (MethodDoc methodDoc : paramClassDoc.methods()) {
/*     */
/*     */
/*     */
/*     */
/*     */
/* 313 */       boolean bool1 = false;
/* 314 */       for (ClassDoc classDoc : methodDoc.thrownExceptions()) {
/* 315 */         if (this.env.docRemoteException().subclassOf(classDoc)) {
/* 316 */           bool1 = true;
/*     */
/*     */
/*     */
/*     */           break;
/*     */         }
/*     */       }
/*     */
/*     */
/*     */
/* 326 */       if (!bool1) {
/* 327 */         this.env.error("rmic.must.throw.remoteexception", new String[] { paramClassDoc
/* 328 */               .qualifiedName(), methodDoc
/* 329 */               .name() + methodDoc.signature() });
/* 330 */         bool = true;
/*     */
/*     */
/*     */
/*     */       }
/*     */       else {
/*     */
/*     */
/*     */
/*     */
/* 340 */         MethodDoc methodDoc1 = findImplMethod(methodDoc);
/* 341 */         if (methodDoc1 != null) {
/* 342 */           for (ClassDoc classDoc : methodDoc1.thrownExceptions()) {
/* 343 */             if (!classDoc.subclassOf(this.env.docException())) {
/* 344 */               this.env.error("rmic.must.only.throw.exception", new String[] { methodDoc1
/* 345 */                     .name() + methodDoc1.signature(), classDoc
/* 346 */                     .qualifiedName() });
/* 347 */               bool = true;
/*     */
/*     */
/*     */
/*     */               continue label49;
/*     */             }
/*     */           }
/*     */         }
/*     */
/*     */
/* 357 */         Method method1 = new Method(methodDoc);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 372 */         String str = method1.nameAndDescriptor();
/* 373 */         Method method2 = paramMap.get(str);
/* 374 */         if (method2 != null) {
/* 375 */           method1 = method1.mergeWith(method2);
/*     */         }
/* 377 */         paramMap.put(str, method1);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/* 383 */     for (ClassDoc classDoc : paramClassDoc.interfaces()) {
/* 384 */       if (!collectRemoteMethods(classDoc, paramMap)) {
/* 385 */         bool = true;
/*     */       }
/*     */     }
/*     */
/* 389 */     return !bool;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private MethodDoc findImplMethod(MethodDoc paramMethodDoc) {
/* 399 */     String str1 = paramMethodDoc.name();
/* 400 */     String str2 = Util.methodDescriptorOf(paramMethodDoc);
/* 401 */     for (MethodDoc methodDoc : this.implClass.methods()) {
/* 402 */       if (str1.equals(methodDoc.name()) && str2
/* 403 */         .equals(Util.methodDescriptorOf(methodDoc)))
/*     */       {
/* 405 */         return methodDoc;
/*     */       }
/*     */     }
/* 408 */     return null;
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
/*     */   private long computeInterfaceHash() {
/* 430 */     long l = 0L;
/* 431 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */     try {
/* 433 */       MessageDigest messageDigest = MessageDigest.getInstance("SHA");
/* 434 */       DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(byteArrayOutputStream, messageDigest));
/*     */
/*     */
/* 437 */       dataOutputStream.writeInt(1);
/*     */
/* 439 */       for (Method method : this.remoteMethods) {
/* 440 */         MethodDoc methodDoc = method.methodDoc();
/*     */
/* 442 */         dataOutputStream.writeUTF(methodDoc.name());
/* 443 */         dataOutputStream.writeUTF(Util.methodDescriptorOf(methodDoc));
/*     */
/*     */
/* 446 */         ClassDoc[] arrayOfClassDoc = methodDoc.thrownExceptions();
/* 447 */         Arrays.sort(arrayOfClassDoc, new ClassDocComparator());
/* 448 */         for (ClassDoc classDoc : arrayOfClassDoc) {
/* 449 */           dataOutputStream.writeUTF(Util.binaryNameOf(classDoc));
/*     */         }
/*     */       }
/* 452 */       dataOutputStream.flush();
/*     */
/*     */
/* 455 */       byte[] arrayOfByte = messageDigest.digest();
/* 456 */       for (byte b = 0; b < Math.min(8, arrayOfByte.length); b++) {
/* 457 */         l += (arrayOfByte[b] & 0xFF) << b * 8;
/*     */       }
/* 459 */     } catch (IOException iOException) {
/* 460 */       throw new AssertionError(iOException);
/* 461 */     } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
/* 462 */       throw new AssertionError(noSuchAlgorithmException);
/*     */     }
/*     */
/* 465 */     return l;
/*     */   }
/*     */
/*     */   private static class ClassDocComparator
/*     */     implements Comparator<ClassDoc>
/*     */   {
/*     */     private ClassDocComparator() {}
/*     */
/*     */     public int compare(ClassDoc param1ClassDoc1, ClassDoc param1ClassDoc2) {
/* 474 */       return Util.binaryNameOf(param1ClassDoc1).compareTo(Util.binaryNameOf(param1ClassDoc2));
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   final class Method
/*     */     implements Cloneable
/*     */   {
/*     */     private final MethodDoc methodDoc;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private final String operationString;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private final String nameAndDescriptor;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private final long methodHash;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private ClassDoc[] exceptionTypes;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     Method(MethodDoc param1MethodDoc) {
/* 519 */       this.methodDoc = param1MethodDoc;
/* 520 */       this.exceptionTypes = param1MethodDoc.thrownExceptions();
/*     */
/*     */
/*     */
/*     */
/* 525 */       Arrays.sort(this.exceptionTypes, new ClassDocComparator());
/* 526 */       this.operationString = computeOperationString();
/* 527 */       this
/* 528 */         .nameAndDescriptor = param1MethodDoc.name() + Util.methodDescriptorOf(param1MethodDoc);
/* 529 */       this.methodHash = computeMethodHash();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     MethodDoc methodDoc() {
/* 537 */       return this.methodDoc;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     Type[] parameterTypes() {
/* 544 */       Parameter[] arrayOfParameter = this.methodDoc.parameters();
/* 545 */       Type[] arrayOfType = new Type[arrayOfParameter.length];
/* 546 */       for (byte b = 0; b < arrayOfType.length; b++) {
/* 547 */         arrayOfType[b] = arrayOfParameter[b].type();
/*     */       }
/* 549 */       return arrayOfType;
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
/*     */     ClassDoc[] exceptionTypes() {
/* 562 */       return (ClassDoc[])this.exceptionTypes.clone();
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     long methodHash() {
/* 570 */       return this.methodHash;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     String operationString() {
/* 579 */       return this.operationString;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     String nameAndDescriptor() {
/* 587 */       return this.nameAndDescriptor;
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
/*     */     Method mergeWith(Method param1Method) {
/* 600 */       if (!nameAndDescriptor().equals(param1Method.nameAndDescriptor())) {
/* 601 */         throw new AssertionError("attempt to merge method \"" + param1Method
/*     */
/* 603 */             .nameAndDescriptor() + "\" with \"" +
/* 604 */             nameAndDescriptor());
/*     */       }
/*     */
/* 607 */       ArrayList<ClassDoc> arrayList = new ArrayList();
/* 608 */       collectCompatibleExceptions(param1Method.exceptionTypes, this.exceptionTypes, arrayList);
/*     */
/* 610 */       collectCompatibleExceptions(this.exceptionTypes, param1Method.exceptionTypes, arrayList);
/*     */
/*     */
/* 613 */       Method method = clone();
/* 614 */       method
/* 615 */         .exceptionTypes = arrayList.<ClassDoc>toArray(new ClassDoc[arrayList.size()]);
/*     */
/* 617 */       return method;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     protected Method clone() {
/*     */       try {
/* 626 */         return (Method)super.clone();
/* 627 */       } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 628 */         throw new AssertionError(cloneNotSupportedException);
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
/*     */     private void collectCompatibleExceptions(ClassDoc[] param1ArrayOfClassDoc1, ClassDoc[] param1ArrayOfClassDoc2, List<ClassDoc> param1List) {
/* 641 */       for (ClassDoc classDoc : param1ArrayOfClassDoc1) {
/* 642 */         if (!param1List.contains(classDoc)) {
/* 643 */           for (ClassDoc classDoc1 : param1ArrayOfClassDoc2) {
/* 644 */             if (classDoc.subclassOf(classDoc1)) {
/* 645 */               param1List.add(classDoc);
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
/*     */     private long computeMethodHash() {
/* 660 */       long l = 0L;
/* 661 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
/*     */       try {
/* 663 */         MessageDigest messageDigest = MessageDigest.getInstance("SHA");
/* 664 */         DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(byteArrayOutputStream, messageDigest));
/*     */
/*     */
/* 667 */         String str = nameAndDescriptor();
/* 668 */         dataOutputStream.writeUTF(str);
/*     */
/*     */
/* 671 */         dataOutputStream.flush();
/* 672 */         byte[] arrayOfByte = messageDigest.digest();
/* 673 */         for (byte b = 0; b < Math.min(8, arrayOfByte.length); b++) {
/* 674 */           l += (arrayOfByte[b] & 0xFF) << b * 8;
/*     */         }
/* 676 */       } catch (IOException iOException) {
/* 677 */         throw new AssertionError(iOException);
/* 678 */       } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
/* 679 */         throw new AssertionError(noSuchAlgorithmException);
/*     */       }
/*     */
/* 682 */       return l;
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
/*     */     private String computeOperationString() {
/* 696 */       Type type = this.methodDoc.returnType();
/*     */
/* 698 */       String str = type.qualifiedTypeName() + " " + this.methodDoc.name() + "(";
/* 699 */       Parameter[] arrayOfParameter = this.methodDoc.parameters();
/* 700 */       for (byte b = 0; b < arrayOfParameter.length; b++) {
/* 701 */         if (b > 0) {
/* 702 */           str = str + ", ";
/*     */         }
/* 704 */         str = str + arrayOfParameter[b].type().toString();
/*     */       }
/* 706 */       str = str + ")" + type.dimension();
/* 707 */       return str;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\newrmic\jrmp\RemoteClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
