/**
 * CoronaVirusData.java
 * 
 * Reads a data set coronavirus cases by state day by day
 * from a text file to produce a graphical representation
 * of the data in a bar graph if coronavirus cases per state
 * using StdDraw. Draws a scale and labels for the state name
 * and exact case count for each individual bar which represents
 * one of the five largest states(California, Texas, Florida,
 * New York, Illinois). Then draws a label in the corner titling
 * the graph and showing the total number of cases in the US on
 * the date. The scale for the graph is determined dynamically based
 * on the largest number of cases. The graphical representation pauses
 * briefly on each day resulting in an animation of the rising cases each
 * day.
 *
 * @author   Leon Liu
 * @version  1.0
 * @since    9/11/2020
 */

import java.awt.Color;
import java.awt.Font;
import java.util.Scanner;

public class CoronaVirusData
{	
	//  Provide comments for your fields and methods.

	/**
	 * An int array of length 5 to store number of
	 * coronavirus cases in the five states:
	 * index 0: California
	 * index 1: Texas
	 * index 2: Florida
	 * index 3: New York
	 * index 4: Illinois
	 */
	private int[] stateCases;

	/**
	 * Sum of total cases in all states/entire US
	 */
	private int totalCases;

	/**
	 * The day which the data is coming from.
	 */
	private String caseDate;

	/**
	 * No-arg constructor that initializes
	 * the field variables. Specifically, sets
	 * the length of the cases array to 5 for
	 * the 5 states. Sets totalCases to 0 so that
	 * it can be added to later.
	 */
	public CoronaVirusData ( )
	{
		stateCases = new int[5];
		totalCases = 0;
		caseDate = "none";
	}

	/**
	 * Creates an instance of CoronaVirusData
	 * and reads in the coronavirus case data
	 * from data.txt and draws a bar graph with 
	 * labels using StdDraw.
	 */
	public static void main(String [] args)   
	{
		//  Just a couple of lines; create an instance of CoronaVirusData,
		//  and call up to three methods.  Don't forget to set up the canvas
		//  (MeCollage may be helpful).

		CoronaVirusData graphicalRep = new CoronaVirusData();
		graphicalRep.readFileAndGraph();
	}

	/**
	 * Sets up the canvas.
	 * 
	 * Opens and reads through the data file
	 * to sum up the coronavirus cases and also
	 * saves the case count for California, Texas, 
	 * Florida, New York, and Illinois. Uses a 
	 * Scanner passed from the OpenFile class
	 * and separates different elements of each
	 * line by commas checking only the state
	 * name for storing and case count to sum
	 * up the total cases. After updating the field
	 * variables based on the data for a given date
	 * the graph is redrawn and shown. It 
	 * checks that it is only reading
	 * the data from one day by comparing the 
	 * date in each line to the current date.
	 * 
	 * Afterwards, closes the Scanner.
	 */
	private void readFileAndGraph()
	{
		setupCanvas();


		Scanner fileRead = OpenFile.openToRead("data4.txt");

		String currentDate;
		totalCases = 0; //resets sum of cases
		boolean firstLine = true; //first line of the dates
		
		fileRead.nextLine();//skips the first line of column headers
		
		do
		{
			String currLine = fileRead.nextLine();

			//finds indexes of the 4 commas to use for substring()
			int[] commas = new int[4];
			for(int i = 0, index = currLine.indexOf(','); i < 4; i++, index = currLine.indexOf(',', index+1))
			{
				commas[i] = index;
			}

			currentDate = currLine.substring(0, commas[0]);//date is between start and 1st comma
			if(firstLine)
			{
				caseDate = currentDate;
				firstLine = false;
			}

			/*
			 * This line has a date which
			 * doesn't match previously stored date
			 * which means a new set of data has begun
			 * so the graph is updated and then the
			 * total cases and firstLine boolean are
			 * reset.
			 */
			if(!currentDate.equals(caseDate))
			{
				drawGraph();
				//resets
				totalCases = 0;
				firstLine = true;
			}

			
			String casesString = currLine.substring(commas[2]+1, commas[3]); //number of cases is between 3rd and 4th comma
			int cases = -1;

			cases = parseStrToInt(casesString);

			String stateName = currLine.substring(commas[0]+1, commas[1]); //state name is between 1st and 2nd comma
			switch(stateName)
			{
			case "California": 
				stateCases[0] = cases;
				break;
			case "Texas":
				stateCases[1] = cases;
				break;
			case "Florida":
				stateCases[2] = cases;
				break;
			case "New York":
				stateCases[3] = cases;
				break;
			case "Illinois":
				stateCases[4] = cases;
				break;
			default:
				break;
			}

			totalCases += cases;
		
		}
		while(fileRead.hasNext()); 
		
		fileRead.close();
		
		//draws one last time because file ends before the date changes
		//and so the conditional within the loop to update isn't passed
		//to draw the final date
		drawGraph();
	}
	
