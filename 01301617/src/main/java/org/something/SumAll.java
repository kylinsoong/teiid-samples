package org.something;

import org.teiid.CommandContext;
import org.teiid.UserDefinedAggregate;

public class SumAll implements UserDefinedAggregate<Integer>{
	
	private boolean isNull = true;
    private int result;

	@Override
	public void reset() {
		isNull = true;
        result = 0;
	}

	@Override
	public Integer getResult(CommandContext commandContext) {
		if (isNull) {
            return null;
        }
        return result;
	}
	
	public void addInput(Integer... vals) {
        isNull = false;
        for (int i : vals) {
            result += i;
        }
    }

}
