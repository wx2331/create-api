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
/*     */ public class NCInterfaceType
/*     */   extends InterfaceType
/*     */ {
/*     */   public static NCInterfaceType forNCInterface(ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/*  61 */     if (paramContextStack.anyErrors()) return null;
/*     */     
/*  63 */     boolean bool = false;
/*     */ 
/*     */     
/*     */     try {
/*  67 */       Type type = paramClassDefinition.getType();
/*  68 */       Type type1 = getType(type, paramContextStack);
/*     */       
/*  70 */       if (type1 != null) {
/*     */         
/*  72 */         if (!(type1 instanceof NCInterfaceType)) return null;
/*     */ 
/*     */ 
/*     */         
/*  76 */         return (NCInterfaceType)type1;
/*     */       } 
/*     */       
/*  79 */       NCInterfaceType nCInterfaceType = new NCInterfaceType(paramContextStack, paramClassDefinition);
/*  80 */       putType(type, nCInterfaceType, paramContextStack);
/*  81 */       paramContextStack.push(nCInterfaceType);
/*  82 */       bool = true;
/*     */       
/*  84 */       if (nCInterfaceType.initialize(paramContextStack)) {
/*  85 */         paramContextStack.pop(true);
/*  86 */         return nCInterfaceType;
/*     */       } 
/*  88 */       removeType(type, paramContextStack);
/*  89 */       paramContextStack.pop(false);
/*  90 */       return null;
/*     */     }
/*  92 */     catch (CompilerError compilerError) {
/*  93 */       if (bool) paramContextStack.pop(false); 
/*  94 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeDescription() {
/* 102 */     return "Non-conforming interface";
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
/*     */   private NCInterfaceType(ContextStack paramContextStack, ClassDefinition paramClassDefinition) {
/* 114 */     super(paramContextStack, paramClassDefinition, 167788544);
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
/*     */   private boolean initialize(ContextStack paramContextStack) {
/* 126 */     if (paramContextStack.getEnv().getParseNonConforming()) {
/*     */       
/* 128 */       Vector vector1 = new Vector();
/* 129 */       Vector vector2 = new Vector();
/* 130 */       Vector vector3 = new Vector();
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 135 */         addNonRemoteInterfaces(vector1, paramContextStack);
/*     */ 
/*     */ 
/*     */         
/* 139 */         if (addAllMethods(getClassDefinition(), vector2, false, false, paramContextStack) != null)
/*     */         {
/*     */ 
/*     */           
/* 143 */           if (addConformingConstants(vector3, false, paramContextStack))
/*     */           {
/*     */ 
/*     */             
/* 147 */             if (!initialize(vector1, vector2, vector3, paramContextStack, false)) {
/* 148 */               return false;
/*     */             }
/*     */           }
/*     */         }
/* 152 */         return true;
/*     */       }
/* 154 */       catch (ClassNotFound classNotFound) {
/* 155 */         classNotFound(paramContextStack, classNotFound);
/*     */         
/* 157 */         return false;
/*     */       } 
/* 159 */     }  return initialize((Vector)null, (Vector)null, (Vector)null, paramContextStack, false);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\NCInterfaceType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */