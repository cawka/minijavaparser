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
import edu.ucla.parser.miniJava.symbols.Function;

public class GraphNode 
		implements Comparable
{
	public Map<String,String> _name=new TreeMap<String,String>();
	public CallsMap           _calls=new CallsMap();
	
	public List<Function> _methods=new  LinkedList<Function>();
	
	public List<GraphNode> _out_s=new LinkedList<GraphNode>(); 
	public List<GraphNode> _in_s =new LinkedList<GraphNode>();
	public List<GraphNode> _out  =new LinkedList<GraphNode>();
	public List<GraphNode> _in   =new LinkedList<GraphNode>();
	
	int _context_start=1; //assume, that all are static contexts
	public int _context_num=1;
	
	boolean _visited=false;
	
	public GraphNode( String name )
	{
		_name.put( name, name );
	}
	
	public GraphNode group( GraphNode n )
	{
		_name.putAll( n._name );
		_calls.addAll( n._calls );
		_methods.addAll( n._methods );
		return this;
	}
	
	// method=where, invoke=on what line, node=what
	public GraphNode addEdge( Function method, Call invoke, String node )
	{
		GraphNode n=CallGraph._all_nodes.get( node );
		n._calls.add( method._name.getName(),invoke );
		_methods.add( method );
		_out_s.add( n );
		n._in_s.add( this );
		return this;
	}
	
	public String getName( )
	{
		String name="";
		for( Iterator<String> i=_name.values().iterator(); i.hasNext(); )
			name=name+(name.equals("")?"":",")+i.next();
		return "\""+name+" ("+_context_num+")\"";
	}
	
	public String getFirstName( )
	{
		return _name.values().iterator().next();
	}
	
	public String toString( )
	{
		String ret="";
		for( Iterator<GraphNode> i=_out.iterator(); i.hasNext(); )
			ret=ret+getName()+" -> "+(ret.equals("")?"":" ")+i.next().getName( )+"\n";

		String t="";
		for( Iterator<GraphNode> i=_out_s.iterator(); i.hasNext(); )
			t=t+(ret.equals("")?"":" ")+i.next().getName( )+"\n";

//			return getName()+" -> { " + ret + " }";
		ret= ret+"//"+getName()+" -> "+t+"\n";
		ret+=_calls;
		return ret;
	}
	
	public String toStringCycles( )
	{
		String ret="";
		for( Iterator<GraphNode> i=_out_s.iterator(); i.hasNext(); )
			ret=ret+getName()+" -> "+(ret.equals("")?"":" ")+i.next().getName( )+"\n";

		return ret;
	}

	public static void removeAll( List<GraphNode> list, GraphNode node )
	{
		boolean full=true; 
		while( full )  //remove all this occurrences
		{ 
			full=list.remove( node ); 
		}
	}
	
	public void moveNonCycleEdges( GraphNode next )
	{
		next._in.addAll( _in );
		next._out.addAll( _out );
		
		for( Iterator<GraphNode> i=_in.iterator(); i.hasNext(); )
		{
			GraphNode n=i.next();
			removeAll( n._out, this );
			n._out.add( next );
		}

		for( Iterator<GraphNode> i=_out.iterator(); i.hasNext(); )
		{
			GraphNode n=i.next();
			removeAll( n._in, this );
			n._in.add( next );
		}
	}
	
	public GraphNode collapseIn( GraphNode next )
	{
		next.group( this );
		removeAll( next._in_s, this );
		next._in_s.addAll( _in_s ); removeAll( next._in_s, next ); 

		moveNonCycleEdges( next );

		return next;
	}

	public GraphNode collapse( )
	{
		GraphNode ret=null;

		for( Iterator<GraphNode> i=_out_s.iterator(); i.hasNext(); )
		{
			ret=CallGraph._all_nodes.get( i.next( ).getFirstName() );
			collapseIn( ret );
			for( Iterator<String> s=_name.values().iterator(); s.hasNext(); )
				CallGraph.replaceNode( s.next(), ret );
			//return null;
			removeFromAllCycles( this );
		}
		if( ret!=null ) { replateList( ret ); ret=null; }
		
		return null;
	}
	
	public static void removeFromAllCycles( GraphNode node )
	{
		for( Iterator<GraphNode> i=CallGraph._all_nodes_list.iterator(); i.hasNext(); )
		{
			GraphNode n=i.next( );
			removeAll( n._in_s, node );
			removeAll( n._out_s, node );
		}
	}
	
	public void replateList( GraphNode ret )
	{
		CallGraph._all_nodes_list.remove( this ); // node was collapsed
		// replace all references to this in _in_s and _out_s rules
		// it doesn't matter what ret node will be. All cycles will be collapsed to single node at the end
		
		for( Iterator<GraphNode> i=CallGraph._all_nodes_list.iterator(); i.hasNext(); )
		{
			GraphNode n=i.next( );
			if( !n.equals(ret) )
			{
				replaceInList( n._in_s, this, ret );
				replaceInList( n._out_s, this, ret );
			}
			else
			{
				removeAll( n._in_s, this );
				removeAll( n._out_s, this );
			}
		}
	}
	
	public void replaceInList( List<GraphNode> list, GraphNode invalid, GraphNode valid )
	{
		if( list.contains(invalid) )
		{
			removeAll( list, invalid );
			list.add( valid );
		}
	}

	public boolean removeNonCycles( )
	{
		if( _out_s.size()==0 && _in_s.size()==0 ) return false;
		if( _out_s.size()==0 )
		{
			_in.addAll( _in_s );
			for( Iterator<GraphNode> i=_in_s.iterator(); i.hasNext(); ) i.next( ).notOutCycle( this );
			_in_s.clear( );
			return true;
		}
		if( _in_s.size()==0 )
		{
			_out.addAll( _out_s );
			for( Iterator<GraphNode> i=_out_s.iterator(); i.hasNext(); ) i.next( ).notInCycle( this );
			_out_s.clear( );
			return true;
		}
		return false; //>0 input and >0 output edges. Can't say anything according to algorithm
	}
	
	public void notOutCycle( GraphNode node )
	{
		removeAll( _out_s, node );
		_out.add( node );
	}
	
	public void notInCycle( GraphNode node )
	{
		removeAll( _in_s, node );
		_in.add( node );
	}

	public int compareTo(Object o) {
		boolean equal=true;
		Iterator<String> i1=_name.values().iterator();
		Iterator<String> i2=((GraphNode)o)._name.values().iterator();
		for( ; i1.hasNext() && i2.hasNext(); )
		{
			String s1=i1.next();
			String s2=i2.next();
			int compare=s1.compareTo( s2 );
			if( compare!=0 ) return compare;
		}
		return i1.hasNext()==i2.hasNext() ?0:1;
	}
	
	public void calculateContexts( )
	{
		if( _visited ) return;
		int number=0;
		for( Iterator<GraphNode> i=_in.iterator(); i.hasNext(); )
		{
			number+=i.next()._context_num;
		}
		if( number==0 ) number=1;
		_context_num=number;
//			_visited=true;
		
		for( Iterator<GraphNode> i=_out.iterator(); i.hasNext(); )
		{
			i.next().calculateContexts( );
		}
	}
}
