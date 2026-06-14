package org.bigearpig.sys.module.websocket.controller.mo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PublicMo implements Serializable {

        private String destination;

        private String message;

}
