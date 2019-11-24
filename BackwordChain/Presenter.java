import java.util.ArrayList;

class Presenter {
    private RuleBaseSystem rulebasesystem = new RuleBaseSystem();
    //private RuleBaseSystemGUI gui = new RuleBaseSystemGUI();
    private ViewInterface view;
    public Presenter(ViewInterface view) {
        this.view = view;
    }

    // 初期ルールデータの読み込み
    public void start(String filename) {
    	rulebasesystem.start(filename);
    	view.showStart();
    }

    // 検索を順に追った結果を返すよう指示する(返却方法検討)
    public ArrayList<StepResult> stepResults(String wmname, String target) {
    	ArrayList<StepResult> stepresults = rulebasesystem.stepResult(wmname, target);
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
            view.successUpdataRule(result);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
    }

    // ルールの一覧表示を行うよう指示する
    public ArrayList<Rule> fetchRules() {
        ArrayList<Rule> ruleList = new ArrayList<>();;
        try {
        	// ルールの一覧を取得するメソッドの作成ArrayList<Rule>(データがなければnull)
            ruleList = rulebasesystem.fetchRules();
            view.showRuleList(ruleList);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
        return ruleList;
    }
}