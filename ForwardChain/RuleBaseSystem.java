import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * RuleBaseSystem
 *
 */
public class RuleBaseSystem {
	static RuleBase rb;
    static ArrayList<String> firstAssertions;
    static String fileName;
    static boolean question;
    ArrayList<String> answer;
    ArrayList<String> answerString;
    SearchStep ss;
    ArrayList<SearchStep> sss;

	// コンストラクタ
	RuleBaseSystem () {
		//RuleBase rb;
		firstAssertions = new ArrayList<>();
	}

    public static void main(String args[]){
    	rb = new RuleBase();	//ルールベースの構築
        rb.forwardChain();		//解析

        //質問応答
		Scanner stdIn1 = new Scanner(System.in);	//文字列読み込み
		Scanner stdIn2 = new Scanner(System.in);	//数値読み込み
		int returnFlag = 0;
		do {
			RuleBaseSystem NL = new RuleBaseSystem();
	        NL.answer = new ArrayList<>();
	        NL.answerString = new ArrayList<>();
		    //ここの仮説の部分を変更します
			System.out.println("質問を入力してください");
			String englishQuestion = stdIn1.nextLine();
		    //String s = args[0];		//質問英語を入力
		    System.out.println("質問内容 = " + englishQuestion);

		    //質問応答メソッド&解析
		    question = true;
		    NL.NaturalLanguage(englishQuestion);
		    for (int i = 0; i < NL.answer.size(); i++) {
		    	System.out.println("答え = " + NL.answer.get(i));
		    }
		    System.out.print("もう１回? 1...Yes/ 0...No ");
		    returnFlag = stdIn2.nextInt();
		    //rb.qFlag = 0;
		}while(returnFlag == 1);
    }

    // ここで全クラス＆メソッドを管理
    public void start(ArrayList<String> firstAssertions, String fileName) {
    	RuleBaseSystem.question = false;
    	rb = new RuleBase(firstAssertions, fileName);
        rb.forwardChain();
        RuleBaseSystem.fileName = fileName;
    }

    // 再試行＆再構築
    public void restart(ArrayList<String> assertions, ArrayList<Rule> rules) {
    	RuleBaseSystem.question = false;
    	rb = new RuleBase(assertions, rules);
    	rb.forwardChain();
    }

    // ルールの追加
    public boolean addRule(String newRuleName, ArrayList<String> newRuleAntecedents, String newRuleConsequent) {
    	return rb.insertRule( new Rule(newRuleName, newRuleAntecedents, newRuleConsequent) );
    }

    // ルールの削除
    public boolean deleteRule(Rule targetRule) {
    	return rb.deleteRule(targetRule);
    }

    // ルールの更新
    public boolean updateRule(Rule targetRule) {
    	return rb.updateRule(targetRule);
    }

    // 更新済みルールの取得
    public ArrayList<Rule> getRules() {
    	return rb.getRules();
    }

    // 更新済みアサーションの取得
    public ArrayList<Assertion> getAssertions() {
    	return rb.getWorkingMemory().getAssertions();
    }

    // 推論順に探索結果返却
    public ArrayList<StepResult> getStepResults() {
    	return rb.getStepResults();
    }

    // 検索を行い結果を返す(複数検索の場合は改良)
    public ArrayList<SearchStep> searchAssertion(String target) {
		RuleBaseSystem.question = true;
		answer = new ArrayList<>();
		answerString = new ArrayList<>();
		sss = new ArrayList<>();
		List<StepResult> subSRs = new ArrayList<>();
    	NaturalLanguage(target);
    	for (int i = 0; i < answer.size(); i++) {
    		if (answerString.size() > 0) {
    			subSRs = searchSRs(answerString.get(i));
    			ss = new SearchStep(answer.get(i), subSRs);
    		} else {
    			ss = new SearchStep(answer.get(i), null);
    		}
    		sss.add(ss);
    		subSRs = new ArrayList<>();
    	}
		Matcher.answer = new ArrayList<>();
		Matcher.answerString = new ArrayList<>();
		return sss;
    }

