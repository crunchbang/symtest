package com.symtest.backtracking_heuristic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ComputationTreeHandler {
    private static ComputationTree compTree;
    // PENDING CLEANUP
    public static int N;

    public static void init(String filename, int N) throws IOException {
        ComputationTreeHandler.N = N;
        // Historial data file: <filename>.data
        String path = filename + ".data";
        // Metadata file: <filename>.meta
		// filename.meta : Metadata used to analyze historical runs
		//				   Format:
		//		           STARTING_EDGE LOOPING_EDGE
		//				   LIST_OF_TARGETS (space separated)
        String metadataPath = filename + ".meta";
        System.out.println("### META " + metadataPath);
        Scanner sc = new Scanner(new File(metadataPath));
        List<String> lines = new ArrayList<String>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        sc.close();
        String[] nodes = lines.get(0).split("\\s+");
        String rootNode = nodes[0];
        String loopNode = nodes[1];
        System.out.println("### NODES " + rootNode + " " + loopNode);
        compTree = new ComputationTree(path, rootNode, loopNode);
        String targetList = lines.get(1);
        System.out.println("### TARGETLIST " + targetList);
        compTree.computeOrdering(targetList);
    }

    public static boolean isInitialized() {
        if (compTree == null) {
            return false;
        }
        return true;
    }

    public static ComputationTree get() {
        return compTree;
    }

}