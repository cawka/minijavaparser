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
		
		tmp=this.join( new atom() );
		
		return var;
	}
	
	public atom join( atom var )
	{
		boolean temp;
		atom tmp2;
		
		if( 1<0 ) tmp2=this.sintesis( var ); else { }
		temp=this.group( new molecule() );
		temp=this.awake( new organism() );
		return var;
	}
	
	public boolean group( atom var )
	{
		boolean test;
		atom temp;
		
		test=this.awake( var );
		temp=this.sintesis( new cell() );
		return true;
	}

	public boolean awake( atom var )
	{
		System.out.println( var.log_count() );
		return true;
	}
}

class atom {
	public int log_count( ) { return 24; }
}

class molecule extends atom {
	public int log_count( ) { return 23; }
}

class cell extends molecule {
}

class organism extends cell {
	public int log_count( ) { return 0; }
}
