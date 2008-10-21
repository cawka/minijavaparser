/**
 * 
 */
package edu.ucla.parser.miniJava.relations;

public class UnknownRelation 
	implements Comparable<UnknownRelation>,
			   PrintRelation
{
	public int compareTo(UnknownRelation o) {
		return 1;
	}
	
	public String toRelation( ) throws NoRelation
	{
		return toString( );
	}

	public String toRelationDot() throws NoRelation {
		return toRelation( );
	}
}