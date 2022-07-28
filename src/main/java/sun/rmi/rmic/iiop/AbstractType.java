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
/*     */ public class AbstractType
/*     */   extends RemoteType
/*     */ {
/*     */   public static AbstractType forAbstract(ClassDefinition paramClassDefinition, ContextStack paramContextStack, boolean paramBoolean) {
/*  65 */     boolean bool = false;
/*  66 */     AbstractType abstractType = null;
/*     */
/*     */
/*     */
/*     */
/*     */     try {
/*  72 */       Type type = paramClassDefinition.getType();
/*  73 */       Type type1 = getType(type, paramContextStack);
/*     */
/*  75 */       if (type1 != null) {
/*     */
/*  77 */         if (!(type1 instanceof AbstractType)) return null;
/*     */
/*     */
/*     */
/*  81 */         return (AbstractType)type1;
/*     */       }
/*     */
/*     */
/*     */
/*     */
/*  87 */       if (couldBeAbstract(paramContextStack, paramClassDefinition, paramBoolean)) {
/*     */
/*     */
/*     */
/*  91 */         AbstractType abstractType1 = new AbstractType(paramContextStack, paramClassDefinition);
/*  92 */         putType(type, abstractType1, paramContextStack);
/*  93 */         paramContextStack.push(abstractType1);
/*  94 */         bool = true;
/*     */
/*  96 */         if (abstractType1.initialize(paramBoolean, paramContextStack)) {
/*  97 */           paramContextStack.pop(true);
/*  98 */           abstractType = abstractType1;
/*     */         } else {
/* 100 */           removeType(type, paramContextStack);
/* 101 */           paramContextStack.pop(false);
/*     */         }
/*     */       }
/* 104 */     } catch (CompilerError compilerError) {
/* 105 */       if (bool) paramContextStack.pop(false);
/*     */
/*     */     }
/* 108 */     return abstractType;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public String getTypeDescription() {
/* 115 */     return "Abstract interface";
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
/*     */   private AbstractType(ContextStack paramContextStack, ClassDefinition paramClassDefinition) {
/* 127 */     super(paramContextStack, paramClassDefinition, 167780352);
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
/*     */   private static boolean couldBeAbstract(ContextStack paramContextStack, ClassDefinition paramClassDefinition, boolean paramBoolean) {
/* 140 */     boolean bool = false;
/*     */
/* 142 */     if (paramClassDefinition.isInterface()) {
/* 143 */       BatchEnvironment batchEnvironment = paramContextStack.getEnv();
/*     */
/*     */       try {
/* 146 */         bool = !batchEnvironment.defRemote.implementedBy((Environment)batchEnvironment, paramClassDefinition.getClassDeclaration()) ? true : false;
/* 147 */         if (!bool) failedConstraint(15, paramBoolean, paramContextStack, paramClassDefinition.getName());
/* 148 */       } catch (ClassNotFound classNotFound) {
/* 149 */         classNotFound(paramContextStack, classNotFound);
/*     */       }
/*     */     } else {
/* 152 */       failedConstraint(14, paramBoolean, paramContextStack, paramClassDefinition.getName());
/*     */     }
/*     */
/*     */
/* 156 */     return bool;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean initialize(boolean paramBoolean, ContextStack paramContextStack) {
/* 165 */     boolean bool = false;
/* 166 */     ClassDefinition classDefinition = getClassDefinition();
/*     */
/*     */
/*     */
/*     */
/*     */     try {
/* 172 */       Vector<Method> vector = new Vector();
/*     */
/* 174 */       if (addAllMethods(classDefinition, vector, true, paramBoolean, paramContextStack) != null) {
/*     */
/*     */
/*     */
/* 178 */         boolean bool1 = true;
/*     */
/* 180 */         if (vector.size() > 0)
/*     */         {
/*     */
/*     */
/* 184 */           for (byte b = 0; b < vector.size(); b++) {
/*     */
/* 186 */             if (!isConformingRemoteMethod(vector.elementAt(b), true)) {
/* 187 */               bool1 = false;
/*     */             }
/*     */           }
/*     */         }
/*     */
/* 192 */         if (bool1)
/*     */         {
/*     */
/*     */
/* 196 */           bool = initialize((Vector)null, vector, (Vector)null, paramContextStack, paramBoolean);
/*     */         }
/*     */       }
/* 199 */     } catch (ClassNotFound classNotFound) {
/* 200 */       classNotFound(paramContextStack, classNotFound);
/*     */     }
/*     */
/* 203 */     return bool;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\AbstractType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
