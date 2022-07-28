/*     */ package sun.rmi.rmic.iiop;
/*     */
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
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
/*     */ public class RemoteType
/*     */   extends InterfaceType
/*     */ {
/*     */   public static RemoteType forRemote(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean) {
/*  65 */     if (paramContextStack.anyErrors()) return null;
/*     */
/*  67 */     boolean bool = false;
/*  68 */     RemoteType remoteType = null;
/*     */
/*     */
/*     */
/*     */     try {
/*  73 */       Type type = paramClassDefinition.getType();
/*  74 */       Type type1 = getType(type, paramContextStack);
/*     */
/*  76 */       if (type1 != null) {
/*     */
/*  78 */         if (!(type1 instanceof RemoteType)) return null;
/*     */
/*     */
/*     */
/*  82 */         return (RemoteType)type1;
/*     */       }
/*     */
/*     */
/*     */
/*  87 */       if (couldBeRemote(paramBoolean, paramContextStack, paramClassDefinition)) {
/*     */
/*     */
/*     */
/*  91 */         RemoteType remoteType1 = new RemoteType(paramContextStack, paramClassDefinition);
/*  92 */         putType(type, remoteType1, paramContextStack);
/*  93 */         paramContextStack.push(remoteType1);
/*  94 */         bool = true;
/*     */
/*  96 */         if (remoteType1.initialize(paramBoolean, paramContextStack)) {
/*  97 */           paramContextStack.pop(true);
/*  98 */           remoteType = remoteType1;
/*     */         } else {
/* 100 */           removeType(type, paramContextStack);
/* 101 */           paramContextStack.pop(false);
/*     */         }
/*     */       }
/* 104 */     } catch (CompilerError compilerError) {
/* 105 */       if (bool) paramContextStack.pop(false);
/*     */
/*     */     }
/* 108 */     return remoteType;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getTypeDescription() {
/* 115 */     return "Remote interface";
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
/*     */   protected RemoteType(ContextStack paramContextStack, ClassDefinition paramClassDefinition) {
/* 127 */     super(paramContextStack, paramClassDefinition, 167776256);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected RemoteType(ContextStack paramContextStack, ClassDefinition paramClassDefinition, int paramInt) {
/* 135 */     super(paramContextStack, paramClassDefinition, paramInt);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private static boolean couldBeRemote(boolean paramBoolean, ContextStack paramContextStack, ClassDefinition paramClassDefinition) {
/* 146 */     boolean bool = false;
/* 147 */     BatchEnvironment batchEnvironment = paramContextStack.getEnv();
/*     */
/*     */     try {
/* 150 */       if (!paramClassDefinition.isInterface()) {
/* 151 */         failedConstraint(16, paramBoolean, paramContextStack, paramClassDefinition.getName());
/*     */       } else {
/* 153 */         bool = batchEnvironment.defRemote.implementedBy((Environment)batchEnvironment, paramClassDefinition.getClassDeclaration());
/* 154 */         if (!bool) failedConstraint(1, paramBoolean, paramContextStack, paramClassDefinition.getName());
/*     */       }
/* 156 */     } catch (ClassNotFound classNotFound) {
/* 157 */       classNotFound(paramContextStack, classNotFound);
/*     */     }
/*     */
/* 160 */     return bool;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean initialize(boolean paramBoolean, ContextStack paramContextStack) {
/* 169 */     boolean bool = false;
/*     */
/*     */
/*     */
/* 173 */     Vector vector1 = new Vector();
/* 174 */     Vector vector2 = new Vector();
/* 175 */     Vector vector3 = new Vector();
/*     */
/* 177 */     if (isConformingRemoteInterface(vector1, vector2, vector3, paramBoolean, paramContextStack))
/*     */     {
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 185 */       bool = initialize(vector1, vector2, vector3, paramContextStack, paramBoolean);
/*     */     }
/*     */
/* 188 */     return bool;
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
/*     */   private boolean isConformingRemoteInterface(Vector paramVector1, Vector<Method> paramVector2, Vector paramVector3, boolean paramBoolean, ContextStack paramContextStack) {
/* 210 */     ClassDefinition classDefinition = getClassDefinition();
/*     */
/*     */
/*     */
/*     */
/*     */     try {
/* 216 */       if (addRemoteInterfaces(paramVector1, false, paramContextStack) == null) {
/* 217 */         return false;
/*     */       }
/*     */
/*     */
/*     */
/* 222 */       if (!addAllMembers(paramVector3, true, paramBoolean, paramContextStack)) {
/* 223 */         return false;
/*     */       }
/*     */
/*     */
/*     */
/* 228 */       if (addAllMethods(classDefinition, paramVector2, true, paramBoolean, paramContextStack) == null)
/*     */       {
/* 230 */         return false;
/*     */       }
/*     */
/*     */
/*     */
/* 235 */       boolean bool = true;
/* 236 */       for (byte b = 0; b < paramVector2.size(); b++) {
/* 237 */         if (!isConformingRemoteMethod(paramVector2.elementAt(b), paramBoolean)) {
/* 238 */           bool = false;
/*     */         }
/*     */       }
/* 241 */       if (!bool) {
/* 242 */         return false;
/*     */       }
/* 244 */     } catch (ClassNotFound classNotFound) {
/* 245 */       classNotFound(paramContextStack, classNotFound);
/* 246 */       return false;
/*     */     }
/*     */
/* 249 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\RemoteType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
