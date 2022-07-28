/*     */ package sun.tools.jcmd;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
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
/*     */ class Arguments
/*     */ {
/*     */   private boolean listProcesses = false;
/*     */   private boolean listCounters = false;
/*     */   private boolean showUsage = false;
/*  36 */   private int pid = -1;
/*  37 */   private String command = null;
/*     */   private String processSubstring;
/*     */   
/*  40 */   public boolean isListProcesses() { return this.listProcesses; }
/*  41 */   public boolean isListCounters() { return this.listCounters; }
/*  42 */   public boolean isShowUsage() { return this.showUsage; }
/*  43 */   public int getPid() { return this.pid; }
/*  44 */   public String getCommand() { return this.command; } public String getProcessSubstring() {
/*  45 */     return this.processSubstring;
/*     */   }
/*     */   public Arguments(String[] paramArrayOfString) {
/*  48 */     if (paramArrayOfString.length == 0 || paramArrayOfString[0].equals("-l")) {
/*  49 */       this.listProcesses = true;
/*     */       
/*     */       return;
/*     */     } 
/*  53 */     if (paramArrayOfString[0].equals("-h") || paramArrayOfString[0].equals("-help")) {
/*  54 */       this.showUsage = true;
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/*  59 */       this.pid = Integer.parseInt(paramArrayOfString[0]);
/*  60 */     } catch (NumberFormatException numberFormatException) {
/*     */       
/*  62 */       if (paramArrayOfString[0].charAt(0) != '-')
/*     */       {
/*  64 */         this.processSubstring = paramArrayOfString[0];
/*     */       }
/*     */     } 
/*     */     
/*  68 */     StringBuilder stringBuilder = new StringBuilder();
/*  69 */     for (byte b = 1; b < paramArrayOfString.length; b++) {
/*  70 */       if (paramArrayOfString[b].equals("-f")) {
/*  71 */         if (paramArrayOfString.length == b + 1) {
/*  72 */           throw new IllegalArgumentException("No file specified for parameter -f");
/*     */         }
/*  74 */         if (paramArrayOfString.length == b + 2) {
/*     */           try {
/*  76 */             readCommandFile(paramArrayOfString[b + 1]);
/*  77 */           } catch (IOException iOException) {
/*  78 */             throw new IllegalArgumentException("Could not read from file specified with -f option: " + paramArrayOfString[b + 1]);
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/*  84 */         throw new IllegalArgumentException("Options after -f are not allowed");
/*     */       } 
/*     */       
/*  87 */       if (paramArrayOfString[b].equals("PerfCounter.print")) {
/*  88 */         this.listCounters = true;
/*     */       } else {
/*  90 */         stringBuilder.append(paramArrayOfString[b]).append(" ");
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     if (this.listCounters != true && stringBuilder.length() == 0) {
/*  95 */       throw new IllegalArgumentException("No command specified");
/*     */     }
/*     */     
/*  98 */     this.command = stringBuilder.toString().trim();
/*     */   }
/*     */   
/*     */   private void readCommandFile(String paramString) throws IOException {
/* 102 */     try (BufferedReader null = new BufferedReader(new FileReader(paramString))) {
/* 103 */       StringBuilder stringBuilder = new StringBuilder();
/*     */       String str;
/* 105 */       while ((str = bufferedReader.readLine()) != null) {
/* 106 */         stringBuilder.append(str).append("\n");
/*     */       }
/* 108 */       this.command = stringBuilder.toString();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void usage() {
/* 113 */     System.out.println("Usage: jcmd <pid | main class> <command ...|PerfCounter.print|-f file>");
/* 114 */     System.out.println("   or: jcmd -l                                                    ");
/* 115 */     System.out.println("   or: jcmd -h                                                    ");
/* 116 */     System.out.println("                                                                  ");
/* 117 */     System.out.println("  command must be a valid jcmd command for the selected jvm.      ");
/* 118 */     System.out.println("  Use the command \"help\" to see which commands are available.   ");
/* 119 */     System.out.println("  If the pid is 0, commands will be sent to all Java processes.   ");
/* 120 */     System.out.println("  The main class argument will be used to match (either partially ");
/* 121 */     System.out.println("  or fully) the class used to start Java.                         ");
/* 122 */     System.out.println("  If no options are given, lists Java processes (same as -p).     ");
/* 123 */     System.out.println("                                                                  ");
/* 124 */     System.out.println("  PerfCounter.print display the counters exposed by this process  ");
/* 125 */     System.out.println("  -f  read and execute commands from the file                     ");
/* 126 */     System.out.println("  -l  list JVM processes on the local machine                     ");
/* 127 */     System.out.println("  -h  this help                                                   ");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jcmd\Arguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */