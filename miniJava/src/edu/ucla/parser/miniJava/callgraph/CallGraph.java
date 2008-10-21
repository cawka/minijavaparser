package edu.ucla.parser.miniJava.callgraph;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class CallGraph 
{
	public static Map<String,GraphNode> _all_nodes=new TreeMap<String,GraphNode>();
	public static List<GraphNode> _all_nodes_list=new LinkedList<GraphNode>();
	
	public static void replaceNode( String name,GraphNode n )
	{
		_all_nodes.put( name, n );		
	}
	
	public static boolean find( Collection list, Object obj )
	{
		for( Iterator<Object> i=list.iterator(); i.hasNext(); )
			if( obj.equals(obj) ) return true;
		
		return false;
	}
	
	public void findCycles( )
	{
		boolean cycles;
		do
		{
			cycles=false;
			
			for( Iterator<GraphNode> i=_all_nodes.values().iterator(); i.hasNext(); )
			{
				cycles|=i.next().removeNonCycles( );
			}
		} while( cycles );
	}

	public void dumpGraph( PrintStream os )
	{
		os.println( "digraph TheGraph {" ); 
		for( Iterator<GraphNode> i=_all_nodes_list.iterator(); i.hasNext(); )
		{
			os.println( i.next().toString() );
		}
		os.println( "}" );
	}
	
	public void dumpGraphCycles( PrintStream os )
	{
		os.println( "digraph TheGraph {" ); 
		for( Iterator<GraphNode> i=_all_nodes.values().iterator(); i.hasNext(); )
		{
			os.println( i.next().toStringCycles() );
		}
		os.println( "}" );
	}
	
	public void collapse( )
	{
		_all_nodes_list.addAll( _all_nodes.values() );
		
		for( Iterator<GraphNode> i=_all_nodes.values().iterator(); i.hasNext(); )
		{
			i.next().collapse( );
		}
	}
}
