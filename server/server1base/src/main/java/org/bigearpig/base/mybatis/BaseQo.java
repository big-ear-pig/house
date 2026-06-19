package org.bigearpig.base.mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseQo implements Serializable {

	private static final long serialVersionUID = 1L;

	private long size = 10;

	private long current = 1;

	private List<BaseOrderItem> orderItemList=new ArrayList<>();


}
