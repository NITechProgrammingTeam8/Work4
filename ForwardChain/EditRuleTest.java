import java.io.IOException;
import java.util.ArrayList;

public class EditRuleTest {
    public static void main(String[] args) {
        EditRule editRule = new EditRule();
        String ruleName = "TestRule1";
        ArrayList<String> antecedents = new ArrayList();
        antecedents.add("?x is a professor");
        antecedents.add("?x has a toralab");
        antecedents.add("?x likes Mr. Ozono");
        String consequent = "?x is Mr. Shintani";
        Rule rule = new Rule("TestRule1", antecedents, consequent);
        try {
            editRule.insertRule("EditRuleTest.data", rule);
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
}