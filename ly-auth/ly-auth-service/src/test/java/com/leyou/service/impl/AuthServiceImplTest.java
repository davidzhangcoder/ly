package com.leyou.service.impl;

import com.leyou.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceImplTest {

    @Autowired
    private AuthService authService;

//    @Test
    public void getToken() {
    }

    @Test
    public void getTokenCSV(){
        try {
            File csv = new File("//Users//davidzhang//Documents//dockerworkspace//Token.csv");//CSV文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
            for (int i =1; i <= 1; i++) {
                //新增一行数据
                String token = this.authService.authorize("username"+i,"abcdefg"+i);
                bw.write("username"+i+","+token);
                bw.newLine();
            }
            bw.close();
        } catch (FileNotFoundException e) {
            //捕获File对象生成时的异常
            e.printStackTrace();
        } catch (IOException e) {
            //捕获BufferedWriter对象关闭时的异常
            e.printStackTrace();
        }
    }

}