package com.basquiat.address.service.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basquiat.address.code.Constants;
import com.basquiat.address.node.BlockChainNodeInterface;
import com.basquiat.address.redis.queue.RedisMessagePublisher;
import com.basquiat.address.service.wallet.mapper.WalletMapper;
import com.basquiat.address.service.wallet.vo.WalletVO;
import com.basquiat.address.util.CommonUtil;

/**
 * 
 * 주소 생성 및 tx 조회 수집/저장
 * 
 * created by basquiat
 *
 */
@Service("addressService")
public class WalletService {

	@Autowired
	private RedisMessagePublisher redisMessagePublisher;
	
	@Autowired
	private WalletMapper walletMapper;
	
	/**
	 * 주소 생성
	 * @param walletVO
	 * @throws Exception
	 */
	public void createWallet(WalletVO walletVO) throws Exception {

		// userId, coinId로 셀렉트해서 존재하는지 확인
		WalletVO selectWallet = this.getWallet(walletVO);
		if( selectWallet == null || selectWallet.getAddress() == null || selectWallet.getAddress().toString().equals("")) {			//insert wallet
			
			BlockChainNodeInterface node = CommonUtil.createInstance(Constants.PRE_FIX, walletVO.getCoinType().toUpperCase());
			
			String address = (String) node.getNewAddress();
			walletVO.setAddress(address);
			if("XRP".equals(walletVO.getCoinType())) {
				walletVO.setDestinationTag(this.getTagSequence().toString());
			} else {
				walletVO.setDestinationTag("");
			}
			walletMapper.initializeWallet(walletVO);
			redisMessagePublisher.publish(CommonUtil.convertJsonStringFromObject(walletVO));
			
		}
		
	}
	
	/**
	 * select wallet by address
	 * @param address
	 * @return WalletVO
	 * @throws Exception
	 */
	public WalletVO getWalletByAddress(String address) throws Exception {
		return walletMapper.selectWalletByAddress(address);
	}
	
	/**
	 * update balance by address
	 * @param walletVO
	 * @throws Exception
	 */
	public void updateWalletByAddress(WalletVO walletVO) throws Exception {
		walletMapper.updateWalletByAddress(walletVO);
	}
	
	/**
	 * get tag sequence for ripple destination tag
	 * @return Integer
	 */
	public Integer getTagSequence() throws Exception {
		walletMapper.createNextSequence();
		return walletMapper.selectTagSequence();
	}
	
	/**
	 * get tag sequence for ripple destination tag
	 * @return Integer
	 */
	private WalletVO getWallet(WalletVO walletVO) throws Exception {
		return walletMapper.selectWallet(walletVO);
	}
}
