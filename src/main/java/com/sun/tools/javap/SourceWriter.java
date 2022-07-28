/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.Attribute;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.LineNumberTable_attribute;
/*     */ import com.sun.tools.classfile.SourceFile_attribute;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SourceWriter
/*     */   extends InstructionDetailWriter
/*     */ {
/*     */   private JavaFileManager fileManager;
/*     */   private ClassFile classFile;
/*     */   private SortedMap<Integer, SortedSet<Integer>> lineMap;
/*     */   private List<Integer> lineList;
/*     */   private String[] sourceLines;
/*     */   
/*     */   static SourceWriter instance(Context paramContext) {
/*  62 */     SourceWriter sourceWriter = paramContext.<SourceWriter>get(SourceWriter.class);
/*  63 */     if (sourceWriter == null)
/*  64 */       sourceWriter = new SourceWriter(paramContext); 
/*  65 */     return sourceWriter;
/*     */   }
/*     */   
/*     */   protected SourceWriter(Context paramContext) {
/*  69 */     super(paramContext);
/*  70 */     paramContext.put(SourceWriter.class, this);
/*     */   }
/*     */   
/*     */   void setFileManager(JavaFileManager paramJavaFileManager) {
/*  74 */     this.fileManager = paramJavaFileManager;
/*     */   }
/*     */   
/*     */   public void reset(ClassFile paramClassFile, Code_attribute paramCode_attribute) {
/*  78 */     setSource(paramClassFile);
/*  79 */     setLineMap(paramCode_attribute);
/*     */   }
/*     */   
/*     */   public void writeDetails(Instruction paramInstruction) {
/*  83 */     String str = space(40);
/*  84 */     Set set = this.lineMap.get(Integer.valueOf(paramInstruction.getPC()));
/*  85 */     if (set != null) {
/*  86 */       for (Iterator<Integer> iterator = set.iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
/*  87 */         print(str);
/*  88 */         print(String.format(" %4d ", new Object[] { Integer.valueOf(i) }));
/*  89 */         if (i < this.sourceLines.length)
/*  90 */           print(this.sourceLines[i]); 
/*  91 */         println();
/*  92 */         int j = nextLine(i);
/*  93 */         for (int k = i + 1; k < j; k++) {
/*  94 */           print(str);
/*  95 */           print(String.format("(%4d)", new Object[] { Integer.valueOf(k) }));
/*  96 */           if (k < this.sourceLines.length)
/*  97 */             print(this.sourceLines[k]); 
/*  98 */           println();
/*     */         }  }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasSource() {
/* 105 */     return (this.sourceLines.length > 0);
/*     */   }
/*     */   
/*     */   private void setLineMap(Code_attribute paramCode_attribute) {
/* 109 */     TreeMap<Object, Object> treeMap = new TreeMap<>();
/*     */     
/* 111 */     TreeSet<Integer> treeSet = new TreeSet();
/* 112 */     for (Attribute attribute : paramCode_attribute.attributes) {
/* 113 */       if (attribute instanceof LineNumberTable_attribute) {
/* 114 */         LineNumberTable_attribute lineNumberTable_attribute = (LineNumberTable_attribute)attribute;
/* 115 */         for (LineNumberTable_attribute.Entry entry : lineNumberTable_attribute.line_number_table) {
/* 116 */           int i = entry.start_pc;
/* 117 */           int j = entry.line_number;
/* 118 */           SortedSet<Integer> sortedSet = (SortedSet)treeMap.get(Integer.valueOf(i));
/* 119 */           if (sortedSet == null) {
/* 120 */             sortedSet = new TreeSet();
/* 121 */             treeMap.put(Integer.valueOf(i), sortedSet);
/*     */           } 
/* 123 */           sortedSet.add(Integer.valueOf(j));
/* 124 */           treeSet.add(Integer.valueOf(j));
/*     */         } 
/*     */       } 
/*     */     } 
/* 128 */     this.lineMap = (SortedMap)treeMap;
/* 129 */     this.lineList = new ArrayList<>(treeSet);
/*     */   }
/*     */   
/*     */   private void setSource(ClassFile paramClassFile) {
/* 133 */     if (paramClassFile != this.classFile) {
/* 134 */       this.classFile = paramClassFile;
/* 135 */       this.sourceLines = splitLines(readSource(paramClassFile));
/*     */     } 
/*     */   }
/*     */   private String readSource(ClassFile paramClassFile) {
/*     */     StandardLocation standardLocation;
/* 140 */     if (this.fileManager == null) {
/* 141 */       return null;
/*     */     }
/*     */     
/* 144 */     if (this.fileManager.hasLocation(StandardLocation.SOURCE_PATH)) {
/* 145 */       standardLocation = StandardLocation.SOURCE_PATH;
/*     */     } else {
/* 147 */       standardLocation = StandardLocation.CLASS_PATH;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 154 */       String str1 = paramClassFile.getName();
/*     */       
/* 156 */       SourceFile_attribute sourceFile_attribute = (SourceFile_attribute)paramClassFile.attributes.get("SourceFile");
/* 157 */       if (sourceFile_attribute == null) {
/* 158 */         report(this.messages.getMessage("err.no.SourceFile.attribute", new Object[0]));
/* 159 */         return null;
/*     */       } 
/* 161 */       String str2 = sourceFile_attribute.getSourceFile(paramClassFile.constant_pool);
/*     */       
/* 163 */       String str3 = str2.endsWith(".java") ? str2.substring(0, str2.length() - 5) : str2;
/* 164 */       int i = str1.lastIndexOf("/");
/* 165 */       String str4 = (i == -1) ? "" : str1.substring(0, i + 1);
/* 166 */       String str5 = (str4 + str3).replace('/', '.');
/*     */       
/* 168 */       JavaFileObject javaFileObject = this.fileManager.getJavaFileForInput(standardLocation, str5, JavaFileObject.Kind.SOURCE);
/*     */ 
/*     */       
/* 171 */       if (javaFileObject == null) {
/* 172 */         report(this.messages.getMessage("err.source.file.not.found", new Object[0]));
/* 173 */         return null;
/*     */       } 
/* 175 */       return javaFileObject.getCharContent(true).toString();
/* 176 */     } catch (ConstantPoolException constantPoolException) {
/* 177 */       report(constantPoolException);
/* 178 */       return null;
/* 179 */     } catch (IOException iOException) {
/* 180 */       report(iOException.getLocalizedMessage());
/* 181 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String[] splitLines(String paramString) {
/* 186 */     if (paramString == null) {
/* 187 */       return new String[0];
/*     */     }
/* 189 */     ArrayList<String> arrayList = new ArrayList();
/* 190 */     arrayList.add("");
/*     */     try {
/* 192 */       BufferedReader bufferedReader = new BufferedReader(new StringReader(paramString));
/*     */       String str;
/* 194 */       while ((str = bufferedReader.readLine()) != null)
/* 195 */         arrayList.add(str); 
/* 196 */     } catch (IOException iOException) {}
/*     */     
/* 198 */     return arrayList.<String>toArray(new String[arrayList.size()]);
/*     */   }
/*     */   
/*     */   private int nextLine(int paramInt) {
/* 202 */     int i = this.lineList.indexOf(Integer.valueOf(paramInt));
/* 203 */     if (i == -1 || i == this.lineList.size() - 1)
/* 204 */       return -1; 
/* 205 */     return ((Integer)this.lineList.get(i + 1)).intValue();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\SourceWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */