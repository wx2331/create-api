/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDocComment
/*     */   extends JCommentPart
/*     */   implements JGenerable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  46 */   private final Map<String, JCommentPart> atParams = new HashMap<>();
/*     */ 
/*     */   
/*  49 */   private final Map<String, Map<String, String>> atXdoclets = new HashMap<>();
/*     */ 
/*     */   
/*  52 */   private final Map<JClass, JCommentPart> atThrows = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private JCommentPart atReturn = null;
/*     */ 
/*     */   
/*  60 */   private JCommentPart atDeprecated = null;
/*     */   
/*     */   private final JCodeModel owner;
/*     */   private static final String INDENT = " *     ";
/*     */   
/*     */   public JDocComment(JCodeModel owner) {
/*  66 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public JDocComment append(Object o) {
/*  70 */     add(o);
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addParam(String param) {
/*  78 */     JCommentPart p = this.atParams.get(param);
/*  79 */     if (p == null)
/*  80 */       this.atParams.put(param, p = new JCommentPart()); 
/*  81 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addParam(JVar param) {
/*  88 */     return addParam(param.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addThrows(Class<? extends Throwable> exception) {
/*  96 */     return addThrows(this.owner.ref(exception));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addThrows(JClass exception) {
/* 103 */     JCommentPart p = this.atThrows.get(exception);
/* 104 */     if (p == null)
/* 105 */       this.atThrows.put(exception, p = new JCommentPart()); 
/* 106 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addReturn() {
/* 113 */     if (this.atReturn == null)
/* 114 */       this.atReturn = new JCommentPart(); 
/* 115 */     return this.atReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JCommentPart addDeprecated() {
/* 122 */     if (this.atDeprecated == null)
/* 123 */       this.atDeprecated = new JCommentPart(); 
/* 124 */     return this.atDeprecated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> addXdoclet(String name) {
/* 131 */     Map<String, String> p = this.atXdoclets.get(name);
/* 132 */     if (p == null)
/* 133 */       this.atXdoclets.put(name, p = new HashMap<>()); 
/* 134 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> addXdoclet(String name, Map<String, String> attributes) {
/* 141 */     Map<String, String> p = this.atXdoclets.get(name);
/* 142 */     if (p == null)
/* 143 */       this.atXdoclets.put(name, p = new HashMap<>()); 
/* 144 */     p.putAll(attributes);
/* 145 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> addXdoclet(String name, String attribute, String value) {
/* 152 */     Map<String, String> p = this.atXdoclets.get(name);
/* 153 */     if (p == null)
/* 154 */       this.atXdoclets.put(name, p = new HashMap<>()); 
/* 155 */     p.put(attribute, value);
/* 156 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(JFormatter f) {
/* 163 */     f.p("/**").nl();
/*     */     
/* 165 */     format(f, " * ");
/*     */     
/* 167 */     f.p(" * ").nl();
/* 168 */     for (Map.Entry<String, JCommentPart> e : this.atParams.entrySet()) {
/* 169 */       f.p(" * @param ").p(e.getKey()).nl();
/* 170 */       ((JCommentPart)e.getValue()).format(f, " *     ");
/*     */     } 
/* 172 */     if (this.atReturn != null) {
/* 173 */       f.p(" * @return").nl();
/* 174 */       this.atReturn.format(f, " *     ");
/*     */     } 
/* 176 */     for (Map.Entry<JClass, JCommentPart> e : this.atThrows.entrySet()) {
/* 177 */       f.p(" * @throws ").t(e.getKey()).nl();
/* 178 */       ((JCommentPart)e.getValue()).format(f, " *     ");
/*     */     } 
/* 180 */     if (this.atDeprecated != null) {
/* 181 */       f.p(" * @deprecated").nl();
/* 182 */       this.atDeprecated.format(f, " *     ");
/*     */     } 
/* 184 */     for (Map.Entry<String, Map<String, String>> e : this.atXdoclets.entrySet()) {
/* 185 */       f.p(" * @").p(e.getKey());
/* 186 */       if (e.getValue() != null) {
/* 187 */         for (Map.Entry<String, String> a : (Iterable<Map.Entry<String, String>>)((Map)e.getValue()).entrySet()) {
/* 188 */           f.p(" ").p(a.getKey()).p("= \"").p(a.getValue()).p("\"");
/*     */         }
/*     */       }
/* 191 */       f.nl();
/*     */     } 
/* 193 */     f.p(" */").nl();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JDocComment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */