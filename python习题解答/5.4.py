#将极坐标转换为平面坐标
import math
def polar_to_cartesian(r, theta): 
    x = r * math.cos(theta)
    y=  r * math.sin(theta)
    return x,y

print('平面坐标为：',polar_to_cartesian(1, math.pi/4))