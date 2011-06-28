package org.nutz.el2.opt.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.nutz.el2.Operator;
import org.nutz.el2.opt.RunMethod;
import org.nutz.el2.opt.TwoTernary;
import org.nutz.el2.opt.custom.CustomMake;


/**
 * 方法体封装.
 * 主要是把方法的左括号做为边界
 * @author juqkai(juqkai@gmail.com)
 *
 */
public class MethodOpt extends TwoTernary {

	public int fetchPriority() {
		return 1;
	}
	
	public void wrap(Queue<Object> rpn) {
		if(rpn.peek() instanceof AccessOpt){
			left = rpn.poll();
			return;
		}
		super.wrap(rpn);
	}
	
	public Object calculate(){
		return fetchMethod().run(fetchParam());
	}
	
	private RunMethod fetchMethod(){
		if(!(left instanceof AccessOpt)){
			return CustomMake.make(left.toString());
		}
		AccessOpt lval = (AccessOpt) left;
		return lval;
	}
	
	
	/**
	 * 取得方法执行的参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Object> fetchParam(){
		List<Object> rvals = new ArrayList<Object>();
		if(right != null){
			if(right instanceof CommaOpt){
				rvals = (List<Object>) ((CommaOpt) right).calculate();
			} else {
				rvals.add(calculateItem(right));
			}
		}
		if(!rvals.isEmpty()){
			for(int i = 0; i < rvals.size(); i ++){
				if(rvals.get(i) instanceof Operator){
					rvals.set(i, ((Operator)rvals.get(i)).calculate());
				}
			}
		}
		return rvals;
	}
	
	public String fetchSelf() {
		return "method";
	}

}