import java.util.ArrayList;

class Presenter {

    private RuleBaseSystem rulebasesystem = new RuleBaseSystem();
    //private RuleBaseSystemGUI gui = new RuleBaseSystemGUI();
    private ViewInterface view;

    public Presenter(ViewInterface view) {
        this.view = view;
    }

    /*
     * 始めと終わりにやりたいことがあれば…
     *
     */
    // GUI起動時に初期データの読み込みと出力
    public void start(ArrayList<String> firstAssertions, String fileName) {
    	rulebasesystem.start(firstAssertions, fileName);
    	view.successStart();
    }

    // ルール更新後のデータから推論の再試行を行う
    public void restart(ArrayList<String> assertions) {
    	ArrayList<Rule> rules = rulebasesystem.getRules();
    	rulebasesystem.restart(assertions, rules);
    	view.successRestart();
    }

    // 探索を順に追った結果を返すよう指示する(返却方法検討中)
    public ArrayList<StepResult> stepResult() {
    	ArrayList<StepResult> stepresults = rulebasesystem.getStepResults();
    	view.showStepResult(stepresults);
    	return stepresults;
    }

    // 新規ルールを追加するように指示する
    public void addRule(String newRuleName, ArrayList<String> newRuleAntecedents, String newRuleConsequent) {
        try {
        	// ルール追加用メソッドの作成
        	boolean result = rulebasesystem.addRule(newRuleName, newRuleAntecedents, newRuleConsequent);
            view.successAddRule(result);
        } catch (Exception e) {
			view.showError(e.toString());
		}
    }

    // 指定ルールを削除するよう指示する
    public void deleteRule(Rule targetData) {
        try {
        	// ルール削除用メソッドの作成
            boolean result = rulebasesystem.deleteRule(targetData);
            view.successDeleteRule(result);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
    }

    // 指定ルールの値の更新を指示する
    public void updateRule(Rule targetData) {
    	try {
        	// ルール更新用メソッドの作成
            boolean result = rulebasesystem.updateRule(targetData);
            view.successUpdateRule(result);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
    }

    // ルールの一覧表示を行うよう指示する
    public ArrayList<Rule> fetchRules() {
        ArrayList<Rule> ruleList = new ArrayList<>();;
        try {
        	// ルールの一覧を取得するメソッドの作成ArrayList<Rule>(データがなければnullのはず)
            ruleList = rulebasesystem.getRules();
            view.showRuleList(ruleList);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
        return ruleList;
    }

    // アサーションによる検索をするよう指示する
    public ArrayList<ArrayList<SearchStep>> searchAssertion(ArrayList<String> targetData) {
        //ArrayList<String> resultList;
        // アサーションの検索結果を返すメソッドの作成
        // 複数の場合は改良必要あり
    	ArrayList<ArrayList<SearchStep>> resultList = new ArrayList<>();
        for (String target : targetData) {
        	resultList.add(rulebasesystem.searchAssertion(target));
        }
        view.showSearchAssertion(resultList);
        return resultList;
    }
}