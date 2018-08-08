package com.basquiat.address.service.block;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basquiat.address.service.block.mapper.BlockHistoryMapper;
import com.basquiat.address.service.block.vo.BlockHistoryVO;

/**
 * 
 * 마지막으로 확인한 블록 넘버 확인 및 업데이트 서비스
 * 
 * created by basquiat
 *
 */
@Service("blockHistoryService")
public class BlockHistoryService {

	
	@Autowired
	private BlockHistoryMapper blockHistoryMapper;
	
	/**
	 * select block history
	 * @param coinType
	 * @throws Exception
	 */
	public BlockHistoryVO getBlockHistory(String coinType) throws Exception {
		return blockHistoryMapper.selectBlockHistory(coinType);
	}
	
	/**
	 * 마지막으로 확인한 블럭 넘버를 업데이트한다.
	 * @param blockHistoryVO
	 */
	@Transactional
	public void updateBlockHistory(BlockHistoryVO blockHistoryVO) throws Exception {
		blockHistoryMapper.updateBlockHistory(blockHistoryVO);
	}
	
}
