package com.uday.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uday.dto.ApplyATM;
import com.uday.dto.AtmTransactions;
import com.uday.dto.BalanceEnquireATM;
import com.uday.dto.CancleATM;
import com.uday.dto.CheckBalance;
import com.uday.dto.MiniStatementATM;
import com.uday.dto.SetAtmPin;
import com.uday.dto.Statement;
import com.uday.dto.TransferAmount;
import com.uday.dto.TransferByATM;
import com.uday.dto.UpdateAccount;
import com.uday.model.Bank;
import com.uday.service.BankService;

@WebMvcTest(BankController.class)
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- REGISTER ----------
    @Test
    void testCreateBankAccount() throws Exception {
        Bank bank = new Bank();
        bank.setAccountNumber(12345L);

        when(bankService.createBankAccount(any(Bank.class))).thenReturn(bank);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bank)))
                .andExpect(status().isOk());
    }
    
    @Test
    void testCreateBankAccount_Exception() throws Exception {

        Bank bank = new Bank();
        bank.setAccountNumber(12345L);

        when(bankService.createBankAccount(any(Bank.class)))
                .thenThrow(new IllegalArgumentException("Account already exists"));

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bank)))
                .andExpect(status().isBadRequest());
    }

    // ---------- GET ONE ----------
    @Test
    void testGetOneAccountHolder() throws Exception {
        Bank bank = new Bank();
        bank.setAccountNumber(12345L);

        when(bankService.getOneHolder(12345L)).thenReturn(bank);

        mockMvc.perform(get("/getone/12345"))
                .andExpect(status().isOk());
    }

    // ---------- GET ALL ----------
    @Test
    void testGetAllAccountHolders() throws Exception {
        when(bankService.getAllAccountHolders())
                .thenReturn(Arrays.asList(new Bank(), new Bank()));

        mockMvc.perform(get("/getall"))
                .andExpect(status().isOk());
    }

    // ---------- WITHDRAW ----------
    @Test
    void testWithdraw() throws Exception {
        Bank bank = new Bank();

        when(bankService.withdraw(any(Bank.class))).thenReturn("Withdraw Success");

        mockMvc.perform(post("/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bank)))
                .andExpect(status().isOk());
    }

    // ---------- DEPOSIT ----------
    @Test
    void testDeposit() throws Exception {
        Bank bank = new Bank();

        when(bankService.deposit(any(Bank.class))).thenReturn("Deposit Success");

        mockMvc.perform(post("/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bank)))
                .andExpect(status().isOk());
    }

    // ---------- CHECK BALANCE ----------
    @Test
    void testCheckBalance() throws Exception {
        CheckBalance req = new CheckBalance();

        when(bankService.balanceEnquire(any(CheckBalance.class)))
                .thenReturn("Balance: 5000");

        mockMvc.perform(post("/checkbalance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    // ---------- TRANSFER ----------
    @Test
    void testTransferAmount() throws Exception {
        TransferAmount req = new TransferAmount();

        when(bankService.transferMoney(any(TransferAmount.class)))
                .thenReturn("Transfer Success");

        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    // ---------- APPLY ATM ----------
    @Test
    void testApplyAtm() throws Exception {
        ApplyATM req = new ApplyATM();
        Bank bank = new Bank();

        when(bankService.applyForAtm(any(ApplyATM.class))).thenReturn(bank);

        mockMvc.perform(post("/applyatm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    // ---------- CANCEL ATM ----------
    @Test
    void testCancelAtm() throws Exception {
        CancleATM req = new CancleATM();
        Bank bank = new Bank();

        when(bankService.cancelAtm(any(CancleATM.class))).thenReturn(bank);

        mockMvc.perform(post("/cancelatm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    // ---------- SEND OTP ----------
    @Test
    void testSendOtp() throws Exception {
        SetAtmPin req = new SetAtmPin();
        req.setAtmNumber(123456);

        when(bankService.sendOtp(123456)).thenReturn("OTP Sent");

        mockMvc.perform(post("/send-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
    
    @Test
    void testSendOtp_Exception() throws Exception {

        SetAtmPin req = new SetAtmPin();
        req.setAtmNumber(123456); // use String if DTO is String

        when(bankService.sendOtp(123456))
                .thenThrow(new IllegalArgumentException("Invalid ATM Number"));

        mockMvc.perform(post("/send-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ---------- SET PIN ----------
    @Test
    void testSetPin() throws Exception {
        SetAtmPin req = new SetAtmPin();
        Bank bank = new Bank();

        when(bankService.setPin(any(SetAtmPin.class))).thenReturn(bank);

        mockMvc.perform(post("/set-pin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
    
    @Test
    void testSetPin_Exception() throws Exception {

        SetAtmPin req = new SetAtmPin();

        when(bankService.setPin(any(SetAtmPin.class)))
                .thenThrow(new IllegalArgumentException("Invalid PIN"));

        mockMvc.perform(post("/set-pin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetStatement() throws Exception {

        Statement statement = new Statement();

        doNothing().when(bankService).transactionStatement(any(Statement.class));

        mockMvc.perform(post("/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statement)))
                .andExpect(status().isOk());

        verify(bankService, times(1)).transactionStatement(any(Statement.class));
    }
    
    @Test
    void testMiniStatement() throws Exception {

        Statement statement = new Statement();

        doNothing().when(bankService).generateMiniStatement(any(Statement.class));

        mockMvc.perform(post("/ministatement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statement)))
                .andExpect(status().isOk());

        verify(bankService, times(1)).generateMiniStatement(any(Statement.class));
    }
    
    @Test
    void testMiniStatementByATM() throws Exception {

        MiniStatementATM mini = new MiniStatementATM();

        doNothing().when(bankService).miniStatementByATM(any(MiniStatementATM.class));

        mockMvc.perform(post("/miniStatementATM")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mini)))
                .andExpect(status().isOk());

        verify(bankService, times(1)).miniStatementByATM(any(MiniStatementATM.class));
    }
    @Test
    void testUpdateAccount() throws Exception {

        UpdateAccount update = new UpdateAccount();
        Bank bank = new Bank();

        when(bankService.updateAccount(any(UpdateAccount.class)))
                .thenReturn(bank);

        mockMvc.perform(post("/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());
    }

    // ---------- ATM WITHDRAW ----------
    @Test
    void testWithdrawATM() throws Exception {
        AtmTransactions req = new AtmTransactions();

        when(bankService.withdrawUsingATM(any(AtmTransactions.class)))
                .thenReturn("ATM Withdraw Success");

        mockMvc.perform(post("/withdrawATM")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    // ---------- ATM DEPOSIT ----------
    @Test
    void testDepositATM() throws Exception {
        AtmTransactions req = new AtmTransactions();

        when(bankService.depositUsingATM(any(AtmTransactions.class)))
                .thenReturn("ATM Deposit Success");

        mockMvc.perform(post("/depositATM")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    // ---------- ATM BALANCE ----------
    @Test
    void testBalanceATM() throws Exception {
        BalanceEnquireATM req = new BalanceEnquireATM();

        when(bankService.balanceEnquireByATM(any(BalanceEnquireATM.class)))
                .thenReturn("ATM Balance: 1000");

        mockMvc.perform(post("/balanceATM")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    // ---------- ATM TRANSFER ----------
    @Test
    void testTransferATM() throws Exception {
        TransferByATM req = new TransferByATM();

        when(bankService.transferUsingATM(any(TransferByATM.class)))
                .thenReturn("ATM Transfer Success");

        mockMvc.perform(post("/transferATM")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}