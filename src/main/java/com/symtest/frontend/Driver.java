package com.symtest.frontend;

/**
 * The driver function for SymTest. Parses the input file, and calls the
 * visitor. If the input file has the name xxx.cymbol, the target list is
 * expected to be present in a file xxx.cymbol.target.
 * Target file format is:
 * LINE_NO - COVERAGE_TYPE
 */

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.symtest.backtracking_heuristic.ComputationTree;
import com.symtest.backtracking_heuristic.ComputationTreeHandler;
import com.symtest.cymbol.CymbolLexer;
import com.symtest.cymbol.CymbolParser;;

public class Driver {

	private static final Logger logger = Logger
			.getLogger(Driver.class.getName());

	public static void main(String[] args) throws Exception {
		// Input format
		// Arg[0] - File_Name
		// Convention:
		// filename.cymbol : Cymbol Program
		// filename.target : List of target edges in Cymbol program
		// filename.data : Data from historical runs
		// filename.meta : Metadata used to analyze historical runs
		//				   Format:
		//		           STARTING_EDGE LOOPING_EDGE
		//				   LIST_OF_TARGETS (space separated)
		// arg[1] - Number of Decision Nodes
		System.out.println("### ARGS " + args[0] + " " + args[1]);
		logger.info("Input file: " + args[0]);
		// TIME - Computation Tree Construction
		long startTime = System.nanoTime();
		// Initialize Computation Tree
		ComputationTreeHandler.init(args[0], Integer.parseInt(args[1]));
        System.out.println("Computation Tree Initialized");
		long endTime = System.nanoTime();
		double duration = (endTime - startTime) / 1000000.0;
		System.out.println("\n---- TIME - Computation Tree Construction : " + duration + "ms ----");
		// ANTLR Boilerplate code
		// ANTLRInputStream input = new ANTLRInputStream(is);
		CharStream input = CharStreams.fromFileName(args[0] + ".cymbol");
		CymbolLexer lexer = new CymbolLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CymbolParser parser = new CymbolParser(tokens);
		ParseTree tree = parser.file();
		CFGVisitor visitor = new CFGVisitor(args[0] + ".target");
		// TIME - Symtest
		startTime = System.nanoTime();
		visitor.visit(tree);
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000.0;
		System.out.println("\n---- TIME - Symtest : " + duration + "ms ----");
	}
}
