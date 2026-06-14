package org.bigearpig.sys.module.websocket.controller.mo;

import lombok.Data;

import java.io.Serializable;
@Data
public class PrivateMo implements Serializable {

    private Long userId;

    private String message;

}
