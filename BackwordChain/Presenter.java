import java.util.ArrayList;

class Presenter {
    private RuleBaseSystem rulebasesystem = new RuleBaseSystem();
    //private RuleBaseSystemGUI gui = new RuleBaseSystemGUI();
    private ViewInterface view;
    public Presenter(ViewInterface view) {
        this.view = view;
    }

	// VIEW全く触れてないですすみません！！！好きに改良お願いします！！！
	
    // 探索を順に追った結果を返すよう指示する(返却方法検討中)
    public ArrayList<StepResult> stepResults(String filename, String wmname, String target) {
    	ArrayList<StepResult> stepresults = rulebasesystem.stepResult(filename, wmname, target);
    	//view.showStepResult(stepresults);
    	return stepresults;
    }

    public ArrayList<StepResult> reStepResults(String wmname, String target) {
    	ArrayList<Rule> rules = rulebasesystem.getRules();
    	/*
    	for (Rule rule : rules) {
    		System.out.println("●" + rule.getName());
    	}
    	*/
    	ArrayList<StepResult> stepresults = rulebasesystem.reStepResult(rules, wmname, target);
    	//view.showStepResult(stepresults);
    	return stepresults;
    }

    /*
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
    public void deleteRule(int targetData) {
        try {
        	// ルール削除用メソッドの作成(id指定で削除)
            boolean result = rulebasesystem.deleteRule(targetData);
            view.successDeleteRule(result);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
    }

    // 指定ルールの名前を変更するよう指示する
    public void changeRuleName(int targetData, String newName) {
        try {
        	// ルールの名称変更用メソッドの作成(idでルール指定)
            boolean result = rulebasesystem.changeRuleName(targetData, newName);
            view.successChangeRuleName(result);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
    }

    // 指定ルールの前件を変更するよう指示する
    public void changeRuleAntecedents(int targetData, ArrayList<String> newAntecedents) {
        try {
        	// ルールの前件変更用メソッドの作成(idでルール指定)
            boolean result = rulebasesystem.changeRuleAntecedents(targetData, newAntecedents);
            view.successChangeRuleAntecedents(result);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
    }

    // 指定ルールの後件を変更するよう指示する
    public void changeRuleConsequent(int targetData, String newConsequent) {
        try {
        	// ルールの後件変更用メソッドの作成(idでルール指定)
            boolean result = rulebasesystem.changeRuleConsequent(targetData, newConsequent);
            view.successChangeRuleConsequent(result);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
    }

    // ルールの一覧表示を行うよう指示する
    public void fetchRules() {
        ArrayList<Rule> ruleList;
        try {
        	// ルールの一覧を取得するメソッドの作成ArrayList<Rule>(データがなければnull)
            ruleList = rulebasesystem.fetchRules();
            view.showRuleList(ruleList);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
    }

    // アサーションによる検索をするよう指示する
    public void searchAssertion(String targetData) {
        ArrayList<String> resultList;
        try {
        	// アサーションの検索結果を返すメソッドの作成(メソッド名はまだ仮決定)
            resultList = rulebasesystem.searchAssertion();
            view.showSearchAssertion(resultList);
        } catch (Exception e) {
        	view.showError(e.toString());
		}
    }
    */
}