/*     */ package sun.rmi.rmic.iiop;
/*     */
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
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
/*     */ public class ImplementationType
/*     */   extends ClassType
/*     */ {
/*     */   public static ImplementationType forImplementation(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean) {
/*  66 */     if (paramContextStack.anyErrors()) return null;
/*     */
/*  68 */     boolean bool = false;
/*  69 */     ImplementationType implementationType = null;
/*     */
/*     */
/*     */
/*     */     try {
/*  74 */       Type type = paramClassDefinition.getType();
/*  75 */       Type type1 = getType(type, paramContextStack);
/*     */
/*  77 */       if (type1 != null) {
/*     */
/*  79 */         if (!(type1 instanceof ImplementationType)) return null;
/*     */
/*     */
/*     */
/*  83 */         return (ImplementationType)type1;
/*     */       }
/*     */
/*     */
/*     */
/*     */
/*  89 */       if (couldBeImplementation(paramBoolean, paramContextStack, paramClassDefinition)) {
/*     */
/*     */
/*     */
/*  93 */         ImplementationType implementationType1 = new ImplementationType(paramContextStack, paramClassDefinition);
/*  94 */         putType(type, implementationType1, paramContextStack);
/*  95 */         paramContextStack.push(implementationType1);
/*  96 */         bool = true;
/*     */
/*  98 */         if (implementationType1.initialize(paramContextStack, paramBoolean)) {
/*  99 */           paramContextStack.pop(true);
/* 100 */           implementationType = implementationType1;
/*     */         } else {
/* 102 */           removeType(type, paramContextStack);
/* 103 */           paramContextStack.pop(false);
/*     */         }
/*     */       }
/* 106 */     } catch (CompilerError compilerError) {
/* 107 */       if (bool) paramContextStack.pop(false);
/*     */
/*     */     }
/* 110 */     return implementationType;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getTypeDescription() {
/* 117 */     return "Implementation";
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
/*     */   private ImplementationType(ContextStack paramContextStack, ClassDefinition paramClassDefinition) {
/* 130 */     super(100728832, paramClassDefinition, paramContextStack);
/*     */   }
/*     */
/*     */
/*     */
/*     */   private static boolean couldBeImplementation(boolean paramBoolean, ContextStack paramContextStack, ClassDefinition paramClassDefinition) {
/* 136 */     boolean bool = false;
/* 137 */     BatchEnvironment batchEnvironment = paramContextStack.getEnv();
/*     */
/*     */     try {
/* 140 */       if (!paramClassDefinition.isClass()) {
/* 141 */         failedConstraint(17, paramBoolean, paramContextStack, paramClassDefinition.getName());
/*     */       } else {
/* 143 */         bool = batchEnvironment.defRemote.implementedBy((Environment)batchEnvironment, paramClassDefinition.getClassDeclaration());
/* 144 */         if (!bool) failedConstraint(8, paramBoolean, paramContextStack, paramClassDefinition.getName());
/*     */       }
/* 146 */     } catch (ClassNotFound classNotFound) {
/* 147 */       classNotFound(paramContextStack, classNotFound);
/*     */     }
/*     */
/* 150 */     return bool;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean initialize(ContextStack paramContextStack, boolean paramBoolean) {
/* 159 */     boolean bool = false;
/* 160 */     ClassDefinition classDefinition = getClassDefinition();
/*     */
/* 162 */     if (initParents(paramContextStack)) {
/*     */
/*     */
/*     */
/* 166 */       Vector<InterfaceType> vector = new Vector();
/* 167 */       Vector vector1 = new Vector();
/*     */
/*     */
/*     */
/*     */       try {
/* 172 */         if (addRemoteInterfaces(vector, true, paramContextStack) != null) {
/*     */
/* 174 */           boolean bool1 = false;
/*     */
/*     */
/*     */
/* 178 */           for (byte b = 0; b < vector.size(); b++) {
/* 179 */             InterfaceType interfaceType = vector.elementAt(b);
/* 180 */             if (interfaceType.isType(4096) || interfaceType
/* 181 */               .isType(524288)) {
/* 182 */               bool1 = true;
/*     */             }
/*     */
/* 185 */             copyRemoteMethods(interfaceType, vector1);
/*     */           }
/*     */
/*     */
/*     */
/* 190 */           if (!bool1) {
/* 191 */             failedConstraint(8, paramBoolean, paramContextStack, getQualifiedName());
/* 192 */             return false;
/*     */           }
/*     */
/*     */
/*     */
/*     */
/* 198 */           if (checkMethods(classDefinition, vector1, paramContextStack, paramBoolean))
/*     */           {
/*     */
/*     */
/* 202 */             bool = initialize(vector, vector1, (Vector)null, paramContextStack, paramBoolean);
/*     */           }
/*     */         }
/* 205 */       } catch (ClassNotFound classNotFound) {
/* 206 */         classNotFound(paramContextStack, classNotFound);
/*     */       }
/*     */     }
/*     */
/* 210 */     return bool;
/*     */   }
/*     */
/*     */
/*     */   private static void copyRemoteMethods(InterfaceType paramInterfaceType, Vector<Method> paramVector) {
/* 215 */     if (paramInterfaceType.isType(4096)) {
/*     */
/*     */
/*     */
/* 219 */       Method[] arrayOfMethod = paramInterfaceType.getMethods();
/*     */
/* 221 */       for (byte b1 = 0; b1 < arrayOfMethod.length; b1++) {
/* 222 */         Method method = arrayOfMethod[b1];
/*     */
/* 224 */         if (!paramVector.contains(method)) {
/* 225 */           paramVector.addElement(method);
/*     */         }
/*     */       }
/*     */
/*     */
/*     */
/* 231 */       InterfaceType[] arrayOfInterfaceType = paramInterfaceType.getInterfaces();
/*     */
/* 233 */       for (byte b2 = 0; b2 < arrayOfInterfaceType.length; b2++) {
/* 234 */         copyRemoteMethods(arrayOfInterfaceType[b2], paramVector);
/*     */       }
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
/*     */   private boolean checkMethods(ClassDefinition paramClassDefinition, Vector paramVector, ContextStack paramContextStack, boolean paramBoolean) {
/* 247 */     Method[] arrayOfMethod = new Method[paramVector.size()];
/* 248 */     paramVector.copyInto((Object[])arrayOfMethod);
/*     */
/* 250 */     MemberDefinition memberDefinition = paramClassDefinition.getFirstMember();
/* 251 */     for (; memberDefinition != null;
/* 252 */       memberDefinition = memberDefinition.getNextMember()) {
/*     */
/* 254 */       if (memberDefinition.isMethod() && !memberDefinition.isConstructor() &&
/* 255 */         !memberDefinition.isInitializer())
/*     */       {
/*     */
/*     */
/* 259 */         if (!updateExceptions(memberDefinition, arrayOfMethod, paramContextStack, paramBoolean)) {
/* 260 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 264 */     return true;
/*     */   }
/*     */
/*     */
/*     */   private boolean updateExceptions(MemberDefinition paramMemberDefinition, Method[] paramArrayOfMethod, ContextStack paramContextStack, boolean paramBoolean) {
/* 269 */     int i = paramArrayOfMethod.length;
/* 270 */     String str = paramMemberDefinition.toString();
/*     */
/* 272 */     for (byte b = 0; b < i; b++) {
/* 273 */       Method method = paramArrayOfMethod[b];
/* 274 */       MemberDefinition memberDefinition = method.getMemberDefinition();
/*     */
/*     */
/*     */
/* 278 */       if (str.equals(memberDefinition.toString())) {
/*     */
/*     */         try {
/*     */
/*     */
/* 283 */           ValueType[] arrayOfValueType = getMethodExceptions(paramMemberDefinition, paramBoolean, paramContextStack);
/* 284 */           method.setImplExceptions(arrayOfValueType);
/* 285 */         } catch (Exception exception) {
/* 286 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 290 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\ImplementationType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
