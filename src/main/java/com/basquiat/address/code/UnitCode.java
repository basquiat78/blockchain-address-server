package com.basquiat.address.code;

import java.math.BigDecimal;

/**
 * Unit commonCode
 * eth의 balance값을 조정하기 위한 enum class
 * created by basquiat
 *
 */
@SuppressWarnings("unused")
public enum UnitCode {
	
	ETHER("ether", 18);
	
	private String name;
	
	private BigDecimal weiFactor;
	
	UnitCode(String name, int factor) {
        this.name = name;
        this.weiFactor = BigDecimal.TEN.pow(factor);
    }
	
	public BigDecimal getWeiFactor() {
        return weiFactor;
    }
			
}
