package cn.gmj.taotao.item.common.vo;

import lombok.Data;

import java.util.Date;


@Data
public class SpuVo {
    private Long id;
    private Long brandId;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private String title;
    private String subTitle;
    private Boolean saleable;
    private Date createTime;
    private String cname;
    private String bname;
}

