package com.basquiat.address.service.wallet;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service("walletService")
public class WalletService {

	private static final Logger LOG = LoggerFactory.getLogger(WalletService.class);
	
	@Value("${redis.queue.key}")
	private String REDIS_QUEUE_KEY;
	
	@Value("${redis.retry}")
	private Integer REDIS_QUEUE_RETRY;
	
	@Autowired
	private RedisMessagePublisher redisMessagePublisher;
	
	@Autowired
	private WalletMapper walletMapper;
	
	@Autowired
	private ApplicationContext context;
	
	@Resource(name = "redisTemplate") 
	private ListOperations<String, String> listQueueOperations;
	
	/**
	 * 주소 생성
	 * @param walletVO
	 * @throws Exception
	 */
	@Transactional
	public void createWallet() {

		// queue에서 생성할 정보를 가져온다.
		// queue에서 정보를 가져오는 이유는 클러스터 환경에서 subscribe한 모든 consumer들은 같은 로직을 태울 수밖에 없다.
		// queue에서 pop을 하게 되면 먼저 가져간 consumer에 의해 한해서 로직이 구현된다.
		String requestCreateWalletInfo = listQueueOperations.leftPop(REDIS_QUEUE_KEY);
		if(requestCreateWalletInfo != null ) {
			try {
				WalletVO walletVO = CommonUtil.convertObjectFromJsonString(requestCreateWalletInfo, WalletVO.class);
				// userId, coinId로 셀렉트해서 존재하는지 확인
				WalletVO selectWallet = this.getWallet(walletVO);
				if( selectWallet == null || selectWallet.getAddress() == null || selectWallet.getAddress().toString().equals("")) {
					
					BlockChainNodeInterface node = CommonUtil.createInstance(walletVO.getCoinType().toLowerCase(), context);
					
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
				
			} catch (Exception e) {
				//에러가 발생했을 경우에는 requestCreateWalletInfo정보를 다시 retry로 메세지를 보낸다.
				try {
					WalletVO retryMessageVO = CommonUtil.convertObjectFromJsonString(requestCreateWalletInfo, WalletVO.class);
					//retry시에는 retry변수를 하나씩 증가해서 다시 요청한다.
					//무한 반복을 회피하기 위해서 retry횟수가 일정 회수에 도달하면 그냥 에러 메세지를 보낸다.
					if(retryMessageVO.getRetry() == REDIS_QUEUE_RETRY) {
						// 메세지는 정해서 하자
						redisMessagePublisher.publish("Please Create Address Again");
					} else {
						// 위에서 생성한 주소가 업데이트 이후에 발생한 에러인지 한번더 확인해야한다.
						// 주소가 업데이트 되지 않았다면 주소 생성 요청을 다시한다.
						WalletVO checkWallet = this.getWallet(retryMessageVO);
						if( checkWallet == null || checkWallet.getAddress() == null || checkWallet.getAddress().toString().equals("")) {
							int retry = retryMessageVO.getRetry()+1;
							retryMessageVO.setRetry(retry);
							listQueueOperations.rightPush(REDIS_QUEUE_KEY, CommonUtil.convertJsonStringFromObject(retryMessageVO));
							redisMessagePublisher.retry(CommonUtil.convertJsonStringFromObject(retryMessageVO));
						}
					}
				} catch (Exception e1) {
					// requestCreateWalletInfo가 API 서버로부터 잘못 왔기 때문에 String에서 Object로 매핑하는 과정중 발생하는 이 에러는 에러메세지를 publisher한다.
					// 에러 메세지 내용 및 형식은 차후에 정해서 보내주자
					redisMessagePublisher.publish("Request Info is Bad Request");
				}
			}
		} else {
			LOG.info("Already Queue is Pop by Other Node!!!!!");
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
	@Transactional
	public void updateWalletByAddress(WalletVO walletVO) throws Exception {
		walletMapper.updateWalletByAddress(walletVO);
	}
	
	/**
	 * get tag sequence for ripple destination tag
	 * @return Integer
	 */
	private Integer getTagSequence() throws Exception {
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
