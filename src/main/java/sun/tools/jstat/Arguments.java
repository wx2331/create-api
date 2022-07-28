/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Arguments
/*     */ {
/*  44 */   private static final boolean debug = Boolean.getBoolean("jstat.debug");
/*     */   
/*  46 */   private static final boolean showUnsupported = Boolean.getBoolean("jstat.showUnsupported");
/*     */   
/*     */   private static final String JVMSTAT_USERDIR = ".jvmstat";
/*     */   
/*     */   private static final String OPTIONS_FILENAME = "jstat_options";
/*     */   
/*     */   private static final String UNSUPPORTED_OPTIONS_FILENAME = "jstat_unsupported_options";
/*     */   
/*     */   private static final String ALL_NAMES = "\\w*";
/*     */   private Comparator<Monitor> comparator;
/*     */   private int headerRate;
/*     */   private boolean help;
/*     */   private boolean list;
/*     */   private boolean options;
/*     */   private boolean constants;
/*     */   private boolean constantsOnly;
/*     */   private boolean strings;
/*     */   private boolean timestamp;
/*     */   private boolean snap;
/*     */   private boolean verbose;
/*     */   private String specialOption;
/*     */   private String names;
/*     */   private OptionFormat optionFormat;
/*  69 */   private int count = -1;
/*  70 */   private int interval = -1;
/*     */   
/*     */   private String vmIdString;
/*     */   private VmIdentifier vmId;
/*     */   
/*     */   public static void printUsage(PrintStream paramPrintStream) {
/*  76 */     paramPrintStream.println("Usage: jstat -help|-options");
/*  77 */     paramPrintStream.println("       jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]]");
/*  78 */     paramPrintStream.println();
/*  79 */     paramPrintStream.println("Definitions:");
/*  80 */     paramPrintStream.println("  <option>      An option reported by the -options option");
/*  81 */     paramPrintStream.println("  <vmid>        Virtual Machine Identifier. A vmid takes the following form:");
/*  82 */     paramPrintStream.println("                     <lvmid>[@<hostname>[:<port>]]");
/*  83 */     paramPrintStream.println("                Where <lvmid> is the local vm identifier for the target");
/*  84 */     paramPrintStream.println("                Java virtual machine, typically a process id; <hostname> is");
/*  85 */     paramPrintStream.println("                the name of the host running the target Java virtual machine;");
/*  86 */     paramPrintStream.println("                and <port> is the port number for the rmiregistry on the");
/*  87 */     paramPrintStream.println("                target host. See the jvmstat documentation for a more complete");
/*  88 */     paramPrintStream.println("                description of the Virtual Machine Identifier.");
/*  89 */     paramPrintStream.println("  <lines>       Number of samples between header lines.");
/*  90 */     paramPrintStream.println("  <interval>    Sampling interval. The following forms are allowed:");
/*  91 */     paramPrintStream.println("                    <n>[\"ms\"|\"s\"]");
/*  92 */     paramPrintStream.println("                Where <n> is an integer and the suffix specifies the units as ");
/*  93 */     paramPrintStream.println("                milliseconds(\"ms\") or seconds(\"s\"). The default units are \"ms\".");
/*  94 */     paramPrintStream.println("  <count>       Number of samples to take before terminating.");
/*  95 */     paramPrintStream.println("  -J<flag>      Pass <flag> directly to the runtime system.");
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
/*     */   private static int toMillis(String paramString) throws IllegalArgumentException {
/* 110 */     String[] arrayOfString = { "ms", "s" };
/*     */     
/* 112 */     String str1 = null;
/* 113 */     String str2 = paramString;
/*     */     int i;
/* 115 */     for (i = 0; i < arrayOfString.length; i++) {
/* 116 */       int j = paramString.indexOf(arrayOfString[i]);
/* 117 */       if (j > 0) {
/* 118 */         str1 = paramString.substring(j);
/* 119 */         str2 = paramString.substring(0, j);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     try {
/* 125 */       i = Integer.parseInt(str2);
/*     */       
/* 127 */       if (str1 == null || str1.compareTo("ms") == 0)
/* 128 */         return i; 
/* 129 */       if (str1.compareTo("s") == 0) {
/* 130 */         return i * 1000;
/*     */       }
/* 132 */       throw new IllegalArgumentException("Unknow time unit: " + str1);
/*     */     
/*     */     }
/* 135 */     catch (NumberFormatException numberFormatException) {
/* 136 */       throw new IllegalArgumentException("Could not convert interval: " + paramString);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Arguments(String[] paramArrayOfString) throws IllegalArgumentException {
/* 142 */     byte b = 0;
/*     */     
/* 144 */     if (paramArrayOfString.length < 1) {
/* 145 */       throw new IllegalArgumentException("invalid argument count");
/*     */     }
/*     */     
/* 148 */     if (paramArrayOfString[0].compareTo("-?") == 0 || paramArrayOfString[0]
/* 149 */       .compareTo("-help") == 0) {
/* 150 */       this.help = true; return;
/*     */     } 
/* 152 */     if (paramArrayOfString[0].compareTo("-options") == 0) {
/* 153 */       this.options = true; return;
/*     */     } 
/* 155 */     if (paramArrayOfString[0].compareTo("-list") == 0) {
/* 156 */       this.list = true;
/* 157 */       if (paramArrayOfString.length > 2) {
/* 158 */         throw new IllegalArgumentException("invalid argument count");
/*     */       }
/*     */       
/* 161 */       b++;
/*     */     } 
/*     */     
/* 164 */     for (; b < paramArrayOfString.length && paramArrayOfString[b].startsWith("-"); b++) {
/* 165 */       String str = paramArrayOfString[b];
/*     */       
/* 167 */       if (str.compareTo("-a") == 0) {
/* 168 */         this.comparator = new AscendingMonitorComparator();
/* 169 */       } else if (str.compareTo("-d") == 0) {
/* 170 */         this.comparator = new DescendingMonitorComparator();
/* 171 */       } else if (str.compareTo("-t") == 0) {
/* 172 */         this.timestamp = true;
/* 173 */       } else if (str.compareTo("-v") == 0) {
/* 174 */         this.verbose = true;
/* 175 */       } else if (str.compareTo("-constants") == 0 || str
/* 176 */         .compareTo("-c") == 0) {
/* 177 */         this.constants = true;
/* 178 */       } else if (str.compareTo("-strings") == 0 || str
/* 179 */         .compareTo("-s") == 0) {
/* 180 */         this.strings = true;
/* 181 */       } else if (str.startsWith("-h")) {
/*     */         String str1;
/* 183 */         if (str.compareTo("-h") != 0) {
/* 184 */           str1 = str.substring(2);
/*     */         } else {
/* 186 */           b++;
/* 187 */           if (b >= paramArrayOfString.length) {
/* 188 */             throw new IllegalArgumentException("-h requires an integer argument");
/*     */           }
/*     */           
/* 191 */           str1 = paramArrayOfString[b];
/*     */         } 
/*     */         try {
/* 194 */           this.headerRate = Integer.parseInt(str1);
/* 195 */         } catch (NumberFormatException numberFormatException) {
/* 196 */           this.headerRate = -1;
/*     */         } 
/* 198 */         if (this.headerRate < 0) {
/* 199 */           throw new IllegalArgumentException("illegal -h argument: " + str1);
/*     */         }
/*     */       }
/* 202 */       else if (str.startsWith("-name")) {
/* 203 */         if (str.startsWith("-name=")) {
/* 204 */           this.names = str.substring(7);
/*     */         } else {
/* 206 */           b++;
/* 207 */           if (b >= paramArrayOfString.length) {
/* 208 */             throw new IllegalArgumentException("option argument expected");
/*     */           }
/*     */           
/* 211 */           this.names = paramArrayOfString[b];
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 225 */         String str1 = null;
/* 226 */         int i = paramArrayOfString[b].indexOf('@');
/* 227 */         if (i < 0) {
/* 228 */           str1 = paramArrayOfString[b];
/*     */         } else {
/* 230 */           str1 = paramArrayOfString[b].substring(0, i);
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/* 235 */           int j = Integer.parseInt(str1);
/*     */           
/*     */           break;
/* 238 */         } catch (NumberFormatException numberFormatException) {
/*     */ 
/*     */           
/* 241 */           if (b == 0 && paramArrayOfString[b].compareTo("-snap") == 0) {
/* 242 */             this.snap = true;
/* 243 */           } else if (b == 0) {
/* 244 */             this.specialOption = paramArrayOfString[b].substring(1);
/*     */           } else {
/* 246 */             throw new IllegalArgumentException("illegal argument: " + paramArrayOfString[b]);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 254 */     if (this.specialOption == null && !this.list && !this.snap && this.names == null) {
/* 255 */       throw new IllegalArgumentException("-<option> required");
/*     */     }
/*     */     
/* 258 */     switch (paramArrayOfString.length - b) {
/*     */       case 3:
/* 260 */         if (this.snap) {
/* 261 */           throw new IllegalArgumentException("invalid argument count");
/*     */         }
/*     */         try {
/* 264 */           this.count = Integer.parseInt(paramArrayOfString[paramArrayOfString.length - 1]);
/* 265 */         } catch (NumberFormatException numberFormatException) {
/* 266 */           throw new IllegalArgumentException("illegal count value: " + paramArrayOfString[paramArrayOfString.length - 1]);
/*     */         } 
/*     */         
/* 269 */         this.interval = toMillis(paramArrayOfString[paramArrayOfString.length - 2]);
/* 270 */         this.vmIdString = paramArrayOfString[paramArrayOfString.length - 3];
/*     */         break;
/*     */       case 2:
/* 273 */         if (this.snap) {
/* 274 */           throw new IllegalArgumentException("invalid argument count");
/*     */         }
/* 276 */         this.interval = toMillis(paramArrayOfString[paramArrayOfString.length - 1]);
/* 277 */         this.vmIdString = paramArrayOfString[paramArrayOfString.length - 2];
/*     */         break;
/*     */       case 1:
/* 280 */         this.vmIdString = paramArrayOfString[paramArrayOfString.length - 1];
/*     */         break;
/*     */       case 0:
/* 283 */         if (!this.list) {
/* 284 */           throw new IllegalArgumentException("invalid argument count");
/*     */         }
/*     */         break;
/*     */       default:
/* 288 */         throw new IllegalArgumentException("invalid argument count");
/*     */     } 
/*     */ 
/*     */     
/* 292 */     if (this.count == -1 && this.interval == -1) {
/*     */       
/* 294 */       this.count = 1;
/* 295 */       this.interval = 0;
/*     */     } 
/*     */ 
/*     */     
/* 299 */     if (this.comparator == null) {
/* 300 */       this.comparator = new AscendingMonitorComparator();
/*     */     }
/*     */ 
/*     */     
/* 304 */     this.names = (this.names == null) ? "\\w*" : this.names.replace(',', '|');
/*     */ 
/*     */     
/*     */     try {
/* 308 */       Pattern pattern = Pattern.compile(this.names);
/* 309 */     } catch (PatternSyntaxException patternSyntaxException) {
/* 310 */       throw new IllegalArgumentException("Bad name pattern: " + patternSyntaxException
/* 311 */           .getMessage());
/*     */     } 
/*     */ 
/*     */     
/* 315 */     if (this.specialOption != null) {
/* 316 */       OptionFinder optionFinder = new OptionFinder(optionsSources());
/* 317 */       this.optionFormat = optionFinder.getOptionFormat(this.specialOption, this.timestamp);
/* 318 */       if (this.optionFormat == null) {
/* 319 */         throw new IllegalArgumentException("Unknown option: -" + this.specialOption);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 326 */       this.vmId = new VmIdentifier(this.vmIdString);
/* 327 */     } catch (URISyntaxException uRISyntaxException) {
/* 328 */       IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Malformed VM Identifier: " + this.vmIdString);
/*     */       
/* 330 */       illegalArgumentException.initCause(uRISyntaxException);
/* 331 */       throw illegalArgumentException;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Comparator<Monitor> comparator() {
/* 336 */     return this.comparator;
/*     */   }
/*     */   
/*     */   public boolean isHelp() {
/* 340 */     return this.help;
/*     */   }
/*     */   
/*     */   public boolean isList() {
/* 344 */     return this.list;
/*     */   }
/*     */   
/*     */   public boolean isSnap() {
/* 348 */     return this.snap;
/*     */   }
/*     */   
/*     */   public boolean isOptions() {
/* 352 */     return this.options;
/*     */   }
/*     */   
/*     */   public boolean isVerbose() {
/* 356 */     return this.verbose;
/*     */   }
/*     */   
/*     */   public boolean printConstants() {
/* 360 */     return this.constants;
/*     */   }
/*     */   
/*     */   public boolean isConstantsOnly() {
/* 364 */     return this.constantsOnly;
/*     */   }
/*     */   
/*     */   public boolean printStrings() {
/* 368 */     return this.strings;
/*     */   }
/*     */   
/*     */   public boolean showUnsupported() {
/* 372 */     return showUnsupported;
/*     */   }
/*     */   
/*     */   public int headerRate() {
/* 376 */     return this.headerRate;
/*     */   }
/*     */   
/*     */   public String counterNames() {
/* 380 */     return this.names;
/*     */   }
/*     */   
/*     */   public VmIdentifier vmId() {
/* 384 */     return this.vmId;
/*     */   }
/*     */   
/*     */   public String vmIdString() {
/* 388 */     return this.vmIdString;
/*     */   }
/*     */   
/*     */   public int sampleInterval() {
/* 392 */     return this.interval;
/*     */   }
/*     */   
/*     */   public int sampleCount() {
/* 396 */     return this.count;
/*     */   }
/*     */   
/*     */   public boolean isTimestamp() {
/* 400 */     return this.timestamp;
/*     */   }
/*     */   
/*     */   public boolean isSpecialOption() {
/* 404 */     return (this.specialOption != null);
/*     */   }
/*     */   
/*     */   public String specialOption() {
/* 408 */     return this.specialOption;
/*     */   }
/*     */   
/*     */   public OptionFormat optionFormat() {
/* 412 */     return this.optionFormat;
/*     */   }
/*     */   
/*     */   public List<URL> optionsSources() {
/* 416 */     ArrayList<URL> arrayList = new ArrayList();
/* 417 */     boolean bool = false;
/*     */     
/* 419 */     String str = "jstat_options";
/*     */     
/*     */     try {
/* 422 */       String str1 = System.getProperty("user.home");
/* 423 */       String str2 = str1 + "/" + ".jvmstat";
/* 424 */       File file = new File(str2 + "/" + str);
/* 425 */       arrayList.add(file.toURI().toURL());
/* 426 */     } catch (Exception exception) {
/* 427 */       if (debug) {
/* 428 */         System.err.println(exception.getMessage());
/* 429 */         exception.printStackTrace();
/*     */       } 
/* 431 */       throw new IllegalArgumentException("Internal Error: Bad URL: " + exception
/* 432 */           .getMessage());
/*     */     } 
/* 434 */     URL uRL = getClass().getResource("resources/" + str);
/* 435 */     assert uRL != null;
/* 436 */     arrayList.add(uRL);
/*     */     
/* 438 */     if (showUnsupported) {
/* 439 */       uRL = getClass().getResource("resources/jstat_unsupported_options");
/* 440 */       assert uRL != null;
/* 441 */       arrayList.add(uRL);
/*     */     } 
/* 443 */     return arrayList;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Arguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */