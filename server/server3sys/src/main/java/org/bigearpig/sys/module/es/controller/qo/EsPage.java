package org.bigearpig.sys.module.es.controller.qo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EsPage {

    private int current = 1;

    private int size = 20;

    private List<EsOrderItem> orderItemList = new ArrayList<>();
}
