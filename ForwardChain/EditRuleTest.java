import java.io.IOException;
import java.util.ArrayList;

// ルール挿入・削除・更新テスト用のクラス
// 使用しない
public class EditRuleTest {
    public static void main(String[] args) {
        String ruleName = "TestRule1";
        ArrayList<String> antecedents = new ArrayList();
        antecedents.add("?x is a professor");
        antecedents.add("?x has a toralab");
        antecedents.add("?x likes Mr. Ozono");
        String consequent = "?x is Mr. Shintani";
        Rule rule = new Rule(ruleName, antecedents, consequent);
        ArrayList<Rule> rules = new ArrayList();
        rules.add(rule);
        EditRule editRule = new EditRule("EditRuleTest.data", rules);

        // ルール挿入テスト
        String insertRuleName = "TestRule2";
        ArrayList<String> insertAntecedents = new ArrayList();
        insertAntecedents.add("?x is a elderly-man");
        String insertConsequent = "?x is Mr. Shintani";
        Rule insertRule = new Rule(insertRuleName, insertAntecedents, insertConsequent);
        editRule.insertRule(insertRule);

        // ルール削除テスト
        editRule.deleteRule(rule);

        // ルール更新テスト
        antecedents.remove(2);
        editRule.updateRule(rule);

    }
}