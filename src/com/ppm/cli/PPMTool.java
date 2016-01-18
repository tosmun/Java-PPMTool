package com.ppm.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.ppm.EdgeDetectionAlgorithm;
import com.ppm.PPM;
import com.ppm.javafx.Display;
import com.ppm.utils.Utils;

public class PPMTool
{
	private static final int DEFAULT_MAX_COLOR = 255;
	private static final EdgeDetectionAlgorithm DEFAULT_EDGE_DETECTION_ALGORITHM = EdgeDetectionAlgorithm.SOBEL;
	//Help
	private static final String OP_HELP = "h";
	private static final String OP_HELP_LONG = "help";
	private static final String OP_HELP_HELP = "Display usage information";
	//Input
	private static final String OP_STDIN = "i";
	private static final String OP_STDIN_LONG = "stdin";
	private static final String OP_STDIN_HELP = "Read PPM from stdin";
	private static final String OP_IN_FILE = "if";
	private static final String OP_IN_FILE_LONG = "in_file";
	private static final String O_IN_FILE_HELP = "Read PPM from file";
	//Output
	private static final String OP_STDOUT = "o";
	private static final String OP_STDOUT_LONG = "stdout";
	private static final String OP_STDOUT_HELP = "Write as PPM to stdout";
	private static final String OP_OUT_FILE = "of";
	private static final String OP_OUT_FILE_LONG = "out_file";
	private static final String OP_OUT_FILE_HELP = "Write as PPM to file";
	private static final String OP_DISPLAY = "d";
	private static final String OP_DISPLAY_LONG = "display";
	private static final String OP_DISPLAY_HELP = "Display output using Javafx";
	//Optional output
	private static final String OP_OUT_EDGE_DETECTION = "e";
	private static final String OP_OUT_EDGE_DETECTION_LONG = "edge_detection";
	private static final String OP_OUT_EDGE_DETECTION_HELP = "Perform edge detection. Can optionally be provded the algorithm to use. Supported algorithms: " + EdgeDetectionAlgorithm.allToString() + ". Default algorithm: " + DEFAULT_EDGE_DETECTION_ALGORITHM;
	private static final String OP_OUT_GREYSCALE = "g";
	private static final String OP_OUT_GREYSCALE_LONG = "greyscale";
	private static final String OP_OUT_GREYSCALE_HELP = "Transform the PPM image to greyscale.";
	private static final String OP_OUT_MAX_COLOR = "c";
	private static final String OP_OUT_MAX_COLOR_LONG = "max_color";
	private static final String OP_OUT_MAX_COLOR_HELP = "Maximum color value used when outputting the PPM. Min=0, Max=" + PPM.MAX_MAX_COLOR_VALUE + ", Default=" + DEFAULT_MAX_COLOR;
	private static Options options = new Options();
	static
	{
		//Help
		options.addOption(new Option(OP_HELP, OP_HELP_LONG, false, OP_HELP_HELP));
		//Input
		{
			final OptionGroup inputGroup = new OptionGroup();
			inputGroup.addOption(new Option(OP_STDIN, OP_STDIN_LONG, false, OP_STDIN_HELP));
			inputGroup.addOption(new Option(OP_IN_FILE, OP_IN_FILE_LONG, true, O_IN_FILE_HELP));
			options.addOptionGroup(inputGroup);
		}
		//Output formats
		{
			final OptionGroup outputGroup = new OptionGroup();
			outputGroup.addOption(new Option(OP_STDOUT, OP_STDOUT_LONG, false, OP_STDOUT_HELP));
			outputGroup.addOption(new Option(OP_OUT_FILE, OP_OUT_FILE_LONG, true, OP_OUT_FILE_HELP));
			outputGroup.addOption(new Option(OP_DISPLAY, OP_DISPLAY_LONG, false, OP_DISPLAY_HELP));
			options.addOptionGroup(outputGroup);
		}
		//Output transformations
		options.addOption(new Option(OP_OUT_MAX_COLOR, OP_OUT_MAX_COLOR_LONG, true, OP_OUT_MAX_COLOR_HELP));
		{
			final Option edgeDetectionOption = new Option(OP_OUT_EDGE_DETECTION, OP_OUT_EDGE_DETECTION_LONG, true, OP_OUT_EDGE_DETECTION_HELP);
			edgeDetectionOption.setOptionalArg(true);
			options.addOption(edgeDetectionOption);
		}
		options.addOption(new Option(OP_OUT_GREYSCALE, OP_OUT_GREYSCALE_LONG, false, OP_OUT_GREYSCALE_HELP));
	}
	public static void main(final String[] args)
	{
		try
		{
			final CommandLine parsed = new DefaultParser().parse(options, args);
			if(parsed.hasOption(OP_HELP))
			{
				new HelpFormatter().printHelp(PPMTool.class.getName(), options);
				return;
			}
			final int maxColor;
			if(parsed.hasOption(OP_OUT_MAX_COLOR))
			{
				final String maxColorStr = parsed.getOptionValue(OP_OUT_MAX_COLOR);
				try
				{
					final Integer maxColorObj = Integer.parseInt(maxColorStr);
					if(maxColorObj.intValue() < 0 || maxColorObj.intValue() > PPM.MAX_MAX_COLOR_VALUE)
						throw new NumberFormatException("Invalid -" + OP_OUT_MAX_COLOR + "(--" + OP_OUT_MAX_COLOR_LONG + "): " + maxColorStr);
					maxColor = maxColorObj.intValue();
				}
				catch(NumberFormatException e) { throw new ParseException(e.getMessage()); }
			}
			else
			{
				maxColor = DEFAULT_MAX_COLOR;
			}
			final EdgeDetectionAlgorithm edgeDetectionAlgorithm;
			if(parsed.hasOption(OP_OUT_EDGE_DETECTION))
			{
				final String edgeDetectionAlgorithmStr = parsed.getOptionValue(OP_OUT_EDGE_DETECTION);
				if(edgeDetectionAlgorithmStr == null)
				{
					edgeDetectionAlgorithm = DEFAULT_EDGE_DETECTION_ALGORITHM;
				}
				else
				{
					try { edgeDetectionAlgorithm = EdgeDetectionAlgorithm.valueOf(edgeDetectionAlgorithmStr); }
					catch(IllegalArgumentException e) { throw new ParseException("Invalid -" + OP_OUT_EDGE_DETECTION + "(--" + OP_OUT_EDGE_DETECTION_LONG + "): " + edgeDetectionAlgorithmStr); }
				}
			}
			else
			{
				edgeDetectionAlgorithm = null;
			}
			final PPM ppm;
			{
				final BufferedReader in = getInput(parsed);
				try { ppm = new PPM(in); }
				finally { in.close(); }
			}
			if(edgeDetectionAlgorithm != null)
				ppm.detectEdges(edgeDetectionAlgorithm);
			if(parsed.hasOption(OP_OUT_GREYSCALE))
				ppm.greyscale();
			doOutput(parsed, ppm, maxColor);
		}
		catch(ParseException e)
		{
			System.err.println(e.getMessage());
			new HelpFormatter().printHelp(PPMTool.class.getName(), options);
			System.exit(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(255);
		}
	}
	private static void doOutput(final CommandLine parsed, final PPM ppm, final int maxColor) throws IllegalArgumentException, IOException, ParseException
	{
		Utils.throwIAEIfNull(parsed, CommandLine.class, "parsed");
		Utils.throwIAEIfNull(ppm, PPM.class, "ppm");
		final List<OutputStream> outs = new ArrayList<OutputStream>();
		final List<OutputStream> closeableOuts = new ArrayList<OutputStream>();
		//Ensure at least one output argument
		if(! parsed.hasOption(OP_STDOUT) && ! parsed.hasOption(OP_OUT_FILE) && ! parsed.hasOption(OP_DISPLAY))
		{
			throw new ParseException("Expecting one or more of the following output arguments to be provided: ["
				+ "-" + OP_STDOUT + "(--" + OP_STDOUT_LONG + "), "
				+ "-" + OP_OUT_FILE + "(--" + OP_OUT_FILE_LONG + "), "
				+ "-" + OP_DISPLAY + "(--" + OP_DISPLAY_LONG + ")"
				+ "]");
		}
		//Write to output streams
		try
		{
			//stdout
			if(parsed.hasOption(OP_STDOUT))
			{
				outs.add(System.out);
			}
			if(parsed.hasOption(OP_OUT_FILE))
			{
				final FileOutputStream fout = new FileOutputStream(new File(parsed.getOptionValue(OP_OUT_FILE)));
				closeableOuts.add(fout);
				outs.add(fout);
			}
			ppm.writeToStreams(maxColor, outs.toArray(new OutputStream[outs.size()]));
		}
		finally
		{
			for(final OutputStream out : closeableOuts)
				out.close();
		}
		//javafx display
		if(parsed.hasOption(OP_DISPLAY))
			Display.display(ppm);
	}
	private static BufferedReader getInput(final CommandLine parsed) throws ParseException
	{
		Utils.throwIAEIfNull(parsed, CommandLine.class, "parsed");
		//Input file?
		if(parsed.hasOption(OP_IN_FILE))
		{
			final File inputFile = new File(parsed.getOptionValue(OP_IN_FILE));
			try { return new BufferedReader(new FileReader(inputFile)); }
			catch(FileNotFoundException e) { throw new ParseException("Input file does not exist or is not a valid file: " + inputFile.getAbsolutePath()); }
		}
		//Stdin
		else if(parsed.hasOption(OP_STDIN))
			return new BufferedReader(new InputStreamReader(System.in));
		throw new ParseException("Expecting one of the following input arguments to be provided: ["
			+ "-" + OP_IN_FILE + "(--" + OP_IN_FILE_LONG + "), "
			+ "-" + OP_STDIN + "(--" + OP_STDIN_LONG + ")"
			+ "]");
	}
}
