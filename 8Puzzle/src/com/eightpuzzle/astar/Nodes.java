package com.eightpuzzle.astar;

import java.util.ArrayList;

public class Nodes implements Comparable<Nodes>
{
	public int f;
	public String[][] nodeset;
	public int level;
	public int nodeno;
	
	public Nodes(String[][] a, int level)
	{
		int N = a.length;
		this.nodeset = new String[N][N];
		for (int i=0;i<N;i++)
		{
			for (int j=0; j<N; j++)
			{
				this.nodeset[i][j] = a[i][j];
			}
		}
		
		this.level = level;
		if(Solver.ismanhattan)
			this.f = manhattandistance()+level;
		else
			this.f = misplacedtiles()+level;

	}
	
//Below function calculates the Manhattan distance(heuristic value)	for each node.
 	private int manhattandistance()
 
	{
		int sum=0;
		int[] index= new int[2];
		int N = Solver.goalnode.length;
		for (int i = 0;i<N;i++)
		{
			for (int j = 0; j<N; j++)
			{
				if((this.nodeset[i][j]).trim().isEmpty())
				{
					continue;
				}
				index = get_index(Integer.parseInt(this.nodeset[i][j]));
				sum = sum + (Math.abs(i-index[0])+Math.abs(j-index[1]));
			}
		}
		return sum;
	}
 	
 	private int misplacedtiles()
 	{
 		int sum=0;
		int[] index= new int[2];
		int N = Solver.goalnode.length;
		for (int i = 0;i<N;i++)
		{
			for (int j = 0; j<N; j++)
			{
				if( (this.nodeset[i][j]).trim().isEmpty())
				{
					continue;
				}
				index = get_index(Integer.parseInt(this.nodeset[i][j]));
				if(index[0]!=i || index[1]!=j)
					sum = sum + 1;
				
			}
		}
		return sum;
 		
 	}
	
	
//Below method find the indices of a particular element in the goalnode node and return them in an array.
	private int[] get_index(int a)
	{
		int[] index = new int[2];
		int N = Solver.goalnode.length;
		for (int i = 0;i<N; i++)
		{
			for (int j = 0; j<N; j++)
			{
				if( (Solver.goalnode[i][j]).trim().isEmpty())
				{
					continue;
				}
				if (Solver.goalnode[i][j].trim().equals(String.valueOf(a)))
				{
					index[0]=i;
					index[1]=j;
					return index;
				}
			}
		}
		return index;
	}
	
//Below method generates all the possible child nodes from a given parent node.
	public ArrayList<Nodes> expand(Nodes parent)
	{
		ArrayList<Nodes> nextnode= new ArrayList<Nodes>();
		int N = this.nodeset.length;
		for (int i=0; i< N; i++)
		{
			for (int j = 0; j<N; j++)
			{
				if ((parent.nodeset[i][j]).trim().isEmpty()) //search for the index of space in the node(where a tile can be moved)
				{
					if(i-1>=0)			//checks where a tile can be moved towards the top.
					{
						String[][] a = new String[N][N];
						for (int l=0;l<N;l++)
						{
							for(int m=0;m<N;m++)
							{
								a[l][m]=parent.nodeset[l][m];
							}
						}
						 a = swap(a,i,j,i-1,j);
						 Nodes b = new Nodes(a,parent.level+1);
						 nextnode.add(b);
					}
					if(j-1>=0)			//checks whether a tile can be moved towards left of the space.
					{
						String[][] a = new String[N][N];
						for (int l=0;l<N;l++)
						{
							for(int m=0;m<N;m++)
							{
								a[l][m]=parent.nodeset[l][m];
							}
						}
						 a = swap(a,i,j,i,j-1);
						 Nodes b = new Nodes(a,parent.level+1);
						 nextnode.add(b);
					}  
					if(i+1<N)			//checks whether a tile can be moved towards downward.
					{
						String[][] a = new String[N][N];
						for (int l=0;l<N;l++)
						{
							for(int m=0;m<N;m++)
							{
								a[l][m]=parent.nodeset[l][m];
							}
						}
						 a = swap(a,i,j,i+1,j);
						 Nodes b = new Nodes(a,parent.level+1);
						 nextnode.add(b);
					} 
					if(j+1<N)			//checks whether a tile can be moved towards right side.
					{
						String[][] a = new String[N][N];
						for (int l=0;l<N;l++)
						{
							for(int m=0;m<N;m++)
							{
								a[l][m]=parent.nodeset[l][m];
							}
						}
						 a = swap(a,i,j,i,j+1);
						 Nodes b = new Nodes(a,parent.level+1);
						 nextnode.add(b);
					} 
				}
			}
		}
		return nextnode;
	}
	
	
//Below method is for swapping the desired elements in the indices of the blocks provided
	private String[][] swap(String[][] a,int row1, int col1, int row2, int col2)
	{
        String[][] copy = a;
        String tmp = copy[row1][col1];
        copy[row1][col1] = copy[row2][col2];
        copy[row2][col2] = tmp;

        return copy;
    }
	
	
//Below function provide the sorting technique for the priority queue created in Solution class
	@Override
	public int compareTo(Nodes o) {
		if(this.f==o.f)
		{
			return ((Solver.ismanhattan==true)?(this.manhattandistance() - o.manhattandistance()):(this.misplacedtiles() - o.misplacedtiles()));
		}
		return this.f-o.f;
	}
}