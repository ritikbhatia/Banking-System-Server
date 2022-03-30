import sys
from xml.etree.ElementTree import TreeBuilder

from pyparsing import null_debug_action
import socket
import time
from clientMessage.RequestPython import *
from clientMessage.ResponsePython import *
from clientBank.EnumPython import OpType
from clientMain.ClientMessageHandlerPython import ClientMessageHandler


class ClientInterface:
    def __init__(self, port, serverIP, simulate, clientLossRate) -> None:
        try:
            self.ds = socket.socket(
                family=socket.AF_INET, type=socket.SOCK_DGRAM)
            self.ds.settimeout(10)
            self.port = port
            self.request_id = 0
            self.serverIP = serverIP
            self.simulate = simulate
            self.clientLossRate = clientLossRate
        except:
            pass

    def startService(self, icontent, opType):
        request = Request(self.request_id, opType, icontent)
        clientMessageHandler = ClientMessageHandler()
        content = clientMessageHandler.marshal(request)
        retryAttempts = 2
        for i in range(retryAttempts):
            self.sendRequest(content)
            response = self.receiveResponse(clientMessageHandler)
            if response == None:
                continue

        return response

    def monitorUpdates(self, interval, opType):
        request = Request(self.request_id, opType, interval)
        clientMessageHandler = ClientMessageHandler()
        content = clientMessageHandler.marshal(request)
        timeStart = time.time()
        timeNow = time.time()

        while timeNow-timeStart < interval:
            response = self.receiveResponse()
            print(response)

        print("Monitoring Finished")  # EDIT LATER

    def sendRequest(self, content):
        try:
            serverAddressPort = (self.serverIP, self.port)
            self.ds.sendto(content, serverAddressPort)
        except:
            retry += 1
            print('trying again')  # Remove this line later

    def receiveResponse(self, clientMessageHandler):
        packet = b''
        bufferSize = 1024
        while True:
            try:
                packet = self.ds.recvfrom(bufferSize)
                break
            except:
                continue
        response = Response(ClientMessageHandler.unmarshal(packet))
        return response
