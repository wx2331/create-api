/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpecialInterfaceType
/*     */   extends InterfaceType
/*     */ {
/*     */   public static SpecialInterfaceType forSpecial(ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/*  75 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*     */ 
/*     */     
/*  79 */     Type type = paramClassDefinition.getType();
/*  80 */     Type type1 = getType(type, paramContextStack);
/*     */     
/*  82 */     if (type1 != null) {
/*     */       
/*  84 */       if (!(type1 instanceof SpecialInterfaceType)) return null;
/*     */ 
/*     */ 
/*     */       
/*  88 */       return (SpecialInterfaceType)type1;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  93 */     if (isSpecial(type, paramClassDefinition, paramContextStack)) {
/*     */ 
/*     */ 
/*     */       
/*  97 */       SpecialInterfaceType specialInterfaceType = new SpecialInterfaceType(paramContextStack, 0, paramClassDefinition);
/*  98 */       putType(type, specialInterfaceType, paramContextStack);
/*  99 */       paramContextStack.push(specialInterfaceType);
/*     */       
/* 101 */       if (specialInterfaceType.initialize(type, paramContextStack)) {
/* 102 */         paramContextStack.pop(true);
/* 103 */         return specialInterfaceType;
/*     */       } 
/* 105 */       removeType(type, paramContextStack);
/* 106 */       paramContextStack.pop(false);
/* 107 */       return null;
/*     */     } 
/*     */     
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeDescription() {
/* 117 */     return "Special interface";
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
/*     */   private SpecialInterfaceType(ContextStack paramContextStack, int paramInt, ClassDefinition paramClassDefinition) {
/* 129 */     super(paramContextStack, paramInt | 0x20000000 | 0x8000000 | 0x2000000, paramClassDefinition);
/* 130 */     setNames(paramClassDefinition.getName(), (String[])null, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSpecial(Type paramType, ClassDefinition paramClassDefinition, ContextStack paramContextStack) {
/* 136 */     if (paramType.isType(10)) {
/* 137 */       Identifier identifier = paramType.getClassName();
/*     */       
/* 139 */       if (identifier.equals(idRemote)) return true; 
/* 140 */       if (identifier == idJavaIoSerializable) return true; 
/* 141 */       if (identifier == idJavaIoExternalizable) return true; 
/* 142 */       if (identifier == idCorbaObject) return true; 
/* 143 */       if (identifier == idIDLEntity) return true; 
/* 144 */       BatchEnvironment batchEnvironment = paramContextStack.getEnv();
/*     */       try {
/* 146 */         if (batchEnvironment.defCorbaObject.implementedBy((Environment)batchEnvironment, paramClassDefinition.getClassDeclaration())) return true; 
/* 147 */       } catch (ClassNotFound classNotFound) {
/* 148 */         classNotFound(paramContextStack, classNotFound);
/*     */       } 
/*     */     } 
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean initialize(Type paramType, ContextStack paramContextStack) {
/* 156 */     int i = 0;
/* 157 */     Identifier identifier = null;
/* 158 */     String str = null;
/* 159 */     String[] arrayOfString = null;
/* 160 */     boolean bool = (paramContextStack.size() > 0 && paramContextStack.getContext().isConstant()) ? true : false;
/*     */     
/* 162 */     if (paramType.isType(10)) {
/* 163 */       identifier = paramType.getClassName();
/*     */       
/* 165 */       if (identifier.equals(idRemote)) {
/* 166 */         i = 524288;
/* 167 */         str = "Remote";
/* 168 */         arrayOfString = IDL_JAVA_RMI_MODULE;
/* 169 */       } else if (identifier == idJavaIoSerializable) {
/* 170 */         i = 1024;
/* 171 */         str = "Serializable";
/* 172 */         arrayOfString = IDL_JAVA_IO_MODULE;
/* 173 */       } else if (identifier == idJavaIoExternalizable) {
/* 174 */         i = 1024;
/* 175 */         str = "Externalizable";
/* 176 */         arrayOfString = IDL_JAVA_IO_MODULE;
/* 177 */       } else if (identifier == idIDLEntity) {
/* 178 */         i = 1024;
/* 179 */         str = "IDLEntity";
/* 180 */         arrayOfString = IDL_ORG_OMG_CORBA_PORTABLE_MODULE;
/*     */       } else {
/*     */         
/* 183 */         i = 2048;
/*     */ 
/*     */ 
/*     */         
/* 187 */         if (identifier == idCorbaObject) {
/*     */ 
/*     */ 
/*     */           
/* 191 */           str = IDLNames.getTypeName(i, bool);
/* 192 */           arrayOfString = null;
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 202 */             str = IDLNames.getClassOrInterfaceName(identifier, this.env);
/* 203 */             arrayOfString = IDLNames.getModuleNames(identifier, isBoxed(), this.env);
/*     */           }
/* 205 */           catch (Exception exception) {
/* 206 */             failedConstraint(7, false, paramContextStack, identifier.toString(), exception.getMessage());
/* 207 */             throw new CompilerError("");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     if (i == 0) {
/* 214 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 219 */     setTypeCode(i | 0x20000000 | 0x8000000 | 0x2000000);
/*     */ 
/*     */ 
/*     */     
/* 223 */     if (str == null) {
/* 224 */       throw new CompilerError("Not a special type");
/*     */     }
/*     */     
/* 227 */     setNames(identifier, arrayOfString, str);
/*     */ 
/*     */ 
/*     */     
/* 231 */     return initialize((Vector)null, (Vector)null, (Vector)null, paramContextStack, false);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\SpecialInterfaceType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */