/*    */ package com.sun.tools.javap;
/*    */ 
/*    */ import com.sun.tools.classfile.AccessFlags;
/*    */ import java.util.EnumSet;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Options
/*    */ {
/*    */   public boolean help;
/*    */   public boolean verbose;
/*    */   public boolean version;
/*    */   public boolean fullVersion;
/*    */   public boolean showFlags;
/*    */   public boolean showLineAndLocalVariableTables;
/*    */   public int showAccess;
/*    */   public Set<String> accessOptions;
/*    */   public Set<InstructionDetailWriter.Kind> details;
/*    */   public boolean showDisassembled;
/*    */   public boolean showDescriptors;
/*    */   public boolean showAllAttrs;
/*    */   public boolean showConstants;
/*    */   public boolean sysInfo;
/*    */   public boolean showInnerClasses;
/*    */   public int indentWidth;
/*    */   public int tabColumn;
/*    */   
/*    */   public static Options instance(Context paramContext) {
/* 44 */     Options options = paramContext.<Options>get(Options.class);
/* 45 */     if (options == null)
/* 46 */       options = new Options(paramContext); 
/* 47 */     return options;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Options(Context paramContext) {
/* 81 */     this.accessOptions = new HashSet<>();
/* 82 */     this.details = EnumSet.noneOf(InstructionDetailWriter.Kind.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 89 */     this.indentWidth = 2;
/* 90 */     this.tabColumn = 40;
/*    */     paramContext.put(Options.class, this);
/*    */   }
/*    */   
/*    */   public boolean checkAccess(AccessFlags paramAccessFlags) {
/*    */     boolean bool1 = paramAccessFlags.is(1);
/*    */     boolean bool2 = paramAccessFlags.is(4);
/*    */     boolean bool3 = paramAccessFlags.is(2);
/*    */     boolean bool = (!bool1 && !bool2 && !bool3) ? true : false;
/*    */     if (this.showAccess == 1 && (bool2 || bool3 || bool))
/*    */       return false; 
/*    */     if (this.showAccess == 4 && (bool3 || bool))
/*    */       return false; 
/*    */     if (this.showAccess == 0 && bool3)
/*    */       return false; 
/*    */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\Options.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */