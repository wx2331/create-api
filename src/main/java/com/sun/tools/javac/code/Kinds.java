/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.source.tree.MemberReferenceTree;
/*     */ import com.sun.tools.javac.api.Formattable;
/*     */ import com.sun.tools.javac.api.Messages;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Kinds
/*     */ {
/*     */   public static final int NIL = 0;
/*     */   public static final int PCK = 1;
/*     */   public static final int TYP = 2;
/*     */   public static final int VAR = 4;
/*     */   public static final int VAL = 12;
/*     */   public static final int MTH = 16;
/*     */   public static final int POLY = 32;
/*     */   public static final int ERR = 63;
/*     */   public static final int AllKinds = 63;
/*     */   public static final int ERRONEOUS = 128;
/*     */   public static final int AMBIGUOUS = 129;
/*     */   public static final int HIDDEN = 130;
/*     */   public static final int STATICERR = 131;
/*     */   public static final int MISSING_ENCL = 132;
/*     */   public static final int ABSENT_VAR = 133;
/*     */   public static final int WRONG_MTHS = 134;
/*     */   public static final int WRONG_MTH = 135;
/*     */   public static final int ABSENT_MTH = 136;
/*     */   public static final int ABSENT_TYP = 137;
/*     */   public static final int WRONG_STATICNESS = 138;
/*     */   
/*     */   public enum KindName
/*     */     implements Formattable
/*     */   {
/* 103 */     ANNOTATION("kindname.annotation"),
/* 104 */     CONSTRUCTOR("kindname.constructor"),
/* 105 */     INTERFACE("kindname.interface"),
/* 106 */     ENUM("kindname.enum"),
/* 107 */     STATIC("kindname.static"),
/* 108 */     TYPEVAR("kindname.type.variable"),
/* 109 */     BOUND("kindname.type.variable.bound"),
/* 110 */     VAR("kindname.variable"),
/* 111 */     VAL("kindname.value"),
/* 112 */     METHOD("kindname.method"),
/* 113 */     CLASS("kindname.class"),
/* 114 */     STATIC_INIT("kindname.static.init"),
/* 115 */     INSTANCE_INIT("kindname.instance.init"),
/* 116 */     PACKAGE("kindname.package");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     KindName(String param1String1) {
/* 121 */       this.name = param1String1;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 125 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getKind() {
/* 129 */       return "Kindname";
/*     */     }
/*     */     
/*     */     public String toString(Locale param1Locale, Messages param1Messages) {
/* 133 */       String str = toString();
/* 134 */       return param1Messages.getLocalizedString(param1Locale, "compiler.misc." + str, new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static KindName kindName(int paramInt) {
/* 141 */     switch (paramInt) { case 1:
/* 142 */         return KindName.PACKAGE;
/* 143 */       case 2: return KindName.CLASS;
/* 144 */       case 4: return KindName.VAR;
/* 145 */       case 12: return KindName.VAL;
/* 146 */       case 16: return KindName.METHOD; }
/* 147 */      throw new AssertionError("Unexpected kind: " + paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public static KindName kindName(MemberReferenceTree.ReferenceMode paramReferenceMode) {
/* 152 */     switch (paramReferenceMode) { case PACKAGE:
/* 153 */         return KindName.METHOD;
/* 154 */       case ENUM: return KindName.CONSTRUCTOR; }
/* 155 */      throw new AssertionError("Unexpected mode: " + paramReferenceMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static KindName kindName(Symbol paramSymbol) {
/* 162 */     switch (paramSymbol.getKind()) {
/*     */       case PACKAGE:
/* 164 */         return KindName.PACKAGE;
/*     */       
/*     */       case ENUM:
/* 167 */         return KindName.ENUM;
/*     */       
/*     */       case ANNOTATION_TYPE:
/*     */       case CLASS:
/* 171 */         return KindName.CLASS;
/*     */       
/*     */       case INTERFACE:
/* 174 */         return KindName.INTERFACE;
/*     */       
/*     */       case TYPE_PARAMETER:
/* 177 */         return KindName.TYPEVAR;
/*     */       
/*     */       case ENUM_CONSTANT:
/*     */       case FIELD:
/*     */       case PARAMETER:
/*     */       case LOCAL_VARIABLE:
/*     */       case EXCEPTION_PARAMETER:
/*     */       case RESOURCE_VARIABLE:
/* 185 */         return KindName.VAR;
/*     */       
/*     */       case CONSTRUCTOR:
/* 188 */         return KindName.CONSTRUCTOR;
/*     */       
/*     */       case METHOD:
/* 191 */         return KindName.METHOD;
/*     */       case STATIC_INIT:
/* 193 */         return KindName.STATIC_INIT;
/*     */       case INSTANCE_INIT:
/* 195 */         return KindName.INSTANCE_INIT;
/*     */     } 
/*     */     
/* 198 */     if (paramSymbol.kind == 12)
/*     */     {
/*     */       
/* 201 */       return KindName.VAL;
/*     */     }
/* 203 */     throw new AssertionError("Unexpected kind: " + paramSymbol.getKind());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EnumSet<KindName> kindNames(int paramInt) {
/* 210 */     EnumSet<KindName> enumSet = EnumSet.noneOf(KindName.class);
/* 211 */     if ((paramInt & 0xC) != 0)
/* 212 */       enumSet.add(((paramInt & 0xC) == 4) ? KindName.VAR : KindName.VAL); 
/* 213 */     if ((paramInt & 0x10) != 0) enumSet.add(KindName.METHOD); 
/* 214 */     if ((paramInt & 0x2) != 0) enumSet.add(KindName.CLASS); 
/* 215 */     if ((paramInt & 0x1) != 0) enumSet.add(KindName.PACKAGE); 
/* 216 */     return enumSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static KindName typeKindName(Type paramType) {
/* 222 */     if (paramType.hasTag(TypeTag.TYPEVAR) || (paramType
/* 223 */       .hasTag(TypeTag.CLASS) && (paramType.tsym.flags() & 0x1000000L) != 0L))
/* 224 */       return KindName.BOUND; 
/* 225 */     if (paramType.hasTag(TypeTag.PACKAGE))
/* 226 */       return KindName.PACKAGE; 
/* 227 */     if ((paramType.tsym.flags_field & 0x2000L) != 0L)
/* 228 */       return KindName.ANNOTATION; 
/* 229 */     if ((paramType.tsym.flags_field & 0x200L) != 0L) {
/* 230 */       return KindName.INTERFACE;
/*     */     }
/* 232 */     return KindName.CLASS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static KindName absentKind(int paramInt) {
/* 239 */     switch (paramInt) {
/*     */       case 133:
/* 241 */         return KindName.VAR;
/*     */       case 134: case 135: case 136: case 138:
/* 243 */         return KindName.METHOD;
/*     */       case 137:
/* 245 */         return KindName.CLASS;
/*     */     } 
/* 247 */     throw new AssertionError("Unexpected kind: " + paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\Kinds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */