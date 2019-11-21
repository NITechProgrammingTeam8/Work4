import java.util.List;
import java.io.*;


// ルール挿入・削除・更新テスト用の試作クラス
// RuleBaseSystem内部に同様のものが用意されているため使用しない
public class EditRule {

    String fileName;
    List<Rule> rules;

    public EditRule(String fileName, List<Rule> rules) {
        this.fileName = fileName;
        this.rules = rules;
    }

    // データ挿入用メソッド
    public void insertRule(Rule targetRule) {
        rules.add(targetRule);
        try {
            writeFile();
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }

    // データ削除用メソッド
    public void deleteRule(Rule targetRule) {
        for(int ruleNum = 0; ruleNum < rules.size(); ruleNum++) {
            if(rules.get(ruleNum).getName().equals(targetRule.getName())) {
                rules.remove(ruleNum);
            }
        }
        try {
            writeFile();
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }

    // データ更新用メソッド
    public void updateRule(Rule targetRule) {
        for(int ruleNum = 0; ruleNum < rules.size(); ruleNum++) {
            if(rules.get(ruleNum).getName().equals(targetRule.getName())) {
                rules.set(ruleNum, targetRule);
            }
        }
        try {
            writeFile();
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }

    // ファイル更新用メソッド
    private void writeFile() throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, false), "UTF-8"));

        for(Rule rule: rules) {
            // ルール名の出力
            writer.println("rule	\"" + rule.getName() + "\"");
            // 前件の出力
            List<String> antecedents = rule.getAntecedents();
            for(int antecedentNum = 0; antecedentNum < antecedents.size(); antecedentNum++) {
                if(antecedentNum == 0) {
                    writer.println("if	\"" + antecedents.get(antecedentNum) + "\"");
                } else {
                    writer.println("	\"" + antecedents.get(antecedentNum) + "\"");
                }
            }
            // 後件の出力
            writer.println("then	\"" + rule.getConsequent() + "\"");
        }
        writer.close();
    }
}