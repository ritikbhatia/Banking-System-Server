from os import sep
import socket

from numpy import float64
from clientMain.ClientInterfacePython import ClientInterface
from clientBank.EnumPython import Currency
from clientBank.EnumPython import OpType


def getName():
    accountHolderName = input("Enter the account holder's name: ")
    return(accountHolderName)


def getPassword():
    while True:
        try:
            password = input("Enter the password: ")
            if len(password) < 1:
                print("Error: Please enter a password of length atleast 1 character!")
                continue
        except:
            print('Invalid Input!')
            continue
        return(password)


def getCurrency():
    for cur in (Currency):
        print(cur.value, end='. ')
        print(cur._name_)
    while True:
        try:
            currency_index = int(input('Select the currency: '))
            if currency_index <= 3 and currency_index > 0:
                currency = Currency(currency_index)
            else:
                print("Invalid Choice. Please choose again!")
                continue
        except:
            print('Invalid Input!')
            continue
        return(currency.value)


def getAccountBalance():
    while True:
        try:
            accountBalance = float(input("Enter the balance amount: "))
            if accountBalance < 0:
                print("Invalid input. The balance amount cannot be negative!")
                continue
        except:
            print("Invalid Input!")
            continue
        return(accountBalance)


def getAccountNumber():
    while True:
        try:
            accountNumber = int(input("Enter the bank account number: "))
        except:
            print("Invalid Input!")
            continue
        return(accountNumber)


def createNewAccountMain(clientInterface):
    print("Creating a new account.")
    lineBreak(45)
    accountHolderName = getName()
    password = getPassword()
    currency = getCurrency()
    accountBalance = getAccountBalance()
    print()
    content = [accountHolderName, password, currency, accountBalance]
    response = clientInterface.startService(
        content, OpType.CREATE_ACCOUNT.value)
    print(response)


def closeAccountMain(clientInterface):
    print("Closing the account.")
    lineBreak(45)
    accountHolderName = getName()
    password = getPassword()
    accountNumber = getAccountNumber()
    print()
    content = [accountHolderName, password, accountNumber]
    response = clientInterface.startService(
        content, OpType.CLOSE_ACCOUNT.value)
    print(response)


def depositMoneyMain(clientInterface):
    print("Depositing Money.")
    lineBreak(45)
    accountHolderName = getName()
    password = getPassword()
    currency = getCurrency()
    accountNumber = getAccountNumber()
    amount = 0
    while True:
        try:
            amount = float64(input("Enter the amount to be deposited: "))
            if amount <= 0:
                print("Invalid amount!")
                continue
        except:
            print("Invalid Input!")
            continue
        break
    print()
    content = [accountHolderName, password, currency, amount, accountNumber]
    response = clientInterface.startService(
        content, OpType.DEPOSIT_MONEY.value)
    print(response)


def withdrawMoneyMain(clientInterface):
    print("Withdrawing money.")
    lineBreak(45)
    accountHolderName = getName()
    password = getPassword()
    currency = getCurrency()
    accountNumber = getAccountNumber()
    amount = 0
    while True:
        try:
            amount = float64(input("Enter the amount to be withdrawn: "))
            if amount <= 0:
                print("Invalid amount!")
                continue
        except:
            print("Invalid Input!")
            continue
        break
    print()
    content = [accountHolderName, password, currency, amount, accountNumber]
    response = clientInterface.startService(
        content, OpType.WITHDRAW_MONEY.value)
    print(response)


def transferMoneyMain(clientInterface):
    print("Transferring money")
    lineBreak(45)
    print()
    print("Transferring from:")
    accountHolderName = getName()
    password = getPassword()
    currency = getCurrency()
    accountNumber = getAccountNumber()
    print()
    print("Transferring to:")
    accountNumberTo = getAccountNumber()
    amount = 0
    while True:
        try:
            amount = float64(input("Enter the amount to be transferred: "))
            if amount <= 0:
                print("Invalid amount!")
                continue
        except:
            print("Invalid Input!")
            continue
        break
    print()
    content = [accountHolderName, password, currency,
               accountNumber, accountNumberTo, amount]
    response = clientInterface.startService(
        content, OpType.TRANSFER_MONEY.value)
    print(response)


def viewTransactionHistoryMain(clientInterface):
    print("Viewing transaction history.")
    lineBreak(45)

    accountHolderName = getName()
    password = getPassword()
    accountNumber = getAccountNumber()
    content = [accountHolderName, password, accountNumber]
    response = clientInterface.startService(
        content, OpType.TRANSACTION_HISTORY.value)
    print(response)


def monitorUpdatesMain(clientInterface):
    print("Monitoring Updates.")
    lineBreak(45)
    interval = 0
    while True:
        try:
            interval = int(input("Enter the interval: "))
        except:
            print("Invalid Input!")
            continue
        break
    content = [interval]
    clientInterface.monitorUpdates(
        content, OpType.MONITOR_UPDATES.value)


def printOptions():
    lineBreak(45)
    print("Enter your choice!")
    lineBreak(45)
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
        print('-', end='')
    print()


serverPort = 6789  # Modify this value to change Server port
# serverIp = socket.gethostbyname('RITIK-DELL-XPS') # Modify this to change Server IP
serverIp = '192.168.0.130'
simulate = False
clientLossRate = 0.0
# INITIALIZE THE LISTENER11

print('Starting bank client...')
clientInterface = ClientInterface(
    serverPort, serverIp, simulate, clientLossRate)
while True:
    print()
    printOptions()
    option = None
    while True:
        try:
            option = int(input())
        except:
            print('Invalid Input. Please enter again!')
            continue
        if option > 0 and option <= 8:
            break

    if option == 8:
        print('The program has ended!')
        break
    print()
    lineBreak(45)
    options = {
        1: createNewAccountMain,
        2: closeAccountMain,
        3: depositMoneyMain,
        4: withdrawMoneyMain,
        5: transferMoneyMain,
        6: viewTransactionHistoryMain,
        7: monitorUpdatesMain
    }
    options[option](clientInterface)
