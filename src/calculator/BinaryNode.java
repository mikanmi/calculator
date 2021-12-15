/*
BSD 2-Clause License
Copyright (c) 2013, 2020, 2021, Patineboot
All rights reserved.
*/
package calculator;

/**
 * 二分木ノード
 */
public class BinaryNode {

	/**
	 * トークンノードの値
	 */ 
	private final String value;

	/**
	 * 親ノード
	 */ 
	// private BinaryNode parentNode;

	/**
	 * 子ノード(左側)
	 */ 
	private BinaryNode leftNode;

	/**
	 * 子ノード(右側)
	 */ 
	private BinaryNode rightNode;

	/**
	 * コンストラクタ
	 * @param value ノードに設定する文字列形式の値
	 */
	public BinaryNode(String value) {
		this.value = value;
	}

	/**
	 * ノードが保持する値を取得する
	 * @return ノードが保持する値
	 */
	public String getValue() {
		return value;
	}

	/**
	 * ノードへ左側の子ノードを設定する。
	 * @param node 設定するノード
	 */
	public void setLeftNode(BinaryNode node) {
		leftNode = node;
	}

	/**
	 * ノードへ右側の子ノードを設定する。
	 * @param node 設定するノード
	 */
	public void setRightNode(BinaryNode node) {
		rightNode = node;
	}

	/**
	 * 左側の子ノードを取得する
	 * @return 左側子ノード
	 */
	public BinaryNode getLeftNode() {
		return leftNode;
	}

	/**
	 * 右側の子ノードを取得する
	 * @return 右側子ノード
	 */
	public BinaryNode getRightNode() {
		return rightNode;
	}

	public void debugPrint() {
		System.out.println(value);
		if (leftNode != null) {
			leftNode.debugPrint();
		}
		if (rightNode != null) {
			rightNode.debugPrint();
		}
	}
}
