package com.basquiat.address.apitest;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.basquiat.address.service.block.BlockHistoryService;
import com.basquiat.address.service.block.vo.BlockHistoryVO;
import com.basquiat.address.service.wallet.WalletService;
import com.basquiat.address.service.wallet.vo.WalletVO;
import com.basquiat.address.util.CommonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

	@Autowired
	private BlockHistoryService blockHistoryService;
	
	@Autowired
	private WalletService walletService;
	
	private String coinType;
	
	private BlockHistoryVO blockHistoryVO;
	
	@Before
    public void setUp() {
		
		coinType = "BTC";
		
		blockHistoryVO = new BlockHistoryVO();
		blockHistoryVO.setCoinType(coinType);
		blockHistoryVO.setLastBlockNumber(1353056);
    }
	
	@Test
	public void getTagSequenceTest() throws Exception {
		System.out.println(walletService.getTagSequence());
	}
	
	//@Test
	public void getBlockHistoryTest() throws Exception {
		System.out.println(CommonUtil.convertJsonStringFromObject(blockHistoryService.getBlockHistory(coinType)));
	}
	
	//@Test
	public void updateBlockHistoryTest() throws Exception {
		blockHistoryService.updateBlockHistory(blockHistoryVO);
		System.out.println(CommonUtil.convertJsonStringFromObject(blockHistoryService.getBlockHistory(coinType)));
	}
	
	//@Test
	public void getWalletByAddressTest() throws Exception {
		System.out.println(CommonUtil.convertJsonStringFromObject(walletService.getWalletByAddress("n4ZEsTqSe1ad155kb3JYEnuFsz6Eyp7Dzk")));
	}
	
	//@Test
	public void updateWalletByAddressTest() throws Exception {
		BigDecimal balance = walletService.getWalletByAddress("n4ZEsTqSe1ad155kb3JYEnuFsz6Eyp7Dzk").getBalance();
		BigDecimal total = balance.add(BigDecimal.valueOf(10));
		WalletVO walletVO = new WalletVO();
		walletVO.setAddress("n4ZEsTqSe1ad155kb3JYEnuFsz6Eyp7Dzk");
		walletVO.setBalance(total);
		walletService.updateWalletByAddress(walletVO);
		System.out.println(CommonUtil.convertJsonStringFromObject(walletService.getWalletByAddress("n4ZEsTqSe1ad155kb3JYEnuFsz6Eyp7Dzk")));
	}
	
}
