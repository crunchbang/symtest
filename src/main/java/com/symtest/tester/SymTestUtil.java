package com.symtest.tester;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.symtest.expression.IExpression;
import com.symtest.expression.IIdentifier;
import com.symtest.expression.Variable;
import com.symtest.Solver.DRealSolver;
import com.symtest.Solver.ISolver;
import com.symtest.Solver.SolverResult;
import com.symtest.Solver.YicesSolver;
import com.symtest.see.SEE;
import com.symtest.set.SET;
import com.symtest.set.SETNode;
import com.symtest.cfg.ICFEdge;
import com.symtest.cfg.ICFG;

/**
 * Contains the common functionalities required for SymTest
 * @author pavithra
 *
 */
public class SymTestUtil {

	private static final Logger logger = Logger
			.getLogger(SymTestUtil.class.getName());
	
	public static SolverResult solveSequence(SET set) throws Exception {
		set.updateLeafNodeSet();
		Set<SETNode> leaves = set.getLeafNodes();
		if (leaves.size() != 1) {
			Exception e = new Exception(
					"SymTest.generateTestSequence : SET should have 1 and only 1 leaf. It has "
							+ leaves.size());
			throw e;
		}

		// The check for leaves(Set) having a single node is already done.
		SETNode leaf = leaves.iterator().next();

		IExpression exp = leaf.getPathPredicate();
		logger.fine("path predicate = " + exp.toString());
		Set<IIdentifier> symVars = set.getVariables();
		// Using SMT Solver
		ISolver solver = new YicesSolver(symVars, exp);
//		ISolver solver = new DRealSolver(symVars, exp);
		SolverResult solution = solver.solve();
		return solution;
	}
	
	public static SET getSET(List<ICFEdge> path, ICFG mCFG) throws Exception {
		SEE mSEE = new SEE(mCFG);		
		mSEE.expandSET(path);
		return mSEE.getSET();
	}
	
	public static int getLongestSatisfiablePrefix(List<ICFEdge> path, ICFG cfg)
			throws Exception {
		return binsearch(path, 0, path.size(), cfg);
	}

	private static int binsearch(List<ICFEdge> path, int start, int end,
			ICFG cfg) throws Exception {
		if (start >= (end-1)) {
			return start;
		}
		int mid = (start + end) / 2;
		if (isSAT(path.subList(0, (mid + 1)), cfg))
			return binsearch(path, mid, end, cfg);
		else
			return binsearch(path, start, mid, cfg);

	}

	private static boolean isSAT(List<ICFEdge> path, ICFG cfg) throws Exception {
		System.out.println("@@@UTIL Bin candidate getSET");
		SET set = getSET(path, cfg);
		// Solve the predicate
		SolverResult solution = solveSequence(set);

		return solution.getResult();
	}
	
}