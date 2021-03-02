package cn.gmj.taotao.user.service;

import cn.gmj.taotao.common.Utils.CodecUtils;
import cn.gmj.taotao.common.Utils.NumberUtils;
import cn.gmj.taotao.user.common.pojo.User;
import cn.gmj.taotao.user.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "user:verify:phone";

    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new RuntimeException("checkData方法输入参数错误");
        }
        return userMapper.selectCount(record) == 0;
    }

    public void sendCode(String phone) {
        String key = KEY_PREFIX + phone;
        Map<String, String> msg = new HashMap<>();
        String code = NumberUtils.generateCode(6);
        code = "666666";
        msg.put("phone", phone);
        msg.put("code", code);  // todo
        // 发送验证码
        amqpTemplate.convertAndSend("taotao.sms.exchange", "sms.verify.code", msg);
        // 保存验证码到redis
        stringRedisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }

    public Boolean register(User user, String code) {
        String cacheCode = stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (!StringUtils.equals(code, cacheCode)) return false;
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        user.setId(null);
        user.setCreated(new Date());
        boolean result = userMapper.insertSelective(user) == 1;
        if (result) {
            stringRedisTemplate.delete(KEY_PREFIX + user.getPhone());
        }
        return result;
    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if (user == null) return null;
        if (!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))) return null;
        return user;
    }
}
