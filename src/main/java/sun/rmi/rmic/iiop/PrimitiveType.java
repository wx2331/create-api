/*     */ package sun.rmi.rmic.iiop;
/*     */ 
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
/*     */ public class PrimitiveType
/*     */   extends Type
/*     */ {
/*     */   public static PrimitiveType forPrimitive(Type paramType, ContextStack paramContextStack) {
/*     */     char c;
/*     */     PrimitiveType primitiveType;
/*  63 */     if (paramContextStack.anyErrors()) return null;
/*     */ 
/*     */ 
/*     */     
/*  67 */     Type type = getType(paramType, paramContextStack);
/*     */     
/*  69 */     if (type != null) {
/*     */       
/*  71 */       if (!(type instanceof PrimitiveType)) return null;
/*     */ 
/*     */ 
/*     */       
/*  75 */       return (PrimitiveType)type;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  80 */     switch (paramType.getTypeCode()) { case 11:
/*  81 */         c = '\001';
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  93 */         primitiveType = new PrimitiveType(paramContextStack, c);
/*     */ 
/*     */ 
/*     */         
/*  97 */         putType(paramType, primitiveType, paramContextStack);
/*     */ 
/*     */ 
/*     */         
/* 101 */         paramContextStack.push(primitiveType);
/* 102 */         paramContextStack.pop(true);
/*     */         
/* 104 */         return primitiveType;case 0: c = '\002'; primitiveType = new PrimitiveType(paramContextStack, c); putType(paramType, primitiveType, paramContextStack); paramContextStack.push(primitiveType); paramContextStack.pop(true); return primitiveType;case 1: c = '\004'; primitiveType = new PrimitiveType(paramContextStack, c); putType(paramType, primitiveType, paramContextStack); paramContextStack.push(primitiveType); paramContextStack.pop(true); return primitiveType;case 2: c = '\b'; primitiveType = new PrimitiveType(paramContextStack, c); putType(paramType, primitiveType, paramContextStack); paramContextStack.push(primitiveType); paramContextStack.pop(true); return primitiveType;case 3: c = '\020'; primitiveType = new PrimitiveType(paramContextStack, c); putType(paramType, primitiveType, paramContextStack); paramContextStack.push(primitiveType); paramContextStack.pop(true); return primitiveType;case 4: c = ' '; primitiveType = new PrimitiveType(paramContextStack, c); putType(paramType, primitiveType, paramContextStack); paramContextStack.push(primitiveType); paramContextStack.pop(true); return primitiveType;case 5: c = '@'; primitiveType = new PrimitiveType(paramContextStack, c); putType(paramType, primitiveType, paramContextStack); paramContextStack.push(primitiveType); paramContextStack.pop(true); return primitiveType;case 6: c = ''; primitiveType = new PrimitiveType(paramContextStack, c); putType(paramType, primitiveType, paramContextStack); paramContextStack.push(primitiveType); paramContextStack.pop(true); return primitiveType;case 7: c = 'Ā'; primitiveType = new PrimitiveType(paramContextStack, c); putType(paramType, primitiveType, paramContextStack); paramContextStack.push(primitiveType); paramContextStack.pop(true); return primitiveType; }
/*     */     
/*     */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/* 112 */     switch (getTypeCode()) { case 1:
/* 113 */         return "V";
/* 114 */       case 2: return "Z";
/* 115 */       case 4: return "B";
/* 116 */       case 8: return "C";
/* 117 */       case 16: return "S";
/* 118 */       case 32: return "I";
/* 119 */       case 64: return "J";
/* 120 */       case 128: return "F";
/* 121 */       case 256: return "D"; }
/* 122 */      return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeDescription() {
/* 130 */     return "Primitive";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getQualifiedIDLName(boolean paramBoolean) {
/* 140 */     return super.getQualifiedIDLName(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class loadClass() {
/* 151 */     switch (getTypeCode()) { case 1:
/* 152 */         return Null.class;
/* 153 */       case 2: return boolean.class;
/* 154 */       case 4: return byte.class;
/* 155 */       case 8: return char.class;
/* 156 */       case 16: return short.class;
/* 157 */       case 32: return int.class;
/* 158 */       case 64: return long.class;
/* 159 */       case 128: return float.class;
/* 160 */       case 256: return double.class; }
/* 161 */      throw new CompilerError("Not a primitive type");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PrimitiveType(ContextStack paramContextStack, int paramInt) {
/* 170 */     super(paramContextStack, paramInt | 0x1000000);
/*     */ 
/*     */ 
/*     */     
/* 174 */     String str = IDLNames.getTypeName(paramInt, false);
/* 175 */     Identifier identifier = null;
/*     */     
/* 177 */     switch (paramInt) { case 1:
/* 178 */         identifier = idVoid; break;
/* 179 */       case 2: identifier = idBoolean; break;
/* 180 */       case 4: identifier = idByte; break;
/* 181 */       case 8: identifier = idChar; break;
/* 182 */       case 16: identifier = idShort; break;
/* 183 */       case 32: identifier = idInt; break;
/* 184 */       case 64: identifier = idLong; break;
/* 185 */       case 128: identifier = idFloat; break;
/* 186 */       case 256: identifier = idDouble; break;
/* 187 */       default: throw new CompilerError("Not a primitive type"); }
/*     */ 
/*     */     
/* 190 */     setNames(identifier, (String[])null, str);
/* 191 */     setRepositoryID();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\PrimitiveType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */