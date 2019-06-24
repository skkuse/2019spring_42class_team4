#!/usr/bin/python
import MPU
import gps
from bluetooth import *

server_socket=BluetoothSocket(RFCOMM)
port=1
server_socket.bind(("",PORT_ANY))
server_socket.listen(1)
 
port = server_socket.getsockname()[1]
#advertise_service( server_socket, "BtChat",service_id = uuid,service_classes = [ uuid, SERIAL_PORT_CLASS ],profiles = [ SERIAL_PORT_PROFILE ] )
print("Waiting for connection : channel %d" % port)
client_sock, client_info = server_socket.accept()
print('accepted')
infoarray = []
while (True):
    infoarray [0]=MPU.get_x()
    infoarray [1]=MPU.get_y()
    infoarray [2]=MPU.get_z()
    infoarray [3]=gps.get_lat()
    infoarray [4]=gps.get_lon()

    client_sock.send(infoarray)

    

