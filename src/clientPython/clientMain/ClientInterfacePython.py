import socket
from clientMessage.RequestPython import *
from clientBank.EnumPython import OpType
from clientMain.ClientMessageHandlerPython import ClientMessageHandler


class ClientInterface:
    def __init__(self, port, serverIP) -> None:
        self.ds = socket.socket(
            family=socket.AF_INET, type=socket.SOCK_DGRAM)
        self.port = port
        self.request_id = 0
        self.serverIP = serverIP
        self.message_handler = ClientMessageHandler()

    def startService(self, icontent, opType):
        request = Request(self.request_id, opType, icontent)
        content = self.message_handler.marshal(request)
        self.sendRequest(self.serverIP, self.port, content)
        response = self.receiveResponse()
        return response

    def monitorUpdates(self):
        pass

    def sendRequest(self):
        retry = 0
        while retry < 3:
            try:
                pass
            except:
                pass

    def receiveResponse(self):
        pass
