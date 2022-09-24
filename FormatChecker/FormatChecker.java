package FormatChecker;

/**
 * @author Nick Codispoti
 * 
 * @param args are the files that contains values
 * @throws FileNotFoundException thrown if the file does not exist
 */

//pain (yes i imported everything i could just cause, i got bored)
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Scanner;
import com.sun.jarsigner.*;
import com.sun.java.accessibility.util.*;
import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.connect.spi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;
import com.sun.management.*;
import com.sun.net.httpserver.*;
import com.sun.net.httpserver.spi.*;
import com.sun.nio.sctp.*;
import com.sun.security.auth.*;
import com.sun.security.auth.callback.*;
import com.sun.security.auth.login.*;
import com.sun.security.auth.module.*;
import com.sun.security.jgss.*;
import com.sun.source.doctree.*;
import com.sun.source.tree.*;
import com.sun.source.util.*;
import com.sun.tools.attach.*;
import com.sun.tools.attach.spi.*;
import com.sun.tools.javac.*;
import com.sun.tools.jconsole.*;
import java.applet.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.datatransfer.*;
import java.awt.desktop.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.im.*;
import java.awt.im.spi.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import java.awt.print.*;
import java.beans.*;
import java.beans.beancontext.*;
import java.io.*;
import java.lang.*;
import java.lang.annotation.*;
import java.lang.constant.*;
import java.lang.instrument.*;
import java.lang.invoke.*;
import java.lang.management.*;
import java.lang.module.*;
import java.lang.ref.*;
import java.lang.reflect.*;
import java.math.*;
import java.net.*;
import java.net.http.*;
import java.net.spi.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.nio.charset.*;
import java.nio.charset.spi.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.nio.file.spi.*;
import java.rmi.*;
import java.rmi.activation.*;
import java.rmi.dgc.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.security.*;
import java.security.cert.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.sql.*;
import java.text.*;
import java.text.spi.*;
import java.time.*;
import java.time.chrono.*;
import java.time.format.*;
import java.time.temporal.*;
import java.time.zone.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import java.util.function.*;
import java.util.jar.*;
import java.util.logging.*;
import java.util.prefs.*;
import java.util.regex.*;
import java.util.spi.*;
import java.util.stream.*;
import java.util.zip.*;
import javax.accessibility.*;
import javax.annotation.processing.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;
import javax.imageio.*;
import javax.imageio.event.*;
import javax.imageio.metadata.*;
import javax.imageio.plugins.bmp.*;
import javax.imageio.plugins.jpeg.*;
import javax.imageio.plugins.tiff.*;
import javax.imageio.spi.*;
import javax.imageio.stream.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;
import javax.management.*;
import javax.management.loading.*;
import javax.management.modelmbean.*;
import javax.management.monitor.*;
import javax.management.openmbean.*;
import javax.management.relation.*;
import javax.management.remote.*;
import javax.management.remote.rmi.*;
import javax.management.timer.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.event.*;
import javax.naming.ldap.*;
import javax.naming.ldap.spi.*;
import javax.naming.spi.*;
import javax.net.*;
import javax.net.ssl.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.*;
import javax.rmi.ssl.*;
import javax.script.*;
import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.kerberos.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;
import javax.security.auth.x500.*;
import javax.security.cert.*;
import javax.security.sasl.*;
import javax.sound.midi.*;
import javax.sound.midi.spi.*;
import javax.sound.sampled.*;
import javax.sound.sampled.spi.*;
import javax.sql.*;
import javax.sql.rowset.*;
import javax.sql.rowset.serial.*;
import javax.sql.rowset.spi.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.multi.*;
import javax.swing.plaf.nimbus.*;
import javax.swing.plaf.synth.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
import javax.swing.text.rtf.*;
import javax.swing.tree.*;
import javax.swing.undo.*;
import javax.tools.*;
import javax.transaction.xa.*;
import javax.xml.*;
import javax.xml.catalog.*;
import javax.xml.crypto.*;
import javax.xml.crypto.dom.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.*;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.crypto.dsig.spec.*;
import javax.xml.datatype.*;
import javax.xml.namespace.*;
import javax.xml.parsers.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import javax.xml.stream.util.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stax.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import javax.xml.xpath.*;
import jdk.dynalink.*;
import jdk.dynalink.beans.*;
import jdk.dynalink.linker.*;
import jdk.dynalink.linker.support.*;
import jdk.dynalink.support.*;
import jdk.javadoc.doclet.*;
import jdk.jfr.*;
import jdk.jfr.consumer.*;
import jdk.jshell.*;
import jdk.jshell.execution.*;
import jdk.jshell.spi.*;
import jdk.jshell.tool.*;
import jdk.management.jfr.*;
import jdk.net.*;
import jdk.nio.*;
import jdk.security.jarsigner.*;
import netscape.javascript.*;
import org.ietf.jgss.*;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.*;
import org.w3c.dom.css.*;
import org.w3c.dom.events.*;
import org.w3c.dom.html.*;
import org.w3c.dom.ls.*;
import org.w3c.dom.ranges.*;
import org.w3c.dom.stylesheets.*;
import org.w3c.dom.traversal.*;
import org.w3c.dom.views.*;
import org.w3c.dom.xpath.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.xml.sax.helpers.*;
//end of pain

