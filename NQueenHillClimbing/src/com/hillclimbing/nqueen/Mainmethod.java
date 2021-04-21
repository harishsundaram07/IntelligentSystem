package com.hillclimbing.nqueen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Mainmethod {

	static int attempts;
	public static void main(String args[]) {
		Scanner sc1 = new Scanner(System.in);
		System.out.println("Please define a no. of attempts to run between 100 t0 1000 : \n");
		 attempts=sc1.nextInt();
	
		if(attempts<100)
		{
			System.out.println("Please enter attempts more than 100");
			System.out.println("Exiting..........");

			return;

		}
		System.out.println("Please define a size (more than 3) for nqueenboard size: \n");
		Scanner sc = new Scanner(System.in);
		int n = Integer.parseInt(sc.nextLine());
		sc.close();
		 if(n<4)
		 {
			 System.out.println("Please enter queen more than 3");
			 System.out.println("Exiting..........");
				return;

		 }
			
		HillClimbingSearchmain(n);
		hillClimbingWithSidewaysMoveMain(n);
		randomRestart(n, false);
		randomRestart(n, true);

	}

	private static void HillClimbingSearchmain(int n) {
		NQueenModel state = new NQueenModel();
		int[] currentBoard = new int[n];
		state.setNoOfTries(0.0f);
		state.setGoalReached(false);
		System.out.println("\nA. Hill Climbing Search : ");
		for (int i = 0; i < attempts; i++) {
			currentBoard = generateNqueen(n);
			HillClimbingSearch(currentBoard, state, i);

			if (state.isGoalReached()) {
				state.setNoOfTries(state.getNoOfTries() + 1);
				state.setGoalReached(false);
			}
		}
		float successRate = (state.getNoOfTries() / attempts) * 100;
		System.out.println("\n*************************************************************************\n");
		System.out.println("\nHill Climbing Search Results: ");
		System.out.println("Success Rate:: " + successRate + " %");
		System.out.println("Failure Rate:: " + (100.0f - successRate) + " %");
		System.out.println("Success Average: "
				+ String.format("%.00f", state.getSuccessSteps().stream().mapToDouble(a -> a).average().getAsDouble()));
		System.out.println("Failure Average: "
				+ String.format("%.00f", state.getFailureSteps().stream().mapToDouble(a -> a).average().getAsDouble()));
		System.out.println("\n*************************************************************************\n");
	}


	private static void hillClimbingWithSidewaysMoveMain(int n) {
		NQueenModel state = new NQueenModel();
		state.setNoOfTries(0.0f);
		state.setGoalReached(false);
		int[] currentBoard = new int[n];
		System.out.println("\n*************************************************************************\n");
		System.out.println("\n B. Hill Climbing With Sideways Moves: ");
		for (int i = 0; i < attempts; i++) {
			currentBoard = generateNqueen(n);
			hillClimbingWithSidewaysMove(currentBoard, state, i);

			if (state.isGoalReached()) {
				state.setNoOfTries(state.getNoOfTries() + 1);
				state.setGoalReached(false);
			}
		}
		float successRate = (state.getNoOfTries() / attempts) * 100;
		System.out.println("\n*************************************************************************\n");
		System.out.println("\nHill Climbing With Sideways Moves Results: ");
		System.out.println("Success Rate:: " + successRate + " %");
		System.out.println("Failure Rate:: " + (100.0f - successRate) + " %");
		System.out.println("Success Average: "
				+ String.format("%.00f", state.getSuccessSteps().stream().mapToDouble(a -> a).average().getAsDouble()));
		System.out.println("Failure Average: " + String.format("%.00f", (state.getFailureSteps().isEmpty() ? 0
				: state.getFailureSteps().stream().mapToDouble(a -> a).average().getAsDouble())));
		System.out.println("\n*************************************************************************\n");
	}

	
	private static void randomRestart(int n, boolean sidewayMoves) {
		NQueenModel state = new NQueenModel();
		state.setNoOfTries(1.0f);
		state.setGoalReached(false);
		int[] currentBoard = new int[n];
		int printCount = 0;
		currentBoard = generateNqueen(n);
		System.out.println("\n*************************************************************************\n");
		if (sidewayMoves) {
			System.out.println("\nC. Random Restart  With Sideways Moves: ");
			hillClimbingWithSidewaysMove(currentBoard, state, printCount);
		} else {
			System.out.println("\nC. Random Restart  Without Sideways Moves: ");
			HillClimbingSearch(currentBoard, state, printCount);
		}
		System.out.println("\n*************************************************************************\n");
		while (!state.isGoalReached()) {
			currentBoard = generateNqueen(n);
			state.setNoOfTries(state.getNoOfTries() + 1);
			printCount++;
			if (sidewayMoves) {
				hillClimbingWithSidewaysMove(currentBoard, state, printCount);
			} else {
				HillClimbingSearch(currentBoard, state, printCount);
			}
		}
		System.out.println("\nResults: ");
		System.out.println("Average restarts required: " + printCount + 1);
		System.out.println("Average Steps: " + String.format("%.00f",
				(state.getFailureSteps().isEmpty()
						? state.getSuccessSteps().stream().mapToDouble(a -> a).average().getAsDouble()
						: state.getSuccessSteps().stream().mapToDouble(a -> a).average().getAsDouble()
								+ state.getFailureSteps().stream().mapToDouble(a -> a).average().getAsDouble())));
		System.out.println("\n*************************************************************************\n");
	}
	

	private static int[] HillClimbingSearch(int[] currentBoard, NQueenModel state, int printCount) {

		int currentThreats = calculateHeuristic(currentBoard);
		int min = 0;
		Integer steps = 0;
		if (printCount < 4) {
			System.out.println("\n Attempt " + (printCount + 1));
			printNqueen(currentBoard);
		}

		while (null != currentBoard && min < currentThreats) {

			int[][] heuristicBoard = calculatePotentialMoves(currentBoard);
			min = Arrays.stream(heuristicBoard).flatMapToInt(Arrays::stream).min().getAsInt();
			if (min < currentThreats) {

				currentBoard = traverseToNeighbor(min, printCount, currentBoard, heuristicBoard);
				if(null != currentBoard) {
				steps++;
				currentThreats = calculateHeuristic(currentBoard);
				heuristicBoard = calculatePotentialMoves(currentBoard);
				min = Arrays.stream(heuristicBoard).flatMapToInt(Arrays::stream).min().getAsInt();
				}
			}
		}
		if (0 == currentThreats) {
			state.setGoalReached(true);
			state.getSuccessSteps().add(steps);
		} else {
			state.getFailureSteps().add(steps);
		}
		return currentBoard;
	}

	
	
	private static int[] hillClimbingWithSidewaysMove(int[] currentBoard, NQueenModel state, int printCount) {

		int currentThreats = calculateHeuristic(currentBoard);
		int min = 0, sidewaysMovesCounter = 0;
		int steps = 0;
		if (printCount < 4) {
			System.out.println("\nAttempt " + (printCount + 1));
			printNqueen(currentBoard);
			

		}
		while (null!=currentBoard && min <= currentThreats && 100 >= sidewaysMovesCounter) {
			if (0 == currentThreats) {
				state.setGoalReached(true);
				state.getSuccessSteps().add(steps);
				break;
			} else {

				int[][] heuristicBoard = calculatePotentialMoves(currentBoard);
				min = Arrays.stream(heuristicBoard).flatMapToInt(Arrays::stream).min().getAsInt();
				if (min <= currentThreats) {
					if (min == currentThreats) {
						sidewaysMovesCounter++;
					} else {
						sidewaysMovesCounter = 0;
					}
					currentBoard = traverseToNeighbor(min, printCount, currentBoard, heuristicBoard);
					if(null!=currentBoard) {
					steps++;
					currentThreats = calculateHeuristic(currentBoard);
					heuristicBoard = calculatePotentialMoves(currentBoard);
					min = Arrays.stream(heuristicBoard).flatMapToInt(Arrays::stream).min().getAsInt();
					}
				} else {
					state.getFailureSteps().add(steps);
					break;
				}
			}
		}
		if (0 == currentThreats) {
			state.setGoalReached(true);
			state.getSuccessSteps().add(steps);
		} else {
			state.getFailureSteps().add(steps);
		}
		return currentBoard;
	}
	

	private static int[][] calculatePotentialMoves(int[] currentBoard) {
		
		int[][] heuristicBoard = new int[currentBoard.length][currentBoard.length];
		for (int i = 0; i < currentBoard.length; i++) {
			int[] newBoard = currentBoard.clone();
			for (int j = 0; j < currentBoard.length; j++) {
				newBoard[i] = j;
				heuristicBoard[i][j] = calculateHeuristic(newBoard);
				if(i == currentBoard[j]) {
					heuristicBoard[i][j] = 1000;
				}
			}
		}
		return heuristicBoard;
	}

	private static int[] traverseToNeighbor(int min, int printCount, int[] currentBoard, int[][] heuristicBoard) {
		List<int[]> minimumPositions = new ArrayList<>();
		
		int[] cb = new int[currentBoard.length];
		cb = currentBoard.clone();
		
		for (int i = 0; i < heuristicBoard.length; i++) {
			for (int j = 0; j < heuristicBoard.length; j++) {
				if (min == heuristicBoard[i][j] && currentBoard[j]!=i) {
					int[] position = {i, j};
					minimumPositions.add(position);
				}
			}
		}

		int[] position = minimumPositions.get(new Random().nextInt(minimumPositions.size()));
		cb[position[0]] = position[1];
		
	
		while (isArrayEqual(cb, currentBoard)) {
			if (minimumPositions.size() == 1) {
				return null;
			} else {
				minimumPositions.remove(minimumPositions.indexOf(position));
				position = minimumPositions.get(new Random().nextInt(minimumPositions.size()));
				cb[position[0]] = position[1];
			}
		}
		if (printCount < 3) {
			printNqueen(cb);
		}
		return cb;
	}
	

	private static int calculateHeuristic(int[] currentBoard) {
		int threats = 0;
		for (int i = 0; i < currentBoard.length; i++) {
			int currentQueen = currentBoard[i];
			for (int j = i + 1; j < currentBoard.length; j++) {
				int potentialThreatPosition = Math.abs(j - i);
				int distanceInQueens = Math.abs(currentQueen - currentBoard[j]);
				if (currentQueen == currentBoard[j] || potentialThreatPosition == distanceInQueens) {
					threats++;
				}
			}
		}
		return threats;
	}
	
	
	private static void printNqueen(int[] nqueenBoard) {
		System.out.println(" ");
		System.out.println(Arrays.toString(nqueenBoard));
		for (int i = 0; i < nqueenBoard.length; i++) {
			System.out.println(" ");
			for (int j = 0; j < nqueenBoard.length; j++) {
				if (i == nqueenBoard[j]) {
					System.out.print("1 ");
				} else {
					System.out.print("0 ");
				}
			}
		}
		System.out.println("\n*************************************************************************\n");
	}
	

	private static boolean isArrayEqual(int[] cb, int[] currentBoard) {
		boolean equal = true;
		for (int i = 0; i < currentBoard.length; i++) {
			if(cb[i] != currentBoard[i]) {
				equal = false;
			}
		}
		return equal;
	}

	
	private static int[] generateNqueen(int n) {
		return new Random().ints(n, 0, n - 1).toArray();
	}
}
