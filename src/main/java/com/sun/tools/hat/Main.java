/*     */ package com.sun.tools.hat;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.ReachableExcludes;
/*     */ import com.sun.tools.hat.internal.model.ReachableExcludesImpl;
/*     */ import com.sun.tools.hat.internal.model.Snapshot;
/*     */ import com.sun.tools.hat.internal.parser.Reader;
/*     */ import com.sun.tools.hat.internal.server.QueryListener;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*  49 */   private static String VERSION_STRING = "jhat version 2.0";
/*     */   
/*     */   private static void usage(String paramString) {
/*  52 */     if (paramString != null) {
/*  53 */       System.err.println("ERROR: " + paramString);
/*     */     }
/*  55 */     System.err.println("Usage:  jhat [-stack <bool>] [-refs <bool>] [-port <port>] [-baseline <file>] [-debug <int>] [-version] [-h|-help] <file>");
/*  56 */     System.err.println();
/*  57 */     System.err.println("\t-J<flag>          Pass <flag> directly to the runtime system. For");
/*  58 */     System.err.println("\t\t\t  example, -J-mx512m to use a maximum heap size of 512MB");
/*  59 */     System.err.println("\t-stack false:     Turn off tracking object allocation call stack.");
/*  60 */     System.err.println("\t-refs false:      Turn off tracking of references to objects");
/*  61 */     System.err.println("\t-port <port>:     Set the port for the HTTP server.  Defaults to 7000");
/*  62 */     System.err.println("\t-exclude <file>:  Specify a file that lists data members that should");
/*  63 */     System.err.println("\t\t\t  be excluded from the reachableFrom query.");
/*  64 */     System.err.println("\t-baseline <file>: Specify a baseline object dump.  Objects in");
/*  65 */     System.err.println("\t\t\t  both heap dumps with the same ID and same class will");
/*  66 */     System.err.println("\t\t\t  be marked as not being \"new\".");
/*  67 */     System.err.println("\t-debug <int>:     Set debug level.");
/*  68 */     System.err.println("\t\t\t    0:  No debug output");
/*  69 */     System.err.println("\t\t\t    1:  Debug hprof file parsing");
/*  70 */     System.err.println("\t\t\t    2:  Debug hprof file parsing, no server");
/*  71 */     System.err.println("\t-version          Report version number");
/*  72 */     System.err.println("\t-h|-help          Print this help and exit");
/*  73 */     System.err.println("\t<file>            The file to read");
/*  74 */     System.err.println();
/*  75 */     System.err.println("For a dump file that contains multiple heap dumps,");
/*  76 */     System.err.println("you may specify which dump in the file");
/*  77 */     System.err.println("by appending \"#<number>\" to the file name, i.e. \"foo.hprof#3\".");
/*  78 */     System.err.println();
/*  79 */     System.err.println("All boolean options default to \"true\"");
/*  80 */     System.exit(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean booleanValue(String paramString) {
/*  87 */     if ("true".equalsIgnoreCase(paramString))
/*  88 */       return true; 
/*  89 */     if ("false".equalsIgnoreCase(paramString)) {
/*  90 */       return false;
/*     */     }
/*  92 */     usage("Boolean value must be true or false");
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*  98 */     if (paramArrayOfString.length < 1) {
/*  99 */       usage("No arguments supplied");
/*     */     }
/*     */     
/* 102 */     boolean bool1 = false;
/* 103 */     int i = 7000;
/* 104 */     boolean bool2 = true;
/* 105 */     boolean bool3 = true;
/* 106 */     String str1 = null;
/* 107 */     String str2 = null;
/* 108 */     int j = 0;
/* 109 */     for (byte b = 0;; b += 2) {
/* 110 */       if (b > paramArrayOfString.length - 1) {
/* 111 */         usage("Option parsing error");
/*     */       }
/* 113 */       if ("-version".equals(paramArrayOfString[b])) {
/* 114 */         System.out.print(VERSION_STRING);
/* 115 */         System.out.println(" (java version " + System.getProperty("java.version") + ")");
/* 116 */         System.exit(0);
/*     */       } 
/*     */       
/* 119 */       if ("-h".equals(paramArrayOfString[b]) || "-help".equals(paramArrayOfString[b])) {
/* 120 */         usage(null);
/*     */       }
/*     */       
/* 123 */       if (b == paramArrayOfString.length - 1) {
/*     */         break;
/*     */       }
/* 126 */       String str4 = paramArrayOfString[b];
/* 127 */       String str5 = paramArrayOfString[b + 1];
/* 128 */       if ("-stack".equals(str4)) {
/* 129 */         bool2 = booleanValue(str5);
/* 130 */       } else if ("-refs".equals(str4)) {
/* 131 */         bool3 = booleanValue(str5);
/* 132 */       } else if ("-port".equals(str4)) {
/* 133 */         i = Integer.parseInt(str5, 10);
/* 134 */       } else if ("-exclude".equals(str4)) {
/* 135 */         str2 = str5;
/* 136 */       } else if ("-baseline".equals(str4)) {
/* 137 */         str1 = str5;
/* 138 */       } else if ("-debug".equals(str4)) {
/* 139 */         j = Integer.parseInt(str5, 10);
/* 140 */       } else if ("-parseonly".equals(str4)) {
/*     */         
/* 142 */         bool1 = booleanValue(str5);
/*     */       } 
/*     */     } 
/* 145 */     String str3 = paramArrayOfString[paramArrayOfString.length - 1];
/* 146 */     Snapshot snapshot = null;
/* 147 */     File file = null;
/* 148 */     if (str2 != null) {
/* 149 */       file = new File(str2);
/* 150 */       if (!file.exists()) {
/* 151 */         System.out.println("Exclude file " + file + " does not exist.  Aborting.");
/*     */         
/* 153 */         System.exit(1);
/*     */       } 
/*     */     } 
/*     */     
/* 157 */     System.out.println("Reading from " + str3 + "...");
/*     */     try {
/* 159 */       snapshot = Reader.readFile(str3, bool2, j);
/* 160 */     } catch (IOException iOException) {
/* 161 */       iOException.printStackTrace();
/* 162 */       System.exit(1);
/* 163 */     } catch (RuntimeException runtimeException) {
/* 164 */       runtimeException.printStackTrace();
/* 165 */       System.exit(1);
/*     */     } 
/* 167 */     System.out.println("Snapshot read, resolving...");
/* 168 */     snapshot.resolve(bool3);
/* 169 */     System.out.println("Snapshot resolved.");
/*     */     
/* 171 */     if (file != null) {
/* 172 */       snapshot.setReachableExcludes((ReachableExcludes)new ReachableExcludesImpl(file));
/*     */     }
/*     */     
/* 175 */     if (str1 != null) {
/* 176 */       System.out.println("Reading baseline snapshot...");
/* 177 */       Snapshot snapshot1 = null;
/*     */       try {
/* 179 */         snapshot1 = Reader.readFile(str1, false, j);
/*     */       }
/* 181 */       catch (IOException iOException) {
/* 182 */         iOException.printStackTrace();
/* 183 */         System.exit(1);
/* 184 */       } catch (RuntimeException runtimeException) {
/* 185 */         runtimeException.printStackTrace();
/* 186 */         System.exit(1);
/*     */       } 
/* 188 */       snapshot1.resolve(false);
/* 189 */       System.out.println("Discovering new objects...");
/* 190 */       snapshot.markNewRelativeTo(snapshot1);
/* 191 */       snapshot1 = null;
/*     */     } 
/* 193 */     if (j == 2) {
/* 194 */       System.out.println("No server, -debug 2 was used.");
/* 195 */       System.exit(0);
/*     */     } 
/*     */     
/* 198 */     if (bool1) {
/*     */       
/* 200 */       System.out.println("-parseonly is true, exiting..");
/* 201 */       System.exit(0);
/*     */     } 
/*     */     
/* 204 */     QueryListener queryListener = new QueryListener(i);
/* 205 */     queryListener.setModel(snapshot);
/* 206 */     Thread thread = new Thread((Runnable)queryListener, "Query Listener");
/* 207 */     thread.setPriority(6);
/* 208 */     thread.start();
/* 209 */     System.out.println("Started HTTP server on port " + i);
/* 210 */     System.out.println("Server is ready.");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */