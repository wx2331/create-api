/*     */ package com.sun.tools.corba.se.idl.som.idlemit;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.ForwardEntry;
/*     */ import com.sun.tools.corba.se.idl.PragmaHandler;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.som.cff.Messages;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetaPragma
/*     */   extends PragmaHandler
/*     */ {
/*  65 */   public static int metaKey = SymtabEntry.getVariableKey();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean process(String paramString1, String paramString2) {
/*  75 */     if (!paramString1.equals("meta")) {
/*  76 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  81 */       SymtabEntry symtabEntry = scopedName();
/*  82 */       if (symtabEntry == null) {
/*     */         
/*  84 */         parseException(Messages.msg("idlemit.MetaPragma.scopedNameNotFound"));
/*  85 */         skipToEOL();
/*     */       } else {
/*     */         
/*  88 */         String str = currentToken() + getStringToEOL();
/*     */ 
/*     */         
/*  91 */         Vector vector = (Vector)symtabEntry.dynamicVariable(metaKey);
/*  92 */         if (vector == null) {
/*  93 */           vector = new Vector();
/*  94 */           symtabEntry.dynamicVariable(metaKey, vector);
/*     */         } 
/*  96 */         parseMsg(vector, str);
/*     */       } 
/*  98 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void processForward(ForwardEntry paramForwardEntry) {
/*     */     Vector<Object> vector;
/*     */     try {
/* 114 */       vector = (Vector)paramForwardEntry.dynamicVariable(metaKey);
/* 115 */     } catch (Exception exception) {
/* 116 */       vector = null;
/*     */     } 
/* 118 */     SymtabEntry symtabEntry = paramForwardEntry.type();
/* 119 */     if (vector != null && symtabEntry != null) {
/*     */       Vector vector1;
/*     */       try {
/* 122 */         vector1 = (Vector)symtabEntry.dynamicVariable(metaKey);
/* 123 */       } catch (Exception exception) {
/* 124 */         vector1 = null;
/*     */       } 
/*     */       
/* 127 */       if (vector1 == null) {
/*     */         
/*     */         try {
/* 130 */           symtabEntry.dynamicVariable(metaKey, vector);
/* 131 */         } catch (Exception exception) {}
/*     */       }
/* 133 */       else if (vector1 != vector) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 138 */         for (byte b = 0; b < vector.size(); b++) {
/*     */           try {
/* 140 */             Object object = vector.elementAt(b);
/* 141 */             vector1.addElement(object);
/* 142 */           } catch (Exception exception) {}
/*     */         } 
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   private static int initialState = 0;
/* 177 */   private static int commentState = 1;
/* 178 */   private static int textState = 2;
/* 179 */   private static int finalState = 3;
/*     */   
/*     */   private void parseMsg(Vector<String> paramVector, String paramString) {
/* 182 */     int i = initialState;
/* 183 */     String str = "";
/* 184 */     byte b = 0;
/* 185 */     while (i != finalState) {
/* 186 */       boolean bool1 = (b >= paramString.length()) ? true : false;
/* 187 */       char c = ' ';
/* 188 */       boolean bool2 = false;
/* 189 */       boolean bool3 = false;
/* 190 */       boolean bool4 = false;
/* 191 */       boolean bool5 = false;
/* 192 */       boolean bool6 = false;
/* 193 */       if (!bool1) {
/* 194 */         c = paramString.charAt(b);
/* 195 */         if (c == '/' && b + 1 < paramString.length())
/* 196 */         { if (paramString.charAt(b + 1) == '/')
/* 197 */           { bool3 = true;
/* 198 */             b++; }
/*     */           
/* 200 */           else if (paramString.charAt(b + 1) == '*')
/* 201 */           { bool2 = true;
/* 202 */             b++; }
/* 203 */           else { bool6 = true; }
/*     */            }
/* 205 */         else if (c == '*' && b + 1 < paramString.length())
/* 206 */         { if (paramString.charAt(b + 1) == '/')
/* 207 */           { bool5 = true;
/* 208 */             b++; }
/* 209 */           else { bool6 = true; }
/*     */            }
/* 211 */         else if (Character.isSpace(c) || c == ',' || c == ';')
/*     */         
/* 213 */         { bool4 = true; }
/* 214 */         else { bool6 = true; }
/*     */       
/*     */       } 
/* 217 */       if (i == initialState) {
/* 218 */         if (bool2) {
/* 219 */           i = commentState;
/*     */         }
/* 221 */         else if (bool3 || bool1) {
/* 222 */           i = finalState;
/*     */         }
/* 224 */         else if (bool6) {
/* 225 */           i = textState;
/* 226 */           str = str + c;
/*     */         }
/*     */       
/* 229 */       } else if (i == commentState) {
/* 230 */         if (bool1) {
/* 231 */           i = finalState;
/*     */         }
/* 233 */         else if (bool5) {
/* 234 */           i = initialState;
/*     */         }
/*     */       
/* 237 */       } else if (i == textState) {
/* 238 */         if (bool1 || bool5 || bool3 || bool2 || bool4) {
/*     */           
/* 240 */           if (!str.equals("")) {
/* 241 */             paramVector.addElement(str);
/*     */             
/* 243 */             str = "";
/*     */           } 
/* 245 */           if (bool1)
/* 246 */           { i = finalState; }
/* 247 */           else if (bool2)
/* 248 */           { i = commentState; }
/* 249 */           else { i = initialState; }
/*     */         
/* 251 */         } else if (bool6) {
/* 252 */           str = str + c;
/*     */         } 
/*     */       } 
/* 255 */       b++;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\som\idlemit\MetaPragma.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */