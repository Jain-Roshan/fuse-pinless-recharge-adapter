package com.telkom.utility;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telkom.balanceTopup.Amount;
import com.telkom.balanceTopup.BalanceTopup;
import com.telkom.balanceTopup.Channel;
import com.telkom.balanceTopup.Characteristic;
import com.telkom.balanceTopup.Product;

import za.co.telkom.Recharge;
import za.co.telkom.ValidateAccount;
import za.co.telkom.eai.billing.topupenquirymanage.TopUpEnquiryManage;
import za.co.telkom.eai.billing.topuprechargemanage.RequestType;

public class Mapper {
	private static Logger logger = LoggerFactory.getLogger(Mapper.class);

	public static Recharge mapRechargeXmlValuesToPojo(String body) throws Exception {
		Recharge result = null;
		try {
			JAXBContext instance = JAXBContext.newInstance(Recharge.class);

			Unmarshaller unmarshaller = instance.createUnmarshaller();

			StringReader reader = new StringReader(body);
			logger.info("body==  " + body);
			logger.info("reader==  " + reader);

			result = (Recharge) unmarshaller.unmarshal(reader);

		} catch (Exception ex) {
			logger.error("Problem occured during marshalling in mapper : " + ex.getMessage());
		}
		return result;

	}
	
	
	public static ValidateAccount mapValidateAccountXmlValuesToPojo(String body) throws Exception {
		ValidateAccount result = null;
		try {
			JAXBContext instance = JAXBContext.newInstance(ValidateAccount.class);

			Unmarshaller unmarshaller = instance.createUnmarshaller();

			StringReader reader = new StringReader(body);
			logger.info("body==  " + body);
			logger.info("reader==  " + reader);

			result = (ValidateAccount) unmarshaller.unmarshal(reader);

		} catch (Exception ex) {
			logger.error("Problem occured during marshalling in mapper : " + ex.getMessage());
		}
		return result;

	}
	
