package com.telkom.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telkom.balanceTopup.BalanceTopup;
import com.telkom.balanceTopup.BalanceTopupWrapper;
import com.telkom.utility.HeadersWrapper;
import com.telkom.utility.Mapper;

import za.co.telkom.ValidateAccount;

public class ValidateAccountBalanceTopUpProcessor implements Processor{

	private static Logger logger = LoggerFactory.getLogger(ValidateAccountBalanceTopUpProcessor.class);
	ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("==========Inside ValidateAccountBalanceTopUpProcessor ==========");
		HeadersWrapper.HEADERS = exchange.getIn().getHeaders();
		MessageContentsList result = (MessageContentsList)exchange.getIn().getBody();
		ValidateAccount validateAccount = (ValidateAccount)result.get(0);
		//ValidateAccount validateAccount = Mapper.mapValidateAccountXmlValuesToPojo(exchange.getIn().getBody(String.class));
		BalanceTopup balanceTopup = Mapper.mapBalanceTopupFromValidateAccount(validateAccount);
		BalanceTopupWrapper.BALANCE_TOPUP =  balanceTopup;
		logger.info("==========Exit ValidateAccountBalanceTopUpProcessor =========="+objectMapper.writeValueAsString(balanceTopup));
		exchange.getIn().setBody(objectMapper.writeValueAsString(balanceTopup));
	}

}