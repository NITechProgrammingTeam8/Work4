import java.util.ArrayList;

/*
 * Presenter動作確認用ファイル
 * Viewを上手く利用できていない
 */

class Test {
	public static void main(String arg[]){
		Presenter presenter = new Presenter(new View());
		ArrayList<String> firstAssertions = new ArrayList<>();
		String fileName = "CarShop.data";
		firstAssertions.add("my-car is inexpensive");
    	firstAssertions.add("my-car has a VTEC engine");
    	firstAssertions.add("my-car is stylish");
    	firstAssertions.add("my-car has several color models");
    	firstAssertions.add("my-car has several seats");
    	firstAssertions.add("my-car is a wagon");
		presenter.start(firstAssertions, fileName);

		// 推論を順に探索した結果をクラスとして格納しリストで受け取る
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

		// データの再構築(本来はルールの編集後)
		ArrayList<String> Assertions = new ArrayList<>();
		Assertions.add("my-car is inexpensive");
    	Assertions.add("my-car has a VTEC engine");
    	Assertions.add("my-car is stylish");
    	Assertions.add("my-car has several color models");
    	//Assertions.add("my-car has several seats");
    	//Assertions.add("my-car is a wagon");
		// この状態で再試行を行うと、全て出ているのでとくに新しくアサーションが出来るわけでもなく推論が終わる
		presenter.restart(Assertions);

		// 推論を順に探索した結果をクラスとして格納しリストで受け取る
		System.out.println();
		ArrayList<StepResult> stepresults2 = presenter.stepResult();
		for (StepResult stepresult : stepresults2) {
			ArrayList<Assertion> add = stepresult.getAdd();
			Rule apply = stepresult.getApply();
			Assertion success = stepresult.getSuccess();
			//System.out.println("add size:" + add.size());
			//System.out.println("apply size:" + apply.size());
			//System.out.println("success size:" + success.size());
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
	}
}