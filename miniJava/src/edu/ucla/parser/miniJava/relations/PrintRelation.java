package edu.ucla.parser.miniJava.relations;

public interface PrintRelation {
	public String toRelation( ) throws NoRelation;
	public String toRelationDot( ) throws NoRelation;
}
