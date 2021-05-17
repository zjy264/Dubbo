package com.xxxx.api.mapper;

import com.xxxx.api.entity.Shopping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.api.entity.dto.ShopDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
public interface ShoppingMapper extends BaseMapper<Shopping> {
    @Select("SELECT m_bookmain.name,m_bookmain.pic,m_bookmain.price,m_shopping.size FROM m_shopping,m_bookmain WHERE m_shopping.book_name=m_bookmain.name AND m_shopping.username=#{username} AND m_shopping.buy=1")
    List<ShopDto> getBookShopping(@Param("username") String username);

    @Select("SELECT m_bookmain.name,m_bookmain.pic,m_bookmain.price,m_shopping.size FROM m_shopping,m_bookmain WHERE m_shopping.book_name=m_bookmain.name AND m_shopping.username=#{username} AND m_shopping.buy=0")
    List<ShopDto> getBookShopping_no_buy(@Param("username") String username);
}
