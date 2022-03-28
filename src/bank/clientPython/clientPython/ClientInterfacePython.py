import socket
from clientMessage.RequestPython import *
from clientBank.EnumPython import OpType
import ClientMessageHandlerPython
class ClientInterface:
    def __init__(self, port,serverIP) -> None:
        try:
            self.ds = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
            self.port = port
            self.request_id = 0
            self.serverIP = serverIP

        except:
            pass

    def openAccount(self, account):
        request = Request(self.request_id, OpType.CREATE_ACCOUNT.value, [account])
        content = ClientMessageHandlerPython.marshal(request)
        self.sendRequest(self.serverIP, self.port, content)
        response = self.receiveResponse()
        return response

        
    def closeAccount(self, account):
        request = Request(self.request_id, OpType.CLOSE_ACCOUNT.value, [account])
        content = ClientMessageHandlerPython.marshal(request)
        self.sendRequest(self.serverIP, self.port, content)
        response = self.receiveResponse()
        return response

    def depositMoney(self, account, amount):
        request = Request(self.request_id, OpType.DEPOSIT_MONEY.value, [account,amount])
        content = ClientMessageHandlerPython.marshal(request)
        self.sendRequest(self.serverIP, self.port, content)
        response = self.receiveResponse()
        return response

    def withdrawMoney(self, account, amount):
        request = Request(self.request_id, OpType.WITHDRAW_MONEY.value, [account,amount])
        content = ClientMessageHandlerPython.marshal(request)
        self.sendRequest(self.serverIP, self.port, content)
        response = self.receiveResponse()
        return response


    def transferMoney(self, accountFrom, accountTo, amount):
        request = Request(self.request_id, OpType.TRANSFER_MONEY.value, [accountFrom, accountTo, amount])
        content = ClientMessageHandlerPython.marshal(request)
        self.sendRequest(self.serverIP, self.port, content)
        response = self.receiveResponse()
        return response

    def transationHistory(self, account):
        request = Request(self.request_id, OpType.TRANSACTION_HISTORY.value, [account])
        content = ClientMessageHandlerPython.marshal(request)
        self.sendRequest(self.serverIP, self.port, content)
        response = self.receiveResponse()
        return response

    def monitorUpdates():
        pass

    def sendRequest():
        retry = 0
        while retry < 3:
            try:
                pass
            except:
                pass
        

    def receiveResponse():
        pass
