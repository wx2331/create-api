/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import sun.tools.javac.Main;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BinaryClass
/*     */   extends ClassDefinition
/*     */   implements Constants
/*     */ {
/*     */   BinaryConstantPool cpool;
/*     */   BinaryAttribute atts;
/*     */   Vector dependencies;
/*     */   private boolean haveLoadedNested = false;
/*     */   private boolean basicCheckDone;
/*     */   private boolean basicChecking;
/*     */   
/*     */   public BinaryClass(Object paramObject, ClassDeclaration paramClassDeclaration1, int paramInt, ClassDeclaration paramClassDeclaration2, ClassDeclaration[] paramArrayOfClassDeclaration, Vector paramVector) {
/*  55 */     super(paramObject, 0L, paramClassDeclaration1, paramInt, (IdentifierToken)null, (IdentifierToken[])null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     this.basicCheckDone = false;
/*  66 */     this.basicChecking = false;
/*     */     this.dependencies = paramVector;
/*     */     this.superClass = paramClassDeclaration2;
/*     */     this.interfaces = paramArrayOfClassDeclaration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void basicCheck(Environment paramEnvironment) throws ClassNotFound {
/*  76 */     paramEnvironment.dtEnter("BinaryClass.basicCheck: " + getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     if (this.basicChecking || this.basicCheckDone) {
/*  84 */       paramEnvironment.dtExit("BinaryClass.basicCheck: OK " + getName());
/*     */       
/*     */       return;
/*     */     } 
/*  88 */     paramEnvironment.dtEvent("BinaryClass.basicCheck: CHECKING " + getName());
/*  89 */     this.basicChecking = true;
/*     */     
/*  91 */     super.basicCheck(paramEnvironment);
/*     */ 
/*     */     
/*  94 */     if (doInheritanceChecks) {
/*  95 */       collectInheritedMethods(paramEnvironment);
/*     */     }
/*     */     
/*  98 */     this.basicCheckDone = true;
/*  99 */     this.basicChecking = false;
/* 100 */     paramEnvironment.dtExit("BinaryClass.basicCheck: " + getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BinaryClass load(Environment paramEnvironment, DataInputStream paramDataInputStream) throws IOException {
/* 107 */     return load(paramEnvironment, paramDataInputStream, -7);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BinaryClass load(Environment paramEnvironment, DataInputStream paramDataInputStream, int paramInt) throws IOException {
/* 113 */     int i = paramDataInputStream.readInt();
/* 114 */     if (i != -889275714) {
/* 115 */       throw new ClassFormatError("wrong magic: " + i + ", expected " + -889275714);
/*     */     }
/* 117 */     int j = paramDataInputStream.readUnsignedShort();
/* 118 */     int k = paramDataInputStream.readUnsignedShort();
/* 119 */     if (k < 45)
/* 120 */       throw new ClassFormatError(
/* 121 */           Main.getText("javac.err.version.too.old", 
/*     */             
/* 123 */             String.valueOf(k))); 
/* 124 */     if (k > 52 || (k == 52 && j > 0))
/*     */     {
/*     */       
/* 127 */       throw new ClassFormatError(
/* 128 */           Main.getText("javac.err.version.too.recent", k + "." + j));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     BinaryConstantPool binaryConstantPool = new BinaryConstantPool(paramDataInputStream);
/*     */ 
/*     */     
/* 137 */     Vector vector = binaryConstantPool.getDependencies(paramEnvironment);
/*     */ 
/*     */     
/* 140 */     int m = paramDataInputStream.readUnsignedShort() & 0xE31;
/*     */ 
/*     */     
/* 143 */     ClassDeclaration classDeclaration1 = binaryConstantPool.getDeclaration(paramEnvironment, paramDataInputStream.readUnsignedShort());
/*     */ 
/*     */     
/* 146 */     ClassDeclaration classDeclaration2 = binaryConstantPool.getDeclaration(paramEnvironment, paramDataInputStream.readUnsignedShort());
/*     */ 
/*     */     
/* 149 */     ClassDeclaration[] arrayOfClassDeclaration = new ClassDeclaration[paramDataInputStream.readUnsignedShort()];
/* 150 */     for (byte b1 = 0; b1 < arrayOfClassDeclaration.length; b1++)
/*     */     {
/* 152 */       arrayOfClassDeclaration[b1] = binaryConstantPool.getDeclaration(paramEnvironment, paramDataInputStream.readUnsignedShort());
/*     */     }
/*     */ 
/*     */     
/* 156 */     BinaryClass binaryClass = new BinaryClass(null, classDeclaration1, m, classDeclaration2, arrayOfClassDeclaration, vector);
/*     */     
/* 158 */     binaryClass.cpool = binaryConstantPool;
/*     */ 
/*     */     
/* 161 */     binaryClass.addDependency(classDeclaration2);
/*     */ 
/*     */     
/* 164 */     int n = paramDataInputStream.readUnsignedShort(); int i1;
/* 165 */     for (i1 = 0; i1 < n; i1++) {
/*     */       
/* 167 */       int i2 = paramDataInputStream.readUnsignedShort() & 0xDF;
/*     */       
/* 169 */       Identifier identifier = binaryConstantPool.getIdentifier(paramDataInputStream.readUnsignedShort());
/*     */       
/* 171 */       Type type = binaryConstantPool.getType(paramDataInputStream.readUnsignedShort());
/* 172 */       BinaryAttribute binaryAttribute = BinaryAttribute.load(paramDataInputStream, binaryConstantPool, paramInt);
/* 173 */       binaryClass.addMember(new BinaryMember(binaryClass, i2, type, identifier, binaryAttribute));
/*     */     } 
/*     */ 
/*     */     
/* 177 */     i1 = paramDataInputStream.readUnsignedShort();
/* 178 */     for (byte b2 = 0; b2 < i1; b2++) {
/*     */       
/* 180 */       int i2 = paramDataInputStream.readUnsignedShort() & 0xD3F;
/*     */       
/* 182 */       Identifier identifier = binaryConstantPool.getIdentifier(paramDataInputStream.readUnsignedShort());
/*     */       
/* 184 */       Type type = binaryConstantPool.getType(paramDataInputStream.readUnsignedShort());
/* 185 */       BinaryAttribute binaryAttribute = BinaryAttribute.load(paramDataInputStream, binaryConstantPool, paramInt);
/* 186 */       binaryClass.addMember(new BinaryMember(binaryClass, i2, type, identifier, binaryAttribute));
/*     */     } 
/*     */ 
/*     */     
/* 190 */     binaryClass.atts = BinaryAttribute.load(paramDataInputStream, binaryConstantPool, paramInt);
/*     */ 
/*     */     
/* 193 */     byte[] arrayOfByte = binaryClass.getAttribute(idSourceFile);
/* 194 */     if (arrayOfByte != null) {
/* 195 */       DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte));
/*     */       
/* 197 */       binaryClass.source = binaryConstantPool.getString(dataInputStream.readUnsignedShort());
/*     */     } 
/*     */ 
/*     */     
/* 201 */     arrayOfByte = binaryClass.getAttribute(idDocumentation);
/* 202 */     if (arrayOfByte != null) {
/* 203 */       binaryClass.documentation = (new DataInputStream(new ByteArrayInputStream(arrayOfByte))).readUTF();
/*     */     }
/*     */ 
/*     */     
/* 207 */     if (binaryClass.getAttribute(idDeprecated) != null) {
/* 208 */       binaryClass.modifiers |= 0x40000;
/*     */     }
/*     */ 
/*     */     
/* 212 */     if (binaryClass.getAttribute(idSynthetic) != null) {
/* 213 */       binaryClass.modifiers |= 0x80000;
/*     */     }
/*     */     
/* 216 */     return binaryClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadNested(Environment paramEnvironment) {
/* 225 */     loadNested(paramEnvironment, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadNested(Environment paramEnvironment, int paramInt) {
/* 230 */     if (this.haveLoadedNested) {
/*     */ 
/*     */ 
/*     */       
/* 234 */       paramEnvironment.dtEvent("loadNested: DUPLICATE CALL SKIPPED");
/*     */       return;
/*     */     } 
/* 237 */     this.haveLoadedNested = true;
/*     */ 
/*     */     
/*     */     try {
/* 241 */       byte[] arrayOfByte = getAttribute(idInnerClasses);
/* 242 */       if (arrayOfByte != null) {
/* 243 */         initInnerClasses(paramEnvironment, arrayOfByte, paramInt);
/*     */       }
/* 245 */     } catch (IOException iOException) {
/*     */ 
/*     */ 
/*     */       
/* 249 */       paramEnvironment.error(0L, "malformed.attribute", getClassDeclaration(), idInnerClasses);
/*     */ 
/*     */       
/* 252 */       paramEnvironment.dtEvent("loadNested: MALFORMED ATTRIBUTE (InnerClasses)");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initInnerClasses(Environment paramEnvironment, byte[] paramArrayOfbyte, int paramInt) throws IOException {
/* 259 */     DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(paramArrayOfbyte));
/* 260 */     int i = dataInputStream.readUnsignedShort();
/* 261 */     for (byte b = 0; b < i; b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 287 */       int j = dataInputStream.readUnsignedShort();
/*     */       
/* 289 */       ClassDeclaration classDeclaration1 = this.cpool.getDeclaration(paramEnvironment, j);
/*     */ 
/*     */ 
/*     */       
/* 293 */       ClassDeclaration classDeclaration2 = null;
/*     */       
/* 295 */       int k = dataInputStream.readUnsignedShort();
/* 296 */       if (k != 0) {
/* 297 */         classDeclaration2 = this.cpool.getDeclaration(paramEnvironment, k);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 304 */       Identifier identifier = idNull;
/*     */       
/* 306 */       int m = dataInputStream.readUnsignedShort();
/* 307 */       if (m != 0) {
/* 308 */         identifier = Identifier.lookup(this.cpool.getString(m));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 313 */       int n = dataInputStream.readUnsignedShort();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 340 */       boolean bool = (classDeclaration2 != null && !identifier.equals(idNull) && ((n & 0x2) == 0 || (paramInt & 0x4) != 0)) ? true : false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 356 */       if (bool) {
/*     */         
/* 358 */         Identifier identifier1 = Identifier.lookupInner(classDeclaration2.getName(), identifier);
/*     */ 
/*     */         
/* 361 */         Type.tClass(identifier1);
/*     */         
/* 363 */         if (classDeclaration1.equals(getClassDeclaration())) {
/*     */           
/*     */           try {
/* 366 */             ClassDefinition classDefinition = classDeclaration2.getClassDefinition(paramEnvironment);
/* 367 */             initInner(classDefinition, n);
/* 368 */           } catch (ClassNotFound classNotFound) {}
/*     */         
/*     */         }
/* 371 */         else if (classDeclaration2.equals(getClassDeclaration())) {
/*     */ 
/*     */           
/*     */           try {
/* 375 */             ClassDefinition classDefinition = classDeclaration1.getClassDefinition(paramEnvironment);
/* 376 */             initOuter(classDefinition, n);
/* 377 */           } catch (ClassNotFound classNotFound) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initInner(ClassDefinition paramClassDefinition, int paramInt) {
/* 386 */     if (getOuterClass() != null) {
/*     */       return;
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
/* 401 */     if ((paramInt & 0x2) != 0) {
/*     */       
/* 403 */       paramInt &= 0xFFFFFFFA;
/* 404 */     } else if ((paramInt & 0x4) != 0) {
/*     */       
/* 406 */       paramInt &= 0xFFFFFFFE;
/*     */     } 
/* 408 */     if ((paramInt & 0x200) != 0)
/*     */     {
/*     */       
/* 411 */       paramInt |= 0x408;
/*     */     }
/* 413 */     if (paramClassDefinition.isInterface()) {
/*     */ 
/*     */       
/* 416 */       paramInt |= 0x9;
/* 417 */       paramInt &= 0xFFFFFFF9;
/*     */     } 
/* 419 */     this.modifiers = paramInt;
/*     */     
/* 421 */     setOuterClass(paramClassDefinition);
/*     */     
/* 423 */     MemberDefinition memberDefinition = getFirstMember();
/* 424 */     for (; memberDefinition != null; 
/* 425 */       memberDefinition = memberDefinition.getNextMember()) {
/* 426 */       if (memberDefinition.isUplevelValue() && paramClassDefinition
/* 427 */         .getType().equals(memberDefinition.getType()) && memberDefinition
/* 428 */         .getName().toString().startsWith("this$")) {
/* 429 */         setOuterMember(memberDefinition);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initOuter(ClassDefinition paramClassDefinition, int paramInt) {
/* 435 */     if (paramClassDefinition instanceof BinaryClass)
/* 436 */       ((BinaryClass)paramClassDefinition).initInner(this, paramInt); 
/* 437 */     addMember(new BinaryMember(paramClassDefinition));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(Environment paramEnvironment, OutputStream paramOutputStream) throws IOException {
/* 444 */     DataOutputStream dataOutputStream = new DataOutputStream(paramOutputStream);
/*     */ 
/*     */     
/* 447 */     dataOutputStream.writeInt(-889275714);
/* 448 */     dataOutputStream.writeShort(paramEnvironment.getMinorVersion());
/* 449 */     dataOutputStream.writeShort(paramEnvironment.getMajorVersion());
/*     */ 
/*     */     
/* 452 */     this.cpool.write(dataOutputStream, paramEnvironment);
/*     */ 
/*     */     
/* 455 */     dataOutputStream.writeShort(getModifiers() & 0xE31);
/* 456 */     dataOutputStream.writeShort(this.cpool.indexObject(getClassDeclaration(), paramEnvironment));
/* 457 */     dataOutputStream.writeShort((getSuperClass() != null) ? this.cpool
/* 458 */         .indexObject(getSuperClass(), paramEnvironment) : 0);
/* 459 */     dataOutputStream.writeShort(this.interfaces.length); byte b1;
/* 460 */     for (b1 = 0; b1 < this.interfaces.length; b1++) {
/* 461 */       dataOutputStream.writeShort(this.cpool.indexObject(this.interfaces[b1], paramEnvironment));
/*     */     }
/*     */ 
/*     */     
/* 465 */     b1 = 0; byte b2 = 0; MemberDefinition memberDefinition;
/* 466 */     for (memberDefinition = this.firstMember; memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) {
/* 467 */       if (memberDefinition.isMethod()) { b2++; } else { b1++; }
/*     */     
/*     */     } 
/* 470 */     dataOutputStream.writeShort(b1);
/* 471 */     for (memberDefinition = this.firstMember; memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) {
/* 472 */       if (!memberDefinition.isMethod()) {
/* 473 */         dataOutputStream.writeShort(memberDefinition.getModifiers() & 0xDF);
/* 474 */         String str1 = memberDefinition.getName().toString();
/* 475 */         String str2 = memberDefinition.getType().getTypeSignature();
/* 476 */         dataOutputStream.writeShort(this.cpool.indexString(str1, paramEnvironment));
/* 477 */         dataOutputStream.writeShort(this.cpool.indexString(str2, paramEnvironment));
/* 478 */         BinaryAttribute.write(((BinaryMember)memberDefinition).atts, dataOutputStream, this.cpool, paramEnvironment);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 483 */     dataOutputStream.writeShort(b2);
/* 484 */     for (memberDefinition = this.firstMember; memberDefinition != null; memberDefinition = memberDefinition.getNextMember()) {
/* 485 */       if (memberDefinition.isMethod()) {
/* 486 */         dataOutputStream.writeShort(memberDefinition.getModifiers() & 0xD3F);
/* 487 */         String str1 = memberDefinition.getName().toString();
/* 488 */         String str2 = memberDefinition.getType().getTypeSignature();
/* 489 */         dataOutputStream.writeShort(this.cpool.indexString(str1, paramEnvironment));
/* 490 */         dataOutputStream.writeShort(this.cpool.indexString(str2, paramEnvironment));
/* 491 */         BinaryAttribute.write(((BinaryMember)memberDefinition).atts, dataOutputStream, this.cpool, paramEnvironment);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 496 */     BinaryAttribute.write(this.atts, dataOutputStream, this.cpool, paramEnvironment);
/* 497 */     dataOutputStream.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getDependencies() {
/* 504 */     return this.dependencies.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDependency(ClassDeclaration paramClassDeclaration) {
/* 511 */     if (paramClassDeclaration != null && !this.dependencies.contains(paramClassDeclaration)) {
/* 512 */       this.dependencies.addElement(paramClassDeclaration);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BinaryConstantPool getConstants() {
/* 520 */     return this.cpool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getAttribute(Identifier paramIdentifier) {
/* 527 */     for (BinaryAttribute binaryAttribute = this.atts; binaryAttribute != null; binaryAttribute = binaryAttribute.next) {
/* 528 */       if (binaryAttribute.name.equals(paramIdentifier)) {
/* 529 */         return binaryAttribute.data;
/*     */       }
/*     */     } 
/* 532 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\BinaryClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */