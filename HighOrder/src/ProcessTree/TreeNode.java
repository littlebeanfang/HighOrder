package ProcessTree;

public class TreeNode implements Comparable{
	public String form;
	public String rel;
	public String POS;
	private int processindex;
	public TreeNode(String form, String rel, String POS, int processindex){
		this.form=form;
		this.rel=rel;
		this.POS=POS;
		this.processindex=processindex;
	}
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		TreeNode tNode=(TreeNode) o;
		return this.processindex-tNode.processindex;
	}
	
}
