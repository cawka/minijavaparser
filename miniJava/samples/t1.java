class TestContexts {
	public static void main( String[] preved )
	{
		System.out.println( new A().recombine( ) );
	}
}

class x {
}

class y extends x {
}

class A {
	public int recombine( )
	{
	    x test;
	    x test2;
	    
	    test=this.sintesis( new x() );
	    
	    test2=this.sintesis( new y() );
	    
	    return 0;
	}
	
	public x sintesis( x who_am_i )
	{
		return who_am_i;
	}
}
