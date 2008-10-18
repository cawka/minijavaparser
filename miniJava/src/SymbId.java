import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class SymbId 
	implements Comparable, Comparator
{
	public SymbId( final String name ) 
	{
		_scope.add( name );
	}
	
	public SymbId( final List<String> scope )
	{
		_scope.addAll( scope );
	}
	
	public SymbId( final SymbId parent, String name )
	{
		_scope.addAll( parent._scope );
		_scope.add( name );
	}
	
	public String getName( )
	{
		return _scope.get( _scope.size()-1 );
	}
	
	public String toString( )
	{
		String ret="";
		for( Iterator<String> i=_scope.iterator(); i.hasNext(); )
		{
			ret+=i.next();
			if( i.hasNext() ) ret+=".";
		}
		return ret+"("+_line+")";
	}
	
	public SymbId getPrevScope( )
	{
		SymbId ret=new SymbId( _scope );
		if( ret._scope.size()>0 ) ret._scope.remove( ret._scope.size()-1 );
		
		return ret;
	}
	
	public SymbId getScopeLevel( int level )
	{
		assert( level>=1 );
		assert( _scope.size()>=level );
		
		int i=0;
		SymbId ret=new SymbId( _scope.get(i++) );
		
		for( ; i<level; i++ ) ret._scope.add( _scope.get(i) );
		return ret;
	}
	
	static public List<String> newScope( final List<String> scope, final String add )
	{
		List<String> list=new LinkedList<String>(scope);
		list.add( add );
		return list;
	}
	
	public int compareTo(Object o) 
	{
		if( o instanceof SymbId )
		{
			SymbId s=(SymbId)o;
			int size=_scope.size();
			if( s._scope.size() < _scope.size() ) size=s._scope.size(); 
			Iterator<String> i1=_scope.iterator();
			Iterator<String> i2=s._scope.iterator();
			for( int i=0; i<size; i++ )
			{
				int ret=( i1.next()).compareTo( i2.next() );
				if( ret!=0 ) return ret;
			}
			if( s._scope.size() < _scope.size() ) 
				return 1;
			else if( s._scope.size() > _scope.size() )
				return -1;
			else
				return 0;
		}
		return 1;
	}
	
	public boolean equals( Object o )
	{
		if( o instanceof SymbId )
			return compareTo(o)==0;
		else
			return false;
	}

	public int compare(Object o1, Object o2) {
		if( o1 instanceof SymbId && o2 instanceof SymbId )
		{
			return ((SymbId)o1).compareTo( o2 );
		}
		return 1;
	}

	
	public List<String> _scope=new LinkedList<String>();
	public int _line; ///< Value will be set during the export process
}
