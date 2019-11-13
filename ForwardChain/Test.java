import java.util.ArrayList;

/*
 * Presenter動作確認用ファイル
 */

class Test {
	public static void main(String arg[]){
		Presenter presenter = new Presenter(new View());
		ArrayList<String> firstAssertions = new ArrayList<>();
		String fileName = "CarShop.data";
		firstAssertions.add("my-car is inexpensive");
    	firstAssertions.add("my-car has a VTEC engine");
    	firstAssertions.add("my-car is stylish");
    	firstAssertions.add("my-car has several color models");
    	firstAssertions.add("my-car has several seats");
    	firstAssertions.add("my-car is a wagon");
		presenter.start(firstAssertions, fileName);
		// この状態で再試行を行うと、全て出ているのでとくに新しくアサーションが出来るわけでもなく推論が終わる
		presenter.restart();
	}
}