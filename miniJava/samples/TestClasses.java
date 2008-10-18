class TestClasses {
	public static void main( String[] preved )
	{
		System.out.println( new Bear().eat() );
//		System.out.println( new G().m() );
	}
}
//
//class A {
//}
//
//class B extends A {
//	public int m( )
//	{
//		return 1;
//	}
//}
//
//class C extends B {
//	public int m( )
//	{
//		return 1;
//	}
//}
//
//class D extends C {
//	
//}
//
//class E extends D {
//	public int m()
//	{
//		return 1;
//	}
//}
//
//class F extends E {
//	
//}
//
//class G extends F {
//
//}

class Bear extends Animal
{
	public int eat( )
	{
		Food dinner;
		FlyingBird bird;
		boolean still_hungry;
		//_needfood=2;
		
		dinner=new Hummingbird( );
		still_hungry=!this.chew( dinner );
		
		///////////////////////  //a1: [[bird]]=={new Hummingbird,new Bird}
		bird=dinner;
		///////////////////////  //a2: [[bird]]=={new Hummingbird}
		
		while( still_hungry ) //let's assume, that new can sometimes return NULL.
		{
			//a1,a2 adds to set {new Bird, new Bear, new Animal}
			//a3 adds to set {new Bird, new Animal}
			dinner=this.search_for_food( );
			still_hungry=this.chew( dinner );
		}

		return 0;
	}
	
	public boolean chew( Food food )
	{
		return false;
	}
}

class Animal extends Food
{
	int  _needfood;
	
	public boolean chew( Food food )
	{
		boolean retval;
		_needfood=_needfood-1;
		
		if( 1<_needfood )
		{
			retval=false;
		}
		else
		{
			retval=true;
		}
		
		return retval;
	}
	
	public Food search_for_food( )
	{
		return new Bird( );
	}
}


class Hummingbird extends FlyingBird
{
}

class FlyingBird extends Bird
{
}

class Dinosaur extends Bird
{
	public Food search_for_food( )
	{
		return new Bear( );
	}
}


class Bird extends Food
{
}

class Food
{
	public Food search_for_food( )
	{
		return new Animal( );
	}

	public boolean chew( Food food )
	{
		return true;
	}
}
