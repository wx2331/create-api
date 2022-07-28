/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
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
/*     */ public class ValueEntry
/*     */   extends InterfaceEntry
/*     */ {
/*     */   protected ValueEntry() {}
/*     */   
/*     */   protected ValueEntry(ValueEntry paramValueEntry) {
/*  59 */     super(paramValueEntry);
/*  60 */     this._supportsNames = (Vector)paramValueEntry._supportsNames.clone();
/*  61 */     this._supports = (Vector)paramValueEntry._supports.clone();
/*  62 */     this._initializers = (Vector)paramValueEntry._initializers.clone();
/*  63 */     this._custom = paramValueEntry._custom;
/*  64 */     this._isSafe = paramValueEntry._isSafe;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ValueEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  69 */     super(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  74 */     return new ValueEntry(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter) {
/*  85 */     valueGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/*  93 */     return valueGen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSupport(SymtabEntry paramSymtabEntry) {
/* 102 */     this._supports.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector supports() {
/* 108 */     return this._supports;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSupportName(String paramString) {
/* 114 */     this._supportsNames.addElement(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector supportsNames() {
/* 123 */     return this._supportsNames;
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
/*     */   void derivedFromAddElement(SymtabEntry paramSymtabEntry, boolean paramBoolean, Scanner paramScanner) {
/* 137 */     if (((InterfaceType)paramSymtabEntry).getInterfaceType() != 1) {
/* 138 */       if (isAbstract()) {
/* 139 */         ParseException.nonAbstractParent2(paramScanner, fullName(), paramSymtabEntry.fullName());
/* 140 */       } else if (derivedFrom().size() > 0) {
/* 141 */         ParseException.nonAbstractParent3(paramScanner, fullName(), paramSymtabEntry.fullName());
/*     */       } 
/*     */     }
/* 144 */     if (derivedFrom().contains(paramSymtabEntry)) {
/* 145 */       ParseException.alreadyDerived(paramScanner, paramSymtabEntry.fullName(), fullName());
/*     */     }
/* 147 */     if (paramBoolean) {
/* 148 */       this._isSafe = true;
/*     */     }
/* 150 */     addDerivedFrom(paramSymtabEntry);
/* 151 */     addDerivedFromName(paramSymtabEntry.fullName());
/* 152 */     addParentType(paramSymtabEntry, paramScanner);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void derivedFromAddElement(SymtabEntry paramSymtabEntry, Scanner paramScanner) {
/* 158 */     addSupport(paramSymtabEntry);
/* 159 */     addSupportName(paramSymtabEntry.fullName());
/* 160 */     addParentType(paramSymtabEntry, paramScanner);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean replaceForwardDecl(ForwardEntry paramForwardEntry, InterfaceEntry paramInterfaceEntry) {
/* 165 */     if (super.replaceForwardDecl(paramForwardEntry, paramInterfaceEntry))
/* 166 */       return true; 
/* 167 */     int i = this._supports.indexOf(paramForwardEntry);
/* 168 */     if (i >= 0)
/* 169 */       this._supports.setElementAt(paramInterfaceEntry, i); 
/* 170 */     return (i >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void initializersAddElement(MethodEntry paramMethodEntry, Scanner paramScanner) {
/* 176 */     Vector<ParameterEntry> vector = paramMethodEntry.parameters();
/* 177 */     int i = vector.size();
/* 178 */     for (Enumeration<MethodEntry> enumeration = this._initializers.elements(); enumeration.hasMoreElements(); ) {
/*     */       
/* 180 */       Vector<ParameterEntry> vector1 = ((MethodEntry)enumeration.nextElement()).parameters();
/* 181 */       if (i == vector1.size()) {
/*     */         
/* 183 */         byte b = 0;
/* 184 */         for (; b < i && (
/* 185 */           (ParameterEntry)vector.elementAt(b)).type().equals(((ParameterEntry)vector1
/* 186 */             .elementAt(b)).type()); b++);
/*     */         
/* 188 */         if (b >= i)
/* 189 */           ParseException.duplicateInit(paramScanner); 
/*     */       } 
/*     */     } 
/* 192 */     this._initializers.addElement(paramMethodEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector initializers() {
/* 197 */     return this._initializers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tagMethods() {
/* 205 */     for (Enumeration<MethodEntry> enumeration = methods().elements(); enumeration.hasMoreElements();) {
/* 206 */       ((MethodEntry)enumeration.nextElement()).valueMethod(true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCustom() {
/* 391 */     return this._custom;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCustom(boolean paramBoolean) {
/* 397 */     this._custom = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSafe() {
/* 404 */     return this._isSafe;
/*     */   }
/*     */   
/* 407 */   private Vector _supportsNames = new Vector();
/* 408 */   private Vector _supports = new Vector();
/* 409 */   private Vector _initializers = new Vector();
/*     */   private boolean _custom = false;
/*     */   private boolean _isSafe = false;
/*     */   static ValueGen valueGen;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\ValueEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */