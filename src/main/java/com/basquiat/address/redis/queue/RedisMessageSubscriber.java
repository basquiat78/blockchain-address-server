package com.basquiat.address.redis.queue;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.basquiat.address.service.wallet.WalletService;
import com.basquiat.address.service.wallet.vo.WalletVO;
import com.basquiat.address.util.CommonUtil;

/**
 * Redis Subscriber Service
 * created by basquiat
 *
 */
public class RedisMessageSubscriber implements MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(RedisMessageSubscriber.class);
	
	private WalletService walletService;
	
	private String subChannel;
	
	public RedisMessageSubscriber(String channel, WalletService walletService) {
		this.subChannel = channel;
		this.walletService = walletService;
	}
	
    public static List<String> messageList = new ArrayList<String>();

    public void onMessage(final Message message, final byte[] pattern) {
        String patternString = new String(pattern, 0, pattern.length);
        //new String(message.getBody())
        // 수많은 채널중 필요한 채널만 걸려서 로그를 찍고 서비스를 태워야 한다.
        // 메세지로부터 객체로 변환시키고 변환된 메세지를 통해서 DB에 인서트 또는 업데이트 로직을 태운다.
        if(subChannel.equals(patternString)) {
        	LOG.info("Request Channel is " + patternString);
        	LOG.info("Request Info : " + new String(message.getBody()));
        	try {
				walletService.createWallet(CommonUtil.convertObjectFromJsonString(message.toString(), WalletVO.class));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}