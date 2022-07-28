/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.som.cff.FileLocator;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
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
/*     */ public class Util
/*     */ {
/*     */   public static String getVersion() {
/*  67 */     return getVersion("com/sun/tools/corba/se/idl/idl.prp");
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
/*     */   public static String getVersion(String paramString) {
/*  80 */     String str = "";
/*  81 */     if (messages == null) {
/*     */       
/*  83 */       Vector vector = msgFiles;
/*  84 */       if (paramString == null || paramString.equals(""))
/*  85 */         paramString = "com/sun/tools/corba/se/idl/idl.prp"; 
/*  86 */       paramString = paramString.replace('/', File.separatorChar);
/*  87 */       registerMessageFile(paramString);
/*  88 */       str = getMessage("Version.product", getMessage("Version.number"));
/*  89 */       msgFiles = vector;
/*  90 */       messages = null;
/*     */     }
/*     */     else {
/*     */       
/*  94 */       str = getMessage("Version.product", getMessage("Version.number"));
/*     */     } 
/*  96 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAttribute(String paramString, Hashtable paramHashtable) {
/* 101 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 102 */     return (symtabEntry == null) ? false : (symtabEntry instanceof AttributeEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isConst(String paramString, Hashtable paramHashtable) {
/* 107 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 108 */     return (symtabEntry == null) ? false : (symtabEntry instanceof ConstEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isEnum(String paramString, Hashtable paramHashtable) {
/* 113 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 114 */     return (symtabEntry == null) ? false : (symtabEntry instanceof EnumEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isException(String paramString, Hashtable paramHashtable) {
/* 119 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 120 */     return (symtabEntry == null) ? false : (symtabEntry instanceof ExceptionEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isInterface(String paramString, Hashtable paramHashtable) {
/* 125 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 126 */     return (symtabEntry == null) ? false : (symtabEntry instanceof InterfaceEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isMethod(String paramString, Hashtable paramHashtable) {
/* 131 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 132 */     return (symtabEntry == null) ? false : (symtabEntry instanceof MethodEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isModule(String paramString, Hashtable paramHashtable) {
/* 137 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 138 */     return (symtabEntry == null) ? false : (symtabEntry instanceof ModuleEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isParameter(String paramString, Hashtable paramHashtable) {
/* 143 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 144 */     return (symtabEntry == null) ? false : (symtabEntry instanceof ParameterEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPrimitive(String paramString, Hashtable paramHashtable) {
/* 151 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 152 */     if (symtabEntry == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 157 */       int i = paramString.indexOf('(');
/* 158 */       if (i >= 0)
/* 159 */         symtabEntry = (SymtabEntry)paramHashtable.get(paramString.substring(0, i)); 
/*     */     } 
/* 161 */     return (symtabEntry == null) ? false : (symtabEntry instanceof PrimitiveEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isSequence(String paramString, Hashtable paramHashtable) {
/* 166 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 167 */     return (symtabEntry == null) ? false : (symtabEntry instanceof SequenceEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isStruct(String paramString, Hashtable paramHashtable) {
/* 172 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 173 */     return (symtabEntry == null) ? false : (symtabEntry instanceof StructEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isString(String paramString, Hashtable paramHashtable) {
/* 178 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 179 */     return (symtabEntry == null) ? false : (symtabEntry instanceof StringEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isTypedef(String paramString, Hashtable paramHashtable) {
/* 184 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 185 */     return (symtabEntry == null) ? false : (symtabEntry instanceof TypedefEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isUnion(String paramString, Hashtable paramHashtable) {
/* 190 */     SymtabEntry symtabEntry = (SymtabEntry)paramHashtable.get(paramString);
/* 191 */     return (symtabEntry == null) ? false : (symtabEntry instanceof UnionEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMessage(String paramString) {
/* 199 */     if (messages == null)
/* 200 */       readMessages(); 
/* 201 */     String str = messages.getProperty(paramString);
/* 202 */     if (str == null)
/* 203 */       str = getDefaultMessage(paramString); 
/* 204 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getMessage(String paramString1, String paramString2) {
/* 209 */     if (messages == null)
/* 210 */       readMessages(); 
/* 211 */     String str = messages.getProperty(paramString1);
/* 212 */     if (str == null) {
/* 213 */       str = getDefaultMessage(paramString1);
/*     */     } else {
/*     */       
/* 216 */       int i = str.indexOf("%0");
/* 217 */       if (i >= 0)
/* 218 */         str = str.substring(0, i) + paramString2 + str.substring(i + 2); 
/*     */     } 
/* 220 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getMessage(String paramString, String[] paramArrayOfString) {
/* 225 */     if (messages == null)
/* 226 */       readMessages(); 
/* 227 */     String str = messages.getProperty(paramString);
/* 228 */     if (str == null) {
/* 229 */       str = getDefaultMessage(paramString);
/*     */     } else {
/* 231 */       for (byte b = 0; b < paramArrayOfString.length; b++) {
/*     */         
/* 233 */         int i = str.indexOf("%" + b);
/* 234 */         if (i >= 0)
/* 235 */           str = str.substring(0, i) + paramArrayOfString[b] + str.substring(i + 2); 
/*     */       } 
/* 237 */     }  return str;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getDefaultMessage(String paramString) {
/* 242 */     String str = messages.getProperty(defaultKey);
/* 243 */     int i = str.indexOf("%0");
/* 244 */     if (i > 0)
/* 245 */       str = str.substring(0, i) + paramString; 
/* 246 */     return str;
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
/*     */   private static void readMessages() {
/* 273 */     messages = new Properties();
/* 274 */     Enumeration<String> enumeration = msgFiles.elements();
/*     */     
/* 276 */     while (enumeration.hasMoreElements()) {
/*     */       
/*     */       try {
/* 279 */         DataInputStream dataInputStream = FileLocator.locateLocaleSpecificFileInClassPath(enumeration.nextElement());
/* 280 */         messages.load(dataInputStream);
/*     */       }
/* 282 */       catch (IOException iOException) {}
/*     */     } 
/*     */     
/* 285 */     if (messages.size() == 0) {
/* 286 */       messages.put(defaultKey, "Error reading Messages File.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerMessageFile(String paramString) {
/* 293 */     if (paramString != null) {
/* 294 */       if (messages == null) {
/* 295 */         msgFiles.addElement(paramString);
/*     */       } else {
/*     */         
/*     */         try {
/* 299 */           DataInputStream dataInputStream = FileLocator.locateLocaleSpecificFileInClassPath(paramString);
/* 300 */           messages.load(dataInputStream);
/*     */         }
/* 302 */         catch (IOException iOException) {}
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/* 307 */   private static Properties messages = null;
/* 308 */   private static String defaultKey = "default";
/* 309 */   private static Vector msgFiles = new Vector();
/*     */   
/*     */   static {
/* 312 */     msgFiles.addElement("com/sun/tools/corba/se/idl/idl.prp");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String capitalize(String paramString) {
/* 320 */     String str = new String(paramString.substring(0, 1));
/* 321 */     str = str.toUpperCase();
/* 322 */     return str + paramString.substring(1);
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
/*     */   public static String getAbsolutePath(String paramString, Vector paramVector) throws FileNotFoundException {
/* 336 */     String str = null;
/* 337 */     File file = new File(paramString);
/* 338 */     if (file.canRead()) {
/* 339 */       str = file.getAbsolutePath();
/*     */     } else {
/*     */       
/* 342 */       String str1 = null;
/* 343 */       Enumeration<String> enumeration = paramVector.elements();
/* 344 */       while (!file.canRead() && enumeration.hasMoreElements()) {
/*     */         
/* 346 */         str1 = (String)enumeration.nextElement() + File.separatorChar + paramString;
/* 347 */         file = new File(str1);
/*     */       } 
/* 349 */       if (file.canRead()) {
/* 350 */         str = file.getPath();
/*     */       } else {
/* 352 */         throw new FileNotFoundException(paramString);
/*     */       } 
/* 354 */     }  return str;
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
/*     */   public static float absDelta(float paramFloat1, float paramFloat2) {
/* 371 */     double d = (paramFloat1 - paramFloat2);
/* 372 */     return (float)((d < 0.0D) ? (d * -1.0D) : d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 378 */   static RepositoryID emptyID = new RepositoryID();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */