class TestContexts {
	public static void main( String[] preved )
	{
		System.out.println( new A().m1() );
	}
}

class A {
	public boolean m1( )
	{
		boolean test;
		
		test=this.m2();
		test=this.m3();
		return true;
	}

	public boolean m2( )
	{
		boolean test;
		
		test=this.m3();
		test=this.m4();
		return true;
	}

	public boolean m3( )
	{
		boolean test;
		
		test=this.m2();
		test=this.m4();
		test=this.m5();
		return true;
	}

	public boolean m4( )
	{
		boolean test;
		
		test=this.m6();
		return true;
	}

	public boolean m5( )
	{
		boolean test;
		
		test=this.m6();
		return true;
	}

	public boolean m6( )
	{
		return true;
	}
}