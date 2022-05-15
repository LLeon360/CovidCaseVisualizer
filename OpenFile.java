/**
 * Utilities for opening a text file.  The text file
 * can be opened and read from, or the file can be
 * opened (created) and written to.
 * @author   Scott DeRuiter and Your Name Here
 * @version  1.0
 * @since    9/9/2020
 */

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class OpenFile
{	
	/**
	 * Opens a file for reading.
	 *
	 * @param filestring   The name of the file to be opened.
	 * @return             A Scanner instance of the file to be opened.
	 */
	public static Scanner openToRead (String filestring)
	{		
		Scanner fromFile = null;
		File fileName = new File("src/"+filestring); //SPECIAL src prefix
		try
		{
			fromFile = new Scanner(fileName);
		}
		catch(FileNotFoundException error)
		{
			System.err.println("Sorry, but the file "  + filestring + 
					" could not be found.");
			System.exit(1);
		}
		
		return fromFile;
	}
	
	/**
	 * Opens a file for writing.
	 *
	 * @param filestring   The name of the file to be opened (created).
	 * @return             A PrintWriter instance of the file to be opened (created).
	 */
	public static PrintWriter openToWrite (String filestring)
	{
		PrintWriter outFile = null;
		try
		{
			outFile = new PrintWriter("src/"+filestring); //SPECIAL src prefix
		}
		catch(IOException error)
		{
			System.err.println("Sorry, but the file "  + filestring + 
					" could not be created.");
			System.exit(2);
		}
		return outFile;
	}
}