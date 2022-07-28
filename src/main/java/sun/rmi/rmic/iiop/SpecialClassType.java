/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Identifier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpecialClassType
/*     */   extends ClassType
/*     */ {
/*     */   public static SpecialClassType forSpecial(ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/*  68 */     if (paramContextStack.anyErrors()) return null;
/*     */     
/*  70 */     Type type = paramClassDefinition.getType();
/*     */ 
/*     */ 
/*     */     
/*  74 */     String str = type.toString() + paramContextStack.getContextCodeString();
/*     */     
/*  76 */     Type type1 = getType(str, paramContextStack);
/*     */     
/*  78 */     if (type1 != null) {
/*     */       
/*  80 */       if (!(type1 instanceof SpecialClassType)) return null;
/*     */ 
/*     */ 
/*     */       
/*  84 */       return (SpecialClassType)type1;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  89 */     int i = getTypeCode(type, paramClassDefinition, paramContextStack);
/*     */     
/*  91 */     if (i != 0) {
/*     */ 
/*     */ 
/*     */       
/*  95 */       SpecialClassType specialClassType = new SpecialClassType(paramContextStack, i, paramClassDefinition);
/*  96 */       putType(str, specialClassType, paramContextStack);
/*  97 */       paramContextStack.push(specialClassType);
/*  98 */       paramContextStack.pop(true);
/*  99 */       return specialClassType;
/*     */     } 
/*     */ 
/*     */     
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeDescription() {
/* 111 */     return "Special class";
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
/*     */   private SpecialClassType(ContextStack paramContextStack, int paramInt, ClassDefinition paramClassDefinition) {
/* 123 */     super(paramContextStack, paramInt | 0x10000000 | 0x4000000 | 0x2000000, paramClassDefinition);
/* 124 */     Identifier identifier = paramClassDefinition.getName();
/* 125 */     String str = null;
/* 126 */     String[] arrayOfString = null;
/* 127 */     boolean bool = (paramContextStack.size() > 0 && paramContextStack.getContext().isConstant()) ? true : false;
/*     */ 
/*     */ 
/*     */     
/* 131 */     switch (paramInt) {
/*     */       case 512:
/* 133 */         str = IDLNames.getTypeName(paramInt, bool);
/* 134 */         if (!bool) {
/* 135 */           arrayOfString = IDL_CORBA_MODULE;
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1024:
/* 141 */         str = "_Object";
/* 142 */         arrayOfString = IDL_JAVA_LANG_MODULE;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 147 */     setNames(identifier, arrayOfString, str);
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (!initParents(paramContextStack))
/*     */     {
/*     */ 
/*     */       
/* 155 */       throw new CompilerError("SpecialClassType found invalid parent.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 160 */     initialize((Vector)null, (Vector)null, (Vector)null, paramContextStack, false);
/*     */   }
/*     */   
/*     */   private static int getTypeCode(Type paramType, ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/* 164 */     if (paramType.isType(10)) {
/* 165 */       Identifier identifier = paramType.getClassName();
/* 166 */       if (identifier == idJavaLangString) return 512; 
/* 167 */       if (identifier == idJavaLangObject) return 1024; 
/*     */     } 
/* 169 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\SpecialClassType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */