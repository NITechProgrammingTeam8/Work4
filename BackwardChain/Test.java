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
		// ルールの一覧を取得
		ArrayList<Rule> ruleLists = presenter.fetchRules();
		for (Rule ruleList : ruleLists) {
			System.out.println("ruleName:" + ruleList.getName());
			for (String antecedent : ruleList.getAntecedents()) {
				System.out.println("antecedentName:" + antecedent);
			}
			System.out.println("ruleConsequent:" + ruleList.getConsequent());
			System.out.println();
		}

		//String target = "Is his car inexpensive ?";
		String target = "What is an Accord Wagon ?";
		//target = "Is his car stylish ?";
		// target = "What does his car have ?"; // 質問そのものの推論ができない？
		ArrayList<SearchStep> stepresults = presenter.stepResults(wmname, target);
		System.out.println("【出力結果】");
		for (SearchStep stepresult : stepresults) {
			if (stepresult.getKekka() != null) {
				System.out.println("Answer: " + stepresult.getKekka());
				ArrayList<StepResult> keiro = stepresult.getKeiro();
				for (StepResult keirounit : keiro) {
					ArrayList<StepResult> addLink = keirounit.getAddSR();
					Rule AF = keirounit.getAnswerField();
					String A = keirounit.getAnswer();
					if (addLink != null) {
						for (StepResult addunit : addLink) {
							if (addunit.getAnswerField() != null) {
								System.out.println("◇Link rule: " + addunit.getAnswerField());
							} else {
								System.out.println("◇Link rule: No Link(Data is WM)");
							}
							System.out.println("◆Link: " + addunit.getAnswer());
						}
					} else {
						System.out.println("◆Link: No Link(Data is WM)");
					}
					if (AF != null) {
						System.out.println("□Answer Field: " + AF.getName());
					} else {
						System.out.println("□Answer Field: No rule");
					}
					System.out.println("■Answer: " + A);
					System.out.println();
				}
			} else {
				System.out.println("Answer: No matching");
			}
		}


		//targetData.add("Is my car inexpensive ?");
		target = "Is his car inexpensive ?";
		stepresults = presenter.stepResults(wmname, target);
		System.out.println("【出力結果】");
		for (SearchStep stepresult : stepresults) {
			if (stepresult.getKekka() != null) {
				System.out.println("Answer: " + stepresult.getKekka());
				ArrayList<StepResult> keiro = stepresult.getKeiro();
				for (StepResult keirounit : keiro) {
					ArrayList<StepResult> addLink = keirounit.getAddSR();
					Rule AF = keirounit.getAnswerField();
					String A = keirounit.getAnswer();
					if (addLink != null) {
						for (StepResult addunit : addLink) {
							if (addunit.getAnswerField() != null) {
								System.out.println("◇Link rule: " + addunit.getAnswerField());
							} else {
								System.out.println("◇Link rule: No Link(Data is WM)");
							}
							System.out.println("◆Link: " + addunit.getAnswer());
						}
					} else {
						System.out.println("◆Link: No Link(Data is WM)");
					}
					if (AF != null) {
						System.out.println("□Answer Field: " + AF.getName());
					} else {
						System.out.println("□Answer Field: No rule");
					}
					System.out.println("■Answer: " + A);
					System.out.println();
				}
			} else {
				System.out.println("Answer: No matching");
			}
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

	}
}