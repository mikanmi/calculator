/*
BSD 2-Clause License
Copyright (c) 2013, 2020, 2021, Patineboot
All rights reserved.
*/
package calculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 計算機フレームクラス
 *
 * フレームには計算式、計算結果、ボタンが表示されます。
 */
public class CalculatorFrame extends JFrame {
	/**
	 * JFrameに設定するPanel
	 */
	private JPanel contentPanel = new JPanel();

	/**
	 * contentPanel用レイアウトマネージャ
	 */
	private GridBagLayout gridBagLayout = new GridBagLayout();

	/**
	 * 計算式、計算結果を表示するテキストフィールド向けフォント
	 */
	private Font textFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);

	/**
	 * ボタン向けフォント
	 */
	private Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);

	/**
	 * 計算式を表示するテキストフィールド
	 */
	JTextField expressionText = new JTextField("");

	/**
	 * 計算結果を表示するテキストフィールド
	 */
	JTextField resultText = new JTextField("0");

	/**
	 * mainメソッド
	 * @param args コマンド引数
	 */
	public static void main(String[] args) {
		new CalculatorFrame();
	}

	/**
	 * コンストラクタ
	 *
	 * 計算式、計算結果、テンキー、計算キーをFrameに設定します。
	 */
	public CalculatorFrame() {
		/* JFrameの設定 */
		super("Calculator");
		this.addKeyListener(new KeyListenerImpl());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setContentPane(contentPanel);
		contentPanel.setLayout(gridBagLayout);

		/* 計算式、計算結果の設定 */
		addJTextField(expressionText, 0, 0, 4, 1);
		addJTextField(resultText, 0, 1, 4, 1);

		/* ボタンの設定 */
		ActionListener nbal = new NumberButtonActionListner();
		ActionListener obal = new OperatorButtonActionListner();
		ActionListener cbal = new ClearButtonActionListner();
		ActionListener dbal = new DotButtonActionListner();
		ActionListener ebal = new EqualButtonActionListner();

//		addComponent(new JButton("("), 0, 2, 1, 1, obal);
//		addComponent(new JButton(")"), 1, 2, 1, 1, obal);
		addButton(new JButton("C"), 2, 2, 1, 1, cbal);
		addButton(new JButton("CE"), 3, 2, 1, 1, cbal);
		addButton(new JButton("7"), 0, 3, 1, 1, nbal);
		addButton(new JButton("8"), 1, 3, 1, 1, nbal);
		addButton(new JButton("9"), 2, 3, 1, 1, nbal);
		addButton(new JButton("/"), 3, 3, 1, 1, obal);
		addButton(new JButton("4"), 0, 4, 1, 1, nbal);
		addButton(new JButton("5"), 1, 4, 1, 1, nbal);
		addButton(new JButton("6"), 2, 4, 1, 1, nbal);
		addButton(new JButton("*"), 3, 4, 1, 1, obal);
		addButton(new JButton("1"), 0, 5, 1, 1, nbal);
		addButton(new JButton("2"), 1, 5, 1, 1, nbal);
		addButton(new JButton("3"), 2, 5, 1, 1, nbal);
		addButton(new JButton("-"), 3, 5, 1, 1, obal);
		addButton(new JButton("0"), 0, 6, 1, 1, nbal);
		addButton(new JButton("."), 1, 6, 1, 1, dbal);
		addButton(new JButton("="), 2, 6, 1, 1, ebal);
		addButton(new JButton("+"), 3, 6, 1, 1, obal);

		this.pack();
		this.setFocusable(true);
		this.setVisible(true);
	}

	/**
	 * コンポーネントをcontentPanelに追加する。
	 *
	 * @param component 追加するコンポーネント
	 * @param x contentPanel中のコンポーネントのx位置
	 * @param y contentPanel中のコンポーネントのy位置
	 * @param w コンポーネントの幅
	 * @param h コンポーネントの高さ
	 */
	private void addComponent(JComponent component, int x, int y, int w, int h) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.insets = new Insets(1, 1, 1, 1);
		gridBagLayout.setConstraints(component, gbc);
		contentPanel.add(component);
		component.setFocusable(false);
	}

	/**
	 * JTextFieldをcontentPanelに追加する。
	 * @param textFiled 追加するJTextField
	 * @param x contentPanel中のコンポーネントのx位置
	 * @param y contentPanel中のコンポーネントのy位置
	 * @param w コンポーネントの幅
	 * @param h コンポーネントの高さ
	 */
	private void addJTextField(JTextField textFiled, int x, int y, int w, int h) {
		addComponent(textFiled, x, y, w, h);
		textFiled.setEditable(false);
		textFiled.setHorizontalAlignment(JTextField.RIGHT);
		textFiled.setPreferredSize(new Dimension(246, 40));
		textFiled.setFont(textFont);
	}

	/**
	 * JButtonをcontentPanelに追加する。
	 * @param button 追加するJButton
	 * @param x contentPanel中のコンポーネントのx位置
	 * @param y contentPanel中のコンポーネントのy位置
	 * @param w コンポーネントの幅
	 * @param h コンポーネントの高さ
	 * @param l アクションリスナー
	 */
	private void addButton(JButton button, int x, int y, int w, int h, ActionListener l) {
		addComponent(button, x, y, w, h);
		button.addActionListener(l);
		button.setPreferredSize(new Dimension(60, 40));
		button.setFont(buttonFont);
	}

	/**
	 * 数字ボタンと数字キーの押下に対応する処理を行います。
	 * @param key 数字ボタンと数字キーの種類を示す文字列
	 */
	private void processNumberKey(String key) {
		/* 式を取得 */
		String expressionString = expressionText.getText();

		String token = getLastToken(expressionString);
		/* 最終トークンが0の場合 */
		if(token.equals("0")) {
			/* 最終トークンを取り除く */
			int expressionStringLength = expressionString.length();
			expressionString = expressionString.substring(0, expressionStringLength - 1);
		}
		String resultString = expressionString + key;
		expressionText.setText(resultString);
		double result = evaluateExpression(resultString);
		resultText.setText(Double.toString(result));
	}

	/**
	 * 演算子ボタンと演算子キーの押下に対応する処理を行います。
	 * @param key 演算子ボタンと演算子キーの種類を示す文字列
	 */
	private void processOperatorKey(String key) {
		String expressionString = expressionText.getText();
		int expressionStringLength = expressionString.length();

		/* 未入力の場合は、単項演算子"-"のみ許容する */
		if (expressionStringLength == 0) {
			if (key.equals("-")) {
				expressionText.setText(expressionString + key);
			}
			return;
		}

		/* 入力済みで、演算子で完了している場合 */
		char lastChar = getLastCharacter(expressionString);
		if (Expression.isOperator(lastChar)) {
			/* --- 現在式に対するチェック --- */
			/*
			 * 式が演算子と'-'で終わっている(e.g."*-")場合、入力を許可しない
			 * 式が'-'のみの場合、入力を許可しない
			 */
			char preLastChar = getPreLastCharacter(expressionString);
			if ((lastChar == '-') &&
					(Expression.isOperator(preLastChar) ||
					expressionStringLength == 1)) {
				return;
			}

			/* --- 入力文字に対するチェック --- */
			/*
			 * "-"の場合は式が-で終了している場合は無視しそれ以外は入力許可
			 * "-"以外は演算子を書き換える
			 */
			if (key.equals("-")) {
				if (lastChar == '-') {
					return;
				}
			}
			else {
				expressionString = expressionString.substring(0, expressionString.length() - 1);
			}

		}
		expressionText.setText(expressionString + key);
	}

	/**
	 * =ボタンと=キーの押下に対応する処理を行います。
	 * @param key "="を指定してください
	 */
	private void processEqualKey(String key) {
		/* 演算子が"="の場合 */
		String expressionString = expressionText.getText();
		double result = evaluateExpression(expressionString);

		resultText.setText(Double.toString(result));
		expressionText.setText("");
	}

	/**
	 * 消去ボタンと消去キーの押下に対応する処理を行います。
	 * @param key 消去ボタンと消去キーの種類を示す文字列
	 */
	private void processClearKey(String key) {
		if (key.equals("CE")) {
			/* 式から末尾の文字を削除する */
			String expression = expressionText.getText();
			int expressionLength = expression.length();
			if (expressionLength > 0) {
				expression = expression.substring(0, expressionLength - 1);
				expressionText.setText(expression);
			}
			double result = evaluateExpression(expression);
			resultText.setText(Double.toString(result));
		}
		else {
			expressionText.setText("");
			resultText.setText("0");
		}
	}

	/**
	 * "."ボタンの押下に対応する処理を行います。
	 * @param key "."文字列を指定してください
	 */
	private void processDotKey(String key) {
		/* 最終トークンにドット(.)を含む場合は無視する */
		String expressionString = expressionText.getText();
		String lastToken = getLastToken(expressionString);
		if(lastToken.indexOf(".") == -1) {
			expressionText.setText(expressionString + key);
		}
	}

	/**
	 * 式の評価を行う。
	 * 
	 * 式の最後に演算子があった場合は無視されます。
	 * 式は小数点を含むことのできる数字と演算子から構成されている必要があります。
	 * @param expression 式
	 * @return 式の評価結果
	 */
	private double evaluateExpression(String expression) {
		char lastChar =  getLastCharacter(expression);
		String challengExpression = expression;
		if (Expression.isOperator(lastChar)) {
			challengExpression = expression.substring(0, expression.length() - 1);
		}
		Expression exp = new Expression(challengExpression);
		double result = exp.evaluation();
		return result;
	}

	/**
	 * 式の中の一番右側の文字を取得する
	 * @param expression 文字を取得する対象の式
	 * @return 一番右側の文字
	 */
	private char getLastCharacter(String expression) {
		char result = '\0';
		int expressionLength = expression.length();
		if (expressionLength > 0) {
			result = expression.charAt(expressionLength - 1);
		}
		return result;
	}

	/**
	 * 式の中の右側から２番目の文字を取得する
	 * @param expression 文字を取得する対象の式
	 * @return 右側から２番目の文字
	 */
	private char getPreLastCharacter(String expression) {
		char result = '\0';
		int expressionLength = expression.length();
		if (expressionLength > 2) {
			result = expression.charAt(expressionLength - 2);
		}
		return result;
	}

	/**
	 * 式の中の一番右側のトークンを取得する
	 * @param expression 文字を取得する対象の式
	 * @return 一番右側のトークン
	 */
	public String getLastToken(String expression) {
		String result = expression;
		String value = expression;
		int length = value.length();
		for (int i = length - 1; i >= 0; i--) {
			char c = value.charAt(i);
			if (Expression.isOperator(c)) {
				/* 式が演算子で終了している場合 */
				if (i == length - 1) {
					result = value.substring(i);
				}
				/* 式が演算子で終了していない場合 */
				else {
					result = value.substring(i + 1);
				}
				break;
			}
		}
		return result;
	}

	/**
	 * キーコードリスナ
	 *
	 * キーボードが押下されたときに呼び出されます。
	 */
	private class KeyListenerImpl implements KeyListener {

		/**
		 * キーボードがタイプされたときに呼び出されます。
		 * 押下されたキーに従い処理を行います。
		 */
		@Override
		public void keyPressed(KeyEvent e){
			String keyString = null;
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				keyString = "=";
				processOperatorKey(keyString);
				break;
			case KeyEvent.VK_BACK_SPACE:
				keyString = "CE";
				processClearKey(keyString);
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			/* do nothing */
		}

		/**
		 * キーボードのキーがタイプされたときの処理
		 * @param e キーイベント
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			String keyString = null;
			char c = e.getKeyChar();
			switch (c) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				keyString = Character.toString(c);
				processNumberKey(keyString);
				break;
			case '+':
			case '-':
			case '*':
			case '/':
				keyString = Character.toString(c);
				processOperatorKey(keyString);
				break;
			case '=':
				keyString = Character.toString(c);
				processEqualKey(keyString);
				break;
			case '.':
				keyString = Character.toString(c);
				processDotKey(keyString);
				break;
			}
		}
	}

	/**
	 * 数字ボタン押下時の処理
	 */ 
	private class NumberButtonActionListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ac) {
			/* ボタンのラベルでどのボタンかを判定する */
			JButton button = (JButton)ac.getSource();
			String number = button.getText();
			processNumberKey(number);
		}
	}

	/**
	 * 演算子ボタン押下時の処理
	 */ 
	private class OperatorButtonActionListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ac) {
			/* ボタンのラベルでどのボタンかを判定する */
			JButton button = (JButton)ac.getSource();
			String label = button.getText();
			processOperatorKey(label);
		}
	}

	/**
	 * 等価ボタン押下時の処理
	 */ 
	private class EqualButtonActionListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ac) {
			/* ボタンのラベルでどのボタンかを判定する */
			JButton button = (JButton)ac.getSource();
			String label = button.getText();
			processEqualKey(label);
		}
	}

	/**
	 * 消去ボタン押下時の処理
	 */ 
	private class ClearButtonActionListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ac) {
			/* ボタンのラベルでどのボタンかを判定する */
			JButton button = (JButton)ac.getSource();
			String label = button.getText();
			processClearKey(label);
		}
	}

	/**
	 * ドットボタン押下時の処理
	 */ 
	private class DotButtonActionListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ac) {
			JButton button = (JButton)ac.getSource();
			String label = button.getText();
			processDotKey(label);
		}
	}

}
