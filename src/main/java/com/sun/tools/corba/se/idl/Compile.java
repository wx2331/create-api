/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.constExpr.DefaultExprFactory;
/*     */ import com.sun.tools.corba.se.idl.constExpr.ExprFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Stack;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ {
/*     */   public Arguments arguments;
/*     */   protected Hashtable overrideNames;
/*     */   protected Hashtable symbolTable;
/*     */   protected Vector includes;
/*     */   protected Vector includeEntries;
/*     */   
/*     */   public Compile() {
/* 482 */     this.arguments = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 490 */     this.overrideNames = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 496 */     this.symbolTable = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 503 */     this.includes = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 510 */     this.includeEntries = new Vector();
/*     */     
/* 512 */     this.genFactory = null;
/* 513 */     this.symtabFactory = null;
/* 514 */     this.exprFactory = null;
/* 515 */     this.parser = null;
/* 516 */     this.preprocessor = new Preprocessor();
/* 517 */     this.noPragma = new NoPragma();
/* 518 */     this.emitList = null;
/* 519 */     this.keywords = null;
/*     */     this.noPragma.init(this.preprocessor);
/*     */     this.preprocessor.registerPragma(this.noPragma);
/*     */     ParseException.detected = false;
/*     */     SymtabEntry.includeStack = new Stack();
/*     */     SymtabEntry.setEmit = true;
/*     */     Parser.repIDStack = new Stack();
/*     */   }
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     (new Compile()).start(paramArrayOfString);
/*     */   }
/*     */   
/*     */   protected Factories factories() {
/*     */     return new Factories();
/*     */   }
/*     */   
/*     */   protected void registerPragma(PragmaHandler paramPragmaHandler) {
/*     */     paramPragmaHandler.init(this.preprocessor);
/*     */     this.preprocessor.registerPragma(paramPragmaHandler);
/*     */   }
/*     */   
/*     */   protected void init(String[] paramArrayOfString) throws InvalidArgument {
/*     */     initFactories();
/*     */     this.arguments.parseArgs(paramArrayOfString);
/*     */     initGenerators();
/*     */     this.parser = new Parser(this.preprocessor, this.arguments, this.overrideNames, this.symbolTable, this.symtabFactory, this.exprFactory, this.keywords);
/*     */     this.preprocessor.init(this.parser);
/*     */     this.parser.includes = this.includes;
/*     */     this.parser.includeEntries = this.includeEntries;
/*     */   }
/*     */   
/*     */   protected Enumeration parse() throws IOException {
/*     */     if (this.arguments.verbose)
/*     */       System.out.println(Util.getMessage("Compile.parsing", this.arguments.file)); 
/*     */     this.parser.parse(this.arguments.file);
/*     */     if (!ParseException.detected)
/*     */       this.parser.forwardEntryCheck(); 
/*     */     if (this.arguments.verbose)
/*     */       System.out.println(Util.getMessage("Compile.parseDone", this.arguments.file)); 
/*     */     if (ParseException.detected) {
/*     */       this.symbolTable = null;
/*     */       this.emitList = null;
/*     */     } else {
/*     */       this.symbolTable = Parser.symbolTable;
/*     */       this.emitList = this.parser.emitList.elements();
/*     */     } 
/*     */     return this.emitList;
/*     */   }
/*     */   
/*     */   protected void generate() throws IOException {
/*     */     if (ParseException.detected) {
/*     */       this.emitList = null;
/*     */     } else {
/*     */       this.emitList = this.parser.emitList.elements();
/*     */     } 
/*     */     if (this.emitList != null) {
/*     */       if (this.arguments.verbose)
/*     */         System.out.println(); 
/*     */       while (this.emitList.hasMoreElements()) {
/*     */         SymtabEntry symtabEntry = this.emitList.nextElement();
/*     */         if (this.arguments.verbose && !(symtabEntry.generator() instanceof Noop))
/*     */           if (symtabEntry.module().equals("")) {
/*     */             System.out.println(Util.getMessage("Compile.generating", symtabEntry.name()));
/*     */           } else {
/*     */             System.out.println(Util.getMessage("Compile.generating", symtabEntry.module() + '/' + symtabEntry.name()));
/*     */           }  
/*     */         symtabEntry.generate(this.symbolTable, null);
/*     */         if (!this.arguments.verbose || symtabEntry.generator() instanceof Noop)
/*     */           continue; 
/*     */         if (symtabEntry.module().equals("")) {
/*     */           System.out.println(Util.getMessage("Compile.genDone", symtabEntry.name()));
/*     */           continue;
/*     */         } 
/*     */         System.out.println(Util.getMessage("Compile.genDone", symtabEntry.module() + '/' + symtabEntry.name()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start(String[] paramArrayOfString) {
/*     */     try {
/*     */       init(paramArrayOfString);
/*     */       if (this.arguments.versionRequest) {
/*     */         displayVersion();
/*     */       } else {
/*     */         parse();
/*     */         generate();
/*     */       } 
/*     */     } catch (InvalidArgument invalidArgument) {
/*     */       System.err.println(invalidArgument);
/*     */     } catch (IOException iOException) {
/*     */       System.err.println(iOException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initFactories() {
/*     */     Factories factories = factories();
/*     */     if (factories == null)
/*     */       factories = new Factories(); 
/*     */     Arguments arguments = factories.arguments();
/*     */     if (arguments == null) {
/*     */       this.arguments = new Arguments();
/*     */     } else {
/*     */       this.arguments = arguments;
/*     */     } 
/*     */     SymtabFactory symtabFactory = factories.symtabFactory();
/*     */     if (symtabFactory == null) {
/*     */       this.symtabFactory = new DefaultSymtabFactory();
/*     */     } else {
/*     */       this.symtabFactory = symtabFactory;
/*     */     } 
/*     */     ExprFactory exprFactory = factories.exprFactory();
/*     */     if (exprFactory == null) {
/*     */       this.exprFactory = (ExprFactory)new DefaultExprFactory();
/*     */     } else {
/*     */       this.exprFactory = exprFactory;
/*     */     } 
/*     */     GenFactory genFactory = factories.genFactory();
/*     */     if (genFactory == null) {
/*     */       this.genFactory = noop;
/*     */     } else {
/*     */       this.genFactory = genFactory;
/*     */     } 
/*     */     this.keywords = factories.languageKeywords();
/*     */     if (this.keywords == null)
/*     */       this.keywords = new String[0]; 
/*     */   }
/*     */   
/*     */   private void initGenerators() {
/*     */     AttributeGen attributeGen = this.genFactory.createAttributeGen();
/*     */     AttributeEntry.attributeGen = (attributeGen == null) ? noop : attributeGen;
/*     */     ConstGen constGen = this.genFactory.createConstGen();
/*     */     ConstEntry.constGen = (constGen == null) ? noop : constGen;
/*     */     EnumGen enumGen = this.genFactory.createEnumGen();
/*     */     EnumEntry.enumGen = (enumGen == null) ? noop : enumGen;
/*     */     ExceptionGen exceptionGen = this.genFactory.createExceptionGen();
/*     */     ExceptionEntry.exceptionGen = (exceptionGen == null) ? noop : exceptionGen;
/*     */     ForwardGen forwardGen = this.genFactory.createForwardGen();
/*     */     ForwardEntry.forwardGen = (forwardGen == null) ? noop : forwardGen;
/*     */     ForwardValueGen forwardValueGen = this.genFactory.createForwardValueGen();
/*     */     ForwardValueEntry.forwardValueGen = (forwardValueGen == null) ? noop : forwardValueGen;
/*     */     IncludeGen includeGen = this.genFactory.createIncludeGen();
/*     */     IncludeEntry.includeGen = (includeGen == null) ? noop : includeGen;
/*     */     InterfaceGen interfaceGen = this.genFactory.createInterfaceGen();
/*     */     InterfaceEntry.interfaceGen = (interfaceGen == null) ? noop : interfaceGen;
/*     */     ValueGen valueGen = this.genFactory.createValueGen();
/*     */     ValueEntry.valueGen = (valueGen == null) ? noop : valueGen;
/*     */     ValueBoxGen valueBoxGen = this.genFactory.createValueBoxGen();
/*     */     ValueBoxEntry.valueBoxGen = (valueBoxGen == null) ? noop : valueBoxGen;
/*     */     MethodGen methodGen = this.genFactory.createMethodGen();
/*     */     MethodEntry.methodGen = (methodGen == null) ? noop : methodGen;
/*     */     ModuleGen moduleGen = this.genFactory.createModuleGen();
/*     */     ModuleEntry.moduleGen = (moduleGen == null) ? noop : moduleGen;
/*     */     NativeGen nativeGen = this.genFactory.createNativeGen();
/*     */     NativeEntry.nativeGen = (nativeGen == null) ? noop : nativeGen;
/*     */     ParameterGen parameterGen = this.genFactory.createParameterGen();
/*     */     ParameterEntry.parameterGen = (parameterGen == null) ? noop : parameterGen;
/*     */     PragmaGen pragmaGen = this.genFactory.createPragmaGen();
/*     */     PragmaEntry.pragmaGen = (pragmaGen == null) ? noop : pragmaGen;
/*     */     PrimitiveGen primitiveGen = this.genFactory.createPrimitiveGen();
/*     */     PrimitiveEntry.primitiveGen = (primitiveGen == null) ? noop : primitiveGen;
/*     */     SequenceGen sequenceGen = this.genFactory.createSequenceGen();
/*     */     SequenceEntry.sequenceGen = (sequenceGen == null) ? noop : sequenceGen;
/*     */     StringGen stringGen = this.genFactory.createStringGen();
/*     */     StringEntry.stringGen = (stringGen == null) ? noop : stringGen;
/*     */     StructGen structGen = this.genFactory.createStructGen();
/*     */     StructEntry.structGen = (structGen == null) ? noop : structGen;
/*     */     TypedefGen typedefGen = this.genFactory.createTypedefGen();
/*     */     TypedefEntry.typedefGen = (typedefGen == null) ? noop : typedefGen;
/*     */     UnionGen unionGen = this.genFactory.createUnionGen();
/*     */     UnionEntry.unionGen = (unionGen == null) ? noop : unionGen;
/*     */   }
/*     */   
/*     */   protected void displayVersion() {
/*     */     String str = Util.getMessage("Version.product", Util.getMessage("Version.number"));
/*     */     System.out.println(str);
/*     */   }
/*     */   
/*     */   static Noop noop = new Noop();
/*     */   private GenFactory genFactory;
/*     */   private SymtabFactory symtabFactory;
/*     */   private ExprFactory exprFactory;
/*     */   private Parser parser;
/*     */   Preprocessor preprocessor;
/*     */   private NoPragma noPragma;
/*     */   private Enumeration emitList;
/*     */   private String[] keywords;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\Compile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */