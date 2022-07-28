/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ConstFold
/*     */ {
/*  45 */   protected static final Context.Key<ConstFold> constFoldKey = new Context.Key();
/*     */   
/*     */   private Symtab syms;
/*     */ 
/*     */   
/*     */   public static strictfp ConstFold instance(Context paramContext) {
/*  51 */     ConstFold constFold = (ConstFold)paramContext.get(constFoldKey);
/*  52 */     if (constFold == null)
/*  53 */       constFold = new ConstFold(paramContext); 
/*  54 */     return constFold;
/*     */   }
/*     */   
/*     */   private strictfp ConstFold(Context paramContext) {
/*  58 */     paramContext.put(constFoldKey, this);
/*     */     
/*  60 */     this.syms = Symtab.instance(paramContext);
/*     */   }
/*     */   
/*  63 */   static final Integer minusOne = Integer.valueOf(-1);
/*  64 */   static final Integer zero = Integer.valueOf(0);
/*  65 */   static final Integer one = Integer.valueOf(1);
/*     */ 
/*     */ 
/*     */   
/*     */   private static strictfp Integer b2i(boolean paramBoolean) {
/*  70 */     return paramBoolean ? one : zero;
/*     */   }
/*  72 */   private static strictfp int intValue(Object paramObject) { return ((Number)paramObject).intValue(); }
/*  73 */   private static strictfp long longValue(Object paramObject) { return ((Number)paramObject).longValue(); }
/*  74 */   private static strictfp float floatValue(Object paramObject) { return ((Number)paramObject).floatValue(); } private static strictfp double doubleValue(Object paramObject) {
/*  75 */     return ((Number)paramObject).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   strictfp Type fold(int paramInt, List<Type> paramList) {
/*  86 */     int i = paramList.length();
/*  87 */     if (i == 1)
/*  88 */       return fold1(paramInt, (Type)paramList.head); 
/*  89 */     if (i == 2) {
/*  90 */       return fold2(paramInt, (Type)paramList.head, (Type)paramList.tail.head);
/*     */     }
/*  92 */     throw new AssertionError();
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
/*     */   strictfp Type fold1(int paramInt, Type paramType) {
/*     */     try {
/* 105 */       Object object = paramType.constValue();
/* 106 */       switch (paramInt) {
/*     */         case 0:
/* 108 */           return paramType;
/*     */         case 116:
/* 110 */           return this.syms.intType.constType(Integer.valueOf(-intValue(object)));
/*     */         case 130:
/* 112 */           return this.syms.intType.constType(Integer.valueOf(intValue(object) ^ 0xFFFFFFFF));
/*     */         case 257:
/* 114 */           return this.syms.booleanType.constType(b2i((intValue(object) == 0)));
/*     */         case 153:
/* 116 */           return this.syms.booleanType.constType(b2i((intValue(object) == 0)));
/*     */         case 154:
/* 118 */           return this.syms.booleanType.constType(b2i((intValue(object) != 0)));
/*     */         case 155:
/* 120 */           return this.syms.booleanType.constType(b2i((intValue(object) < 0)));
/*     */         case 157:
/* 122 */           return this.syms.booleanType.constType(b2i((intValue(object) > 0)));
/*     */         case 158:
/* 124 */           return this.syms.booleanType.constType(b2i((intValue(object) <= 0)));
/*     */         case 156:
/* 126 */           return this.syms.booleanType.constType(b2i((intValue(object) >= 0)));
/*     */         
/*     */         case 117:
/* 129 */           return this.syms.longType.constType(new Long(-longValue(object)));
/*     */         case 131:
/* 131 */           return this.syms.longType.constType(new Long(longValue(object) ^ 0xFFFFFFFFFFFFFFFFL));
/*     */         
/*     */         case 118:
/* 134 */           return this.syms.floatType.constType(new Float(-floatValue(object)));
/*     */         
/*     */         case 119:
/* 137 */           return this.syms.doubleType.constType(new Double(-doubleValue(object)));
/*     */       } 
/*     */       
/* 140 */       return null;
/*     */     }
/* 142 */     catch (ArithmeticException arithmeticException) {
/* 143 */       return null;
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
/*     */   strictfp Type fold2(int paramInt, Type paramType1, Type paramType2) {
/*     */     try {
/* 157 */       if (paramInt > 511) {
/*     */ 
/*     */         
/* 160 */         Type type = fold2(paramInt >> 9, paramType1, paramType2);
/* 161 */         return (type.constValue() == null) ? type : 
/* 162 */           fold1(paramInt & 0x1FF, type);
/*     */       } 
/* 164 */       Object object1 = paramType1.constValue();
/* 165 */       Object object2 = paramType2.constValue();
/* 166 */       switch (paramInt) {
/*     */         case 96:
/* 168 */           return this.syms.intType.constType(Integer.valueOf(intValue(object1) + intValue(object2)));
/*     */         case 100:
/* 170 */           return this.syms.intType.constType(Integer.valueOf(intValue(object1) - intValue(object2)));
/*     */         case 104:
/* 172 */           return this.syms.intType.constType(Integer.valueOf(intValue(object1) * intValue(object2)));
/*     */         case 108:
/* 174 */           return this.syms.intType.constType(Integer.valueOf(intValue(object1) / intValue(object2)));
/*     */         case 112:
/* 176 */           return this.syms.intType.constType(Integer.valueOf(intValue(object1) % intValue(object2)));
/*     */         case 126:
/* 178 */           return (paramType1.hasTag(TypeTag.BOOLEAN) ? this.syms.booleanType : this.syms.intType)
/*     */             
/* 180 */             .constType(Integer.valueOf(intValue(object1) & intValue(object2)));
/*     */         case 258:
/* 182 */           return this.syms.booleanType.constType(b2i(((intValue(object1) & intValue(object2)) != 0)));
/*     */         case 128:
/* 184 */           return (paramType1.hasTag(TypeTag.BOOLEAN) ? this.syms.booleanType : this.syms.intType)
/*     */             
/* 186 */             .constType(Integer.valueOf(intValue(object1) | intValue(object2)));
/*     */         case 259:
/* 188 */           return this.syms.booleanType.constType(b2i(((intValue(object1) | intValue(object2)) != 0)));
/*     */         case 130:
/* 190 */           return (paramType1.hasTag(TypeTag.BOOLEAN) ? this.syms.booleanType : this.syms.intType)
/*     */             
/* 192 */             .constType(Integer.valueOf(intValue(object1) ^ intValue(object2)));
/*     */         case 120: case 270:
/* 194 */           return this.syms.intType.constType(Integer.valueOf(intValue(object1) << intValue(object2)));
/*     */         case 122: case 272:
/* 196 */           return this.syms.intType.constType(Integer.valueOf(intValue(object1) >> intValue(object2)));
/*     */         case 124: case 274:
/* 198 */           return this.syms.intType.constType(Integer.valueOf(intValue(object1) >>> intValue(object2)));
/*     */         case 159:
/* 200 */           return this.syms.booleanType.constType(
/* 201 */               b2i((intValue(object1) == intValue(object2))));
/*     */         case 160:
/* 203 */           return this.syms.booleanType.constType(
/* 204 */               b2i((intValue(object1) != intValue(object2))));
/*     */         case 161:
/* 206 */           return this.syms.booleanType.constType(
/* 207 */               b2i((intValue(object1) < intValue(object2))));
/*     */         case 163:
/* 209 */           return this.syms.booleanType.constType(
/* 210 */               b2i((intValue(object1) > intValue(object2))));
/*     */         case 164:
/* 212 */           return this.syms.booleanType.constType(
/* 213 */               b2i((intValue(object1) <= intValue(object2))));
/*     */         case 162:
/* 215 */           return this.syms.booleanType.constType(
/* 216 */               b2i((intValue(object1) >= intValue(object2))));
/*     */         
/*     */         case 97:
/* 219 */           return this.syms.longType.constType(new Long(
/* 220 */                 longValue(object1) + longValue(object2)));
/*     */         case 101:
/* 222 */           return this.syms.longType.constType(new Long(
/* 223 */                 longValue(object1) - longValue(object2)));
/*     */         case 105:
/* 225 */           return this.syms.longType.constType(new Long(
/* 226 */                 longValue(object1) * longValue(object2)));
/*     */         case 109:
/* 228 */           return this.syms.longType.constType(new Long(
/* 229 */                 longValue(object1) / longValue(object2)));
/*     */         case 113:
/* 231 */           return this.syms.longType.constType(new Long(
/* 232 */                 longValue(object1) % longValue(object2)));
/*     */         case 127:
/* 234 */           return this.syms.longType.constType(new Long(
/* 235 */                 longValue(object1) & longValue(object2)));
/*     */         case 129:
/* 237 */           return this.syms.longType.constType(new Long(
/* 238 */                 longValue(object1) | longValue(object2)));
/*     */         case 131:
/* 240 */           return this.syms.longType.constType(new Long(
/* 241 */                 longValue(object1) ^ longValue(object2)));
/*     */         case 121: case 271:
/* 243 */           return this.syms.longType.constType(new Long(
/* 244 */                 longValue(object1) << intValue(object2)));
/*     */         case 123: case 273:
/* 246 */           return this.syms.longType.constType(new Long(
/* 247 */                 longValue(object1) >> intValue(object2)));
/*     */         case 125:
/* 249 */           return this.syms.longType.constType(new Long(
/* 250 */                 longValue(object1) >>> intValue(object2)));
/*     */         case 148:
/* 252 */           if (longValue(object1) < longValue(object2))
/* 253 */             return this.syms.intType.constType(minusOne); 
/* 254 */           if (longValue(object1) > longValue(object2)) {
/* 255 */             return this.syms.intType.constType(one);
/*     */           }
/* 257 */           return this.syms.intType.constType(zero);
/*     */         case 98:
/* 259 */           return this.syms.floatType.constType(new Float(
/* 260 */                 floatValue(object1) + floatValue(object2)));
/*     */         case 102:
/* 262 */           return this.syms.floatType.constType(new Float(
/* 263 */                 floatValue(object1) - floatValue(object2)));
/*     */         case 106:
/* 265 */           return this.syms.floatType.constType(new Float(
/* 266 */                 floatValue(object1) * floatValue(object2)));
/*     */         case 110:
/* 268 */           return this.syms.floatType.constType(new Float(
/* 269 */                 floatValue(object1) / floatValue(object2)));
/*     */         case 114:
/* 271 */           return this.syms.floatType.constType(new Float(
/* 272 */                 floatValue(object1) % floatValue(object2)));
/*     */         case 149: case 150:
/* 274 */           if (floatValue(object1) < floatValue(object2))
/* 275 */             return this.syms.intType.constType(minusOne); 
/* 276 */           if (floatValue(object1) > floatValue(object2))
/* 277 */             return this.syms.intType.constType(one); 
/* 278 */           if (floatValue(object1) == floatValue(object2))
/* 279 */             return this.syms.intType.constType(zero); 
/* 280 */           if (paramInt == 150) {
/* 281 */             return this.syms.intType.constType(one);
/*     */           }
/* 283 */           return this.syms.intType.constType(minusOne);
/*     */         case 99:
/* 285 */           return this.syms.doubleType.constType(new Double(
/* 286 */                 doubleValue(object1) + doubleValue(object2)));
/*     */         case 103:
/* 288 */           return this.syms.doubleType.constType(new Double(
/* 289 */                 doubleValue(object1) - doubleValue(object2)));
/*     */         case 107:
/* 291 */           return this.syms.doubleType.constType(new Double(
/* 292 */                 doubleValue(object1) * doubleValue(object2)));
/*     */         case 111:
/* 294 */           return this.syms.doubleType.constType(new Double(
/* 295 */                 doubleValue(object1) / doubleValue(object2)));
/*     */         case 115:
/* 297 */           return this.syms.doubleType.constType(new Double(
/* 298 */                 doubleValue(object1) % doubleValue(object2)));
/*     */         case 151: case 152:
/* 300 */           if (doubleValue(object1) < doubleValue(object2))
/* 301 */             return this.syms.intType.constType(minusOne); 
/* 302 */           if (doubleValue(object1) > doubleValue(object2))
/* 303 */             return this.syms.intType.constType(one); 
/* 304 */           if (doubleValue(object1) == doubleValue(object2))
/* 305 */             return this.syms.intType.constType(zero); 
/* 306 */           if (paramInt == 152) {
/* 307 */             return this.syms.intType.constType(one);
/*     */           }
/* 309 */           return this.syms.intType.constType(minusOne);
/*     */         case 165:
/* 311 */           return this.syms.booleanType.constType(b2i(object1.equals(object2)));
/*     */         case 166:
/* 313 */           return this.syms.booleanType.constType(b2i(!object1.equals(object2)));
/*     */         case 256:
/* 315 */           return this.syms.stringType.constType(paramType1
/* 316 */               .stringValue() + paramType2.stringValue());
/*     */       } 
/* 318 */       return null;
/*     */     
/*     */     }
/* 321 */     catch (ArithmeticException arithmeticException) {
/* 322 */       return null;
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
/*     */   strictfp Type coerce(Type paramType1, Type paramType2) {
/* 334 */     if (paramType1.tsym.type == paramType2.tsym.type)
/* 335 */       return paramType1; 
/* 336 */     if (paramType1.isNumeric()) {
/* 337 */       Object object = paramType1.constValue();
/* 338 */       switch (paramType2.getTag()) {
/*     */         case BYTE:
/* 340 */           return this.syms.byteType.constType(Integer.valueOf(0 + (byte)intValue(object)));
/*     */         case CHAR:
/* 342 */           return this.syms.charType.constType(Integer.valueOf(0 + (char)intValue(object)));
/*     */         case SHORT:
/* 344 */           return this.syms.shortType.constType(Integer.valueOf(0 + (short)intValue(object)));
/*     */         case INT:
/* 346 */           return this.syms.intType.constType(Integer.valueOf(intValue(object)));
/*     */         case LONG:
/* 348 */           return this.syms.longType.constType(Long.valueOf(longValue(object)));
/*     */         case FLOAT:
/* 350 */           return this.syms.floatType.constType(Float.valueOf(floatValue(object)));
/*     */         case DOUBLE:
/* 352 */           return this.syms.doubleType.constType(Double.valueOf(doubleValue(object)));
/*     */       } 
/*     */     } 
/* 355 */     return paramType2;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\comp\ConstFold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */