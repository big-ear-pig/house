package org.bigearpig.sso.module.user.service;

import org.bigearpig.common.UserInfo;
import org.bigearpig.sso.module.user.controller.mo.LoginMo;

public interface UserService {


     String login( LoginMo mo) ;

     UserInfo info();
}
