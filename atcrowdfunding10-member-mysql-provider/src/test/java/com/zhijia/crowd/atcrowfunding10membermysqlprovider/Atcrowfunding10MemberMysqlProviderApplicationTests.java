package com.zhijia.crowd.atcrowfunding10membermysqlprovider;

import com.zhijia.crowd.entity.po.MemberPO;
import com.zhijia.crowd.entity.vo.*;
import com.zhijia.crowd.handler.OrderProviderHandler;
import com.zhijia.crowd.mapper.MemberPOMapper;
import com.zhijia.crowd.mapper.OrderProjectPOMapper;
import com.zhijia.crowd.mapper.ProjectPOMapper;
import com.zhijia.crowd.service.api.OrderService;
import com.zhijia.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@SpringBootTest
class Atcrowfunding10MemberMysqlProviderApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private MemberPOMapper memberPOMapper;

	@Autowired
	private ProjectPOMapper projectPOMapper;
	@Autowired
	private OrderProjectPOMapper orderProjectPOMapper;
	@Autowired
	private OrderProviderHandler orderProviderHandler;
	@Autowired
	private OrderService orderService;


	@Test
	void testMapper()  {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encode = passwordEncoder.encode("123123");

		memberPOMapper.insert(new MemberPO(null,"jack",encode,"杰克","jack@qq.com",1,1,"杰克","123233",2));

	}


	@Test
	void contextLoads() throws SQLException {
		Connection connection = dataSource.getConnection();
		log.info(connection.toString());
	}

	@Test
	public void testLoadTypeData(){
		List<PortalTypeVO> typeVOList = projectPOMapper.selectPortalTypeOVList();
		for (PortalTypeVO portalTypeVO : typeVOList) {
			System.out.println(portalTypeVO.getId() +" "+ portalTypeVO.getName() +" "+ portalTypeVO.getRemark());
			List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
			for (PortalProjectVO portalProjectVO : portalProjectVOList) {
				System.out.println(portalProjectVO);
			}
		}
	}

	@Test
	public void testLoadDetailProjectVO(){
		DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(6);
		System.out.println(detailProjectVO);
	}

	@Test
	public void testOrderProjectVO(){
//		OrderProjectVO orderProjectVO = orderProjectPOMapper.selectOrderProjectVO(2);
//		System.out.println(orderProjectVO);
//		ResultEntity<OrderProjectVO> orderProjectVORemote = orderProviderHandler.getOrderProjectVORemote(null, 3);
//		System.out.println(orderProjectVORemote);
		List<AddressVO> addressVOList = orderService.getAddressVOList(2);
		System.out.println(addressVOList);
	}
}
