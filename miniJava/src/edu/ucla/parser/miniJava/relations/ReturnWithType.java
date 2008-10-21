/**
 * 
 */
package edu.ucla.parser.miniJava.relations;

import edu.ucla.parser.miniJava.symbols.GeneralSymbol;
import edu.ucla.parser.miniJava.symbols.Variable;

public class ReturnWithType extends UnknownRelation
{
	public GeneralSymbol _s1;
	public Variable _s2;

	public ReturnWithType( GeneralSymbol s1, Variable s2 )
	{
		_s1=s1;
		_s2=s2;
	}
	
	public String toRelation( ) throws NoRelation
	{
		if( _s2._type==null ) throw new NoRelation();
		return _s1._name._line+" "+_s2._name._line+" "+_s2._type._name._line;
	}
	
	public void dump( )
	{
		System.out.println( _s1._name+" / "+_s2._name+" / "+((_s2._type!=null)?_s2._type._name:"NULL") );
	}
}