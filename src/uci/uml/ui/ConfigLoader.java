// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package uci.uml.ui;

import java.util.*;
import java.io.*;


public class ConfigLoader {

  ////////////////////////////////////////////////////////////////
  // static utility functions

  public static String TabPath = "uci.uml.ui";

  public static void loadTabs(Vector tabs, String panelName,
			      StatusBar sb) {
    String configFile = System.getProperty("argo.config", "argo.ini");
    LineNumberReader lnr = null;
    try {
      java.net.URL configURL = ConfigLoader.class.getResource(configFile);
      Object config = configURL.getContent();
      if (config != null) {
	InputStream is = (InputStream) config;
	lnr = new LineNumberReader(new InputStreamReader(is));
      }
    }
    catch (java.io.IOException io) { System.out.println("IOException"); }
    catch (ClassCastException io) { System.out.println("ClassCastException"); }

    if (lnr != null) {
      try {
	String line = lnr.readLine();
	while (line != null) {
	  Class tabClass = parseConfigLine(line, panelName, lnr.getLineNumber(),
					   configFile, sb);
	  if (tabClass != null) {
	    //System.out.println("tab=" + tabClass.getName());
	    try {
	      Object newTab = tabClass.newInstance();
	      tabs.addElement(newTab);
	    }
	    catch (InstantiationException ex) {
	      System.out.println("Could not make instance of " +
				 tabClass.getName());
	    }
	    catch (IllegalAccessException ex) {
	      System.out.println("Could not make instance of " +
				 tabClass.getName());
	    }
	  }
	  line = lnr.readLine();
	}
      }
      catch (java.io.IOException io) { System.out.println("IOException"); }
    }
    else { System.out.println("lnr is null"); }
  }

  public static Class parseConfigLine(String line, String panelName,
				      int lineNum, String configFile,
				      StatusBar sb) {
    if (line.startsWith("tabpath")) {
      String newPath = stripBeforeColon(line).trim();
      if (newPath.length() > 0) TabPath = newPath;
      return null;
    }
    else if (line.startsWith(panelName)) {
      String tabNames = stripBeforeColon(line).trim();
      StringTokenizer tabAlternatives = new StringTokenizer(tabNames, "|");
      Class res = null;
      while (tabAlternatives.hasMoreElements()) {
	String tabSpec = tabAlternatives.nextToken().trim();
	String tabName = tabSpec;  //needs-more-work: arguments
	String tabClassName = TabPath + "." + tabName;
	try {
	  res = Class.forName(tabClassName);
	}
	catch (ClassNotFoundException cnfe) { }
	catch (Exception e) {
	  System.out.println("Unanticipated exception, skipping "+tabName);
	  System.out.println(e.toString());
	}
	if (res != null) {
	  sb.showStatus("Making Project Browser: " + tabName);
	  sb.incProgress(2);
	  return res;
	}
      }
      if (Boolean.getBoolean("dbg")) {
	System.out.println("\nCould not find any of these classes:\n" +
			   "TabPath=" + TabPath + "\n" +
			   "Config file=" + configFile + "\n" +
			   "Config line #" + lineNum + ":" + line);
      }
    }
    return null;
  }

  public static String stripBeforeColon(String s) {
    int colonPos = s.indexOf(":");
    return s.substring(colonPos  + 1);
  }


} /* end class ConfigLoader */
