'''
This class is used to define the structure of the Request. It contains the id, which is used the Server
to uniquely identify the request from a particular user, the opType which indicates the type of 
service requested by the user and the arguments which are the information required by the Server
in order to perform the service requested.
'''


class Request():
    def __init__(self, id, opType, arguments) -> None:
        self.id = id
        self.opType = opType
        self.arguments = arguments
