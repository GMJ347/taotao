package cn.gmj.taotao.user.mapper;

import cn.gmj.taotao.user.common.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserMapper extends Mapper<User> {
}