	/**
	 * General run method for drawing the
	 * entire bar graph to execute each helper
	 * method in order of their layering.
	 * Elements to be drawn farthest in the
	 * back are drawn first and so on. 
	 * First sets up the canvas for StdDraw
	 * and then draws the scale in vertical lines
	 * and then draws the bars with lengths 
	 * proportional to the number of cases
	 * per that state according to scale
	 * along with a label with the name
	 * and exact case count. Then draws
	 * a title for the graph and other
	 * information in the bottom left
	 * corner. Pauses StdDraw afterwards.
	 */
	private void drawGraph()
	{
		clearCanvas();
		double scale = determineScale();
		drawScale(scale);
		drawBars(scale);
		drawText(caseDate);
		StdDraw.show();
		StdDraw.pause(100);
	}

	/**
	 * Sets the canvas size for StdDraw and sets up the
	 * x and y scale (0 - 1400) and (0 - 900) pixels respectively.
	 * Sets the background of the canvas to be a medium shade of blue.
	 * Sets StdDraws default font in anticipation to draw the scale and
	 * labels for the bars on the bar graph. Enables double buffering.
	 */
	private void setupCanvas()
	{
		StdDraw.setCanvasSize(1400, 900);
		StdDraw.setXscale(0, 1400);
		StdDraw.setYscale(0, 900);
		clearCanvas();
		StdDraw.enableDoubleBuffering();
		StdDraw.setFont(new Font("Helvetica", Font.BOLD, 20));
	}

	/**
	 * Clears the canvas by setting the background of
	 * StdDraw to a light blue.
	 */
	private void clearCanvas()
	{
		StdDraw.clear(new Color(110, 155, 230));
	}

	/**
	 * Draws a scale in a lighter color for the bar graph with vertical lines. 
	 * The scale is determined by the largest case number and each line represents
	 * an increase by the greatest power of 10 that is below the largest case
	 * count. Determines the number of lines by dividing the largest case count
	 * by the same greatest power of 10. Ex: (for 750,000 cases the greatest power of 10 below
	 * is 10^5 which is 10^(floor function(trucated) log(750,000)) and 750,000/10^5 is 7
	 * so there are 7 lines each representing a interval of 10^5(100,000). 
	 * However, if the number of lines is too small the power of 10 will be
	 * broken down into half and if the number is too great, the number of
	 * cases per line will be doubled.
	 * 
	 * @param scale The ratio of pixels per case based on the largest state case count.
	 */
	private void drawScale(double scale)
	{
		StdDraw.setFont(new Font("Helvetica", Font.BOLD, 20));
		//reverses function to get scale to get largest case count
		int largestCaseCount = (int) (1125/scale);
		//casesPerLine is the greatest power of 10
		int casesPerLine = (int)(Math.pow(10, (int)(Math.log10(largestCaseCount))));
		int lineNumber;
		if(casesPerLine != 0)
		{
			if(largestCaseCount/casesPerLine < 3) //breaks down the interval in half if the number of lines would be too small
				casesPerLine /= 2;
			else if(largestCaseCount/casesPerLine > 8) //doubles the interval if the number of lines is too large
				casesPerLine *= 2;
			lineNumber = largestCaseCount/casesPerLine;
		}
		else
			lineNumber = 1;
		for(int i = 0; i <= lineNumber; i++)
		{
			StdDraw.setPenColor(220, 230, 245);
			double x = i * (scale*casesPerLine) + 50;
			int labelNumber = i*casesPerLine;
			String numScale;
			//truncates number > 1000 to k
			if(casesPerLine>1000)
				numScale = labelNumber/1000 + "k";
			else
				numScale = ""+labelNumber;
			StdDraw.text(x, 850, numScale, 5);
			StdDraw.setPenColor(190, 200, 225);
			StdDraw.line(x, 800, x, 0); 
		}
	}

