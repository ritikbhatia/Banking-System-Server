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
            self.ds.settimeout(5)
            self.port = port
            self.requestID = 0
            self.serverIP = serverIP
            self.simulate = simulate
            self.clientLossRate = clientLossRate
        except:
            pass

    def startService(self, icontent, opType):
        request = Request(self.requestID, opType, icontent)
        clientMessageHandler = ClientMessageHandler()
        content = clientMessageHandler.marshal(request)
        retryAttempts = 2
        for i in range(retryAttempts):
            self.sendRequest(content)
            response = self.receiveResponse(clientMessageHandler)
            if response == None:
                continue
            break
        self.requestID += 1
        return response

    def monitorUpdates(self, interval, opType):
        request = Request(self.requestID, opType, interval)
        clientMessageHandler = ClientMessageHandler()
        content = clientMessageHandler.marshal(request)
        self.sendRequest(content)
        timeStart = time.time()
        timeout = self.ds.gettimeout()
        self.ds.settimeout(interval[0])

        while time.time()-timeStart < float(interval[0]):
            response = self.receiveResponse(clientMessageHandler)
            if response != None:
                print(response)
        self.requestID += 1
        print("Monitoring Finished")

    def sendRequest(self, content):
        try:
            serverAddressPort = (self.serverIP, self.port)
            self.ds.sendto(content, serverAddressPort)
        except:
            retry += 1
            print('trying again')  # Remove this line later

    def receiveResponse(self, clientMessageHandler):
        packet = b''
        bufferSize = 10240
        while True:
            try:
                packet = self.ds.recvfrom(bufferSize)
                break
            except socket.timeout:
                return(None)
            except:
                continue
        response = ClientMessageHandler.unmarshal(self, packetData=packet)
        return response