    public List<StepResult> searchSRs(String answerS) {
    	ArrayList<StepResult> SRs = getStepResults();
    	List<StepResult> subSRs = new ArrayList<>();
    	int target = -1;
    	for (int i = 0; i < SRs.size(); i++) {
    		String success = SRs.get(i).getSuccess().getName();
    		if (success.equals(answerS)) {
    			target = i;
    		}
    	}
    	subSRs = SRs.subList(0, target+1);
    	return subSRs;
    }

    /***
	 *	NaturalLanguageメソッド
	 *	引数 : 英語における自然言語の質問文「What is an Accord Wagon ?」
	 *  return: 変数を含むパターン 「?x is an Accord Wagon」
	 *  に置き換える
	 */
	public void NaturalLanguage(String equestion) {
		/***
		 *	1. "英語の質問:s"を"変数含むパターン"に置き換える
		 *	2. その"変数を含むパターン"を解析する:rb.backwardChain()を実行
		 */
		//解
		ArrayList<String> tokenList = new ArrayList<>();
		//今どこを指しているか
		int tokenPoint = 0;

		//前処理
		/**
		 * 「What color is his car?」と
		 * 「What color is Ito's car?」の違いをしっかりいきたい!
		 */
		if(equestion.contains("'s c")) {
			equestion = equestion.replace("'s c", "'s-C");
		}
		else if(equestion.contains("his")) {
			equestion = equestion.replace("his c", "his-c");
		}
		else if(equestion.contains("my")) {
			equestion = equestion.replace("my c", "my-c");
		}

		//1.まずはトークンに分解して,
		StringTokenizer stoken = new StringTokenizer(equestion);
		//  トークンの数を保存
		int tokenSize = stoken.countTokens() - 1;	//最後の?は除くからね！

		//2.いろいろいじって,
		/***
		 *  注意1)今回は前回と違って,3つ(Head,Tail,Label)に当てはめればいいわけじゃないから
		 *  注意2)aかanかだいぶ変わるな
		 */
		String firstToken = stoken.nextToken();
		tokenPoint ++;
		String secondToken = stoken.nextToken();
		tokenPoint ++;
		if(firstToken.equals("Is")) {
			tokenList.add(secondToken);
			tokenList.add("is");
		}
		else if(firstToken.equals("What")) {
			//rb.qFlag = 1;
			if(secondToken.equals("color")) {
				String thirdToken = stoken.nextToken();
				tokenPoint ++;
				tokenList.add(stoken.nextToken());
				tokenPoint ++;
				tokenList.add(thirdToken);
				tokenList.add(" ?x");
			}
			else if(secondToken.equals("does")) {
				tokenList.add(stoken.nextToken());
				tokenPoint ++;
				String thirdToken = stoken.nextToken();
				tokenPoint ++;

				//三単現のsの処理
				//System.out.println("thirdToken = " + thirdToken);
				//String thirdToken3s = thirdToken.substring(thirdToken.length()-1);
				//System.out.println("thirdToken3s = " + thirdToken3s);

				if(thirdToken.equals("have")) {
					tokenList.add("has");
				}
				tokenList.add("?x");
			}
			else if(secondToken.equals("is")) {
				tokenList.add("?x");
				tokenList.add("is");
			}
		}
		else if(firstToken.equals("Does")) {
			String thirdToken = stoken.nextToken();
			tokenPoint ++;

			//三単現のsの処理
			/*	三人称単数じゃなのは...
			 *  1人称... I, We
			 *  2人称... You
			 *  3人称... They
			 *
			 **/
			//System.out.println("secondToken = " + secondToken);

			String s = secondToken.substring(secondToken.length()-1);
			//System.out.println("s = " + s);

			if(thirdToken.equals("have") & !s.equals("s")) {		//んんん～～
				thirdToken = "has";
			}
			else {
				thirdToken = thirdToken.replace(thirdToken, thirdToken+"s");
			}
			tokenList.add(secondToken);
			tokenList.add(thirdToken);
		}

		//3.toStringで最後に合体させる(今回はあくまでStrigの文字列にしないといけないのだ.)
		//  格納
		for(int i = tokenPoint; i < tokenSize; i++) {
			tokenList.add(stoken.nextToken());
		}
		//  ArrayList → String文字へ
		String patarn = tokenList.toString();
		patarn = patarn.replace("[", "");
		patarn = patarn.replace("]", "");
		patarn = patarn.replace(",", "");
		//str = str.replace(" ?", "");	//ここで処理すると変数「?x」の?も消えちゃう

		//System.out.println("patarn = " + patarn);


		//String patarn = "Ito's-Car is ?x";	//前件文の内容「RULEの後件部にないから,WMから見る」
		//String patarn = "?x is a Ferrari F50";		//後件文の内容「RULEを見て,WMを見る」


		/*(注意)
		 *	Yes/No返事のときは, そのままでいいんだけど,
		 *  「my-car has ?x」のときに, 「?x= a VTEC engine」とかできなんすよね...
		 *  ので, Whatの場合は別にしないといけない
		 */

		//whatの場合
		if(patarn.contains("?x")) {

			ArrayList<String> newWorkingMemory = new ArrayList<>();

			for(int i = 0; i < rb.wm.memorySize(); i ++) {
				ArrayList<String> newAssertion = new ArrayList<>();
				StringTokenizer wmtoken = new StringTokenizer(rb.wm.getValue(i));

				String wmfirstToken = wmtoken.nextToken();
				newAssertion.add(wmfirstToken);

				String wmsecondToken = wmtoken.nextToken();
				String wmthirdToken = wmtoken.nextToken();
				if(wmsecondToken.equals("is") && wmthirdToken.equals("a")) {
					newAssertion.add("is-a");
				}
				else if(wmsecondToken.equals("is") && wmthirdToken.equals("an")) {
					newAssertion.add("is-an");
				}
				else if(wmsecondToken.equals("has") && wmthirdToken.equals("a")) {
					newAssertion.add("has-a");
				}
				else if(wmsecondToken.equals("has") && wmthirdToken.equals("an")) {
					newAssertion.add("has-an");
				}
				else if(wmthirdToken.equals("several")) {
					newAssertion.add(wmsecondToken);
				}
				else {
					newAssertion.add(wmsecondToken);
					newAssertion.add(wmthirdToken);
				}

				//System.out.println("残りトークンの数 = " + wmtoken.countTokens());
				if(wmtoken.countTokens() > 1) {
					String wmforthToken = wmtoken.nextToken();
					String wmfifthToken = wmtoken.nextToken();
					newAssertion.add(wmforthToken + "-" + wmfifthToken);
				}
				else if(wmtoken.countTokens() == 1){
					newAssertion.add(wmtoken.nextToken());
				}

				//System.out.println("newAssertion = " + newAssertion);
				//  ArrayList → String文字へ
				String stringAssertion = newAssertion.toString();
				stringAssertion = stringAssertion.replace("[", "");
				stringAssertion = stringAssertion.replace("]", "");
				stringAssertion = stringAssertion.replace(",", "");
				//System.out.println("stringAssertion = " + stringAssertion);
				newWorkingMemory.add(stringAssertion);
			}
			//System.out.println("newWorkingMemory = " + newWorkingMemory);

			//解析
			for(int i = 0; i < newWorkingMemory.size(); i++) {
				(new Matcher()).matching(patarn, newWorkingMemory.get(i));
				answer = Matcher.answer;
				answerString = Matcher.answerString;
			}
		}

		else {
			//解析
			boolean flag = false;
			for(int i = 0; i < rb.wm.memorySize(); i ++) {
				if((new Matcher()).matching(patarn, rb.wm.getValue(i))) {
					//System.out.println("答え = Yes");
					answer.add("Yes");
					answerString.add(rb.wm.getValue(i));
					flag = true;
				}
			}
			if(!flag) {
				//System.out.println("答え = No");
				answer.add("No");
			}
		}
	}
}

