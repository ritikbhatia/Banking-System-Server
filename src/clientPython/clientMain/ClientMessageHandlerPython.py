from sys import byteorder
from numpy import byte
from clientMessage.RequestPython import Request
from clientMessage.ResponsePython import Response
from clientBank.EnumPython import Status
import struct


class ClientMessageHandler:

    def __init__(self):
        pass

    def float_to_bytes(self, float_num):
        byte_array = bytearray(struct.pack("f", float_num))
        return bytes(byte_array)

    def marshal(self, request) -> bytearray:

        UPCOMING_STRING = b'\x01'
        UPCOMING_INT = b'\x02'
        UPCOMING_FLOAT = b'\x03'

        requestID = request.id
        opType = request.opType
        mcontent = b''
        index = len(request.arguments)
        for content in request.arguments:
            print("This is the content being added")
            print(content)
            if isinstance(content, str):
                content = content.rstrip('\x00')
                mcontent += UPCOMING_STRING
                print("Added String header")
                print(mcontent)
                #mcontent += len(bytes(content, 'UTF-16')).to_bytes(length=4, byteorder='big')
                mcontent += bytes(bytearray(struct.pack("!I",
                                  len(bytes(content, 'UTF-16')))))
                print("Added String length")
                print(mcontent)
                mcontent += bytes(content, 'UTF-16')
                print("Added String")
                print(mcontent)
            elif isinstance(content, int):
                mcontent += UPCOMING_INT
                print(mcontent)
                print("Added INT header")
                # mcontent += content.to_bytes(length=4, byteorder='big')
                mcontent += bytes(bytearray(struct.pack("!I", content)))
                print(mcontent)
                print("Added int")
            elif isinstance(content, float):
                mcontent += UPCOMING_FLOAT
                print(mcontent)
                print("Added float header")
                mcontent += bytes(bytearray(struct.pack("!f", content)))
                print(mcontent)
                print("Added float")

        # mcontent = requestID.to_bytes(length=4, byteorder='big') + opType.to_bytes(length=4, byteorder = 'big') + mcontent
        mcontent = bytes(bytearray(struct.pack("!I", requestID))) + \
            bytes(bytearray(struct.pack("!I", opType))) + \
            bytes(bytearray(struct.pack("!I", index))) + mcontent
        print("Added Headers")
        print(mcontent)
        return bytearray(mcontent)

    def unmarshal(self, packetData) -> Response:
        SIZE_INT = 4
        UPCOMING_STRING = b'0x01'
        UPCOMING_INT = b'0x02'
        UPCOMING_FLOAT = b'0x03'

        index = 0
        statusType = int.from_bytes(packetData[index:index + SIZE_INT], "big")
        index += SIZE_INT
        messageLen = int.from_bytes(packetData[index:index+SIZE_INT], "big")
        index += SIZE_INT

        message = packetData[index:index+messageLen]

        return(Response(Status(statusType)))
