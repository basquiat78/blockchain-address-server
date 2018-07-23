package com.basquiat.address.service.transaction.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * TransactioVO
 * created by basquiat
 *
 */
@Getter
@Setter
public class TransactionVO {

	/**
	 * 트랜잭션 아이디
	 */
	private String txId;
	
	/**
	 * txId가 포함된 블록 해쉬
	 */
	private String blockHash;
	
	/**
	 * 코인 코드
	 */
	private String coinType;
	
	/**
	 * 코인 지갑
	 */
	private String address;
	
	/**
	 * balance
	 */
	private BigDecimal balance;
	
	/**
	 * confirmations number
	 */
	private Integer confirmations;
	
	/**
	 * 해당 트랜잭션 정보를 업데이트한 시각
	 */
	private String updateDttm;
	
}