/**
 * ワーキングメモリを表すクラス．
 *
 *
 */
class WorkingMemory {
    ArrayList<Assertion> assertions;
    ArrayList<Assertion> add;
    Assertion assertion;

    WorkingMemory(){
        assertions = new ArrayList<Assertion>();
        add = new ArrayList<>();
    }

    //WMの数を取得
    public int memorySize() {
    	return assertions.size();
    }

    //WMの要素を取得
    public String getValue(int i) {
    	return assertions.get(i).getName();
    }

    /**
     * マッチするアサーションに対するバインディング情報を返す
     * （再帰的）
     *
     * @param     前件を示す ArrayList
     * @return    バインディング情報が入っている ArrayList
     */
    public ArrayList matchingAssertions(ArrayList<String> theAntecedents){
        ArrayList bindings = new ArrayList();
        add = new ArrayList<>();
        ArrayList result = matchable(theAntecedents,0,bindings);
        if (result == null) {
        	add = new ArrayList<>();
        }
        return result;
        //return matchable(theAntecedents,0,bindings);
    }

    private ArrayList matchable(ArrayList<String> theAntecedents,int n,ArrayList bindings){
        if(n == theAntecedents.size()){
            return bindings;
        } else if (n == 0){
            boolean success = false;
            for(int i = 0 ; i < assertions.size() ; i++){
                HashMap<String,String> binding = new HashMap<String,String>();
                if((new Matcher()).matching(
                    (String)theAntecedents.get(n),
                    (String)assertions.get(i).getName(),
                    binding)){
                    bindings.add(binding);
                    success = true;
                    add.add(assertions.get(i));
                }
            }
            if(success){
                return matchable(theAntecedents, n+1, bindings);
            } else {
                return null;
            }
        } else {
            boolean success = false;
            ArrayList newBindings = new ArrayList();
            for(int i = 0 ; i < bindings.size() ; i++){
                for(int j = 0 ; j < assertions.size() ; j++){
                    if((new Matcher()).matching(
                        (String)theAntecedents.get(n),
                        (String)assertions.get(j).getName(),
                        (HashMap)bindings.get(i))){
                        newBindings.add(bindings.get(i));
                        success = true;
                        add.add(assertions.get(j));
                    }
                }
            }
            if(success){
                return matchable(theAntecedents,n+1,newBindings);
            } else {
                return null;
            }
        }
    }

