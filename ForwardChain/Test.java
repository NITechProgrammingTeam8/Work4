import java.util.ArrayList;

/*
 * Presenter動作確認用ファイル
 * Viewを上手く利用できていない印象
 */

class Test {
	public static void main(String arg[]){
		Presenter presenter = new Presenter(new View());
		ArrayList<String> firstAssertions = new ArrayList<>();
		String fileName = "CarShop2.data";
		firstAssertions.add("my-car is inexpensive");
    	firstAssertions.add("my-car has a VTEC engine");
    	firstAssertions.add("my-car is stylish");
    	firstAssertions.add("my-car has several color models");
    	firstAssertions.add("my-car has several seats");
    	firstAssertions.add("my-car is a wagon");
		presenter.start(firstAssertions, fileName);

		System.out.println();
		ArrayList<StepResult> stepresults = presenter.stepResult();
		for (StepResult stepresult : stepresults) {
			ArrayList<Assertion> add = stepresult.getAdd();
			Rule apply = stepresult.getApply();
			Assertion success = stepresult.getSuccess();
			//System.out.println("add size:" + add.size());
			//System.out.println("apply size:" + apply.size());
			//System.out.println("success size:" + success.size());
			if (add.size() > 0) {
				for (Assertion addunit : add) {
					System.out.println("◆ADD: " + addunit.getName());
				}
			} else {
				System.out.println("◆ADD: No data");
			}
			if (apply != null) {
				System.out.println("◇apply rule: " + apply.getName());
			} else {
				System.out.println("◇apply rule: No rule");
			}
			if (success != null) {
				System.out.println("◆success: " + success.getName());
			} else {
				System.out.println("◆success: No data");
			}
			System.out.println();
		}

		// とりあえず要素一つで複数検索はしない
		ArrayList<String> targetData = new ArrayList<>();
		targetData.add("Is my car inexpensive ?");
		ArrayList<ArrayList<String>> resultLists = presenter.searchAssertion(targetData);
		for (int i = 0; i < resultLists.size(); i++) {
			System.out.println("検索質問：" + targetData.get(i));
			for (int k = 0; k < resultLists.get(i).size(); k++) {
				System.out.println("答え = " + resultLists.get(i).get(k));
			}
			System.out.println();
		}
		targetData = new ArrayList<>();
		targetData.add("What does my car have ?");
		resultLists = presenter.searchAssertion(targetData);
		for (int i = 0; i < resultLists.size(); i++) {
			System.out.println("検索質問：" + targetData.get(i));
			for (int k = 0; k < resultLists.get(i).size(); k++) {
				System.out.println("答え = " + resultLists.get(i).get(k));
			}
			System.out.println();
		}

		// ルールの一覧を取得
		// IDの動きが微妙
		ArrayList<Rule> ruleLists = presenter.fetchRules();
		for (Rule ruleList : ruleLists) {
			System.out.println("ruleName:" + ruleList.getName());
			for (String antecedent : ruleList.getAntecedents()) {
				System.out.println("antecedentName:" + antecedent);
			}
			System.out.println("ruleConsequent:" + ruleList.getConsequent());
			System.out.println("ruleid:" + ruleList.getId());
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
        // IDの動きが微妙
        ruleLists = presenter.fetchRules();
        for (Rule ruleList : ruleLists) {
        	System.out.println("ruleName:" + ruleList.getName());
        	for (String antecedent : ruleList.getAntecedents()) {
        		System.out.println("antecedentName:" + antecedent);
        	}
        	System.out.println("ruleConsequent:" + ruleList.getConsequent());
        	System.out.println("ruleid:" + ruleList.getId());
        	System.out.println();
        }

        // ルール更新テスト
        // ルール名は編集しないことが前提
        Rule update = ruleLists.get(ruleLists.size() - 1);
        update.getAntecedents().set(2, "?x is wide");
        presenter.updateRule(update);

        // ルールの一覧を取得
        // IDの動きが微妙(削除したid番号を再び使うことは出来ない)
        ruleLists = presenter.fetchRules();
        for (Rule ruleList : ruleLists) {
        	System.out.println("ruleName:" + ruleList.getName());
        	for (String antecedent : ruleList.getAntecedents()) {
        		System.out.println("antecedentName:" + antecedent);
        	}
        	System.out.println("ruleConsequent:" + ruleList.getConsequent());
        	System.out.println("ruleid:" + ruleList.getId());
        	System.out.println();
        }

        // ルール削除テスト
        presenter.deleteRule(ruleLists.get(ruleLists.size() - 1)); // 挿入したものを削除
        presenter.deleteRule(ruleLists.get(ruleLists.size() - 2)); // 消去後データの後ろから2番目削除

        // ルールの一覧を取得
        // IDの動きが微妙(削除したid番号を再び使うことは出来ない)
        ruleLists = presenter.fetchRules();
        for (Rule ruleList : ruleLists) {
        	System.out.println("ruleName:" + ruleList.getName());
        	for (String antecedent : ruleList.getAntecedents()) {
        		System.out.println("antecedentName:" + antecedent);
        	}
        	System.out.println("ruleConsequent:" + ruleList.getConsequent());
        	System.out.println("ruleid:" + ruleList.getId());
        	System.out.println();
        }

		ArrayList<String> Assertions = new ArrayList<>();
		Assertions.add("my-car is inexpensive");
    	Assertions.add("my-car has a VTEC engine");
    	Assertions.add("my-car is stylish");
    	Assertions.add("my-car has several color models");
    	//Assertions.add("my-car has several seats");
    	//Assertions.add("my-car is a wagon");
		presenter.restart(Assertions);

		System.out.println();
		ArrayList<StepResult> stepresults2 = presenter.stepResult();
		for (StepResult stepresult : stepresults2) {
			ArrayList<Assertion> add = stepresult.getAdd();
			Rule apply = stepresult.getApply();
			Assertion success = stepresult.getSuccess();

			if (add.size() > 0) {
				for (Assertion addunit : add) {
					System.out.println("【再】◆ADD: " + addunit.getName());
				}
			} else {
				System.out.println("【再】◆ADD: No data");
			}
			if (apply != null) {
				System.out.println("【再】◇apply rule: " + apply.getName());
			} else {
				System.out.println("【再】◇apply rule: No rule");
			}
			if (success != null) {
				System.out.println("【再】◆success: " + success.getName());
			} else {
				System.out.println("【再】◆success: No data");
			}
			System.out.println();
		}

		targetData = new ArrayList<>();
		targetData.add("What does my car have ?");
		resultLists = presenter.searchAssertion(targetData);
		for (int i = 0; i < resultLists.size(); i++) {
			System.out.println("検索質問：" + targetData.get(i));
			for (int k = 0; k < resultLists.get(i).size(); k++) {
				System.out.println("答え = " + resultLists.get(i).get(k));
			}
			System.out.println();
		}
	}

	// いまできること
	/*
	 * 検索結果に対する推論手順の出力以外は完了しているはず
	 */
}