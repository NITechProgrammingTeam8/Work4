import java.util.ArrayList;

// DaoやTextDAOなどのModelクラスとViewクラスの接合部

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

    // 更新後のデータから再試行を行う
    public void restart() {
    	// ◎変更後のルールとアサーションの受け取り、それに基づいた出力の再構築
    	ArrayList<Rule> rules = rulebasesystem.getRules();
    	ArrayList<Assertion> assertions = rulebasesystem.getAssertions();
    	System.out.println("長さ：" + assertions.size());
    	//assertions.remove(6);
    	//assertions.remove(6);
    	//assertions.remove(6);
    	rulebasesystem.restart(assertions, rules);
    	view.successRestart();
    }

    /*
    // GUI終了時にTextDAOに対してデータベースからテキストファイルへの書き込みを指示する
    // あおしゅー：終了時に必ずこのメソッドを呼んでください
    // のりこさん：データベースからテキストファイルへの書き込みを行うメソッドを用意してください
    public void finish() {
        try {
            textCon.writeTextFile();
            view.successFinish();
        } catch(FileNotFoundException e) {
            view.showError(e.toString());
        }
    }
    */

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

    // 探索を順に追った結果を返すよう指示する
    // 何を返す？？(全部まとめて？？)

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

    // 始めにいれるアサーションの入れ方(これは任意で入れるようにするのか？)

    // 処理のやり直しはいつやるか？？？

    /*
     * やること
     *
     * 1.アサーションの入力をリストで管理(恐らく成功)
     * 2.出力を何(アサーション)と何(ルール)を使って何(アサーション)が出来たかを返す
     * 3.処理のやり直しを行うためのメソッド(ルール変更時)(恐らく成功)
     */
}