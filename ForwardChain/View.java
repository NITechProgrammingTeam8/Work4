import java.util.ArrayList;

class View implements ViewInterface {
	// ルール追加完了メソッド(void？)
 	public boolean successAddRule(boolean result) {
 		return result;
 	}
 	// ルール削除完了メソッド(void？)
 	public boolean successDeleteRule(boolean result) {
 		return result;
 	}
 	// 例外処理表示メソッド
 	public void showError(String errorText) {
 		System.out.println("errorText");
 	}
 	// ルール名称変更メソッド
 	public boolean successChangeRuleName(boolean result) {
 		return result;
 	}
 	//一覧表示メソッド(void？)
 	public ArrayList<Rule> showRuleList(ArrayList<Rule> resultList) {
 		return resultList;
 	}
 	//検索結果反映メソッド
    public ArrayList<String> showSearchAssertion(ArrayList<String> result) {
    	return result;
    }
 	// ルール前件変更メソッド
 	public boolean successChangeRuleAntecedents(boolean result) {
 		return result;
 	}
 	// ルール後件変更メソッド
 	public boolean successChangeRuleConsequent(boolean result) {
 		return result;
 	}
}