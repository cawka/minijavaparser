/**
 * 
 */
package edu.ucla.parser.miniJava.callgraph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.ucla.parser.miniJava.symbols.Call;

public class CallsMap
{
	public Map<String,List<Call>> _calls=new TreeMap<String,List<Call>>();
	
	public List<Call> add( String caller, Call call )
	{
		List<Call> calls=_calls.get( caller );
		if( calls==null )
		{
			calls=new LinkedList<Call>();
			_calls.put( caller, calls );
		}
		
		if( !CallGraph.find(calls,call) ) calls.add( call );
		return calls;
	}
	
	public boolean isEmpty( )
	{
		return _calls.isEmpty();
	}
	
	public List<Call> addAll( String caller, List<Call> src_calls )
	{
		List<Call> calls=_calls.get( caller );
		if( calls==null )
		{
			calls=new LinkedList<Call>();
			_calls.put( caller, calls );
		}
		
		calls.addAll( src_calls );
		return calls;
	}
	
	public List<Call> get( String caller )
	{
		return _calls.get( caller );
	}
	
	public void addAll( CallsMap src )
	{
		for( Iterator<String> i=src._calls.keySet().iterator(); i.hasNext(); )
		{
			String key=i.next( );
			addAll( key, src.get(key) );
		}
	}
	
	public String toString( )
	{
		String ret="";
		for( Iterator<String> i=_calls.keySet().iterator(); i.hasNext(); )
		{
			String caller=i.next();
			for( Iterator<Call> call=_calls.get(caller).iterator(); call.hasNext(); )
			{
				ret+="//	("+caller+") :: "+call.next()+"\n";
			}
		}
		return ret;
	}
}