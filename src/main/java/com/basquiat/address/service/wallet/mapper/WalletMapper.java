package com.basquiat.address.service.wallet.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.basquiat.address.service.wallet.vo.WalletVO;

/**
 * WalletMapper
 * 
 * created by basquiat
 *
 */
@Mapper
public interface WalletMapper {

	/**
	 * insert Wallet
	 * @param walletVO
	 */
	public void initializeWallet(WalletVO walletVO);

	/**
	 * sequence_generator table의 sequence 증가
	 */
	public void createNextSequence();

	/**
	 * 해당 주소의 발란스를 업데이트 한다.
	 * @param walletVO
	 */
	public void updateWalletByAddress(WalletVO walletVO);
	

	/**
	 * select Wallet Info
	 * @param walletVO
	 * @return WalletVO
	 */
	public WalletVO selectWallet(WalletVO walletVO);
	
	/**
	 * receive된 트랜잭선에 존재하는 주소가 실제 거래소에 있는지 확인하기 위한 용도
	 * @param address
	 * @return WalletVO
	 */
	public WalletVO selectWalletByAddress(String address);

	
	/**
	 * select sequence for ripple destination tag
	 * @return Integer
	 */
	public Integer selectTagSequence();
	
}