	/**
	 * Draws bars representing the number of
	 * cases in each state. It determines the 
	 * length of each bar according to the same
	 * scale used for drawing the scale label which
	 * is determined from the largest case count. It then labels
	 * the bar at the end with the exact number of cases
	 * and the name of the state which is determined by
	 * the index which the number is read from. The array
	 * has hard-coded state names corresponding to each 
	 * index with a switch statement. It iterates through
	 * the array which contains the number of cases for each
	 * state, drawing each bar with a slight change in color
	 * and movement downward. The order of the state cases are
	 * hard-coded into the order of the array.
	 * @param scale The ratio of pixels per case based on 
	 * the largest state case count.
	 */
	private void drawBars(double scale)
	{

		StdDraw.setFont(new Font("Helvetica", Font.BOLD, 18));
		for(int i = 0; i < stateCases.length; i++)
		{
			String stateName;
			switch(i)
			{
			case 0: 
				stateName = "California";
				break;
			case 1:
				stateName = "Texas";
				break;
			case 2:
				stateName = "Florida";
				break;
			case 3:
				stateName = "New York";
				break;
			case 4:
				stateName = "Illinois";
				break;
			default:
				stateName = "!";
				break;
			}

			double length;
			if(scale == 0)
				length = 0;
			else
				length = scale * stateCases[i];
			int y = 775-60*i;

			//shadow box
			StdDraw.setPenColor(220, 230, 245);
			int shadowLength = 2;
			StdDraw.filledRectangle(length/2+50 + shadowLength/2, y - shadowLength*4.0/5, length/2 + shadowLength/2, 20 + shadowLength/2);
			//actual box
			StdDraw.setPenColor(170, 205+5*i, 250-10*i);
			StdDraw.filledRectangle(length/2+50, y, length/2, 20);

			//state and case number labels
			double barEnd = length+50;
			StdDraw.setPenColor(220, 230, 245);
			StdDraw.textRight(barEnd - 10, y-5, ""+ addCommas(stateCases[i]));
			StdDraw.textLeft(barEnd + 10, y-5, stateName);
		}
	}

	/**
	 * First changes the font to be something larger for the title
	 * of the graph.
	 * Draws a text label in the bottom right with the date of the 
	 * data set and the US Total. Below that, it draws
	 * the title of the graph, "CORONAVIRUS Cases By State".
	 */
	private void drawText(String date)
	{
		String year = date.substring(0, date.indexOf('-'));
		String monthText = date.substring(date.indexOf('-')+1, date.lastIndexOf('-'));
		String monthName;
		int monthNum = parseStrToInt(monthText);

		switch(monthNum)
		{
		case 1:
			monthName = "January";
			break;
		case 2:
			monthName = "February";
			break;
		case 3:
			monthName = "March";
			break;
		case 4:
			monthName = "April";
			break;
		case 5:
			monthName = "May";
			break;
		case 6:
			monthName = "June";
			break;
		case 7:
			monthName = "July";
			break;
		case 8:
			monthName = "August";
			break;
		case 9:
			monthName = "September";
			break;
		case 10:
			monthName = "October";
			break;
		case 11:
			monthName = "November";
			break;
		case 12:
			monthName = "December";
			break;
		default:
			monthName = "Bad Number Given";
			break;
		}

		String day = date.substring(date.lastIndexOf('-')+1);

		StdDraw.setFont(new Font("Helvetica", Font.BOLD, 35));
		StdDraw.setPenColor(220, 230, 245);
		StdDraw.textLeft(1050, 250, monthName+" "+day+", "+year);
		StdDraw.textLeft(1050, 200, "US Total: " + addCommas(totalCases));
		StdDraw.setPenColor(170, 250, 250);
		StdDraw.textLeft(1050, 150, "CORONAVIRUS");
		StdDraw.textLeft(1050, 100, "Cases by State");
	}

	/**
	 * Takes a number input and adds commas between every 3 digits
	 * and only works with numbers that are integers.
	 * 
	 * @param num The number to convert to a String and add commas between digits.
	 * @return A String of num but with commas between digits.
	 */
	private String addCommas(int num)
	{
		String numWithCommas = ""+num;
		int commaNumber = numWithCommas.length() / 3;
		if(numWithCommas.length() % 3 == 0)
			commaNumber--;
		for(int i = commaNumber; i >= 1; i--)
		{
			int commaIndex = numWithCommas.length()-3*i;
			numWithCommas = numWithCommas.substring(0, commaIndex) + "," + numWithCommas.substring(commaIndex);
		}
		return numWithCommas;
	}

	/**
	 * Determines a scale by fitting the largest
	 * bar/case count within 1125 pixels.
	 * 
	 * @return A scale of pixels per case.
	 */
	private double determineScale()
	{
		int largestCaseCount = Integer.MIN_VALUE;
		for(int i = 0; i < stateCases.length; i++)
		{
			largestCaseCount = Math.max(largestCaseCount, stateCases[i]);
		}
		if(largestCaseCount == 0)
			return 0;
		return 1125.0/largestCaseCount;
	}

	/**
	 * Takes a given number as a String and uses the
	 * Integer static method to parse it into an integer
	 * and crashes when a non-number String is passed.
	 * 
	 * @param text Number as a string to be parsed to int.
	 * @return The number parsed from the given string.
	 */
	private int parseStrToInt(String text)
	{
		try
		{
			return Integer.parseInt(text);
		}
		catch(NumberFormatException e)
		{
			System.err.println("Unexpected string value \"" + text + "\" can't be parsed to an int.");
			System.exit(3);
			return -1;
		}
	}
}