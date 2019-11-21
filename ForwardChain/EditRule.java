import java.util.List;
import java.io.*;

public class EditRule {

    public void insertRule(String fileName, Rule rule) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"));
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
        writer.close();
    }

    public void deleteRule(String fileName, Rule rule) throws IOException {

    }

    public void updateRule(String fileName, Rule rule) throws IOException {

    }
}