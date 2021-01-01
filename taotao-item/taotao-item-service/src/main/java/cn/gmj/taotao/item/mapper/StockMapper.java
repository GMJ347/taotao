package cn.gmj.taotao.item.mapper;

import cn.gmj.taotao.item.common.pojo.Stock;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface StockMapper extends Mapper<Stock>, InsertListMapper<Stock> {
}
