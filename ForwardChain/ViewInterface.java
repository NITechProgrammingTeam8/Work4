import java.util.ArrayList;

interface ViewInterface {
	// 初期データ読み込み完了メソッド
    public void successStart();
    // 更新データ再読み込み完了メソッド
    public void successRestart();
    // 推論順に探索結果返却メソッド
    public ArrayList<StepResult> showStepResult(ArrayList<StepResult> stepresults);
    // 検索探索結果返却メソッド
    public ArrayList<ArrayList<SearchStep>> showSearchAssertion(ArrayList<ArrayList<SearchStep>> resultList);
    // ルール追加完了メソッド
  	public void successAddRule(boolean result);
  	// ルール削除完了メソッド
  	public void successDeleteRule(boolean result);
  	// ルール編集完了メソッド
  	public void successUpdateRule(boolean result);
  	//一覧表示メソッド
  	public ArrayList<Rule> showRuleList(ArrayList<Rule> resultList);
  	// 例外処理表示メソッド
  	public void showError(String errorText);
}