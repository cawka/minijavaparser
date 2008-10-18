
public class Relations	
{
	public static class Unknown 
		implements Comparable<Unknown>,
				   PrintRelation
	{
		public int compareTo(Unknown o) {
			return 1;
		}
		
		public String toRelation( ) throws NoRelation
		{
			return toString( );
		}
	}

	public static class TwoSymbols extends Unknown 
	{
		public Symbols.General _s1;
		public Symbols.General _s2;
		
		public TwoSymbols( Symbols.General s1, Symbols.General s2 )
		{
			_s1=s1;
			_s2=s2;
		}
		
		public String toString()
		{
			return _s1._name._line+" "+_s2._name._line;
		}
		
		public void dump( )
		{
			System.out.println( _s1._name+" = "+_s2._name );
		}
	}
	
	public static class ReturnWithType extends Unknown
	{
		public Symbols.General _s1;
		public Symbols.Variable _s2;
	
		public ReturnWithType( Symbols.General s1, Symbols.Variable s2 )
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
	
	public static class mI extends Unknown
	{
		public Symbols.Function _method;
		public Symbols.Call     _invocation;
		public String			_name;
	
		public mI( Symbols.Function method, Symbols.Call invocation, String name )
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
}
