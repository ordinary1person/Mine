#绘制平面坐标系
from turtle import *
import math

def my_goto(x,y):      #移动到某点，不留轨迹的函数
    penup()                 
    goto(x,y)
    pendown()

def my_mov(x,y,my_seth,long):   #直线运动函数，移动到原点两个值，方向，长度两个值
    my_goto(x,y)

    seth(my_seth)         #绝对角度
    fd(long)              #移动长度

def rc(function,start,end):                #Rectangular Coordinates直角坐标系简称
    scwide=600           #屏幕宽度
    scheight=600         #屏幕高度
    #bgcolor("black")

    pencolor("green")
    pensize("2")
    Screen().delay(0)

    my_mov(scwide/2,0,180,scwide)           #画横轴及箭头
    my_mov(scwide/2,0,135,scwide/30)
    my_mov(scwide/2,0,225,scwide/30)

    my_mov(0,scheight/2,-90,scheight)       #画纵轴及箭头          
    my_mov(0,scheight/2,225,scwide/30)
    my_mov(0,scheight/2,315,scwide/30)

    my_goto(scwide/2-10,-40)        #写个“X”
    write("X",  font = ("Times", 12,"bold"))

    my_goto(30,scheight/2-30)       #写个“Y”
    write("Y",  font = ("Times", 12,"bold"))
 
    my_goto(-40,-40)                #写个“0”
    write("O",  font = ("Times", 12,"bold"))

    for i in range(-300,300,50):
        if i==0:
            pass
        else:
            my_mov(i,-5,90,10)
            write(i,font = ("Times", 9,"bold"))
            my_mov(-5,i,0,10)
            my_goto(15,i-5)
            write(i,font = ("Times", 9,"bold"))
    
    
    draw_fun(function,start,end)
            
def draw_fun(function,start,end):
    x,y = start,function(start)
    penup()
    pencolor("black")
    goto(x,y)
    pendown()
    for x in range(int(start),int(end)):
        y= function(x)
        goto(x,y)
    penup()

rc(lambda x: x**2-x,-100,100)
hideturtle()
done()