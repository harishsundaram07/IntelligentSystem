package com.eightpuzzle.astar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Solver {
	
	public static HashMap<Nodes, Nodes> backtrackparent;
	public static List<Nodes> pathlist;
	static Nodes startnode;
	static boolean ismanhattan=true;
	static boolean issolvable=false;

	// Priority queue hold all the Nodes.
	public static PriorityQueue<Nodes> priorityqueue;

	// ArrayList hold all the explored nodes.
	public static ArrayList<Nodes> expandednodes;
	public static String[][] goalnode ;

	public Solver(Nodes intial) {
		if (intial == null) {
			System.out.println("Please provide an input");
		}
		priorityqueue.add(intial);
		ArrayList<Nodes> list = new ArrayList<Nodes>();
		while (!priorityqueue.isEmpty()) {
			int alreadyexpanded;
			Nodes currentnode = priorityqueue.poll();
			// returns and deletes the first node of the priority queue and store it in
			// 'current' variable.
			expandednodes.add(currentnode);


			// Adds current object to the 'end' to the end.
			if (Arrays.deepEquals(currentnode.nodeset, goalnode)) {
				break;
			}
			list = currentnode.expand(currentnode); // expands the current node & stores in list

			// It check if node is already expored or not
			for (Nodes l : list) {
				alreadyexpanded = 0;
				for (Nodes e : expandednodes) {
					if (Arrays.deepEquals(l.nodeset, e.nodeset)) {
						alreadyexpanded = 1;
					}
				}
				if (alreadyexpanded == 1)
					continue;
				backtrackparent.put(l, currentnode);
				
				
				//Check is puzzle is solvable or not
				  if((currentnode.f-currentnode.level)>(l.f-l.level))
					  issolvable=true;
				 
				priorityqueue.add(l);
			}
			if(issolvable==false)
				{
					return;
				}
				
		}
	}

	// Main Function
	public static void main(String args[]) {
		
		
	
			Scanner sc = new Scanner(System.in);
			 System.out.println("\n Please Enter the space  for the blanksquare. Add input nextline");

			 String[][] start = new String[3][3];
			 String[][] goal = new String[3][3];
			 
			 
			 
			 //To get start node input
			 System.out.println(
						"\n**********************************************************************");
			 System.out.println("\n Please Enter the Start node");
			 for (int i=0;i<start.length;i++)
				{
					for(int  j=0;j<start.length;j++)
					{
						start[i][j] = sc.nextLine();
						if(start[i][j].length()!=1 || (start[i][j].charAt(0)<'0' && start[i][j].charAt(0)!=' ') || start[i][j].charAt(0)>'8')
						{
							System.out.println("Error: Input should be any number between 1 to 8 or a single space\nProgram Terminated");
							return;
						}
					}
				}
			 
			 
			 //Get Goal node input
			 System.out.println("\n Please Enter the  Goal node");
			 for (int i=0;i<goal.length;i++)
				{
					for(int j=0;j<goal.length;j++)
					{
						goal[i][j] = sc.nextLine();
						if(goal[i][j].length()!=1 || (goal[i][j].charAt(0)<'0' && goal[i][j].charAt(0)!=' ') || goal[i][j].charAt(0)>'8')
						{
							System.out.println("Error: Input should be any number between 1 to 8 or a single space\nProgram Terminated");
							return;
						}
					}
				}
			 			 
		 
			 for(int m=0;m<2;m++)
			{
				ismanhattan=(m==0)?true:false;
				callsolver(start, goal);
			}
			 
				 


		

	}

	//TODO to track the parent
	private static void getpath(Nodes childnode) {

		pathlist.add(childnode);
		if (childnode != startnode)
			 getpath(backtrackparent.get(childnode));

	}

	private static void callsolver(String[][] start, String[][] goal) {
		// TODO Auto-generated method stub

		int i, j;
		pathlist=new ArrayList<Nodes>();
		backtrackparent=new HashMap<Nodes, Nodes>();
		expandednodes= new ArrayList<Nodes>();
		priorityqueue=new PriorityQueue<Nodes>();
		
		System.out.println(
				"\n**************************************************************************************************");

		
		//Print start nodes
		System.out.println("The Initial State of puzzle\n");

		for (i = 0; i < start.length; i++) {
			for (j = 0; j < start.length; j++)
				System.out.print(start[i][j] + "\t");
			System.out.println();
		}

		//Print Goal nodes
		System.out.println("\nThe Goal Node of   is:\n");
		goalnode = goal;
		for (i = 0; i < goalnode.length; i++) {
			for (j = 0; j < goalnode.length; j++)
				System.out.print(goalnode[i][j] + "\t");
			System.out.println();

		}
		 
			System.out.println("*******************************************************************");
			System.out.println(ismanhattan?"\nThe Solution path to Goal node using Manhattan Distance is\n":"\nThe Solution path to Goal node using Misplaced Tiles is\n");
			long startTime = System.currentTimeMillis();
			Nodes node = new Nodes(start, 0);
			startnode = node;
			new Solver(startnode);
			
			
			
			//Tracks the path to parent
			getpath(expandednodes.get(expandednodes.size() - 1));
			
			
			//Prints 
			for (int i2 = pathlist.size() - 1; i2 >= 0; i2--)
				printExpandedNodes(pathlist.get(i2));
			
			
			if(issolvable)
				System.out.println("Total Cost to reach goal is : "
						+ (expandednodes.size() > 0 ? expandednodes.get(expandednodes.size() - 1).f : 0));
				else
					System.out.println("The puzzle is not solvable. Incomplete");
			
			System.out.println("Total Nodes expandednodes :" + expandednodes.size());
			System.out.println("Total Nodes generated:" + (expandednodes.size() + priorityqueue.size()));		
			
			long endTime = System.currentTimeMillis();
			System.out.println("Time Taken in milli seconds: " + (endTime - startTime));
		
	}

	
	//TODO TO print the nodes
	public static void printExpandedNodes(Nodes node) {
		System.out.println("*******************************************************************");
		for (int l = 0; l < 3; l++) {
			for (int m = 0; m < 3; m++) {
				System.out.print(node.nodeset[l][m] + "\t");
			}
			System.out.println();
		}
		System.out.println("f(n) :" + node.f);
		System.out.println("h(n) :" + (node.f - node.level));
		System.out.println("g(n) :" + (node.level));
		System.out.println('\n');

	}
}