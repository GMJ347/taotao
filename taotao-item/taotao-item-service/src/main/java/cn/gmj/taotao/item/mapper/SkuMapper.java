package cn.gmj.taotao.item.mapper;

import cn.gmj.taotao.item.common.pojo.Sku;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

@Repository
public interface SkuMapper extends Mapper<Sku>, InsertListMapper<Sku> {
}
