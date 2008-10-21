package edu.ucla.parser.miniJava.symbols;
import java.util.Map;
import java.util.TreeMap;


public class SymbList 
	implements Comparable
{
	Map<String,String> _list=new TreeMap<String,String>();
	
	public SymbList( String name )
	{
		_list.put( name, name );
	}
	
	public SymbList addGroup( String name )
	{
		_list.put( name, name );
		return this;
	}

	public int compareTo(Object o) 
	{
		boolean ret=false;
		if( o instanceof String )
		{
			ret=_list.get( (String)o )==null;
		}
//		else if
		return ret?0:1;
	}
	
}
