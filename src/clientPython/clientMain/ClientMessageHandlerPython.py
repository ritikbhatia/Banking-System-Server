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
            if isinstance(content, str):
                mcontent += UPCOMING_STRING
                mcontent += bytes(bytearray(struct.pack("!I",
                                  len(bytes(content, 'UTF-16')))))
                mcontent += bytes(content, 'UTF-16')
            elif isinstance(content, int):
                mcontent += UPCOMING_INT
                mcontent += bytes(bytearray(struct.pack("!I", content)))
            elif isinstance(content, float):
                mcontent += UPCOMING_FLOAT
                mcontent += bytes(bytearray(struct.pack("!d", content)))

        mcontent = bytes(bytearray(struct.pack("!I", requestID))) + \
            bytes(bytearray(struct.pack("!I", opType))) + \
            bytes(bytearray(struct.pack("!I", index))) + mcontent
        return bytearray(mcontent)

    def unmarshal(self, packetData) -> Response:
        SIZE_INT = 4
        packetData = packetData[0]
        index = 0
        statusType = struct.unpack('!I', bytes(
            packetData[index:index + SIZE_INT]))
        index += SIZE_INT

        messageLen = struct.unpack('!I', bytes(
            packetData[index:index + SIZE_INT]))
        index += SIZE_INT
        message = packetData[index:index+messageLen[0]]
        message = message.decode('UTF-16')
        return Response(status=Status(statusType[0]), message=message)
