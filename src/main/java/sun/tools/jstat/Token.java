/*     */ package sun.tools.jstat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Token
/*     */ {
/*     */   public String sval;
/*     */   public double nval;
/*     */   public int ttype;
/*     */   
/*     */   public Token(int paramInt, String paramString, double paramDouble) {
/*  43 */     this.ttype = paramInt;
/*  44 */     this.sval = paramString;
/*  45 */     this.nval = paramDouble;
/*     */   }
/*     */   
/*     */   public Token(int paramInt, String paramString) {
/*  49 */     this(paramInt, paramString, 0.0D);
/*     */   }
/*     */   
/*     */   public Token(int paramInt) {
/*  53 */     this(paramInt, null, 0.0D);
/*     */   }
/*     */   
/*     */   public String toMessage() {
/*  57 */     switch (this.ttype) {
/*     */       case 10:
/*  59 */         return "\"EOL\"";
/*     */       case -1:
/*  61 */         return "\"EOF\"";
/*     */       case -2:
/*  63 */         return "NUMBER";
/*     */       case -3:
/*  65 */         if (this.sval == null) {
/*  66 */           return "IDENTIFIER";
/*     */         }
/*  68 */         return "IDENTIFIER " + this.sval;
/*     */     } 
/*     */     
/*  71 */     if (this.ttype == 34) {
/*  72 */       String str = "QUOTED STRING";
/*  73 */       if (this.sval != null)
/*  74 */         str = str + " \"" + this.sval + "\""; 
/*  75 */       return str;
/*     */     } 
/*  77 */     return "CHARACTER '" + (char)this.ttype + "'";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  83 */     StringBuilder stringBuilder = new StringBuilder();
/*  84 */     switch (this.ttype)
/*     */     { case 10:
/*  86 */         stringBuilder.append("ttype=TT_EOL");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 109 */         return stringBuilder.toString();case -1: stringBuilder.append("ttype=TT_EOF"); return stringBuilder.toString();case -2: stringBuilder.append("ttype=TT_NUM,").append("nval=" + this.nval); return stringBuilder.toString();case -3: if (this.sval == null) { stringBuilder.append("ttype=TT_WORD:IDENTIFIER"); } else { stringBuilder.append("ttype=TT_WORD:").append("sval=" + this.sval); }  return stringBuilder.toString(); }  if (this.ttype == 34) { stringBuilder.append("ttype=TT_STRING:").append("sval=" + this.sval); } else { stringBuilder.append("ttype=TT_CHAR:").append((char)this.ttype); }  return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Token.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */