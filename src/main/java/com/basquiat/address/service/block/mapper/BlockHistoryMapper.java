package com.basquiat.address.service.block.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.basquiat.address.service.block.vo.BlockHistoryVO;

/**
 * BlockHistoryMapper
 * 
 * created by basquiat
 *
 */
@Mapper
public interface BlockHistoryMapper {

	/**
	 * 최신 블록으로 업데이트 한다.
	 * @param blockHistoryVO
	 */
	public void updateBlockHistory(BlockHistoryVO blockHistoryVO);
	
	/**
	 * select 마지막으로 체크한 블록을 셀렉트한다.
	 * @param blockHistoryVO
	 * @return BlockHistoryVO
	 */
	public BlockHistoryVO selectBlockHistory(String coinType);
	
}
