import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class RuleBaseSystem {
    static RuleBase rb;
    static FileManager fm;
    static String fileName = null;
    ArrayList<Rule> firstRule;

    public static void main(String args[]){
	fm = new FileManager();
	//ファイルからルールの読み取り
	//ArrayList<Rule> rules = fm.loadRules("CarShop.data");
	ArrayList<Rule> rules = fm.loadRules("InstantNoodle.data");
	//ArrayList rules = fm.loadRules("AnimalWorld.data");

	//ファイルからワーキングメモリの読み取り
	//ArrayList<String> wm    = fm.loadWm("CarShopWm.data");
	ArrayList<String> wm    = fm.loadWm("InstantNoodleWm.data");
	//ArrayList wm    = fm.loadWm("AnimalWorldWm.data");

	rb = new RuleBase(rules,wm);		//ルールベースの構築

	Scanner stdIn1 = new Scanner(System.in);	//文字列読み込み
	Scanner stdIn2 = new Scanner(System.in);	//数値読み込み
	int returnFlag = 0;
	do {
	    //ここの仮説の部分を変更します
		System.out.println("質問を入力してください");
		String englishQuestion = stdIn1.nextLine();
	    //String s = args[0];		//質問英語を入力
	    System.out.println("質問内容 = " + englishQuestion);

	    //質問応答メソッド&解析
	    ArrayList<String> queries = NaturalLanguage(englishQuestion);
	    rb.backwardChain(queries);

	    System.out.print("もう１回? 1...Yes/ 0...No ");
	    returnFlag = stdIn2.nextInt();
	    rb.qFlag = 0;
	}while(returnFlag == 1);
    }

    // 初期ルールデータの読み込み
    public void start(String filename) {
    	fm = new FileManager();
    	firstRule = new ArrayList<>();
    	RuleBaseSystem.fileName = filename;
    	firstRule = fm.loadRules(filename);
    }

    // 更新前ルールの読み込み
    public ArrayList<Rule> getFirstRules() {
    	return firstRule;
    }

    // 更新前ルールでの推論
    public ArrayList<StepResult> stepResult(String wmname, String target) {
    	ArrayList<Rule> rules = getFirstRules();
    	ArrayList<String> wm = fm.loadWm(wmname);
    	rb = new RuleBase(rules,wm);
    	/*
    	// これは探索の中に入るかもしれない
    	StringTokenizer st = new StringTokenizer(target,",");
		ArrayList<String> queries = new ArrayList<String>();
		for(int i = 0 ; i < st.countTokens();){
			queries.add(st.nextToken());
		}
		*/
    	ArrayList<String> queries = NaturalLanguage(target);
		rb.backwardChain(queries);
		ArrayList<StepResult> answer = rb.getStepResults();
		return answer;
    }

    // 更新後ルールでの推論
    public ArrayList<StepResult> reStepResult(ArrayList<Rule> rules, String wmname, String target) {
    	fm = new FileManager();
    	ArrayList<String> wm = fm.loadWm(wmname);
    	rb = new RuleBase(rules,wm);
    	/*
    	// これは探索の中に入るかもしれない
    	StringTokenizer st = new StringTokenizer(target,",");
		ArrayList<String> queries = new ArrayList<String>();
		for(int i = 0 ; i < st.countTokens();){
			queries.add(st.nextToken());
		}
		*/
    	ArrayList<String> queries = NaturalLanguage(target);
		rb.backwardChain(queries);
		ArrayList<StepResult> answer = rb.getStepResults();
		return answer;
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

    /***
     *	NaturalLanguageメソッド
     *	引数 : 英語における自然言語の質問文「What is an Accord Wagon ?」
     *  return: 変数を含むパターン 「?x is an Accord Wagon」
     *  に置き換える
     */
    public static ArrayList<String> NaturalLanguage(String equestion) {
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
			if(secondToken.equals("it")) {
				secondToken = "It";
			}
    		tokenList.add(secondToken);
    		tokenList.add("is");
    	}
    	else if(firstToken.equals("What")) {
    		rb.qFlag = 1;
    		if(secondToken.equals("color")) {
    			String thirdToken = stoken.nextToken();
    			tokenPoint ++;
    			tokenList.add(stoken.nextToken());
    			tokenPoint ++;
    			tokenList.add(thirdToken);
    			tokenList.add(" ?x");
    		}
    		else if(secondToken.equals("does")) {
    			String thirdToken = stoken.nextToken();
    			if(thirdToken.equals("it")) {
					thirdToken = "It";
				}
    			tokenList.add(thirdToken);
    			tokenPoint ++;
    			String fourthToken = stoken.nextToken();
    			tokenPoint ++;
    			if(fourthToken.equals("have")) {
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
   		if(secondToken.equals("it")) {
				secondToken = "It";
			}
    		String thirdToken = stoken.nextToken();
    		tokenPoint ++;

    		//三人称単数のsの処理
			String s = secondToken.substring(secondToken.length()-1);
			if(thirdToken.equals("have") & !s.equals("s")) {		
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
    	System.out.println("patarn = " + patarn);


    	//String patarn = "Ito's-Car is ?x";	//前件文の内容「RULEの後件部にないから,WMから見る」
    	//String patarn = "?x is a Ferrari F50";		//後件文の内容「RULEを見て,WMを見る」

    	//解析
	    StringTokenizer patarns = new StringTokenizer(patarn,",");
	    ArrayList<String> queries = new ArrayList<String>();
	    for(int i = 0 ; i < patarns.countTokens();){
	    	queries.add(patarns.nextToken());
	    	System.out.println("queries = " + queries);
	    }
	    //rb.backwardChain(queries);	//ここのqueriesはStringの文字列
	    return queries;
    }
}

class RuleBase implements Serializable{
    String fileName;
    ArrayList<String> wm;
    ArrayList<Rule> rules;
    int qFlag;	//疑問詞判定フラグ

    Rule questionField = null;
    Rule answerField = null;
    String question = null;
    String answer = null;
    Rule sub = null;
    ArrayList<Rule> subs;
    ArrayList<StepResult> srs;
    StepResult sr = new StepResult();
    int miss;
    int overwrite;
    ArrayList<String> targets;

    RuleBase(ArrayList<Rule> theRules,ArrayList<String> theWm){
	wm = theWm;
	rules = theRules;
	srs = new ArrayList<>();
	targets = new ArrayList<>();
	miss = 0;
	overwrite = 100000;
	sr.shokika();
	subs = new ArrayList<>();
    }

    public void setWm(ArrayList<String> theWm){
	wm = theWm;
    }

    public void setRules(ArrayList<Rule> theRules){
	rules = theRules;
    }

    public ArrayList<Rule> getRules() {
    	return rules;
    }

    public void backwardChain(ArrayList<String> hypothesis){
	System.out.println("Hypothesis:"+hypothesis);
	ArrayList<String> orgQueries = (ArrayList)hypothesis.clone();
	//HashMap<String,String> binding = new HashMap<String,String>();
	HashMap<String,String> binding = new HashMap<String,String>();
	targets = new ArrayList<>(hypothesis);
	targets.remove(0);
	if(matchingPatterns(hypothesis,binding)){
	    System.out.println("Yes");
	    System.out.println(binding);
	    // 最終的な結果を基のクェリーに代入して表示する
	    for(int i = 0 ; i < orgQueries.size() ; i++){
		String aQuery = (String)orgQueries.get(i);
		System.out.println("binding: "+binding);
		String anAnswer = instantiate(aQuery,binding);
		System.out.println("Query: "+aQuery);
		System.out.println("Answer:"+anAnswer);
	    }
	} else {
	    System.out.println("No");
	}
    }

    /**
     * マッチするワーキングメモリのアサーションとルールの後件
     * に対するバインディング情報を返す
     */
    private boolean matchingPatterns(ArrayList<String> thePatterns,HashMap<String,String> theBinding){
    	String firstPattern;
	if(thePatterns.size() == 1){
	    firstPattern = (String)thePatterns.get(0);
	    if(matchingPatternOne(firstPattern,theBinding,0) != -1){
	      return true;
	    } else {
	      return false;
	    }
	} else {
	    firstPattern = (String)thePatterns.get(0);
	    thePatterns.remove(0);

	    int cPoint = 0;
	    while(cPoint < wm.size() + rules.size()){
	      // 元のバインディングを取っておく
	      HashMap<String,String> orgBinding = new HashMap<String,String>();
	      for(Iterator<String> i = theBinding.keySet().iterator() ; i.hasNext();){
	          String key = i.next();
	          String value = (String)theBinding.get(key);
	          orgBinding.put(key,value);
	      }
	      int tmpPoint = matchingPatternOne(firstPattern,theBinding,cPoint);
	      System.out.println("tmpPoint: "+tmpPoint);
	      if(tmpPoint != -1){
	          System.out.println("Success:"+firstPattern);
	          if(matchingPatterns(thePatterns,theBinding)){
	              //成功
	              return true;
	          } else {
	              //失敗
	              //choiceポイントを進める
	              cPoint = tmpPoint;
	              // 失敗したのでバインディングを戻す
	              theBinding.clear();
	              for(Iterator<String> i = orgBinding.keySet().iterator(); i.hasNext();){
	                  String key = i.next();
	                  String value = orgBinding.get(key);
	                  theBinding.put(key,value);
	              }
	          }
	      } else {
	          // 失敗したのでバインディングを戻す
	          theBinding.clear();
	          for(Iterator<String> i = orgBinding.keySet().iterator(); i.hasNext();){
	              String key = i.next();
	              String value = orgBinding.get(key);
	              theBinding.put(key,value);
	          }
	          return false;
	      }
	    }
	    return false;
	    /*
	    if(matchingPatternOne(firstPattern,theBinding)){
	      return matchingPatterns(thePatterns,theBinding);
	    } else {
	      return false;
	    }
	    */
	}
    }

    private int matchingPatternOne(String thePattern,HashMap<String,String> theBinding,int cPoint){
      if(cPoint < wm.size() ){
	// WME(Working Memory Elements) と Unify してみる．
	for(int i = cPoint ; i < wm.size() ; i++){
	    if((new Unifier()).unify(thePattern,
				     (String)wm.get(i),
				     theBinding)){
		System.out.println("Success WM");
		System.out.println((String)wm.get(i)+" <=> "+thePattern);
		answerField = null;
		answer = (String)wm.get(i);
		question = thePattern;
		srsAdd(questionField, question, answerField, answer);
		return i+1;
	    }
	}
      }
      if(cPoint < wm.size() + rules.size()){
	// Ruleと Unify してみる．
 	for(int i = cPoint ; i < rules.size() ; i++){
 	    Rule aRule = rename((Rule)rules.get(i));
 	    sub = aRule;
 	    subs.add(aRule);
	    // 元のバインディングを取っておく．
	    HashMap<String,String> orgBinding = new HashMap<String,String>();
	    for(Iterator<String> itr = theBinding.keySet().iterator(); itr.hasNext();){
		String key = itr.next();
		String value = theBinding.get(key);
		orgBinding.put(key,value);
	    }
 	    if((new Unifier()).unify(thePattern,
 				     (String)aRule.getConsequent(),
 				     theBinding)){
		System.out.println("Success RULE");
		System.out.println("Rule:"+aRule+" <=> "+thePattern);
		answerField = aRule;
		question = thePattern;
		answer = (String)aRule.getConsequent();
		if (targets.size() > 0) {
			if(targets.get(0).equals(question)) {
				questionField = null;
				if (targets.size() > 0) {
					targets.remove(0);
				}
			}
		}
		srsAdd(questionField, question, answerField, answer);
		System.out.println((String)aRule.getConsequent());
		// さらにbackwardChaining
 		ArrayList<String> newPatterns = aRule.getAntecedents();
 		questionField = aRule;
		if(matchingPatterns(newPatterns,theBinding)){
		    return wm.size()+i+1;
		} else {
			miss = 1;
		    // 失敗したら元に戻す．
		    theBinding.clear();
		    for(Iterator<String> itr = orgBinding.keySet().iterator(); itr.hasNext();){
			String key = itr.next();
			String value = orgBinding.get(key);
			theBinding.put(key,value);
		    }
		}
	    }
 	}
      }
      return -1;
    }

    /**
     * 与えられたルールの変数をリネームしたルールのコピーを返す．
     * @param   変数をリネームしたいルール
     * @return  変数がリネームされたルールのコピーを返す．
     */
    int uniqueNum = 0;
    private Rule rename(Rule theRule){
	Rule newRule = theRule.getRenamedRule(uniqueNum);
	uniqueNum = uniqueNum + 1;
	return newRule;
    }

    private String instantiate(String thePattern, HashMap<String,String> theBindings){
	String result = new String();
	StringTokenizer st = new StringTokenizer(thePattern);
	for(int i = 0 ; i < st.countTokens();){
	    String tmp = st.nextToken();
	    if(var(tmp)){
		result = result + " " + (String)theBindings.get(tmp);
	      System.out.println("tmp: "+tmp+", result: "+result);
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

    private void srsAdd(Rule questionField, String question, Rule answerField, String answer) {
    	if (questionField != null) {
    		String[] split = question.split(" ", 0);
    		int number = Integer.parseInt(split[0].replaceAll("[^0-9]",""));
    		if (number < subs.size()) {
    			questionField = null;
    			questionField = subs.get(number);
    		}
    	}
    	if (miss != 0) {
    		for (StepResult sr : srs) {
    			if (sr.getQ().equals(question)) {
    				overwrite = sr.getId();
    				sr.setAF(answerField);
    				sr.setA(answer);
    				break;
    			}
    		}
    		miss = 0;
    		overwrite++;
    	} else if (overwrite < srs.size()) {
    		srs.get(overwrite).setQF(questionField);
    		srs.get(overwrite).setQ(question);
    		srs.get(overwrite).setAF(answerField);
    		srs.get(overwrite).setA(answer);
    		overwrite++;
    	} else {
    		srs.add( new StepResult(questionField, question, answerField, answer) );
    		overwrite = 100000;
    	}
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

class FileManager {
    FileReader f;
    StreamTokenizer st;
    public ArrayList<Rule> loadRules(String theFileName){
	ArrayList<Rule> rules = new ArrayList<Rule>();
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
			}
			rules.add(
			    new Rule(name,antecedents,consequent));
			break;
		    default:
			System.out.println(token);
			break;
		}
	    }
	} catch(Exception e){
	    System.out.println(e);
	}
	return rules;
    }

    public ArrayList<String> loadWm(String theFileName){
	ArrayList<String> wm = new ArrayList<String>();
	String line;
	try{
	    int token;
	    f = new FileReader(theFileName);
	    st = new StreamTokenizer(f);
	    st.eolIsSignificant(true);
	    st.wordChars('\'','\'');
	    while((token = st.nextToken())!= StreamTokenizer.TT_EOF){
		line = new String();
		while( token != StreamTokenizer.TT_EOL){
		    line = line + st.sval + " ";
		    token = st.nextToken();
		}
		wm.add(line.trim());
	    }
	} catch(Exception e){
	    System.out.println(e);
	}
	return wm;
    }
}


/**
 * ルールを表すクラス．
 */
class Rule implements Serializable{
    String name;
    ArrayList<String> antecedents;
    String consequent;

    Rule(String theName,ArrayList<String> theAntecedents,String theConsequent){
	this.name = theName;
	this.antecedents = theAntecedents;
	this.consequent = theConsequent;
    }

    public Rule getRenamedRule(int uniqueNum){
	ArrayList<String> vars = new ArrayList<String>();
	for(int i = 0 ; i < antecedents.size() ; i++){
	    String antecedent = (String)this.antecedents.get(i);
	    vars = getVars(antecedent,vars);
	}
	vars = getVars(this.consequent,vars);
	HashMap<String,String> renamedVarsTable = makeRenamedVarsTable(vars,uniqueNum);

	ArrayList<String> newAntecedents = new ArrayList<String>();
	for(int i = 0 ; i < antecedents.size() ; i++){
	    String newAntecedent =
		renameVars((String)antecedents.get(i),
			   renamedVarsTable);
	    newAntecedents.add(newAntecedent);
	}
	String newConsequent = renameVars(consequent,
					  renamedVarsTable);

	Rule newRule = new Rule(name,newAntecedents,newConsequent);
	return newRule;
    }

    private ArrayList<String> getVars(String thePattern,ArrayList<String> vars){
	StringTokenizer st = new StringTokenizer(thePattern);
	for(int i = 0 ; i < st.countTokens();){
	    String tmp = st.nextToken();
	    if(var(tmp)){
		vars.add(tmp);
	    }
	}
	return vars;
    }

    private HashMap<String,String> makeRenamedVarsTable(ArrayList<String> vars,int uniqueNum){
	HashMap<String,String> result = new HashMap<String,String>();
	for(int i = 0 ; i < vars.size() ; i++){
	    String newVar =
		(String)vars.get(i) + uniqueNum;
	    result.put((String)vars.get(i),newVar);
	}
	return result;
    }

    private String renameVars(String thePattern,
			      HashMap<String,String> renamedVarsTable){
	String result = new String();
	StringTokenizer st = new StringTokenizer(thePattern);
	for(int i = 0 ; i < st.countTokens();){
	    String tmp = st.nextToken();
	    if(var(tmp)){
		result = result + " " + renamedVarsTable.get(tmp);
	    } else {
		result = result + " " + tmp;
	    }
	}
	return result.trim();
    }

    private boolean var(String str){
	// 先頭が ? なら変数
	return str.startsWith("?");
    }

    public String getName(){
	return name;
    }

    public String toString(){
	return name+" "+antecedents.toString()+"->"+consequent;
    }

    public ArrayList<String> getAntecedents(){
	return antecedents;
    }

    public String getConsequent(){
	return consequent;
    }
}

/**
 * 各ステップごとの結果を表すクラス．
 *
 *
 */
class StepResult {
	private Rule questionField;
	private Rule answerField;
	private String question;
	private String answer;
	private static int counter = 0;
	private int id;

	StepResult() { }

	StepResult(Rule theQF, String theQ, Rule theAF, String theA){
        this.questionField = theQF;
        this.question = theQ;
        this.answerField = theAF;
        this.answer = theA;
        id = counter++;
    }

	/**
     * ルールを返す．
     *
     * @return    本体を表す Rule
     */
    public Rule getQF(){
        return questionField;
    }

	/**
     * ルールを返す．
     *
     * @return    本体を表す Rule
     */
    public Rule getAF(){
        return answerField;
    }

	/**
     * 検索質問を返す．
     *
     * @return    本体を表す String
     */
    public String getQ(){
        return question;
    }

    /**
     * 検索結果を返す．
     *
     * @return    本体を表す String
     */
    public String getA(){
        return answer;
    }

    /**
     * 各値の変更．
     *
     */
    public void setQF(Rule theQF){ this.questionField = theQF; }
    public void setAF(Rule theAF){ this.answerField = theAF; }
    public void setQ(String theQ){ this.question = theQ; }
    public void setA(String theA){ this.answer = theA; }

    /**
     * StepResultのidを返す．
     *
     * @return    通し番号を表す id
     */
    public int getId(){
        return id;
    }

    public void shokika() {
    	counter = 0;
    }
}


class Unifier {
    StringTokenizer st1;
    String buffer1[];
    StringTokenizer st2;
    String buffer2[];
    HashMap<String,String> vars;

    Unifier(){
	//vars = new HashMap();
    }

    public boolean unify(String string1,String string2,HashMap<String,String> theBindings){
	HashMap<String,String> orgBindings = new HashMap<String,String>();
	for(Iterator<String> i = theBindings.keySet().iterator(); i.hasNext();){
	    String key = i.next();
	    String value = theBindings.get(key);
	    orgBindings.put(key,value);
	}
	this.vars = theBindings;
	if(unify(string1,string2)){
	    return true;
	} else {
	    // 失敗したら元に戻す．
	    theBindings.clear();
	    for(Iterator<String> i = orgBindings.keySet().iterator(); i.hasNext();){
		String key = i.next();
		String value = orgBindings.get(key);
		theBindings.put(key,value);
	    }
	    return false;
	}
    }

    public boolean unify(String string1,String string2){
	// 同じなら成功
	if(string1.equals(string2)) return true;

	// 各々トークンに分ける
	st1 = new StringTokenizer(string1);
	st2 = new StringTokenizer(string2);

	// 数が異なったら失敗
	if(st1.countTokens() != st2.countTokens()) return false;

	// 定数同士
	int length = st1.countTokens();
	buffer1 = new String[length];
	buffer2 = new String[length];
	for(int i = 0 ; i < length; i++){
	    buffer1[i] = st1.nextToken();
	    buffer2[i] = st2.nextToken();
	}

	// 初期値としてバインディングが与えられていたら
	if(this.vars.size() != 0){
	    for(Iterator<String> i = vars.keySet().iterator(); i.hasNext();){
		String key = i.next();
		String value = vars.get(key);
		replaceBuffer(key,value);
	    }
	}

	for(int i = 0 ; i < length ; i++){
	    if(!tokenMatching(buffer1[i],buffer2[i])){
		return false;
	    }
	}

	return true;
    }

    boolean tokenMatching(String token1,String token2){
	if(token1.equals(token2)) return true;
	if( var(token1) && !var(token2)) return varMatching(token1,token2);
	if(!var(token1) &&  var(token2)) return varMatching(token2,token1);
	if( var(token1) &&  var(token2)) return varMatching(token1,token2);
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
	    replaceBuffer(vartoken,token);
	    if(vars.containsValue(vartoken)){
		replaceBindings(vartoken,token);
	    }
	    vars.put(vartoken,token);
	}
	return true;
    }

    void replaceBuffer(String preString,String postString){
	for(int i = 0 ; i < buffer1.length ; i++){
	    if(preString.equals(buffer1[i])){
		buffer1[i] = postString;
	    }
	    if(preString.equals(buffer2[i])){
		buffer2[i] = postString;
	    }
	}
    }

    void replaceBindings(String preString,String postString){
	for(Iterator<String> i = vars.keySet().iterator(); i.hasNext();){
	    String key = i.next();
	    if(preString.equals(vars.get(key))){
		vars.put(key,postString);
	    }
	}
    }

    boolean var(String str1){
	// 先頭が ? なら変数
	return str1.startsWith("?");
    }
}