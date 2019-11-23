import java.util.ArrayList;

class View implements ViewInterface {
	// 初期データ読み込み完了メソッド
    public void successStart() {
    	System.out.println("Successfully started");
    }
    // 更新データ再読み込み完了メソッド
    public void successRestart() {
    	System.out.println("Successfully restarted");
    }
    // 推論順に探索結果返却メソッド
    public ArrayList<StepResult> showStepResult(ArrayList<StepResult> stepresults) {
    	return stepresults;
    }
    // 検索探索結果返却メソッド
    public ArrayList<ArrayList<SearchStep>> showSearchAssertion(ArrayList<ArrayList<SearchStep>> resultList) {
    	return resultList;
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
  	public void successUpdateRule(boolean result) {
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