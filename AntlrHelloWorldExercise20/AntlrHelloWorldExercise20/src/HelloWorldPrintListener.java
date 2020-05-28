import compiler.HelloWorldBaseListener;
import compiler.HelloWorldParser;

public class HelloWorldPrintListener extends HelloWorldBaseListener {

	public void enterGreeting(HelloWorldParser.GreetingContext ctx) {
		System.out.print("Greeting: ");
		System.out.println(ctx.ID());
	}

}
