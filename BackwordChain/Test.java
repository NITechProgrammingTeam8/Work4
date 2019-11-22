import java.util.ArrayList;

/*
 * Presenter動作確認用ファイル
 * Viewを上手く利用できていない
 */

class Test {
	public static void main(String arg[]){
		Presenter presenter = new Presenter(new View());
		String filename = "CarShop.data";
		String wmname = "CarShopWm.data";
		String target = "?x is an Accord Wagon";
		
		ArrayList<StepResult> stepresults = presenter.stepResults(filename, wmname, target);
		for (StepResult stepresult : stepresults) {
			Rule QF = stepresult.getQF();
			String Q = stepresult.getQ();
			Rule AF = stepresult.getAF();
			String A = stepresult.getA();
			if (QF != null) {
				System.out.print("◇QuestionField: " + QF.getName());
			} else {
				System.out.print("◇QuestionField: Target Question(No Field)");
			}
			System.out.println("  ◆Question: " + Q);
			if (AF != null) {
				System.out.print("◇AnswerField: " + AF.getName());
			} else {
				System.out.print("◇AnswerField: WorkingSpace");
			}
			System.out.println("  ◆Answer: " + A);
			System.out.println();
		}

		target = "?x is an Accord Wagon,?y is a Ferrari F50";
		stepresults = new ArrayList<>();
		stepresults = presenter.reStepResults(wmname, target);
		for (StepResult stepresult : stepresults) {
			Rule QF = stepresult.getQF();
			String Q = stepresult.getQ();
			Rule AF = stepresult.getAF();
			String A = stepresult.getA();
			if (QF != null) {
				System.out.print("【再】◇QuestionField: " + QF.getName());
			} else {
				System.out.print("【再】◇QuestionField: Target Question(No Field)");
			}
			System.out.println("◆Question: " + Q);
			if (AF != null) {
				System.out.print("【再】◇AnswerField: " + AF.getName());
			} else {
				System.out.print("【再】◇AnswerField: WorkingSpace");
			}
			System.out.println("◆Answer: " + A);
			System.out.println();
		}

	}
}