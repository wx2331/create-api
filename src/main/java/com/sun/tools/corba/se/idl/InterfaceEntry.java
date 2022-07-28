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
/*     */ public class InterfaceEntry
/*     */   extends SymtabEntry
/*     */   implements InterfaceType
/*     */ {
/*     */   protected InterfaceEntry() {}
/*     */   
/*     */   protected InterfaceEntry(InterfaceEntry paramInterfaceEntry) {
/*  58 */     super(paramInterfaceEntry);
/*  59 */     this._derivedFromNames = (Vector)paramInterfaceEntry._derivedFromNames.clone();
/*  60 */     this._derivedFrom = (Vector)paramInterfaceEntry._derivedFrom.clone();
/*  61 */     this._methods = (Vector)paramInterfaceEntry._methods.clone();
/*  62 */     this._allMethods = (Vector)paramInterfaceEntry._allMethods.clone();
/*  63 */     this.forwardedDerivers = (Vector)paramInterfaceEntry.forwardedDerivers.clone();
/*  64 */     this._contained = (Vector)paramInterfaceEntry._contained.clone();
/*  65 */     this._interfaceType = paramInterfaceEntry._interfaceType;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InterfaceEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  70 */     super(paramSymtabEntry, paramIDLID);
/*  71 */     if (module().equals("")) {
/*  72 */       module(name());
/*  73 */     } else if (!name().equals("")) {
/*  74 */       module(module() + "/" + name());
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  79 */     return (this._interfaceType == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocal() {
/*  84 */     return (this._interfaceType == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocalServant() {
/*  89 */     return (this._interfaceType == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocalSignature() {
/*  94 */     return (this._interfaceType == 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  99 */     return new InterfaceEntry(this);
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
/* 110 */     interfaceGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/* 118 */     return interfaceGen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDerivedFrom(SymtabEntry paramSymtabEntry) {
/* 127 */     this._derivedFrom.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector derivedFrom() {
/* 133 */     return this._derivedFrom;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDerivedFromName(String paramString) {
/* 139 */     this._derivedFromNames.addElement(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector derivedFromNames() {
/* 148 */     return this._derivedFromNames;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMethod(MethodEntry paramMethodEntry) {
/* 154 */     this._methods.addElement(paramMethodEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector methods() {
/* 161 */     return this._methods;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContained(SymtabEntry paramSymtabEntry) {
/* 167 */     this._contained.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector contained() {
/* 176 */     return this._contained;
/*     */   }
/*     */ 
/*     */   
/*     */   void methodsAddElement(MethodEntry paramMethodEntry, Scanner paramScanner) {
/* 181 */     if (verifyMethod(paramMethodEntry, paramScanner, false)) {
/*     */       
/* 183 */       addMethod(paramMethodEntry);
/* 184 */       this._allMethods.addElement(paramMethodEntry);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 189 */       addToForwardedAllMethods(paramMethodEntry, paramScanner);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void addToForwardedAllMethods(MethodEntry paramMethodEntry, Scanner paramScanner) {
/* 195 */     Enumeration<InterfaceEntry> enumeration = this.forwardedDerivers.elements();
/* 196 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 198 */       InterfaceEntry interfaceEntry = enumeration.nextElement();
/* 199 */       if (interfaceEntry.verifyMethod(paramMethodEntry, paramScanner, true)) {
/* 200 */         interfaceEntry._allMethods.addElement(paramMethodEntry);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean verifyMethod(MethodEntry paramMethodEntry, Scanner paramScanner, boolean paramBoolean) {
/* 208 */     boolean bool = true;
/* 209 */     String str = paramMethodEntry.name().toLowerCase();
/* 210 */     Enumeration<MethodEntry> enumeration = this._allMethods.elements();
/* 211 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 213 */       MethodEntry methodEntry = enumeration.nextElement();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 218 */       String str1 = methodEntry.name().toLowerCase();
/* 219 */       if (paramMethodEntry != methodEntry && str.equals(str1)) {
/*     */         
/* 221 */         if (paramBoolean) {
/* 222 */           ParseException.methodClash(paramScanner, fullName(), paramMethodEntry.name());
/*     */         } else {
/* 224 */           ParseException.alreadyDeclared(paramScanner, paramMethodEntry.name());
/* 225 */         }  bool = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 229 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   void derivedFromAddElement(SymtabEntry paramSymtabEntry, Scanner paramScanner) {
/* 234 */     addDerivedFrom(paramSymtabEntry);
/* 235 */     addDerivedFromName(paramSymtabEntry.fullName());
/* 236 */     addParentType(paramSymtabEntry, paramScanner);
/*     */   }
/*     */ 
/*     */   
/*     */   void addParentType(SymtabEntry paramSymtabEntry, Scanner paramScanner) {
/* 241 */     if (paramSymtabEntry instanceof ForwardEntry) {
/* 242 */       addToDerivers((ForwardEntry)paramSymtabEntry);
/*     */     } else {
/*     */       
/* 245 */       InterfaceEntry interfaceEntry = (InterfaceEntry)paramSymtabEntry;
/*     */ 
/*     */ 
/*     */       
/* 249 */       Enumeration<MethodEntry> enumeration = interfaceEntry._allMethods.elements();
/* 250 */       while (enumeration.hasMoreElements()) {
/*     */         
/* 252 */         MethodEntry methodEntry = enumeration.nextElement();
/* 253 */         if (verifyMethod(methodEntry, paramScanner, true)) {
/* 254 */           this._allMethods.addElement(methodEntry);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 259 */         addToForwardedAllMethods(methodEntry, paramScanner);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 266 */       lookForForwardEntrys(paramScanner, interfaceEntry);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void lookForForwardEntrys(Scanner paramScanner, InterfaceEntry paramInterfaceEntry) {
/* 272 */     Enumeration<SymtabEntry> enumeration = paramInterfaceEntry.derivedFrom().elements();
/* 273 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 275 */       SymtabEntry symtabEntry = enumeration.nextElement();
/* 276 */       if (symtabEntry instanceof ForwardEntry) {
/* 277 */         addToDerivers((ForwardEntry)symtabEntry); continue;
/* 278 */       }  if (symtabEntry == paramInterfaceEntry) {
/* 279 */         ParseException.selfInherit(paramScanner, paramInterfaceEntry.fullName()); continue;
/*     */       } 
/* 281 */       lookForForwardEntrys(paramScanner, (InterfaceEntry)symtabEntry);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean replaceForwardDecl(ForwardEntry paramForwardEntry, InterfaceEntry paramInterfaceEntry) {
/* 287 */     int i = this._derivedFrom.indexOf(paramForwardEntry);
/* 288 */     if (i >= 0)
/* 289 */       this._derivedFrom.setElementAt(paramInterfaceEntry, i); 
/* 290 */     return (i >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToDerivers(ForwardEntry paramForwardEntry) {
/* 298 */     paramForwardEntry.derivers.addElement(this);
/* 299 */     Enumeration<InterfaceEntry> enumeration = this.forwardedDerivers.elements();
/* 300 */     while (enumeration.hasMoreElements()) {
/* 301 */       paramForwardEntry.derivers.addElement(enumeration.nextElement());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector state() {
/* 310 */     return this._state;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initState() {
/* 315 */     this._state = new Vector();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addStateElement(InterfaceState paramInterfaceState, Scanner paramScanner) {
/* 320 */     if (this._state == null)
/* 321 */       this._state = new Vector(); 
/* 322 */     String str = paramInterfaceState.entry.name();
/* 323 */     for (Enumeration enumeration = this._state.elements(); enumeration.hasMoreElements();) {
/* 324 */       if (str.equals(((InterfaceState)enumeration.nextElement()).entry.name()))
/* 325 */         ParseException.duplicateState(paramScanner, str); 
/* 326 */     }  this._state.addElement(paramInterfaceState);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInterfaceType() {
/* 331 */     return this._interfaceType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInterfaceType(int paramInt) {
/* 336 */     this._interfaceType = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector allMethods() {
/* 342 */     return this._allMethods;
/*     */   }
/*     */   
/* 345 */   private Vector _derivedFromNames = new Vector();
/* 346 */   private Vector _derivedFrom = new Vector();
/* 347 */   private Vector _methods = new Vector();
/* 348 */   Vector _allMethods = new Vector();
/* 349 */   Vector forwardedDerivers = new Vector();
/* 350 */   private Vector _contained = new Vector();
/* 351 */   private Vector _state = null;
/* 352 */   private int _interfaceType = 0;
/*     */   static InterfaceGen interfaceGen;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\InterfaceEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */