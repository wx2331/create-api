/*     */ package sun.rmi.rmic.iiop;
/*     */
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Environment;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class ValueType
/*     */   extends ClassType
/*     */ {
/*     */   private boolean isCustom;
/*     */
/*     */   public static ValueType forValue(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean) {
/*  72 */     if (paramContextStack.anyErrors()) return null;
/*     */
/*     */
/*     */
/*  76 */     Type type = paramClassDefinition.getType();
/*  77 */     String str = type.toString();
/*  78 */     Type type1 = getType(str, paramContextStack);
/*     */
/*  80 */     if (type1 != null) {
/*     */
/*  82 */       if (!(type1 instanceof ValueType)) return null;
/*     */
/*     */
/*     */
/*  86 */       return (ValueType)type1;
/*     */     }
/*     */
/*     */
/*     */
/*  91 */     boolean bool = false;
/*     */
/*  93 */     if (paramClassDefinition.getClassDeclaration().getName() == idJavaLangClass) {
/*     */
/*     */
/*     */
/*     */
/*  98 */       bool = true;
/*  99 */       BatchEnvironment batchEnvironment = paramContextStack.getEnv();
/* 100 */       ClassDeclaration classDeclaration = batchEnvironment.getClassDeclaration(idClassDesc);
/* 101 */       ClassDefinition classDefinition = null;
/*     */
/*     */       try {
/* 104 */         classDefinition = classDeclaration.getClassDefinition((Environment)batchEnvironment);
/* 105 */       } catch (ClassNotFound classNotFound) {
/* 106 */         classNotFound(paramContextStack, classNotFound);
/* 107 */         return null;
/*     */       }
/*     */
/* 110 */       paramClassDefinition = classDefinition;
/*     */     }
/*     */
/*     */
/*     */
/* 115 */     if (couldBeValue(paramContextStack, paramClassDefinition)) {
/*     */
/*     */
/*     */
/* 119 */       ValueType valueType = new ValueType(paramClassDefinition, paramContextStack, bool);
/* 120 */       putType(str, valueType, paramContextStack);
/* 121 */       paramContextStack.push(valueType);
/*     */
/* 123 */       if (valueType.initialize(paramContextStack, paramBoolean)) {
/* 124 */         paramContextStack.pop(true);
/* 125 */         return valueType;
/*     */       }
/* 127 */       removeType(str, paramContextStack);
/* 128 */       paramContextStack.pop(false);
/* 129 */       return null;
/*     */     }
/*     */
/* 132 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public String getTypeDescription() {
/* 141 */     String str = addExceptionDescription("Value");
/* 142 */     if (this.isCustom) {
/* 143 */       str = "Custom " + str;
/*     */     }
/* 145 */     if (this.isIDLEntity) {
/* 146 */       str = str + " [IDLEntity]";
/*     */     }
/* 148 */     return str;
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
/*     */   public boolean isCustom() {
/* 160 */     return this.isCustom;
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
/*     */   private ValueType(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean) {
/* 175 */     super(paramContextStack, paramClassDefinition, 100696064);
/* 176 */     this.isCustom = false;
/*     */
/*     */
/*     */
/*     */
/* 181 */     if (paramBoolean) {
/* 182 */       setNames(idJavaLangClass, IDL_CLASS_MODULE, "ClassDesc");
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
/*     */   private static boolean couldBeValue(ContextStack paramContextStack, ClassDefinition paramClassDefinition) {
/* 196 */     boolean bool = false;
/* 197 */     ClassDeclaration classDeclaration = paramClassDefinition.getClassDeclaration();
/* 198 */     BatchEnvironment batchEnvironment = paramContextStack.getEnv();
/*     */
/*     */
/*     */
/*     */     try {
/* 203 */       if (batchEnvironment.defRemote.implementedBy((Environment)batchEnvironment, classDeclaration)) {
/* 204 */         failedConstraint(10, false, paramContextStack, paramClassDefinition.getName());
/*     */
/*     */
/*     */
/*     */       }
/* 209 */       else if (!batchEnvironment.defSerializable.implementedBy((Environment)batchEnvironment, classDeclaration)) {
/* 210 */         failedConstraint(11, false, paramContextStack, paramClassDefinition.getName());
/*     */       } else {
/* 212 */         bool = true;
/*     */       }
/*     */
/* 215 */     } catch (ClassNotFound classNotFound) {
/* 216 */       classNotFound(paramContextStack, classNotFound);
/*     */     }
/*     */
/* 219 */     return bool;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean initialize(ContextStack paramContextStack, boolean paramBoolean) {
/* 227 */     ClassDefinition classDefinition = getClassDefinition();
/* 228 */     ClassDeclaration classDeclaration = getClassDeclaration();
/*     */
/*     */
/*     */
/*     */
/*     */     try {
/* 234 */       if (!initParents(paramContextStack)) {
/* 235 */         failedConstraint(12, paramBoolean, paramContextStack, getQualifiedName());
/* 236 */         return false;
/*     */       }
/*     */
/*     */
/*     */
/*     */
/* 242 */       Vector vector1 = new Vector();
/* 243 */       Vector vector2 = new Vector();
/* 244 */       Vector vector3 = new Vector();
/*     */
/*     */
/*     */
/* 248 */       if (addNonRemoteInterfaces(vector1, paramContextStack) != null)
/*     */       {
/*     */
/*     */
/* 252 */         if (addAllMethods(classDefinition, vector2, false, false, paramContextStack) != null)
/*     */         {
/*     */
/* 255 */           if (updateParentClassMethods(classDefinition, vector2, false, paramContextStack) != null) {
/*     */
/*     */
/*     */
/* 259 */             if (addAllMembers(vector3, false, false, paramContextStack)) {
/*     */
/*     */
/*     */
/* 263 */               if (!initialize(vector1, vector2, vector3, paramContextStack, paramBoolean)) {
/* 264 */                 return false;
/*     */               }
/*     */
/*     */
/*     */
/* 269 */               boolean bool = false;
/* 270 */               if (!this.env.defExternalizable.implementedBy((Environment)this.env, classDeclaration)) {
/*     */
/*     */
/*     */
/*     */
/* 275 */                 if (!checkPersistentFields(getClassInstance(), paramBoolean)) {
/* 276 */                   return false;
/*     */
/*     */                 }
/*     */               }
/*     */               else {
/*     */
/* 282 */                 bool = true;
/*     */               }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 292 */               if (bool) {
/* 293 */                 this.isCustom = true;
/*     */               } else {
/* 295 */                 MemberDefinition memberDefinition = classDefinition.getFirstMember();
/* 296 */                 for (; memberDefinition != null;
/* 297 */                   memberDefinition = memberDefinition.getNextMember()) {
/*     */
/* 299 */                   if (memberDefinition.isMethod() &&
/* 300 */                     !memberDefinition.isInitializer() && memberDefinition
/* 301 */                     .isPrivate() && memberDefinition
/* 302 */                     .getName().toString().equals("writeObject")) {
/*     */
/*     */
/*     */
/* 306 */                     Type type1 = memberDefinition.getType();
/* 307 */                     Type type2 = type1.getReturnType();
/*     */
/* 309 */                     if (type2 == Type.tVoid) {
/*     */
/*     */
/*     */
/* 313 */                       Type[] arrayOfType = type1.getArgumentTypes();
/* 314 */                       if (arrayOfType.length == 1 && arrayOfType[0]
/* 315 */                         .getTypeSignature().equals("Ljava/io/ObjectOutputStream;"))
/*     */                       {
/*     */
/*     */
/*     */
/* 320 */                         this.isCustom = true;
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */
/* 328 */             return true;
/*     */           }
/*     */         }
/*     */       }
/* 332 */     } catch (ClassNotFound classNotFound) {
/* 333 */       classNotFound(paramContextStack, classNotFound);
/*     */     }
/*     */
/* 336 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean checkPersistentFields(Class paramClass, boolean paramBoolean) {
/* 344 */     for (byte b1 = 0; b1 < this.methods.length; b1++) {
/* 345 */       if (this.methods[b1].getName().equals("writeObject") && (this.methods[b1]
/* 346 */         .getArguments()).length == 1) {
/*     */
/* 348 */         Type type1 = this.methods[b1].getReturnType();
/* 349 */         Type type2 = this.methods[b1].getArguments()[0];
/* 350 */         String str = type2.getQualifiedName();
/*     */
/* 352 */         if (type1.isType(1) && str
/* 353 */           .equals("java.io.ObjectOutputStream"))
/*     */         {
/*     */
/*     */
/* 357 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/* 364 */     MemberDefinition memberDefinition = null;
/*     */
/* 366 */     for (byte b2 = 0; b2 < this.members.length; b2++) {
/* 367 */       if (this.members[b2].getName().equals("serialPersistentFields")) {
/*     */
/* 369 */         Member member = this.members[b2];
/* 370 */         Type type1 = member.getType();
/* 371 */         Type type2 = type1.getElementType();
/*     */
/*     */
/*     */
/*     */
/* 376 */         if (type2 != null && type2
/* 377 */           .getQualifiedName().equals("java.io.ObjectStreamField"))
/*     */         {
/*     */
/*     */
/* 381 */           if (member.isStatic() && member
/* 382 */             .isFinal() && member
/* 383 */             .isPrivate()) {
/*     */
/*     */
/*     */
/* 387 */             memberDefinition = member.getMemberDefinition();
/*     */
/*     */           }
/*     */           else {
/*     */
/*     */
/* 393 */             failedConstraint(4, paramBoolean, this.stack, getQualifiedName());
/* 394 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/* 403 */     if (memberDefinition == null) {
/* 404 */       return true;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/* 410 */     Hashtable hashtable = getPersistentFields(paramClass);
/* 411 */     boolean bool = true;
/*     */
/* 413 */     for (byte b3 = 0; b3 < this.members.length; b3++) {
/* 414 */       String str1 = this.members[b3].getName();
/* 415 */       String str2 = this.members[b3].getType().getSignature();
/*     */
/*     */
/*     */
/* 419 */       String str3 = (String)hashtable.get(str1);
/*     */
/* 421 */       if (str3 == null) {
/*     */
/*     */
/*     */
/* 425 */         this.members[b3].setTransient();
/*     */
/*     */
/*     */
/*     */
/*     */       }
/* 431 */       else if (str3.equals(str2)) {
/*     */
/*     */
/*     */
/* 435 */         hashtable.remove(str1);
/*     */
/*     */       }
/*     */       else {
/*     */
/*     */
/* 441 */         bool = false;
/* 442 */         failedConstraint(2, paramBoolean, this.stack, str1, getQualifiedName());
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 450 */     if (bool && hashtable.size() > 0) {
/*     */
/* 452 */       bool = false;
/* 453 */       failedConstraint(9, paramBoolean, this.stack, getQualifiedName());
/*     */     }
/*     */
/*     */
/*     */
/* 458 */     return bool;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private Hashtable getPersistentFields(Class<?> paramClass) {
/* 465 */     Hashtable<Object, Object> hashtable = new Hashtable<>();
/* 466 */     ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(paramClass);
/* 467 */     if (objectStreamClass != null) {
/* 468 */       ObjectStreamField[] arrayOfObjectStreamField = objectStreamClass.getFields();
/* 469 */       for (byte b = 0; b < arrayOfObjectStreamField.length; b++) {
/*     */
/* 471 */         String str1, str2 = String.valueOf(arrayOfObjectStreamField[b].getTypeCode());
/* 472 */         if (arrayOfObjectStreamField[b].isPrimitive()) {
/* 473 */           str1 = str2;
/*     */         } else {
/* 475 */           if (arrayOfObjectStreamField[b].getTypeCode() == '[') {
/* 476 */             str2 = "";
/*     */           }
/* 478 */           str1 = str2 + arrayOfObjectStreamField[b].getType().getName().replace('.', '/');
/* 479 */           if (str1.endsWith(";")) {
/* 480 */             str1 = str1.substring(0, str1.length() - 1);
/*     */           }
/*     */         }
/* 483 */         hashtable.put(arrayOfObjectStreamField[b].getName(), str1);
/*     */       }
/*     */     }
/* 486 */     return hashtable;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\ValueType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