public class FormatChecker {
	@SuppressWarnings("unused")
	private static int rows;
	@SuppressWarnings("unused")
	private static int cols;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("aaaa you messed up the syntax noooooo...");
		}
		else {
			for (String file : args) {
				boolean valid = true;
				File filename = new File(file);
				String invalidInfo = "";
				try {
					Scanner fileScan = new Scanner(filename);

					int rowCount = 0;
					int gridDimCount = 0;
					while (fileScan.hasNextLine()) {
						// Reads every line in file
						String line = fileScan.nextLine().trim();

						try {
							Scanner lineScan = new Scanner(line);
							try {
								//check num rows
								if (rowCount > rows && lineScan.hasNextDouble()) {
									valid = false;
									invalidInfo = "- First row of this file is too big >:(";
									break;
								}

								int colCount = 0;
								while (lineScan.hasNext()) {
									//check if first line gives valid dimensions and sets the rows/cols values based off it
									if (rowCount == 0) {
										colCount++;
										if (colCount == 1) {
											rows = lineScan.nextInt();
											gridDimCount++;
										}
										else if (colCount == 2) {
											cols = lineScan.nextInt();
											gridDimCount++;
										}
										else {
											if (lineScan.nextInt() > 0) {
												valid = false;
												gridDimCount++;
												invalidInfo = "- Too many numbers, imma scream if you feed me that many again";
												break;
											}
										}
									}
									else if (rowCount <= rows && colCount < cols) {
										colCount++;
										Double.parseDouble(lineScan.next());
									}
									else {
										colCount++;
										// vomit an error if there's too many columns
										if (colCount > cols) {
											valid = false;
											invalidInfo = "-You've got too many columns my dude";
											break;
										}
										Double.parseDouble(lineScan.next());
									}
								}
								lineScan.close();
							}
							// complain if there is a letter in the data (all my homies hate letters)
							catch (InputMismatchException m) {
								valid = false;
								invalidInfo = m.toString();
								break;
							}
						}
						
						catch (NumberFormatException n) {
							//if value is NaN then scream in pain the words "invalid"
							valid = false;
							invalidInfo = n.toString();
							break;
						}

						if (rowCount == 0) {
							//invalid if the file gives the wrong grid dimensions
							if (gridDimCount != 2) {
								valid = false;
								break;
							}
						}
						rowCount++;
					}

					if (valid == false) {
						//scream in pain if invalid, if not then be happy
						System.out.println("+++ " + file + " +++");
						System.out.println(invalidInfo);
						System.out.println("INVALID");
						System.out.println();
					} else {
						System.out.println("+++ " + file + " +++");
						System.out.println("VALID");
						System.out.println();
					}
					fileScan.close();
				} catch (FileNotFoundException error) {
					System.out.println("+++ " + filename + " +++");
					System.out.println(error.toString());
					System.out.println("INVALID");
					System.out.println();
				}
			}
		}
	}
}