	public static BalanceTopup mapBalanceTopupFromRecharge(Recharge recharge) {
		logger.info("Inside getBalanceTopupFromRecharge mapping recharge to balanceTopup ");
		BalanceTopup balanceTopup = null;
		if(recharge != null && recharge.getTopUpRechargeManage() != null && recharge.getTopUpRechargeManage().getRequest() != null) {
			balanceTopup = new BalanceTopup();
			RequestType requestType = recharge.getTopUpRechargeManage().getRequest();
			
			//characteristics
			List<Characteristic> characteristics = new ArrayList<>();
			if(null != requestType.getTransactionType())
			{
				Characteristic transactionTypeCharacteristic = new Characteristic();
				transactionTypeCharacteristic.setName("TransactionType");
				transactionTypeCharacteristic.setValue(requestType.getTransactionType());
				characteristics.add(transactionTypeCharacteristic);
			}
			
			if(null != requestType.getTransactionType())
			{
				Characteristic rechargeReferenceNumberCharacteristic = new Characteristic();
				rechargeReferenceNumberCharacteristic.setName("RechargeReferenceNumber");
				rechargeReferenceNumberCharacteristic.setValue(requestType.getTopUpReferenceNumber());
				characteristics.add(rechargeReferenceNumberCharacteristic);
			}
			
			if(null != requestType.getSessionID())
			{
				Characteristic sessionIDCharacteristic = new Characteristic();
				sessionIDCharacteristic.setName("SessionID");
				sessionIDCharacteristic.setValue(requestType.getSessionID());
				characteristics.add(sessionIDCharacteristic);
			}
			/*Characteristic terminalIDCharacteristic = new Characteristic();
			terminalIDCharacteristic.setName("TerminalID");
			terminalIDCharacteristic.setValue(requestType.getSessionID());
			characteristics.add(terminalIDCharacteristic);*/
			
			if(null != requestType.getServicePackCode())
			{
				Characteristic servicePackCodeCharacteristic = new Characteristic();
				servicePackCodeCharacteristic.setName("ServicePackCode");
				servicePackCodeCharacteristic.setValue(requestType.getServicePackCode());
				characteristics.add(servicePackCodeCharacteristic);
			}
			//set balanceTopup
			balanceTopup.setType(requestType.getProductId());
			
			if(requestType.getVoucherInfo() != null) {
				balanceTopup.setVoucher(requestType.getVoucherInfo().getVoucherNumber());	
			}
			
			Channel channel = new Channel();
			channel.setId(requestType.getMechantId());
			balanceTopup.setChannel(channel);
			
			Product product = new Product();
			product.setId(requestType.getServiceId());
			balanceTopup.setProduct(product);
			
			Amount amount = new Amount();
			amount.setAmount(Integer.valueOf(requestType.getRechargeAmount().toString()));
			balanceTopup.setAmount(amount);
			
			balanceTopup.setCharacteristic(characteristics);
		}
		logger.info("Exit from getBalanceTopupFromRecharge ");
		return balanceTopup;
	}
	
	
	public static BalanceTopup mapBalanceTopupFromValidateAccount(ValidateAccount validateAccount) {
		logger.info("Inside getBalanceTopupFromValidateAccount mapping validateAccount to balanceTopup ");
		BalanceTopup balanceTopup = null;
		if(validateAccount != null && validateAccount.getTopUpEnquiryManage() != null && validateAccount.getTopUpEnquiryManage().getRequest() != null) {
			balanceTopup = new BalanceTopup();
			TopUpEnquiryManage.Request request = validateAccount.getTopUpEnquiryManage().getRequest();
			
			//characteristics
			List<Characteristic> characteristics = new ArrayList<>();
			if(null != request.getTransactionType())
			{
				Characteristic transactionTypeCharacteristic = new Characteristic();
				transactionTypeCharacteristic.setName("TransactionType");
				transactionTypeCharacteristic.setValue(request.getTransactionType());
				characteristics.add(transactionTypeCharacteristic);
			}
			
			if(null != request.getTopUpReferenceNumber())
			{
				Characteristic rechargeReferenceNumberCharacteristic = new Characteristic();
				rechargeReferenceNumberCharacteristic.setName("RechargeReferenceNumber");
				rechargeReferenceNumberCharacteristic.setValue(request.getTopUpReferenceNumber());
				characteristics.add(rechargeReferenceNumberCharacteristic);
			}
			
			if(null != request.getSessionID())
			{
				Characteristic sessionIDCharacteristic = new Characteristic();
				sessionIDCharacteristic.setName("SessionID");
				sessionIDCharacteristic.setValue(request.getSessionID());
				characteristics.add(sessionIDCharacteristic);
			}
			
		    /*Characteristic terminalIDCharacteristic = new Characteristic();
			terminalIDCharacteristic.setName("TerminalID");
			terminalIDCharacteristic.setValue(request.getSessionID());
			characteristics.add(terminalIDCharacteristic);*/
			
			if(null != request.getServicePackCode())
			{
				Characteristic servicePackCodeCharacteristic = new Characteristic();
				servicePackCodeCharacteristic.setName("ServicePackCode");
				servicePackCodeCharacteristic.setValue(request.getServicePackCode());
				characteristics.add(servicePackCodeCharacteristic);
			}
			
			//set balanceTopup
			balanceTopup.setType(request.getProductID());
			
			if(request.getVoucherInfo() != null) {
				balanceTopup.setVoucher(request.getVoucherInfo().getVoucherNumber());	
			}
			
			Channel channel = new Channel();
			channel.setId(request.getMerchantID());
			balanceTopup.setChannel(channel);
			
			Product product = new Product();
			product.setId(request.getCallingLineID());
			balanceTopup.setProduct(product);
			
			Amount amount = new Amount();
			amount.setAmount(Integer.valueOf(request.getRequestedAmount()));
			balanceTopup.setAmount(amount);
			
			balanceTopup.setCharacteristic(characteristics);
		}
		logger.info("Exit from getBalanceTopupFromValidateAccount ");
		return balanceTopup;
	}
	
}
