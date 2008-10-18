import java.io.PrintStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CallGraph 
{
	public static Map<String,Node> _all_nodes=new TreeMap<String,Node>();
	public static List<Node> _all_nodes_list=new LinkedList<Node>();
	
	public static void replaceNode( String name,Node n )
	{
		_all_nodes.put( name, n );		
	}
	
//	public CallGraph( )
//	{
//		_all_nodes.put( "1", new Node("1") );
//		_all_nodes.put( "2", new Node("2") );
//		_all_nodes.put( "3", new Node("3") );
//		_all_nodes.put( "4", new Node("4") );
//		_all_nodes.put( "5", new Node("5") );
//		_all_nodes.put( "6", new Node("6") );
//		
//		_all_nodes.get( "1" ).addEdge( "2" )
//							 .addEdge( "3" );
//		_all_nodes.get( "2" ).addEdge( "3" )
//							 .addEdge( "5" );
//		_all_nodes.get( "3" ).addEdge( "2" )
//							 .addEdge( "4" )
//							 .addEdge( "5" )
//							 .addEdge( "6" );
//		_all_nodes.get( "4" ).addEdge( "3" );
//	}
	
	static class CallsMap
	{
		public Map<String,List<Symbols.Call>> _calls=new TreeMap<String,List<Symbols.Call>>();
		
		public List<Symbols.Call> add( String caller, Symbols.Call call )
		{
			List<Symbols.Call> calls=_calls.get( caller );
			if( calls==null )
			{
				calls=new LinkedList<Symbols.Call>();
				_calls.put( caller, calls );
			}
			
			calls.add( call );
			return calls;
		}
		
		public boolean isEmpty( )
		{
			return _calls.isEmpty();
		}
		
		public List<Symbols.Call> addAll( String caller, List<Symbols.Call> src_calls )
		{
			List<Symbols.Call> calls=_calls.get( caller );
			if( calls==null )
			{
				calls=new LinkedList<Symbols.Call>();
				_calls.put( caller, calls );
			}
			
			calls.addAll( src_calls );
			return calls;
		}
		
		public List<Symbols.Call> get( String caller )
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
				for( Iterator<Symbols.Call> call=_calls.get(caller).iterator(); call.hasNext(); )
				{
					ret+="//	("+caller+") :: "+call.next()+"\n";
				}
			}
			return ret;
		}
	}
	
	static class Node 
		implements Comparable
	{
		public Map<String,String> _name=new TreeMap<String,String>();
		public CallsMap _calls=new CallsMap();
		
		public List<Symbols.Function> _methods=new  LinkedList<Symbols.Function>();
		
		public List<Node> _out_s=new LinkedList<Node>(); 
		public List<Node> _in_s =new LinkedList<Node>();
		public List<Node> _out  =new LinkedList<Node>();
		public List<Node> _in   =new LinkedList<Node>();
		
		int _context_start=1; //assume, that all are static contexts
		int _context_num=1;
		
		boolean _visited=false;
		
		public Node( String name )
		{
			_name.put( name, name );
		}
		
		public Node group( Node n )
		{
			_name.putAll( n._name );
			_calls.addAll( n._calls );
			_methods.addAll( n._methods );
			return this;
		}
		
		// method=where, invoke=on what line, node=what
		public Node addEdge( Symbols.Function method, Symbols.Call invoke, String node )
		{
			Node n=_all_nodes.get( node );
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
		
		public String toString( )
		{
			String ret="";
			for( Iterator<Node> i=_out.iterator(); i.hasNext(); )
				ret=ret+getName()+" -> "+(ret.equals("")?"":" ")+i.next().getName( )+"\n";

			String t="";
			for( Iterator<Node> i=_out_s.iterator(); i.hasNext(); )
				t=t+(ret.equals("")?"":" ")+i.next().getName( )+"\n";

//			return getName()+" -> { " + ret + " }";
			ret= ret+"//"+getName()+" -> "+t+"\n";
			ret+=_calls;
			return ret;
		}
		
		public static void removeAll( List<Node> list, Node node )
		{
			boolean full=true; 
			while( full )  //remove all this occurrences
			{ 
				full=list.remove( node ); 
			}
		}
		
		public void moveNonCycleEdges( Node next )
		{
			next._in.addAll( _in );
			next._out.addAll( _out );
			
			for( Iterator<Node> i=_in.iterator(); i.hasNext(); )
			{
				Node n=i.next();
				removeAll( n._out, this );
				n._out.add( next );
			}

			for( Iterator<Node> i=_out.iterator(); i.hasNext(); )
			{
				Node n=i.next();
				removeAll( n._in, this );
				n._in.add( next );
			}
		}
		
		public Node collapseIn( Node next )
		{
			next.group( this );
			removeAll( next._in_s, this );
			next._in_s.addAll( _in_s ); removeAll( next._in_s, next ); 

			moveNonCycleEdges( next );

			return next;
		}

		public Node collapseOut( Node next )
		{
			next.group( this );
			removeAll( next._out_s, this );
			next._out_s.addAll( _out_s ); removeAll( next._out_s, next );

			moveNonCycleEdges( next );
			return next;
		}
		
		public Node collapse( )
		{
			Node ret=null;

			for( Iterator<Node> i=_out_s.iterator(); i.hasNext(); )
			{
				ret=i.next( );
				collapseIn( ret );
				for( Iterator<String> s=_name.values().iterator(); s.hasNext(); )
					replaceNode( s.next(), ret );
				//return null;
				removeAll( _in_s, ret );
			}
			
			for( Iterator<Node> i=_in_s.iterator(); i.hasNext(); )
			{
				ret=i.next();
				collapseOut( ret );
				for( Iterator<String> s=_name.values().iterator(); s.hasNext(); )
					replaceNode( s.next(), ret );
				removeAll( _in_s, ret );
			}
			if( ret!=null )	
			{
				_all_nodes_list.remove( this ); // node was collapsed
				// replace all references to this in _in_s and _out_s rules
				// it doesn't matter what ret node will be. All cycles will be collapsed to single node at the end
				
				for( Iterator<Node> i=_all_nodes_list.iterator(); i.hasNext(); )
				{
					Node n=i.next( );
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
			
			return null;
		}
		
		public void replaceInList( List<Node> list, Node invalid, Node valid )
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
				for( Iterator<Node> i=_in_s.iterator(); i.hasNext(); ) i.next( ).notOutCycle( this );
				_in_s.clear( );
				return true;
			}
			if( _in_s.size()==0 )
			{
				_out.addAll( _out_s );
				for( Iterator<Node> i=_out_s.iterator(); i.hasNext(); ) i.next( ).notInCycle( this );
				_out_s.clear( );
				return true;
			}
			return false; //>0 input and >0 output edges. Can't say anything according to algorithm
		}
		
		public void notOutCycle( Node node )
		{
			removeAll( _out_s, node );
			_out.add( node );
		}
		
		public void notInCycle( Node node )
		{
			removeAll( _in_s, node );
			_in.add( node );
		}

		public int compareTo(Object o) {
			boolean equal=true;
			Iterator<String> i1=_name.values().iterator();
			Iterator<String> i2=((Node)o)._name.values().iterator();
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
			for( Iterator<Node> i=_in.iterator(); i.hasNext(); )
			{
				number+=i.next()._context_num;
			}
			if( number==0 ) number=1;
			_context_num=number;
//			_visited=true;
			
			for( Iterator<Node> i=_out.iterator(); i.hasNext(); )
			{
				i.next().calculateContexts( );
			}
		}
		
	}

	public void findCycles( )
	{
		boolean cycles;
		do
		{
			cycles=false;
			
			for( Iterator<Node> i=_all_nodes.values().iterator(); i.hasNext(); )
			{
				cycles|=i.next().removeNonCycles( );
			}
		} while( cycles );
	}

	public void dumpGraph( PrintStream os )
	{
		os.println( "digraph TheGraph {" ); 
		for( Iterator<Node> i=_all_nodes_list.iterator(); i.hasNext(); )
		{
			os.println( i.next() );
		}
		os.println( "}" );
	}
	
	public void collapse( )
	{
		_all_nodes_list.addAll( _all_nodes.values() );
		
		for( Iterator<Node> i=_all_nodes.values().iterator(); i.hasNext(); )
		{
			i.next().collapse( );
		}
	}
}
