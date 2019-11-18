import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * RuleBaseSystem
 *
 */
public class RuleBaseSystem {
	static RuleBase rb;
    static ArrayList<String> firstAssertions;
    static String fileName;
	// コンストラクタ
	RuleBaseSystem () {
		//RuleBase rb;
		firstAssertions = new ArrayList<>();
	}

    public static void main(String args[]){
    	fileName = "CarShop.data";
    	firstAssertions.add("my-car is inexpensive");
    	firstAssertions.add("my-car has a VTEC engine");
    	firstAssertions.add("my-car is stylish");
    	firstAssertions.add("my-car has several color models");
    	firstAssertions.add("my-car has several seats");
    	firstAssertions.add("my-car is a wagon");
        rb = new RuleBase(firstAssertions, fileName);
        rb.forwardChain();
    }

    // ここで全クラス＆メソッドを管理
    public void start(ArrayList<String> firstAssertions, String fileName) {
    	//System.out.println("仮");
    	rb = new RuleBase(firstAssertions, fileName);
        rb.forwardChain();
    }

    // 再試行＆再構築
    public void restart(ArrayList<String> assertions, ArrayList<Rule> rules) {
    	rb = new RuleBase(assertions, rules);
    	rb.forwardChain();
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
        assertion = new Assertion(theAssertion);
        assertions.add(assertion);
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
            //System.out.println("Working Memory"+wm);
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
	//static int counter = 0;
	//int id;
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

class Matcher {
    StringTokenizer st1;
    StringTokenizer st2;
    HashMap<String,String> vars;

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
        if(string1.equals(string2)) return true;

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
        return true;
    }

    boolean var(String str1){
        // 先頭が ? なら変数
        return str1.startsWith("?");
    }

}
