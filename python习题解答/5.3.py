#绘制函数图像
from time import sleep
import turtle
import math

def draw_fun(function,start,end):
    x,y = start,function(start)
    turtle.penup()
    turtle.goto(x,y)
    turtle.pendown()
    for x in range(int(start),int(end)):
        y= function(x)
        turtle.goto(x,y)
    turtle.penup()
    
draw_fun(lambda x: math.cos(x)+1/3*math.sin(3*x),-math.pi*100,math.pi*100)
turtle.done()