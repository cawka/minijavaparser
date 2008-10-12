
public class Relations	
{
	public static class Unknown 
		implements Comparable<Unknown>
	{
		public int compareTo(Unknown o) {
			return 1;
		}
	}

	public static class vP extends Unknown 
	{
		public Symbols.Variable _variable;
		public Symbols.Variable _heap;
		
		public vP( Symbols.Variable variable, Symbols.Variable heap )
		{
			_variable=variable;
			_heap=heap;
		}
		
		public String toString()
		{
			return _variable._name._line+" "+_heap._name._line;
		}
		
		public void dump( )
		{
			System.out.println( _variable._name+" = "+_heap._name );
		}
	}
}
