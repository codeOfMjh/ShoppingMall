package com.mjh.mapper;

import com.mjh.my.mapper.MyMapper;
import com.mjh.pojo.Items;
import com.mjh.pojo.vo.ItemCommentVO;
import com.mjh.pojo.vo.SearchItemsVO;
import com.mjh.pojo.vo.ShopCartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom extends MyMapper<Items> {

    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

    public List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);

    public List<SearchItemsVO> searchItemsByThirdCategory(@Param("paramsMap") Map<String, Object> map);

    public List<ShopCartVO> queryItemsBySpecIds(@Param("paramList") List<String> specIds);

    public int decreaseItemSpecStock(@Param("specId") String specId, @Param("pendingCounts") int pendingCounts);
}