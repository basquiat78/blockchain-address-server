package com.basquiat.address.service.block.vo;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * block history VO
 * created by basquiat
 *
 */
@Getter
@Setter
public class BlockHistoryVO {

	/**
	 * 코인 명
	 */
	private String coinType;
	
	/**
	 * 마지막으로 확인한 블록 넘버
	 */
	private BigInteger lastBlockNumber;
}
