from os import sep
import socket

from click import pass_context
from clientBank.EnumPython import Currency
from clientBank.AccountPython import Account
from clientPython.ClientInterfacePython import ClientInterface


def getInputDetails(iaccountHolderName,  ipassword,  icurrency, iaccountBalance,  iaccountNumber) -> Account:
        accountHolderName = ''
        password = ''
        currency = ''
        accountBalance = ''
        accountNumber = Account.DEFAULT_ACCOUNT_NUMBER

        if iaccountHolderName == True:
            accountHolderName = input("Enter the account holder's name: ")
        
        if ipassword == True:
            while True:
                try:
                    password = input("Enter the password: ")
                    if len(password)!=10:
                        print("Error: Please enter a password of length atleast 10 characters!")
                        continue
                except:
                    print('Invalid Input!')
                    continue
                break

        if icurrency == True:
            index = 1
            for cur in (Currency):
                print(cur.value, end='. ')
                print(cur._name_)
                index+=1
            while True:
                try:
                    currency_index = int(input('Select the currency: '))
                    if currency_index<=index and currency_index>0:
                        currency = Currency(currency_index)
                    else:
                        print("Invalid Choice. Please choose again!")
                except:
                    print('Invalid Input!')
                    continue
                break
        
        if iaccountBalance == True:
            while True:
                try:
                    accountBalance = float(input("Enter the balance amount: "))
                    if accountBalance < 0:
                        print("Invalid input. The balance amount cannot be negative!")
                        continue
                except:
                    print("Invalid Input!")
                    continue
                break
        
        if iaccountNumber == True:
            while True:
                try:
                    accountNumber = int(input("Enter the bank account number: "))
                except:
                    print("Invalid Input!")
                    continue
                break

        account = Account(accountNumber, accountHolderName, password, currency, accountBalance)             
        return account

   
def createNewAccountMain(self, clientInterface):
        print("Creating a new account.")
        account = self.getInputDetails(True,True,True,True,False) 
        response = clientInterface.openAccount(self, account)
        print(response)


def closeAccountMain(self,clientInterface):
        print("Closing the account.")
        account = self.getInputDetails(True,True,True,False,True) 
        response = clientInterface.closeAccount(self, account)
        print(response)


def depositMoneyMain(self,clientInterface):
        print("Depositing Money.")
        account = self.getInputDetails(True,True,True,False,True) 
        amount = 0
        while True:
            try:
                amount = int(input("Enter the amount to be deposited"))
                if amount<=0:
                    print("Invalid amount!")
                    continue
            except:
                print("Invalid Input!")
                continue
            break
        response = clientInterface.depositMoney(self, account)
        print(response)


def withdrawMoneyMain(self,clientInterface):
        print("Withdrawing money.")
        account = self.getInputDetails(True,True,True,True,False) 
        amount = 0
        while True:
            try:
                amount = int(input("Enter the amount to be withdrawn"))
                if amount<=0:
                    print("Invalid amount!")
                    continue
            except:
                print("Invalid Input!")
                continue
            break
        response = clientInterface.withdrawMoney(self, account, amount)
        print(response)


def transferMoneyMain(self,clientInterface):
        print("Transferring money")
        print("Transferring from:")
        account = self.getInputDetails(True,True,False,False,True) 
        print("Transferring to:")
        account_to = self.getInputDetails(False,False, False, False, True)
        amount = 0
        while True:
            try:
                amount = int(input("Enter the amount to be transferred"))
                if amount<=0:
                    print("Invalid amount!")
                    continue
            except:
                print("Invalid Input!")
                continue
            break
        response = clientInterface.transferMoney(self, account, account_to, amount)
        print(response)

def viewTransactionHistoryMain(self,clientInterface):
        print("Viewing transaction history.")
        account = self.getInputDetails(True,True,True,False,True) 
        response = clientInterface.transactionHistory(account)
        print(response)


def monitorUpdatesMain(self,clientInterface):
        print("Monitoring Updates.")
        account = self.getInputDetails(True,True,True,True,False) 
        response = clientInterface.monitorUpdates(account)
        #### NEED TO DOOOO


def printOptions():
        print("Enter your choice!")
        print("1. Open Account")
        print("2. Close Existing Account")
        print("3. Deposit Money")
        print("4. Withdraw Money")
        print("5. Transfer Money")
        print("6. View Transaction History")
        print("7. Monitor Updates")
        print("8. Exit!")

def lineBreak(length):
        for i in range(length):
            print('-',end='')
    

serverPort = 1234
# INITIALIZE THE LISTENER
print('Starting bank client...')
clientInterface = ClientInterface()
while True:
    printOptions()
    option = None
    while True:
        try:
            option=input()
        except:
            print('Invalid Input. Please enter again!')
            continue
        if option>0 and option<= 8:
            break
            
    if option == 8:
        print('The program has ended!')
        break
    print()
    lineBreak(25)
    options = {
            1:'createNewAccountMain',
            2:'closeAccountMain',
            3:'depositMoneyMain',
            4:'withdrawMoneyMain',
            5:'transferMoneyMain',
            6:'viewTransactionHistoryMain',
            7:'monitorUpdatesMain'}
    options[option](clientInterface)
