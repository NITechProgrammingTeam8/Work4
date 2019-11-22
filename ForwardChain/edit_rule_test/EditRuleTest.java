import java.io.IOException;
import java.util.ArrayList;

// ルール挿入・削除・更新テスト用のクラス
// 使用しない
public class EditRuleTest {
    public static void main(String[] args) {
        String ruleName = "CarRule10";
        ArrayList<String> antecedents = new ArrayList();
        antecedents.add("?x is a Toyota");
        antecedents.add("?x has several seats");
        antecedents.add("?x is a hybrid car");
        String consequent = "?x is a Prius";
        Rule rule = new Rule(ruleName, antecedents, consequent);
        ArrayList<Rule> rules = new ArrayList();
        rules.add(rule);
        EditRule editRule = new EditRule("EditRuleTest.data", rules);

        // ルール挿入テスト
        String insertRuleName = "CarRule2";
        ArrayList<String> insertAntecedents = new ArrayList();
        insertAntecedents.add("?x is small");
        String insertConsequent = "?x is made in Japan";
        Rule insertRule = new Rule(insertRuleName, insertAntecedents, insertConsequent);
        editRule.insertRule(insertRule);

        // ルール削除テスト
        editRule.deleteRule(insertRule);

        // ルール更新テスト
        antecedents.remove(2);
        editRule.updateRule(rule);

    }
}