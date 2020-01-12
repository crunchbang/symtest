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
		//USE THIS to specify the input file
//		String inputFile = "/Users/athul/src/SKC/symtest/test_programs/SingleInput.cymbol";
//		String inputFile = "/Users/athul/src/SKC/symtest/test_programs/MultipleInput.cymbol";
//		String inputFile = "/Users/athul/src/SKC/symtest/test_programs/NoWhile.cymbol";
//		String inputFile = "/Users/athul/src/SKC/symtest/test_programs/NestedIf.cymbol";
//		String inputFile = "/Users/athul/src/SKC/symtest/test_programs/Test3.cymbol";
		
		//HERE BE DRAGONS
//		String inputFile = "/Users/athul/src/SKC/symtest/test_programs/Temp.cymbol";
//		String inputFile = "/Users/athul/src/SKC/symtest/test_programs/Test2.cymbol";
//		String inputFile = "/Users/athul/src/SKC/symtest/test_programs/Test3.cymbol";
		logger.info("Input file: " + args[0]);
		// Initialize Computation Tree
		ComputationTreeHandler.init("/Users/athul/src/symtest/src/main/test/test_programs/H_3targets.out", "BEGIN->BB1", "BB2->D3");
		ComputationTree ct = ComputationTreeHandler.get();
        System.out.println("Computation Tree Initialized");
        ct.computeOrdering("D7->BB9 D10->BB12 D19->BB20");
		// ANTLR Boilerplate code
		// ANTLRInputStream input = new ANTLRInputStream(is);
		CharStream input = CharStreams.fromFileName(args[0]);
		CymbolLexer lexer = new CymbolLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CymbolParser parser = new CymbolParser(tokens);
		ParseTree tree = parser.file();
		CFGVisitor visitor = new CFGVisitor(args[0] + ".target");
		visitor.visit(tree);
	}
}
