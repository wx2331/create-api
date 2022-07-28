/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Compile;
/*     */ import com.sun.tools.corba.se.idl.Factories;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.InvalidArgument;
/*     */ import com.sun.tools.corba.se.idl.ModuleEntry;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.StructEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabFactory;
/*     */ import com.sun.tools.corba.se.idl.UnionBranch;
/*     */ import com.sun.tools.corba.se.idl.UnionEntry;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Compile
/*     */   extends Compile
/*     */ {
/*     */   public Factories _factories;
/*     */   ModuleEntry org;
/*     */   ModuleEntry omg;
/*     */   ModuleEntry corba;
/*     */   InterfaceEntry object;
/*     */   public Vector importTypes;
/*     */   public SymtabFactory factory;
/*     */   public static int typedefInfo;
/*     */   public Hashtable list;
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 145 */     compiler = new Compile();
/* 146 */     compiler.start(paramArrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(String[] paramArrayOfString) {
/*     */     try {
/* 158 */       Util.registerMessageFile("com/sun/tools/corba/se/idl/toJavaPortable/toJavaPortable.prp");
/* 159 */       init(paramArrayOfString);
/* 160 */       if (this.arguments.versionRequest) {
/* 161 */         displayVersion();
/*     */       } else {
/*     */         
/* 164 */         preParse();
/* 165 */         Enumeration enumeration = parse();
/* 166 */         if (enumeration != null)
/*     */         {
/* 168 */           preEmit(enumeration);
/* 169 */           generate();
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 176 */     catch (InvalidArgument invalidArgument) {
/*     */       
/* 178 */       System.err.println(invalidArgument);
/*     */     }
/* 180 */     catch (IOException iOException) {
/*     */       
/* 182 */       System.err.println(iOException);
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
/*     */   protected Compile() {
/* 204 */     this._factories = new Factories();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 425 */     this.importTypes = new Vector();
/*     */ 
/*     */     
/* 428 */     this.list = new Hashtable<>();
/* 429 */     this.factory = factories().symtabFactory(); } public static Compile compiler = null;
/*     */   
/*     */   protected Factories factories() {
/*     */     return this._factories;
/*     */   }
/*     */   
/*     */   protected void preParse() {
/*     */     Util.setSymbolTable(this.symbolTable);
/*     */     Util.setPackageTranslation(((Arguments)this.arguments).packageTranslation);
/*     */     this.org = this.factory.moduleEntry();
/*     */     this.org.emit(false);
/*     */     this.org.name("org");
/*     */     this.org.container(null);
/*     */     this.omg = this.factory.moduleEntry();
/*     */     this.omg.emit(false);
/*     */     this.omg.name("omg");
/*     */     this.omg.module("org");
/*     */     this.omg.container((SymtabEntry)this.org);
/*     */     this.org.addContained((SymtabEntry)this.omg);
/*     */     this.corba = this.factory.moduleEntry();
/*     */     this.corba.emit(false);
/*     */     this.corba.name("CORBA");
/*     */     this.corba.module("org/omg");
/*     */     this.corba.container((SymtabEntry)this.omg);
/*     */     this.omg.addContained((SymtabEntry)this.corba);
/*     */     this.symbolTable.put("org", this.org);
/*     */     this.symbolTable.put("org/omg", this.omg);
/*     */     this.symbolTable.put("org/omg/CORBA", this.corba);
/*     */     this.object = (InterfaceEntry)this.symbolTable.get("Object");
/*     */     this.object.module("org/omg/CORBA");
/*     */     this.object.container((SymtabEntry)this.corba);
/*     */     this.symbolTable.put("org/omg/CORBA/Object", this.object);
/*     */     PrimitiveEntry primitiveEntry = this.factory.primitiveEntry();
/*     */     primitiveEntry.name("TypeCode");
/*     */     primitiveEntry.module("org/omg/CORBA");
/*     */     primitiveEntry.container((SymtabEntry)this.corba);
/*     */     this.symbolTable.put("org/omg/CORBA/TypeCode", primitiveEntry);
/*     */     this.symbolTable.put("CORBA/TypeCode", primitiveEntry);
/*     */     this.overrideNames.put("CORBA/TypeCode", "org/omg/CORBA/TypeCode");
/*     */     this.overrideNames.put("org/omg/CORBA/TypeCode", "CORBA/TypeCode");
/*     */     primitiveEntry = this.factory.primitiveEntry();
/*     */     primitiveEntry.name("Principal");
/*     */     primitiveEntry.module("org/omg/CORBA");
/*     */     primitiveEntry.container((SymtabEntry)this.corba);
/*     */     this.symbolTable.put("org/omg/CORBA/Principle", primitiveEntry);
/*     */     this.symbolTable.put("CORBA/Principal", primitiveEntry);
/*     */     this.overrideNames.put("CORBA/Principal", "org/omg/CORBA/Principal");
/*     */     this.overrideNames.put("org/omg/CORBA/Principal", "CORBA/Principal");
/*     */     this.overrideNames.put("TRUE", "true");
/*     */     this.overrideNames.put("FALSE", "false");
/*     */     this.symbolTable.put("CORBA", this.corba);
/*     */     this.overrideNames.put("CORBA", "org/omg/CORBA");
/*     */     this.overrideNames.put("org/omg/CORBA", "CORBA");
/*     */   }
/*     */   
/*     */   protected void preEmit(Enumeration<SymtabEntry> paramEnumeration) {
/*     */     typedefInfo = SymtabEntry.getVariableKey();
/*     */     Hashtable hashtable = (Hashtable)this.symbolTable.clone();
/*     */     Enumeration<SymtabEntry> enumeration;
/*     */     for (enumeration = hashtable.elements(); enumeration.hasMoreElements(); ) {
/*     */       SymtabEntry symtabEntry = enumeration.nextElement();
/*     */       preEmitSTElement(symtabEntry);
/*     */     } 
/*     */     enumeration = this.symbolTable.elements();
/*     */     while (enumeration.hasMoreElements()) {
/*     */       SymtabEntry symtabEntry = enumeration.nextElement();
/*     */       if (symtabEntry instanceof com.sun.tools.corba.se.idl.TypedefEntry || symtabEntry instanceof com.sun.tools.corba.se.idl.SequenceEntry) {
/*     */         Util.fillInfo(symtabEntry);
/*     */       } else if (symtabEntry instanceof StructEntry) {
/*     */         Enumeration<SymtabEntry> enumeration1 = ((StructEntry)symtabEntry).members().elements();
/*     */         while (enumeration1.hasMoreElements())
/*     */           Util.fillInfo(enumeration1.nextElement()); 
/*     */       } else if (symtabEntry instanceof InterfaceEntry && ((InterfaceEntry)symtabEntry).state() != null) {
/*     */         Enumeration enumeration1 = ((InterfaceEntry)symtabEntry).state().elements();
/*     */         while (enumeration1.hasMoreElements())
/*     */           Util.fillInfo((SymtabEntry)((InterfaceState)enumeration1.nextElement()).entry); 
/*     */       } else if (symtabEntry instanceof UnionEntry) {
/*     */         Enumeration enumeration1 = ((UnionEntry)symtabEntry).branches().elements();
/*     */         while (enumeration1.hasMoreElements())
/*     */           Util.fillInfo((SymtabEntry)((UnionBranch)enumeration1.nextElement()).typedef); 
/*     */       } 
/*     */       if (symtabEntry.module().equals("") && !(symtabEntry instanceof ModuleEntry) && !(symtabEntry instanceof com.sun.tools.corba.se.idl.IncludeEntry) && !(symtabEntry instanceof PrimitiveEntry))
/*     */         this.importTypes.addElement(symtabEntry); 
/*     */     } 
/*     */     while (paramEnumeration.hasMoreElements()) {
/*     */       SymtabEntry symtabEntry = paramEnumeration.nextElement();
/*     */       preEmitELElement(symtabEntry);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void preEmitSTElement(SymtabEntry paramSymtabEntry) {
/*     */     Hashtable hashtable = ((Arguments)this.arguments).packages;
/*     */     if (hashtable.size() > 0) {
/*     */       String str = (String)hashtable.get(paramSymtabEntry.fullName());
/*     */       if (str != null) {
/*     */         String str1 = null;
/*     */         ModuleEntry moduleEntry1 = null;
/*     */         ModuleEntry moduleEntry2 = null;
/*     */         while (str != null) {
/*     */           int i = str.indexOf('.');
/*     */           if (i < 0) {
/*     */             str1 = str;
/*     */             str = null;
/*     */           } else {
/*     */             str1 = str.substring(0, i);
/*     */             str = str.substring(i + 1);
/*     */           } 
/*     */           String str2 = (moduleEntry2 == null) ? str1 : (moduleEntry2.fullName() + '/' + str1);
/*     */           moduleEntry1 = (ModuleEntry)this.symbolTable.get(str2);
/*     */           if (moduleEntry1 == null) {
/*     */             moduleEntry1 = this.factory.moduleEntry();
/*     */             moduleEntry1.name(str1);
/*     */             moduleEntry1.container((SymtabEntry)moduleEntry2);
/*     */             if (moduleEntry2 != null)
/*     */               moduleEntry1.module(moduleEntry2.fullName()); 
/*     */             this.symbolTable.put(str1, moduleEntry1);
/*     */           } 
/*     */           moduleEntry2 = moduleEntry1;
/*     */         } 
/*     */         paramSymtabEntry.module(moduleEntry1.fullName());
/*     */         paramSymtabEntry.container((SymtabEntry)moduleEntry1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void preEmitELElement(SymtabEntry paramSymtabEntry) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\Compile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */