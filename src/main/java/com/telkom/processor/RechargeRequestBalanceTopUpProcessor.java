package com.telkom.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telkom.balanceTopup.BalanceTopup;
import com.telkom.balanceTopup.BalanceTopupWrapper;
import com.telkom.utility.Mapper;

import za.co.telkom.Recharge;

public class RechargeRequestBalanceTopUpProcessor implements Processor{

	private static Logger logger = LoggerFactory.getLogger(RechargeRequestBalanceTopUpProcessor.class);
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("==========Inside RechargeRequestBalanceTopUpProcessor ==========" + exchange.getIn().getBody(String.class));
		//Recharge recharge = Mapper.mapRechargeXmlValuesToPojo(exchange.getIn().getBody(String.class));
		
		MessageContentsList result = (MessageContentsList)exchange.getIn().getBody();
		Recharge recharge = (Recharge)result.get(0);
		BalanceTopup balanceTopup = Mapper.mapBalanceTopupFromRecharge(recharge);
		BalanceTopupWrapper.BALANCE_TOPUP =  balanceTopup;
		logger.info("==========Exit from RechargeRequestBalanceTopUpProcessor sending balanceTopupRequest ==========");
		exchange.getIn().setBody(objectMapper.writeValueAsString(balanceTopup));
	}

}
