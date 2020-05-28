
import compiler.csvBaseListener;
import compiler.csvParser;
import compiler.csvParser.LineContext;

public class CSVPrintListener extends csvBaseListener {

	@Override
	public void enterFile(csvParser.FileContext ctx) {
		System.out.print("File: ");
		System.out.println("Lines: " + ctx.line().size());
	}

	@Override
	public void enterLine(LineContext ctx) {
		System.out.print("Line: ");
		System.out.println(ctx.Num(0) + " " + ctx.Num(1) + " " + ctx.Num(1) + " " + ctx.Txt() + " " + ctx.Num(2));
	}

}