    /**
     * アサーションをワーキングメモリに加える．
     *
     * @param     アサーションを表す String
     */
    public void addAssertion(String theAssertion){
        System.out.println("ADD:"+theAssertion);
        //assertions.add(new Assertion(theAssertion));
        assertion = new Assertion(theAssertion);
        assertions.add(assertion);
        //add.add(assertion);
    }

    /**
     * 指定されたアサーションがすでに含まれているかどうかを調べる．
     *
     * @param     アサーションを表す String
     * @return    含まれていれば true，含まれていなければ false
     */
    public boolean contains(String theAssertion){
    	int flag = 0;
    	for (Assertion assertion : assertions) {
    		if (assertion.getName().equals(theAssertion)) {
    			flag++;
    		}
    	}
    	if (flag <= 0) {
    		return false;
    	} else {
    		return true;
    	}
        //return assertions.contains(theAssertion);
    }

    /**
     * ワーキングメモリの情報をストリングとして返す．
     *
     * @return    ワーキングメモリの情報を表す String
     */
    public String toString(){
        return assertions.toString();
    }

    public ArrayList<Assertion> getAssertions() {
    	return assertions;
    }

    public ArrayList<Assertion> getAdd() {
    	return add;
    }

    public Assertion getSuccess() {
    	return assertion;
    }
}

/**
 * ルールベースを表すクラス．
 *
 *
 */
class RuleBase {
    String fileName;
    FileReader f;
    StreamTokenizer st;
    WorkingMemory wm;
    ArrayList<Rule> rules;
    StepResult sr;
    ArrayList<StepResult> srs;
    ArrayList<Assertion> add;
    ArrayList<Rule> apply;
    Assertion success;
    WorkingMemory cash;
    Rule applyUnit;

