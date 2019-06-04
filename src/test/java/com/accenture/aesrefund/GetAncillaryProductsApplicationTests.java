package com.accenture.aesrefund;

import com.accenture.aesrefund.model.FopVo;
import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetAncillaryProductsApplicationTests extends Thread{

	@Test
	public void contextLoads(){


	}



	@Test
	public void testArrayLists(){

		List<FopVo> list  =new ArrayList<FopVo>();
		FopVo fopVo = new FopVo();
		fopVo.setFopCode("1232");
		fopVo.setFopFreeText("1231");
		list.add(fopVo);

		Iterator<FopVo> it = list.iterator();
		while (it.hasNext()){
			Object object = it.next();
			System.out.println(object);
		}

	}


	@Test
	public void testThread(){

	}

}
