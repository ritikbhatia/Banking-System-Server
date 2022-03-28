from clientMessage.RequestPython import Request
from clientMessage.RequestPython import Response
from clientBank.EnumPython import Status
class ClientMessageHandler:
    

    def marshal(request) -> bytearray:

        UPCOMING_STRING = b'0x01'
        UPCOMING_INT = b'0x02'
        UPCOMING_FLOAT = b'0x03'

        requestID = request.id
        opType = request.opType
        mcontent = b''
        for content in request.arguments:
            if isinstance(content, str):
                mcontent+=UPCOMING_STRING
                mcontent+=len(bytes(content,'UTF-16')).to_bytes(2,'big')
                mcontent+=bytes(content, 'UTF-16')
            elif isinstance(content, int):
                mcontent+=UPCOMING_INT
                mcontent+=content.to_bytes(2, 'big')
            elif isinstance(content, float):
                mcontent+=UPCOMING_FLOAT
                mcontent+=content.to_bytes(2, 'big')
                
        mcontent = requestID.to_bytes(2,'big') + opType.to_bytes(2,'big') + mcontent
        return(mcontent)
    
        

    def unmarshal(packetData) -> Response:
        SIZE_INT = 4
        UPCOMING_STRING = b'0x01'
        UPCOMING_INT = b'0x02'
        UPCOMING_FLOAT = b'0x03'

        index = 0
        statusType = int.from_bytes(packetData[index:index + SIZE_INT], "big")
        index+=SIZE_INT
        messageLen = int.from_bytes(packetData[index:index+SIZE_INT], "big")
        index+=SIZE_INT
        
        message = packetData[index:index+messageLen]
        
        return(Response(Status(statusType)))

    