  RuleBase(){
        fileName = "CarShop.data";
        wm = new WorkingMemory();
        wm.addAssertion("my-car is inexpensive");
        wm.addAssertion("my-car has a VTEC engine");
        wm.addAssertion("my-car is stylish");
        wm.addAssertion("my-car has several color models");
        wm.addAssertion("my-car has several seats");
        wm.addAssertion("my-car is a wagon");
        rules = new ArrayList<Rule>();
        loadRules(fileName);
        srs = new ArrayList<StepResult>();
        add = new ArrayList<>();
        apply = new ArrayList<>();
        cash = new WorkingMemory();
        applyUnit = null;
    }

    RuleBase(ArrayList<String> firstAssertions, String fileName){
        wm = new WorkingMemory();
        for (String firstAssertion : firstAssertions) {
        	wm.addAssertion(firstAssertion);
        }
        rules = new ArrayList<Rule>();
        loadRules(fileName);
        srs = new ArrayList<StepResult>();
        add = new ArrayList<>();
        apply = new ArrayList<>();
        cash = new WorkingMemory();
        applyUnit = null;
    }

    RuleBase(ArrayList<String> assertions, ArrayList<Rule> rules) {
    	wm = new WorkingMemory();
    	for (String assertion : assertions) {
        	wm.addAssertion(assertion);
        }
    	this.rules = rules;
    	for(int i = 0 ; i < rules.size() ; i++){
            System.out.println(((Rule)rules.get(i)).toString());
        }
    	srs = new ArrayList<StepResult>();
        add = new ArrayList<>();
        apply = new ArrayList<>();
        cash = new WorkingMemory();
        applyUnit = null;
    }

    //public ArrayList memory() {
    //	return wm;
    //}

    /**
     * 前向き推論を行うためのメソッド
     *
     */
    public void forwardChain(){
        boolean newAssertionCreated;
        // 新しいアサーションが生成されなくなるまで続ける．
        do {
            newAssertionCreated = false;
            for(int i = 0 ; i < rules.size(); i++){
                Rule aRule = (Rule)rules.get(i);
                System.out.println("apply rule:"+aRule.getName());
                apply.add(aRule);
                ArrayList<String> antecedents = aRule.getAntecedents();
                String consequent  = aRule.getConsequent();
                //HashMap bindings = wm.matchingAssertions(antecedents);
                ArrayList bindings = wm.matchingAssertions(antecedents);
                if(bindings != null){
                    for(int j = 0 ; j < bindings.size() ; j++){
                        //後件をインスタンシエーション
                        String newAssertion =
                            instantiate((String)consequent,
                                        (HashMap)bindings.get(j));
                        //ワーキングメモリーになければ成功
                        if(!wm.contains(newAssertion)){
                            System.out.println("Success: "+newAssertion);
                            add = wm.getAdd();
                            wm.addAssertion(newAssertion);
                            success = wm.getSuccess();
                            applyUnit = apply.get(apply.size() - 1);
                            sr = new StepResult(add, applyUnit, success);
                            srs.add( sr );
                            apply = new ArrayList<>();
                            applyUnit = null;
                            success = null;
                            newAssertionCreated = true;
                        }
                    }
                }
            }
            System.out.println("Working Memory"+printWorkingMemory(wm));
            if (wm != cash) {
            	apply = new ArrayList<>();
            	cash = new WorkingMemory();
                cash = wm;
            }
        } while(newAssertionCreated);
        System.out.println("No rule produces a new assertion");
    }

    private String printWorkingMemory(WorkingMemory wm) {
    	ArrayList<Assertion> wmA = wm.getAssertions();
    	StringBuffer buf = new StringBuffer();
    	buf.append("[");
    	for (int i = 0; i < wmA.size()-1; i++) {
    		buf.append(wmA.get(i).getName());
    		buf.append(", ");
    	}
    	buf.append(wmA.get(wmA.size()-1).getName());
    	buf.append("]");
    	return buf.toString();
    }

    private String instantiate(String thePattern, HashMap theBindings){
        String result = new String();
        StringTokenizer st = new StringTokenizer(thePattern);
        for(int i = 0 ; i < st.countTokens();){
            String tmp = st.nextToken();
            if(var(tmp)){
                result = result + " " + (String)theBindings.get(tmp);
            } else {
                result = result + " " + tmp;
            }
        }
        return result.trim();
    }

    private boolean var(String str1){
        // 先頭が ? なら変数
        return str1.startsWith("?");
    }

