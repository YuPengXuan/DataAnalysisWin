package com.xn.alex.data.roc;

import java.util.Vector;

import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
import com.xn.alex.data.resultobj.ProfitResultNode;
import com.xn.alex.data.resultobj.algorithmResultObj2;

public abstract class ProfitModelInterface {
	
	public abstract void takeAction();
	
	protected Vector<algorithmResultObj2> m_resultObjVec;

	public Vector<algorithmResultObj2> getResultObjVec() {
		return m_resultObjVec;
	}

	public void setResultObjVec(Vector<algorithmResultObj2> m_resultObjVec) {
		this.m_resultObjVec = m_resultObjVec;
	}
	
    protected Vector<ProfitResultNode> m_pResultVec;

	public Vector<ProfitResultNode> getpResultVec() {
		return m_pResultVec;
	}

	public void setpResultVec(Vector<ProfitResultNode> m_pResultVec) {
		this.m_pResultVec = m_pResultVec;
	}
	
	protected ROC_TYPE rocType;

	public ROC_TYPE getRocType() {
		return rocType;
	}

	public void setRocType(ROC_TYPE rocType) {
		this.rocType = rocType;
	}

}
