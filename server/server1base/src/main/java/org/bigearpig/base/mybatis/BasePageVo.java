package org.bigearpig.base.mybatis;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class BasePageVo<T> implements Serializable {

    private List<T> records = Collections.emptyList();
    private long total = 0;
    private long size = 10;
    private long current = 1;

}
