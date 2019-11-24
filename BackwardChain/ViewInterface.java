import java.util.ArrayList;

interface ViewInterface {
	// 初期ルールデータの読み込み完了メソッド
	void showStart();
	//初期ルールデータ一覧表示メソッド
	ArrayList<Rule> showFirstRuleList(ArrayList<Rule> resultList);
	// 推論順に探索結果返却メソッド
	ArrayList<StepResult> showStepResult(ArrayList<StepResult> stepresults);
    // 推論順に探索結果返却メソッド【再】
    ArrayList<StepResult> showReStepResult(ArrayList<StepResult> stepresults);
	// ルール追加完了メソッド
 	void successAddRule(boolean result);
 	// ルール削除完了メソッド
 	void successDeleteRule(boolean result);
 	// ルール編集完了メソッド
 	void successUpdataRule(boolean result);
 	//一覧表示メソッド
 	ArrayList<Rule> showRuleList(ArrayList<Rule> resultList);
 	// 例外処理表示メソッド
 	void showError(String errorText);
}