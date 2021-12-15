/*
BSD 2-Clause License
Copyright (c) 2013, 2020, 2021, Patineboot
All rights reserved.
*/
package calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 計算式クラス
 *
 * 数字と演算子によって構成される計算式を表すクラスです。
 * 受け付ける演算子は、+、-、*、/です。
 * 四則演算の優先順位によって式を評価します。
 * <br>
 * e.g. 1+2*3/4、では、2.5
 */
public class Expression {
	/**
	 * 計算式のトークン区切り文字の演算子
	 * 演算子と優先度と演算方法は、並びを一致させること
	 */
	private static final char[] operators = {
		/* 演算子 */
		'+', '-', '*', '/',
	};

	/**
	 * 計算式のトークン区切り文字の優先度
	 * 演算子と優先度と演算方法は、並びを一致させること
	 */
	private static final int[] priorities = {
		/* 優先度 */
		/* 数字が大きい方が優先度が高い */
		1, 1, 2, 2,
	};

	/**
	 * 計算式のトークン区切り文字の演算方法
	 * 演算子と優先度と演算方法は、並びを一致させること
	 */
	private static final Evaluater[] evaluaters = {
		/* 演算方法 */
		new AddEvaluater(),
		new SubEvaluater(),
		new MulEvaluater(),
		new DivEvaluater(),
	};

	/**
	 * 計算式のパース結果を格納する二分木
	 */
	private final BinaryNode rootNode;

	/**
	 * 計算式の文字列表現
	 */
	private final String expression;

	/**
	 * 計算式の字句解析結果
	 */
	private final List<String> scanResult;
	
	/**
	 * コンストラクタ
	 *
	 * 計算式の文字列表現から、計算式のノードを内部にもつExpressionを構築する
	 * @param expression 計算式の文字列表現
	 */
	public Expression(String expression) {
		this.expression = expression;
		scanResult = scan(expression);
		rootNode = parse(scanResult);
		rootNode.debugPrint();
	}

	/**
	 * 計算を実行する
	 * @return 計算結果
	 */
	public double evaluation() {
		return evaluateToken(rootNode);
	}

	/**
	 * 文字列をトークンに分割する。
	 * @param str 計算式の文字列
	 * @return 分割した結果の文字列集合
	 * 注意：このAPIは構文チェックは未実装
	 */
	private List<String> scan(String str) {
		List<String> result = new ArrayList<>();
		int length = str.length();
		if (length == 0) {
			return result;
		}

		int lastPosition = 0;
		for (int i = 0; i < length ; i++) {
			char c = str.charAt(i);
			/* トークンを決定する */
			if (isOperator(c)) {
				if (lastPosition != i) {
					String token = str.substring(lastPosition, i);
					result.add(token);
				}
				String token = str.substring(i, i + 1);
				result.add(token);
				lastPosition = i + 1;
			}
		}
		/* 残りの文字列をトークンとする */
		if (lastPosition != length) {
			String token = str.substring(lastPosition, length);
			result.add(token);
		}
		return result;
	}

	/**
	 * トークンの文字列集合を二分木ツリーで表現する
	 * @param tokenList トークンの文字列集合
	 * @return 二分木ツリー
	 */
	private BinaryNode parse(List<String> tokenList) {
		int foundIndex = 0x7FFFFFFF;
		int foundPriority = 0x7FFFFFFF;
		int listSize = tokenList.size();
		String value = (listSize == 0) ? "" : tokenList.get(0);

		ListIterator<String> iterator = tokenList.listIterator(listSize);
		while (iterator.hasPrevious()) {
			int index = iterator.previousIndex();
			String token = iterator.previous();

			String preToken = null;
			if (iterator.hasPrevious()) {
				preToken = iterator.previous();
				iterator.next();
			}

			if (isOperator(token)) {
				int priority = getPriority(token, preToken);
				if (priority < foundPriority) {
					foundPriority = priority;
					foundIndex = index;
					value = token;
				}
			}
		}

		BinaryNode token = new BinaryNode(value);
		if (foundIndex != 0x7FFFFFFF) {
			List<String> left = tokenList.subList(0, foundIndex);
			BinaryNode leftToken = parse(left);
			token.setLeftNode(leftToken);

			List<String> right = tokenList.subList(foundIndex + 1, listSize);
			BinaryNode rightToken = parse(right);
			token.setRightNode(rightToken);

		}
		return token;
	}

	/**
	 * 二分木のノードを計算する
	 * #nodeを再帰的に評価する
	 * @param node 計算対象ノード
	 * @return 計算結果
	 */
	private double evaluateToken(BinaryNode node) {
		// Initialize result with NaN.
		double result = Double.NaN;

		String value = node.getValue();

		int valueLength = value.length();
		if (valueLength > 0) {
			if (isOperator(value)) {
				BinaryNode leftNode = node.getLeftNode();
				BinaryNode rightNode = node.getRightNode();
				double leftNumber = evaluateToken(leftNode);
				double rightNumber = evaluateToken(rightNode);
				Evaluater eva = getEvaluater(value);
				result = eva.calculate(leftNumber, rightNumber);
			}
			else {
				result = Double.parseDouble(value);
			}
		}
		else {
			result = 0.0;
		}
		return result;
	}

	/**
	 * 文字が演算子か判定する
	 * @param c 入力文字
	 * @return 演算子の場合はtrue、そうでないならfalse
	 */
	public static boolean isOperator(char c) {
		boolean result = false;
		for (int i = 0, length = operators.length; i < length; i++) {
			if (c == operators[i]) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 文字列が演算子か判定する
	 * @param str 入力文字列
	 * @return 演算子の場合true、そうでないならfalse
	 */
	public static boolean isOperator(String str) {
		if (str == null || str.length() != 1) {
			return false;
		}
		return isOperator(str.charAt(0));
	}

	/**
	 * 演算子の優先度を取得する。
	 *
	 * @param token 演算子トークン
	 * @param preToken 演算子の前のトークン。前のトークンがない場合はnullを指定する
	 * @return 1から3をとる優先度。演算子以外の指定は-1
	 */
	private int getPriority(String token, String preToken) {
		// Initialize result with -1.
		int result = -1;
		if (!isOperator(token)) {
			return result;
		}

		for (int i = 0, length = operators.length; i < length; i++) {
			if (token.charAt(0) == operators[i]) {
				/* 単項演算子は優先度を一番高くする */
				if (isOperator(preToken)) {
					result = 3;
				}
				else {
					result = priorities[i];
				}
				break;
			}
		}
		return result;
	}

	/**
	 * 演算方法を取得する
	 *
	 * @param token 演算子トークン
	 * @return 演算方法
	 */
	private Evaluater getEvaluater(String token) {
		Evaluater result = null;
		
		for (int i = 0, length = operators.length; i < length; i++) {
			if (token.charAt(0) == operators[i]) {
				result = evaluaters[i];
				break;
			}
		}
		return result;
	}
}

interface Evaluater {
	public double calculate(double ope1, double ope2);
}

class AddEvaluater implements Evaluater {
	@Override
	public double calculate(double ope1, double ope2) {
		return ope1 + ope2;
	}
}

class SubEvaluater implements Evaluater {
	@Override
	public double calculate(double ope1, double ope2) {
		return ope1 - ope2;
	}
}

class MulEvaluater implements Evaluater {
	@Override
	public double calculate(double ope1, double ope2) {
		return ope1 * ope2;
	}
}

class DivEvaluater implements Evaluater {
	@Override
	public double calculate(double ope1, double ope2) {
		return ope1 / ope2;
	}
}
