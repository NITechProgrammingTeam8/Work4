import java.util.ArrayList;

// Viewの使用方法検討

class View implements ViewInterface {
	// 初期ルールデータの読み込み完了メソッド
	public void showStart() {
		System.out.println("read ruledata finish");
	}
    // 推論順に探索結果返却メソッド
    public ArrayList<StepResult> showStepResult(ArrayList<StepResult> stepresults) {
    	return stepresults;
    }
	// ルール追加完了メソッド
 	public void successAddRule(boolean result) {
 		System.out.println("add finish:" + result);
 	}
 	// ルール削除完了メソッド
 	public void successDeleteRule(boolean result) {
 		System.out.println("delete finish:" + result);
 	}
 	// ルール編集完了メソッド
 	public void successUpdataRule(boolean result) {
 		System.out.println("update finish:" + result);
 	}
 	//一覧表示メソッド
 	public ArrayList<Rule> showRuleList(ArrayList<Rule> resultList) {
 		return resultList;
 	}
 	// 例外処理表示メソッド
 	public void showError(String errorText) {
 		System.out.println("errorText");
 	}
}