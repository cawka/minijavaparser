class TestContexts {
	public static void main( String[] preved )
	{
		System.out.println( new A().recombine( ) );
	}
}

class A {
	public int recombine( )
	{
		atom var;
		
		var=this.sintesis( new atom() );
		var=this.join( new molecule() );
		
		return 1;
	}
	
	public atom sintesis( atom var )
	{
		atom tmp;
		
		tmp=this.join( new organism() );
		
		return var;
	}
	
	public atom join( atom var )
	{
		boolean temp;
		atom tmp2;
		
		if( 1<0 ) tmp2=this.sintesis( var ); else { }
		temp=this.group( new organism() );
		temp=this.awake( var );
		return var;
	}
	
	public boolean group( atom var )
	{
		boolean test;
		atom who_am_i;
		
		test=this.awake( var );
		who_am_i=this.sintesis( new cell() );
		return true;
	}

	public boolean awake( atom var )
	{
		return true;
	}
}

class atom {
}

class molecule extends atom {
}

class cell extends molecule {
}

class organism extends cell {
}