    private void loadRules(String theFileName){
        String line;
        try{
            int token;
            f = new FileReader(theFileName);
            st = new StreamTokenizer(f);
            while((token = st.nextToken())!= StreamTokenizer.TT_EOF){
                switch(token){
                    case StreamTokenizer.TT_WORD:
                        String name = null;
                        ArrayList<String> antecedents = null;
                        String consequent = null;
                        if("rule".equals(st.sval)){
			    st.nextToken();
//                            if(st.nextToken() == '"'){
                                name = st.sval;
                                st.nextToken();
                                if("if".equals(st.sval)){
                                    antecedents = new ArrayList<String>();
                                    st.nextToken();
                                    while(!"then".equals(st.sval)){
                                        antecedents.add(st.sval);
                                        st.nextToken();
                                    }
                                    if("then".equals(st.sval)){
                                        st.nextToken();
                                        consequent = st.sval;
                                    }
                                }
//                            }
                        }
			// ルールの生成
                        rules.add(new Rule(name,antecedents,consequent));
                        break;
                    default:
                        System.out.println(token);
                        break;
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
        for(int i = 0 ; i < rules.size() ; i++){
        	System.out.println(((Rule)rules.get(i)).toString());
        }
    }

    public ArrayList<Rule> getRules() {
    	return rules;
    }

    public WorkingMemory getWorkingMemory() {
    	return wm;
    }

    public ArrayList<StepResult> getStepResults() {
    	return srs;
    }

    // データ挿入用メソッド
    public boolean insertRule(Rule targetRule) {
    	boolean add = true;
        rules.add(targetRule);
        try {
            writeFile();
        } catch(IOException e) {
        	add = false;
            System.out.println(e.toString());
        }
        return add;
    }

    // データ削除用メソッド
    public boolean deleteRule(Rule targetRule) {
    	boolean delete = true;
        for(int ruleNum = 0; ruleNum < rules.size(); ruleNum++) {
            if(rules.get(ruleNum).getName().equals(targetRule.getName())) {
                rules.remove(ruleNum);
            }
        }
        try {
            writeFile();
        } catch(IOException e) {
        	delete = false;
            System.out.println(e.toString());
        }
        return delete;
    }

    // データ更新用メソッド
    public boolean updateRule(Rule targetRule) {
    	boolean update = true;
        for(int ruleNum = 0; ruleNum < rules.size(); ruleNum++) {
            if(rules.get(ruleNum).getName().equals(targetRule.getName())) {
                rules.set(ruleNum, targetRule);
            }
        }
        try {
            writeFile();
        } catch(IOException e) {
           	update = false;
            System.out.println(e.toString());
        }
        return update;
    }

    // ファイル更新用メソッド
    private void writeFile() throws IOException {
    	String fileName = RuleBaseSystem.fileName;
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
            writer.println();
        }
        writer.close();
    }
}

/**
 * ルールを表すクラス．
 *
 *
 */
class Rule {
	static int counter = 0;
    String name;
    ArrayList<String> antecedents;
    String consequent;
    int id;

    Rule(String theName,ArrayList<String> theAntecedents,String theConsequent){
        this.name = theName;
        this.antecedents = theAntecedents;
        this.consequent = theConsequent;
        id = counter++;
    }

    /**
     * ルールの名前を返す．
     *
     * @return    名前を表す String
     */
    public String getName(){
        return name;
    }

    /**
     * ルールをString形式で返す
     *
     * @return    ルールを整形したString
     */
    public String toString(){
        return name+" "+antecedents.toString()+"->"+consequent;
    }

    /**
     * ルールの前件を返す．
     *
     * @return    前件を表す ArrayList
     */
    public ArrayList<String> getAntecedents(){
        return antecedents;
    }

    /**
     * ルールの後件を返す．
     *
     * @return    後件を表す String
     */
    public String getConsequent(){
        return consequent;
    }

    /**
     * ルールのidを返す．
     *
     * @return    通し番号を表す int
     */
    public int getId(){
        return id;
    }
}

/**
 * アサーションを表すクラス．
 *
 *
 */
class Assertion {
	static int counter = 0;
	String name;
	int id;

	Assertion(String theName){
        this.name = theName;
        id = counter++;
    }

	/**
     * アサーションをString形式で返す．
     *
     * @return    本体を表す String
     */
    public String getName(){
        return name;
    }

    /**
     * アサーションのidを返す．
     *
     * @return    通し番号を表す id
     */
    public int getId(){
        return id;
    }
}

/**
 * 各ステップごとの結果を表すクラス．
 *
 *
 */
class StepResult {
	private ArrayList<Assertion> add;
	private Rule apply;
	private Assertion success;

	StepResult(ArrayList<Assertion> theAdd, Rule theApply, Assertion theSuccess){
        this.add = theAdd;
        this.apply = theApply;
        this.success = theSuccess;
    }

	/**
     * アサーションをArrayList形式で返す．
     *
     * @return    本体を表す ArrayList<Assertion>
     */
    public ArrayList<Assertion> getAdd(){
        return add;
    }

	/**
     * ルールをArrayList形式で返す．
     *
     * @return    本体を表す ArrayList<Rule>
     */
    public Rule getApply(){
        return apply;
    }

	/**
     * アサーションをAssertion形式で返す．
     *
     * @return    本体を表す Assertion
     */
    public Assertion getSuccess(){
        return success;
    }
}

/**
 * 探索結果のステップ結果を表すクラス．
 *
 *
 */
class SearchStep {
	private String kekka;
	private List<StepResult> keiro;

	SearchStep(String theKekka, List<StepResult> theKeiro){
        this.kekka = theKekka;
        this.keiro = theKeiro;
    }

	/**
     * 結果をString形式で返す．
     *
     * @return    本体を表す String
     */
    public String getKekka(){
        return kekka;
    }

	/**
     * ルールをArrayList形式で返す．
     *
     * @return    本体を表す ArrayList<StepResult>
     */
    public List<StepResult> getKeiro(){
        return keiro;
    }
}

class Matcher {
    StringTokenizer st1;
    StringTokenizer st2;
    HashMap<String,String> vars;
    static ArrayList<String> answer = new ArrayList<>();
    static ArrayList<String> answerString = new ArrayList<>();

    Matcher(){
        vars = new HashMap<String,String>();
    }

    public boolean matching(String string1,String string2,HashMap<String,String> bindings){
        this.vars = bindings;
        return matching(string1,string2);
    }

    public boolean matching(String string1,String string2){
        //System.out.println(string1);
        //System.out.println(string2);

        // 同じなら成功
        if(string1.equals(string2)) {
        	return true;
        }

        // 各々トークンに分ける
        st1 = new StringTokenizer(string1);
        st2 = new StringTokenizer(string2);

        // 数が異なったら失敗
        if(st1.countTokens() != st2.countTokens()) return false;

        // 定数同士
        for(int i = 0 ; i < st1.countTokens();){
            if(!tokenMatching(st1.nextToken(),st2.nextToken())){
                // トークンが一つでもマッチングに失敗したら失敗
                return false;
            }
        }

        // 最後まで O.K. なら成功
        if (RuleBaseSystem.question == true) {
        	answerString.add(string2);
        }
        return true;
    }

    boolean tokenMatching(String token1,String token2){
        //System.out.println(token1+"<->"+token2);
        if(token1.equals(token2)) return true;
        if( var(token1) && !var(token2)) return varMatching(token1,token2);
        if(!var(token1) &&  var(token2)) return varMatching(token2,token1);
        return false;
    }

    boolean varMatching(String vartoken,String token){
        if(vars.containsKey(vartoken)){
            if(token.equals(vars.get(vartoken))){
                return true;
            } else {
                return false;
            }
        } else {
            vars.put(vartoken,token);
        }
        if (RuleBaseSystem.question == true) {
        	answer.add(token);
        }
        return true;
    }

    boolean var(String str1){
        // 先頭が ? なら変数
        return str1.startsWith("?");
    }

    ArrayList<String> getAnswer() {
    	return answer;
    }

    ArrayList<String> getAnswerString() {
    	return answerString;
    }
}
