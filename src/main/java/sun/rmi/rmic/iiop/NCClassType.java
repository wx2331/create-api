/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
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
/*     */ public class NCClassType
/*     */   extends ClassType
/*     */ {
/*     */   public static NCClassType forNCClass(ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/*  65 */     if (paramContextStack.anyErrors()) return null;
/*     */     
/*  67 */     boolean bool = false;
/*     */ 
/*     */     
/*     */     try {
/*  71 */       Type type = paramClassDefinition.getType();
/*  72 */       Type type1 = getType(type, paramContextStack);
/*     */       
/*  74 */       if (type1 != null) {
/*     */         
/*  76 */         if (!(type1 instanceof NCClassType)) return null;
/*     */ 
/*     */ 
/*     */         
/*  80 */         return (NCClassType)type1;
/*     */       } 
/*     */ 
/*     */       
/*  84 */       NCClassType nCClassType = new NCClassType(paramContextStack, paramClassDefinition);
/*  85 */       putType(type, nCClassType, paramContextStack);
/*  86 */       paramContextStack.push(nCClassType);
/*  87 */       bool = true;
/*     */       
/*  89 */       if (nCClassType.initialize(paramContextStack)) {
/*  90 */         paramContextStack.pop(true);
/*  91 */         return nCClassType;
/*     */       } 
/*  93 */       removeType(type, paramContextStack);
/*  94 */       paramContextStack.pop(false);
/*  95 */       return null;
/*     */     }
/*  97 */     catch (CompilerError compilerError) {
/*  98 */       if (bool) paramContextStack.pop(false); 
/*  99 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeDescription() {
/* 107 */     return addExceptionDescription("Non-conforming class");
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
/*     */   private NCClassType(ContextStack paramContextStack, ClassDefinition paramClassDefinition) {
/* 119 */     super(paramContextStack, paramClassDefinition, 100794368);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean initialize(ContextStack paramContextStack) {
/* 130 */     if (!initParents(paramContextStack)) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     if (paramContextStack.getEnv().getParseNonConforming()) {
/*     */       
/* 136 */       Vector vector1 = new Vector();
/* 137 */       Vector vector2 = new Vector();
/* 138 */       Vector vector3 = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 144 */         if (addAllMethods(getClassDefinition(), vector2, false, false, paramContextStack) != null)
/*     */         {
/*     */ 
/*     */           
/* 148 */           if (updateParentClassMethods(getClassDefinition(), vector2, false, paramContextStack) != null)
/*     */           {
/*     */ 
/*     */             
/* 152 */             if (addConformingConstants(vector3, false, paramContextStack))
/*     */             {
/*     */ 
/*     */               
/* 156 */               if (!initialize(vector1, vector2, vector3, paramContextStack, false)) {
/* 157 */                 return false;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 162 */         return true;
/*     */       }
/* 164 */       catch (ClassNotFound classNotFound) {
/* 165 */         classNotFound(paramContextStack, classNotFound);
/*     */         
/* 167 */         return false;
/*     */       } 
/* 169 */     }  return initialize((Vector)null, (Vector)null, (Vector)null, paramContextStack, false);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\NCClassType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */