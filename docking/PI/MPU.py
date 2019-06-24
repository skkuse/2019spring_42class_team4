#!/usr/bin/python
import smbus
import math
 
# Register
power_mgmt_1 = 0x6b
power_mgmt_2 = 0x6c
 
def read_byte(reg):
    return bus.read_byte_data(address, reg)
 
def read_word(reg):
    h = bus.read_byte_data(address, reg)
    l = bus.read_byte_data(address, reg+1)
    value = (h << 8) + l
    return value
 
def read_word_2c(reg):
    val = read_word(reg)
    if (val >= 0x8000):
        return -((65535 - val) + 1)
    else:
        return val
 
def dist(a,b):
    return math.sqrt((a*a)+(b*b))
 
def get_y_rotation(x,y,z):
    radians = math.atan2(x, dist(y,z))
    return -math.degrees(radians)
 
def get_x_rotation(x,y,z):
    radians = math.atan2(y, dist(x,z))
    return math.degrees(radians)
def get_x():
    bus = smbus.SMBus(1) # differs from rasppi version
    address = 0x68       # via i2cdetect
    bus.write_byte_data(address, power_mgmt_1, 0)
    return read_word_2c(0x3b)
def get_y():
    bus = smbus.SMBus(1) # differs from rasppi version
    address = 0x68       # via i2cdetect
    bus.write_byte_data(address, power_mgmt_1, 0)
    return read_word_2c(0x3d)

def get_z():
    bus = smbus.SMBus(1) # differs from rasppi version
    address = 0x68       # via i2cdetect
    bus.write_byte_data(address, power_mgmt_1, 0)
    return read_word_2c(0x3f)
 
bus = smbus.SMBus(1) # differs from rasppi version
address = 0x68       # via i2cdetect

#while(True):
    # Aktivieren, um das Modul ansprechen zu koennen
#    bus.write_byte_data(address, power_mgmt_1, 0)
    #print "Gyroskop"
    #print "--------"
 
#    gyroskop_xout = read_word_2c(0x43)
#    gyroskop_yout = read_word_2c(0x45)
#    gyroskop_zout = read_word_2c(0x47)
 
#    print "gyroskop_xout: ", ("%5d" % gyroskop_xout), " scaled: ", (gyroskop_xout / 131)
#    print "gyroskop_yout: ", ("%5d" % gyroskop_yout), " scaled: ", (gyroskop_yout / 131)
#    print "gyroskop_zout: ", ("%5d" % gyroskop_zout), " scaled: ", (gyroskop_zout / 131)
 
#    print
#    print "accelssensor"
#    print "---------------------"
#    accel_xout = read_word_2c(0x3b)
#    accel_yout = read_word_2c(0x3d)
#    accel_zout = read_word_2c(0x3f)
 
#    accel_xout_scaled = accel_xout / 16384.0
#    accel_yout_scaled = accel_yout / 16384.0
#    accel_zout_scaled = accel_zout / 16384.0
 
#    print "accel_xout: ", ("%6d" % accel_xout), " scaled: ", accel_xout_scaled
#    print "accel_yout: ", ("%6d" % accel_yout), " scaled: ", accel_yout_scaled
#    print "accel_zout: ", ("%6d" % accel_zout), " scaled: ", accel_zout_scaled
 

#    print "X Rotation: " , get_x_rotation(accel_xout_scaled, accel_yout_scaled, accel_zout_scaled)
#    print "Y Rotation: " , get_y_rotation(accel_xout_scaled, accel_yout_scaled, accel_zout_scaled)'''