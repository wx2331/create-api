/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.HashSet;
/*     */ import java.util.Vector;
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
/*     */ public class ArrayType
/*     */   extends Type
/*     */ {
/*     */   private Type type;
/*     */   private int arrayDimension;
/*     */   private String brackets;
/*     */   private String bracketsSig;
/*     */   
/*     */   public static ArrayType forArray(Type paramType, ContextStack paramContextStack) {
/*  71 */     ArrayType arrayType = null;
/*  72 */     Type type = paramType;
/*     */     
/*  74 */     if (type.getTypeCode() == 9) {
/*     */ 
/*     */ 
/*     */       
/*  78 */       while (type.getTypeCode() == 9) {
/*  79 */         type = type.getElementType();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  84 */       Type type1 = getType(paramType, paramContextStack);
/*  85 */       if (type1 != null) {
/*     */         
/*  87 */         if (!(type1 instanceof ArrayType)) return null;
/*     */ 
/*     */ 
/*     */         
/*  91 */         return (ArrayType)type1;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  96 */       Type type2 = CompoundType.makeType(type, null, paramContextStack);
/*     */       
/*  98 */       if (type2 != null) {
/*     */ 
/*     */ 
/*     */         
/* 102 */         arrayType = new ArrayType(paramContextStack, type2, paramType.getArrayDimension());
/*     */ 
/*     */ 
/*     */         
/* 106 */         putType(paramType, arrayType, paramContextStack);
/*     */ 
/*     */ 
/*     */         
/* 110 */         paramContextStack.push(arrayType);
/* 111 */         paramContextStack.pop(true);
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     return arrayType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/* 123 */     return this.bracketsSig + this.type.getSignature();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getElementType() {
/* 130 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArrayDimension() {
/* 137 */     return this.arrayDimension;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getArrayBrackets() {
/* 144 */     return this.brackets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return getQualifiedName() + this.brackets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeDescription() {
/* 158 */     return "Array of " + this.type.getTypeDescription();
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
/*     */   public String getTypeName(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 171 */     if (paramBoolean2) {
/* 172 */       return super.getTypeName(paramBoolean1, paramBoolean2, paramBoolean3);
/*     */     }
/* 174 */     return super.getTypeName(paramBoolean1, paramBoolean2, paramBoolean3) + this.brackets;
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
/*     */   protected void swapInvalidTypes() {
/* 187 */     if (this.type.getStatus() != 1) {
/* 188 */       this.type = getValidType(this.type);
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
/*     */   protected boolean addTypes(int paramInt, HashSet paramHashSet, Vector paramVector) {
/* 202 */     boolean bool = super.addTypes(paramInt, paramHashSet, paramVector);
/*     */ 
/*     */ 
/*     */     
/* 206 */     if (bool)
/*     */     {
/*     */ 
/*     */       
/* 210 */       getElementType().addTypes(paramInt, paramHashSet, paramVector);
/*     */     }
/*     */     
/* 213 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayType(ContextStack paramContextStack, Type paramType, int paramInt) {
/* 221 */     super(paramContextStack, 262144);
/* 222 */     this.type = paramType;
/* 223 */     this.arrayDimension = paramInt;
/*     */ 
/*     */ 
/*     */     
/* 227 */     this.brackets = "";
/* 228 */     this.bracketsSig = "";
/* 229 */     for (byte b = 0; b < paramInt; b++) {
/* 230 */       this.brackets += "[]";
/* 231 */       this.bracketsSig += "[";
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 236 */     String str = IDLNames.getArrayName(paramType, paramInt);
/* 237 */     String[] arrayOfString = IDLNames.getArrayModuleNames(paramType);
/* 238 */     setNames(paramType.getIdentifier(), arrayOfString, str);
/*     */ 
/*     */ 
/*     */     
/* 242 */     setRepositoryID();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class loadClass() {
/* 250 */     Class<?> clazz1 = null;
/* 251 */     Class<?> clazz2 = this.type.getClassInstance();
/* 252 */     if (clazz2 != null) {
/* 253 */       clazz1 = Array.newInstance(clazz2, new int[this.arrayDimension]).getClass();
/*     */     }
/* 255 */     return clazz1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroy() {
/* 262 */     super.destroy();
/* 263 */     if (this.type != null) {
/* 264 */       this.type.destroy();
/* 265 */       this.type = null;
/*     */     } 
/* 267 */     this.brackets = null;
/* 268 */     this.bracketsSig = null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\ArrayType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */