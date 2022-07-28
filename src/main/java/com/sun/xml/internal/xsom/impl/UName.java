/*     */ package com.sun.xml.internal.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UName
/*     */ {
/*     */   private final String nsUri;
/*     */   private final String localName;
/*     */   private final String qname;
/*     */   
/*     */   public UName(String _nsUri, String _localName, String _qname) {
/*  43 */     if (_nsUri == null || _localName == null || _qname == null) {
/*  44 */       throw new NullPointerException(_nsUri + " " + _localName + " " + _qname);
/*     */     }
/*  46 */     this.nsUri = _nsUri.intern();
/*  47 */     this.localName = _localName.intern();
/*  48 */     this.qname = _qname.intern();
/*     */   }
/*     */   
/*     */   public UName(String nsUri, String localName) {
/*  52 */     this(nsUri, localName, localName);
/*     */   }
/*     */   
/*     */   public UName(XSDeclaration decl) {
/*  56 */     this(decl.getTargetNamespace(), decl.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  63 */     return this.localName;
/*  64 */   } public String getNamespaceURI() { return this.nsUri; } public String getQualifiedName() {
/*  65 */     return this.qname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  73 */     if (obj instanceof UName) {
/*  74 */       UName u = (UName)obj;
/*     */       
/*  76 */       return (getName().compareTo(u.getName()) == 0 && 
/*  77 */         getNamespaceURI().compareTo(u.getNamespaceURI()) == 0 && 
/*  78 */         getQualifiedName().compareTo(u.getQualifiedName()) == 0);
/*     */     } 
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  86 */     int hash = 7;
/*  87 */     hash = 13 * hash + ((this.nsUri != null) ? this.nsUri.hashCode() : 0);
/*  88 */     hash = 13 * hash + ((this.localName != null) ? this.localName.hashCode() : 0);
/*  89 */     hash = 13 * hash + ((this.qname != null) ? this.qname.hashCode() : 0);
/*  90 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public static final Comparator comparator = new Comparator() {
/*     */       public int compare(Object o1, Object o2) {
/*  98 */         UName lhs = (UName)o1;
/*  99 */         UName rhs = (UName)o2;
/* 100 */         int r = lhs.nsUri.compareTo(rhs.nsUri);
/* 101 */         if (r != 0) return r; 
/* 102 */         return lhs.localName.compareTo(rhs.localName);
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\UName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */