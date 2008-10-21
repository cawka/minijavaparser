/**
 * 
 */
package edu.ucla.parser.miniJava.relations;

import edu.ucla.parser.miniJava.MyNode;
import edu.ucla.parser.miniJava.symbols.Call;
import edu.ucla.parser.miniJava.symbols.Function;

public class mI extends UnknownRelation
{
	public Function _method;
	public Call     _invocation;
	public String			_name;

	public mI( Function method, Call invocation, String name )
	{
		_method=method;
		_invocation=invocation;
		_name=name;
	}

	public String toString()
	{
		return _method._name._line+" " + _invocation._name._line+" "+MyNode._method_names.get(_name)._name._line;
	}
	
	public void dump( )
	{
		System.out.println( _method._name+" / " + _invocation._name +" / "+_name );
	}

}