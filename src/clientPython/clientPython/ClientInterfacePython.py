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
    
    def startService(self, icontent, opType):
        request = Request(self.request_id, opType, icontent)
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
