import java.util.ArrayList;

/*
 * Presenter動作確認用ファイル
 * Viewを上手く利用できていない
 */

class Test {
	public static void main(String arg[]){
		Presenter presenter = new Presenter(new View());
		String filename = "CarShop2.data";
		String wmname = "CarShopWm.data";
		//ArrayList<String> targetData = new ArrayList<>();
		/*
		StringTokenizer st = new StringTokenizer(target,",");
		ArrayList<String> queries = new ArrayList<String>();
		for(int i = 0 ; i < st.countTokens();){
			queries.add(st.nextToken());
		}
		*/

		presenter.start(filename);
		// 初期ルールの一覧を取得
		ArrayList<Rule> ruleLists = presenter.fetchFirstRules();
		for (Rule ruleList : ruleLists) {
			System.out.println("ruleName:" + ruleList.getName());
			for (String antecedent : ruleList.getAntecedents()) {
				System.out.println("antecedentName:" + antecedent);
			}
			System.out.println("ruleConsequent:" + ruleList.getConsequent());
			System.out.println();
		}

		//targetData.add("Is my car inexpensive ?");
		String target = "Is his car inexpensive ?";
		ArrayList<StepResult> stepresults = presenter.stepResults(wmname, target);
		System.out.println("【出力結果】");
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

		// ルールの一覧を取得
		ruleLists = presenter.fetchRules();
		for (Rule ruleList : ruleLists) {
			System.out.println("ruleName:" + ruleList.getName());
			for (String antecedent : ruleList.getAntecedents()) {
				System.out.println("antecedentName:" + antecedent);
			}
			System.out.println("ruleConsequent:" + ruleList.getConsequent());
			System.out.println();
		}

		// ルール追加
		String ruleName = "CarRule16";
		ArrayList<String> antecedents = new ArrayList<>();
		antecedents.add("?x is a Toyota");
		antecedents.add("?x has several color models");
		antecedents.add("?x is quiet");
		String consequent = "?x is Crown";
		presenter.addRule(ruleName, antecedents, consequent);

		// ルールの一覧を取得
		ruleLists = presenter.fetchRules();
		for (Rule ruleList : ruleLists) {
			System.out.println("ruleName:" + ruleList.getName());
			for (String antecedent : ruleList.getAntecedents()) {
				System.out.println("antecedentName:" + antecedent);
			}
			System.out.println("ruleConsequent:" + ruleList.getConsequent());
			System.out.println();
		}

		// ルール更新テスト
		// ルール名は編集しないことが前提
		Rule update = ruleLists.get(ruleLists.size() - 1);
		update.getAntecedents().set(2, "?x is wide");
		presenter.updateRule(update);

		// ルールの一覧を取得
		ruleLists = presenter.fetchRules();
		for (Rule ruleList : ruleLists) {
			System.out.println("ruleName:" + ruleList.getName());
			for (String antecedent : ruleList.getAntecedents()) {
				System.out.println("antecedentName:" + antecedent);
			}
			System.out.println("ruleConsequent:" + ruleList.getConsequent());
			System.out.println();
		}

		// ルール削除テスト
		presenter.deleteRule(ruleLists.get(ruleLists.size() - 1)); // 挿入したものを削除
		presenter.deleteRule(ruleLists.get(ruleLists.size() - 2)); // 消去後データの後ろから2番目削除

		// ルールの一覧を取得
		ruleLists = presenter.fetchRules();
		for (Rule ruleList : ruleLists) {
			System.out.println("ruleName:" + ruleList.getName());
			for (String antecedent : ruleList.getAntecedents()) {
				System.out.println("antecedentName:" + antecedent);
			}
			System.out.println("ruleConsequent:" + ruleList.getConsequent());
			System.out.println();
		}

		target = "What is an Accord Wagon ?";
		stepresults = new ArrayList<>();
		stepresults = presenter.reStepResults(wmname, target);
		System.out.println("【出力結果】");
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