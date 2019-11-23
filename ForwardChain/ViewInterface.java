import java.util.ArrayList;

interface ViewInterface {
	// 初期データ読み込み完了メソッド
    void successStart();
    // 更新データ再読み込み完了メソッド
    void successRestart();
    // 推論順に探索結果返却完了メソッド
    ArrayList<StepResult> showStepResult(ArrayList<StepResult> stepresults);
	// ルール追加完了メソッド(void？)
	boolean successAddRule(boolean result);
	// ルール削除完了メソッド(void？)
    boolean successDeleteRule(boolean result);
	// 例外処理表示メソッド
    void showError(String errorText);
    // ルール名称変更メソッド
    boolean successChangeRuleName(boolean result);
    //一覧表示メソッド(void？)
    ArrayList<Rule> showRuleList(ArrayList<Rule> resultList);
    //検索結果反映メソッド
    ArrayList<String> showSearchAssertion(ArrayList<String> result);
    // ルール前件変更メソッド
    boolean successChangeRuleAntecedents(boolean result);
    // ルール後件変更メソッド
    boolean successChangeRuleConsequent(boolean result);
    // アサーションの検索メソッド

	/*
    //データベース初期化完了メソッド
    void successStart();
    //テキストファイル記録完了メソッド
    void successFinish();
    //データ追加完了メソッド
    void successAddData();
    //検索結果反映メソッド
    void showSearchResult(List<String> resultList);
    //データ削除メソッド
    void successDeleteData();
    //一覧表示メソッド
    void showResultList(List<TextModel> resultList);
    //例外処理表示メソッド
    void showError(String errorText);
    //データ無し表示メソッド
    void showNoData();
    */
}