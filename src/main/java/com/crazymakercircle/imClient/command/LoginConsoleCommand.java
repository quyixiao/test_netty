package com.crazymakercircle.imClient.command;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Data
@Service("LoginConsoleCommand")
// 概述IM 的登录流程
// 单体IM 系统中，首先需要先合建，登录流程，从端到端（End to End） 的角度来说，包括台下环节 。
// 1. 客户端发送登陆数据包。
// 2. 服务端解码，解码登陆请求的Protobuf数据包解码
// 3. 服务器编码，编码登陆的响应的Protobuf 数据包编码 。
// 客户端解码，解码登陆响应的Protobuf数据包解码
// 从客户端到服务器端再到客户端，9 个环节的介绍如下 :
// 1. 首先，客户端收集用户ID和密码，这一步需要使用到LoginConsoleCommand 控制台命令类。
// 2. 然后，客户端发送Protobuf 数据包到客户端通道，这一步需要通过LoginSender发送器组装Protobuf数据包。
// 3. 客户端通道将Protobuf数据包发送到对端，这一步需要通过Netty底层来完成 。
// 4. 服务端通道将Protobuf数据包发送到对端，这一步需要通过Netty底层来完成 。
// 5. 服务端UserLoginHandler入站处理器收到登陆消息， 交给业务处理器LoginMsgProcesser处理异步的业务逻辑.
// 6. 服务端LoginMsgProcesser处理完异步的业务逻辑，就将处理结果写入到用户绑定的子通道
// 7. 服务器子通道将登陆响应的Protobuf数据帧写入到对端，这一步需要通过Netty 底层来完成 。
// 8. 客户端通道收到Protobuf合建响应的数据包，这一步需要通过Netty 底层来完成 。
// 9. 客户端LoginResponseHandler业务处理器处理登陆响应，例如设置登陆udyd，保存会话的SessionId 等等。
// LoginConsoleCommand 类：属于ClientCommand模块，它负责收集用户在控制台输入的用户ID和密码。
//
public class LoginConsoleCommand implements BaseCommand {
    public static final String KEY = "1";

    private String userName;
    private String password;

    @Override
    public void exec(Scanner scanner) {

        System.out.println("请输入用户信息(id:password)  ");
        String[] info = null;
        while (true) {
            String input = scanner.next();
            info = input.split(":");
            if (info.length != 2) {
                System.out.println("请按照格式输入(id:password):");
            } else {
                break;
            }
        }
        userName = info[0];
        password = info[1];
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "登录";
    }

}
